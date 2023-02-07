package com.visitscotland.brxm.factory;

import com.visitscotland.brxm.model.FlatLink;
import com.visitscotland.brxm.model.LinkType;
import com.visitscotland.brxm.model.SignpostModule;
import com.visitscotland.brxm.services.ResourceBundleService;
import com.visitscotland.brxm.utils.HippoHtmlWrapper;
import com.visitscotland.brxm.utils.Properties;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class SignpostFactory {

    private static  final String BUNDLE_ID = "newsletter-signpost";

    private final ResourceBundleService bundle;
    private final Properties properties;

    public SignpostFactory(ResourceBundleService bundle, Properties properties) {
        this.bundle = bundle;
        this.properties = properties;
    }

    public SignpostModule createNewsletterSignpostModule(Locale locale) {
        SignpostModule signpostModule = new SignpostModule();
        FlatLink cta = new FlatLink(bundle.getResourceBundle(BUNDLE_ID, "newsletter.cta.text", locale),
                properties.getSiteNewsletter(locale), LinkType.INTERNAL);
        signpostModule.setCta(cta);
        signpostModule.setTitle(bundle.getResourceBundle(BUNDLE_ID, "newsletter.title", locale));
        signpostModule.setCopy(new HippoHtmlWrapper(bundle.getResourceBundle(BUNDLE_ID, "newsletter.copy", locale)));
        return signpostModule;
    }

}
