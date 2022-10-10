package ru.miron.nonstop.controllers;

import javafx.scene.control.Label;
import javafx.scene.control.TextInputControl;
import ru.miron.nonstop.locales.AppLocaleManager;
import ru.miron.nonstop.locales.ElementsLocaleSetter;
import ru.miron.nonstop.locales.entities.LabelText;

import java.util.HashMap;
import java.util.Map;
import java.util.MissingFormatArgumentException;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

import static ru.miron.nonstop.controllers.Validate.ErrorVariant.*;
import static ru.miron.nonstop.locales.entities.LabelText.TextType.*;

public class Validate {
    public enum ErrorVariant { // todo: do
        NONE,
        BLANK,
        BLANK_CHARS_AROUND,
        TOO_BIG,
        TOO_SMALL,
        NOT_EQUALS_TO,
        NOT_EVEN_NUMBER,
        NOT_POSITIVE,
        NEGATIVE,
        FLOAT;

        /**
         * @throws IllegalStateException if labelErrorVariant is NONE
         */
        public void checkOnError() throws IllegalStateException {
            if (this == NONE) {
                throw new IllegalStateException("Error label variant shouldn't be NONE");
            }
        }
    }

    @FunctionalInterface
    public interface LocalizedErrorLabelUpdater {
        /**
         * @param formatter can be null if nothing to format
         * @throws IllegalStateException if errorVariantToSet is NONE or has illegal variant
         * @throws IllegalArgumentException if formatter is null when should be
         * @throws MissingFormatArgumentException if wrong formatter
         */
        void update(Label errorLabel, ErrorVariant variantToSet, UnaryOperator<String> formatter) throws IllegalStateException, IllegalArgumentException, MissingFormatArgumentException;
    }

    public static void setVisibility(Label errorLabel, ErrorVariant errorVariant) {
        if (errorVariant != NONE) {
            errorLabel.setVisible(true);
        } else {
            errorLabel.setVisible(false);
        }
    }

    public static class InputWithErrorLabelProcess {
        private TextInputControl inputValueToValidate = null;
        private Label errorLabel = null;
        private Function<String, ErrorVariant> validator;
        private LocalizedErrorLabelUpdater localizedErrorLabelUpdater;
        private Map<ErrorVariant, UnaryOperator<String>> errorLabelFormatters;

        /**
         * @throws IllegalArgumentException if any (except of errorLabelFormatters)
         */
        private InputWithErrorLabelProcess(
                Function<String, ErrorVariant> validator,
                LocalizedErrorLabelUpdater localizedErrorLabelUpdater,
                Map<ErrorVariant, UnaryOperator<String>> errorLabelFormatters) throws IllegalArgumentException {
           if (validator == null || localizedErrorLabelUpdater == null) {
               throw new IllegalArgumentException();
           }
           this.validator = validator;
           this.localizedErrorLabelUpdater = localizedErrorLabelUpdater;
           this.errorLabelFormatters = errorLabelFormatters;
        }

        /**
         * @throws IllegalArgumentException if validator returned illegal value or required to set inputs (fieldValueToValidate, errorLabel) is null
         * @throws IllegalStateException if labelErrorVariant is NONE
         */
        public ErrorVariant updateErrorLabel() throws IllegalArgumentException, IllegalStateException {
            if (inputValueToValidate == null || errorLabel == null) {
                throw new IllegalArgumentException("Required inputs is null");
            }
            return Validate.updateErrorLabel(
                    inputValueToValidate,
                    errorLabel,
                    validator,
                    localizedErrorLabelUpdater,
                    errorLabelFormatters);
        }

        public void setLocalizedErrorLabelIfHas(ErrorVariant errorVariant) {
            if (errorVariant != NONE) {
                if (errorLabelFormatters.containsKey(errorVariant)) {
                    localizedErrorLabelUpdater.update(errorLabel, errorVariant, errorLabelFormatters.get(errorVariant));
                } else {
                    localizedErrorLabelUpdater.update(errorLabel, errorVariant, null);
                }
            }
        }

        public void setInputValueToValidate(TextInputControl inputValueToValidate) {
            this.inputValueToValidate = inputValueToValidate;
        }

        public void setErrorLabel(Label errorLabel) {
            this.errorLabel = errorLabel;
        }

        public void updateFormatters(Map<ErrorVariant, UnaryOperator<String>> errorLabelFormatters) {
            this.errorLabelFormatters = errorLabelFormatters;
        }

