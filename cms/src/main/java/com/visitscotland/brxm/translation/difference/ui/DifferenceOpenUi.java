package com.visitscotland.brxm.translation.difference.ui;

import com.visitscotland.brxm.translation.SessionFactory;
import com.visitscotland.brxm.translation.TranslationService;
import com.visitscotland.brxm.translation.plugin.JcrDocument;
import com.visitscotland.brxm.translation.plugin.JcrDocumentFactory;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.hippoecm.repository.HippoStdNodeType;
import org.hippoecm.repository.api.HippoNode;
import org.hippoecm.repository.translation.HippoTranslationNodeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.context.annotation.RequestScope;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Controller
@RequestScope
public class DifferenceOpenUi {
    public static final String ATTR_NODE_ID = "nodeId";
    public static final String ATTR_EDITOR = "editor";
    public static final String ATTR_HAS_TRANSLATION_PENDING = "hasTranslationPending";
    public static final String ATTR_HAS_CHANGED = "hasDocumentChanged";
    public static final String ATTR_TRANSLATION_FLAG = "translationFlag";
    public static final String ATTR_ERROR_MESSAGE = "errorMessage";
    public static final String ATTR_STACK_TRACE = "stackTrace";
    public static final String ATTR_DIFF_CONTENT = "diffContent";
    public static final String ATTR_TRANSLATION_LIST = "translations";
    public static final String ENGLISH_TEMPLATE = "diffButtonContentEnglish";
    public static final String FOREIGN_TEMPLATE = "diffButtonContent";
    public static final String EMPTY_TEMPLATE = "diffButtonEmpty";
    public static final String ERROR_TEMPLATE = "diffViewError";
    public static final String DIFF_VIEW_TEMPLATE = "diffView";
    public static final String CONFIRM_DIALOG_TEMPLATE = "dialogTranslationConfirm";
    public static final String COMPLETE_DIALOG_TEMPLATE = "dialogTranslationComplete";
    public static final String BLOCKED_DIALOG_TEMPLATE = "dialogTranslationBlocked";
    public static final String BLOCKED_CURRENT_DIALOG_TEMPLATE = "dialogCurrentBlocked";
    public static final String NO_TRANSLATIONS_DIALOG_TEMPLATE = "dialogNoTranslations";

    private TranslationService translationService;
    private JcrDocumentFactory jcrDocumentFactory;
    private SessionFactory sessionFactory;

    @Autowired
    public DifferenceOpenUi(TranslationService translationService,
                            JcrDocumentFactory jcrDocumentFactory,
                            SessionFactory sessionFactory) {
        this.translationService = translationService;
        this.jcrDocumentFactory = jcrDocumentFactory;
        this.sessionFactory = sessionFactory;
    }

    @GetMapping("/vs-openui/diff")
    public String openUiComponent() {
        return "diffButtonWrapper";
    }

    @GetMapping("/vs-openui/diff/{nodeId}")
    public String openUiButton(@PathVariable String nodeId, Model model) {
        try {
            model.addAttribute(ATTR_NODE_ID, nodeId);
            JcrDocument document = translationService.getDocument(nodeId);
            if (document.getVariantNode(JcrDocument.VARIANT_UNPUBLISHED) == null) {
                return EMPTY_TEMPLATE;
            }
            if ( "en".equals(document.getLocaleName()) ) {
                model.addAttribute(ATTR_HAS_TRANSLATION_PENDING, translationService.hasPendingTranslations(document));
                model.addAttribute(ATTR_HAS_CHANGED, translationService.hasChangePending(document));
                return ENGLISH_TEMPLATE;
            } else {
                model.addAttribute(ATTR_TRANSLATION_FLAG, translationService.getTranslationFlag(document));
                return FOREIGN_TEMPLATE;
            }
        } catch(RepositoryException ex) {
            return gotoErrorPage(ex, model);
        }
    }

    @GetMapping("/vs-openui/diff/{nodeId}/view")
    public String diffView(@PathVariable String nodeId, Model model) {
        try {
            model.addAttribute(ATTR_NODE_ID, nodeId);
            String diff = translationService.getDocumentDifference(nodeId);
            if ( null != diff ) {
                model.addAttribute(ATTR_DIFF_CONTENT, diff);
            } else {
                return gotoErrorPage(new IllegalStateException("No difference found for document."), model);
            }
            return DIFF_VIEW_TEMPLATE;
        } catch(RepositoryException | IOException ex) {
            return gotoErrorPage(ex, model);
        }
    }

