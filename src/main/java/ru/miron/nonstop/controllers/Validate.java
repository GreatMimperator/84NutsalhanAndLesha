package ru.miron.nonstop.controllers;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import ru.miron.nonstop.locales.ElementsLocaleSetter;

import java.util.regex.Pattern;

public class Validate {
    public enum LabelErrorVariant { // todo: do
        BLANK,
        TOO_BIG,
        TOO_SMALL,
        NOT_EQUALS_TO,
        NOT_EVEN_NUMBER,
        NOT_POSITIVE,
        NEGATIVE,
        FLOAT
    }

    public enum LoginErrorLabelVariant {
        BLANK, TOO_BIG, TOO_SMALL
    }

    public enum PasswordErrorLabelVariant {
        BLANK, TOO_SMALL
    }


    public enum ConfirmPasswordErrorLabelVariant {
        BLANK, NOT_EQUALS_TO_PASSWORD
    }

    public enum SimpleStringErrorLabelVariant {
        BLANK, TOO_BIG
    }

    public enum PositiveWholeNumberErrorLabelVariant {
        BLANK, NOT_EVEN_NUMBER, NOT_POSITIVE, FLOAT, TOO_BIG
    }

    public enum WholeNumberErrorLabelVariant {
        BLANK, NOT_EVEN_NUMBER, FLOAT, TOO_BIG
    }

    public enum NotNegativeFloatErrorLabelVariant {
        BLANK, NOT_EVEN_NUMBER, NEGATIVE
    }

    public enum FloatErrorLabelVariant {
        BLANK, NOT_EVEN_NUMBER
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

    /**
     * do nothing if variant is null
     */
    public static void setSimpleStringErrorLabelInCurrentLanguageIfHasVariant(Label simpleStringErrorLabel, SimpleStringErrorLabelVariant simpleStringErrorLabelVariant, int maxLength) {
        if (simpleStringErrorLabelVariant != null) {
            setSimpleStringErrorLabelInCurrentLanguage(simpleStringErrorLabel, simpleStringErrorLabelVariant, maxLength);
        }
    }

    /**
     * @throws IllegalStateException if variant is null
     */
    public static void setSimpleStringErrorLabelInCurrentLanguage(Label simpleStringErrorLabel, SimpleStringErrorLabelVariant simpleStringErrorLabelVariant, int maxLength) throws  IllegalStateException {
        if (simpleStringErrorLabelVariant == null) {
            throw new IllegalStateException();
        }
        switch (simpleStringErrorLabelVariant) {
            case BLANK -> {
                ElementsLocaleSetter.setLabelTextInCurrentLanguage(simpleStringErrorLabel, "fieldErrorLabelIsBlank");
            }
            case TOO_BIG -> {
                ElementsLocaleSetter.setLabelTextInCurrentLanguage(simpleStringErrorLabel, String.format("stringErrorLabelTooBig", maxLength));
            }
        }
    }

    /**
     * @return null if hasn't problems with login. Sets login error label text if has and returns error variant
     */
    public static SimpleStringErrorLabelVariant validateSimpleStringAndShowTextLabelWithErrorIfBad(TextField textField, Label simpleStringErrorLabel, int maxLength) {
        return validateSimpleStringAndShowTextLabelWithErrorIfBad(textField.getText(), simpleStringErrorLabel, maxLength);
    }

    /**
     * @return null if hasn't problems with login. Sets login error label text if has and returns error variant
     */
    public static SimpleStringErrorLabelVariant validateSimpleStringAndShowTextLabelWithErrorIfBad(String string, Label simpleStringErrorLabel, int maxLength) {
        SimpleStringErrorLabelVariant errorVariant = validateSimpleString(string, maxLength);
        if (errorVariant != null) {
            setSimpleStringErrorLabelInCurrentLanguage(simpleStringErrorLabel, errorVariant, maxLength);
            simpleStringErrorLabel.setVisible(true);
        } else {
            simpleStringErrorLabel.setVisible(false);
        }
        return errorVariant;
    }

    public static SimpleStringErrorLabelVariant validateSimpleStringField(TextField textField, int maxLength) {
        return validateSimpleString(textField.getText(), maxLength);
    }

    public static SimpleStringErrorLabelVariant validateSimpleString(String string, int maxLength) {
        string = string.trim();
        if (string.isBlank()) {
            return SimpleStringErrorLabelVariant.BLANK;
        }
        if (string.length() > maxLength) {
            return SimpleStringErrorLabelVariant.TOO_BIG;
        }
        return null;
    }

