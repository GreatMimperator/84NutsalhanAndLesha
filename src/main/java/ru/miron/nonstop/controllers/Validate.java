package ru.miron.nonstop.controllers;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import ru.miron.nonstop.locales.AppLocaleManager;
import ru.miron.nonstop.locales.ElementsLocaleSetter;
import ru.miron.nonstop.locales.entities.LabelText;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;
import static ru.miron.nonstop.controllers.Validate.LabelErrorVariant.*;
import static ru.miron.nonstop.locales.entities.LabelText.TextType.LABEL_NAME;
import static ru.miron.nonstop.locales.entities.LabelText.TextType.PLAIN_TEXT;

public class Validate {
    public enum LabelErrorVariant { // todo: do
        NONE,
        BLANK,
        TOO_BIG,
        TOO_SMALL,
        NOT_EQUALS_TO,
        NOT_EVEN_NUMBER,
        NOT_POSITIVE,
        NEGATIVE,
        FLOAT;
    }

    @FunctionalInterface
    public interface ErrorLabelInCurrentLanguageSetter {
        /**
         * @param errorLabelFormatter can be null if nothing to format
         * @throws IllegalStateException if errorVariantToSet is NONE
         * @throws IllegalArgumentException if errorVariantToSet has illegal variant
         */
        void setErrorLabelInCurrentLanguage(Label errorLabel, LabelErrorVariant errorVariantToSet, UnaryOperator<String> errorLabelFormatter) throws IllegalStateException, IllegalArgumentException;
    }

    public static void setLabelVisibility(Label errorLabel, LabelErrorVariant labelErrorVariant) {
        if (labelErrorVariant != NONE) {
            errorLabel.setVisible(true);
        } else {
            errorLabel.setVisible(false);
        }
    }

    /**
     * @throws IllegalStateException if labelErrorVariant is NONE
     */
    public static void checkErrorLabelOnVariantContain(LabelErrorVariant labelErrorVariant) throws IllegalStateException {
        if (labelErrorVariant == NONE) {
            throw new IllegalStateException("error label variant shouldn't be NONE");
        }
    }

    public static class ValidatorAndLocaledErrorLabelSetterWithFormat {
        private Function<String, LabelErrorVariant> validator;
        private ErrorLabelInCurrentLanguageSetter errorLabelInCurrentLanguageSetter;
        private UnaryOperator<String> errorLabelFormatter;

        public ValidatorAndLocaledErrorLabelSetterWithFormat(
                Function<String, LabelErrorVariant> validator,
                ErrorLabelInCurrentLanguageSetter errorLabelInCurrentLanguageSetter,
                UnaryOperator<String> errorLabelFormatter) {
            this.validator = validator;
            this.errorLabelInCurrentLanguageSetter = errorLabelInCurrentLanguageSetter;
            this.errorLabelFormatter = errorLabelFormatter;
        }

        /**
         * @param errorLabelFormatter can be null if nothing to format
         * @throws IllegalArgumentException if validator returned illegal value
         * @throws IllegalStateException if labelErrorVariant is NONE
         */
        public LabelErrorVariant validateAndSetErrorLabelVisibility(TextField fieldValueToValidate, Label errorLabel) {
            return Validate.validateAndSetErrorLabelVisibility(fieldValueToValidate, errorLabel, validator, errorLabelInCurrentLanguageSetter, errorLabelFormatter);
        }

        public LabelErrorVariant validateAndSetErrorLabelVisibility(String valueToValidate, Label errorLabel) {
            return Validate.validateAndSetErrorLabelVisibility(valueToValidate, errorLabel, validator, errorLabelInCurrentLanguageSetter, errorLabelFormatter);
        }
    }

    public static class FieldValidateProcess {
        private TextField fieldValueToValidate = null;
        private Label errorLabel = null;
        private Function<String, LabelErrorVariant> validator;
        private ErrorLabelInCurrentLanguageSetter errorLabelInCurrentLanguageSetter;
        private UnaryOperator<String> errorLabelFormatter = null;

        /**
         * @throws IllegalArgumentException if any (except of errorLabelFormatter)
         */
        private FieldValidateProcess(
                Function<String, LabelErrorVariant> validator,
                ErrorLabelInCurrentLanguageSetter errorLabelInCurrentLanguageSetter,
                UnaryOperator<String> errorLabelFormatter) throws IllegalArgumentException {
           if (validator == null || errorLabelInCurrentLanguageSetter == null) {
               throw new IllegalArgumentException();
           }
           this.validator = validator;
           this.errorLabelInCurrentLanguageSetter = errorLabelInCurrentLanguageSetter;
           this.errorLabelFormatter = errorLabelFormatter;
        }

