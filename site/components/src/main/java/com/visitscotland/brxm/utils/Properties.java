package com.visitscotland.brxm.utils;

import com.visitscotland.brxm.services.ResourceBundleService;
import com.visitscotland.utils.Contract;
import org.hippoecm.hst.configuration.hosting.Mount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Component
public class Properties {

    private static final Logger logger = LoggerFactory.getLogger(Properties.class.getName());

    static final String DEFAULT_CONFIG = "default.config";

    static final String INSTAGRAM_API = "instagram.api";
    static final String INSTAGRAM_ACCESS_TOKEN ="instagram.accesstoken";
    static final String INSTAGRAM_APP_ID ="instagram.app-id";
    static final String INSTAGRAM_URL ="instagram.post-url";
    static final String HELPDESK_EMAIL = "helpdesk-email";
    static final String IKNOW_COMMUNITY_URL = "iknow-community.url";
    static final String IKNOW_COMMUNITY_TAGGED_DISCUSSION = "iknow-community.tagged-discussion";
    static final String YOUTUBE_API_KEY = "youtube.api-key";
    static final String CHANNEL_ORDER = "seo.alternate-link-locale-order";
    static final String GLOBAL_SEARCH_PATH = "global-search.path";
    static final String ENGINE_ID = "global-search.engine-id";
    static final String CONTENT_CACHE_ENABLED = "content-cache.enabled";
    static final String CONTENT_CACHE_RETENTION_PERIOD = "content-cache.retention-period";
    static final String CONTENT_CACHE_MAX_ELEMENTS = "content-cache.max-elements";

    //Environment
    static final String USE_RELATIVE_URLS = "links.use-relative-urls";
    static final String INTERNAL_SITES = "links.internal-sites";
    static final String CMS_BASE_PATH = "links.cms-base-path.url";
    static final String CONVERT_TO_RELATIVE = "links.convert-to-relative";
    static final String SERVE_LECAGY_CSS = "data-internal.serve-legacy-css";
    static final String DMS_INTERNAL_PATH = "data-internal.path";

    // DMS Properties
    public static final String API_DATA_BACKEND_HOST = "api-data.backend-url";
    public static final String DMS_DATA_HOST = "dms-data.private-url";
    static final String DMS_DATA_PUBLIC_HOST = "dms-data.public-url";
    static final String DMS_DATA_ENCODING = "dms-data.encoding";
    static final String DMS_DATA_API_KEY = "dms-data.api-key";
    static final String DMS_DATA_TIMEOUT = "dms-data.timeout";
    static final String DMS_DATA_TRIES = "dms-data.tries";
    static final String DMS_DATA_SLEEP_TIME = "dms-data.sleep-time";
    static final String DMS_HOST = "links.vs-dms-products.url";
    static final String DMS_MAP_DEFAULT_DISTANCE = "dms.default-distance";

    //Page References
    static final String GLOBAL_SEARCH = "site.path.global-search";
    static final String SKI_SECTION = "site.path.ski-landing";

    private final ResourceBundleService bundle;

    private final HippoUtilsService utils;

    public Properties(ResourceBundleService bundle, HippoUtilsService utils){
        this.bundle = bundle;
        this.utils = utils;
    }

    public String getInstagramApi() {
        return readString(INSTAGRAM_API);
    }

    public String getInstagramURL() {
        return readString(INSTAGRAM_URL);
    }

    public String getGlobalSearchURL() {
        return readString(GLOBAL_SEARCH_PATH);
    }

    public String getInstagramToken() {
        String accessCode = readString(INSTAGRAM_ACCESS_TOKEN);
        if (Contract.isEmpty(accessCode)){
            return readString(INSTAGRAM_APP_ID);
        } else {
            return readString(INSTAGRAM_APP_ID) +"|"+accessCode;
        }
    }

    public String getChannelOrder(){
        return readString(CHANNEL_ORDER);
    }

    public String getHelpdeskEmail() {
        return readString(HELPDESK_EMAIL);
    }

    public boolean isRelativeURLs(){
        return readBoolean(USE_RELATIVE_URLS);
    }

    public String getDmsHost() {
        if (isRelativeURLs()){
            return "";
        } else {
            return readString(DMS_HOST);
        }
    }

    public String getCmsBasePath() {
        if (isRelativeURLs()){
            return "";
        } else {
            return readString(CMS_BASE_PATH);
        }
    }

    public String getConvertToRelative() {
        if (isRelativeURLs()){
            return readString(CONVERT_TO_RELATIVE);
        } else {
            return "";
        }
    }

    public String getApiDataBackendHost() {
        return readString(API_DATA_BACKEND_HOST);
    }

    public String getDmsDataHost() {
        return readString(DMS_DATA_HOST);
    }

    public String getDmsDataPublicHost() {
        return readString(DMS_DATA_PUBLIC_HOST);
    }

    public Double getDmsMapDefaultDistance() {
        return readDouble(DMS_MAP_DEFAULT_DISTANCE);
    }