    public static String numberPattern = "^-?\\d+\\.?\\d*$";
    public static String negativeNumberPattern = "^-\\d+\\.?\\d*$";
    public static String floatNumberPattern = "^-?\\d+\\.\\d*$";

    /**
     * @return null if hasn't problems with login. Sets login error label text if has and returns error variant
     */
    public static PositiveWholeNumberErrorLabelVariant validatePositiveIntegerAndShowTextLabelWithErrorIfBad(TextField textField, Label integerErrorLabel) {
        return validatePositiveIntegerAndShowTextLabelWithErrorIfBad(textField.getText(), integerErrorLabel);
    }

    /**
     * @return null if hasn't problems with login. Sets login error label text if has and returns error variant
     */
    public static PositiveWholeNumberErrorLabelVariant validatePositiveIntegerAndShowTextLabelWithErrorIfBad(String string, Label integerErrorLabel) {
        PositiveWholeNumberErrorLabelVariant errorVariant = validatePositiveInteger(string);
        if (errorVariant != null) {
            setPositiveWholeNumberErrorLabelInCurrentLanguageIfHasVariant(integerErrorLabel, errorVariant);
            integerErrorLabel.setVisible(true);
        } else {
            integerErrorLabel.setVisible(false);
        }
        return errorVariant;
    }

    public static PositiveWholeNumberErrorLabelVariant validatePositiveInteger(TextField textField) {
        return validatePositiveInteger(textField.getText());
    }

    public static PositiveWholeNumberErrorLabelVariant validatePositiveInteger(String positiveIntegerAsString) {
        positiveIntegerAsString = positiveIntegerAsString.trim();
        var checksWithoutParseCheck = validatePositiveWholeNumberFieldWithoutParseCheck(positiveIntegerAsString);
        if (checksWithoutParseCheck != null) {
            return checksWithoutParseCheck;
        }
        try {
            Integer.parseInt(positiveIntegerAsString);
        } catch (NumberFormatException e) {
            return PositiveWholeNumberErrorLabelVariant.TOO_BIG;
        }
        return null;
    }

    /**
     * @return null if hasn't problems with login. Sets login error label text if has and returns error variant
     */
    public static PositiveWholeNumberErrorLabelVariant validatePositiveLongAndShowTextLabelWithErrorIfBad(TextField textField, Label longErrorLabel) {
        return validatePositiveLongAndShowTextLabelWithErrorIfBad(textField.getText(), longErrorLabel);
    }

    /**
     * @return null if hasn't problems with login. Sets login error label text if has and returns error variant
     */
    public static PositiveWholeNumberErrorLabelVariant validatePositiveLongAndShowTextLabelWithErrorIfBad(String string, Label longErrorLabel) {
        PositiveWholeNumberErrorLabelVariant errorVariant = validatePositiveLong(string);
        if (errorVariant != null) {
            setPositiveWholeNumberErrorLabelInCurrentLanguageIfHasVariant(longErrorLabel, errorVariant);
            longErrorLabel.setVisible(true);
        } else {
            longErrorLabel.setVisible(false);
        }
        return errorVariant;
    }

    public static PositiveWholeNumberErrorLabelVariant validatePositiveLongField(TextField positiveIntegerField) {
        return validatePositiveLong(positiveIntegerField.toString());
    }

    public static PositiveWholeNumberErrorLabelVariant validatePositiveLong(String positiveLongAsString) {
        positiveLongAsString = positiveLongAsString.trim();
        var checksWithoutParseCheck = validatePositiveWholeNumberFieldWithoutParseCheck(positiveLongAsString);
        if (checksWithoutParseCheck != null) {
            return checksWithoutParseCheck;
        }
        try {
            Long.parseLong(positiveLongAsString);
        } catch (NumberFormatException e) {
            return PositiveWholeNumberErrorLabelVariant.TOO_BIG;
        }
        return null;
    }


    private static PositiveWholeNumberErrorLabelVariant validatePositiveWholeNumberFieldWithoutParseCheck(TextField positiveWholeNumberField) {
        return validatePositiveWholeNumberFieldWithoutParseCheck(positiveWholeNumberField.getText());
    }

