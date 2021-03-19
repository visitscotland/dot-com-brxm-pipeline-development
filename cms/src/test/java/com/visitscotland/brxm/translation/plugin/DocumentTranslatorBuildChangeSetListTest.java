package com.visitscotland.brxm.translation.plugin;

import com.visitscotland.brxm.hippobeans.Itinerary;
import com.visitscotland.brxm.translation.SessionFactory;
import org.hippoecm.frontend.translation.ILocaleProvider;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.repository.api.HippoNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DocumentTranslatorBuildChangeSetListTest {
    public static final String PAGE_TYPE = "visitscotland:Page";
    private DocumentTranslator documentTranslator;
    @Mock
    private HippoNode mockSourceNode;
    @Mock
    private JcrDocument mockSourceDocument;
    @Mock
    private ILocaleProvider.HippoLocale mockItalianLocale;
    private ChangeSet mockItalianChangeSet;
    @Mock
    private ILocaleProvider.HippoLocale mockSpanishLocale;
    private ChangeSet mockSpanishChangeSet;
    @Mock
    private ILocaleProvider.HippoLocale mockFrenchLocale;
    private ChangeSet mockFrenchChangeSet;

    private List<ILocaleProvider.HippoLocale> targetLocaleList;
    @Mock
    private HippoTranslatedNodeFactory mockNodeFactory;
    @Mock
    private SessionFactory mockSessionFactory;
    @Mock
    private JcrDocumentFactory mockJcrDocumentFactory;
    @Mock
    private ChangeSetFactory mockChangeSetFactory;

    @BeforeEach
    public void beforeEach() throws Exception {
        targetLocaleList = new ArrayList<>();
        targetLocaleList.add(mockItalianLocale);
        targetLocaleList.add(mockSpanishLocale);
        targetLocaleList.add(mockFrenchLocale);

        documentTranslator = spy(new DocumentTranslator(mockNodeFactory, mockSessionFactory, mockJcrDocumentFactory,
                mockChangeSetFactory));
        mockItalianChangeSet = createMockChangeSet();
        lenient().doReturn(mockItalianChangeSet).when(mockChangeSetFactory).createChangeSet(same(mockItalianLocale));

        mockSpanishChangeSet = createMockChangeSet();
        lenient().doReturn(mockSpanishChangeSet).when(mockChangeSetFactory).createChangeSet(same(mockSpanishLocale));

        mockFrenchChangeSet = createMockChangeSet();
        lenient().doReturn(mockFrenchChangeSet).when(mockChangeSetFactory).createChangeSet(same(mockFrenchLocale));

        lenient().doReturn(mockSourceDocument).when(mockJcrDocumentFactory).createFromNode(same(mockSourceNode));

    }

    @Test
    public void buildDocumentChangeSetList_emptyTargetLocaleList() throws Exception {
        // When the target locale list passed to the method is empty
        // it should return an empty ChangeSet not cause an exception
        List<ChangeSet> result = documentTranslator.buildChangeSetList(mockSourceNode, Collections.emptyList());

        assertTrue(result.isEmpty());
    }

    @Test
    public void buildDocumentChangeSetList_sourceDocumentTranslated_notPageType() throws Exception {
        // The source document is already translated into every target locale and is not a Page type
        // and contains no hippo:mirror children
        // Should return an empty ChangeSet list
        when(mockSourceDocument.hasTranslation(same(mockItalianLocale))).thenReturn(true);
        when(mockSourceDocument.hasTranslation(same(mockFrenchLocale))).thenReturn(true);
        when(mockSourceDocument.hasTranslation(same(mockSpanishLocale))).thenReturn(true);

        HippoBean mockHippoBean = mock(HippoBean.class);
        when(mockSourceDocument.asHippoBean()).thenReturn(mockHippoBean);
        when(mockSourceDocument.isNodeType(eq(PAGE_TYPE))).thenReturn(false);
        when(mockHippoBean.getNode()).thenReturn(mockSourceNode);

        NodeIterator emptyIterator = createNodeIterator();
        when(mockSourceNode.getNodes()).thenReturn(emptyIterator);

        List<ChangeSet> result = documentTranslator.buildChangeSetList(mockSourceNode, targetLocaleList);

        assertTrue(result.isEmpty());

        verify(mockItalianChangeSet).populateFolders(same(mockSourceDocument));
        verify(mockFrenchChangeSet).populateFolders(same(mockSourceDocument));
        verify(mockSpanishChangeSet).populateFolders(same(mockSourceDocument));

        verify(documentTranslator, times(3)).addTranslationLinkChangeSets(same(mockHippoBean), any(ILocaleProvider.HippoLocale.class), anyList());
        verify(documentTranslator, times(3)).addTranslationLinkChangeSets(any(), any(ILocaleProvider.HippoLocale.class), anyList());
    }

    @Test
    public void buildDocumentChangeSetList_sourceDocumentHasMissingLocales_notPageType() throws Exception {
        // The source document has missing locale translations, is not a Page type,
        // With no nested hippo:mirror nodes.
        // Should return a ChangeSet for each missing locale
        when(mockSourceDocument.hasTranslation(same(mockItalianLocale))).thenReturn(true);
        when(mockSourceDocument.hasTranslation(same(mockFrenchLocale))).thenReturn(false);
        when(mockSourceDocument.hasTranslation(same(mockSpanishLocale))).thenReturn(false);

        HippoBean mockHippoBean = mock(HippoBean.class);
        when(mockSourceDocument.asHippoBean()).thenReturn(mockHippoBean);
        when(mockSourceDocument.isNodeType(eq(PAGE_TYPE))).thenReturn(false);
        when(mockHippoBean.getNode()).thenReturn(mockSourceNode);

        NodeIterator emptyIterator = createNodeIterator();
        when(mockSourceNode.getNodes()).thenReturn(emptyIterator);

        List<ChangeSet> result = documentTranslator.buildChangeSetList(mockSourceNode, targetLocaleList);

        assertEquals(2, result.size());

        verify(mockItalianChangeSet, never()).addDocument(any(JcrDocument.class), anyBoolean());
        verify(mockFrenchChangeSet).addDocument(same(mockSourceDocument), anyBoolean());
        verify(mockFrenchChangeSet, atMostOnce()).addDocument(any(JcrDocument.class), anyBoolean());
        verify(mockSpanishChangeSet).addDocument(same(mockSourceDocument), anyBoolean());
        verify(mockSpanishChangeSet, atMostOnce()).addDocument(any(JcrDocument.class), anyBoolean());

        verify(mockItalianChangeSet).populateFolders(same(mockSourceDocument));
        verify(mockFrenchChangeSet).populateFolders(same(mockSourceDocument));
        verify(mockSpanishChangeSet).populateFolders(same(mockSourceDocument));

        verify(documentTranslator, times(3)).addTranslationLinkChangeSets(same(mockHippoBean), any(ILocaleProvider.HippoLocale.class), anyList());
        verify(documentTranslator, times(3)).addTranslationLinkChangeSets(any(), any(ILocaleProvider.HippoLocale.class), anyList());
    }

    @Test
    public void buildDocumentChangeSetList_sourceDocumentTranslated_PageType_TranslationParent_noChildren() throws Exception {
        // The source document is already translated into every target locale, is a Page type and a TranslationParent
        // The source document has no siblings
        // Should return an empty ChangeSet list
        when(mockSourceDocument.hasTranslation(same(mockItalianLocale))).thenReturn(true);
        when(mockSourceDocument.hasTranslation(same(mockFrenchLocale))).thenReturn(true);
        when(mockSourceDocument.hasTranslation(same(mockSpanishLocale))).thenReturn(true);

        Itinerary mockHippoBean = mock(Itinerary.class);
        when(mockHippoBean.getChildJcrTypes()).thenReturn(new String[] {"type1", "type2"});
        when(mockHippoBean.getNode()).thenReturn(mockSourceNode);

        NodeIterator emptyIterator = createNodeIterator();
        when(mockSourceNode.getNodes()).thenReturn(emptyIterator);

        Node mockContainingFolder = mock(Node.class);
        NodeIterator mockNodeIterator = mock(NodeIterator.class);
        when(mockContainingFolder.getNodes()).thenReturn(mockNodeIterator);
        when(mockNodeIterator.hasNext()).thenReturn(false);

        when(mockSourceDocument.getContainingFolder()).thenReturn(mockContainingFolder);
        when(mockSourceDocument.asHippoBean()).thenReturn(mockHippoBean);
        when(mockSourceDocument.isNodeType(eq(PAGE_TYPE))).thenReturn(true);

        List<ChangeSet> result = documentTranslator.buildChangeSetList(mockSourceNode, targetLocaleList);

        assertTrue(result.isEmpty());

        verify(mockItalianChangeSet).populateFolders(same(mockSourceDocument));
        verify(mockFrenchChangeSet).populateFolders(same(mockSourceDocument));
        verify(mockSpanishChangeSet).populateFolders(same(mockSourceDocument));

        verify(documentTranslator, times(3)).addTranslationLinkChangeSets(same(mockHippoBean), any(ILocaleProvider.HippoLocale.class), anyList());
        verify(documentTranslator, times(3)).addTranslationLinkChangeSets(any(), any(ILocaleProvider.HippoLocale.class), anyList());
    }

    @Test
    public void buildDocumentChangeSetList_sourceDocumentTranslated_PageType_TranslationParent_withNonMatchingChildren() throws Exception {
        // The source document is already translated into every target locale, is a Page type and a TranslationParent
        // The source document has siblings but they do not match any of the JCR types of the TranslationParent
        // The siblings also contain folders and other types that are not hippo:handle or hippo:translated
        // Should return an empty ChangeSet list
        // The source document is already translated into every target locale, is a Page type and a TranslationParent
        // The source document has no siblings that match the TranslationParent child types
        // Should return an empty ChangeSet list
        when(mockSourceDocument.hasTranslation(same(mockItalianLocale))).thenReturn(true);
        when(mockSourceDocument.hasTranslation(same(mockFrenchLocale))).thenReturn(true);
        when(mockSourceDocument.hasTranslation(same(mockSpanishLocale))).thenReturn(true);

        Itinerary mockHippoBean = mock(Itinerary.class);
        when(mockHippoBean.getNode()).thenReturn(mockSourceNode);

        NodeIterator emptyIterator = createNodeIterator();
        when(mockSourceNode.getNodes()).thenReturn(emptyIterator);

        HippoNode mockFolderType = createMockSibling("hippostd:folder");
        HippoNode mockOtherType = createMockSibling("some:other");
        HippoNode mockHandleType = createMockSibling(JcrDocument.HIPPO_HANDLE);
        HippoNode mockTranslatedType = createMockSibling(JcrDocument.HIPPO_TRANSLATED);

        JcrDocument mockHandleDocument = mock(JcrDocument.class);
        doReturn(mockHandleDocument).when(mockJcrDocumentFactory).createFromNode(same(mockHandleType));
        when(mockHandleDocument.isNodeType(ArgumentMatchers.<String>any())).thenReturn(false);

        JcrDocument mockTranslatedDocument = mock(JcrDocument.class);
        doReturn(mockTranslatedDocument).when(mockJcrDocumentFactory).createFromNode(same(mockTranslatedType));
        when(mockTranslatedDocument.isNodeType(ArgumentMatchers.<String>any())).thenReturn(false);

        HippoNode mockContainingFolder = mock(HippoNode.class);
        when(mockContainingFolder.getNodes()).thenAnswer(new Answer<NodeIterator>() {
            @Override
            public NodeIterator answer(InvocationOnMock invocation) throws Throwable {
                return createNodeIterator(mockFolderType, mockOtherType, mockHandleType,
                        mockTranslatedType);
            }
        });

        when(mockSourceDocument.getContainingFolder()).thenReturn(mockContainingFolder);
        when(mockSourceDocument.asHippoBean()).thenReturn(mockHippoBean);
        when(mockSourceDocument.isNodeType(eq(PAGE_TYPE))).thenReturn(true);

        List<ChangeSet> result = documentTranslator.buildChangeSetList(mockSourceNode, targetLocaleList);

        assertTrue(result.isEmpty());

        verify(mockItalianChangeSet).populateFolders(same(mockSourceDocument));
        verify(mockFrenchChangeSet).populateFolders(same(mockSourceDocument));
        verify(mockSpanishChangeSet).populateFolders(same(mockSourceDocument));

        // Called only for the main document
        verify(documentTranslator, times(3)).addTranslationLinkChangeSets(same(mockHippoBean), any(ILocaleProvider.HippoLocale.class), anyList());
        verify(documentTranslator, times(3)).addTranslationLinkChangeSets(any(HippoBean.class), any(ILocaleProvider.HippoLocale.class), anyList());
    }

    @Test
    public void buildDocumentChangeSetList_sourceDocumentTranslated_PageType_TranslationParent_withMatchingChildren_allTranslated() throws Exception {
        // The source document is already translated into every target locale, is a Page type and a TranslationParent
        // The source document has siblings they match one of the JCR types of the TranslationParent
        // and are already translated.
        // The siblings also contain folders and other types that are not hippo:handle or hippo:translated
        // Should return an empty ChangeSet list
        // Verify that the addTranslationLinkChangeSets is called for each of the siblings,
        // even though they are already translated because their links might not be.
        when(mockSourceDocument.hasTranslation(same(mockItalianLocale))).thenReturn(true);
        when(mockSourceDocument.hasTranslation(same(mockFrenchLocale))).thenReturn(true);
        when(mockSourceDocument.hasTranslation(same(mockSpanishLocale))).thenReturn(true);

        Itinerary mockHippoBean = mock(Itinerary.class);
        when(mockHippoBean.getChildJcrTypes()).thenReturn(new String[] {"type1", "type2"});
        when(mockHippoBean.getNode()).thenReturn(mockSourceNode);

        NodeIterator emptyIterator = createNodeIterator();
        when(mockSourceNode.getNodes()).thenReturn(emptyIterator);

        HippoNode mockFolderType = createMockSibling("hippostd:folder");
        HippoNode mockOtherType = createMockSibling("some:other");
        HippoNode mockHandleType = createMockSibling(JcrDocument.HIPPO_HANDLE);
        HippoNode mockTranslatedType = createMockSibling(JcrDocument.HIPPO_TRANSLATED);
        HippoNode mockTranslationSibling = createMockSibling(JcrDocument.HIPPO_TRANSLATED);

        JcrDocument mockHandleDocument = mock(JcrDocument.class);
        doReturn(mockHandleDocument).when(mockJcrDocumentFactory).createFromNode(same(mockHandleType));
        when(mockHandleDocument.isNodeType(ArgumentMatchers.<String>any())).thenReturn(false);

        JcrDocument mockTranslatedDocument = mock(JcrDocument.class);
        doReturn(mockTranslatedDocument).when(mockJcrDocumentFactory).createFromNode(same(mockTranslatedType));
        when(mockTranslatedDocument.isNodeType(ArgumentMatchers.<String>any())).thenReturn(false);

        JcrDocument mockTranslationSiblingDocument = mock(JcrDocument.class);
        Node mockTranslationSiblingNode = mock(Node.class);
        doReturn(mockTranslationSiblingDocument)
                .when(mockJcrDocumentFactory).createFromNode(same(mockTranslationSibling));
        when(mockTranslationSiblingDocument.isNodeType(eq("type1"), eq("type2"))).thenReturn(true);
        when(mockTranslationSiblingDocument.hasTranslation(any(ILocaleProvider.HippoLocale.class))).thenReturn(true);
        HippoBean mockTranslationSiblingHippoBean = mock(HippoBean.class);
        when(mockTranslationSiblingDocument.asHippoBean()).thenReturn(mockTranslationSiblingHippoBean);
        when(mockTranslationSiblingHippoBean.getNode()).thenReturn(mockTranslationSiblingNode);

        NodeIterator emptySiblingIterator = createNodeIterator();
        when(mockTranslationSiblingNode.getNodes()).thenReturn(emptySiblingIterator);

        HippoNode mockContainingFolder = mock(HippoNode.class);
        when(mockContainingFolder.getNodes()).thenAnswer(new Answer<NodeIterator>() {
            @Override
            public NodeIterator answer(InvocationOnMock invocation) throws Throwable {
                return createNodeIterator(mockFolderType, mockOtherType, mockHandleType,
                        mockTranslatedType, mockTranslationSibling);
            }
        });

        when(mockSourceDocument.getContainingFolder()).thenReturn(mockContainingFolder);
        when(mockSourceDocument.asHippoBean()).thenReturn(mockHippoBean);
        when(mockSourceDocument.isNodeType(eq(PAGE_TYPE))).thenReturn(true);

        List<ChangeSet> result = documentTranslator.buildChangeSetList(mockSourceNode, targetLocaleList);

        assertTrue(result.isEmpty());

        verify(mockItalianChangeSet).populateFolders(same(mockSourceDocument));
        verify(mockFrenchChangeSet).populateFolders(same(mockSourceDocument));
        verify(mockSpanishChangeSet).populateFolders(same(mockSourceDocument));

        // Each locale for the main document
        verify(documentTranslator, times(3)).addTranslationLinkChangeSets(same(mockHippoBean), any(ILocaleProvider.HippoLocale.class), anyList());
        // and once for each targetLocale for the sibling
        verify(documentTranslator, times(3)).addTranslationLinkChangeSets(same(mockTranslationSiblingHippoBean), any(ILocaleProvider.HippoLocale.class), anyList());
        verify(documentTranslator, times(6)).addTranslationLinkChangeSets(any(), any(ILocaleProvider.HippoLocale.class), anyList());
    }

    @Test
    public void buildDocumentChangeSetList_sourceDocumentTranslated_PageType_TranslationParent_withMatchingChildren_missingLocales() throws Exception {
        // The source document is already translated into every target locale, is a Page type and a TranslationParent.
        // The source document has siblings they match one of the JCR types of the TranslationParent
        // and have missing locales.
        // The siblings also contain folders and other types that are not hippo:handle or hippo:translated
        // Should return a ChangeSet list for each sibling missing locale and the sibling
        // documents should be in the relevant ChangeSet
        when(mockSourceDocument.hasTranslation(same(mockItalianLocale))).thenReturn(true);
        when(mockSourceDocument.hasTranslation(same(mockFrenchLocale))).thenReturn(true);
        when(mockSourceDocument.hasTranslation(same(mockSpanishLocale))).thenReturn(true);

        Itinerary mockHippoBean = mock(Itinerary.class);
        when(mockHippoBean.getChildJcrTypes()).thenReturn(new String[] {"type1", "type2"});
        when(mockHippoBean.getNode()).thenReturn(mockSourceNode);

        NodeIterator emptyIterator = createNodeIterator();
        when(mockSourceNode.getNodes()).thenReturn(emptyIterator);

        HippoNode mockFolderType = createMockSibling("hippostd:folder");
        HippoNode mockOtherType = createMockSibling("some:other");
        HippoNode mockHandleType = createMockSibling(JcrDocument.HIPPO_HANDLE);
        HippoNode mockTranslatedType = createMockSibling(JcrDocument.HIPPO_TRANSLATED);
        HippoNode mockTranslationSibling = createMockSibling(JcrDocument.HIPPO_TRANSLATED);

        JcrDocument mockHandleDocument = mock(JcrDocument.class);
        doReturn(mockHandleDocument).when(mockJcrDocumentFactory).createFromNode(same(mockHandleType));
        when(mockHandleDocument.isNodeType(ArgumentMatchers.<String>any())).thenReturn(false);

        JcrDocument mockTranslatedDocument = mock(JcrDocument.class);
        doReturn(mockTranslatedDocument).when(mockJcrDocumentFactory).createFromNode(same(mockTranslatedType));
        when(mockTranslatedDocument.isNodeType(ArgumentMatchers.<String>any())).thenReturn(false);

        JcrDocument mockTranslationSiblingDocument = mock(JcrDocument.class);
        Node mockTranslationSiblingNode = mock(Node.class);
        doReturn(mockTranslationSiblingDocument)
                .when(mockJcrDocumentFactory).createFromNode(same(mockTranslationSibling));
        when(mockTranslationSiblingDocument.isNodeType(eq("type1"), eq("type2"))).thenReturn(true);
        when(mockTranslationSiblingDocument.hasTranslation(same(mockItalianLocale))).thenReturn(false);
        when(mockTranslationSiblingDocument.hasTranslation(same(mockFrenchLocale))).thenReturn(false);
        when(mockTranslationSiblingDocument.hasTranslation(same(mockSpanishLocale))).thenReturn(true);
        HippoBean mockTranslationSiblingHippoBean = mock(HippoBean.class);
        when(mockTranslationSiblingDocument.asHippoBean()).thenReturn(mockTranslationSiblingHippoBean);
        when(mockTranslationSiblingHippoBean.getNode()).thenReturn(mockTranslationSiblingNode);

        NodeIterator emptySiblingIterator = createNodeIterator();
        when(mockTranslationSiblingNode.getNodes()).thenReturn(emptySiblingIterator);

        HippoNode mockContainingFolder = mock(HippoNode.class);
        when(mockContainingFolder.getNodes()).thenAnswer(new Answer<NodeIterator>() {
            @Override
            public NodeIterator answer(InvocationOnMock invocation) throws Throwable {
                return createNodeIterator(mockFolderType, mockOtherType, mockHandleType,
                        mockTranslatedType, mockTranslationSibling);
            }
        });

        when(mockSourceDocument.getContainingFolder()).thenReturn(mockContainingFolder);
        when(mockSourceDocument.asHippoBean()).thenReturn(mockHippoBean);
        when(mockSourceDocument.isNodeType(eq(PAGE_TYPE))).thenReturn(true);

        List<ChangeSet> result = documentTranslator.buildChangeSetList(mockSourceNode, targetLocaleList);

        assertEquals(2, result.size());

        verify(mockSpanishChangeSet, never()).addDocument(any(JcrDocument.class), anyBoolean());
        verify(mockFrenchChangeSet).addDocument(same(mockTranslationSiblingDocument), anyBoolean());
        verify(mockFrenchChangeSet, atMostOnce()).addDocument(any(JcrDocument.class), anyBoolean());
        verify(mockItalianChangeSet).addDocument(same(mockTranslationSiblingDocument), anyBoolean());
        verify(mockItalianChangeSet, atMostOnce()).addDocument(any(JcrDocument.class), anyBoolean());

        verify(mockItalianChangeSet).populateFolders(same(mockSourceDocument));
        verify(mockFrenchChangeSet).populateFolders(same(mockSourceDocument));
        verify(mockSpanishChangeSet).populateFolders(same(mockSourceDocument));

        // Each locale for the main document
        verify(documentTranslator, times(3)).addTranslationLinkChangeSets(same(mockHippoBean), any(ILocaleProvider.HippoLocale.class), anyList());
        // and once for each targetLocale for the sibling
        verify(documentTranslator, times(3)).addTranslationLinkChangeSets(same(mockTranslationSiblingHippoBean), any(ILocaleProvider.HippoLocale.class), anyList());
        verify(documentTranslator, times(6)).addTranslationLinkChangeSets(any(), any(ILocaleProvider.HippoLocale.class), anyList());
    }

    private ChangeSet createMockChangeSet() throws Exception {
        ChangeSet mockChangeSet = mock(ChangeSet.class);
        final List<JcrDocument> documentList = new ArrayList<>();
        lenient().doAnswer(new Answer<List<JcrDocument>>() {
            @Override
            public List<JcrDocument> answer(InvocationOnMock invocation) throws Throwable {
                return documentList;
            }
        }).when(mockChangeSet).getDocuments();
        lenient().doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                documentList.add(invocation.getArgument(0));
                return null;
            }
        }).when(mockChangeSet).addDocument(any(JcrDocument.class), anyBoolean());
        return mockChangeSet;
    }

    private NodeIterator createNodeIterator(Node... nodes) {
        List<Node> nodeList = Arrays.asList(nodes);
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

    private HippoNode createMockSibling(String... matchingTypes) throws Exception {
        HippoNode mockNode = mock(HippoNode.class);
        lenient().when(mockNode.isNodeType(anyString())).thenReturn(false);
        for (String matchingType : matchingTypes) {
            lenient().when(mockNode.isNodeType(eq(matchingType))).thenReturn(true);
        }
        return mockNode;
    }
}
