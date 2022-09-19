package ru.miron.nonstop.locales.entities;

import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class LocaleWithBundle {
    private Locale locale;
    private ResourceBundle resourceBundle;

    public LocaleWithBundle(Locale locale, ResourceBundle resourceBundle) {
        this.locale = locale;
        this.resourceBundle = resourceBundle;
    }

    public String getString(String key) throws MissingResourceException {
        return resourceBundle.getString(key);
    }

    public Locale getLocale() {
        return locale;
    }

    public ResourceBundle getResourceBundle() {
        return resourceBundle;
    }

    public static boolean isLocaleInList(Locale locale, List<LocaleWithBundle> localesWithBundles) {
        for (var localeWithBundle : localesWithBundles) {
            if (locale.equals(localeWithBundle.getLocale())) {
                return true;
            }
        }
        return false;
    }

    /**
     * @throws IllegalStateException if hasn't locale in list
     */
    public static LocaleWithBundle getLocaleWithBundleByLocale(Locale locale, List<LocaleWithBundle> localesWithBundles) throws IllegalStateException {
        for (var localeWithBundle : localesWithBundles) {
            if (locale.equals(localeWithBundle.getLocale())) {
                return localeWithBundle;
            }
        }
        throw new IllegalStateException();
    }
}
