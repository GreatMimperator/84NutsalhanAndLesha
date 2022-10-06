package ru.miron.nonstop.locales;

import javafx.scene.control.*;
import ru.miron.nonstop.locales.entities.LabelText;

import static ru.miron.nonstop.locales.entities.LabelText.TextType.*;

public class ElementsLocaleSetter {
    public static void setLabelTextInCurrentLanguage(Label label, String labelName) {
        setLabelTextInCurrentLanguage(label, new LabelText(labelName, LABEL_NAME));
    }

    public static void setLabelTextInCurrentLanguage(Label label, LabelText labelText) {
        var text = labelText.getPlainText();
        label.setText(text);
    }

    public static void setTextFieldPromptTextInCurrentLanguage(TextField field, String promptTextName) {
        setTextFieldPromptTextInCurrentLanguage(field, new LabelText(promptTextName, LABEL_NAME));
    }

    public static void setTextFieldPromptTextInCurrentLanguage(TextField field, LabelText promptLabelText) {
        var text = promptLabelText.getPlainText();
        field.setPromptText(text);
    }

    public static void setButtonLabelInCurrentLanguage(Button button, String labelName) {
        setButtonLabelInCurrentLanguage(button, new LabelText(labelName, LABEL_NAME));
    }

    public static void setButtonLabelInCurrentLanguage(Button button, LabelText labelText) {
        var text = labelText.getPlainText();
        button.setText(text);
    }

    public static void setHeaderTextOfColumnInConcreteLanguage(TableColumn tableColumn, String textName) {
        setHeaderTextOfColumnInConcreteLanguage(tableColumn, new LabelText(textName, LABEL_NAME));
    }

    public static void setHeaderTextOfColumnInConcreteLanguage(TableColumn tableColumn, LabelText labelText) {
        var text = labelText.getPlainText();
        tableColumn.setText(text);
    }

    public static void setTextAreaPromptTextInCurrentLanguage(TextArea textArea, String promptLabelName) {
        setTextAreaPromptTextInCurrentLanguage(textArea, new LabelText(promptLabelName, LABEL_NAME));
    }

    public static void setTextAreaPromptTextInCurrentLanguage(TextArea textArea, LabelText promptLabelText) {
        var text = promptLabelText.getPlainText();
        textArea.setPromptText(text);
    }
}
