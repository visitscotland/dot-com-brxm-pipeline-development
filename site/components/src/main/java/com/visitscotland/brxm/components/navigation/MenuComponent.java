package com.visitscotland.brxm.components.navigation;


import com.visitscotland.brxm.hippobeans.Page;
import com.visitscotland.brxm.hippobeans.Widget;
import com.visitscotland.brxm.model.LocalizedURL;
import com.visitscotland.brxm.config.VsComponentManager;
import com.visitscotland.brxm.components.navigation.info.MenuComponentInfo;
import com.visitscotland.brxm.services.ResourceBundleService;
import com.visitscotland.brxm.services.CommonUtilsService;
import com.visitscotland.brxm.utils.HippoUtilsService;
import com.visitscotland.brxm.utils.Language;
import com.visitscotland.utils.Contract;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoFolder;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.linking.HstLink;
import org.hippoecm.hst.core.parameters.ParametersInfo;
import org.hippoecm.hst.core.request.ResolvedSiteMapItem;
import org.hippoecm.hst.core.sitemenu.HstSiteMenu;
import org.hippoecm.hst.core.sitemenu.HstSiteMenuItem;
import org.onehippo.cms7.essentials.components.EssentialsMenuComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.MissingFormatArgumentException;

@ParametersInfo(
        type = MenuComponentInfo.class
)
public class MenuComponent extends EssentialsMenuComponent {

    private static final Logger logger = LoggerFactory.getLogger(MenuComponent.class);

    static final String STATIC = "navigation.static";
    static final String NAVIGATION_PREFIX = "navigation.";
    static final String CTA_SUFFIX = ".cta";

    static final String MENU = "menu";

    private ResourceBundleService bundle;
    private HippoUtilsService utils;

    public MenuComponent() {
        this(VsComponentManager.get(ResourceBundleService.class), VsComponentManager.get(HippoUtilsService.class));
    }

    public MenuComponent(ResourceBundleService bundle, HippoUtilsService utils) {
        this.bundle = bundle;
        this.utils = utils;
    }

    @Override
    public void doBeforeRender(HstRequest request, HstResponse response) {
        super.doBeforeRender(request, response);
        enhanceRequest(request);
        addLocalizedURLs(request);
    }

    public void addLocalizedURLs(HstRequest request) {
        List<LocalizedURL> translatedURL = new ArrayList<>(Language.values().length);


        HippoBean document = request.getRequestContext().getContentBean();

        HippoBean englishSite = null;
        if (document != null) {
            for (Language language : Language.values()) {
                LocalizedURL lan = new LocalizedURL();
                lan.setLocale(language.getLocale());
                lan.setLanguage(language.getLocale().getLanguage());
                lan.setDisplayName(bundle.getResourceBundle("universal", lan.getLanguage(), request.getLocale()));

                HippoBean translation = document.getAvailableTranslations().getTranslation(lan.getLanguage());

                if (Locale.UK.equals(language.getLocale())) {
                    if (translation == null) {
                        logger.error("The requested page does not exist in English: {}", document.getPath());
                    } else {
                        englishSite = translation;
                    }
                }

                if (translation != null) {
                    lan.setUrl(utils.createUrl(translation));
                    lan.setExists(true);
                } else {
                    //TODO: Define if the URL is made up, or we use the englishSite link instead
                    //lan.setUrl(utils.createUrl(englishSite));
                    lan.setUrl(composeNonExistingURL(language.getLocale(), request));
                    lan.setExists(false);
                }
                translatedURL.add(lan);
            }
        } else {
            logger.error("Menu functionality is not supported for Channel Manager Pages at the moment");
        }
        request.setModel("localizedURLs", translatedURL);
    }

    /**
     * Composes the URL from the current request for a non-existing URL.
     */
    private String composeNonExistingURL(Locale locale, HstRequest request){
        String languagePath = "";

        if (locale != null) {
            languagePath += "/" + locale.getLanguage();
        }

        return request.getRequestContext().getBaseURL().getHostName() +
                request.getRequestContext().getBaseURL().getContextPath() +
                languagePath + request.getRequestContext().getBaseURL().getPathInfo();
    }

    protected void enhanceRequest(HstRequest request) {
        enhanceMenu(request);
    }

