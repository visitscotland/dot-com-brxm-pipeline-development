package com.visitscotland.brxm.validator;

import com.visitscotland.brxm.config.VsComponentManager;
import com.visitscotland.brxm.services.CommonUtilsService;
import org.hippoecm.hst.core.container.ComponentManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.onehippo.cms.services.validation.api.ValidationContext;
import org.onehippo.cms.services.validation.api.Violation;


import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExternalDocumentValidatorTest {

    ExternalDocumentValidator validator;

    @Mock(lenient = true)
    ValidationContext context;

    @Mock
    CommonUtilsService service;

    @Mock
    ComponentManager manager;

    @BeforeEach
    void init(){
        validator = new ExternalDocumentValidator();

        VsComponentManager.setComponentManager(manager);
        when(manager.getComponent(CommonUtilsService.class)).thenReturn(service);

        when(context.createViolation()).thenReturn(mock(Violation.class));
    }

    @Test
    @DisplayName("The URL is valid")
    void externalDocument() {
        when(service.getExternalDocumentSize(any(), any())).thenReturn("DOWNLOAD PDF 3 MB");

        String value = "https://www.visitscotland.com/ebrochures/en/what-to-see-and-do/perthshireanddundee";
        assertFalse(validator.validate(context, value).isPresent());
    }

    @Test
    @DisplayName("When the URL is not valid a violation is created")
    void externalDocumentIncorrect() {
        //When the URL is not valid the method getExternalDocumentSize will return null which indicates to the validator
        //that the method it cannot be downloaded.
        String value = "https://www.visitscotland.com/ebrochures/en/what-to-see-and-do/perthshireanddundee";
        assertTrue(validator.validate(context, value).isPresent());
    }

}
