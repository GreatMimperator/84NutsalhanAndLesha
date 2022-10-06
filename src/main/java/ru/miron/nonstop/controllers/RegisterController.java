package ru.miron.nonstop.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import ru.miron.nonstop.EmoCore;
import ru.miron.nonstop.locales.AppLocaleChoiceBoxSetter;
import ru.miron.nonstop.locales.ElementsLocaleSetter;
import ru.miron.nonstop.locales.AppLocaleManager;
import ru.miron.nonstop.locales.LanguageUpdatable;
import ru.miron.nonstop.logic.commands.*;

import java.io.IOException;
import java.net.ConnectException;
import java.util.Locale;

import static ru.miron.nonstop.logic.commands.CommandAnswerWithoutArgs.EnterState.ENTERED;

public class RegisterController implements LanguageUpdatable {
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
    private Label confirmPasswordLabel;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private Label confirmPasswordErrorLabel;

    @FXML
    private Button registerButton;

    @FXML
    private Button enterButton;

    private Validate.LoginErrorLabelVariant loginErrorLabelVariant = null;

    private Validate.PasswordErrorLabelVariant passwordErrorLabelVariant = null;

    private Validate.ConfirmPasswordErrorLabelVariant confirmPasswordErrorLabelVariant = null;

    public void enterBtnActionHandler(ActionEvent actionEvent) {
        System.out.println("Enter btn of reg controller clicked");
        EmoCore.setHelloScene();
    }

    public void registerBtnActionHandler(ActionEvent actionEvent){
        System.out.println("Register btn of reg controller clicked");
        if (checkFieldsAndShowIfBad()) {
            System.out.println("Register fields are bad. So, wont send any data");
            try {
                EmoCore.createInfoAutoClosableWindow("badEnteredInfoMsg", "Bad entered info");
            } catch (IOException e) {}
        } else {
            System.out.println("Register fields are good. So, will send to server");
            var enterEntry = getEnterEntryFromFields();
            var registerCommand = new Command(CommandName.REGISTER, enterEntry, null);
            CommandAnswer registerCommandAnswer;
            try {
                registerCommandAnswer = EmoCore.tryToGetCommandAnswerWithErrorWindowsGenOnFailOrErrorAnswer(registerCommand);
            } catch (IllegalStateException e) {
                return;
            }
            var registerEnterState = registerCommandAnswer.getCommandAnswerWithoutArgs().getEnterState();
            switch (registerEnterState) {
                case ENTERED -> {
                    EmoCore.enterEntry = enterEntry;
                    EmoCore.setHelloScene();
                    try {
                        EmoCore.createInfoAutoClosableWindow("registerRegisteredMsg", "Registered");
                    } catch (IOException e) {}
                }
                case WRONG_LOGIN -> {
                    try {
                        EmoCore.createInfoAutoClosableWindow("registerLoginDuplicateMsg", "Login duplicate");
                    } catch (IOException e) {}
                }
                default -> {
                    throw new IllegalStateException("Api changed");
                }
            }
        }
    }

    public EnterEntry getEnterEntryFromFields() {
        var login = Validate.getLogin(loginField);
        var password = Validate.getPassword(passwordField);
        return new EnterEntry(login, password);
    }

    private boolean checkFieldsAndShowIfBad() {
        loginErrorLabelVariant =
                Validate.validateLoginAndShowTextLabelWithErrorIfBad(loginField, loginErrorLabel);
        passwordErrorLabelVariant =
                Validate.validatePasswordAndShowTextLabelWithErrorIfBad(passwordField, passwordErrorLabel);
        confirmPasswordErrorLabelVariant =
                Validate.validateConfirmPasswordAndShowTextLabelWithErrorIfBad(
                        confirmPasswordField,
                        passwordField,
                        confirmPasswordErrorLabel);
        return loginErrorLabelVariant != null ||
                passwordErrorLabelVariant != null ||
                confirmPasswordErrorLabelVariant != null;
    }

    private void hideErrorLabels() {
        loginErrorLabel.setVisible(false);
        passwordErrorLabel.setVisible(false);
        confirmPasswordErrorLabel.setVisible(false);
    }

    @FXML
    public void initialize() {
        System.out.println("inited register controller");
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
        ElementsLocaleSetter.setLabelTextInCurrentLanguage(confirmPasswordLabel, "confirmPasswordLabel");
        ElementsLocaleSetter.setTextFieldPromptTextInCurrentLanguage(confirmPasswordField, "confirmPasswordFieldPromptText");
        ElementsLocaleSetter.setButtonLabelInCurrentLanguage(registerButton, "registerButtonLabel");
        ElementsLocaleSetter.setButtonLabelInCurrentLanguage(enterButton, "enterButtonLabel");
        Validate.setLoginErrorLabelInCurrentLanguageIfHasVariant(loginErrorLabel, loginErrorLabelVariant);
        Validate.setPasswordErrorLabelInCurrentLanguageIfHasVariant(passwordErrorLabel, passwordErrorLabelVariant);
        Validate.setConfirmPasswordErrorLabelInCurrentLanguageIfHasVariant(confirmPasswordErrorLabel, confirmPasswordErrorLabelVariant);
        AppLocaleChoiceBoxSetter.updateLanguage(languageSelector);
    }
}
