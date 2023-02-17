package com.visitscotland.brxm.factory;

import com.fasterxml.jackson.databind.JsonNode;
import com.visitscotland.brxm.dms.DMSDataService;
import com.visitscotland.brxm.hippobeans.*;
import com.visitscotland.brxm.model.FlatLink;

import com.visitscotland.brxm.model.LinkType;
import com.visitscotland.brxm.model.SkiListModule;
import com.visitscotland.brxm.model.SkiModule;
import com.visitscotland.brxm.model.megalinks.EnhancedLink;
import com.visitscotland.brxm.services.DocumentUtilsService;
import com.visitscotland.brxm.services.LinkService;
import com.visitscotland.brxm.services.ResourceBundleService;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static com.visitscotland.brxm.dms.DMSConstants.DMSProduct.*;

@Component
public class SkiFactory {

    static final String BUNDLE_FILE = "ski";

    private final DMSDataService dataService;
    private final ResourceBundleService bundle;
    private final DocumentUtilsService documentUtils;
    private final LinkService linkService;

    private final Logger contentLogger;

    public SkiFactory(DMSDataService dataService, ResourceBundleService bundle, DocumentUtilsService documentUtils, Logger contentLogger, LinkService linkService) {
        this.dataService = dataService;
        this.bundle = bundle;
        this.documentUtils = documentUtils;
        this.contentLogger = contentLogger;
        this.linkService = linkService;
    }

    public SkiListModule createSkyListModule(SkiCentreList document, Locale locale){
        SkiListModule module = new SkiListModule();
        module.setHippoBean(document);

        module.setTitle(document.getTitle());
        module.setIntroduction(document.getCopy());
        module.setSkiCentres(new ArrayList<>());

        for (Page page:  document.getMegalinkItems()){
            Optional<EnhancedLink> link = linkService.createEnhancedLink(page, module,locale, false);

            if (!link.isPresent()){
                String message = String.format("Page for the document %s was not found.", page.getDisplayName());
                module.addErrorMessage(message);
                contentLogger.error("{} (Path={})", message, document.getPath());
            }  else {
                List<SkiCentre> skiDocument = documentUtils.getSiblingDocuments(page, SkiCentre.class, "visitscotland:SkiCentre");
                if (skiDocument.size() == 0){

                    String message = String.format("Ski Centre Document wasn't found for %s.", page.getDisplayName());
                    module.addErrorMessage(message);
                    contentLogger.warn("{}. (Path={})", message, document.getPath());
                } else {
                    if (skiDocument.size() > 1) {
                        String message = String.format("Too Many Ski Centre Documents for %s. Only one document should be defined for this type of page.",
                                page.getDisplayName());
                        module.addErrorMessage(message);
                        contentLogger.error("{} (Path={})", message, document.getPath());
                    }
                    SkiModule skiModule = createSkyModule(skiDocument.get(0), locale);
                    //Title is overridden with the title of the page.
                    skiModule.setTitle(page.getTitle());
                    skiModule.setIntroduction(null);
                    skiModule.setCmsPage(link.get());

                    module.getSkiCentres().add(skiModule);
                }
            }
        }
        return module;
    }

    public SkiModule createSkyModule(SkiCentre document, Locale locale){
        SkiModule module = new SkiModule();

        module.setHippoBean(document);
        module.setTitle(document.getTitle());
        module.setIntroduction(document.getCopy());
        module.setFeedURL(document.getFeed());
        module.setPisteMap(document.getPisteMap());

        JsonNode product = dataService.productCard(document.getProductId(), locale);

        if (product == null){
            String errorMessage = String.format("The DMS product associated with this Ski Centre (ID=%s) is not available", document.getProductId());
            contentLogger.error("{}. Path={} ", errorMessage, document.getPath());
            module.addErrorMessage(errorMessage);
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
