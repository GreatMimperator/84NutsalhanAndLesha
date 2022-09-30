package ru.miron.nonstop.controllers;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import ru.miron.nonstop.locales.ElementsLocaleSetter;

public class Validate {

    public enum LoginErrorLabelVariant {
        BLANK, TOO_BIG, TOO_SMALL
    }

    public enum PasswordErrorLabelVariant {
        BLANK, TOO_SMALL
    }

    public enum ConfirmPasswordErrorLabelVariant {
        BLANK, NOT_EQUALS_TO_PASSWORD
    }

    /**
     * @return null if hasn't problems with login. Sets login error label text if has and returns error variant
     */
    public static LoginErrorLabelVariant validateLoginAndShowTextLabelWithErrorIfBad(TextField loginField, Label loginErrorLabel) {
        return validateLoginAndShowTextLabelWithErrorIfBad(
                getLogin(loginField),
                loginErrorLabel);
    }

    /**
     * @return null if hasn't problems with login. Sets login error label text if has and returns error variant
     */
    public static LoginErrorLabelVariant validateLoginAndShowTextLabelWithErrorIfBad(String login, Label loginErrorLabel) {
        LoginErrorLabelVariant errorVariant = null;
        if (login.isEmpty()) {
            errorVariant = LoginErrorLabelVariant.BLANK;
        } else if (login.length() > 80) {
            errorVariant = LoginErrorLabelVariant.TOO_BIG;
        } else if (login.length() < 5) {
            errorVariant = LoginErrorLabelVariant.TOO_SMALL;
        }
        if (errorVariant != null) {
            setLoginErrorLabelInCurrentLanguage(loginErrorLabel, errorVariant);
            loginErrorLabel.setVisible(true);
        } else {
            loginErrorLabel.setVisible(false);
        }
        return errorVariant;
    }


    public static void setLoginErrorLabelInCurrentLanguageIfHasVariant(Label loginErrorLabel, LoginErrorLabelVariant loginErrorLabelVariant) {
        if (loginErrorLabelVariant != null) {
            setLoginErrorLabelInCurrentLanguage(loginErrorLabel, loginErrorLabelVariant);
        }
    }

    /**
     * @throws IllegalStateException if variant is null
     */
    public static void setLoginErrorLabelInCurrentLanguage(Label loginErrorLabel, LoginErrorLabelVariant loginErrorLabelVariant) throws IllegalStateException {
        if (loginErrorLabelVariant == null) {
            throw new IllegalStateException();
        }
        switch (loginErrorLabelVariant) {
            case BLANK -> {
                ElementsLocaleSetter.setLabelTextInCurrentLanguage(loginErrorLabel, "loginErrorLabelIsBlank");
            }
            case TOO_BIG -> {
                ElementsLocaleSetter.setLabelTextInCurrentLanguage(loginErrorLabel, "loginErrorLabelIsTooBig");
            }
            case TOO_SMALL -> {
                ElementsLocaleSetter.setLabelTextInCurrentLanguage(loginErrorLabel, "loginErrorLabelIsTooSmall");
            }
        }
    }

    /**
     * @return null if hasn't problems with password. Sets password error label text if has and returns error variant
     */
    public static PasswordErrorLabelVariant validatePasswordAndShowTextLabelWithErrorIfBad(TextField passwordField, Label passwordErrorLabel) {
        return validatePasswordAndShowTextLabelWithErrorIfBad(
                getPassword(passwordField),
                passwordErrorLabel);
    }

    /**
     * @return null if hasn't problems with password. Sets password error label text if has and returns error variant
     */
    public static PasswordErrorLabelVariant validatePasswordAndShowTextLabelWithErrorIfBad(String password, Label passwordErrorLabel) {
        PasswordErrorLabelVariant errorVariant = null;
        if (password.isEmpty()) {
            errorVariant = PasswordErrorLabelVariant.BLANK;
        } else if (password.length() < 5) {
            errorVariant = PasswordErrorLabelVariant.TOO_SMALL;
        }
        if (errorVariant != null) {
            setPasswordErrorLabelInCurrentLanguage(passwordErrorLabel, errorVariant);
            passwordErrorLabel.setVisible(true);
        } else {
            passwordErrorLabel.setVisible(false);
        }
        return errorVariant;
    }

