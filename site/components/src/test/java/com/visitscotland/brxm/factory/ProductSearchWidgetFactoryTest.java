package com.visitscotland.brxm.factory;

import com.visitscotland.brxm.dms.DMSConstants;
import com.visitscotland.brxm.dms.LocationLoader;
import com.visitscotland.brxm.dms.PSType;
import com.visitscotland.brxm.dms.model.LocationObject;
import com.visitscotland.brxm.hippobeans.Destination;
import com.visitscotland.brxm.hippobeans.Page;
import com.visitscotland.brxm.model.PSModule;
import com.visitscotland.brxm.services.ResourceBundleService;
import com.visitscotland.brxm.utils.Properties;
import org.hippoecm.hst.mock.core.component.MockHstRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Locale;

import static com.visitscotland.brxm.factory.ProductSearchWidgetFactory.BUNDLE_ID;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductSearchWidgetFactoryTest {

    private static final Locale LOCALE = Locale.UK;

    @Mock
    ResourceBundleService bundle;

    @Mock
    LocationLoader locationLoader;

    @Mock
    Properties properties;

    @InjectMocks
    ProductSearchWidgetFactory factory;

    MockHstRequest request;

    @BeforeEach
    void setUp() {
        MockHstRequest request = new MockHstRequest();
        request.setLocale(LOCALE);
        request.setRequestURI("/");
        this.request = request;
    }

    @Test
    @DisplayName("VS-3084 - ProductSearchWidget - Create a simple module")
    void createBasicModule(){
        when(properties.getDmsHost()).thenReturn("p://dms-host:1234");

        PSModule module = factory.getWidget(request);

        Assertions.assertEquals(PSType.SEE_DO, module.getCategory());
        Assertions.assertEquals("p://dms-host:1234/info/see-do/search-results?", module.getSearchUrl());
        Assertions.assertEquals("p://dms-host:1234", module.getDomain());

    }

    @Test
    @DisplayName("VS-3084 - ProductSearchWidget - Path accommodation is identified and Resource Bundle are coherent")
    void defaultTextForAccommodation(){
        request.setRequestURI("/accommodation");
        when(bundle.getResourceBundle(BUNDLE_ID, "accommodation.title", Locale.UK)).thenReturn("Title");
        when(bundle.getResourceBundle(BUNDLE_ID, "accommodation.description", Locale.UK)).thenReturn("Description");

        PSModule module = factory.getWidget(request);

        Assertions.assertEquals(PSType.ACCOMMODATION, module.getCategory());
        Assertions.assertEquals("Title", module.getTitle());
        Assertions.assertEquals("Description", module.getDescription());
        Assertions.assertTrue( module.getSearchUrl().contains("accommodation"));
    }

    @Test
    @DisplayName("VS-3084 - ProductSearchWidget - Extract the location from the page if the page is a Destination DT")
    void inferLocation(){
        LocationObject location = new LocationObject();
        Destination page = mock(Destination.class);

        request.setModel("document", page);
        when(page.getLocation()).thenReturn("location");
        when(locationLoader.getLocation("location", Locale.UK)).thenReturn(location);

        PSModule module = factory.getWidget(request);

        Assertions.assertEquals(location, module.getLocation());
    }


    @Test
    @DisplayName("VS-3084 - ProductSearchWidget - Identifies the location from one of the parent pages")
    void inferLocationFromParent(){
        LocationObject location = new LocationObject();
        Page page = mock(Page.class, RETURNS_DEEP_STUBS);
        Destination parent = mock(Destination.class);

        request.setModel("document", page);
        when(page.getParentBean().getParentBean().getBean("content")).thenReturn(parent);
        when(parent.getLocation()).thenReturn("location");
        when(locationLoader.getLocation("location", Locale.UK)).thenReturn(location);

        PSModule module = factory.getWidget(request);

        Assertions.assertEquals(location, module.getLocation());
    }

    @ParameterizedTest
    @ValueSource(strings = {"accommodation", "/accommodation", "/destinations-maps/edinburgh/accommodation", "x/accommodation/y/z", "see-do/accommodation", "accommodation/see-do"})
    @DisplayName("VS-3084 - ProductSearchWidget - Detect valid URLs related to accommodation")
    void validAccommodation(String uri){
        Assertions.assertEquals(PSType.ACCOMMODATION, PSType.getType(uri));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "/", "/see-do", "/accommodation-map/", "/accomodation", "accommo/dation", "/event", "/#accomodation", "/x/?accommodation"})
    @DisplayName("VS-3084 - ProductSearchWidget - Default all non-recognized URLs to see-do")
    void defaultTextCategory(String uri){
        Assertions.assertEquals(PSType.SEE_DO, PSType.getType(uri));
    }
}
