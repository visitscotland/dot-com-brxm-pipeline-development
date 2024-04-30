package com.visitscotland.brxm.utils;

import com.visitscotland.brxm.services.ResourceBundleService;
import com.visitscotland.utils.Contract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@Component
public class CMSProperties extends Properties {

    private static final Logger logger = LoggerFactory.getLogger(CMSProperties.class.getName());

    static final String DEFAULT_CONFIG = "default.cms.config";
    static final String OVERRIDE_PROPERTY = "visitscotland:cmsProperties";

    //Internal Configuration
    static final String HELPDESK_EMAIL = "helpdesk-email";
    static final String USE_RELATIVE_URLS = "links.use-relative-urls";

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
    static final String MAP_MULTIPOLYGONS = "map.multipolygon.regions";


    // Third-party API Configuration
    static final String INSTAGRAM_API = "instagram.api";
    static final String INSTAGRAM_ACCESS_TOKEN = "instagram.accesstoken";
    static final String INSTAGRAM_APP_ID = "instagram.app-id";
    static final String INSTAGRAM_URL = "instagram.post-url";
    static final String YOUTUBE_API_KEY = "youtube.api-key";
    static final String YOUTUBE_API_BASE = "youtube.api-base";

    //CMS Cache
    static final String SNIPPET_CACHE = "snippet-cache.enable";
    static final String CONTENT_CACHE_ENABLED = "content-cache.enabled";
    static final String CONTENT_CACHE_RETENTION_PERIOD = "content-cache.retention-period";
    static final String CONTENT_CACHE_MAX_ELEMENTS = "content-cache.max-elements";
    static final String NAVIGATION_CACHE = "navigation.cms.cache";

    // External Header and Footer configuration.
    static final String SERVE_LECAGY_CSS = "data-internal.serve-legacy-css";
    static final String DMS_INTERNAL_PATH = "data-internal.path";

    static final String CMS_BASE_PATH = "links.cms-base-path.url";

    public CMSProperties(ResourceBundleService bundle, HippoUtilsService utils) {
        super(bundle, utils);
    }

    @Override
    public String getDefaultConfig() {
        return DEFAULT_CONFIG;
    }

    @Override
    public String getOverrideProperty() {
        return OVERRIDE_PROPERTY;
    }

    public String getInstagramApi() {
        return readString(INSTAGRAM_API);
    }

    public String getInstagramURL() {
        return readString(INSTAGRAM_URL);
    }

    public String getInstagramToken() {
        String accessCode = readString(INSTAGRAM_ACCESS_TOKEN);
        if (Contract.isEmpty(accessCode)) {
            return readString(INSTAGRAM_APP_ID);
        } else {
            return readString(INSTAGRAM_APP_ID) + "|" + accessCode;
        }
    }

    public String getHelpdeskEmail() {
        return readString(HELPDESK_EMAIL);
    }

    public boolean isRelativeURLs() {
        return readBoolean(USE_RELATIVE_URLS);
    }

    public Double getDmsMapDefaultDistance() {
        return readDouble(DMS_MAP_DEFAULT_DISTANCE);
    }

    public String getDmsHost() {
        return isRelativeURLs() ? "" : readString(DMS_HOST);
    }

    public String getCmsBasePath() {
        return isRelativeURLs() ? "" : readString(CMS_BASE_PATH);
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

    public String getYoutubeApiKey() {
        return readString(YOUTUBE_API_KEY);
    }

    public String getYoutubeApiBase() {
        return readString(YOUTUBE_API_BASE);
    }

    public String getDmsInternalPath() {
        return readString(DMS_INTERNAL_PATH);
    }

    public Boolean getNavigationCache() {
        return readBoolean(NAVIGATION_CACHE);
    }

    public Integer getContentCacheRetention() {
        //Note that the retention period is defined in seconds and java.util.Date measures the time in seconds
        return readInteger(CONTENT_CACHE_RETENTION_PERIOD) * 1000;
    }

    public boolean isContentCacheEnabled() {
        return readBoolean(CONTENT_CACHE_ENABLED);
    }

    public boolean isSnippetCacheEnabled() {
        return readBoolean(SNIPPET_CACHE);
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

    public Charset getDmsEncoding() {
        String value = getProperty(DMS_DATA_ENCODING);
        try {
            if (!Contract.isEmpty(value)) {
                return Charset.forName(value);
            }
        } catch (Exception e) {
            logger.warn("{} is not a valid value for the property {}", value, DMS_DATA_ENCODING);
        }
        return StandardCharsets.UTF_8;
    }

    public String getMapMultipolygons() {
        return readString(MAP_MULTIPOLYGONS);
    }
}
