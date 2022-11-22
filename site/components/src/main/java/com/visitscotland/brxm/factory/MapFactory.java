package com.visitscotland.brxm.factory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.visitscotland.brxm.config.VsComponentManager;
import com.visitscotland.brxm.dms.*;
import com.visitscotland.brxm.dms.model.LocationObject;
import com.visitscotland.brxm.dms.RegionsMapTab;
import com.visitscotland.brxm.hippobeans.*;
import com.visitscotland.brxm.model.FlatImage;
import com.visitscotland.brxm.model.FlatLink;
import com.visitscotland.brxm.model.LinkType;
import com.visitscotland.brxm.model.MapsModule;
import com.visitscotland.brxm.services.LinkService;
import com.visitscotland.brxm.services.ResourceBundleService;
import com.visitscotland.brxm.utils.HippoUtilsService;
import com.visitscotland.utils.Contract;
import org.hippoecm.hst.content.beans.query.HstQueryResult;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoBeanIterator;
import org.hippoecm.hst.core.component.HstRequest;
import org.onehippo.taxonomy.api.Category;
import org.onehippo.taxonomy.api.Taxonomy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static com.visitscotland.brxm.dms.DMSConstants.DMSProduct.*;

@Component
public class MapFactory {

    static final String GEOMETRY = "geometry";
    static final String LABEL = "label";
    static final String DISCOVER = "map.discover";
    static final String PROPERTIES = "properties";
    static final String DESCRIPTION = "description";
    static final String NAME = "name";
    static final String FEATURE = "feature";
    static final String ID = "id";
    static final String REGIONS = "regions";
    static final String MAP = "map";
    static final String TYPE = "type";
    static final String POINT = "Point";

    private final LinkService linkService;
    private final LocationLoader locationLoader;
    private final DMSDataService dmsDataService;
    private final ImageFactory imageFactory;
    private final ResourceBundleService bundle;
    private final HippoUtilsService hippoUtilsService;
    private final ObjectMapper mapper;
    private static final Logger contentLogger = LoggerFactory.getLogger("content");

