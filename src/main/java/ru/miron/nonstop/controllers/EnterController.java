package ru.miron.nonstop.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import ru.miron.nonstop.EmoCore;
import ru.miron.nonstop.locales.AppLocaleChoiceBoxSetter;
import ru.miron.nonstop.locales.ElementsLocaleSetter;
import ru.miron.nonstop.locales.AppLocaleManager;
import ru.miron.nonstop.locales.LanguageUpdatable;

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

    private Validate.LoginErrorLabelVariant loginErrorLabelVariant = null;

    private Validate.PasswordErrorLabelVariant passwordErrorLabelVariant = null;

    public void registerBtnActionHandler(ActionEvent actionEvent) {
        System.out.println("Register btn of enter controller clicked");
        EmoCore.setRegisterScene();
    }

    public void enterBtnActionHandler(ActionEvent actionEvent) {
        System.out.println("Enter btn of reg controller clicked");
        if (checkFieldsAndShowIfBad()) {
            System.out.println("Enter fields are bad. So, wont send any data");
        } else {
            System.out.println("Enter fields are good. So, will send to server");
        }
    }

    private boolean checkFieldsAndShowIfBad() {
        loginErrorLabelVariant =
                Validate.validateLoginAndShowTextLabelWithErrorIfBad(loginField, loginErrorLabel);
        passwordErrorLabelVariant =
                Validate.validatePasswordAndShowTextLabelWithErrorIfBad(passwordField, passwordErrorLabel);
        return loginErrorLabelVariant != null ||
                passwordErrorLabelVariant != null;
    }

    private void hideErrorLabels() {
        loginErrorLabel.setVisible(false);
        passwordErrorLabel.setVisible(false);
    }

    @FXML
    public void initialize() {
        System.out.println("inited enter controller");
        AppLocaleChoiceBoxSetter.setContentAndOnChangeLanguageChange(languageSelector);
        setLabels();
    }

    public void setLabels() {
        updateLanguage();
    }

    @Override
    public void updateLanguage() {
        System.out.println("Can be updated to " + AppLocaleManager.getCurrentLocale().getDisplayName());
        ElementsLocaleSetter.setLabelTextInCurrentLanguage(enterLoginLabel, "enterLoginLabel");
        ElementsLocaleSetter.setTextFieldPromptTextInCurrentLanguage(loginField, "enterLoginFieldPromptText");
        ElementsLocaleSetter.setLabelTextInCurrentLanguage(enterPasswordLabel, "enterPasswordLabel");
        ElementsLocaleSetter.setTextFieldPromptTextInCurrentLanguage(passwordField, "enterPasswordFieldPromptText");
        ElementsLocaleSetter.setButtonLabelInCurrentLanguage(registerButton, "registerButtonLabel");
        ElementsLocaleSetter.setButtonLabelInCurrentLanguage(enterButton, "enterButtonLabel");
        Validate.setLoginErrorLabelInCurrentLanguageIfHasVariant(loginErrorLabel, loginErrorLabelVariant);
        Validate.setPasswordErrorLabelInCurrentLanguageIfHasVariant(passwordErrorLabel, passwordErrorLabelVariant);
    }
}
