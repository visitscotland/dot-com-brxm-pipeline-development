package com.visitscotland.brxm.factory;

import com.fasterxml.jackson.databind.JsonNode;
import com.visitscotland.brxm.hippobeans.*;
import com.visitscotland.brxm.hippobeans.capabilities.Linkable;
import com.visitscotland.brxm.model.Coordinates;
import com.visitscotland.brxm.model.FlatLink;
import com.visitscotland.brxm.model.ListicleModule;
import com.visitscotland.brxm.dms.DMSDataService;
import com.visitscotland.brxm.dms.DMSUtils;
import com.visitscotland.brxm.model.megalinks.EnhancedLink;
import com.visitscotland.brxm.services.LinkService;
import com.visitscotland.brxm.services.DocumentUtilsService;
import com.visitscotland.utils.Contract;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static com.visitscotland.brxm.dms.DMSConstants.DMSProduct.*;

@Component
public class ListicleFactory {

    private static final Logger logger = LoggerFactory.getLogger(ListicleFactory.class);
    private static final Logger contentLogger = LoggerFactory.getLogger("content");

    private final LinkService linksService;
    private final DMSDataService dmsData;
    private final ImageFactory imageFactory;
    private final DMSUtils dmsUtils;
    private final DocumentUtilsService documentUtils;

    public ListicleFactory(LinkService linksService, DMSDataService dmsData, ImageFactory imageFactory, DMSUtils dmsUtils, DocumentUtilsService documentUtils) {
        this.linksService = linksService;
        this.dmsData = dmsData;
        this.imageFactory = imageFactory;
        this.dmsUtils = dmsUtils;
        this.documentUtils = documentUtils;
    }

    /**
     * Build a listicleModule from a ListicleItem document
     *
     * @param locale Set the language for the labels
     * @param listicleItem CMS document with the data
     * @param index Index of the item
     * @return Listicle Module created
     */
    public ListicleModule getListicleItem(Locale locale, ListicleItem listicleItem, Integer index) {
        logger.info("Creating ListicleItem module for {}", listicleItem.getPath());

        List<FlatLink> links = new ArrayList<>();
        FlatLink link;

        ListicleModule module = new ListicleModule();
        module.setIndex(index);
        module.setHippoBean(listicleItem);
        module.setTitle(listicleItem.getTitle());
        module.setDescription(listicleItem.getDescription());
        module.setSubtitle(listicleItem.getSubtitle());

        //Set the image
        if (listicleItem.getListicleItemImage() != null) {
            module.setImage(imageFactory.getImage(listicleItem.getListicleItemImage(), module, locale));
        }

        //Set the main product
        link = processMainProduct(locale, listicleItem.getListicleItem(), module);

        if (link != null) {
            links.add(link);
        }

        //Set Extra Links
        //Original designs used to had more that one link, so the logic is prepared to be opened to several links
        for (HippoCompound compound : listicleItem.getExtraLinks()) {
            if (compound instanceof CMSLink) {
                CMSLink cmsLink = (CMSLink) compound;
                link = linksService.createSimpleLink((Linkable) cmsLink.getLink(),module, locale);
                if(!Contract.isEmpty(cmsLink.getLabel())){
                    link.setLabel(cmsLink.getLabel());
                }
            }else{
                link = linksService.createFindOutMoreLink(module, locale, compound);
            }


        if (link != null) {
            links.add(link);
        }
    }

        if (Contract.isEmpty(module.getSubtitle()) && module.getImage() != null) {
            module.setSubtitle(module.getImage().getLocation());
        }

        module.setLinks(links);

        return module;
    }

