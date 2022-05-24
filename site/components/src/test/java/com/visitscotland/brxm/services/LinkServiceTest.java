package com.visitscotland.brxm.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.visitscotland.brxm.factory.ImageFactory;
import com.visitscotland.brxm.hippobeans.*;
import com.visitscotland.brxm.hippobeans.capabilities.Linkable;
import com.visitscotland.brxm.mock.MegalinksMockBuilder;
import com.visitscotland.brxm.mock.SharedLinkMockBuilder;
import com.visitscotland.brxm.mock.VideoMockBuilder;
import com.visitscotland.brxm.model.FlatImage;
import com.visitscotland.brxm.model.FlatLink;
import com.visitscotland.brxm.model.LinkType;
import com.visitscotland.brxm.config.VsComponentManager;
import com.visitscotland.brxm.dms.DMSDataService;
import com.visitscotland.brxm.dms.ProductSearchBuilder;
import com.visitscotland.brxm.model.Module;
import com.visitscotland.brxm.model.megalinks.EnhancedLink;
import com.visitscotland.brxm.model.YoutubeVideo;
import com.visitscotland.brxm.utils.HippoUtilsService;
import com.visitscotland.brxm.utils.Properties;
import com.visitscotland.brxm.dms.DMSConstants;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.hippoecm.hst.core.container.ComponentManager;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LinkServiceTest {

    @Mock
    private DMSDataService dmsData;

    @Mock(answer = Answers.RETURNS_SELF)
    private ProductSearchBuilder builder;

    @Mock
    private ResourceBundleService resourceBundle;

    @Mock
    private HippoUtilsService utils;

    @Mock
    private Properties properties;

    @Mock
    private CommonUtilsService commonUtils;

    @Mock
    private DocumentUtilsService documentUtilsService;

    @Mock
    private ImageFactory imageFactory;

    @Mock
    private YoutubeApiService youtubeApiService;

    @Resource
    @InjectMocks
    LinkService service;

    private void initProductSearchBuilder() {
        ComponentManager context = mock(ComponentManager.class, withSettings().lenient());
        when(context.getComponent(ProductSearchBuilder.class)).thenReturn(builder);
        VsComponentManager.setComponentManager(context);
    }

    @Test
    @DisplayName("Create a link from an ExternalLink Compound")
    void externalLink() {
        ExternalLink externalLink = mock(ExternalLink.class, withSettings().lenient());
        when(externalLink.getLink()).thenReturn("http://fake.link");
        when(externalLink.getLabel()).thenReturn("");

        when(properties.getDmsHost()).thenReturn("http://localhost:8080");
        when(properties.getDmsHost()).thenReturn("http://localhost:8080");
        when(resourceBundle.getCtaLabel(eq(""), any())).thenReturn("Find out more");

        FlatLink link = service.createFindOutMoreLink(null, Locale.UK, externalLink);

        assertEquals("http://fake.link", link.getLink());
        assertEquals("Find out more", link.getLabel());
        assertEquals(LinkType.EXTERNAL, link.getType());
    }

    @Test
    @DisplayName("Create a url from an SharedLink with an ExternalLink Compound ")
    void getPlainLink_externalLink() {
        ExternalLink externalLink = mock(ExternalLink.class, withSettings().lenient());
        when(externalLink.getLink()).thenReturn("http://fake.link");
        when(externalLink.getLabel()).thenReturn("");

        String link = service.getPlainLink(Locale.UK,externalLink, null);

        assertEquals("http://fake.link", link);
    }

    @Test
    @DisplayName("Create a url from an SharedLink with an External document ")
    void getPlainLink_externalDocument() {
        ExternalDocument externalDocument = mock(ExternalDocument.class, withSettings().lenient());
        when(externalDocument.getLink()).thenReturn("https://www.visitscotland.com/ebrochures/en/what-to-see-and-do/perthshireanddundee.pdf");

        String link = service.getPlainLink(Locale.UK,externalDocument, null);

        assertEquals("https://www.visitscotland.com/ebrochures/en/what-to-see-and-do/perthshireanddundee.pdf", link);
    }

    @Test
    @DisplayName("Create a link from an CMSLink Compound to a page")
    void cmsPageLink() {
        CMSLink cmsLink = mock(CMSLink.class, withSettings().lenient());
        when(cmsLink.getLink()).thenReturn(mock(Page.class));

        when(utils.createUrl(any(Page.class))).thenReturn("http://cms-url");

        FlatLink link = service.createFindOutMoreLink(null, Locale.UK, cmsLink);

        assertEquals("http://cms-url", link.getLink());
        assertEquals(LinkType.INTERNAL, link.getType());
    }

    @Test
    @DisplayName("VS-3206 Create a link from an CMSLink Compound to a shared link, default label")
    void cmsSharedLink_findOutMore() {
        SharedLink sharedLink = mock(SharedLink.class);
        ExternalLink externalLink = mock(ExternalLink.class, withSettings().lenient());
        CMSLink cmsLink = mock(CMSLink.class, withSettings().lenient());

        when(sharedLink.getLinkType()).thenReturn(externalLink);
        when(cmsLink.getLabel()).thenReturn("");
        when(externalLink.getLink()).thenReturn("http://cms-url");
        when(cmsLink.getLink()).thenReturn(sharedLink);
        when(resourceBundle.getCtaLabel(any(), any())).thenReturn("Find out more");


        FlatLink link = service.createFindOutMoreLink(null, Locale.UK, cmsLink);

        assertEquals("http://cms-url", link.getLink());
        assertEquals("Find out more", link.getLabel());
        assertEquals(LinkType.EXTERNAL, link.getType());

    }

    @Test
    @DisplayName("VS-3206 Create a link from an CMSLink Compound to a shared link, default label")
    void cmsPageLink_linkNull() {
        Page pageLink = mock(Page.class);
        Module m = new Module();
        CMSLink cmsLink = mock(CMSLink.class, withSettings().lenient());

        when(cmsLink.getLabel()).thenReturn("");
        when(pageLink.getTitle()).thenReturn("Edinburgh");
        when(cmsLink.getLink()).thenReturn(pageLink);

        FlatLink link = service.createFindOutMoreLink(m, Locale.UK, cmsLink);

        assertNull(link);
        assertTrue(m.getErrorMessages().size()>0);

    }

    @Test
    @DisplayName("VS-3206 Create a link from an CMSLink Compound to a shared link, override label")
    void cmsSharedLink_overrideFindOutMore() {
        SharedLink sharedLink = mock(SharedLink.class);
        ExternalLink externalLink = mock(ExternalLink.class, withSettings().lenient());
        CMSLink cmsLink = mock(CMSLink.class, withSettings().lenient());

        when(sharedLink.getLinkType()).thenReturn(externalLink);
        when(cmsLink.getLabel()).thenReturn("CTA override");
        when(externalLink.getLink()).thenReturn("http://cms-url");
        when(cmsLink.getLink()).thenReturn(sharedLink);
        when(resourceBundle.getCtaLabel(any(), any())).thenReturn("Find out more");


        FlatLink link = service.createFindOutMoreLink(null, Locale.UK, cmsLink);

        assertEquals("http://cms-url", link.getLink());
        assertEquals("CTA override", link.getLabel());
        assertEquals(LinkType.EXTERNAL, link.getType());

    }

    @Test
    @DisplayName("A non existing DMS link doesn't return a link")
    void dmsLink_notExistingProduct() {
        //Verifies that when and DMS item doesn't exist, the link is not created.
        DMSLink dmsLink = mock(DMSLink.class);
        Module m = new Module();
        when(dmsLink.getProduct()).thenReturn("123");

        when(dmsData.productCard("123", Locale.UK)).thenReturn(null);


        FlatLink link = service.createFindOutMoreLink(m, Locale.UK, dmsLink);

        assertTrue(m.getErrorMessages().size() > 1);
        assertNull(link);
    }

    @Test
    @DisplayName("Create a link form a DMSLink Compound")
    void dmsLink() {
        //Verifies that is able to create a link from DMSLink and the url is taken from the JSON Response
        JsonNode node = mock(JsonNode.class,RETURNS_DEEP_STUBS);
        JsonNode url = mock(JsonNode.class);

        DMSLink dmsLink = mock(DMSLink.class);
        when(dmsLink.getProduct()).thenReturn("123");

        when(dmsData.productCard("123", Locale.UK)).thenReturn(node);
        when(node.get(DMSConstants.DMSProduct.URL).get(DMSConstants.DMSProduct.URL_LINK)).thenReturn(url);
        when(url.asText()).thenReturn("/dms-page");

        FlatLink link = service.createFindOutMoreLink(null, Locale.UK, dmsLink);

        assertTrue(link.getLink().endsWith("/dms-page"));
        assertEquals(LinkType.INTERNAL, link.getType());

    }

    @Test
    @DisplayName("Create a link from a ProductSearchLink  Compound")
    void productSearchLink() {
        //Verifies that it can create a URL from the ProductSearchLink
        initProductSearchBuilder();

        ProductSearchLink productSearchLink = mock(ProductSearchLink.class, withSettings().lenient());
        ProductsSearch ps = mock(ProductsSearch.class);
        when(productSearchLink.getSearch()).thenReturn(ps);

        FlatLink link = service.createFindOutMoreLink(null, Locale.UK, productSearchLink);

        verify(builder, times(1)).build();
        assertEquals(LinkType.INTERNAL, link.getType());
    }

    @Test
    @DisplayName("Create a url from an SharedLink with an ProductSearchLink Compound ")
    void getPlainLink_productSearchLink() {
        initProductSearchBuilder();

        ProductSearchLink productSearchLink = mock(ProductSearchLink.class, withSettings().lenient());
        ProductsSearch ps = mock(ProductsSearch.class);
        when(productSearchLink.getSearch()).thenReturn(ps);

        service.getPlainLink(Locale.UK,productSearchLink, null);

        verify(builder, times(1)).build();
    }

    @Test
    @DisplayName("Create a url from an SharedLink with a ProductSearch Compound ")
    void getPlainLink_productSearch() {
        initProductSearchBuilder();

        ProductsSearch productSearch = mock(ProductsSearch.class);

        service.getPlainLink(Locale.UK,productSearch, null);

        verify(builder, times(1)).build();
    }

    @Test
    @DisplayName("Create a link from a ProductSearchLink  Compound")
    void unexpectedLinkReturnsNull() {
        //Tests that the addition of a new type will not introduce an exception
        HippoCompound unrecognizedType = mock(HippoCompound.class);
        assertNull(service.getPlainLink(Locale.UK,unrecognizedType, null));
    }

    @Test
    @DisplayName("Null link doesn't throw an exception and returns null")
    void getType_null() {
        assertNull(service.getType(null));
        assertNull(service.getType(""));
    }

    @ParameterizedTest
    @ValueSource(strings = {"http://www.visitscotland.com/something",
            "http://localhost:8080/site",
            "http://localhost:1234/site",
            "/info/edinburgh-castle-p00001",
            "#htmlID"

    })
    @DisplayName("Identifies internal URL patterns")
    void internalURLs(String url) {
        if (url.startsWith("http")) {
            when(properties.getInternalSites()).thenReturn(Arrays.asList("localhost,www.visitscotland.com".split(",")));
        }
        assertEquals(LinkType.INTERNAL, service.getType(url));
    }

    @ParameterizedTest
    @ValueSource(strings = {"http://www.edinburgh.com/",
            "http://www.gov-uk.com/visitscotland",
            "https://businessevents.visitscotland.com/",
            "https://www.visitscotland.org/",
            "http://www.prize-draw.com/scotland?referral=www.visitscotland.com"
    })
    @DisplayName("Identifies external URL patterns")
    void externalURLs(String url) {
        when(properties.getInternalSites()).thenReturn(Arrays.asList("localhost,www.visitscotland.com".split(",")));
        assertEquals(LinkType.EXTERNAL, service.getType(url));
    }

    @ParameterizedTest
    @ValueSource(strings = {"http://www.visitscotland.com/edinburgh/edinburgh.pdf",
            "http://www.visitscotland.com/edinburgh/edinburgh.PDF",
            "https://edinburgh.com/guide.pdf"
    })
    @DisplayName("Identifies download URLs ")
    void downloadType(String url) {
        assertEquals(LinkType.DOWNLOAD, service.getType(url));
    }

    @Test
    @DisplayName("Return a link from a DMSLink")
    void getPlainLink_dmsLink() {
        //Verifies that when and DMS item doesn't exist, the link is not created.
        DMSLink dmsLink = mock(DMSLink.class);
        JsonNode node = mock(JsonNode.class, RETURNS_DEEP_STUBS);
        JsonNode url = mock(JsonNode.class);

        when(node.get(DMSConstants.DMSProduct.URL).get(DMSConstants.DMSProduct.URL_LINK)).thenReturn(url);
        when(url.asText()).thenReturn("/dms-page");

        String link = service.getPlainLink(Locale.UK,dmsLink, node);

        assertTrue(link.contains("/dms-page"));
    }

    @Test
    @DisplayName("A non existing DMS link doesn't return a link for getPlainLnk")
    void getPlainLink_dmsLink_notExistingProduct() {
        //Verifies that when and DMS item doesn't exist, the link is not created.
        DMSLink dmsLink = mock(DMSLink.class);

        String link = service.getPlainLink(Locale.UK,dmsLink, null);

        assertNull(link);
    }

    @Test
    @DisplayName("Return the category for the link/page")
    void getLinkCategory() {
        when(properties.getDmsHost()).thenReturn("http://localhost:8080");
        when(properties.getInternalSites()).thenReturn(Arrays.asList("www.visitscotland.com,ebooks.visitscotland.com,blog.visitscotland.com".split(",")));

        when(resourceBundle.getResourceBundle("navigation.categories", "ebooks", Locale.UK )).thenReturn("eBooks");
        assertEquals("eBooks", service.getLinkCategory("https://ebooks.visitscotland.com/whisky-distilleries-guides/",Locale.UK));

        String blog = "Travel Blog";
        when(resourceBundle.getResourceBundle("navigation.categories", "travel-blog", Locale.UK )).thenReturn(blog);
        assertEquals(blog, service.getLinkCategory("https://blog.visitscotland.com/discover-our-best-ebooks",Locale.UK));
        assertEquals(blog, service.getLinkCategory("https://www.visitscotland.com/blog/culture/scottish-words-meanings/",Locale.UK));

        String seeDo= "See do";
        when(resourceBundle.getResourceBundle("navigation.categories", "see-do", Locale.UK )).thenReturn(seeDo);
        assertEquals(seeDo, service.getLinkCategory("https://www.visitscotland.com/destinations-maps/edinburgh/see-do/",Locale.UK));
        assertEquals(seeDo, service.getLinkCategory("https://www.visitscotland.com/info/events/developing-a-garden-sketchbook-after-hours-p2216101",Locale.UK));
        assertEquals(seeDo, service.getLinkCategory("https://www.visitscotland.com/info/tours/shore-excursion-from-invergordon-battles-loch-ness-whisky-a56a372f",Locale.UK));
        assertEquals(seeDo, service.getLinkCategory("https://www.visitscotland.com/info/see-do/riverside-museum-p995001",Locale.UK));
        assertEquals(seeDo, service.getLinkCategory("https://www.visitscotland.com/site-search-results",Locale.UK));

        when(resourceBundle.getResourceBundle("navigation.categories", "accommodation", Locale.UK )).thenReturn("Accommodation");
        assertEquals("Accommodation", service.getLinkCategory("https://www.visitscotland.com/destinations-maps/edinburgh/accommodation/self-catering/",Locale.UK));

        String destination = "Places to go";
        when(resourceBundle.getResourceBundle("navigation.categories", "destinations-map", Locale.UK )).thenReturn(destination);
        assertEquals(destination, service.getLinkCategory("https://www.visitscotland.com/destinations-maps/edinburgh/",Locale.UK));
        assertEquals(destination, service.getLinkCategory("https://www.visitscotland.com/destinations-maps/perthshire/short-break-itinerary",Locale.UK));
        assertEquals(destination, service.getLinkCategory("https://www.visitscotland.com/info/towns-villages/ayr-p242821",Locale.UK));
        assertEquals(destination, service.getLinkCategory("https://www.visitscotland.com/destinations-maps/island/orkney",Locale.UK));

        String travel = "Plan your trip";
        when(resourceBundle.getResourceBundle("navigation.categories", "travel-planning", Locale.UK )).thenReturn(travel);
        assertEquals(travel, service.getLinkCategory("https://www.visitscotland.com/holidays-breaks/scotland-life/sam-audrey-scottish-road-trip/",Locale.UK));
        assertEquals(travel, service.getLinkCategory("https://www.visitscotland.com/travel/getting-around-scotland/coach/",Locale.UK));
        assertEquals(travel, service.getLinkCategory("https://www.visitscotland.com/info/transport/turner-hire-drive-edinburgh-p1916901",Locale.UK));

        when(resourceBundle.getResourceBundle("navigation.categories", "inspiration", Locale.UK )).thenReturn("Inspiration");
        assertEquals("Inspiration", service.getLinkCategory("https://www.visitscotland.com/brochures/",Locale.UK));

        String information = "Visitor information";
        when(resourceBundle.getResourceBundle("navigation.categories", "footer.visitor-information", Locale.UK )).thenReturn(information);
        assertEquals(information, service.getLinkCategory("https://www.visitscotland.com/about-us/",Locale.UK));
        assertEquals(information, service.getLinkCategory("https://www.visitscotland.com/info/services/fort-william-icentre-p333001",Locale.UK));
        assertEquals(information, service.getLinkCategory("https://www.visitscotland.com/contact-us/",Locale.UK));
        assertEquals(information, service.getLinkCategory("https://www.visitscotland.com/policies/acceptable-use/",Locale.UK));

        assertEquals("visitwales.com".toUpperCase(), service.getLinkCategory("https://www.visitwales.com/brochures",Locale.UK));

    }

    @Test
    @DisplayName("An exception if the URL is mal formed")
    void getLinkCategory_MalformedURLException(){
        assertNull(service.getLinkCategory("http//example.com",Locale.UK));
    }

    @Test
    @DisplayName("VS-2308 External document definition with category")
    void createEnhancedLink_externalDocument_category() {
        final String url= "https://www.visitscotland.com/ebrochures/en/what-to-see-and-do/perthshireanddundee.pdf";
        final String category= "see-do";
        SharedLink externalDocument = new SharedLinkMockBuilder().externalDocument("title",url,category).build();

        when(commonUtils.getExternalDocumentSize(any(), any())).thenReturn("PDF 15.5MB");
        EnhancedLink enhancedLink = service.createEnhancedLink(externalDocument,null, Locale.UK, true).get();

        assertEquals("title | PDF 15.5MB", enhancedLink.getLabel());
        assertEquals(com.visitscotland.brxm.model.LinkType.DOWNLOAD, enhancedLink.getType());
        assertEquals(category, enhancedLink.getCategory());
    }

    @Test
    @DisplayName("VS-1696 - If size cannot be calculated the link still appears")
    void createEnhancedLink_externalDocument_broken() {
        final String url = "https://www.visitscotland.com/ebrochures/en/what-to-see-and-do/perthshireanddundee.pdf";
        final Module<?> module = new Module<>();

        EnhancedLink enhancedLink = service.createEnhancedLink(
                new SharedLinkMockBuilder().externalDocument("title",url,"see-do").build(), module,
                Locale.UK, true).get();

        assertEquals("title", enhancedLink.getLabel());
        assertTrue(module.getErrorMessages().contains("The Link to the External document might be broken"));
    }

    @Test
    @DisplayName("Itineraries have days and main transport added")
    void createEnhancedLink_itinerary() {
        Itinerary itinerary = new MegalinksMockBuilder().getItinerary("bus");
        when(documentUtilsService.getSiblingDocuments(itinerary,Day.class, "visitscotland:Day")).thenReturn(Arrays.asList(mock(Day.class), mock(Day.class)));
        when(utils.createUrl(itinerary)).thenReturn("URL");

        EnhancedLink enhancedLink = service.createEnhancedLink(itinerary, null, Locale.UK, false).get();

        assertEquals(2, enhancedLink.getItineraryDays());
        assertEquals("bus",enhancedLink.getItineraryTransport());
    }

    @Test
    @DisplayName("VS-2308 External document definition without category")
    void createEnhancedLink_externalDocument() {
        final String url= "https://www.visitscotland.com/ebrochures/en/what-to-see-and-do/perthshireanddundee.pdf";
        SharedLink externalDocument = new SharedLinkMockBuilder().externalDocument("title",url,  null).build();

        when(commonUtils.getExternalDocumentSize(any(), any())).thenReturn("PDF 15.5MB");
        EnhancedLink enhancedLink = service.createEnhancedLink(externalDocument, null, Locale.UK, false).get();

        assertEquals("title | PDF 15.5MB", enhancedLink.getLabel());
        assertEquals(com.visitscotland.brxm.model.LinkType.DOWNLOAD, enhancedLink.getType());
        Mockito.verify((ExternalDocument)externalDocument.getLinkType(),Mockito.never()).getCategory();
    }

    @Test
    @DisplayName("DMSLink - DMS Id is not valid")
    void DMS_enhanced_notValidId(){
        Module<?> module = new Module<>();
        JsonNode node = mock(JsonNode.class, RETURNS_DEEP_STUBS);
        SharedLink dmsLink = new SharedLinkMockBuilder().dmsLink(dmsData, node).build();

        when(node.get(DMSConstants.DMSProduct.URL).get(DMSConstants.DMSProduct.URL_LINK).asText()).thenReturn("mock-url");

        service.createEnhancedLink(dmsLink, module,Locale.UK,false);

        assertTrue( module.getErrorMessages().size() > 0);
    }

    @Test
    @DisplayName("DMSLink - Test that the image is loaded from the DMS")
    void DMS_enhanced_SharedLink_defaultsImage() throws IOException {
        JsonNode node = new ObjectMapper().readTree(MegalinksMockBuilder.MOCK_JSON);
        Module<?> module = new Module<>();

        SharedLink dmsLink = new SharedLinkMockBuilder().dmsLink(dmsData, node).build();
        when(imageFactory.createImage(node, module, Locale.UK)).thenReturn(new FlatImage());

        EnhancedLink link = service.createEnhancedLink(dmsLink, module,Locale.UK,false).get();

        assertNotNull(link.getImage());
    }

    @Test
    @DisplayName("No image throw a warning in preview mode")
    void noImageDefined_DMS_defaultsImageNotFound() throws IOException {
        final String NO_IMAGE_JSON = "{" +
                " \"productLink\": {\"link\": \"/info/fake-product-p0123456798\"}," +
                " \"name\":\"Fake Product\" " +
                "}";
        JsonNode node = new ObjectMapper().readTree(NO_IMAGE_JSON);
        SharedLink dmsLink = new SharedLinkMockBuilder().dmsLink(dmsData, node).build();
        Module<?> module = new Module<>();

        when(properties.getDmsHost()).thenReturn("");

        EnhancedLink link = service.createEnhancedLink(dmsLink, module,Locale.UK,false).get();
        assertEquals("/info/fake-product-p0123456798", link.getLink());
        assertTrue( module.getErrorMessages().size() > 0);
        assertNull(link.getImage());
    }

    @Test
    @DisplayName("getDownloadText returns the label with the size")
    void getDownloadText() {

        when(commonUtils.getExternalDocumentSize(any(), any())).thenReturn("PDF 15.5MB");
        when(utils.getRequestLocale()).thenReturn(Locale.CANADA);

        assertEquals(" | PDF 15.5MB", service.getDownloadText("http://www.visitscotlan.com/pdf"));
    }

    @Test
    @DisplayName("VS-2756 - create an External Link for an English page")
    void createExternalLink(){
        when(properties.getInternalSites()).thenReturn(Collections.singletonList("www.visitscotland.com"));

        assertEquals("/unit-test/", service.createExternalLink(Locale.UK, "/unit-test/",null).getLink());
        assertEquals("/info/accommodation/unit-test/", service.createExternalLink(Locale.UK, "/info/accommodation/unit-test/",null).getLink());
        assertEquals("https://www.visitscotland.com/unit-test/", service.createExternalLink(Locale.UK, "https://www.visitscotland.com/unit-test/",null).getLink());

    }

    @Test
    @DisplayName("VS-2756 - Create an External Link for a lacocalized page from a fully qualified URLs")
    void createExternalLink_languange_fullyqualified(){
        when(properties.getInternalSites()).thenReturn(Arrays.asList("www.visitscotland.com,x.y.z".split(",")));

        assertEquals("https://www.visitscotland.com/fr/unit-test/", service.createExternalLink(Locale.FRANCE, "https://www.visitscotland.com/unit-test/",null).getLink());
        assertEquals("https://www.visitscotland.com/fr/info/accommodation/unit-test/", service.createExternalLink(Locale.FRANCE, "https://www.visitscotland.com/info/accommodation/unit-test/",null).getLink());
        assertEquals("ftp://x.y.z/fr", service.createExternalLink(Locale.FRANCE, "ftp://x.y.z",null).getLink());
        assertEquals("https://www.visitedimburg.com/unit-test/", service.createExternalLink(Locale.FRANCE, "https://www.visitedimburg.com/unit-test/",null).getLink());
    }

    @Test
    @DisplayName("VS-2756 - When the URL is already localized it doesn't add the path language path again")
    void createExternalLink_relativeUrls(){
        when(properties.getInternalSites()).thenReturn(Collections.singletonList("www.visitscotland.com"));
        when(properties.getConvertToRelative()).thenReturn("www.visitscotland.com");

        assertEquals("/unit-test/", service.createExternalLink(Locale.UK, "https://www.visitscotland.com/unit-test/",null).getLink());
        assertEquals("/info/accommodation/unit-test/", service.createExternalLink(Locale.UK, "https://www.visitscotland.com/info/accommodation/unit-test/",null).getLink());
        assertEquals("/fr/unit-test/", service.createExternalLink(Locale.FRANCE, "https://www.visitscotland.com/unit-test/",null).getLink());
    }

    @ParameterizedTest
    @DisplayName("VS-2756 - Some URLs must not be altered")
    @ValueSource(strings = {"en-gb", "es-es", "en-us", "en", "us"})
    void anchorLink(String locale){
        assertEquals("#anchor-link", service.createExternalLink(Locale.forLanguageTag(locale), "#anchor-link",null).getLink());
        assertEquals("https://www.visitedimburgh.com/unit-test/", service.createExternalLink(Locale.forLanguageTag(locale), "https://www.visitedimburgh.com/unit-test/",null).getLink());
        assertEquals("mailto:jcalcines@visitscotland.com", service.createExternalLink(Locale.forLanguageTag(locale), "mailto:jcalcines@visitscotland.com",null).getLink());
        assertEquals("tel:+441311234567", service.createExternalLink(Locale.forLanguageTag(locale), "tel:+441311234567",null).getLink());
    }

    @Test
    @DisplayName("VS-2756 - Create a localized  External Link for a non-existing locale page")
    void createExternalLink_unrecognized_language(){
        when(properties.getInternalSites()).thenReturn(Collections.singletonList("www.visitscotland.com"));

        assertEquals("/unit-test/", service.createExternalLink(Locale.JAPAN, "/unit-test/","Label").getLink());
        assertEquals("/info/accommodation/unit-test/", service.createExternalLink(Locale.JAPAN, "/info/accommodation/unit-test/",null).getLink());
        assertEquals("https://www.visitscotland.com/unit-test/", service.createExternalLink(Locale.JAPAN, "https://www.visitscotland.com/unit-test/",null).getLink());
        assertEquals("#anchor-link", service.createExternalLink(Locale.JAPAN, "#anchor-link",null).getLink());
    }

    @Test
    @DisplayName("VS-2756 - Create a localized External Link for an French page")
    void createExternalLink_languange(){
        assertEquals("/fr/unit-test/", service.createExternalLink(Locale.FRANCE, "/unit-test/","Label").getLink());
        assertEquals("/fr/info/accommodation/unit-test/", service.createExternalLink(Locale.FRANCE, "/info/accommodation/unit-test/",null).getLink());
        assertEquals("#anchor-link", service.createExternalLink(Locale.FRANCE, "#anchor-link",null).getLink());
    }



    @Test
    @DisplayName("VS-2756 - When the URL is already localized it doesn't add the path language path again")
    void createExternalLink_double_localization(){
        when(properties.getInternalSites()).thenReturn(Collections.singletonList("www.visitscotland.com"));

        assertEquals("https://www.visitscotland.com/fr/unit-test/", service.createExternalLink(Locale.FRANCE, "https://www.visitscotland.com/fr/unit-test/",null).getLink());
        assertEquals("https://www.visitscotland.com/fr-fr/info/accommodation/unit-test/", service.createExternalLink(Locale.FRANCE, "https://www.visitscotland.com/fr-fr/info/accommodation/unit-test/",null).getLink());
    }

    @Test
    @DisplayName("Allow shared links to be recognized")
    void createSimpleLink(){
        SharedLink sl = new SharedLinkMockBuilder().externalDocument("title", "doc.pdf", null).build();

        when(properties.getInternalSites()).thenReturn(Collections.singletonList("www.visitscotland.com"));

        FlatLink link = service.createSimpleLink(sl, null, Locale.UK);

        assertEquals("doc.pdf", link.getLink());
        assertEquals("title", link.getLabel());
    }

    //TODO
    @Test
    @DisplayName(("VS-2949 - Create video link to be used by Freemarker"))
    void createVideo(){
        Video video = new VideoMockBuilder().withImage().url("http://youtube.com?v=123")
                .title("Title").teaser("Teaser").label("Enjoy the video").build();

        when(resourceBundle.getVideoCtaLabel(any(), any())).thenReturn("Enjoy the video");
        EnhancedLink link = service.createVideo(video, null, null);

        assertEquals("Title", link.getLabel());
        assertEquals("Teaser", link.getTeaser());
        assertEquals("Enjoy the video", link.getCta());
        assertEquals("http://youtube.com?v=123", link.getLink());
        assertEquals("123", link.getYoutubeId());
        assertEquals(LinkType.VIDEO, link.getType());
    }

    @Test
    @DisplayName(("VS-2935 - Allow videos for Megalinks Items"))
    void enhancedLink_fromVideo(){
        Video video = new VideoMockBuilder().url("youtu.be?v=1").build();

        EnhancedLink link = service.createEnhancedLink(video, null, null, false).get();

        assertNotNull(link);
        assertEquals("youtu.be?v=1", link.getLink());
        assertEquals("1", link.getYoutubeId());
        assertEquals(LinkType.VIDEO, link.getType());
    }

    @Test
    @DisplayName(("VS-2935 - Allow videos for Megalinks Items"))
    void getPlainLink_fromVideo(){
        Video video = new VideoMockBuilder().url("https://www.youtube.com/watch?v=h9bQwcndGfo").build();

        assertEquals("https://www.youtube.com/watch?v=h9bQwcndGfo", service.getPlainLink(Locale.UK, video,null));
        assertEquals("https://www.youtube.com/watch?v=h9bQwcndGfo", service.getPlainLink(Locale.FRANCE, video,null));
    }

    @Test
    @DisplayName(("VS-3065 - Handling of non-published documents"))
    void enhancedLink_empty(){
        Optional<EnhancedLink> link = service.createEnhancedLink(null, null, null, false);

        assertFalse(link.isPresent());
    }

    @Test
    @DisplayName("VS-1419  - YouTube video published date obtained from api")
    void enhancedLink_fromVideoWithPublishedDate() throws ParseException {
        Video video = new VideoMockBuilder().url("https://www.youtube.com/watch?v=h9bQwcndGfo").build();
        Date datePublished = new SimpleDateFormat("yyyy-MM-dd").parse("2020-10-10");
        YoutubeVideo yt = new YoutubeVideo();
        yt.setPublishDate(datePublished);
        when(youtubeApiService.getVideoInfo("h9bQwcndGfo")).thenReturn(Optional.of(yt));

        EnhancedLink link = service.createEnhancedLink(video, null, null, false).get();

        assertEquals(datePublished, link.getPublishedDate());
    }

    @Test
    @DisplayName("VS-1419 Published date is never null, even when YouTube API is unavailable")
    void enhancedLink_youtubeDateNotAvailable() {
        Video video = new VideoMockBuilder().url("https://www.youtube.com/watch?v=h9bQwcndGfo").build();
        when(youtubeApiService.getVideoInfo("h9bQwcndGfo")).thenReturn(Optional.empty());

        EnhancedLink link = service.createEnhancedLink(video, null, null, false).get();

        assertNotNull(link.getPublishedDate());
    }

}
