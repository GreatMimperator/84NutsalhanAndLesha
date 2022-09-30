package ru.miron.nonstop.logic.util.json.types.values;

import ru.miron.nonstop.logic.util.json.types.JSONValue;

public class JSONDoubleNumber extends JSONValue {
    private double value;

    public JSONDoubleNumber(double value) {
        this.value = value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    @Override
    public String toString() {
        return Double.toString(value);
    }
}
