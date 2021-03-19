package com.visitscotland.brxm.validator;

import com.visitscotland.brxm.hippobeans.ListicleItem;
import com.visitscotland.brxm.hippobeans.Stop;
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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LocationValidatorTest {

    @Mock
    ValidationContext context;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    SessionFactory mockSessionFactory;

    @Test
    @DisplayName("Stop module, if the product is not a DMS and no location/subtitle is provided, error")
    void stopNoDms_noLocation() throws RepositoryException {
        LocationValidator validator = new LocationValidator(mockSessionFactory);

        Node node = Mockito.mock(Node.class,RETURNS_DEEP_STUBS);
        when(node.hasNode(Stop.PRODUCTS)).thenReturn(true);
        Node auxNode = Mockito.mock(Node.class,withSettings().lenient());
        when(node.getNode(Stop.PRODUCTS)).thenReturn(auxNode);

        Node childNode = Mockito.mock(Node.class);
        when(auxNode.getIdentifier()).thenReturn("linkName");
        when(mockSessionFactory.getJcrSession().getNodeByIdentifier("linkName")).thenReturn(childNode);

        when(node.getProperty("visitscotland:subtitle").getString()).thenReturn("");

        when(context.createViolation()).thenReturn(mock(Violation.class));

        assertTrue(validator.validate(context, node).isPresent());
    }

    @Test
    @DisplayName("Stop module is valid if the product is not a DMS and location/subtitle is provided")
    void stopNoDms_Location() throws RepositoryException {
        LocationValidator validator = new LocationValidator(mockSessionFactory);

        Node node = Mockito.mock(Node.class,RETURNS_DEEP_STUBS);
        when(node.hasNode(Stop.PRODUCTS)).thenReturn(true);
        Node auxNode = Mockito.mock(Node.class,withSettings().lenient());
        when(node.getNode(Stop.PRODUCTS)).thenReturn(auxNode);

        Node childNode = Mockito.mock(Node.class);
        when(auxNode.getIdentifier()).thenReturn("linkName");
        when(mockSessionFactory.getJcrSession().getNodeByIdentifier("linkName")).thenReturn(childNode);

        when(node.getProperty("visitscotland:subtitle").getString()).thenReturn("subtitle");

        assertFalse(validator.validate(context, node).isPresent());
    }
    @Test
    @DisplayName("Listicle item, if the product is not a DMS and no location/subtitle is provided, error")
    void listicleItemNoDms_noLocation() throws RepositoryException {
        LocationValidator validator = new LocationValidator(mockSessionFactory);

        Node node = Mockito.mock(Node.class,RETURNS_DEEP_STUBS);
        when(node.hasNode(Stop.PRODUCTS)).thenReturn(false);
        when(node.hasNode(ListicleItem.PRODUCT)).thenReturn(true);
        Node auxNode = Mockito.mock(Node.class,withSettings().lenient());
        when(node.getNode(ListicleItem.PRODUCT)).thenReturn(auxNode);

        Node childNode = Mockito.mock(Node.class);
        when(auxNode.getIdentifier()).thenReturn("linkName");
        when(mockSessionFactory.getJcrSession().getNodeByIdentifier("linkName")).thenReturn(childNode);

        when(node.getProperty("visitscotland:subtitle").getString()).thenReturn("");

        when(context.createViolation()).thenReturn(mock(Violation.class));

        assertTrue(validator.validate(context, node).isPresent());
    }

    @Test
    @DisplayName("Listicle item module is valid if the product is not a DMS and location/subtitle is provided")
    void listicleItemNoDms_Location() throws RepositoryException {
        LocationValidator validator = new LocationValidator(mockSessionFactory);

        Node node = Mockito.mock(Node.class,RETURNS_DEEP_STUBS);
        when(node.hasNode(Stop.PRODUCTS)).thenReturn(false);
        when(node.hasNode(ListicleItem.PRODUCT)).thenReturn(true);
        Node auxNode = Mockito.mock(Node.class,withSettings().lenient());
        when(node.getNode(ListicleItem.PRODUCT)).thenReturn(auxNode);

        Node childNode = Mockito.mock(Node.class);
        when(auxNode.getIdentifier()).thenReturn("linkName");
        when(mockSessionFactory.getJcrSession().getNodeByIdentifier("linkName")).thenReturn(childNode);

        when(node.getProperty("visitscotland:subtitle").getString()).thenReturn("subtitle");

        assertFalse(validator.validate(context, node).isPresent());
    }

}
