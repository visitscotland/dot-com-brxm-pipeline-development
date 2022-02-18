package com.visitscotland.brxm.translation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.visitscotland.brxm.translation.plugin.JcrDocument;
import com.visitscotland.brxm.translation.plugin.JcrDocumentFactory;
import com.visitscotland.brxm.translation.plugin.TranslationWorkflow;
import org.hippoecm.repository.HippoStdNodeType;
import org.hippoecm.repository.api.Workflow;
import org.hippoecm.repository.api.WorkflowException;
import org.hippoecm.repository.standardworkflow.EditableWorkflow;
import org.hippoecm.repository.translation.HippoTranslationNodeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.*;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

@Component
@Scope(SCOPE_PROTOTYPE)
public class TranslationService {
    private static final Logger log = LoggerFactory.getLogger(TranslationService.class);
    private JcrDocumentFactory jcrDocumentFactory;
    private ObjectMapper objectMapper;
    private SessionFactory sessionFactory;

    @Autowired
    public TranslationService(JcrDocumentFactory jcrDocumentFactory,
                              ObjectMapper objectMapper,
                              SessionFactory sessionFactory) {
        this.jcrDocumentFactory = jcrDocumentFactory;
        this.objectMapper = objectMapper;
        this.sessionFactory = sessionFactory;
    }

    public JcrDocument getDocument(String nodeId) throws RepositoryException {
        Node jcrNode = sessionFactory.getJcrSession().getNodeByIdentifier(nodeId);
        return jcrDocumentFactory.createFromNode(jcrNode);
    }

    public boolean hasPendingTranslations(JcrDocument document) throws RepositoryException {
        Set<JcrDocument> translations = document.getTranslations();
        for (JcrDocument translation : translations) {
            Node unpublishedNode = translation.getVariantNode(JcrDocument.VARIANT_UNPUBLISHED);
            if ( unpublishedNode.hasProperty(JcrDocument.VS_TRANSLATION_FLAG) &&
                    unpublishedNode.getProperty(JcrDocument.VS_TRANSLATION_FLAG).getBoolean()) {
                return true;
            }
        }
        return false;
    }

    public boolean hasChangePending(JcrDocument jcrDocument) throws RepositoryException {
        try {
            Node unpublishedVariant = jcrDocument.getVariantNode(JcrDocument.VARIANT_UNPUBLISHED);
            if (unpublishedVariant.hasProperty(HippoStdNodeType.HIPPOSTD_STATESUMMARY) &&
                    ("changed".equals(unpublishedVariant.getProperty(HippoStdNodeType.HIPPOSTD_STATESUMMARY).getString()) ||
                            "new".equals(unpublishedVariant.getProperty(HippoStdNodeType.HIPPOSTD_STATESUMMARY).getString()))) {
                return true;
            }
        } catch(RepositoryException ex) {
            // Just consume the exception
            log.warn("Failed to lookup unpublished status", ex);
        }
        return false;
    }

    public boolean getTranslationFlag(JcrDocument document) throws RepositoryException {
        Node unpublishedNode = document.getVariantNode(JcrDocument.VARIANT_UNPUBLISHED);
        if ( unpublishedNode.hasProperty(JcrDocument.VS_TRANSLATION_FLAG) ) {
            return unpublishedNode.getProperty(JcrDocument.VS_TRANSLATION_FLAG).getBoolean();
        }
        return false;
    }

    // Returns a JSON representation of TranslationContent
    public String getDocumentDifference(String nodeId) throws RepositoryException, IOException {
        JcrDocument document = getDocument(nodeId);
        Node unpublishedNode = document.getVariantNode(JcrDocument.VARIANT_UNPUBLISHED);
        if ( unpublishedNode.hasProperty(JcrDocument.VS_TRANSLATION_DIFF) ) {
            return unpublishedNode.getProperty(JcrDocument.VS_TRANSLATION_DIFF).getString();
        }
        return null;
    }

    public void clearTranslationFlag(JcrDocument jcrDocument)
            throws RepositoryException, WorkflowException, RemoteException {
        if (jcrDocument.isDraftBeingEdited()) {
            throw new WorkflowException("Document being edited");
        }

        Node unpublishedNode = jcrDocument.getVariantNode(JcrDocument.VARIANT_UNPUBLISHED);
        Property localeProperty = unpublishedNode.getProperty(HippoTranslationNodeType.LOCALE);
        if (Locale.ENGLISH.getLanguage().equals(localeProperty.getString())) {
            throw new IllegalArgumentException("Unable to clear translation flag for English document");
        }

        Workflow workflow = sessionFactory.getUserSession().getWorkflowManager().getWorkflow("translation", unpublishedNode);
        if (workflow instanceof TranslationWorkflow) {
            TranslationWorkflow translationWorkflow = (TranslationWorkflow) workflow;
            translationWorkflow.clearTranslationFlag();
        } else {
            throw new IllegalStateException("Unable to get EditableWorkflow");
        }
    }

