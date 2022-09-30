package ru.miron.nonstop.logic.commands.specificAnswers;

import java.util.Map;

import ru.miron.nonstop.logic.util.json.types.JSONValue;

public class ClearDragonsCommandSpecificAnswer {
    private long clearedCount;

    private ClearDragonsCommandSpecificAnswer() {}

    public static ClearDragonsCommandSpecificAnswer initFromTheRoot(Map<String, JSONValue> commandSpecificAnswerRoot) throws IllegalArgumentException {
        var clearedCountJSONValue = commandSpecificAnswerRoot.get("cleared count");
        try {
            var specificAnswer = new ClearDragonsCommandSpecificAnswer();
            specificAnswer.setClearedCount(clearedCountJSONValue.asInteger().getValue());
            return specificAnswer;
        } catch (ClassCastException e) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * @throws IllegalArgumentException if clearedCount is not positive
     */
    private void setClearedCount(long clearedCount) throws IllegalArgumentException {
        if (clearedCount < 0) {
            throw new IllegalArgumentException();
        }
        this.clearedCount = clearedCount;
    }

    public long getClearedCount() {
        return clearedCount;
    }
}
