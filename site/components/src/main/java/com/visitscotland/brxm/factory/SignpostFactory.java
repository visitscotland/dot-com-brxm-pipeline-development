package com.visitscotland.brxm.factory;

import com.visitscotland.brxm.hippobeans.CTABanner;
import com.visitscotland.brxm.hippobeans.capabilities.Linkable;
import com.visitscotland.brxm.model.FlatImage;
import com.visitscotland.brxm.model.FlatLink;
import com.visitscotland.brxm.model.LinkType;
import com.visitscotland.brxm.model.SignpostModule;
import com.visitscotland.brxm.services.LinkService;
import com.visitscotland.brxm.services.ResourceBundleService;
import com.visitscotland.brxm.utils.HippoHtmlWrapper;
import com.visitscotland.brxm.utils.HippoUtilsService;
import com.visitscotland.brxm.utils.SiteProperties;
import com.visitscotland.utils.Contract;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class SignpostFactory {

    private static final String BUNDLE_ID = "newsletter-signpost";

    private final ResourceBundleService bundle;
    private final SiteProperties properties;
    private final HippoUtilsService hippoUtilsService;
    private final LinkService linkService;

    public SignpostFactory(ResourceBundleService bundle, SiteProperties properties, HippoUtilsService hippoUtilsService, LinkService linkService) {
        this.bundle = bundle;
        this.properties = properties;
        this.hippoUtilsService = hippoUtilsService;
        this.linkService = linkService;
    }

    public SignpostModule createNewsletterSignpostModule(Locale locale) {
        String newsletterUrl = hippoUtilsService.createUrlFromNode(properties.getSiteNewsletter(), true);
        if (!Contract.isNull(newsletterUrl)) {
            SignpostModule signpostModule = createSignPostModule(BUNDLE_ID, "newsletter", locale);
            if (signpostModule != null) {
                signpostModule.getCta().setLink(newsletterUrl);
                return signpostModule;
            }
        }

        return null;
    }


    public SignpostModule createSnowAlertsModule(Locale locale) {
        return createSignPostModule(BUNDLE_ID, "snow-alerts", locale);
    }

    public SignpostModule createDeliveryAPIModule(Locale locale) {
        return createSignPostModule(properties.getSiteId() +"." + BUNDLE_ID, "newsletter", locale);
    }

    public SignpostModule createModule (CTABanner ctaBanner){
        SignpostModule signpostModule = new SignpostModule();
        Linkable linkable = (Linkable) ctaBanner.getCtaLink().getLink();
        FlatLink cta = linkService.createSimpleLink(linkable,signpostModule, null);

        if (Contract.isNull(cta.getLink())) {
            return null;
        }

        FlatImage image = new FlatImage();
        image.setCmsImage(ctaBanner.getImage());
        signpostModule.setCta(cta);
        signpostModule.setImage(image);
        signpostModule.setTitle(ctaBanner.getTitle());
        signpostModule.setCopy(ctaBanner.getIntroduction());

        return signpostModule;
    }

    private SignpostModule createSignPostModule(String bundleName, String prefix, Locale locale) {
        SignpostModule signpostModule = new SignpostModule();
        FlatLink cta = new FlatLink(bundle.getResourceBundle(bundleName, prefix + ".cta.text", locale),
                bundle.getResourceBundle(bundleName, prefix + ".cta.link", locale), LinkType.INTERNAL);

        if (Contract.isNull(cta.getLink())) {
            return null;
        }

        FlatImage image = new FlatImage();
        image.setExternalImage(bundle.getResourceBundle(bundleName, prefix + ".image", locale));
        signpostModule.setCta(cta);
        signpostModule.setImage(image);
        signpostModule.setTitle(bundle.getResourceBundle(bundleName, prefix + ".title", locale));
        signpostModule.setCopy(new HippoHtmlWrapper(bundle.getResourceBundle(bundleName, prefix + ".copy", locale)));

        return signpostModule;
    }
}
