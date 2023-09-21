package com.visitscotland.brxm.factory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.visitscotland.brxm.dms.DMSDataService;
import com.visitscotland.brxm.dms.DMSUtils;
import com.visitscotland.brxm.hippobeans.*;
import com.visitscotland.brxm.mock.ItineraryDayMockBuilder;
import com.visitscotland.brxm.model.*;
import com.visitscotland.brxm.services.DocumentUtilsService;
import com.visitscotland.brxm.services.LinkService;
import com.visitscotland.brxm.services.ResourceBundleService;
import com.visitscotland.brxm.utils.ContentLogger;
import com.visitscotland.brxm.utils.Properties;
import com.visitscotland.utils.Contract;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItineraryFactoryTest {

    @InjectMocks
    ItineraryFactory factory;

    @Mock
    Itinerary itinerary;

    @Mock
    ResourceBundleService bundle;
    @Mock
    DMSDataService dmsData;
    @Mock
    ImageFactory imageFactory;
    @Mock
    DMSUtils utils;
    @Mock
    DocumentUtilsService documentUtils;
    @Mock
    Properties properties;
    @Mock
    LinkService linkService;
    @Mock
    ContentLogger logger;


    @Test
    @DisplayName("Create an itinerary page")
    void createSimpleItinerary() {
        when(documentUtils.getAllowedDocuments(itinerary, Day.class)).thenReturn(Collections.emptyList());

        ItineraryPage iti = factory.buildItinerary(itinerary, Locale.UK);
        assertNotNull(iti);
        assertEquals(BigDecimal.ZERO, iti.getDistance());

    }

    @Test
    @DisplayName("Create a simple itinerary with one stop")
    void simpleStop() {
        List<Day> days = new ItineraryDayMockBuilder().addStop().title("Title").subtitle("location")
                .addDescription().tip("Don't miss", true).buildAsList();

        when(documentUtils.getAllowedDocuments(itinerary, Day.class)).thenReturn(days);

        ItineraryPage iti = factory.buildItinerary(itinerary, Locale.UK);
        ItineraryStopModule module = getSingleStop(iti);
        assertNotNull(iti);
        assertEquals(1, iti.getDays().size());
        assertEquals(1, iti.getStops().size());
        assertEquals("Title", module.getTitle());
        assertEquals("location", module.getSubTitle());
        assertEquals("Don't miss", module.getTipsTitle());
        assertNotNull(module.getDescription());
        assertNotNull(module.getTipsBody());

    }

    @ParameterizedTest
    @ValueSource(doubles = {0., 500.})
    @DisplayName("Calculates distance between points and total if it hasn't been specified")
    void itineraryDistance(Double distance) {
        List<Day> days = new ArrayList<>();
        days.add(new ItineraryDayMockBuilder()
                .addExternalStop("1.1", false).coordinates(-1., -1.)
                .addExternalStop("1.2",false).coordinates(-1., 1.).build());
        days.add(new ItineraryDayMockBuilder()
                .addExternalStop("2.1",false).coordinates(1., 1.)
                .addExternalStop("2.2",false).coordinates(1., -1.).build());

        when(documentUtils.getAllowedDocuments(itinerary, Day.class)).thenReturn(days);
        when(itinerary.getDistance()).thenReturn(distance);

        ItineraryPage iti = factory.buildItinerary(itinerary, Locale.UK);


        assertEquals(2, iti.getDays().size());
        assertEquals(4, iti.getStops().size());
        assertEquals(distance == 500. ? BigDecimal.valueOf(500).setScale(0, BigDecimal.ROUND_HALF_UP) : BigDecimal.valueOf(414.9).setScale(0, BigDecimal.ROUND_HALF_UP), iti.getDistance().setScale(0, BigDecimal.ROUND_HALF_UP));
    }

    private void initFirstAndLastStopDistanceTest() {
        List<Day> days = new ArrayList<>();
        days.add(new ItineraryDayMockBuilder().addStop().subtitle("Apple").addStop().subtitle("Pear").build());
        days.add(new ItineraryDayMockBuilder().addStop().subtitle("Orange").addStop().build());
        days.add(new ItineraryDayMockBuilder().addStop().subtitle("Banana").addStop().subtitle("Pineapple").build());

        when(documentUtils.getAllowedDocuments(itinerary, Day.class)).thenReturn(days);
    }

    @Test
    @DisplayName("Determines the first and last stop location from the Stops")
    void itinerary_firstAndLastStopDistance() {
        initFirstAndLastStopDistanceTest();

        // Case 1: start = null & end = null
        ItineraryPage iti = factory.buildItinerary(itinerary, Locale.UK);
        assertEquals("Apple", iti.getFirstStopLocation());
        assertEquals("Pineapple", iti.getLastStopLocation());
    }

    @Test
    @DisplayName("Overrides the first and last stop location")
    void itinerary_firstAndLastStopDistance_override() {
        initFirstAndLastStopDistanceTest();
        when(itinerary.getStart()).thenReturn("Pomegranate");
        when(itinerary.getFinish()).thenReturn("Kiwi");


        ItineraryPage iti = factory.buildItinerary(itinerary, Locale.UK);
        assertEquals("Pomegranate", iti.getFirstStopLocation());
        assertEquals("Kiwi", iti.getLastStopLocation());
    }

    @Test
    @DisplayName("Determines the last stop location from the Stops and sets the first stop location from the itinerary")
    void itinerary_firstAndLastStopDistance_mix() {
        initFirstAndLastStopDistanceTest();
        when(itinerary.getFinish()).thenReturn("Kiwi");

        ItineraryPage iti = factory.buildItinerary(itinerary, Locale.UK);
        assertEquals("Apple", iti.getFirstStopLocation());
        assertEquals("Kiwi", iti.getLastStopLocation());
    }

    @Test
    @DisplayName("Creates a simple stop from a DMSLink")
    void dmsStop_basic() throws JsonProcessingException {
        final String JSON = "{" +
                " \"dmsLink\": {\"link\": \"/info/fake-product-p123\"}," +
                " \"name\":\"Fake Product\", " +
                " \"latitude\": 55.98129618868665, " +
                " \"longitude\": -3.1749625514667117, " +
                " \"address\": {\"city\": \"Edinburgh\"}, " +
                " \"images\":[{" +
                "    \"mediaUrl\":\"https://img.visitscotland.com/fake-product.jpg\"" +
                "}]}";
        JsonNode node = new ObjectMapper().readTree(JSON);
        List<Day> days = new ItineraryDayMockBuilder().addDmsStop("123").buildAsList();

        when(documentUtils.getAllowedDocuments(itinerary, Day.class)).thenReturn(days);
        when(dmsData.productCard("123", Locale.UK)).thenReturn(node);
//        when(properties.getDmsHost()).thenReturn("https://mock.visitscotland.com");
        when(linkService.createDmsLink(eq(Locale.UK),any(), any(), any())).thenReturn(
                new FlatLink("Find out more", "https://mock.visitscotland.com/info/fake-product-p123", LinkType.INTERNAL));

        ItineraryPage iti = factory.buildItinerary(itinerary, Locale.UK);
        assertNotNull(iti);
        assertEquals(1, iti.getStops().size());
        ItineraryStopModule module = getSingleStop(iti);
        assertTrue(Contract.isEmpty(module.getErrorMessages()));
        assertEquals(55.98129618868665, module.getCoordinates().getLatitude());
        assertEquals(-3.1749625514667117, module.getCoordinates().getLongitude());
        assertEquals("https://mock.visitscotland.com/info/fake-product-p123", module.getCtaLink().getLink());
        assertEquals("Find out more", module.getCtaLink().getLabel());
        assertEquals("Edinburgh", module.getSubTitle());
        verify(imageFactory).createImage(any(JsonNode.class), any(), any());
    }

    @Test
    @DisplayName("Itinerary Stop's Image overrides DMS Image")
    void dmsStop_imagePrecedence() throws JsonProcessingException {
        final String JSON = "{" +
                " \"dmsLink\": {\"link\": \"/info/fake-product-p123\"}," +
                " \"images\":[{" +
                "    \"mediaUrl\":\"https://img.visitscotland.com/fake-product.jpg\"" +
                "}]}";
        JsonNode node = new ObjectMapper().readTree(JSON);
        List<Day> days = new ItineraryDayMockBuilder().addDmsStop("123").addImage().buildAsList();
        FlatImage image = new FlatImage();
        image.setCmsImage(mock(Image.class));

        when(documentUtils.getAllowedDocuments(itinerary, Day.class)).thenReturn(days);
        when(dmsData.productCard("123", Locale.UK)).thenReturn(node);
        when(imageFactory.createImage(any(Image.class), any(), any())).thenReturn(image);


        ItineraryStopModule module = getSingleStop(factory.buildItinerary(itinerary, Locale.UK));
        assertNull(module.getImage().getExternalImage());
        assertNotNull(module.getImage().getCmsImage());
    }

    @ParameterizedTest
    @CsvSource({
            "0.5,stop.hours",
            "1,stop.hour",
            "2,stop.hours",
            "10,stop.hours",
    })
    @DisplayName("Text for hours depends on the number of hours")
    void dmsStop_timeToExplore(String value, String bundleKey) throws JsonProcessingException {
        final String JSON = "{" +
                " \"dmsLink\": {\"link\": \"/info/fake-product-p123\"}," +
                " \"timeToExplore\":" + value + " " +
                "}";
        JsonNode node = new ObjectMapper().readTree(JSON);
        List<Day> days = new ItineraryDayMockBuilder().addDmsStop("123").addImage().buildAsList();

        when(documentUtils.getAllowedDocuments(itinerary, Day.class)).thenReturn(days);
        when(dmsData.productCard("123", Locale.UK)).thenReturn(node);
        when(bundle.getResourceBundle(ItineraryFactory.BUNDLE_FILE, bundleKey, Locale.UK)).thenReturn("hour(s)");

        ItineraryPage iti = factory.buildItinerary(itinerary, Locale.UK);
        assertTrue(getSingleStop(iti).getTimeToExplore().contains(value));
    }

    @Test
    @DisplayName("DMSStop - Prices")
    void dmsStop_price() throws JsonProcessingException {
        final String JSON = "{" +
                " \"dmsLink\": {\"link\": \"/info/fake-product-p123\"}," +
                " \"price\": {\"displayPrice\": \"Free\"} " +
                "}";
        JsonNode node = new ObjectMapper().readTree(JSON);
        List<Day> days = new ItineraryDayMockBuilder().addDmsStop("123").addImage().buildAsList();

        when(documentUtils.getAllowedDocuments(itinerary, Day.class)).thenReturn(days);
        when(dmsData.productCard("123", Locale.UK)).thenReturn(node);

        ItineraryStopModule module = getSingleStop(factory.buildItinerary(itinerary, Locale.UK));
        assertEquals("Free", module.getPrice());
    }

    @Test
    @DisplayName("DMSStop - Opening Times")
    void dmsStop_openingTimes() throws JsonProcessingException {
        final String JSON = "{" +
                " \"dmsLink\": {\"link\": \"/info/fake-product-p123\"}," +
                " \"opening\": {}" +
                "}";
        JsonNode node = new ObjectMapper().readTree(JSON);
        List<Day> days = new ItineraryDayMockBuilder().addDmsStop("123").addImage().buildAsList();

        when(documentUtils.getAllowedDocuments(itinerary, Day.class)).thenReturn(days);
        when(dmsData.productCard("123", Locale.UK)).thenReturn(node);
        when(bundle.getResourceBundle(ItineraryFactory.BUNDLE_FILE, "stop.opening", Locale.UK)).thenReturn("show times");
//        when(properties.getDmsHost()).thenReturn("https://mock.visitscotland.com");
        when(linkService.createDmsLink(eq(Locale.UK), any(), any(), any())).thenReturn(
                new FlatLink(null, "https://mock.visitscotland.com/info/fake-product-p123", LinkType.INTERNAL));

        ItineraryStopModule module = getSingleStop(factory.buildItinerary(itinerary, Locale.UK));
        assertNotNull(module.getOpening());
        assertEquals("show times", module.getOpenLink().getLabel());
        assertEquals("https://mock.visitscotland.com/info/fake-product-p123#opening", module.getOpenLink().getLink());
    }

    @Test
    @DisplayName("Verifies that a broken Link returns a Stop with a warning")
    void dmsStopBroken() {
        List<Day> days = new ItineraryDayMockBuilder().addDmsStop("123").addImage().buildAsList();

        when(documentUtils.getAllowedDocuments(itinerary, Day.class)).thenReturn(days);
        when(dmsData.productCard("123", Locale.UK)).thenReturn(null);

        ItineraryPage iti = factory.buildItinerary(itinerary, Locale.UK);
        assertNotNull(iti);
        assertEquals(1, iti.getStops().size());
        assertEquals(1, getSingleStop(iti).getErrorMessages().size());
    }

    @Test
    void externalStop() {
        List<Day> days = new ItineraryDayMockBuilder().addExternalStop("mysite.com",false).coordinates(1., -2.).buildAsList();

        when(documentUtils.getAllowedDocuments(itinerary, Day.class)).thenReturn(days);

        ItineraryStopModule module = getSingleStop(factory.buildItinerary(itinerary, Locale.UK));

        verify(linkService, atLeastOnce()).createExternalLink(any(), any(), any());

        assertTrue(Contract.isEmpty(module.getErrorMessages()));
        assertEquals(1., module.getCoordinates().getLatitude());
        assertEquals(-2., module.getCoordinates().getLongitude());
    }

    @Test
    void externalStopWithCta() {
        List<Day> days = new ItineraryDayMockBuilder().addExternalStop("mysite.com",true).coordinates(1., -2.).buildAsList();

        when(documentUtils.getAllowedDocuments(itinerary, Day.class)).thenReturn(days);

        ItineraryStopModule module = getSingleStop(factory.buildItinerary(itinerary, Locale.UK));

        verify(linkService, atLeastOnce()).createExternalLink(any(), any(), any());

        assertTrue(Contract.isEmpty(module.getErrorMessages()));
        assertEquals(1., module.getCoordinates().getLatitude());
        assertEquals(-2., module.getCoordinates().getLongitude());
    }

    @Test
    void externalStop_timeToExplore() {
        List<Day> days = new ItineraryDayMockBuilder().addExternalStop("mysite.com",false).timeToExplore("2").buildAsList();

        when(documentUtils.getAllowedDocuments(itinerary, Day.class)).thenReturn(days);
        when(bundle.getResourceBundle(ItineraryFactory.BUNDLE_FILE, "stop.hours", Locale.UK)).thenReturn("hours");

        ItineraryStopModule module = getSingleStop(factory.buildItinerary(itinerary, Locale.UK));
        assertEquals("2 hours", module.getTimeToExplore());
    }

    private ItineraryStopModule getSingleStop(ItineraryPage page) {
        return page.getStops().values().iterator().next();
    }

    @Test
    @DisplayName("Duplicate stops on the same day are removed")
    void duplicateStops_sameDay() {
        List<Day> days = new ArrayList<>();
        days.add(new ItineraryDayMockBuilder().addStop("a").subtitle("Apple").addStop("a").build());
        Itinerary itin = mock(Itinerary.class);
        when(documentUtils.getAllowedDocuments(itin, Day.class)).thenReturn(days);

        ItineraryPage iti = factory.buildItinerary(itin, Locale.UK);

        assertEquals(1, iti.getStops().size());
        assertEquals("Apple", iti.getStops().get("a").getSubTitle());
        assertEquals(1, iti.getErrorMessages().size());
    }

    @Test
    @DisplayName("Duplicate stops across multiple are removed")
    void duplicateStops_multipleDays() {
        List<Day> days = new ArrayList<>();
        days.add(new ItineraryDayMockBuilder().addStop("a").subtitle("Apple").addStop("b").subtitle("Pear").build());
        days.add(new ItineraryDayMockBuilder().addStop("a").build());
        Itinerary itin = mock(Itinerary.class);
        when(documentUtils.getAllowedDocuments(itin, Day.class)).thenReturn(days);

        ItineraryPage iti = factory.buildItinerary(itin, Locale.UK);

        assertEquals(2, iti.getStops().size());
        assertEquals("Apple", iti.getStops().get("a").getSubTitle());
        assertEquals("Pear", iti.getStops().get("b").getSubTitle());
        assertEquals(1, iti.getErrorMessages().size());
    }

    @Test
    @DisplayName("When no cta provided for DMS link, default cta is set")
    void dmsCtaSet() throws Exception {
        final String JSON = "{" +
                " \"dmsLink\": {\"link\": \"/info/fake-product-p123\"}," +
                " \"name\":\"dms title\", " +
                " \"images\":[{" +
                "    \"mediaUrl\":\"https://img.visitscotland.com/fake-product.jpg\"" +
                "}]}";
        JsonNode node = new ObjectMapper().readTree(JSON);
        List<Day> days = new ItineraryDayMockBuilder().addDmsStop("123").title("module title").buildAsList();
        when(documentUtils.getAllowedDocuments(itinerary, Day.class)).thenReturn(days);
        when(dmsData.productCard("123", Locale.UK)).thenReturn(node);
        when(bundle.getFindOutMoreAboutCta("module title", Locale.UK)).thenReturn("Find out more about module title");

        factory.buildItinerary(itinerary, Locale.UK);

        verify(linkService).createDmsLink(Locale.UK, (DMSLink) days.get(0).getStops().get(0).getStopItem(), node, "Find out more about module title");
    }

    @Test
    @DisplayName("When no cta provided for external link, default cta is set")
    void externalCtaSet() {
        List<Day> days = new ItineraryDayMockBuilder().addExternalStop("https://example.com",false).title("title").buildAsList();
        when(documentUtils.getAllowedDocuments(itinerary, Day.class)).thenReturn(days);
        when(bundle.getFindOutMoreAboutCta("title", Locale.UK)).thenReturn("Find out more about title");

        factory.buildItinerary(itinerary, Locale.UK);

        verify(linkService).createExternalLink(Locale.UK, "https://example.com", "Find out more about title");
    }



}
