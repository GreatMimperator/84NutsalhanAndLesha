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
    private ChoiceBox<String> languageSelector;
    @FXML
    private Button okButton;

    private LabelText infoLabelText;
    private LabelText okButtonLabelText;
    private Runnable toDoOnOkClick;

    public InfoTextBoxController() {
        okButtonLabelText = new LabelText("infoTextBoxDefaultOkButtonText", LabelText.TextType.LABEL_NAME);
    }

    @FXML
    public void initialize() {
        System.out.println("inited error controller");
        AppLocaleChoiceBoxSetter.setContentAndOnChangeLanguageChange(languageSelector);
        setLabels();
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
            ElementsLocaleSetter.setLabelTextInCurrentLanguage(infoLabel, infoLabelText);
        }
        ElementsLocaleSetter.setButtonLabelInCurrentLanguage(okButton, okButtonLabelText);
        AppLocaleChoiceBoxSetter.updateLanguage(languageSelector);
    }

    public void setInfoLabelText(LabelText labelText) {
        this.infoLabelText = labelText;
        setLabels();
    }

    public void setOnButtonLabelName(LabelText okButtonLabelText) {
        this.okButtonLabelText = okButtonLabelText;
        setLabels();
    }
}
