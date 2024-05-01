package com.visitscotland.brxm.dms;

import com.visitscotland.brxm.hippobeans.ProductsSearch;
import com.visitscotland.brxm.dms.model.LocationObject;
import com.visitscotland.brxm.utils.Language;
import com.visitscotland.brxm.utils.Properties;
import com.visitscotland.brxm.utils.VsException;
import com.visitscotland.utils.Contract;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Locale;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static com.visitscotland.brxm.dms.DMSConstants.ProductSearch.*;

@ExtendWith(MockitoExtension.class)
class ProductSearchBuilderTest {

    private final static String DEFAULT_TYPE = "cate";

    @Mock
    Properties properties;

    @Mock
    LocationLoader locationLoader;

    @InjectMocks
    ProductSearchBuilder builder;

    private ProductSearchBuilder createBuilder(){
        return builder;
    }

    @Test()
    @DisplayName("A Search with no Product Type should throw an exception")
    void noProductType() {
        ProductSearchBuilder productSearchBuilder = createBuilder();
        assertThrows(VsException.class, () -> productSearchBuilder.build());
    }

    @Test
    @DisplayName("Most basic ProductSearch Query")
    void basic() {
        String url = createBuilder().productTypes(DEFAULT_TYPE).build();

        validateUrl(url);
        assertFalse(Contract.isEmpty(url), "A basic URL couldn't be build");
    }

    @Test()
    @DisplayName("Constructs relative URLs &&  add the product type path variable")
    void productSearch() {
        String url = createBuilder().productTypes(DEFAULT_TYPE).build();

        assertTrue(
                url.contains(String.format(DMSConstants.PRODUCT_SEARCH, DMSConstants.PATH_FOOD_DRINK)),
                String.format("The Generated URL is expected to contain " + DMSConstants.PRODUCT_SEARCH + " (%s) ", DMSConstants.PATH_FOOD_DRINK, url));
        assertTrue(url.startsWith("/info"),"The URL is expected to be relative");
        validateUrl(url);
    }

    @Test()
    @DisplayName("Accommodation searches can include availability (avail)")
    void productSearchAccommodation() {
        String url = createBuilder().productTypes("acco").build();

        assertTrue(
                url.contains(AVAILABILITY), String.format("The Generated URL is expected to contain the parameter %s (%s) ", AVAILABILITY, url)
                );
        validateUrl(url);
    }

    @Test
    @DisplayName("Undefined product types are considered on the see-do group")
    void productSearchUndefinedType() {
        final String TYPE = "test";
        String url = createBuilder().productTypes(TYPE).build();

        assertTrue(
                url.contains(DMSConstants.PATH_SEE_DO),
                String.format("The Generated URL is expected to contain the url path %s ", DMSConstants.PATH_SEE_DO)
                );
    }

    @Test
    @DisplayName("Location - Parameters when the location is a point")
    void location() {
        final String LOCATION = "Edinburgh";

        mockLocationLoader(LOCATION);

        String url = createBuilder().productTypes(DEFAULT_TYPE)
                .location(LOCATION)
                .build();
        assertTrue(url.contains(LOCATION_NAME_PARAM + "=" + LOCATION) && url.contains(LOCATION_PLACE_PARAM + "="),
                String.format("The Generated URL is expected to contain %s=%s and %s={some id} (%s) ", LOCATION_NAME_PARAM, LOCATION, LOCATION_PLACE_PARAM, url)
                );
        validateUrl(url);
    }

    @Test
    @DisplayName("Location - Parameter when the location is a polygon")
    void locationPolygon() {
        final String LOCATION = "The Highlands";

        LocationObject loc = new LocationObject();
        loc.setType("POLYGON");
        loc.setName(LOCATION);
        loc.setKey("555");
        when(locationLoader.getLocation(LOCATION, null)).thenReturn(loc);

        String url = createBuilder().productTypes(DEFAULT_TYPE)
                .location(LOCATION)
                .build();
        assertTrue(
                url.contains(LOCATION_NAME_PARAM + "=") && url.contains(LOCATION_POLYGON_PARAM + "="),
                String.format("The Generated URL is expected to contain the parameters '%s' and '%s' (%s) ", LOCATION_NAME_PARAM, LOCATION_POLYGON_PARAM, url)
                );
        validateUrl(url);
    }

