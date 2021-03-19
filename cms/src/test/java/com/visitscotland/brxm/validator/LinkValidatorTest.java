package com.visitscotland.brxm.validator;

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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LinkValidatorTest {

    @Mock
    ValidationContext context;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    SessionFactory mockSessionFactory;

    @Test
    @DisplayName("Validates that days module allows stops")
    void daysCorrectModule() throws RepositoryException {
        LinkValidator validator = new LinkValidator(mockSessionFactory);

        Node node = Mockito.mock(Node.class, RETURNS_DEEP_STUBS);
        Node childNode = mockLink(true, false, false);


        when(node.getProperty(HIPPO_DOCBASE).getValue().getString()).thenReturn("linkName");
        when(node.getParent().isNodeType("visitscotland:Day")).thenReturn(true);
        when(mockSessionFactory.getHippoNodeByIdentifier("linkName")).thenReturn(childNode);

        assertFalse(validator.validate(context, node).isPresent());
    }

    @Test
    @DisplayName("Validates that days module can't link any other type of page")
    void daysIncorrectModule() throws RepositoryException {
        LinkValidator validator = new LinkValidator(mockSessionFactory);

        Node node = Mockito.mock(Node.class, RETURNS_DEEP_STUBS);
        Node childNode = mockLink(false, false, false);


        when(node.getProperty(HIPPO_DOCBASE).getValue().getString()).thenReturn("linkName");
        when(node.getParent().isNodeType("visitscotland:Day")).thenReturn(true);
        when(mockSessionFactory.getHippoNodeByIdentifier("linkName")).thenReturn(childNode);
        when(context.createViolation("stop")).thenReturn(mock(Violation.class));

        assertTrue(validator.validate(context, node).isPresent());
    }

    @Test
    @DisplayName("Validates that megalinks or OTYML allow Page types")
    void LinksCorrectPages() throws RepositoryException {
        LinkValidator validator = new LinkValidator(mockSessionFactory);

        Node node = Mockito.mock(Node.class, RETURNS_DEEP_STUBS);
        Node childNode = mockLink(false, true, false);


        when(node.getProperty(HIPPO_DOCBASE).getValue().getString()).thenReturn("linkName");
        when(node.getParent().isNodeType("visitscotland:Day")).thenReturn(false);
        when(mockSessionFactory.getHippoNodeByIdentifier("linkName")).thenReturn(childNode);

        assertFalse(validator.validate(context, node).isPresent());
    }

    @Test
    @DisplayName("Validates that megalinks or OTYML allow Shared link types")
    void LinksCorrectSharedLinks() throws RepositoryException {
        LinkValidator validator = new LinkValidator(mockSessionFactory);

        Node node = Mockito.mock(Node.class, RETURNS_DEEP_STUBS);
        Node childNode = mockLink(false, false, true);


        when(node.getProperty(HIPPO_DOCBASE).getValue().getString()).thenReturn("linkName");
        when(node.getParent().isNodeType("visitscotland:Day")).thenReturn(false);
        when(mockSessionFactory.getHippoNodeByIdentifier("linkName")).thenReturn(childNode);

        assertFalse(validator.validate(context, node).isPresent());
    }

    @Test
    @DisplayName("Validates that megalinks or OTYML does not allow any other type that is not a Page or SharedLink")
    void LinksIncorrectValues() throws RepositoryException {
        LinkValidator validator = new LinkValidator(mockSessionFactory);

        Node node = Mockito.mock(Node.class, RETURNS_DEEP_STUBS);
        Node childNode = mockLink(false, false, false);


        when(node.getProperty(HIPPO_DOCBASE).getValue().getString()).thenReturn("linkName");
        when(node.getParent().isNodeType("visitscotland:Day")).thenReturn(false);
        when(mockSessionFactory.getHippoNodeByIdentifier("linkName")).thenReturn(childNode);
        when(context.createViolation()).thenReturn(mock(Violation.class));

        assertTrue(validator.validate(context, node).isPresent());
    }

    @Test
    @DisplayName("Validates that links have been selected")
    void LinksEmptyValues() throws RepositoryException {
        LinkValidator validator = new LinkValidator(mockSessionFactory);

        Node node = Mockito.mock(Node.class, RETURNS_DEEP_STUBS);
        Node childNode = mockLink(false, false, false);


        when(node.getProperty(HIPPO_DOCBASE).getValue().getString()).thenReturn(LinkValidator.EMPTY_DOCUMENT);
        when(context.createViolation("EmptyLink")).thenReturn(mock(Violation.class));

        assertTrue(validator.validate(context, node).isPresent());
    }

    private Node mockLink(boolean stop, boolean page, boolean shared) {
        Node node = Mockito.mock(Node.class, withSettings().lenient());
        try {
            when(node.isNodeType("visitscotland:Stop")).thenReturn(stop);
            when(node.isNodeType("visitscotland:Page")).thenReturn(page);
            when(node.isNodeType("visitscotland:SharedLink")).thenReturn(shared);
        } catch (RepositoryException e) {
            //This cannot happen
            e.printStackTrace();
        }

        return node;
    }
}
