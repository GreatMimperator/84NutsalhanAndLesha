package ru.miron.nonstop.logic.util.json.types.values;

import ru.miron.nonstop.logic.util.json.types.JSONValue;

public class JSONIntegerNumber extends JSONValue {
    private long value;
    
    public JSONIntegerNumber(long value) {
        this.value = value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    public long getValue() {
        return value;
    }

    @Override
    public String toString() {
        return Long.toString(value);
    }
}
