package com.visitscotland.brxm.factory;

import com.visitscotland.brxm.components.navigation.MenuItem;
import com.visitscotland.brxm.components.navigation.RootMenuItem;
import com.visitscotland.brxm.dms.ProductSearchBuilder;
import com.visitscotland.brxm.hippobeans.CMSLink;
import com.visitscotland.brxm.hippobeans.FeaturedWidget;
import com.visitscotland.brxm.hippobeans.Page;
import com.visitscotland.brxm.hippobeans.ProductsSearch;
import com.visitscotland.brxm.hippobeans.capabilities.Linkable;
import com.visitscotland.brxm.model.megalinks.EnhancedLink;
import com.visitscotland.brxm.model.navigation.FeaturedEvent;
import com.visitscotland.brxm.model.navigation.FeaturedItem;
import com.visitscotland.brxm.model.navigation.NavigationWidget;
import com.visitscotland.brxm.services.LinkService;
import com.visitscotland.brxm.services.ResourceBundleService;
import com.visitscotland.brxm.utils.ContentLogger;
import com.visitscotland.brxm.utils.HippoUtilsService;
import com.visitscotland.utils.Contract;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoFolder;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.linking.HstLink;
import org.hippoecm.hst.core.sitemenu.HstSiteMenu;
import org.hippoecm.hst.core.sitemenu.HstSiteMenuItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class NavigationFactory {

    private static final Logger logger = LoggerFactory.getLogger(NavigationFactory.class);


    static final String STATIC = "navigation.static";
    static final String CTA_SUFFIX = ".cta";
    static final String NAVIGATION_PREFIX = "navigation.";
    static final String WIDGET_LIST = "widgetList";

    private ResourceBundleService bundle;
    private HippoUtilsService utils;
    private LinkService linkService;
    private ProductSearchBuilder productSearchBuilder;
    private final Logger contentLogger;

    public NavigationFactory(ResourceBundleService bundle, HippoUtilsService utils, LinkService linkService, ProductSearchBuilder productSearchBuilder,
                             ContentLogger contentLogger) {
        this.bundle = bundle;
        this.utils = utils;
        this.linkService = linkService;
        this.productSearchBuilder = productSearchBuilder;
        this.contentLogger = contentLogger;
    }

    /**
     * Builds a VisitScotland enhanced menu from the out of the box menu
     */
    @Cacheable(
            value = "navigation",
            key = "{#request.locale, #hstSiteMenu.name}",
            unless = "#request.getAttribute(\"editMode\")"
    )
    public RootMenuItem buildMenu(HstRequest request, HstSiteMenu hstSiteMenu) {
        List<HstSiteMenuItem> enhancedMenu = new ArrayList<>();
        RootMenuItem root = new RootMenuItem(hstSiteMenu);

        if (hstSiteMenu != null) {
            //Calculate the resource bundle id
            String resourceBundle = NAVIGATION_PREFIX + hstSiteMenu.getName();

            for (HstSiteMenuItem hstItem : hstSiteMenu.getSiteMenuItems()) {
                Object menuItem = getMenuItem(request, hstItem, resourceBundle);
                if (menuItem instanceof MenuItem) {
                    enhancedMenu.add((MenuItem) menuItem);
                }
            }

            root.setSiteMenuItems(enhancedMenu);
        }
        return root;
    }

    /**
     * Creates a new MenuItem that Matches with Bloomreach's MenuItem specification. which enhanced information
     * about the linked item
     *
     * If the item happens to be a widget the  MenuItem is descarded and a Navigation Widget is returned instead
     */
    private Object getMenuItem(HstRequest request, HstSiteMenuItem hstItem, String resourceBundle) {
        MenuItem menuItem = new MenuItem(hstItem);

        //By default, the name would be populated by the resourceBundle
        menuItem.setTitle(bundle.getResourceBundle(resourceBundle, hstItem.getName(), request.getLocale(), true));

        //if document base page or widget, we enhance the document
        if (isDocumentBased(hstItem.getHstLink()) && hstItem.resolveToSiteMapItem() != null) {
            HippoBean bean = utils.getBeanForResolvedSiteMapItem(request, hstItem.resolveToSiteMapItem());
            //if the document does not exist or is not published
            if (bean instanceof Page) {
                createMenuItemFromPage(menuItem, (Page) bean, resourceBundle, request.getLocale());
            } else if (bean != null) {
                return createWidget(request, bean);
            }
        }

        if (menuItem.getTitle() != null) {
            //Process all children
            for (HstSiteMenuItem hstChild : hstItem.getChildMenuItems()) {
                Object childItem = getMenuItem(request, hstChild, resourceBundle);
                if (childItem instanceof MenuItem) {
                    menuItem.addChild((MenuItem)childItem);
                } else if (childItem instanceof  NavigationWidget) {
                    menuItem.setWidget((NavigationWidget) childItem);
                }
            }
            return menuItem;
        } else {
            //Menu Items with no title cannot be displayed, so they are not included in the list of menu Items.
            return null;
        }
    }

    /**
     * Identifies the type of document linked and populated the data on the menu item accordingly
     */
    private NavigationWidget createWidget(HstRequest request, HippoBean bean) {
        if (bean instanceof FeaturedWidget) {
            return addFeatureItem((FeaturedWidget) bean, request);
        } else if (bean instanceof HippoFolder) {
            contentLogger.warn("The following Navigation item '{}' might be unpublished for locale: {}.",
                    bean.getPath(), request.getLocale());
        } else {
            contentLogger.warn("Skipping Unexpected document type: {}, pointing at: {}, for locale: {}. ",
                    bean.getClass().getSimpleName(), bean.getPath(), request.getLocale());
        }

        return null;
    }

    /**
     * Adds a Featured Navigation Widget
     */
    private NavigationWidget addFeatureItem(FeaturedWidget document, HstRequest request) {
        NavigationWidget widget;
        if (document.getItems().size() == 1 && document.getItems().get(0) instanceof ProductsSearch) {
            widget = addFeatureEvent((ProductsSearch) document.getItems().get(0), document, request.getLocale());
        } else {
            List<CMSLink> cmsLinks = new ArrayList<>();
            for (HippoBean item : document.getItems()) {
                if (!(item instanceof CMSLink)) {
                    contentLogger.error("{} found in featured item - expected CMSLink", item.getClass().getSimpleName());
                    return null;
                }
                cmsLinks.add((CMSLink) item);
            }
            return addFeatureItem(cmsLinks, document, request);
        }
        return widget;
    }

    private FeaturedItem addFeatureItem(List<CMSLink> cmsLinks, FeaturedWidget document, HstRequest request) {
        FeaturedItem widget = new FeaturedItem();

        List<EnhancedLink> enhancedLinks = new ArrayList<>();
        for (CMSLink cmsLink : cmsLinks) {
            if (cmsLink.getLink() instanceof Linkable){
                Optional<EnhancedLink> optionalLink = linkService.createEnhancedLink((Linkable) cmsLink.getLink(), widget, request.getLocale(), false);

                if (!optionalLink.isPresent()) {
                    contentLogger.warn("Failed to create widget: {}. Check link is published & valid", document.getPath());
                    continue;
                }
                EnhancedLink link = optionalLink.get();
                link.setCta(bundle.getCtaLabel(cmsLink.getLabel(), request.getLocale()));
                enhancedLinks.add(link);
            } else {
                contentLogger.warn("An incorrect Type of link has been set in a featured item: {}", document.getPath());
            }
        }
        widget.setHippoBean(document);
        widget.setLinks(enhancedLinks);

        if (widget.getErrorMessages() != null ) {
            List<FeaturedItem> listWidget;
            if (request.getModel(WIDGET_LIST) == null) {
                listWidget = new ArrayList<>();
            } else {
                listWidget = request.getModel(WIDGET_LIST);
            }
            listWidget.add(widget);
            request.setModel(WIDGET_LIST, listWidget);
        }
        return widget;
    }

    private FeaturedEvent addFeatureEvent(ProductsSearch productsSearch, FeaturedWidget document, Locale locale) {
        String productSearchApi = productSearchBuilder.fromHippoBean(productsSearch).size(1).locale(locale).buildCannedSearch();
        FeaturedEvent featuredEvent = new FeaturedEvent();
        featuredEvent.setApiUrl(productSearchApi);
        featuredEvent.setHippoBean(document);
        return featuredEvent;
    }

    /**
     * Updates the menu item with enhanced information taken from the labels, or the core document where they link to
     *
     * @param menuItem Menu Item to enhance
     * @param document HippoBean that contains the relevant document that is linked from the header
     * @param bundleId Resource Bundle where the labels of the menu item might come from
     * @param locale   Request Locale
     */
    private void createMenuItemFromPage(MenuItem menuItem, Page document, String bundleId, Locale locale) {
        menuItem.setPage(document);

        //If the menu hasn't been set we use the title coming from the document.
        if (Contract.isEmpty(menuItem.getTitle())) {
            if (!Contract.isEmpty(document.getBreadcrumb())) {
                menuItem.setTitle(document.getBreadcrumb());
            } else {
                menuItem.setTitle(document.getTitle());
            }
        }

        if (bundle.existsResourceBundleKey(bundleId, menuItem.getHstMenuItem().getName() + CTA_SUFFIX, locale)) {
            menuItem.setCta(bundle.getResourceBundle(bundleId, menuItem.getHstMenuItem().getName() + CTA_SUFFIX, locale));
        } else if (menuItem.getTitle() != null) {
            String seeAll = bundle.getResourceBundle(STATIC, "see-all-cta", locale);
            if (seeAll != null) {
                try {
                    menuItem.setCta(String.format(seeAll, menuItem.getTitle()));
                } catch (MissingFormatArgumentException e) {

                    contentLogger.warn("The label '{}' has more parameters than expected. File: {}, key: {}",
                            seeAll, STATIC, "see-all-cta");
                    //After Catching the exception, we can eliminate the parameters.
                    menuItem.setCta(seeAll.replace("%s", ""));
                }
            }
        } else {
            //Title is a mandatory field for all page documents. The following line being executed might mean that the node is corrupted
            logger.error("The node {} does not have a title", document.getPath());
        }
    }

    /**
     * Indicates if the link is based on a document
     */
    private boolean isDocumentBased(HstLink link) {
        return link != null && link.getPath() != null && link.getPath().length() > 0;
    }
}