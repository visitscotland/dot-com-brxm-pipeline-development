package com.visitscotland.brxm.report.translation;

import java.util.List;

public class DocumentTranslationReportModel {
    public DocumentTranslationReportModel(String handleId, String displayName, String translationStatus,
                                          TranslationPriority translationPriority, List<String> translatedLocales,
                                          List<String> sentForTranslationLocales, String type, String lastModified, String lastModifiedBy,
                                          PublishStatus publishStatus) {
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
    }

    private final String displayName;
    private final List<String> translatedLocales;
    private final List<String> sentForTranslationLocales;
    private final String translationStatus;
    private final String handleId;
    private final String translationPriority;
    private final String type;
    private final String lastModified;
    private final String lastModifiedBy;
    private final PublishStatus publishStatus;

    public String getDisplayName() {
        return displayName;
    }

    public List<String> getTranslatedLocales() {
        return translatedLocales;
    }

    public String getTranslationStatus() {
        return translationStatus;
    }

    public List<String> getSentForTranslationLocales() {
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
}
