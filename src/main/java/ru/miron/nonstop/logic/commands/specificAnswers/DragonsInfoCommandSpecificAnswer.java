package ru.miron.nonstop.logic.commands.specificAnswers;

import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.util.Map;

import ru.miron.nonstop.logic.util.json.types.JSONValue;

public class DragonsInfoCommandSpecificAnswer {
    private String collectionType;
    private ZonedDateTime creationDate;
    private long dragonsCount;

    private DragonsInfoCommandSpecificAnswer () {}

    public static DragonsInfoCommandSpecificAnswer initFromTheRoot(Map<String, JSONValue> commandSpecificAnswerRoot) throws IllegalArgumentException {
        var collectionTypeAsJSONValue = commandSpecificAnswerRoot.get("collection type");
        var creationDateAsJSONValue = commandSpecificAnswerRoot.get("creation date");
        var dragonsCountAsJSONValue = commandSpecificAnswerRoot.get("dragons count");
        try {
            var specificAnswer = new DragonsInfoCommandSpecificAnswer();
            specificAnswer.collectionType = collectionTypeAsJSONValue.asString().getValue();
            specificAnswer.creationDate = ZonedDateTime.parse(creationDateAsJSONValue.asString().getValue());
            specificAnswer.dragonsCount = dragonsCountAsJSONValue.asInteger().getValue(); 
            return specificAnswer;
        } catch (ClassCastException | DateTimeParseException e) {
            throw new IllegalArgumentException();
        }
    }

    public String getCollectionType() {
        return collectionType;
    }

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public long getDragonsCount() {
        return dragonsCount;
    }
    
}