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

    //TODO Should we rename these fields?
    /** Mainly used by Listicle */
    final static String PRODUCT = ListicleItem.PRODUCT;
    /** Mainly used by Stop */
    final static String PRODUCTS = Stop.PRODUCTS;
    /** Mainly used by SharedLink */
    final static String LINK_TYPE = SharedLink.LINK_TYPES;

    /**
     * Evaluates if the Media item is required for the document.
     * - When the document is linked to a Page the image can be obtained from the hero Image
     * - When the document is linked to a SharedLink the image can be obtained from the main Image of the document
     * - When the document is linked to a DMS product the image can be obtained from the DMS
     * - In any other case the image should be mandatory.
     */
    private boolean isMediaRequired(final Node document) throws RepositoryException {
        Node node = null;
        if (document.hasNode(LINK_TYPE)) {
            node = document.getNode(LINK_TYPE);
        } else if (document.hasNode(PRODUCTS)) {
            node = document.getNode(PRODUCTS);
        } else if (document.hasNode(PRODUCT)) {
            node = document.getNode(PRODUCT);
        }

        return node != null && !node.isNodeType("visitscotland:CMSLink") && !node.isNodeType("visitscotland:DMSLink");
    }

    @Override
    public Optional<Violation> validate(final ValidationContext context, final Node document) {
        try {
            if (isMediaRequired(document)) {
                boolean valid = false;
                Node media;

                if (document.hasNode("visitscotland:image")) {
                    media = document.getNode("visitscotland:image");
                } else if (document.hasNode("visitscotland:images")) {
                    media = document.getNode("visitscotland:images");
                } else if (document.hasNode("visitscotland:media")) {
                    media = document.getNode("visitscotland:media");
                } else {
                    return Optional.of(context.createViolation());
                }

                if (media.hasProperty(HIPPO_DOCBASE)) {
                    valid = !media.getProperty(HIPPO_DOCBASE).getString().equals(EMPTY_IMAGE);
                } else if (media.isNodeType("visitscotland:VideoLink") ||
                        media.isNodeType("visitscotland:InstagramImage")) {
                    valid = true;
                }

                if (!valid) {
                    return Optional.of(context.createViolation());
                }
            }
        } catch (RepositoryException e) {
            return Optional.of(context.createViolation());
        }

        return Optional.empty();
    }
}

