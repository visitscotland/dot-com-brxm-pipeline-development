package com.visitscotland.brxm.translation;

import com.visitscotland.brxm.translation.plugin.JcrDocument;
import com.visitscotland.brxm.translation.plugin.JcrDocumentFactory;
import org.assertj.core.api.Condition;
import org.hippoecm.repository.api.WorkflowException;
import org.hippoecm.repository.translation.HippoTranslationNodeType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.jcr.ItemNotFoundException;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TranslationRestServiceTest {
    private TranslationRestService service;
    @Mock
    private SessionFactory mockSessionFactory;
    @Mock
    private JcrDocumentFactory mockJcrDocumentFactory;
    @Mock
    private TranslationService mockTranslationService;
    @Mock
    private Session mockJcrSession;

    @BeforeEach
    public void beforeEach() {
        service = new TranslationRestService(mockSessionFactory, mockJcrDocumentFactory, mockTranslationService);
        when(mockSessionFactory.getJcrSession()).thenReturn(mockJcrSession);
    }

    @DisplayName("getNodeDifference - English document, invalid request")
    @Test
    public void getNodeDifference_englishDocument() throws Exception {
        // When passed an English document is should return a 400 response.
        // English documents never have a difference attached
        String handleId = "1234";
        Node handleNode = new MockNodeBuilder()
                .inJcrSession(mockJcrSession)
                .withNodeId(handleId).build();
        Node unpublishedNode = new MockNodeBuilder()
                .withProperty(HippoTranslationNodeType.LOCALE, "en").build();
        JcrDocument handleDocument = new MockJcrDocumentBuilder()
                .withVariantNode(JcrDocument.VARIANT_UNPUBLISHED, unpublishedNode).build();
        when(mockJcrDocumentFactory.createFromNode(same(handleNode))).thenReturn(handleDocument);

        Condition<ResponseStatusException> badRequest = new Condition<>(ex -> ex.getStatus().equals(HttpStatus.BAD_REQUEST), "Bad Request");
        assertThatExceptionOfType(ResponseStatusException.class).isThrownBy(
                () -> service.getNodeDifference(handleId)
        ).has(badRequest);
    }

    @DisplayName("getNodeDifference - document with no content property")
    @Test
    public void getNodeDifference_noContent() throws Exception {
        String handleId = "1234";
        Node handleNode = new MockNodeBuilder()
                .inJcrSession(mockJcrSession)
                .withNodeId(handleId).build();
        Node unpublishedNode = new MockNodeBuilder()
                .withProperty(HippoTranslationNodeType.LOCALE, "es").build();
        JcrDocument handleDocument = new MockJcrDocumentBuilder()
                .withVariantNode(JcrDocument.VARIANT_UNPUBLISHED, unpublishedNode).build();
        when(mockJcrDocumentFactory.createFromNode(same(handleNode))).thenReturn(handleDocument);

        when(mockTranslationService.getDocumentDifference(eq(handleId))).thenReturn(null);

        Condition<ResponseStatusException> noContent = new Condition<>(ex -> ex.getStatus().equals(HttpStatus.NO_CONTENT), " No Content");
        assertThatExceptionOfType(ResponseStatusException.class).isThrownBy(
                () -> service.getNodeDifference(handleId)
        ).has(noContent);
    }

    @DisplayName("getNodeDifference - document with content property")
    @Test
    public void getNodeDifference_withContent() throws Exception {
        String handleId = "1234";
        String content = "the quick brown fox";
        Node handleNode = new MockNodeBuilder()
                .inJcrSession(mockJcrSession)
                .withNodeId(handleId).build();
        Node unpublishedNode = new MockNodeBuilder()
                .withProperty(HippoTranslationNodeType.LOCALE, "es").build();
        JcrDocument handleDocument = new MockJcrDocumentBuilder()
                .withVariantNode(JcrDocument.VARIANT_UNPUBLISHED, unpublishedNode).build();
        when(mockJcrDocumentFactory.createFromNode(same(handleNode))).thenReturn(handleDocument);

        when(mockTranslationService.getDocumentDifference(eq(handleId))).thenReturn(content);

        String result = service.getNodeDifference(handleId);
        assertThat(result).isEqualTo(content);
    }

    @DisplayName("getNodeDifference - handle ID not found")
    @Test
    public void getNodeDifference_nodeNotFound() throws Exception{
        when(mockJcrSession.getNodeByIdentifier(anyString())).thenThrow(new ItemNotFoundException());

        Condition<ResponseStatusException> notFound = new Condition<>(ex -> ex.getStatus().equals(HttpStatus.NOT_FOUND), "Not Found");
        assertThatExceptionOfType(ResponseStatusException.class).isThrownBy(
                () -> service.getNodeDifference("1234")
        ).has(notFound);
    }

    @DisplayName("getNodeDifference - node Id not for a variant document")
    @Test
    public void getNodeDifference_nodeNotJcrDocument() throws Exception {
        String handleId = "1234";
        Node handleNode = new MockNodeBuilder()
                .inJcrSession(mockJcrSession)
                .withNodeId(handleId).build();
        when(mockJcrDocumentFactory.createFromNode(same(handleNode))).thenThrow(new IllegalArgumentException());

        Condition<ResponseStatusException> badRequest = new Condition<>(ex -> ex.getStatus().equals(HttpStatus.BAD_REQUEST), "Bad Request");
        assertThatExceptionOfType(ResponseStatusException.class).isThrownBy(
                () -> service.getNodeDifference(handleId)
        ).has(badRequest);
    }

    @DisplayName("getNodeDifference - node Id not for a variant document")
    @Test
    public void getNodeDifference_generalRepositoryException() throws Exception {
        when(mockJcrSession.getNodeByIdentifier(anyString())).thenThrow(new RepositoryException());

        Condition<ResponseStatusException> serverError = new Condition<>(ex -> ex.getStatus().equals(HttpStatus.INTERNAL_SERVER_ERROR), "Internal Server Error");
        assertThatExceptionOfType(ResponseStatusException.class).isThrownBy(
                () -> service.getNodeDifference("1234")
        ).has(serverError);
    }

    @DisplayName("setTranslationFlag - repository exception")
    @Test
    public void setTranslationFlag_generalRepositoryException() throws Exception {
        String nodeId = "1234";
        String body = "the lazy dog";
        when(mockJcrSession.getNodeByIdentifier(eq(nodeId))).thenThrow(new RepositoryException());

        Condition<ResponseStatusException> serverError =
                new Condition<>(ex -> ex.getStatus().equals(HttpStatus.INTERNAL_SERVER_ERROR), "500 Internal Server Error");
        assertThatExceptionOfType(ResponseStatusException.class)
                .isThrownBy(() -> service.setTranslationFlag(nodeId, body)).has(serverError);
    }

    @DisplayName("setTranslationFlag - node not found")
    @Test
    public void setTranslationFlag_nodeNotFound() throws Exception {
        String nodeId = "1234";
        String body = "the lazy dog";
        when(mockJcrSession.getNodeByIdentifier(eq(nodeId))).thenThrow(new ItemNotFoundException());

        Condition<ResponseStatusException> notFound =
                new Condition<>(ex -> ex.getStatus().equals(HttpStatus.NOT_FOUND), "404 NotFound");
        assertThatExceptionOfType(ResponseStatusException.class)
                .isThrownBy(() -> service.setTranslationFlag(nodeId, body)).has(notFound);
    }

    @DisplayName("setTranslationFlag - not an English document")
    @Test
    public void setTranslationFlag_notEnglishDocument() throws Exception {
        // The service will throw an IllegalArgumentException if the document is not an English document
        String nodeId = "1234";
        String body = "the lazy dog";
        TranslationService.TranslationContent content = new TranslationService.TranslationContent();
        content.setContent(body);
        Node handleNode = new MockNodeBuilder()
                .inJcrSession(mockJcrSession)
                .withNodeId(nodeId).build();
        JcrDocument jcrDocument = new MockJcrDocumentBuilder().build();
        when(mockJcrDocumentFactory.createFromNode(same(handleNode))).thenReturn(jcrDocument);
        when(mockTranslationService.setTranslationContent(same(jcrDocument), eq(content))).thenThrow(new IllegalArgumentException());

        Condition<ResponseStatusException> badRequest =
                new Condition<>(ex -> ex.getStatus().equals(HttpStatus.BAD_REQUEST), "400 Bad Request");
        assertThatExceptionOfType(ResponseStatusException.class)
                .isThrownBy(() -> service.setTranslationFlag(nodeId, body)).has(badRequest);
    }

    @DisplayName("setTranslationFlag - no foreign translations")
    @Test
    public void setTranslationFlag_noTranslations() throws Exception {
        // The service will throw an IllegalArgumentException if the document is not an English document
        String nodeId = "1234";
        String body = "the lazy dog";
        TranslationService.TranslationContent content = new TranslationService.TranslationContent();
        content.setContent(body);
        Node handleNode = new MockNodeBuilder()
                .inJcrSession(mockJcrSession)
                .withNodeId(nodeId).build();
        JcrDocument jcrDocument = new MockJcrDocumentBuilder().build();
        when(mockJcrDocumentFactory.createFromNode(same(handleNode))).thenReturn(jcrDocument);
        when(mockTranslationService.setTranslationContent(same(jcrDocument), eq(content))).thenThrow(new IllegalStateException());

        Condition<ResponseStatusException> badRequest =
                new Condition<>(ex -> ex.getStatus().equals(HttpStatus.NO_CONTENT), "204 No Content");
        assertThatExceptionOfType(ResponseStatusException.class)
                .isThrownBy(() -> service.setTranslationFlag(nodeId, body)).has(badRequest);
    }

    @DisplayName("setTranslationFlag - some documents being edited")
    @Test
    public void setTranslationFlag_unableToLockDocument() throws Exception {
        String nodeId = "1234";
        String body = "the lazy dog";
        TranslationService.TranslationContent content = new TranslationService.TranslationContent();
        content.setContent(body);
        Node handleNode = new MockNodeBuilder()
                .inJcrSession(mockJcrSession)
                .withNodeId(nodeId).build();
        JcrDocument jcrDocument = new MockJcrDocumentBuilder().build();
        when(mockJcrDocumentFactory.createFromNode(same(handleNode))).thenReturn(jcrDocument);
        List<JcrDocument> blockingDocuments = Arrays.asList(new MockJcrDocumentBuilder().build());
        when(mockTranslationService.setTranslationContent(same(jcrDocument), eq(content))).thenReturn(blockingDocuments);
        Condition<ResponseStatusException> conflict =
                new Condition<>(ex -> ex.getStatus().equals(HttpStatus.CONFLICT), "409 Conflict");
        assertThatExceptionOfType(ResponseStatusException.class)
                .isThrownBy(() -> service.setTranslationFlag(nodeId, body)).has(conflict);
    }

    @DisplayName("setTranslationFlag - success")
    @Test
    public void setTranslationFlag() throws Exception {
        String nodeId = "1234";
        String body = "the lazy dog";
        TranslationService.TranslationContent content = new TranslationService.TranslationContent();
        content.setContent(body);
        Node handleNode = new MockNodeBuilder()
                .inJcrSession(mockJcrSession)
                .withNodeId(nodeId).build();
        JcrDocument jcrDocument = new MockJcrDocumentBuilder().build();
        when(mockJcrDocumentFactory.createFromNode(same(handleNode))).thenReturn(jcrDocument);
        when(mockTranslationService.setTranslationContent(same(jcrDocument), eq(content))).thenReturn(new ArrayList<>());

        service.setTranslationFlag(nodeId, body);
    }

    @DisplayName("deleteTranslationFlag - repository exception")
    @Test
    public void deleteTranslationFlag_generalRepositoryException() throws Exception {
        String nodeId = "1234";
        when(mockJcrSession.getNodeByIdentifier(eq(nodeId))).thenThrow(new RepositoryException());

        Condition<ResponseStatusException> serverError =
                new Condition<>(ex -> ex.getStatus().equals(HttpStatus.INTERNAL_SERVER_ERROR), "500 Internal Server Error");
        assertThatExceptionOfType(ResponseStatusException.class)
                .isThrownBy(() -> service.deleteTranslationFlag(nodeId)).has(serverError);
    }

    @DisplayName("deleteTranslationFlag - node not found")
    @Test
    public void deleteTranslationFlag_nodeNotFound() throws Exception {
        String nodeId = "1234";
        when(mockJcrSession.getNodeByIdentifier(eq(nodeId))).thenThrow(new ItemNotFoundException());

        Condition<ResponseStatusException> notFound =
                new Condition<>(ex -> ex.getStatus().equals(HttpStatus.NOT_FOUND), "404 NotFound");
        assertThatExceptionOfType(ResponseStatusException.class)
                .isThrownBy(() -> service.deleteTranslationFlag(nodeId)).has(notFound);
    }

    @DisplayName("deleteTranslationFlag - not an English document")
    @Test
    public void deleteTranslationFlag_notEnglishDocument() throws Exception {
        // The service will throw an IllegalArgumentException if the document is not an English document
        String nodeId = "1234";
        Node handleNode = new MockNodeBuilder()
                .inJcrSession(mockJcrSession)
                .withNodeId(nodeId).build();
        JcrDocument jcrDocument = new MockJcrDocumentBuilder().build();
        when(mockJcrDocumentFactory.createFromNode(same(handleNode))).thenReturn(jcrDocument);
        doThrow(new IllegalArgumentException()).when(mockTranslationService).clearTranslationFlag(same(jcrDocument));

        Condition<ResponseStatusException> badRequest =
                new Condition<>(ex -> ex.getStatus().equals(HttpStatus.BAD_REQUEST), "400 Bad Request");
        assertThatExceptionOfType(ResponseStatusException.class)
                .isThrownBy(() -> service.deleteTranslationFlag(nodeId)).has(badRequest);
    }

    @DisplayName("deleteTranslationFlag - document being edited")
    @Test
    public void deleteTranslationFlag_unableToLockDocument() throws Exception {
        String nodeId = "1234";
        Node handleNode = new MockNodeBuilder()
                .inJcrSession(mockJcrSession)
                .withNodeId(nodeId).build();
        JcrDocument jcrDocument = new MockJcrDocumentBuilder().build();
        when(mockJcrDocumentFactory.createFromNode(same(handleNode))).thenReturn(jcrDocument);
        List<JcrDocument> blockingDocuments = Arrays.asList(new MockJcrDocumentBuilder().build());
        doThrow(new WorkflowException("")).when(mockTranslationService).clearTranslationFlag(same(jcrDocument));
        Condition<ResponseStatusException> conflict =
                new Condition<>(ex -> ex.getStatus().equals(HttpStatus.CONFLICT), "409 Conflict");
        assertThatExceptionOfType(ResponseStatusException.class)
                .isThrownBy(() -> service.deleteTranslationFlag(nodeId)).has(conflict);
    }

    @DisplayName("deleteTranslationFlag - success")
    @Test
    public void deleteTranslationFlag() throws Exception {
        String nodeId = "1234";
        Node handleNode = new MockNodeBuilder()
                .inJcrSession(mockJcrSession)
                .withNodeId(nodeId).build();
        JcrDocument jcrDocument = new MockJcrDocumentBuilder().build();
        when(mockJcrDocumentFactory.createFromNode(same(handleNode))).thenReturn(jcrDocument);

        service.deleteTranslationFlag(nodeId);
    }
}
