package com.visitscotland.brxm.config;

import org.hippoecm.hst.configuration.hosting.Mount;
import org.hippoecm.hst.content.rewriter.HtmlCleanerFactoryBean;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.hippoecm.hst.pagemodelapi.v10.content.rewriter.HtmlContentRewriter;
import org.htmlcleaner.HtmlCleaner;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

/**
 * For reference: https://documentation.bloomreach.com/14/library/concepts/rewriting-rich-text-field-runtime/hst-2-rewriting-rich-text-field-runtime.html
 */
public class DeliveryAPIContentRewriter extends HtmlContentRewriter {

    static final String NESTED = "visitscotland:nested";
    private final HTMLtoVueTransformer transformer;

    public DeliveryAPIContentRewriter() throws Exception {
        this(new HtmlCleanerFactoryBean().getObject(), VsComponentManager.get(HTMLtoVueTransformer.class));
    }

    public DeliveryAPIContentRewriter(HtmlCleaner htmlCleaner) throws Exception {
        this(htmlCleaner, VsComponentManager.get(HTMLtoVueTransformer.class));
    }

    public DeliveryAPIContentRewriter(HtmlCleaner htmlCleaner, HTMLtoVueTransformer transformer) throws Exception {
        super(htmlCleaner);
        this.transformer = transformer;
    }

    @Override
    public String rewrite(final String html, final Node node,
                          final HstRequestContext requestContext,
                          final Mount targetMount) {
        final String hippoHtml = super.rewrite(html, node, requestContext, targetMount);
        final String parentDocument;
        boolean nested = false;
        try {
            if (node != null) {
                parentDocument = node.getPath();
                if (parentDocument.endsWith("/visitscotland:copy") && parentDocument.contains("/visitscotland:paragraph")
                        && node.getParent().getParent().hasProperty(NESTED)) {
                    nested = node.getParent().getParent().getProperty(NESTED).getBoolean();
                }
            } else {
                parentDocument = "DeliveryAPIContentRewriter";
            }

            return transformer.process(hippoHtml, parentDocument, nested);
        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        }
    }

}
