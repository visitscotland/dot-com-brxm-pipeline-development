package com.visitscotland.brxm.validator;

import com.visitscotland.brxm.translation.MockNodeBuilder;
import com.visitscotland.brxm.translation.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.AdditionalMatchers;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.onehippo.cms.services.validation.api.ValidationContext;
import org.onehippo.cms.services.validation.api.Violation;

import javax.jcr.*;

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

    LinkValidator validator;

    @BeforeEach
    void init(){
        validator = new LinkValidator(mockSessionFactory);
    }

    @Test
    @DisplayName("Validates that links have been selected")
    void LinksEmptyValues() throws RepositoryException {
        Node node = Mockito.mock(Node.class, RETURNS_DEEP_STUBS);

        when(node.hasProperty(HIPPO_DOCBASE)).thenReturn(true);
        when(node.getProperty(HIPPO_DOCBASE).getString()).thenReturn(LinkValidator.EMPTY_DOCUMENT);
        when(context.createViolation("emptyLink")).thenReturn(mock(Violation.class));

        assertTrue(validator.validate(context, node).isPresent());
    }

    @ParameterizedTest
    @CsvSource({
            "visitscotland:MegalinkItem,visitscotland:Page",
            "visitscotland:OTYML,visitscotland:SharedLink",
            "visitscotland:MadeUpDocument,visitscotland:SharedLink",
            "visitscotland:Day,visitscotland:Stop",
            "visitscotland:VideoLink,visitscotland:Video",
            "visitscotland:MapCategory,visitscotland:Destination",
            "visitscotland:MapCategory,visitscotland:Stop",
            "visitscotland:SpecialLinkCoordinates,visitscotland:Page"
    })
    @DisplayName("VS-2905 - Validates that links are correctly validated depending on the parent")
    void correctValues(String parentType, String childType) throws RepositoryException {
        assertFalse(validator.validate(context, mockLink(parentType, childType, true)).isPresent());
    }

    @ParameterizedTest
    @CsvSource({
            "visitscotland:MegalinkItem,visitscotland:MegalinkItem",
            "visitscotland:MegalinkItem,visitscotland:Video",
            "visitscotland:MegalinkItem,visitscotland:Stop",
            "visitscotland:Day,visitscotland:SharedLink",
            "visitscotland:Day,visitscotland:Video",
            "visitscotland:VideoLink,visitscotland:Stop",
            "visitscotland:VideoLink,visitscotland:Page",
            "visitscotland:MapCategory,visitscotland:Video",
            "visitscotland:MapCategory,visitscotland:General",
            "visitscotland:SpecialLinkCoordinates,visitscotland:Stop"
    })
    @DisplayName("VS-2905 - Invalid documents cause a validation exception")
    void incorrectValues(String parentType, String childType) throws RepositoryException {
         assertTrue(validator.validate(context, mockLink(parentType, childType, false)).isPresent());
    }

    @Test
    @DisplayName("VS-2886 - documents can't link to a documents in different languages (except english)")
    void incorrectChannel() throws RepositoryException {
        Node parentNode = Mockito.mock(Node.class, withSettings().defaultAnswer(RETURNS_DEEP_STUBS));
        Node childNode = Mockito.mock(Node.class, withSettings().defaultAnswer(RETURNS_DEEP_STUBS));

        when(parentNode.hasProperty(HIPPO_DOCBASE)).thenReturn(true);
        when(parentNode.getProperty(HIPPO_DOCBASE).getString()).thenReturn("NODE-ID");
        when(mockSessionFactory.getHippoNodeByIdentifier("NODE-ID")).thenReturn(childNode);

        when(parentNode.getPath()).thenReturn("/document/content/visitscotland");
        when(childNode.getPath()).thenReturn("/document/content/visitscotland-es");

        when(context.createViolation("channel")).thenReturn(mock(Violation.class));
        assertTrue(validator.validate(context, parentNode).isPresent());
    }

    @Test
    @DisplayName("VS-3348 - link to a deleted document")
    void deletedDocumentChannel() throws RepositoryException {
        Node parentNode = Mockito.mock(Node.class, withSettings().defaultAnswer(RETURNS_DEEP_STUBS));
        Node childNode = Mockito.mock(Node.class, withSettings().defaultAnswer(RETURNS_DEEP_STUBS));

        when(parentNode.hasProperty(HIPPO_DOCBASE)).thenReturn(true);
        when(parentNode.getProperty(HIPPO_DOCBASE).getString()).thenReturn("NODE-ID");
        when(mockSessionFactory.getHippoNodeByIdentifier("NODE-ID")).thenReturn(childNode);

        when(childNode.getPath()).thenReturn("/content/attic/");

        when(context.createViolation("removedLink")).thenReturn(mock(Violation.class));
        assertTrue(validator.validate(context, parentNode).isPresent());
    }

    @Test
    @DisplayName("VS-2784 - documents can't link to themselves - direct link on content")
    void linkToSelf() throws Exception {
        Node linkedToNode = new MockNodeBuilder().withNodeId("link-id").withPrimaryNodeType("visitscotland:Page").build();
        when(linkedToNode.getPath()).thenReturn("/a/b/c/d");
        when(mockSessionFactory.getHippoNodeByIdentifier("link-id")).thenReturn(linkedToNode);

        Node link = new MockNodeBuilder().withProperty(HIPPO_DOCBASE, "link-id").build();
        when(link.getPath()).thenReturn("/a/b/c/d");

        Node content = new MockNodeBuilder().withNodeId("link-id").build();
        Node folder =  new MockNodeBuilder().withPrimaryNodeType("hippostd:folder").withChildNode("content", content).build();
        when(link.getParent()).thenReturn(content);
        when(content.getParent()).thenReturn(folder);

        when(context.createViolation("linkToSelf")).thenReturn(mock(Violation.class));

        assertTrue(validator.validate(context, link).isPresent());
    }

    @Test
    @DisplayName("VS-2784 - documents can't link to themselves - link to self from module")
    void linkToSelfFromModule() throws Exception {
        Node linkedToNode = new MockNodeBuilder().withNodeId("link-id").withPrimaryNodeType("visitscotland:Page").build();
        when(linkedToNode.getPath()).thenReturn("/a/b/c/d");
        when(mockSessionFactory.getHippoNodeByIdentifier("link-id")).thenReturn(linkedToNode);

        Node link = new MockNodeBuilder().withProperty(HIPPO_DOCBASE, "link-id").build();
        when(link.getPath()).thenReturn("/a/b/c/d");

        Node module = new MockNodeBuilder().build();
        Node content = new MockNodeBuilder().withNodeId("link-id").build();
        Node folder =  new MockNodeBuilder().withPrimaryNodeType("hippostd:folder").withChildNode("content", content).build();
        when(link.getParent()).thenReturn(module);
        when(module.getParent()).thenReturn(folder);

        when(context.createViolation("linkToSelf")).thenReturn(mock(Violation.class));

        assertTrue(validator.validate(context, link).isPresent());
    }

    /**
     * Mocks a Document that contains a link, the linked document and stubs any expected behaviour during the validation
     *
     * @param parentType JCR Type that act as a container (i.e. visitscotland:VideoLink)
     * @param childType JCR Type that represent the linked document (i.e. visitscotland:Video)
     *
     * @return Node to be validated against the validator
     */
    private Node mockLink(String parentType, String childType, boolean expected) throws  RepositoryException{
        Node parentNode = Mockito.mock(Node.class, withSettings().lenient());
        Node childNode = Mockito.mock(Node.class, withSettings().lenient());
        boolean isDefault = !LinkValidator.DAY.equals(parentType) && !LinkValidator.VIDEO.equals(parentType) && !LinkValidator.MAP.equals(parentType) && !LinkValidator.LINK_COORDINATES.equals(parentType);

        Property docbaseProp = mock(Property.class);
        when(parentNode.hasProperty(HIPPO_DOCBASE)).thenReturn(true);
        when(docbaseProp.getString()).thenReturn("NODE-ID");
        when(parentNode.getProperty(HIPPO_DOCBASE)).thenReturn(docbaseProp);
        when(mockSessionFactory.getHippoNodeByIdentifier("NODE-ID")).thenReturn(childNode);
        Node linkParent = mock(Node.class);
        lenient().when(linkParent.getParent()).thenThrow(ItemNotFoundException.class);
        lenient().when(linkParent.isNodeType(AdditionalMatchers.not(eq(parentType)))).thenReturn(false);
        lenient().when(parentNode.getParent()).thenReturn(linkParent);
        if (!isDefault){
            when(linkParent.isNodeType(parentType)).thenReturn(true);
        }
        when(childNode.getPath()).thenReturn("/content/document/visitscotland");
        when(childNode.isNodeType(childType)).thenReturn(true);
        when(childNode.isNodeType(AdditionalMatchers.not(eq(childType)))).thenReturn(false);

        if (!expected) {
            if (isDefault) {
                when(context.createViolation()).thenReturn(mock(Violation.class));
            } else {
                when(context.createViolation(any(String.class))).thenReturn(mock(Violation.class));
            }
        }

        return parentNode;
    }

}
