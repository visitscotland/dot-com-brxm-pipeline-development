package com.visitscotland.brxm.report.translation;

import java.util.Set;

public class DocumentTranslationReportModel {
    public DocumentTranslationReportModel(String handleId, String displayName, String translationStatus,
                                          TranslationPriority translationPriority, Set<String> translatedLocales,
                                          Set<String> sentForTranslationLocales, String type, String lastModified, String lastModifiedBy,
                                          PublishStatus publishStatus, String translationDeadline) {
        this.handleId = handleId;
        this.displayName = displayName;
        this.translatedLocales = translatedLocales;
        this.translationPriority = translationPriority.toString();
        this.translationStatus = translationStatus;
        this.sentForTranslationLocales = sentForTranslationLocales;
        this.type = type;
        this.lastModified = lastModified;
        this.lastModifiedBy = lastModifiedBy;
        this.publishStatus = publishStatus;
        this.translationDeadline = translationDeadline;
    }


    private final String displayName;
    private final Set<String> translatedLocales;
    private final Set<String> sentForTranslationLocales;
    private final String translationStatus;
    private final String handleId;
    private final String translationPriority;
    private final String type;
    private final String lastModified;
    private final String lastModifiedBy;
    private final PublishStatus publishStatus;
    private final String translationDeadline;

    public String getDisplayName() {
        return displayName;
    }

    public Set<String> getTranslatedLocales() {
        return translatedLocales;
    }

    public String getTranslationStatus() {
        return translationStatus;
    }

    public Set<String> getSentForTranslationLocales() {
        return sentForTranslationLocales;
    }

    public String getHandleId() {
        return handleId;
    }

    public String getTranslationPriority() {
        return translationPriority;
    }

    public String getType() {
        return type;
    }

    public String getLastModified() {
        return lastModified;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public PublishStatus getPublishStatus() {
        return publishStatus;
    }

    public String getTranslationDeadline() {
        return translationDeadline;
    }

}
