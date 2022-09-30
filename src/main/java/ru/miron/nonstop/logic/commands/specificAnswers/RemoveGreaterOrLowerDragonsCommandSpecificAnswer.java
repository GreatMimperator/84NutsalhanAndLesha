package ru.miron.nonstop.logic.commands.specificAnswers;

import java.util.Map;

import ru.miron.nonstop.logic.util.json.types.JSONValue;

public class RemoveGreaterOrLowerDragonsCommandSpecificAnswer {
    private long removedCount;

    private RemoveGreaterOrLowerDragonsCommandSpecificAnswer() {}

    public static RemoveGreaterOrLowerDragonsCommandSpecificAnswer initFromTheRoot(Map<String, JSONValue> commandSpecificAnswerRoot) throws IllegalArgumentException {
        var removedCountJSONValue = commandSpecificAnswerRoot.get("removed count");
        try {
            var specificAnswer = new RemoveGreaterOrLowerDragonsCommandSpecificAnswer();
            specificAnswer.setRemovedCount(removedCountJSONValue.asInteger().getValue());
            return specificAnswer ;
        } catch (ClassCastException e) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * @throws IllegalArgumentException if removedCount is not positive
     */
    private void setRemovedCount(long removedCount) throws IllegalArgumentException {
        if (removedCount < 0) {
            throw new IllegalArgumentException();
        }
        this.removedCount = removedCount;
    }

    public long getRemovedCount() {
        return removedCount;
    }
}
