package com.visitscotland.brxm.factory;

import com.visitscotland.brxm.hippobeans.*;
import com.visitscotland.brxm.mock.MegalinksMockBuilder;
import com.visitscotland.brxm.model.FlatLink;
import com.visitscotland.brxm.model.Module;
import com.visitscotland.brxm.model.megalinks.*;
import com.visitscotland.brxm.services.LinkService;
import com.visitscotland.brxm.services.ResourceBundleService;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static org.easymock.EasyMock.anyObject;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class MegalinkFactoryTest {

    public static final String DMS_ID = "0123456798";
    public static final String EXTERNAL_URL = "http://www.fake.site";
    public static final String PLAIN_LINK = "http://www.plain-link.site";
    public static final String PSR_URL = "http://psr.visitscotland.com/info/search?value1&value2";

    public enum LinkType {CMS, DMS, EXTERNAL, PRODUCT_SEARCH}

    @Mock
    ResourceBundleService resourceBundleService;

    @Mock(lenient = true)
    LinkService linkService;

    @Mock
    ImageFactory imageFactory;

    @Resource
    @InjectMocks
    MegalinkFactory factory;

    @BeforeEach
    public void beforeEach() {
        when(linkService.getPlainLink(any(), any(HippoCompound.class), any())).thenReturn(PLAIN_LINK);
    }


    private Megalinks mockMultiImage() {
        Megalinks mega = mock(Megalinks.class, withSettings().lenient());
        MegalinkItem item = mockItem();

        when(mega.getMegalinkItems()).thenReturn(Collections.singletonList(item));

        return mega;
    }

    public MegalinkItem mockItem() {
        return mockItem(false, LinkType.CMS);
    }

    public MegalinkItem mockItem(boolean featured, LinkType type) {
        MegalinkItem item = mock(MegalinkItem.class, withSettings().lenient());

        when(item.getFeature()).thenReturn(featured);
        if (type == LinkType.CMS) {
            when(item.getLinkItem()).thenReturn(mockPage());
        } else {
            SharedLink link = mockSharedLink(type);
            when(item.getLinkItem()).thenReturn(link);
        }

        return item;
    }


    private SharedLink mockSharedLink(LinkType linkType) {
        SharedLink link = mock(SharedLink.class, withSettings().lenient());
        when(link.getImage()).thenReturn(mock(Image.class, withSettings().lenient()));

        switch (linkType) {
            case DMS:
                DMSLink type = mock(DMSLink.class, withSettings().lenient());
                when(type.getProduct()).thenReturn(DMS_ID);
                when(link.getLinkType()).thenReturn(type);
                break;
            case PRODUCT_SEARCH:
                ProductsSearch ps = mock(ProductsSearch.class, withSettings().lenient());
                when(link.getLinkType()).thenReturn(ps);
                break;
            case EXTERNAL:
                ExternalLink external = mock(ExternalLink.class, withSettings().lenient());
                when(external.getLink()).thenReturn(EXTERNAL_URL);
                when(link.getLinkType()).thenReturn(external);
                break;
            default:

        }
        return link;
    }

    private Page mockPage() {
        return mock(Page.class);
    }

    @Test
    @DisplayName("Allowed items are processed and included to the list")
    void megalinkItem_allowedLinkTypes() {
        List<MegalinkItem> items = new MegalinksMockBuilder().addPageLink().addSharedLink().build().getMegalinkItems();
        when(linkService.createEnhancedLink(any(),any(), any(), anyBoolean())).thenReturn(new EnhancedLink());

        assertEquals(2, factory.convertToEnhancedLinks(null, items, Locale.UK, false).size());
    }

    @Test
    @DisplayName("Non Allowed items are removed from the list")
    void megalinkItem_notAllowedLinkTypes() {
        //Test that not allowed types gets skipped without throwing exception
        List<MegalinkItem> items =  new MegalinksMockBuilder().addLink(null).addLink(mock(MegalinkItem.class)).build().getMegalinkItems();
        Module<Megalinks> module = new Module<>();

        assertEquals(0, factory.convertToEnhancedLinks(module, items, Locale.UK, false).size());

        verify(linkService, never()).createEnhancedLink(any(),any(), any(), anyBoolean());
        assertEquals(1, module.getErrorMessages().size());
    }

    @Test
    @DisplayName("VS-3065 -  Non Published Videos are handled correctly")
    void megalinkItem_nonPublishedVideo() {
        //Test that not allowed types gets skipped without throwing exception
        VideoLink unpublishedVideo = mock(VideoLink.class);
        List<MegalinkItem> items =  new MegalinksMockBuilder().addLink(unpublishedVideo).build().getMegalinkItems();
        Module<Megalinks> module = new Module<>();

        when(unpublishedVideo.getPath()).thenReturn("path-to-document");
        when(linkService.createEnhancedLink(null, module, Locale.UK, false)).thenReturn(null);

        assertEquals(0, factory.convertToEnhancedLinks(module, items, Locale.UK, false).size());
    }


    @Test
    @DisplayName("CTA is populated when the ProductItem field is populated")
    void multiImageLayout_optionCTA() {
        //Verifies that when the field ProductItem is populated
        ExternalLink mockLink = mock(ExternalLink.class);
        Megalinks mega = mockMultiImage();
        when(mega.getProductItem()).thenReturn(mockLink);
        when(linkService.createFindOutMoreLink(any(), any(Locale.class), eq(mockLink))).thenReturn(new FlatLink(null, "cta-link", null));

        LinksModule<?> layout = factory.multiImageLayout(mega, Locale.UK);

        assertEquals("cta-link", layout.getCta().getLink());
    }

    @Test
    @DisplayName("Get a horizontal layout")
    void getMegalinkModule_horizontalListLayout() {
        Megalinks mega = new MegalinksMockBuilder().horizontalLayout(7).build();

        LinksModule linkModule = factory.getMegalinkModule(mega,Locale.UK);
        assertEquals("HorizontalListLinksModule", linkModule.getType());
    }

    @Test
    @DisplayName("Get a list layout when horizontal list is selected but there are less than 5 links")
    void getMegalinkModule_horizontalListLayoutNoEnoughItems() {
        Megalinks mega = new MegalinksMockBuilder().horizontalLayout(4).build();

        LinksModule linkModule = factory.getMegalinkModule(mega,Locale.UK);
        assertEquals("ListLinksModule", linkModule.getType());
    }

    @Test
    @DisplayName("VS-1696 get OTYML module overriding the default title")
    void horizontalListLayout_OTYML() {
        OTYML otyml = mock (OTYML.class);

        when(otyml.getTitle()).thenReturn("Other things");
        when(otyml.getIntroduction()).thenReturn(mock(HippoHtml.class));

        HorizontalListLinksModule module= factory.horizontalListLayout(otyml,Locale.UK);
        assertEquals("Other things", module.getTitle());
        assertNotNull(module.getIntroduction());
    }

    @Test
    @DisplayName("VS-1696 get OTYML module, default title")
    void horizontalListLayout_OTYML_defaultLabel() {
        OTYML otyml = mock (OTYML.class);

        when (resourceBundleService.getResourceBundle("otyml", "otyml.title.default", Locale.UK )).thenReturn("otyml");

        HorizontalListLinksModule module= factory.horizontalListLayout(otyml,Locale.UK);
        assertEquals("otyml", module.getTitle());
    }

    @Test
    @DisplayName("Returns a SingleImageLayout when the Single image fields are populated")
    void getSingleImage(){
        Megalinks mega = new MegalinksMockBuilder().singleImageLayout().build();

        LinksModule layout = factory.getMegalinkModule(mega, Locale.UK);

        Assertions.assertEquals("SingleImageLinksModule", layout.getType());

    }

    @ParameterizedTest
    @DisplayName("From 7 items is not Featured any longer")
    @CsvSource({"1,MultiImageLinksModule", "2,MultiImageLinksModule", "6,MultiImageLinksModule", "7,ListLinksModule"})
    void featuredLayoutDependsOnNumberOfItem(Integer links, String expectedModule){
        MegalinksMockBuilder builder = new MegalinksMockBuilder();

        for (int i = 0; i < links; i++){
            builder.addPageLink();
        }

        LinksModule layout = factory.getMegalinkModule(builder.build(), Locale.UK);

        Assertions.assertEquals(expectedModule, layout.getType());
    }

    @ParameterizedTest
    @DisplayName("From 7 items is not Featured any longer")
    @CsvSource({"1,SingleImageLinksModule", "6,SingleImageLinksModule", "7,ListLinksModule"})
    void singleImageLayoutDependsOnNumberOfItem(Integer links, String expectedModule){
        MegalinksMockBuilder builder = new MegalinksMockBuilder();

        for (int i = 0; i < links; i++){
            builder.addPageLink();
        }

        Megalinks mega = builder.build();
        //The following condition won't be used whe the number of items is above 6, But it has to be mocked to
        //verify that it does not affect the output
        lenient().when(mega.getSingleImageModule()).thenReturn(mock(SingleImageModule.class));

        LinksModule layout = factory.getMegalinkModule(mega, Locale.UK);

        Assertions.assertEquals(expectedModule, layout.getType());
    }

    @ParameterizedTest
    @DisplayName("Validate Maximun and minimum number of featured items")
    @CsvSource({"1,1,1", "2,0,0", "3,0,1", "4,1,2","5,1,2", "6,1,2"})
    void createFeaturedLayoutAndCheckItems(Integer total, Integer minItems, Integer maxItems){
        MegalinksMockBuilder min = new MegalinksMockBuilder();
        MegalinksMockBuilder max = new MegalinksMockBuilder();

        for (int i = 0; i < total; i++){
            min.addPageLink().featured(false);
            max.addPageLink().featured(true);
        }

        when(linkService.createEnhancedLink(any(),any(), any(), anyBoolean())).thenReturn(new EnhancedLink());

        Assertions.assertEquals(minItems, ((MultiImageLinksModule) factory.getMegalinkModule(min.build(), Locale.UK)).getFeaturedLinks().size());
        Assertions.assertEquals(maxItems, ((MultiImageLinksModule) factory.getMegalinkModule(max.build(), Locale.UK)).getFeaturedLinks().size());
    }


}
