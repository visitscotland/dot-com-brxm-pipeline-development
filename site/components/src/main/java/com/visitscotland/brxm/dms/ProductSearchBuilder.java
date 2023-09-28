package com.visitscotland.brxm.dms;

import com.visitscotland.brxm.hippobeans.ProductsSearch;
import com.visitscotland.brxm.dms.model.LocationObject;
import com.visitscotland.brxm.config.VsComponentManager;
import com.visitscotland.brxm.utils.Language;
import com.visitscotland.brxm.utils.Properties;
import com.visitscotland.brxm.utils.VsException;
import com.visitscotland.utils.Contract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;
import static com.visitscotland.brxm.dms.DMSConstants.ProductSearch.*;

/**
 * @author jose.calcines
 */
@Component
@Scope(SCOPE_PROTOTYPE)
public class ProductSearchBuilder {

    private static final Logger logger = LoggerFactory.getLogger(ProductSearchBuilder.class.getName());

    enum Order {
        DISTANCE("proximityAsc"), ALPHA("alpha"),  NONE(null);

        final String value;

        Order  (String value){
            this.value = value;
        }

        static Order fromValue(String value){
            try {
                return valueOf(value);
            } catch (IllegalArgumentException e){
                logger.warn ("Incorrect value for Order: {}", value);
            }
            return Order.NONE;
        }
    }

    private String productTypes;
    private String path;
    private Double proximity;
    private String location;
    private Double longitude;
    private Double latitude;
    private Locale locale;
    private Boolean offers;
    private Boolean free;
    private Boolean safeTravels;
    private Boolean daysOut;
    private String keywords;
    private Order order;
    private Integer size;
    private String channel;

    private final Set<String> categories = new TreeSet<>();
    private final Set<String> awards = new TreeSet<>();
    private final Set<String> facilities = new TreeSet<>();
    private final Set<String> ratings = new TreeSet<>();

    private final LocationLoader locationLoader;

    private final Properties properties;

    public ProductSearchBuilder(LocationLoader locationLoader, Properties properties){
        this.locationLoader = locationLoader;
        this.properties = properties;
        this.order = Order.NONE;
        this.offers = false;
        this.free = false;
    }

    /**
     * Allow new instances from FreeMarker
     */
    public static ProductSearchBuilder newInstance(){
        return VsComponentManager.get(ProductSearchBuilder.class);
    }

    public ProductSearchBuilder fromHippoBean(ProductsSearch ps){
        if (ps.getProductType() != null) {
            productTypes(ps.getProductType());
            location(ps.getLocation());
            category(ps.getDmsCategories());
            facility(ps.getDmsFacilities());
            award(ps.getDmsAwards());
            rating(ps.getOfficialrating());
            proximity(ps.getDistance());
            offers(ps.getOffers());
            free(ps.getFree());
            keywords(ps.getKeywords());
            safeTravels(ps.getSafeTravels());
            daysOut(ps.getDaysOut());
            channel(ps.getChannel());
        }
        return this;
    }

    public ProductSearchBuilder locale(Locale locale){
        if (locale != null) {
            for (Locale loc : Language.getLocales()) {
                if (locale.equals(loc) || (loc != null && locale.getLanguage().equals(loc.getLanguage()))) {
                    this.locale = loc;
                    return this;
                }
            }
        }

        logger.info("locale {} not found",  (locale != null ? locale.toLanguageTag() : "null"));
        this.locale = null;
        return this;
    }

    public ProductSearchBuilder locale(String locale){
        if (locale != null) {
            locale(Locale.forLanguageTag(locale));
        }
        return this;
    }

    private void path(String types){
        switch (types) {
            case "acco":
                path = DMSConstants.PATH_ACCOMMODATION;
                break;
            case "cate":
                path = DMSConstants.PATH_FOOD_DRINK;
                break;
            case "even":
                path = DMSConstants.PATH_EVENTS;
                break;
            default:
                //Note: if category is not acti,attr,reta (in that other) the category "Thing to see and do" is not selected on the Product Search
                path = DMSConstants.PATH_SEE_DO;
                break;
        }
    }

