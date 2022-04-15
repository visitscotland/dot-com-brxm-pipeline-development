package com.visitscotland.brxm.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.visitscotland.brxm.config.VsComponentManager;
import com.visitscotland.brxm.dms.DMSConstants;
import com.visitscotland.brxm.dms.DMSDataService;
import com.visitscotland.brxm.dms.ProductSearchBuilder;
import com.visitscotland.brxm.factory.ImageFactory;
import com.visitscotland.brxm.hippobeans.*;
import com.visitscotland.brxm.hippobeans.capabilities.Linkable;
import com.visitscotland.brxm.model.FlatLink;
import com.visitscotland.brxm.model.LinkType;
import com.visitscotland.brxm.model.Module;
import com.visitscotland.brxm.model.megalinks.EnhancedLink;
import com.visitscotland.brxm.model.YoutubeVideo;
import com.visitscotland.brxm.utils.HippoUtilsService;
import com.visitscotland.brxm.utils.Language;
import com.visitscotland.brxm.utils.Properties;
import com.visitscotland.utils.Contract;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;

@Component
public class LinkService {

    private static final Logger logger = LoggerFactory.getLogger(LinkService.class);
    private static final Logger contentLogger = LoggerFactory.getLogger("content");

    private static final String DMS_PAGE = "(/[a-z0-9\\-._~%!$&'()*+,;=@]*)?(/info/).*";

    private final DMSDataService dmsData;
    private final ResourceBundleService bundle;
    private final HippoUtilsService utils;
    private final Properties properties;
    private final ImageFactory imageFactory;
    private final CommonUtilsService commonUtils;
    private final DocumentUtilsService documentUtilsService;
    private final YoutubeApiService youtubeApiService;

    @Autowired
    public LinkService(DMSDataService dmsData, ResourceBundleService bundle, HippoUtilsService utils, Properties properties, ImageFactory imageFactory, CommonUtilsService commonUtils, DocumentUtilsService documentUtilsService, YoutubeApiService youtubeApiService) {
        this.dmsData = dmsData;
        this.bundle = bundle;
        this.utils = utils;
        this.properties = properties;

        this.imageFactory = imageFactory;
        this.commonUtils = commonUtils;
        this.documentUtilsService = documentUtilsService;
        this.youtubeApiService = youtubeApiService;
    }

    /**
     * Fetches a new Product Search Object
     */
    private ProductSearchBuilder productSearch() {
        return VsComponentManager.get(ProductSearchBuilder.class);
    }

    /**
     * Creates a link from a Compound item of one of the following types:
     * DMSLink, ProductSearchLink, ExternalLink, CMSLink
     *
     * @param locale locale Language for the labels
     * @param item   Compound Item
     */
    public FlatLink createFindOutMoreLink(Module<?> module, Locale locale, HippoBean item) {
        if (item instanceof DMSLink) {
            DMSLink dmsLink = (DMSLink) item;
            JsonNode product = dmsData.productCard(dmsLink.getProduct(), locale);

            if (product == null) {
                contentLogger.warn("There is no product with the id '{}', ({}) ", dmsLink.getProduct(), item.getPath());
                module.addErrorMessage("There is no product with the id " + dmsLink.getProduct());
            } else {
                return createDmsLink(locale, dmsLink, product);
            }
        } else if (item instanceof ProductSearchLink) {
            ProductSearchLink productSearchLink = (ProductSearchLink) item;
            ProductSearchBuilder psb = productSearch().fromHippoBean(productSearchLink.getSearch()).locale(locale);

            return new FlatLink(bundle.getCtaLabel(productSearchLink.getLabel(), locale), psb.build(), LinkType.INTERNAL);
        } else if (item instanceof ExternalLink) {
            return createExternalLink(locale, ((ExternalLink) item).getLink(), bundle.getCtaLabel(((ExternalLink) item).getLabel(), locale));
        } else if (item instanceof CMSLink) {
            return createCMSLink(module, locale, (CMSLink) item);
        }

        logger.warn("The document {} could not be turned into a link", item.getPath());
        module.addErrorMessage("The link was not correctly processed");
        return null;
    }

