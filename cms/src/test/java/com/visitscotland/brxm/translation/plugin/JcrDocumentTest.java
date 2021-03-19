package com.visitscotland.brxm.translation.plugin;

import org.hippoecm.repository.HippoStdNodeType;
import org.hippoecm.repository.api.HippoNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Property;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JcrDocumentTest {
    private JcrDocument document;
    @Mock
    HippoNode mockHandle;

    @BeforeEach
    public void beforeEach() throws Exception {
        when(mockHandle.isNodeType(JcrDocument.HIPPO_HANDLE)).thenReturn(true);
        document = new JcrDocument(mockHandle);
    }

    @Test
    public void constructor_withNull() {
        assertThrows(IllegalArgumentException.class, () -> new JcrDocument(null));
    }

    @Test
    public void constructor_withDocumentNode() throws Exception {
        // When a document node is passed it should populate the handle in the document correctly
        HippoNode mockDocumentNode = mock(HippoNode.class);
        when(mockDocumentNode.getParent()).thenReturn(mockHandle);
        when(mockDocumentNode.isNodeType(JcrDocument.HIPPO_HANDLE)).thenReturn(false);

        JcrDocument newDocument = new JcrDocument(mockDocumentNode);

        assertSame(mockHandle, newDocument.getHandle());
    }

    @Test
    public void constructor_withNullParent() throws Exception {
        // When a document node is passed it should have a non null parent
        // this test ensures that if it's parent is null the condition it caught
        HippoNode mockDocumentNode = mock(HippoNode.class);
        when(mockDocumentNode.getParent()).thenReturn(null);
        when(mockDocumentNode.isNodeType(JcrDocument.HIPPO_HANDLE)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> new JcrDocument(mockDocumentNode));
    }

    @Test
    public void constructor_withParentNotHandle() throws Exception {
        // When the document passed to the constructor, or its parent are not null then we cannot use this Node
        HippoNode mockParent = mock(HippoNode.class);
        when(mockParent.isNodeType(JcrDocument.HIPPO_HANDLE)).thenReturn(false);
        HippoNode mockDocumentNode = mock(HippoNode.class);
        when(mockDocumentNode.getParent()).thenReturn(mockParent);
        when(mockDocumentNode.isNodeType(JcrDocument.HIPPO_HANDLE)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> new JcrDocument(mockDocumentNode));
    }

    @Test
    public void getVariants_populateMap() throws Exception {
        // Test the lazy load and
        // Ensure all the sibling variants are populated in the map correctly
        // Also ensures that a missing hippostd:state on the node does not cause an error
        HippoNode missingState = mock(HippoNode.class);
        when(missingState.isNodeType(JcrDocument.HIPPOSTD_PUBLISHABLE)).thenReturn(false);

        HippoNode published = mock(HippoNode.class);
        Property publishedProp = mock(Property.class);
        when(publishedProp.getString()).thenReturn(JcrDocument.VARIANT_PUBLISHED);
        when(published.getProperty(JcrDocument.HIPPOSTD_STATE)).thenReturn(publishedProp);
        when(published.isNodeType(JcrDocument.HIPPOSTD_PUBLISHABLE)).thenReturn(true);

        HippoNode unpublished = mock(HippoNode.class);
        Property unpublishedProp = mock(Property.class);
        when(unpublishedProp.getString()).thenReturn(JcrDocument.VARIANT_UNPUBLISHED);
        when(unpublished.getProperty(JcrDocument.HIPPOSTD_STATE)).thenReturn(unpublishedProp);
        when(unpublished.isNodeType(JcrDocument.HIPPOSTD_PUBLISHABLE)).thenReturn(true);

        HippoNode draft = mock(HippoNode.class);
        Property draftProp = mock(Property.class);
        when(draftProp.getString()).thenReturn(JcrDocument.VARIANT_DRAFT);
        when(draft.getProperty(JcrDocument.HIPPOSTD_STATE)).thenReturn(draftProp);
        when(draft.isNodeType(JcrDocument.HIPPOSTD_PUBLISHABLE)).thenReturn(true);

        NodeIterator variantIterator = createNodeIterator(missingState, published, unpublished, draft);
        when(mockHandle.getNodes()).thenReturn(variantIterator);

        assertNull(document.variantMap);

        Map<String, Node> variantMap = document.getVariants();

        assertNotNull(document.getVariants());
        assertEquals(3, variantMap.size());
        assertSame(published, variantMap.get(JcrDocument.VARIANT_PUBLISHED));
        assertSame(unpublished, variantMap.get(JcrDocument.VARIANT_UNPUBLISHED));
        assertSame(draft, variantMap.get(JcrDocument.VARIANT_DRAFT));
    }

    @Test
    public void getVariantNode() throws Exception {
        // Ensure that the method does not cause an error when the variants have not been loaded yet
        HippoNode published = mock(HippoNode.class);
        Property publishedProp = mock(Property.class);
        when(publishedProp.getString()).thenReturn(JcrDocument.VARIANT_PUBLISHED);
        when(published.getProperty(JcrDocument.HIPPOSTD_STATE)).thenReturn(publishedProp);
        when(published.isNodeType(JcrDocument.HIPPOSTD_PUBLISHABLE)).thenReturn(true);

        NodeIterator variantIterator = createNodeIterator(published);
        when(mockHandle.getNodes()).thenReturn(variantIterator);

        assertNull(document.variantMap);
        assertNull(document.getVariantNode(JcrDocument.VARIANT_DRAFT));
        assertNotNull(document.variantMap);
        assertEquals(published, document.getVariantNode(JcrDocument.VARIANT_PUBLISHED));
    }

    @Test
    public void isNodeType_noUnpublishedVariant() throws Exception {
        // Test lazy load and
        // ensure that a missing unpublished variant always returns false, no error
        HippoNode published = mock(HippoNode.class);
        Property publishedProp = mock(Property.class);
        when(publishedProp.getString()).thenReturn(JcrDocument.VARIANT_PUBLISHED);
        when(published.getProperty(JcrDocument.HIPPOSTD_STATE)).thenReturn(publishedProp);
        when(published.isNodeType(JcrDocument.HIPPOSTD_PUBLISHABLE)).thenReturn(true);

        NodeIterator variantIterator = createNodeIterator(published);
        when(mockHandle.getNodes()).thenReturn(variantIterator);

        assertNull(document.variantMap);
        assertFalse(document.isNodeType("type1"));
        assertNotNull(document.variantMap);
    }

    @Test
    public void isNodeType_notMatchingTypes() throws Exception {
        // Test lazy load and
        // ensure that when the document does not match any of the types it returns false
        HippoNode unpublished = mock(HippoNode.class);
        when(unpublished.isNodeType("type1")).thenReturn(false);
        Property unpublishedProp = mock(Property.class);
        when(unpublishedProp.getString()).thenReturn(JcrDocument.VARIANT_UNPUBLISHED);
        when(unpublished.getProperty(JcrDocument.HIPPOSTD_STATE)).thenReturn(unpublishedProp);
        when(unpublished.isNodeType(JcrDocument.HIPPOSTD_PUBLISHABLE)).thenReturn(true);

        NodeIterator variantIterator = createNodeIterator(unpublished);
        when(mockHandle.getNodes()).thenReturn(variantIterator);

        assertNull(document.variantMap);
        assertFalse(document.isNodeType("type1"));
        assertNotNull(document.variantMap);
    }

    @Test
    public void isNodeType_matchingTypes() throws Exception {
        // Test lazy load and
        // ensure that when the document matches any of the types it returns true
        HippoNode unpublished = mock(HippoNode.class);
        when(unpublished.isNodeType("type1")).thenReturn(true);
        Property unpublishedProp = mock(Property.class);
        when(unpublishedProp.getString()).thenReturn(JcrDocument.VARIANT_UNPUBLISHED);
        when(unpublished.getProperty(JcrDocument.HIPPOSTD_STATE)).thenReturn(unpublishedProp);
        when(unpublished.isNodeType(JcrDocument.HIPPOSTD_PUBLISHABLE)).thenReturn(true);

        NodeIterator variantIterator = createNodeIterator(unpublished);
        when(mockHandle.getNodes()).thenReturn(variantIterator);

        assertNull(document.variantMap);
        assertTrue(document.isNodeType("type1"));
        assertNotNull(document.variantMap);
    }

    @Test
    public void getContainingFolder_parentIsFolder() throws Exception {
        // When the parent is a folder it is returned
        HippoNode parentFolder = mock(HippoNode.class);
        when(parentFolder.isNodeType(eq(HippoStdNodeType.NT_FOLDER))).thenReturn(true);
        when(mockHandle.getParent()).thenReturn(parentFolder);

        Node folder = document.getContainingFolder();

        assertSame(parentFolder, folder);
    }

    @Test
    public void getContainingFolder_parentIsNull() throws Exception{
        // When the parent is null an IllegalStateException is thrown
        when(mockHandle.getParent()).thenReturn(null);

        assertThrows(IllegalStateException.class, () -> document.getContainingFolder());
    }

    @Test
    public void getContainingFolder_parentIsNotFolder() throws Exception {
        // When the parent is not a folder an IllegalStateException is thrown
        HippoNode parentFolder = mock(HippoNode.class);
        when(parentFolder.isNodeType(eq(HippoStdNodeType.NT_FOLDER))).thenReturn(false);
        when(mockHandle.getParent()).thenReturn(parentFolder);

        assertThrows(IllegalStateException.class, () -> document.getContainingFolder());
    }

    @Test
    public void idDraftBeingEdited_null() throws Exception {
        NodeIterator variantIterator = createNodeIterator();
        when(mockHandle.getNodes()).thenReturn(variantIterator);

        assertFalse(document.isDraftBeingEdited());
    }

    @Test
    public void isDraftBeingEdited_isBeingEdited() throws Exception {
        HippoNode draft = mock(HippoNode.class);
        Property draftProp = mock(Property.class);
        when(draftProp.getString()).thenReturn(JcrDocument.VARIANT_DRAFT);
        when(draft.getProperty(JcrDocument.HIPPOSTD_STATE)).thenReturn(draftProp);
        when(draft.isNodeType(JcrDocument.HIPPOSTD_PUBLISHABLE)).thenReturn(true);
        when(draft.hasProperty(eq(HippoStdNodeType.HIPPOSTD_HOLDER))).thenReturn(true);

        NodeIterator variantIterator = createNodeIterator(draft);
        when(mockHandle.getNodes()).thenReturn(variantIterator);

        assertTrue(document.isDraftBeingEdited());
    }

    @Test
    public void isDraftBeingEdited_notBeingEdited() throws Exception {
        HippoNode draft = mock(HippoNode.class);
        Property draftProp = mock(Property.class);
        when(draftProp.getString()).thenReturn(JcrDocument.VARIANT_DRAFT);
        when(draft.getProperty(JcrDocument.HIPPOSTD_STATE)).thenReturn(draftProp);
        when(draft.isNodeType(JcrDocument.HIPPOSTD_PUBLISHABLE)).thenReturn(true);
        when(draft.hasProperty(eq(HippoStdNodeType.HIPPOSTD_HOLDER))).thenReturn(false);

        NodeIterator variantIterator = createNodeIterator(draft);
        when(mockHandle.getNodes()).thenReturn(variantIterator);

        assertFalse(document.isDraftBeingEdited());
    }

    private NodeIterator createNodeIterator(Node... nodes) {
        List<Node> nodeList = Arrays.asList(nodes);
        final Iterator<Node> nodeIterator = nodeList.iterator();
        NodeIterator mockNodeIterator = mock(NodeIterator.class);
        when(mockNodeIterator.hasNext()).thenAnswer(new Answer<Boolean>() {
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
}
