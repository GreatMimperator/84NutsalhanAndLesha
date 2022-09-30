package ru.miron.nonstop.logic.commands.specificAnswers;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;

import ru.miron.nonstop.givenClasses.DragonWithKeyAndOwner;
import ru.miron.nonstop.logic.util.json.types.JSONValue;

public class DragonsGettingCommandSpecificAnswer {
    private List<DragonWithKeyAndOwner> dragons;

    private DragonsGettingCommandSpecificAnswer() {}

    public static DragonsGettingCommandSpecificAnswer initFromTheRoot(Map<String, JSONValue> commandSpecificAnswerRoot) throws IllegalArgumentException {
        var dragonsJSONValue = commandSpecificAnswerRoot.get("dragons");
        List<JSONValue> dragonsAsJSONValues;
        try {
            dragonsAsJSONValues = dragonsJSONValue.asArray().getValues();
        } catch (ClassCastException e) {
            throw new IllegalArgumentException();
        }
        var specificAnswer = new DragonsGettingCommandSpecificAnswer();
        specificAnswer.dragons = new ArrayList<>(dragonsAsJSONValues.size()); 
        try {
            for (var dragonAsJSONValue : dragonsAsJSONValues) {
                specificAnswer.dragons.add(DragonWithKeyAndOwner.initFromTheRoot(dragonAsJSONValue.asMap().getValues()));
            }
        } catch (ClassCastException e) {
            throw new IllegalArgumentException();
        }
        return specificAnswer;
    }

    public List<DragonWithKeyAndOwner> getDragons() {
        return dragons;
    }
}
