package com.visitscotland.brxm.validator;

import org.onehippo.cms.services.validation.api.ValidationContext;
import org.onehippo.cms.services.validation.api.ValidationContextException;
import org.onehippo.cms.services.validation.api.Validator;
import org.onehippo.cms.services.validation.api.Violation;
import org.onehippo.repository.util.JcrConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import java.util.*;
import java.util.stream.StreamSupport;

/**
 * Ensure that a list of links is unique. Must be configured on a hipposysedit:nodetype, with targetField property
 * specifying the field to be validated
 *
 * The configuration is
 *  <ul>
 *      <li>targetField - The name of the child node that contains the list of links</li>
 *      <li>linkIdField (optional) - The field or node that must be unique. If a node, then the hippo:docbase property is
 *                                   used for uniqueness. If not provided, the hippo:docbase on the targetField node is used</li>
 *  </ul>
 *
 */
public class UniqueLinksValidator implements Validator<Node> {

    private static final String TARGET_FIELD = "targetField";
    private static final String LINK_ID_FIELD = "linkIdField";
    private static final String DEFAULT_HIPPO_LINK = "hippo:docbase";
    private final String targetField;
    private final String linkIdProperty;
    private static final Logger logger = LoggerFactory.getLogger(UniqueLinksValidator.class);

    public UniqueLinksValidator(final Node config) {
        try {
            if (!config.hasProperty(TARGET_FIELD)) {
                throw new ValidationContextException("A targetField must be provided for UniqueLinksValidator");
            }
            targetField = config.getProperty(TARGET_FIELD).getString();
            linkIdProperty = config.hasProperty(LINK_ID_FIELD) ? config.getProperty(LINK_ID_FIELD).getString() : DEFAULT_HIPPO_LINK;
        } catch (RepositoryException ex) {
            throw new ValidationContextException("Cannot read required properties for the UniqueLinksValidator. Verify the node ", ex);
        }
    }

    @Override
    public Optional<Violation> validate(ValidationContext validationContext, Node node) {
        try {
            NodeIterator linkNodes = node.getParent().getNodes(targetField);
            boolean seenLinkId = false;
            String targetLinkId = getLinkId(node);
            // ID given to link that is not set. Mandatory validator takes care of ensuring links are set
            if (targetLinkId == null || targetLinkId.equals(JcrConstants.ROOT_NODE_ID)) {
                return Optional.empty();
            }

            while (linkNodes.hasNext()) {
                Node linkNode = linkNodes.nextNode();
                String id = getLinkId(linkNode);
                if (id == null) {
                    logger.error("Link on node `{}` does not have a linkIdProperty `{}` or property with hippo:docbase",node.getPath(), linkIdProperty);
                    continue;
                }
                if (id.equals(targetLinkId)) {
                    if (seenLinkId) {
                        return Optional.of(validationContext.createViolation());
                    } else {
                        seenLinkId = true;
                    }
                }
            }
        } catch (RepositoryException ex) {
            logger.error("Repository error during unique links validator", ex);
        }
        return Optional.empty();
    }

    /**
     * This method takes care of nested structure where the hippo:mirror is deep down inside the document
     */
    private String getLinkId(Node linkNode) throws RepositoryException {
        String id = null;
        if (linkNode.hasNode(linkIdProperty)) {
            Node node = linkNode.getNode(linkIdProperty);
            while (id == null) {
                if (node.hasProperty(DEFAULT_HIPPO_LINK)){
                    id = node.getProperty(DEFAULT_HIPPO_LINK).getString();
                } else if (node.getNodes().getSize() == 1) {
                    node = node.getNodes().nextNode();
                } else {
                    logger.error("LinkValidator does not support the structure of the node `{}`", node.getPath());
                    id = JcrConstants.ROOT_NODE_ID;
                }
            }
        }

        if (id == null || id.equals(JcrConstants.ROOT_NODE_ID) && linkNode.hasProperty(linkIdProperty)) {
            return linkNode.getProperty(linkIdProperty).getString();
        }
        return id;
    }
}