package ru.miron.nonstop.logic.commands.args;

import ru.miron.nonstop.givenClasses.DragonWithKeyAndOwner;
import ru.miron.nonstop.logic.util.json.JSONConvertable;
import ru.miron.nonstop.logic.util.json.types.values.JSONMap;
import ru.miron.nonstop.logic.util.json.types.values.JSONString;

public class ReplaceIfDragonIsGreaterCommandArgs implements JSONConvertable {
    /**
     * not null and isn't empty 
     */
    private String key;
    /**
     * not null with key
     */
    private DragonWithKeyAndOwner dragonWithKeyAndOwner;
    
    /**
     * @param key not null and isn't empty
     * @param dragonWithKeyAndOwner not null with key
     * @throws IllegalArgumentException if dragonWithKeyAndOwner is null or hasn't key or key is null or empty 
     */
    public ReplaceIfDragonIsGreaterCommandArgs(String key, DragonWithKeyAndOwner dragonWithKeyAndOwner) throws IllegalArgumentException {
        setKey(key);
        setDragonWithKeyAndOwner(dragonWithKeyAndOwner);
    }

    public DragonWithKeyAndOwner getDragonWithKeyAndOwner() {
        return dragonWithKeyAndOwner;
    }

    @Override
    public JSONMap toJSON() {
        JSONMap root = new JSONMap();
        root.put("key", new JSONString(key));
        root.put("dragon", dragonWithKeyAndOwner.toJSON());
        return root;
    }

    /**
     * @param key isn't null and isn't empty 
     * @throws IllegalArgumentException if key is null or empty 
     */
    public void setKey(String key) throws IllegalArgumentException {
        if (key == null || key.isEmpty())
            throw new IllegalArgumentException();
        this.key = key;
    }

    /**
     * @param dragonWithKeyAndOwner is not null with key
     * @throws IllegalArgumentException if dragonWithOwner is null or hasn't key
     */
    public void setDragonWithKeyAndOwner(DragonWithKeyAndOwner dragonWithKeyAndOwner) throws IllegalArgumentException {
        if (dragonWithKeyAndOwner == null || !dragonWithKeyAndOwner.hasKey()) 
            throw new IllegalArgumentException();
        this.dragonWithKeyAndOwner = dragonWithKeyAndOwner;
    }
}
