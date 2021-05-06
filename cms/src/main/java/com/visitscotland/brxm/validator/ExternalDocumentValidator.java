package com.visitscotland.brxm.validator;

import com.visitscotland.brxm.config.VsComponentManager;
import com.visitscotland.brxm.services.CommonUtilsService;
import org.onehippo.cms.services.validation.api.ValidationContext;
import org.onehippo.cms.services.validation.api.Validator;
import org.onehippo.cms.services.validation.api.Violation;

import java.util.Locale;
import java.util.Optional;

/**
 * jcr:Name = visitscotland:external-document-validator

 */
public class ExternalDocumentValidator implements Validator<String>  {

    public Optional<Violation> validate(ValidationContext context, String value) {
        return getCommonUtilsService().getExternalDocumentSize(value, Locale.UK) == null?Optional.of(context.createViolation()) : Optional.empty();
    }

    private CommonUtilsService getCommonUtilsService(){
        return VsComponentManager.get(CommonUtilsService.class);
    }
}