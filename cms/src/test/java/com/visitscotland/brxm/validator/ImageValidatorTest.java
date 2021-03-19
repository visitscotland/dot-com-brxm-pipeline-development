package com.visitscotland.brxm.validator;

import com.visitscotland.brxm.hippobeans.Image;
import com.visitscotland.brxm.translation.SessionFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.onehippo.cms.services.validation.api.ValidationContext;
import org.onehippo.cms.services.validation.api.Violation;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

import static org.hippoecm.repository.api.HippoNodeType.HIPPO_DOCBASE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ImageValidatorTest {

    @Mock
    ValidationContext context;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    SessionFactory mockSessionFactory;

    @Test
    @DisplayName("Validates that the image has credit and alt-text added")
    void correctValues() throws RepositoryException {
        //Validates that correct values are accepted
        ImageValidator validator = new ImageValidator(mockSessionFactory);

        Node node = Mockito.mock(Node.class, RETURNS_DEEP_STUBS);
        Node childNode = mockImage("credit", "alt-text");


        when(node.getProperty(HIPPO_DOCBASE).getValue().getString()).thenReturn("imageName");
        when(mockSessionFactory.getHippoNodeByIdentifier("imageName")).thenReturn(childNode);

        assertFalse(validator.validate(context, node).isPresent());
    }

    @Test
    @DisplayName("Validates that the image throws an Error when there is no credit")
    void noCredit() throws RepositoryException {
        //Validates that the credit is not present
        ImageValidator validator = new ImageValidator(mockSessionFactory);

        Node node = Mockito.mock(Node.class, RETURNS_DEEP_STUBS);
        Node noCredit = mockImage(null, "alt-text");

        when(node.getProperty(HIPPO_DOCBASE).getValue().getString()).thenReturn("imageName");

        when(mockSessionFactory.getHippoNodeByIdentifier("imageName")).thenReturn(noCredit);
        when(context.createViolation()).thenReturn(mock(Violation.class));
        assertTrue(validator.validate(context, node).isPresent());
    }


    @Test
    @DisplayName("Validates that the image throws an Error when there is no alternative text")
    void noAltText() throws RepositoryException {
        //Validates that the alternative text is not present
        ImageValidator validator = new ImageValidator(mockSessionFactory);

        Node node = Mockito.mock(Node.class, RETURNS_DEEP_STUBS);
        Node noCredit = mockImage("credit", null);

        when(node.getProperty(HIPPO_DOCBASE).getValue().getString()).thenReturn("imageName");

        when(mockSessionFactory.getHippoNodeByIdentifier("imageName")).thenReturn(noCredit);
        when(context.createViolation()).thenReturn(mock(Violation.class));
        assertTrue(validator.validate(context, node).isPresent());
    }

    private Node mockImage(String credit, String altText) {
        Node node = Mockito.mock(Node.class, withSettings().lenient());
        try {
            when(node.hasProperty(Image.CREDIT)).thenReturn(credit != null);
            when(node.hasProperty(Image.ALT_TEXT)).thenReturn(altText != null);
        } catch (RepositoryException e) {
            //This cannot happen
            e.printStackTrace();
        }

        return node;
    }
}
