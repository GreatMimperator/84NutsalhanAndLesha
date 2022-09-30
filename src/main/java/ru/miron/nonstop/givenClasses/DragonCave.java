package ru.miron.nonstop.givenClasses;

import java.util.Objects;

import ru.miron.nonstop.logic.util.json.JSONConvertable;
import ru.miron.nonstop.logic.util.json.types.JSONValue;
import ru.miron.nonstop.logic.util.json.types.values.JSONDoubleNumber;
import ru.miron.nonstop.logic.util.json.types.values.JSONMap;

public class DragonCave implements JSONConvertable {
    /**
     * {@link #DragonCave}'s numberOfTreasures
     * Positive value
     */
    private float numberOfTreasures; 

    /**
     * @throws IllegalArgumentException if numberOfTreasures is not positive
     */
    public DragonCave (float numberOfTreasures) throws IllegalArgumentException {
        setNumberOfTreasures(numberOfTreasures);
    }

    /**
     * @throws IllegalArgumentException if numberOfTreasures is not positive 
     */
    public void setNumberOfTreasures(float numberOfTreasures) throws IllegalArgumentException {
        if (numberOfTreasures <= 0) {
            throw new IllegalArgumentException();
        }
        this.numberOfTreasures = numberOfTreasures;
    }

    /**
     * @return numberOfTreasures
     */
    public float getNumberOfTreasures() {
        return numberOfTreasures;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(numberOfTreasures);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        return ((DragonCave) obj).numberOfTreasures == this.numberOfTreasures;
    }

    @Override
    public String toString() {
        return "DragonCave [numberOfTreasures=" + numberOfTreasures + "]";
    }

    @Override
    public JSONValue toJSON() {
        JSONMap root = new JSONMap();
        root.put("number of treasures", new JSONDoubleNumber(numberOfTreasures));
        return root;
    }
}