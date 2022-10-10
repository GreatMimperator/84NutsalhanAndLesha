package ru.miron.nonstop.locales;

import javafx.scene.control.*;
import javafx.util.StringConverter;
import ru.miron.nonstop.givenClasses.DragonType;
import ru.miron.nonstop.locales.entities.LabelText;

import static ru.miron.nonstop.locales.entities.LabelText.TextType.*;

public class ElementsLocaleSetter {
    public static void setLocalizedText(Labeled labeled, String labelName) {
        setText(labeled, new LabelText(labelName, LABEL_NAME));
    }

    public static void setText(Labeled labeled, LabelText labelText) {
        var text = labelText.getPlainText();
        labeled.setText(text);
    }

    public static void setLocalizedPromptText(TextInputControl input, String labelName) {
        setPromptText(input, new LabelText(labelName, LABEL_NAME));
    }

    public static void setPromptText(TextInputControl input, LabelText labelText) {
        var text = labelText.getPlainText();
        input.setPromptText(text);
    }

    public static void setHeaderLocalizedText(TableColumn tableColumn, String labelName) {
        setHeaderText(tableColumn, new LabelText(labelName, LABEL_NAME));
    }

    public static void setHeaderText(TableColumn tableColumn, LabelText labelText) {
        var text = labelText.getPlainText();
        tableColumn.setText(text);
    }

    public static void setLocalizedDragonTypeChoiceBox(ChoiceBox<DragonType> dragonTypeChoiceBox) {
        dragonTypeChoiceBox.getItems().forEach(System.out::println);
        dragonTypeChoiceBox.setConverter(new StringConverter<DragonType>() {
            @Override
            public String toString(DragonType dragonType) {
                return getLocalizedDragonType(dragonType);
            }

            @Override
            public DragonType fromString(String s) {
                throw new UnsupportedOperationException();
            }
        });
    }

    public static String getLocalizedDragonType(DragonType dragonType) {
        if (dragonType == null) {
            return "null";
        }
        var typeLabelName = switch (dragonType) {
            case WATER -> "waterTypeName";
            case UNDERGROUND -> "undergroundTypeName";
            case AIR -> "airTypeName";
            case FIRE -> "fireTypeName";
        };
        return AppLocaleManager.getTextByLabel(typeLabelName);
    }
}