        public static class Builder {
            private Function<String, ErrorVariant> validator;
            private LocalizedErrorLabelUpdater localizedErrorLabelUpdater;
            private Map<ErrorVariant, UnaryOperator<String>> errorLabelFormatters;

            public Builder() {
                validator = null;
                localizedErrorLabelUpdater = null;
                errorLabelFormatters = null;
            }

            /**
             * @throws IllegalStateException if either of validator or errorLabelInCurrentLanguageSetter inputs is null
             */
            public InputWithErrorLabelProcess build() throws IllegalStateException {
                if (validator == null || localizedErrorLabelUpdater == null) {
                    throw new IllegalStateException("Any of required inputs is null");
                }
                return new InputWithErrorLabelProcess(validator, localizedErrorLabelUpdater, errorLabelFormatters);
            }

            public Builder setValidator(Function<String, ErrorVariant> validator) {
                this.validator = validator;
                return this;
            }

            public Builder setErrorLabelInCurrentLanguageSetter(LocalizedErrorLabelUpdater localizedErrorLabelUpdater) {
                this.localizedErrorLabelUpdater = localizedErrorLabelUpdater;
                return this;
            }

            public Builder setErrorLabelFormatters(Map<ErrorVariant, UnaryOperator<String>> errorLabelFormatters) {
                this.errorLabelFormatters = errorLabelFormatters;
                return this;
            }
        }
    }

    /**
     * @param errorLabelFormatters can be null if nothing to format
     * @throws IllegalArgumentException if validator returned illegal value
     * @throws IllegalStateException if labelErrorVariant is NONE
     */
    public static ErrorVariant updateErrorLabel(
            TextInputControl inputValueToValidate,
            Label errorLabel,
            Function<String, ErrorVariant> validator,
            LocalizedErrorLabelUpdater localizedErrorLabelUpdater,
            Map<ErrorVariant, UnaryOperator<String>> errorLabelFormatters) throws IllegalArgumentException, IllegalStateException {
        return updateErrorLabel(
                inputValueToValidate.getText(),
                errorLabel,
                validator,
                localizedErrorLabelUpdater,
                errorLabelFormatters);
    }

    /**
     * @param errorLabelFormatters can be null if nothing to format
     * @throws IllegalArgumentException if validator returned illegal value
     */
    public static ErrorVariant updateErrorLabel(
            String valueToValidate,
            Label errorLabel,
            Function<String, ErrorVariant> validator,
            LocalizedErrorLabelUpdater localizedErrorLabelUpdater,
            Map<ErrorVariant, UnaryOperator<String>> errorLabelFormatters) {
        ErrorVariant errorVariant = validator.apply(valueToValidate);
        updateErrorLabelIfHasVariant(errorLabel, errorVariant, localizedErrorLabelUpdater, errorLabelFormatters);
        setVisibility(errorLabel, errorVariant);
        return errorVariant;
    }

    /**
     * @param errorLabelFormatters can be null if nothing to format
     * @throws IllegalStateException if variant has illegal value
     * @throws IllegalArgumentException if needed formatter
     */
    public static void updateErrorLabelIfHasVariant(
            Label errorLabel,
            ErrorVariant errorVariant,
            LocalizedErrorLabelUpdater localizedErrorLabelUpdater,
            Map<ErrorVariant, UnaryOperator<String>> errorLabelFormatters) throws IllegalArgumentException {
        if (errorVariant != NONE) {
            UnaryOperator<String> errorLabelFormatter = null;
            if (errorLabelFormatters != null) {
                errorLabelFormatter = errorLabelFormatters.get(errorVariant);
            }
            localizedErrorLabelUpdater.update(errorLabel, errorVariant, errorLabelFormatter);
        }
    }

    /**
     * @param errorLabelFormatter can be null if nothing to format
     */
    public static LabelText getFormattedStringErrorLabel(String labelName, UnaryOperator<String> errorLabelFormatter) {
        String notFormattedString = AppLocaleManager.getTextByLabel(labelName);
        if (errorLabelFormatter == null) {
            return new LabelText(notFormattedString, PLAIN_TEXT);
        }
        String formattedString = errorLabelFormatter.apply(notFormattedString);
        return new LabelText(formattedString, PLAIN_TEXT);
    }