    private static PositiveWholeNumberErrorLabelVariant validatePositiveWholeNumberFieldWithoutParseCheck(String positiveWholeNumberAsString) {
        positiveWholeNumberAsString = positiveWholeNumberAsString.trim();
        if (positiveWholeNumberAsString.isEmpty()) {
            return PositiveWholeNumberErrorLabelVariant.BLANK;
        }
        if (!Pattern.matches(numberPattern, positiveWholeNumberAsString)) {
            return PositiveWholeNumberErrorLabelVariant.NOT_EVEN_NUMBER;
        }
        if (Pattern.matches(negativeNumberPattern, positiveWholeNumberAsString)) {
            return PositiveWholeNumberErrorLabelVariant.NOT_POSITIVE;
        }
        if (Pattern.matches(floatNumberPattern, positiveWholeNumberAsString)) {
            return PositiveWholeNumberErrorLabelVariant.FLOAT;
        }
        return null;
    }


    /**
     * do nothing if variant is null
     */
    public static void setPositiveWholeNumberErrorLabelInCurrentLanguageIfHasVariant(Label positiveWholeNumberErrorLabel, PositiveWholeNumberErrorLabelVariant positiveWholeNumberErrorLabelVariant) {
        if (positiveWholeNumberErrorLabelVariant != null) {
            setPositiveWholeNUmberErrorLabelInCurrentLanguage(positiveWholeNumberErrorLabel, positiveWholeNumberErrorLabelVariant);
        }
    }

    /**
     * @throws IllegalStateException if variant is null
     */
    public static void setPositiveWholeNUmberErrorLabelInCurrentLanguage(Label positiveWholeNumberErrorLabel, PositiveWholeNumberErrorLabelVariant positiveWholeNumberErrorLabelVariant) throws IllegalStateException {
        if (positiveWholeNumberErrorLabelVariant == null) {
            throw new IllegalStateException();
        }
        switch (positiveWholeNumberErrorLabelVariant) {
            case BLANK -> {
                ElementsLocaleSetter.setLabelTextInCurrentLanguage(positiveWholeNumberErrorLabel, "fieldErrorLabelIsBlank");
            }
            case NOT_EVEN_NUMBER -> {
                ElementsLocaleSetter.setLabelTextInCurrentLanguage(positiveWholeNumberErrorLabel, "numberErrorLabelNotNumber");
            }
            case NOT_POSITIVE -> {
                ElementsLocaleSetter.setLabelTextInCurrentLanguage(positiveWholeNumberErrorLabel, "numberErrorLabelNotPositive");
            }
            case FLOAT -> {
                ElementsLocaleSetter.setLabelTextInCurrentLanguage(positiveWholeNumberErrorLabel, "numberErrorLabelIsFloat");
            }
            case TOO_BIG -> {
                ElementsLocaleSetter.setLabelTextInCurrentLanguage(positiveWholeNumberErrorLabel, "numberErrorLabelTooBig");
            }
        }
    }

    /**
     * @return null if hasn't problems with login. Sets login error label text if has and returns error variant
     */
    public static WholeNumberErrorLabelVariant validateLongAndShowTextLabelWithErrorIfBad(TextField textField, Label longErrorLabel) {
        return validateLongAndShowTextLabelWithErrorIfBad(textField.getText(), longErrorLabel);
    }

    /**
     * @return null if hasn't problems with login. Sets login error label text if has and returns error variant
     */
    public static WholeNumberErrorLabelVariant validateLongAndShowTextLabelWithErrorIfBad(String string, Label longErrorLabel) {
        WholeNumberErrorLabelVariant errorVariant = validateLong(string);
        if (errorVariant != null) {
            setWholeNumberErrorLabelInCurrentLanguageIfHasVariant(longErrorLabel, errorVariant);
            longErrorLabel.setVisible(true);
        } else {
            longErrorLabel.setVisible(false);
        }
        return errorVariant;
    }

    public static WholeNumberErrorLabelVariant validateLongField(TextField longField) {
        return validateLong(longField.toString());
    }

    public static WholeNumberErrorLabelVariant validateLong(String positiveLongAsString) {
        positiveLongAsString = positiveLongAsString.trim();
        var checksWithoutParseCheck = validateWholeNumberFieldWithoutParseCheck(positiveLongAsString);
        if (checksWithoutParseCheck != null) {
            return checksWithoutParseCheck;
        }
        try {
            Long.parseLong(positiveLongAsString);
        } catch (NumberFormatException e) {
            return WholeNumberErrorLabelVariant.TOO_BIG;
        }
        return null;
    }


