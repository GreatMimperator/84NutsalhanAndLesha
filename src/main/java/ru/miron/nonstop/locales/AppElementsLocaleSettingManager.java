package ru.miron.nonstop.locales;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class AppElementsLocaleSettingManager {
    public static void setLabelLanguage(Label label, String labelName) {
        var text = AppLocaleManager.getTextByLabel(labelName);
        label.setText(text);
    }

    public static void setTextFieldPromptTextLanguage(TextField field, String promptTextName) {
        var text = AppLocaleManager.getTextByLabel(promptTextName);
        field.setPromptText(text);
    }

    public static void setButtonLabelLanguage(Button button, String labelName) {
        var text = AppLocaleManager.getTextByLabel(labelName);
        button.setText(text);
    }
}
