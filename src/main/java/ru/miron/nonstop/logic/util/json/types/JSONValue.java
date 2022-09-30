package ru.miron.nonstop.logic.util.json.types;

import ru.miron.nonstop.logic.util.json.types.values.JSONArray;
import ru.miron.nonstop.logic.util.json.types.values.JSONBoolean;
import ru.miron.nonstop.logic.util.json.types.values.JSONDoubleNumber;
import ru.miron.nonstop.logic.util.json.types.values.JSONIntegerNumber;
import ru.miron.nonstop.logic.util.json.types.values.JSONMap;
import ru.miron.nonstop.logic.util.json.types.values.JSONString;

public abstract class JSONValue {
    public boolean isArray() {
        return this instanceof JSONArray;
    }

    public JSONArray asArray() throws ClassCastException {
        return (JSONArray) this;
    }

    public boolean isDouble() {
        return this instanceof JSONDoubleNumber;
    }
    
    public JSONDoubleNumber asDouble() throws ClassCastException {
        return (JSONDoubleNumber) this;
    }

    public boolean isInteger() {
        return this instanceof JSONIntegerNumber;
    }
    
    public JSONIntegerNumber asInteger() throws ClassCastException {
        return (JSONIntegerNumber) this;
    }

    public boolean isMap() {
        return this instanceof JSONMap;
    }
    
    public JSONMap asMap() throws ClassCastException {
        return (JSONMap) this;
    }

    public boolean isString() {
        return this instanceof JSONString;
    }
    
    public JSONString asString() throws ClassCastException {
        return (JSONString) this;
    }

    public boolean isBoolean() {
        return this instanceof JSONBoolean;
    }

    public JSONBoolean asBoolean() throws ClassCastException {
        return (JSONBoolean) this;
    }
}
