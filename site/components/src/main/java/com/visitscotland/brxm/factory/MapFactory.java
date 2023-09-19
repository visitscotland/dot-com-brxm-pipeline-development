package com.visitscotland.brxm.factory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.visitscotland.brxm.config.VsComponentManager;
import com.visitscotland.brxm.dms.*;
import com.visitscotland.brxm.dms.model.LocationObject;
import com.visitscotland.brxm.hippobeans.*;
import com.visitscotland.brxm.model.*;
import com.visitscotland.brxm.services.MapService;
import com.visitscotland.brxm.services.ResourceBundleService;
import com.visitscotland.brxm.utils.HippoUtilsService;
import com.visitscotland.brxm.utils.Properties;
import com.visitscotland.utils.Contract;
import org.hippoecm.hst.core.component.HstRequest;
import org.onehippo.taxonomy.api.Category;
import org.onehippo.taxonomy.api.Taxonomy;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Locale;

@Component
public class MapFactory {

    static final String ID = "id";
    static final String TYPE = "type";
    static final String GEOMETRY = "geometry";
    static final String DISCOVER = "map.discover";
    static final String PROPERTIES = "properties";
    static final String DESCRIPTION = "description";
    static final String NAME = "name";
    static final String REGIONS = "regions";
    static final String TITLE = "title";
    static final String MAP = "map";
    static final String POINT = "Point";

    private final MapService mapService;
    private final HippoUtilsService hippoUtilsService;
    private final ObjectMapper mapper;
    private final DMSDataService dmsDataService;
    private final ResourceBundleService bundle;
    private final Properties propertiesService;
    private final ImageFactory imageFactory;
    private final LocationLoader locationLoader;

    public MapFactory(MapService mapService, HippoUtilsService hippoUtilsService, DMSDataService dmsDataService, ResourceBundleService bundle, Properties properties, ImageFactory imageFactory,LocationLoader locationLoader) {
        this.hippoUtilsService = hippoUtilsService;
        this.mapper = new ObjectMapper();
        this.mapService = mapService;
        this.dmsDataService = dmsDataService;
        this.bundle = bundle;
        this.propertiesService = properties;
        this.imageFactory = imageFactory;
        this.locationLoader = locationLoader;
    }

    /**
     * Method to build a MapsModule to be used by feds with
     *
     * @param request HstRequest request
     * @param mapModuleDocument CMS document
     * @return module to be sent to feds
     */
    public MapsModule getModule(HstRequest request, MapModule mapModuleDocument, Page page) {
        MapsModule module = new MapsModule();

        module.setId(mapModuleDocument.getCanonicalUUID());
        module.setHippoBean(mapModuleDocument);
        module.setTitle(mapModuleDocument.getTitle());
        module.setIntroduction(mapModuleDocument.getCopy());
        module.setTabTitle(mapModuleDocument.getTabTitle());

        ObjectNode featureCollectionGeoJson = mapper.createObjectNode();
        featureCollectionGeoJson.put(TYPE, "FeatureCollection");
        ArrayNode features = mapper.createArrayNode();
        ArrayNode keys = mapper.createArrayNode();

        if (page instanceof Destination){
            buildDestinationMapPages(request.getLocale(),(Destination)page, mapModuleDocument, module, keys, features);
        }else{
            module.setDetailsEndpoint("");
            module.setMapPosition(mapper.createObjectNode());
            //bespoke maps data and pins coming from DMS
            if (!Contract.isEmpty(mapModuleDocument.getMapType())){

                //Feature places on top of these maps
                if (!Contract.isNull(mapModuleDocument.getFeaturedPlacesItem())) {
                    mapService.addFeaturePlacesNode(module, mapModuleDocument.getCategories(), request.getLocale(), keys, features);
                }
                for (BespokeDmsMap bespokeMap : getValues(mapModuleDocument.getMapType())) {
                    buildDMSMapPages(bespokeMap, module, keys, features, request.getLocale());
                    module.setMapType(bespokeMap.getCategory());
                }
            }else {
                // CMS maps, data and pins coming from CMS
                buildMapGeneralPages(request, mapModuleDocument, module, keys, features);
            }
        }

        //add first Json for filters
        module.setFilters(keys);

        //add second json (geoJson) to the module
        featureCollectionGeoJson.set("features", features);
        module.setGeoJson(featureCollectionGeoJson);
        return module;
    }

