package ru.miron.nonstop.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import ru.miron.nonstop.locales.AppLocaleChoiceBoxSetter;
import ru.miron.nonstop.locales.ElementsLocaleSetter;
import ru.miron.nonstop.locales.LanguageUpdatable;
import ru.miron.nonstop.locales.entities.LabelText;

public class InfoTextBoxController implements LanguageUpdatable {
    @FXML
    private Label infoLabel;
    @FXML
    private Button okButton;

    private LabelText infoLabelText;
    private LabelText okButtonLabelText;
    private Runnable toDoOnOkClick;

    @FXML
    public void initialize() {
        System.out.println("inited info controller");
        setLabels();
    }

    public InfoTextBoxController() {
        okButtonLabelText = new LabelText("infoTextBoxDefaultOkButtonText", LabelText.TextType.LABEL_NAME);
    }

    public void setOnOkClickAction(Runnable toDoOnOkClick) {
        this.toDoOnOkClick = toDoOnOkClick;
    }

    @FXML
    private void onOkClick() {
        if (toDoOnOkClick != null) {
            toDoOnOkClick.run();
        }
    }

    public void setLabels() {
        updateLanguage();
    }

    @Override
    public void updateLanguage() {
        if (infoLabelText != null) {
            ElementsLocaleSetter.setText(infoLabel, infoLabelText);
        }
        ElementsLocaleSetter.setText(okButton, okButtonLabelText);
    }

    public void setInfoLabelText(LabelText labelText) {
        System.out.println(labelText.getPlainText());
        this.infoLabelText = labelText;
        setLabels();
    }

    public void setOnButtonLabelName(LabelText okButtonLabelText) {
        this.okButtonLabelText = okButtonLabelText;
        setLabels();
    }
}
