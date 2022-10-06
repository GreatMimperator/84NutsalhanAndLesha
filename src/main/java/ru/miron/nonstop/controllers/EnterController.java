package ru.miron.nonstop.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import ru.miron.nonstop.EmoCore;
import ru.miron.nonstop.locales.AppLocaleChoiceBoxSetter;
import ru.miron.nonstop.locales.ElementsLocaleSetter;
import ru.miron.nonstop.locales.AppLocaleManager;
import ru.miron.nonstop.locales.LanguageUpdatable;
import ru.miron.nonstop.logic.commands.Command;
import ru.miron.nonstop.logic.commands.CommandAnswer;
import ru.miron.nonstop.logic.commands.CommandName;
import ru.miron.nonstop.logic.commands.EnterEntry;

import java.io.IOException;

import static ru.miron.nonstop.logic.commands.CommandAnswerWithoutArgs.EnterState.*;

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
        if (checkFieldsAndShowIfBad()) {
            System.out.println("Enter fields are bad. So, wont send any data");
            try {
                EmoCore.createInfoAutoClosableWindow("badEnteredInfoMsg", "Bad entered info");
            } catch (IOException e) {}
        } else {
            System.out.println("Enter fields are good. So, will send to server");
            var enterEntry = getEnterEntryFromFields();
            var registerCommand = new Command(CommandName.SIGN_IN, enterEntry, null);
            CommandAnswer signInCommandAnswer;
            try {
                signInCommandAnswer = EmoCore.tryToGetCommandAnswerWithErrorWindowsGenOnFailOrErrorAnswer(registerCommand);
            } catch (IllegalStateException e) {
                return;
            }
            var signInEnterState = signInCommandAnswer.getCommandAnswerWithoutArgs().getEnterState();
            switch (signInEnterState) {
                case ENTERED -> {
                    EmoCore.enterEntry = enterEntry;
                    System.out.println("HERE CHANGES TO MAIN SCENE");
                    EmoCore.setTableScene();
                }
                case WRONG_LOGIN -> {
                    try {
                        EmoCore.createInfoAutoClosableWindow("signInLoginDoesntExistMsg", "Login doesn't exist");
                    } catch (IOException e) {}
                }
                case WRONG_PASSWORD -> {
                    try {
                        EmoCore.createInfoAutoClosableWindow("signInWrongPasswordMsg", "Wrong password");
                    } catch (IOException e) {}
                }
                default -> {
                    throw new IllegalStateException("Api Changed");
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
        AppLocaleChoiceBoxSetter.updateLanguage(languageSelector);
    }
}
