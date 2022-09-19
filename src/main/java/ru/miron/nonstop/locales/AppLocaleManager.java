package ru.miron.nonstop.locales;

import javafx.beans.property.Property;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ChoiceBox;
import ru.miron.nonstop.locales.bundles.BundleLoader;
import ru.miron.nonstop.locales.entities.LocaleWithBundle;

import java.io.FileNotFoundException;
import java.util.*;

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
            var bundle = BundleLoader.load(localesBundlePath, locale);
            var localeWithBundle = new LocaleWithBundle(locale, bundle);
            localesWithBundlesList.add(localeWithBundle);
        }
        return localesWithBundlesList;
    }

    public static Locale getClonedDefaultLocale() {
        return (Locale) defaultLocale.clone();
    }

    public static LocaleManager getAppLocaleManager() {
        return appLocaleManager;
    }

    public static String getTextByLabel(String label) {
        return appLocaleManager.getTextByLabel(label);
    }

    public static void addLanguageUpdatableListener(LanguageUpdatable languageUpdatable) {
        languageUpdatableListenersList.add(languageUpdatable);
    }
}
