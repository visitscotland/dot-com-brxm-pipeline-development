package com.visitscotland.brxm.rest;

import com.visitscotland.brxm.services.CommonUtilsService;
import com.visitscotland.brxm.utils.Properties;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InternalResourceTest {

    final String MOCK_RESPONSE = "<html><body><internal-f1>Fragment1</internal-f1><internal-f2>Fragment2</internal-f2></body></html>";

    InternalResource service;

    @Mock
    CommonUtilsService utils;

    @Mock
    Properties properties;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    HttpServletRequest request;

    @BeforeEach
    void init(){
        service = new InternalResource(utils, properties);
    }

    @Test
    @DisplayName("Health Endpoint")
    void health(){
        Assertions.assertEquals(200,service.health().getStatus());
    }

    @Test
    @DisplayName("fragment - Return a fragment from the page")
    void fragment() throws IOException {
        when(utils.requestUrl(Mockito.anyString())).thenReturn(MOCK_RESPONSE);
        Response res = service.fragment(request,"f1",  null,null, null, null);
        assertEquals("Fragment1",res.getEntity().toString());
        assertEquals(200,res.getStatus());
    }

    @Test
    @DisplayName("fragment - Return a No match when the fragment is not found")
    void fragment_noMatch() throws IOException {
        when(utils.requestUrl(Mockito.anyString())).thenReturn(MOCK_RESPONSE);
        Response res = service.fragment(request,"f3", null, null,null, null);
        assertEquals(InternalResource.NO_MATCH,res.getEntity().toString());
        assertEquals(404,res.getStatus());
    }

    @Test
    @DisplayName("fragment - The fragment paramenters are being processed (Except locale)")
    void fragment_buildUrl_parameters() throws IOException {
        //Verifies that all relevant parameters are sent to the internal page
        //Note that locale is handled in a different way
        ArgumentCaptor<Map<String, String>> parametersCaptor = ArgumentCaptor.forClass(Map.class);
        ArgumentCaptor<String> urlCaptor = ArgumentCaptor.forClass(String.class);
        when(utils.requestUrl(urlCaptor.capture())).thenReturn(MOCK_RESPONSE);
        when(utils.buildQueryString(parametersCaptor.capture(), any())).thenReturn("?params");
        when(request.getParameterMap().containsKey("external")).thenReturn(true);

        String fragment = service.fragment(request,"f1", "root-path", "sso", null, null).getEntity().toString();
        assertEquals("Fragment1",fragment);
        assertEquals("true", parametersCaptor.getValue().get("external"));
        assertEquals("sso", parametersCaptor.getValue().get("sso"));
        assertEquals("root-path", parametersCaptor.getValue().get("root-path"));
        assertTrue(urlCaptor.getValue().endsWith("?params"));
    }

    @Test
    @DisplayName("fragment - When there is no parameter the URL does not use them ")
    void fragment_buildUrl_noParameters() throws IOException {
        //Verifies that all relevant parameters are sent to the internal page
        //Note that locale is handled in a different way
        ArgumentCaptor<Map<String, String>> parametersCaptor = ArgumentCaptor.forClass(Map.class);
        ArgumentCaptor<String> urlCaptor = ArgumentCaptor.forClass(String.class);
        when(utils.requestUrl(urlCaptor.capture())).thenReturn(MOCK_RESPONSE);
        when(utils.buildQueryString(parametersCaptor.capture(), any())).thenReturn("?params");

        String fragment = service.fragment(request,"f1", null, null, null, null).getEntity().toString();
        assertEquals("Fragment1",fragment);
        assertNull(parametersCaptor.getValue().get("external"));
        assertNull(parametersCaptor.getValue().get("sso"));
        assertNull(parametersCaptor.getValue().get("root-path"));
    }

    @Test
    @DisplayName("fragment - The fragment locale is being processed")
    void fragment_buildUrl_locale() throws IOException {
        when(properties.getCmsBasePath()).thenReturn("http://localhost:8080");
        ArgumentCaptor<String> urlCaptor = ArgumentCaptor.forClass(String.class);
        when(utils.requestUrl(urlCaptor.capture())).thenReturn(MOCK_RESPONSE);
        when(utils.buildQueryString(any(), any())).thenReturn("");

        service.fragment(request,"f1", null,null,  "es", null).getEntity().toString();
        assertEquals("http://localhost:8080/es/internal", urlCaptor.getValue());
    }

    @Test
    @DisplayName("version - The fragment locale is being processed")
    void fragment_buildUrl_version() {
        ArgumentCaptor<Map<String, String>> parametersCaptor = ArgumentCaptor.forClass(Map.class);
        when(utils.buildQueryString(parametersCaptor.capture(), any())).thenReturn("?params");

        String fragment = service.fragment(request,"f1", null, null, null, "legacy").getEntity().toString();
        assertEquals("legacy", parametersCaptor.getValue().get("version"));
    }

    @Test
    @DisplayName("fragment - An error code 500 is returned when an error happens")
    void fragment_error() throws IOException {
        when(utils.requestUrl(any())).thenThrow(new RuntimeException());

        Response res = service.fragment(request,"f1", null,null, null, null);
        assertEquals(500, res.getStatus());
    }

}
