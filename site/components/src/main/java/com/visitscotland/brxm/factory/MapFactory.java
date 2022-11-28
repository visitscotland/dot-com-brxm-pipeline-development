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
    static final String MAP = "map";
    static final String POINT = "Point";

    private final MapService mapService;
    private final HippoUtilsService hippoUtilsService;
    private final ObjectMapper mapper;
    private final DMSDataService dmsDataService;
    private final ResourceBundleService bundle;
    private final Properties propertiesService;
    private final ImageFactory imageFactory;

    public MapFactory(MapService mapService, HippoUtilsService hippoUtilsService, DMSDataService dmsDataService, ResourceBundleService bundle, Properties properties, ImageFactory imageFactory) {
        this.hippoUtilsService = hippoUtilsService;
        this.mapper = new ObjectMapper();
        this.mapService = mapService;
        this.dmsDataService = dmsDataService;
        this.bundle = bundle;
        this.propertiesService = properties;
        this.imageFactory = imageFactory;
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
        module.setTitle(mapModuleDocument.getTitle());
        module.setIntroduction(mapModuleDocument.getCopy());
        module.setTabTitle(mapModuleDocument.getTabTitle());

        ObjectNode featureCollectionGeoJson = mapper.createObjectNode();
        featureCollectionGeoJson.put(TYPE, "FeatureCollection");
        ArrayNode features = mapper.createArrayNode();
        ArrayNode keys = mapper.createArrayNode();
        if (page instanceof General){
            buildMapGeneralPages(request, mapModuleDocument, module, keys, features);
        }else{
            buildDestinationMapPages(request.getLocale(),(Destination)page, mapModuleDocument, module, keys, features);
        }

        //add first Json for filters
        module.setFilters(keys);

        //add second json (geoJson) to the module
        featureCollectionGeoJson.set("features", features);
        module.setGeoJson(featureCollectionGeoJson);
        module.setHippoBean(mapModuleDocument);
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
        for (String taxonomy : mapModuleDocument.getKeys()) {
            //get all the Taxonomy information
            Taxonomy vsTaxonomyTree = hippoUtilsService.getTaxonomy();
            for (Category mainCategory : vsTaxonomyTree.getCategoryByKey(taxonomy).getChildren()) {
                keys.add(mapService.getFilterNode(mainCategory, request.getLocale()));
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
            if (!Contract.isNull(mapModuleDocument.getFeaturedPlacesItem())) {
                mapService.addFeaturePlacesNode(module, mapModuleDocument.getCategories(), request.getLocale(), keys, features);
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
        if (!Contract.isNull(mapModuleDocument.getFeaturedPlacesItem())) {
            mapService.addFeaturePlacesNode(module, mapModuleDocument.getCategories(), locale , keys, features);
        }
        //TODO create a class for Regions, another for Cities and ski, icentre...
        if (Arrays.asList(destinationPage.getKeys()).contains(REGIONS)) {
            for (RegionsMapTab prodType: RegionsMapTab.values()) {
                ObjectNode regionFilters = this.addRegionsFilters(prodType, locale);
                features.add(regionFilters);
                this.addRegionsData(prodType, destinationPage, module,  regionFilters, features,  locale);
            }

        }else{
            for (CitiesMapTab prodType : CitiesMapTab.values()) {
                //filters
                ObjectNode filter = mapService.getCategoryNode(prodType.getProdTypeId(), bundle.getResourceBundle(MAP, prodType.getLabel(), locale));

                ///endpoint for data (pins)
                ProductSearchBuilder dmsQuery = VsComponentManager.get(ProductSearchBuilder.class).location(destinationPage.getLocation())
                        .productTypes(prodType.getProdTypeId());
                filter.put("pinsEndpoint", dmsQuery.buildDataMap());

                //Endpoint base to bring 24 random results
                filter.put("listProductsEndPoint", dmsQuery.buildCannedSearch());

                filter.put("pinClickEndPoint", propertiesService.getDmsDataPublicHost() + DMSConstants.VS_DMS_PRODUCT_MAP_CARD
                        +"locale="+locale+"&id=");


                //subcategories added
                ArrayNode childrenArray = dmsDataService.getCatGroup(prodType.getProdTypeId(),locale.getLanguage());
                filter.set("subCategory",childrenArray);
                keys.add(filter);
            }
        }
    }

    private ObjectNode addRegionsFilters (RegionsMapTab prodType, Locale locale){
        return  mapService.getCategoryNode(prodType.getProdTypeId().equalsIgnoreCase(DMSConstants.TYPE_TOWN)?"cities":"towns",
                bundle.getResourceBundle(MAP, prodType.getLabel(), locale));
    }

    private void addRegionsData (RegionsMapTab prodType, Destination destinationPage, MapsModule module, ObjectNode filter, ArrayNode features, Locale locale){
        ProductSearchBuilder dmsQuery = VsComponentManager.get(ProductSearchBuilder.class).location(destinationPage.getLocation())
                .productTypes(prodType.getProdTypeId()).category(prodType.getCategory())
                .sortBy(DMSConstants.SORT_ALPHA).size(100);
        JsonNode regionsData =  dmsDataService.cannedSearch(dmsQuery);
        if (regionsData != null && !regionsData.isEmpty()) {
            for (JsonNode jsonNode : regionsData) {
                FlatImage image = imageFactory.createImage(jsonNode, module,locale);
                FlatLink link = new FlatLink(bundle.getResourceBundle(MAP, DISCOVER, locale), jsonNode.get("productLink").get("link").asText(), LinkType.INTERNAL);
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

}

