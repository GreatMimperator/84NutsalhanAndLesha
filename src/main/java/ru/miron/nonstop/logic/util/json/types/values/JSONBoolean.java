package ru.miron.nonstop.logic.util.json.types.values;

import ru.miron.nonstop.logic.util.json.types.JSONValue;

public class JSONBoolean extends JSONValue {
    public boolean value;

    public JSONBoolean(boolean value) {
        this.value = value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    @Override
    public String toString() {
        return Boolean.toString(value);
    }
}
