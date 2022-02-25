package com.visitscotland.brxm.factory;

import com.visitscotland.brxm.components.navigation.MenuComponent;
import com.visitscotland.brxm.components.navigation.MenuItem;
import com.visitscotland.brxm.components.navigation.RootMenuItem;
import com.visitscotland.brxm.factory.NavigationFactory;
import com.visitscotland.brxm.hippobeans.FeaturedWidget;
import com.visitscotland.brxm.hippobeans.Page;
import com.visitscotland.brxm.mock.NavigationMockBuilder;
import com.visitscotland.brxm.services.ResourceBundleService;
import com.visitscotland.brxm.utils.HippoUtilsService;
import org.hippoecm.hst.content.beans.standard.HippoFolder;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.sitemenu.HstSiteMenu;
import org.hippoecm.hst.core.sitemenu.HstSiteMenuItem;
import org.hippoecm.hst.mock.core.component.MockHstRequest;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class NavigationFactoryTest {

    private static final String MENU_ID = "mock";
    private static final String BUNDLE_ID = NavigationFactory.NAVIGATION_PREFIX + MENU_ID;

    private static final Locale LOCALE = Locale.UK;

    @Mock
    HippoUtilsService utils;

    @Mock
    ResourceBundleService bundle;

    @Mock
    HstSiteMenuItem hstMenuItem;

    @InjectMocks
    NavigationFactory factory;

    HstRequest request;

    @BeforeEach
    void setUp() {
        MockHstRequest request = new MockHstRequest();
        request.setLocale(LOCALE);
        this.request = request;
    }

    /**
     *
     * @param menuItem
     */
    private void addMenuToRequest(HstSiteMenuItem menuItem){
        HstSiteMenu model = mock(HstSiteMenu.class);
        when(model.getName()).thenReturn(MENU_ID);
        request.setModel(MenuComponent.MENU, model);
        when(model.getSiteMenuItems()).thenReturn(Collections.singletonList(menuItem));
    }

    public NavigationMockBuilder newMockBuilder(){
        return new NavigationMockBuilder(request, utils, bundle);
    }

    @Test
    @DisplayName("Title can be populated from Labels (Static Texts)")
    void titleFromResourceBundle_butNotCta() {
        //Basic element with no link. Takes the title of the menu from a ResourceBundle but doesn't add a CTA text
        hstMenuItem = newMockBuilder().name("home").build();
        addMenuToRequest(hstMenuItem);

        when(bundle.getResourceBundle(BUNDLE_ID, "home", LOCALE, true)).thenReturn("Home Page");

        RootMenuItem menu = factory.buildMenu(request, request.getModel("menu"));

        Assertions.assertEquals(1, menu.getSiteMenuItems().size());
        Assertions.assertEquals("Home Page", ((MenuItem)menu.getSiteMenuItems().get(0)).getTitle());
        Assertions.assertNull(((MenuItem)menu.getSiteMenuItems().get(0)).getCta());
    }

    @Test
    @DisplayName("Title from a label takes precedence over Page title and breadcrumb")
    void resourceBundleTakesPrecedenceFromDocumentTitle() {
        //Basic element with no link. Takes the title of the menu from a ResourceBundle but doesn't add a CTA text
        Page page = mock(Page.class, withSettings().lenient());
        hstMenuItem = newMockBuilder().name("home").addLink("url", page).breadcrumb("Breadcrumb Title").title("Page Title").build();
        addMenuToRequest(hstMenuItem);

        mockLabels("home", "Resource Bundle Title", null);

        RootMenuItem menu = factory.buildMenu(request, request.getModel("menu"));

        Assertions.assertEquals(1, menu.getSiteMenuItems().size());
        Assertions.assertEquals("Resource Bundle Title", ((MenuItem)menu.getSiteMenuItems().get(0)).getTitle());
    }

    @Test
    @DisplayName("Page BreadCrumb from a label takes precedence over Page title")
    void titleFromBreadCrumbs() {
        //Basic element with no link. Takes the title of the menu from a ResourceBundle but doesn't add a CTA text
        Page page = mock(Page.class, withSettings().lenient());
        hstMenuItem = newMockBuilder().name("home").addLink("url", page).breadcrumb("Breadcrumb Title").title("Page Title").build();
        addMenuToRequest(hstMenuItem);

        RootMenuItem menu = factory.buildMenu(request, request.getModel("menu"));

        Assertions.assertEquals(1, menu.getSiteMenuItems().size());
        Assertions.assertEquals("Breadcrumb Title", ((MenuItem)menu.getSiteMenuItems().get(0)).getTitle());
    }

    @Test
    @DisplayName("Non existing or non published documents would not appear in the Navigation in the live version")
    void notExisting_or_notPublishedDocument() {
        //The document is not available (main reason: is that it is not published)
        hstMenuItem = newMockBuilder().addLink("url", null).build();
        addMenuToRequest(hstMenuItem);

        RootMenuItem menu = factory.buildMenu(request, request.getModel("menu"));

        Assertions.assertEquals(0, menu.getSiteMenuItems().size());
    }

    @Test
    @DisplayName("When the link Resolves in a non valid page or link, It disappears from the navigation")
    void existingFolderWithNoContentDocument() {
        //The path resolves a folder but not a document (main reason: content document was renamed or deleted)
        hstMenuItem = newMockBuilder().addLink("url", mock(HippoFolder.class)).build();
        addMenuToRequest(hstMenuItem);

        RootMenuItem menu = factory.buildMenu(request, request.getModel("menu"));

        Assertions.assertEquals(0, menu.getSiteMenuItems().size());
    }


    @Test
    @DisplayName("Pages with no title would disappear from the navigation")
    void documentWithNoTitle_skippedFromRender() {
        //When a document has a link but it has no title and no resource bundle. It gets dropped from the list
        // On the current specification the field title is mandatory. So, this is more defensive programming rather than a real case
        hstMenuItem = newMockBuilder().name("home").addMockLink().build();
        addMenuToRequest(hstMenuItem);

        mockLabels("home",null, null);

        RootMenuItem menu = factory.buildMenu(request, request.getModel("menu"));

        Assertions.assertEquals(0, menu.getSiteMenuItems().size());
    }

    @Test
    @DisplayName("Cta Text (See all link) gets a default link text from a label")
    void populateCtaText_defaultValue() {
        //The cta gets the text defaulted to CMS configuration
        hstMenuItem = newMockBuilder().name("home").addMockLink().title("Cities").build();
        addMenuToRequest(hstMenuItem);

        mockLabels("home", null, null);
        when(bundle.getResourceBundle(NavigationFactory.STATIC, "see-all-cta", request.getLocale())).thenReturn("See all %s");

        RootMenuItem menu = factory.buildMenu(request, request.getModel("menu"));

        Assertions.assertEquals(1, menu.getSiteMenuItems().size());
        Assertions.assertEquals("See all Cities", ((MenuItem)menu.getSiteMenuItems().get(0)).getCta());
    }

    @Test
    @DisplayName("Cta Text can be overridden")
    void populateCtaText() {
        //The cta gets populated with the resourceBundle information
        hstMenuItem = newMockBuilder().name("home").addMockLink().build();
        addMenuToRequest(hstMenuItem);

        mockLabels("home", "Home Page", "See all");

        RootMenuItem menu = factory.buildMenu(request, request.getModel("menu"));

        Assertions.assertEquals(1, menu.getSiteMenuItems().size());
        Assertions.assertEquals("See all", ((MenuItem)menu.getSiteMenuItems().get(0)).getCta());
    }

    @ParameterizedTest
    @ValueSource(strings = {"See all", "See all%s%s%s", "%sSee all%s"})
    @DisplayName("Cta Text can handle unexpected text configurations")
    void populateCtaText_unexpectedConfiguration(String label) {
        //ResourceBundle does not throw an exception when the text is not specified correctly
        hstMenuItem = newMockBuilder().name("home").addMockLink().build();
        addMenuToRequest(hstMenuItem);

        mockLabels("home","Pages", null);
        when(bundle.getResourceBundle(NavigationFactory.STATIC, "see-all-cta", request.getLocale())).thenReturn(label);

        RootMenuItem menu = factory.buildMenu(request, request.getModel("menu"));

        Assertions.assertEquals(1, menu.getSiteMenuItems().size());
        Assertions.assertEquals("See all", ((MenuItem)menu.getSiteMenuItems().get(0)).getCta());
    }

    private void mockLabels(String nodeId, String title, String cta){
        when(bundle.getResourceBundle(BUNDLE_ID, nodeId, LOCALE, true)).thenReturn(title);
        if (cta == null) {
            when(bundle.existsResourceBundleKey(BUNDLE_ID, nodeId + ".cta", LOCALE)).thenReturn(false);
        } else {
            when(bundle.existsResourceBundleKey(BUNDLE_ID, nodeId + ".cta", LOCALE)).thenReturn(true);
            when(bundle.getResourceBundle(BUNDLE_ID, nodeId + ".cta", LOCALE)).thenReturn(cta);
        }
    }

    @Test
    @DisplayName("Nested Menu items")
    void nestedConfiguration_complexCase() {
        //Complex Scenario that tests the nested menu items

        HstSiteMenuItem grandChild = newMockBuilder().name("grandchild").addMockLink().build();
        HstSiteMenuItem child = newMockBuilder().name("child").addMockLink().addChildren(grandChild).build();
        HstSiteMenuItem home = newMockBuilder().name("home").addMockLink().addChildren(child).build();

        mockLabels("home", "I'm the granny", "See my family");
        mockLabels("child", "Still a Parent", "See my children");
        mockLabels("grandchild", "Leaf menu item","Too young!");

        addMenuToRequest(home);

        RootMenuItem menu = factory.buildMenu(request, request.getModel("menu"));

        Assertions.assertEquals(1, menu.getSiteMenuItems().size());
        Assertions.assertEquals("home", (menu.getSiteMenuItems().get(0)).getName());
        Assertions.assertEquals("I'm the granny", ((MenuItem)menu.getSiteMenuItems().get(0)).getTitle());
        Assertions.assertEquals("See my family", ((MenuItem)menu.getSiteMenuItems().get(0)).getCta());

        Assertions.assertEquals(1, (menu.getSiteMenuItems().get(0)).getChildMenuItems().size());
        MenuItem aux = (MenuItem) (menu.getSiteMenuItems().get(0)).getChildMenuItems().get(0);
        Assertions.assertEquals("child", aux.getName());
        Assertions.assertEquals("Still a Parent", aux.getTitle());
        Assertions.assertEquals("See my children", aux.getCta());

        Assertions.assertEquals(1, aux.getChildMenuItems().size());
        aux = (MenuItem) aux.getChildMenuItems().get(0);
        Assertions.assertEquals("grandchild", aux.getName());
        Assertions.assertEquals("Leaf menu item", aux.getTitle());
        Assertions.assertEquals("Too young!", aux.getCta());
    }

    @Test
    @DisplayName("Widget Items are being processed")
    void nestedConfiguration_widget() {
        //Widget cannot exist in level 0 (Root) or 1 (top Level Menu Item)

        HstSiteMenuItem grandChild = newMockBuilder().name("grandchild").addLink("mock", mock(FeaturedWidget.class)).build();
        HstSiteMenuItem child = newMockBuilder().name("topLevel").addMockLink().addChildren(grandChild).build();
        HstSiteMenuItem home = newMockBuilder().name("root").addMockLink().addChildren(child).build();

        mockLabels("root", "root Item", "cta");
        mockLabels("topLevel", "Inspiration", "cta");

        addMenuToRequest(home);

        RootMenuItem menu = factory.buildMenu(request, request.getModel("menu"));

        MenuItem topLevelItem = (MenuItem) (menu.getSiteMenuItems().get(0)).getChildMenuItems().get(0);
        Assertions.assertNotNull(topLevelItem.getWidget());
    }

    @Test
    @DisplayName("addLocalizedURLs")
    @Disabled
    void addLocalizedURLs(){
        assertFalse(true, "The method addLocalizedURLs doesn't have any coverage");
    }


}
