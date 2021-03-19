package com.visitscotland.brxm.translation.plugin;

import org.apache.commons.lang3.StringUtils;
import org.hippoecm.addon.workflow.WorkflowSNSException;
import org.hippoecm.frontend.translation.ILocaleProvider;
import org.hippoecm.hst.content.beans.ObjectBeanManagerException;
import org.hippoecm.repository.api.HippoNode;
import org.hippoecm.repository.translation.HippoTranslationNodeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Is used to contain all the changes required for a translation operation.
 * This is split into a list of the folders to be created and the documents to be cloned.
 * All of the documents to be cloned exist in a single folder, this will be the tip of the folders list.
 * A ChangeSet usually specifies the changes to be applied for a single language.
 */
public class ChangeSet {
    private static final Logger logger = LoggerFactory.getLogger(ChangeSet.class);
    private JcrFolderTranslationFactory jcrFolderTranslationFactory;
    private HippoTranslatedNodeFactory translatedNodeFactory;
    private List<FolderTranslation> folders;
    private List<FolderTranslation> documents;
    private ILocaleProvider.HippoLocale targetLocale;

    public ChangeSet(ILocaleProvider.HippoLocale targetLocale) {
        this(targetLocale, new HippoTranslatedNodeFactory(), new JcrFolderTranslationFactory());
    }

    protected ChangeSet(ILocaleProvider.HippoLocale targetLocale,
                        HippoTranslatedNodeFactory translatedNodeFactory,
                        JcrFolderTranslationFactory jcrFolderTranslationFactory) {
        folders = new ArrayList<>();
        documents = new ArrayList<>();
        this.targetLocale = targetLocale;
        this.translatedNodeFactory = translatedNodeFactory;
        this.jcrFolderTranslationFactory = jcrFolderTranslationFactory;
    }

    public List<FolderTranslation> getFolders() {
        return folders;
    }

    public List<FolderTranslation> getDocuments() {
        return documents;
    }

    /**
     * Converts the JcrDocument to a FolderTranslation instance and adds it to the List of documents to be cloned.
     *
     * @param document The JcrDocument to be added to the document list
     * @throws RepositoryException
     */
    public void addDocument(JcrDocument document, boolean isLinkedDocument) throws RepositoryException, ObjectBeanManagerException {
        FolderTranslation documentTranslation = jcrFolderTranslationFactory.createFolderTranslation(document.getHandle(), null);
        documentTranslation.setNamefr(documentTranslation.getName() + " (" + targetLocale.getName().toUpperCase() + ")");
        documentTranslation.setUrlfr(documentTranslation.getUrl());
        documentTranslation.setIsLinkedDocument(isLinkedDocument);
        documents.add(documentTranslation);
    }

    public void populateFolders(JcrDocument sourceDocument) throws RepositoryException {
        Node sourceFolder = findHighestTranslatedSourceFolder(sourceDocument.getHandle());
        String targetLanguage = targetLocale.getName();
        if (sourceFolder == null) return;

        TranslatedFolder sourceTranslatedFolder = addAllUntranslatedFolders(targetLanguage, folders, createTranslatedFolder(sourceFolder));
        TranslatedFolder targetTranslatedFolder = sourceTranslatedFolder.getSibling(targetLanguage);
        assert targetTranslatedFolder != null;
        while (sourceTranslatedFolder != null) {
            FolderTranslation ftx = jcrFolderTranslationFactory.createFolderTranslation(sourceTranslatedFolder.getNode(), targetTranslatedFolder.getNode());
            ftx.setEditable(false);
            this.folders.add(ftx);
            sourceTranslatedFolder = sourceTranslatedFolder.getParent();
            targetTranslatedFolder = targetTranslatedFolder.getParent();
            if (sourceTranslatedFolder == null) {
                break;
            }
        }

        Collections.reverse(this.folders);
    }

    public String getTargetPath() {
        StringBuilder pathBuilder = new StringBuilder();
        Iterator<FolderTranslation> ftIterator = folders.iterator();
        do {
            pathBuilder.append("/");
            pathBuilder.append(ftIterator.next().getUrlfr());
        } while (ftIterator.hasNext());
        return pathBuilder.toString();
    }

    public boolean containsDocumentMatchingUrl(String url) {
        return documents.stream().anyMatch(
                (document) -> (url.equals(document.getUrlfr()) || url.equals(document.getUrl())));
    }

    protected Node findHighestTranslatedSourceFolder(Node sourceFolder) throws RepositoryException {
        // The JCR has a pair of Nodes to represent a document. The highest node has a Mixin of
        // hippo:translated, but its parent does not but is hippo:named. It is the hippo:named Node that
        // is passed to the populateFolders method this method recurses over the nodes to find the first
        // folder that the document is in.
        try {
            while (!sourceFolder.isNodeType(HippoTranslationNodeType.NT_TRANSLATED)) {
                sourceFolder = sourceFolder.getParent();
            }
        } catch (ItemNotFoundException infe) {
            logger.warn("Parent folder of translatable document could not be found", infe);
            return null;
        } catch (AccessDeniedException ade) {
            logger.warn("Parent folder of translatable document is not accessible", ade);
            return null;
        }
        return sourceFolder;
    }

