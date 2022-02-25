package com.visitscotland.brxm.report.translation;

import com.visitscotland.brxm.report.ReportException;
import com.visitscotland.brxm.translation.SessionFactory;
import com.visitscotland.brxm.translation.plugin.JcrDocument;
import com.visitscotland.brxm.translation.plugin.JcrDocumentFactory;
import com.visitscotland.brxm.translation.plugin.TranslationWorkflow;
import com.visitscotland.brxm.utils.Language;
import com.visitscotland.utils.Contract;
import org.hippoecm.repository.api.HippoNode;
import org.hippoecm.repository.api.Workflow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.jcr.*;
import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class TranslationReportService {

    private final Logger log = LoggerFactory.getLogger(TranslationReportService.class);
    private final String LAST_MODIFIED_BY_PROPERTY = "hippostdpubwf:lastModifiedBy";
    private final String LAST_MODIFIED_DATE_PROPERTY = "hippostdpubwf:lastModificationDate";
    private final List<String> SUPPORTED_LOCALES = Language.getLocales().stream().map(Locale::getLanguage).collect(Collectors.toList());
    private Set<String> cachedPageTypes;
    private Set<String> cachedModuleTypes;

    private final SessionFactory sessionFactory;
    private final JcrDocumentFactory jcrDocumentFactory;
    private final JcrUtilService jcrUtilService;

    public TranslationReportService(SessionFactory sessionFactory, JcrDocumentFactory jcrDocumentFactory, JcrUtilService jcrUtilService) {
        this.sessionFactory = sessionFactory;
        this.jcrDocumentFactory = jcrDocumentFactory;
        this.jcrUtilService = jcrUtilService;
    }

    public void setTranslationPriority(String handleId, TranslationPriority priority) {
        try {
            Node jcrNode = sessionFactory.getJcrSession().getNodeByIdentifier(handleId);
            JcrDocument doc = jcrDocumentFactory.createFromNode(jcrNode);
            Workflow workflow = sessionFactory.getUserSession().getWorkflowManager().getWorkflow("translation", doc.getVariantNode(JcrDocument.VARIANT_UNPUBLISHED));
            if (workflow instanceof TranslationWorkflow) {
                TranslationWorkflow translationWorkflow = (TranslationWorkflow) workflow;
                translationWorkflow.setTranslationPriority(priority);
            } else {
                throw new ReportException("Failed to load TranslationWorkflow");
            }
        } catch (ItemNotFoundException ex) {
            throw new ReportException("Can not find item", ex);
        } catch (RepositoryException | RemoteException ex) {
            throw new ReportException("Failed to update item", ex);
        }
    }

    public void setTranslationDeadline(String handleId, Calendar deadline) {
        try {
            Node jcrNode = sessionFactory.getJcrSession().getNodeByIdentifier(handleId);
            JcrDocument doc = jcrDocumentFactory.createFromNode(jcrNode);
            Workflow workflow = sessionFactory.getUserSession().getWorkflowManager().getWorkflow("translation", doc.getVariantNode(JcrDocument.VARIANT_UNPUBLISHED));
            if (workflow instanceof TranslationWorkflow) {
                TranslationWorkflow translationWorkflow = (TranslationWorkflow) workflow;
                translationWorkflow.setTranslationDeadline(deadline);
            } else {
                throw new ReportException("Failed to load TranslationWorkflow");
            }
        } catch (ItemNotFoundException ex) {
            throw new ReportException("Can not find item", ex);
        } catch (RepositoryException | RemoteException ex) {
            throw new ReportException("Failed to update item", ex);
        }
    }

    public List<DocumentTranslationReportModel> getUntranslatedDocuments(String targetLocale) {
        List<DocumentTranslationReportModel> docModels = new ArrayList<>();
        try {
            for (JcrDocument englishDoc : jcrUtilService.getAllUnpublishedDocuments()) {
                Set<String> translatedLocales = new HashSet<>();
                Set<String> sendForTranslationLocales = new HashSet<>();
                TranslationStatus infoStatus = TranslationStatus.NOT_SENT_FOR_TRANSLATION;

                for (String locale : SUPPORTED_LOCALES) {
                    TranslationStatus status = getDocumentTranslationStatus(englishDoc, locale);
                    if (status == TranslationStatus.TRANSLATED) {
                        translatedLocales.add(locale);
                    } else if (status == TranslationStatus.SEND_FOR_TRANSLATION) {
                        sendForTranslationLocales.add(locale);
                    }

                    if (locale.equals(targetLocale)) {
                        infoStatus = status;
                    }
                }
                if (infoStatus == TranslationStatus.TRANSLATED) continue;
                String displayName = ((HippoNode) englishDoc.getHandle()).getDisplayName();
                TranslationPriority translationPriority = getNodeTranslationPriority(englishDoc);
                Node unpublished = englishDoc.getVariantNode(JcrDocument.VARIANT_UNPUBLISHED);
                String primaryNodeType = unpublished.getProperty("jcr:primaryType").getString().replace("visitscotland:", "");
                String lastModifiedBy = unpublished.hasProperty(LAST_MODIFIED_BY_PROPERTY) ? unpublished.getProperty(LAST_MODIFIED_BY_PROPERTY).getString() : "";
                String lastModified = getDateAsIsoString(unpublished, LAST_MODIFIED_DATE_PROPERTY);
                String translationDeadline = getDateAsIsoString(unpublished, JcrDocument.VS_TRANSLATION_DEADLINE);
                docModels.add(new DocumentTranslationReportModel(englishDoc.getHandle().getIdentifier(), displayName,
                        infoStatus.toString(), translationPriority, translatedLocales, sendForTranslationLocales,
                        primaryNodeType, lastModified, lastModifiedBy, getDocumentPublishStatus(englishDoc), translationDeadline));
            }
        } catch (RepositoryException ex) {
            throw new ReportException("Failed to access repository", ex);
        }

        return docModels;
    }

    public Set<String> getPageTypes() {
        try {
            if (cachedPageTypes == null) {
                cachedPageTypes = jcrUtilService.getTypesDeriving("visitscotland:Page");
            }
        } catch (RepositoryException ex) {
            throw new ReportException("Failed to access repository", ex);
        }

        return cachedPageTypes;
    }

    public Set<String> getModuleTypes() {
        try {
            if (cachedModuleTypes == null) {
                cachedModuleTypes = jcrUtilService.getTypesDeriving("visitscotland:basedocument");
                cachedModuleTypes.remove("Page");
            }
        } catch (RepositoryException ex) {
            throw new ReportException("Failed to access repository", ex);
        }

        return cachedModuleTypes;
    }

    public boolean isLocaleSupported(String locale) {
        return SUPPORTED_LOCALES.contains(locale);
    }

    private String getDateAsIsoString(Node node, String property) throws RepositoryException {
        if (!node.hasProperty(property)) return "";
        try {
            Calendar dateProp = node.getProperty(property).getDate();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
            df.setTimeZone(TimeZone.getTimeZone("UTC"));
            return df.format(dateProp.getTime());
        } catch (ValueFormatException ex) {
            return "";
        }
    }

    private PublishStatus getDocumentPublishStatus(JcrDocument document) throws RepositoryException {
        Node unpublishedNode = document.getVariantNode(JcrDocument.VARIANT_UNPUBLISHED);
        if (unpublishedNode == null) {
            return PublishStatus.NOT_LIVE;
        }
        switch (unpublishedNode.getProperty("hippostd:stateSummary").getString()) {
            case "live":
                return PublishStatus.CURR_VERSION_LIVE;
            case "new":
                return PublishStatus.NOT_LIVE;
            case "changed":
                return PublishStatus.PREV_VERSION_LIVE;
            default: return PublishStatus.UNKNOWN;
        }
    }

    private TranslationPriority getNodeTranslationPriority(JcrDocument document) throws RepositoryException {
        Node node = document.getVariantNode(JcrDocument.VARIANT_UNPUBLISHED);
        String priorityString = node.hasProperty(JcrDocument.VS_TRANSLATION_PRIORITY) ?
                node.getProperty(JcrDocument.VS_TRANSLATION_PRIORITY).getString() : "";
        if (Contract.isEmpty(priorityString)) {
            return TranslationPriority.NORMAL;
        }

        try {
            return TranslationPriority.valueOf(priorityString);
        } catch (IllegalArgumentException ex) {
            log.error("Bad " + JcrDocument.VS_TRANSLATION_PRIORITY + " on node " + node.getIdentifier() + " of " + priorityString, ex);
        }
        return TranslationPriority.NORMAL;
    }

    /**
     * Determines the translation status of a given document (i.e. Untranslated, Sent for translation or Untranslated)
     *
     * Depends on value of the visitscotland:translationFlag. This is a boolean, but encodes three different states
     *      - Empty/missing: The document has not been sent for translation yet
     *      - true           Set when the document has been sent for translation
     *      - false          Set when the document has been translated. If the english document is edited, this field
     *                       is then reset to empty.
     */
    private TranslationStatus getDocumentTranslationStatus(JcrDocument englishDoc, String locale) throws RepositoryException {
        if (locale.equals("en")) return TranslationStatus.TRANSLATED;
        if (!englishDoc.getTranslationLocaleNames().contains(locale)) {
            // If the clone has not been created, then the document can not have been sent for translation
            return TranslationStatus.NOT_SENT_FOR_TRANSLATION;
        } else {
            Node translatedClone = englishDoc.getTranslation(locale);
            Node unpublishedTranslatedClone = new JcrDocument(translatedClone).getVariantNode(JcrDocument.VARIANT_UNPUBLISHED);
            if (unpublishedTranslatedClone == null || !unpublishedTranslatedClone.hasProperty(JcrDocument.VS_TRANSLATION_FLAG)
                    || unpublishedTranslatedClone.getProperty(JcrDocument.VS_TRANSLATION_FLAG).getString().isEmpty()) {
                // If the translationFlag does not exist on the document, then it has not been sent
                return TranslationStatus.NOT_SENT_FOR_TRANSLATION;
            } else {
                // The translationFlag is only set once the document has been sent for translation
                // Initially to true to indicate that the document has been sent, and then to false once the Translation
                // Completed button has been pressed
                return unpublishedTranslatedClone.getProperty(JcrDocument.VS_TRANSLATION_FLAG).getBoolean() ? TranslationStatus.SEND_FOR_TRANSLATION : TranslationStatus.TRANSLATED;
            }
        }
    }


}
