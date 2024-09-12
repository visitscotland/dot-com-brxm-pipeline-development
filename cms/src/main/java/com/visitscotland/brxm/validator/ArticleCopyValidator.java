package com.visitscotland.brxm.validator;

import com.visitscotland.brxm.hippobeans.Article;
import com.visitscotland.brxm.hippobeans.ArticleSection;
import org.onehippo.cms.services.validation.api.ValidationContext;
import org.onehippo.cms.services.validation.api.ValidationContextException;
import org.onehippo.cms.services.validation.api.Validator;
import org.onehippo.cms.services.validation.api.Violation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Value;
import java.util.*;
import java.util.stream.Collectors;

/**
 * jcr:Name = visitscotland:scotland-coordinates-validator
 *
 * @author jose.calcines
 */
public class ArticleCopyValidator implements Validator<Node> {

    private static final Logger logger = LoggerFactory.getLogger(LinkValidator.class);

    static final String MANDATORY_H3 = "mandatory.h3";

    private Set<String> mandatoryH3;

    public ArticleCopyValidator(final Node config) {

        try {
            if (config.hasProperty(MANDATORY_H3)) {
                mandatoryH3 = new HashSet<>();
                for (Value v: config.getProperty(MANDATORY_H3).getValues()){
                    mandatoryH3.add(v.getString());
                }
            }
        } catch (RepositoryException e) {
            throw new ValidationContextException("Unexpected Error when loading ArticleCopyValidator class", e);
        }
    }

    public Optional<Violation> validate(final ValidationContext context, final Node document) {
        try {
            if (!validateH3(document)){
                return Optional.of(context.createViolation());
            }
            return Optional.empty();
        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        }

    }

    private boolean validateH3(final Node node) throws RepositoryException {
        if (mandatoryH3.contains(node.getProperty(Article.THEME).getValue().getString())){
            for (NodeIterator it = node.getNodes(Article.PARAGRAPH); it.hasNext(); ) {
                Node section = it.nextNode();
                if (!section.getNode(ArticleSection.COPY).getProperty("hippostd:content").getString().startsWith("<h3>")){
                    return false;
                }
            }
        }
        return true;
    }

}
