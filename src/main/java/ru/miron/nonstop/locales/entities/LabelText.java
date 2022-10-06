package ru.miron.nonstop.locales.entities;

import ru.miron.nonstop.locales.AppLocaleManager;

public class LabelText {
    String textOrLabel;
    TextType textType;

    public enum TextType {
        LABEL_NAME, PLAIN_TEXT
    }

    /**
     * @param textOrLabel not null plain text or label (translated through static func of AppLocaleManager)
     * @param textType not null
     * @throws IllegalArgumentException if  textOrLabel or textType is null
     */
    public LabelText(String textOrLabel, TextType textType) {
        if (textOrLabel == null || textType == null) {
            throw new IllegalArgumentException();
        }
        this.textOrLabel = textOrLabel;
        this.textType = textType;
    }

    public String getPlainText() {
        switch (textType) {
            case LABEL_NAME -> {
                return AppLocaleManager.getTextByLabel(textOrLabel);
            }
            case PLAIN_TEXT -> {
                return textOrLabel;
            }
        }
        throw new IllegalStateException("Introduced new text types");
    }
}