    protected void enhanceMenu(HstRequest request) {
        List<HstSiteMenuItem> enhancedMenu = new ArrayList<>();
        RootMenuItem root = new RootMenuItem(request.getModel(MENU));

        //Calculate the resource bundle id
        String resourceBundle = NAVIGATION_PREFIX + ((HstSiteMenu) request.getModel(MENU)).getName();

        if (request.getModel(MENU) != null) {
            for (HstSiteMenuItem hstItem : (((HstSiteMenu) request.getModel(MENU)).getSiteMenuItems())) {
                MenuItem menuItem = enhanceMenuItem(request, hstItem, resourceBundle);
                if (menuItem != null) {
                    enhancedMenu.add(menuItem);
                }
            }

            root.setSiteMenuItems(enhancedMenu);

            request.setModel(MENU, root);
        }
    }

    private MenuItem enhanceMenuItem(HstRequest request, HstSiteMenuItem hstItem, String resourceBundle) {
        MenuItem menuItem = new MenuItem(hstItem);

        //By default, the name would be populated by the resourceBundle
        menuItem.setTitle(bundle.getResourceBundle(resourceBundle, hstItem.getName(), request.getLocale(), true));

        //if document base page or widget, we enhance the document
        if (isDocumentBased(hstItem.getHstLink())) {
            ResolvedSiteMapItem rsi = hstItem.resolveToSiteMapItem();
            if (rsi != null) {
                HippoBean bean = utils.getBeanForResolvedSiteMapItem(request, rsi);
                //if the document does not exist or no publish
                if (bean != null && !(bean instanceof HippoFolder)) {

                    //Widget document
                    if (bean instanceof Widget) {
                        menuItem.setWidget((Widget) bean);
                    } else if (bean instanceof Page) {
                        updateMenuItemFromDocument(menuItem, (Page) bean, resourceBundle, request);
                    } else {
                        logger.warn("Skipping Unexpected document type: {}", bean.getClass().getSimpleName());
                    }
                }
            }
        }

        if (menuItem.getTitle() != null || menuItem.getWidget() != null) {
            //Process all children
            for (HstSiteMenuItem hstChild : hstItem.getChildMenuItems()) {
                menuItem.addChild(enhanceMenuItem(request, hstChild, resourceBundle));
            }
            return menuItem;
        } else {
            //Menu Items with no title cannot be displayed, so they are not included in the list of menu Items.
            return null;
        }
    }

    /**
     * Updates the menu item with enhanced information taken from the labels, or the core document where they link to
     *
     * @param menuItem Menu Item to enhance
     * @param document HippoBean that contains the relevant document that is linked from the header
     * @param bundleId Resource Bundle where the labels of the menu item might come from
     * @param request  HstRequest
     */
    private void updateMenuItemFromDocument(MenuItem menuItem, Page document, String bundleId, HstRequest request) {
        //If the menu hasn't been set we use the title coming from the document.
        if (Contract.isEmpty(menuItem.getTitle())) {
            if (!Contract.isEmpty(document.getBreadcrumb())) {
                menuItem.setTitle(document.getBreadcrumb());
            } else {
                menuItem.setTitle(document.getTitle());
            }
        }

        if (bundle.existsResourceBundleKey(bundleId, menuItem.getHstMenuItem().getName() + CTA_SUFFIX, request.getLocale())) {
            menuItem.setCta(bundle.getResourceBundle(bundleId, menuItem.getHstMenuItem().getName() + CTA_SUFFIX, request.getLocale()));
        } else if (menuItem.getTitle() != null) {
            String seeAll = bundle.getResourceBundle(STATIC, "see-all-cta", request.getLocale());
            if (seeAll != null) {
                try {
                    menuItem.setCta(String.format(seeAll, menuItem.getTitle()));
                } catch (MissingFormatArgumentException e) {
                    String message = String.format("The label '%s' has more parameters than expected. File: %s, key: %s",
                            seeAll, STATIC, "see-all-cta");
                    logger.warn(message);
                    CommonUtilsService.contentIssue(message.replace("%", "%%"));

                    //After Catching the exception, we can eliminate the parameters.
                    menuItem.setCta(seeAll.replace("%s", ""));
                }
            }
        } else {
            //Title is a mandatory field for all page documents. The following line being executed might mean that the node is corrupted
            logger.error("The node {} does not have a title", document.getPath());
        }
    }

    private boolean isDocumentBased(HstLink link){
        return link != null && link.getPath() != null && link.getPath().length() > 0;
    }

}
