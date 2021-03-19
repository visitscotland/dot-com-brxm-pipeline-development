package com.visitscotland.brxm.validator;

import com.visitscotland.brxm.hippobeans.Image;
import com.visitscotland.brxm.translation.SessionFactory;
import org.onehippo.cms.services.validation.api.ValidationContext;
import org.onehippo.cms.services.validation.api.Validator;
import org.onehippo.cms.services.validation.api.Violation;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import java.util.Optional;

import static org.hippoecm.repository.api.HippoNodeType.HIPPO_DOCBASE;

/**
 * jcr:Name = visitscotland:image-validator
 */
public class ImageValidator implements Validator<Node> {

    private static final String EMPTY_IMAGE = "cafebabe-cafe-babe-cafe-babecafebabe";
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
            if (!nodeId.equals(EMPTY_IMAGE)) {
                Node childNode = sessionFactory.getHippoNodeByIdentifier(nodeId);
                if (!childNode.hasProperty(Image.CREDIT) || !childNode.hasProperty(Image.ALT_TEXT)) {
                    return Optional.of(context.createViolation());
                }
            }
        } catch (RepositoryException e) {
            return Optional.of(context.createViolation());
        }

        return Optional.empty();
    }
}

