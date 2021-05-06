package com.visitscotland.brxm.factory;

import com.visitscotland.brxm.hippobeans.MegalinkItem;
import com.visitscotland.brxm.hippobeans.MegalinkItemMockService;
import com.visitscotland.brxm.hippobeans.Megalinks;
import com.visitscotland.brxm.hippobeans.MegalinksMockService;
import com.visitscotland.brxm.dms.model.LocationObject;
import com.visitscotland.brxm.model.megalinks.LinksModule;
import com.visitscotland.brxm.model.megalinks.MultiImageLinksModule;
import com.visitscotland.brxm.model.megalinks.SingleImageLinksModule;
import com.visitscotland.brxm.dms.DMSDataService;
import com.visitscotland.brxm.dms.DMSProxy;
import com.visitscotland.brxm.dms.LocationLoader;
import com.visitscotland.brxm.services.CommonUtilsService;
import com.visitscotland.brxm.services.DocumentUtilsService;
import com.visitscotland.brxm.services.LinkService;
import com.visitscotland.brxm.services.ResourceBundleService;
import com.visitscotland.brxm.utils.HippoUtilsService;
import com.visitscotland.brxm.utils.Properties;
import org.easymock.EasyMockSupport;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Locale;

import static org.easymock.EasyMock.*;


class LinkModulesFactoryLegacyTest extends EasyMockSupport {

    private final String TITLE = "Megalink title";
    
    //These constants must not generate the name from the class since Freemarker is not aware of them so any change would break the template
    private final String LIST = "ListLinksModule";
    private final String FEATURED = "MultiImageLinksModule";
    private final String SINGLE_IMAGE = "SingleImageLinksModule";

    private static MegalinksMockService megalinkService;
    private static MegalinkItemMockService megalinkItemService;

    private LinkModulesFactory factory;
    private HippoUtilsService utils;
    private LinkService linkService;
    private DMSDataService dms;
    private Properties properties;
    private LocationLoader locationloader;
    private ImageFactory imageFactory;
    private CommonUtilsService commonUtils;
    private CommonUtilsService documUtilsServiceUtils;


    /**
     * {@code factory} needs a static method (createUrl) to be mocked since it relies on a static BloomReach dependency
     *
     * {@code page} represent a dummy link.
     */
    @BeforeAll
    static void init() {
        megalinkService = new MegalinksMockService();
        megalinkItemService = new MegalinkItemMockService();
    }

    @BeforeEach
    void initFactory(){
        properties = mock(Properties.class);
        utils = createNiceMock(HippoUtilsService.class);
        dms = new DMSDataService(new DMSProxy(properties));

        ResourceBundleService rs = createNiceMock(ResourceBundleService.class);
        utils = createNiceMock(HippoUtilsService.class);
        linkService = new LinkService(dms, rs,utils, properties);
        commonUtils = new CommonUtilsService();

        expect(utils.createUrl(anyObject(HippoBean.class))).andStubReturn("/fake-url/mock");

        factory = partialMockBuilder(LinkModulesFactory.class)
                .withConstructor(HippoUtilsService.class,DMSDataService.class, LinkService.class, ResourceBundleService.class, LocationLoader.class, ImageFactory.class, CommonUtilsService.class, DocumentUtilsService.class)
                .withArgs(utils, dms, linkService, rs, locationloader, imageFactory, commonUtils, documUtilsServiceUtils)
                .addMockedMethod("getLocation", String.class, Locale.class)
                .createMock();

        expect(factory.getLocation(anyString(), anyObject(Locale.class))).andStubReturn(null);
    }

    @Test
    void getListLayout(){
        Megalinks mega = megalinkService.createMock(TITLE, false, true, true, 0);
        LinksModule layout = factory.getMegalinkModule(mega, Locale.UK);
        replayAll();

        verifyAll();
        Assertions.assertEquals(layout.getType(), LIST);
    }

    @Test
    void getListLayoutOn7OrMoreItems(){
        replayAll();

        //6 elements => Featured Layout
        Megalinks mega = megalinkService.createMock(TITLE, false, false, true, LinkModulesFactory.MAX_ITEMS);
        LinksModule layout = factory.getMegalinkModule(mega, Locale.UK);

        verifyAll();
        Assertions.assertNotEquals(layout.getType(), LIST);

        // 7 elements => Convert to List Layout
        mega = megalinkService.createMock(TITLE, false, false, true, LinkModulesFactory.MAX_ITEMS + 1);
        layout = factory.getMegalinkModule(mega, Locale.UK);

        verifyAll();
        Assertions.assertEquals(layout.getType(), LIST);
    }

    @Test
    void getSingleImage(){
        replayAll();

        Megalinks mega = megalinkService.createMock(TITLE, false, false, true, 0, "Single image title");
        LinksModule layout = factory.getMegalinkModule(mega, Locale.UK);

        verifyAll();
        Assertions.assertEquals(layout.getType(), SINGLE_IMAGE);
    }

