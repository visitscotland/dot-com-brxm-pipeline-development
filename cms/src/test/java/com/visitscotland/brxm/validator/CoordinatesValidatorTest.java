package com.visitscotland.brxm.validator;

import com.visitscotland.brxm.hippobeans.Coordinates;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.onehippo.cms.services.validation.api.ValidationContext;
import org.onehippo.cms.services.validation.api.ValidationContextException;
import org.onehippo.cms.services.validation.api.Violation;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CoordinatesValidatorTest {

    @Mock
    ValidationContext context;

    @Test
    @DisplayName("Wrong definition - no coordinates")
    void constructor_emptyCoordinates() {
        //Verifies that the values for the extremes have been set
        Node node = Mockito.mock(Node.class);
        assertThrows(ValidationContextException.class, () -> new CoordinatesValidator(node));
    }

    @Test
    @DisplayName("Wrong definition - no coordinates")
    void constructor_wrongValues() throws RepositoryException {
        //Verifies that the coordinates are not inverted
        assertThrows(ValidationContextException.class, () -> new CoordinatesValidator(mockArea(-1, 0, 1, 0)));
        assertThrows(ValidationContextException.class, () -> new CoordinatesValidator(mockArea(1, 0, -1, 0)));
    }

    @Test
    @DisplayName("Constructor - Error Handling when accessing the node")
    void constructor_errorHandling_accesingTheNode() throws RepositoryException {
        //The exception happens when accessing the node
        Node node = mock(Node.class);
        when(node.getPath()).thenThrow(new RepositoryException());
        assertThrows(ValidationContextException.class, () -> new CoordinatesValidator(node));
    }

    @Test
    @DisplayName("Constructor - Error Handling when accessing the properties")
    void constructor_errorHandling_accesingProperties() throws RepositoryException {
        //The exception happens when acce
        Node node = mock(Node.class);
        when(node.getPath()).thenReturn("path-to-node");
        when(node.getProperty(any())).thenThrow(new RepositoryException());
        ValidationContextException exception = assertThrows(ValidationContextException.class, () -> new CoordinatesValidator(node));
        assertTrue(exception.getMessage().contains("path-to-node"));
    }

    @Test
    @DisplayName("Validates that the coodinates are contained in the area")
    void correctValues() {
        //Validates that correct values are accepted
        Node scotland = mockArea(61, 54, 0, -13);
        CoordinatesValidator validator = new CoordinatesValidator(scotland);

        assertFalse(validator.validate(context, mockPoint(60, -10)).isPresent());
        assertFalse(validator.validate(context, mockPoint(60.999, -10)).isPresent());
        assertFalse(validator.validate(context, mockPoint(54.001, -10)).isPresent());
        assertFalse(validator.validate(context, mockPoint(60, -.0001)).isPresent());
        assertFalse(validator.validate(context, mockPoint(60, -12.999)).isPresent());
    }

    @Test
    @DisplayName("Validates that the coodinates are contained in the area")
    void wrongValues() {
        //Validates that wrong values are not accepted
        Node scotland = mockArea(61., 54., 0., -13.);
        CoordinatesValidator validator = new CoordinatesValidator(scotland);
        when (context.createViolation()).thenReturn(mock(Violation.class));

        //Longitud outside the area
        assertTrue(validator.validate(context, mockPoint(60., 10.)).isPresent());
        //Latitude outside the area
        assertTrue(validator.validate(context, mockPoint(0., -10.)).isPresent());
        //Both, latitude and longitud outside the area
        assertTrue(validator.validate(context, mockPoint(99., 99.)).isPresent());

    }

    @Test
    @DisplayName("Exceptions are caught as error in the validation, preventing wrong values to be saved")
    void errorHandling() throws RepositoryException {
        //Verifies that an Exception is interpreted as an Error in Validation
        Node scotland = mockArea(61, 54, 0, -13);
        CoordinatesValidator validator = new CoordinatesValidator(scotland);
        Node point = mock(Node.class);
        when(point.hasProperty(any())).thenThrow(new RepositoryException());
        when (context.createViolation()).thenReturn(mock(Violation.class));

        assertTrue(validator.validate(context, point).isPresent());
    }

    private Node mockArea(Number north, Number south, Number east, Number west) {
        Node node = Mockito.mock(Node.class, RETURNS_DEEP_STUBS);
        try {
            when(node.getProperty(CoordinatesValidator.NORTH_EXTREME).getDouble()).thenReturn(north.doubleValue());
            when(node.getProperty(CoordinatesValidator.SOUTH_EXTREME).getDouble()).thenReturn(south.doubleValue());
            when(node.getProperty(CoordinatesValidator.EAST_EXTREME).getDouble()).thenReturn(east.doubleValue());
            when(node.getProperty(CoordinatesValidator.WEST_EXTREME).getDouble()).thenReturn(west.doubleValue());
        } catch (RepositoryException e) {
            //This cannot happen
            e.printStackTrace();
        }

        return node;
    }

    private Node mockPoint(Number latitude, Number longitude) {
        Node node = Mockito.mock(Node.class, RETURNS_DEEP_STUBS);
        try {
            when(node.hasProperty(Coordinates.LATITUDE)).thenReturn(true);
            when(node.hasProperty(Coordinates.LONGITUDE)).thenReturn(true);
            when(node.getProperty(Coordinates.LATITUDE).getDouble()).thenReturn(latitude.doubleValue());
            when(node.getProperty(Coordinates.LONGITUDE).getDouble()).thenReturn(longitude.doubleValue());
        } catch (RepositoryException e) {
            //This cannot happen
            e.printStackTrace();
        }

        return node;
    }
}
