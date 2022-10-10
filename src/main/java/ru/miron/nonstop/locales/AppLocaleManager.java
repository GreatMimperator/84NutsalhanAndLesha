package ru.miron.nonstop.locales;

import ru.miron.nonstop.locales.bundles.LocaleBundleLoader;
import ru.miron.nonstop.locales.entities.LocaleManager;
import ru.miron.nonstop.locales.entities.LocaleWithBundle;
import ru.miron.nonstop.logic.commands.ErrorDescription;

import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class AppLocaleManager {
    private final static String localesBundlePath;
    private static LocaleManager appLocaleManager;
    private final static Locale defaultLocale;

    private static List<LanguageUpdatable> languageUpdatableListenersList;

    static {
        var localesPath = AppLocaleManager.class.getClassLoader().getResource("ru/miron/nonstop/locales").getPath();
        localesBundlePath = localesPath + "/messages";
        defaultLocale = new Locale("ru-RU");
        try {
            appLocaleManager = new LocaleManager(getClonedDefaultLocale(), getListOfAvailableLocalesWithBundles());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        languageUpdatableListenersList = new LinkedList<>();
    }

    /**
     * @return new list of available locales
     * */
    private static List<LocaleWithBundle> getListOfAvailableLocalesWithBundles() throws FileNotFoundException {
        var localesList = new LinkedList<Locale>();
        localesList.add(new Locale("ru-RU")); // Русский
        localesList.add(new Locale("sq-AL")); // Албанский
        localesList.add(new Locale("sk-SK")); // Словацкий
        localesList.add(new Locale("en-CA")); // Английский (Канада)
        return transformLocalesListToLocalesWithBundlesList(localesList);
    }

    private static List<LocaleWithBundle> transformLocalesListToLocalesWithBundlesList(LinkedList<Locale> localesList) throws FileNotFoundException {
        var localesWithBundlesList = new ArrayList<LocaleWithBundle>(localesList.size());
        for (var locale : localesList) {
            ListResourceBundle bundle = null;
            try {
                bundle = LocaleBundleLoader.load(localesBundlePath, locale);
            } catch (Exception e) {
                e.printStackTrace();
            }
            var localeWithBundle = new LocaleWithBundle(locale, bundle);
            localesWithBundlesList.add(localeWithBundle);
        }
        return localesWithBundlesList;
    }

    public static Locale getClonedDefaultLocale() {
        return (Locale) defaultLocale.clone();
    }

    public static Locale getCurrentLocale() {
        return appLocaleManager.getCurrentLocale();
    }

    public static void setCurrentLocaleWithListenersUpdate(Locale locale) {
        appLocaleManager.setCurrentLocaleWithBundleWithLocale(locale);
        updateLanguageUpdatableListeners();
    }

    public static List<Locale> getAvailableLocales() {
        return appLocaleManager.getAvailableLocalesWithBundles()
                .stream()
                .map(localeWithBundle -> localeWithBundle.getLocale())
                .collect(Collectors.toList());
    }

    public static String getTextByLabel(String label) {
        return appLocaleManager.getTextByLabel(label);
    }

    public static void addLanguageUpdatableListener(LanguageUpdatable languageUpdatable) {
        languageUpdatableListenersList.add(languageUpdatable);
    }

    public static void removeLanguageUpdatableListener(LanguageUpdatable languageUpdatable) {
        languageUpdatableListenersList.remove(languageUpdatable);
    }

    private static void updateLanguageUpdatableListeners() {
        languageUpdatableListenersList.forEach(languageUpdatable -> languageUpdatable.updateLanguage());
    }

    public static String getQueryErrorTypeLabelName(ErrorDescription.ErrorType errorType) {
        switch (errorType) {
            case KEY_NOT_FOUND -> {
                return "keyNotFoundErrorName";
            }
            case KEY_VALUE_HAS_ILLEGAL_TYPE -> {
                return "keyValueHasIllegalTypeErrorName";
            }
            case KEY_VALUE_IS_OUT_OF_BOUNDS -> {
                return "keyValueIsOutOfBoundsErrorName";
            }
            case SERVER_INTERNAL_ERROR -> {
                return "serverInternalErrorName";
            }
        }
        throw new IllegalStateException("Introduced new web error type");
    }
}
