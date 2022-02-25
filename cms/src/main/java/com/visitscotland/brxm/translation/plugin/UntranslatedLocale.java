package com.visitscotland.brxm.translation.plugin;

import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.request.resource.ResourceReference;
import org.hippoecm.frontend.service.IconSize;
import org.hippoecm.frontend.translation.ILocaleProvider;

import java.util.Locale;

/**
 * This is a fake HippoLocale that is used to display a translation menu item.
 * A HippoLocale was needed as we were extending an existing Hippo plugin and
 * a HippoLocale was already used.
 * 
 * The Class should not be expected to behave as other HippoLocales do, it is
 * actually Locale independent.
 */
public class UntranslatedLocale extends ILocaleProvider.HippoLocale {
    public static final String MENU_TEXT = "Create clone";

    public UntranslatedLocale() {
        super(Locale.ENGLISH, "Missing");
    }

    @Override
    public ResourceReference getIcon(IconSize size, ILocaleProvider.LocaleState type) {
        return new PackageResourceReference(TranslationWorkflowPlugin.class, "translations-16.png");
    }

    @Override
    public String getDisplayName(Locale locale) {
        return MENU_TEXT;
    }
}
