package com.visitscotland.brxm.translation.plugin;

import com.visitscotland.brxm.report.translation.TranslationPriority;
import org.hippoecm.hst.content.beans.ObjectBeanManagerException;
import org.hippoecm.repository.HippoStdNodeType;
import org.hippoecm.repository.HippoStdPubWfNodeType;
import org.hippoecm.repository.api.*;
import org.hippoecm.repository.ext.InternalWorkflow;
import org.hippoecm.repository.standardworkflow.CopyWorkflow;
import org.hippoecm.repository.standardworkflow.FolderWorkflow;
import org.hippoecm.repository.translation.HippoTranslatedNode;
import org.hippoecm.repository.translation.HippoTranslationNodeType;
import org.hippoecm.repository.util.JcrUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.*;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.*;

public class TranslationWorkflowImpl implements TranslationWorkflow, InternalWorkflow {
    public static final String CAFEBABE = "cafebabe-";
    private static final Logger log = LoggerFactory.getLogger(TranslationWorkflowImpl.class);
    private final Session userSession;
    private final Session rootSession;
    private final WorkflowContext workflowContext;
    private final Node rootSubject;
    private final Node userSubject;
    private DocumentFactory documentFactory;

    public TranslationWorkflowImpl(WorkflowContext context,
                                   Session userSession,
                                   Session rootSession,
                                   Node subject) throws RepositoryException {
        this(context,
                userSession,
                rootSession,
                subject,
                new DocumentFactory());
    }

    protected TranslationWorkflowImpl(WorkflowContext context,
                                      Session userSession,
                                      Session rootSession,
                                      Node subject,
                                      DocumentFactory documentFactory) throws RepositoryException {
        this.workflowContext = context;
        this.rootSession = rootSession;
        this.userSession = userSession;
        this.userSubject = userSession.getNodeByIdentifier(subject.getIdentifier());
        this.rootSubject = rootSession.getNodeByIdentifier(subject.getIdentifier());
        this.documentFactory = documentFactory;

        if (!userSubject.isNodeType(HippoTranslationNodeType.NT_TRANSLATED)) {
            throw new RepositoryException("Node is not of type " + HippoTranslationNodeType.NT_TRANSLATED);
        }
    }

    @Override
    public Document addTranslation(String language, String newDocumentName) throws WorkflowException, RepositoryException, RemoteException, ObjectBeanManagerException {
        return addTranslation(language, newDocumentName, userSubject);
    }

    @Override
    public Document addTranslation(final String language, final String newDocumentName, final Node sourceNode) throws WorkflowException,
            RepositoryException, RemoteException, ObjectBeanManagerException {
        Node userSourceSubject = userSession.getNodeByIdentifier(sourceNode.getIdentifier());

        Node rootSource = rootSession.getNodeByIdentifier(sourceNode.getIdentifier());
        final HippoTranslatedNode originNode = new HippoTranslatedNode(rootSource);
        final Node originFolder = originNode.getContainingFolder();
        if (originFolder == null || !originFolder.isNodeType(HippoTranslationNodeType.NT_TRANSLATED)) {
            throw new WorkflowException("No translated ancestor folder found");
        }

        final HippoTranslatedNode originTranslatedFolderNode = new HippoTranslatedNode(originFolder);
        final Node targetFolderNode = originTranslatedFolderNode.getTranslation(language);

        Node copiedNode;
        if (userSourceSubject.getParent().isNodeType(HippoNodeType.NT_HANDLE)) {
            copiedNode = addTranslatedDocument(language, newDocumentName, userSourceSubject, targetFolderNode);
        } else {
            copiedNode = addTranslatedFolder(language, newDocumentName, targetFolderNode);
        }


        return new Document(copiedNode);
    }

    @Override
    public void saveSession() throws RepositoryException {
        rootSession.save();
        rootSession.refresh(false);
    }