    public String getDmsToken() {
        return readString(DMS_DATA_API_KEY);
    }

    public Integer getDmsTimeout() {
        return readInteger(DMS_DATA_TIMEOUT);
    }

    public Integer getDmsTries() {
        return readInteger(DMS_DATA_TRIES);
    }

    public Integer getDmsWaitTime() {
        return readInteger(DMS_DATA_SLEEP_TIME);
    }

    public String getIknowCommunityUrl() {
        return readString(IKNOW_COMMUNITY_URL);
    }

    public String getIknowCommunityTaggedDiscussion() {
        return readString(IKNOW_COMMUNITY_TAGGED_DISCUSSION);
    }

    public String getYoutubeApiKey() {
        return readString(YOUTUBE_API_KEY);
    }

    public String getDmsInternalPath() {
        return readString(DMS_INTERNAL_PATH);
    }

    public String getSiteSkiSection() {
        return readString(SKI_SECTION);
    }

    public String getSiteGlobalSearch() {
        return readString(GLOBAL_SEARCH);
    }

    public Integer getContentCacheRetention() {
        //Note that the retention period is defined in seconds and java.util.Date measures the time in seconds
        return readInteger(CONTENT_CACHE_RETENTION_PERIOD) * 1000;
    }

    public boolean isContentCacheEnabled() {
        //Note that the retention period is defined in seconds and java.util.Date measures the time in seconds
        return readBoolean(CONTENT_CACHE_ENABLED);
    }

    /**
     * Max number of elements cached. If the property is not defined in the CMS, there is no maximum
     */
    public Integer getContentCacheMaxElements() {
        Integer size = readInteger(CONTENT_CACHE_MAX_ELEMENTS);
        return size > 0 ? size : Integer.MAX_VALUE;
    }

    /**
     * Default DMS version served by Hippo.
     * <p>
     * Current allowed values:
     * <ul>
     *  <li>"legacy": For legacy applications based on 10 pixels base line</li>
     *  <li>"": Standard version for newly developed applications. </li>
     * </ul>
     * <p>
     * Values that are not in this list are going to be interpreted as standard version.
     * <p>
     */
    public Boolean isServeLegacyCss() {
        return readBoolean(SERVE_LECAGY_CSS);
    }

    public List<String> getInternalSites() {
        String sites = readString(INTERNAL_SITES);
        if (!Contract.isEmpty(sites)){
            //TODO: Java 10 -> toUnmodifiableList()
            //TODO: Java 11 -> Predicate.not(String::isEmpty)
            return Arrays.stream(sites.trim().split("\\s*,\\s*")).filter(((Predicate<String>) String::isEmpty).negate()).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }


    public Charset getDmsEncoding() {
        String value = getProperty(DMS_DATA_ENCODING);
        try{
            if (!Contract.isEmpty(value)) {
                return Charset.forName(value);
            }
        } catch (Exception e){
            logger.warn("{} is not a valid value for the property {}", value, DMS_DATA_ENCODING);
        }
        return StandardCharsets.UTF_8;
    }

    public String readString(String key){
        String value = getProperty(key);
        if (value != null){
            return value;
        } else {
            return "";
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
            String bundleId = mount.getProperty("visitscotland:cmsProperties");
            if (bundleId != null){
                return bundleId;
            } else if (mount.getParent() != null){
                //Other languages and data endpoints are mounted as subsites in the configuration
                bundleId = mount.getParent().getProperty("visitscotland:cmsProperties");

                if (bundleId != null){
                    return bundleId;
                }
            }
        }

        return DEFAULT_CONFIG;
    }

    public String getProperty(String key){
        return getProperty(key, Locale.UK);
    }

    public String getProperty(String key, String locale){
        return getProperty(key, Locale.forLanguageTag(locale));
    }

    public String getProperty(String key, Locale locale){
        String bundleId = getEnvironmentProperties();
        boolean defaultConfig = bundleId.equals(DEFAULT_CONFIG);
        boolean englishLocale = Locale.UK.equals(locale);

        String value = bundle.getResourceBundle(bundleId, key, locale, !defaultConfig || !englishLocale);

        if (Contract.isEmpty(value)) {

            if (!englishLocale) {
                value = bundle.getResourceBundle(bundleId, key, Locale.UK, !defaultConfig );
            }
            if (Contract.isEmpty(value) && !defaultConfig) {
                value = bundle.getResourceBundle(DEFAULT_CONFIG, key,locale, !englishLocale);
                if (Contract.isEmpty(value) && !englishLocale){
                    value = bundle.getResourceBundle(DEFAULT_CONFIG, key,Locale.UK, false);
                }
            }
        }

        if (Contract.isEmpty(value)) {
            logger.info("The property {} hasn't been set in the resourceBundle {}", key, bundleId);
        } else if (value.startsWith("$")){
            return getEnvironmentVariable(value.substring(1));
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
}
