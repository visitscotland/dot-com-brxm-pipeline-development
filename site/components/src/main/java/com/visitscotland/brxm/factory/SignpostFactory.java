package com.visitscotland.brxm.factory;

import com.visitscotland.brxm.model.FlatImage;
import com.visitscotland.brxm.model.FlatLink;
import com.visitscotland.brxm.model.LinkType;
import com.visitscotland.brxm.model.SignpostModule;
import com.visitscotland.brxm.services.ResourceBundleService;
import com.visitscotland.brxm.utils.HippoHtmlWrapper;
import com.visitscotland.brxm.utils.HippoUtilsService;
import com.visitscotland.brxm.utils.Properties;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class SignpostFactory {

    private static  final String BUNDLE_ID = "newsletter-signpost";

    private final ResourceBundleService bundle;
    private final Properties properties;
    private final HippoUtilsService hippoUtilsService;

    public SignpostFactory(ResourceBundleService bundle, Properties properties, HippoUtilsService hippoUtilsService) {
        this.bundle = bundle;
        this.properties = properties;
        this.hippoUtilsService = hippoUtilsService;
    }

    public SignpostModule createNewsletterSignpostModule(Locale locale) {
        SignpostModule signpostModule = new SignpostModule();
        FlatLink cta = new FlatLink(bundle.getResourceBundle(BUNDLE_ID, "newsletter.cta.text", locale),
                hippoUtilsService.createUrlFromNode(properties.getSiteNewsletter(),true), LinkType.INTERNAL);
        FlatImage image = new FlatImage();
        image.setExternalImage(bundle.getResourceBundle(BUNDLE_ID, "newsletter.image", locale));
        signpostModule.setCta(cta);
        signpostModule.setImage(image);
        signpostModule.setTitle(bundle.getResourceBundle(BUNDLE_ID, "newsletter.title", locale));
        signpostModule.setCopy(new HippoHtmlWrapper(bundle.getResourceBundle(BUNDLE_ID, "newsletter.copy", locale)));
        return signpostModule;
    }

    public SignpostModule createSnowAlertsModule(Locale locale) {
        SignpostModule signpostModule = new SignpostModule();
        FlatLink cta = new FlatLink(bundle.getResourceBundle(BUNDLE_ID, "snow-alerts.cta.text", locale),
                bundle.getResourceBundle(BUNDLE_ID, "snow-alerts.cta.link", locale), LinkType.INTERNAL);
        FlatImage image = new FlatImage();

        image.setExternalImage(bundle.getResourceBundle(BUNDLE_ID, "snow-alerts.image", locale));

        signpostModule.setCta(cta);
        signpostModule.setImage(image);
        signpostModule.setTitle(bundle.getResourceBundle(BUNDLE_ID, "snow-alerts.title", locale));
        signpostModule.setCopy(new HippoHtmlWrapper(bundle.getResourceBundle(BUNDLE_ID, "snow-alerts.copy", locale)));
        return signpostModule;
    }

}
