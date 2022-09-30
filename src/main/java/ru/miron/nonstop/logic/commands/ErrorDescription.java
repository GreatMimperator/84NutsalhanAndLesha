package ru.miron.nonstop.logic.commands;

import java.util.Map;

import ru.miron.nonstop.logic.util.json.types.JSONValue;

public class ErrorDescription {
    private ErrorType errorType;
    private String message;

    /**
     * @throws IllegalArgumentException if identificator is null or cannot define error type  
     */
    public ErrorDescription(String identificator, String message) {
        if (identificator == null) {
            throw new IllegalArgumentException();
        }
        this.errorType = ErrorType.initFromString(identificator);
        this.message = message;
    }

    /**
     * @throws IllegalArgumentException if root hasn't any required keys or has wrong types on known keys
     */
    public static ErrorDescription initFromTheRoot(Map<String, JSONValue> root) throws IllegalArgumentException {
        var errorIdentificatorJSONValue = root.get("identificator");
        var errorMessageJSONValue = root.get("message"); 
        if (errorIdentificatorJSONValue == null) {
            throw new IllegalArgumentException();
        }
        try {
            var errorIdentificator = errorIdentificatorJSONValue.asString().getValue(); 
            String errorMessage = null;
            if (errorMessageJSONValue != null) {
                errorMessage = errorIdentificatorJSONValue.asString().getValue();
            }
            return new ErrorDescription(errorIdentificator, errorMessage);
        } catch (ClassCastException e) {
            throw new IllegalArgumentException();
        }
    }

    public static enum ErrorType {
        KEY_NOT_FOUND,
        KEY_VALUE_HAS_ILLEGAL_TYPE,
        KEY_VALUE_IS_OUT_OF_BOUNDS,
        SERVER_INTERNAL_ERROR;

        /**
         * @throws IllegalArgumentException if couldn't define ErrorType
         */
        public static ErrorType initFromString(String string) throws IllegalArgumentException {
            return switch (string) {
                case "key not found" -> KEY_NOT_FOUND;
                case "key value has illegal type" -> KEY_VALUE_HAS_ILLEGAL_TYPE;
                case "key value is out of bounds" -> KEY_VALUE_IS_OUT_OF_BOUNDS;
                case "server internal error" -> SERVER_INTERNAL_ERROR;
                default -> {
                    throw new IllegalArgumentException();
                }
            };
        }
    }

    public ErrorType getErrorType() {
        return errorType;
    }

    public String getMessage() {
        return message;
    }
}
