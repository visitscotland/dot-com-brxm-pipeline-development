package com.visitscotland.brxm.factory;

import com.visitscotland.brxm.components.content.GeneralContentComponent;
import com.visitscotland.brxm.components.content.PageContentComponent;
import com.visitscotland.brxm.dms.DMSConstants;
import com.visitscotland.brxm.dms.LocationLoader;
import com.visitscotland.brxm.dms.PSType;
import com.visitscotland.brxm.dms.model.LocationObject;
import com.visitscotland.brxm.hippobeans.Destination;
import com.visitscotland.brxm.hippobeans.General;
import com.visitscotland.brxm.hippobeans.Page;
import com.visitscotland.brxm.model.PSModule;
import com.visitscotland.brxm.services.ResourceBundleService;
import com.visitscotland.brxm.utils.Language;
import com.visitscotland.brxm.utils.Properties;
import org.hippoecm.hst.core.component.HstRequest;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class ProductSearchWidgetFactory {

    static final String BUNDLE_ID = "product-search-widget";

    final ResourceBundleService bundle;
    final LocationLoader locationLoader;
    final Properties properties;

    public static final String POSITION_TOP = "top";
    public static final String POSITION_BOTTOM = "bottom";

    public ProductSearchWidgetFactory(ResourceBundleService bundle, LocationLoader locationLoader, Properties properties) {
        this.bundle = bundle;
        this.locationLoader = locationLoader;
        this.properties = properties;
    }

    public PSModule getWidget(@NotNull HstRequest request){
        PSModule module = new PSModule();
        PSType type = PSType.getType(request.getRequestURI());

        module.setTitle(bundle.getResourceBundle(BUNDLE_ID, type.getPathVariable() + ".title", request.getLocale()));
        module.setDescription(bundle.getResourceBundle(BUNDLE_ID, type.getPathVariable() + ".description", request.getLocale()));
        module.setCategory(type);
        module.setLocation(getLocation(request));
        module.setDomain(properties.getDmsHost());

        // Non-JavaScript fall-back URL
        module.setSearchUrl(properties.getDmsHost() + Language.getLanguageForLocale(request.getLocale()).getPathVariable() + String.format(DMSConstants.PRODUCT_SEARCH, type.getPathVariable()));
        module.setPosition(calculatePosition(request));

        return module;
    }

    private String calculatePosition(HstRequest request){
        Page page = (Page) request.getAttribute(PageContentComponent.DOCUMENT);
        if (page instanceof General){
            General general = ((General) page);
            String pattern;

            if (general.getTheme().equals(GeneralContentComponent.TOP_LEVEL) || general.getTheme().equals(GeneralContentComponent.STANDARD)) {
                pattern = "^/((places-to-go)(/[\\w-]+)?|(things-to-do)(/[\\w-]+){2,}|(inspiration)(/[\\w-]+){1,})$";
            } else {
                return POSITION_BOTTOM;
            }

            Pattern p = Pattern.compile(pattern);
            return p.matcher(request.getPathInfo()).matches()?POSITION_BOTTOM:POSITION_TOP;
        } else if (page instanceof Destination){
            return POSITION_TOP;
        } else {
            return POSITION_BOTTOM;
        }
    }

    /**
     * @param request
     * @return
     */
    private LocationObject getLocation(HstRequest request){
        Page page = request.getModel("document");

        while (page != null && !(page instanceof Destination)){
            page = page.getParentBean().getParentBean().getBean("content");
        }

        if (page instanceof Destination) {
            return locationLoader.getLocation(((Destination) page).getLocation(), request.getLocale());
        } else {
            return null;
        }
    }
}