    public static class LabelsTextFactory {
        public static void setInputErrorLabelIsBlankLabel(Label errorLabel) {
            ElementsLocaleSetter.setText(errorLabel, getInputErrorLabelIsBlank());
        }

        public static LabelText getInputErrorLabelIsBlank() {
            return new LabelText("inputErrorLabelIsBlank", LABEL_NAME);
        }


        /**
         * @throws IllegalArgumentException if formatter is null
         */
        public static void setStringErrorLabelIsTooBigLabel(Label errorLabel, UnaryOperator<String> errorLabelFormatter) throws IllegalArgumentException {
            ElementsLocaleSetter.setText(errorLabel, getStringErrorLabelTooBig(errorLabelFormatter));
        }

        /**
         * @throws IllegalArgumentException if formatter is null
         */
        public static LabelText getStringErrorLabelTooBig(UnaryOperator<String> errorLabelFormatter) throws IllegalArgumentException {
            String notFormattedString = AppLocaleManager.getTextByLabel("stringErrorLabelTooBig");
            if (errorLabelFormatter == null) {
                throw new IllegalArgumentException("Formatter have to be not null");
            }
            String formattedString = errorLabelFormatter.apply(notFormattedString);
            return new LabelText(formattedString, PLAIN_TEXT);
        }

        /**
         * @throws IllegalArgumentException if formatter is null
         */
        public static void setStringErrorLabelIsTooSmallLabel(Label errorLabel, UnaryOperator<String> errorLabelFormatter) throws IllegalArgumentException {
            ElementsLocaleSetter.setText(errorLabel, getStringErrorLabelTooSmall(errorLabelFormatter));
        }

        /**
         * @throws IllegalArgumentException if formatter is null
         */
        public static LabelText getStringErrorLabelTooSmall(UnaryOperator<String> errorLabelFormatter) throws IllegalArgumentException {
            String notFormattedString = AppLocaleManager.getTextByLabel("stringErrorLabelTooSmall");
            if (errorLabelFormatter == null) {
                throw new IllegalArgumentException("Formatter have to be not null");
            }
            String formattedString = errorLabelFormatter.apply(notFormattedString);
            return new LabelText(formattedString, PLAIN_TEXT);
        }
    }

    public static class Login {
        /**
         * @return BLANK (only empty symbols or nothing), TOO_BIG (len without corner empty symbols is over than 80) or TOO_SMALL (lower than 5),
         * NONE if hasn't
         */
        public static ErrorVariant validate(String login) {
            login = login.trim();
            if (login.isEmpty()) {
                return BLANK;
            }
            if (login.length() > 80) {
                return TOO_BIG;
            }
            if (login.length() < 5) {
                return TOO_SMALL;
            }
            return NONE;
        }

        /**
         * @throws IllegalStateException if variant is NONE or not BLANK, TOO_BIG or TOO_SMALL
         * @throws IllegalArgumentException if format is null, when needed
         */
        public static void updateLocalizedErrorLabel(Label loginErrorLabel, ErrorVariant loginErrorVariantToSet, UnaryOperator<String> errorLabelFormatter) throws IllegalStateException, IllegalArgumentException {
            loginErrorVariantToSet.checkOnError();
            switch (loginErrorVariantToSet) {
                case BLANK -> LabelsTextFactory.setInputErrorLabelIsBlankLabel(loginErrorLabel);
                case TOO_BIG -> LabelsTextFactory.setStringErrorLabelIsTooBigLabel(loginErrorLabel, errorLabelFormatter);
                case TOO_SMALL -> LabelsTextFactory.setStringErrorLabelIsTooSmallLabel(loginErrorLabel, errorLabelFormatter);
                default -> throw new IllegalStateException("Wrong enum value");
            }
        }

        public static InputWithErrorLabelProcess initProcessor(TextInputControl loginField, Label loginErrorLabel) {
            var process = initProcessor();
            process.setInputValueToValidate(loginField);
            process.setErrorLabel(loginErrorLabel);
            return process;
        }

        public static InputWithErrorLabelProcess initProcessor() {
            var formatters = new HashMap<ErrorVariant, UnaryOperator<String>>();
            formatters.put(TOO_BIG, (var string) -> string.formatted(80) );
            formatters.put(TOO_SMALL, (var string) -> string.formatted(5) );
            return new Validate.InputWithErrorLabelProcess.Builder()
                    .setValidator(Login::validate)
                    .setErrorLabelInCurrentLanguageSetter(Login::updateLocalizedErrorLabel)
                    .setErrorLabelFormatters(formatters)
                    .build();
        }
    }


