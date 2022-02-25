package com.visitscotland.brxm.translation.plugin.menu;

import com.visitscotland.brxm.translation.plugin.TranslationWorkflowPlugin;
import com.visitscotland.brxm.translation.plugin.UntranslatedLocale;
import org.apache.wicket.Component;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.tester.WicketTester;
import org.hippoecm.frontend.translation.ILocaleProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TranslationLocaleMenuDataViewTest {
    private WicketTester tester;
    private TranslationLocaleMenuDataView menuItem;
    @Mock
    private TranslationWorkflowPlugin mockTranslationWorkflowPlugin;
    @Mock
    private IModel<String> mockLanguageModel;
    @Mock
    private MenuLocaleProvider mockMenuLocaleProvider;

    @BeforeEach
    public void beforeEach() {
        // This is needed so a TranslationLocaleMenuItem can be created, provided a mock WicketApplication
        tester = new WicketTester();
        menuItem = new TranslationLocaleMenuDataView("compId", mockTranslationWorkflowPlugin, mockLanguageModel, mockMenuLocaleProvider);
    }

    @Test
    public void languageItem() {
        // verify that when a language item is passed to populate a ViewTranslationAction is added to the menu
        Item<ILocaleProvider.HippoLocale> mockItem = createLanguageItem("es", "en");

        menuItem.populateItem(mockItem);

        ArgumentCaptor<Component> wicketComponentCaptor = ArgumentCaptor.forClass(Component.class);
        verify(mockItem).add(wicketComponentCaptor.capture());

        Component componentAdded = wicketComponentCaptor.getValue();
        assertTrue(componentAdded instanceof ViewTranslationAction);
    }

    @Test
    public void missing_language_not_english() {
        // verify that when a missing language locale is provided
        // but the current page locale is not english the clone item is not added to the menu
        Item<ILocaleProvider.HippoLocale> mockItem = createMissingLanguageItem("es");

        menuItem.populateItem(mockItem);

        verify(mockItem, never()).add(any(Component.class));
    }

    @Test
    public void missing_language_on_english() {
        // verify that when a missing language locale is provided
        // and the current page locale is english the clone item is added to the menu
        Item<ILocaleProvider.HippoLocale> mockItem = createMissingLanguageItem("en");

        menuItem.populateItem(mockItem);

        ArgumentCaptor<Component> wicketComponentCaptor = ArgumentCaptor.forClass(Component.class);
        verify(mockItem).add(wicketComponentCaptor.capture());

        Component componentAdded = wicketComponentCaptor.getValue();
        assertTrue(componentAdded instanceof TranslationAction);
    }

    @Test
    public void onDetach() {
        // verify the language model is also detached
        menuItem.detach();

        verify(mockLanguageModel).detach();
    }

    private Item<ILocaleProvider.HippoLocale> createLanguageItem(String localeName, String currentPageLocale) {
        Item<ILocaleProvider.HippoLocale> mockItem = mock(Item.class);
        ILocaleProvider.HippoLocale mockLocale = mock(ILocaleProvider.HippoLocale.class);
        when(mockItem.getModelObject()).thenReturn(mockLocale);
        when(mockLocale.getName()).thenReturn(localeName);
        when(mockTranslationWorkflowPlugin.getCurrentlySelectedDocumentLocale()).thenReturn(currentPageLocale);
        return mockItem;
    }

    private Item<ILocaleProvider.HippoLocale> createMissingLanguageItem(String currentPageLocale) {
        Item<ILocaleProvider.HippoLocale> mockItem = mock(Item.class);
        ILocaleProvider.HippoLocale mockLocale = mock(UntranslatedLocale.class);
        when(mockItem.getModelObject()).thenReturn(mockLocale);
        when(mockLocale.getName()).thenReturn("es");
        when(mockTranslationWorkflowPlugin.getCurrentlySelectedDocumentLocale()).thenReturn(currentPageLocale);
        return mockItem;
    }
}