    /**
     * Creates a FlatLink from a CMSLink Document
     * @param module
     * @param locale
     * @param cmsLink
     * @return
     */
    FlatLink createCMSLink(Module<?> module, Locale locale, CMSLink cmsLink){
        FlatLink link = null;
        if (cmsLink.getLink() instanceof SharedLink) {
            link = createFindOutMoreLink(module, locale, ((SharedLink) cmsLink.getLink()).getLinkType());
            if (!Contract.isEmpty(cmsLink.getLabel()) && link != null && !Contract.isEmpty(link.getLink())) {
                link.setLabel(cmsLink.getLabel());
            }
        } else if (cmsLink.getLink() instanceof Linkable) {
            Linkable linkable = (Linkable) cmsLink.getLink();
            link = createSimpleLink(linkable, module, locale);

            if (link != null && !Contract.isEmpty(link.getLink())) {
                link.setLabel(formatLabel(cmsLink.getLink(), bundle.getCtaLabel(cmsLink.getLabel(), locale), module, locale));
            }
        }

        if (link == null || link.getLink() == null){
            contentLogger.warn("There is an unexpected issue with the link at {}", cmsLink.getPath());
            module.addErrorMessage("There is an unexpected issue with the link at " + cmsLink.getPath());
            return null;
        }

        return link;
    }

    /**
     * Creates a localized FlatLink from a URL.
     *
     * @param url: URl
     */
    public FlatLink createExternalLink(final String url) {
        return createExternalLink(utils.getRequestLocale(), url, null);
    }

    FlatLink createExternalLink(final Locale locale, final String url, final String label) {
        LinkType linkType = getType(url);
        String localizedUrl = processURL(locale, url);

        if (!locale.equals(Locale.UK) && url != null && url.equals(localizedUrl) && linkType == LinkType.INTERNAL && !url.startsWith("#")) {
            logger.warn("The URL {} could not be localized", url);
        }

        return new FlatLink(label, localizedUrl, linkType);
    }

    private String processURL(Locale locale, String url) {
        if (url == null) {
            return null;
        } else if (url.startsWith("/")) {
            return localize(locale, "", url);
        }

        if (!Contract.isEmpty(properties.getInternalSites())) {
            try {
                URL urlObject = new URL(url);

                for (String host : properties.getInternalSites()) {
                    if (urlObject.getHost().equals(host)) {
                        String site = host.equals(properties.getConvertToRelative()) ? "" : url.substring(0, url.lastIndexOf(urlObject.getFile()));
                        return localize(locale, site, urlObject.getFile());
                    }
                }
            } catch (IllegalArgumentException | MalformedURLException e) {
                logger.error("The URL {} cannot be parsed for localization", url);
            }
        }

        return url;
    }

    /**
     * TODO Refactor this method when DMS language URLs are the same as CMS language URLs
     */
    private String localize(Locale locale, String site, String path) {
        boolean isDms = path.matches(DMS_PAGE);
        String languagePath = isDms ?
                Language.getLanguageForLocale(locale).getDMSPathVariable() : Language.getLanguageForLocale(locale).getCMSPathVariable();


        if (path.startsWith(languagePath)) {
            return site + path;
        } else if (isDms && path.startsWith(Language.getLanguageForLocale(locale).getCMSPathVariable())) {
            return site + languagePath + path.substring(path.indexOf("/", 1));
        } else {
            return site + languagePath + path;
        }
    }

    public FlatLink createDmsLink(Locale locale, DMSLink dmsLink, JsonNode dmsProductJson) {
        return new FlatLink(bundle.getCtaLabel(dmsLink.getLabel(), locale), getPlainLink(locale, dmsLink, dmsProductJson), LinkType.INTERNAL);
    }

    /**
     * Creates a standard link from a Shared Link
     *
     * @param locale Locale
     * @param link   SharedLink Object;
     * @return
     */
    public String getPlainLink(Module module, Locale locale, SharedLink link) {
        return getPlainLink(locale, link.getLinkType(), getNodeFromSharedLink(link, module,locale));
    }

    /**
     * Creates a standard link from a Shared Link
     *
     * @param locale  Locale
     * @param link    SharedLink Object;
     * @param product JsonNode with the data of the product. It is only used when the type of SharedLink is DMSLink.
     * @return String URL from the SharedLink
     */
    public String getPlainLink(Locale locale, HippoBean link, JsonNode product) {
        String url = null;

        if (link instanceof DMSLink) {
            if (product == null) {//((DMSLink) link).getDmsData(locale)
                contentLogger.warn("The product id '{}' does not exist but is linked - {}", ((DMSLink) link).getProduct(), link.getPath());
            } else {
                url = properties.getDmsHost() + product.get(DMSConstants.DMSProduct.URL).get(DMSConstants.DMSProduct.URL_LINK).asText();
            }
        } else if (link instanceof ExternalLink) {
            url = ((ExternalLink) link).getLink();
        } else if (link instanceof ProductsSearch) {
            url = productSearch().fromHippoBean(((ProductsSearch) link)).locale(locale).build();
        } else if (link instanceof ProductSearchLink) {
            url = productSearch().fromHippoBean(((ProductSearchLink) link).getSearch()).locale(locale).build();
        } else if (link instanceof ExternalDocument) {
            url = ((ExternalDocument) link).getLink();
        } else if (link instanceof Video) {
            url = ((Video) link).getUrl();
        } else {
            String linkType = link == null ? "null" : link.getClass().getSimpleName();
            logger.warn("This class {} is not recognized as a link type and cannot be converted", linkType);
        }
        return processURL(locale, url);
    }

