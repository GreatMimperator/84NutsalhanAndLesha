package ru.miron.nonstop.logic.commands.args;

import ru.miron.nonstop.logic.util.json.JSONConvertable;
import ru.miron.nonstop.logic.util.json.types.values.JSONMap;
import ru.miron.nonstop.logic.util.json.types.values.JSONString;

public class GetDragonsWithThisDescriptionCommandArgs implements JSONConvertable {
    /**
     * is not null and is not empty
     */  
    private String description;

    /**
     * @param description is not null and is not empty
     * @throws IllegalArgumentException if description is null of empty
     */
    public GetDragonsWithThisDescriptionCommandArgs(String description) throws IllegalArgumentException {
        setDescription(description);
    }

    public String getDescription() {
        return description;
    }

    /**
     * @param description is not null and is not empty
     * @throws IllegalArgumentException if description is null or empty
     */
    public void setDescription(String description) throws IllegalArgumentException {
        if (description == null || description.isEmpty()) 
            throw new IllegalArgumentException();
        this.description = description;
    }

    @Override
    public JSONMap toJSON() {
        JSONMap root = new JSONMap();
        root.put("description", new JSONString(description));
        return root;
    }
}
