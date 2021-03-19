package com.visitscotland.brxm.translation.plugin;

import com.visitscotland.brxm.translation.SessionFactory;
import org.hippoecm.repository.api.Document;
import org.hippoecm.repository.api.HippoWorkspace;
import org.hippoecm.repository.api.WorkflowException;
import org.hippoecm.repository.api.WorkflowManager;
import org.hippoecm.repository.standardworkflow.DefaultWorkflow;
import org.hippoecm.repository.translation.HippoTranslatedNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.rmi.RemoteException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.same;

@ExtendWith(MockitoExtension.class)
public class DocumentTranslatorSaveFolderTest {
    @Mock
    private Session mockSession;
    private DocumentTranslator translator;
    @Mock
    HippoTranslatedNodeFactory mockNodeFactory;
    @Mock
    SessionFactory mockSessionFactory;
    @Mock
    JcrDocumentFactory mockJcrDocumentFactory;
    @Mock
    ChangeSetFactory mockChangeSetFactory;

    @BeforeEach
    public void beforeEach() {
        translator = new DocumentTranslator(mockNodeFactory, mockSessionFactory, mockJcrDocumentFactory,
                mockChangeSetFactory);
    }

    @Test
    public void saveFolder_immutable() {
        FolderTranslation folder = createFolderTranslation(false);

        assertThrows(UnsupportedOperationException.class,
                () -> translator.saveFolder(folder, mockSession, "fr"));
    }

    @Test
    public void saveFolder_getNodeByIdentifier_RepositoryException() throws Exception {
        String nodeId = "ft1";
        FolderTranslation folder = createFolderTranslation(true, nodeId);
        when(mockSession.getNodeByIdentifier(eq(nodeId))).thenThrow(new RepositoryException());

        assertFalse(translator.saveFolder(folder, mockSession, "es"));
    }

    @Test
    public void saveFolder_getSession_RepositoryException() throws Exception {
        String nodeId = "ft2";
        FolderTranslation folder = createFolderTranslation(true, nodeId);
        Node mockNode = mock(Node.class);
        when(mockNode.getSession()).thenThrow(new RepositoryException());
        setTranslated(false, mockNode);
        when(mockSession.getNodeByIdentifier(eq(nodeId))).thenReturn(mockNode);

        assertFalse(translator.saveFolder(folder, mockSession, "de"));
    }

    @Test
    public void saveFolder_getWorkflowManager_RepositoryException() throws Exception {
        String nodeId = "ft3";
        HippoWorkspace mockWorkspace = mock(HippoWorkspace.class);
        when(mockWorkspace.getWorkflowManager()).thenThrow(new RepositoryException());
        Session mockNodeSession = createMockNodeSession(mockWorkspace);
        Node mockNode = createMockNode(mockNodeSession);
        setTranslated(false, mockNode);
        when(mockSession.getNodeByIdentifier(eq(nodeId))).thenReturn(mockNode);
        FolderTranslation folder = createFolderTranslation(true, nodeId);

        assertFalse(translator.saveFolder(folder, mockSession, "it"));
    }

    @Test
    public void saveFolder_getWorkflow_RepositoryException() throws Exception {
        String nodeId = "ft4";
        WorkflowManager mockManager = mock(WorkflowManager.class);
        when(mockManager.getWorkflow(anyString(), any(Node.class))).thenThrow(new RepositoryException());
        HippoWorkspace mockWorkspace = createMockHippoWorkspace(mockManager);
        Session mockNodeSession = createMockNodeSession(mockWorkspace);
        Node mockNode = createMockNode(mockNodeSession);
        setTranslated(false, mockNode);
        when(mockSession.getNodeByIdentifier(eq(nodeId))).thenReturn(mockNode);
        FolderTranslation folder = createFolderTranslation(true, nodeId);

        assertFalse(translator.saveFolder(folder, mockSession, "en"));
    }

    @Test
    public void saveFolder_addTranslation_WorkflowException() throws Exception {
        String nodeId = "ft5";
        TranslationWorkflow mockWorkflow = mock(TranslationWorkflow.class);
        when(mockWorkflow.addTranslation(anyString(), anyString())).thenThrow(new WorkflowException("workflow"));
        WorkflowManager mockManager = createMockWorkflowManager(mockWorkflow);
        HippoWorkspace mockWorkspace = createMockHippoWorkspace(mockManager);
        Session mockNodeSession = createMockNodeSession(mockWorkspace);
        Node mockNode = createMockNode(mockNodeSession);
        setTranslated(false, mockNode);
        when(mockSession.getNodeByIdentifier(eq(nodeId))).thenReturn(mockNode);
        FolderTranslation folder = createFolderTranslation(true, nodeId, "translatedName", "translatedUrl");

        assertFalse(translator.saveFolder(folder, mockSession, "nn"));
    }

    @Test
    public void saveFolder_addTranslation_RepositoryException() throws Exception {
        String nodeId = "ft6";
        TranslationWorkflow mockWorkflow = mock(TranslationWorkflow.class);
        when(mockWorkflow.addTranslation(anyString(), anyString())).thenThrow(new RepositoryException());
        WorkflowManager mockManager = createMockWorkflowManager(mockWorkflow);
        HippoWorkspace mockWorkspace = createMockHippoWorkspace(mockManager);
        Session mockNodeSession = createMockNodeSession(mockWorkspace);
        Node mockNode = createMockNode(mockNodeSession);
        setTranslated(false, mockNode);
        when(mockSession.getNodeByIdentifier(eq(nodeId))).thenReturn(mockNode);
        FolderTranslation folder = createFolderTranslation(true, nodeId, "translatedName", "translatedUrl");

        assertFalse(translator.saveFolder(folder, mockSession, "nl"));
    }

