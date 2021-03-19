package com.visitscotland.brxm.factory;

import com.fasterxml.jackson.databind.JsonNode;
import com.visitscotland.brxm.hippobeans.Image;
import com.visitscotland.brxm.hippobeans.Listicle;
import com.visitscotland.brxm.hippobeans.ListicleItem;
import com.visitscotland.brxm.model.FlatImage;
import com.visitscotland.brxm.model.FlatLink;
import com.visitscotland.brxm.model.ListicleModule;
import com.visitscotland.brxm.dms.DMSDataService;
import com.visitscotland.brxm.dms.DMSUtils;
import com.visitscotland.brxm.mock.ListicleItemMockBuilder;
import com.visitscotland.brxm.services.LinkService;
import com.visitscotland.brxm.services.DocumentUtilsService;
import com.visitscotland.brxm.utils.VsException;
import com.visitscotland.dataobjects.DataType;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static com.visitscotland.brxm.dms.DMSConstants.DMSProduct.LATITUDE;
import static com.visitscotland.brxm.dms.DMSConstants.DMSProduct.LONGITUDE;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListicleFactoryTest {

    @Mock
    LinkService linksService;
    @Mock
    DMSDataService dmsData;
    @Mock
    ImageFactory imageFactory;
    @Mock
    DMSUtils dmsUtils;
    @Mock
    DocumentUtilsService documentUtils;

    @Resource
    @InjectMocks
    ListicleFactory factory;

    @Mock
    Listicle page;

    @BeforeEach
    void initRequest() {
        when(page.getDescOrder()).thenReturn(Boolean.FALSE);
    }


    @Test
    @DisplayName("Create a listicle page")
    void createListiclePage() {
        when(documentUtils.getAllowedDocuments(page, ListicleItem.class)).thenReturn(Collections.emptyList());

        Assertions.assertNotNull(factory.generateItems(Locale.UK, page));
    }

    @Test
    @DisplayName("ListicleItem - Basic Item with no main product")
    void listicle_basic() {

        ListicleItem item = new ListicleItemMockBuilder().title("Title").sutitle("Edinburgh").addDescription().extraLink().build();
        FlatLink link = new FlatLink();

        when(documentUtils.getAllowedDocuments(page, ListicleItem.class)).thenReturn(Collections.singletonList(item));
        when(linksService.createLink(any(), any())).thenReturn(link);

        List<ListicleModule> items = factory.generateItems(Locale.UK, page);

        Assertions.assertEquals(1, items.size());
        Assertions.assertEquals(1, items.get(0).getIndex());
        Assertions.assertEquals(item, items.get(0).getHippoBean());
        Assertions.assertEquals("Title", items.get(0).getTitle());
        Assertions.assertEquals("Edinburgh", items.get(0).getSubtitle());
        Assertions.assertNotNull(items.get(0).getDescription());
        Assertions.assertEquals(1, items.get(0).getLinks().size());
        Assertions.assertEquals(link, items.get(0).getLinks().get(0));
    }

    @Test
    @DisplayName("ListicleItem from CMSLink")
    void listicle_cmsLink() {
        ListicleItem item = new ListicleItemMockBuilder().addImage().cmsLink().build();
        FlatLink link = new FlatLink();
        FlatImage moduleImage = new FlatImage();

        when(documentUtils.getAllowedDocuments(page, ListicleItem.class)).thenReturn(Collections.singletonList(item));
        when(imageFactory.getImage(eq(item.getListicleItemImage()), any(), any())).thenReturn(moduleImage);
        when(linksService.createLink(any(), any())).thenReturn(link);

        List<ListicleModule> items = factory.generateItems(Locale.UK, page);

        Assertions.assertEquals(1, items.size());
        Assertions.assertEquals(1, items.get(0).getLinks().size());
        Assertions.assertEquals(moduleImage, items.get(0).getImage());
        Assertions.assertEquals(link, items.get(0).getLinks().get(0));
    }

    @Test
    @DisplayName("ListicleItem from CMSLink - When the image is not set on the CMS")
    void listicle_cmsLink_fallbackImage() {
        ListicleItem item = new ListicleItemMockBuilder().cmsLink(true).build();
        FlatLink link = new FlatLink();
        FlatImage heroImage = new FlatImage();

        when(documentUtils.getAllowedDocuments(page, ListicleItem.class)).thenReturn(Collections.singletonList(item));
        when(linksService.createLink(any(), any())).thenReturn(link);
        when(imageFactory.getImage(any(Image.class), any(), any())).thenReturn(heroImage);

        List<ListicleModule> items = factory.generateItems(Locale.UK, page);

        Assertions.assertEquals(1, items.size());
        Assertions.assertEquals(1, items.get(0).getLinks().size());

        Assertions.assertNull(item.getListicleItemImage());
        Assertions.assertEquals(heroImage, items.get(0).getImage());
    }

    @Test
    @DisplayName("ListicleItem from DMSLink")
    void listicle_dmsLink() {
        ListicleItem item = new ListicleItemMockBuilder().addImage().sutitle("Subtitle").dmsLink("1234").build();
        FlatLink link = new FlatLink();
        FlatImage moduleImage = new FlatImage();
        JsonNode node = mock(JsonNode.class);
        List<DataType> facilities = Collections.emptyList();

        when(node.has(LATITUDE)).thenReturn(false);

        when(documentUtils.getAllowedDocuments(page, ListicleItem.class)).thenReturn(Collections.singletonList(item));
        when(imageFactory.getImage(any(Image.class), any(), any())).thenReturn(moduleImage);
        when(dmsData.productCard("1234", Locale.UK)).thenReturn(node);
        when(linksService.createLink(any(), any())).thenReturn(link);
        when(dmsUtils.getKeyFacilities(node)).thenReturn(facilities);

        List<ListicleModule> items = factory.generateItems(Locale.UK, page);

        Assertions.assertEquals(1, items.size());
        ListicleModule module = items.get(0);

        Assertions.assertEquals("Subtitle", module.getSubtitle());
        Assertions.assertEquals(moduleImage, module.getImage());
        Assertions.assertEquals(1, module.getLinks().size());
        Assertions.assertSame(facilities, module.getFacilities());

        Assertions.assertEquals(link, module.getLinks().get(0));
    }

    @Test
    @DisplayName("ListicleItem from DMSLink - Subtitle & Image take fallback values from the DMS")
    void listicle_dmsLink_fallback() {
        ListicleItem item = new ListicleItemMockBuilder().dmsLink("1234").build();
        FlatImage dmsImage = new FlatImage();
        dmsImage.setLocation("Location");
        JsonNode node = mock(JsonNode.class, withSettings().defaultAnswer(Answers.RETURNS_DEEP_STUBS));

        when(documentUtils.getAllowedDocuments(page, ListicleItem.class)).thenReturn(Collections.singletonList(item));
        when(imageFactory.createImage(any(JsonNode.class), any())).thenReturn(dmsImage);
        when(dmsData.productCard("1234", Locale.UK)).thenReturn(node);

        ListicleModule module = factory.generateItems(Locale.UK, page).get(0);

        Assertions.assertEquals("Location", module.getSubtitle());
        Assertions.assertEquals(dmsImage, module.getImage());
    }

    @Test
    @DisplayName("ListicleItem from DMSLink - Coordinates take fallback values from the DMS")
    void listicle_dmsLink_coordinates() {
        ListicleItem item = new ListicleItemMockBuilder().addImage().dmsLink("1234").build();
        FlatImage moduleImage = new FlatImage();
        moduleImage.setCmsImage((Image) item.getListicleItemImage());
        JsonNode node = mock(JsonNode.class, withSettings().defaultAnswer(Answers.RETURNS_DEEP_STUBS));

        when(node.has(LATITUDE)).thenReturn(true);
        when(node.get(LATITUDE).asDouble()).thenReturn(12.);
        when(node.get(LONGITUDE).asDouble()).thenReturn(-2.1);

        when(documentUtils.getAllowedDocuments(page, ListicleItem.class)).thenReturn(Collections.singletonList(item));
        when(imageFactory.getImage(any(Image.class), any(), any())).thenReturn(moduleImage);
        when(dmsData.productCard("1234", Locale.UK)).thenReturn(node);

        ListicleModule module = factory.generateItems(Locale.UK, page).get(0);

        Assertions.assertEquals(moduleImage, module.getImage());
        Assertions.assertEquals(12., moduleImage.getCoordinates().getLatitude());
        Assertions.assertEquals(-2.1, moduleImage.getCoordinates().getLongitude());
    }

    @Test
    @DisplayName("ListicleItem from DMSLink - No DMS id generates a CMS warning")
    void listicle_dmsLink_noMatch() {
        ListicleItem item = new ListicleItemMockBuilder().dmsLink("1234").build();

        when(documentUtils.getAllowedDocuments(page, ListicleItem.class)).thenReturn(Collections.singletonList(item));
        when(dmsData.productCard("1234", Locale.UK)).thenReturn(null);

        ListicleModule module = factory.generateItems(Locale.UK, page).get(0);

        Assertions.assertEquals(1, module.getErrorMessages().size());
    }


    @Test
    @DisplayName("The items will be shown with descendent order")
    void descendentOrder() {
        List<ListicleItem> listicleItems = Arrays.asList(
                new ListicleItemMockBuilder().build(),
                new ListicleItemMockBuilder().build(),
                new ListicleItemMockBuilder().build()
        );

        when(documentUtils.getAllowedDocuments(page, ListicleItem.class)).thenReturn(listicleItems);

        List<ListicleModule> items = factory.generateItems(Locale.UK, page);

        Assertions.assertEquals(listicleItems.size(), items.size());
        for (int i = items.size() - 1; i > 0; i--) {
            Assertions.assertEquals(i + 1, items.get(i).getIndex());
        }
    }

    @Test
    @DisplayName("The items will be shown with ascendant order")
    void ascendantOrder() {
        List<ListicleItem> listicleItems = Arrays.asList(
                new ListicleItemMockBuilder().build(),
                new ListicleItemMockBuilder().build(),
                new ListicleItemMockBuilder().build()
        );

        when(documentUtils.getAllowedDocuments(page, ListicleItem.class)).thenReturn(listicleItems);

        List<ListicleModule> items = factory.generateItems(Locale.UK, page);

        Assertions.assertEquals(listicleItems.size(), items.size());
        for (int i = 0; i < items.size(); i++) {
            Assertions.assertEquals(i + 1, items.get(i).getIndex());
        }
    }

    @Test
    @DisplayName("The items will be shown with ascendant order")
    @Disabled("To be confirmed")
    void imageOrder() {
        /**
         * TODO
         1 (most important): Location(Subtitle) field
         2. DMS
         3. Location from the image
         */
        throw new VsException("Not implemented yet");

    }


}
