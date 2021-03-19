package com.visitscotland.brxm.validator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.onehippo.cms.services.validation.api.ValidationContext;
import org.onehippo.cms.services.validation.api.Violation;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StringLengthValidatorTest {

    @Mock
    ValidationContext context;

    @Test
    @DisplayName("If the length of the field is bigger than the max allowed, an error should be displayed")
    void stringIncorrectLength() throws RepositoryException {
        Node node = Mockito.mock(Node.class, RETURNS_DEEP_STUBS);
        Long maxLength = 55L;
        when(node.getProperty(StringLengthValidator.MAX_LENGTH).getLong()).thenReturn(maxLength);
        StringLengthValidator validator = new StringLengthValidator(node);

        when(context.createViolation()).thenReturn(mock(Violation.class));
        String value = "Scotland is blessed with an expansive coastline, with pretty seaside villages to be found in a number of coastal regions. In Fife, the charming East Neuk has a chain of former fishing communities which is where you'll find Crail. Here, you could book a holiday at 21 Shoregate, which dates from the 1700s and is just a stone's throw from the harbour. In Aberdeenshire, the utterly gorgeous villages of Pennan and Crovie are home to self-catering properties like Rockpool Cottage";
        assertTrue(validator.validate(context, value).isPresent());
    }
    @Test
    @DisplayName("If the length of the field is smaller than the max allowed, the field is correct")
    void stringCorrectLength() throws RepositoryException {
        Node node = Mockito.mock(Node.class, RETURNS_DEEP_STUBS);
        Long maxLength = 55L;
        when(node.getProperty(StringLengthValidator.MAX_LENGTH).getLong()).thenReturn(maxLength);
        StringLengthValidator validator = new StringLengthValidator(node);

        String value = "Scotland is blessed with an expansive coastline";
        assertFalse(validator.validate(context, value).isPresent());
    }

}
