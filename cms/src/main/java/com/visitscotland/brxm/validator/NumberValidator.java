package com.visitscotland.brxm.validator;

import org.onehippo.cms.services.validation.api.ValidationContext;
import org.onehippo.cms.services.validation.api.Validator;
import org.onehippo.cms.services.validation.api.Violation;

import java.util.Optional;

/**
 * jcr:Name = visitscotland:number-validator
 *
 * @author juanluis.hurtado
 */
public class NumberValidator implements Validator<String> {

    public Optional<Violation> validate(ValidationContext context, String value) {
        try {
            Integer.valueOf(value);
            return Optional.empty();
        } catch (Exception e) {
            return Optional.of(context.createViolation());
        }
    }
}
