package ru.miron.nonstop.locales.entities;

import ru.miron.nonstop.locales.entities.LocaleWithBundle;

import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;

public class LocaleManager {
    protected LocaleWithBundle currentLocaleWithBundle;
    protected List<LocaleWithBundle> availableLocalesWithBundles;

    /**
     * @throws IllegalArgumentException if available locales list doesn't contain locale
     */
    public LocaleManager(Locale startLocale, List<LocaleWithBundle> availableLocales) throws IllegalArgumentException {
        init(startLocale, availableLocales);
    }

    public String getTextByLabel(String label) throws MissingResourceException {
        return currentLocaleWithBundle.getString(label);
    }

    /**
     * @throws IllegalArgumentException if available locales list doesn't contain locale
     */
    public void init(Locale startLocale, List<LocaleWithBundle> availableLocalesWithBundles) throws IllegalArgumentException {
        if (!LocaleWithBundle.isLocaleInList(startLocale, availableLocalesWithBundles)) {
            throw new IllegalArgumentException();
        }
        this.availableLocalesWithBundles = availableLocalesWithBundles;
        this.currentLocaleWithBundle = LocaleWithBundle.getLocaleWithBundleByLocale(startLocale, availableLocalesWithBundles);
    }

    /**
     * @throws IllegalStateException if current locale is not in available locales list. Use init method instead
     */
    public void setAvailableLocales(List<LocaleWithBundle> availableLocalesWithBundles) throws IllegalStateException {
        if (!isAvailable(currentLocaleWithBundle)) {
            throw new IllegalStateException();
        }
        this.availableLocalesWithBundles = availableLocalesWithBundles;
    }

    /**
     * @return true if locale is one of available locales
     */
    public boolean isAvailable(Locale locale) {
        return LocaleWithBundle.isLocaleInList(locale, availableLocalesWithBundles);
    }

    /**
     * @return true if locale is one of available locales
     */
    public boolean isAvailable(LocaleWithBundle localeWithBundle) {
        return availableLocalesWithBundles.contains(localeWithBundle);
    }

    /**
     * @throws IllegalStateException if locale to set is not in list of available locales
     */
    public void setCurrentLocaleWithBundleWithLocale(Locale locale) throws IllegalStateException {
        if (!isAvailable(locale)) {
            throw new IllegalArgumentException();
        }
        this.currentLocaleWithBundle = LocaleWithBundle.getLocaleWithBundleByLocale(locale, availableLocalesWithBundles);
    }

    public LocaleWithBundle getCurrentLocaleWithBundle() {
        return currentLocaleWithBundle;
    }

    public Locale getCurrentLocale() {
        return currentLocaleWithBundle.getLocale();
    }

    public List<LocaleWithBundle> getAvailableLocalesWithBundles() {
        return availableLocalesWithBundles;
    }

}
