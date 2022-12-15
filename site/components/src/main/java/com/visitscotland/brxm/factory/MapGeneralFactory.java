package com.visitscotland.brxm.factory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.visitscotland.brxm.hippobeans.*;
import com.visitscotland.brxm.model.MapsModule;
import com.visitscotland.brxm.services.MapService;
import com.visitscotland.brxm.utils.HippoUtilsService;
import com.visitscotland.utils.Contract;
import org.hippoecm.hst.content.beans.query.HstQueryResult;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoBeanIterator;
import org.hippoecm.hst.core.component.HstRequest;
import org.onehippo.taxonomy.api.Category;
import org.onehippo.taxonomy.api.Taxonomy;
import org.springframework.stereotype.Component;

import java.util.Locale;


@Component
public class MapGeneralFactory {

    static final String LABEL = "label";
    static final String FEATURE = "feature";
    static final String ID = "id";
    static final String TYPE = "type";

    private final MapService mapService;
    private final HippoUtilsService hippoUtilsService;
    private final ObjectMapper mapper;

    public MapGeneralFactory(MapService mapService, HippoUtilsService hippoUtilsService) {
        this.hippoUtilsService = hippoUtilsService;
        this.mapper = new ObjectMapper();
        this.mapService = mapService;
    }

    /**
     * Method to build a MapsModule to be used by feds with
     *
     * @param request HstRequest request
     * @param mapModuleDocument CMS document
     * @return module to be sent to feds
     */
    public MapsModule getModule(HstRequest request, MapModule mapModuleDocument) {
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
        //TODO check if the map is iCentre then create a new method to handle it
        buildMapGeneralPages(request, mapModuleDocument, module, keys, features);

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
                mapService.addFeaturePlacesNode(module, mapModuleDocument.getCategories(), request.getLocale(), keys, features);
            }
        }
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
                mapService.buildPageNode(locale, getCategoryNode(category, locale), module,((Destination) bean), feature);
            } else {
                mapService.buildStopNode(locale,getCategoryNode(category, locale),module, ((Stop) bean), feature);
            }
        }
        return feature;
    }
}