    @GetMapping("/vs-openui/{nodeId}/dialog/translation/confirm")
    public String dialogTranslationConfirm(@PathVariable String nodeId, Model model) {
        // Need a list of all the translation languages that the translation will be copied to
        try {
            Node englishNode = sessionFactory.getJcrSession().getNodeByIdentifier(nodeId);
            JcrDocument jcrDocument = jcrDocumentFactory.createFromNode(englishNode);
            Set<JcrDocument> translationDocuments = jcrDocument.getTranslations();
            List<TranslationDocument> translationList = new ArrayList<>();
            for (JcrDocument jcrTranslation : translationDocuments) {
                HippoNode unpublishedNode = (HippoNode)jcrTranslation.getVariantNode(JcrDocument.VARIANT_UNPUBLISHED);
                TranslationDocument translationDocument = new TranslationDocument();
                translationDocument.name = unpublishedNode.getDisplayName();
                String localeString = unpublishedNode.getProperty(HippoTranslationNodeType.LOCALE).getString();
                translationDocument.image = localeString;
                translationList.add(translationDocument);
            }
            model.addAttribute(ATTR_TRANSLATION_LIST, translationList);
            model.addAttribute(ATTR_NODE_ID, nodeId);
            return CONFIRM_DIALOG_TEMPLATE;
        } catch(RepositoryException ex) {
            return gotoErrorPage(ex, model);
        }
    }

    @GetMapping("/vs-openui/{nodeId}/dialog/translation/complete")
    public String dialogTranslationComplete(@PathVariable String nodeId, Model model) {
        model.addAttribute(ATTR_NODE_ID, nodeId);
        return COMPLETE_DIALOG_TEMPLATE;
    }

    @GetMapping("/vs-openui/{nodeId}/dialog/translation/none")
    public String dialogTranslationNone(@PathVariable String nodeId, Model model) {
        model.addAttribute(ATTR_NODE_ID, nodeId);
        return NO_TRANSLATIONS_DIALOG_TEMPLATE;
    }

    @GetMapping("/vs-openui/{nodeId}/dialog/translation/blocked")
    public String dialogTranslationBlocked(@PathVariable String nodeId, Model model) {
        model.addAttribute(ATTR_NODE_ID, nodeId);
        // Need to lookup the foreign language versions that are being edited
        return BLOCKED_DIALOG_TEMPLATE;
    }

    @GetMapping("/vs-openui/{nodeId}/dialog/error")
    public String dialogError(@PathVariable String nodeId, Model model) {
        model.addAttribute(ATTR_NODE_ID, nodeId);
        return ERROR_TEMPLATE;
    }

    @GetMapping("/vs-openui/{nodeId}/dialog/current/blocked")
    public String dialogCurrentBlocked(@PathVariable String nodeId, Model model) {
        model.addAttribute(ATTR_NODE_ID, nodeId);
        String editor;
        try {
            Node currentNode = sessionFactory.getJcrSession().getNodeByIdentifier(nodeId);
            JcrDocument jcrDocument = jcrDocumentFactory.createFromNode(currentNode);
            Node draftNode = jcrDocument.getVariantNode(JcrDocument.VARIANT_DRAFT);
            Property holder = draftNode.getProperty(HippoStdNodeType.HIPPOSTD_HOLDER);
            editor = holder.getString();
        } catch(RepositoryException ex) {
            editor = "unknown";
        }
        model.addAttribute(ATTR_EDITOR, editor);
        return BLOCKED_CURRENT_DIALOG_TEMPLATE;
    }

    public class TranslationDocument {
        public String name;
        public String image;
    }

    public String gotoErrorPage(Exception cause, Model model) {
        model.addAttribute(ATTR_ERROR_MESSAGE, cause.getMessage());
        model.addAttribute(ATTR_STACK_TRACE, ExceptionUtils.getStackTrace(cause));
        return ERROR_TEMPLATE;
    }
}