    public static void setPasswordErrorLabelInCurrentLanguageIfHasVariant(Label passwordErrorLabel, PasswordErrorLabelVariant passwordErrorLabelVariant) {
        if (passwordErrorLabelVariant != null) {
            setPasswordErrorLabelInCurrentLanguage(passwordErrorLabel, passwordErrorLabelVariant);
        }
    }

    /**
     * @throws IllegalStateException if variant is null
     */
    public static void setPasswordErrorLabelInCurrentLanguage(Label passwordErrorLabel, PasswordErrorLabelVariant passwordErrorLabelVariant) throws IllegalStateException{
        if (passwordErrorLabelVariant == null) {
            throw new IllegalStateException();
        }
        switch (passwordErrorLabelVariant) {
            case BLANK -> {
                ElementsLocaleSetter.setLabelTextInCurrentLanguage(passwordErrorLabel, "passwordErrorLabelIsBlank");
            }
            case TOO_SMALL -> {
                ElementsLocaleSetter.setLabelTextInCurrentLanguage(passwordErrorLabel, "passwordErrorLabelIsTooSmall");
            }
        }
    }

    /**
     * @return null if hasn't problems with confirmPassword. Sets confirm password error label text if has and returns error variant
     */
    public static ConfirmPasswordErrorLabelVariant validateConfirmPasswordAndShowTextLabelWithErrorIfBad(TextField confirmPasswordField, TextField passwordField, Label confirmPasswordErrorLabel) {
        return validateConfirmPasswordAndShowTextLabelWithErrorIfBad(
                getPassword(confirmPasswordField),
                getPassword(passwordField),
                confirmPasswordErrorLabel);
    }

    /**
     * @return null if hasn't problems with confirmPassword. Sets confirm password error label text if has and returns error variant
     */
    public static ConfirmPasswordErrorLabelVariant validateConfirmPasswordAndShowTextLabelWithErrorIfBad(String confirmPassword, String password, Label confirmPasswordErrorLabel) {
        ConfirmPasswordErrorLabelVariant errorVariant = null;
        if (confirmPassword.isEmpty()) {
            errorVariant = ConfirmPasswordErrorLabelVariant.BLANK;
        } else if (confirmPassword.equals(password) == false) {
            errorVariant = ConfirmPasswordErrorLabelVariant.NOT_EQUALS_TO_PASSWORD;
        }
        if (errorVariant != null) {
            setConfirmPasswordErrorLabelInCurrentLanguage(confirmPasswordErrorLabel, errorVariant);
            confirmPasswordErrorLabel.setVisible(true);
        } else {
            confirmPasswordErrorLabel.setVisible(false);
        }
        return errorVariant;
    }

    public static void setConfirmPasswordErrorLabelInCurrentLanguageIfHasVariant(Label confirmPasswordErrorLabel, ConfirmPasswordErrorLabelVariant confirmPasswordErrorLabelVariant) {
        if (confirmPasswordErrorLabelVariant != null) {
            setConfirmPasswordErrorLabelInCurrentLanguage(confirmPasswordErrorLabel, confirmPasswordErrorLabelVariant);
        }
    }
    /**
     * @throws IllegalStateException if variant is null
     */
    public static void setConfirmPasswordErrorLabelInCurrentLanguage(Label confirmPasswordErrorLabel, ConfirmPasswordErrorLabelVariant confirmPasswordErrorLabelVariant) throws IllegalStateException {
        if (confirmPasswordErrorLabelVariant == null) {
            throw new IllegalStateException();
        }
        switch (confirmPasswordErrorLabelVariant) {
            case BLANK -> {
                ElementsLocaleSetter.setLabelTextInCurrentLanguage(confirmPasswordErrorLabel, "confirmPasswordErrorLabelIsBlank");
            }
            case NOT_EQUALS_TO_PASSWORD -> {
                ElementsLocaleSetter.setLabelTextInCurrentLanguage(confirmPasswordErrorLabel, "confirmPasswordErrorLabelNotEqualsToPassword");
            }
        }
    }

    public static String getLogin(TextField loginField) {
        return loginField.getText().trim();
    }

    public static String getPassword(TextField passwordField) {
        return passwordField.getText();
    }
}
