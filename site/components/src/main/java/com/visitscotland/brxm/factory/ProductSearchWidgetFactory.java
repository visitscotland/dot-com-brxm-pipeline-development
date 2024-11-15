package com.visitscotland.brxm.factory;

import com.visitscotland.brxm.services.ResourceBundleService;
import com.visitscotland.brxm.dms.model.LocationObject;
import com.visitscotland.brxm.hippobeans.Destination;
import com.visitscotland.brxm.utils.CMSProperties;
import com.visitscotland.brxm.dms.LocationLoader;
import com.visitscotland.brxm.dms.DMSConstants;
import com.visitscotland.brxm.hippobeans.Page;
import com.visitscotland.brxm.utils.Language;
import com.visitscotland.brxm.model.PSModule;
import com.visitscotland.brxm.dms.PSType;

import org.hippoecm.hst.core.component.HstRequest;
import org.springframework.stereotype.Component;
import org.jetbrains.annotations.NotNull;

@Component
public class ProductSearchWidgetFactory {
    static final String BUNDLE_ID = "product-search-widget";

    final ResourceBundleService bundle;
    final LocationLoader locationLoader;
    final CMSProperties properties;

    private static final String POSITION_BOTTOM = "Bottom";

    public ProductSearchWidgetFactory(ResourceBundleService bundle, LocationLoader locationLoader, CMSProperties properties) {
        this.bundle = bundle;
        this.locationLoader = locationLoader;
        this.properties = properties;
    }

    public PSModule getWidget(@NotNull HstRequest request) {
        PSModule module = new PSModule();
        PSType type = PSType.getType(request.getRequestURI());

        module.setTitle(bundle.getResourceBundle(BUNDLE_ID, type.getPathVariable() + ".title", request.getLocale()));
        module.setDescription(bundle.getResourceBundle(BUNDLE_ID, type.getPathVariable() + ".description", request.getLocale()));
        module.setCategory(type);
        module.setLocation(getLocation(request));
        module.setDomain(properties.getDmsHost());

        // Non-JavaScript fall-back URL
        module.setSearchUrl(properties.getDmsHost() + Language.getLanguageForLocale(request.getLocale()).getPathVariable() + String.format(DMSConstants.PRODUCT_SEARCH, type.getPathVariable()));
        module.setPosition(POSITION_BOTTOM);

        return module;
    }

//    TODO - Once the requirements around this are reviewed and solidified, this method can be removed.
//    private String calculatePosition(HstRequest request){
//        Page page = (Page) request.getAttribute(PageContentComponent.DOCUMENT);
//        if (page instanceof General){
//            General general = ((General) page);
//
//            if (general.getBlog() != null) {
//                return POSITION_BOTTOM;
//            } else if (!Contract.isEmpty(general.getPswPosition()) && !general.getPswPosition().equals(POSITION_DEFAULT)) {
//                return general.getPswPosition();
//            } else if (!general.getTheme().equals(GeneralContentComponent.SIMPLE)) {
//                return POSITION_TOP;
//            }
//        } else if (page instanceof Destination){
//            return POSITION_TOP;
//        }
//
//        return POSITION_BOTTOM;
//    }

    /**
     * @param request the HstRequest request
     * @return Location object populated
     */
    private LocationObject getLocation(HstRequest request) {
        Page page = request.getModel("document");

        while (page != null && !(page instanceof Destination)) {
            page = page.getParentBean().getParentBean().getBean("content");
        }

        if (page instanceof Destination) {
            return locationLoader.getLocation(((Destination) page).getLocation(), request.getLocale());
        } else {
            return null;
        }
    }
}