package com.visitscotland.brxm.validator;

import org.onehippo.cms.services.validation.api.ValidationContext;
import org.onehippo.cms.services.validation.api.ValidationContextException;
import org.onehippo.cms.services.validation.api.Validator;
import org.onehippo.cms.services.validation.api.Violation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.*;
import java.util.Optional;

/**
 * Validator to set lower and upper limits on the number of items that can be selected in a multiselect control.
 * The configuration options are as follows
 *      minLength:      The min number of selected items allowed
 *      maxLength:      The max number of selected items allowed
 *      targetField:    The field to apply validation to. Can be either a property of a child node
 *
 * targetField must be supplied. Either minLength or maxLength must be supplied, or both.
 *
 * This validator MUST be applied at the document level, NOT the field level. This means it must be added to
 * hipposysedit:validators on the inner hipposysedit:nodetype
 *
 */
public class MultiValueNumberSelectedValidator implements Validator<Node> {
    private static final Logger logger = LoggerFactory.getLogger(MultiValueNumberSelectedValidator.class);

    private static final String MIN_LENGTH = "minLength";
    private static final String MAX_LENGTH = "maxLength";
    private static final String TARGET_FIELD = "targetField";

    private  Long minLength;
    private Long maxLength;
    private final String targetField;

    public MultiValueNumberSelectedValidator(final Node config) {
        try {
            if (!config.hasProperty(TARGET_FIELD)) {
                throw new ValidationContextException("A targetField must be provided for MultiValueNumberSelectedValidator");
            }
            targetField = config.getProperty(TARGET_FIELD).getString();
            if (config.hasProperty(MIN_LENGTH)) {
                minLength = config.getProperty(MIN_LENGTH).getLong();
            }
            if (config.hasProperty(MAX_LENGTH)) {
                maxLength = config.getProperty(MAX_LENGTH).getLong();
            }
            if (minLength == null && maxLength == null) {
                throw new ValidationContextException("A maxLength and/or a minLength property must be provided to validator");
            }
        } catch (RepositoryException ex) {
            throw new ValidationContextException("Cannot read required properties for the MultiValueNumberSelectedValidator. Verify the node ", ex);
        }
    }


    @Override
    public Optional<Violation> validate(ValidationContext validationContext, Node node) {
        Optional<Integer> itemCount = countNumberOfItems(node);
        if (!itemCount.isPresent()) {
            return Optional.empty();
        }
        int numItems = itemCount.get();
        if (minLength != null && numItems < minLength) {
            return Optional.of(validationContext.createViolation());
        }
        if (maxLength != null && numItems > maxLength) {
            return Optional.of(validationContext.createViolation());
        }

        return Optional.empty();
    }

    private Optional<Integer> countNumberOfItems(Node node) {
        try {
            // Simple primitive types (e..g String) will just be a property
            // More complex types are represented as child nodes
            if (node.hasProperty(targetField)) {
                return Optional.of(node.getProperty(targetField).getValues().length);
            }
            if (node.hasNode(targetField)) {
                int count = 0;
                NodeIterator childNodes = node.getNodes(targetField);
                while (childNodes.hasNext()) {
                    count++;
                    childNodes.nextNode();
                }
                return Optional.of(count);
            }
            logger.error("Can not run MultiValueNumberSelectedValidator as node {} has no property or child {}", node.getPath(), targetField);
        } catch (RepositoryException ex) {
            logger.error("Repository error when running multi value validator", ex);
        }

        return Optional.empty();
    }
}
