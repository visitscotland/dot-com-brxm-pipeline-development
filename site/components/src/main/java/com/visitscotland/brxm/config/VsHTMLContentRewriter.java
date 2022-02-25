package com.visitscotland.brxm.config;

import org.hippoecm.hst.configuration.hosting.Mount;
import org.hippoecm.hst.content.rewriter.impl.SimpleContentRewriter;
import org.hippoecm.hst.core.request.HstRequestContext;

import javax.jcr.Node;

/**
 * For reference: https://documentation.bloomreach.com/14/library/concepts/rewriting-rich-text-field-runtime/hst-2-rewriting-rich-text-field-runtime.html
 */
public class VsHTMLContentRewriter
        extends SimpleContentRewriter {

    private final HTMLtoVueTransformer transformer;

    public VsHTMLContentRewriter (){
        transformer = VsComponentManager.get(HTMLtoVueTransformer.class);
    }

    @Override
    public String rewrite(final String html, final Node node,
                          final HstRequestContext requestContext,
                          final Mount targetMount) {

        String hippoHtml = super.rewrite(html, node, requestContext, targetMount);
        return transformer.process(hippoHtml);
    }
}
