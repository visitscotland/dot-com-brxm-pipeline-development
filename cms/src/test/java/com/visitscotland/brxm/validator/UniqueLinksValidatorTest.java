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
        Node linkNodeA = new MockNodeBuilder().withProperty("hippo:docbase", "a").build();
        Node linkNodeB = new MockNodeBuilder().withProperty("hippo:docbase", "a").build();
        Node linkNodeC = new MockNodeBuilder().withProperty("hippo:docbase", "b").build();
        new MockNodeBuilder()
                .withChildNode("links", linkNodeA)
                .withChildNode("links", linkNodeB)
                .withChildNode("links", linkNodeC).build();

        Optional<Violation> validationResult1 = new UniqueLinksValidator(config).validate(context, linkNodeA);
        Optional<Violation> validationResult2 = new UniqueLinksValidator(config).validate(context, linkNodeB);
        Optional<Violation> validationResult3 = new UniqueLinksValidator(config).validate(context, linkNodeC);
        Assertions.assertTrue(validationResult1.isPresent());
        Assertions.assertTrue(validationResult2.isPresent());
        Assertions.assertFalse(validationResult3.isPresent());
    }

    @DisplayName("When list of unique links provided, validation passes")
    @Test
    void uniqueLinksPassesTest() throws Exception {
        Node config = new MockNodeBuilder().withProperty("targetField", "links").build();
        Node link1 =new MockNodeBuilder().withProperty("hippo:docbase", "a").build();
        Node link2 =new MockNodeBuilder().withProperty("hippo:docbase", "b").build();
        Node link3 =new MockNodeBuilder().withProperty("different", "b").build();

        new MockNodeBuilder()
                .withChildNode("links", link1)
                .withChildNode("links", link2)
                .withChildNode("links", link3).build();

        Optional<Violation> validationResult1 = new UniqueLinksValidator(config).validate(context, link1);
        Optional<Violation> validationResult2 = new UniqueLinksValidator(config).validate(context, link1);
        Optional<Violation> validationResult3 = new UniqueLinksValidator(config).validate(context, link1);
        Assertions.assertFalse(validationResult1.isPresent());
        Assertions.assertFalse(validationResult2.isPresent());
        Assertions.assertFalse(validationResult3.isPresent());
    }

    @DisplayName("When multiple links with no value are present, validation passes")
    @Test
    void linksNotSet() throws Exception {
        Node config = new MockNodeBuilder().withProperty("targetField", "links").build();
        Node node1 = new MockNodeBuilder().withProperty("hippo:docbase", JcrConstants.ROOT_NODE_ID).build();
        Node node2 = new MockNodeBuilder().withProperty("hippo:docbase", JcrConstants.ROOT_NODE_ID).build();
        Node node3 = new MockNodeBuilder().withProperty("different", "b").build();
         new MockNodeBuilder()
                .withChildNode("links", node1)
                .withChildNode("links", node2)
                .withChildNode("links", node3).build();

        Optional<Violation> validationResult1 = new UniqueLinksValidator(config).validate(context, node1);
        Optional<Violation> validationResult2 = new UniqueLinksValidator(config).validate(context, node1);
        Optional<Violation> validationResult3 = new UniqueLinksValidator(config).validate(context, node1);
        Assertions.assertFalse(validationResult1.isPresent());
        Assertions.assertFalse(validationResult2.isPresent());
        Assertions.assertFalse(validationResult3.isPresent());
    }

    @DisplayName("When different link id is used and unique values, test passes")
    @Test
    void differentLinkIdAndUnique() throws Exception {
        Node config = new MockNodeBuilder().withProperty("targetField", "links").withProperty("linkIdField", "id").build();
        Node node1 = new MockNodeBuilder().withProperty("id", "a").build();
        Node node2 = new MockNodeBuilder().withProperty("id", "b").build();
        Node node3 = new MockNodeBuilder().withProperty("hippo:docbase", "c").build();
        Node node4 = new MockNodeBuilder().withProperty("hippo:docbase", "c").build();
        new MockNodeBuilder()
                .withChildNode("links", node1)
                .withChildNode("links", node2)
                .withChildNode("links", node3)
                .withChildNode("links", node4).build();

        Optional<Violation> validationResult1 = new UniqueLinksValidator(config).validate(context, node1);
        Optional<Violation> validationResult2 = new UniqueLinksValidator(config).validate(context, node2);
        Optional<Violation> validationResult3 = new UniqueLinksValidator(config).validate(context, node3);
        Optional<Violation> validationResult4 = new UniqueLinksValidator(config).validate(context, node4);
        Assertions.assertFalse(validationResult1.isPresent());
        Assertions.assertFalse(validationResult2.isPresent());
        Assertions.assertFalse(validationResult3.isPresent());
        Assertions.assertFalse(validationResult4.isPresent());
    }

    @DisplayName("When different link id is used and non-unique values, test fails")
    @Test
    void differentLinkIdAndNonUnique() throws Exception {
        Node config = new MockNodeBuilder().withProperty("targetField", "links").withProperty("linkIdField", "id").build();
        Node node1 = new MockNodeBuilder().withProperty("id", "a").build();
        Node node2 = new MockNodeBuilder().withProperty("id", "a").build();
        Node node3 = new MockNodeBuilder().withProperty("hippo:docbase", "a").build();
        Node node4 = new MockNodeBuilder().withProperty("hippo:docbase", "b").build();
         new MockNodeBuilder()
                .withChildNode("links", node1)
                .withChildNode("links", node2)
                .withChildNode("links", node3)
                .withChildNode("links", node4).build();

        Optional<Violation> validationResult1 = new UniqueLinksValidator(config).validate(context, node1);
        Optional<Violation> validationResult2 = new UniqueLinksValidator(config).validate(context, node2);
        Optional<Violation> validationResult3 = new UniqueLinksValidator(config).validate(context, node3);
        Optional<Violation> validationResult4 = new UniqueLinksValidator(config).validate(context, node4);
        Assertions.assertTrue(validationResult1.isPresent());
        Assertions.assertTrue(validationResult2.isPresent());
        Assertions.assertFalse(validationResult3.isPresent());
        Assertions.assertFalse(validationResult4.isPresent());
    }

    @DisplayName("When linkIdField is node & non-unique links provided, validation fails")
    @Test
    void linkIdIsNode_nonUniqueFails() throws Exception {
        Node config = new MockNodeBuilder().withProperty("targetField", "links").withProperty("linkIdField", "child").build();

        Node aNode = new MockNodeBuilder().withProperty("hippo:docbase", "a").build();
        Node bNode = new MockNodeBuilder().withProperty("hippo:docbase", "a").build();
        Node linkAChild = new MockNodeBuilder().withChildNode("child", aNode).build();
        Node linkBChild = new MockNodeBuilder().withChildNode("child", bNode).build();
        new MockNodeBuilder()
                .withChildNode("links", linkAChild)
                .withChildNode("links", linkBChild).build();

        Optional<Violation> validationResult1 = new UniqueLinksValidator(config).validate(context, linkAChild);
        Optional<Violation> validationResult2 = new UniqueLinksValidator(config).validate(context, linkBChild);
        Assertions.assertTrue(validationResult1.isPresent());
        Assertions.assertTrue(validationResult2.isPresent());
    }

    @DisplayName("When linkIdField is node & unique links provided, validation passes")
    @Test
    void linkIdIsNode_uniquePasses() throws Exception {
        Node config = new MockNodeBuilder().withProperty("targetField", "links").withProperty("linkIdField", "child").build();

        Node aNode = new MockNodeBuilder().withProperty("hippo:docbase", "a").build();
        Node bNode = new MockNodeBuilder().withProperty("hippo:docbase", "b").build();
        Node linkAChild = new MockNodeBuilder().withChildNode("child", aNode).build();
        Node linkBChild = new MockNodeBuilder().withChildNode("child", bNode).build();
        new MockNodeBuilder()
                .withChildNode("links", linkAChild)
                .withChildNode("links", linkBChild).build();

        Optional<Violation> validationResult1 = new UniqueLinksValidator(config).validate(context, linkAChild);
        Optional<Violation> validationResult2 = new UniqueLinksValidator(config).validate(context, linkBChild);
        Assertions.assertFalse(validationResult1.isPresent());
        Assertions.assertFalse(validationResult2.isPresent());
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
        Node parent = new MockNodeBuilder()
                .withChildNode("links", linkAChild)
                .withChildNode("links", linkBChild).build();
        when(parent.hasNode("links")).thenReturn(true);
        when(linkAChild.hasNode("child")).thenReturn(true);
        when(linkBChild.hasNode("child")).thenReturn(true);

        Optional<Violation> validationResult1 = new UniqueLinksValidator(config).validate(context, linkAChild);
        Optional<Violation> validationResult2 = new UniqueLinksValidator(config).validate(context, linkBChild);
        Assertions.assertFalse(validationResult1.isPresent());
        Assertions.assertFalse(validationResult2.isPresent());
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

        new MockNodeBuilder()
                .withChildNode("links", container1)
                .withChildNode("links", container2).build();

        Optional<Violation> validationResult1 = new UniqueLinksValidator(config).validate(context, container1);
        Optional<Violation> validationResult2 = new UniqueLinksValidator(config).validate(context, container2);
        Assertions.assertFalse(validationResult1.isPresent());
        Assertions.assertFalse(validationResult2.isPresent());
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

        new MockNodeBuilder()
                .withChildNode("links", container1)
                .withChildNode("links", container2).build();

        Optional<Violation> validationResult1 = new UniqueLinksValidator(config).validate(context, container1);
        Optional<Violation> validationResult2 = new UniqueLinksValidator(config).validate(context, container2);
        Assertions.assertTrue(validationResult1.isPresent());
        Assertions.assertTrue(validationResult2.isPresent());
    }

}
