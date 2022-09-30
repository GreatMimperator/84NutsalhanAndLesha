package ru.miron.nonstop.logic.commands.args;

import ru.miron.nonstop.givenClasses.DragonWithKeyAndOwner;
import ru.miron.nonstop.logic.util.json.JSONConvertable;
import ru.miron.nonstop.logic.util.json.types.values.JSONIntegerNumber;
import ru.miron.nonstop.logic.util.json.types.values.JSONMap;

public class UpdateCommandArgs implements JSONConvertable {
    /**
     * positive
     */
    private long id;
    /**
     * not null with key
     */
    private DragonWithKeyAndOwner dragonWithKeyAndOwner;
    
    /**
     * @param id positive
     * @param dragonWithKeyAndOwner not null with key
     * @throws IllegalArgumentException if dragonWithKeyAndOwner is null or hasn't key or id isn't positive 
     */
    public UpdateCommandArgs(long id, DragonWithKeyAndOwner dragonWithKeyAndOwner) throws IllegalArgumentException {
        setId(id);
        setDragonWithKeyAndOwner(dragonWithKeyAndOwner);
    }

    public DragonWithKeyAndOwner getDragonWithKeyAndOwner() {
        return dragonWithKeyAndOwner;
    }

    @Override
    public JSONMap toJSON() {
        JSONMap root = new JSONMap();
        root.put("id", new JSONIntegerNumber(id));
        root.put("dragon", dragonWithKeyAndOwner.toJSON());
        return root;
    }

    /**
     * @param id positive
     * @throws IllegalArgumentException if id isn't positive
     */
    public void setId(long id) throws IllegalArgumentException {
        if (id <= 0)
            throw new IllegalArgumentException();
        this.id = id;
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
