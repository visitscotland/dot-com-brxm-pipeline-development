package com.visitscotland.brxm.translation.plugin;

import com.visitscotland.brxm.translation.SessionFactory;
import org.hippoecm.frontend.translation.ILocaleProvider;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.Session;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DocumentTranslatorAddTranslationLinkChangeSets {
    private DocumentTranslator documentTranslator;
    @Mock
    private Session mockSession;
    @Mock
    HippoBean sourceDocument;
    @Mock
    Node mockSourceNode;
    @Mock
    ILocaleProvider.HippoLocale mockLocale;
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
        when(sourceDocument.getNode()).thenReturn(mockSourceNode);
        documentTranslator = spy(new DocumentTranslator(mockNodeFactory, mockSessionFactory, mockJcrDocumentFactory, mockChangeSetFactory));
        lenient().doReturn(mockSession).when(mockSessionFactory).getJcrSession();
    }

    @Test
    public void noTranslatableChildren() throws Exception {
        // When the sourceDocument has no translation children, nothing is added to the List.
        doReturn(Collections.emptyList()).when(documentTranslator).getChildTranslatableLinkNodes(same(mockSourceNode));

        List<ChangeSet> changeSetList = new ArrayList<>();
        documentTranslator.addTranslationLinkChangeSets(sourceDocument, mockLocale, changeSetList);

        assertTrue(changeSetList.isEmpty());
    }

    @Test
    public void withTranslatableChildren_hasTranslation() throws Exception {
        // When the translatable children are already translated they should not be added to the list
        Node translatableChild = mock(Node.class);
        Property docbaseProp = mock(Property.class);
        when(docbaseProp.getString()).thenReturn("node1uuid");
        when(translatableChild.getProperty(eq("hippo:docbase"))).thenReturn(docbaseProp);

        Node node1 = mock(Node.class);
        when(mockSession.getNodeByIdentifier("node1uuid")).thenReturn(node1);
        JcrDocument node1jcr = mock(JcrDocument.class);
        doReturn(node1jcr).when(mockJcrDocumentFactory).createFromNode(same(node1));
        when(node1jcr.hasTranslation(same(mockLocale))).thenReturn(true);

        List<Node> translatableLinkNodes = new ArrayList<>();
        translatableLinkNodes.add(translatableChild);
        doReturn(translatableLinkNodes).when(documentTranslator).getChildTranslatableLinkNodes(same(mockSourceNode));

        List<ChangeSet> changeSetList = new ArrayList<>();
        documentTranslator.addTranslationLinkChangeSets(sourceDocument, mockLocale, changeSetList);

        assertTrue(changeSetList.isEmpty());
    }

    @Test
    public void withTranslatableChildren_noTranslation_noExistingChangeSets() throws Exception {
        // When the translatable children are not already translated they should be added to the list
        Node translatableChild = mock(Node.class);
        Property docbaseProp = mock(Property.class);
        when(docbaseProp.getString()).thenReturn("node1uuid");
        when(translatableChild.getProperty(eq("hippo:docbase"))).thenReturn(docbaseProp);

        // This is the document that the child link points to
        Node node1 = mock(Node.class);
        when(mockSession.getNodeByIdentifier("node1uuid")).thenReturn(node1);
        JcrDocument node1jcr = mock(JcrDocument.class);
        doReturn(node1jcr).when(mockJcrDocumentFactory).createFromNode(same(node1));
        when(node1jcr.hasTranslation(same(mockLocale))).thenReturn(false);

        List<Node> translatableLinkNodes = new ArrayList<>();
        translatableLinkNodes.add(translatableChild);
        doReturn(translatableLinkNodes).when(documentTranslator).getChildTranslatableLinkNodes(same(mockSourceNode));

        ChangeSet mockChangeSet = mock(ChangeSet.class);
        doReturn(mockChangeSet).when(mockChangeSetFactory).createChangeSet(same(mockLocale));

        List<ChangeSet> changeSetList = new ArrayList<>();
        documentTranslator.addTranslationLinkChangeSets(sourceDocument, mockLocale, changeSetList);

        assertFalse(changeSetList.isEmpty());
        verify(mockChangeSet).addDocument(same(node1jcr), eq(true));
        assertSame(mockChangeSet, changeSetList.get(0));
    }

    @Test
    public void withTranslatableChildren_noTranslation_withExistingMatchingChangeSet() throws Exception {
        // When the translatable children are not already translated
        // and the url for the document already has a ChangeSet
        // the document should be added to the existing ChangeSet
        Node translatableChild = mock(Node.class);
        Property docbaseProp = mock(Property.class);
        when(docbaseProp.getString()).thenReturn("node1uuid");
        when(translatableChild.getProperty(eq("hippo:docbase"))).thenReturn(docbaseProp);

        // This is the document that the child link points to
        Node node1 = mock(Node.class);
        when(mockSession.getNodeByIdentifier("node1uuid")).thenReturn(node1);
        JcrDocument node1jcr = mock(JcrDocument.class);
        doReturn(node1jcr).when(mockJcrDocumentFactory).createFromNode(same(node1));
        when(node1jcr.hasTranslation(same(mockLocale))).thenReturn(false);
        Node node1unpublished = mock(Node.class);
        when(node1jcr.getVariantNode(eq(JcrDocument.VARIANT_UNPUBLISHED))).thenReturn(node1unpublished);

        List<Node> translatableLinkNodes = new ArrayList<>();
        translatableLinkNodes.add(translatableChild);
        doReturn(translatableLinkNodes).when(documentTranslator).getChildTranslatableLinkNodes(same(mockSourceNode));

        ChangeSet mockChangeSet = mock(ChangeSet.class);
        when(mockChangeSet.getTargetPath()).thenReturn("/matching");

        doReturn(mockChangeSet).when(mockChangeSetFactory).createChangeSet(same(mockLocale));

        List<ChangeSet> changeSetList = new ArrayList<>();
        ChangeSet doesntMatchPath = mock(ChangeSet.class);
        when(doesntMatchPath.getTargetPath()).thenReturn("/notMatching");
        ChangeSet existingChangeSet = mock(ChangeSet.class);
        when(existingChangeSet.getTargetPath()).thenReturn("/matching");
        when(existingChangeSet.containsDocumentMatchingUrl(any())).thenReturn(false);
        changeSetList.add(doesntMatchPath);
        changeSetList.add(existingChangeSet);
        documentTranslator.addTranslationLinkChangeSets(sourceDocument, mockLocale, changeSetList);

        verify(existingChangeSet).addDocument(same(node1jcr), eq(true));
        assertEquals(2, changeSetList.size());
        assertSame(existingChangeSet, changeSetList.get(1));
    }

    @Test
    public void withTranslatableChildren_noTranslation_withExistingMatchingChangeSet_documentAlreadyAdded() throws Exception {
        // When the translatable children are not already translated
        // and the url for the document already has a ChangeSet
        // and the document has already been added to the ChangeSet, so should not be added again
        Node translatableChild = mock(Node.class);
        Property docbaseProp = mock(Property.class);
        when(docbaseProp.getString()).thenReturn("node1uuid");
        when(translatableChild.getProperty(eq("hippo:docbase"))).thenReturn(docbaseProp);

        // This is the document that the child link points to
        Node node1 = mock(Node.class);
        when(mockSession.getNodeByIdentifier("node1uuid")).thenReturn(node1);
        JcrDocument node1jcr = mock(JcrDocument.class);
        doReturn(node1jcr).when(mockJcrDocumentFactory).createFromNode(same(node1));
        when(node1jcr.hasTranslation(same(mockLocale))).thenReturn(false);
        Node node1unpublished = mock(Node.class);
        when(node1jcr.getVariantNode(eq(JcrDocument.VARIANT_UNPUBLISHED))).thenReturn(node1unpublished);

        List<Node> translatableLinkNodes = new ArrayList<>();
        translatableLinkNodes.add(translatableChild);
        doReturn(translatableLinkNodes).when(documentTranslator).getChildTranslatableLinkNodes(same(mockSourceNode));

        ChangeSet mockChangeSet = mock(ChangeSet.class);
        when(mockChangeSet.getTargetPath()).thenReturn("/matching");

        doReturn(mockChangeSet).when(mockChangeSetFactory).createChangeSet(same(mockLocale));

        List<ChangeSet> changeSetList = new ArrayList<>();
        ChangeSet doesntMatchPath = mock(ChangeSet.class);
        when(doesntMatchPath.getTargetPath()).thenReturn("/notMatching");
        ChangeSet existingChangeSet = mock(ChangeSet.class);
        when(existingChangeSet.getTargetPath()).thenReturn("/matching");
        when(existingChangeSet.containsDocumentMatchingUrl(any())).thenReturn(true);
        changeSetList.add(doesntMatchPath);
        changeSetList.add(existingChangeSet);
        documentTranslator.addTranslationLinkChangeSets(sourceDocument, mockLocale, changeSetList);

        verify(existingChangeSet, never()).addDocument(same(node1jcr), eq(true));
        assertEquals(2, changeSetList.size());
        assertSame(existingChangeSet, changeSetList.get(1));
    }

}
