package com.visitscotland.brxm.validator;

import com.visitscotland.brxm.hippobeans.InstagramImage;
import com.visitscotland.brxm.hippobeans.ListicleItem;
import com.visitscotland.brxm.hippobeans.SharedLink;
import com.visitscotland.brxm.hippobeans.Stop;
import com.visitscotland.brxm.translation.SessionFactory;
import org.onehippo.cms.services.validation.api.ValidationContext;
import org.onehippo.cms.services.validation.api.Validator;
import org.onehippo.cms.services.validation.api.Violation;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import java.util.Optional;

import static org.hippoecm.repository.api.HippoNodeType.HIPPO_DOCBASE;

/**
 * jcr:Name = visitscotland:link-image-validator
 */
public class LinkImageValidator implements Validator<Node> {

    public static final String EMPTY_IMAGE = "cafebabe-cafe-babe-cafe-babecafebabe";
    private SessionFactory sessionFactory;

    public LinkImageValidator() {
        this.sessionFactory = new SessionFactory();
    }

    LinkImageValidator(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Optional<Violation> validate(final ValidationContext context, final Node document) {
        try {
            Node node = null;
            if (document.hasNode(SharedLink.LINK_TYPES)) {
                node = sessionFactory.getJcrSession().getNodeByIdentifier(document.getNode(SharedLink.LINK_TYPES).getIdentifier());
            } else if (document.hasNode(Stop.PRODUCTS)) {
                node = sessionFactory.getJcrSession().getNodeByIdentifier(document.getNode(Stop.PRODUCTS).getIdentifier());
            } else if (document.hasNode(ListicleItem.PRODUCT)) {
                node = sessionFactory.getJcrSession().getNodeByIdentifier(document.getNode(ListicleItem.PRODUCT).getIdentifier());
            }

            //Make sure that for Share links and stops, if the product is not dms or cms, an image is provided or/and selected
            if (node != null && !node.hasProperty(ListicleItem.PRODUCT) && !node.hasNode("visitscotland:link")) {
                if (document.hasNode("visitscotland:image")) {
                    if (document.getNode("visitscotland:image").getProperty(HIPPO_DOCBASE).getValue().getString().equals(EMPTY_IMAGE)) {
                        return Optional.of(context.createViolation());
                    }
                } else if (document.hasNode(ListicleItem.IMAGES)) {
                    //Images for Listicle that allows CMS images but also Instagram images
                    Node images = sessionFactory.getJcrSession().getNodeByIdentifier(document.getNode(ListicleItem.IMAGES).getIdentifier());
                    if (!images.hasProperty(InstagramImage.CAPTION) && ((!images.hasProperty(HIPPO_DOCBASE))
                            || (images.hasProperty(HIPPO_DOCBASE) && images.getProperty(HIPPO_DOCBASE).getValue().getString().equals(EMPTY_IMAGE)))) {
                        return Optional.of(context.createViolation());
                    }
                } else {
                    return Optional.of(context.createViolation());
                }
            }
        } catch (RepositoryException e) {
            return Optional.of(context.createViolation());
        }

        return Optional.empty();
    }
}

