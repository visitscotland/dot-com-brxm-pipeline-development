package com.visitscotland.brxm.rest;

import com.visitscotland.brxm.config.VsComponentManager;
import com.visitscotland.brxm.hippobeans.*;
import com.visitscotland.brxm.model.sitemap.Sitemap;
import com.visitscotland.brxm.model.sitemap.SitemapEntry;
import com.visitscotland.brxm.services.CommonUtilsService;
import com.visitscotland.brxm.services.DocumentUtilsService;
import com.visitscotland.brxm.services.LinkService;
import com.visitscotland.brxm.utils.HippoUtilsService;
import com.visitscotland.brxm.utils.Properties;
import com.visitscotland.brxm.utils.VsException;
import org.hippoecm.hst.configuration.hosting.Mount;
import org.hippoecm.hst.configuration.hosting.VirtualHosts;
import org.hippoecm.hst.container.RequestContextProvider;
import org.hippoecm.hst.content.beans.query.HstQuery;
import org.hippoecm.hst.content.beans.query.HstQueryManager;
import org.hippoecm.hst.content.beans.query.HstQueryResult;
import org.hippoecm.hst.content.beans.query.exceptions.QueryException;
import org.hippoecm.hst.content.beans.query.filter.Filter;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoBeanIterator;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.hippoecm.hst.jaxrs.services.AbstractResource;
import org.hippoecm.hst.util.HstRequestUtils;
import org.hippoecm.hst.util.HstSiteMapUtils;
import org.hippoecm.hst.util.PathUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Note: These endpoint don't use Spring but JAX-RS for mapping to the main application
 *
 * https://en.wikipedia.org/wiki/Jakarta_RESTful_Web_Services#Implementations
 */
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
    @Produces()
    public Response health() {
        return Response.ok().entity("status OK!").build();
    }

    @GET
    @Path("/pages/")
    @Produces("application/json")
    public Response fragment(@Context HstRequest request,
                             @DefaultValue("hst:root") @QueryParam("channel") String locale) {
        try {
            return Response.ok().entity(getPages(locale, Destination.class, Listicle.class, General.class, Itinerary.class))
                    .build();
        } catch (VsException e){
            return Response.serverError().build();
        }
    }


    private List<SitemapEntry> getPages(String channel, Class<? extends Page>... types){
        List<SitemapEntry> entries = new ArrayList<>();
        try {
            HippoBeanIterator iterator = findAllPages(channel, types);

            while (iterator.hasNext()){
                entries.add(getSitemapEntry(channel, (Page) iterator.nextHippoBean()));
            }

        } catch (RepositoryException | QueryException e) {
            throw new RuntimeException(e);
        }

        return entries;
    }

    private HippoBeanIterator findAllPages(String channel, Class<? extends Page>... types) throws RepositoryException, QueryException {
        HstRequestContext requestContext = RequestContextProvider.get();
        HstQueryManager hstQueryManager =
                getHstQueryManager(requestContext.getSession(),
                        requestContext);

        String mountContentPath = getMountforChannel(channel).getContentPath();
        Node mountContentNode = requestContext.getSession().getRootNode()
                .getNode(PathUtils.normalizePath(mountContentPath));
        HstQuery hstQuery = hstQueryManager.createQuery(mountContentNode, types);
        Filter filter = hstQuery.createFilter();
        //TODO Use Constants for these fields
        //Only pages where the NoIndex field is unselected
        filter.addEqualTo("visitscotland:seoNoIndex", "false");
        // Only documents of published type (green)
        filter.addEqualTo("hippostd:state", "published");
        // Only documents that are actually live and (opposing to those taken offline)
        filter.addEqualTo("hippostd:stateSummary", "live");
        hstQuery.setFilter(filter);

        HstQueryResult result = hstQuery.execute();

        return result.getHippoBeans();
    }

    private SitemapEntry getSitemapEntry(String channel, Page page) throws RepositoryException {
        SitemapEntry entry = new SitemapEntry();

        entry.setUrl(getUrl(channel, page));
        entry.setLastModification(getLastModification(page));

        return entry;
    }

    private final static String LAST_MODIFICATION_DATE = "hippostdpubwf:lastModificationDate";

    private String getUrl(String channel, Page page) {
        if (page.getName().equals("content")) {
            return RequestContextProvider.get().getHstLinkCreator().create(page.getNode(), getMountforChannel(channel))
                    .toUrlForm(RequestContextProvider.get(), true);
        } else {
            logger.info("The following document cannot be rendered as a page: {}", page.getPath());
            return null;
        }
    }
    private Date getLastModification(Page page) throws RepositoryException {
        // Set Last Mod
        Calendar lastMod = page.getNode().getProperty(LAST_MODIFICATION_DATE).getDate();
        for (BaseDocument doc : VsComponentManager.get(DocumentUtilsService.class).getSiblingDocuments(page,
                BaseDocument.class, page.getChildJcrTypes())){
            Calendar childMod = doc.getNode().getProperty(LAST_MODIFICATION_DATE).getDate();
            if (childMod.after(lastMod)){
                lastMod = childMod;
            }
            if (doc instanceof Day){
                for (BaseDocument stop: ((Day)doc).getStops()){
                    childMod = stop.getNode().getProperty(LAST_MODIFICATION_DATE).getDate();
                    if (childMod.after(lastMod)){
                        lastMod = childMod;
                    }
                }
            }
        }
        return lastMod.getTime();
    }

    /**
     * TODO Move to Utils
     * @param channel
     * @return
     */
    public Mount getMountforChannel(String channel){
        HstRequestContext context = RequestContextProvider.get();
        String currentHost = context.getVirtualHost().getHostGroupName();

        for (Mount mount : context.getVirtualHost().getVirtualHosts().getMountsByHostGroup(currentHost)) {
            if (mount.getParent() == null){
                if (channel.equals("hst:root")){
                    return mount;
                }
            } else if (channel.equals(mount.getName())){
                return mount;
            }
        }
        logger.warn("The mount point for the channel {} was not located. Defaulting to English", channel);
        throw new VsException("The requested channel was not found");
    }
}
