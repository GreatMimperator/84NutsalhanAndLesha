package ru.miron.nonstop.locales;

public interface LanguageUpdatable {
    /**
     * Uses the state, which could be changed before call, to update language
     */
    void updateLanguage();
}