    protected Node addTranslatedDocument(final String language, final String newDocumentName, final Node sourceDocumentNode, final Node targetFolderNode)
            throws WorkflowException, RepositoryException, RemoteException, ObjectBeanManagerException {

        Node copyRootSubject = rootSession.getNodeByIdentifier(sourceDocumentNode.getIdentifier());

        try {
            getOriginsCopyWorkflow(copyRootSubject).copy(documentFactory.createFromNode(targetFolderNode), newDocumentName);
        } catch (WorkflowException ex) {
            // Typically due to the document already existing. If not, then a WorkflowException is re-thrown later on
            log.warn("Failed to copy document {}/{}", targetFolderNode.getPath(), newDocumentName);
        }

        Node newDocumentHandle = null;

        final NodeIterator siblings = targetFolderNode.getNodes(newDocumentName);
        while (siblings.hasNext()) {
            final Node sibling = siblings.nextNode();
            if (sibling.isNodeType(HippoNodeType.NT_HANDLE)) {
                newDocumentHandle = sibling;
            }
        }
        if (newDocumentHandle == null) {
            throw new WorkflowException("Could not locate handle for document after copying");
        }

        // Iterate over the child Nodes in the document looking for Translatable children
        final NodeIterator copiedVariants = newDocumentHandle.getNodes(newDocumentHandle.getName());
        // Now that the Node has been copied to the language channel update all the properties that can be translated
        // Iterate over all the variants that exist for the document, looks like it should only have an unpublished
        // variant when first copied.
        while (copiedVariants.hasNext()) {
            // Ensure the document is checked out so we can make changes
            final Node copiedVariant = copiedVariants.nextNode();
            JcrUtils.ensureIsCheckedOut(copiedVariant);
            // Update the locale that this copy is for,
            // will currently have the value of the node that was copied (English)
            copiedVariant.setProperty(HippoTranslationNodeType.LOCALE, language);
            // Translation created date must be set to the time the document was cloned
            copiedVariant.setProperty(HippoStdPubWfNodeType.HIPPOSTDPUBWF_CREATION_DATE, Calendar.getInstance());

            // If the document has Translatable children (hippo:mirror) we need to attempt to change
            // the links to point to the relevant translated child
            translateHippoMirrors(copiedVariant, language);

            // When document is cloned, immediately send it for translation
            if (copiedVariant.getProperty(JcrDocument.HIPPOSTD_STATE).getString().equals(JcrDocument.VARIANT_UNPUBLISHED)) {
                copiedVariant.setProperty(JcrDocument.VS_TRANSLATION_FLAG, true);
                copiedVariant.setProperty(JcrDocument.VS_TRANSLATION_DIFF, "");
            }
        }

        return newDocumentHandle;
    }

    protected void translateHippoMirrors(Node parentNode, String language) throws RepositoryException {
        NodeIterator childIterator = parentNode.getNodes();
        while (childIterator.hasNext()) {
            Node childNode = childIterator.nextNode();
            if (childNode.isNodeType("hippo:mirror")) {
                if (!childNode.hasProperty("hippo:docbase")) {
                    log.warn("Unable to find linking node UUID");
                    continue;
                }
                // Now we have the UUID of the node we are linking to, get the Node and see if there is a
                // translation for the current language.
                // If the linkUUID does not exist or points to the root Node then skip it.
                String linkUUID = childNode.getProperty("hippo:docbase").getString();
                if (linkUUID == null || linkUUID.equals("") || linkUUID.startsWith(CAFEBABE)) {
                    log.warn("Link contains an empty Node");
                    continue;
                }
                Node linkedNode = rootSession.getNodeByIdentifier(linkUUID);
                JcrDocument linkedJcrDocument = new JcrDocument(linkedNode);
                if (linkedJcrDocument.hasTranslation(language)) {
                    // Translated link is the unpublished variant. Must link to parent hippo:handle instead
                    Node unpublishedTranslatedVariant = linkedJcrDocument.getTranslation(language);
                    Node translatedHandle = unpublishedTranslatedVariant.getParent();
                    if (translatedHandle.isNodeType(HippoNodeType.NT_HANDLE)) {
                        childNode.setProperty("hippo:docbase", translatedHandle.getIdentifier());
                    } else {
                        log.warn("Can not translate node link - variant " + unpublishedTranslatedVariant.getIdentifier()
                                + " has parent " + translatedHandle.getPrimaryNodeType().toString() + "that is not a hippo handle");
                    }
                } else {
                    log.warn("Missing link translation node");
                }
            } else {
                translateHippoMirrors(childNode, language);
            }
        }
    }

    private CopyWorkflow getOriginsCopyWorkflow(Node copyRootSubject) throws RepositoryException, WorkflowException {
        // first check if a copy workflow is configured on the handle itself
        Workflow workflow = workflowContext.getWorkflow("translation-copy", documentFactory.createFromNode(copyRootSubject.getParent()));

        if (workflow == null) {
            // No? Fallback to a copy workflow on the subject itself
            workflow = workflowContext.getWorkflow("translation-copy", documentFactory.createFromNode(copyRootSubject));
        }

        if (workflow instanceof CopyWorkflow) {
            return (CopyWorkflow) workflow;
        } else {
            throw new WorkflowException("No copy workflow defined; cannot copy document");
        }
    }

