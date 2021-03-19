package com.visitscotland.brxm.services;

import com.visitscotland.brxm.hippobeans.BaseDocument;
import com.visitscotland.brxm.hippobeans.Listicle;
import com.visitscotland.brxm.hippobeans.ListicleItem;
import com.visitscotland.brxm.hippobeans.Page;
import com.visitscotland.brxm.services.DocumentUtilsService;
import com.visitscotland.brxm.utils.HippoUtilsService;
import org.apache.jackrabbit.commons.iterator.NodeIteratorAdapter;
import org.hippoecm.hst.content.beans.ObjectBeanManagerException;
import org.hippoecm.hst.content.beans.query.exceptions.QueryException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DocumentUtilsServiceTest {

    private DocumentUtilsService documentUtils;

    @Mock
    HippoUtilsService utils;

    @BeforeEach
    void init() {
        documentUtils = new DocumentUtilsService(utils);
    }

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

}
