package com.visitscotland.brxm.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.visitscotland.brxm.hippobeans.*;
import com.visitscotland.brxm.model.FlatLink;
import com.visitscotland.brxm.model.LinkType;
import com.visitscotland.brxm.config.VsComponentManager;
import com.visitscotland.brxm.dms.DMSDataService;
import com.visitscotland.brxm.dms.ProductSearchBuilder;
import com.visitscotland.brxm.utils.HippoUtilsService;
import com.visitscotland.brxm.utils.Properties;
import com.visitscotland.utils.Contract;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

@Component
public class LinkService {

    private static final Logger logger = LoggerFactory.getLogger(LinkService.class);

    public static final String URL = "url";

    private final DMSDataService dmsData;
    private final ResourceBundleService resourceBundle;
    private final HippoUtilsService utils;
    private final Properties properties;

    public LinkService(DMSDataService dmsData, ResourceBundleService resourceBundle, HippoUtilsService utils, Properties properties) {
        this.dmsData = dmsData;
        this.resourceBundle = resourceBundle;
        this.utils = utils;
        this.properties = properties;
    }

    private ProductSearchBuilder productSearch(){
        return VsComponentManager.get(ProductSearchBuilder.class);
    }

    /**
     * Creates a link from a Compound item of one of the following types:
     * DMSLink, ProductSearchLink, ExternalLink, CMSLink
     *
     * @param locale locale Language for the labels
     * @param item   Compound Item
     */
    public FlatLink createLink(Locale locale, HippoCompound item) {

        if (item instanceof DMSLink) {
            DMSLink dmsLink = (DMSLink) item;
            JsonNode product = dmsData.productCard(dmsLink.getProduct(), locale);

            if (dmsLink.getProduct() == null) {
                String message = CommonUtilsService.contentIssue("There is no product with the id '%s', (%s) ",
                        dmsLink.getProduct(), item.getPath());
                logger.warn(message);
            } else if (product != null) {
                //TODO build the link for the DMS product properly
                return new FlatLink(resourceBundle.getCtaLabel(dmsLink.getLabel(), locale), properties.getDmsHost() + product.get(URL).asText(), LinkType.INTERNAL);
            }
        } else if (item instanceof ProductSearchLink) {
            ProductSearchLink productSearchLink = (ProductSearchLink) item;
            ProductSearchBuilder psb = productSearch().fromHippoBean(productSearchLink.getSearch()).locale(locale);

            return new FlatLink(resourceBundle.getCtaLabel(productSearchLink.getLabel(), locale), psb.build(), LinkType.INTERNAL);

        } else if (item instanceof ExternalLink) {
            ExternalLink externalLink = (ExternalLink) item;
            LinkType linkType = getType(externalLink.getLink());
            return new FlatLink(resourceBundle.getCtaLabel(externalLink.getLabel(), locale), externalLink.getLink(), linkType);

        } else if (item instanceof CMSLink) {
            CMSLink cmsLink = (CMSLink) item;
            return new FlatLink(resourceBundle.getCtaLabel(cmsLink.getLabel(), locale), utils.createUrl(cmsLink.getLink()), LinkType.INTERNAL);
        } else {
            String message = CommonUtilsService.contentIssue("The document %s could not be turned into a link'  ", item.getPath());
            logger.warn(message);
        }

        return null;
    }

    /**
     * Extracts the information about the link form a SharedLink and returns it in a URL.
     *
     * @param link    SharedLink Object;
     * @param product JsonNode with the data of the product. It is only used when the type of SharedLink is DMSLink.
     * @return String URL from the SharedLink
     */
    public String getPlainLink(SharedLink link, JsonNode product) {

        if (link.getLinkType() instanceof DMSLink) {
            if (product == null) {//((DMSLink) link).getDmsData(locale)
                CommonUtilsService.contentIssue("The product id '%s' does not exist but is linked ",
                        ((DMSLink) link.getLinkType()).getProduct(), link.getPath());
            } else {
                return properties.getDmsHost() + product.get(URL).asText();
            }
        } else if (link.getLinkType() instanceof ExternalLink) {
            return ((ExternalLink) link.getLinkType()).getLink();
        } else if (link.getLinkType() instanceof ProductsSearch) {
            return productSearch().fromHippoBean(((ProductsSearch) link.getLinkType())).build();
        } else if (link.getLinkType() instanceof ProductSearchLink) {
            return productSearch().fromHippoBean(((ProductSearchLink) link.getLinkType()).getSearch()).build();
        } else if (link.getLinkType() instanceof ExternalDocument) {
            return ((ExternalDocument) link.getLinkType()).getLink();
        } else {
            String linkType = link.getLinkType() == null ? "null" : link.getLinkType().getClass().getSimpleName();
            logger.warn("This class {} is not recognized as a link type and cannot be converted", linkType);
        }
        return null;
    }

    /**
     * Analyzes the URL and identifies what type of link it is.
     *
     * @param url URL to analyze
     * @return
     */
    public LinkType getType(String url) {
        //TODO the following if block requires some refinement
        if (Contract.isEmpty(url)) {
            return null;
        } else if (url.startsWith("/") || url.contains("localhost") || url.contains("visitscotland.com")
                || url.startsWith(properties.getDmsHost())) {
            return LinkType.INTERNAL;
        }

        return LinkType.EXTERNAL;
    }

    /**
     * Method to assign the right category based on the url/cms structure
     *
     * @param path   String path of the document
     * @param locale Locale
     * @return category
     */
    public String getLinkCategory(String path, Locale locale) {
        try {
            if (getType(path) == LinkType.EXTERNAL) {
                java.net.URL url = new URL(path);
                String host = url.getHost();
                String category = host.toUpperCase().startsWith("WWW.") ? host.substring(4) : host;
                return category.toUpperCase();
            } else if (path.contains("ebooks.visitscotland.com")) {
                return "eBooks";
            } else if (path.contains("blog")) {
                return resourceBundle.getResourceBundle("navigation.main", "blog", locale, true);
            } else if (path.contains("see-do") || path.contains("events") || path.contains("tours")) {
                return resourceBundle.getResourceBundle("navigation.main", "see-do", locale, true);
            } else if (path.contains("accommodation")) {
                return resourceBundle.getResourceBundle("navigation.main", "accommodation", locale, true);
            } else if (path.contains("destination") || path.contains("towns-villages")) {
                return resourceBundle.getResourceBundle("navigation.main", "destinations-map", locale, true);
            } else if (path.contains("travel") || path.contains("holidays") || path.contains("transport")) {
                return resourceBundle.getResourceBundle("navigation.main", "travel-planning", locale, true);
            } else if (path.contains("brochures")) {
                return resourceBundle.getResourceBundle("navigation.main", "inspiration", locale, true);
            } else if (path.contains("about") || path.contains("contact") || path.contains("policies") || path.contains("services")) {
                return resourceBundle.getResourceBundle("navigation.footer", "footer.visitor-information", locale, true);
            }

            return resourceBundle.getResourceBundle("navigation.main", "see-do", locale, true);

        } catch (MalformedURLException e) {
            logger.error("The URL " + path + " is not valid", e);
            return null;
        }

    }
}
