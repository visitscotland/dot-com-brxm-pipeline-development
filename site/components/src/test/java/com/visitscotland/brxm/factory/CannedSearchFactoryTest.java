package com.visitscotland.brxm.factory;

import com.visitscotland.brxm.config.VsComponentManager;
import com.visitscotland.brxm.dms.DMSConstants;
import com.visitscotland.brxm.dms.DMSDataService;
import com.visitscotland.brxm.dms.DMSProxy;
import com.visitscotland.brxm.dms.ProductSearchBuilder;
import com.visitscotland.brxm.hippobeans.*;
import com.visitscotland.brxm.mock.CannedSearchMockBuilder;
import com.visitscotland.brxm.mock.CannedSearchToursMockBuilder;
import com.visitscotland.brxm.model.*;
import com.visitscotland.brxm.services.LinkService;
import com.visitscotland.brxm.services.ResourceBundleService;
import com.visitscotland.brxm.utils.ContentLogger;
import com.visitscotland.brxm.utils.Properties;
import org.hippoecm.hst.core.container.ComponentManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import vs.ase.dms.ProductTypes;

import java.util.*;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CannedSearchFactoryTest {

    @Mock
    LinkService linkService;

    @Mock
    ResourceBundleService bundle;

    @Mock
    DMSDataService dmsData;

    @Mock
    Properties properties;

    @Mock
    ContentLogger logger;

    @InjectMocks
    CannedSearchFactory factory;

    private static final String BUNDLE_ID = "canned-search";
    private static final String PSR_URL = "canned-search";
    static ProductSearchBuilder psBuilder;

    @BeforeAll
    public static void initContext(){
        ComponentManager context = mock(ComponentManager.class, withSettings().lenient());
        psBuilder = mock(ProductSearchBuilder.class, Answers.RETURNS_SELF);
        when(psBuilder.buildCannedSearch()).thenReturn(PSR_URL);
        when(context.getComponent(ProductSearchBuilder.class)).thenReturn(psBuilder);

        VsComponentManager.setComponentManager(context);
    }

    @DisplayName("VS-2715 - When no cta for list view is provided then default cta should be used")
    @Test
    void whenNoCtaProvided_thenDefaultCtaUsed() {
        when(bundle.getResourceBundle(BUNDLE_ID, "canned-search.listview", Locale.UK))
                .thenReturn("default cta");

        FlatLink flatLink = new FlatLink();
        CannedSearch cannedSearch =  new CannedSearchMockBuilder().criteria(ProductTypes.ACCOMMODATION.getId()).build();

        when(linkService.createFindOutMoreLink(any(), any(), any())).thenReturn(flatLink);
        when(dmsData.cannedSearchHasResults(any())).thenReturn(true);

        CannedSearchModule module = factory.getCannedSearchModule(cannedSearch, Locale.UK);

        Assertions.assertEquals("default cta", module.getViewAllLink().getLabel());
        Assertions.assertNull(module.getCredit());
        Assertions.assertEquals(PSR_URL, module.getCannedSearchEndpoint());
    }

    @DisplayName("When no results, add CMS error")
    @Test
    void noResults() {
        when(bundle.getResourceBundle(BUNDLE_ID, "canned-search.listview", Locale.UK))
                .thenReturn("default cta");

        FlatLink flatLink = new FlatLink();
        CannedSearch cannedSearch =  new CannedSearchMockBuilder().criteria(ProductTypes.ACCOMMODATION.getId()).build();

        when(linkService.createFindOutMoreLink(any(), any(), any())).thenReturn(flatLink);
        when(dmsData.cannedSearchHasResults(any())).thenReturn(false);

        CannedSearchModule module = factory.getCannedSearchModule(cannedSearch, Locale.UK);

        Assertions.assertTrue(module.getErrorMessages().size()>0);
    }

    @DisplayName("VS-2715 - When cta for list view is provided then default cta should be used")
    @Test
    void whenCtaProvided_thenDefaultCtaUsed() {
        FlatLink flatLink = new FlatLink();
        flatLink.setLabel("View all castles");
        CannedSearch cannedSearch =  new CannedSearchMockBuilder().criteria(ProductTypes.ACCOMMODATION.getId()).build();

        when(linkService.createFindOutMoreLink(any(), any(), any())).thenReturn(flatLink);

        CannedSearchModule module = factory.getCannedSearchModule(cannedSearch, Locale.UK);

        Assertions.assertEquals("View all castles", module.getViewAllLink().getLabel());
        Assertions.assertEquals(PSR_URL, module.getCannedSearchEndpoint());
    }

    @DisplayName("VS-2715 - When the product type is an even the credit must be displayed")
    @Test
    void whenCreditProvided() {
        when(bundle.getResourceBundle(BUNDLE_ID, "canned-search.listview", Locale.UK))
                .thenReturn("default cta");

        when(bundle.getResourceBundle(BUNDLE_ID, "canned-search.credit-events", Locale.UK))
                .thenReturn("credit");

        FlatLink flatLink = new FlatLink();
        CannedSearch cannedSearch =  new CannedSearchMockBuilder().criteria(ProductTypes.EVENT.getId()).build();

        when((linkService).createFindOutMoreLink(any(), any(), any())).thenReturn(flatLink);

        CannedSearchModule module = factory.getCannedSearchModule(cannedSearch, Locale.UK);

        Assertions.assertEquals("default cta", module.getViewAllLink().getLabel());
        Assertions.assertEquals("credit", module.getCredit());
        Assertions.assertEquals(PSR_URL, module.getCannedSearchEndpoint());
    }

    @DisplayName("Canned search tours")
    @Test
    void cannedSearchTours() {
        CannedSearchTours tours = new CannedSearchToursMockBuilder()
                .toursSearch("https://visitscotland.com?prodtypes=tour&origins%5B%5D=edinburgh&when=february&source=tms")
                .title("title").copy("copy").viewAllCta("viewAllCta").build();

        when(linkService.createExternalLink("https://visitscotland.com?prodtypes=tour&origins%5B%5D=edinburgh&when=february&source=tms")).thenReturn(new FlatLink());
        when(properties.getDmsDataPublicHost()).thenReturn("http://dms-host");

        CannedSearchModule module = factory.getCannedSearchToursModule(tours, Locale.UK);

        String expectedDmsUrl = "http://dms-host" + DMSConstants.VS_DMS_CANNED_SEARCH_TOURS + "?prodtypes=tour&origins%5B%5D=edinburgh&when=february&source=tms&locale=en-GB&limit=24";
        Assertions.assertEquals("title", module.getTitle());
        Assertions.assertEquals("copy", module.getDescription().getContent());
        Assertions.assertEquals(expectedDmsUrl, module.getCannedSearchEndpoint());
        Assertions.assertEquals("viewAllCta", module.getViewAllLink().getLabel());
    }

    @DisplayName("When no CTA override label chosen for tours, then default used instead")
    @Test
    void cannedSearchTorus_defaultCtaLabel() {
        when(bundle.getResourceBundle(BUNDLE_ID, "canned-search.listview", Locale.UK))
                .thenReturn("default cta");

        CannedSearchTours tours = new CannedSearchToursMockBuilder()
                .toursSearch("https://visitscotland.com?prodtypes=tour")
                .title("title").viewAllCta("").build();

        when(linkService.createExternalLink(any())).thenReturn(new FlatLink());
        when(properties.getDmsDataPublicHost()).thenReturn("http://dms-host");

        CannedSearchModule module = factory.getCannedSearchToursModule(tours, Locale.UK);

        Assertions.assertEquals("default cta", module.getViewAllLink().getLabel());
    }

    @DisplayName("If invalid tours search url provided, then null returned")
    @Test
    void cannedSearchTours_badToursSearchUrl() {
        CannedSearchTours tours = new CannedSearchToursMockBuilder()
                .toursSearch("invalid url").build();
        Assertions.assertNull(factory.getCannedSearchToursModule(tours, Locale.UK));
    }

}
