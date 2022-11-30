package com.visitscotland.brxm.services;

import com.visitscotland.brxm.config.VsComponentManager;
import com.visitscotland.brxm.utils.ContentLogger;
import com.visitscotland.utils.Contract;
import org.hippoecm.hst.resourcebundle.ResourceBundleRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.ResourceBundle;

@Component
public class ResourceBundleService {

    private static final Logger logger = LoggerFactory.getLogger(ResourceBundleService.class.getName());
    private static final String GLOBAL_BUNDLE_FILE = "essentials.global";

    private final Logger contentLogger;

    public ResourceBundleService(ContentLogger contentLogger){
        this.contentLogger = contentLogger;
    }

    ResourceBundleRegistry registry;

    /**
     * ResourceBundleRegistry is not a Spring Component, therefore when Spring is wiring the component it cannot
     * wire this the {@code ResourceBundleRegistry}. That's the reason why we need to check the registry before
     * using it.
     */
    private ResourceBundleRegistry getResourceBundleRegistry(){
        if (registry== null){
            registry = VsComponentManager.get(ResourceBundleRegistry.class);
        }
        return registry;
    }

    /**
     * Gets a string for the given key from this resource bundle or one of its parents.
     *
     * @param bundleName id of the Resource Bundle defined in Hippo
     * @param key key
     * @param locale locale
     */
    public String getResourceBundle(String bundleName, String key, Locale locale){
        return getResourceBundle(bundleName, key, locale, false);
    }

    /**
     * Gets a string for the given key from this resource bundle or one of its parents.
     *
     * This method is usually used from FREEMARKER
     *
     * @param bundleName id of the Resource Bundle defined in Hippo
     * @param key key
     * @param locale locale
     */
    public String getResourceBundle(String bundleName, String key, String locale){
        return getResourceBundle(bundleName, key, toLocale(locale));
    }

    /**
     * Gets a string for the given key from this resource bundle (or one of its parents when is not {@code optional}).
     *
     * This method is usually used from FREEMARKER
     *
     * @param bundleName id of the Resource Bundle defined in Hippo
     * @param key key
     * @param locale locale
     * @param optional when {@code false} if the value does not exist in the language it would fallback to English
     *
     * @return string for the given key
     */
    public String getResourceBundle(String bundleName, String key, String locale, boolean optional){
        return getResourceBundle(bundleName, key, toLocale(locale), optional);
    }

    /**
     * when locale is null or empty a {@code null}value is returned. Otherwise, a locale is created according to
     * Locale.forLanguageTag(String) specification
     *
     * @param locale String with the locale information
     *
     * @return a {@code Locale} object version of the {@code String} or {@code null} when empty String or null
     */
    Locale toLocale(String locale){
        if (locale == null || locale.length() == 0){
            return null;
        } else {
            return Locale.forLanguageTag(locale);
        }
    }

    /**
     * Gets a string for the given key from this resource bundle (or one of its parents when is not {@code optional}).
     *
     *
     * @param bundleName id of the Resource Bundle defined in Hippo
     * @param key key
     * @param locale locale
     * @param optional when {@code false} if the value does not exist in the language it would fallback to English
     * @return string for the given key
     */
    public String getResourceBundle(String bundleName, String key, Locale locale, boolean optional){

        ResourceBundle bundle = getResourceBundle(bundleName, locale);

        String value = null;

        if (bundle == null) {
            logger.warn("The resource bundle '{}' does not exist", bundleName);
        } else {
            if(bundle.containsKey(key)) {
                value = bundle.getString(key);
                if (Contract.isEmpty(value) && locale != null && !optional) {
                    value = getResourceBundle(bundleName,key, (Locale) null, false);
                    if (!Contract.isEmpty(value)) {
                        logContentIssue("The label key {} does not exists for the {} channel. Resource Bundle key {}", key, locale, bundleName);
                    }
                }
            }
            if (Contract.isEmpty(value) && !optional){
                logContentIssue("The label key {} does not exists for the English channel. Resource Bundle key {}", key, bundleName);
            }
        }

        return value;
    }

    /**
     * Return a resource bundle for a specific locale
     *
     * @param bundleName id of the Resource Bundle defined in Hippo
     * @param locale locale
     *
     * @return resource bundle for a specific locale
     */
    private ResourceBundle getResourceBundle(String bundleName, Locale locale){
        if (locale == null) {
            return getResourceBundleRegistry().getBundle(bundleName);
        } else {
            return getResourceBundleRegistry().getBundle(bundleName, locale);
        }
    }

    public void setResourceBundleRegistry(ResourceBundleRegistry registry){
        this.registry = registry;
    }

    /**
     * Verify that a value exists for a key in the specified language
     *
     * @param bundleName id of the Resource Bundle defined in Hippo
     * @param key Key
     * @param locale Locale
     *
     * @return {@code true} when the key exists in that language and has a value.
     */
    public boolean existsResourceBundleKey(String bundleName, String key,  Locale locale){
        ResourceBundle bundle = getResourceBundle(bundleName, locale);
        return bundle != null && bundle.containsKey(key) && !Contract.isEmpty(bundle.getString(key));
    }

    /**
     * Logs an issue that can be solved from the CMS
     *
     * @param message message
     * @param args arguments for the message
     */
    void logContentIssue(String message, Object... args) {
        contentLogger.warn(message, args);
    }

    /**
     * Returns the default global label or the override Text when that is defined
     *
     * @param overrideText Manual CTA defined in the CMS
     * @param bundleKey key of the label in the essentials.global Resource Bundle File
     * @param locale    Locale
     * @return the manual CTA if provided otherwise the default CTA
     */
    public String getCtaLabel(String overrideText, String bundleKey, Locale locale) {
        if (!Contract.isEmpty(overrideText)) {
            return overrideText;
        } else {
            return getResourceBundle(GLOBAL_BUNDLE_FILE, bundleKey,  locale);
        }
    }

    public String getCtaLabel(String overrideText, Locale locale) {
        return getCtaLabel(overrideText, "button.find-out-more", locale);
    }

    public String getVideoCtaLabel(String overrideText, Locale locale){
        return getCtaLabel(overrideText, "video.play-btn", locale);
    }

    public String getFindOutMoreAboutCta(String title, Locale locale) {
        return String.format("%s %s", getResourceBundle(GLOBAL_BUNDLE_FILE, "find-out-more-about", locale), title);
    }
}