    public List<JcrDocument> setTranslationContent(JcrDocument jcrDocument, TranslationContent translationContent) throws WorkflowException, RepositoryException, RemoteException {
        Session jcrSession = sessionFactory.getJcrSession();

        if (jcrDocument.isNodeType(JcrDocument.VS_TRANSLATABLE_TYPE)) {
            // The Hippo CMS uses the handle of the document variants to perform checkout, commit and discard
            // operations. But the lockEditableNode returns the unpublished Node so we need to keep hold of the
            // handle so we can perform the commit, or discard operations.

            // Can only use an English document to lookup the translations
            if (!Locale.ENGLISH.getLanguage().equals(jcrDocument.getLocaleName())) {
                throw new IllegalArgumentException("Must use an English document to set the translations");
            }

            // HashMap<Handle, Editable>
            HashMap<Node, Node> editableNodes = new HashMap<>();
            List<JcrDocument> nodesBeingEdited = new ArrayList<>();

            // Need to check if the root (English) document is being edited by another user. Don't want to send for
            // translation if it is
            if (jcrDocument.isDraftBeingEdited()) {
                nodesBeingEdited.add(jcrDocument);
            }

            Set<JcrDocument> jcrTranslations = jcrDocument.getTranslations();

            if (jcrTranslations.size() == 0) {
                throw new IllegalStateException("Document has no foreign translations");
            }

            for (JcrDocument translatedDocument : jcrTranslations) {
                if (translatedDocument.isDraftBeingEdited()) {
                    nodesBeingEdited.add(translatedDocument);
                    log.debug("Document already checked out for edit, unable to send for translation");
                    continue;
                }
                try {
                    Node handle = translatedDocument.getHandle();
                    // The editable node returned is the draft variant of the document. If we apply changes to the
                    // draft, and then commit changes to the node it also flags the document as changed.
                    // We do not want that, we want the editor to choose if the document has changed.
                    // Getting the editable node still ensures that nobody else is editing the document, but if the
                    // changes are applied to the unpublished variant and the draft is discarded it should have the
                    // desired result, changes applied without flagging the document as changed.
                    lockEditableNode(handle);

                    Node unpublishedNode = translatedDocument.getVariantNode(JcrDocument.VARIANT_UNPUBLISHED);
                    editableNodes.put(handle, unpublishedNode);
                } catch (WorkflowException ex) {
                    // If we get a workflow exception we have not been able to check the document out
                    // add the Node to a list of failed documents
                    log.debug("Document already checked out for edit, unable to send for translation", ex);
                    nodesBeingEdited.add(translatedDocument);
                }
            }

            if (nodesBeingEdited.isEmpty()) {
                if (!editableNodes.isEmpty()) {
                    for (HashMap.Entry<Node, Node> editableNodeEntry : editableNodes.entrySet()) {
                        try {
                            Node editableUnpublishedVariant = editableNodeEntry.getValue();
                            Workflow workflow = sessionFactory.getUserSession().getWorkflowManager().getWorkflow("translation", editableUnpublishedVariant);
                            if (workflow instanceof TranslationWorkflow) {
                                TranslationWorkflow translationWorkflow = (TranslationWorkflow) workflow;
                                translationWorkflow.setTranslationFlag(true);
                                translationWorkflow.setTranslationDiff(objectMapper.writeValueAsString(translationContent));
                            } else {
                                throw new IllegalStateException("Unable to get TranslationWorkflow");
                            }
                        } catch(JsonProcessingException ex) {
                            // just log the error, should never happen.
                            log.error("Unable to serailise the translation.", ex);
                        }
                        discardEditableNode(editableNodeEntry.getKey());
                    }
                    jcrSession.save();
                }
            } else {
                for (Node handle : editableNodes.keySet()) {
                    discardEditableNode(handle);
                }
            }
            jcrSession.refresh(false);
            return nodesBeingEdited;
        }
        return Collections.emptyList();
    }

    protected void lockEditableNode(Node handle) throws RemoteException, WorkflowException, RepositoryException {
        final Workflow editing = sessionFactory.getUserSession().getWorkflowManager().getWorkflow("editing", handle);
        if (editing instanceof EditableWorkflow) {
            EditableWorkflow editableWorkflow = (EditableWorkflow) editing;
            editableWorkflow.obtainEditableInstance();
        } else {
            throw new WorkflowException("Unable to obtain an EditableWorkflow to perform translation");
        }
    }

    protected void discardEditableNode(Node handle) throws RemoteException, WorkflowException, RepositoryException {
        final Workflow editing = sessionFactory.getUserSession().getWorkflowManager().getWorkflow("editing", handle);
        if (editing instanceof EditableWorkflow) {
            EditableWorkflow editableWorkflow = (EditableWorkflow) editing;
            editableWorkflow.disposeEditableInstance();
        } else {
            throw new WorkflowException("Unable to obtain an EditableWorkflow to discard checkout");
        }
    }

    public static final class TranslationContent {
        private String content;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TranslationContent that = (TranslationContent) o;
            return Objects.equals(content, that.content);
        }

        @Override
        public int hashCode() {
            return Objects.hash(content);
        }
    }
}