    @Test
    @DisplayName("Location - Translates the Display name in the url (loc parameter)")
    void locationTranslation() {
        final String LOCATION = "Edinburgh";

        mockLocationLoader(LOCATION);
        mockLocationLoader(LOCATION,Locale.forLanguageTag("es-es"), "Edimburgo" );

        String url = createBuilder().productTypes(DEFAULT_TYPE)
                .location(LOCATION)
                .locale("es-es")
                .build();

        validateUrl(url);
        assertTrue(
                url.contains(LOCATION_NAME_PARAM + "=Edimburgo"),
                String.format("The Generated URL is expected to contain the parametersloc=Edimburgo (%s) ",  url)
                );
    }

    @Test
    @DisplayName("Category - A single parameters is included")
    void category() {
        String url = createBuilder().productTypes(DEFAULT_TYPE)
                .category("cat1")
                .build();

        validateUrl(url);
        assertTrue(
                url.contains(CATEGORY_PARAM + "=cat1"),
                String.format("The Generated URL is expected to contain %s=cat1 (%s) ", CATEGORY_PARAM, url)
                );

    }

    @Test
    @DisplayName("Category - The single parameters are included")
    void category_multiple() {
        String url = createBuilder().productTypes(DEFAULT_TYPE)
                .category("cat1")
                .category("cat2")
                .build();

        validateUrl(url);
        assertTrue(
                url.contains(CATEGORY_PARAM + "=cat1") && url.contains(CATEGORY_PARAM + "=cat2"),
                String.format("The Generated URL is expected to contain two categories (%s) ", url)
                );
    }

    @Test
    @DisplayName("Category - Invalid parameters are omitted")
    void category_invalid() {
        String url = createBuilder().productTypes(DEFAULT_TYPE)
                .category("")
                .category((String) null)
                .category(" \t \n ")
                .category("cat1&cat2")
                .build();

        validateUrl(url);
        assertFalse(
                url.contains(CATEGORY_PARAM + "="),
                String.format("The Generated URL is expected to contain no categories (%s) ", url)
                );
    }

    @Test
    @DisplayName("Awards - A single parameters is included")
    void awards() {
        String url = createBuilder().productTypes(DEFAULT_TYPE)
                .award("1")
                .build();

        validateUrl(url);
        assertTrue(
                url.contains(AWARD_PARAM + "=1"),
                String.format("The Generated URL is expected to contain %s=1 (%s) ", AWARD_PARAM, url)
                );
    }

    @Test
    @DisplayName("Awards - The single parameters are included")
    void awards_multiple() {
        String url = createBuilder().productTypes(DEFAULT_TYPE)
                .award("1").award("2")
                .build();

        validateUrl(url);
        assertTrue(
                url.contains(AWARD_PARAM + "=1") && url.contains(AWARD_PARAM + "=2"),
                String.format("The Generated URL is expected to contain two awards (%s) ", url)
                );
    }

    @Test
    @DisplayName("Awards - Invalid parameters are omitted")
    void awards_invalid() {
        String url = createBuilder().productTypes(DEFAULT_TYPE)
                .award("")
                .award((String) null)
                .award(" \t \n ")
                .award("1&2")
                .build();

        validateUrl(url);
        assertFalse(
                url.contains(AWARD_PARAM + "="),
                String.format("The Generated URL is expected to contain no awards (%s) ", url)
                );
    }

    @Test
    @DisplayName("Facilities - A single parameters is included")
    void facility() {
        String url = createBuilder().productTypes(DEFAULT_TYPE)
                .facility("1")
                .build();

        validateUrl(url);
        assertTrue(
                url.contains(FACILITY_PARAM + "=1"),
                String.format("The Generated URL is expected to contain %s=1 (%s) ", FACILITY_PARAM, url)
                );

    }

