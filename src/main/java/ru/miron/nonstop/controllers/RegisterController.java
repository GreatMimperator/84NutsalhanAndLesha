package ru.miron.nonstop.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import ru.miron.nonstop.EmoCore;
import ru.miron.nonstop.locales.AppLocaleManager;
import ru.miron.nonstop.locales.LanguageUpdatable;

import java.util.Locale;
import java.util.ResourceBundle;

public class RegisterController implements LanguageUpdatable {
    @FXML
    private ChoiceBox<String> languageSelector;

    @FXML
    private TextField loginField;

    @FXML
    private Label loginErrorLabel;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private Label passwordErrorLabel;

    @FXML
    private Button registerButton;

    @FXML
    private Button enterButton;

    public void enterBtnActionHandler(ActionEvent actionEvent) {
        System.out.println("Enter btn of reg controller clicked");
        EmoCore.setHelloScene();
    }

    public void registerBtnActionHandler(ActionEvent actionEvent) {
        System.out.println("Register btn of reg controller clicked");
        if (checkFieldsAndAwareIfBad()) {
            return;
        }
//        if (EmoCore.register(loginField.getText(), passwordField.getText()))
    }

    private boolean checkFieldsAndAwareIfBad() {
        var login = loginField.getText();
        var password = passwordField.getText();
        Locale locale = new Locale("ru", "RU");
//        ResourceBundle resourceBundle = new ResourceBundle();
//        if (loginField.getText().isBlank()) {
//
//        }
        return false;
    }

    public void initialize() {
        System.out.println("hello worldd");
    }

    @Override
    public void updateLanguage() {
        System.out.println("Can be updated to " + AppLocaleManager.getAppLocaleManager().getCurrentLocale().getDisplayName());
    }
}
