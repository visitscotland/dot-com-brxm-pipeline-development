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
import org.hippoecm.hst.content.beans.standard.HippoBean;
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
    static final String TYPE = "type";
    static final String POINT = "Point";
    private static final Logger logger = LoggerFactory.getLogger(MapService.class);

    private final ObjectMapper mapper;
    private final DMSDataService dmsData;
    private final ResourceBundleService bundle;
    private final ImageFactory imageFactory;
    private final LinkService linkService;
    private final LocationLoader locationLoader;

    @Autowired
    public MapService(DMSDataService dmsData, ResourceBundleService bundle,ImageFactory imageFactory, LinkService linkService, ObjectMapper mapper, LocationLoader locationLoader) {
        this.dmsData = dmsData;
        this.bundle = bundle;
        this.locationLoader = locationLoader;
        this.imageFactory = imageFactory;
        this.linkService = linkService;
        this.mapper = mapper;
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
     * Method to build nodes when the document selected is a Stop
     *
     * @param locale locale to bring labels in the right language
     * @param category category or taxonomy for the mapcard (id and label)
     * @param module Mapsmodule needed for images and links
     * @param stop stop document information
     * @param feature ObjectNode to add the Stop information
     */
    public void buildStopNode(Locale locale, ObjectNode category, MapsModule module, Stop stop, ObjectNode feature){
        if (stop != null){
            Double latitude = null;
            Double longitude = null;
            FlatLink flatLink = null;
            HippoBean item = stop.getStopItem();
            FlatImage image = imageFactory.createImage(stop.getImage(), module, locale);
            boolean validPoint = true;
            if (item instanceof DMSLink) {
                JsonNode dmsNode = dmsData.productCard(((DMSLink) item).getProduct(), locale);
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
                logger.error(errorMessage);
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
    public void buildPageNode(Locale locale, ObjectNode category, MapsModule module, Page page, ObjectNode feature){
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
                JsonNode geometryNode = dmsData.getPolygonCoordinates(locationLoader.getLocation(destination.getLocation(), null).getId());
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
    public ObjectNode getPropertyNode(String title, String description, FlatImage image, ObjectNode  category, FlatLink link, String id) {
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
    public ObjectNode getGeometryNode(ArrayNode coordinates, String type) {
        ObjectNode geometry = mapper.createObjectNode();
        geometry.put(TYPE, type);
        geometry.set("coordinates", coordinates);

        return geometry;
    }


    public ArrayNode getCoordinates(Double longitude, Double latitude){
        ArrayNode coordinates = mapper.createArrayNode();
        coordinates.add(longitude);
        coordinates.add(latitude);
        return coordinates;

    }

}
