package com.visitscotland.brxm.config;

import org.hippoecm.hst.container.RequestContextProvider;
import org.hippoecm.hst.core.container.ContainerConstants;
import org.hippoecm.hst.core.internal.HstMutableRequestContext;
import org.hippoecm.hst.core.request.ResolvedMount;
import org.hippoecm.hst.core.request.ResolvedSiteMapItem;
import org.hippoecm.hst.core.request.ResolvedVirtualHost;
import org.hippoecm.hst.core.request.SiteMapItemHandlerConfiguration;
import org.hippoecm.hst.core.sitemapitemhandler.HstSiteMapItemHandler;
import org.hippoecm.hst.core.sitemapitemhandler.HstSiteMapItemHandlerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TranslationHstSiteMapItemHandler implements HstSiteMapItemHandler {

    private static final Logger logger = LoggerFactory.getLogger(TranslationHstSiteMapItemHandler.class);
    private static final String PAGE_NOT_FOUND_COMPONENT = "hst:pages/pagenotfound";

    @Override
    public void init(ServletContext servletContext, SiteMapItemHandlerConfiguration siteMapItemHandlerConfiguration) throws HstSiteMapItemHandlerException {

    }

    /**
     * Implement the translation fallback mechanism.
     * For a non-english locale, if the translated content is not found but the english version exists, then the english content should be shown instead
     * By returning an English sitemap but keeping the translated mount, brxm will attempt to look up all  content (including menus, breadcrunmb etc) in
     * the translated repository (instead of returning to a pagenotfound sitemap). Then the content component can override the
     * content lookup to check the english repository instead of the translated repository
     */
    @Override
    public ResolvedSiteMapItem process(ResolvedSiteMapItem resolvedSiteMapItem, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws HstSiteMapItemHandlerException {
        if (resolvedSiteMapItem == null || resolvedSiteMapItem.getResolvedMount() == null || !isPageNotFound(resolvedSiteMapItem)) {
            return resolvedSiteMapItem;
        }
        ResolvedVirtualHost resolvedVirtualHost = (ResolvedVirtualHost) httpServletRequest.getAttribute(ContainerConstants.VIRTUALHOSTS_REQUEST_ATTR);
        if (resolvedVirtualHost == null) {
            logger.error("Failed to get ResolvedVirtualHost from request servlet");
            return resolvedSiteMapItem;
        }
        ResolvedMount englishMount = resolvedVirtualHost.matchMount("/");
        HstMutableRequestContext requestContext = (HstMutableRequestContext) RequestContextProvider.get();
        requestContext.setResolvedMount(englishMount);
        ResolvedSiteMapItem englishSiteMapItem = englishMount.matchSiteMapItem("/" + resolvedSiteMapItem.getPathInfo());
        requestContext.setResolvedSiteMapItem(englishSiteMapItem);
        boolean isNotFound = isPageNotFound(englishSiteMapItem);
        requestContext.setResolvedMount(resolvedSiteMapItem.getResolvedMount());
        if (!isNotFound) {
            // English content does exist - use translated mount with an english sitemap
            return englishSiteMapItem;
        }
        return resolvedSiteMapItem;
    }

    private boolean isPageNotFound(ResolvedSiteMapItem resolvedSiteMapItem) {
        if (resolvedSiteMapItem == null || resolvedSiteMapItem.getHstComponentConfiguration() == null) {
            return false;
        }
        return resolvedSiteMapItem.getHstComponentConfiguration().getId().equals(PAGE_NOT_FOUND_COMPONENT);
    }

    @Override
    public void destroy() throws HstSiteMapItemHandlerException {

    }
}
