package com.visitscotland.brxm.translation.plugin;

import com.visitscotland.brxm.translation.SessionFactory;
import org.hippoecm.addon.workflow.WorkflowSNSException;
import org.hippoecm.frontend.translation.ILocaleProvider;
import org.hippoecm.repository.api.Document;
import org.hippoecm.repository.api.HippoNode;
import org.hippoecm.repository.api.HippoWorkspace;
import org.hippoecm.repository.api.WorkflowManager;
import org.hippoecm.repository.standardworkflow.DefaultWorkflow;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.jcr.Session;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DocumentTranslatorApplyChangeSetTest {
    private DocumentTranslator documentTranslator;
    @Mock
    private ChangeSet mockChange;
    @Mock
    private Session mockSession;
    @Mock
    private HippoWorkspace mockWorkspace;
    @Mock
    private WorkflowManager mockWorkflowManager;
    @Mock
    private DefaultWorkflow mockDefaultWorkflow;
    @Mock
    private TranslationWorkflow mockWorkflow;
    @Mock
    private ILocaleProvider.HippoLocale mockTargetLocale;
    @Mock
    HippoTranslatedNodeFactory mockNodeFactory;
    @Mock
    SessionFactory mockSessionFactory;
    @Mock
    JcrDocumentFactory mockJcrDocumentFactory;
    @Mock
    ChangeSetFactory mockChangeSetFactory;
    private List<FolderTranslation> folderList;
    private List<FolderTranslation> documentList;
    private ArrayList<ChangeSet> changeSetList;

    @BeforeEach
    public void beforeEach() throws Exception {
        documentTranslator = spy(new DocumentTranslator(mockNodeFactory, mockSessionFactory, mockJcrDocumentFactory,
                mockChangeSetFactory));
        folderList = new ArrayList<>();
        documentList = new ArrayList<>();
        lenient().when(mockChange.getFolders()).thenReturn(folderList);
        lenient().when(mockChange.getDocuments()).thenReturn(documentList);
        lenient().when(mockChange.getTargetLocale()).thenReturn(mockTargetLocale);
        lenient().when(mockTargetLocale.getName()).thenReturn("fr");

        changeSetList = new ArrayList<>();
        changeSetList.add(mockChange);
        lenient().when(mockSessionFactory.getJcrSession()).thenReturn(mockSession);
        lenient().when(mockSession.getWorkspace()).thenReturn(mockWorkspace);
        lenient().when(mockWorkspace.getWorkflowManager()).thenReturn(mockWorkflowManager);
        lenient().when(mockWorkflowManager.getWorkflow(eq("core"), any(Document.class))).thenReturn(mockDefaultWorkflow);
        lenient().when(mockWorkflow.addTranslation(anyString(), anyString(), any())).thenReturn(mock(Document.class));
    }

    @Test
    public void applyChangeSet_allEmpty() throws Exception {
        // Not a realistic scenario, but should not cause an exception
        documentTranslator.applyChangeSet(changeSetList, mockSession, mockWorkflow);

        verify(mockWorkflow).saveSession();
    }

    @Test
    public void applyChangeSet_hasSameNameSibling() throws Exception {
        // When there is a same name sibling should throw a WorkflowSNSException
        doThrow(new WorkflowSNSException("")).when(mockChange).checkForSameNameSiblings(eq(mockSession));
        assertThrows(WorkflowSNSException.class, () -> documentTranslator.applyChangeSet(changeSetList, mockSession, mockWorkflow));

        verify(mockWorkflow, never()).saveSession();
    }

    @Test
    public void applyChangeSet_allFoldersTranslated() throws Exception {
        // When all the folders in the list are translated the saveFolder method should never be called
        FolderTranslation rootFolder = new FolderTranslation("rootId");
        FolderTranslation folder1 = new FolderTranslation("folder1id");
        folder1.setEditable(false);
        FolderTranslation folder2 = new FolderTranslation("folder2id");
        folder2.setEditable(false);

        folderList.add(rootFolder);
        folderList.add(folder1);
        folderList.add(folder2);

        documentTranslator.applyChangeSet(changeSetList, mockSession, mockWorkflow);

        verify(documentTranslator, never()).saveFolder(any(), any(), any());
        verify(mockWorkflow).saveSession();
    }

    @Test
    public void applyChangeSet_missingFolders_saveSuccess() throws Exception {
        // Should call saveFolder method for each folder with no translation
        FolderTranslation rootFolder = new FolderTranslation("rootId");
        FolderTranslation folder1 = new FolderTranslation("folder1id");
        folder1.setEditable(false);
        FolderTranslation folder2 = new FolderTranslation("folder2id");
        folder2.setEditable(true);

        folderList.add(rootFolder);
        folderList.add(folder1);
        folderList.add(folder2);

        doReturn(true).when(documentTranslator).saveFolder(any(), any(), any());

        documentTranslator.applyChangeSet(changeSetList, mockSession, mockWorkflow);

        verify(documentTranslator, times(1)).saveFolder(same(folder2), same(mockSession), eq("fr"));
        verify(mockWorkflow).saveSession();
    }

    @Test
    public void applyChangeSet_missingFolders_saveFail() throws Exception {
        // Should call saveFolder method for each folder with no translation
        FolderTranslation rootFolder = new FolderTranslation("rootId");
        FolderTranslation folder1 = new FolderTranslation("folder1id");
        folder1.setEditable(false);
        FolderTranslation folder2 = new FolderTranslation("folder2id");
        folder2.setEditable(true);

        folderList.add(rootFolder);
        folderList.add(folder1);
        folderList.add(folder2);

        doReturn(false).when(documentTranslator).saveFolder(any(), any(), any());

        assertThrows(TranslationException.class, () -> documentTranslator.applyChangeSet(changeSetList, mockSession, mockWorkflow));

        verify(documentTranslator, times(1)).saveFolder(any(), any(), any());
        verify(mockWorkflow, never()).saveSession();
    }

    @Test
    public void applyChangeSet_withDocuments() throws Exception {
        // There should be a call for addTranslation for each document in the ChangeSet
        FolderTranslation translation1 = mock(FolderTranslation.class);
        HippoNode document1Node = mock(HippoNode.class);
        JcrDocument document1 = mock(JcrDocument.class);
        HippoNode document1Variant = mock(HippoNode.class);
        when(translation1.getNamefr()).thenReturn("document1");
        when(translation1.getUrlfr()).thenReturn("document1url");
        when(document1.getVariantNode(eq(JcrDocument.VARIANT_UNPUBLISHED))).thenReturn(document1Variant);
        when(translation1.getId()).thenReturn("doc1");
        when(mockSession.getNodeByIdentifier(eq("doc1"))).thenReturn(document1Node);
        doReturn(document1).when(mockJcrDocumentFactory).createFromNode(document1Node);
        documentList.add(translation1);

        FolderTranslation translation2 = mock(FolderTranslation.class);
        HippoNode document2Node = mock(HippoNode.class);
        JcrDocument document2 = mock(JcrDocument.class);
        HippoNode document2Variant = mock(HippoNode.class);
        when(translation2.getNamefr()).thenReturn("document2");
        when(translation2.getUrlfr()).thenReturn("document2url");
        when(document2.getVariantNode(eq(JcrDocument.VARIANT_UNPUBLISHED))).thenReturn(document2Variant);
        when(translation2.getId()).thenReturn("doc2");
        when(mockSession.getNodeByIdentifier(eq("doc2"))).thenReturn(document2Node);
        doReturn(document2).when(mockJcrDocumentFactory).createFromNode(document2Node);
        documentList.add(translation2);

        documentTranslator.applyChangeSet(changeSetList, mockSession, mockWorkflow);

        verify(mockWorkflow).addTranslation(eq("fr"), eq("document1url"), same(document1Variant));
        verify(mockDefaultWorkflow).setDisplayName(eq("document1"));
        verify(mockWorkflow).addTranslation(eq("fr"), eq("document2url"), same(document2Variant));
        verify(mockDefaultWorkflow).setDisplayName(eq("document2"));
        verify(mockWorkflow).saveSession();
    }

    @Test
    public void applyChangeSet_withDocuments_andFolders() throws Exception {
        // Saving both folder and documents, just doing both in the same test to be sure there is no interdependency
        FolderTranslation rootFolder = new FolderTranslation("rootId");
        FolderTranslation folder1 = new FolderTranslation("folder1id");
        folder1.setEditable(false);
        FolderTranslation folder2 = new FolderTranslation("folder2id");
        folder2.setEditable(true);

        folderList.add(rootFolder);
        folderList.add(folder1);
        folderList.add(folder2);

        doReturn(true).when(documentTranslator).saveFolder(any(), any(), any());

        FolderTranslation translation1 = mock(FolderTranslation.class);
        HippoNode document1Node = mock(HippoNode.class);
        JcrDocument document1 = mock(JcrDocument.class);
        HippoNode document1Variant = mock(HippoNode.class);
        when(translation1.getNamefr()).thenReturn("document1");
        when(translation1.getUrlfr()).thenReturn("document1url");
        when(document1.getVariantNode(eq(JcrDocument.VARIANT_UNPUBLISHED))).thenReturn(document1Variant);
        when(translation1.getId()).thenReturn("doc1");
        when(mockSession.getNodeByIdentifier(eq("doc1"))).thenReturn(document1Node);
        doReturn(document1).when(mockJcrDocumentFactory).createFromNode(document1Node);
        documentList.add(translation1);

        FolderTranslation translation2 = mock(FolderTranslation.class);
        HippoNode document2Node = mock(HippoNode.class);
        JcrDocument document2 = mock(JcrDocument.class);
        HippoNode document2Variant = mock(HippoNode.class);
        when(translation2.getNamefr()).thenReturn("document2");
        when(translation2.getUrlfr()).thenReturn("document2url");
        when(document2.getVariantNode(eq(JcrDocument.VARIANT_UNPUBLISHED))).thenReturn(document2Variant);
        when(translation2.getId()).thenReturn("doc2");
        when(mockSession.getNodeByIdentifier(eq("doc2"))).thenReturn(document2Node);
        doReturn(document2).when(mockJcrDocumentFactory).createFromNode(document2Node);
        documentList.add(translation2);

        documentTranslator.applyChangeSet(changeSetList, mockSession, mockWorkflow);

        verify(documentTranslator, times(1)).saveFolder(same(folder2), same(mockSession), eq("fr"));

        verify(mockWorkflow).addTranslation(eq("fr"), eq("document1url"), same(document1Variant));
        verify(mockDefaultWorkflow).setDisplayName(eq("document1"));
        verify(mockWorkflow).addTranslation(eq("fr"), eq("document2url"), same(document2Variant));
        verify(mockDefaultWorkflow).setDisplayName(eq("document2"));
        verify(mockWorkflow).saveSession();
    }

    @Test
    public void applyChangeSet_emptyList() throws Exception {
        // When there are no ChangeSet instances in the List it should not cause an error
        // but should also not try to save the session.
        documentTranslator.applyChangeSet(Collections.emptyList(), mockSession, mockWorkflow);
        verify(mockWorkflow, never()).saveSession();
    }

    @Test
    public void applyChangeSet_mixedTranslationLinkContainers() throws Exception {
        // When there is a mixture of linked documents and non links
        // verify that linked documents are cloned first.
        FolderTranslation translation1 = mock(FolderTranslation.class);
        HippoNode document1Node = mock(HippoNode.class);
        JcrDocument document1 = mock(JcrDocument.class);
        HippoNode document1Variant = mock(HippoNode.class);
        when(translation1.getNamefr()).thenReturn("document1");
        when(translation1.getUrlfr()).thenReturn("document1url");
        when(document1.getVariantNode(eq(JcrDocument.VARIANT_UNPUBLISHED))).thenReturn(document1Variant);
        when(translation1.getId()).thenReturn("doc1");
        when(translation1.isLinkedDocument()).thenReturn(false);
        when(mockSession.getNodeByIdentifier(eq("doc1"))).thenReturn(document1Node);
        doReturn(document1).when(mockJcrDocumentFactory).createFromNode(document1Node);
        documentList.add(translation1);

        FolderTranslation translation2 = mock(FolderTranslation.class);
        HippoNode document2Node = mock(HippoNode.class);
        JcrDocument document2 = mock(JcrDocument.class);
        HippoNode document2Variant = mock(HippoNode.class);
        when(translation2.getNamefr()).thenReturn("document2");
        when(translation2.getUrlfr()).thenReturn("document2url");
        when(document2.getVariantNode(eq(JcrDocument.VARIANT_UNPUBLISHED))).thenReturn(document2Variant);
        when(translation2.getId()).thenReturn("doc2");
        when(translation2.isLinkedDocument()).thenReturn(true);
        when(mockSession.getNodeByIdentifier(eq("doc2"))).thenReturn(document2Node);
        doReturn(document2).when(mockJcrDocumentFactory).createFromNode(document2Node);
        documentList.add(translation2);

        FolderTranslation translation3 = mock(FolderTranslation.class);
        HippoNode document3Node = mock(HippoNode.class);
        JcrDocument document3 = mock(JcrDocument.class);
        HippoNode document3Variant = mock(HippoNode.class);
        when(translation3.getNamefr()).thenReturn("document3");
        when(translation3.getUrlfr()).thenReturn("document3url");
        when(document3.getVariantNode(eq(JcrDocument.VARIANT_UNPUBLISHED))).thenReturn(document3Variant);
        when(translation3.getId()).thenReturn("doc3");
        when(translation3.isLinkedDocument()).thenReturn(false);
        when(mockSession.getNodeByIdentifier(eq("doc3"))).thenReturn(document3Node);
        doReturn(document3).when(mockJcrDocumentFactory).createFromNode(document3Node);
        documentList.add(translation3);

        documentTranslator.applyChangeSet(changeSetList, mockSession, mockWorkflow);

        InOrder inOrder = inOrder(mockWorkflow);
        InOrder inOrderDefault = inOrder(mockDefaultWorkflow);
        inOrder.verify(mockWorkflow).addTranslation(eq("fr"), eq("document2url"), same(document2Variant));
        inOrderDefault.verify(mockDefaultWorkflow).setDisplayName("document2");
        inOrder.verify(mockWorkflow).addTranslation(eq("fr"), eq("document1url"), same(document1Variant));
        inOrderDefault.verify(mockDefaultWorkflow).setDisplayName("document1");
        inOrder.verify(mockWorkflow).addTranslation(eq("fr"), eq("document3url"), same(document3Variant));
        inOrderDefault.verify(mockDefaultWorkflow).setDisplayName("document3");
        inOrder.verify(mockWorkflow).saveSession();
    }

    @Test
    public void applyChangeSet_onlyTranslationLinkContainers() throws Exception {
        // When there are only TranslationLinkContainer documents
        // verify that processing is still successful.
        FolderTranslation translation1 = mock(FolderTranslation.class);
        HippoNode document1Node = mock(HippoNode.class);
        JcrDocument document1 = mock(JcrDocument.class);
        HippoNode document1Variant = mock(HippoNode.class);
        when(translation1.getNamefr()).thenReturn("document1");
        when(translation1.getUrlfr()).thenReturn("document1url");
        when(document1.getVariantNode(eq(JcrDocument.VARIANT_UNPUBLISHED))).thenReturn(document1Variant);
        when(translation1.getId()).thenReturn("doc1");
        when(translation1.isLinkedDocument()).thenReturn(true);
        when(mockSession.getNodeByIdentifier(eq("doc1"))).thenReturn(document1Node);
        doReturn(document1).when(mockJcrDocumentFactory).createFromNode(document1Node);
        documentList.add(translation1);

        FolderTranslation translation2 = mock(FolderTranslation.class);
        HippoNode document2Node = mock(HippoNode.class);
        JcrDocument document2 = mock(JcrDocument.class);
        HippoNode document2Variant = mock(HippoNode.class);
        when(translation2.getNamefr()).thenReturn("document2");
        when(translation2.getUrlfr()).thenReturn("document2url");
        when(document2.getVariantNode(eq(JcrDocument.VARIANT_UNPUBLISHED))).thenReturn(document2Variant);
        when(translation2.getId()).thenReturn("doc2");
        when(translation2.isLinkedDocument()).thenReturn(true);
        when(mockSession.getNodeByIdentifier(eq("doc2"))).thenReturn(document2Node);
        doReturn(document2).when(mockJcrDocumentFactory).createFromNode(document2Node);
        documentList.add(translation2);

        documentTranslator.applyChangeSet(changeSetList, mockSession, mockWorkflow);

        verify(mockWorkflow).addTranslation(eq("fr"), eq("document2url"), same(document2Variant));
        verify(mockDefaultWorkflow).setDisplayName(eq("document2"));
        verify(mockWorkflow).addTranslation(eq("fr"), eq("document1url"), same(document1Variant));
        verify(mockDefaultWorkflow).setDisplayName(eq("document1"));
        verify(mockWorkflow).saveSession();
    }
}
