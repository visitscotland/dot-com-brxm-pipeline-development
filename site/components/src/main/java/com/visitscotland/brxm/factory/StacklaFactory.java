package com.visitscotland.brxm.factory;

import com.visitscotland.brxm.hippobeans.Stackla;
import com.visitscotland.brxm.model.StacklaModule;
import com.visitscotland.brxm.services.ResourceBundleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class StacklaFactory {

    private final ResourceBundleService bundle;
    private static final Logger logger = LoggerFactory.getLogger(StacklaFactory.class);
    static final String BUNDLE_ID = "stackla";

    public StacklaFactory(ResourceBundleService bundle) {
        this.bundle = bundle;
    }

    public StacklaModule getStacklaModule(Stackla document, Locale locale) {
        logger.info("Creating stacklaModule for {}", document.getPath());
        StacklaModule stacklaModule = new  StacklaModule();
        stacklaModule.setTitle(document.getTitle());
        stacklaModule.setCopy(document.getCopy());
        stacklaModule.setDataId(document.getStacklaId());
        stacklaModule.setDataHash(document.getStacklaHash());
        stacklaModule.setHippoBean(document);
        stacklaModule.setNoCookiesMessage(bundle.getResourceBundle(BUNDLE_ID, "stackla.no-cookies-message", locale));
        stacklaModule.setNoJsMessage(bundle.getResourceBundle(BUNDLE_ID, "stackla.no-js-message", locale));
        stacklaModule.setNoCookiesLinkLabel(bundle.getResourceBundle(BUNDLE_ID, "stackla.update-cookies-link.label", locale));
        return stacklaModule;
    }


}