    @Test
    @DisplayName("Facilities - The single parameters are included")
    void facility_multiple() {
        String url = createBuilder().productTypes(DEFAULT_TYPE)
                .facility("1")
                .facility("2")
                .build();

        validateUrl(url);
        assertTrue(
                url.contains(FACILITY_PARAM + "=1") && url.contains(FACILITY_PARAM + "=2"),
                String.format("The Generated URL is expected to contain two facilities(%s) ", url)
                );
    }

    @Test
    @DisplayName("Facilities - Invalid parameters are omitted")
    void facility_invalid() {
        String url = createBuilder().productTypes(DEFAULT_TYPE)
                .facility("")
                .facility((String) null)
                .facility(" \t \n ")
                .facility("1&2")
                .build();

        validateUrl(url);
        assertFalse(
                url.contains(FACILITY_PARAM + "="),
                String.format("The Generated URL is expected to contain no facilities (%s) ", url)
                );
    }

    @Test
    @DisplayName("Rating - A single parameters is included")
    void rating() {
        String url = createBuilder().productTypes(DEFAULT_TYPE)
                .rating("1")
                .build();

        validateUrl(url);
        assertTrue(
                url.contains(RATING_PARAM + "=1"),
                String.format("The Generated URL is expected to contain %s=1 (%s) ", RATING_PARAM, url)
                );
    }

    @Test
    @DisplayName("Rating - The single parameters are included")
    void rating_multiple() {
        String url = createBuilder().productTypes(DEFAULT_TYPE)
                .rating("1")
                .rating("2")
                .build();

        validateUrl(url);
        assertTrue(
                url.contains(RATING_PARAM + "=1") && url.contains(RATING_PARAM + "=2"),
                String.format("The Generated URL is expected to contain two facilities(%s) ", url)
                );
    }

    @Test
    @DisplayName("Rating - Invalid parameters are omitted")
    void rating_invalid() {
        String url = createBuilder().productTypes(DEFAULT_TYPE)
                .rating("")
                .rating((String) null)
                .rating(" \t \n ")
                .rating("1&2")
                .build();

        validateUrl(url);
        assertFalse(
                url.contains(RATING_PARAM + "="),
                String.format("The Generated URL is expected to contain no facilities (%s) ", url)
                );
    }

    @Test
    @DisplayName("Order - Valid parameters are include")
    void order() {
        String url = createBuilder().productTypes(DEFAULT_TYPE)
                .sortBy("DISTANCE")
                .build();

        validateUrl(url);
        assertTrue(
                url.contains(ORDER_PARAM + "="),
                String.format("The Generated URL is expected to have an order (%s) ", url)
                );
    }

    @Test
    @DisplayName("Order - Invalid parameters or NONE, skips the order paramater")
    void order_invalid() {
        String url;

        url = createBuilder().productTypes(DEFAULT_TYPE)
                .sortBy("NONE")
                .build();
        assertFalse(
                url.contains(ORDER_PARAM + "="),
                String.format("The Generated URL is expected to have no order (%s) ", url)
                );
        validateUrl(url);

        url = createBuilder().productTypes(DEFAULT_TYPE)
                .sortBy("SOMETHING THAT DOES NOT EXISTS")
                .build();
        assertFalse(
                url.contains(ORDER_PARAM + "="),
                String.format("The Generated URL is expected to have no order (%s) ", url)
                );
        validateUrl(url);

        url = createBuilder().productTypes(DEFAULT_TYPE)
                .build();
        assertFalse(
                url.contains(ORDER_PARAM + "="),
                String.format("The Generated URL is expected to have no order (%s) ", url)
                );
        validateUrl(url);
    }

    @Test
    @DisplayName("Proximity - A single parameters is included")
    void proximity() {
        mockLocationDistrictLoader("Leith");
        String url = createBuilder().productTypes(DEFAULT_TYPE)
                .location("Leith")
                .proximity(6.5)
                .build();

        validateUrl(url);
        assertTrue(url.contains("areaproxdist=6.5"),
                String.format("The Generated URL is expected to have the parameter areaproxdist (%s) ", url)
                );
        assertTrue(url.contains("locprox=1"));
    }

