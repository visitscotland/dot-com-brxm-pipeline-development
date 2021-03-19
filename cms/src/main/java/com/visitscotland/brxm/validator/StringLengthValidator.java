package com.visitscotland.brxm.validator;

import org.onehippo.cms.services.validation.api.ValidationContext;
import org.onehippo.cms.services.validation.api.ValidationContextException;
import org.onehippo.cms.services.validation.api.Validator;
import org.onehippo.cms.services.validation.api.Violation;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import java.util.Optional;

/**
 * jcr:Name = visitscotland:short-string-validator
 * jcr:Name = visitscotland:medium-string-validator
 * jcr:Name = visitscotland:long-string-validator
 * jcr:Name = visitscotland:extralong-string-validator
 * jcr:Name = visitscotland:seotitle-string-validator
 * jcr:Name = visitscotland:seodescription-string-validator

 */
public class StringLengthValidator implements Validator<String>  {

    static final String MAX_LENGTH = "maxLength";

    protected long maxLength;

    public StringLengthValidator(final Node config) {
        try {
            maxLength = config.getProperty(MAX_LENGTH).getLong();
        } catch (RepositoryException e) {
            throw new ValidationContextException("Cannot read required properties for the StringLengthValidator. Verify the node ", e);
        }
    }


    public Optional<Violation> validate(ValidationContext context, String value) {
        return value.length()>maxLength?Optional.of(context.createViolation()) : Optional.empty();
    }
}