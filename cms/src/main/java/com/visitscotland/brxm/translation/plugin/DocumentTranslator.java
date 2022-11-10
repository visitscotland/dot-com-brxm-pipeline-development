package com.visitscotland.brxm.translation.plugin;

import com.visitscotland.brxm.config.VsComponentManager;
import com.visitscotland.brxm.hippobeans.capabilities.TranslationParent;
import com.visitscotland.brxm.translation.SessionFactory;
import com.visitscotland.brxm.utils.ContentLogger;
import org.hippoecm.frontend.translation.ILocaleProvider;
import org.hippoecm.hst.content.beans.ObjectBeanManagerException;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.repository.HippoStdNodeType;
import org.hippoecm.repository.api.*;
import org.hippoecm.repository.standardworkflow.DefaultWorkflow;
import org.hippoecm.repository.translation.HippoTranslatedNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class DocumentTranslator {
    public static final String COULD_NOT_CREATE_FOLDERS = "could-not-create-folders";
    private static final Logger logger = LoggerFactory.getLogger(DocumentTranslator.class);
    private HippoTranslatedNodeFactory hippoTranslatedNodeFactory;
    private SessionFactory sessionFactory;
    private JcrDocumentFactory jcrDocumentFactory;
    private ChangeSetFactory changeSetFactory;

    private Logger getContentLogger(){
        return VsComponentManager.get(ContentLogger.class);
    }

    public DocumentTranslator() {
        this(new HippoTranslatedNodeFactory(),
                new SessionFactory(),
                new JcrDocumentFactory(),
                new ChangeSetFactory());
    }

    protected DocumentTranslator(HippoTranslatedNodeFactory hippoTranslatedNodeFactory,
                                 SessionFactory sessionFactory,
                                 JcrDocumentFactory jcrDocumentFactory,
                                 ChangeSetFactory changeSetFactory) {
        this.hippoTranslatedNodeFactory = hippoTranslatedNodeFactory;
        this.sessionFactory = sessionFactory;
        this.jcrDocumentFactory = jcrDocumentFactory;
        this.changeSetFactory = changeSetFactory;
    }

    /**
     * Build a change set for each language, listing the folders and documents that are not already translated.
     * If the document is a TranslationParent then add untranslated siblings also.
     * The document, and it's siblings, if they implement TranslationLinkContainer will have their translatable
     * links checked for translations and a ChangeSet added if they are missing.
     * <p>
     * Will check every document that implements TranslationLinkContainer for links in the document that need to
     * be translated.
     *
     * @param sourceDocument
     * @param targetLocaleList
     * @return
     */
    public List<ChangeSet> buildChangeSetList(Node sourceDocument, List<ILocaleProvider.HippoLocale> targetLocaleList) throws RepositoryException, ObjectBeanManagerException, TranslationException {
        List<ChangeSet> changeSetList = new ArrayList<>();
        for (ILocaleProvider.HippoLocale targetLocale : targetLocaleList) {
            ChangeSet change = changeSetFactory.createChangeSet(targetLocale);
            JcrDocument document = jcrDocumentFactory.createFromNode(sourceDocument);
            change.populateFolders(document);
            if (!document.hasTranslation(targetLocale)) {
                change.addDocument(document, false);
            }
            HippoBean bean = document.asHippoBean();

            if (document.isNodeType("visitscotland:Page")) {
                // Convert the node to a HippoBean so we can check the type
                if (bean instanceof TranslationParent) {
                    TranslationParent parent = (TranslationParent) bean;
                    String[] childJcrTypes = parent.getChildJcrTypes();
                    Node containingFolder = document.getContainingFolder();
                    // Getting the nodes that are in the folder will give us all the folders and hippo:handle
                    // nodes in the folder. We want to convert these into the unpublished versions of the nodes.
                    NodeIterator handleIterator = containingFolder.getNodes();
                    while (handleIterator.hasNext()) {
                        HippoNode siblingNode = (HippoNode) handleIterator.nextNode();
                        // We want to make sure we are not cloning folders and other types.
                        if (!siblingNode.isNodeType("hippostd:folder") &&
                                (siblingNode.isNodeType(JcrDocument.HIPPO_HANDLE) ||
                                        siblingNode.isNodeType(JcrDocument.HIPPO_TRANSLATED))) {
                            JcrDocument siblingDocument = jcrDocumentFactory.createFromNode(siblingNode);
                            if (siblingDocument.isNodeType(childJcrTypes)) {
                                if (!siblingDocument.hasTranslation(targetLocale)) {
                                    change.addDocument(siblingDocument, false);
                                }
                                // Also need to check siblings for Translation links inside the document
                                HippoBean siblingBean = siblingDocument.asHippoBean();
                                addTranslationLinkChangeSets(siblingBean, targetLocale, changeSetList);
                            }
                        }
                    }
                }
            }

            // This document might be a TranslationLinkContainer,
            // check to see if we need to handle links inside the document
            addTranslationLinkChangeSets(bean, targetLocale, changeSetList);

            if (!change.getDocuments().isEmpty()) {
                changeSetList.add(change);
            }
        }
        return changeSetList;
    }

    protected void addTranslationLinkChangeSets(HippoBean sourceDocument, ILocaleProvider.HippoLocale targetLocale, List<ChangeSet> changeSetList)
            throws RepositoryException, ObjectBeanManagerException, TranslationException {
        // Get the translatable links from the container,
        // and then check each one to see if it has been translated
        List<Node> translatableChildNodes = getChildTranslatableLinkNodes(sourceDocument.getNode());
        for (Node link : translatableChildNodes) {
            Node linkedNode = sessionFactory.getJcrSession().getNodeByIdentifier(link.getProperty("hippo:docbase").getString());
            JcrDocument linkDocument = jcrDocumentFactory.createFromNode(linkedNode);
            if (linkDocument.isDeleted()) {
                // Links can reference deleted documents - these should be fixed immediately
                getContentLogger().error("Document {} contains reference to deleted item {}", sourceDocument.getPath(), linkDocument.getHandle().getPath());
                throw new TranslationException(String.format("Document %s contains link to deleted item", sourceDocument.getPath()));
            }

            if (!linkDocument.hasTranslation(targetLocale)) {
                // Create a ChangeSet for the linked document, and populate the folders,
                // this will allow the checking of the ChangeSet path to see if there is already a
                // ChangeSet for this path
                ChangeSet linkChange = changeSetFactory.createChangeSet(targetLocale);
                linkChange.populateFolders(linkDocument);
                //We might already have a ChangeSet for this folder, check it doesn't already exist
                ChangeSet existingChangeSet = null;
                String changeSetPath = linkChange.getTargetPath();
                for (ChangeSet toCheck : changeSetList) {
                    if (changeSetPath.equals(toCheck.getTargetPath())) {
                        existingChangeSet = toCheck;
                        break;
                    }
                }
                if (existingChangeSet != null) {
                    if (!existingChangeSet.containsDocumentMatchingUrl(
                            linkDocument.getVariantNode(JcrDocument.VARIANT_UNPUBLISHED).getName())) {
                        existingChangeSet.addDocument(linkDocument, true);
                    } else {
                        logger.debug("Duplicate document, not adding to ChangeSet.");
                    }
                } else {
                    linkChange.addDocument(linkDocument, true);
                    changeSetList.add(linkChange);
                }
            }
        }
    }

    protected List<Node> getChildTranslatableLinkNodes(Node sourceNode) throws RepositoryException {
        NodeIterator childNodes = sourceNode.getNodes();
        List<Node> translatableChildNodes = new ArrayList<>();
        while (childNodes.hasNext()) {
            Node childNode = childNodes.nextNode();
            if (childNode.isNodeType(HippoStdNodeType.NT_CONTAINER)) {
                translatableChildNodes.addAll(getChildTranslatableLinkNodes(childNode));
            } else if (childNode.isNodeType("hippo:mirror")) {
                String linkUUID = childNode.getProperty("hippo:docbase").getString();
                if (linkUUID != null && !linkUUID.equals("") && !linkUUID.startsWith("cafebabe-")) {
                    translatableChildNodes.add(childNode);
                }
            }
        }
        return translatableChildNodes;
    }

    public void applyChangeSet(List<ChangeSet> changeSetList,
                               Session session,
                               TranslationWorkflow workflow)
            throws TranslationException, WorkflowException,
            RepositoryException, RemoteException, ObjectBeanManagerException {
        // We want to check them all at the same time for same name siblings,
        // otherwise the creation of nested folders could throw a false SNS exception
        for (ChangeSet changeSet : changeSetList) {
            changeSet.checkForSameNameSiblings(session);
        }
        for (ChangeSet changeSet : changeSetList) {
            // working from index zero work our way up the folders creating them as we go
            // there must always be a root folder and it always exists so skipping to index 1
            List<FolderTranslation> folders = changeSet.getFolders();
            for (int index = 1; index < folders.size(); index++) {
                FolderTranslation translation = folders.get(index);
                if (translation.isEditable()) {
                    if (!saveFolder(translation, session, changeSet.getTargetLocale().getName())) {
                        throw new TranslationException(COULD_NOT_CREATE_FOLDERS);
                    }
                }
            }

            // Need to apply all non TranslationLinkContainer instances first to ensure we have a
            // translated version of all linked documents
            applyDocumentChanges(changeSet, true, session, workflow);

            // Now we can apply all TranslationLinkContainer instance, all linked documents should exist
            applyDocumentChanges(changeSet, false, session, workflow);
        }

        if (!changeSetList.isEmpty()) {
            workflow.saveSession();
        }
    }

    protected void applyDocumentChanges(ChangeSet changeSet,
                                        boolean includeLinks,
                                        Session session,
                                        TranslationWorkflow workflow)
            throws RepositoryException, ObjectBeanManagerException, RemoteException, WorkflowException {
        WorkflowManager manager = ((HippoWorkspace) session.getWorkspace()).getWorkflowManager();
        for (FolderTranslation document : changeSet.getDocuments()) {
            if (includeLinks == document.isLinkedDocument()) {
                JcrDocument sourceDocument = jcrDocumentFactory.createFromNode(session.getNodeByIdentifier(document.getId()));
                Document translatedDocument = workflow.addTranslation(changeSet.getTargetLocale().getName(), document.getUrlfr(),
                        sourceDocument.getVariantNode(JcrDocument.VARIANT_UNPUBLISHED));
                DefaultWorkflow defaultWorkflow = (DefaultWorkflow) manager.getWorkflow("core", translatedDocument);
                defaultWorkflow.setDisplayName(document.getNamefr());
            }
        }
    }

    protected boolean saveFolder(FolderTranslation ft, Session session, String targetLanguage)
            throws ObjectBeanManagerException {
        if (!ft.isEditable()) {
            throw new UnsupportedOperationException("Translation is immutable");
        }
        String id = ft.getId();
        try {
            Node node = session.getNodeByIdentifier(id);
            // If the folder has already been translated just act as though we have just created it
            HippoTranslatedNode translatedNode = hippoTranslatedNodeFactory.createFromNode(node);
            if (translatedNode.hasTranslation(targetLanguage)) {
                return true;
            }
            WorkflowManager manager = ((HippoWorkspace) node.getSession().getWorkspace()).getWorkflowManager();
            TranslationWorkflow tw = (TranslationWorkflow) manager.getWorkflow("translation", node);
            String namefr = ft.getNamefr();
            String urlfr = ft.getUrlfr();
            Document translationDoc = tw.addTranslation(targetLanguage, urlfr);
            if (namefr != null && !urlfr.equals(namefr)) {
                DefaultWorkflow defaultWorkflow = (DefaultWorkflow) manager.getWorkflow("core", translationDoc);
                defaultWorkflow.setDisplayName(namefr);
            }
            return true;
        } catch (RepositoryException e) {
            logger.error("Could not persist folder translation for {}", id, e);
        } catch (RemoteException e) {
            logger.error("Could not contact repository when storing folder translation for {}", id, e);
        } catch (WorkflowException e) {
            logger.error("Workflow prevented storing translation for {}", id, e);
        }
        return false;
    }
}
