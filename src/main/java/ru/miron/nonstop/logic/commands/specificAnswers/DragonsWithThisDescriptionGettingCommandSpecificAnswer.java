package ru.miron.nonstop.logic.commands.specificAnswers;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;

import ru.miron.nonstop.givenClasses.DragonWithKeyAndOwner;
import ru.miron.nonstop.logic.util.json.types.JSONValue;

public class DragonsWithThisDescriptionGettingCommandSpecificAnswer {
    private List<DragonWithKeyAndOwner> dragons;

    private DragonsWithThisDescriptionGettingCommandSpecificAnswer() {}

    public static DragonsWithThisDescriptionGettingCommandSpecificAnswer initFromTheRoot(Map<String, JSONValue> commandSpecificAnswerRoot) throws IllegalArgumentException {
        var dragonsJSONValue = commandSpecificAnswerRoot.get("dragons"); // todo: обобщить?
        List<JSONValue> dragonsAsJSONValues;
        try {
            dragonsAsJSONValues = dragonsJSONValue.asArray().getValues();
        } catch (ClassCastException e) {
            throw new IllegalArgumentException();
        }
        var specificAnswer = new DragonsWithThisDescriptionGettingCommandSpecificAnswer();
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
