package com.visitscotland.brxm.components.content;

import com.visitscotland.brxm.config.VsComponentManager;
import com.visitscotland.brxm.factory.ImageFactory;
import com.visitscotland.brxm.factory.LinkModulesFactory;
import com.visitscotland.brxm.hippobeans.BaseDocument;
import com.visitscotland.brxm.hippobeans.Page;
import com.visitscotland.brxm.model.FlatImage;
import com.visitscotland.brxm.model.Module;
import com.visitscotland.utils.Contract;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.onehippo.cms7.essentials.components.EssentialsContentComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class PageContentComponent<T extends Page> extends EssentialsContentComponent {

    private static final Logger logger = LoggerFactory.getLogger(PageContentComponent.class);

    public static final String DOCUMENT = "document";
    public static final String EDIT_PATH = "path";
    public static final String OTYML = "otyml";

    private LinkModulesFactory linksFactory;
    private ImageFactory imageFactory;

    public PageContentComponent() {
        linksFactory = VsComponentManager.get(LinkModulesFactory.class);
        imageFactory = VsComponentManager.get(ImageFactory.class);
    }

    @Override
    public void doBeforeRender(HstRequest request, HstResponse response) {
        super.doBeforeRender(request, response);

        addDocumentPath(request);
        addHeroImage(request);

        addOTYML(request);
    }

    /**
     *
     * - Alerts are only used for issues related with the hero image at the moment
     * - Hero Image is not necessary for all document types. Is it better to add the field in order to keep consistency?
     */
    private void addHeroImage(HstRequest request){
        final String HERO_IMAGE = "heroImage";
        final String ALERTS = "alerts";
        Module<T> introModule = new Module<>();

        FlatImage heroImage = imageFactory.createImage(getDocument(request).getHeroImage(), introModule, request.getLocale());
        request.setAttribute(HERO_IMAGE, heroImage);

        if (!Contract.isEmpty(introModule.getErrorMessages())) {
            request.setAttribute(ALERTS, introModule.getErrorMessages());
        }
    }

    /**
     * Set the OTYML module if present
     */
    protected void addOTYML(HstRequest request) {
        Page page = getDocument(request);
        if (page.getOtherThings() != null) {
            request.setAttribute(OTYML, linksFactory.horizontalListLayout(page.getOtherThings(), request.getLocale()));
        }
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

    /**
     * Add the document path that will be used as path by default when creating documents. So for Example, the prompted
     * path for the days of an itinenrary would be where the itinerary (masted document) lives
     *
     * @param request Request where the master document is defined and where the path will be added
     */
    private void addDocumentPath(HstRequest request) {
        final String ROOT_SITE = "/site/";

        if (request.getAttribute(DOCUMENT) instanceof BaseDocument) {
            BaseDocument document = getDocument(request);
            //Extract the document path for the CMS Editor
            String path = document.getPath().substring(
                    document.getPath().indexOf(ROOT_SITE) + ROOT_SITE.length(),
                    document.getPath().indexOf("/content/content"));

            request.setAttribute(EDIT_PATH, path);
        }
    }
}
