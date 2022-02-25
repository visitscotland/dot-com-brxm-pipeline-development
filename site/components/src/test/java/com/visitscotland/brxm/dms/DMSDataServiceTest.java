package com.visitscotland.brxm.dms;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.Locale;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DMSDataServiceTest {

    DMSDataService service;

    @Mock
    DMSProxy proxy;    
    
    @BeforeEach
    void init (){
        service = new DMSDataService(proxy);
    }

    @Test
    @DisplayName("productCard - Typical request returns a node")
    void productCard_productCard() throws IOException {
        //Regular scenario: ID and locale are defined and a request is passed to vs-dms-products with both values
        ArgumentCaptor<String> url = ArgumentCaptor.forClass(String.class);
        when(proxy.request(anyString())).thenReturn("{ \"data\":[]}");

        JsonNode node = service.productCard("0123456789", Locale.UK);

        verify(proxy).request(url.capture());

        Assertions.assertTrue(url.getValue().contains("id=0123456789"));
        Assertions.assertTrue(url.getValue().contains("locale="+Locale.UK.getLanguage()));
        Assertions.assertNotNull(node);
    }

    @Test
    @DisplayName("productCard - When a locale is not provide the request doesn't contain the parameter")
    void productCard_noLocale() throws IOException{
        //When locale is not defined the parameter is not set
        //Regular scenario: ID and locale are defined and a request is passed to vs-dms-products with both values
        ArgumentCaptor<String> url = ArgumentCaptor.forClass(String.class);
        when(proxy.request(anyString())).thenReturn("{ \"data\":[]}");

        JsonNode node = service.productCard("0123456789", null);

        verify(proxy).request(url.capture());

        Assertions.assertTrue(url.getValue().contains("id=0123456789"));
        Assertions.assertFalse(url.getValue().contains("locale="));
        Assertions.assertNotNull(node);
    }

    @Test
    @DisplayName("productCard - No product Id return a null")
    void productCard_noProductsId () throws IOException {
        //When a productId is not defined a null object is returned
        JsonNode node = service.productCard(null, Locale.UK);

        verify(proxy, never()).request(anyString());

        Assertions.assertNull(node);
    }

    @Test
    @DisplayName("legacyMapSeach - Returns the list of featured nodes")
    void legacyMapSeach() throws IOException {
        //Verifies that a typical call would work correctly
        final String SAMPLE_URL = "https://www.visitscotland.com/data/product-search/map?cat=vics&loc=Scotland&locplace=&locprox=0";
        final String SAMPLE_RESPONSE = "{\n" +
                "   \"type\": \"FeatureCollection\",\n" +
                "   \"features\": [{\n" +
                "      \"type\": \"Feature\"\n" +
                "   }]\n" +
                "}";

        ProductSearchBuilder psb = mock(ProductSearchBuilder.class);
        when(psb.buildDataMap()).thenReturn(SAMPLE_URL);
        when(proxy.request(SAMPLE_URL)).thenReturn(SAMPLE_RESPONSE);

        JsonNode output = service.legacyMapSearch(psb);

        Assertions.assertEquals(1, output.size());
        Assertions.assertEquals("Feature", output.get(0).get("type").asText());
    }

    @Test
    @DisplayName("legacyMapSeach - Errors are handled properly")
    void corruptedResponseReturnsNull_errorControl() throws IOException{
        //Tries different errors and verifies that the answer doesn't break the method
        ProductSearchBuilder psb = mock(ProductSearchBuilder.class);
        when(psb.buildDataMap()).thenReturn("URL");

        //No features nodes in the response
        when(proxy.request("URL")).thenReturn("{\"type\": \"FeatureCollection\"}");
        JsonNode node = service.legacyMapSearch(psb);
        Assertions.assertNull(node);

        //Corrupted Response
        when(proxy.request("URL")).thenReturn("{ \"ty");
        JsonNode corruptedNode = service.legacyMapSearch(psb);
        Assertions.assertNull(corruptedNode);
    }
}
