package com.visitscotland.brxm.report.translation;

public enum TranslationPriority {

    HIGH("High", 3), NORMAL("Normal", 2), LOW("Low", 1);

    public final String name;
    public final Integer sortOrder;

    TranslationPriority(String name, Integer sortOrder) {
        this.sortOrder = sortOrder;
        this.name = name;
    }

}
