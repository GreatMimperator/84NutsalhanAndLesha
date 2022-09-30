package ru.miron.nonstop.logic.commands.specificAnswers;

import java.util.Map;

import ru.miron.nonstop.logic.util.json.types.JSONValue;

public class WingspansSumGettingCommandSpecificAnswer {
    private long wingspansSum;

    private WingspansSumGettingCommandSpecificAnswer() {}

    public static WingspansSumGettingCommandSpecificAnswer initFromTheRoot(Map<String, JSONValue> commandSpecificAnswerRoot) throws IllegalArgumentException {
        var wingspansSumJSONValue = commandSpecificAnswerRoot.get("wingspans sum");
        try {
            var specificAnswer = new WingspansSumGettingCommandSpecificAnswer();
            specificAnswer.setWingspansSum(wingspansSumJSONValue.asInteger().getValue());
            return specificAnswer;
        } catch (ClassCastException e) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * @throws IllegalArgumentException if wingspansSum is not positive
     */
    private void setWingspansSum(long wingspansSum) throws IllegalArgumentException {
        if (wingspansSum <= 0) {
            throw new IllegalArgumentException();
        }
        this.wingspansSum = wingspansSum;
    }

    public long getWingspansSum() {
        return wingspansSum;
    }
}
