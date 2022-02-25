package com.visitscotland.brxm.validator;

import com.visitscotland.brxm.translation.SessionFactory;
import com.visitscotland.brxm.translation.plugin.JcrDocument;
import org.onehippo.cms.services.validation.api.ValidationContext;
import org.onehippo.cms.services.validation.api.Validator;
import org.onehippo.cms.services.validation.api.Violation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.*;
import java.util.Locale;
import java.util.Optional;

import static org.hippoecm.repository.api.HippoNodeType.HIPPO_DOCBASE;

/**
 * jcr:Name = visitscotland:link-validator

 */
public class LinkValidator implements Validator<Node> {
    private static final Logger logger = LoggerFactory.getLogger(LinkValidator.class);

    public static final String EMPTY_DOCUMENT = "cafebabe-cafe-babe-cafe-babecafebabe";
    public static final String ENGLISH_CHANNEL = "visitscotland";
    private SessionFactory sessionFactory;

    static final String DAY = "visitscotland:Day";
    static final String VIDEO = "visitscotland:VideoLink";

    public LinkValidator() {
        this.sessionFactory = new SessionFactory();
    }

    LinkValidator(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }

    public Optional<Violation> validate(final ValidationContext context, final Node document) {
        try {
            String nodeId = document.getProperty(HIPPO_DOCBASE).getString();
            if (!nodeId.equals(EMPTY_DOCUMENT)) {
                Node childNode = sessionFactory.getHippoNodeByIdentifier(nodeId);
                String childNodeChannel = childNode.getPath().split("/")[3];
                //VS-2886 Any language can link to english documents but no to any other different language
                if (!childNodeChannel.equals(ENGLISH_CHANNEL) && !document.getPath().split("/")[3].equals(childNodeChannel)) {
                    return Optional.of(context.createViolation("channel"));
                } else {
                    Optional<Violation> checkAllowed = checkAllowedDocuments(context, document, childNode);
                    if (checkAllowed.isPresent()) {
                        return checkAllowed;
                    }
                    return checkLinkToSameDocument(context, document, nodeId);
                }
            } else {
                return Optional.of(context.createViolation("emptyLink"));
            }
        } catch (PathNotFoundException e) {
            return Optional.of(context.createViolation("translation"));
        } catch (RepositoryException e) {
            return Optional.of(context.createViolation());
        }
    }

    public Optional<Violation> checkLinkToSameDocument(final ValidationContext context, final Node document, final String linkNodeId) throws RepositoryException {
        try {
            Node folder = document;
            while (!folder.isNodeType("hippostd:folder")) {
                folder = folder.getParent();
            }
            Node contentNode = folder.getNode("content");
            if (contentNode.getIdentifier().equals(linkNodeId)) {
                return Optional.of(context.createViolation("linkToSelf"));
            }
        } catch (ItemNotFoundException | PathNotFoundException e) {
            logger.info("Failed to find folder or content relative to node {}", document.getPath(), e);
        }
        return Optional.empty();
    }

    private Optional<Violation> checkAllowedDocuments(final ValidationContext context, final Node document, final Node childNode) throws RepositoryException {
        if (document.getParent().isNodeType(DAY)) {
            if (!childNode.isNodeType("visitscotland:Stop")) {
                return Optional.of(context.createViolation("stop"));
            }
        } else if (document.getParent().isNodeType(VIDEO)) {
            if (!childNode.isNodeType("visitscotland:Video")){
                return Optional.of(context.createViolation("video"));
            }
        } else {
            if (!childNode.isNodeType("visitscotland:Page") && !childNode.isNodeType("visitscotland:SharedLink")){
                return Optional.of(context.createViolation());
            }
        }

        return Optional.empty();
    }


}

