package ru.miron.nonstop.logic.commands.args;

import ru.miron.nonstop.logic.util.json.JSONConvertable;
import ru.miron.nonstop.logic.util.json.types.values.JSONMap;
import ru.miron.nonstop.logic.util.json.types.values.JSONString;

public class RemoveDragonByKeyCommandArgs implements JSONConvertable {
    /**
     * is not null and is not empty
     */  
    private String key;

    /**
     * @param key is not null and is not empty
     * @throws IllegalArgumentException if key is null of empty
     */
    public RemoveDragonByKeyCommandArgs(String key) throws IllegalArgumentException {
        setKey(key);
    }

    public String getKey() {
        return key;
    }

    /**
     * @param key is not null and is not empty
     * @throws IllegalArgumentException if key is null or empty
     */
    public void setKey(String key) throws IllegalArgumentException {
        if (key == null || key.isEmpty()) 
            throw new IllegalArgumentException();

        this.key = key;
    }

    @Override
    public JSONMap toJSON() {
        JSONMap root = new JSONMap();
        root.put("key", new JSONString(key));
        return root;
    }
}