        /**
         * @throws IllegalArgumentException if validator returned illegal value or required to set fields (fieldValueToValidate, errorLabel) is null
         * @throws IllegalStateException if labelErrorVariant is NONE
         */
        public LabelErrorVariant validateAndSetErrorLabelVisibility() throws IllegalArgumentException, IllegalStateException {
            if (fieldValueToValidate == null || errorLabel == null) {
                throw new IllegalArgumentException("Required fields is null");
            }
            return Validate.validateAndSetErrorLabelVisibility(
                    fieldValueToValidate,
                    errorLabel,
                    validator,
                    errorLabelInCurrentLanguageSetter,
                    errorLabelFormatter);
        }

        public void setErrorLabelInCurrentLanguage(LabelErrorVariant labelErrorVariant) {
            errorLabelInCurrentLanguageSetter.setErrorLabelInCurrentLanguage(errorLabel, labelErrorVariant, errorLabelFormatter);
        }

        public void setFieldValueToValidate(TextField fieldValueToValidate) {
            this.fieldValueToValidate = fieldValueToValidate;
        }

        public void setErrorLabel(Label errorLabel) {
            this.errorLabel = errorLabel;
        }

        public static class FieldValidateProcessBuilder {
            private Function<String, LabelErrorVariant> validator;
            private ErrorLabelInCurrentLanguageSetter errorLabelInCurrentLanguageSetter;
            private UnaryOperator<String> errorLabelFormatter;

            public FieldValidateProcessBuilder() {
                validator = null;
                errorLabelInCurrentLanguageSetter = null;
                errorLabelFormatter = null;
            }

            /**
             * @throws IllegalStateException if either of validator or errorLabelInCurrentLanguageSetter fields is null
             */
            public FieldValidateProcess build() throws IllegalStateException {
                if (validator == null || errorLabelInCurrentLanguageSetter == null) {
                    throw new IllegalStateException("Any of required fields is null");
                }
                return new FieldValidateProcess(validator, errorLabelInCurrentLanguageSetter, errorLabelFormatter);
            }

            public FieldValidateProcessBuilder setValidator(Function<String, LabelErrorVariant> validator) {
                this.validator = validator;
                return this;
            }

            public FieldValidateProcessBuilder setErrorLabelInCurrentLanguageSetter(ErrorLabelInCurrentLanguageSetter errorLabelInCurrentLanguageSetter) {
                this.errorLabelInCurrentLanguageSetter = errorLabelInCurrentLanguageSetter;
                return this;
            }

            public FieldValidateProcessBuilder setErrorLabelFormatter(UnaryOperator<String> errorLabelFormatter) {
                this.errorLabelFormatter = errorLabelFormatter;
                return this;
            }
        }

    }

    /**
     * @param errorLabelFormatter can be null if nothing to format
     * @throws IllegalArgumentException if validator returned illegal value
     * @throws IllegalStateException if labelErrorVariant is NONE
     */
    public static LabelErrorVariant validateAndSetErrorLabelVisibility(
            TextField fieldValueToValidate,
            Label errorLabel,
            Function<String, LabelErrorVariant> validator,
            ErrorLabelInCurrentLanguageSetter errorLabelInCurrentLanguageSetter,
            UnaryOperator<String> errorLabelFormatter) throws IllegalArgumentException, IllegalStateException {
        return validateAndSetErrorLabelVisibility(
                fieldValueToValidate.getText(),
                errorLabel,
                validator,
                errorLabelInCurrentLanguageSetter,
                errorLabelFormatter);
    }

    /**
     * @param errorLabelFormatter can be null if nothing to format
     * @throws IllegalArgumentException if validator returned illegal value
     */
    public static LabelErrorVariant validateAndSetErrorLabelVisibility(
            String valueToValidate,
            Label errorLabel,
            Function<String, LabelErrorVariant> validator,
            ErrorLabelInCurrentLanguageSetter errorLabelInCurrentLanguageSetter,
            UnaryOperator<String> errorLabelFormatter) {
        LabelErrorVariant errorVariant = validator.apply(valueToValidate);
        setErrorLabelInCurrentLanguageIfHasVariant(errorLabel, errorVariant, errorLabelInCurrentLanguageSetter, errorLabelFormatter);
        setLabelVisibility(errorLabel, errorVariant);
        return errorVariant;
    }

