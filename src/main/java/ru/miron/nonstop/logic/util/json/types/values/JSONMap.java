package ru.miron.nonstop.logic.util.json.types.values;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import ru.miron.nonstop.logic.util.json.types.JSONValue;

public class JSONMap extends JSONValue {
    private Map<String, JSONValue> values;

    public JSONMap(Map<String, JSONValue> values) {
        this.values = values;
    }

    public JSONMap() {
        this.values = new HashMap<>();
    }

    public void setValues(Map<String, JSONValue> values) {
        this.values = values;
    }
    
    public void putValues(JSONMap jsonMap) {
        this.putValues(jsonMap.values);
    }
    
    public void putValues(Map<String, JSONValue> values) {
        this.values.putAll(values);
    }

    public Map<String, JSONValue> getValues() {
        return values;
    }

    public void put(String key, JSONValue value) {
        values.put(key, value);
    }

    public JSONValue get(String key) {
        return values.get(key);
    }

    @Override
    public String toString() {
        return "{" + String.join(",", toValuesStrings()) + "}";
    }

    public List<String> toValuesStrings() {
        return values.entrySet().stream()
            .map(entry -> "\"" + entry.getKey() + "\":" + entry.getValue().toString()).collect(Collectors.toList());
    } 
}
