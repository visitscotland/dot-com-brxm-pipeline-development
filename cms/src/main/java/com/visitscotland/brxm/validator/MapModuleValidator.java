package com.visitscotland.brxm.validator;

import org.onehippo.cms.services.validation.api.ValidationContext;
import org.onehippo.cms.services.validation.api.Validator;
import org.onehippo.cms.services.validation.api.Violation;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Value;
import java.util.Optional;

/**
 * jcr:Name = visitscotland:map-module-validator
 *
 */
public class MapModuleValidator implements Validator<Node> {

    static final String TITLE = "visitscotland:title";
    static final String TAB_TITLE = "visitscotland:tabTitle";

    public Optional<Violation> validate(final ValidationContext context, final Node document) {
        try {
            if (document.getProperty(TITLE).getString().isEmpty() && document.getProperty(TAB_TITLE).getString().isEmpty()) {
                return Optional.of(context.createViolation());
            }
        } catch (RepositoryException e) {
            return Optional.of(context.createViolation());
        }

        return Optional.empty();
    }
}
