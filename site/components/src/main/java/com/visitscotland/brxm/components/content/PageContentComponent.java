package com.visitscotland.brxm.components.content;

import com.visitscotland.brxm.config.VsComponentManager;
import com.visitscotland.brxm.factory.*;
import com.visitscotland.brxm.hippobeans.Page;
import com.visitscotland.brxm.hippobeans.VideoLink;
import com.visitscotland.brxm.model.FlatBlog;
import com.visitscotland.brxm.model.FlatImage;
import com.visitscotland.brxm.model.Module;
import com.visitscotland.brxm.model.SignpostModule;
import com.visitscotland.brxm.model.megalinks.EnhancedLink;
import com.visitscotland.brxm.model.megalinks.HorizontalListLinksModule;
import com.visitscotland.brxm.services.LinkService;
import com.visitscotland.brxm.services.ResourceBundleService;
import com.visitscotland.brxm.utils.ContentLogger;
import com.visitscotland.brxm.utils.HippoUtilsService;
import com.visitscotland.brxm.utils.MetadataFactory;
import com.visitscotland.brxm.utils.Properties;
import com.visitscotland.utils.Contract;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;


public class PageContentComponent<T extends Page> extends ContentComponent {

    private static final Logger logger = LoggerFactory.getLogger(PageContentComponent.class);
    //TODO: Content Logger?
    private final Logger freemarkerLogger = LoggerFactory.getLogger("freemarker");

    public static final String DOCUMENT = "document";
    public static final String OTYML = "otyml";
    public static final String BLOG = "blog";
    public static final String AUTHOR = "author";
    public static final String NEWSLETTER_SIGNPOST = "newsletterSignpost";
    public static final String PREVIEW_ALERTS = "alerts";
    public static final String LABELS = "labels";

    public static final String HERO_IMAGE = "heroImage";
    public static final String HERO_VIDEO = "heroVideo";
    public static final String PSR_WIDGET = "psrWidget";

    public static final String SEARCH_RESULTS = "searchResultsPage";
    public static final String METADATA_MODEL = "metadata";

    private final BlogFactory blogFactory;
    private final MegalinkFactory megalinkFactory;
    private final ImageFactory imageFactory;
    private final LinkService linksService;
    private final SignpostFactory signpostFactory;
    private final ProductSearchWidgetFactory psrFactory;
    private final PreviewModeFactory previewFactory;
    private final HippoUtilsService hippoUtils;
    private final ResourceBundleService bundle;
    private final Properties properties;
    private final Logger contentLogger;

    private final MetadataFactory metadata;

    public PageContentComponent() {
        blogFactory = VsComponentManager.get(BlogFactory.class);
        megalinkFactory = VsComponentManager.get(MegalinkFactory.class);
        imageFactory = VsComponentManager.get(ImageFactory.class);
        signpostFactory = VsComponentManager.get(SignpostFactory.class);
        linksService = VsComponentManager.get(LinkService.class);
        psrFactory = VsComponentManager.get(ProductSearchWidgetFactory.class);
        previewFactory = VsComponentManager.get(PreviewModeFactory.class);
        contentLogger = VsComponentManager.get(ContentLogger.class);
        hippoUtils = VsComponentManager.get(HippoUtilsService.class);
        properties = VsComponentManager.get(Properties.class);
        bundle = VsComponentManager.get(ResourceBundleService.class);
        metadata = VsComponentManager.get(MetadataFactory.class);

    }

    @Override
    public void doBeforeRender(HstRequest request, HstResponse response) {
        super.doBeforeRender(request, response);
        addMetadata(request);

        addHeroImage(request);

        addOTYML(request);
        addNewsletterSignup(request);
        addLogging(request);
        addFlags(request);
        addBlog(request);

        addLabels(request);
        addSiteSpecificConfiguration(request);
    }

    private void addMetadata(HstRequest request){
        request.setModel(METADATA_MODEL, metadata.getMetadata());
    }

    /**
     * Add flags to the freekarker to indicate what type of page is being processed
     */
    private void addFlags(HstRequest request) {
        if (request.getPathInfo().contains(properties.getSiteGlobalSearch())) {
            request.setModel(SEARCH_RESULTS, true);
        }
    }

