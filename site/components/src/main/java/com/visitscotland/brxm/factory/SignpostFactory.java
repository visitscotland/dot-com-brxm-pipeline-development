package com.visitscotland.brxm.factory;

import com.visitscotland.brxm.model.FlatImage;
import com.visitscotland.brxm.model.FlatLink;
import com.visitscotland.brxm.model.LinkType;
import com.visitscotland.brxm.model.SignpostModule;
import com.visitscotland.brxm.services.ResourceBundleService;
import com.visitscotland.brxm.utils.HippoHtmlWrapper;
import com.visitscotland.brxm.utils.HippoUtilsService;
import com.visitscotland.brxm.utils.Properties;
import com.visitscotland.utils.Contract;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class SignpostFactory {

    private static final String BUNDLE_ID = "newsletter-signpost";
    private static final String BE_BUNDLE_ID = "be.newsletter-signpost";

    private final ResourceBundleService bundle;
    private final Properties properties;
    private final HippoUtilsService hippoUtilsService;

    public SignpostFactory(ResourceBundleService bundle, Properties properties, HippoUtilsService hippoUtilsService) {
        this.bundle = bundle;
        this.properties = properties;
        this.hippoUtilsService = hippoUtilsService;
    }

    public SignpostModule createNewsletterSignpostModule(Locale locale) {
        String newsletterUrl = hippoUtilsService.createUrlFromNode(properties.getSiteNewsletter(), true);
        if (!Contract.isNull(newsletterUrl)) {
            SignpostModule signpostModule = createModule(BUNDLE_ID, "newsletter", locale);
            if (signpostModule != null) {
                signpostModule.getCta().setLink(newsletterUrl);
                return signpostModule;
            }
        }

        return null;
    }

    public SignpostModule createSnowAlertsModule(Locale locale) {
        return createModule(BUNDLE_ID, "snow-alerts", locale);
    }

    public SignpostModule createBusinessEventsModule(Locale locale) {
        return createModule(BE_BUNDLE_ID, "newsletter", locale);
    }

    private SignpostModule createModule(String bundleName, String prefix, Locale locale) {
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
