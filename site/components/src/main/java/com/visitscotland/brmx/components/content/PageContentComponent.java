package com.visitscotland.brmx.components.content;

import com.visitscotland.brmx.beans.*;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.onehippo.cms7.essentials.components.EssentialsContentComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class PageContentComponent<TYPE extends Page> extends EssentialsContentComponent {

    private static final Logger logger = LoggerFactory.getLogger(PageContentComponent.class);

    public final String DOCUMENT = "document";
    public final String EDIT_PATH = "path";


    @Override
    public void doBeforeRender(HstRequest request, HstResponse response) {
        super.doBeforeRender(request, response);

        addDocumentPath(request);
    }

    /**
     * Return the document from the request
     *
     * @param request HstRequest
     *
     * @return the master document of
     */
    protected TYPE getDocument(HstRequest request) {
        if (request.getAttribute(DOCUMENT) instanceof Page){
            return (TYPE) request.getAttribute(DOCUMENT);
        } else {
            logger.error("The master document is not an instance of Page.", new ClassCastException());
            return null;
        }

    }

    /**
     * Add the document path that will be used as path by default when creating documents. So for Example, the prompted
     * path for the days of an itinenrary would be where the itinerary (masted document) lives
     *
     * @param request Request where the master document is defined and where the path will be added
     */
    private void addDocumentPath(HstRequest request) {
        final String ROOT_SITE = "/site/";

        if (request.getAttribute(DOCUMENT) instanceof BaseDocument){
            BaseDocument document = getDocument(request);
            //Extract the document path for the CMS Editor
            String path = document.getPath().substring(
                    document.getPath().indexOf(ROOT_SITE) + ROOT_SITE.length(),
                    document.getPath().indexOf("/content/content"));

            request.setAttribute(EDIT_PATH, path);
        }
    }
}
