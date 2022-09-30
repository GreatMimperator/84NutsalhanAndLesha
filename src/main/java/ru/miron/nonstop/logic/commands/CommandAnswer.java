package ru.miron.nonstop.logic.commands;

import java.util.Map;

import ru.miron.nonstop.logic.commands.specificAnswers.ClearDragonsCommandSpecificAnswer;
import ru.miron.nonstop.logic.commands.specificAnswers.DragonsInfoCommandSpecificAnswer;
import ru.miron.nonstop.logic.commands.specificAnswers.DragonsGettingCommandSpecificAnswer;
import ru.miron.nonstop.logic.commands.specificAnswers.DragonsWithThisDescriptionGettingCommandSpecificAnswer;
import ru.miron.nonstop.logic.commands.specificAnswers.UniqueNumbersOfTreasuresGettingCommandSpecificAnswer;
import ru.miron.nonstop.logic.commands.specificAnswers.WingspansSumGettingCommandSpecificAnswer;
import ru.miron.nonstop.logic.commands.specificAnswers.RemoveGreaterOrLowerDragonsCommandSpecificAnswer;
import ru.miron.nonstop.logic.util.json.types.JSONValue;

public class CommandAnswer {
    private CommandAnswerWithoutArgs commandAnswerWithoutArgs;
    private Object commandSpecificAnswerObj;

    private CommandAnswer() {}

    /**
     * @throws IllegalArgumentException if root hasn't any required keys or has wrong types on known keys
     */
    public static CommandAnswer initFromTheRoot(CommandName commandName, Map<String, JSONValue> root) throws IllegalArgumentException {
        var commandAnswer = new CommandAnswer();
        commandAnswer.initCommandAnswerWithoutArgsFromTheRoot(root);
        if (commandAnswer.getCommandAnswerWithoutArgs().getIsError()) {
            return commandAnswer;
        }
        commandAnswer.initCommandSpecificAnswerObjFromTheRootIfShouldHave(commandName, root);
        return commandAnswer;
    }

    /**
     * @throws IllegalArgumentException if root hasn't any required keys or has wrong types on known keys
     */
    public void initCommandAnswerWithoutArgsFromTheRoot(Map<String, JSONValue> root) throws IllegalArgumentException {
        commandAnswerWithoutArgs = CommandAnswerWithoutArgs.initFromTheRoot(root);
    }

    /**
     * @throws IllegalArgumentException if root hasn't any required keys or has wrong types on known keys
     */
    public void initCommandSpecificAnswerObjFromTheRootIfShouldHave(CommandName commandName, Map<String, JSONValue> root) throws IllegalArgumentException {
        Map<String, JSONValue> commandSpecificAnswerRoot;
        try {
            var commandSpecificAnswerRootJSONValue = root.get("command specific answer");
            if (commandSpecificAnswerRootJSONValue != null) {
                commandSpecificAnswerRoot = commandSpecificAnswerRootJSONValue.asMap().getValues();
            } else {
                commandSpecificAnswerRoot = null;
            }
        } catch (ClassCastException e) {
            throw new IllegalArgumentException();
        }
        this.commandSpecificAnswerObj = switch (commandName) {
            case REGISTER,
                    SIGN_IN,
                    INSERT_DRAGON,
                    UPDATE_DRAGON,
                    REMOVE_DRAGON_BY_KEY,
                    REPLACE_IF_DRAGON_IS_GREATER -> 
                null;
            case GET_DRAGONS -> 
                DragonsGettingCommandSpecificAnswer.initFromTheRoot(commandSpecificAnswerRoot);
            case DRAGONS_INFO ->
                DragonsInfoCommandSpecificAnswer.initFromTheRoot(commandSpecificAnswerRoot); 
            case CLEAR_DRAGONS -> 
                ClearDragonsCommandSpecificAnswer.initFromTheRoot(commandSpecificAnswerRoot);
            case REMOVE_GREATER_DRAGONS, REMOVE_LOWER_DRAGONS ->
                RemoveGreaterOrLowerDragonsCommandSpecificAnswer.initFromTheRoot(commandSpecificAnswerRoot);
            case GET_WINGSPANS_SUM ->
                WingspansSumGettingCommandSpecificAnswer.initFromTheRoot(commandSpecificAnswerRoot);
            case GET_DRAGONS_WITH_THIS_DESCRIPTION ->
                DragonsWithThisDescriptionGettingCommandSpecificAnswer.initFromTheRoot(commandSpecificAnswerRoot);
            case GET_UNIQUE_NUMBERS_OF_TREASURES ->
                UniqueNumbersOfTreasuresGettingCommandSpecificAnswer.initFromTheRoot(commandSpecificAnswerRoot);
            case HELP, EXIT -> 
                throw new IllegalStateException();
        };
    }

    public CommandAnswerWithoutArgs getCommandAnswerWithoutArgs() {
        return commandAnswerWithoutArgs;
    }

    public Object getCommandSpecificAnswerObj() {
        return commandSpecificAnswerObj;
    }
}