    /**
     * @param errorLabelFormatter can be null if nothing to format
     * @throws IllegalArgumentException if variant has illegal value
     */
    public static void setErrorLabelInCurrentLanguageIfHasVariant(
            Label loginErrorLabel,
            LabelErrorVariant loginErrorLabelVariant,
            ErrorLabelInCurrentLanguageSetter errorLabelInCurrentLanguageSetter,
            UnaryOperator<String> errorLabelFormatter) throws IllegalArgumentException {
        if (loginErrorLabelVariant != NONE) {
            errorLabelInCurrentLanguageSetter.setErrorLabelInCurrentLanguage(loginErrorLabel, loginErrorLabelVariant, errorLabelFormatter);
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
        public static void setFieldErrorLabelIsBlankLabel(Label errorLabel) {
            ElementsLocaleSetter.setLabelTextInCurrentLanguage(errorLabel, getFieldErrorLabelIsBlank());
        }

        public static LabelText getFieldErrorLabelIsBlank() {
            return new LabelText("fieldErrorLabelIsBlank", LABEL_NAME);
        }


        public static void setStringErrorLabelIsTooBigLabel(Label errorLabel, UnaryOperator<String> errorLabelFormatter) {
            ElementsLocaleSetter.setLabelTextInCurrentLanguage(errorLabel, getStringErrorLabelTooBig(errorLabelFormatter));
        }

        public static LabelText getStringErrorLabelTooBig(UnaryOperator<String> errorLabelFormatter) {
            String notFormattedString = AppLocaleManager.getTextByLabel("stringErrorLabelTooBig");
            String formattedString = errorLabelFormatter.apply(notFormattedString);
            return new LabelText(formattedString, PLAIN_TEXT);
        }

        public static void setStringErrorLabelIsTooSmallLabel(Label errorLabel, UnaryOperator<String> errorLabelFormatter) {
            ElementsLocaleSetter.setLabelTextInCurrentLanguage(errorLabel, getStringErrorLabelTooSmall(errorLabelFormatter));
        }

        public static LabelText getStringErrorLabelTooSmall(UnaryOperator<String> errorLabelFormatter) {
            String notFormattedString = AppLocaleManager.getTextByLabel("stringErrorLabelTooSmall");
            String formattedString = errorLabelFormatter.apply(notFormattedString);
            return new LabelText(formattedString, PLAIN_TEXT);
        }
    }

    public static class Login {
        /**
         * @return BLANK (only empty symbols or nothing), TOO_BIG (len without corner empty symbols is over than 80) or TOO_SMALL (lower than 5),
         * NONE if hasn't
         */
        public static LabelErrorVariant validate(String login) {
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
         * @throws IllegalStateException if variant is NONE
         * @throws IllegalArgumentException if variant is not BLANK, TOO_BIG or TOO_SMALL
         */
        public static void setErrorLabelInCurrentLanguage(Label loginErrorLabel, LabelErrorVariant loginErrorVariantToSet, UnaryOperator<String> errorLabelFormatter) throws IllegalStateException, IllegalArgumentException {
            Validate.checkErrorLabelOnVariantContain(loginErrorVariantToSet);
            switch (loginErrorVariantToSet) {
                case BLANK -> LabelsTextFactory.setFieldErrorLabelIsBlankLabel(loginErrorLabel);
                case TOO_BIG -> LabelsTextFactory.setStringErrorLabelIsTooBigLabel(loginErrorLabel, errorLabelFormatter);
                case TOO_SMALL -> LabelsTextFactory.setStringErrorLabelIsTooSmallLabel(loginErrorLabel, errorLabelFormatter);
                default -> throw new IllegalArgumentException("Wrong enum value");
            }
        }
    }


    public static class Password {
        /**
         * @return BLANK (only empty symbols or nothing) or TOO_SMALL (len without corner empty symbols is lower than 5),
         * NONE if hasn't
         */
        public static LabelErrorVariant validate(String password) {
            password = password.trim();
            if (password.isEmpty()) {
                return BLANK;
            }
            if (password.length() < 5) {
                return TOO_SMALL;
            }
            return NONE;
        }

        /**
         * @throws IllegalStateException if variant is NONE
         * @throws IllegalArgumentException if variant is not BLANK or TOO_SMALL
         */
        public static void setErrorLabelInCurrentLanguage(Label passwordErrorLabel, LabelErrorVariant passwordErrorLabelVariant, UnaryOperator<String> errorLabelFormatter) throws IllegalStateException, IllegalArgumentException  {
            Validate.checkErrorLabelOnVariantContain(passwordErrorLabelVariant);
            switch (passwordErrorLabelVariant) {
                case BLANK -> LabelsTextFactory.setFieldErrorLabelIsBlankLabel(passwordErrorLabel);
                case TOO_SMALL -> LabelsTextFactory.setStringErrorLabelIsTooSmallLabel(passwordErrorLabel, errorLabelFormatter);
                default -> throw new IllegalArgumentException("Wrong enum value");
            };
        }
    }


    public static class ConfirmPassword {
        /**
         * @return BLANK (only empty symbols or nothing) or TOO_SMALL (len without corner empty symbols is lower than 5),
         * NONE if hasn't
         */
        public static LabelErrorVariant validate(String repeatedPassword) {
            repeatedPassword = repeatedPassword.trim();
            if (repeatedPassword.isEmpty()) {
                return BLANK;
            }
            if (repeatedPassword.length() < 5) {
                return TOO_SMALL;
            }
            return NONE;
        }

        /**
         * @throws IllegalStateException if variant is NONE
         * @throws IllegalArgumentException if variant is not BLANK or TOO_SMALL
         */
        public static void setErrorLabelInCurrentLanguage(Label passwordErrorLabel, LabelErrorVariant passwordErrorLabelVariant, UnaryOperator<String> errorLabelFormatter) throws IllegalStateException, IllegalArgumentException {
            Validate.checkErrorLabelOnVariantContain(passwordErrorLabelVariant);
            switch (passwordErrorLabelVariant) {
                case BLANK -> LabelsTextFactory.setFieldErrorLabelIsBlankLabel(passwordErrorLabel);
                case TOO_SMALL -> LabelsTextFactory.setStringErrorLabelIsTooSmallLabel(passwordErrorLabel, errorLabelFormatter);
                case NOT_EQUALS_TO -> ElementsLocaleSetter.setLabelTextInCurrentLanguage(passwordErrorLabel, "confirmPasswordErrorLabelNotEqualsToPassword");
                default -> throw new IllegalArgumentException("Wrong enum value");
            };
        }
    }


    public static class SimpleString {
        public static LabelErrorVariant validateIfBlank(String string) {
            return string.isBlank() ? BLANK : NONE;
        }

        /**
         * @throws IllegalStateException if variant is NONE
         * @throws IllegalArgumentException if variant is not BLANK, TOO_SMALL or TOO_BIG
         */
        public static void setErrorLabelInCurrentLanguage(Label simpleStringErrorLabel, LabelErrorVariant simpleStringErrorLabelVariant, UnaryOperator<String> errorLabelFormatter) throws IllegalStateException, IllegalArgumentException {
            Validate.checkErrorLabelOnVariantContain(simpleStringErrorLabelVariant);
            switch (simpleStringErrorLabelVariant) {
                case BLANK -> LabelsTextFactory.setFieldErrorLabelIsBlankLabel(simpleStringErrorLabel);
                case TOO_BIG -> LabelsTextFactory.setStringErrorLabelIsTooBigLabel(simpleStringErrorLabel, errorLabelFormatter);
                case TOO_SMALL -> LabelsTextFactory.setStringErrorLabelIsTooSmallLabel(simpleStringErrorLabel, errorLabelFormatter);
                default -> throw new IllegalArgumentException("Wrong enum value");
            }
        }
    }

    /**
     * @returns labelErrorVariantBeforeCheck if trimmedString is not blank or labelErrorVariantBeforeCheck is not NONE
     * @param validator validates String, returns NONE if ok, variant of error else
     * @throws IllegalArgumentException if labelErrorVariantBeforeCheck is null
     */
    public static LabelErrorVariant validateIfNone(Function<String, LabelErrorVariant> validator, String stringToValidate, LabelErrorVariant labelErrorVariantBeforeCheck) throws IllegalArgumentException {
        if (labelErrorVariantBeforeCheck == null) {
            throw new IllegalArgumentException("Shouldn't be null");
        }
        if (labelErrorVariantBeforeCheck != NONE) {
            return labelErrorVariantBeforeCheck;
        }
        return validator.apply(stringToValidate);
    }

    public static class Numbers {
        public static String numberPattern = "^-?(0|[1-9]\\d+)\\.?\\d*$";
        public static String floatNumberPattern = "^-?(0|[1-9]\\d+)\\.\\d+$";

        /**
         * @param trimmedString is trimmed string
         * @return checks only is number (NOT_EVEN_NUMBER)
         */
        public static LabelErrorVariant validateNumber(String trimmedString) {
            return Pattern.matches(numberPattern, trimmedString) ? NONE : NOT_EVEN_NUMBER;
        }

        /**
         * @param trimmedString is trimmed string
         * @return checks only is negative number (FLOAT)
         */
        public static LabelErrorVariant validateNotFloat(String trimmedString) {
            return Pattern.matches(floatNumberPattern, trimmedString) ? FLOAT : NONE;
        }

        /**
         * tries to parse as long
         */
        public static LabelErrorVariant validateNumberNotFloatOnLongBound(String numberString) {
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
        public static LabelErrorVariant validateNumberNotFloatOnIntegerBound(String numberString) {
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
        public static LabelErrorVariant validateNumberValueForValidateIfNone(String string, Predicate<Float> errorTester, LabelErrorVariant errorVariant) throws IllegalStateException {
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
        public static LabelErrorVariant validatePositiveNumber(String string) throws IllegalStateException {
            return validateNumberValueForValidateIfNone(string, (var number) -> number <= 0, NOT_POSITIVE);
        }

        /**
         * @throws IllegalStateException if it is not a number
         */
        public static LabelErrorVariant validateNotNegativeNumber(String string) throws IllegalStateException {
            return validateNumberValueForValidateIfNone(string, (var number) -> number < 0, NEGATIVE);
        }

        /**
         * @param trimmedString is trimmed string
         */
        public static LabelErrorVariant validateLongNumber(String trimmedString) {
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
        public static LabelErrorVariant validateIntegerNumber(String trimmedString) {
            var errorVariant = NONE;
            errorVariant = validateIfNone(Numbers::validateNumber, trimmedString, errorVariant);
            errorVariant = validateIfNone(Numbers::validateNotFloat, trimmedString, errorVariant);
            errorVariant = validateIfNone(Numbers::validateNumberNotFloatOnIntegerBound, trimmedString, errorVariant);
            return errorVariant;
        }

        public static LabelErrorVariant validatePositiveInteger(String trimmedString) {
            var errorVariant = NONE;
            errorVariant = validateIfNone(Numbers::validateIntegerNumber, trimmedString, errorVariant) ;
            errorVariant = validateIfNone(Numbers::validatePositiveNumber, trimmedString, errorVariant) ;
            return errorVariant;
        }

        public static LabelErrorVariant validatePositiveLong(String trimmedString) {
            var errorVariant = NONE;
            errorVariant = validateIfNone(Numbers::validateLongNumber, trimmedString, errorVariant) ;
            errorVariant = validateIfNone(Numbers::validatePositiveNumber, trimmedString, errorVariant) ;
            return errorVariant;
        }

        public static LabelErrorVariant validateNotNegativeFloat(String string) {
            var trimmedString = string.trim();
            var errorVariant = NONE;
            errorVariant = validateIfNone(Numbers::validateNumber, trimmedString, errorVariant);
            errorVariant = validateIfNone(Numbers::validateNotNegativeNumber, trimmedString, errorVariant);
            return errorVariant;
        }

        public static LabelErrorVariant validateFloatOrDouble(String string) {
            var trimmedString = string.trim();
            var errorVariant = NONE;
            errorVariant = validateIfNone(Numbers::validateNumber, trimmedString, errorVariant);
            return errorVariant;
        }

        /**
         * @throws IllegalStateException if variant is NONE
         * @throws IllegalArgumentException if variant is not BLANK or TOO_SMALL
         */
        public static void setErrorLabelInCurrentLanguage(Label numberErrorLabel, LabelErrorVariant numberErrorLabelVariant, UnaryOperator<String> errorLabelFormatter) throws IllegalStateException, IllegalArgumentException {
            Validate.checkErrorLabelOnVariantContain(numberErrorLabelVariant);
            switch (numberErrorLabelVariant) {
                case BLANK -> LabelsTextFactory.setFieldErrorLabelIsBlankLabel(numberErrorLabel);
                case NOT_EVEN_NUMBER -> ElementsLocaleSetter.setLabelTextInCurrentLanguage(numberErrorLabel, "numberErrorLabelNotNumber");
                case FLOAT -> ElementsLocaleSetter.setLabelTextInCurrentLanguage(numberErrorLabel, "numberErrorLabelIsFloat");
                case NEGATIVE -> ElementsLocaleSetter.setLabelTextInCurrentLanguage(numberErrorLabel, "numberErrorLabelIsNegative");
                case NOT_POSITIVE -> ElementsLocaleSetter.setLabelTextInCurrentLanguage(numberErrorLabel, "numberErrorLabelNotPositive");
                case TOO_BIG -> LabelsTextFactory.setStringErrorLabelIsTooBigLabel(numberErrorLabel, errorLabelFormatter);
                case NOT_EQUALS_TO -> ElementsLocaleSetter.setLabelTextInCurrentLanguage(numberErrorLabel, "confirmPasswordErrorLabelNotEqualsToPassword");
                default -> throw new IllegalArgumentException("Wrong enum value");
            };
        }
    }
}
