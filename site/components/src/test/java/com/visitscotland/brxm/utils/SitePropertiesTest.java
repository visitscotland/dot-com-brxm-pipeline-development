package com.visitscotland.brxm.utils;

import com.visitscotland.brxm.services.ResourceBundleService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SitePropertiesTest {

    final static String BUNDLE_ID = SiteProperties.DEFAULT_CONFIG;
    @Mock
    ResourceBundleService bundle;

    @Mock
    HippoUtilsService utils;

    @Mock
    CMSProperties cmsProperties;

    @InjectMocks
    SiteProperties properties;

    @Test
    @DisplayName("VS-2756 - Return InternalSites as a list")
    void getInternalSites(){
        when(bundle.getResourceBundle(BUNDLE_ID, SiteProperties.INTERNAL_SITES, Locale.UK, false))
                .thenReturn("  aaa , bbb,,,ccc,");
        List<String> hosts = properties.getInternalSites();

        assertEquals(3,hosts.size());
        assertEquals("aaa",hosts.get(0));
        assertEquals("bbb",hosts.get(1));
        assertEquals("ccc",hosts.get(2));
     }



}
