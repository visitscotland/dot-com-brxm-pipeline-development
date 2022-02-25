package com.visitscotland.brxm.translation.plugin;

import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.util.lang.Packages;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UntranslatedLocaleTest {
    private UntranslatedLocale missingTranslation;

    @BeforeEach
    public void beforeEach() {
        missingTranslation = new UntranslatedLocale();
    }

    @Test
    public void verifyIconExists() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        ResourceReference.Key key = missingTranslation.getIcon(null, null).getKey();
        // This will return null if the icon file identified by the Key does not exist.
        InputStream stream = classLoader.getResourceAsStream(Packages.absolutePath(Class.forName(key.getScope()), key.getName()));
        assertNotNull(stream);
    }

    @Test
    public void displayName() {
        for (Locale locale : Locale.getAvailableLocales()) {
            assertNotNull(missingTranslation.getDisplayName(locale));
            assertEquals(UntranslatedLocale.MENU_TEXT, missingTranslation.getDisplayName(locale));
        }
    }
}
