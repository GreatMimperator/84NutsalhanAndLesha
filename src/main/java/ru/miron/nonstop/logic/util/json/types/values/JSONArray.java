package ru.miron.nonstop.logic.util.json.types.values;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import ru.miron.nonstop.logic.util.json.types.JSONValue;

public class JSONArray extends JSONValue {
    private List<JSONValue> values;

    /**
     * @throws IllegalArgumentException if values if null
     */
    public JSONArray(List<JSONValue> values) throws IllegalArgumentException {
        setValues(values);
    }

    public JSONArray() {
        this.values = new LinkedList<>();
    }

    /**
     * @throws IllegalArgumentException if values if null 
     */
    public void setValues(List<JSONValue> values) throws IllegalArgumentException {
        if (values == null) {
            throw new IllegalArgumentException();
        }
        this.values = values;
    }

    public List<JSONValue> getValues() {
        return values;
    }

    /**
     * @throws IllegalArgumentException if value if null
     */
    public void add(JSONValue value) throws IllegalArgumentException {
        if (value == null) {
            throw new IllegalArgumentException();
        }
        values.add(value);
    }

    @Override
    public String toString() {
        return "[" + String.join(",", toString(values)) + "]";
    }

    public List<String> toString(List<JSONValue> values) {
        return values.stream().map(a -> a.toString()).collect(Collectors.toList());
    } 
}