    /**
     * Method to build maps for General pages based on taxonomies and manual and optional featured places
     *
     * @param request the request
     * @param mapModuleDocument module coming from the CMS
     * @param module module to be sent to feds
     * @param keys filters for maps
     * @param features features information for mapcards
     */
    private void buildMapGeneralPages (HstRequest request, MapModule mapModuleDocument, MapsModule module, ArrayNode keys, ArrayNode features){

        if (!Contract.isNull(mapModuleDocument.getFeaturedPlacesItem())) {
            mapService.addFeaturePlacesNode(module, mapModuleDocument.getCategories(), request.getLocale(), keys, features);
        }
        for (String taxonomy : mapModuleDocument.getKeys()) {
            //get all the Taxonomy information
            Taxonomy vsTaxonomyTree = hippoUtilsService.getTaxonomy();
            module.setMapType(vsTaxonomyTree.getCategoryByKey(taxonomy).getKey());
            for (Category mainCategory : vsTaxonomyTree.getCategoryByKey(taxonomy).getChildren()) {
                keys.add(mapService.addFilterNode(mainCategory, request.getLocale()));
                //if the map has 2 levels, the parent wont be a category for the mapcards, so pick sons
                if (!mainCategory.getChildren().isEmpty()) {
                    for(Category child : mainCategory.getChildren()){
                        //find all the documents with a taxonomy/category
                        mapService.addMapDocumentsToJson(request, module,child, features);
                    }
                } else {
                    //find all the documents with a taxonomy/category
                    mapService.addMapDocumentsToJson(request, module, mainCategory, features);
                }
            }

        }
    }

    /**
     *
     * Method to build maps for Destination pages based on DMS products and manual and optional featured places
     *
     * @param locale the language
     * @param mapModuleDocument module coming from the CMS
     * @param module module to be sent to feds
     * @param keys filters for maps
     * @param features features information for mapcards
     */
    private void buildDestinationMapPages (Locale locale,Destination destinationPage, MapModule mapModuleDocument, MapsModule module, ArrayNode keys, ArrayNode features){
       if (Contract.isEmpty(module.getTabTitle())){
           String tabTitle = bundle.getResourceBundle(MAP, "map.explore", locale) + " " + destinationPage.getTitle();
           module.setTabTitle(tabTitle);
       }
        LocationObject location = locationLoader.getLocation(destinationPage.getLocation(),locale);
        JsonNode geometryNode;
        //Feature places on top of these maps
        if (!Contract.isNull(mapModuleDocument.getFeaturedPlacesItem())) {
            mapService.addFeaturePlacesNode(module, mapModuleDocument.getCategories(), locale , keys, features);
        }
        if (destinationPage.getKeys() == null || !Arrays.asList(destinationPage.getKeys()).contains(REGIONS)) {
            module.setMapType(MapType.CITIES.getMapType());
            geometryNode = dmsDataService.getLocationBorders(location.getId(),false);
            for (CitiesMapTab prodType : CitiesMapTab.values()) {
                //filters
                ObjectNode filter = mapService.buildCategoryNode(prodType.getProdTypeId(), bundle.getResourceBundle(MAP, prodType.getLabel(), locale));

                ///endpoint for data (pins)
                ProductSearchBuilder dmsQuery = this.buildProductSearch (destinationPage.getLocation(), prodType.getProdTypeId(), null, locale, null, 24);
                filter.put("pinsEndpoint", dmsQuery.buildDataMap(false));

                //Endpoint base to bring 24 random results
                filter.put("listProductsEndPoint", dmsQuery.buildCannedSearch());

                filter.put("pinClickEndPoint", propertiesService.getDmsDataPublicHost() + DMSConstants.VS_DMS_PRODUCT_MAP_CARD
                        +"locale="+locale.toLanguageTag()+"&id=");

                //subcategories added
                ArrayNode childrenArray = dmsDataService.getCatGroup(prodType.getProdTypeId(),locale.getLanguage());
                filter.set("subCategory",childrenArray);
                keys.add(filter);
            }
        }else{
            module.setMapType(MapType.REGIONAL.getMapType());
            geometryNode = dmsDataService.getLocationBorders(location.getId(),false);
            //for multipolygon regions we need the bounds, if geometryNode is empty means it is a polygon
            if (geometryNode == null || geometryNode.isEmpty()) {
                geometryNode = dmsDataService.getLocationBorders(location.getId(),true);
            }

            for (RegionsMapTab regionMap: RegionsMapTab.values()) {
                buildDMSMapPages(regionMap, module, keys, features, locale);
            }
        }
        module.setDetailsEndpoint(propertiesService.getDmsDataPublicHost() + DMSConstants.VS_DMS_PRODUCT_MAP_CARD+"locale="+locale.toLanguageTag()+"&id=");
        if (geometryNode != null) {
            module.setMapPosition((ObjectNode) geometryNode);
        }else{
            module.setMapPosition(mapper.createObjectNode());
        }
    }