    private Node addTranslatedFolder(final String language, final String newDocumentName, final Node targetFolderNode)
            throws WorkflowException, RemoteException, RepositoryException {

        final FolderWorkflow workflowOfTargetFolder = getFolderWorkflow(targetFolderNode);
        final Map<String, Set<String>> prototypes = getPrototypes(workflowOfTargetFolder);

        // find best matching category and type from prototypes
        final String primaryType = userSubject.getPrimaryNodeType().getName();
        String category = null;
        String type = null;

        for (Map.Entry<String, Set<String>> candidate : prototypes.entrySet()) {
            final String categoryName = candidate.getKey();
            final Set<String> types = candidate.getValue();
            if (types.contains(primaryType)) {
                category = categoryName;
                type = primaryType;
                break;
            }
            if (category == null) {
                category = categoryName;
            }
            if (type == null && types.size() > 0) {
                type = types.iterator().next();
            }
        }

        if (category == null) {
            throw new WorkflowException("No category found to use for adding translation to target folder");
        }
        if (type == null) {
            throw new WorkflowException("No type found to use for adding translation to target folder");
        }

        final String newFolderPath = workflowOfTargetFolder.add(category, type, newDocumentName);
        Node copiedNode = rootSession.getNode(newFolderPath);

        JcrUtils.ensureIsCheckedOut(copiedNode);
        if (!copiedNode.isNodeType(HippoTranslationNodeType.NT_TRANSLATED)) {
            copiedNode.addMixin(HippoTranslationNodeType.NT_TRANSLATED);
        }
        copiedNode.setProperty(HippoTranslationNodeType.ID,
                userSubject.getProperty(HippoTranslationNodeType.ID).getString());
        copiedNode.setProperty(HippoTranslationNodeType.LOCALE, language);
        rootSession.save();

        copyFolderTypes(copiedNode, prototypes);

        return copiedNode;
    }

    private Map<String, Set<String>> getPrototypes(final FolderWorkflow folderWorkflow) throws WorkflowException,
            RemoteException, RepositoryException {

        final Map<String, Set<String>> prototypes = (Map<String, Set<String>>) folderWorkflow.hints().get("prototypes");
        if (prototypes == null) {
            throw new WorkflowException("No prototype hints available in workflow of target folder.");
        }
        return prototypes;
    }

    private FolderWorkflow getFolderWorkflow(final Node targetFolderNode) throws WorkflowException,
            RepositoryException {

        Workflow workflow = workflowContext.getWorkflow("internal", documentFactory.createFromNode(targetFolderNode));
        if (!(workflow instanceof FolderWorkflow)) {
            throw new WorkflowException("Target folder does not have a folder workflow in the category 'internal'.");
        }
        return (FolderWorkflow) workflow;
    }

    public void addTranslation(final String language, final Document document) throws WorkflowException,
            RepositoryException {
        HippoTranslatedNode translatedNode = new HippoTranslatedNode(rootSubject);
        if (translatedNode.hasTranslation(language)) {
            throw new WorkflowException("Language already exists");
        }

        Node copiedDocNode = document.getNode(rootSession);
        JcrUtils.ensureIsCheckedOut(copiedDocNode);
        if (!copiedDocNode.isNodeType(HippoTranslationNodeType.NT_TRANSLATED)) {
            copiedDocNode.addMixin(HippoTranslationNodeType.NT_TRANSLATED);
        }
        copiedDocNode.setProperty(HippoTranslationNodeType.LOCALE, language);
        copiedDocNode.setProperty(HippoTranslationNodeType.ID, userSubject.getProperty(HippoTranslationNodeType.ID)
                .getString());

        rootSession.save();
        rootSession.refresh(false);
    }

    @Override
    public void setTranslationPriority(TranslationPriority priority) throws RepositoryException, RemoteException {
        rootSubject.setProperty(JcrDocument.VS_TRANSLATION_PRIORITY, priority.toString());
        saveSession();
    }

    @Override
    public void clearTranslationFlag() throws RepositoryException, RemoteException {
        rootSubject.setProperty(JcrDocument.VS_TRANSLATION_FLAG, false);
        if (rootSubject.hasProperty(JcrDocument.VS_TRANSLATION_DIFF)) {
            Property diffProperty = rootSubject.getProperty(JcrDocument.VS_TRANSLATION_DIFF);
            diffProperty.remove();
        }
        saveSession();
    }

