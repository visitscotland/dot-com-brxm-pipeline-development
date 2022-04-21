package com.visitscotland.brxm.validator;

import com.visitscotland.brxm.translation.MockNodeBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.Test;
import org.onehippo.cms.services.validation.api.ValidationContext;
import org.onehippo.cms.services.validation.api.Violation;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FeaturedWidgetValidatorTest {

    @InjectMocks
    FeaturedWidgetValidator validator;

    @Mock
    ValidationContext context;

    @Test
    @DisplayName("VS-3057 When two featured items, validation passes")
    public void whenTwoFeaturedItems_validationPasses() throws RepositoryException {
        Node featureItem1 = new MockNodeBuilder().withPrimaryNodeType("visitscotland:CMSLink").build();
        Node featureItem2 = new MockNodeBuilder().withPrimaryNodeType("visitscotland:CMSLink").build();
        Node featureWidgetNode = new MockNodeBuilder()
                .withChildNode("visitscotland:items", featureItem1)
                .withChildNode("visitscotland:items", featureItem2).build();

        Optional<Violation> result = validator.validate(context, featureWidgetNode);

        Assertions.assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("VS-3057 When product search a event search, validation passes")
    public void productSearchNotEvents_valdiationPasses() throws RepositoryException {
        Node productSearch = new MockNodeBuilder()
                .withPrimaryNodeType("visitscotland:ProductSearch")
                .withProperty("visitscotland:productType", "even")
                .build();
        Node featureWidgetNode = new MockNodeBuilder()
                .withChildNode("visitscotland:items", productSearch).build();

        Optional<Violation> result = validator.validate(context, featureWidgetNode);

        Assertions.assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("VS-3057 When product search a event search, validation fails")
    public void productSearchNotEvents_valdiationFails() throws RepositoryException {
        Node productSearch = new MockNodeBuilder()
                .withPrimaryNodeType("visitscotland:ProductSearch")
                .withProperty("visitscotland:productType", "acco")
                .build();
        Node featureWidgetNode = new MockNodeBuilder()
                .withChildNode("visitscotland:items", productSearch).build();
        when(context.createViolation("notEvent")).thenReturn(mock(Violation.class));

        Optional<Violation> result = validator.validate(context, featureWidgetNode);

        Assertions.assertTrue(result.isPresent());
    }

    @CsvSource({
            "visitscotland:CMSLink,visitscotland:ProductSearch",
            "visitscotland:ProductSearch,visitscotland:CMSLink",
            "visitscotland:ProductSearch,visitscotland:ProductSearch"})
    @ParameterizedTest
    @DisplayName("VS-3057 Two featured items not cms links, then validation fails")
    public void whenTwoFeaturedItemsNotBothCmsLink_validationFails(String firstType, String secondType) throws RepositoryException {
        Node featureItem1 = new MockNodeBuilder().withPrimaryNodeType(firstType).build();
        Node featureItem2 = new MockNodeBuilder().withPrimaryNodeType(secondType).build();
        when(context.createViolation(anyString())).thenReturn(mock(Violation.class));

        Node featureWidgetNode = new MockNodeBuilder()
                .withChildNode("visitscotland:items", featureItem1)
                .withChildNode("visitscotland:items", featureItem2).build();

        Optional<Violation> result = validator.validate(context, featureWidgetNode);

        Assertions.assertTrue(result.isPresent());
    }



}