    /**
     * Process the main product of the item extracting valuable information that will enhance the
     * module.
     */
    private FlatLink processMainProduct(Locale locale, HippoCompound link, ListicleModule module){
        if (link == null) {
            contentLogger.warn("The ListicleItem {} doesn't contain a main product", module.getHippoBean().getPath());
        } else if (link instanceof DMSLink) {
            DMSLink dmsLink = (DMSLink) link;
            JsonNode product = dmsData.productCard(dmsLink.getProduct(), locale);
            if (product == null) {
                String message = String.format("The DMS product added to '%s' was not found, please review the DMS Product id field in the document %s at: %s ", module.getTitle(), dmsLink.getDisplayName(), dmsLink.getPath());
                contentLogger.warn(message);
                module.addErrorMessage(message);
            }else {
                processDMSMainProduct(locale, module, dmsLink, product);
                return linksService.createDmsLink(locale, dmsLink, product);
            }
        } else if (link instanceof CMSLink) {
            CMSLink cmsLink = (CMSLink) link;
            Optional<EnhancedLink> optionalLink = linksService.createEnhancedLink((Linkable) cmsLink.getLink(), module, locale,false);
            if (!optionalLink.isPresent()) {
                String linkPath = cmsLink.getLink() == null ? "" : cmsLink.getLink().getPath();
                contentLogger.error("Failed to add main product link to listicle item {} - check link is valid and published", linkPath);
                return null;
            }
            EnhancedLink eLink = optionalLink.get();
            //Override default link label when the module has an override text
            if (!Contract.isEmpty(cmsLink.getLabel())){
                eLink.setCta(linksService.formatLabel(cmsLink.getLink(), cmsLink.getLabel(), module, locale));
                eLink.setLabel(eLink.getCta());
            }

            if (module.getImage() == null) {
                module.setImage(eLink.getImage());
            }
            if (eLink.getLink() == null){
                contentLogger.warn("There is no product with the id '{}', ({}) ", cmsLink.getLink(), cmsLink.getLink().getPath());
                module.addErrorMessage("Main Link: The DMS id is not valid, please review the document at: " + cmsLink.getLink().getPath());
                return null;
            }
            return  eLink;
        }  else if (link instanceof ExternalLink || link instanceof ProductSearchLink ) {
                return linksService.createFindOutMoreLink(module, locale,link);
        } else {
            contentLogger.warn("The ListicleItem {} is pointing to a document that is not a page ", module.getHippoBean().getPath());
        }

        return null;
    }

    /**
     * Loads as much information as it can from the DMS Product data:
     *
     * Facilities are loaded from the dmsItem. Subtitle, Image and Coordinates are set only when the listicle item has
     * not defined the values
     */
    private void processDMSMainProduct(Locale locale, ListicleModule item, DMSLink dmsLink, JsonNode product) {
        if (product == null) {
            String message = String.format("The DMS product added to '%s' was not found, please review the DMS Product id field in the document %s at: %s ", item.getTitle(), dmsLink.getDisplayName(), dmsLink.getPath());
            item.addErrorMessage(message);
            contentLogger.warn(message);
        } else {
            if (item.getImage() == null) {
                item.setImage(imageFactory.createImage(product, item, locale));
            } else if (item.getImage().getCoordinates() == null && product.has(LATITUDE)) {
                Coordinates coordinates = new Coordinates(product.get(LATITUDE).asDouble(), product.get(LONGITUDE).asDouble());
                item.getImage().setCoordinates(coordinates);
            }

            item.setFacilities(dmsUtils.getKeyFacilities(product));
        }
    }

    /**
     * Process a Listicle and generate a list of listicle modules
     *
     * @param locale Set the language for the labels
     * @param listicle Page document
     */
    public List<ListicleModule> generateItems(Locale locale, Listicle listicle) {
        final List<ListicleItem> listicleItems = documentUtils.getAllowedDocuments(listicle, ListicleItem.class);
        final List<ListicleModule> items = new ArrayList<>();

        boolean descOrder = Boolean.TRUE.equals(listicle.getDescOrder());
        int index = descOrder ? listicleItems.size() : 1;

        for (ListicleItem listicleItem : listicleItems) {
            Integer itemNumber = descOrder ? index-- : index++;
            items.add(getListicleItem(locale, listicleItem, itemNumber));
        }
        return items;
    }
}