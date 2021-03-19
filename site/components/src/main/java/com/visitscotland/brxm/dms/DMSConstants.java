package com.visitscotland.brxm.dms;

import com.visitscotland.brxm.utils.NonTestable;

@NonTestable()
public class DMSConstants {

    private DMSConstants() {
        utilityClassException();
    }

    private void utilityClassException(){
        throw new IllegalStateException("Utility class");
    }

    public static final String TYPE_SERVICES = "serv";
    public static final String TYPE_SEE_DO = "cate,acti,attr,reta";

    public static final String CAT_ICENTRE = "vics";

    public static final String AWARD_IKNOW = "qaiknowscotland";

    public static final String SORT_ALPHA = "alpha";

    public final class MapSearch {

        private MapSearch (){
            utilityClassException();
        }

        public static final String PROPERTIES = "properties";
        public static final String ID = "id";
        public static final String NAME = "name";
    }

    public final class ProductSearch {

        private ProductSearch (){
            utilityClassException();
        }

        public static final String PRODUCT_TYPE_PARAM = "prodtypes";
        public static final String LOCATION_NAME_PARAM = "loc";
        public static final String LOCATION_PLACE_PARAM = "locplace";
        public static final String LOCATION_POLYGON_PARAM = "locpoly";
        public static final String CATEGORY_PARAM = "cat";
        public static final String AWARD_PARAM = "src_awards__0";
        public static final String FACILITY_PARAM = "fac_id";
        public static final String RATING_PARAM = "grade";
        public static final String LATITUDE_PARAM = "lat";
        public static final String LONGITUDE_PARAM = "lng";
        public static final String PROXIMITY_LOCATION_PARAM = "locprox";
        public static final String PROXIMITY_PIN_PARAM = "areaproxdist";
        public static final String ORDER_PARAM = "c";
        public static final String AVAILABILITY = "avail";
    }

    /**
     * Note: This interface is marked by SonarQube as a Critical issue, however since those constant are translated
     * into parameters later on, AND they really belong to the dms project, this is a perfectly reasonable use for a
     * constants interface.
     */
    public final class DMSProduct {

        private DMSProduct (){
            utilityClassException();
        }

        public static final String MEDIA = "mediaUrl";
        public static final String CREDIT = "copyright";
        public static final String ALT_TEXT = "altText";
        public static final String IMAGE = "images";
        public static final String NAME = "name";
        public static final String LATITUDE = "latitude";
        public static final String LONGITUDE = "longitude";
        public static final String ID = "id";
        public static final String ADDRESS = "address";
        public static final String LOCATION = "city";
        public static final String FACILITIES = "keyFacilities";
    }

    //Endpoints
    //TODO Remove %s at the beginning of the constants
    public static final String PRODUCT_SEARCH = "/info/%s/search-results?";
    public static final String PRODUCT_SEARCH_DATA_MAP = "/data/product-search/map?";
    public static final String VS_DMS_PRODUCT_CARD = "%s/data/products/card?";
    public static final String META_PRODUCT_LIST = "/data/meta/%s/list?%s";
    public static final String META_LOCATIONS = "/data/meta/location/list?full";

}
