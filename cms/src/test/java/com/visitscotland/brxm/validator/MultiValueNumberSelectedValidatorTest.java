package com.visitscotland.brxm.validator;

import static org.mockito.Mockito.*;

import com.visitscotland.brxm.translation.MockNodeBuilder;
import org.apache.jackrabbit.value.LongValue;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.onehippo.cms.services.validation.api.ValidationContext;
import org.onehippo.cms.services.validation.api.ValidationContextException;
import org.onehippo.cms.services.validation.api.Violation;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.Value;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ExtendWith(MockitoExtension.class)
class MultiValueNumberSelectedValidatorTest {

    @Mock
    ValidationContext context;

    @DisplayName("When target field not provided, then constructor throws exception")
    @Test
    void targetFieldNotProvided() throws Exception  {
        Node node = mock(Node.class, withSettings().lenient());
        // Lenient as failure of earlier check (targetField) means that this property is never checked
        // However, we don't want the order of the checks carried out in the constructor to impact the test result
        Property maxValueProp = mock(Property.class, withSettings().lenient());
        when(maxValueProp.getLong()).thenReturn(5L);
        when(node.hasProperty("targetField")).thenReturn(false);
        when(node.hasProperty("maxLength")).thenReturn(true);
        when(node.getProperty("maxValue")).thenReturn(maxValueProp);

        Assertions.assertThrows(ValidationContextException.class, () -> {
             new MultiValueNumberSelectedValidator(node);
        });
    }

    @DisplayName("When neither maxLength of minLength is provided, then constructor throws exception")
    @Test
    void minMaxNotProvided() throws Exception  {
        Node node = mock(Node.class);
        // Lenient as failure of earlier check (targetField) means that this property is never checked
        // However, we don't want the order of the checks carried out in the constructor to impact the test result
        Property targetFieldProp = mock(Property.class);
        lenient().when(targetFieldProp.getString()).thenReturn("fieldName");
        lenient().when(node.hasProperty("targetField")).thenReturn(true);
        lenient().when(node.getProperty("targetField")).thenReturn(targetFieldProp);

        Assertions.assertThrows(ValidationContextException.class, () -> {
            new MultiValueNumberSelectedValidator(node);
        });
    }

    @DisplayName("Validation passes - various examples")
    @ParameterizedTest
    @MethodSource("validationPassesArgSource")
    void validationPassesExamples(Long minLength, Long maxLength) throws Exception {
        MockNodeBuilder configNodeBuilder = new MockNodeBuilder()
                .withProperty("targetField", "field");
        if (minLength != null) {
            configNodeBuilder.withProperty("minLength", minLength);
        }
        if (maxLength != null) {
            configNodeBuilder.withProperty("maxLength", maxLength);
        }

        List<Value> values = Stream.of(1, 2, 3, 4, 5).map(LongValue::new).collect(Collectors.toList());
        Node documentNode = new MockNodeBuilder()
                .withProperty("field", values.toArray(new Value[0])).build();

        MultiValueNumberSelectedValidator validator = new MultiValueNumberSelectedValidator(configNodeBuilder.build());
        Optional<Violation> validateResult = validator.validate(context, documentNode);
        Assertions.assertFalse(validateResult.isPresent());
    }

    static Stream<Arguments> validationPassesArgSource() {
        return Stream.of(Arguments.of(5L, 10L), Arguments.of(3L, 5L), Arguments.of(5L, 5L), Arguments.of(2L, 7L),
                Arguments.of(null, 5L), Arguments.of(5L, null), Arguments.of(null, 7L), Arguments.of(3L, null));
    }

    @DisplayName("Validation failures - various examples")
    @ParameterizedTest
    @MethodSource("validationFailuresArgSource")
    void validationFailuresExamples(Long minLength, Long maxLength) throws Exception {
        when(context.createViolation()).thenReturn(mock(Violation.class));
        MockNodeBuilder configNodeBuilder = new MockNodeBuilder()
                .withProperty("targetField", "field");
        if (minLength != null) {
            configNodeBuilder.withProperty("minLength", minLength);
        }
        if (maxLength != null) {
            configNodeBuilder.withProperty("maxLength", maxLength);
        }

        List<Value> values = Stream.of(1, 2, 3, 4, 5).map(LongValue::new).collect(Collectors.toList());
        Node documentNode = new MockNodeBuilder()
                .withProperty("field", values.toArray(new Value[0])).build();

        MultiValueNumberSelectedValidator validator = new MultiValueNumberSelectedValidator(configNodeBuilder.build());
        Optional<Violation> validateResult = validator.validate(context, documentNode);
        Assertions.assertTrue(validateResult.isPresent());
    }

    static Stream<Arguments> validationFailuresArgSource() {
        return Stream.of(Arguments.of(2L, 4L), Arguments.of(6L, 6L), Arguments.of(null, 4L), Arguments.of(6L, null));
    }

    @DisplayName("When targetField not found on document, then validation passes")
    @Test
    void targetFieldNotFoundOnNode() throws Exception {
        MockNodeBuilder configNodeBuilder = new MockNodeBuilder()
                .withProperty("targetField", ("fieldDoesNotExist"))
                .withProperty("minLength", 2L)
                .withProperty("maxLength", 4L);

        Node documentNode = mock(Node.class);
        MultiValueNumberSelectedValidator validator = new MultiValueNumberSelectedValidator(configNodeBuilder.build());
        Optional<Violation> validateResult = validator.validate(context, documentNode);
        Assertions.assertFalse(validateResult.isPresent());
    }


}
