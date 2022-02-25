package com.visitscotland.brxm.components.breadcrumb;


import com.visitscotland.brxm.hippobeans.BaseDocument;
import com.visitscotland.brxm.hippobeans.Page;
import com.visitscotland.brxm.config.VsComponentManager;
import com.visitscotland.brxm.services.DocumentUtilsService;
import com.visitscotland.brxm.utils.HippoUtilsService;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.request.ComponentConfiguration;
import org.onehippo.cms7.essentials.components.CommonComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import java.util.List;
import java.util.Optional;

public class VsBreadcrumbComponent extends CommonComponent {

    private static final Logger logger = LoggerFactory.getLogger(VsBreadcrumbComponent.class.getName());

    final String REQUESTED_URI = "requestedURI";
    final String IS_HOME = "isHome";
    final String BREADCRUMB = "breadcrumb";
    final String DOCUMENT = "document";
    final String ORDERED_TRANSLATIONS = "orderedTranslations";

    private VsBreadCrumbProvider breadcrumbProvider;
    private HippoUtilsService hippoUtilsService;
    private DocumentUtilsService documentUtils;

    public void doBeforeRender(HstRequest request, HstResponse response) throws HstComponentException {
        super.doBeforeRender(request, response);

        //Requested URL to identify the current page from the breadcrumb
        request.setAttribute(REQUESTED_URI, request.getRequestURI());
        //Identify if the page is the home page independently of the environment (local, dev, acct, prod) and language
        request.setAttribute(IS_HOME, "root".equals(request.getRequestContext().getResolvedSiteMapItem().getHstSiteMapItem().getId()));
        //Breadcrumb Items list
        request.setAttribute(BREADCRUMB, this.breadcrumbProvider.getBreadcrumb(request));
        //Main document for the page
        setDocument(request, response);
    }

    private void setDocument(HstRequest request, HstResponse response) {
        Optional<HippoBean> document = hippoUtilsService.getContentBeanWithTranslationFallback(request);
        if (document.isPresent() && document.get() instanceof Page) {
            request.setAttribute(DOCUMENT, document.get());
            // Translations ordered by SEO order
            List<BaseDocument> availableTranslations = ((Page) document.get()).getAvailableTranslations(BaseDocument.class).getTranslations();
            request.setAttribute(ORDERED_TRANSLATIONS, documentUtils.sortTranslationsForSeo(availableTranslations));
        } else {
            logger.error("There is not a document associated for the following request: " + request.getRequestURI());
            this.pageNotFound(response);
        }
    }

    public void init(ServletContext servletContext, ComponentConfiguration componentConfig) throws HstComponentException {
        super.init(servletContext, componentConfig);
        this.breadcrumbProvider = new VsBreadCrumbProvider(this);
        this.hippoUtilsService = VsComponentManager.get(HippoUtilsService.class);
        this.documentUtils = VsComponentManager.get(DocumentUtilsService.class);
    }

}
