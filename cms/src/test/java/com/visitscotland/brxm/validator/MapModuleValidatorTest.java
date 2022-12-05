package com.visitscotland.brxm.validator;

import com.visitscotland.brxm.translation.SessionFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.onehippo.cms.services.validation.api.ValidationContext;
import org.onehippo.cms.services.validation.api.Violation;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.Value;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MapModuleValidatorTest {

    @Mock
    ValidationContext context;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    SessionFactory mockSessionFactory;

    @Test
    @DisplayName("VS-3996 Validates that at least one title (field title) is filled")
    @MockitoSettings(strictness = Strictness.LENIENT)
    void correctValues() throws RepositoryException {
        MapModuleValidator validator = new MapModuleValidator();

        Node node = Mockito.mock(Node.class);
        Property property = Mockito.mock(Property.class);
        when(node.getProperty(MapModuleValidator.TITLE)).thenReturn(property);
        when(node.getProperty(MapModuleValidator.TAB_TITLE)).thenReturn(property);
        when(property.getString()).thenReturn("title");

        Property mapType = Mockito.mock(Property.class);
        when(node.getProperty(MapModuleValidator.MAP_TYPE)).thenReturn(mapType);
        when(mapType.getString()).thenReturn("");

        Property mapKeys = Mockito.mock(Property.class);
        when(node.getProperty(MapModuleValidator.MAP_KEYS)).thenReturn(mapKeys);

        Value value = Mockito.mock(Value.class);
        when(mapKeys.getValues()).thenReturn(new Value[]{value});

        assertFalse(validator.validate(context, node).isPresent());
    }


    @Test
    @DisplayName("VS-3996 Validates that an error is displayed if both titles are empty")
    void errorNoTitlesProvided() throws RepositoryException {
        MapModuleValidator validator = new MapModuleValidator();

        Node node = Mockito.mock(Node.class, withSettings().lenient());
        Property property = Mockito.mock(Property.class);

        when(node.getProperty(any())).thenReturn(property);
        when(property.getString()).thenReturn("");

        when(context.createViolation()).thenReturn(mock(Violation.class));

        assertTrue(validator.validate(context, node).isPresent());
    }


    @Test
    @DisplayName("VS-3996 Validates that at least one title (field title) is filled")
    @MockitoSettings(strictness = Strictness.LENIENT)
    void errorBespokeMapWithTaxonomy() throws RepositoryException {
        MapModuleValidator validator = new MapModuleValidator();

        Node node = Mockito.mock(Node.class);
        Property property = Mockito.mock(Property.class);
        when(node.getProperty(MapModuleValidator.TITLE)).thenReturn(property);
        when(node.getProperty(MapModuleValidator.TAB_TITLE)).thenReturn(property);
        when(property.getString()).thenReturn("title");

        Property mapType = Mockito.mock(Property.class);
        when(node.getProperty(MapModuleValidator.MAP_TYPE)).thenReturn(mapType);
        when(mapType.getString()).thenReturn("bespokeMap");

        Property mapKeys = Mockito.mock(Property.class);
        when(node.getProperty(MapModuleValidator.MAP_KEYS)).thenReturn(mapKeys);

        Value value = Mockito.mock(Value.class);
        when(mapKeys.getValues()).thenReturn(new Value[]{value});

        when(context.createViolation("bespokeMap")).thenReturn(mock(Violation.class));

        assertTrue(validator.validate(context, node).isPresent());
    }

}
