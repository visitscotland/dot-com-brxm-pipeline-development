package com.visitscotland.brxm.rest;

import com.visitscotland.brxm.services.CommonUtilsService;
import com.visitscotland.brxm.utils.Language;
import com.visitscotland.brxm.utils.Properties;
import org.hippoecm.hst.jaxrs.services.AbstractResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


@Path("/internal/")
public class InternalResource extends AbstractResource {

    private static final Logger logger = LoggerFactory.getLogger(InternalResource.class);

    static final String NO_MATCH = "<!-- No match -->";

    private final CommonUtilsService utils;

    private final Properties properties;

    public InternalResource(CommonUtilsService utils, Properties properties) {
        this.utils = utils;
        this.properties = properties;
    }

    @GET
    @Path("/")
    public Response health() {
        return Response.ok().entity("status OK!").build();
    }

    @GET
    @Path("/{fragment}")
    public Response fragment(@Context HttpServletRequest request,
                             @PathParam("fragment") String fragment,
                             @QueryParam("root-path") String rootPath,
                             @QueryParam("sso") String sso,
                             @QueryParam("vs-locale-ctx") String locale) {
        // It is not possible to difference between a non send parameter and a parameter without value.
        // This is the reason why the parameter is extracted from the request instead of using injection.
        boolean external = request.getParameterMap().containsKey("external");

        String url = buildUrl(external, rootPath, sso, locale);
        try {
            String body = getFragment(utils.requestUrl(url), fragment);
            if (NO_MATCH.equals(body)) {
                return Response.status(Response.Status.NOT_FOUND).entity(body).build();
            } else {
                return Response.ok().entity(body).build();
            }
        } catch (Exception e) {
            logger.error("Error while requesting the data to {} ", url, e);
            return Response.serverError().entity("Error while handling the request. Please contact Helpdesk at " + properties.getHelpdeskEmail()).build();
        }
    }

    /**
     * Build the URL for the internal page rendered in freemarker.
     */
    private String buildUrl(boolean external,
                            String rootPath,
                            String sso,
                            String locale) {
        Map<String, String> parameters = new HashMap<>();
        String languageSubsite = "";
        if (external) {
            parameters.put("external", "true");
        }
        if (rootPath != null) {
            parameters.put("root-path", rootPath);
        }
        if (sso != null) {
            parameters.put("sso", sso);
        }

        if (locale != null) {
            languageSubsite = Language.getLanguageForLocale(Locale.forLanguageTag(locale)).getCMSPathVariable();
        }

        return  properties.getCmsBasePath() + languageSubsite + "/internal" +
                utils.buildQueryString(parameters, StandardCharsets.UTF_8.name());
    }

    private String getFragment(String content, String fragment) {
        final String open = "<internal-" + fragment + ">";
        final String close = "</internal-" + fragment + ">";

        try {
            return content.substring(content.indexOf(open) + open.length(),
                    content.indexOf(close));
        } catch (Exception e) {
            logger.warn("No coincidence has been found for the fragment {}", fragment);
            return NO_MATCH;
        }
    }
}