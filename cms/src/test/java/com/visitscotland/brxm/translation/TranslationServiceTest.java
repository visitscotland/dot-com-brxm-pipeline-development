package com.visitscotland.brxm.translation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.visitscotland.brxm.translation.plugin.JcrDocument;
import com.visitscotland.brxm.translation.plugin.JcrDocumentFactory;
import org.hippoecm.frontend.session.UserSession;
import org.hippoecm.repository.api.Workflow;
import org.hippoecm.repository.api.WorkflowException;
import org.hippoecm.repository.api.WorkflowManager;
import org.hippoecm.repository.standardworkflow.EditableWorkflow;
import org.hippoecm.repository.translation.HippoTranslationNodeType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.Session;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TranslationServiceTest {
    @Mock
    private JcrDocumentFactory mockJcrDocumentFactory;
    @Mock
    private SessionFactory mockSessionFactory;
    @Mock
    private Session mockJcrSession;
    @Mock
    private UserSession mockUserSession;
    @Mock
    private WorkflowManager mockWorkflowManager;
    @Mock
    private ObjectMapper mockObjectMapper;

    private TranslationService service;

    @BeforeEach
    public void beforeEach() {
        service = new TranslationService(mockJcrDocumentFactory, mockObjectMapper, mockSessionFactory);
    }

    @DisplayName("getDocument - simple coverage")
    @Test
    public void getDocument() throws Exception {
        String nodeId = "ABCD34";
        Node mockNode = new MockNodeBuilder()
                .inJcrSession(mockJcrSession)
                .withNodeId(nodeId).build();
        JcrDocument mockJcrDocument = new MockJcrDocumentBuilder()
                .withJcrDocumentFactory(mockJcrDocumentFactory)
                .createdFromNode(mockNode).build();
        when(mockSessionFactory.getJcrSession()).thenReturn(mockJcrSession);

        JcrDocument document = service.getDocument(nodeId);

        assertThat(document).isSameAs(mockJcrDocument);
    }

    @DisplayName("hasPendingTranslations - document with no translations")
    @Test
    public void hasPendingTranslations_noTranslations() throws Exception {
        JcrDocument mockJcrDocument = new MockJcrDocumentBuilder().withTranslations().build();

        boolean hasPendingTranslations = service.hasPendingTranslations(mockJcrDocument);

        assertThat(hasPendingTranslations).isFalse();
    }

    @DisplayName("hasPendingTranslations - document with translation documents and no pending")
    @Test
    public void hasPendingTranslations_withTranslations_nonePending() throws Exception {
        Node translation1Node = new MockNodeBuilder().build();
        JcrDocument translation1 = new MockJcrDocumentBuilder()
                .withVariantNode(JcrDocument.VARIANT_UNPUBLISHED, translation1Node).build();

        Node translation2Node = new MockNodeBuilder().withProperty(JcrDocument.VS_TRANSLATION_FLAG, false).build();
        JcrDocument translation2 = new MockJcrDocumentBuilder()
                .withVariantNode(JcrDocument.VARIANT_UNPUBLISHED, translation2Node).build();

        JcrDocument mockJcrDocument = new MockJcrDocumentBuilder().withTranslations(translation1, translation2).build();

        boolean hasPendingTranslations = service.hasPendingTranslations(mockJcrDocument);

        assertThat(hasPendingTranslations).isFalse();
    }

    @DisplayName("hasPendingTranslations - document with translation documents and pending translations")
    @Test
    public void hasPendingTranslations_withPendingTranslations() throws Exception {
        Node translation1Node = new MockNodeBuilder().withProperty(JcrDocument.VS_TRANSLATION_FLAG, true).build();
        JcrDocument translation1 = new MockJcrDocumentBuilder()
                .withVariantNode(JcrDocument.VARIANT_UNPUBLISHED, translation1Node).build();

        JcrDocument mockJcrDocument = new MockJcrDocumentBuilder().withTranslations(translation1).build();

        boolean hasPendingTranslations = service.hasPendingTranslations(mockJcrDocument);

        assertThat(hasPendingTranslations).isTrue();
    }

    @DisplayName("getTranslationFlag - no translation flag property on node")
    @Test
    public void getTranslationFlag_withoutProperty() throws Exception {
        Node mockNode = new MockNodeBuilder().build();
        JcrDocument mockJcrDocument = new MockJcrDocumentBuilder().withVariantNode(JcrDocument.VARIANT_UNPUBLISHED, mockNode).build();
        boolean translationFlag = service.getTranslationFlag(mockJcrDocument);
        assertThat(translationFlag).isFalse();
    }

    @DisplayName("getTranslationFlag - translation flag property with false value")
    @Test
    public void getTranslationFlag_withProperty_false() throws Exception {
        Node mockNode = new MockNodeBuilder().withProperty(JcrDocument.VS_TRANSLATION_FLAG, false).build();
        JcrDocument mockJcrDocument = new MockJcrDocumentBuilder().withVariantNode(JcrDocument.VARIANT_UNPUBLISHED, mockNode).build();
        boolean translationFlag = service.getTranslationFlag(mockJcrDocument);
        assertThat(translationFlag).isFalse();
    }

    @DisplayName("getTranslationFlag - translation flag property with true value")
    @Test
    public void getTranslationFlag_withProperty_true() throws Exception {
        Node mockNode = new MockNodeBuilder().withProperty(JcrDocument.VS_TRANSLATION_FLAG, true).build();
        JcrDocument mockJcrDocument = new MockJcrDocumentBuilder()
                .withVariantNode(JcrDocument.VARIANT_UNPUBLISHED, mockNode).build();
        boolean translationFlag = service.getTranslationFlag(mockJcrDocument);
        assertThat(translationFlag).isTrue();
    }

    @DisplayName("getDocumentDifference - no diff value property")
    @Test
    public void getDocumentDifference_withoutProperty() throws Exception {
        String nodeId = "1234";
        Node mockUnpublishedNode = new MockNodeBuilder().withNodeId(nodeId).inJcrSession(mockJcrSession).build();
        JcrDocument mockJcrDocument = new MockJcrDocumentBuilder()
                .withVariantNode(JcrDocument.VARIANT_UNPUBLISHED, mockUnpublishedNode).build();
        when(mockSessionFactory.getJcrSession()).thenReturn(mockJcrSession);
        when(mockJcrDocumentFactory.createFromNode(same(mockUnpublishedNode))).thenReturn(mockJcrDocument);

        String diff = service.getDocumentDifference(nodeId);
        assertThat(diff).isNull();
    }

    @DisplayName("getDocumentDifference - with a diff value property")
    @Test
    public void getDocumentDifference_withProperty() throws Exception {
        String nodeId = "1234";
        String diffBody = "changed body value";
        Node mockUnpublishedNode = new MockNodeBuilder()
                .withNodeId(nodeId)
                .inJcrSession(mockJcrSession)
                .withProperty(JcrDocument.VS_TRANSLATION_DIFF, diffBody).build();
        JcrDocument mockJcrDocument = new MockJcrDocumentBuilder()
                .withVariantNode(JcrDocument.VARIANT_UNPUBLISHED, mockUnpublishedNode).build();
        when(mockSessionFactory.getJcrSession()).thenReturn(mockJcrSession);
        when(mockJcrDocumentFactory.createFromNode(same(mockUnpublishedNode))).thenReturn(mockJcrDocument);

        String diff = service.getDocumentDifference(nodeId);
        assertThat(diff).isEqualTo(diffBody);
    }

    @DisplayName("clearTranslationFlag - draft being edited, cannot clear")
    @Test
    public void clearTranslationFlag_draftBeingEdited() throws Exception {
        JcrDocument mockJcrDocument = new MockJcrDocumentBuilder().isDraftBeingEdited(true).build();

        assertThatThrownBy(() -> service.clearTranslationFlag(mockJcrDocument)).isInstanceOf(WorkflowException.class);
    }

    @DisplayName("clearTranslationFlag - workflow is the wrong type")
    @Test
    public void clearTranslationFlag_workflowWrongType() throws Exception {
        Node mockUnpublishedNode = new MockNodeBuilder()
                .withProperty(HippoTranslationNodeType.LOCALE, "es").build();
        Node mockHandleNode = new MockNodeBuilder()
                .build();
        JcrDocument mockJcrDocument = new MockJcrDocumentBuilder()
                .withVariantNode(JcrDocument.VARIANT_UNPUBLISHED, mockUnpublishedNode)
                .withHandle(mockHandleNode)
                .isDraftBeingEdited(false).build();
        when(mockSessionFactory.getJcrSession()).thenReturn(mockJcrSession);

        when(mockSessionFactory.getUserSession()).thenReturn(mockUserSession);
        Workflow mockWorkflow = mock(Workflow.class);
        addToMockWorkflowManager("editing", mockHandleNode, mockWorkflow);

        assertThatThrownBy(() -> service.clearTranslationFlag(mockJcrDocument)).isInstanceOf(IllegalStateException.class);
    }

    @DisplayName("clearTranslationFlag - cannot clear translation flag for an English document")
    @Test
    public void clearTranslationFlag_englishDocument() throws Exception {
        Node mockUnpublishedNode = new MockNodeBuilder()
                .withProperty(HippoTranslationNodeType.LOCALE, "en").build();
        Node mockHandleNode = new MockNodeBuilder()
                .build();
        JcrDocument mockJcrDocument = new MockJcrDocumentBuilder()
                .withVariantNode(JcrDocument.VARIANT_UNPUBLISHED, mockUnpublishedNode)
                .isDraftBeingEdited(false).build();
        when(mockSessionFactory.getJcrSession()).thenReturn(mockJcrSession);

        assertThatThrownBy(() -> service.clearTranslationFlag(mockJcrDocument)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("clearTranslationFlag - already cleared, should not fail or change state")
    @Test
    public void clearTranslationFlag_alreadyCleared() throws Exception {
        // Has no diff property and flag is false.
        Node mockUnpublishedNode = new MockNodeBuilder()
                .withProperty(HippoTranslationNodeType.LOCALE, "es")
                .build();
        Node mockHandleNode = new MockNodeBuilder()
                .build();
        JcrDocument mockJcrDocument = new MockJcrDocumentBuilder()
                .withVariantNode(JcrDocument.VARIANT_UNPUBLISHED, mockUnpublishedNode)
                .withHandle(mockHandleNode)
                .isDraftBeingEdited(false).build();
        when(mockSessionFactory.getJcrSession()).thenReturn(mockJcrSession);

        when(mockSessionFactory.getUserSession()).thenReturn(mockUserSession);
        EditableWorkflow mockWorkflow = mock(EditableWorkflow.class);
        addToMockWorkflowManager("editing", mockHandleNode, mockWorkflow);

        service.clearTranslationFlag(mockJcrDocument);

        verify(mockWorkflow).disposeEditableInstance();
        verify(mockUnpublishedNode).setProperty(eq(JcrDocument.VS_TRANSLATION_FLAG), eq(false));
        verify(mockJcrSession).save();
    }

    @DisplayName("clearTranslationFlag - should clear the diff and the flag")
    @Test
    public void clearTranslationFlag() throws Exception {
        Property mockDiffProperty = mock(Property.class);
        Node mockUnpublishedNode = new MockNodeBuilder()
                .withProperty(HippoTranslationNodeType.LOCALE, "es")
                .withProperty(JcrDocument.VS_TRANSLATION_DIFF, mockDiffProperty)
                .build();
        Node mockHandleNode = new MockNodeBuilder()
                .build();
        JcrDocument mockJcrDocument = new MockJcrDocumentBuilder()
                .withVariantNode(JcrDocument.VARIANT_UNPUBLISHED, mockUnpublishedNode)
                .withHandle(mockHandleNode)
                .isDraftBeingEdited(false).build();
        when(mockSessionFactory.getJcrSession()).thenReturn(mockJcrSession);

        when(mockSessionFactory.getUserSession()).thenReturn(mockUserSession);
        EditableWorkflow mockWorkflow = mock(EditableWorkflow.class);
        addToMockWorkflowManager("editing", mockHandleNode, mockWorkflow);

        service.clearTranslationFlag(mockJcrDocument);

        verify(mockWorkflow).disposeEditableInstance();
        verify(mockUnpublishedNode).setProperty(eq(JcrDocument.VS_TRANSLATION_FLAG), eq(false));
        verify(mockDiffProperty).remove();
        verify(mockJcrSession).save();
    }

    protected void addToMockWorkflowManager(String name, Node node, Workflow workflow) throws Exception {
        when(mockWorkflowManager.getWorkflow(eq(name), eq(node))).thenReturn(workflow);
        when(mockUserSession.getWorkflowManager()).thenReturn(mockWorkflowManager);
    }

    @DisplayName("setTranslationContent - node that is not translatable should not cause exception")
    @Test
    public void setTranslationContent_notTranslatable() throws Exception {
        // Should not cause an error, Should never happen so no point throwing an exception
        String contentString = "the quick brown fox jumped over the lazy dog";
        TranslationService.TranslationContent content = new TranslationService.TranslationContent();
        content.setContent(contentString);
        JcrDocument mockJcrDocument = new MockJcrDocumentBuilder().build();

        List<JcrDocument> documentsBeingEditedList = service.setTranslationContent(mockJcrDocument, content);
        assertThat(documentsBeingEditedList).isNotNull().isEmpty();
    }

    @DisplayName("setTranslationContent - the English document is being edited")
    @Test
    public void setTranslationContent_englishDraftBeingEdited_noTranslations() throws Exception {
        // Should not fail, but should return the english document in the documents being edited list
        String contentString = "the quick brown fox jumped over the lazy dog";
        TranslationService.TranslationContent content = new TranslationService.TranslationContent();
        content.setContent(contentString);

        Node translation1Handle = new MockNodeBuilder().build();
        Node translation1Unpublished = new MockNodeBuilder().build();
        EditableWorkflow translation1Workflow = mock(EditableWorkflow.class);
        addToMockWorkflowManager("editing", translation1Handle, translation1Workflow);
        JcrDocument translation1 = new MockJcrDocumentBuilder()
                .withHandle(translation1Handle)
                .withVariantNode(JcrDocument.VARIANT_UNPUBLISHED, translation1Unpublished).build();
        Node translation2Handle = new MockNodeBuilder().build();
        Node translation2Unpublished = new MockNodeBuilder().build();
        EditableWorkflow translation2Workflow = mock(EditableWorkflow.class);
        addToMockWorkflowManager("editing", translation2Handle, translation2Workflow);
        JcrDocument translation2 = new MockJcrDocumentBuilder()
                .withHandle(translation2Handle)
                .withVariantNode(JcrDocument.VARIANT_UNPUBLISHED, translation2Unpublished).build();
        JcrDocument mockJcrDocument = new MockJcrDocumentBuilder()
                .isNodeType(JcrDocument.VS_TRANSLATABLE_TYPE)
                .withLocaleName("en")
                .isDraftBeingEdited(true)
                .withTranslations(translation1, translation2).build();
        when(mockSessionFactory.getJcrSession()).thenReturn(mockJcrSession);
        when(mockSessionFactory.getUserSession()).thenReturn(mockUserSession);

        List<JcrDocument> documentsBeingEditedList = service.setTranslationContent(mockJcrDocument, content);
        assertThat(documentsBeingEditedList).isNotNull().containsExactly(mockJcrDocument);
    }

    @DisplayName("setTranslationContent - the English document has no translations")
    @Test
    public void setTranslationContent_noTranslations() throws Exception {
        String contentString = "the quick brown fox jumped over the lazy dog";
        TranslationService.TranslationContent content = new TranslationService.TranslationContent();
        content.setContent(contentString);
        JcrDocument mockJcrDocument = new MockJcrDocumentBuilder()
                .isNodeType(JcrDocument.VS_TRANSLATABLE_TYPE)
                .isDraftBeingEdited(false)
                .withLocaleName("en")
                .withTranslations().build();
        when(mockSessionFactory.getJcrSession()).thenReturn(mockJcrSession);

        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> service.setTranslationContent(mockJcrDocument, content));
        verify(mockJcrSession, never()).save();
    }

    @DisplayName("setTranslationContent - document has translations, but some are being edited")
    @Test
    public void setTranslationContent_someBeingEdited() throws Exception {
        // If any of the translations are being edited it should not set any of the attributes
        // Any documents checked out should be disposed of
        String contentString = "the quick brown fox jumped over the lazy dog";
        TranslationService.TranslationContent content = new TranslationService.TranslationContent();
        content.setContent(contentString);
        Node translation1Handle = new MockNodeBuilder().build();
        EditableWorkflow translation1Workflow = mock(EditableWorkflow.class);
        addToMockWorkflowManager("editing", translation1Handle, translation1Workflow);
        JcrDocument translation1 = new MockJcrDocumentBuilder()
                .isDraftBeingEdited(false)
                .withHandle(translation1Handle).build();

        Node translation2Handle = new MockNodeBuilder().build();
        EditableWorkflow translation2Workflow = mock(EditableWorkflow.class);
        addToMockWorkflowManager("editing", translation2Handle, translation2Workflow);
        JcrDocument translation2 = new MockJcrDocumentBuilder()
                .isDraftBeingEdited(false)
                .withHandle(translation2Handle).build();

        JcrDocument translation3 = new MockJcrDocumentBuilder()
                .isDraftBeingEdited(true).build();

        Node translation4Handle = new MockNodeBuilder().build();
        EditableWorkflow translation4Workflow = mock(EditableWorkflow.class);
        when(translation4Workflow.obtainEditableInstance()).thenThrow(new WorkflowException("already locked"));
        addToMockWorkflowManager("editing", translation4Handle, translation4Workflow);
        JcrDocument translation4 = new MockJcrDocumentBuilder()
                .isDraftBeingEdited(false)
                .withHandle(translation4Handle).build();

        JcrDocument mockJcrDocument = new MockJcrDocumentBuilder()
                .isNodeType(JcrDocument.VS_TRANSLATABLE_TYPE)
                .isDraftBeingEdited(false)
                .withLocaleName("en")
                .withTranslations(translation1, translation2, translation3, translation4).build();
        when(mockSessionFactory.getJcrSession()).thenReturn(mockJcrSession);
        when(mockSessionFactory.getUserSession()).thenReturn(mockUserSession);

        List<JcrDocument> documentsBeingEditedList = service.setTranslationContent(mockJcrDocument, content);
        assertThat(documentsBeingEditedList).isNotNull().containsExactlyInAnyOrder(translation3, translation4);
        verify(translation1Workflow).obtainEditableInstance();
        verify(translation1Workflow).disposeEditableInstance();

        verify(translation2Workflow).obtainEditableInstance();
        verify(translation2Workflow).disposeEditableInstance();

        verify(mockJcrSession, never()).save();
    }

    @DisplayName("setTranslationContent - document has translations, none are being edited")
    @Test
    public void setTranslationContent_noneBeingEdited() throws Exception {
        // If none of the documents are being edited the difference attributes should be set on all the translations
        String contentString = "the quick brown fox jumped over the lazy dog";
        TranslationService.TranslationContent content = new TranslationService.TranslationContent();
        content.setContent(contentString);
        when(mockObjectMapper.writeValueAsString(eq(content))).thenReturn("serializedContent");
        Node translation1Handle = new MockNodeBuilder().build();
        Node translation1Unpublished = new MockNodeBuilder().build();
        EditableWorkflow translation1Workflow = mock(EditableWorkflow.class);
        addToMockWorkflowManager("editing", translation1Handle, translation1Workflow);
        JcrDocument translation1 = new MockJcrDocumentBuilder()
                .withHandle(translation1Handle)
                .withVariantNode(JcrDocument.VARIANT_UNPUBLISHED, translation1Unpublished).build();
        Node translation2Handle = new MockNodeBuilder().build();
        Node translation2Unpublished = new MockNodeBuilder().build();
        EditableWorkflow translation2Workflow = mock(EditableWorkflow.class);
        addToMockWorkflowManager("editing", translation2Handle, translation2Workflow);
        JcrDocument translation2 = new MockJcrDocumentBuilder()
                .withHandle(translation2Handle)
                .withVariantNode(JcrDocument.VARIANT_UNPUBLISHED, translation2Unpublished).build();
        JcrDocument mockJcrDocument = new MockJcrDocumentBuilder()
                .isNodeType(JcrDocument.VS_TRANSLATABLE_TYPE)
                .withLocaleName("en")
                .isDraftBeingEdited(false)
                .withTranslations(translation1, translation2).build();
        when(mockSessionFactory.getJcrSession()).thenReturn(mockJcrSession);
        when(mockSessionFactory.getUserSession()).thenReturn(mockUserSession);

        List<JcrDocument> documentsBeingEditedList = service.setTranslationContent(mockJcrDocument, content);
        assertThat(documentsBeingEditedList).isNotNull().isEmpty();
        verify(translation1Workflow).obtainEditableInstance();
        verify(translation1Unpublished).setProperty(eq(JcrDocument.VS_TRANSLATION_FLAG), eq(true));
        verify(translation1Unpublished).setProperty(eq(JcrDocument.VS_TRANSLATION_DIFF), eq("serializedContent"));
        verify(translation1Workflow).disposeEditableInstance();

        verify(translation2Workflow).obtainEditableInstance();
        verify(translation2Unpublished).setProperty(eq(JcrDocument.VS_TRANSLATION_FLAG), eq(true));
        verify(translation2Unpublished).setProperty(eq(JcrDocument.VS_TRANSLATION_DIFF), eq("serializedContent"));
        verify(translation2Workflow).disposeEditableInstance();

        verify(mockJcrSession).save();
    }

    @DisplayName("setTranslationContent - not an English document")
    @Test
    public void setTranslationContent_notEnglishDocument() throws Exception {
        // Should fail, should only be requesting to set content for an English document
        String contentString = "the quick brown fox jumped over the lazy dog";
        TranslationService.TranslationContent content = new TranslationService.TranslationContent();
        content.setContent(contentString);
        JcrDocument mockJcrDocument = new MockJcrDocumentBuilder()
                .isNodeType(JcrDocument.VS_TRANSLATABLE_TYPE)
                .withLocaleName("es").build();
        when(mockSessionFactory.getJcrSession()).thenReturn(mockJcrSession);

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> service.setTranslationContent(mockJcrDocument, content));
    }
}
