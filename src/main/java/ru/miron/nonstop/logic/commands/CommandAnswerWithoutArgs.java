package ru.miron.nonstop.logic.commands;

import java.util.Map;

import ru.miron.nonstop.logic.util.json.types.JSONValue;

public class CommandAnswerWithoutArgs {
    private boolean isError;
    private ErrorDescription errorDescription;
    private EnterState enterState;
    private Boolean isSuccess;
    private String state;

    private CommandAnswerWithoutArgs() {}

    /**
     * @throws IllegalArgumentException if root hasn't any required keys or has wrong types on known keys
     */
    public static CommandAnswerWithoutArgs initFromTheRoot(Map<String, JSONValue> root) throws IllegalArgumentException {
        var commandAnswerWithoutArgs = new CommandAnswerWithoutArgs(); 
        commandAnswerWithoutArgs.initErrorStateFromTheRoot(root);
        commandAnswerWithoutArgs.initErrorDescriptionFromTheRootIfHas(root);
        if (commandAnswerWithoutArgs.isError) {
            return commandAnswerWithoutArgs;
        }
        commandAnswerWithoutArgs.initEnterStateFromTheRoot(root);
        commandAnswerWithoutArgs.initSuccessFlagFromTheRootIfHas(root);
        commandAnswerWithoutArgs.initStateFromTheRootIfHas(root);
        return commandAnswerWithoutArgs;
    }

    public void initErrorStateFromTheRoot(Map<String, JSONValue> root) throws IllegalArgumentException {
        var errorFlagJSONValue = root.get("error flag");
        if (errorFlagJSONValue == null) {
            throw new IllegalArgumentException();
        }
        try {
            this.isError = errorFlagJSONValue.asBoolean().value; 
        } catch (ClassCastException e) {
            throw new IllegalArgumentException();
        }
    }

    public void initErrorDescriptionFromTheRootIfHas(Map<String, JSONValue> root) throws IllegalArgumentException {
        var errorDescriptionRootJSONValue = root.get("error");
        if (errorDescriptionRootJSONValue == null) {
            return; // hasn't
        }
        Map<String, JSONValue> errorDescriptionRoot;
        try {
            errorDescriptionRoot = errorDescriptionRootJSONValue.asMap().getValues(); 
        } catch (ClassCastException e) {
            throw new IllegalArgumentException();
        }
        this.errorDescription = ErrorDescription.initFromTheRoot(errorDescriptionRoot);
    }

    public void initEnterStateFromTheRoot(Map<String, JSONValue> root) throws IllegalArgumentException {
        var enterStateJSONValue = root.get("enter state");
        if (enterStateJSONValue == null) {
            throw new IllegalArgumentException();
        }
        try {
            this.enterState = EnterState.initFromString(enterStateJSONValue.asString().getValue()); 
        } catch (ClassCastException e) {
            throw new IllegalArgumentException();
        }
    }

    public void initSuccessFlagFromTheRootIfHas(Map<String, JSONValue> root) throws IllegalArgumentException {
        var successFlagJSONValue = root.get("success flag");
        if (successFlagJSONValue == null) {
            return; // hasn't
        }
        try {
            this.isSuccess = successFlagJSONValue.asBoolean().value; 
        } catch (ClassCastException e) {
            throw new IllegalArgumentException();
        }
    }

    public void initStateFromTheRootIfHas(Map<String, JSONValue> root) throws IllegalArgumentException {
        var stateJSONValue = root.get("state");
        if (stateJSONValue == null) {
            return; // hasn't
        }
        try {
            this.state = stateJSONValue.asString().getValue(); 
        } catch (ClassCastException e) {
            throw new IllegalArgumentException();
        }
    }

    public enum EnterState {
        ENTERED,
        WRONG_LOGIN,
        WRONG_PASSWORD;

        /**
         * @throws IllegalArgumentException if couldn't define EnterState 
         */
        public static EnterState initFromString(String string) throws IllegalArgumentException {
            return switch (string) {
                case "entered" -> ENTERED;
                case "wrong login" -> WRONG_LOGIN;
                case "wrong password" -> WRONG_PASSWORD;
                default -> {
                    throw new IllegalArgumentException();
                }
            };
        }
    }

    public boolean getIsError() {
        return isError;
    }

    public ErrorDescription getErrorDescription() {
        return errorDescription;
    }

    public EnterState getEnterState() {
        return enterState;
    }

    public Boolean getIsSuccess() {
        return isSuccess;
    }

    public String getState() {
        return state;
    }
}
