package com.visitscotland.brxm.factory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.visitscotland.brxm.config.VsComponentManager;
import com.visitscotland.brxm.dms.*;
import com.visitscotland.brxm.hippobeans.*;
import com.visitscotland.brxm.model.FlatImage;
import com.visitscotland.brxm.model.FlatLink;
import com.visitscotland.brxm.model.LinkType;
import com.visitscotland.brxm.model.MapsModule;
import com.visitscotland.brxm.services.MapService;
import com.visitscotland.brxm.services.ResourceBundleService;
import com.visitscotland.utils.Contract;
import org.hippoecm.hst.core.component.HstRequest;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class MapDestinationFactory {

    static final String GEOMETRY = "geometry";
    static final String LABEL = "label";
    static final String DISCOVER = "map.discover";
    static final String PROPERTIES = "properties";
    static final String DESCRIPTION = "description";
    static final String NAME = "name";
    static final String ID = "id";
    static final String REGIONS = "regions";
    static final String MAP = "map";
    static final String TYPE = "type";
    static final String POINT = "Point";

    private final MapService mapService;
    private final DMSDataService dmsDataService;
    private final ResourceBundleService bundle;
    private final ObjectMapper mapper;

    public MapDestinationFactory(MapService mapService, DMSDataService dmsDataService, ResourceBundleService bundle) {
        this.dmsDataService = dmsDataService;
        this.bundle = bundle;
        this.mapper = new ObjectMapper();
        this.mapService = mapService;
    }

    /**
     * Method to build a MapsModule to be used by feds with
     *
     * @param request HstRequest request
     * @param mapModuleDocument CMS document
     * @param page type of page where the map is added
     * @return module to be sent to feds
     */
    public MapsModule getModule(HstRequest request, MapModule mapModuleDocument, Page page) {
        //TODO Factory inherance from a new MapFactory?
        MapsModule module = new MapsModule();

        module.setId(mapModuleDocument.getCanonicalUUID());
        module.setTitle(mapModuleDocument.getTitle());
        module.setIntroduction(mapModuleDocument.getCopy());
        module.setTabTitle(mapModuleDocument.getTabTitle());

        ObjectNode featureCollectionGeoJson = mapper.createObjectNode();
        featureCollectionGeoJson.put(TYPE, "FeatureCollection");
        ArrayNode features = mapper.createArrayNode();
        ArrayNode keys = mapper.createArrayNode();
        buildMapPages(request,(Destination)page, mapModuleDocument, module, keys, features);
        //add first Json for filters
        module.setFilters(keys);

        //add second json (geoJson) to the module
        featureCollectionGeoJson.set("features", features);
        module.setGeoJson(featureCollectionGeoJson);
        module.setHippoBean(mapModuleDocument);
        return module;
    }


    /**
     *
     * Method to build maps for Destination pages based on DMS products and manual and optional featured places
     *
     * @param request the request
     * @param mapModuleDocument module coming from the CMS
     * @param module module to be sent to feds
     * @param keys filters for maps
     * @param features features information for mapcards
     */
    private void buildMapPages (HstRequest request,Destination destinationPage, MapModule mapModuleDocument, MapsModule module, ArrayNode keys, ArrayNode features){
        if (!Contract.isNull(mapModuleDocument.getFeaturedPlacesItem())) {
            mapService.addFeaturePlacesNode(module, mapModuleDocument.getCategories(), request.getLocale() , keys, features);
        }
        //TODO create a class for Regions, another for Cities and ski, icentre...
    /*   if (Arrays.asList(destinationPage.getKeys()).contains(REGIONS)) {
           for (RegionsMapTab prodType: RegionsMapTab.values()){
               //filters
               ObjectNode filter = mapper.createObjectNode();

               filter.put(ID, prodType.getProdTypeId().equalsIgnoreCase(DMSConstants.TYPE_TOWN)?"cities":"towns");
               filter.put(LABEL,bundle.getResourceBundle(MAP,prodType.getLabel(), request.getLocale()));
               keys.add(filter);

               ProductSearchBuilder dmsQuery = VsComponentManager.get(ProductSearchBuilder.class).location(destinationPage.getLocation())
                       .productTypes(prodType.getProdTypeId()).category(prodType.getCategory())
                       .sortBy(DMSConstants.SORT_ALPHA).size(100);
               JsonNode regionsData =  dmsDataService.cannedSearch(dmsQuery);
               if (regionsData != null && !regionsData.isEmpty()) {
                   for (JsonNode jsonNode : regionsData) {
                       FlatImage image = new FlatImage();
                       if (jsonNode.has("images")) {
                           image.setExternalImage(jsonNode.get("images").get(0).get("mediaUrl").asText());
                       }
                       FlatLink link = new FlatLink(bundle.getResourceBundle(MAP, DISCOVER, request.getLocale()), jsonNode.get("productLink").get("link").asText(), LinkType.INTERNAL);
                       ObjectNode data = mapper.createObjectNode();
                       String name = jsonNode.has(NAME) ? jsonNode.get(NAME).asText() : null;
                       String description = jsonNode.has(DESCRIPTION) ? jsonNode.get(DESCRIPTION).asText() : null;
                       String id = jsonNode.has(ID) ? jsonNode.get(ID).asText() : null;
                       data.set(PROPERTIES, mapService.getPropertyNode(name, description, image, filter, link, id));
                       data.set(GEOMETRY, mapService.getGeometryNode(mapService.getCoordinates(jsonNode.get("longitude").asDouble(), jsonNode.get("latitude").asDouble()), POINT));

                       features.add(data);
                   }
               }
           }
        }else{
           for (CitiesMapTab prodType : CitiesMapTab.values()) {
               //filters
               ObjectNode filter = mapper.createObjectNode();
               filter.put(ID, prodType.getProdTypeId());
               filter.put(LABEL,bundle.getResourceBundle(MAP,"map."+prodType.getProdTypeId(), request.getLocale()));

               ///endpoint for data (pins)
               ProductSearchBuilder dmsQuery = VsComponentManager.get(ProductSearchBuilder.class).location(destinationPage.getLocation())
                       .productTypes(prodType.getProdTypeId());
               filter.put("geoJsonEndpoint", dmsQuery.buildDataMap());

                //Endpoint base to bring 24 random results
               filter.put("listProductsEndPoint", dmsQuery.buildCannedSearch());

               //subcategories added
               ArrayNode childrenArray = dmsDataService.getCatGroup(prodType.getProdTypeId(),request.getLocale().getLanguage());
               filter.set("subCategory",childrenArray);
               keys.add(filter);
           }

       }*/
    }
}