    @Override
    public void setTranslationDeadline(Calendar deadline) throws RepositoryException, RemoteException {
        rootSubject.setProperty(JcrDocument.VS_TRANSLATION_DEADLINE, deadline);
        saveSession();
    }

    @Override
    public void setTranslationFlag(boolean flag) throws RepositoryException, RemoteException {
        rootSubject.setProperty(JcrDocument.VS_TRANSLATION_FLAG, flag);
        saveSession();
    }

    @Override
    public void setTranslationDiff(String diff) throws RepositoryException, RemoteException{
        rootSubject.setProperty(JcrDocument.VS_TRANSLATION_DIFF, diff);
        saveSession();
    }

    public Map<String, Serializable> hints() throws WorkflowException, RepositoryException {
        Map<String, Serializable> hints = new TreeMap<>();

        if (userSubject.isNodeType(HippoStdNodeType.NT_PUBLISHABLE)) {
            String state = userSubject.getProperty(HippoStdNodeType.HIPPOSTD_STATE).getString();
            if (HippoStdNodeType.DRAFT.equals(state)) {
                hints.put("addTranslation", Boolean.FALSE);
            } else {
                NodeIterator siblings = userSubject.getParent().getNodes(userSubject.getName());
                Node unpublished = null;
                Node published = null;
                while (siblings.hasNext()) {
                    Node sibling = siblings.nextNode();
                    if (sibling.isNodeType(HippoStdNodeType.NT_PUBLISHABLE)) {
                        String siblingState = sibling.getProperty(HippoStdNodeType.HIPPOSTD_STATE).getString();
                        if (HippoStdNodeType.UNPUBLISHED.equals(siblingState)) {
                            unpublished = sibling;
                        } else if (HippoStdNodeType.PUBLISHED.equals(siblingState)) {
                            published = sibling;
                        }
                    }
                }
                if (unpublished != null && published != null) {
                    if (HippoStdNodeType.PUBLISHED.equals(state)) {
                        hints.put("addTranslation", Boolean.FALSE);
                    }
                }
            }
        }

        final HippoTranslatedNode translatedNode = new HippoTranslatedNode(userSubject);
        Set<String> translations;
        try {
            translations = translatedNode.getTranslations();
        } catch (RepositoryException ex) {
            throw new WorkflowException("Exception during searching for available translations", ex);
        }

        hints.put("locales", (Serializable) translations);

        Set<String> available = new TreeSet<>();
        // for all the available translations we pick the highest ancestor of userSubject of type
        // HippoTranslationNodeType.NT_TRANSLATED, and take all the translations for that node
        Node highestTranslatedNode = translatedNode.getFarthestTranslatedAncestor();
        if (highestTranslatedNode != null) {
            try {
                available = new HippoTranslatedNode(highestTranslatedNode).getTranslations();
            } catch (RepositoryException ex) {
                throw new WorkflowException("Exception during searching for available translations", ex);
            }
        }

        hints.put("setTranslationPriority", Boolean.FALSE);
        hints.put("available", (Serializable) available);
        hints.put("locale", translatedNode.getLocale());
        return hints;
    }

    private void copyFolderTypes(final Node copiedNode, final Map<String, Set<String>> prototypes)
            throws RepositoryException {

        try {
            // check if we have all subject folder types
            final FolderWorkflow folderWorkflow = getFolderWorkflow(rootSubject);
            final Map<String, Set<String>> copyPrototypes = getPrototypes(folderWorkflow);

            // check if equal
            if (copyPrototypes == null || copyPrototypes.size() == 0) {
                return;
            }

            final Set<String> protoKeys = prototypes.keySet();
            final Set<String> copyKeys = copyPrototypes.keySet();
            // check if we have a difference and overwrite
            if (copyKeys.size() != protoKeys.size() || !copyKeys.containsAll(protoKeys)) {
                final String[] newValues = copyKeys.toArray(new String[0]);
                copiedNode.setProperty(HippoStdNodeType.HIPPOSTD_FOLDERTYPE, newValues);
            }
        } catch (WorkflowException e) {
            log.warn(e.getClass().getName() + ": " + e.getMessage(), e);
        } catch (RemoteException e) {
            log.error(e.getClass().getName() + ": " + e.getMessage(), e);
        }
    }

    public static class DocumentFactory {
        public Document createFromNode(Node node) throws RepositoryException {
            return new Document(node);
        }
    }

}
