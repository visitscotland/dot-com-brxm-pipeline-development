package com.visitscotland.brxm.translation.plugin;

import org.apache.wicket.model.IModel;
import org.hippoecm.frontend.translation.ILocaleProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UntranslatedLocaleProviderTest {
    private UntranslatedLocaleProvider provider;
    @Mock
    private TranslationWorkflowPlugin mockPlugin;
    @Mock
    private ILocaleProvider mockLocaleProvider;

    @BeforeEach
    public void beforeEach() {
        provider = new UntranslatedLocaleProvider(mockPlugin, mockLocaleProvider);

        lenient().when(mockLocaleProvider.getLocale(anyString())).thenReturn(mock(ILocaleProvider.HippoLocale.class));
    }

    @Test
    public void model() {
        // The model should return an IModel instance with the same locale instance inside
        ILocaleProvider.HippoLocale mockLocale = mock(ILocaleProvider.HippoLocale.class);
        IModel<ILocaleProvider.HippoLocale> model = provider.model(mockLocale);
        assertSame(mockLocale, model.getObject());
    }

    @Test
    public void size_lazyLoad() throws Exception {
        // Test that the data is loaded if null, but not reloaded on subsequent calls
        assertNull(provider.getMisingLocales());

        initialiseTranslatedLanguages(new String[]{"fr", "es", "nl"}, new String[]{"fr"});

        assertEquals(2, provider.size());

        assertNotNull(provider.getMisingLocales());
        List<ILocaleProvider.HippoLocale> initailList = provider.getMisingLocales();

        assertEquals(2, provider.size());

        assertSame(initailList, provider.getMisingLocales());
    }

    @Test
    public void iterator_lazyLoad() throws Exception {
        // Test that the data is loaded if null, but not reloaded on subsequent calls
        assertNull(provider.getMisingLocales());

        initialiseTranslatedLanguages(new String[]{"fr", "es", "nl", "no"}, new String[]{"fr"});

        provider.iterator(0, 2);

        assertNotNull(provider.getMisingLocales());
        List<ILocaleProvider.HippoLocale> initailList = provider.getMisingLocales();

        provider.iterator(0, 1);

        assertSame(initailList, provider.getMisingLocales());
    }

    @Test
    public void detach() throws Exception {
        // The missing locales should be null after detach. Should not cause an error if already null.
        assertNull(provider.getMisingLocales());
        provider.detach();
        assertNull(provider.getMisingLocales());
        initialiseTranslatedLanguages(new String[]{"fr", "es", "nl", "no"}, new String[]{"fr"});
        provider.load();
        assertNotNull(provider.getMisingLocales());
        provider.detach();
        assertNull(provider.getMisingLocales());
    }

    @Test
    public void load_no_available_languages() {
        // Should not cause an error, should just return an empty list
        when(mockPlugin.getAvailableLanguages()).thenReturn(new HashSet<>());
        provider.load();

        assertTrue(provider.getMisingLocales().isEmpty());
    }

    @Test
    public void load_all_translated() {
        // Should just return an empty list
        initialiseTranslatedLanguages(new String[]{"en", "es", "fr"}, new String[]{"en", "es", "fr"});
        provider.load();

        assertTrue(provider.getMisingLocales().isEmpty());
    }

    @Test
    public void load_mixed_translations() {
        // Should return the untranslated languages in the list
        initialiseTranslatedLanguages(new String[]{"en", "es", "fr"}, new String[]{"en"});
        provider.load();

        assertEquals(2, provider.getMisingLocales().size());
    }

    @Test
    public void load_none_translated() {
        // Should return all the languages in the list
        initialiseTranslatedLanguages(new String[]{"en", "es", "fr"}, new String[]{});
        provider.load();

        assertEquals(3, provider.getMisingLocales().size());
    }

    private void initialiseTranslatedLanguages(String[] availableLanguages, String[] translatedLanguages) {
        when(mockPlugin.getAvailableLanguages()).thenReturn(new HashSet<>(Arrays.asList(availableLanguages)));
        lenient().when(mockPlugin.hasLocaleTranslation(anyString())).thenReturn(false);
        for (String language : translatedLanguages) {
            when(mockPlugin.hasLocaleTranslation(eq(language))).thenReturn(true);
        }
    }
}
