package com.visitscotland.brxm.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.visitscotland.brxm.dms.DMSDataService;
import com.visitscotland.brxm.dms.LocationLoader;
import com.visitscotland.brxm.dms.model.LocationObject;
import com.visitscotland.brxm.factory.ImageFactory;
import com.visitscotland.brxm.hippobeans.*;
import com.visitscotland.brxm.model.*;
import com.visitscotland.brxm.utils.HippoUtilsService;
import com.visitscotland.utils.Contract;
import org.hippoecm.hst.content.beans.query.HstQueryResult;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoBeanIterator;
import org.hippoecm.hst.core.component.HstRequest;
import org.onehippo.taxonomy.api.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.*;

import static com.visitscotland.brxm.dms.DMSConstants.DMSProduct.*;

@Component
public class MapService {
    static final String GEOMETRY = "geometry";
    static final String LABEL = "label";
    static final String DISCOVER = "map.discover";
    static final String PROPERTIES = "properties";
    static final String DESCRIPTION = "description";
    static final String FEATURE = "feature";
    static final String ID = "id";
    static final String REGIONS = "regions";
    static final String MAP = "map";
    static final String BESPOKEMAP = "bespoke-maps";
    static final String TYPE = "type";
    static final String POINT = "Point";
    static final String ALTERNATIVE_CATEGORIES = "alternativeCategories";
    private static final Logger logger = LoggerFactory.getLogger(MapService.class);

    private final ObjectMapper mapper;
    private final DMSDataService dmsData;
    private final ResourceBundleService bundle;
    private final ImageFactory imageFactory;
    private final LinkService linkService;
    private final LocationLoader locationLoader;
    private final HippoUtilsService hippoUtilsService;

    @Autowired
    public MapService(DMSDataService dmsData, ResourceBundleService bundle,ImageFactory imageFactory, LinkService linkService, ObjectMapper mapper, LocationLoader locationLoader, HippoUtilsService hippoUtilsService) {
        this.dmsData = dmsData;
        this.bundle = bundle;
        this.locationLoader = locationLoader;
        this.imageFactory = imageFactory;
        this.linkService = linkService;
        this.mapper = mapper;
        this.hippoUtilsService = hippoUtilsService;
    }

    /**
     * Method to build a normal Json with parameters for the maps, in particular for the filters to use in the map coming from Taxonomies
     * @param child Category or taxonomy to use as a filter, adding id and label
     * @param locale locale to bring the label in the right language
     * @return ObjectNode with the filters to be used
     */
    public ObjectNode addFilterNode(Category child, Locale locale) {
        ObjectNode filter = buildCategoryNode(child.getKey(), getTaxonomyLabel(child, locale));
        if (!child.getChildren().isEmpty()){
            ArrayNode childrenArray = mapper.createArrayNode();
            for (Category children : child.getChildren()) {
                childrenArray.add(buildCategoryNode(children.getKey(), children.getInfo(locale).getName()));
            }
            filter.set(ALTERNATIVE_CATEGORIES,childrenArray);
        }
        return filter;
    }


