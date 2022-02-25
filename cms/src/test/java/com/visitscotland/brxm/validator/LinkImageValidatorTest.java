package com.visitscotland.brxm.validator;

import com.visitscotland.brxm.hippobeans.*;
import com.visitscotland.brxm.translation.MockNodeBuilder;
import com.visitscotland.brxm.translation.SessionFactory;
import jdk.nashorn.internal.runtime.regexp.joni.constants.NodeType;
import org.junit.Ignore;
import org.junit.jupiter.api.BeforeEach;
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
import static org.hippoecm.repository.api.HippoNodeType.NT_MIRROR;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LinkImageValidatorTest {

    @Mock(lenient = true)
    ValidationContext context;

    @BeforeEach
    void init (){
        when(context.createViolation()).thenReturn(mock(Violation.class));
    }

    @Test
    @DisplayName("Shared links - Validation fails when the product is not a DMS and no image is provided")
    void sharedLinkNoDms_noImageProvided() throws RepositoryException {
        LinkImageValidator validator = new LinkImageValidator();
        Node image = new MockNodeBuilder().withPrimaryNodeType(NT_MIRROR).withProperty(HIPPO_DOCBASE, LinkImageValidator.EMPTY_IMAGE).build();
        Node document = new MockNodeBuilder()
                .withChildNode(LinkImageValidator.PRODUCT, Mockito.mock(Node.class))
                .withChildNode("visitscotland:image", image).build();

        assertTrue(validator.validate(context, document).isPresent());
    }

    @Test
    @DisplayName("Shared links - Validation passes")
    void sharedLinkNoDms_validImage() throws RepositoryException {
        LinkImageValidator validator = new LinkImageValidator();
        Node image = new MockNodeBuilder().withPrimaryNodeType(NT_MIRROR).withProperty(HIPPO_DOCBASE, "node-id-for-image").build();
        Node document = new MockNodeBuilder()
                .withChildNode(LinkImageValidator.PRODUCT, Mockito.mock(Node.class))
                .withChildNode("visitscotland:image", image).build();

        assertFalse(validator.validate(context, document).isPresent());
    }

    @Test
    @DisplayName("Stop - Validation fails when the product is not a DMS and no image is provided")
    void stopNoDms_noImageProvided() throws RepositoryException {
        LinkImageValidator validator = new LinkImageValidator();
        Node image = new MockNodeBuilder().withPrimaryNodeType(NT_MIRROR).withProperty(HIPPO_DOCBASE, LinkImageValidator.EMPTY_IMAGE).build();
        Node document = new MockNodeBuilder()
                .withChildNode(LinkImageValidator.PRODUCTS, Mockito.mock(Node.class))
                .withChildNode("visitscotland:image", image).build();

        assertTrue(validator.validate(context, document).isPresent());
    }

    @Test
    @DisplayName("Stops - Validation passes")
    void stopNoDms_validImage() throws RepositoryException {
        LinkImageValidator validator = new LinkImageValidator();
        Node image = new MockNodeBuilder().withPrimaryNodeType(NT_MIRROR).withProperty(HIPPO_DOCBASE, "node-id-for-image").build();
        Node document = new MockNodeBuilder()
                .withChildNode(LinkImageValidator.PRODUCTS, Mockito.mock(Node.class))
                .withChildNode("visitscotland:image", image).build();

        assertFalse(validator.validate(context, document).isPresent());
    }

    @Test
    @DisplayName("Listicle - Validation fails when the product is not a DMS or CMSLink and no media is provided")
    void listicleNoDms_noContentblockImagesProvided() throws RepositoryException {
        LinkImageValidator validator = new LinkImageValidator();
        Node document = new MockNodeBuilder()
                .withChildNode(LinkImageValidator.PRODUCT, new MockNodeBuilder().build())
                .build();

        assertTrue(validator.validate(context, document).isPresent());
    }


    @Test
    @DisplayName("Listicle - The image is not mandatory for CMS Items (Shared Link & Pages)")
    void listicle_cmsLink() throws RepositoryException {
        LinkImageValidator validator = new LinkImageValidator();
        Node product = new MockNodeBuilder().withPrimaryNodeType("visitscotland:CMSLink").build();
        Node document = new MockNodeBuilder()
                .withChildNode(LinkImageValidator.PRODUCT, product).build();

        assertFalse(validator.validate(context, document).isPresent());
  }

    @Test
    @DisplayName("Validation passes when an Instagram Image is provided")
    void listicleNoDms_InstagramProvided() throws RepositoryException {
        LinkImageValidator validator = new LinkImageValidator();
        Node media = new MockNodeBuilder().withPrimaryNodeType("visitscotland:InstagramImage").build();
        Node compound = new MockNodeBuilder().withPrimaryNodeType("visitscotland:CMSLink").build();
        Node document = new MockNodeBuilder()
                .withChildNode(LinkImageValidator.PRODUCT, compound)
                .withChildNode("visitscotland:image", media).build();

        assertFalse(validator.validate(context, document).isPresent());
    }

    @Test
    @DisplayName("Validation passes when a video is provided")
    void video() throws RepositoryException {

        LinkImageValidator validator = new LinkImageValidator();
        Node media = new MockNodeBuilder().withPrimaryNodeType("visitscotland:VideoLink").build();
        Node document = new MockNodeBuilder()
                .withChildNode(LinkImageValidator.PRODUCT, new MockNodeBuilder().build())
                .withChildNode("visitscotland:image", media).build();

        assertFalse(validator.validate(context, document).isPresent());
    }
}
