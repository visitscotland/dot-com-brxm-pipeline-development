package com.visitscotland.brxm.validator;

import com.visitscotland.brxm.config.VsComponentManager;
import com.visitscotland.brxm.services.CommonUtilsService;
import org.onehippo.cms.services.validation.api.ValidationContext;
import org.onehippo.cms.services.validation.api.ValidationContextException;
import org.onehippo.cms.services.validation.api.Validator;
import org.onehippo.cms.services.validation.api.Violation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Value;
import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * jcr:Name = visitscotland:external-document-validator

 */

public class ExternalDocumentValidator implements Validator<String>  {

    private static final Logger logger = LoggerFactory.getLogger(FeaturedWidgetValidator.class);

    static final String TYPES = "visitscotland:types";
    static final String PDF = "pdf";

    private final Set<String> validTypes;

    public ExternalDocumentValidator(final Node config) {
        try {
            if (config.hasProperty(TYPES)) {
                validTypes = new HashSet<>();
                for (Value v: config.getProperty(TYPES).getValues()){
                    validTypes.add(v.getString());
                }
            } else {
                validTypes = Set.of(PDF);
            }
        } catch (RepositoryException e) {
            throw new ValidationContextException("An unexpected error happened while loading the validation node", e);
        }
    }

    public Optional<Violation> validate(ValidationContext context, String value) {

        try {
            String contentType = getCommonUtilsService().getContentType(value);
            if (contentType != null && (contentType.startsWith("application")
                    || contentType.startsWith("image"))) {
                for (String mimetype : validTypes) {
                    if (contentType.contains(mimetype) || value.endsWith(mimetype)){
                        return Optional.empty();
                    }
                }
            }
        } catch (IOException e) {
            logger.warn("ExternalDocumentValidator couldn't access to file {} ", value);
        }

        return Optional.of(context.createViolation());
    }

    private CommonUtilsService getCommonUtilsService(){
        return VsComponentManager.get(CommonUtilsService.class);
    }
}
