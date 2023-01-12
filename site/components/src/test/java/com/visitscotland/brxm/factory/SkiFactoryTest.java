package com.visitscotland.brxm.factory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.visitscotland.brxm.dms.DMSDataService;
import com.visitscotland.brxm.hippobeans.SkiCentre;
import com.visitscotland.brxm.model.SkiModule;
import com.visitscotland.brxm.services.ResourceBundleService;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Locale;
import org.slf4j.Logger;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SkiFactoryTest {

    // Real sample taken form Cairngorm
    final String SAMPLE = "{\"data\": {\"id\":\"2165531\",\"name\":\"Cairngorm Mountain (Scotland) Ltd\",\"category\":[{\"id\":\"nationalparks\",\"name\":\"National Parks\"}],\"images\":[{\"mediaType\":\"PICTURE\",\"mediaUrl\":\"http://www.visitscotland.com/wsimgs/Mountain_view_1345279508.jpg\",\"altText\":\"View from Cairngorm Mountain\",\"copyright\":\"Cairngorm Mountain\",\"default\":true}],\"address\":{\"line1\":\"Cairngorm Ski Area\",\"city\":\"Aviemore\",\"county\":\"The Highlands\",\"postCode\":\"PH22 1RB\",\"shortAddress\":\"Aviemore, The Highlands\",\"streetAddress\":\"Cairngorm Ski Area\",\"empty\":false},\"productLink\":{\"label\":\"View Details\",\"link\":\"/info/see-do/cairngorm-mountain-scotland-ltd-p2165531\",\"type\":\"INTERNAL\"},\"website\":{\"label\":\"Visit Website\",\"link\":\"https://www.cairngormmountain.co.uk\",\"type\":\"EXTERNAL\"},\"price\":{\"displayPrice\":\"Admission charge\"},\"keyFacilities\":[{\"id\":\"wifi\",\"name\":\"WiFi\"},{\"id\":\"petswelcom\",\"name\":\"Pets Welcome\"},{\"id\":\"accessparkdrop\",\"name\":\"Accessible Parking Or Drop-off Point\"},{\"id\":\"accesstoliet\",\"name\":\"Accessible toilets\"},{\"id\":\"pubtranrte\",\"name\":\"On Public Transport Route\"},{\"id\":\"parking\",\"name\":\"Parking\"},{\"id\":\"cafereston\",\"name\":\"Cafe or Restaurant\"}],\"opening\":{\"openNewYear\":\"Open over New Year\",\"extraDescription\":\"Café and Mountain Shop open 9.30am to 4pm.  As a year round visitor attraction we are open 7 days a week with the exception of Christmas Day\"},\"latitude\":57.13367,\"longitude\":-3.67027,\"telephoneNumber\":\"+44 1479 861261\",\"socialChannels\":[{\"label\":\"Facebook\",\"link\":\"https://www.facebook.com/cairngormmountainscotlandltd/\",\"type\":\"EXTERNAL\"},{\"label\":\"Instagram\",\"link\":\"https://www.instagram.com/cairngormoperationsupdates/\",\"type\":\"EXTERNAL\"}]}}";

    @Mock
    ResourceBundleService bundle;

    @Mock
    DMSDataService dataService;

    @Mock
    Logger logger;

    @InjectMocks
    SkiFactory skiFactory;

    @DisplayName("Create Basic Module")
    @Test
    void createModule(){
        SkiCentre document = mock(SkiCentre.class);

        Assertions.assertNotNull(skiFactory.createSkyModule(document, Locale.UK));
    }

    @DisplayName("Create Module with all Fields")
    @Test
    void allFields(){
        SkiCentre document = mock(SkiCentre.class);
        HippoHtml description = mock(HippoHtml.class);

        when(document.getTitle()).thenReturn("Title");
        when(document.getCopy()).thenReturn(description);
        when(document.getFeed()).thenReturn("http://www.ski.scot/cairngorm");
        when(document.getProductId()).thenReturn("NOT-VALID");

        SkiModule module = skiFactory.createSkyModule(document, Locale.UK);

        Assertions.assertEquals("Title", module.getTitle());
        Assertions.assertEquals("http://www.ski.scot/cairngorm", module.getFeedURL());
        Assertions.assertEquals(description, document.getCopy());
    }

    @DisplayName("Populate information from the DMS")
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

    @DisplayName("Flag error when the DMS is not valid")
    @Test
    void dms_notValid() {
        SkiCentre document = mock(SkiCentre.class);

        when(document.getProductId()).thenReturn("NOT-VALID-ID");

        SkiModule module = skiFactory.createSkyModule(document, Locale.UK);

        Assertions.assertEquals(1, module.getErrorMessages().size());
        Assertions.assertTrue(module.getErrorMessages().get(0).contains("NOT-VALID-ID"));
    }


}