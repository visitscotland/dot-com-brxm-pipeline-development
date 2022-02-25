package com.visitscotland.brxm.factory;

import com.visitscotland.brxm.hippobeans.Banner;
import com.visitscotland.brxm.model.BannerModule;
import com.visitscotland.brxm.model.FlatLink;
import com.visitscotland.brxm.services.LinkService;
import com.visitscotland.brxm.services.ResourceBundleService;
import com.visitscotland.brxm.utils.HippoUtilsService;
import com.visitscotland.utils.Contract;
import org.hippoecm.hst.content.beans.ObjectBeanManagerException;
import org.hippoecm.hst.content.beans.query.exceptions.QueryException;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.core.component.HstRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import java.util.Locale;
import java.util.NoSuchElementException;

@Component
public class BannerFactory {

    private final LinkService linkService;
    private final HippoUtilsService hippoUtilsService;
    private final ResourceBundleService bundle;
    private static final Logger logger = LoggerFactory.getLogger(BannerFactory.class);
    private static final Logger contentLogger = LoggerFactory.getLogger("content");

    public BannerFactory(LinkService linkService, HippoUtilsService hippoUtilsService, ResourceBundleService bundle) {
        this.linkService = linkService;
        this.hippoUtilsService = hippoUtilsService;
        this.bundle = bundle;
    }

    public BannerModule getBannerModule(HstRequest request) {
        String relativeBannerPath = bundle.getResourceBundle("banner", "path", Locale.UK);
        if (Contract.isEmpty(relativeBannerPath)) {
            return null;
        }
        try {
            HippoBean bean = hippoUtilsService.getDocumentFromContent(relativeBannerPath);
            if (bean instanceof Banner) {
                return getBannerModule((Banner)bean, request.getLocale());
            } else {
               contentLogger.warn("Expected Banner bean at {}, but found {}", relativeBannerPath, bean == null ? "null" : bean.getClass().getSimpleName());
            }
        } catch (PathNotFoundException | NoSuchElementException e) {
            logger.trace("No published banner found at {}", relativeBannerPath);
        } catch (RepositoryException | ObjectBeanManagerException | QueryException e) {
            logger.error("Failed to access repo to access banner at location {}", relativeBannerPath, e);
        }
        return null;
    }

    private BannerModule getBannerModule(Banner document, Locale locale) {
        BannerModule module = new BannerModule();
        module.setTitle(document.getTitle());
        module.setCopy(document.getCopy());
        FlatLink ctaLink = linkService.createFindOutMoreLink(module, locale, document.getCtaLink());
        module.setCtaLink(ctaLink);
        return module;
    }
}