    /**
     * Method to build ObjectNode key label for category/taxonomy
     *
     * @param key taxonomy category to build key
     * @param label taxonomy category to build label
     * @return ObjectNode key label for categories
     */
    public ObjectNode buildCategoryNode(String key, String label) {
        ObjectNode filter = mapper.createObjectNode();
        filter.put(ID, key);
        filter.put(LABEL, label);

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
    public void addFeaturePlacesNode(MapsModule module, List<MapCategory> categories, Locale locale, ArrayNode keys, ArrayNode features) {
        for (MapCategory featuredPlaces : categories) {
            ObjectNode filter = mapper.createObjectNode();
            filter.put(ID, "featured");
            if (Contract.isEmpty(featuredPlaces.getTitle())) {
                filter.put(LABEL, bundle.getResourceBundle(MAP, "map.feature-default-title", locale));
            } else {
                filter.put(LABEL, featuredPlaces.getTitle());
            }
            boolean addFeaturedTab = false;
            for(HippoBean link : featuredPlaces.getMapPins()) {
                boolean validPoint = true;
                ObjectNode feat = mapper.createObjectNode();
                feat.put(TYPE, FEATURE);
                if (link != null) {
                    addFeaturedTab = true;
                    if (link instanceof Destination) {
                        buildPageNode(locale, filter, module, (Destination) link, feat);
                    } else if (link instanceof Stop) {
                        validPoint= buildStopNode(locale, filter, module, (Stop) link, feat);
                    } else if (link instanceof SpecialLinkCoordinates) {
                        SpecialLinkCoordinates linkCoordinates = ((SpecialLinkCoordinates) link);
                        Page otherPage = (Page) (linkCoordinates).getLink();
                        buildPageNode(locale, filter, module, otherPage, feat);
                        feat.set(GEOMETRY, getGeometryNode(getCoordinates(((SpecialLinkCoordinates) link).getCoordinates().getLongitude(), ((SpecialLinkCoordinates) link).getCoordinates().getLatitude()
                        ), POINT));
                    }
                    if (validPoint) {
                        features.add(feat);
                    }
                }
            }
            if (addFeaturedTab){
                keys.add(filter);
            }
        }
    }

    /**
     * Method to build nodes when the document selected is a Stop
     *
     * @param locale locale to bring labels in the right language
     * @param category category or taxonomy for the mapcard (id and label)
     * @param module Mapsmodule needed for images and links
     * @param stop stop document information
     * @param feature ObjectNode to add the Stop information
     *
     * @return boolean to indicate if the stop/dms/pin is valid and was built as expected
     */
    private boolean buildStopNode(Locale locale, ObjectNode category, MapsModule module, Stop stop, ObjectNode feature){
        boolean validPoint = false;
        if (stop != null){
            Double latitude = null;
            Double longitude = null;
            FlatLink flatLink = null;
            HippoBean item = stop.getStopItem();
            FlatImage image = imageFactory.createImage(stop.getImage(), module, locale);
            if (item instanceof DMSLink) {
                JsonNode dmsNode = dmsData.productCard(((DMSLink) item).getProduct(), locale);
                if (!Contract.isNull(dmsNode)) {
                    validPoint = true;
                    flatLink = linkService.createDmsLink(locale,(DMSLink) item, dmsNode);
                    flatLink.setLabel(bundle.getResourceBundle(MAP, DISCOVER, locale));
                    if (Contract.isNull(stop.getImage()) && dmsNode.has(IMAGE)) {
                        image = imageFactory.createImage(dmsNode, module, locale);
                    }
                    if (dmsNode.has(LATITUDE) && dmsNode.has(LONGITUDE)) {
                        latitude = dmsNode.get(LATITUDE).asDouble();
                        longitude = dmsNode.get(LONGITUDE).asDouble();
                    }
                }
            } else if (item instanceof ItineraryExternalLink) {
                validPoint = true;
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

                ObjectNode properties = getPropertyNode(stop.getTitle(), description,
                        image, category, flatLink, stop.getCanonicalUUID());

                if (stop.getKeys() != null && stop.getKeys().length>0) {
                    List<String> listKeys = new ArrayList<>(Arrays.asList(stop.getKeys()));
                    listKeys.remove(category.get("id").asText());
                    addSubcategories(properties, listKeys, locale);
                }

                feature.set(PROPERTIES, properties);
                feature.set(GEOMETRY, getGeometryNode(getCoordinates(longitude,latitude),POINT));
            }else{
                String errorMessage = String.format("Failed to create map card '%s', please review the document attached at: %s", item.getDisplayName(), item.getPath() );
                module.setErrorMessages(Collections.singletonList(errorMessage));
                logger.error(errorMessage);
            }
        }
        return validPoint;
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
                JsonNode geometryNode = dmsData.getLocationBorders(locationLoader.getLocation(destination.getLocation(), null).getId(),true);
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



    /** Method to build the property section for the GeoJson file generated for maps
     *
     * @param title Mapcard title
     * @param description Mapcard description
     * @param image Mapcard image
     * @param category Mapcard category (id and label)
     * @param link Mapcard link to the page
     * @return ObjectNode with the right format to be consumed by the front end team
     */
    public ObjectNode getPropertyNode(String title, String description, FlatImage image, ObjectNode category, FlatLink link, String id) {
        ObjectNode rootNode = mapper.createObjectNode();

        rootNode.put(ID, id);
        rootNode.put("title", title);
        rootNode.put(DESCRIPTION, description);
        rootNode.set("category", category);

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
    public ObjectNode getGeometryNode(ArrayNode coordinates, String type) {
        ObjectNode geometry = mapper.createObjectNode();
        geometry.put(TYPE, type);
        geometry.set("coordinates", coordinates);

        return geometry;
    }

    /**
     * build array node of coordinates given longitude and latitude
     * @param longitude longitude value
     * @param latitude latitude value
     * @return array node of coordinates given longitude and latitude
     */
    public ArrayNode getCoordinates(Double longitude, Double latitude){
        ArrayNode coordinates = mapper.createArrayNode();
        coordinates.add(longitude);
        coordinates.add(latitude);
        return coordinates;
    }

    /**
     *
     * @param request the request
     * @param module module to be consumed
     * @param category the category or taxonomy wanted
     * @param features ArrayNode to add the features to the mapcard
     */
    public void addMapDocumentsToJson(HstRequest request, MapsModule module, Category category, ArrayNode features) {
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
                buildPageNode(locale, buildCategoryNode(category.getKey(), getTaxonomyLabel (category, locale)), module,((Destination) bean), feature);
            } else {
                buildStopNode(locale, buildCategoryNode(category.getKey(),getTaxonomyLabel (category, locale)),module, ((Stop) bean), feature);
            }
        }
        return feature;
    }
    /**
     *
     * @param locale the locale/language
     * @param category the category/taxonomy selected
     *
     * @return String with the right label for the final user
     */
    private String getTaxonomyLabel (Category category, Locale locale){
        return bundle.getResourceBundle(BESPOKEMAP,category.getKey() ,locale) != null?
               bundle.getResourceBundle(BESPOKEMAP,category.getKey() ,locale) : category.getInfo(locale).getName();
    }

    private void addSubcategories (ObjectNode rootNode, List<String> subcategory, Locale locale) {
        if (subcategory != null && !subcategory.isEmpty()) {
            ArrayNode subcategoryArrayNode = mapper.createArrayNode();
            StringBuilder jsonNodeName = new  StringBuilder();
            for (String categoryKey : subcategory) {
                Category mainCategory = hippoUtilsService.getTaxonomy().getCategoryByKey(categoryKey);

                ObjectNode subcategoryNode = mapper.createObjectNode();
                subcategoryNode.put(ID, mainCategory.getKey().split("-", 2)[1]);
                String categoryLabel = getTaxonomyLabel(mainCategory, locale);
                if (Contract.isEmpty(jsonNodeName.toString())){
                    jsonNodeName.append(bundle.getResourceBundle(BESPOKEMAP, mainCategory.getParent().getKey(), locale));
                }else{
                    jsonNodeName.append(",");
                }
                jsonNodeName.append(" ").append(categoryLabel.split(" ", 2)[1]);
                subcategoryNode.put(LABEL,categoryLabel);
                subcategoryArrayNode.add(subcategoryNode);
            }
            String concatenatedSubCategories = jsonNodeName.toString();
            if (concatenatedSubCategories.contains(",")){
                concatenatedSubCategories = jsonNodeName.replace(concatenatedSubCategories.lastIndexOf(","), concatenatedSubCategories.lastIndexOf(",") + 1, " &" ).toString();
            }
            rootNode.put ("subtitle", concatenatedSubCategories);
            rootNode.put("alternativeCategories", subcategoryArrayNode);

        }
    }
}
