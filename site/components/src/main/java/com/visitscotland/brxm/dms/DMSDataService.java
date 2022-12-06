package com.visitscotland.brxm.dms;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.visitscotland.brxm.services.CommonUtilsService;
import com.visitscotland.brxm.utils.Properties;
import com.visitscotland.utils.Contract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Locale;

@Component
public class DMSDataService {

    private static final Logger logger = LoggerFactory.getLogger(DMSDataService.class.getName());

    private final DMSProxy proxy;
    private final CommonUtilsService utilsService;
    private final Properties propertiesService;

    public DMSDataService(DMSProxy proxy, CommonUtilsService utilsService, Properties propertiesService) {
        this.proxy = proxy;
        this.utilsService = utilsService;
        this.propertiesService = propertiesService;
    }


    /**
     * Returns a summary of the product as a JsonNode. This is usually use to show product cards
     *
     * @param productId Id of the product
     * @param locale locale for translated texts
     *
     * @return JsonNode with the product card Information
     *
     *
     */
    @Cacheable(value="dmsProduct")
    public JsonNode productCard(String productId, Locale locale) {
        String responseString = null;

        if (!Contract.isEmpty(productId)) {
            String dmsUrl = DMSConstants.VS_DMS_PRODUCT_CARD;
            dmsUrl += "id=" + productId;
            if (locale != null) {
                dmsUrl += "&locale=" + locale.toLanguageTag().toLowerCase();
            }

            logger.info("Requesting data to the dms: {}", dmsUrl);
            try {
                responseString = proxy.request(dmsUrl);
                if (responseString!=null) {

                    ObjectMapper mapper = new ObjectMapper();
                    JsonNode json = mapper.readTree(responseString);

                    if (json.has("data")) {
                        return json.get("data");
                    }
                }
            } catch (JsonProcessingException e){
                logger.error("The response could not be parsed:\n {}", responseString, e);
            }
        } else {
            logger.info("productCard data requested but the product id was not provided");
        }
        return null;
    }

    /**
     * This method invokes the legacy data Map endpoint from a ProductSearchBuilder
     *
     * @param psb ProductSearchBuilder
     *
     * @return Json node with DMS results
     */
    @Cacheable (value="dmsProductSearch")
    public JsonNode legacyMapSearch(ProductSearchBuilder psb){

        String responseString = null;
        String dmsUrl = psb.buildDataMap();

        logger.info("Requesting data to the dms: {}", dmsUrl);
        try {
            responseString = proxy.request(dmsUrl);

            if (responseString!=null) {

                ObjectMapper mapper = new ObjectMapper();
                JsonNode json = mapper.readTree(responseString);

                if (json.has("features")) {
                    return json.get("features");
                }
            }
        } catch (JsonProcessingException e){
            logger.error("The response could not be parsed:\n {}", responseString, e);
        }

        return  null;
    }
    @Cacheable (value="dmsProductSearch")
    public JsonNode cannedSearch(ProductSearchBuilder psb){

        String dmsUrl = psb.buildCannedSearchInternal();

        logger.info("Requesting data to the canned search: {}", dmsUrl);
        String responseString = proxy.request(dmsUrl);
        if (responseString != null) {
            try {
                ObjectMapper m = new ObjectMapper();
                if (m.readTree(responseString).has("data")){
                    return m.readTree(responseString).get("data").get("products");
                }
            } catch (JsonProcessingException e) {
                logger.error("The response could not be parsed:\n {}", responseString, e);
            }
        }
        return null;
    }

    /**
     * This method invokes the canned search endpoint to check if there are results coming
     *
     * @param cannedSearch tours search url
     *
     * @return boolean to indicate if the search returns products
     */
    @Cacheable (value="dmsProductSearch")
    public boolean cannedSearchHasResults(String cannedSearch){

        String responseString = null;

        logger.info("Requesting data to the canned search url: {}", cannedSearch);
        try {
            responseString = proxy.request(cannedSearch);

            if (responseString!=null) {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode json = mapper.readTree(responseString);

                if (json.has("data") ) {
                    int count =  Integer.parseInt(String.valueOf(json.get("data").get("count")));
                    return count>0;
                }
            }
        } catch (JsonProcessingException e){
            logger.error("The response could not be parsed:\n {}", responseString, e);
        }

        return false;
    }

    /**
     * This method invokes the polygon endpoint to retrieve polygons borders
     *
     * @param location DMS location
     *
     * @return ArrayNode with the data for coordinates
     */
    //TODO this cache should be for a longer period of time?
    @Cacheable (value="dmsProductSearch")
    public JsonNode getPolygonCoordinates(String location){
        logger.info("Requesting data to retrieve the coordinates for the polygon: {}", location);
        if (!Contract.isEmpty(location)) {
            String apiUrl = propertiesService.getApiDataBackendHost() + "maps/meta/location/polygon?";
            apiUrl += location;
            String responseString = null;
            try {
                responseString = utilsService.requestUrl(apiUrl);

                if (responseString != null) {
                        ObjectMapper m = new ObjectMapper();
                        return m.readTree(responseString).get("geometry");
                }
            } catch (IOException e) {
                logger.error("The response could not be parsed:\n {}", responseString, e);
            }
        }
      return null;
    }

    /**
     * This method invokes the category group endpoint to retrieve category groups for product type passed
     *
     * @param prodType DMS product type
     * @param locale language
     *
     * @return ArrayNode with the data for coordinates
     */
    @Cacheable (value="dmsProductSearch")
    public ArrayNode getCatGroup(String prodType, String locale){
        logger.info("Requesting data to retrieve the category groups for produc type: {}", prodType);
        if (!Contract.isEmpty(prodType)) {
            String dmsUrl = DMSConstants.META_CATEGORY_GROUP;
            dmsUrl += "prodtypes=" + prodType + "&locale=" + locale;

            return getArrayData(dmsUrl);
        }
        return null;
    }

    /**
     * method to call vs-dms-project endpoint and retrieve an array to be consumed by feds
     *
     * @param dmsUrl endpoint url
     *
     * @return ArrayNode with the response
     */
    private ArrayNode getArrayData(String dmsUrl) {
        String responseString = proxy.request(dmsUrl);
        if (responseString != null) {
            try {
                ObjectMapper m = new ObjectMapper();
                return (ArrayNode) m.readTree(responseString).get("data");
            } catch (JsonProcessingException e) {
                logger.error("The response could not be parsed:\n {}", responseString, e);
            }
        }
        return null;
    }
}