    @Test
    void getSingleImageWithCoordinates(){
        LocationObject location = new LocationObject("edinburgh","key", "Edinburgh","DISTRICT", 10.,-10., Collections.emptyList(), Collections.emptySet());

        reset(factory);
        expect(factory.getLocation(anyString(), anyObject(Locale.class))).andReturn(location);
        replayAll();

        Megalinks mega = megalinkService.createMock(TITLE, false, false, true, 0, "Single image title");
        LinksModule layout = factory.getMegalinkModule(mega, Locale.UK);

        verifyAll();
        Assertions.assertEquals(layout.getType(), SINGLE_IMAGE);
        Assertions.assertNotNull(((SingleImageLinksModule)layout).getImage().getCoordinates());
    }

    @Test
    void getListLayoutWhenSingleImageLinksIsHigherThan6(){
        replayAll();

        Megalinks mega = megalinkService.createMock(TITLE, false, false, true, LinkModulesFactory.MAX_ITEMS + 1, "List Layout Title");
        LinksModule layout = factory.getMegalinkModule(mega, Locale.UK);

        verifyAll();
        Assertions.assertEquals(LIST, layout.getType());
    }

    @Test
    void getFeatured(){
        replayAll();

        for (int i= 0; i <= LinkModulesFactory.MAX_ITEMS; i++) {
            Megalinks mega = megalinkService.createMock(TITLE, false, false, true, i);
            LinksModule layout = factory.getMegalinkModule(mega, Locale.UK);
            Assertions.assertEquals(FEATURED, layout.getType());
        }

        verifyAll();
    }

    @Test
    void countFeaturedItems(){
        //Test the maximum and minimum number of featured items per the amount of module
        replayAll();

        createFeaturedLayoutAndCheckItems(1,1,1);
        createFeaturedLayoutAndCheckItems(2,0,0);
        createFeaturedLayoutAndCheckItems(3,0,1);
        createFeaturedLayoutAndCheckItems(4,1,2);
        createFeaturedLayoutAndCheckItems(5,1,2);
        createFeaturedLayoutAndCheckItems(6,1,2);

        //Check that from 7 items is not Featured any longer.
        Megalinks mega = megalinkService.createMock(TITLE, false, false, true, 7);
        LinksModule layout = factory.getMegalinkModule(mega, Locale.UK);

        verifyAll();
        Assertions.assertNotEquals(layout.getType(), FEATURED);
    }
    
    private void createFeaturedLayoutAndCheckItems(int total, int minItems, int maxItems){
        Megalinks min = megalinkService.createMock(TITLE, false, false, true, 0);
        Megalinks max = megalinkService.createMock(TITLE, false, false, true, 0);

        for (int i = 0; i < total; i++){
            min.getMegalinkItems().add(megalinkItemService.createMock(false));
            max.getMegalinkItems().add(megalinkItemService.createMock(true));
        }

        verifyAll();
        Assertions.assertEquals(((MultiImageLinksModule) factory.getMegalinkModule(min, Locale.UK)).getFeaturedLinks().size(), minItems);
        Assertions.assertEquals(((MultiImageLinksModule) factory.getMegalinkModule(max, Locale.UK)).getFeaturedLinks().size(), maxItems);
    }

    @Test
    void addValidLinkElements(){
        replayAll();

        MegalinkItem mi = megalinkItemService.createMock(false);

        verifyAll();
        Assertions.assertEquals(1, factory.convertToFlatLinks(Collections.singletonList(mi), null).size());
        Assertions.assertEquals(1, factory.convertToEnhancedLinks(Collections.singletonList(mi), Locale.UK,false).size());
    }

    @Test
    void skipNullLinkElements(){
        MegalinkItem mi = createMock(MegalinkItem.class);

        expect(mi.getFeature()).andReturn(false).anyTimes();
        expect(mi.getLink()).andReturn(null).anyTimes();
        expect(mi.getPath()).andReturn("path/to/node").times(2);

        replayAll();


        Assertions.assertEquals(0, factory.convertToFlatLinks(Collections.singletonList(mi), null).size());
        Assertions.assertEquals(0, factory.convertToEnhancedLinks(Collections.singletonList(mi), Locale.UK,false).size());

        //This verifies that messages were generated and include the problematic node
        verify(mi);
    }

    @Test
    void skipInvalidLinkElements(){

        MegalinkItem mi = createMock(MegalinkItem.class);

        expect(mi.getFeature()).andReturn(false).anyTimes();
        expect(mi.getLink()).andReturn(createNiceMock(Megalinks.class)).anyTimes();
        expect(mi.getPath()).andReturn("path/to/node").times(2);

        replay(mi);

        Assertions.assertEquals(0, factory.convertToFlatLinks(Collections.singletonList(mi), null).size());
        Assertions.assertEquals(0, factory.convertToEnhancedLinks(Collections.singletonList(mi), Locale.UK,false).size());

        //This verifies that messages were generated and include the problematic node
        verify(mi);
    }

}

