package ru.miron.nonstop.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import ru.miron.nonstop.EmoCore;
import ru.miron.nonstop.locales.AppLocaleChoiceBoxSetter;
import ru.miron.nonstop.locales.AppLocaleManager;
import ru.miron.nonstop.locales.ElementsLocaleSetter;
import ru.miron.nonstop.locales.LanguageUpdatable;
import ru.miron.nonstop.logic.commands.Command;
import ru.miron.nonstop.logic.commands.CommandAnswer;
import ru.miron.nonstop.logic.commands.CommandName;
import ru.miron.nonstop.logic.commands.EnterEntry;

import java.io.IOException;

import static ru.miron.nonstop.controllers.Validate.ErrorVariant.NONE;

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

    private Validate.ErrorVariant loginErrorLabelVariant;
    private Validate.InputWithErrorLabelProcess loginFieldWithErrorLabelProcess;

    private Validate.ErrorVariant passwordErrorLabelVariant;
    private Validate.InputWithErrorLabelProcess passwordFieldWithErrorLabelProcess;

    private Validate.ErrorVariant confirmPasswordErrorLabelVariant;
    private Validate.InputWithErrorLabelProcess confirmPasswordFieldWithErrorLabelProcess;

    @FXML
    public void initialize() {
        System.out.println("inited register controller");
        initErrorVariantsInfrastructure();
        AppLocaleChoiceBoxSetter.setContentAndOnChangeLanguageChange(languageSelector);
        setLabels();
    }

    private void initErrorVariantsInfrastructure() {
        loginFieldWithErrorLabelProcess = Validate.Login.initProcessor(loginField, loginErrorLabel);
        passwordFieldWithErrorLabelProcess = Validate.Password.initProcessor(passwordField, passwordErrorLabel);
        confirmPasswordFieldWithErrorLabelProcess = Validate.ConfirmPassword.initProcessor(passwordField, confirmPasswordField, confirmPasswordLabel);

        loginErrorLabelVariant = NONE;
        passwordErrorLabelVariant = NONE;
        confirmPasswordErrorLabelVariant = NONE;
    }

    public void changeSceneToEnter(ActionEvent actionEvent) {
        System.out.println("Enter btn of reg controller clicked");
        EmoCore.setHelloScene();
    }

    public void register(ActionEvent actionEvent){
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
        var login = loginField.getText().trim();
        var password = passwordField.getText();
        return new EnterEntry(login, password);
    }

    private boolean checkFieldsAndShowIfBad() {
        loginErrorLabelVariant = loginFieldWithErrorLabelProcess.updateErrorLabel();
        passwordErrorLabelVariant = passwordFieldWithErrorLabelProcess.updateErrorLabel();
        confirmPasswordErrorLabelVariant = confirmPasswordFieldWithErrorLabelProcess.updateErrorLabel();
        return loginErrorLabelVariant != NONE ||
                passwordErrorLabelVariant != NONE ||
                confirmPasswordErrorLabelVariant != NONE;
    }

    private void hideErrorLabels() {
        loginErrorLabel.setVisible(false);
        passwordErrorLabel.setVisible(false);
        confirmPasswordErrorLabel.setVisible(false);
    }

    public void setLabels() {
        updateLanguage();
    }

    @Override
    public void updateLanguage() {
        System.out.println("Can be updated to " + AppLocaleManager.getCurrentLocale().getDisplayName());
        ElementsLocaleSetter.setLocalizedText(enterLoginLabel, "loginLabel");
        ElementsLocaleSetter.setLocalizedPromptText(loginField, "loginFieldPromptText");
        ElementsLocaleSetter.setLocalizedText(enterPasswordLabel, "passwordLabel");
        ElementsLocaleSetter.setLocalizedPromptText(passwordField, "passwordFieldPromptText");
        ElementsLocaleSetter.setLocalizedText(confirmPasswordLabel, "confirmPasswordLabel");
        ElementsLocaleSetter.setLocalizedPromptText(confirmPasswordField, "confirmPasswordFieldPromptText");
        ElementsLocaleSetter.setLocalizedText(registerButton, "registerButtonLabel");
        ElementsLocaleSetter.setLocalizedText(enterButton, "enterButtonLabel");
        loginFieldWithErrorLabelProcess.setLocalizedErrorLabelIfHas(loginErrorLabelVariant);
        passwordFieldWithErrorLabelProcess.setLocalizedErrorLabelIfHas(passwordErrorLabelVariant);
        confirmPasswordFieldWithErrorLabelProcess.setLocalizedErrorLabelIfHas(confirmPasswordErrorLabelVariant);
        AppLocaleChoiceBoxSetter.updateLanguage(languageSelector);
    }
}
