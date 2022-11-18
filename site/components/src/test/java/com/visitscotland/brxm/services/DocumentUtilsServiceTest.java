package com.visitscotland.brxm.services;

import com.visitscotland.brxm.hippobeans.*;
import com.visitscotland.brxm.model.LocalizedURL;
import com.visitscotland.brxm.utils.ContentLogger;
import com.visitscotland.brxm.utils.HippoUtilsService;
import com.visitscotland.brxm.utils.Language;
import com.visitscotland.brxm.utils.Properties;
import org.apache.jackrabbit.commons.iterator.NodeIteratorAdapter;
import org.hippoecm.hst.content.beans.ObjectBeanManagerException;
import org.hippoecm.hst.content.beans.query.exceptions.QueryException;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.hippoecm.hst.mock.core.component.MockHstRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DocumentUtilsServiceTest {

    private final static int AVAILABLE_LANGUAGES = 6;

    @Mock
    HippoUtilsService utils;
    @Mock
    ResourceBundleService bundle;
    @Mock
    Properties properties;
    @Mock
    ContentLogger logger;

    @InjectMocks
    DocumentUtilsService documentUtils;

    private void mockPage(Page page, Node... documents) throws RepositoryException {
        List<Node> nodes = new ArrayList(Arrays.asList(documents));
        nodes.add(page.getNode());

        NodeIterator it = new NodeIteratorAdapter(nodes.iterator());

        when(page.getNode().getParent().getParent().getNodes()).thenReturn(it);
        when(page.getNode().getNodes().getSize()).thenReturn(1L);
        when(page.getNode().getNodes().nextNode().getProperty(DocumentUtilsService.DOCUMENT_TYPE).getString()).thenReturn("visitscotland:Page");


        for (Node doc : documents) {
            when(doc.getNodes().getSize()).thenReturn(1L);
        }
    }

    @Test
    @DisplayName("getAllowedDocuments() - The page doesn't have any children")
    void getAllowedDocuments_noChildren() throws RepositoryException {
        Page page = mock(Page.class, withSettings().defaultAnswer(Mockito.RETURNS_DEEP_STUBS));
        doCallRealMethod().when(page).getChildJcrTypes();

        mockPage(page);

        List documents = documentUtils.getAllowedDocuments(page);
        assertEquals(0, documents.size());
    }

    @Test
    @DisplayName("getAllowedDocuments() - The page has one not allowed child")
    void getAllowedDocuments_notAllowedChildren() throws RepositoryException {
        Page page = mock(Page.class, withSettings().defaultAnswer(Mockito.RETURNS_DEEP_STUBS));
        doCallRealMethod().when(page).getChildJcrTypes();

        Node listicleItemNode = mock(Node.class, withSettings().defaultAnswer(Mockito.RETURNS_DEEP_STUBS));
        when(listicleItemNode.getNodes().nextNode().getProperty(DocumentUtilsService.DOCUMENT_TYPE).getString()).thenReturn("visitscotland:ListicleItem");

        mockPage(page, listicleItemNode);

        List documents = documentUtils.getAllowedDocuments(page);
        assertEquals(0, documents.size());
    }

    @Test
    @DisplayName("getAllowedDocuments() - The page has one non-parseable document")
    void getAllowedDocuments_nonParseableChildren() throws RepositoryException {
        Page page = mock(Page.class, withSettings().defaultAnswer(Mockito.RETURNS_DEEP_STUBS));
        doCallRealMethod().when(page).getChildJcrTypes();

        Node listicleItemNode = mock(Node.class, withSettings().defaultAnswer(Mockito.RETURNS_DEEP_STUBS));
        when(listicleItemNode.getNodes().nextNode().getProperty(DocumentUtilsService.DOCUMENT_TYPE).getString()).thenReturn("hippo:document");

        mockPage(page, listicleItemNode);

        List documents = documentUtils.getAllowedDocuments(page);
        assertEquals(0, documents.size());
    }

    @Test
    @DisplayName("getAllowedDocuments() - The page has one allowed document")
    void getAllowedDocuments_allowedChild() throws RepositoryException, QueryException, ObjectBeanManagerException {
        Page page = mock(Listicle.class, withSettings().defaultAnswer(Mockito.RETURNS_DEEP_STUBS));
        doCallRealMethod().when(page).getChildJcrTypes();

        Node jcrNode = mock(Node.class, withSettings().defaultAnswer(Mockito.RETURNS_DEEP_STUBS));
        when(jcrNode.getNodes().nextNode().getProperty(DocumentUtilsService.DOCUMENT_TYPE).getString()).thenReturn("visitscotland:ListicleItem");

        mockPage(page, jcrNode);

        BaseDocument child = mock(BaseDocument.class);
        when(utils.getDocumentFromNode(jcrNode)).thenReturn(child);

        List documents = documentUtils.getAllowedDocuments(page);
        assertEquals(1, documents.size());
    }

    @Test
    @DisplayName("getSiblingDocuments() - The document skips not allowed types")
    void getSiblingDocuments_onlyAllowedChildren() throws RepositoryException, QueryException, ObjectBeanManagerException {
        Page page = mock(Listicle.class, withSettings().defaultAnswer(Mockito.RETURNS_DEEP_STUBS));

        //Allowed type
        ListicleItem item = mock(ListicleItem.class);
        Node listicleItemNode = mock(Node.class, withSettings().defaultAnswer(Mockito.RETURNS_DEEP_STUBS));
        when(listicleItemNode.getNodes().nextNode().getProperty(DocumentUtilsService.DOCUMENT_TYPE).getString()).thenReturn("visitscotland:ListicleItem");
        when(utils.getDocumentFromNode(listicleItemNode)).thenReturn(item);

        //Not allowed type
        Node dayNode = mock(Node.class, withSettings().defaultAnswer(Mockito.RETURNS_DEEP_STUBS));
        when(dayNode.getNodes().nextNode().getProperty(DocumentUtilsService.DOCUMENT_TYPE).getString()).thenReturn("visitscotland:ItineraryDay");

        mockPage(page, listicleItemNode, dayNode, listicleItemNode, dayNode);

        List<ListicleItem> documents = documentUtils.getSiblingDocuments(page, ListicleItem.class, "visitscotland:ListicleItem");
        assertEquals(2, documents.size());
    }

    private HstRequest prepareRequest(){
        MockHstRequest request = new MockHstRequest();
        HstRequestContext rc = mock(HstRequestContext.class, RETURNS_DEEP_STUBS);
        HippoBean document = mock(HippoBean.class, RETURNS_DEEP_STUBS);

        request.setRequestContext(rc);
        request.setLocale(Locale.UK);

        when(rc.getContentBean()).thenReturn(document);

        return request;
    }

    @Test
    @DisplayName("Get URLs from translated documents")
    void getLocales(){
        HstRequest request = prepareRequest();
        HippoBean document = request.getRequestContext().getContentBean();

        //Prepare the document's tranlations
        final String TRANSLATED_URL = "/site/lan-ctr/rest/of/the/path";
        HippoBean translation = mock(Page.class, RETURNS_DEEP_STUBS);
        when(document.getAvailableTranslations().getTranslation(anyString())).thenReturn(translation);
        when(bundle.getResourceBundle(any(), any(), any(Locale.class))).thenReturn("Label");
        when(utils.createUrl(any(), anyBoolean())).thenReturn(TRANSLATED_URL);
        when(utils.getContentBeanWithTranslationFallback(any())).thenReturn(Optional.of(document));

        List<LocalizedURL> list = documentUtils.getLocalizedURLs(request);

        assertEquals(AVAILABLE_LANGUAGES, list.size());

        for (LocalizedURL url : list){
            assertEquals(TRANSLATED_URL, url.getUrl());
            assertEquals("Label", url.getDisplayName());
            assertTrue(url.isExists());
        }
    }

    @Test
    @DisplayName("Composes non-existing URLs")
    void composeNonExistingLanguageURL(){
        HstRequest request = prepareRequest();
        HippoBean document = request.getRequestContext().getContentBean();

        //Prepare the document's tranlations
        final String ENGLISH_URL = "/site/english/url";
        HippoBean translation = mock(Page.class, RETURNS_DEEP_STUBS);
        when(document.getAvailableTranslations().getTranslation(Locale.UK.getLanguage())).thenReturn(translation);
        when(utils.createUrl(any(), anyBoolean())).thenReturn(ENGLISH_URL);
        when(utils.getContentBeanWithTranslationFallback(any())).thenReturn(Optional.of(document));

        List<LocalizedURL> list = documentUtils.getLocalizedURLs(request);

        //It should not compose the URL for the English version
        verify(properties, times(AVAILABLE_LANGUAGES-1)).getCmsBasePath();
        assertEquals(AVAILABLE_LANGUAGES, list.size());

        for (LocalizedURL url : list){
            if (url.getLocale().equals(Locale.UK)){
                assertEquals(ENGLISH_URL, url.getUrl());
                assertTrue(url.isExists());
            } else {
                assertNotEquals(ENGLISH_URL, url.getUrl());
                assertFalse(url.isExists());
            }
        }
    }

    @Test
    @DisplayName("Translations sorted by SEO order")
    void sortTranslationsBySeoOrdering() {
        when(properties.getChannelOrder()).thenReturn("en,fr,es");
        General englishDoc = mock(General.class);
        when(englishDoc.getLocale()).thenReturn(Locale.UK);
        General frenchDoc = mock(General.class);
        when(frenchDoc.getLocale()).thenReturn(Locale.FRANCE);
        General spanishDoc = mock(General.class);
        when(spanishDoc.getLocale()).thenReturn(Language.SPANISH.getLocale());

        List<General> sorted = documentUtils.sortTranslationsForSeo(Arrays.asList(frenchDoc, spanishDoc, englishDoc));

        assertEquals(Locale.UK, sorted.get(0).getLocale());
        assertEquals(Locale.FRANCE, sorted.get(1).getLocale());
        assertEquals(Language.SPANISH.getLocale(), sorted.get(2).getLocale());
    }

    @ParameterizedTest
    @DisplayName("When alternate link locale order property is invalid, no sorting occurs")
    @NullSource
    @ValueSource(strings = {"", "en,fr", "notalocale"})
    void sortTranslations_invalidSeoProperty(String seoProperty) {
        when(properties.getChannelOrder()).thenReturn(seoProperty);
        General englishDoc = mock(General.class);
        when(englishDoc.getLocale()).thenReturn(Locale.UK);
        General frenchDoc = mock(General.class);
        when(frenchDoc.getLocale()).thenReturn(Locale.FRANCE);
        General spanishDoc = mock(General.class);
        when(spanishDoc.getLocale()).thenReturn(Language.SPANISH.getLocale());

        List<General> sorted = documentUtils.sortTranslationsForSeo(Arrays.asList(frenchDoc, spanishDoc, englishDoc));

        // Ordering not changed
        assertEquals(Locale.UK, sorted.get(2).getLocale());
        assertEquals(Locale.FRANCE, sorted.get(0).getLocale());
        assertEquals(Language.SPANISH.getLocale(), sorted.get(1).getLocale());
    }


}
