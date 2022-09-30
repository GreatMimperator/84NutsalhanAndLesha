package ru.miron.nonstop.logic.commands.specificAnswers;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;

import ru.miron.nonstop.givenClasses.DragonCave;
import ru.miron.nonstop.logic.util.json.types.JSONValue;

public class UniqueNumbersOfTreasuresGettingCommandSpecificAnswer {
    private List<DragonCave> dragonCaves;

    private UniqueNumbersOfTreasuresGettingCommandSpecificAnswer() {}

    public static UniqueNumbersOfTreasuresGettingCommandSpecificAnswer initFromTheRoot(Map<String, JSONValue> commandSpecificAnswerRoot) throws IllegalArgumentException {
        var uniqueNumbersOfTreasuresJSONValue = commandSpecificAnswerRoot.get("unique numbers of treasures");
        List<JSONValue> uniqueNumbersOfTreasuresAsJSONValues;
        try {
            uniqueNumbersOfTreasuresAsJSONValues = uniqueNumbersOfTreasuresJSONValue.asArray().getValues();
        } catch (ClassCastException e) {
            throw new IllegalArgumentException();
        }
        var specificAnswer = new UniqueNumbersOfTreasuresGettingCommandSpecificAnswer();
        specificAnswer.dragonCaves = new ArrayList<>(uniqueNumbersOfTreasuresAsJSONValues.size()); 
        try {
            for (var uniqueNumbersOfTreasuresAsJSONValue : uniqueNumbersOfTreasuresAsJSONValues) {
                var dragonCave = new DragonCave((float) uniqueNumbersOfTreasuresAsJSONValue.asDouble().getValue());
                specificAnswer.dragonCaves.add(dragonCave);
            }
        } catch (ClassCastException e) {
            throw new IllegalArgumentException();
        }
        return specificAnswer;
    }

    public List<DragonCave> getDragonCaves() {
        return dragonCaves;
    }
}