    protected TranslatedFolder addAllUntranslatedFolders(String targetLanguage, List<FolderTranslation> folders, TranslatedFolder sourceFolder) throws RepositoryException {
        // walk up the source tree until a translated ancestor is found
        while (sourceFolder.getSibling(targetLanguage) == null) {
            FolderTranslation ft = jcrFolderTranslationFactory.createFolderTranslation(sourceFolder.getNode(),
                    null);
            ft.setEditable(true);
            ft.setNamefr(ft.getName());
            ft.setUrlfr(ft.getUrl());
            folders.add(ft);

            sourceFolder = sourceFolder.getParent();
            if (sourceFolder == null) {
                throw new RepositoryException("Unable to find root folder for language " + targetLanguage);
            }
        }
        return sourceFolder;
    }

    protected TranslatedFolder createTranslatedFolder(Node node) {
        return new TranslatedFolder(node);
    }

    public ILocaleProvider.HippoLocale getTargetLocale() {
        return targetLocale;
    }

    protected int getIndexOfDeepestTranslatedFolder() {
        if (folders.isEmpty()) {
            throw new IllegalStateException("Unable to find a translated folder when empty");
        }
        if (folders.get(0).isEditable()) {
            throw new IllegalStateException("The root folder must never be untranslated");
        }
        int index = folders.size() - 1;
        while (index > 0 &&
                folders.get(index).isEditable()) {
            index--;
        }

        return index;
    }

    public void checkForSameNameSiblings(Session session) throws WorkflowSNSException, RepositoryException {
        checkForSameNameSiblings(session, true);
    }

    public void markSameNameSiblings(Session session) throws WorkflowSNSException, RepositoryException {
        checkForSameNameSiblings(session, false);
    }

    protected void checkForSameNameSiblings(Session session, boolean throwException)
            throws WorkflowSNSException, RepositoryException {
        final int indexOfDeepestTranslatedFolder = getIndexOfDeepestTranslatedFolder();
        final FolderTranslation deepestTranslatedFolder = folders.get(indexOfDeepestTranslatedFolder);
        final Node deepestTranslatedSourceNode = session.getNodeByIdentifier(deepestTranslatedFolder.getId());
        final Node deepestTranslatedTargetNode = translatedNodeFactory.createFromNode(deepestTranslatedSourceNode).getTranslation(targetLocale.getName());

        if (deepestTranslatedTargetNode == null) {
            // this means that there's a programmatic problem in the construction ot the "folders" list.
            throw new WorkflowSNSException("Invalid folder structure unable to find translated folder");
        }

        // if the highest untranslated item is a folder then no need to check the documents
        // if it is a document then we need to check every document for a same name sibling
        if (indexOfDeepestTranslatedFolder < folders.size() - 1) {
            final FolderTranslation highestUntranslatedItem = folders.get(indexOfDeepestTranslatedFolder + 1);
            hasSameNameSibling(highestUntranslatedItem, deepestTranslatedTargetNode, throwException);
        } else {
            for (FolderTranslation document : documents) {
                hasSameNameSibling(document, deepestTranslatedTargetNode, throwException);
            }
        }
    }

    protected void hasSameNameSibling(FolderTranslation targetFolderTranslation, Node sourceFolderNode, boolean throwException) throws WorkflowSNSException, RepositoryException {
        String targetUrlName = targetFolderTranslation.getUrlfr();
        String targetLocalizedName = targetFolderTranslation.getNamefr();
        if (sourceFolderNode.hasNode(targetUrlName)) {
            Node sameUrlSibling = sourceFolderNode.getNode(targetUrlName);
            targetFolderTranslation.setHasSameUrlSibling(true);
            targetFolderTranslation.setSameUrlSiblingId(sameUrlSibling.getIdentifier());
            if (throwException) {
                throw new WorkflowSNSException("A folder or document with name '" + targetUrlName + "' already exists", targetUrlName);
            }
        }
        // check for duplicated localized name
        Node sameNameSibling = getChildWithDisplayName(sourceFolderNode, targetLocalizedName);
        if (null != sameNameSibling) {
            targetFolderTranslation.setHasSameNameSibling(true);
            targetFolderTranslation.setSameNameSiblingId(sameNameSibling.getIdentifier());
            if (throwException) {
                throw new WorkflowSNSException("A folder or document with localized name '" + targetLocalizedName + "' already exists", targetLocalizedName);
            }
        }
    }

    /**
     * Will return true if there are any documents or folder that have same name siblings in the ChangeSet List
     *
     * @return
     */
    public boolean hasSameNameSiblingConflicts() {
        return folders.stream().anyMatch((folder) -> folder.hasSameNameSibling() || folder.hasSameUrlSibling()) ||
                documents.stream().anyMatch((document) -> document.hasSameNameSibling() || document.hasSameUrlSibling());
    }

    public Node getChildWithDisplayName(Node parentNode, String displayName) throws RepositoryException {
        NodeIterator children = parentNode.getNodes();

        String childName;
        Node child;
        do {
            do {
                if (!children.hasNext()) {
                    return null;
                }

                child = children.nextNode();
            } while (!child.isNodeType("hippostd:folder") && !child.isNodeType("hippo:handle"));

            childName = ((HippoNode) child).getDisplayName();
        } while (!StringUtils.equals(childName, displayName));

        return child;
    }
}
