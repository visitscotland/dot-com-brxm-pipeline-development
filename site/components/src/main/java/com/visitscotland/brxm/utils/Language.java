package com.visitscotland.brxm.utils;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public enum Language {
    ENGLISH(Locale.UK, "/"),
    SPANISH(Locale.forLanguageTag("es-es"), "/es"),
    ITALIAN(Locale.forLanguageTag("it-it"), "/it"),
    GERMAN(Locale.forLanguageTag("de-de"), "/de"),
    DUTCH(Locale.forLanguageTag("nl-nl"), "/nl"),
    FRENCH(Locale.forLanguageTag("fr-fr"), "/fr");

    private final Locale locale;
    private final String cmsMount;

    Language(Locale locale, String cmsMount) {
        this.locale = locale;
        this.cmsMount = cmsMount;
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
     * @return List of locales
     */
    public static List<Locale> getLocales(){
        return Arrays.stream(values()).map(Language::getLocale).collect(Collectors.toList());
    }

    public Locale getLocale(){
        return locale;
    }

    public String getPathVariable() {
        if (this == ENGLISH) {
            return "";
        } else {
            return "/" + locale.toLanguageTag().toLowerCase();
        }
    }

    public String getCmsMount(){
        return cmsMount;
    }
}
