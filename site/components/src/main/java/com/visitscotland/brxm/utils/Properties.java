package com.visitscotland.brxm.utils;

import com.visitscotland.brxm.services.ResourceBundleService;
import com.visitscotland.utils.Contract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

@Component
public class Properties {

    private static final Logger logger = LoggerFactory.getLogger(Properties.class.getName());

    public static final String INSTAGRAM_API = "instagram.api";
    public static final String INSTAGRAM_TOKEN ="instagram.accesstoken";
    public static final String INSTAGRAM_URL ="instagram.post-url";
    public static final String LOCALHOST = "localhost";
    public static final String HELPDESK_EMAIL = "helpdesk-email";
    public static final String DMS_HOST = "dms.host";
    public static final String DMS_ENCODING = "dms.encoding";
    public static final String DMS_TOKEN = "dms.token";
    public static final String DMS_TIMEOUT = "dms.timeout";
    public static final String DMS_TRIES = "dms.tries";
    public static final String DMS_SLEEP_TIME = "dms.sleep-time";
    public static final String DMS_MAP_DEFAULT_DISTANCE = "dms.default-distance";
    private static final String CONFIGURATION = "config.cms";
    private static final String IKNOW_COMMUNITY_URL = "iknow-community.url";
    private static final String IKNOW_COMMUNITY_TAGGED_DISCUSSION= "iknow-community.tagged-discussion";

    private final ResourceBundleService bundle;

    public Properties(ResourceBundleService bundle){
        this.bundle = bundle;
    }

    public String getLocalhost() {
        return readString(LOCALHOST);
    }

    public String getInstagramApi() {
        return readString(INSTAGRAM_API);
    }

    public String getInstagramURL() {
        return readString(INSTAGRAM_URL);
    }

    public String getInstagramToken() {
        return readString(INSTAGRAM_TOKEN);
    }

    public String getHelpdeskEmail() {
        return readString(HELPDESK_EMAIL);
    }

    public String getDmsHost() {
        return readString(DMS_HOST);
    }

    public String getDmsMapDefaultDistance() {
        return readString(DMS_MAP_DEFAULT_DISTANCE);
    }



    public String getDmsToken() {
        return readString(DMS_TOKEN);
    }

    public Integer getDmsTimeout() {
        return readInteger(DMS_TIMEOUT);
    }

    public Integer getDmsTries() {
        return readInteger(DMS_TRIES);
    }

    public Integer getDmsWaitTime() {
        return readInteger(DMS_SLEEP_TIME);
    }

    public String getIknowCommunityUrl() {
        return readString(IKNOW_COMMUNITY_URL);
    }

    public String getIknowCommunityTaggedDiscussion() {
        return readString(IKNOW_COMMUNITY_TAGGED_DISCUSSION);
    }


    //TODO Test
    public Charset getDmsEncoding() {
        String value = bundle.getResourceBundle(CONFIGURATION, DMS_ENCODING, Locale.UK);
        try{
            if (!Contract.isEmpty(value)) {
                return Charset.forName(value);
            }
        } catch (Exception e){
            logger.warn("{} is not a valid value for the property {}", value, DMS_ENCODING);
        }
        return StandardCharsets.UTF_8;
    }

    //TODO Test
    private String readString(String key){
        String value = bundle.getResourceBundle(CONFIGURATION, key, Locale.UK);

        if (Contract.isEmpty(value)) {
            logger.warn("The property {} hasn't been set in the resourceBundle {}", key, CONFIGURATION);
        } else if (value.startsWith("$")){
            String env = System.getenv(value.substring(1));
            if (env != null){
                return env;
            }
        } else  {
            return value;
        }

        return "";
    }

    //TODO test
    private Integer readInteger(String key){
        String value = bundle.getResourceBundle(CONFIGURATION, key, Locale.UK);
        try {
            if (Contract.isEmpty(value)){
                logger.warn("The property {} hasn't been set in the resourceBundle {}", key, CONFIGURATION);
            } else {
                return Integer.valueOf(value);
            }
        } catch (NumberFormatException nfe){
            logger.error("The property value of the property {} cannot be casted to Integer. '{}' is not allowed.", key,value);
        }
        return 0;
    }
}
