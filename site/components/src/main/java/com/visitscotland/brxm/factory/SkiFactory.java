package com.visitscotland.brxm.factory;

import com.fasterxml.jackson.databind.JsonNode;
import com.visitscotland.brxm.dms.DMSDataService;
import com.visitscotland.brxm.hippobeans.SkiCentre;
import com.visitscotland.brxm.hippobeans.SkiCentreList;
import com.visitscotland.brxm.model.FlatLink;

import com.visitscotland.brxm.model.LinkType;
import com.visitscotland.brxm.model.SkiListModule;
import com.visitscotland.brxm.model.SkiModule;
import com.visitscotland.brxm.services.ResourceBundleService;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.visitscotland.brxm.dms.DMSConstants.DMSProduct.*;

@Component
public class SkiFactory {

    static final String BUNDLE_FILE = "ski";

    private final DMSDataService dataService;
    private final ResourceBundleService bundle;
    private final Logger contentLogger;

    public SkiFactory(DMSDataService dataService, ResourceBundleService bundle, Logger contentLogger) {
        this.dataService = dataService;
        this.bundle = bundle;
        this.contentLogger = contentLogger;
    }

    /**
     * TODO:  VS-4376
     */
    public SkiListModule createSkyListModule(SkiCentreList document, Locale locale){
        SkiListModule module = new SkiListModule();

        module.setHippoBean(document);
        //NOT IMPLEMENTED YET
        return null;
    }

    public SkiModule createSkyModule(SkiCentre document, Locale locale){
        SkiModule module = new SkiModule();

        module.setHippoBean(document);
        module.setTitle(document.getTitle());
        module.setIntroduction(document.getCopy());
        module.setFeedURL(document.getFeed());

        JsonNode product = dataService.productCard(document.getProductId(), locale);

        if (product == null){
            contentLogger.error("The DMS product associated with this Ski Centre (ID={}) is not available", document.getProductId());
        } else {
            populateDmsData(module, product, locale);
        }
        return module;
    }


    /**
     * Reads the data from the Json Object to populate the fields in the module.
     *
     * @param module SkiCentre Module
     *
     * @param product JSON Object containing the Product data.
     */
    private void populateDmsData(@NotNull SkiModule module, @NotNull JsonNode product,@NotNull Locale locale){
        List<JsonNode> links = new ArrayList<>();

        if (product.has(ADDRESS)) {
            module.setAddress(product.get(ADDRESS));
        }

        if (product.has(TELEPHONE_NUMBER)) {
            module.setPhone(product.get(TELEPHONE_NUMBER).asText());
        }

        if (product.has(WEBSITE)) {
            module.setWebsite(product.get(WEBSITE));
        }

        if (product.has(OPENING)){
            module.setOpeningLink(new FlatLink(bundle.getResourceBundle(BUNDLE_FILE, "ski-centre.opening-times.label", locale),
                    product.get(URL).get(URL_LINK).asText() + "#opening", LinkType.INTERNAL));
        }

        if (product.has(SOCIAL_CHANNEL)) {
            for (JsonNode channel : product.get(SOCIAL_CHANNEL)) {
                links.add(channel);
            }
        }
        module.setSocialChannels(links);
    }


}
