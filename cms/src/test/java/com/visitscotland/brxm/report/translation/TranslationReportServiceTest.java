package com.visitscotland.brxm.report.translation;

import com.visitscotland.brxm.translation.MockJcrDocumentBuilder;
import com.visitscotland.brxm.translation.MockNodeBuilder;
import com.visitscotland.brxm.translation.SessionFactory;
import com.visitscotland.brxm.translation.plugin.JcrDocument;
import com.visitscotland.brxm.translation.plugin.JcrDocumentFactory;
import org.assertj.core.util.Sets;
import org.codehaus.jackson.map.util.StdDateFormat;
import org.hamcrest.MatcherAssert;
import org.hippoecm.repository.HippoStdNodeType;
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

import java.time.ZoneId;
import java.time.ZonedDateTime;
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
    @DisplayName("Items with a clone have correct translation status")
    @MethodSource("translationStatusTestArguments")
    void translationStatusItemsWithClone(Boolean translationFlag, TranslationStatus expectedStatus, Collection<String> translatedLocales, Collection<String> sentForTranslationLocales) throws Exception {
        Node englishHandle = new MockNodeBuilder().withProperty("hippotranslation:locale", "en").build();
        MockNodeBuilder frenchHandleBuilder = new MockNodeBuilder().withProperty("hippotranslation:locale", "fr").withNodeType("hippo:handle");
        Node frenchUnpublished = new MockNodeBuilder().withNodeType(HippoStdNodeType.NT_PUBLISHABLE).withState("unpublished", "live").withTranslationFlag(translationFlag).build();
        Node frenchHandle = translationFlag == null ? frenchHandleBuilder.build() :  frenchHandleBuilder.withChildNode("test", frenchUnpublished).build();
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
            Assertions.assertEquals(expectedStatus.toString(), model.getTranslationStatus());
            MatcherAssert.assertThat(model.getTranslatedLocales(), is(new HashSet<>(translatedLocales)));
            MatcherAssert.assertThat(model.getSentForTranslationLocales(), is(new HashSet<>(sentForTranslationLocales)));
        }
    }

    @Test
    @DisplayName("Item with no translation clone has untranslated status")
    void untranslatedStatus() throws Exception{
        Node englishHandle = new MockNodeBuilder().withProperty("hippotranslation:locale", "en").build();
        Node englishUnpublishedVariant = new MockNodeBuilder().translatable("visitscotland:Page").withState("unpublished", "live").build();
        JcrDocument englishJcrDoc = new MockJcrDocumentBuilder().withHandle(englishHandle)
                .withVariantNode("unpublished", englishUnpublishedVariant).build();
        doReturn(Collections.singletonList(englishJcrDoc))
                .when(mockJcrUtilService).getAllUnpublishedDocuments();

        List<DocumentTranslationReportModel> models = service.getUntranslatedDocuments("fr");

        DocumentTranslationReportModel model = models.get(0);
        Assertions.assertEquals(TranslationStatus.NOT_SENT_FOR_TRANSLATION.toString(), model.getTranslationStatus());
    }


    private static Stream<Arguments> translationStatusTestArguments() {
        return Stream.of(
                Arguments.of(true, TranslationStatus.SEND_FOR_TRANSLATION,Collections.singletonList("en"), Collections.singletonList("fr")),
                Arguments.of(null, TranslationStatus.NOT_SENT_FOR_TRANSLATION, Collections.singletonList("en"), Collections.emptyList()),
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
        MatcherAssert.assertThat(model.getTranslatedLocales(), is(new HashSet<>(Collections.singletonList("en"))));
        MatcherAssert.assertThat(model.getSentForTranslationLocales(), is(new HashSet<>(Collections.emptyList())));
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

    @DisplayName("Document last modified date formatted correctly")
    @Test
    void documentDateFormat() throws Exception  {
        Node englishHandle = new MockNodeBuilder().withProperty("hippotranslation:locale", "en").build();
        Node englishUnpublishedVariant = new MockNodeBuilder().translatable("visitscotland:Page")
                .withState("unpublished", "live")
                .lastModifiedAt(ZonedDateTime.of(2021, 10, 12, 18, 14, 15, 50, ZoneId.of("GMT"))).build();
        JcrDocument englishJcrDoc = new MockJcrDocumentBuilder().withHandle(englishHandle)
                .withVariantNode("unpublished", englishUnpublishedVariant).build();
        doReturn(Collections.singletonList(englishJcrDoc))
                .when(mockJcrUtilService).getAllUnpublishedDocuments();

        List<DocumentTranslationReportModel> models = service.getUntranslatedDocuments("fr");

        Assertions.assertEquals(1, models.size());
        DocumentTranslationReportModel doc = models.get(0);
        Assertions.assertEquals("2021-10-12T18:14Z", doc.getLastModified());
    }

    @DisplayName("Types deriving basedocument are included in module types, excluding Page")
    @Test
    void getModuleTypes() throws Exception {
        when(mockJcrUtilService.getTypesDeriving("visitscotland:basedocument"))
                .thenReturn(Sets.newTreeSet("Page", "A", "B"));

        Set<String> moduleTypes = service.getModuleTypes();

        Assertions.assertEquals(2, moduleTypes.size());
        Assertions.assertTrue(moduleTypes.contains("A"));
        Assertions.assertTrue(moduleTypes.contains("B"));

        // Ensure that the result is cached and not queried twice
        service.getModuleTypes();
        verify(mockJcrUtilService, times(1)).getTypesDeriving(any());
    }

    @DisplayName("Page types are returned and cached")
    @Test
    void getPageTypesTest() throws Exception {
        when(mockJcrUtilService.getTypesDeriving("visitscotland:Page"))
                .thenReturn(Sets.newTreeSet("A"));

        Set<String> pageTypes = service.getPageTypes();

        Assertions.assertEquals(1, pageTypes.size());
        Assertions.assertTrue(pageTypes.contains("A"));
        // Ensure that the result is cached and not queried twice
        service.getPageTypes();
        verify(mockJcrUtilService, times(1)).getTypesDeriving(any());
    }

    @DisplayName("Translation priority")
    @MethodSource("translationPrioritySource")
    @ParameterizedTest
    void translationPriority(String translationPriority, String expectedModelPriority) throws Exception {
        Node englishHandle = new MockNodeBuilder().withProperty("hippotranslation:locale", "en").build();
        Node englishUnpublishedVariant = new MockNodeBuilder().translatable("visitscotland:Page")
                .withState("unpublished", "live")
                .withProperty("visitscotland:translationPriority", translationPriority).build();
        JcrDocument englishJcrDoc = new MockJcrDocumentBuilder().withHandle(englishHandle)
                .withVariantNode("unpublished", englishUnpublishedVariant).build();
        doReturn(Collections.singletonList(englishJcrDoc))
                .when(mockJcrUtilService).getAllUnpublishedDocuments();

        List<DocumentTranslationReportModel> models = service.getUntranslatedDocuments("fr");

        Assertions.assertEquals(1, models.size());
        DocumentTranslationReportModel document = models.get(0);
        Assertions.assertEquals(expectedModelPriority, document.getTranslationPriority());
    }

    @DisplayName("Translation deadline model")
    @Test
    void translationDeadline() throws Exception {
        Calendar deadline = Calendar.getInstance();
        deadline.setTime(new StdDateFormat().parse("2050-10-10T10:20:30Z"));
        Node englishHandle = new MockNodeBuilder().withProperty("hippotranslation:locale", "en").build();
        Node englishUnpublishedVariant = new MockNodeBuilder().translatable("visitscotland:Page")
                .withState("unpublished", "live")
                .withProperty("visitscotland:translationDeadline", deadline).build();
        JcrDocument englishJcrDoc = new MockJcrDocumentBuilder().withHandle(englishHandle)
                .withVariantNode("unpublished", englishUnpublishedVariant).build();
        doReturn(Collections.singletonList(englishJcrDoc))
                .when(mockJcrUtilService).getAllUnpublishedDocuments();
        List<DocumentTranslationReportModel> models = service.getUntranslatedDocuments("fr");

        Assertions.assertEquals(1, models.size());
        DocumentTranslationReportModel document = models.get(0);
        Assertions.assertEquals("2050-10-10T10:20Z", document.getTranslationDeadline());
    }

    private static Stream<Arguments> translationPrioritySource() {
        return Stream.of(
                Arguments.of("HIGH", "HIGH"),
                Arguments.of("NORMAL", "NORMAL"),
                Arguments.of("LOW", "LOW"),
                Arguments.of(null, "NORMAL"),
                Arguments.of("NOT_VALID", "NORMAL"),
                Arguments.of("", "NORMAL")
        );
    }

}
