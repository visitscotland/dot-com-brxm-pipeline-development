package com.visitscotland.brxm.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommonUtilsServiceTest {

    final String UTF8 = StandardCharsets.UTF_8.name();

    CommonUtilsService utils;

    @Mock
    HttpURLConnection huc;

    @BeforeEach
    void init() {
        utils = new CommonUtilsService(){
            @Override
            public HttpURLConnection openConnection(String url) {
                return huc;
            }
        };
    }

    @Test
    @DisplayName("buildParamentersUrl - No parameters return an empty String")
    void buildParametersUrl_noParam() {
        String parameters = utils.buildQueryString(new HashMap<String, String>(), UTF8);
        Assertions.assertEquals("", parameters);
    }

    @Test
    @DisplayName("buildParamentersUrl - Tests different configurations of parameters")
    void buildParametersUrl_noValue() {
        Map<String, String> params = new HashMap<>();

        params.put("param1", "value1");
        params.put("param2", "");
        params.put("param3", null);

        String queryParameters = utils.buildQueryString(params, UTF8);

        Assertions.assertTrue(queryParameters.startsWith("?"));
        // Param1 contains a value and it must be printed as 'param1=value1'
        Assertions.assertTrue(queryParameters.contains("param1=value1"));
        // Param2 has an empty value and it must be recognized as 'param2'
        Assertions.assertTrue(queryParameters.contains("param2&") || queryParameters.endsWith("param2"));
        // Param2 has no value and it must be recognized as just 'param3'
        Assertions.assertTrue(!queryParameters.contains("param3="));
        Assertions.assertTrue(queryParameters.contains("param3"));
    }

    @Test
    @DisplayName("buildParamentersUrl - Parameter without parameter name are skipped")
    void buildParametersUrl_noKey() {
        Map<String, String> params = new HashMap<>();

        params.put(null, "value1");
        String queryParameters = utils.buildQueryString(new HashMap<String, String>(), UTF8);

        Assertions.assertEquals("", queryParameters);
    }

    @Test
    @DisplayName("getExternalDocumentSize - happy path")
    void getExternalDocumentSize(){
        when(huc.getContentType()).thenReturn(MediaType.APPLICATION_PDF_VALUE);

        when(huc.getContentLength()).thenReturn(1024*1024*3);
        Assertions.assertEquals("PDF 3MB", utils.getExternalDocumentSize("pdf", Locale.UK));

        when(huc.getContentLength()).thenReturn(1024*512);
        Assertions.assertEquals("PDF 0.5MB", utils.getExternalDocumentSize("pdf", Locale.UK));

        when(huc.getContentLength()).thenReturn(1024*512*201);
        Assertions.assertEquals("PDF 100.5MB", utils.getExternalDocumentSize("pdf", Locale.UK));
    }

    @Test
    @DisplayName("getExternalDocumentSize - Decimal Rounding")
    void getExternalDocumentSize_decimalRounding(){
        when(huc.getContentType()).thenReturn(MediaType.APPLICATION_PDF_VALUE);

        //800Kb File
        when(huc.getContentLength()).thenReturn(1024*800);
        Assertions.assertEquals("PDF 0.8MB", utils.getExternalDocumentSize("/file.pdf", Locale.UK));

        //Almost 10 mb
        when(huc.getContentLength()).thenReturn(1024*1024*10 - 30);
        Assertions.assertEquals("PDF 10MB", utils.getExternalDocumentSize("/file.pdf", Locale.UK));

        //Slightly less than 10 mb
        when(huc.getContentLength()).thenReturn(1024*1024*10 + 30);
        Assertions.assertEquals("PDF 10MB", utils.getExternalDocumentSize("/file.pdf", Locale.UK));
    }

    @Test
    @DisplayName("getExternalDocumentSize - Not allowed types")
    void getExternalDocumentSize_nonAllowedTypes(){
        when(huc.getContentType()).thenReturn("application/msword");
        Assertions.assertNull(utils.getExternalDocumentSize("/file.doc", Locale.UK));

        when(huc.getContentType()).thenReturn("application/epub");
        Assertions.assertNull(utils.getExternalDocumentSize("/file.epub", Locale.UK));
    }

    @Test
    @DisplayName("getExternalDocumentSize - Invalid URL")
    void getExternalDocumentSize_invalidUrl(){
        CommonUtilsService utils = new CommonUtilsService(){
            @Override
            public HttpURLConnection openConnection(String url) throws IOException {
                throw new IOException();
            }
        };

        Assertions.assertNull(utils.getExternalDocumentSize("&invalid-url", Locale.UK));
    }

}