    public ProductSearchBuilder productTypes(String types){
        try {
            this.productTypes = URLEncoder.encode(types, "UTF-8");
            path(types);
        } catch (UnsupportedEncodingException e){
            logger.error(String.format("Unexpected UnsupportedEncodingException for UTF-8 with the types %s", types),e);
        }

        return this;
    }

    public ProductSearchBuilder category(String... categories){
        if (categories != null) {
            for (String category : categories) {
                if (valid(category)) {
                    this.categories.add(category);
                }
            }
        }
        return this;
    }

    public ProductSearchBuilder award(String... awards){
        if (awards != null) {
            for (String category : awards) {
                if (valid(category)) {
                    this.awards.add(category);
                }
            }
        }
        return this;
    }

    public ProductSearchBuilder facility(String... facilities){
        if (facilities != null) {
            for (String category : facilities) {
                if (valid(category)) {
                    this.facilities.add(category);
                }
            }
        }
        return this;
    }

    public ProductSearchBuilder rating(String... ratings){
        if (ratings != null) {
            for (String category : ratings) {
                if (valid(category)) {
                    this.ratings.add(category);
                }
            }
        }
        return this;
    }

    public ProductSearchBuilder location(String location){
        if (locationLoader.getLocation(location, null) != null){
            this.location = location;
        }
        return this;
    }

    public ProductSearchBuilder proximity(Double proximity){
        if (validNumber(proximity) && proximity > 0){
            this.proximity = proximity;
        }
        return this;
    }

    public ProductSearchBuilder coordinates(Number lat, Number lon){
        if (validNumber(lat) && validNumber(lon)){
            this.latitude = lat.doubleValue();
            this.longitude = lon.doubleValue();
        }
        return this;
    }

    public ProductSearchBuilder offers(Boolean offers){
        this.offers = offers;

        return this;
    }
    public ProductSearchBuilder free(Boolean free){
        this.free = free;

        return this;
    }

    public ProductSearchBuilder safeTravels(Boolean safeTravels){
        this.safeTravels = safeTravels;

        return this;
    }

    public ProductSearchBuilder daysOut(Boolean daysOut){
        this.daysOut = daysOut;

        return this;
    }

    public ProductSearchBuilder channel(String channel){
        this.channel = channel;

        return this;
    }

    public ProductSearchBuilder keywords(String keywords){
        this.keywords = keywords;

        return this;
    }

    public ProductSearchBuilder sortBy(String order){
        if (order != null){
            this.order = Order.fromValue(order);
        }
        return this;
    }

    public ProductSearchBuilder size(Integer size) {
        this.size = size;
        return this;
    }

    private boolean valid(String s){
        return  s != null && s.trim().length() > 0 && !s.contains("&");
    }

    private boolean validNumber(Number s){
        return  s != null;
    }

    /**
     * Builds the URL based on the configuration defined.
     *
     * It will return an exception if the type hasn't been set
     */
    public String build(){
        return buildSearchUrl(String.format(DMSConstants.PRODUCT_SEARCH, path), false, false);
    }

    public String buildDataMap(boolean internal){
        return buildSearchUrl(DMSConstants.PRODUCT_SEARCH_DATA_MAP, true, internal);
    }

    public String buildCannedSearch(){
        return buildSearchUrl(DMSConstants.VS_DMS_CANNED_SEARCH, true, false);
    }

    public String buildCannedSearchInternal(){
        return buildSearchUrl(DMSConstants.VS_DMS_CANNED_SEARCH, true, true);
    }

    /**
     * Composes a search URL depending on if the endpoint are internal or external and if they are consumed by a front-end
     * or a back-end application.
     *
     */
    private String buildSearchUrl(String path, boolean dataEndpoint, boolean internal){
        if (productTypes == null){
            throw new VsException("No types have been defined for this search");
        }

        if (dataEndpoint){
            if (locale != null) {
                path = addParams(path, LOCALE, locale.toLanguageTag());
            }

            if (internal && !Contract.isEmpty(properties.getDmsDataHost())) {
                return composeUrl(properties.getDmsDataHost() + path);
            } else if (!internal && !Contract.isEmpty(properties.getDmsDataPublicHost())) {
                return composeUrl(properties.getDmsDataPublicHost() + path);
            } else {
                throw new VsException("Property for the dms data URL hasn't been defined in the CMS");
            }
        } else {
            if (Contract.isEmpty(properties.getDmsHost())) {
                return composeUrl(Language.getLanguageForLocale(locale).getPathVariable() + path);
            } else {
                return composeUrl(properties.getDmsHost() + Language.getLanguageForLocale(locale).getPathVariable() + path);
            }
        }
    }

