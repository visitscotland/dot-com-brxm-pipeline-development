package com.visitscotland.brxm.dms;

import com.visitscotland.brxm.utils.Language;
import com.visitscotland.brxm.utils.Properties;
import com.visitscotland.utils.Contract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.*;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.Locale;

/**
 * Proxies connections to the DMS and halts connections for DMS when there is low availability
 */
@Component
public class DMSProxy {

    private static final Logger logger = LoggerFactory.getLogger(DMSProxy.class);

    static final String HEADER = "VS-API-Key";

    /**
     * Registers the time of the last failure and blocks subsequent requests
     */
    static Long lastRegisteredFailure = null;

    // TODO private final
    Properties properties;

    public DMSProxy(Properties properties){
        this.properties = properties;
    }


    /**
     * Request information to the DMS and returns the body as String.
     *
     * In case of timeout, it tries it several times and finally blocks subsequent requests for a period of time when
     * all retries fail.
     *
     * @param path
     *
     * @return Body of the end point or null if status code not 200 or 300
     *
     * @throws IOException
     */
    public String request(String path) {
        if (!Contract.isEmpty(properties.getDmsHost()) && path.startsWith(properties.getDmsHost())){
            //TODO Fix ProductSearchBuilder to make it build relative URLs
            return request(path.substring(properties.getDmsHost().length()), null);
        }

        return request(path, null);
    }

    public synchronized String request(String path, Locale locale) {
        String languageParam = "";
        // Checks if there is a lock and if has to be released
        if (lastRegisteredFailure != null && lastRegisteredFailure + properties.getDmsWaitTime() < new Date().getTime()){
            lastRegisteredFailure = null;
        }

        if (lastRegisteredFailure == null && path.startsWith("/")) {
            Language lang = Language.getLanguageForLocale(locale);
            if (lang != Language.ENGLISH){
                if (path.contains("?")){
                    languageParam = "&locale=" + lang.getLocale().getLanguage();
                } else {
                    languageParam = "?locale=" + lang.getLocale().getLanguage();
                }
            }
            return request(path + languageParam, properties.getDmsTries());
        } else {
            return null;
        }
    }

    public static synchronized void registerFailure(){
        lastRegisteredFailure = new Date().getTime();
    }

    /**
     * Request a page and return the body as String
     */
    private String request(String path, int retries) {
        try {
            HttpURLConnection dmsConnection = openConnection(properties.getDmsHost() + path);
            dmsConnection.setConnectTimeout(properties.getDmsTimeout());
            dmsConnection.setRequestProperty(HEADER, properties.getDmsToken());
            dmsConnection.setRequestMethod("GET");

            int code = dmsConnection.getResponseCode();

            if (code < 300) {
                return requestContent(dmsConnection, properties.getDmsEncoding());
            } else if (code < 400) {
                logger.warn("The request {} return a {} status code. Please correct the request", path, code);
                HttpURLConnection.setFollowRedirects(true);
                return requestContent(dmsConnection, properties.getDmsEncoding());
            }
        } catch (SocketTimeoutException e) {
            if (retries > 1) {
                return request(path, retries - 1);
            } else {
                //TODO SHOULD we print the URL?
                logger.error("The DMS Service couldn't be reached. Holding all subsequent connections for {} ms ", properties.getDmsWaitTime());
                DMSProxy.registerFailure();
            }
        } catch (IOException e){
            logger.error(e.getLocalizedMessage(), e);
        }
        return null;
    }

    /**
     * Retrieves the body of the request.
     * <p>
     * Note: This method does not verifies that the status code for the request, the strategy about time out or the
     * request method. All that information should be set in the connection.
     *
     * @param connection: A ready-to-consume connection to the resource
     * @param encoding:   Enconding used to read the resource
     * @return Content of the resource
     * @throws IOException propagates any exception
     */
    public String requestContent(URLConnection connection, Charset encoding) throws IOException {
        try (InputStream input = connection.getInputStream()) {
            final BufferedReader br = new BufferedReader(new InputStreamReader(input, encoding));
            final StringBuilder sb = new StringBuilder();
            int cp;

            while ((cp = br.read()) != -1) {
                sb.append((char) cp);
            }

            return sb.toString();
        }
    }

    /**
     * This method allow us to test this class
     */
    HttpURLConnection openConnection(String url) throws IOException {
        return (HttpURLConnection) new URL(url).openConnection();
    }
}
