package com.visitscotland.brxm.validator;

import com.visitscotland.brxm.translation.MockNodeBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.onehippo.cms.services.validation.api.ValidationContext;
import org.onehippo.cms.services.validation.api.ValidationContextException;
import org.onehippo.cms.services.validation.api.Violation;
import org.onehippo.repository.util.JcrConstants;

import javax.jcr.Node;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UniqueLinksValidatorTest {

    @Mock
    ValidationContext context;

    @BeforeEach
    void init (){
        //The following method needs to be initialized leniently otherwise issues in the validation might be hidden
        lenient().when(context.createViolation()).thenReturn(mock(Violation.class));
    }

    @DisplayName("When target field not provided, then constructor throws exception")
    @Test
    void targetFieldNotProvided() throws Exception  {
        Node node = mock(Node.class, withSettings().lenient());
        when(node.hasProperty("targetField")).thenReturn(false);

        Assertions.assertThrows(ValidationContextException.class, () -> {
            new UniqueLinksValidator(node);
        });
    }

    @DisplayName("When list of non-unique links provided, validation fails")
    @Test
    void nonUniqueLinksFailsTest() throws Exception {
        
        Node config = new MockNodeBuilder().withProperty("targetField", "links").build();
        Node toValidate = new MockNodeBuilder()
                .withChildNode("links", new MockNodeBuilder().withProperty("hippo:docbase", "a").build())
                .withChildNode("links", new MockNodeBuilder().withProperty("hippo:docbase", "a").build())
                .withChildNode("links", new MockNodeBuilder().withProperty("hippo:docbase", "b").build()).build();

        Optional<Violation> validationResult = new UniqueLinksValidator(config).validate(context, toValidate);
        Assertions.assertTrue(validationResult.isPresent());
    }

    @DisplayName("When list of unique links provided, validation passes")
    @Test
    void uniqueLinksPassesTest() throws Exception {
        Node config = new MockNodeBuilder().withProperty("targetField", "links").build();
        Node toValidate = new MockNodeBuilder()
                .withChildNode("links", new MockNodeBuilder().withProperty("hippo:docbase", "a").build())
                .withChildNode("links", new MockNodeBuilder().withProperty("hippo:docbase", "b").build())
                .withChildNode("links", new MockNodeBuilder().withProperty("different", "b").build()).build();

        Optional<Violation> validationResult = new UniqueLinksValidator(config).validate(context, toValidate);
        Assertions.assertFalse(validationResult.isPresent());
    }

    @DisplayName("When multiple links with no value are present, validation passes")
    @Test
    void linksNotSet() throws Exception {
        Node config = new MockNodeBuilder().withProperty("targetField", "links").build();
        Node toValidate = new MockNodeBuilder()
                .withChildNode("links", new MockNodeBuilder().withProperty("hippo:docbase", JcrConstants.ROOT_NODE_ID).build())
                .withChildNode("links", new MockNodeBuilder().withProperty("hippo:docbase", JcrConstants.ROOT_NODE_ID).build())
                .withChildNode("links", new MockNodeBuilder().withProperty("different", "b").build()).build();

        Optional<Violation> validationResult = new UniqueLinksValidator(config).validate(context, toValidate);
        Assertions.assertFalse(validationResult.isPresent());
    }

    @DisplayName("When different link id is used and unique values, test passes")
    @Test
    void differentLinkIdAndUnique() throws Exception {
        Node config = new MockNodeBuilder().withProperty("targetField", "links").withProperty("linkIdField", "id").build();
        Node toValidate = new MockNodeBuilder()
                .withChildNode("links", new MockNodeBuilder().withProperty("id", "a").build())
                .withChildNode("links", new MockNodeBuilder().withProperty("id", "b").build())
                .withChildNode("links", new MockNodeBuilder().withProperty("hippo:docbase", "c").build())
                .withChildNode("links", new MockNodeBuilder().withProperty("hippo:docbase", "c").build()).build();

        Optional<Violation> validationResult = new UniqueLinksValidator(config).validate(context, toValidate);
        Assertions.assertFalse(validationResult.isPresent());
    }

    @DisplayName("When different link id is used and non-unique values, test fails")
    @Test
    void differentLinkIdAndNonUnique() throws Exception {
        
        Node config = new MockNodeBuilder().withProperty("targetField", "links").withProperty("linkIdField", "id").build();
        Node toValidate = new MockNodeBuilder()
                .withChildNode("links", new MockNodeBuilder().withProperty("id", "a").build())
                .withChildNode("links", new MockNodeBuilder().withProperty("id", "a").build())
                .withChildNode("links", new MockNodeBuilder().withProperty("hippo:docbase", "a").build())
                .withChildNode("links", new MockNodeBuilder().withProperty("hippo:docbase", "b").build()).build();

        Optional<Violation> validationResult = new UniqueLinksValidator(config).validate(context, toValidate);
        Assertions.assertTrue(validationResult.isPresent());
    }

    @DisplayName("When linkIdField is node & non-unique links provided, validation fails")
    @Test
    void linkIdIsNode_nonUniqueFails() throws Exception {
        Node config = new MockNodeBuilder().withProperty("targetField", "links").withProperty("linkIdField", "child").build();

        Node aNode = new MockNodeBuilder().withProperty("hippo:docbase", "a").build();
        Node bNode = new MockNodeBuilder().withProperty("hippo:docbase", "a").build();
        Node linkAChild = new MockNodeBuilder().withChildNode("child", aNode).build();
        Node linkBChild = new MockNodeBuilder().withChildNode("child", bNode).build();
        Node toValidate = new MockNodeBuilder()
                .withChildNode("links", linkAChild)
                .withChildNode("links", linkBChild).build();

        Optional<Violation> validationResult = new UniqueLinksValidator(config).validate(context, toValidate);
        Assertions.assertTrue(validationResult.isPresent());
    }

    @DisplayName("When linkIdField is node & unique links provided, validation passes")
    @Test
    void linkIdIsNode_uniquePasses() throws Exception {
        Node config = new MockNodeBuilder().withProperty("targetField", "links").withProperty("linkIdField", "child").build();

        Node aNode = new MockNodeBuilder().withProperty("hippo:docbase", "a").build();
        Node bNode = new MockNodeBuilder().withProperty("hippo:docbase", "b").build();
        Node linkAChild = new MockNodeBuilder().withChildNode("child", aNode).build();
        Node linkBChild = new MockNodeBuilder().withChildNode("child", bNode).build();
        Node toValidate = new MockNodeBuilder()
                .withChildNode("links", linkAChild)
                .withChildNode("links", linkBChild).build();

        Optional<Violation> validationResult = new UniqueLinksValidator(config).validate(context, toValidate);
        Assertions.assertFalse(validationResult.isPresent());
    }

    @DisplayName("When linkIdField is node & no hippo:docbase, validation passes")
    @Test
    void linkIdNodeNoDocbase() throws Exception {
        Node config = new MockNodeBuilder().withProperty("targetField", "links").withProperty("linkIdField", "child").build();
        Node aNode = new MockNodeBuilder().withProperty("hippo:notdocbase", "a").build();
        Node bNode = new MockNodeBuilder().withProperty("hippo:notdocbase", "a").build();
        Node linkAChild = mock(Node.class);
        Node linkBChild = mock(Node.class);
        when(linkAChild.getNode("child")).thenReturn(aNode);
        when(linkBChild.getNode("child")).thenReturn(bNode);
        Node toValidate = new MockNodeBuilder()
                .withChildNode("links", linkAChild)
                .withChildNode("links", linkBChild).build();
        when(toValidate.hasNode("links")).thenReturn(true);
        when(linkAChild.hasNode("child")).thenReturn(true);
        when(linkBChild.hasNode("child")).thenReturn(true);

        Optional<Violation> validationResult = new UniqueLinksValidator(config).validate(context, toValidate);
        Assertions.assertFalse(validationResult.isPresent());
    }

    @DisplayName("VS-2934 - Amend LinkValidator to accept Video Documents nested structure")
    @Test
    void nestedLinks() throws Exception {
        //Create hippo Configuration Node
        Node config = new MockNodeBuilder().withProperty("targetField", "links").withProperty("linkIdField", "child").build();
        //Create Node to be validated
        Node link1 = new MockNodeBuilder().withProperty("hippo:docbase", "a").build();
        Node link2 = new MockNodeBuilder().withProperty("hippo:docbase", "b").build();
        Node container1 = new MockNodeBuilder().withChildNode("child", link1).build();
        Node container2 = new MockNodeBuilder().withChildNode("child", link2).build();

        Node toValidate = new MockNodeBuilder()
                .withChildNode("links", container1)
                .withChildNode("links", container2).build();

        Optional<Violation> validationResult = new UniqueLinksValidator(config).validate(context, toValidate);
        Assertions.assertFalse(validationResult.isPresent());
    }

    @DisplayName("VS-2934 - Validator would fail with nested structure when the values are repeated")
    @Test
    void nestedLinks_nonUniqueFails() throws Exception {
        //Create hippo Configuration Node
        Node config = new MockNodeBuilder().withProperty("targetField", "links").withProperty("linkIdField", "child").build();
        //Create Node to be validated
        Node link1 = new MockNodeBuilder().withProperty("hippo:docbase", "a").build();
        Node link2 = new MockNodeBuilder().withProperty("hippo:docbase", "a").build();
        Node container1 = new MockNodeBuilder().withChildNode("child", link1).build();
        Node container2 = new MockNodeBuilder().withChildNode("child", link2).build();

        Node toValidate = new MockNodeBuilder()
                .withChildNode("links", container1)
                .withChildNode("links", container2).build();

        Optional<Violation> validationResult = new UniqueLinksValidator(config).validate(context, toValidate);
        Assertions.assertTrue(validationResult.isPresent());
    }

}
