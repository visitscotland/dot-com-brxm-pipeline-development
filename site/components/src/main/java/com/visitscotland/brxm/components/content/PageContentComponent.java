package com.visitscotland.brxm.components.content;

import com.visitscotland.brxm.config.VsComponentManager;
import com.visitscotland.brxm.factory.ImageFactory;
import com.visitscotland.brxm.factory.MegalinkFactory;
import com.visitscotland.brxm.factory.ProductSearchWidgetFactory;
import com.visitscotland.brxm.factory.PreviewModeFactory;
import com.visitscotland.brxm.factory.SignpostFactory;
import com.visitscotland.brxm.hippobeans.Page;
import com.visitscotland.brxm.hippobeans.VideoLink;
import com.visitscotland.brxm.model.FlatImage;
import com.visitscotland.brxm.model.Module;
import com.visitscotland.brxm.model.SignpostModule;
import com.visitscotland.brxm.model.megalinks.EnhancedLink;
import com.visitscotland.brxm.model.megalinks.HorizontalListLinksModule;
import com.visitscotland.brxm.services.LinkService;
import com.visitscotland.brxm.utils.ContentLogger;
import com.visitscotland.brxm.utils.Properties;
import com.visitscotland.utils.Contract;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;


public class PageContentComponent<T extends Page> extends ContentComponent {

    private static final Logger logger = LoggerFactory.getLogger(PageContentComponent.class);
    //TODO: Content Logger?
    private final Logger freemarkerLogger = LoggerFactory.getLogger("freemarker");

    public static final String DOCUMENT = "document";
    public static final String OTYML = "otyml";
    public static final String NEWSLETTER_SIGNPOST = "newsletterSignpost";
    public static final String PREVIEW_ALERTS = "alerts";
    public static final String HERO_IMAGE = "heroImage";
    public static final String HERO_VIDEO = "heroVideo";
    public static final String PSR_WIDGET = "psrWidget";

    public static final String SEARCH_RESULTS = "searchResultsPage";

    private final MegalinkFactory megalinkFactory;
    private final ImageFactory imageFactory;
    private final LinkService linksService;
    private final SignpostFactory signpostFactory;
    private final ProductSearchWidgetFactory psrFactory;
    private final PreviewModeFactory previewFactory;
    private final Properties properties;
    private final Logger contentLogger;

    public PageContentComponent() {
        megalinkFactory = VsComponentManager.get(MegalinkFactory.class);
        imageFactory = VsComponentManager.get(ImageFactory.class);
        signpostFactory = VsComponentManager.get(SignpostFactory.class);
        linksService = VsComponentManager.get(LinkService.class);
        psrFactory = VsComponentManager.get(ProductSearchWidgetFactory.class);
        previewFactory = VsComponentManager.get(PreviewModeFactory.class);
        contentLogger = VsComponentManager.get(ContentLogger.class);
        properties = VsComponentManager.get(Properties.class);
    }

    @Override
    public void doBeforeRender(HstRequest request, HstResponse response) {
        super.doBeforeRender(request, response);

        addHeroImage(request);

        addOTYML(request);
        addNewsletterSignup(request);
        addProductSearchWidget(request);
        addLogging(request);
        addFlags(request);
    }

    /**
     * Add flags to the freekarker to indicate what type of page is being processed
     * @param request
     */
    private void addFlags(HstRequest request){
        if (request.getPathInfo().contains(properties.getSiteGlobalSearch())){
            request.setAttribute(SEARCH_RESULTS, true);
        }
    }

    /**
     * - Alerts are only used for issues related with the hero image at the moment
     * - Hero Image is not necessary for all document types. Is it better to add the field in order to keep consistency?
     */
    private void addHeroImage(HstRequest request){
        Module<T> introModule = new Module<>();

        FlatImage heroImage = imageFactory.createImage(getDocument(request).getHeroImage(), introModule, request.getLocale());
        if (getDocument(request).getHeroImage() == null){
            String message = String.format("The image selected for '%s' is not available, please select a valid image for '%s' at: %s ",
                    getDocument(request).getTitle(), getDocument(request).getDisplayName(),getDocument(request).getPath());
            contentLogger.warn(message);
            introModule.addErrorMessage(message);
        }
        request.setAttribute(HERO_IMAGE, heroImage);

        VideoLink videoDocument = getDocument(request).getHeroVideo();
        if (videoDocument != null && videoDocument.getVideoLink() != null) {
            EnhancedLink video = linksService.createVideo(videoDocument.getVideoLink(), introModule, request.getLocale());
            request.setAttribute(HERO_VIDEO, video);
        }

        if (!Contract.isEmpty(introModule.getErrorMessages())) {
            setErrorMessages(request, introModule.getErrorMessages());
        }
    }

    /**
     * Set the OTYML module if present
     */
    protected void addOTYML(HstRequest request) {
        Page page = getDocument(request);
        if (page.getOtherThings() != null) {
            HorizontalListLinksModule otyml = megalinkFactory.horizontalListLayout(page.getOtherThings(), request.getLocale());
            if (Contract.isEmpty(otyml.getLinks())) {
                contentLogger.warn("OTYML at {} contains 0 published items. Skipping module", page.getOtherThings().getPath());
                request.setAttribute(OTYML, previewFactory.createErrorModule(otyml));
                return;
            }
            if (otyml.getLinks().size() < MegalinkFactory.MIN_ITEMS_CAROUSEL) {
                contentLogger.warn("OTYML at {} contains only {} published items. Expected a minimum of 5", page.getOtherThings().getPath(), otyml.getLinks().size());
            }
            request.setAttribute(OTYML, otyml);
        }
    }

    protected void addNewsletterSignup(HstRequest request) {
        Page page = getDocument(request);
        if (!Contract.defaultIfNull(page.getHideNewsletter(), false)) {
            if (request.getPathInfo().contains(properties.getSiteSkiSection())){
                //TODO: SKi Section uses its own signup post
            } else {
                SignpostModule signpost = signpostFactory.createNewsletterSignpostModule(request.getLocale());
                request.setAttribute(NEWSLETTER_SIGNPOST, signpost);
            }
        }
    }

    /**
     * Add the configuration related to the Product Search Widget for the page
     */
    private void addProductSearchWidget(HstRequest request){
        if (!request.getPathInfo().contains(properties.getSiteSkiSection())){
            request.setAttribute(PSR_WIDGET, psrFactory.getWidget(request));
        }
    }

    public void addLogging(HstRequest request){
        request.setAttribute("Logger", freemarkerLogger);
    }

    /**
     * Return the document from the request
     *
     * @param request HstRequest
     * @return the master document of
     */
    protected T getDocument(HstRequest request) {
        if (request.getAttribute(DOCUMENT) instanceof Page) {
            return (T) request.getAttribute(DOCUMENT);
        } else {
            logger.error("The master document is not an instance of Page.", new ClassCastException());
            return null;
        }
    }

    public static void setErrorMessages(HstRequest request, Collection<String> errorMessages) {
        if (request.getAttribute(PREVIEW_ALERTS) != null){
            Collection<String> requestMessages = (Collection<String>) request.getAttribute(PREVIEW_ALERTS);
            requestMessages.addAll(errorMessages);
        } else {
            request.setAttribute(PREVIEW_ALERTS, errorMessages);
        }
    }
}
