package com.visitscotland.brxm.services;

import org.hippoecm.hst.resourcebundle.ResourceBundleRegistry;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Locale;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResourceBundleServiceTest {

    static String BUNDLE = "bundle-file";

    private ResourceBundleService service;

    @Mock
    private ResourceBundleRegistry registry;

    @Mock
    ResourceBundle bundle;

    @Mock
    ResourceBundle fbBundle;

    @BeforeEach
    void initialize(){

        // No Locale will simulate the fallback to English
        lenient().when(registry.getBundle(BUNDLE)).thenReturn(fbBundle);
        lenient().when(registry.getBundle(BUNDLE, Locale.UK)).thenReturn(bundle);

        service = new ResourceBundleService(new CommonUtilsService());

        service.setResourceBundleRegistry(registry);

    }

    @Test
    void getResourceBundle_freemarker_overload(){
        //Tests different combination of parameters and
        //Verifies that the main method receives the correct parameters
        service = spy(service);
        ArgumentCaptor<Locale> localeCaptor = ArgumentCaptor.forClass(Locale.class);
        doCallRealMethod().when(service).getResourceBundle(eq(BUNDLE), eq("key"), localeCaptor.capture());
        doReturn("false").when(service).getResourceBundle(eq(BUNDLE), eq("key"), localeCaptor.capture(), eq(false));
        doReturn("true").when(service).getResourceBundle(eq(BUNDLE), eq("key"), localeCaptor.capture(), eq(true));

        Assertions.assertEquals("false", service.getResourceBundle(BUNDLE, "key", "es"));
        Assertions.assertEquals("es", localeCaptor.getValue().getLanguage());

        Assertions.assertEquals("true", service.getResourceBundle(BUNDLE, "key", "en", true));
        Assertions.assertEquals("en", localeCaptor.getValue().getLanguage());

        Assertions.assertEquals("false", service.getResourceBundle(BUNDLE, "key", "fr", false));
        Assertions.assertEquals("fr", localeCaptor.getValue().getLanguage());
    }


    @Test
    void toLocale(){
        // Checks method toLocale when locale is null or empty a Null locale is sent.
        // Otherwise, a locale is created according to Locale.forLanguageTag(String) specification
        Assertions.assertEquals("en", service.toLocale("en").getLanguage());
        Assertions.assertEquals("", service.toLocale(".java").getLanguage());
        assertNull(service.toLocale(""));
        assertNull(service.toLocale(null));
    }

    @Test
    void getResourceBundle_nonExistingBundleRegisterIssue(){
        //Returns null when the name of the resource bundle does not exist.
        String value = service.getResourceBundle("Non-existing", "key", Locale.UK);

        assertNull(value);
    }

    @Test
    void keyExistsInTheLocale(){
        //Returns the value when the key exists.
        when(bundle.containsKey("key")).thenReturn(true);
        when(bundle.getString("key")).thenReturn("value");

        assertEquals("value", service.getResourceBundle(BUNDLE, "key", Locale.UK, true));
    }

    @Test
    void keyExistsInTheLocale_optional(){
        //Returns the value when the key exists and optional does not have any impact on it.
        when(bundle.containsKey("key")).thenReturn(true);
        when(bundle.getString("key")).thenReturn("value");

        assertEquals("value", service.getResourceBundle(BUNDLE, "key", Locale.UK, true));
    }

    @Test
    void fallbackLocaleToDefault(){
        //When a test in a language does not exit it logs the issue and fall back to default (English)
        service = spy(service);
        when(bundle.containsKey("key")).thenReturn(true);
        when(bundle.getString("key")).thenReturn("");
        when(fbBundle.containsKey("key")).thenReturn(true);
        when(fbBundle.getString("key")).thenReturn("value");

        String value = service.getResourceBundle(BUNDLE, "key", Locale.UK);

        verify(service).logContentIssue(anyString(), any());
        assertEquals("value", value);
    }

    @Test
    void fallbackLocaleToDefault_defaultDoesNotExist(){
        // Tries to fallback to English, but the English key does not exist either.
        // It logs 2 messages, one for the original language and other for the global language.
        service = spy(service);
        when(bundle.containsKey("key")).thenReturn(true);
        when(bundle.getString("key")).thenReturn("");
        when(fbBundle.containsKey("key")).thenReturn(true);
        when(fbBundle.getString("key")).thenReturn("");

        String value = service.getResourceBundle(BUNDLE, "key", Locale.UK);

        verify(service, times(2)).logContentIssue(anyString(), any());
        assertEquals("", value);
    }

    @Test
    void optional_DoNotFallbackLocaleToDefault(){
        //When optional, it doesn't log any issue when not found
        service = spy(service);
        when(bundle.containsKey("key")).thenReturn(true);
        when(bundle.getString("key")).thenReturn("");

        String value = service.getResourceBundle(BUNDLE, "key", Locale.UK, true);

        verify(service, times(0)).logContentIssue(anyString(), any());
        assertEquals("", value);
    }

    @Test
    void nonExistingKey(){
        //When a key does not exist it returns null and logs an issue
        service = spy(service);
        when(bundle.containsKey("key")).thenReturn(false);

        String value = service.getResourceBundle(BUNDLE, "key", Locale.UK);

        verify(service).logContentIssue(anyString(), any());
        assertNull(value);
    }

    @Test
    void existsKey(){
        when(bundle.containsKey("key")).thenReturn(true);
        when(bundle.getString("key")).thenReturn("value");

        assertTrue(service.existsResourceBundleKey(BUNDLE, "key", Locale.UK));
    }

    @Test
    void existsKey_emptyValue() {
        when(bundle.containsKey("key")).thenReturn(true);
        when(bundle.getString("key")).thenReturn("");

        assertFalse(service.existsResourceBundleKey(BUNDLE, "key", Locale.UK));
    }

    @Test
    @DisplayName(("Default CTA label implementation"))
    void getCtaLabel_auto() {
        lenient().when(registry.getBundle("essentials.global", Locale.UK)).thenReturn(bundle);

        when(bundle.containsKey("button.find-out-more")).thenReturn(true);
        when(bundle.getString("button.find-out-more")).thenReturn("Find out more");

        assertEquals("Find out more", service.getCtaLabel("",Locale.UK));
        assertEquals("Find out more", service.getCtaLabel(null,Locale.UK));
    }

    @Test
    @DisplayName(("VS-2935 - Default video label implementation"))
    void getPlainLink_fromVideo(){
        lenient().when(registry.getBundle("essentials.global", Locale.GERMAN)).thenReturn(bundle);

        when(bundle.containsKey("video.play-btn")).thenReturn(true);
        when(bundle.getString("video.play-btn")).thenReturn("Play Video");

        assertEquals("Watch it!",service.getVideoCtaLabel("Watch it!", Locale.GERMAN));
        assertEquals("Play Video",service.getVideoCtaLabel("", Locale.GERMAN));
    }

}
