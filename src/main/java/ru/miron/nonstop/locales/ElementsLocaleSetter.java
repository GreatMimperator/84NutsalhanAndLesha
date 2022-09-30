package ru.miron.nonstop.locales;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ElementsLocaleSetter {
    public static void setLabelTextInCurrentLanguage(Label label, String labelName) {
        var text = AppLocaleManager.getTextByLabel(labelName);
        label.setText(text);
    }

    public static void setTextFieldPromptTextInCurrentLanguage(TextField field, String promptTextName) {
        var text = AppLocaleManager.getTextByLabel(promptTextName);
        field.setPromptText(text);
    }

    public static void setButtonLabelInCurrentLanguage(Button button, String labelName) {
        var text = AppLocaleManager.getTextByLabel(labelName);
        button.setText(text);
    }
}