    @Test
    @DisplayName("Proximity - Empty value is replaced with default value")
    void proximity_empty() {
        when(properties.getDmsMapDefaultDistance()).thenReturn(10.1);
        mockLocationDistrictLoader("Leith");
        String url = createBuilder().productTypes(DEFAULT_TYPE)
                .location("Leith")
                .proximity(null)
                .build();

        validateUrl(url);
        assertTrue(url.contains("areaproxdist=10.1"),
                String.format("The Generated URL is expected to have the parameter areaproxdist and locprox (%s) ", url)
                );
        assertTrue(url.contains("locprox=1"));
    }

    @Test
    @DisplayName("Proximity - Zero value is treated as empty value (CMS Limitation overcome)")
    void proximity_zero() {
        when(properties.getDmsMapDefaultDistance()).thenReturn(10.1);
        mockLocationDistrictLoader("Leith");
        String url = createBuilder().productTypes(DEFAULT_TYPE)
                .location("Leith")
                .proximity(0.0)
                .build();

        validateUrl(url);
        assertTrue(
                url.contains("areaproxdist=10.1"),
                String.format("The Generated URL is expected to have the parameter areaproxdist and locprox (%s) ", url)
                );
        assertTrue(url.contains("locprox=1"));
    }

    @Test
    @DisplayName("Offers - Show results with any offers")
    void offers() {
        mockLocationLoader("Edinburgh");
        String url = createBuilder().productTypes(DEFAULT_TYPE)
                .location("Edinburgh").offers(true)
                .proximity(null)
                .build();

        validateUrl(url);
        assertTrue(url.contains("offers=" + true),
                String.format("The Generated URL is expected to have the parameter offers (%s) ", url)
        );
    }

    @Test
    @DisplayName("Keywords - Show results with keywords")
    void keywords() {
        mockLocationLoader("Edinburgh");
        String url = createBuilder().productTypes(DEFAULT_TYPE)
                .location("Edinburgh").keywords("castle")
                .proximity(null)
                .build();

        validateUrl(url);
        assertTrue(url.contains("name=castle"),
                String.format("The Generated URL is expected to have the parameter keywords (%s) ", url)
        );
    }

    @Test
    @DisplayName("Channel - Show results within a channel")
    void channel() {
        mockLocationLoader("Edinburgh");
        String url = createBuilder().productTypes(DEFAULT_TYPE)
                .location("Edinburgh").channel("weddings")
                .proximity(null)
                .build();

        validateUrl(url);
        assertTrue(url.contains("channel=weddings"),
                String.format("The Generated URL is expected to have the parameter channel (%s) ", url)
        );
    }

    @Test
    @DisplayName("Safe Travels - Show results with Covid Safe Travels ")
    void safeTravels() {
        mockLocationLoader("Edinburgh");
        String url = createBuilder().productTypes(DEFAULT_TYPE)
                .location("Edinburgh").safeTravels(true)
                .proximity(null)
                .build();

        validateUrl(url);
        assertTrue(url.contains("fac_id=safetrav"),
                String.format("The Generated URL is expected to have the parameter safe travels (%s) ", url)
        );
    }

    @Test
    @DisplayName("VS-4566 Days Out - Show results with Days out")
    void daysOut() {
        mockLocationLoader("Edinburgh");
        String url = createBuilder().productTypes(DEFAULT_TYPE)
                .location("Edinburgh").daysOut(true)
                .proximity(null)
                .build();

        validateUrl(url);
        assertTrue(url.contains("fac_id=daysoutscotland"),
                String.format("The Generated URL is expected to have the parameter days out offer (%s) ", url)
        );
    }

    @Test
    @DisplayName("Free entry - Show products with free entry")
    void free_entry() {
        mockLocationLoader("Edinburgh");
        String url = createBuilder().productTypes(DEFAULT_TYPE)
                .location("Edinburgh").free(true)
                .proximity(null)
                .build();

        validateUrl(url);
        assertTrue(url.contains("maxprice=" + 0),
                String.format("The Generated URL is expected to have the parameter maxprice to 0 (%s) ", url)
        );
    }

