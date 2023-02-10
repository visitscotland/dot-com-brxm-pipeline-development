package com.visitscotland.brxm.factory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.visitscotland.brxm.dms.DMSDataService;
import com.visitscotland.brxm.hippobeans.Page;
import com.visitscotland.brxm.hippobeans.SkiCentre;
import com.visitscotland.brxm.hippobeans.SkiCentreList;
import com.visitscotland.brxm.model.SkiListModule;
import com.visitscotland.brxm.model.SkiModule;
import com.visitscotland.brxm.model.megalinks.EnhancedLink;
import com.visitscotland.brxm.services.DocumentUtilsService;
import com.visitscotland.brxm.services.LinkService;
import com.visitscotland.brxm.services.ResourceBundleService;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Locale;
import org.slf4j.Logger;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SkiFactoryTest {

    // Real sample taken form Cairngorm
    final String SAMPLE = "{\"data\": {\"id\":\"2165531\",\"name\":\"Cairngorm Mountain (Scotland) Ltd\",\"category\":[{\"id\":\"nationalparks\",\"name\":\"National Parks\"}],\"images\":[{\"mediaType\":\"PICTURE\",\"mediaUrl\":\"http://www.visitscotland.com/wsimgs/Mountain_view_1345279508.jpg\",\"altText\":\"View from Cairngorm Mountain\",\"copyright\":\"Cairngorm Mountain\",\"default\":true}],\"address\":{\"line1\":\"Cairngorm Ski Area\",\"city\":\"Aviemore\",\"county\":\"The Highlands\",\"postCode\":\"PH22 1RB\",\"shortAddress\":\"Aviemore, The Highlands\",\"streetAddress\":\"Cairngorm Ski Area\",\"empty\":false},\"productLink\":{\"label\":\"View Details\",\"link\":\"/info/see-do/cairngorm-mountain-scotland-ltd-p2165531\",\"type\":\"INTERNAL\"},\"website\":{\"label\":\"Visit Website\",\"link\":\"https://www.cairngormmountain.co.uk\",\"type\":\"EXTERNAL\"},\"price\":{\"displayPrice\":\"Admission charge\"},\"keyFacilities\":[{\"id\":\"wifi\",\"name\":\"WiFi\"},{\"id\":\"petswelcom\",\"name\":\"Pets Welcome\"},{\"id\":\"accessparkdrop\",\"name\":\"Accessible Parking Or Drop-off Point\"},{\"id\":\"accesstoliet\",\"name\":\"Accessible toilets\"},{\"id\":\"pubtranrte\",\"name\":\"On Public Transport Route\"},{\"id\":\"parking\",\"name\":\"Parking\"},{\"id\":\"cafereston\",\"name\":\"Cafe or Restaurant\"}],\"opening\":{\"openNewYear\":\"Open over New Year\",\"extraDescription\":\"Café and Mountain Shop open 9.30am to 4pm.  As a year round visitor attraction we are open 7 days a week with the exception of Christmas Day\"},\"latitude\":57.13367,\"longitude\":-3.67027,\"telephoneNumber\":\"+44 1479 861261\",\"socialChannels\":[{\"label\":\"Facebook\",\"link\":\"https://www.facebook.com/cairngormmountainscotlandltd/\",\"type\":\"EXTERNAL\"},{\"label\":\"Instagram\",\"link\":\"https://www.instagram.com/cairngormoperationsupdates/\",\"type\":\"EXTERNAL\"}]}}";

    @Mock
    ResourceBundleService bundle;

    @Mock
    DMSDataService dataService;

    @Mock
    DocumentUtilsService documentUtils;

    @Mock
    LinkService linkService;

    @Mock
    Logger logger;

    @InjectMocks
    SkiFactory skiFactory;

    @DisplayName("VS-4378 - SkiCentre - Create Basic Module")
    @Test
    void createModule(){
        SkiCentre document = mock(SkiCentre.class);

        Assertions.assertNotNull(skiFactory.createSkyModule(document, Locale.UK));
    }

    @DisplayName("VS-4378 - SkiCentre - Create Module with all Fields")
    @Test
    void allFields(){
        SkiCentre document = mock(SkiCentre.class);
        HippoHtml description = mock(HippoHtml.class);

        when(document.getTitle()).thenReturn("Title");
        when(document.getCopy()).thenReturn(description);
        when(document.getFeed()).thenReturn("http://www.ski.scot/cairngorm");
        when(document.getProductId()).thenReturn("NOT-VALID");
        when(document.getPisteMap()).thenReturn("map.pdf");

        SkiModule module = skiFactory.createSkyModule(document, Locale.UK);

        Assertions.assertEquals("Title", module.getTitle());
        Assertions.assertEquals("http://www.ski.scot/cairngorm", module.getFeedURL());
        Assertions.assertEquals(description, document.getCopy());
        Assertions.assertEquals("map.pdf", module.getPisteMap());
    }

    @DisplayName("VS-4378 - SkiCentre - Populate information from the DMS")
    @Test
    void dmsData() throws JsonProcessingException {
        SkiCentre document = mock(SkiCentre.class);

        when(document.getProductId()).thenReturn("2165531");
        when(dataService.productCard("2165531", Locale.UK)).thenReturn(new ObjectMapper().readTree(SAMPLE).get("data"));

        SkiModule module = skiFactory.createSkyModule(document, Locale.UK);

        Assertions.assertEquals("+44 1479 861261", module.getPhone());
        Assertions.assertTrue(module.getWebsite().toString().contains("https://www.cairngormmountain.co.uk"));
        Assertions.assertTrue(module.getAddress().toString().contains("Aviemore"));
        Assertions.assertEquals("/info/see-do/cairngorm-mountain-scotland-ltd-p2165531#opening", module.getOpeningLink().getLink());
        Assertions.assertEquals(2, module.getSocialChannels().size());
        Assertions.assertTrue(module.getSocialChannels().get(0).toString().contains("https://www.facebook.com/cairngormmountainscotlandltd"));
    }

    @DisplayName("VS-4378 - SkiCentre - Flag error when the DMS is not valid")
    @Test
    void dms_notValid() {
        SkiCentre document = mock(SkiCentre.class);

        when(document.getProductId()).thenReturn("NOT-VALID-ID");

        SkiModule module = skiFactory.createSkyModule(document, Locale.UK);

        Assertions.assertEquals(1, module.getErrorMessages().size());
        Assertions.assertTrue(module.getErrorMessages().get(0).contains("NOT-VALID-ID"));
    }

    @DisplayName("VS-4377 - SkiCentreList - Most basic Module")
    @Test
    void skiCentreList(){
        SkiCentreList document = mock(SkiCentreList.class);

        Assertions.assertNotNull(skiFactory.createSkyListModule(document, Locale.UK));
    }


    @DisplayName("VS-4377 - SkiCentreList - Main Fields")
    @Test
    void skiCentreList_fields(){
        SkiCentreList document = mock(SkiCentreList.class);

        when(document.getTitle()).thenReturn("title");
        when(document.getCopy()).thenReturn(mock(HippoHtml.class));

        SkiListModule module = skiFactory.createSkyListModule(document, Locale.UK);

        Assertions.assertEquals("title", module.getTitle());
        Assertions.assertNotNull(module.getIntroduction());
    }

    @DisplayName("VS-4377 - SkiCentreList - Associated Ski Centres are linked")
    @Test
    void skiCentreList_skiCentre(){
        SkiCentreList document = mock(SkiCentreList.class);
        Page page = mock(Page.class);

        when(document.getMegalinkItems()).thenReturn(Collections.singletonList(page));
        when(documentUtils.getSiblingDocuments(page, SkiCentre.class, "visitscotland:SkiCentre"))
                .thenReturn(Collections.singletonList(mock(SkiCentre.class)));
        when(linkService.createEnhancedLink(any(), any(), any(), anyBoolean()))
                .thenReturn(java.util.Optional.of(new EnhancedLink()));

        SkiListModule module = skiFactory.createSkyListModule(document, Locale.UK);

        Assertions.assertEquals(1, module.getSkiCentres().size());
    }

    @DisplayName("VS-4377 - SkiCentreList - Pages with no ski module don't cause any problem but they are flagged (No Ski Module)")
    @Test
    void skiCentreList_broken_skiCentre(){
        SkiCentreList document = mock(SkiCentreList.class);
        Page skiPage = mock(Page.class);
        Page other = mock(Page.class);

        when(other.getDisplayName()).thenReturn("Snow Factor");
        when(document.getMegalinkItems()).thenReturn(Lists.newArrayList(skiPage, other, skiPage));
        when(documentUtils.getSiblingDocuments(skiPage, SkiCentre.class, "visitscotland:SkiCentre"))
                .thenReturn(Collections.singletonList(mock(SkiCentre.class)));
        when(documentUtils.getSiblingDocuments(other, SkiCentre.class, "visitscotland:SkiCentre"))
                .thenReturn(Collections.emptyList());
        when(linkService.createEnhancedLink(any(), any(), any(), anyBoolean()))
                .thenReturn(java.util.Optional.of(new EnhancedLink()));

        SkiListModule module = skiFactory.createSkyListModule(document, Locale.UK);

        Assertions.assertEquals(2, module.getSkiCentres().size());
        Assertions.assertEquals(1, module.getErrorMessages().size());
        Assertions.assertTrue(module.getErrorMessages().get(0).contains("Snow Factor"));
    }

    @DisplayName("VS-4377 - SkiCentreList - Pages with no ski module don't cause any problem but they are flagged (Too many Ski Modules)")
    @Test
    void skiCentreList_multiple_skiCentre(){
        SkiCentreList document = mock(SkiCentreList.class);
        Page skiPage = mock(Page.class);

        when(skiPage.getDisplayName()).thenReturn("Nevis Range");
        when(document.getMegalinkItems()).thenReturn(Collections.singletonList(skiPage));
        when(documentUtils.getSiblingDocuments(skiPage, SkiCentre.class, "visitscotland:SkiCentre"))
                .thenReturn(Lists.newArrayList(mock(SkiCentre.class), mock(SkiCentre.class), mock(SkiCentre.class)));
        when(linkService.createEnhancedLink(any(), any(), any(), anyBoolean()))
                .thenReturn(java.util.Optional.of(new EnhancedLink()));

        SkiListModule module = skiFactory.createSkyListModule(document, Locale.UK);

        Assertions.assertEquals(1, module.getSkiCentres().size());
        Assertions.assertEquals(1, module.getErrorMessages().size());
        Assertions.assertTrue(module.getErrorMessages().get(0).contains("Nevis Range"));
    }
}