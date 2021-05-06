package com.visitscotland.brxm.utils;

import org.hippoecm.hst.component.support.bean.BaseHstComponent;
import org.hippoecm.hst.container.RequestContextProvider;
import org.hippoecm.hst.content.beans.ObjectBeanManagerException;
import org.hippoecm.hst.content.beans.query.exceptions.QueryException;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.linking.HstLink;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.hippoecm.hst.core.request.ResolvedSiteMapItem;
import org.onehippo.forge.selection.hst.contentbean.ValueList;
import org.onehippo.forge.selection.hst.util.SelectionUtil;
import org.springframework.stereotype.Component;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import java.util.Map;

/**
 * Set of utilities related with Hippo that from the whole environment to be running in order to work
 */
@Component
public class HippoUtilsService {

    /**
     * Dummy HstCompoment used to access stateless methods on the ComponentClass
     */
    final BaseHstComponent hstComponent;

    public HippoUtilsService(){
        hstComponent = new BaseHstComponent();
    }

    static HippoUtilsService instance;

    /**
     * Convert and HstLink or a HippoBean into a URL String
     *
     * TODO: Verify that the item can be rendered as a page and return null when it doesn't
     *
     * @param document Page document
     *
     * @return URL for the page that renders the document or null when it cannot be rendered as a page.
     */
    @NonTestable(NonTestable.Cause.BRIDGE)
    public String createUrl(HippoBean document){
        final boolean FULLY_QUALIFIED = true;
        HstRequestContext requestContext = RequestContextProvider.get();

        HstLink link = requestContext.getHstLinkCreator().create(document, requestContext);
        return link.toUrlForm(requestContext, FULLY_QUALIFIED);
    }

    /**
     * Return a HippoBean from the path of the Node
     */
    @NonTestable(NonTestable.Cause.BRIDGE)
    public <T extends HippoBean> T getDocumentFromNode(String path) throws QueryException, ObjectBeanManagerException, RepositoryException {
        return getDocumentFromNode(RequestContextProvider.get().getSession().getNode(path));
    }

    /**
     * Return a HippoBean from the Node
     */
    @NonTestable(NonTestable.Cause.BRIDGE)
    public <T extends HippoBean> T getDocumentFromNode(Node jcrNode) throws QueryException, ObjectBeanManagerException {
        HippoBean bean = RequestContextProvider.get().getQueryManager()
                .createQuery(jcrNode).execute().getHippoBeans().nextHippoBean();

        return (T) bean.getObjectConverter().getObject(bean.getNode());
    }

    @NonTestable(NonTestable.Cause.BRIDGE)
    public HippoBean getBeanForResolvedSiteMapItem(HstRequest request, ResolvedSiteMapItem sitemapItem) {
        return hstComponent.getBeanForResolvedSiteMapItem(request, sitemapItem);
    }

    /**
     * Extract a parameter from the URL (without namespace)
     *
     * @param request HstRequest
     * @param parameter Name of the parameter
     *
     * @return value of the query parameter or null if such parameter hasn't been defined
     */
    @NonTestable(NonTestable.Cause.BRIDGE)
    public String getParameterFromUrl(HstRequest request, String parameter){
        return request.getRequestContext().getServletRequest().getParameter(parameter);
    }

    /**
     * Retrieves a ValueList as a Map
     *
     * New value lists can be configured in essentials and must be added to META-INF/valueList.xml
     * @param valueListIdentifier The identifier as specified in the value list YAML
     * @return A mapping from the value list key to the value list value
     */
    @NonTestable(NonTestable.Cause.BRIDGE)
    public Map<String, String> getValueMap(String valueListIdentifier) {
        ValueList valueList = SelectionUtil.getValueListByIdentifier(valueListIdentifier, RequestContextProvider.get());
        return SelectionUtil.valueListAsMap(valueList);
    }
}
