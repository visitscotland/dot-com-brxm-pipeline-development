package com.visitscotland.brxm.report.translation;

import com.visitscotland.brxm.translation.MockJcrDocumentBuilder;
import com.visitscotland.brxm.translation.MockNodeBuilder;
import com.visitscotland.brxm.translation.SessionFactory;
import com.visitscotland.brxm.translation.plugin.JcrDocument;
import com.visitscotland.brxm.translation.plugin.JcrDocumentFactory;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;
import java.util.stream.Stream;
import javax.jcr.Node;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TranslationReportServiceTest {
    private TranslationReportService service;
    @Mock
    private SessionFactory mockSessionFactory;
    @Mock
    private JcrDocumentFactory mockJcrDocumentFactory;
    @Mock
    private JcrUtilService mockJcrUtilService;


    @BeforeEach
    public void beforeEach() {
        service = new TranslationReportService(mockSessionFactory, mockJcrDocumentFactory, mockJcrUtilService);
    }

    @ParameterizedTest
    @DisplayName("Items with translation flag true should have sent for translation status")
    @MethodSource("translationStatusTestArguments")
    void whenTranslationFlagTrue_thenItemStatusSentForTranslation(Boolean translationFlag, String expectedStatus, Collection<String> translatedLocales, Collection<String> sentForTranslationLocales) throws Exception {
        Node englishHandle = new MockNodeBuilder().withProperty("hippotranslation:locale", "en").build();
        MockNodeBuilder frenchHandleBuilder = new MockNodeBuilder().withProperty("hippotranslation:locale", "fr");
        Node frenchHandle = translationFlag == null ? frenchHandleBuilder.build() :  frenchHandleBuilder.withTranslationFlag(translationFlag).build();
        Node englishUnpublishedVariant = new MockNodeBuilder().translatable("visitscotland:Page").withState("unpublished", "live").build();
        JcrDocument frenchJcrDoc = new MockJcrDocumentBuilder().withHandle(frenchHandle).withLocaleName("fr").build();
        JcrDocument englishJcrDoc = new MockJcrDocumentBuilder().withHandle(englishHandle)
                .withVariantNode("unpublished", englishUnpublishedVariant)
                .withTranslations(frenchJcrDoc).build();
        doReturn(Collections.singletonList(englishJcrDoc))
                .when(mockJcrUtilService).getAllUnpublishedDocuments();

        List<DocumentTranslationReportModel> models = service.getUntranslatedDocuments("fr");

        if (expectedStatus == null) {
            Assertions.assertEquals(0, models.size());
        } else {
            Assertions.assertEquals(1, models.size());
            DocumentTranslationReportModel model = models.get(0);
            Assertions.assertEquals(expectedStatus, model.getTranslationStatus());
            MatcherAssert.assertThat(model.getTranslatedLocales(), is(translatedLocales));
            MatcherAssert.assertThat(model.getSentForTranslationLocales(), is(sentForTranslationLocales));
        }
    }

    private static Stream<Arguments> translationStatusTestArguments() {
        return Stream.of(
                Arguments.of(true, "Sent for translation",Collections.singletonList("en"), Collections.singletonList("fr")),
                Arguments.of(null, "Untranslated", Collections.singletonList("en"), Collections.emptyList()),
                Arguments.of(false, null, Collections.emptyList(), Collections.emptyList())
        );
    }

    @DisplayName("When translation name does not exist, return document as untranslated")
    @Test
    void whenTranslationDoesNotExist_documentReturnedAsUntranslated() throws Exception {
        Node englishHandle = new MockNodeBuilder().withProperty("hippotranslation:locale", "en").build();
        Node englishUnpublishedVariant = new MockNodeBuilder().translatable("visitscotland:Page").withState("unpublished", "live").build();
        JcrDocument englishJcrDoc = new MockJcrDocumentBuilder().withHandle(englishHandle)
                .withVariantNode("unpublished", englishUnpublishedVariant).build();
        doReturn(Collections.singletonList(englishJcrDoc))
                .when(mockJcrUtilService).getAllUnpublishedDocuments();

        List<DocumentTranslationReportModel> models = service.getUntranslatedDocuments("fr");

        Assertions.assertEquals(1, models.size());
        DocumentTranslationReportModel model = models.get(0);
        // Document returned as it has not been translated into French. No clone exists so is Untranslated
        Assertions.assertEquals("Untranslated", model.getTranslationStatus());
        MatcherAssert.assertThat(model.getTranslatedLocales(), is(Collections.singletonList("en")));
        MatcherAssert.assertThat(model.getSentForTranslationLocales(), is(Collections.emptyList()));
    }

    @DisplayName("Publish status is correctly identified")
    @ParameterizedTest
    @MethodSource("publishStatusTestMethodSource")
    void publishStatusIdentifiedOnRecords(String hippoState, PublishStatus publishStatus) throws Exception {
        Node englishHandle = new MockNodeBuilder().withProperty("hippotranslation:locale", "en").build();
        Node englishUnpublishedVariant = new MockNodeBuilder().translatable("visitscotland:Page").withState("unpublished", hippoState).build();
        JcrDocument englishJcrDoc = new MockJcrDocumentBuilder().withHandle(englishHandle)
                .withVariantNode("unpublished", englishUnpublishedVariant).build();
        doReturn(Collections.singletonList(englishJcrDoc))
                .when(mockJcrUtilService).getAllUnpublishedDocuments();

        List<DocumentTranslationReportModel> models = service.getUntranslatedDocuments("fr");

        Assertions.assertEquals(1, models.size());
        Assertions.assertEquals(publishStatus, models.get(0).getPublishStatus());
    }

    private static Stream<Arguments> publishStatusTestMethodSource() {
        return Stream.of(
                Arguments.of("changed", PublishStatus.PREV_VERSION_LIVE),
                Arguments.of("live", PublishStatus.CURR_VERSION_LIVE),
                Arguments.of("new", PublishStatus.NOT_LIVE)
        );
    }



}
