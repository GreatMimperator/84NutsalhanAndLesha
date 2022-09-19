package ru.miron.nonstop.locales;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ChoiceBox;

import java.util.Locale;

public class AppChoiceBoxLocaleController {
    public static void setChoiceBoxContent(ChoiceBox<String> choiceBox) {
        var items = choiceBox.getItems();
        var appLocaleManager = AppLocaleManager.getAppLocaleManager();
        var currentLocale = appLocaleManager.getCurrentLocale();
        items.clear();
        items.add(getChoiceBoxLocaleName(currentLocale));
        choiceBox.setValue(getChoiceBoxLocaleName(currentLocale));
        for (var nextLocaleWithBundle : appLocaleManager.getAvailableLocalesWithBundles()) {
            var nextLocale = nextLocaleWithBundle.getLocale();
            if (nextLocale.equals(appLocaleManager.getCurrentLocale())) {
                continue;
            }
            items.add(getChoiceBoxLocaleName(nextLocale));
        }
    }

    public static String getChoiceBoxLocaleName(Locale locale) {
        return locale.getDisplayName();
    }

    /**
     * @throws IllegalStateException if didn't find in available locale list
     */
    public static Locale getLocaleByChoiceBoxLabel(String choiceBoxLabel) {
        var appLocaleManager = AppLocaleManager.getAppLocaleManager();
        for (var availableLocaleWithBundle : appLocaleManager.getAvailableLocalesWithBundles()) {
            var availableLocale = availableLocaleWithBundle.getLocale();
            if (getChoiceBoxLocaleName(availableLocale).equals(choiceBoxLabel)) {
                return availableLocale;
            }
        }
        throw new IllegalStateException();
    }

    public static void setOnChangeLanguageChange(LanguageUpdatable languageUpdatable, ChoiceBox<String> languageSelector) {
        languageSelector.getSelectionModel().selectedItemProperty().addListener(
            new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                    var localeToSet = getLocaleByChoiceBoxLabel(newValue);
                    AppLocaleManager.getAppLocaleManager().setCurrentLocaleWithBundleWithLocale(localeToSet);
                    languageUpdatable.updateLanguage();
                }
            }
        );
    }
}
