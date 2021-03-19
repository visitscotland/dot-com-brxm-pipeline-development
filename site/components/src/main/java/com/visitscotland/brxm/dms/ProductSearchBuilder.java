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
        DISTANCE("proximityAsc"), NONE(null);

        final String value;

        Order  (String value){
            this.value = value;
        }

        static Order fromValue(String value){
            try {
                 Order order = valueOf(value);
                 if (order != null){
                     return order;
                 }
            } catch (Exception e){
                logger.warn ("Incorrect value for Order: {}", value);
            }
            return Order.NONE;
        }
    }



    //TODO Convert into property --> getDmsMapDefaultDistance
    static final Double DEFAULT_PROXIMITY = 10.0;



    //TODO This path should come from DMS?
    static final String PATH_SEE_DO = "see-do";
    static final String PATH_ACCOMMODATION = "accommodation";
    static final String PATH_FOOD_DRINK = "food-drink";
    static final String PATH_EVENTS = "events";

    private String productTypes;
    private String path;
    private Double proximity;
    private String location;
    private Double longitude;
    private Double latitude;
    private Locale locale;
    private Order order;

    private Set<String> categories = new TreeSet<>();
    private Set<String> awards = new TreeSet<>();
    private Set<String> facilities = new TreeSet<>();
    private Set<String> ratings = new TreeSet<>();

    private final LocationLoader locationLoader;

    private final Properties properties;

    public ProductSearchBuilder(LocationLoader locationLoader, Properties properties){
        this.locationLoader = locationLoader;
        this.properties = properties;
        this.order = Order.NONE;
        this.proximity = DEFAULT_PROXIMITY;
    }

    /**
     * Allow new instances from FreeMarker
     *
     * @return
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
        if (types.equals("acco")) {
            path = PATH_ACCOMMODATION;
        } else if (types.equals("cate")) {
            path = PATH_FOOD_DRINK;
        } else if (types.equals("even")){
            path = PATH_EVENTS;
        } else {
            //Note: if category is not acti,attr,reta (in that other) the category "Thing to see and do" is not selected on the Product Search
            path = PATH_SEE_DO;
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

    public ProductSearchBuilder sortBy(String order){
        this.order = Order.fromValue(order);
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
     * It will return an exception if
     *
     * @return
     */
    public String build(){
        return buildUrl(String.format(DMSConstants.PRODUCT_SEARCH, path));
    }

    public String buildDataMap(){
        return buildUrl(DMSConstants.PRODUCT_SEARCH_DATA_MAP);
    }

    private String buildUrl(String path){
        if (productTypes == null){
            throw new VsException("No types have been defined for this search");
        }
        if (Contract.isEmpty(properties.getDmsHost())){
            return composeUrl(path);
        } else {
            return composeUrl(properties.getDmsHost() + path);
        }
    }

    /**
     * Compose the query parameters for the URL
     *
     * @param urlPath
     * @return
     */
    private String composeUrl (String urlPath){
        String compose = addParams(urlPath, PRODUCT_TYPE_PARAM, productTypes);
        //Accommodations MUST deactivate availavility search
        if (path.equals(PATH_ACCOMMODATION)) {
            compose = addParams(compose, AVAILABILITY, "off");
        }

        //The list of parameters is different if a location is provided from latitude and longitude
        if (location != null) {
            LocationObject loc = locationLoader.getLocation(location, locale);

            compose = addParams(compose, "POLYGON".equals(loc.getType()) ? LOCATION_POLYGON_PARAM : LOCATION_PLACE_PARAM, loc.getKey());
            compose = addParams(compose, PROXIMITY_LOCATION_PARAM, proximity.toString());

            try {
                compose = addParams(compose, LOCATION_NAME_PARAM, URLEncoder.encode(loc.getName(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                logger.error("Unexpected UnsupportedEncodingException for UTF-8", e);
            }
        } else if (latitude != null && longitude != null) {
            compose = addParams(compose, LATITUDE_PARAM, latitude.toString());
            compose = addParams(compose, LONGITUDE_PARAM, longitude.toString());
            compose = addParams(compose, PROXIMITY_PIN_PARAM, proximity.toString());
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
     * Add one parameter to a url, provided the parameter has a value
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
