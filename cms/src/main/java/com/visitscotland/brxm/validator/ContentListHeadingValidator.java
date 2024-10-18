package com.visitscotland.brxm.validator;

import com.visitscotland.brxm.hippobeans.Article;
import com.visitscotland.utils.Contract;
import org.onehippo.cms.services.validation.api.ValidationContext;
import org.onehippo.cms.services.validation.api.ValidationContextException;
import org.onehippo.cms.services.validation.api.Validator;
import org.onehippo.cms.services.validation.api.Violation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Value;
import java.util.*;

/**
 * jcr:Name = visitscotland:scotland-coordinates-validator
 *
 * @author jose.calcines
 */
public class ContentListHeadingValidator implements Validator<Node> {

    private static final Logger logger = LoggerFactory.getLogger(LinkValidator.class);

    static final String MANDATORY_HEADING = "mandatory.heading";

    private Set<String> themes;

    public ContentListHeadingValidator(final Node config) {

        try {
            if (config.hasProperty(MANDATORY_HEADING)) {
                themes = new HashSet<>();
                for (Value v: config.getProperty(MANDATORY_HEADING).getValues()){
                    themes.add(v.getString());
                }
            }
        } catch (RepositoryException e) {
            throw new ValidationContextException("Unexpected Error when loading ArticleCopyValidator class", e);
        }
    }

    public Optional<Violation> validate(final ValidationContext context, final Node document) {
        try {
            if (!hasValidHeading(document)){
                return Optional.of(context.createViolation());
            }
            return Optional.empty();
        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        }

    }

    private boolean hasValidHeading(final Node node) throws RepositoryException {
        if (themes.contains(node.getProperty(Article.THEME).getValue().getString())){
            return !Contract.isEmpty(node.getProperty("visitscotlan:heading").getString());
        }
        return true;
    }

}
