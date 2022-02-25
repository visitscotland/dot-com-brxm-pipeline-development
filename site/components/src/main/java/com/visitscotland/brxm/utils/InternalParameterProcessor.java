package com.visitscotland.brxm.utils;

import com.visitscotland.brxm.model.LocalizedURL;
import com.visitscotland.brxm.services.ResourceBundleService;
import com.visitscotland.utils.Contract;
import org.apache.cxf.jaxrs.impl.UriBuilderImpl;
import org.hippoecm.hst.core.component.HstRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Component
public class InternalParameterProcessor {

    private static final Logger logger = LoggerFactory.getLogger(InternalParameterProcessor.class);

    private ResourceBundleService bundle;
    private HippoUtilsService utils;

    public InternalParameterProcessor(ResourceBundleService bundle, HippoUtilsService utils) {
        this.bundle = bundle;
        this.utils = utils;
    }

    public static final String PARAM_SSO = "sso";
    public static final String PARAM_ROOT_PATH = "root-path";
    public static final String PARAM_EXTERNAL = "external";

    public static final String PATH_PLACEHOLDER = "[PATH-PLACEHOLDER]";

    public static final String FULLY_QUALIFIED_URLS = "fullyQualified";
    public static final String LOGINREDIRECT_PARAMETERS = "loginredirectParameters";
    public static final String GLOBAL_MENU_URLS = "placeholerLocalizedURLs";

    public void processParameters(HstRequest request) {
        final StringBuilder returnUrl = new StringBuilder("returnurl=");
        String external = utils.getParameterFromUrl(request, PARAM_EXTERNAL);
        String authority = getAuthority(request);
        String sso = utils.getParameterFromUrl(request, PARAM_SSO);

        if (authority != null) {
            returnUrl.append(authority).append("/").append(PATH_PLACEHOLDER);
            request.getRequestContext().setModel(FULLY_QUALIFIED_URLS, "true");
        } else if (external != null) {
            returnUrl.append(composeNonExistingURL(request.getLocale(), null, request));
            request.getRequestContext().setModel(FULLY_QUALIFIED_URLS, "true");
        } else {
            returnUrl.append("/").append(PATH_PLACEHOLDER);
        }

        if (sso != null) {
            returnUrl.append("&id=").append(sso);
        }

        request.setModel(LOGINREDIRECT_PARAMETERS, returnUrl.toString());
    }

    /**
     * This piece of logic has been based on visitscotland (legacy project). The logic of this parameter was
     * spread between header.jsp & page-header.tag. The logic has been updated in order to fix a couple of issues
     * related with url
     * <p>
     * Some refactoring has been done to while copying the logic over in order to  fix some bugs and to make the logic a
     * bit more simple. I am not completely sure that this logic is useful but WebOps has advice to take a cautious
     * approach and keep the functionality as It was (even though the specification doesn't states how these bits)
     *
     * @return a URL containing just the schema and the domain (host) or {@code null} if the URL defined in root-path
     * is malformed
     * @see <a href="https://en.wikipedia.org/wiki/URL>URL - Wikipedia</a>
     */
    String getAuthority(HstRequest request) {
        StringBuilder sb = new StringBuilder();
        String rootPath = utils.getParameterFromUrl(request, PARAM_ROOT_PATH);

        if (Contract.isEmpty(rootPath)) {
            return null;
        }

        try {
            URI uri = new URI(rootPath);

            if (uri.getHost() != null) {
                if (uri.getScheme() != null) {
                    sb.append(uri.getScheme()).append(":");
                }

                sb.append("//").append(uri.getAuthority());
                return sb.toString();
            }
        } catch (URISyntaxException e) {
            // Do Nothing. The Error is already logged before the return statement.
        }
        logger.error("The value {} for the root-path parameter couldn't be processed for the request {}",
                rootPath, request.getRequestURI());
        return null;
    }


    /**
     * Add localized URLS.
     * <p>
     * Please note that the URLs on the header need to be relative since they related to the new host is defined with
     * the parameter root-path.
     * <p>
     * For example: If the user is in http://demo.visitscotland.com/demo-page, the URL for the Spanish version should be
     * http://demo.visitscotland.com/es/demo-page instead of https://www.visitscotland.com/es/demo-page
     */
    public void addLocalizedURLs(HstRequest request) {
        List<LocalizedURL> translatedURLs = new ArrayList<>(Language.values().length);
        String authority = getAuthority(request);

        for (Language language : Language.values()) {
            LocalizedURL lan = new LocalizedURL();
            lan.setLocale(language.getLocale());
            lan.setLanguage(language.getLocale().getLanguage());
            lan.setDisplayName(bundle.getResourceBundle("universal", lan.getLanguage(), request.getLocale()));
            lan.setUrl(composeNonExistingURL(language.getLocale(), authority, request));

            translatedURLs.add(lan);
        }
        request.getRequestContext().setModel(GLOBAL_MENU_URLS, translatedURLs);
    }

    /**
     * Composes a non existing URL with the Placeholder.
     */
    private String composeNonExistingURL(Locale locale, String authority, HstRequest request) {
        StringBuilder uri = new StringBuilder();

        if (authority != null){
            uri.append(authority);
        } else if (utils.getParameterFromUrl(request, PARAM_EXTERNAL) != null) {
            uri.append(new UriBuilderImpl()
                    .scheme(request.getScheme())
                    .host(request.getServerName())
                    .port(request.getServerPort())
                    .path(request.getRequestContext().getBaseURL().getContextPath())
                    .build().normalize());
        }

        if (locale != null && Language.getLanguageForLocale(locale).getCMSPathVariable().length() != 0) {
            uri.append(Language.getLanguageForLocale(locale).getCMSPathVariable());
        }

        uri.append("/").append(PATH_PLACEHOLDER);

        return uri.toString();
    }
}
