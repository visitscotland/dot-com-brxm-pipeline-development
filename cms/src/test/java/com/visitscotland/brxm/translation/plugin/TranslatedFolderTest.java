package com.visitscotland.brxm.translation.plugin;

import org.hippoecm.repository.translation.HippoTranslatedNode;
import org.hippoecm.repository.translation.HippoTranslationNodeType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.jcr.Item;
import javax.jcr.ItemNotFoundException;
import javax.jcr.Node;
import javax.jcr.RepositoryException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TranslatedFolderTest {
    private TranslatedFolder folder;
    @Mock
    private Node mockNode;
    @Mock
    private HippoTranslatedNode mockTranslatedNode;

    @BeforeEach
    public void beforeEach() throws Exception {
        folder = spy(new TranslatedFolder(mockNode));
    }

    @Test
    public void constructor_null() {
        // Passing a null node into the constructor should cause an exception
        assertThrows(IllegalArgumentException.class, () -> new TranslatedFolder(null));
    }

    @Test
    public void getParent_none_translated() throws Exception {
        // When the parent path matches /content/documents the method should return null
        Node mockNode3 = mock(Node.class);
        when(mockNode3.getPath()).thenReturn("/content/documents");

        Node mockNode2 = mock(Node.class);
        when(mockNode2.getPath()).thenReturn("/content/documents/one");
        when(mockNode2.getParent()).thenReturn(mockNode3);
        when(mockNode2.isNodeType(HippoTranslationNodeType.NT_TRANSLATED)).thenReturn(false);

        Node mockNode1 = mock(Node.class);
        when(mockNode1.getPath()).thenReturn("/content/documents/one/two");
        when(mockNode1.getParent()).thenReturn(mockNode2);
        when(mockNode1.isNodeType(HippoTranslationNodeType.NT_TRANSLATED)).thenReturn(false);

        when(mockNode.getParent()).thenReturn(mockNode1);

        TranslatedFolder result = folder.getParent();

        assertNull(result);
    }

    @Test
    public void getParent_with_translated() throws Exception {
        Node mockNode2 = mock(Node.class);
        when(mockNode2.getPath()).thenReturn("/content/documents/one");
        when(mockNode2.isNodeType(HippoTranslationNodeType.NT_TRANSLATED)).thenReturn(true);

        Node mockNode1 = mock(Node.class);
        when(mockNode1.getPath()).thenReturn("/content/documents/one/two");
        when(mockNode1.getParent()).thenReturn(mockNode2);
        when(mockNode1.isNodeType(HippoTranslationNodeType.NT_TRANSLATED)).thenReturn(false);

        when(mockNode.getParent()).thenReturn(mockNode1);

        TranslatedFolder result = folder.getParent();

        assertNotNull(result);
        assertSame(mockNode2, result.getNode());
    }

    @Test
    public void getSibling_itemNotFound() throws Exception {
        doReturn(mockTranslatedNode).when(folder).toHippoTranslatedNode();
        when(mockTranslatedNode.getTranslation(Mockito.anyString())).thenThrow(new ItemNotFoundException());

        TranslatedFolder translated = folder.getSibling("fr");

        assertNull(translated);
    }

    @Test
    public void getSibling_repositoryException() throws Exception {
        doReturn(mockTranslatedNode).when(folder).toHippoTranslatedNode();
        when(mockTranslatedNode.getTranslation(Mockito.anyString())).thenThrow(new RepositoryException());

        assertThrows(RepositoryException.class, () -> folder.getSibling("fr"));
    }

    @Test
    public void getNode() {
        // Just testing the node passed to the constructor is the node returned from the getter
        assertSame(mockNode, folder.getNode());
    }

    @Test
    public void hashCode_test() throws Exception {
        String pathOne = "/content/documents/one";
        String pathTwo = "/content/docuents/two";

        Node node = mock(Node.class);
        when(node.getPath()).thenReturn(pathOne);
        TranslatedFolder expectedFolder = new TranslatedFolder(node);

        Node equal = mock(Node.class);
        when(equal.getPath()).thenReturn(pathOne);
        TranslatedFolder equalFolder = new TranslatedFolder(equal);

        Node notEq = mock(Node.class);
        when(notEq.getPath()).thenReturn(pathTwo);
        TranslatedFolder notEqFolder = new TranslatedFolder(notEq);

        assertEquals(expectedFolder.hashCode(), equalFolder.hashCode());
        assertNotEquals(expectedFolder.hashCode(), notEqFolder.hashCode());
    }

    @Test
    public void hashCode_repositoryException() throws Exception {
        when(mockNode.getPath()).thenThrow(new RepositoryException());

        TranslatedFolder nonSpy = new TranslatedFolder(mockNode);

        assertThrows(IllegalStateException.class, () -> nonSpy.hashCode());
    }

    @Test
    public void equals_not_TranslatedFolder() {
        TranslatedFolder nonSpy = new TranslatedFolder(mockNode);
        assertFalse(nonSpy.equals(new Object()));
    }

    @Test
    public void equals_not_same() throws Exception {
        Node notSame = mock(Node.class);
        when(notSame.isSame(any(Item.class))).thenReturn(false);
        TranslatedFolder notSameFolder = new TranslatedFolder(notSame);

        TranslatedFolder nonSpy = new TranslatedFolder(mockNode);

        assertFalse(nonSpy.equals(notSameFolder));
    }

    @Test
    public void equals_RepositoryException() throws Exception {
        Node notSame = mock(Node.class);
        when(notSame.isSame(any(Item.class))).thenThrow(new RepositoryException());
        TranslatedFolder notSameFolder = new TranslatedFolder(notSame);

        TranslatedFolder nonSpy = new TranslatedFolder(mockNode);

        assertThrows(IllegalStateException.class, () -> nonSpy.equals(notSameFolder));
    }

    @Test
    public void equals_same() throws Exception {
        Node same = mock(Node.class);
        when(same.isSame(any(Node.class))).thenReturn(true);
        TranslatedFolder sameFolder = new TranslatedFolder(same);

        TranslatedFolder nonSpy = new TranslatedFolder(mockNode);

        assertTrue(nonSpy.equals(sameFolder));
    }
}
