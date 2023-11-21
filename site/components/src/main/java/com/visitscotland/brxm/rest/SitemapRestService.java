package com.visitscotland.brxm.rest;

import com.visitscotland.brxm.model.sitemap.Sitemap;
import com.visitscotland.brxm.model.sitemap.SitemapEntry;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


@Path("/sitemap/")
public class SitemapRestService extends AbstractResource {

    private static final Logger logger = LoggerFactory.getLogger(SitemapRestService.class);

    static final String NO_MATCH = "<!-- No match -->";

    private final CommonUtilsService utils;

    private final Properties properties;

    public SitemapRestService(CommonUtilsService utils, Properties properties) {
        this.utils = utils;
        this.properties = properties;
    }

    @GET
    @Path("/")
    public Response health() {
        return Response.ok().entity("status OK!").build();
    }

    @GET
    @Path("/pages/{locale}")
    public Response fragment(@Context HttpServletRequest request,
                             @DefaultValue("en") @PathParam("locale") String locale) {

        Sitemap sm = new Sitemap();

        sm.addPage(new SitemapEntry("https://www.visitscotland.com/", new Date()));
        sm.addPage(new SitemapEntry("https://www.visitscotland.com/demo", new Date(new Date().getTime() -2*24*60*60*1000)));

        return Response.ok().entity(sm).build();

    }



//    /**
//     * Build the URL for the internal page rendered in freemarker.
//     */
//    private String buildUrl(boolean external,
//                            String rootPath,
//                            String sso,
//                            String locale,
//                            String version
//    ) {
//        Map<String, String> parameters = new HashMap<>();
//        String languageSubsite = "";
//        if (external) {
//            parameters.put("external", "true");
//        }
//        if (rootPath != null) {
//            parameters.put("root-path", rootPath);
//        }
//        if (sso != null) {
//            parameters.put("sso", sso);
//        }
//        if (version == null) {
//            parameters.put("version", properties.getDefaultCssVersion());
//        } else {
//            parameters.put("version", version);
//        }
//
//        if (locale != null) {
//            languageSubsite = Language.getLanguageForLocale(Locale.forLanguageTag(locale)).getPathVariable();
//        }
//
//        return  properties.getCmsBasePath() + languageSubsite + "/internal" +
//                utils.buildQueryString(parameters, StandardCharsets.UTF_8.name());
//    }

//    private String getFragment(String content, String fragment) {
//        final String open = "<internal-" + fragment + ">";
//        final String close = "</internal-" + fragment + ">";
//
//        try {
//            return content.substring(content.indexOf(open) + open.length(),
//                    content.indexOf(close));
//        } catch (Exception e) {
//            logger.warn("No coincidence has been found for the fragment {}", fragment);
//            return NO_MATCH;
//        }
//    }
}
