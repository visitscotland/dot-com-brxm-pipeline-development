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
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * jcr:Name = visitscotland:scotland-coordinates-validator
 *
 * @author jose.calcines
 */
public class ArticleImageValidator implements Validator<Node> {

    private static final Logger logger = LoggerFactory.getLogger(LinkValidator.class);

    static final String THEMES = "themes";

    private Set<String> themes;

    public ArticleImageValidator(final Node config) {

        try {
            if (config.hasProperty(THEMES)) {
                themes = new HashSet<>();
                for (Value v: config.getProperty(THEMES).getValues()){
                    themes.add(v.getString());
                }
            }
        } catch (RepositoryException e) {
            throw new ValidationContextException("Unexpected Error when loading ArticleImageValidator class", e);
        }
    }

    public Optional<Violation> validate(final ValidationContext context, final Node node) {
        try {
            if (themes.contains(node.getProperty(Article.THEME).getValue().getString())){
                for (NodeIterator it = node.getNodes(Article.PARAGRAPH); it.hasNext(); ) {
                    Node section = it.nextNode();
                    if (!section.hasNode(ArticleSection.MEDIA)
                            || !section.getNode(ArticleSection.MEDIA).hasProperty("hippo:docbase")
                            || section.getNode(ArticleSection.MEDIA).getProperty("hippo:docbase").getString().equals(ImageValidator.EMPTY_IMAGE)) {
                        return Optional.of(context.createViolation());
                    }
                }
            }
            return Optional.empty();
        } catch (RepositoryException e) {
            return Optional.of(context.createViolation());
        }
    }
}
