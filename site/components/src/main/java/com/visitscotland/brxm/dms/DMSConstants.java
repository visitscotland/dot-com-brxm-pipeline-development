package com.visitscotland.brxm.dms;

import com.visitscotland.brxm.utils.NonTestable;

@NonTestable()
public class DMSConstants {

    public static final String PATH_SEE_DO = "see-do";
    public static final String PATH_ACCOMMODATION = "accommodation";
    public static final String PATH_FOOD_DRINK = "food-drink";
    public static final String PATH_EVENTS = "events";
    public static final String PATH_TOURS = "tours";

    public static final String TYPE_SEE_DO = "acti,attr,reta";
    public static final String TYPE_ACCOMMODATION = "acco";
    public static final String TYPE_FOOD_DRINK = "cate";
    public static final String TYPE_EVENTS = "even";
    public static final String TYPE_TOURS = "tour";

    public static final String TYPE_IKNOW = "cate,acti,attr,reta";
    public static final String TYPE_SERVICES = "serv";

    private DMSConstants(){}

    public static final String CAT_ICENTRE = "vics";
    public static final String AWARD_IKNOW = "qaiknowscotland";

    public static final String SORT_ALPHA = "ALPHA";

    public static final class MapSearch {

        private MapSearch(){}

        public static final String PROPERTIES = "properties";
        public static final String ID = "id";
        public static final String NAME = "name";
    }

    public static final class ProductSearch {

        private ProductSearch(){}

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
        public static final String ORDER_PARAM = "order";
        public static final String AVAILABILITY = "avail";
        public static final String OFFERS = "offers";
        public static final String FREE = "maxprice";
        public static final String LOCALE = "locale";
        public static final String KEYWORDS = "name";
        public static final String SIZE = "size";
        public static final String CHANNEL  = "channel";
    }

    public static final class DMSProduct {

        private DMSProduct(){}

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

        public static final String URL = "productLink";
        public static final String URL_LINK = "link";
        public static final String TIME_TO_EXPLORE = "timeToExplore";
        public static final String PRICE = "price";
        public static final String DISPLAY_PRICE = "displayPrice";

        public static final String OPENING = "opening";

    }

    //Endpoints
    public static final String PRODUCT_SEARCH = "/info/%s/search-results?";
    public static final String PRODUCT_SEARCH_DATA_MAP = "/product-search/map?";
    public static final String VS_DMS_PRODUCT_CARD = "/private/products/card?";
    public static final String VS_DMS_CANNED_SEARCH = "/component/cannedsearch?";
    public static final String VS_DMS_CANNED_SEARCH_TOURS = "/component/cannedsearchtours";
    public static final String META_PRODUCT_LIST = "/meta/%s/list?%s";
    public static final String META_LOCATIONS = "/meta/location/list?full";
    public static final String META_LOCATIONS_COORDINATES = "/meta/location/polygon?";

}
