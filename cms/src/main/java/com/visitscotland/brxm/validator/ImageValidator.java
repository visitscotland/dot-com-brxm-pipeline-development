package com.visitscotland.brxm.validator;

import com.visitscotland.brxm.hippobeans.Image;
import com.visitscotland.brxm.translation.SessionFactory;
import com.visitscotland.utils.Contract;
import org.onehippo.cms.services.validation.api.ValidationContext;
import org.onehippo.cms.services.validation.api.Validator;
import org.onehippo.cms.services.validation.api.Violation;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import java.util.Optional;

import static org.hippoecm.repository.api.HippoNodeType.HIPPO_DOCBASE;

/**
 * The purpose of this validator is to validate if a SELECTED image has all the required fields filled,therefore, we need to
 * check the user has selected and image (so image is not an EMPTY_IMAGE)
 *
 * This validator does * not validate if an image is required or optional
 *
 * jcr:Name = visitscotland:image-validator
 */
public class ImageValidator implements Validator<Node> {

    public static final String EMPTY_IMAGE = "cafebabe-cafe-babe-cafe-babecafebabe";
    private SessionFactory sessionFactory;

    public ImageValidator() {
        this(new SessionFactory());
    }

    ImageValidator(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Optional<Violation> validate(final ValidationContext context, final Node document) {
        try {
            String nodeId = document.getProperty(HIPPO_DOCBASE).getValue().getString();
            if(!nodeId.equals(EMPTY_IMAGE)) {
                Node childNode = sessionFactory.getHippoNodeByIdentifier(nodeId);
                if (!childNode.hasProperty("hippogallery:description")) {
                    return Optional.of(context.createViolation());
                } else {
                    if (childNode.hasProperty("hippogallery:description") && Contract.isEmpty(childNode.getProperty("hippogallery:description").getString())) {
                        return Optional.of(context.createViolation());
                    }
                }
            }
        } catch (RepositoryException e) {
            return Optional.of(context.createViolation());
        }

        return Optional.empty();
    }
}