    private void addLabels(HstRequest request) {
        final String SOCIAL_SHARE_BUNDLE = "social.share";
        final String VIDEO_BUNDLE = "video";
        final String SKIP_TO = "skip-to";
        final String CMS_MESSAGES = "cms-messages";

        Map<String, String> globalLabels = new HashMap<>();

        addGlobalLabel(globalLabels, "close", request.getLocale());
        addGlobalLabel(globalLabels, "cookie.link-message", request.getLocale());
        addGlobalLabel(globalLabels, "third-party-error", request.getLocale());
        addGlobalLabel(globalLabels, "default.alt-text", request.getLocale());
        addGlobalLabel(globalLabels, "image.title", request.getLocale());
        addGlobalLabel(globalLabels, "image.no.credit", request.getLocale());
        addGlobalLabel(globalLabels, "home", request.getLocale());
        addGlobalLabel(globalLabels, "page.next", request.getLocale());
        addGlobalLabel(globalLabels, "page.previous", request.getLocale());

        labels(request).put(ResourceBundleService.GLOBAL_BUNDLE_FILE, globalLabels);
        labels(request).put(SOCIAL_SHARE_BUNDLE, bundle.getAllLabels(SOCIAL_SHARE_BUNDLE, request.getLocale()));
        labels(request).put(VIDEO_BUNDLE, bundle.getAllLabels(VIDEO_BUNDLE, request.getLocale()));
        labels(request).put(SKIP_TO, bundle.getAllLabels(SKIP_TO, request.getLocale()));

        if (isEditMode(request)) {
            labels(request).put(CMS_MESSAGES, bundle.getAllLabels(CMS_MESSAGES, request.getLocale()));
        }
    }

    private void addGlobalLabel(Map<String, String> map, String key, Locale locale) {
        map.put(key, bundle.getResourceBundle(ResourceBundleService.GLOBAL_BUNDLE_FILE, key, locale));
    }

    /**
     * - Alerts are only used for issues related with the hero image at the moment
     * - Hero Image is not necessary for all document types. Is it better to add the field in order to keep consistency?
     */
    private void addHeroImage(HstRequest request) {
        Module<T> introModule = new Module<>();

        FlatImage heroImage = imageFactory.createImage(getDocument(request).getHeroImage(), introModule, request.getLocale());
        if (getDocument(request).getHeroImage() == null) {
            String message = String.format("The image selected for '%s' is not available, please select a valid image for '%s' at: %s ",
                    getDocument(request).getTitle(), getDocument(request).getDisplayName(), getDocument(request).getPath());
            contentLogger.warn(message);
            introModule.addErrorMessage(message);
        }
        request.setModel(HERO_IMAGE, heroImage);

        VideoLink videoDocument = getDocument(request).getHeroVideo();
        if (videoDocument != null && videoDocument.getVideoLink() != null) {
            EnhancedLink video = linksService.createVideo(videoDocument.getVideoLink(), introModule, request.getLocale());
            request.setModel(HERO_VIDEO, video);
        }

        if (!Contract.isEmpty(introModule.getErrorMessages())) {
            setErrorMessages(request, introModule.getErrorMessages());
        }
    }

    /**
     * Set the OTYML module if present
     */
    protected void addOTYML(HstRequest request) {
        final String PAGINATION_BUNDLE = "essentials.pagination";
        final String OTYML_BUNDLE = "otyml";

        Page page = getDocument(request);
        if (page.getOtherThings() != null) {
            HorizontalListLinksModule otyml = megalinkFactory.horizontalListLayout(page.getOtherThings(), request.getLocale());
            if (Contract.isEmpty(otyml.getLinks())) {
                contentLogger.warn("OTYML at {} contains 0 published items. Skipping module", page.getOtherThings().getPath());
                request.setModel(OTYML, previewFactory.createErrorModule(otyml));
                return;
            }
            if (otyml.getLinks().size() < MegalinkFactory.MIN_ITEMS_CAROUSEL) {
                contentLogger.warn("OTYML at {} contains only {} published items. Expected a minimum of 5", page.getOtherThings().getPath(), otyml.getLinks().size());
            }
            request.setModel(OTYML, otyml);
        }

        //TODO: Add itinerary labels for days and transport. (https://github.com/visitscotland/business-events-front-end/issues/74)
        labels(request).put(OTYML_BUNDLE, bundle.getAllLabels(OTYML_BUNDLE, request.getLocale()));
        labels(request).put(PAGINATION_BUNDLE, bundle.getAllLabels(PAGINATION_BUNDLE, request.getLocale()));
    }