    @Test
    public void saveFolder_addTranslation_RemoteException() throws Exception {
        String nodeId = "ft7";
        TranslationWorkflow mockWorkflow = mock(TranslationWorkflow.class);
        when(mockWorkflow.addTranslation(anyString(), anyString())).thenThrow(new RemoteException());
        WorkflowManager mockManager = createMockWorkflowManager(mockWorkflow);
        HippoWorkspace mockWorkspace = createMockHippoWorkspace(mockManager);
        Session mockNodeSession = createMockNodeSession(mockWorkspace);
        Node mockNode = createMockNode(mockNodeSession);
        setTranslated(false, mockNode);
        when(mockSession.getNodeByIdentifier(eq(nodeId))).thenReturn(mockNode);
        FolderTranslation folder = createFolderTranslation(true, nodeId, "translatedName", "translatedUrl");

        assertFalse(translator.saveFolder(folder, mockSession, "no"));
    }

    @Test
    public void saveFolder_nameFr_not_equal_urlFr() throws Exception {
        String nodeId = "ft8";

        Document mockDocument = mock(Document.class);
        TranslationWorkflow mockWorkflow = mock(TranslationWorkflow.class);
        when(mockWorkflow.addTranslation(anyString(), anyString())).thenReturn(mockDocument);
        WorkflowManager mockManager = createMockWorkflowManager(mockWorkflow);
        HippoWorkspace mockWorkspace = createMockHippoWorkspace(mockManager);
        Session mockNodeSession = createMockNodeSession(mockWorkspace);
        Node mockNode = createMockNode(mockNodeSession);
        setTranslated(false, mockNode);
        when(mockSession.getNodeByIdentifier(eq(nodeId))).thenReturn(mockNode);
        FolderTranslation folder = createFolderTranslation(true, nodeId, "translatedName", "translatedUrl");

        DefaultWorkflow mockDefaultWorkflow = mock(DefaultWorkflow.class);
        when(mockManager.getWorkflow(eq("core"), any(Document.class))).thenReturn(mockDefaultWorkflow);

        assertTrue(translator.saveFolder(folder, mockSession, "no"));

        verify(mockDefaultWorkflow).setDisplayName(eq("translatedName"));
    }

    @Test
    public void saveFolder_nameFr_equals_urlFr() throws Exception {
        String nodeId = "ft8";

        Document mockDocument = mock(Document.class);
        TranslationWorkflow mockWorkflow = mock(TranslationWorkflow.class);
        when(mockWorkflow.addTranslation(anyString(), anyString())).thenReturn(mockDocument);
        WorkflowManager mockManager = createMockWorkflowManager(mockWorkflow);
        HippoWorkspace mockWorkspace = createMockHippoWorkspace(mockManager);
        Session mockNodeSession = createMockNodeSession(mockWorkspace);
        Node mockNode = createMockNode(mockNodeSession);
        setTranslated(false, mockNode);
        when(mockSession.getNodeByIdentifier(eq(nodeId))).thenReturn(mockNode);
        FolderTranslation folder = createFolderTranslation(true, nodeId, "translatedName", "translatedName");

        assertTrue(translator.saveFolder(folder, mockSession, "no"));

    }

    @Test
    public void saveFolder_alreadyTranslated() throws Exception {
        // When a folder already has a translation the method should
        // just act as thought it has been created successfully and return truw
        String nodeId = "ft9";

        Node mockNode = mock(Node.class);
        setTranslated(true, mockNode);
        when(mockSession.getNodeByIdentifier(eq(nodeId))).thenReturn(mockNode);
        FolderTranslation folder = createFolderTranslation(true, nodeId, "translatedName", "translatedName");

        assertTrue(translator.saveFolder(folder, mockSession, "no"));

    }

    private void setTranslated(boolean isTranslated, Node folderNode) throws Exception {
        HippoTranslatedNode mockTranslatedNode = mock(HippoTranslatedNode.class);
        when(mockNodeFactory.createFromNode(same(folderNode))).thenReturn(mockTranslatedNode);
        when(mockTranslatedNode.hasTranslation(anyString())).thenReturn(isTranslated);
    }

    private FolderTranslation createFolderTranslation(boolean editable) {
        FolderTranslation ft = createFolderTranslation(editable, "id");
        return ft;
    }

    private FolderTranslation createFolderTranslation(boolean editable, String id) {
        FolderTranslation ft = new FolderTranslation(id);
        ft.setEditable(editable);
        return ft;
    }

    private FolderTranslation createFolderTranslation(boolean editable, String id, String translatedName, String translatedUrl) {
        FolderTranslation ft = createFolderTranslation(editable, id);
        ft.setNamefr(translatedName);
        ft.setUrlfr(translatedUrl);
        return ft;
    }

    private Node createMockNode(Session mockNodeSession) throws Exception {
        Node mockNode = mock(Node.class);
        when(mockNode.getSession()).thenReturn(mockNodeSession);
        return mockNode;
    }

    private Session createMockNodeSession(HippoWorkspace mockWorkspace) {
        Session mockNodeSession = mock(Session.class);
        when(mockNodeSession.getWorkspace()).thenReturn(mockWorkspace);
        return mockNodeSession;
    }

    private HippoWorkspace createMockHippoWorkspace(WorkflowManager mockManager) throws Exception {
        HippoWorkspace mockWorkspace = mock(HippoWorkspace.class);
        when(mockWorkspace.getWorkflowManager()).thenReturn(mockManager);
        return mockWorkspace;
    }

    private WorkflowManager createMockWorkflowManager(TranslationWorkflow mockWorkflow) throws Exception {
        WorkflowManager mockManager = mock(WorkflowManager.class);
        when(mockManager.getWorkflow(eq("translation"), any(Node.class))).thenReturn(mockWorkflow);
        return mockManager;
    }
}
