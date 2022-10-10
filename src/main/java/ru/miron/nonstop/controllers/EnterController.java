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

import static ru.miron.nonstop.controllers.Validate.ErrorVariant.*;

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

    private Validate.ErrorVariant loginErrorLabelVariant;
    private Validate.InputWithErrorLabelProcess loginFieldWithErrorLabelProcess;

    private Validate.ErrorVariant passwordErrorLabelVariant;
    private Validate.InputWithErrorLabelProcess passwordFieldWithErrorLabelProcess;

    @FXML
    public void initialize() {
        System.out.println("inited enter controller");
        initErrorVariantsInfrastructure();
        AppLocaleChoiceBoxSetter.setContentAndOnChangeLanguageChange(languageSelector);
        setLabels();
    }

    private void initErrorVariantsInfrastructure() {
        loginFieldWithErrorLabelProcess = Validate.Login.initProcessor(loginField, loginErrorLabel);
        passwordFieldWithErrorLabelProcess = Validate.Password.initProcessor(passwordField, passwordErrorLabel);

        loginErrorLabelVariant = NONE;
        passwordErrorLabelVariant = NONE;
    }

    public void changeToRegisterScene(ActionEvent actionEvent) {
        System.out.println("Register btn of enter controller clicked");
        EmoCore.setRegisterScene();
    }

    public void enter(ActionEvent actionEvent) {
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
        var login = loginField.getText().trim();
        var password = passwordField.getText();
        return new EnterEntry(login, password);
    }

    private boolean checkFieldsAndShowIfBad() {
        loginErrorLabelVariant = loginFieldWithErrorLabelProcess.updateErrorLabel();
        passwordErrorLabelVariant = passwordFieldWithErrorLabelProcess.updateErrorLabel();
        return loginErrorLabelVariant != NONE ||
                passwordErrorLabelVariant != NONE;
    }

    private void hideErrorLabels() {
        loginErrorLabel.setVisible(false);
        passwordErrorLabel.setVisible(false);
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
        ElementsLocaleSetter.setLocalizedText(registerButton, "registerButtonLabel");
        ElementsLocaleSetter.setLocalizedText(enterButton, "enterButtonLabel");
        loginFieldWithErrorLabelProcess.setLocalizedErrorLabelIfHas(loginErrorLabelVariant);
        passwordFieldWithErrorLabelProcess.setLocalizedErrorLabelIfHas(passwordErrorLabelVariant);
        AppLocaleChoiceBoxSetter.updateLanguage(languageSelector);
    }
}