    /**
     * Analyzes the URL and identifies what type of link it is.
     *
     * @param url URL to analyze
     * @return linkType
     */
    public LinkType getType(String url) {
        if (Contract.isEmpty(url)) {
            return null;
        } else if (url.toLowerCase().endsWith(".pdf")) {
            return LinkType.DOWNLOAD;
        } else if (url.startsWith("/") || url.startsWith("#")) {
            return LinkType.INTERNAL;
        } else if (isInternalDomain(url)) {
            return LinkType.INTERNAL;
        }

        return LinkType.EXTERNAL;
    }

    /**
     * Check if the host of the URL is marked as an internal URL
     * <p>
     * Note: Malformed URLs will be treated as external URLs
     *
     * @param url
     * @return
     */
    private boolean isInternalDomain(String url) {
        try {
            String host = new URL(url).getHost();
            return ((!Contract.isEmpty(properties.getInternalSites()) && properties.getInternalSites().contains(host)) ||
                    (!Contract.isEmpty(properties.getDmsHost()) && host.equals(properties.getDmsHost())));
        } catch (MalformedURLException e) {
            logger.info("Malformed URL detected {}", url);
        }
        return false;
    }

    /**
     * TODO : Reduce Cognitive Complexity
     * Method to assign the right category based on the url/cms structure
     *
     * @param path   String path of the document
     * @param locale Locale
     * @return category
     */
    public String getLinkCategory(String path, Locale locale) {
        String navigationCategory = "navigation.categories";
        try {
            if (getType(path) == LinkType.EXTERNAL) {
                java.net.URL url = new URL(path);
                String host = url.getHost();
                String category = host.toUpperCase().startsWith("WWW.") ? host.substring(4) : host;
                return category.toUpperCase();
            } else if (path.contains("ebooks.visitscotland.com")) {
                return bundle.getResourceBundle(navigationCategory, "ebooks", locale);
            } else if (path.contains("blog")) {
                return bundle.getResourceBundle(navigationCategory, "travel-blog", locale);
            } else if (path.contains("see-do") || path.contains("events") || path.contains("tours") || path.contains("things-to-do")) {
                return bundle.getResourceBundle(navigationCategory, "see-do", locale);
            } else if (path.contains("accommodation")|| path.contains("places-to-stay") ) {
                return bundle.getResourceBundle(navigationCategory, "accommodation", locale);
            } else if (path.contains("destination") || path.contains("towns-villages") || path.contains("places-to-go")) {
                return bundle.getResourceBundle(navigationCategory, "destinations-map", locale);
            } else if (path.contains("travel") || path.contains("holidays") || path.contains("transport")) {
                return bundle.getResourceBundle(navigationCategory, "travel-planning", locale);
            } else if (path.contains("brochures")|| path.contains("inspiration")) {
                return bundle.getResourceBundle(navigationCategory, "inspiration", locale);
            } else if (path.contains("about") || path.contains("contact") || path.contains("policies") || path.contains("services")) {
                return bundle.getResourceBundle(navigationCategory, "footer.visitor-information", locale);
            }

            return bundle.getResourceBundle(navigationCategory, "see-do", locale);

        } catch (MalformedURLException e) {
            logger.error("The URL " + path + " is not valid", e);
            return null;
        }
    }

