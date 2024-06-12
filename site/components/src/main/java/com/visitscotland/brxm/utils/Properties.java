package com.visitscotland.brxm.utils;

import com.visitscotland.brxm.services.ResourceBundleService;
import com.visitscotland.utils.Contract;
import org.hippoecm.hst.configuration.hosting.Mount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;

public abstract class Properties {

    private static final Logger logger = LoggerFactory.getLogger(Properties.class.getName());

    private final ResourceBundleService bundle;
    private final HippoUtilsService utils;

    protected Properties(ResourceBundleService bundle, HippoUtilsService utils){
        this.bundle = bundle;
        this.utils = utils;
    }

    abstract String getDefaultConfig();

    abstract String getOverrideProperty();

    public String readString(String key){
        String value = getProperty(key);
        if (value != null){
            return value;
        } else {
            return "";
        }
    }

    public String readString(String key, Locale locale){
        String value = getProperty(key, locale);
        if (value != null){
            return value;
        } else {
            return getProperty(key);
        }
    }

    public int readInteger(String key){
        String value = getProperty(key);

        try {
            if (value != null){
                return Integer.parseInt(value);
            }
        } catch (NumberFormatException nfe){
            logger.error("The property value of the property {} cannot be casted to Integer. '{}' is not allowed.", key,value);
        }
        return 0;
    }

    public double readDouble(String key){
        String value = getProperty(key);

        try {
            if (value != null){
                return Double.parseDouble(value);
            }
        } catch (NumberFormatException nfe){
            logger.error("The property value of the property {} cannot be casted to Double. '{}' is not allowed.", key,value);
        }
        return 0;
    }

    public boolean readBoolean(String key){
        return Boolean.parseBoolean(getProperty(key));
    }

    /**
     * Calculates the properties document defined in the environment (i.e. /hst:visitscotland/hst:hosts/dev-localhost/localhost/)
     * and when none is defined returns the default one.
     *
     * @return Resource Bundle id for the configuration
     */
    private String getEnvironmentProperties(){
        final Mount mount = utils.getResolvedMount(null);
        if (mount  != null) {
            String bundleId = mount.getProperty(getOverrideProperty());
            if (bundleId != null){
                return bundleId;
            } else if (mount.getParent() != null){
                //Other languages and data endpoints are mounted as subsites in the configuration
                bundleId = mount.getParent().getProperty(getOverrideProperty());

                if (bundleId != null){
                    return bundleId;
                }
            }
        }

        return getDefaultConfig();
    }

    public String getProperty(String key){
        return getProperty(key, Locale.UK);
    }

    public String getProperty(String key, String locale){
        return getProperty(key, Locale.forLanguageTag(locale));
    }

    public String getProperty(String key, Locale locale){
        String bundleId = getEnvironmentProperties();
        boolean defaultConfig = bundleId.equals(getDefaultConfig());
        boolean englishLocale = Locale.UK.equals(locale);

        String value = bundle.getResourceBundle(bundleId, key, locale, !defaultConfig || !englishLocale);

        if (Contract.isEmpty(value)) {

            if (!englishLocale) {
                value = bundle.getResourceBundle(bundleId, key, Locale.UK, !defaultConfig );
            }
            if (Contract.isEmpty(value) && !defaultConfig) {
                value = bundle.getResourceBundle(getDefaultConfig(), key,locale, !englishLocale);
                if (Contract.isEmpty(value) && !englishLocale){
                    value = bundle.getResourceBundle(getDefaultConfig(), key,Locale.UK, false);
                }
            }
        }

        if (Contract.isEmpty(value)) {
            logger.info("The property {} hasn't been set in the resourceBundle {}", key, bundleId);
        } else if (value.startsWith("$")){
            return getEnvironmentVariable(value.substring(1));
        } else if (value.startsWith("%")){
            return getSystemProperty(value.substring(1));
        } else {
            return value;
        }

        return null;
    }

    String getEnvironmentVariable(String name){
        try {
            return System.getenv(name);
        } catch (RuntimeException e){
            return null;
        }
    }

    String getSystemProperty(String name){
        return System.getProperty(name, "");
    }
}