    private static WholeNumberErrorLabelVariant validateWholeNumberFieldWithoutParseCheck(TextField wholeNumberField) {
        return validateWholeNumberFieldWithoutParseCheck(wholeNumberField.getText());
    }

    private static WholeNumberErrorLabelVariant validateWholeNumberFieldWithoutParseCheck(String wholeNumberAsString) {
        wholeNumberAsString = wholeNumberAsString.trim();
        if (wholeNumberAsString.isEmpty()) {
            return WholeNumberErrorLabelVariant.BLANK;
        }
        if (!Pattern.matches(numberPattern, wholeNumberAsString)) {
            return WholeNumberErrorLabelVariant.NOT_EVEN_NUMBER;
        }
        if (Pattern.matches(floatNumberPattern, wholeNumberAsString)) {
            return WholeNumberErrorLabelVariant.FLOAT;
        }
        return null;
    }


    /**
     * do nothing if variant is null
     */
    public static void setWholeNumberErrorLabelInCurrentLanguageIfHasVariant(Label wholeNumberErrorLabel, WholeNumberErrorLabelVariant wholeNumberErrorLabelVariant) {
        if (wholeNumberErrorLabelVariant != null) {
            setWholeNumberErrorLabelInCurrentLanguage(wholeNumberErrorLabel, wholeNumberErrorLabelVariant);
        }
    }

    /**
     * @throws IllegalStateException if variant is null
     */
    public static void setWholeNumberErrorLabelInCurrentLanguage(Label wholeNumberErrorLabel, WholeNumberErrorLabelVariant wholeNumberErrorLabelVariant) throws IllegalStateException {
        if (wholeNumberErrorLabelVariant == null) {
            throw new IllegalStateException();
        }
        switch (wholeNumberErrorLabelVariant) {
            case BLANK -> {
                ElementsLocaleSetter.setLabelTextInCurrentLanguage(wholeNumberErrorLabel, "fieldErrorLabelIsBlank");
            }
            case NOT_EVEN_NUMBER -> {
                ElementsLocaleSetter.setLabelTextInCurrentLanguage(wholeNumberErrorLabel, "numberErrorLabelNotNumber");
            }
            case FLOAT -> {
                ElementsLocaleSetter.setLabelTextInCurrentLanguage(wholeNumberErrorLabel, "numberErrorLabelIsFloat");
            }
            case TOO_BIG -> {
                ElementsLocaleSetter.setLabelTextInCurrentLanguage(wholeNumberErrorLabel, "numberErrorLabelTooBig");
            }
        }
    }

    /**
     * @return null if hasn't problems with login. Sets login error label text if has and returns error variant
     */
    public static NotNegativeFloatErrorLabelVariant validateNotNegativeFloatFieldAndShowTextLabelWithErrorIfBad(TextField textField, Label notNegativeFloatErrorLabel) {
        return validateNotNegativeFloatAndShowTextLabelWithErrorIfBad(textField.getText(), notNegativeFloatErrorLabel);
    }

    /**
     * @return null if hasn't problems with login. Sets login error label text if has and returns error variant
     */
    public static NotNegativeFloatErrorLabelVariant validateNotNegativeFloatAndShowTextLabelWithErrorIfBad(String string, Label notNegativeFloatErrorLabel) {
        NotNegativeFloatErrorLabelVariant errorVariant = validateNotNegativeFloatField(string);
        if (errorVariant != null) {
            setNotNegativeFloatErrorLabelInCurrentLanguageIfHasVariant(notNegativeFloatErrorLabel, errorVariant);
            notNegativeFloatErrorLabel.setVisible(true);
        } else {
            notNegativeFloatErrorLabel.setVisible(false);
        }
        return errorVariant;
    }

    public static NotNegativeFloatErrorLabelVariant validateNotNegativeFloatField(TextField floatField) {
        return validateNotNegativeFloatField(floatField.getText());
    }
    public static NotNegativeFloatErrorLabelVariant validateNotNegativeFloatField(String floatAsString) {
        floatAsString = floatAsString.trim();
        if (floatAsString.isEmpty()) {
            return NotNegativeFloatErrorLabelVariant.BLANK;
        }
        try {
            var floatNumber = Float.parseFloat(floatAsString);
            if (floatNumber < 0) {
                return NotNegativeFloatErrorLabelVariant.NEGATIVE;
            }
        } catch (NumberFormatException e) {
            return NotNegativeFloatErrorLabelVariant.NOT_EVEN_NUMBER;
        }
        return null;
    }

