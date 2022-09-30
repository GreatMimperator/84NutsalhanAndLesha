package ru.miron.nonstop.logic.commands.args;

import ru.miron.nonstop.givenClasses.DragonWithKeyAndOwner;
import ru.miron.nonstop.logic.util.json.JSONConvertable;
import ru.miron.nonstop.logic.util.json.types.values.JSONMap;

public class InsertCommandArgs implements JSONConvertable {
    /**
     * not null with key
     */
    private DragonWithKeyAndOwner dragonWithKeyAndOwner;
    
    /**
     * @param dragonWithKeyAndOwner not null with key
     * @throws IllegalArgumentException if dragonWithKeyAndOwner is null or hasn't key 
     */
    public InsertCommandArgs(DragonWithKeyAndOwner dragonWithKeyAndOwner) throws IllegalArgumentException {
        setDragonWithKeyAndOwner(dragonWithKeyAndOwner);
    }

    public DragonWithKeyAndOwner getDragonWithKeyAndOwner() {
        return dragonWithKeyAndOwner;
    }

    @Override
    public JSONMap toJSON() {
        JSONMap root = new JSONMap();
        root.put("dragon", dragonWithKeyAndOwner.toJSON());
        return root;
    }

    /**
     * @param dragonWithKeyAndOwner is not null and with key
     * @throws IllegalArgumentException if dragonWithOwner is null or hasn't key
     */
    public void setDragonWithKeyAndOwner(DragonWithKeyAndOwner dragonWithKeyAndOwner) throws IllegalArgumentException {
        if (dragonWithKeyAndOwner == null || !dragonWithKeyAndOwner.hasKey()) 
            throw new IllegalArgumentException();
        this.dragonWithKeyAndOwner = dragonWithKeyAndOwner;
    }
}