    public static class Password {
        /**
         * @return BLANK (only empty symbols or nothing) or TOO_SMALL (len without corner empty symbols is lower than 5),
         * NONE if hasn't
         */
        public static ErrorVariant validate(String password) {
            int passwordLengthBeforeTrim = password.length();
            password = password.trim();
            if (password.length() != passwordLengthBeforeTrim) {
                return BLANK_CHARS_AROUND;
            }
            if (password.isEmpty()) {
                return BLANK;
            }
            if (password.length() < 5) {
                return TOO_SMALL;
            }
            return NONE;
        }

        /**
         * @throws IllegalStateException if variant is NONE or not BLANK or TOO_SMALL
         * @throws IllegalArgumentException if format is null, when needed
         */
        public static void updateLocalizedErrorLabel(Label passwordErrorLabel, ErrorVariant passwordErrorLabelVariant, UnaryOperator<String> errorLabelFormatter) throws IllegalStateException, IllegalArgumentException  {
            passwordErrorLabelVariant.checkOnError();
            switch (passwordErrorLabelVariant) {
                case BLANK -> LabelsTextFactory.setInputErrorLabelIsBlankLabel(passwordErrorLabel);
                case BLANK_CHARS_AROUND -> ElementsLocaleSetter.setLocalizedText(passwordErrorLabel, "inputErrorLabelBlankCharsAround");
                case TOO_SMALL -> LabelsTextFactory.setStringErrorLabelIsTooSmallLabel(passwordErrorLabel, errorLabelFormatter);
                default -> throw new IllegalStateException("Wrong enum value");
            };
        }

        public static InputWithErrorLabelProcess initProcessor(TextInputControl passwordField, Label passwordErrorLabel) {
            var process = initProcessor();
            process.setInputValueToValidate(passwordField);
            process.setErrorLabel(passwordErrorLabel);
            return process;
        }

        public static InputWithErrorLabelProcess initProcessor() {
            var formatters = new HashMap<ErrorVariant, UnaryOperator<String>>();
            formatters.put(TOO_SMALL, (var string) -> string.formatted(5));
            return new Validate.InputWithErrorLabelProcess.Builder()
                    .setValidator(Password::validate)
                    .setErrorLabelInCurrentLanguageSetter(Password::updateLocalizedErrorLabel)
                    .setErrorLabelFormatters(formatters)
                    .build();
        }
    }


    public static class ConfirmPassword {
        /**
         * @return BLANK (only empty symbols or nothing) or TOO_SMALL (len without corner empty symbols is lower than 5),
         * NONE if hasn't
         */
        public static ErrorVariant validate(TextInputControl password, String repeatedPassword) {
            repeatedPassword = repeatedPassword.trim();
            if (repeatedPassword.isEmpty()) {
                return BLANK;
            }
            if (repeatedPassword.length() < 5) {
                return TOO_SMALL;
            }
            if (!repeatedPassword.equals(password.getText().trim())) {
                return NOT_EQUALS_TO;
            }
            return NONE;
        }

        /**
         * @throws IllegalStateException if variant is NONE or not BLANK, TOO_SMALL or NOT_EQUALS_TO
         * @throws IllegalArgumentException if format is null, when needed
         */
        public static void updateLocalizedErrorLabel(Label passwordErrorLabel, ErrorVariant passwordErrorLabelVariant, UnaryOperator<String> errorLabelFormatter) throws IllegalStateException, IllegalArgumentException {
            passwordErrorLabelVariant.checkOnError();
            switch (passwordErrorLabelVariant) {
                case BLANK -> LabelsTextFactory.setInputErrorLabelIsBlankLabel(passwordErrorLabel);
                case TOO_SMALL -> LabelsTextFactory.setStringErrorLabelIsTooSmallLabel(passwordErrorLabel, errorLabelFormatter);
                case NOT_EQUALS_TO -> ElementsLocaleSetter.setLocalizedText(passwordErrorLabel, "confirmPasswordErrorLabelNotEqualsToPassword");
                default -> throw new IllegalStateException("Wrong enum value");
            }
        }

