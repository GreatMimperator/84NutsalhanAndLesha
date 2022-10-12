package ru.miron.nonstop.givenClasses;

import java.util.Map;
import java.util.Objects;

import ru.miron.nonstop.logic.util.json.JSONConvertable;
import ru.miron.nonstop.logic.util.json.types.JSONValue;
import ru.miron.nonstop.logic.util.json.types.values.JSONDoubleNumber;
import ru.miron.nonstop.logic.util.json.types.values.JSONIntegerNumber;
import ru.miron.nonstop.logic.util.json.types.values.JSONMap;

public class Coordinates implements JSONConvertable {
    /**
     * X axis value
     */
    private float x;

    /**
     * Y axis value
     * Cannot be null
     */
    private Long y;

    /**
     * for creating own f. methods 
     */
    private Coordinates() {}

    /**
     * @param x x to set
     * @param y y to set
     */
    public Coordinates (float x, long y) throws IllegalArgumentException {
        setX(x);
        setY(y);
    }

    /**
     * Sets x axis value
     * 
     * @param x x axis value to set
     */
    public void setX(float x) {
        this.x = x;
    }

    /**
     * @return x axis value
     */
    public float getX() {
        return x;
    }

    /**
     * Sets y axis value
     * 
     * @param y y axis value to set
     */
    public void setY(long y) {
        this.y = y;
    }

    /**
     * @return y axis value
     */
    public long getY() {
        return y;
    }

    @Override
    public JSONMap toJSON() {
        JSONMap root = new JSONMap();
        root.put("x", new JSONDoubleNumber(x));
        root.put("y", new JSONIntegerNumber(y));
        return root;
    }

    /**
     * @throws IllegalArgumentException if root hasn't any required keys or has wrong types on known keys
     */
    public static Coordinates initFromTheRoot(Map<String, JSONValue> coordinatesRoot) {
        var xJSONValue = coordinatesRoot.get("x");
        var yJSONValue = coordinatesRoot.get("y");
        try {
            var coordinates = new Coordinates();
            coordinates.setX((float) xJSONValue.asDouble().getValue());
            coordinates.setY(yJSONValue.asInteger().getValue());
            return coordinates;
        } catch (ClassCastException e) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public boolean equals(Object obj) {
        Coordinates coordinates = (Coordinates) obj;
        if (coordinates == null) {
            return false;
        }
        return Objects.equals(coordinates.x, this.x) &&
                Objects.equals(coordinates.y, this.y);
    }

    @Override
    public String toString() {
        return "Coordinates [x=" + x + ", y=" + y + "]";
    }
}