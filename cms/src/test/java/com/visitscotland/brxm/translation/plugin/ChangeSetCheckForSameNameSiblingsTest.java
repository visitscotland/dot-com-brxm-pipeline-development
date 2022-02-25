package com.visitscotland.brxm.translation.plugin;

import org.hippoecm.addon.workflow.WorkflowSNSException;
import org.hippoecm.frontend.translation.ILocaleProvider;
import org.hippoecm.repository.HippoStdNodeType;
import org.hippoecm.repository.api.HippoNode;
import org.hippoecm.repository.api.HippoNodeType;
import org.hippoecm.repository.translation.HippoTranslatedNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Session;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ChangeSetCheckForSameNameSiblingsTest {
    private ChangeSet changeSet;
    @Mock
    private Session mockSession;
    @Mock
    private ILocaleProvider.HippoLocale mockLocale;
    @Mock
    private HippoTranslatedNodeFactory mockHippoTranslatedNodeFactory;
    @Mock
    private JcrFolderTranslationFactory mockJcrFolderTranslationFactory;

    @BeforeEach
    public void beforeEach() {
        when(mockLocale.getName()).thenReturn("fr");
        changeSet = new ChangeSet(mockLocale, mockHippoTranslatedNodeFactory, mockJcrFolderTranslationFactory);
    }

    @Test
    public void checkForSameNameSiblings_deepestTranslatedTargetNode_is_null() throws Exception {
        // This tests that a missing translated folder returns without error.
        // It is replicating existing functionality and is a case that should never happen.
        FolderTranslation rootFolder = createFolderTranslation("root", false);
        FolderTranslation level1 = createFolderTranslation("level1", false);
        FolderTranslation level2 = createFolderTranslation("level2", true);
        changeSet.getFolders().add(rootFolder);
        changeSet.getFolders().add(level1);
        changeSet.getFolders().add(level2);

        createDeepestTranslatedSourceNode("level1");

        assertThrows(WorkflowSNSException.class, () -> changeSet.checkForSameNameSiblings(mockSession));

        assertThrows(WorkflowSNSException.class, () -> changeSet.markSameNameSiblings(mockSession));
    }

    @Test
    public void checkForSameNameSiblings_same_name_folder_sibling_matches_url() throws Exception {
        // Tests that an exception is thrown when a node with the same url name already exists
        FolderTranslation rootFolder = createFolderTranslation("root", false);
        FolderTranslation level1 = createFolderTranslation("level1", false);
        FolderTranslation level2 = createHighestUntranslatedFolder("level2", "targetUrl", "targetName");
        changeSet.getFolders().add(rootFolder);
        changeSet.getFolders().add(level1);
        changeSet.getFolders().add(level2);

        Node translatedTargetFolder = createDeepestTranslatedTargetNode();
        addSiblingMatchingUrl(translatedTargetFolder, "targetUrl");
        HippoTranslatedNode sourceFolderNode = createDeepestTranslatedSourceNode("level1");
        addNodeTranslation(sourceFolderNode, "fr", translatedTargetFolder);
        addSiblingsWithDisplayName(translatedTargetFolder, "diff");

        assertThrows(WorkflowSNSException.class, () -> changeSet.checkForSameNameSiblings(mockSession));

        changeSet.markSameNameSiblings(mockSession);
        assertTrue(changeSet.hasSameNameSiblingConflicts());
        assertFalse(rootFolder.hasSameNameSibling());
        assertFalse(level1.hasSameNameSibling());
        assertFalse(level2.hasSameNameSibling());
        assertFalse(rootFolder.hasSameUrlSibling());
        assertFalse(level1.hasSameUrlSibling());
        assertTrue(level2.hasSameUrlSibling());
    }

    @Test
    public void checkForSameNameSiblings_same_name_folder_sibling_matches_display_name() throws Exception {
        // Tests that an exception is thrown when a node with the same display name already exists
        // This is a little more complex, the addSiblingsWithDisplayName has to mimic a NodeIterator that the
        // SameNameSiblingsUtil uses
        FolderTranslation rootFolder = createFolderTranslation("root", false);
        FolderTranslation level1 = createFolderTranslation("level1", false);
        FolderTranslation level2 = createHighestUntranslatedFolder("level2", "targetUrl", "targetName");
        changeSet.getFolders().add(rootFolder);
        changeSet.getFolders().add(level1);
        changeSet.getFolders().add(level2);

        Node sameNameSibling = createDeepestTranslatedTargetNode();
        HippoTranslatedNode sourceFolderNode = createDeepestTranslatedSourceNode("level1");
        addNodeTranslation(sourceFolderNode, "fr", sameNameSibling);

        addSiblingsWithDisplayName(sameNameSibling, "targetName", "otherName");

        assertThrows(WorkflowSNSException.class, () -> changeSet.checkForSameNameSiblings(mockSession));

        changeSet.markSameNameSiblings(mockSession);
        assertTrue(changeSet.hasSameNameSiblingConflicts());
        assertFalse(rootFolder.hasSameNameSibling());
        assertFalse(level1.hasSameNameSibling());
        assertTrue(level2.hasSameNameSibling());
        assertFalse(rootFolder.hasSameUrlSibling());
        assertFalse(level1.hasSameUrlSibling());
        assertFalse(level2.hasSameUrlSibling());
    }

    @Test
    public void checkForSameNameSiblings_folder_with_no_siblings() throws Exception {
        FolderTranslation rootFolder = createFolderTranslation("root", false);
        FolderTranslation level1 = createFolderTranslation("level1", false);
        FolderTranslation level2 = createHighestUntranslatedFolder("level2", "targetUrl", "targetName");
        changeSet.getFolders().add(rootFolder);
        changeSet.getFolders().add(level1);
        changeSet.getFolders().add(level2);

        Node deepestTranslatedTargetNode = createDeepestTranslatedTargetNode();
        HippoTranslatedNode sourceFolderNode = createDeepestTranslatedSourceNode("level1");
        addNodeTranslation(sourceFolderNode,"fr", deepestTranslatedTargetNode);

        NodeIterator mockIterator = mock(NodeIterator.class);
        when(deepestTranslatedTargetNode.getNodes()).thenReturn(mockIterator);
        when(mockIterator.hasNext()).thenReturn(false);

        changeSet.checkForSameNameSiblings(mockSession);

        changeSet.markSameNameSiblings(mockSession);
        assertFalse(changeSet.hasSameNameSiblingConflicts());
        assertFalse(rootFolder.hasSameNameSibling());
        assertFalse(level1.hasSameNameSibling());
        assertFalse(level2.hasSameNameSibling());
        assertFalse(rootFolder.hasSameUrlSibling());
        assertFalse(level1.hasSameUrlSibling());
        assertFalse(level2.hasSameUrlSibling());
    }

    @Test
    public void checkForSameNameSiblings_with_siblings_but_all_different_names_and_urls() throws Exception {
        FolderTranslation rootFolder = createFolderTranslation("root", false);
        FolderTranslation level1 = createFolderTranslation("level1", false);
        FolderTranslation level2 = createHighestUntranslatedFolder("level2", "targetUrl", "targetName");
        changeSet.getFolders().add(rootFolder);
        changeSet.getFolders().add(level1);
        changeSet.getFolders().add(level2);

        Node deepestTranslatedTargetNode = createDeepestTranslatedTargetNode();
        HippoTranslatedNode sourceFolderNode = createDeepestTranslatedSourceNode("level1");
        addNodeTranslation(sourceFolderNode, "fr", deepestTranslatedTargetNode);

        addSiblingsWithDisplayName(deepestTranslatedTargetNode, "targetName-diff");

        changeSet.checkForSameNameSiblings(mockSession);

        changeSet.markSameNameSiblings(mockSession);
        assertFalse(changeSet.hasSameNameSiblingConflicts());
        assertFalse(rootFolder.hasSameNameSibling());
        assertFalse(level1.hasSameNameSibling());
        assertFalse(level2.hasSameNameSibling());
        assertFalse(rootFolder.hasSameUrlSibling());
        assertFalse(level1.hasSameUrlSibling());
        assertFalse(level2.hasSameUrlSibling());
    }

    @Test
    public void checkForSameNameSiblings_document_with_sibling_all_different() throws Exception {
        // If there are no same name siblings in the folders it moves on to check all of the documents
        // This test will have a document with siblings, but no same name sibling
        Node documentParentFolder = initialiseFoldersAllTranslated();

        FolderTranslation document1 = createHighestUntranslatedFolder("doc1", "doc1url", "doc1name");
        FolderTranslation document2 = createHighestUntranslatedFolder("doc2", "doc2url", "doc2name");
        FolderTranslation document3 = createHighestUntranslatedFolder("doc3", "doc3url", "doc3name");
        changeSet.getDocuments().add(document1);
        changeSet.getDocuments().add(document2);
        changeSet.getDocuments().add(document3);

        addSiblingsWithDisplayName(documentParentFolder, "doc1-diff");

        changeSet.checkForSameNameSiblings(mockSession);

        changeSet.markSameNameSiblings(mockSession);
        assertFalse(changeSet.hasSameNameSiblingConflicts());
        assertFalse(document1.hasSameNameSibling());
        assertFalse(document2.hasSameNameSibling());
        assertFalse(document3.hasSameNameSibling());
        assertFalse(document1.hasSameUrlSibling());
        assertFalse(document2.hasSameUrlSibling());
        assertFalse(document3.hasSameUrlSibling());
    }

    @Test
    public void checkForSameNameSiblings_document_with_same_url_sibling() throws Exception {
        // If there are no same name siblings in the folders it moves on to check all of the documents
        // This test will have a document with siblings, sibling with same url
        Node documentParentFolder = initialiseFoldersAllTranslated();

        FolderTranslation document1 = createHighestUntranslatedFolder("doc1", "doc1url", "doc1name");
        FolderTranslation document2 = createHighestUntranslatedFolder("doc2", "doc2url", "doc2name");
        FolderTranslation document3 = createHighestUntranslatedFolder("doc3", "doc3url", "doc3name");
        changeSet.getDocuments().add(document1);
        changeSet.getDocuments().add(document2);
        changeSet.getDocuments().add(document3);

        addSiblingsWithDisplayName(documentParentFolder, "doc1name-diff");
        addSiblingMatchingUrl(documentParentFolder,"doc2url");
        addSiblingMatchingUrl(documentParentFolder,"doc3url");

        assertThrows(WorkflowSNSException.class, () -> changeSet.checkForSameNameSiblings(mockSession));

        changeSet.markSameNameSiblings(mockSession);
        assertTrue(changeSet.hasSameNameSiblingConflicts());
        assertFalse(document1.hasSameNameSibling());
        assertFalse(document2.hasSameNameSibling());
        assertFalse(document3.hasSameNameSibling());
        assertFalse(document1.hasSameUrlSibling());
        assertTrue(document2.hasSameUrlSibling());
        assertTrue(document3.hasSameUrlSibling());
    }

    @Test
    public void checkForSameNameSiblings_document_with_same_name_siblings() throws Exception {
        // If there are no same name siblings in the folders it moves on to check all of the documents
        // This test will have a document with siblings, sibling with same display name
        Node documentParentFolder = initialiseFoldersAllTranslated();

        FolderTranslation document1 = createHighestUntranslatedFolder("doc1", "doc1url", "doc1name");
        FolderTranslation document2 = createHighestUntranslatedFolder("doc2", "doc2url", "doc2name");
        FolderTranslation document3 = createHighestUntranslatedFolder("doc3", "doc3url", "doc3name");
        changeSet.getDocuments().add(document1);
        changeSet.getDocuments().add(document2);
        changeSet.getDocuments().add(document3);

        addSiblingsWithDisplayName(documentParentFolder, "doc2name", "doc3name", "doc1Name-diff", "doc-other");

        assertThrows(WorkflowSNSException.class, () -> changeSet.checkForSameNameSiblings(mockSession));

        changeSet.markSameNameSiblings(mockSession);
        assertTrue(changeSet.hasSameNameSiblingConflicts());
        assertFalse(document1.hasSameNameSibling());
        assertTrue(document2.hasSameNameSibling());
        assertTrue(document3.hasSameNameSibling());
        assertFalse(document1.hasSameUrlSibling());
        assertFalse(document2.hasSameUrlSibling());
        assertFalse(document3.hasSameUrlSibling());
    }

    @Test
    public void checkForSameNameSiblings_document_with_no_siblings() throws Exception {
        // If there are no same name siblings in the folders it moves on to check all of the documents
        // This test will have a document with no siblings.
        Node documentParentFolder = initialiseFoldersAllTranslated();

        FolderTranslation document1 = createHighestUntranslatedFolder("doc1", "doc1url", "doc1name");
        FolderTranslation document2 = createHighestUntranslatedFolder("doc2", "doc2url", "doc2name");
        FolderTranslation document3 = createHighestUntranslatedFolder("doc3", "doc3url", "doc3name");
        changeSet.getDocuments().add(document1);
        changeSet.getDocuments().add(document2);
        changeSet.getDocuments().add(document3);

        NodeIterator mockIterator = mock(NodeIterator.class);
        when(documentParentFolder.getNodes()).thenReturn(mockIterator);
        when(mockIterator.hasNext()).thenReturn(false);

        changeSet.checkForSameNameSiblings(mockSession);

        changeSet.markSameNameSiblings(mockSession);
        assertFalse(changeSet.hasSameNameSiblingConflicts());
        assertFalse(document1.hasSameNameSibling());
        assertFalse(document2.hasSameNameSibling());
        assertFalse(document3.hasSameNameSibling());
        assertFalse(document1.hasSameUrlSibling());
        assertFalse(document2.hasSameUrlSibling());
        assertFalse(document3.hasSameUrlSibling());
    }

    private Node initialiseFoldersAllTranslated() throws Exception {
        FolderTranslation rootFolder = createFolderTranslation("root", false);
        FolderTranslation level1 = createFolderTranslation("level1", false);
        FolderTranslation level2 = createFolderTranslation("level2", false);
        changeSet.getFolders().add(rootFolder);
        changeSet.getFolders().add(level1);
        changeSet.getFolders().add(level2);

        Node deepestTranslatedTargetNode = createDeepestTranslatedTargetNode();
        HippoTranslatedNode sourceFolderNode = createDeepestTranslatedSourceNode("level2");
        addNodeTranslation(sourceFolderNode,"fr", deepestTranslatedTargetNode);

        return deepestTranslatedTargetNode;
    }

    private void addSiblingMatchingUrl(Node folderNode, String relPath) throws Exception {
        when(folderNode.hasNode(eq(relPath))).thenReturn(true);
        HippoNode siblingNode = mock(HippoNode.class);
        when(folderNode.getNode(eq(relPath))).thenReturn(siblingNode);
    }

    private void addSiblingsWithDisplayName(Node targetNode, String... targetDisplayNames) throws Exception {
        // This creates a mock NodeIterator by creating a List and using the Iterator from the List as a NodeIterator.
        // The Answer is used to ensure we get a new NodeIterator instance every time getNodes() method is called.
        when(targetNode.getNodes()).thenAnswer(new Answer<NodeIterator>() {
            @Override
            public NodeIterator answer(InvocationOnMock invocation) throws Throwable {
                List<Node> nodeList = new ArrayList<>();

                HippoNode neitherFolderOrHandle = mock(HippoNode.class);
                lenient().when(neitherFolderOrHandle.isNodeType(eq(HippoStdNodeType.NT_FOLDER))).thenReturn(false);
                lenient().when(neitherFolderOrHandle.isNodeType(eq(HippoNodeType.NT_HANDLE))).thenReturn(false);
                nodeList.add(neitherFolderOrHandle);

                int nodeNumber = 1;
                for (String displayName : targetDisplayNames) {
                    HippoNode mockNode = mock(HippoNode.class);
                    nodeList.add(mockNode);
                    lenient().when(mockNode.getDisplayName()).thenReturn(displayName);
                    if (nodeNumber % 2 == 0) {
                        lenient().when(mockNode.isNodeType(eq(HippoStdNodeType.NT_FOLDER))).thenReturn(true);
                    } else {
                        lenient().when(mockNode.isNodeType(eq(HippoStdNodeType.NT_FOLDER))).thenReturn(false);
                        lenient().when(mockNode.isNodeType(eq(HippoNodeType.NT_HANDLE))).thenReturn(true);
                    }
                    nodeNumber++;
                }
                final Iterator<Node> nodeIterator = nodeList.iterator();
                NodeIterator mockNodeIterator = mock(NodeIterator.class);
                lenient().when(mockNodeIterator.hasNext()).thenAnswer(new Answer<Boolean>() {
                    @Override
                    public Boolean answer(InvocationOnMock invocation) throws Throwable {
                        return nodeIterator.hasNext();
                    }
                });
                lenient().when(mockNodeIterator.nextNode()).thenAnswer(new Answer<Node>() {
                    @Override
                    public Node answer(InvocationOnMock invocation) throws Throwable {
                        return nodeIterator.next();
                    }
                });
                return mockNodeIterator;
            }
        });
    }

    private Node createDeepestTranslatedTargetNode() throws Exception {
        Node deepestTranslatedTargetNode = mock(Node.class);
        lenient().when(deepestTranslatedTargetNode.hasNode(anyString())).thenReturn(false);
        return deepestTranslatedTargetNode;
    }

    private FolderTranslation createHighestUntranslatedFolder(String nodeId, String targetUrlName, String targetLocalizedName) {
        FolderTranslation highestUntranslatedItem = createFolderTranslation(nodeId, true);
        highestUntranslatedItem.setNamefr(targetLocalizedName);
        highestUntranslatedItem.setUrlfr(targetUrlName);
        return highestUntranslatedItem;
    }

    private HippoTranslatedNode createDeepestTranslatedSourceNode(String sourceNodeIdentifier) throws Exception {
        Node sourceNode = mock(Node.class);
        HippoTranslatedNode mockHippoTranslatedNode = mock(HippoTranslatedNode.class);
        when(mockSession.getNodeByIdentifier(sourceNodeIdentifier)).thenReturn(sourceNode);
        when(mockHippoTranslatedNodeFactory.createFromNode(same(sourceNode))).thenReturn(mockHippoTranslatedNode);
        return mockHippoTranslatedNode;
    }

    private void addNodeTranslation(HippoTranslatedNode sourceNode, String language, Node translatedNode) throws Exception {
        when(sourceNode.getTranslation(eq(language))).thenReturn(translatedNode);
    }

    private FolderTranslation createFolderTranslation(String nodeId, boolean isEditable) {
        FolderTranslation mockFolder = new FolderTranslation(nodeId);
        mockFolder.setEditable(isEditable);
        return mockFolder;
    }
}