    @Test
    @DisplayName("Coordinate parameters are added (lat, lon)")
    void coordinates() {
        String url = createBuilder().productTypes(DEFAULT_TYPE)
                .coordinates(12, -21)
                .build();

        validateUrl(url);
        assertTrue(
                url.contains("lat=12") && url.contains("lng=-21"),
                "The Generated URL is expected to have coordinates: " + url
                );
    }

    @Test
    @DisplayName("Create a product Search From a HippoBean")
    void fromProductSearch() {
        ProductsSearch ps = mock(ProductsSearch.class);
        when(ps.getProductType()).thenReturn("even");
        when(ps.getLocation()).thenReturn("Skye");
        when(ps.getDistance()).thenReturn(4.);
        when(ps.getDmsCategories()).thenReturn(new String[0]);
        when(ps.getDmsFacilities()).thenReturn(null);
        mockLocationDistrictLoader("Skye");

        String url = createBuilder().fromHippoBean(ps).build();

        assertTrue(url.contains("prodtypes=even"), "The parameter hasn't been populated");
        assertTrue(url.contains("loc=Skye") && url.contains("locplace=555"), "The parameters for location haven't been populated");
        assertTrue(url.contains("areaproxdist=4"), "The parameter proximity hasn't been populated");
        assertTrue(url.contains("locprox=1"), "The parameter proximity hasn't been populated");
        assertFalse(url.contains("cat="), "The parameter for category should not contain any value");
        assertFalse(url.contains("fac_id="), "The parameter for facilities should not contain any value");
    }

    @Test
    @DisplayName("URLs are localized")
    void localization() {
        String url = createBuilder().productTypes(DEFAULT_TYPE)
                .locale(Language.SPANISH.getLocale())
                .build();

        validateUrl(url);
        assertTrue(url.startsWith(Language.SPANISH.getPathVariable()),
                "The URL doesn't seem to be properly localized: " + url
        );
    }

    @Test
    @DisplayName("Data endpoints contain the locale parameter")
    void localization_dataEndpoints() {
        when(properties.getDmsDataPublicHost()).thenReturn("/data");

        String url = createBuilder().productTypes(DEFAULT_TYPE)
                .locale(Language.SPANISH.getLocale())
                .buildCannedSearch();

        validateUrl(url);
        assertTrue(url.contains("locale="),"The URL does not contain the language query parameter (locale): " + url);
    }

    private void mockLocationLoader(String location) {
        mockLocationLoader(location, null, location);
    }

    private void mockLocationLoader(String location, Locale locale, String output) {
        LocationObject loc = new LocationObject();
        loc.setName(output);
        loc.setKey("555");
        when(locationLoader.getLocation(location, locale)).thenReturn(loc);
    }

    private void mockLocationDistrictLoader(String location) {
        mockLocationDistrictLoader(location, null, location);
    }

    private void mockLocationDistrictLoader(String location, Locale locale, String output) {
        LocationObject loc = new LocationObject();
        loc.setName(output);
        loc.setKey("555");
        loc.setType("DISTRICT");
        when(locationLoader.getLocation(location, locale)).thenReturn(loc);
    }

    /**
     * Validates a Product Search URL
     *
     * The following validation might not be accurate for non Product Search URLs
     * @param url psr url
     */
    private void validateUrl(String url) {
        //At least one question mark
        assertTrue(url.contains("?"), "There should be at least one parameter in the request: " + url );
        //No more than one question mark
        assertEquals(
                1, url.chars().filter(ch -> ch == '?').count(),
                "Only one question mark is expected: " + url
        );
        //Starts with http, https (fully qualified) or  / (domain related)
        assertTrue(        url.startsWith("/") || url.startsWith("http"),
                "The url should starts with http[s] or / : "+  url
        );
        //No space character in it
        assertFalse(url.contains(" "), "The url should not contain spaces " + url);
    }


}