    /**
     * Compose the query parameters for the URL
     */
    private String composeUrl (String urlPath){
        String compose = addParams(urlPath, PRODUCT_TYPE_PARAM, productTypes);
        //Accommodations MUST deactivate availability search
        if (path.equals(DMSConstants.PATH_ACCOMMODATION)) {
            compose = addParams(compose, AVAILABILITY, "off");
        }

        //Variable set to 1 only for District or Coordinates, other types can't search by distance therefore this value should remain 0
        String locProx = "0";

        //The list of parameters is different if a location is provided from latitude and longitude
        if (location != null) {
            LocationObject loc = locationLoader.getLocation(location, locale);

            compose = addParams(compose, "POLYGON".equals(loc.getType()) ? LOCATION_POLYGON_PARAM : LOCATION_PLACE_PARAM, loc.getKey());
            if ("DISTRICT".equals(loc.getType())){
                locProx = "1";
            }
            
            try {
                compose = addParams(compose, LOCATION_NAME_PARAM, URLEncoder.encode(loc.getName(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                logger.error("Unexpected UnsupportedEncodingException for UTF-8", e);
            }
        } else if (latitude != null && longitude != null) {
            compose = addParams(compose, LATITUDE_PARAM, latitude.toString());
            compose = addParams(compose, LONGITUDE_PARAM, longitude.toString());
            locProx = "1";
        }

        compose = addParams(compose, PROXIMITY_LOCATION_PARAM, locProx);

        //add parameter distance only for location types that allows search by distance
        if ("1".equals(locProx)){
            if (proximity == null){
                proximity = properties.getDmsMapDefaultDistance();
            }
            compose = addParams(compose, PROXIMITY_PIN_PARAM, proximity.toString());
        }

        if (Boolean.TRUE.equals(free)){
            compose = addParams(compose, FREE, "0");
        }
        if (Boolean.TRUE.equals(safeTravels)){
            compose = addParams(compose, FACILITY_PARAM, "safetrav");
        }
        if (Boolean.TRUE.equals(daysOut)){
            compose = addParams(compose, FACILITY_PARAM, "daysoutscotland");
        }
        if (keywords != null){
            compose = addParams(compose, KEYWORDS, keywords);
        }
        if (!Contract.isEmpty(channel)){
            compose = addParams(compose, CHANNEL, channel);
        }
        if (Boolean.TRUE.equals(offers)){
            compose = addParams(compose, OFFERS, "true");
        }

        if (size != null) {
            compose = addParams(compose, SIZE, size.toString());
        }

        //Add categories, awards, facilities and rating
        compose = addParams(compose, CATEGORY_PARAM, categories);
        compose = addParams(compose, AWARD_PARAM, awards);
        compose = addParams(compose, FACILITY_PARAM, facilities);
        compose = addParams(compose, RATING_PARAM, ratings);

        //Sort the list
        compose = addParams(compose, ORDER_PARAM, order.value);

        return compose;
    }

    /**
     * Add one parameter to an url, provided the parameter has a value
     *
     * @param url URL where the parameter will be added to
     * @param param name of the parameter to be added
     * @param value value of the parameter
     *
     * @return composed URL with the parameter
     */
    private String addParams(String url, String param, String value){
        if (value == null) {
            return url;
        } else if (url.endsWith("?")){
            return url + param + "=" + value;
        } else {
            return url + "&" + param + "=" + value;
        }
    }

    /**
     * Add a parameter with multiple values to a url, provided the parameter has at least one value
     *
     * @param url URL where the parameter will be added to
     * @param param name of the parameter to be added
     * @param values list of values for the parameter
     *
     * @return composed URL with the parameter
     */
    private String addParams(String url, String param, Collection<String> values){
        if (values == null || values.isEmpty()) {
            return url;
        } else {
            String aux = url;

            for (String value: values){
                aux = addParams(aux, param, value);
            }

            return aux;
        }
    }
}
