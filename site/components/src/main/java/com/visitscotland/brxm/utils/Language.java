package com.visitscotland.brxm.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public enum Language {
    ENGLISH(Locale.UK),
    SPANISH(Locale.forLanguageTag("es-es")),
    ITALIAN(Locale.forLanguageTag("it-it")),
    GERMAN(Locale.forLanguageTag("de-de")),
    DUTCH(Locale.forLanguageTag("nl-nl")),
    FRENCH(Locale.forLanguageTag("fr-fr"));

    private final Locale locale;

    Language(Locale locale) {
        this.locale = locale;
    }

    public static Language getLanguageForLocale(Locale locale) {
        if (locale != null) {
            for (Language language : values()) {
                if (locale.getLanguage().equals(language.locale.getLanguage())) {
                    return language;
                }
            }
        }
        return ENGLISH;
    }

    /**
     * Return a list of the allowed locales
     * @return
     */
    public static List<Locale> getLocales(){
        return Arrays.stream(values()).map(Language::getLocale).collect(Collectors.toList());
    }

    public Locale getLocale(){
        return locale;
    }

    public String getCMSPathVariable() {
        if (this == ENGLISH) {
            return "";
        } else {
            return locale.getLanguage();
        }
    }

    public String getDMSPathVariable() {
        if (this == ENGLISH) {
            return null;
        } else {
            return "/" + locale.toLanguageTag();
        }
    }
}
