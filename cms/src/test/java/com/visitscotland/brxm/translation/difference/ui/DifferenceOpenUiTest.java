package com.visitscotland.brxm.translation.difference.ui;

import com.visitscotland.brxm.translation.SessionFactory;
import com.visitscotland.brxm.translation.TranslationService;
import com.visitscotland.brxm.translation.plugin.JcrDocument;
import com.visitscotland.brxm.translation.plugin.JcrDocumentFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DifferenceOpenUiTest {
    private DifferenceOpenUi differenceOpenUi;
    @Mock
    private TranslationService mockTranslationService;
    @Mock
    private Model mockModel;
    @Mock
    private JcrDocumentFactory mockJcrDocumentFactory;
    @Mock
    private SessionFactory mockSessionFactory;

    @BeforeEach
    public void beforeEach() {
        differenceOpenUi = new DifferenceOpenUi(mockTranslationService,
                mockJcrDocumentFactory, mockSessionFactory);
    }

    @DisplayName("openUiButton - an english document, no translations pending")
    @Test
    public void buttonWithEnglishDocument() throws Exception {
        JcrDocument mockJcrDocument = mock(JcrDocument.class);
        when(mockJcrDocument.getLocaleName()).thenReturn("en");
        when(mockJcrDocument.getVariantNode(JcrDocument.VARIANT_UNPUBLISHED)).thenReturn(mock(Node.class));
        when(mockTranslationService.getDocument(eq("nodeId"))).thenReturn(mockJcrDocument);
        when(mockTranslationService.hasPendingTranslations(eq(mockJcrDocument))).thenReturn(false);

        String template = differenceOpenUi.openUiButton("nodeId", mockModel);
        assertThat(template).isEqualTo(DifferenceOpenUi.ENGLISH_TEMPLATE);
        verify(mockModel).addAttribute(eq(DifferenceOpenUi.ATTR_NODE_ID), eq("nodeId"));
        verify(mockModel).addAttribute(eq(DifferenceOpenUi.ATTR_HAS_TRANSLATION_PENDING), eq(false));
        verify(mockModel).addAttribute(eq(DifferenceOpenUi.ATTR_HAS_CHANGED), eq(false));
        verify(mockModel, times(3)).addAttribute(any(), any());
    }

    @DisplayName("VS-3323 - openUiButton - when only draft, empty template returned")
    @Test
    public void draftDocumentOpenUi() throws Exception {
        JcrDocument mockJcrDocument = mock(JcrDocument.class);
        when(mockTranslationService.getDocument(eq("nodeId"))).thenReturn(mockJcrDocument);

        String template = differenceOpenUi.openUiButton("nodeId", mockModel);

        assertThat(template).isEqualTo(DifferenceOpenUi.EMPTY_TEMPLATE);
        verify(mockJcrDocument).getVariantNode(JcrDocument.VARIANT_UNPUBLISHED);
    }


    @DisplayName("openUiButton - an english document, with translations pending")
    @Test
    public void buttonWithEnglishDocumentAndTranslationsPending() throws Exception {
        JcrDocument mockJcrDocument = mock(JcrDocument.class);
        when(mockJcrDocument.getLocaleName()).thenReturn("en");
        when(mockJcrDocument.getVariantNode(JcrDocument.VARIANT_UNPUBLISHED)).thenReturn(mock(Node.class));
        when(mockTranslationService.getDocument(eq("nodeId"))).thenReturn(mockJcrDocument);
        when(mockTranslationService.hasPendingTranslations(eq(mockJcrDocument))).thenReturn(true);

        String template = differenceOpenUi.openUiButton("nodeId", mockModel);
        assertThat(template).isEqualTo(DifferenceOpenUi.ENGLISH_TEMPLATE);
        verify(mockModel).addAttribute(eq(DifferenceOpenUi.ATTR_NODE_ID), eq("nodeId"));
        verify(mockModel).addAttribute(eq(DifferenceOpenUi.ATTR_HAS_TRANSLATION_PENDING), eq(true));
        verify(mockModel).addAttribute(eq(DifferenceOpenUi.ATTR_HAS_CHANGED), eq(false));
        verify(mockModel, times(3)).addAttribute(any(), any());
    }

    @DisplayName("openUiButton - a foreign document, no translation pending")
    @Test
    public void buttonWithForeignDocument() throws Exception {
        JcrDocument mockJcrDocument = mock(JcrDocument.class);
        when(mockJcrDocument.getLocaleName()).thenReturn("es");
        when(mockJcrDocument.getVariantNode(JcrDocument.VARIANT_UNPUBLISHED)).thenReturn(mock(Node.class));
        when(mockTranslationService.getDocument(eq("nodeId"))).thenReturn(mockJcrDocument);
        when(mockTranslationService.getTranslationFlag(eq(mockJcrDocument))).thenReturn(false);

        String template = differenceOpenUi.openUiButton("nodeId", mockModel);
        assertThat(template).isEqualTo(DifferenceOpenUi.FOREIGN_TEMPLATE);
        verify(mockModel).addAttribute(eq(DifferenceOpenUi.ATTR_NODE_ID), eq("nodeId"));
        verify(mockModel).addAttribute(eq(DifferenceOpenUi.ATTR_TRANSLATION_FLAG), eq(false));
        verify(mockModel, times(2)).addAttribute(any(), any());
    }

    @DisplayName("openUiButton - a foreign document, no translation pending")
    @Test
    public void buttonWithForeignDocumentWithTranslationFlag() throws Exception {
        JcrDocument mockJcrDocument = mock(JcrDocument.class);
        when(mockJcrDocument.getLocaleName()).thenReturn("es");
        when(mockJcrDocument.getVariantNode(JcrDocument.VARIANT_UNPUBLISHED)).thenReturn(mock(Node.class));
        when(mockTranslationService.getDocument(eq("nodeId"))).thenReturn(mockJcrDocument);
        when(mockTranslationService.getTranslationFlag(eq(mockJcrDocument))).thenReturn(true);

        String template = differenceOpenUi.openUiButton("nodeId", mockModel);
        assertThat(template).isEqualTo(DifferenceOpenUi.FOREIGN_TEMPLATE);
        verify(mockModel).addAttribute(eq(DifferenceOpenUi.ATTR_NODE_ID), eq("nodeId"));
        verify(mockModel).addAttribute(eq(DifferenceOpenUi.ATTR_TRANSLATION_FLAG), eq(true));
        verify(mockModel, times(2)).addAttribute(any(), any());
    }

    @DisplayName("openUiButton - exception")
    @Test
    public void openUiButtonException() throws Exception {
        JcrDocument mockJcrDocument = mock(JcrDocument.class);
        when(mockTranslationService.getDocument(eq("nodeId"))).thenThrow(new RepositoryException("message"));

        String template = differenceOpenUi.openUiButton("nodeId", mockModel);
        assertThat(template).isEqualTo(DifferenceOpenUi.ERROR_TEMPLATE);
        verify(mockModel).addAttribute(eq(DifferenceOpenUi.ATTR_NODE_ID), eq("nodeId"));
        verify(mockModel).addAttribute(eq(DifferenceOpenUi.ATTR_ERROR_MESSAGE), eq("message"));
        verify(mockModel).addAttribute(eq(DifferenceOpenUi.ATTR_STACK_TRACE), anyString());
        verify(mockModel, times(3)).addAttribute(any(), any());
    }

    @DisplayName("diffView - document has no difference saved")
    @Test
    public void diffViewNoDiff() throws Exception {
        when(mockTranslationService.getDocumentDifference(eq("nodeId"))).thenReturn(null);

        String template = differenceOpenUi.diffView("nodeId", mockModel);

        assertThat(template).isEqualTo(DifferenceOpenUi.ERROR_TEMPLATE);
        verify(mockModel).addAttribute(eq(DifferenceOpenUi.ATTR_NODE_ID), eq("nodeId"));
        verify(mockModel).addAttribute(eq(DifferenceOpenUi.ATTR_ERROR_MESSAGE), anyString());
        verify(mockModel).addAttribute(eq(DifferenceOpenUi.ATTR_STACK_TRACE), anyString());
        verify(mockModel, times(3)).addAttribute(any(), any());
    }

    @DisplayName("diffView - document has difference saved")
    @Test
    public void diffViewWithDiff() throws Exception {
        when(mockTranslationService.getDocumentDifference(eq("nodeId"))).thenReturn("exampleDiff");

        String template = differenceOpenUi.diffView("nodeId", mockModel);

        assertThat(template).isEqualTo(DifferenceOpenUi.DIFF_VIEW_TEMPLATE);
        verify(mockModel).addAttribute(eq(DifferenceOpenUi.ATTR_NODE_ID), eq("nodeId"));
        verify(mockModel).addAttribute(eq(DifferenceOpenUi.ATTR_DIFF_CONTENT), eq("exampleDiff"));
        verify(mockModel, times(2)).addAttribute(any(), any());
    }

    @DisplayName("diffView - RepositoryException thrown getting difference")
    @Test
    public void diffViewNoDiffRepositoryException() throws Exception {
        when(mockTranslationService.getDocumentDifference(eq("nodeId"))).thenThrow(new RepositoryException("message"));

        String template = differenceOpenUi.diffView("nodeId", mockModel);

        assertThat(template).isEqualTo(DifferenceOpenUi.ERROR_TEMPLATE);
        verify(mockModel).addAttribute(eq(DifferenceOpenUi.ATTR_NODE_ID), eq("nodeId"));
        verify(mockModel).addAttribute(eq(DifferenceOpenUi.ATTR_ERROR_MESSAGE), eq("message"));
        verify(mockModel).addAttribute(eq(DifferenceOpenUi.ATTR_STACK_TRACE), anyString());
        verify(mockModel, times(3)).addAttribute(any(), any());
    }

    @DisplayName("diffView - IOException thrown getting the DocumentDifference")
    @Test
    public void diffViewIOException() throws Exception {
        when(mockTranslationService.getDocumentDifference(eq("nodeId"))).thenThrow(new IOException("message"));

        String template = differenceOpenUi.diffView("nodeId", mockModel);

        assertThat(template).isEqualTo(DifferenceOpenUi.ERROR_TEMPLATE);
        verify(mockModel).addAttribute(eq(DifferenceOpenUi.ATTR_NODE_ID), eq("nodeId"));
        verify(mockModel).addAttribute(eq(DifferenceOpenUi.ATTR_ERROR_MESSAGE), eq("message"));
        verify(mockModel).addAttribute(eq(DifferenceOpenUi.ATTR_STACK_TRACE), anyString());
        verify(mockModel, times(3)).addAttribute(any(), any());
    }
}
