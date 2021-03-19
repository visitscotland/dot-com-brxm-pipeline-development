package com.visitscotland.brxm.factory;

import com.fasterxml.jackson.databind.JsonNode;
import com.visitscotland.brxm.hippobeans.*;
import com.visitscotland.brxm.model.Coordinates;
import com.visitscotland.brxm.model.FlatLink;
import com.visitscotland.brxm.model.ListicleModule;
import com.visitscotland.brxm.dms.DMSDataService;
import com.visitscotland.brxm.dms.DMSUtils;
import com.visitscotland.brxm.services.LinkService;
import com.visitscotland.brxm.services.CommonUtilsService;
import com.visitscotland.brxm.services.DocumentUtilsService;
import com.visitscotland.utils.Contract;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.visitscotland.brxm.dms.DMSConstants.DMSProduct.*;

@Component
public class ListicleFactory {

    private static final Logger logger = LoggerFactory.getLogger(ListicleFactory.class);

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
     * @return
     */
    public ListicleModule getListicleItem(Locale locale, ListicleItem listicleItem, Integer index) {

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
            link = linksService.createLink(locale, compound);
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
            String issue = CommonUtilsService.contentIssue("The ListicleItem %s doesn't contain a main product", module.getHippoBean().getPath());
            logger.warn(issue);
            return null;
        } else if (link instanceof DMSLink) {
            processDMSMainProduct(locale, module, (DMSLink) link);
        } else if (link instanceof CMSLink) {
            if (((CMSLink) link).getLink() instanceof Page) {
                if (module.getImage() == null) {
                    Page cmsLink = (Page) ((CMSLink) link).getLink();
                    module.setImage(imageFactory.getImage(cmsLink.getHeroImage(), module, locale));
                }
            } else {
                String issue = CommonUtilsService.contentIssue("The ListicleItem %s is pointing to a document that is not a page ", module.getHippoBean().getPath());
                logger.warn(issue);
                return null;
            }
        }

        return  linksService.createLink(locale, link);
    }

    /**
     * Loads as much information as it can from the DMS Prodcut data:
     *
     * Facilities are loaded from the dmsItem. Subtitle, Image and Coordinates are set only when the listicle item has
     * not defined the values
     */
    private void processDMSMainProduct(Locale locale, ListicleModule item, DMSLink dmsLink) {

        JsonNode product = dmsData.productCard(dmsLink.getProduct(), locale);

        if (product == null) {
            item.addErrorMessage("The product id does not match in the DMS");
            String message = CommonUtilsService.contentIssue("The product id was not provided or the product was not found (id=%s), Listicle = %s - %s",
                    dmsLink.getProduct(), item.getHippoBean(), item.getHippoBean().getTitle());
            logger.warn(message);
        } else {
            if (item.getImage() == null) {
                item.setImage(imageFactory.createImage(product, item));
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