        public static InputWithErrorLabelProcess initProcessor(TextInputControl passwordInput, TextInputControl confirmPasswordField, Label confirmPasswordErrorLabel) {
            var process = initProcessor(passwordInput);
            process.setInputValueToValidate(confirmPasswordField);
            process.setErrorLabel(confirmPasswordErrorLabel);
            return process;
        }

        public static InputWithErrorLabelProcess initProcessor(TextInputControl passwordInput) {
            var formatters = new HashMap<ErrorVariant, UnaryOperator<String>>();
            formatters.put(TOO_SMALL, (var string) -> string.formatted(5) );
            return new Validate.InputWithErrorLabelProcess.Builder()
                    .setValidator((var confirmPassword) -> ConfirmPassword.validate(passwordInput, confirmPassword))
                    .setErrorLabelInCurrentLanguageSetter(ConfirmPassword::updateLocalizedErrorLabel)
                    .setErrorLabelFormatters(formatters)
                    .build();
        }
    }


    public static class SimpleString {
        public static ErrorVariant validateIfBlank(String string) {
            return string.isBlank() ? BLANK : NONE;
        }

        /**
         * @param minLength can be null
         * @param maxLength can be null
         */
        public static ErrorVariant validate(String string, Integer minLength, Integer maxLength) {
            if (minLength != null && string.length() < minLength) {
                return TOO_SMALL;
            }
            if (maxLength != null && string.length() > maxLength) {
                return TOO_BIG;
            }
            return NONE;
        }

        /**
         * @throws IllegalStateException if variant is NONE or not TOO_SMALL or TOO_BIG
         * @throws IllegalArgumentException if format is null, when needed
         */
        public static void updateLocalizedErrorLabel(Label simpleStringErrorLabel, ErrorVariant simpleStringErrorLabelVariant, UnaryOperator<String> errorLabelFormatter) throws IllegalStateException, IllegalArgumentException {
            simpleStringErrorLabelVariant.checkOnError();
            switch (simpleStringErrorLabelVariant) {
                case TOO_SMALL -> LabelsTextFactory.setStringErrorLabelIsTooSmallLabel(simpleStringErrorLabel, errorLabelFormatter);
                case TOO_BIG -> LabelsTextFactory.setStringErrorLabelIsTooBigLabel(simpleStringErrorLabel, errorLabelFormatter);
                default -> throw new IllegalStateException("Wrong enum value");
            }
        }

        public static InputWithErrorLabelProcess initProcessor(TextInputControl simpleStringField, Label confirmPasswordErrorLabel, Integer maxLength) {
            return initProcessor(simpleStringField, confirmPasswordErrorLabel, null, maxLength);
        }

        /**
         * @param minLength null if none
         * @param maxLength null if none
         */
        public static InputWithErrorLabelProcess initProcessor(TextInputControl simpleStringField, Label simpleStringErrorLabel, Integer minLength, Integer maxLength) {
            var process = initProcessor(minLength, maxLength);
            process.setInputValueToValidate(simpleStringField);
            process.setErrorLabel(simpleStringErrorLabel);
            return process;
        }

        /**
         * @param maxLength null if none
         */
        public static InputWithErrorLabelProcess initProcessor(Integer maxLength) {
            return initProcessor(null, maxLength);
        }

        /**
         * @param minLength null if none
         * @param maxLength null if none
         */
        public static InputWithErrorLabelProcess initProcessor(Integer minLength, Integer maxLength) {
            var formatters = new HashMap<ErrorVariant, UnaryOperator<String>>();
            formatters.put(TOO_SMALL, (var string) -> string.formatted(minLength) );
            formatters.put(TOO_BIG, (var string) -> string.formatted(maxLength) );
            return new Validate.InputWithErrorLabelProcess.Builder()
                    .setValidator((var string) -> SimpleString.validate(string, minLength, maxLength))
                    .setErrorLabelInCurrentLanguageSetter(SimpleString::updateLocalizedErrorLabel)
                    .setErrorLabelFormatters(formatters)
                    .build();
        }
    }

    /**
     * @returns labelErrorVariantBeforeCheck if trimmedString is not blank or labelErrorVariantBeforeCheck is not NONE
     * @param validator validates String, returns NONE if ok, variant of error else
     * @throws IllegalArgumentException if labelErrorVariantBeforeCheck is null
     */
    public static ErrorVariant validateIfNone(Function<String, ErrorVariant> validator, String stringToValidate, ErrorVariant labelErrorVariantBeforeCheck) throws IllegalArgumentException {
        if (labelErrorVariantBeforeCheck == null) {
            throw new IllegalArgumentException("Shouldn't be null");
        }
        if (labelErrorVariantBeforeCheck != NONE) {
            return labelErrorVariantBeforeCheck;
        }
        return validator.apply(stringToValidate);
    }