    /**
     * Creates an enhanced link form a {@code Linkable} object
     *
     * @param linkable    Page or Shared link that contains the information about the link
     * @param module      Module to
     * @param locale
     * @param addCategory
     * @return
     */
    public Optional<EnhancedLink> createEnhancedLink(Linkable linkable, Module<?> module, Locale locale, boolean addCategory) {
        EnhancedLink link = null;

        if (linkable instanceof Page) {
            link = enhancedLinkFromPage((Page) linkable, module, locale);
        } else if (linkable instanceof SharedLink) {
            link = enhancedLinkFromSharedLink((SharedLink) linkable, module, locale, addCategory);
        } else if (linkable instanceof Video) {
            link = enhancedLinkFromVideo((Video) linkable, module, locale, addCategory);
        } else if (linkable != null) {
            logger.warn("The type {} was not expected and will be skipped", linkable.getClass().getSimpleName());
        }

        if (link == null || link.getLink() == null){
            return Optional.empty();
        }

        if (addCategory && link.getLink() != null && link.getCategory() == null) {
            link.setCategory(getLinkCategory(link.getLink(), locale));
        }

        if (link.getImage() == null) {
            if (module != null) {
                module.addErrorMessage("The link to '" + link.getLink() + "' does not contain an image.");
            } else {
                logger.error("The error message cannot be displayed in preview");
            }
            contentLogger.warn("The link to {} does not have an image but it is expecting one", ((BaseDocument) linkable).getPath());
        }

        return Optional.of(link);
    }

    /**
     * Creates a FlatLink from a Page or a Shared Document
     *
     * @param linkable
     * @param module
     * @param locale
     * @return
     */
    public FlatLink createSimpleLink(@NotNull Linkable linkable, Module<?> module, Locale locale) {
        FlatLink link = new FlatLink();
        link.setLabel(linkable.getTitle());

        if (linkable instanceof Page) {
            link.setLink(utils.createUrl((Page) linkable));
            link.setType(LinkType.INTERNAL);
        } else if (linkable instanceof SharedLink) {
            SharedLink sharedLink = (SharedLink) linkable;
            link.setLink(getPlainLink(module, locale, sharedLink));
            link.setType(getType(link.getLink()));
        } else if (module != null) {
            module.addErrorMessage(String.format("The type %s cannot be converted into a link", linkable.getClass().getSimpleName()));
            logger.warn("The type {} was not expected and will be skipped", linkable.getClass().getSimpleName());
            return null;
        }
        return link;
    }

    /**
     * Query the DMSDataService and extract the information about the product as a {@code JsonNode}
     *
     * @param sharedLink   SharedLink where the DMS product (ID) is defined
     * @param locale User language to consume DMS texts such a category, location, facilities...
     * @return JSON with DMS product information to create the card or null if the product does not exist
     */
    private JsonNode getNodeFromSharedLink(SharedLink sharedLink, Module<?> module, Locale locale) {
        JsonNode product = null;

        if (sharedLink.getLinkType() instanceof DMSLink) {
            product = dmsData.productCard(((DMSLink) sharedLink.getLinkType()).getProduct(), locale);
            if (product == null){
                contentLogger.warn("The DMS ID for '{}' is not valid. Please review at {}", sharedLink.getTitle(), sharedLink.getPath());
                module.addErrorMessage(String.format("The DMS ID for '%s' is not valid. Please review at %s", sharedLink.getTitle(), sharedLink.getPath()));
            }
        }
        return product;
    }

    /**
     * Populated the information about an enhanced Link from a Page Document.
     *
     * @param page   SharedLink document that contains extra information
     * @param module Module to feed with any possible issue found while creating the page.
     * @param locale Language for the label
     */
    private EnhancedLink enhancedLinkFromPage(Page page, Module<?> module, Locale locale) {
        EnhancedLink link = new EnhancedLink();
        link.setTeaser(page.getTeaser());
        link.setLabel(page.getTitle());
        link.setLink(utils.createUrl(page));
        link.setType(LinkType.INTERNAL);

        link.setImage(imageFactory.createImage(page.getImage(), module, locale));

        if (page instanceof Itinerary) {
            Itinerary itinerary = (Itinerary) page;
            link.setItineraryDays(documentUtilsService.getSiblingDocuments(page, Day.class, "visitscotland:Day").size());
            if (itinerary.getTransports().length > 0) {
                link.setItineraryTransport(itinerary.getTransports()[0]);
            }
        }

        return link;
    }

