package ru.miron.nonstop.locales;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ChoiceBox;

import java.util.Locale;

public class AppLocaleChoiceBoxSetter {
    public static void setContentAndOnChangeLanguageChange(ChoiceBox<String> choiceBox) {
        setContent(choiceBox);
        setOnChangeLanguageChange(choiceBox);
    }

    protected static void setContent(ChoiceBox<String> choiceBox) {
        var items = choiceBox.getItems();
        var currentLocale = AppLocaleManager.getCurrentLocale();
        items.clear();
        items.add(getChoiceBoxLocaleName(currentLocale));
        choiceBox.setValue(getChoiceBoxLocaleName(currentLocale));
        for (var nextLocale : AppLocaleManager.getAvailableLocales()) {
            if (nextLocale.equals(currentLocale)) {
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
        for (var availableLocale : AppLocaleManager.getAvailableLocales()) {
            if (getChoiceBoxLocaleName(availableLocale).equals(choiceBoxLabel)) {
                return availableLocale;
            }
        }
        throw new IllegalStateException();
    }

    protected static void setOnChangeLanguageChange(ChoiceBox<String> languageSelector) {
        languageSelector.getSelectionModel().selectedItemProperty().addListener(
            new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                    var localeToSet = getLocaleByChoiceBoxLabel(newValue);
                    AppLocaleManager.setCurrentLocaleWithListenersUpdate(localeToSet);
                }
            }
        );
    }
}