    private ObjectNode addFilters (String prodTypeId, String prodTypelabel){
        return  mapService.buildCategoryNode(prodTypeId, prodTypelabel);
    }


    private void buildDMSMapPages (BespokeDmsMap bespokeMap, MapsModule module, ArrayNode keys, ArrayNode features, Locale locale) {
        String label = !Contract.isNull(bundle.getResourceBundle(MAP,bespokeMap.getLabel(),locale))?
                bundle.getResourceBundle(MAP,bespokeMap.getLabel(),locale):locationLoader.getLocation(bespokeMap.getLocation(), locale).getName();
        ObjectNode regionFilters = this.addFilters(bespokeMap.getCategory(), label);
        keys.add(regionFilters);
        this.addDmsData(this.buildProductSearch(bespokeMap.getLocation(), bespokeMap.getProdTypeId(), bespokeMap.getDmsCategory(), locale, DMSConstants.SORT_ALPHA, 100),
                module, regionFilters, features, locale);
    }

    private void addDmsData (ProductSearchBuilder dmsQuery, MapsModule module, ObjectNode filter, ArrayNode features, Locale locale){
        JsonNode dmsResponseData =  dmsDataService.cannedSearch(dmsQuery.buildCannedSearchInternal());
        if (dmsResponseData != null && !dmsResponseData.isEmpty()) {
            for (JsonNode jsonNode : dmsResponseData) {
                FlatImage image = imageFactory.createImage(jsonNode, module,locale);
                FlatLink link = new FlatLink(bundle.getResourceBundle(MAP, DISCOVER, locale), jsonNode.get("productLink").get("link").asText(), LinkType.INTERNAL);
                ObjectNode data = mapper.createObjectNode();
                String name = jsonNode.has(NAME) ? jsonNode.get(NAME).asText() : null;
                String description = jsonNode.has(DESCRIPTION) ? jsonNode.get(DESCRIPTION).asText() : null;
                String id = jsonNode.has(ID) ? jsonNode.get(ID).asText() : null;
                ObjectNode properties = mapService.getPropertyNode(name, description, image, filter, link, id);
                properties.put(ID, id);
                properties.put(TITLE, name);
                properties.put(DESCRIPTION, description);
                data.set(PROPERTIES, properties );
                data.set(GEOMETRY, mapService.getGeometryNode(mapService.getCoordinates(jsonNode.get("longitude").asDouble(), jsonNode.get("latitude").asDouble()), POINT));

                features.add(data);
            }
        }
    }

    private ProductSearchBuilder buildProductSearch (String location, String prodType, String category, Locale locale, String order, int size){
        return VsComponentManager.get(ProductSearchBuilder.class).location(location).productTypes(prodType).category(category).sortBy(order).size(size).locale(locale);
    }

    private BespokeDmsMap[] getValues(String mapType){
        switch(mapType) {
            case "IcentresMap":
                return ICentresMapTab.values();
            case "DistilleriesMap":
                return DistilleryMapTab.values();
            default:
                return null;
        }
    }
}