    private Map<String, Map<String, String>> labels(HstRequest request) {
        if (request.getModel(LABELS) == null) {
            Map<String, Map<String, String>> labels = new HashMap<>();
            request.setModel(LABELS, labels);
            return labels;
        }

        return request.getModel(LABELS);
    }

    /**
     * Set the blog if present
     */
    protected void addBlog(HstRequest request) {
        Page page = getDocument(request);
        if (page.getBlog() != null) {
            Collection<String> errorMessages = new ArrayList<>();

            FlatBlog blog = blogFactory.getBlog(page.getBlog(), request.getLocale(), errorMessages);

            //TODO: Remove setAttribute when "blog" is removed from Freemarker
            request.setAttribute(BLOG, blog);
            request.setModel(AUTHOR, blog);

            setErrorMessages(request, errorMessages);
        }
    }

    protected void addNewsletterSignup(HstRequest request) {
        Page page = getDocument(request);
        if (Boolean.FALSE.equals(Contract.defaultIfNull(page.getHideNewsletter(), false))) {
            SignpostModule signpost;
            if (hippoUtils.isBusinessEventsSite(request)){
                signpost = signpostFactory.createBusinessEventsModule(request.getLocale());
            } else if (request.getPathInfo().contains(properties.getSiteSkiSection())) {
                signpost = signpostFactory.createSnowAlertsModule(request.getLocale());
            } else {
                signpost = signpostFactory.createNewsletterSignpostModule(request.getLocale());
            }
            if (signpost != null) {
                request.setModel(NEWSLETTER_SIGNPOST, signpost);
            }
        }
    }

    /**
     * Add the configuration related to the Product Search Widget for the page
     */
    private void addProductSearchWidget(HstRequest request) {
        if (!request.getPathInfo().contains(properties.getSiteSkiSection()) && !request.getPathInfo().contains(properties.getCampaignSection())) {
            request.setModel(PSR_WIDGET, psrFactory.getWidget(request));
        }
    }

    public void addLogging(HstRequest request) {
        request.setModel("Logger", freemarkerLogger);
    }

    /**
     * Return the document from the request
     *
     * @param request HstRequest
     * @return the main document of
     */
    protected T getDocument(HstRequest request) {
        if (request.getAttribute(DOCUMENT) instanceof Page) {
            return (T) request.getAttribute(DOCUMENT);
        } else {
            logger.error("The main document is not an instance of Page. Requested URL = {}", request.getRequestURI(), new ClassCastException());
            return null;
        }
    }

    public static void setErrorMessages(HstRequest request, Collection<String> errorMessages) {
        if (request.getModel(PREVIEW_ALERTS) != null) {
            Collection<String> requestMessages = request.getModel(PREVIEW_ALERTS);
            requestMessages.addAll(errorMessages);
        } else {
            request.setModel(PREVIEW_ALERTS, errorMessages);
        }
    }

    private void addSiteSpecificConfiguration(HstRequest request) {
        final String SOCIAL_MEDIA = "navigation.social-media";
        final String STATIC = "navigation.static";
        String prefix = "";

        if (hippoUtils.isBusinessEventsSite(request)) {
            request.setModel(HippoUtilsService.BUSINESS_EVENTS_SITE, true);
            prefix = "be.";
        } else {
            addProductSearchWidget(request);
        }

        labels(request).put(SOCIAL_MEDIA, bundle.getAllLabels(prefix + SOCIAL_MEDIA, request.getLocale()));
        labels(request).put(STATIC, bundle.getAllLabels(prefix + STATIC, request.getLocale()));
    }

    boolean isEditMode(HstRequest request) {
        return Boolean.TRUE.equals(request.getAttribute("editMode"));
    }
}