    public MapFactory(LinkService linkService, LocationLoader locationLoader, DMSDataService dmsDataService, ImageFactory imageFactory, ResourceBundleService bundle, HippoUtilsService hippoUtilsService) {
        this.linkService = linkService;
        this.locationLoader = locationLoader;
        this.dmsDataService = dmsDataService;
        this.imageFactory = imageFactory;
        this.bundle = bundle;
        this.hippoUtilsService = hippoUtilsService;
        this.mapper = new ObjectMapper();
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
        MapsModule module = new MapsModule();

        module.setId(mapModuleDocument.getCanonicalUUID());
        module.setTitle(mapModuleDocument.getTitle());
        module.setIntroduction(mapModuleDocument.getCopy());
        module.setTabTitle(mapModuleDocument.getTabTitle());

        ObjectNode featureCollectionGeoJson = mapper.createObjectNode();
        featureCollectionGeoJson.put(TYPE, "FeatureCollection");
        ArrayNode features = mapper.createArrayNode();
        ArrayNode keys = mapper.createArrayNode();
        if (page instanceof General) {
            buildMapGeneralPages(request, mapModuleDocument, module, keys, features);
        } else if (page instanceof Destination) {
            buildMapDestinationPages(request,(Destination)page, mapModuleDocument, module, keys, features);
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
                keys.add(getFilterNode(mainCategory, request.getLocale()));
                //if the map has 2 levels, the parent wont be a category for the mapcards, so pick sons
                if (!mainCategory.getChildren().isEmpty()) {
                    for(Category child : mainCategory.getChildren()){
                        //find all the documents with a taxonomy/category
                        addMapDocumentsToJson(request, module,child, features);
                    }
                } else {
                    //find all the documents with a taxonomy/category
                    addMapDocumentsToJson(request, module, mainCategory, features);
                }
            }
            if (!Contract.isNull(mapModuleDocument.getFeaturedPlacesItem())) {
                addFeaturePlacesNode(module, mapModuleDocument.getCategories(), request.getLocale(), keys, features);
            }
        }
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
    private void buildMapDestinationPages (HstRequest request,Destination destinationPage, MapModule mapModuleDocument, MapsModule module, ArrayNode keys, ArrayNode features){
        if (!Contract.isNull(mapModuleDocument.getFeaturedPlacesItem())) {
            addFeaturePlacesNode(module, mapModuleDocument.getCategories(), request.getLocale() , keys, features);
        }
        //TODO create a class for Regions, another for Cities and ski, icentre...
       if (Arrays.asList(destinationPage.getKeys()).contains(REGIONS)) {
           for (RegionsMapTab prodType: RegionsMapTab.values()){
               //filters
               ObjectNode filter = mapper.createObjectNode();

               filter.put(ID, prodType.getProdTypeId().equalsIgnoreCase(DMSConstants.TYPE_TOWN)?"cities":"towns");
               filter.put(LABEL,bundle.getResourceBundle(MAP,"map."+prodType.getProdTypeId(), request.getLocale()));
               keys.add(filter);

               ProductSearchBuilder dmsQuery = VsComponentManager.get(ProductSearchBuilder.class).location(destinationPage.getLocation())
                       .productTypes(prodType.getProdTypeId()).category(prodType.getCategory())
                       .sortBy(DMSConstants.SORT_ALPHA).size(100);
               for (JsonNode jsonNode : dmsDataService.cannedSearch(dmsQuery)) {
                   FlatImage image = new FlatImage();
                   if (jsonNode.has("images")) {
                       image.setExternalImage(jsonNode.get("images").get(0).get("mediaUrl").asText());
                   }
                   FlatLink link = new FlatLink( bundle.getResourceBundle(MAP, DISCOVER, request.getLocale()),jsonNode.get("productLink").get("link").asText(),LinkType.INTERNAL);
                   ObjectNode data = mapper.createObjectNode();
                   String name = jsonNode.has(NAME)?jsonNode.get(NAME).asText() : null;
                   String description = jsonNode.has(DESCRIPTION)?jsonNode.get(DESCRIPTION).asText() : null;
                   String id = jsonNode.has(ID)?jsonNode.get(ID).asText() : null;
                   data.set(PROPERTIES, getPropertyNode(name, description,image,filter,link,id));
                   data.set(GEOMETRY, getGeometryNode(getCoordinates(jsonNode.get("longitude").asDouble(),jsonNode.get("latitude").asDouble()),POINT));

                   features.add(data);
               }

           }
        }/*else{
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

    /** Method to build the property section for the GeoJson file generated for maps
     *
     * @param title Mapcard title
     * @param description Mapcard description
     * @param image Mapcard image
     * @param category Mapcard category (id and label)
     * @param link Mapcard link to the page
     * @return ObjectNode with the right format to be consumed by the front end team
     */
    private ObjectNode getPropertyNode(String title, String description, FlatImage image, ObjectNode  category, FlatLink link, String id) {
        ObjectNode rootNode = mapper.createObjectNode();
        rootNode.set("category", category);
        rootNode.put(ID, id);
        rootNode.put("title", title);
        rootNode.put(DESCRIPTION, description);

        if (!Contract.isNull(image)){
            rootNode.put("image", !Contract.isNull(image.getCmsImage())? HippoUtilsService.createUrl(image.getCmsImage()) : image.getExternalImage());
        }

        if (!Contract.isNull(link)){
            ObjectNode linkNode = mapper.createObjectNode();
            linkNode.put(LABEL, link.getLabel() + " " + title);
            linkNode.put("link", link.getLink());
            linkNode.put(TYPE, link.getType().name());
            rootNode.set("link", linkNode);
        }

        return rootNode;
    }

    /**
     * Method to build the geometry node for the GeoJson file generated for maps
     * @param coordinates coordinates
     * @param type coordinate type
     * @return ObjectNode with the right format to be consumed by the front end team
     */
    private ObjectNode getGeometryNode(ArrayNode coordinates, String type) {
        ObjectNode geometry = mapper.createObjectNode();
        geometry.put(TYPE, type);
        geometry.set("coordinates", coordinates);

        return geometry;
    }

    /**
     * Method to build ObjectNode key label for category/taxonomy
     *
     * @param category taxonomy category to build key label
     * @param locale language wanted
     * @return ObjectNode key label for categories
     */
    private ObjectNode getCategoryNode(Category category, Locale locale) {
        ObjectNode filter = mapper.createObjectNode();
        filter.put(ID, category.getKey());
        filter.put(LABEL, category.getInfo(locale).getName());

        return filter;
    }

    /**
     *
     * @param module module to be sent to feds
     * @param categories list of content blocks to iterate
     * @param locale language/locale
     * @param keys filters for maps
     * @param features features information for mapcards
     */
    private void addFeaturePlacesNode(MapsModule module, List<MapCategory> categories, Locale locale, ArrayNode keys, ArrayNode features) {
        for (MapCategory featuredPlaces : categories) {
            ObjectNode filter = mapper.createObjectNode();
            filter.put(ID, "featured");
            if (Contract.isEmpty(featuredPlaces.getTitle())) {
                filter.put(LABEL, bundle.getResourceBundle(MAP, "map.feature-default-title", locale));
            } else {
                filter.put(LABEL, featuredPlaces.getTitle());
            }
            keys.add(filter);
            for(HippoBean link : featuredPlaces.getMapPins()){
                ObjectNode feat = mapper.createObjectNode();
                feat.put(TYPE, FEATURE);
                if (link instanceof Destination){
                    buildPageNode(locale, filter, module,(Destination) link, feat);
                }else if (link instanceof Stop){
                    buildStopNode(locale, filter, module,(Stop) link,feat);
                }else if (link instanceof SpecialLinkCoordinates){
                    SpecialLinkCoordinates linkCoordinates = ((SpecialLinkCoordinates) link);
                    Page otherPage = (Page)(linkCoordinates).getLink();
                    buildPageNode(locale, filter, module,otherPage,feat);
                    feat.set(GEOMETRY, getGeometryNode(getCoordinates(((SpecialLinkCoordinates)link).getCoordinates().getLongitude(),((SpecialLinkCoordinates)link).getCoordinates().getLatitude()
                    ),POINT));
                }
                features.add(feat);
            }
        }
    }

    /**
     * Method to build a normal Json with parameters for the maps, in particular for the filters to use in the map
     * @param child Category or taxonomy to use as a filter, adding id and label
     * @param locale locale to bring the label in the right language
     * @return ObjectNode with the filters to be used
     */
    private ObjectNode getFilterNode(Category child, Locale locale) {
        ObjectNode filter = this.getCategoryNode(child,locale);
        if (!child.getChildren().isEmpty()){
            ArrayNode childrenArray = mapper.createArrayNode();
            for (Category children : child.getChildren()) {
                childrenArray.add(this.getCategoryNode(children, locale));
            }
            filter.set("subCategory",childrenArray);
        }
        return filter;
    }

    /**
     *
     * @param request the request
     * @param module module to be consumed
     * @param category the category or taxonomy wanted
     * @param features ArrayNode to add the features to the mapcard
     */
    private void addMapDocumentsToJson(HstRequest request, MapsModule module, Category category, ArrayNode features) {
        HstQueryResult result = hippoUtilsService.getDocumentsByTaxonomy(request, category,"@hippotaxonomy:keys","visitscotland:title",Destination.class, Stop.class);
        if (result != null) {
            final HippoBeanIterator it = result.getHippoBeans();
            while (it.hasNext()) {
                ObjectNode feature = getMapDocuments(request.getLocale(), category, module, it);
                if (feature != null && !feature.isEmpty()) {
                    features.add(feature);
                }
            }
        }
    }


    /**
     * Method that build properties and geometry nodes for the GeoJson file to be consumed by feds
     * for each destination and stop with the category/taxonomy selected
     *
     * @param locale the locale/language
     * @param category the category/taxonomy selected
     * @param module the map module
     * @param it iterator to iterate the list of destinations or stops
     * @return ObjectNode with the right format to be sent to FEDs
     */
    private ObjectNode getMapDocuments(Locale locale, Category category, MapsModule module, HippoBeanIterator it){
        //find all the documents with a taxonomy
        final HippoBean bean = it.nextHippoBean();
        ObjectNode feature = null;
        if (!Contract.isNull(bean)) {
            feature = mapper.createObjectNode();
            if (bean instanceof Destination) {
                feature.put(TYPE, FEATURE);
                buildPageNode(locale, getCategoryNode(category, locale), module,((Destination) bean), feature);
            } else {
                buildStopNode(locale,getCategoryNode(category, locale),module, ((Stop) bean), feature);
            }
        }
        return feature;
    }

    /**
     * Method to build nodes when the document selected is a Stop
     *
     * @param locale locale to bring labels in the right language
     * @param category category or taxonomy for the mapcard (id and label)
     * @param module Mapsmodule needed for images and links
     * @param stop stop document information
     * @param feature ObjectNode to add the Stop information
     */
    private void buildStopNode(Locale locale, ObjectNode category, MapsModule module, Stop stop, ObjectNode feature){
        if (stop != null){
            Double latitude = null;
            Double longitude = null;
            FlatLink flatLink = null;
            HippoBean item = stop.getStopItem();
            FlatImage image = imageFactory.createImage(stop.getImage(), module, locale);
            boolean validPoint = true;
            if (item instanceof DMSLink) {
                JsonNode dmsNode = dmsDataService.productCard(((DMSLink) item).getProduct(), locale);
                if (!Contract.isNull(dmsNode)) {
                    flatLink = linkService.createDmsLink(locale,(DMSLink) item, dmsNode);
                    flatLink.setLabel(bundle.getResourceBundle(MAP, DISCOVER, locale));
                    if (Contract.isNull(stop.getImage()) && dmsNode.has(IMAGE)) {
                        image = imageFactory.createImage(dmsNode, module, locale);
                    }
                    if (dmsNode.has(LATITUDE) && dmsNode.has(LONGITUDE)) {
                        latitude = dmsNode.get(LATITUDE).asDouble();
                        longitude = dmsNode.get(LONGITUDE).asDouble();
                    }
                }else{
                    validPoint = false;
                }

            } else if (item instanceof ItineraryExternalLink) {
                    ItineraryExternalLink externalStop = ((ItineraryExternalLink) item);
                    latitude = externalStop.getCoordinates().getLatitude();
                    longitude = externalStop.getCoordinates().getLongitude();
                    flatLink = new FlatLink(bundle.getResourceBundle(MAP, DISCOVER, locale),externalStop.getExternalLink().getLink(), LinkType.EXTERNAL);
            }
            if (validPoint && !Contract.isNull(latitude) && !Contract.isNull(longitude)) {
                feature.put(TYPE, FEATURE);
                String description = stop.getDescription().getContent().trim().replace("\"", "'");
                if (description.startsWith("<p>") && description.endsWith("</p>")) {
                    description = description.substring(3, description.length() - 4);
                }
                feature.set(PROPERTIES, getPropertyNode(stop.getTitle(), description,
                        image, category, flatLink, stop.getCanonicalUUID()));
                feature.set(GEOMETRY, getGeometryNode(getCoordinates(longitude,latitude),POINT));
            }else{
                String errorMessage = String.format("Failed to create map card '%s', please review the document attached at: %s", item.getDisplayName(), item.getPath() );
                module.setErrorMessages(Collections.singletonList(errorMessage));
                contentLogger.error(errorMessage);
            }
        }
    }

    /**
     * Method to build nodes when the document selected is a Destination
     *
     * @param locale language of the destination
     * @param category the category/taxonomy for the destination
     * @param module map module
     * @param page the destination or other pages
     * @param feature json to build the features and geometry nodes
     */
    private void buildPageNode(Locale locale, ObjectNode category, MapsModule module, Page page, ObjectNode feature){
        FlatLink flatLink = linkService.createSimpleLink(page, module, locale);
        flatLink.setLabel(bundle.getResourceBundle(MAP, DISCOVER, locale));
        ObjectNode properties = getPropertyNode(page.getTitle(), page.getTeaser(),
                imageFactory.createImage(page.getImage(), module, locale), category,
                flatLink, page.getCanonicalUUID());
        if (page instanceof Destination){
            Destination destination = (Destination) page;
            LocationObject location = locationLoader.getLocation(destination.getLocation(), Locale.UK);
            feature.set(PROPERTIES, properties);
            if (Arrays.asList(destination.getKeys()).contains(REGIONS)){
                JsonNode geometryNode = dmsDataService.getPolygonCoordinates(locationLoader.getLocation(destination.getLocation(), null).getId());
                if(geometryNode!=null && !geometryNode.isEmpty()) {
                    feature.set(GEOMETRY, getGeometryNode((ArrayNode) geometryNode.get("coordinates"), geometryNode.get(TYPE).asText()));
                }
            }else {
                feature.set(GEOMETRY, getGeometryNode(getCoordinates(location.getLongitude(),location.getLatitude()),POINT));
            }
        }else {
            feature.set(PROPERTIES, properties);
        }
    }


    private ArrayNode getCoordinates(Double longitude, Double latitude){
        ArrayNode coordinates = mapper.createArrayNode();
        coordinates.add(longitude);
        coordinates.add(latitude);
        return coordinates;

    }
}

