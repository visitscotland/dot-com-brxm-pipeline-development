package com.visitscotland.brxm.translation.plugin.menu;

import com.visitscotland.brxm.translation.plugin.ChangeSet;
import com.visitscotland.brxm.translation.plugin.DocumentTranslator;
import com.visitscotland.brxm.translation.plugin.TranslationWorkflowPlugin;
import com.visitscotland.brxm.translation.plugin.UntranslatedLocale;
import org.apache.wicket.model.IModel;
import org.hippoecm.frontend.translation.ILocaleProvider;
import org.hippoecm.hst.content.beans.ObjectBeanManagerException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.jcr.RepositoryException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MenuLocaleProviderTest {
    private MenuLocaleProvider provider;
    @Mock
    private TranslationWorkflowPlugin mockWorkflowPlugin;
    @Mock
    private DocumentTranslator mockDocumentTranslator;
    private List<ChangeSet> changeSetList;
    @Mock
    private ILocaleProvider mockLocaleProvider;

    @BeforeEach
    public void beforeEach() throws Exception {
        when(mockWorkflowPlugin.getLocaleProvider()).thenReturn(mockLocaleProvider);
        lenient().when(mockWorkflowPlugin.hasLocaleTranslation(anyString())).thenReturn(false);
        lenient().when(mockWorkflowPlugin.getLocale()).thenReturn(Locale.ENGLISH);
        changeSetList = new ArrayList<>();
        lenient().when(mockDocumentTranslator.buildChangeSetList(any(), any())).thenReturn(changeSetList);
        provider = new MenuLocaleProvider(mockWorkflowPlugin, mockDocumentTranslator);
    }

    @Test
    public void load_with_no_available_languages() throws Exception {
        // When there are no available locales then an empty list should be returned
        // Note: this is not a realistic test, it should never happen,
        // but is a valid path through the code
        initialiseCurrentDocumentLocale("en");
        when(mockWorkflowPlugin.getAvailableLanguages()).thenReturn(new HashSet<>());

        provider.load();

        assertNotNull(provider.getAvailableLocales());
        assertTrue(provider.getAvailableLocales().isEmpty());
    }

    @Test
    public void load_only_current_locale() throws Exception {
        // If the only locale available is the current locale then an empty list should be returned
        initialiseCurrentDocumentLocale("en");
        initialiseAvailableLocales("en");
        addLocaleTranslation("en");

        provider.load();

        assertNotNull(provider.getAvailableLocales());
        assertTrue(provider.getAvailableLocales().isEmpty());
    }

    @Test
    public void load_with_missing_translation_on_main_document() throws Exception {
        // The available locales contains only the current locale and a locale with a untranslated translation
        // Only the untranslated locale marker should be returned
        initialiseCurrentDocumentLocale("en");
        initialiseAvailableLocales("en", "es");
        addLocaleTranslation("en");

        provider.load();

        assertNotNull(provider.getAvailableLocales());
        assertEquals(1, provider.getAvailableLocales().size());
        assertTrue(provider.getAvailableLocales().get(0) instanceof UntranslatedLocale);
    }

    @Test
    public void load_with_mixed() throws Exception {
        // The available locales contains the current locale and a mix of missing translations and translated
        // locales. The translated locales should be returned and the untranslated locale marker.
        initialiseCurrentDocumentLocale("en");
        addLocaleToProvider("fr", "French");
        addLocaleToProvider("es", "Spanish");
        initialiseAvailableLocales("en", "es", "de", "fr");
        addLocaleTranslation("en", "es", "fr");

        provider.load();

        assertNotNull(provider.getAvailableLocales());
        assertEquals(3, provider.getAvailableLocales().size());
        // The translate option should always be the first
        assertTrue(provider.getAvailableLocales().get(0) instanceof UntranslatedLocale);
        // The other options should be sorted alphabetically
        assertEquals("French", provider.getAvailableLocales().get(1).getDisplayName(Locale.ENGLISH));
        assertEquals("Spanish", provider.getAvailableLocales().get(2).getDisplayName(Locale.ENGLISH));
    }

    @Test
    public void load_with_no_missing() throws Exception {
        // The available locales contains a full set of the locales, i.e. they all have translations.
        // All of the translated locales should be returned.
        initialiseCurrentDocumentLocale("en");
        addLocaleToProvider("fr", "French");
        addLocaleToProvider("es", "Spanish");
        addLocaleToProvider("de", "German");
        initialiseAvailableLocales("en", "es", "de", "fr");
        addLocaleTranslation("en", "es", "de", "fr");

        provider.load();

        assertNotNull(provider.getAvailableLocales());
        assertEquals(3, provider.getAvailableLocales().size());
        // The options should be sorted alphabetically
        assertEquals("French", provider.getAvailableLocales().get(0).getDisplayName(Locale.ENGLISH));
        assertEquals("German", provider.getAvailableLocales().get(1).getDisplayName(Locale.ENGLISH));
        assertEquals("Spanish", provider.getAvailableLocales().get(2).getDisplayName(Locale.ENGLISH));
    }

    @Test
    public void load_with_missing_translation_on_sibling_document() throws Exception {
        // The main document is fully translated, but there is a child document with a missing translation
        // The untranslated locale marker should be returned, and all of the locales for the main document
        initialiseCurrentDocumentLocale("en");
        addLocaleToProvider("fr", "French");
        addLocaleToProvider("es", "Spanish");
        addLocaleToProvider("de", "German");
        initialiseAvailableLocales("en", "es", "de", "fr");
        addLocaleTranslation("en", "es", "de", "fr");

        ChangeSet change = mock(ChangeSet.class);
        changeSetList.add(change);

        provider.load();

        assertNotNull(provider.getAvailableLocales());
        assertEquals(4, provider.getAvailableLocales().size());
        assertTrue(provider.getAvailableLocales().get(0) instanceof UntranslatedLocale);
        // The options should be sorted alphabetically
        assertEquals("French", provider.getAvailableLocales().get(1).getDisplayName(Locale.ENGLISH));
        assertEquals("German", provider.getAvailableLocales().get(2).getDisplayName(Locale.ENGLISH));
        assertEquals("Spanish", provider.getAvailableLocales().get(3).getDisplayName(Locale.ENGLISH));
    }

    @Test
    public void iterator_lazyLoad() throws Exception {
        // Test that the data is loaded if null, but not reloaded on subsequent calls
        assertNull(provider.getAvailableLocales());

        initialiseCurrentDocumentLocale("en");
        initialiseAvailableLocales("en", "es");
        addLocaleTranslation("en");

        provider.iterator(0, 1);

        verify(mockWorkflowPlugin, atMostOnce()).getAvailableLanguages();
        List<ILocaleProvider.HippoLocale> initialLoad = provider.getAvailableLocales();
        assertNotNull(provider.getAvailableLocales());

        provider.iterator(0, 1);

        verify(mockWorkflowPlugin, atMostOnce()).getAvailableLanguages();
        assertNotNull(provider.getAvailableLocales());
        assertSame(initialLoad, provider.getAvailableLocales());
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
        assertNull(provider.getAvailableLocales());

        initialiseCurrentDocumentLocale("en");
        initialiseAvailableLocales("en", "es");
        addLocaleTranslation("en");

        assertEquals(1, provider.size());

        verify(mockWorkflowPlugin, atMostOnce()).getAvailableLanguages();
        List<ILocaleProvider.HippoLocale> initialLoad = provider.getAvailableLocales();
        assertNotNull(provider.getAvailableLocales());

        assertEquals(1, provider.size());

        verify(mockWorkflowPlugin, atMostOnce()).getAvailableLanguages();
        assertNotNull(provider.getAvailableLocales());
        assertSame(initialLoad, provider.getAvailableLocales());
    }

    @Test
    public void detach() throws Exception {
        // The available locales should be null after detach. Should not cause an error if already null.
        initialiseCurrentDocumentLocale("en");
        initialiseAvailableLocales("en", "es");
        addLocaleTranslation("en");
        provider.load();

        assertNotNull(provider.getAvailableLocales());

        provider.detach();

        assertNull(provider.getAvailableLocales());

        provider.detach();
    }

    @Test
    public void load_throws_ObjectBeanManagerException() throws Exception {
        // When an exception is thrown getting untranslated children,
        // All of the translated locales should still be returned.
        initialiseCurrentDocumentLocale("en");
        addLocaleToProvider("fr", "French");
        addLocaleToProvider("es", "Spanish");
        addLocaleToProvider("de", "German");
        initialiseAvailableLocales("en", "es", "de", "fr");
        addLocaleTranslation("en", "es", "de", "fr");

        when(mockDocumentTranslator.buildChangeSetList(any(), any())).thenThrow(new ObjectBeanManagerException());
        provider.load();

        assertNotNull(provider.getAvailableLocales());
        assertEquals(3, provider.getAvailableLocales().size());
        // The options should be sorted alphabetically
        assertEquals("French", provider.getAvailableLocales().get(0).getDisplayName(Locale.ENGLISH));
        assertEquals("German", provider.getAvailableLocales().get(1).getDisplayName(Locale.ENGLISH));
        assertEquals("Spanish", provider.getAvailableLocales().get(2).getDisplayName(Locale.ENGLISH));
    }

    @Test
    public void load_throws_RepositoryException() throws Exception {
        // When an exception is thrown getting untranslated children,
        // All of the translated locales should still be returned.
        initialiseCurrentDocumentLocale("en");
        addLocaleToProvider("fr", "French");
        addLocaleToProvider("es", "Spanish");
        addLocaleToProvider("de", "German");
        initialiseAvailableLocales("en", "es", "de", "fr");
        addLocaleTranslation("en", "es", "de", "fr");

        when(mockDocumentTranslator.buildChangeSetList(any(), any())).thenThrow(new RepositoryException());
        provider.load();

        assertNotNull(provider.getAvailableLocales());
        assertEquals(3, provider.getAvailableLocales().size());
        // The options should be sorted alphabetically
        assertEquals("French", provider.getAvailableLocales().get(0).getDisplayName(Locale.ENGLISH));
        assertEquals("German", provider.getAvailableLocales().get(1).getDisplayName(Locale.ENGLISH));
        assertEquals("Spanish", provider.getAvailableLocales().get(2).getDisplayName(Locale.ENGLISH));
    }

    private void addLocaleToProvider(String localeISO, String displayName) {
        ILocaleProvider.HippoLocale mockLocale = mock(ILocaleProvider.HippoLocale.class);
        lenient().when(mockLocale.getName()).thenReturn(localeISO);
        when(mockLocaleProvider.getLocale(eq(localeISO))).thenReturn(mockLocale);
        lenient().when(mockLocale.getDisplayName(eq(Locale.ENGLISH))).thenReturn(displayName);
    }

    private void initialiseCurrentDocumentLocale(String localeString) {
        when(mockWorkflowPlugin.getCurrentlySelectedDocumentLocale()).thenReturn(localeString);
    }

    private void initialiseAvailableLocales(String... localeISO) {
        Set<String> availableLocales = new HashSet<>(Arrays.asList(localeISO));
        when(mockWorkflowPlugin.getAvailableLanguages()).thenReturn(availableLocales);
    }

    private void addLocaleTranslation(String... localeISO) {
        for (String locale : localeISO) {
            when(mockWorkflowPlugin.hasLocaleTranslation(eq(locale))).thenReturn(true);
        }
    }
}