    public static class Numbers {
        public static String numberPattern = "^-?(0|[1-9]\\d*)\\.?\\d*$";
        public static String floatNumberPattern = "^-?(0|[1-9]\\d*)\\.\\d+$";

        /**
         * @param trimmedString is trimmed string
         * @return checks only is number (NOT_EVEN_NUMBER)
         */
        public static ErrorVariant validateNumber(String trimmedString) {
            return Pattern.matches(numberPattern, trimmedString) ? NONE : NOT_EVEN_NUMBER;
        }

        /**
         * @param trimmedString is trimmed string
         * @return checks only is negative number (FLOAT)
         */
        public static ErrorVariant validateNotFloat(String trimmedString) {
            return Pattern.matches(floatNumberPattern, trimmedString) ? FLOAT : NONE;
        }

        /**
         * tries to parse as long
         */
        public static ErrorVariant validateNumberNotFloatOnLongBound(String numberString) {
            try {
                Long.parseLong(numberString);
                return NONE;
            } catch (NumberFormatException e) {
                return TOO_BIG;
            }
        }

        /**
         * tries to parse as integer
         */
        public static ErrorVariant validateNumberNotFloatOnIntegerBound(String numberString) {
            try {
                Integer.parseInt(numberString);
                return NONE;
            } catch (NumberFormatException e) {
                return TOO_BIG;
            }
        }

        /**
         * @throws IllegalStateException if string is not a number
         * @return Satisfies the requirements of Validator.validateIfNone validator func
         */
        public static ErrorVariant validateNumberValueForValidateIfNone(String string, Predicate<Float> errorTester, ErrorVariant errorVariant) throws IllegalStateException {
            try {
                var numberToTest = Float.parseFloat(string);
                if (errorTester.test(numberToTest)) {
                    return errorVariant;
                } else {
                    return NONE;
                }
            } catch (NumberFormatException e) {
                throw new IllegalStateException("Is not a number");
            }
        }

        /**
         * @throws IllegalStateException if it is not a number
         */
        public static ErrorVariant validatePositiveNumber(String string) throws IllegalStateException {
            return validateNumberValueForValidateIfNone(string, (var number) -> number <= 0, NOT_POSITIVE);
        }

        /**
         * @throws IllegalStateException if it is not a number
         */
        public static ErrorVariant validateNotNegativeNumber(String string) throws IllegalStateException {
            return validateNumberValueForValidateIfNone(string, (var number) -> number < 0, NEGATIVE);
        }

        /**
         * @param trimmedString is trimmed string
         */
        public static ErrorVariant validateLongNumber(String trimmedString) {
            var errorVariant = NONE;
            errorVariant = validateIfNone(Numbers::validateNumber, trimmedString, errorVariant);
            errorVariant = validateIfNone(Numbers::validateNotFloat, trimmedString, errorVariant);
            errorVariant = validateIfNone(Numbers::validateNumberNotFloatOnLongBound, trimmedString, errorVariant);
            return errorVariant;
        }


        /**
         * @param trimmedString is trimmed string
         * @return Satisfies the requirements of Validator.validateIfNone validator func
         */
        public static ErrorVariant validateIntegerNumber(String trimmedString) {
            var errorVariant = NONE;
            errorVariant = validateIfNone(Numbers::validateNumber, trimmedString, errorVariant);
            errorVariant = validateIfNone(Numbers::validateNotFloat, trimmedString, errorVariant);
            errorVariant = validateIfNone(Numbers::validateNumberNotFloatOnIntegerBound, trimmedString, errorVariant);
            return errorVariant;
        }

        public static ErrorVariant validatePositiveInteger(String trimmedString) {
            var errorVariant = NONE;
            errorVariant = validateIfNone(Numbers::validateIntegerNumber, trimmedString, errorVariant) ;
            errorVariant = validateIfNone(Numbers::validatePositiveNumber, trimmedString, errorVariant) ;
            return errorVariant;
        }

