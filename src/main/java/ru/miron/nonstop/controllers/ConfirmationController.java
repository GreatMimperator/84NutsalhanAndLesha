package ru.miron.nonstop.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ru.miron.nonstop.EmoCore;
import ru.miron.nonstop.locales.AppLocaleChoiceBoxSetter;
import ru.miron.nonstop.locales.ElementsLocaleSetter;
import ru.miron.nonstop.locales.LanguageUpdatable;
import ru.miron.nonstop.locales.entities.LabelText;

import static ru.miron.nonstop.locales.entities.LabelText.TextType.LABEL_NAME;

public class ConfirmationController implements LanguageUpdatable {
    @FXML
    private VBox confirmationPane;
    @FXML
    private ChoiceBox<String> languageSelector;
    @FXML
    private Label msgLabel;
    @FXML
    private Button backButton;
    @FXML
    private Button confirmButton;

    private LabelText msgLabelText;
    private LabelText backButtonLabelText;
    private LabelText confirmButtonLabelText;
    private Runnable confirmAction;
    private Runnable backAction;

    @FXML
    public void initialize() {
        System.out.println("inited confirmation controller");
        AppLocaleChoiceBoxSetter.setContentAndOnChangeLanguageChange(languageSelector);
        setLabels();
    }

    public ConfirmationController() {
        backButtonLabelText = new LabelText("backButtonLabel", LABEL_NAME);
        backAction = () -> {};
    }

    public void setLabels() {
        updateLanguage();
    }


    public void confirm(ActionEvent actionEvent) {
        confirmAction.run();
        getStage().close();
    }

    public void back(ActionEvent actionEvent) {
        backAction.run();
        getStage().close();
    }

    public Stage getStage() {
        return (Stage) confirmationPane.getScene().getWindow();
    }


    public void setMsgLabelName(String msgLabelName) {
        setMsgLabelText(new LabelText(msgLabelName, LABEL_NAME));
    }

    public void setMsgLabelText(LabelText msgLabelText) {
        this.msgLabelText = msgLabelText;
    }

    public void setConfirmButtonName(String buttonName) {
        setConfirmButtonName(new LabelText(buttonName, LABEL_NAME));
    }

    public void setConfirmAction(Runnable confirmAction) {
        this.confirmAction = confirmAction;
    }

    public void setBackAction(Runnable backAction) {
        this.backAction = backAction;
    }

    public void setConfirmButtonName(LabelText confirmButtonLabelText) {
        this.confirmButtonLabelText = confirmButtonLabelText;
    }

    public void setBackButtonName(String buttonName) {
        setBackButtonName(new LabelText(buttonName, LABEL_NAME));
    }

    public void setBackButtonName(LabelText backButtonLabelText) {
        this.backButtonLabelText = backButtonLabelText;
    }

    @Override
    public void updateLanguage() {
        if (msgLabelText != null) {
            ElementsLocaleSetter.setText(msgLabel, msgLabelText);
        }
        ElementsLocaleSetter.setText(backButton, backButtonLabelText);
        if (confirmButtonLabelText != null) {
            ElementsLocaleSetter.setText(confirmButton, confirmButtonLabelText);
        }
        AppLocaleChoiceBoxSetter.updateLanguage(languageSelector);
    }
}
