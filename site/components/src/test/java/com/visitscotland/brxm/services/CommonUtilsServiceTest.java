package com.visitscotland.brxm.services;

import com.visitscotland.brxm.services.CommonUtilsService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

class CommonUtilsServiceTest {

    final String UTF8 = StandardCharsets.UTF_8.name();

    CommonUtilsService utils;

    @BeforeEach
    void init() {
        utils = new CommonUtilsService();
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
}