        public static ErrorVariant validatePositiveLong(String trimmedString) {
            var errorVariant = NONE;
            errorVariant = validateIfNone(Numbers::validateLongNumber, trimmedString, errorVariant) ;
            errorVariant = validateIfNone(Numbers::validatePositiveNumber, trimmedString, errorVariant) ;
            return errorVariant;
        }

        public static ErrorVariant validateNotNegativeFloat(String string) {
            var trimmedString = string.trim();
            var errorVariant = NONE;
            errorVariant = validateIfNone(Numbers::validateNumber, trimmedString, errorVariant);
            errorVariant = validateIfNone(Numbers::validateNotNegativeNumber, trimmedString, errorVariant);
            return errorVariant;
        }

        public static ErrorVariant validateFloatOrDouble(String string) {
            var trimmedString = string.trim();
            var errorVariant = NONE;
            errorVariant = validateIfNone(Numbers::validateNumber, trimmedString, errorVariant);
            return errorVariant;
        }

        public static InputWithErrorLabelProcess initPositiveIntegerProcessor(TextInputControl numberInput, Label numberErrorLabel) {
            return initProcessor(Numbers::validatePositiveInteger, numberInput, numberErrorLabel);
        }

        public static InputWithErrorLabelProcess initLongProcessor(TextInputControl numberInput, Label numberErrorLabel) {
            return initProcessor(Numbers::validateLongNumber, numberInput, numberErrorLabel);
        }

        public static InputWithErrorLabelProcess initPositiveLongProcessor(TextInputControl numberInput, Label numberErrorLabel) {
            return initProcessor(Numbers::validatePositiveLong, numberInput, numberErrorLabel);
        }

        public static InputWithErrorLabelProcess initNotNegativeFloatProcessor(TextInputControl numberInput, Label numberErrorLabel) {
            return initProcessor(Numbers::validateNotNegativeFloat, numberInput, numberErrorLabel);
        }

        public static InputWithErrorLabelProcess initFloatOrDoubleProcessor(TextInputControl numberInput, Label numberErrorLabel) {
            return initProcessor(Numbers::validateFloatOrDouble, numberInput, numberErrorLabel);
        }

        public static InputWithErrorLabelProcess initProcessor(Function<String, ErrorVariant> validator, TextInputControl numberField, Label errorLabel) {
            var process = initProcessor(validator);
            process.setInputValueToValidate(numberField);
            process.setErrorLabel(errorLabel);
            return process;
        }

        public static InputWithErrorLabelProcess initProcessor(Function<String, ErrorVariant> validator) {
            return new Validate.InputWithErrorLabelProcess.Builder()
                    .setValidator(validator)
                    .setErrorLabelInCurrentLanguageSetter(Numbers::updateLocalizedErrorLabel)
                    .build();
        }

        /**
         * @param errorLabelFormatter is always null
         * @throws IllegalStateException if variant is NONE or not BLANK or TOO_SMALL
         * @throws IllegalArgumentException if formatter is null when needed
         */
        public static void updateLocalizedErrorLabel(Label numberErrorLabel, ErrorVariant numberErrorLabelVariant, UnaryOperator<String> errorLabelFormatter) throws IllegalStateException, IllegalArgumentException {
            numberErrorLabelVariant.checkOnError();
            switch (numberErrorLabelVariant) {
                case BLANK -> LabelsTextFactory.setInputErrorLabelIsBlankLabel(numberErrorLabel);
                case NOT_EVEN_NUMBER -> ElementsLocaleSetter.setLocalizedText(numberErrorLabel, "numberErrorLabelNotNumber");
                case FLOAT -> ElementsLocaleSetter.setLocalizedText(numberErrorLabel, "numberErrorLabelIsFloat");
                case NEGATIVE -> ElementsLocaleSetter.setLocalizedText(numberErrorLabel, "numberErrorLabelIsNegative");
                case NOT_POSITIVE -> ElementsLocaleSetter.setLocalizedText(numberErrorLabel, "numberErrorLabelNotPositive");
                case TOO_BIG -> ElementsLocaleSetter.setLocalizedText(numberErrorLabel, "numberErrorLabelTooBig");
                case NOT_EQUALS_TO -> ElementsLocaleSetter.setLocalizedText(numberErrorLabel, "confirmPasswordErrorLabelNotEqualsToPassword");
                default -> throw new IllegalStateException("Wrong enum value");
            };
        }
    }
}
