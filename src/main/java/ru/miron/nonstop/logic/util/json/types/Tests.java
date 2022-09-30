package ru.miron.nonstop.logic.util.json.types;

import java.util.LinkedList;
import java.util.List;

import ru.miron.nonstop.logic.util.json.JSON;
import ru.miron.nonstop.logic.util.json.types.values.JSONArray;
import ru.miron.nonstop.logic.util.json.types.values.JSONIntegerNumber;
import ru.miron.nonstop.logic.util.json.types.values.JSONMap;
import ru.miron.nonstop.logic.util.json.types.values.JSONString;

public class Tests {
    public static void main(String[] args) {
        testStruct();
        testReal();
    }

    public static void testStruct() {
        JSONMap root = new JSONMap();
        root.put("long value", new JSONIntegerNumber(213));
        List<JSONValue> arrayValues = new LinkedList<>();
        arrayValues.add(new JSONIntegerNumber(123));
        arrayValues.add(new JSONString("fasdf"));
        root.put("array", new JSONArray(arrayValues));
        System.out.println(JSON.parse(root.toString()));
    }

    public static void testReal() {
        String in = "{\n\t\"id\": \"0001\",\n\t\"type\": \"donut\",\n\t\"name\": \"Cake\",\n\t\"ppu\": 0.55,\n\t\"batters\":\n\t\t{\n\t\t\t\"batter\":\n\t\t\t\t[\n\t\t\t\t\t{ \"id\": \"1001\", \"type\": \"Regular\" },\n\t\t\t\t\t{ \"id\": \"1002\", \"type\": \"Chocolate\" },\n\t\t\t\t\t{ \"id\": \"1003\", \"type\": \"Blueberry\" },\n\t\t\t\t\t{ \"id\": \"1004\", \"type\": \"Devil\'s Food\" }\n\t\t\t\t]\n\t\t},\n\t\"topping\":\n\t\t[\n\t\t\t{ \"id\": \"5001\", \"type\": \"None\" },\n\t\t\t{ \"id\": \"5002\", \"type\": \"Glazed\" },\n\t\t\t{ \"id\": \"5005\", \"type\": \"Sugar\" },\n\t\t\t{ \"id\": \"5007\", \"type\": \"Powdered Sugar\" },\n\t\t\t{ \"id\": \"5006\", \"type\": \"Chocolate with Sprinkles\" },\n\t\t\t{ \"id\": \"5003\", \"type\": \"Chocolate\" },\n\t\t\t{ \"id\": \"5004\", \"type\": \"Maple\" }\n\t\t]\n}";
        System.out.println("FROM: \n" + in);
        System.out.println("TO: \n" + JSON.parse(in));
    }
}