    /**
     * Populates the information about an enhanced Link from a SharedLink Document.
     *
     * @param sharedLink  SharedLink document that contains extra information
     * @param module      Module to feed with any possible issue found while creating the page.
     * @param locale      Language for the label
     * @param addCategory wether or not the category field is populated.
     */
    private EnhancedLink enhancedLinkFromSharedLink(SharedLink sharedLink, Module<?> module, Locale locale, boolean addCategory) {
        EnhancedLink link = new EnhancedLink();
        JsonNode product = getNodeFromSharedLink(sharedLink, module, locale);

        link.setTeaser(sharedLink.getTeaser());
        link.setLink(getPlainLink(locale, sharedLink.getLinkType(), product));

        if (link.getLink() == null){
            return null;
        }

        if (sharedLink.getImage() != null) {
            link.setImage(imageFactory.createImage(sharedLink.getImage(), module, locale));
        } else if (product != null && product.has(DMSConstants.DMSProduct.IMAGE)) {
            link.setImage(imageFactory.createImage(product, module, locale));
        }

        if (sharedLink.getLinkType() instanceof ExternalDocument) {
            link.setLabel(formatLabel(sharedLink, sharedLink.getTitle(), module, locale));
            link.setType(LinkType.DOWNLOAD);

            if (addCategory) {
                link.setCategory(((ExternalDocument) sharedLink.getLinkType()).getCategory());
            }
        } else {
            link.setLabel(sharedLink.getTitle());
            link.setType(getType(link.getLink()));
            if (sharedLink.getLinkType() instanceof DMSLink){
                link.setCta(bundle.getCtaLabel(((DMSLink)sharedLink.getLinkType()).getLabel(), locale));
            }
        }

        return link;
    }

    /**
     * Creates a Video Link from a Video document.
     *
     * @param video       Video document
     * @param module      Module to feed with any possible issue found while creating the page.
     * @param locale      Language for the label
     * @param addCategory Indicates whether the category field is populated
     */
    private EnhancedLink enhancedLinkFromVideo(Video video, Module<?> module, Locale locale, boolean addCategory) {
        EnhancedLink link = createVideo(video, module, locale);

        //TODO: Confirm requirements for Videos in HorizontalLinks VS-2086 indicates that no category is needed but we need to wait for the final designs before taking action.
        if (addCategory) {
            link.setCategory("Video");
            module.addErrorMessage("This module should not contain a Video Link");
        }

        return link;
    }

    /**
     * Formats label and includes additional information when needed
     *
     * @param linkable
     * @param locale   Language for the label
     * @param module   Module to feed with any possible issue found while creating the page.
     * @return Formatted label
     */
    public String formatLabel(HippoBean linkable, String label, Module<?> module, Locale locale) {
        if (linkable instanceof SharedLink && ((SharedLink) linkable).getLinkType() instanceof ExternalDocument) {
            return label + getDownloadText(((ExternalDocument) ((SharedLink) linkable).getLinkType()).getLink(), locale, module);
        } else {
            return label;
        }
    }

    public String getDownloadText(String link) {
        return getDownloadText(link, utils.getRequestLocale(), null);
    }


    public String getDownloadText(String link, Locale locale, Module<?> module) {
        String size = commonUtils.getExternalDocumentSize(link, locale);
        if (size == null) {
            if (module != null) {
                module.addErrorMessage("The Link to the External document might be broken");
            }
            contentLogger.warn("The external document {} might be broken.", link);
            return "";
        } else {
            return " | " + size;
        }
    }

    /**
     * Creates a PlainVideoLink Object from a videoLink
     *
     * @param video  Video Document
     * @param module Module that will log all issues for the modules.
     * @param locale Locale for the localization
     * @return
     */
    public EnhancedLink createVideo(Video video, Module<?> module, Locale locale) {
        EnhancedLink videoLink = new EnhancedLink();
        videoLink.setImage(imageFactory.createImage(video.getImage(), module, locale));
        videoLink.setLabel(video.getTitle());
        videoLink.setTeaser(video.getTeaser());
        videoLink.setLink(video.getUrl());
        videoLink.setCta(bundle.getVideoCtaLabel(video.getLabel(), locale));
        videoLink.setYoutubeId(getYoutubeId(video.getUrl()));
        videoLink.setType(LinkType.VIDEO);
        Optional<YoutubeVideo> videoInfo = youtubeApiService.getVideoInfo(videoLink.getYoutubeId());
        // If the upload date can not be obtained from YouTube, set the published date to today to prevent a malformed
        // schema
        if (videoInfo.isPresent()) {
            videoLink.setPublishedDate(videoInfo.get().getPublishDate());
        } else {
            videoLink.setPublishedDate(new Date());
        }
        return videoLink;
    }

    private String getYoutubeId(String url) {
        return UriComponentsBuilder.fromUriString(url).build().getQueryParams().getFirst("v");
    }

}
