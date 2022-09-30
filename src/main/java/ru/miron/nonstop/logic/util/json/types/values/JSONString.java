package ru.miron.nonstop.logic.util.json.types.values;

import ru.miron.nonstop.logic.util.json.types.JSONValue;

public class JSONString extends JSONValue {
    private String value;

    public JSONString(String value) {
        this.value = value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "\"" + value + "\"";
    }
}
