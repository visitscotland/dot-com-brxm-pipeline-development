package com.visitscotland.brxm.validator;

import org.onehippo.cms.services.validation.api.ValidationContext;
import org.onehippo.cms.services.validation.api.Validator;
import org.onehippo.cms.services.validation.api.Violation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import java.util.Optional;

/**
 * jcr:name = visitscotland:featured-widget-validator
 */
public class FeaturedWidgetValidator implements Validator<Node> {

    private static final String CMS_LINK_TYPE = "visitscotland:CMSLink";
    private static final Logger logger = LoggerFactory.getLogger(FeaturedWidgetValidator.class);

    @Override
    public Optional<Violation> validate(ValidationContext validationContext, Node node) {
        try {
            NodeIterator itemIterator = node.getNodes("visitscotland:items");
            Node firstNode = itemIterator.nextNode();
            boolean isCmsLink = firstNode.isNodeType(CMS_LINK_TYPE);
            if (!isCmsLink && itemIterator.getSize() > 1) {
                // Can't have more than one featured event
                return Optional.of(validationContext.createViolation("multipleFeaturedEvent"));
            } else if (!isCmsLink) {
                if (!firstNode.getProperty("visitscotland:productType").getString().equals("even")) {
                    return Optional.of(validationContext.createViolation("notEvent"));
                }
            } else {
                while (itemIterator.hasNext()) {
                    Node itemNode = itemIterator.nextNode();
                    if (!itemNode.isNodeType(CMS_LINK_TYPE)) {
                        // Can not have ProductSearch mixed with CMSLink
                        return Optional.of(validationContext.createViolation("mixedFeaturedWidget"));
                    }
                }
            }
        } catch (RepositoryException ex) {
            logger.error("Repository error during validation", ex);
        }
        return Optional.empty();
    }
}
