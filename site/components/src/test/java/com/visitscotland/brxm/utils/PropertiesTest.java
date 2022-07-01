package com.visitscotland.brxm.utils;

import com.visitscotland.brxm.services.ResourceBundleService;
import org.hippoecm.hst.configuration.hosting.Mount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PropertiesTest {

    @Mock
    ResourceBundleService bundle;

    @Mock
    HippoUtilsService utils;

    Properties properties;

    String value;

    @BeforeEach
    void init(){
        properties = new Properties(bundle, utils){

            @Override
            String getEnvironmentVariable(String name) {
                return value;
            }
        };
    }

    @Test
    @DisplayName("Reads an String value from a property")
    void readString(){
        when(bundle.getResourceBundle(Properties.DEFAULT_CONFIG, "string", Locale.UK, true)).thenReturn("http://localhost:8181");
        assertEquals("http://localhost:8181", properties.readString("string"));
    }

    @Test
    @DisplayName("Reads an string value from an environment variable")
    void readString_env(){
        when(bundle.getResourceBundle(Properties.DEFAULT_CONFIG, "string", Locale.UK, true)).thenReturn("$TEST_VS");
        value = "http://dms.visitscotland.com";
        assertEquals("http://dms.visitscotland.com", properties.readString("string"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "$"})
    @NullSource
    @DisplayName("Empty values return an empty String for String properties")
    void readString_env(String value){
        when(bundle.getResourceBundle(Properties.DEFAULT_CONFIG, "string", Locale.UK, true)).thenReturn(value);
        value = "";
        assertEquals("", properties.readString("string"));
    }

    @Test
    @DisplayName("Reads an integer number from a property")
    void readInteger(){
        when(bundle.getResourceBundle(Properties.DEFAULT_CONFIG, Properties.DMS_DATA_TRIES, Locale.UK, true)).thenReturn("3718");
        assertEquals(3718, properties.getDmsTries());
    }

    @Test
    @DisplayName("Can parse Integers from environment variables")
    void readInteger_env(){
        when(bundle.getResourceBundle(Properties.DEFAULT_CONFIG, Properties.DMS_DATA_TRIES, Locale.UK, true)).thenReturn("$TEST_VS");
        value = "3718";
        assertEquals(3718, properties.getDmsTries());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "a", "$"})
    @NullSource
    @DisplayName("Empty and wrong values return 0 for Numeric properties")
    void readInteger_empty(String value){
        when(bundle.getResourceBundle(Properties.DEFAULT_CONFIG, Properties.DMS_DATA_TRIES, Locale.UK, true)).thenReturn(value);
        value = "";
        assertEquals(0, properties.getDmsTries());
    }

    @Test
    @DisplayName("Reads an integer number from a property")
    void readDouble(){
        when(bundle.getResourceBundle(Properties.DEFAULT_CONFIG, Properties.DMS_MAP_DEFAULT_DISTANCE, Locale.UK, true)).thenReturn("3.14");
        assertEquals(3.14, properties.getDmsMapDefaultDistance());
    }

    @Test
    @DisplayName("Can parse Integers from environment variables")
    void readDouble_env(){
        when(bundle.getResourceBundle(Properties.DEFAULT_CONFIG, Properties.DMS_MAP_DEFAULT_DISTANCE, Locale.UK, true)).thenReturn("$TEST_VS");
        value = "3.14";
        assertEquals(3.14, properties.getDmsMapDefaultDistance());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "a", "$"})
    @NullSource
    @DisplayName("Empty and wrong values return 0 for Numeric properties")
    void readDouble_empty(String value){
        when(bundle.getResourceBundle(Properties.DEFAULT_CONFIG, Properties.DMS_MAP_DEFAULT_DISTANCE, Locale.UK, true)).thenReturn(value);
        assertEquals(0.0, properties.getDmsMapDefaultDistance());
    }


    @ParameterizedTest
    @ValueSource(strings = {"true", "TRUE"})
    @DisplayName("Reads true from a property")
    void readBoolean(String value){
        when(bundle.getResourceBundle(Properties.DEFAULT_CONFIG, "boolean", Locale.UK, true)).thenReturn(value);
        assertTrue(properties.readBoolean("boolean"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"false", "", "$", "0", "1", "T"})
    @NullSource
    @DisplayName("false, empty and wrong values return false for Boolean properties")
    void readInteger_false(String value){
        when(bundle.getResourceBundle(Properties.DEFAULT_CONFIG, "boolean", Locale.UK, true)).thenReturn(value);
        assertFalse(properties.readBoolean("boolean"));
    }

    @Test
    @DisplayName("getDmsEncoding - Can parse the value from a String")
    void getDmsEncoding(){
        when(bundle.getResourceBundle(Properties.DEFAULT_CONFIG, Properties.DMS_DATA_ENCODING, Locale.UK, true)).thenReturn("ISO-8859-1");
        assertEquals(StandardCharsets.ISO_8859_1, properties.getDmsEncoding());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "latin","UFT-16"})
    @NullSource
    @DisplayName("getDmsEncoding - non recognized values return UTF-8 encoding")
    void getDmsEncoding_empty(String value){
        when(bundle.getResourceBundle(Properties.DEFAULT_CONFIG, Properties.DMS_DATA_ENCODING, Locale.UK, true)).thenReturn(value);
        assertEquals(StandardCharsets.UTF_8, properties.getDmsEncoding());
    }

    @Test
    @DisplayName("As requested by WebOps, links to vs-dms-products URLs will be relative when use relative urls is active")
    void getDmsHost(){
        when(bundle.getResourceBundle(Properties.DEFAULT_CONFIG, Properties.DMS_HOST, Locale.UK, true)).thenReturn("http://test-dms.visitscotland.com");
        when(bundle.getResourceBundle(Properties.DEFAULT_CONFIG, Properties.USE_RELATIVE_URLS, Locale.UK, true)).thenReturn("false");
        assertEquals("http://test-dms.visitscotland.com", properties.getDmsHost());

        when(bundle.getResourceBundle(Properties.DEFAULT_CONFIG, Properties.USE_RELATIVE_URLS, Locale.UK, true)).thenReturn("true");
        assertEquals("", properties.getDmsHost());
    }

    @Test
    @DisplayName("As requested by WebOps, made up links to the CMS, will be relative when use relative urls is active")
    void getLocalHost(){
        when(bundle.getResourceBundle(Properties.DEFAULT_CONFIG, Properties.CMS_BASE_PATH, Locale.UK, true)).thenReturn("http:/localhost:8080/site");
        when(bundle.getResourceBundle(Properties.DEFAULT_CONFIG, Properties.USE_RELATIVE_URLS, Locale.UK, true)).thenReturn("true");
        assertEquals("", properties.getDmsHost());

        when(bundle.getResourceBundle(Properties.DEFAULT_CONFIG, Properties.USE_RELATIVE_URLS, Locale.UK, true)).thenReturn("false");
        assertEquals("http:/localhost:8080/site", properties.getCmsBasePath());
    }

    @Test
    @DisplayName("getInstagramURL() Composes the token from the app-id and the client-token (PR-383)")
    void getInstagramURL(){

        when(bundle.getResourceBundle(Properties.DEFAULT_CONFIG, Properties.INSTAGRAM_APP_ID, Locale.UK, true)).thenReturn("{app-id}");
        when(bundle.getResourceBundle(Properties.DEFAULT_CONFIG, Properties.INSTAGRAM_ACCESS_TOKEN, Locale.UK, true)).thenReturn("{client-token}");
        assertEquals("{app-id}|{client-token}", properties.getInstagramToken());

        when(bundle.getResourceBundle(Properties.DEFAULT_CONFIG, Properties.INSTAGRAM_ACCESS_TOKEN, Locale.UK, true)).thenReturn("");
        assertEquals("{app-id}", properties.getInstagramToken());
    }

    @Test
    @DisplayName("Read properties from hosts configuration")
    void propertiesFromConfiguration(){
        final String ENV_PROPERTIES = "environment-properties";

        Mount mount = mock(Mount.class);
        when(utils.getResolvedMount(null)).thenReturn(mount);
        when(mount.getProperty("visitscotland:cmsProperties")).thenReturn(ENV_PROPERTIES);

        properties.getInstagramToken();

        Mockito.verify(bundle, atLeastOnce()).getResourceBundle(eq(ENV_PROPERTIES), any(), eq(Locale.UK), eq(true));
    }

    @Test
    @DisplayName("Read properties from hosts configuration for language subsites")
    void propertiesFromParentConfiguration(){
        final String ENV_PROPERTIES = "environment-properties";

        Mount mount = mock(Mount.class);
        Mount parent = mock(Mount.class);

        when(utils.getResolvedMount(null)).thenReturn(mount);
        when(mount.getParent()).thenReturn(parent);
        when(parent.getProperty("visitscotland:cmsProperties")).thenReturn(ENV_PROPERTIES);

        properties.getInstagramToken();

        Mockito.verify(bundle, atLeastOnce()).getResourceBundle(eq(ENV_PROPERTIES), any(), eq(Locale.UK), eq(true));
    }

    @Test
    @DisplayName("VS-2756 - Return InternalSites as a list")
    void getInternalSites(){
        when(bundle.getResourceBundle(Properties.DEFAULT_CONFIG, Properties.INTERNAL_SITES, Locale.UK, true)).thenReturn("  aaa , bbb,,,ccc,");
        List<String> hosts = properties.getInternalSites();

        assertEquals(3,hosts.size());
        assertEquals("aaa",hosts.get(0));
        assertEquals("bbb",hosts.get(1));
        assertEquals("ccc",hosts.get(2));
     }

    @Test
    @DisplayName("VS-3183 - Use default.config as the default properties given by the CMS from the codebase")
    void propertyValues_defaultConfig(){
        when(bundle.getResourceBundle(Properties.DEFAULT_CONFIG, Properties.DMS_DATA_API_KEY, Locale.UK, true)).thenReturn("PROP-VALUE");

        assertEquals("PROP-VALUE", properties.getDmsToken());
    }

    @Test
    @DisplayName("VS-3183 - Override properties when configured in the mount")
    void propertyValues_overrideConfig(){
        final String ENV_PROPERTIES = "environment-properties";

        Mount mount = mock(Mount.class);
        when(utils.getResolvedMount(null)).thenReturn(mount);
        when(mount.getProperty("visitscotland:cmsProperties")).thenReturn(ENV_PROPERTIES);
        when(bundle.getResourceBundle(ENV_PROPERTIES, Properties.DMS_DATA_API_KEY, Locale.UK, true)).thenReturn("OVERRIDE-VALUE");

        //This is not expected to be called but if were, It shouldn't affect the outcome of the method
        lenient().when(bundle.getResourceBundle(Properties.DEFAULT_CONFIG, Properties.DMS_DATA_API_KEY, Locale.UK, true)).thenReturn("DEFAULT-VALUE");

        assertEquals("OVERRIDE-VALUE", properties.getDmsToken());
    }

    @Test
    @DisplayName("VS-3183 - If the property is not found in the file defined in the mount, it takes the value from default.config")
    void propertyValues_overrideConfig_defaultValue(){
        final String ENV_PROPERTIES = "environment-properties";

        Mount mount = mock(Mount.class);
        when(utils.getResolvedMount(null)).thenReturn(mount);
        when(mount.getProperty("visitscotland:cmsProperties")).thenReturn(ENV_PROPERTIES);
        when(bundle.getResourceBundle(ENV_PROPERTIES, Properties.DMS_DATA_API_KEY, Locale.UK, true)).thenReturn(null);

        //This is not expected to be called but if were, It shouldn't affect the outcome of the method
        when(bundle.getResourceBundle(Properties.DEFAULT_CONFIG, Properties.DMS_DATA_API_KEY, Locale.UK)).thenReturn("DEFAULT-VALUE");

        assertEquals("DEFAULT-VALUE", properties.getDmsToken());
    }


}