package com.visitscotland.brxm.factory;

import com.visitscotland.brxm.hippobeans.IKnow;
import com.visitscotland.brxm.model.FlatLink;
import com.visitscotland.brxm.model.IKnowModule;
import com.visitscotland.brxm.config.VsComponentManager;
import com.visitscotland.brxm.dms.DMSConstants;
import com.visitscotland.brxm.dms.ProductSearchBuilder;
import com.visitscotland.brxm.services.ResourceBundleService;
import com.visitscotland.brxm.utils.HippoHtmlWrapper;
import com.visitscotland.brxm.utils.HippoUtilsService;
import com.visitscotland.utils.Contract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class IKnowFactory {

    private static final Logger logger = LoggerFactory.getLogger(IKnowFactory.class);

    static final String BUNDLE_ID = "tourism.information";

    private final HippoUtilsService utils;
    private final ResourceBundleService bundle;

    public IKnowFactory(HippoUtilsService utils, ResourceBundleService bundle){
        this.utils = utils;
        this.bundle = bundle;
    }

    public IKnowModule getIKnowModule(IKnow document, String location, Locale locale){
        logger.info("Creating iCentreModule for {}", document.getPath());

        IKnowModule module = new IKnowModule ();

        if (Contract.isEmpty(document.getTitle())){
            module.setTitle(bundle.getResourceBundle(BUNDLE_ID,"iknow.title.default", locale));
        } else {
            module.setTitle(document.getTitle());
        }

        String defaultDescription = bundle.getResourceBundle(BUNDLE_ID,"iknow.description.default", locale);
        module.setDescription(new HippoHtmlWrapper(document.getDescription(), defaultDescription));

        FlatLink link = new FlatLink();

        //TODO get prodTypes from Labels (Configuration)
        //TODO Chekc with http://localhost:8080/cms/content/path/content/documents/administration/options/product-search

        link.setLink(VsComponentManager.get(ProductSearchBuilder.class).locale(locale).productTypes(DMSConstants.TYPE_IKNOW).award(DMSConstants.AWARD_IKNOW).location(location).build());
        link.setLabel(bundle.getResourceBundle(BUNDLE_ID,"iknow.link.label", locale));

        module.setLink(link);

        return module;
    }
}
