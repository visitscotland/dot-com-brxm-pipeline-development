package com.visitscotland.brxm.components.navigation;


import com.visitscotland.brxm.components.navigation.info.MenuComponentInfo;
import com.visitscotland.brxm.config.VsComponentManager;
import com.visitscotland.brxm.factory.NavigationFactory;
import com.visitscotland.brxm.utils.HippoUtilsService;
import com.visitscotland.brxm.utils.InternalParameterProcessor;
import com.visitscotland.brxm.utils.Language;
import com.visitscotland.brxm.utils.Properties;
import org.hippoecm.hst.container.RequestContextProvider;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.internal.HstMutableRequestContext;
import org.hippoecm.hst.core.parameters.ParametersInfo;
import org.hippoecm.hst.core.sitemenu.HstSiteMenu;
import org.onehippo.cms7.essentials.components.EssentialsMenuComponent;

import java.util.Locale;

@ParametersInfo(
        type = MenuComponentInfo.class
)
public class MenuComponent extends EssentialsMenuComponent {

    private static final String VS_PREFIX = "navigation.";
    private static final String BE_PREFIX = "be.navigation.";

    public static final String MENU = "menu";
    public static final String LOCALIZED_URLS = "localizedURLs";

    private NavigationFactory factory;
    private HippoUtilsService utils;
    private Properties properties;


    public MenuComponent() {
        this(VsComponentManager.get(NavigationFactory.class),
                VsComponentManager.get(HippoUtilsService.class),
                VsComponentManager.get(Properties.class));
    }

    public MenuComponent(NavigationFactory factory, HippoUtilsService utils, Properties properties) {
        this.factory = factory;
        this.utils = utils;
        this.properties = properties;
    }

    @Override
    public void doBeforeRender(HstRequest request, HstResponse response) {
        super.doBeforeRender(request, response);

        if (request.getRequestURI().startsWith(properties.getDmsInternalPath())) {
            String vsLocale = utils.getParameterFromUrl(request, InternalParameterProcessor.PARAM_LOCALE);
            if (vsLocale != null) {
                Language language = Language.getLanguageForLocale(Locale.forLanguageTag(vsLocale));
                HstMutableRequestContext requestContext = (HstMutableRequestContext) RequestContextProvider.get();
                requestContext.setResolvedMount(utils.getMount(request, language.getCmsMount()));
                requestContext.setPreferredLocale(language.getLocale());
            }
        }
        boolean editModeCache = (Boolean.TRUE.equals(request.getAttribute("editMode")) && Boolean.TRUE.equals(properties.getNavigationCache()));
        boolean cacheable = (properties.isSnippetCacheEnabled() && Boolean.FALSE.equals(request.getAttribute("editMode"))) || editModeCache;
        String token = getAnyParameter(request, "preview-token");

        RootMenuItem rootMenuItem = factory.buildMenu(request, getResourceBundle(request), token, cacheable);
        rootMenuItem.setCmsCached(editModeCache);

        request.setModel(MENU, rootMenuItem);
    }

    private String getResourceBundle(HstRequest request){
        String prefix;
        if (utils.isBusinessEventsSite(request)){
            prefix = BE_PREFIX;
        } else {
            prefix = VS_PREFIX;
        }

        return prefix +  ((HstSiteMenu) request.getModel(MENU)).getName();
    }
}
