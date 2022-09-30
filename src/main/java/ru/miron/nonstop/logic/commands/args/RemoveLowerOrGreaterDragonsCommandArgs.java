package ru.miron.nonstop.logic.commands.args;

import ru.miron.nonstop.givenClasses.Dragon;
import ru.miron.nonstop.logic.util.json.JSONConvertable;
import ru.miron.nonstop.logic.util.json.types.values.JSONMap;

public class RemoveLowerOrGreaterDragonsCommandArgs implements JSONConvertable {
    /**
     * not null
     */
    private Dragon dragon;
    
    /**
     * @param dragon not null
     * @throws IllegalArgumentException if dragon is null
     */
    public RemoveLowerOrGreaterDragonsCommandArgs(Dragon dragon) throws IllegalArgumentException {
        setDragon(dragon);
    }

    public Dragon getDragon() {
        return dragon;
    }

    @Override
    public JSONMap toJSON() {
        JSONMap root = new JSONMap();
        root.put("dragon", dragon.toJSON());
        return root;
    }

    /**
     * @param dragon is not null
     * @throws IllegalArgumentException if dragon is null
     */
    public void setDragon(Dragon dragon) throws IllegalArgumentException {
        if (dragon == null) 
            throw new IllegalArgumentException();
        this.dragon = dragon;
    }
}