    /**
     * do nothing if variant is null
     */
    public static void setNotNegativeFloatErrorLabelInCurrentLanguageIfHasVariant(Label floatErrorLabel, NotNegativeFloatErrorLabelVariant notNegativeFloatErrorLabelVariant) {
        if (notNegativeFloatErrorLabelVariant != null) {
            setNonNegativeFloatErrorLabelInCurrentLanguage(floatErrorLabel, notNegativeFloatErrorLabelVariant);
        }
    }

    /**
     * @throws IllegalStateException if variant is null
     */
    public static void setNonNegativeFloatErrorLabelInCurrentLanguage(Label floatErrorLabel, NotNegativeFloatErrorLabelVariant notNegativeFloatErrorLabelVariant) throws IllegalStateException {
        if (notNegativeFloatErrorLabelVariant == null) {
            throw new IllegalStateException();
        }
        switch (notNegativeFloatErrorLabelVariant) {
            case BLANK -> {
                ElementsLocaleSetter.setLabelTextInCurrentLanguage(floatErrorLabel, "fieldErrorLabelIsBlank");
            }
            case NOT_EVEN_NUMBER -> {
                ElementsLocaleSetter.setLabelTextInCurrentLanguage(floatErrorLabel, "numberErrorLabelNotNumber");
            }
            case NEGATIVE -> {
                ElementsLocaleSetter.setLabelTextInCurrentLanguage(floatErrorLabel, "numberErrorLabelIsNegative");
            }
        }
    }

    /**
     * @return null if hasn't problems with login. Sets login error label text if has and returns error variant
     */
    public static FloatErrorLabelVariant validateFloatFieldAndShowTextLabelWithErrorIfBad(TextField textField, Label floatErrorLabel) {
        return validateFloatAndShowTextLabelWithErrorIfBad(textField.getText(), floatErrorLabel);
    }

    /**
     * @return null if hasn't problems with login. Sets login error label text if has and returns error variant
     */
    public static FloatErrorLabelVariant validateFloatAndShowTextLabelWithErrorIfBad(String string, Label floatErrorLabel) {
        FloatErrorLabelVariant errorVariant = validateFloat(string);
        if (errorVariant != null) {
            setFloatErrorLabelInCurrentLanguage(floatErrorLabel, errorVariant);
            floatErrorLabel.setVisible(true);
        } else {
            floatErrorLabel.setVisible(false);
        }
        return errorVariant;
    }

    public static FloatErrorLabelVariant validateFloatField(TextField floatField) {
        return validateFloat(floatField.getText());
    }

    public static FloatErrorLabelVariant validateFloat(String floatAsString) {
        floatAsString = floatAsString.trim();
        if (floatAsString.isEmpty()) {
            return FloatErrorLabelVariant.BLANK;
        }
        try {
            Float.parseFloat(floatAsString);
        } catch (NumberFormatException e) {
            return FloatErrorLabelVariant.NOT_EVEN_NUMBER;
        }
        return null;
    }

    /**
     * do nothing if variant is null
     */
    public static void setFloatErrorLabelInCurrentLanguageIfHasVariant(Label floatErrorLabel, FloatErrorLabelVariant floatErrorLabelVariant) {
        if (floatErrorLabelVariant != null) {
            setFloatErrorLabelInCurrentLanguage(floatErrorLabel, floatErrorLabelVariant);
        }
    }

    /**
     * @throws IllegalStateException if variant is null
     */
    public static void setFloatErrorLabelInCurrentLanguage(Label floatErrorLabel, FloatErrorLabelVariant floatErrorLabelVariant) throws IllegalStateException {
        if (floatErrorLabelVariant == null) {
            throw new IllegalStateException();
        }
        switch (floatErrorLabelVariant) {
            case BLANK -> {
                ElementsLocaleSetter.setLabelTextInCurrentLanguage(floatErrorLabel, "fieldErrorLabelIsBlank");
            }
            case NOT_EVEN_NUMBER -> {
                ElementsLocaleSetter.setLabelTextInCurrentLanguage(floatErrorLabel, "numberErrorLabelNotNumber");
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
