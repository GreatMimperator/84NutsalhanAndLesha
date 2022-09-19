package ru.miron.nonstop.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import ru.miron.nonstop.EmoCore;
import ru.miron.nonstop.locales.AppChoiceBoxLocaleController;
import ru.miron.nonstop.locales.AppElementsLocaleSettingManager;
import ru.miron.nonstop.locales.AppLocaleManager;
import ru.miron.nonstop.locales.LanguageUpdatable;

import java.io.IOException;

public class EnterController implements LanguageUpdatable {
    @FXML
    private ChoiceBox<String> languageSelector;

    @FXML
    private Label enterLoginLabel;

    @FXML
    private TextField loginField;

    @FXML
    private Label loginErrorLabel;

    @FXML
    private Label enterPasswordLabel;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label passwordErrorLabel;

    @FXML
    private Button registerButton;

    @FXML
    private Button enterButton;

    public void registerBtnActionHandler(ActionEvent actionEvent) {
        System.out.println("Register btn of enter controller clicked");
        EmoCore.setRegisterScene();
    }

    public void enterBtnActionHandler(ActionEvent actionEvent) {
        System.out.println("Enter btn of enter controller clicked");
    }

    public void initialize() {
        System.out.println("hfvjbkj");
        AppChoiceBoxLocaleController.setChoiceBoxContent(languageSelector);
        AppChoiceBoxLocaleController.setOnChangeLanguageChange(this, languageSelector);
        setLabels();
    }

    public void setLabels() {
        updateLanguage();
    }

    @Override
    public void updateLanguage() {
        System.out.println("Can be updated to " + AppLocaleManager.getAppLocaleManager().getCurrentLocale().getDisplayName());
        AppElementsLocaleSettingManager.setLabelLanguage(enterLoginLabel, "enterLoginLabel");
        AppElementsLocaleSettingManager.setTextFieldPromptTextLanguage(loginField, "enterLoginFieldPromptText");
        AppElementsLocaleSettingManager.setLabelLanguage(enterPasswordLabel, "enterPasswordLabel");
        AppElementsLocaleSettingManager.setTextFieldPromptTextLanguage(passwordField, "enterPasswordFieldPromptText");
        AppElementsLocaleSettingManager.setButtonLabelLanguage(registerButton, "registerButtonLabel");
        AppElementsLocaleSettingManager.setButtonLabelLanguage(enterButton, "enterButtonLabel");
        // todo: не забыть о enum системе с ошибками
    }
}
