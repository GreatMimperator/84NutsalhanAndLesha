package ru.miron.nonstop.logic.util.json;

import ru.miron.nonstop.logic.util.json.types.JSONValue;
import ru.miron.nonstop.logic.util.json.types.values.*;

public class JSON {

    public static JSONValue parse(String exp) throws IllegalStateException {
        return getElement(exp, 0, exp.length(), true);
    }

    private static JSONMap parseJSONMap(String exp, int from, int to) throws IllegalStateException { // bounds is (from, to)
        JSONMap root = new JSONMap();
        int beginBraceIndex = exp.indexOf("{", from);
        int closingBraceIndex = exp.lastIndexOf("}", to - 1);
        // now bounds is (beginBraceIndex + 1, closingBraceIndex)
        if (beginBraceIndex == -1 || closingBraceIndex == -1 || beginBraceIndex >= to || closingBraceIndex >= to) {
            throw new IllegalStateException("Failed at detecting braces(\"{\" and \"}\") in \'" + exp.substring(from, to) + "\'");
        }
        if (beginBraceIndex > closingBraceIndex) {
            throw new IllegalStateException("Last \"}\" stays before first \"{\"");
        }
        int cursor = beginBraceIndex + 1; // cursor is on begin bound
        while (true) {
            int beginQuoteIndex = exp.indexOf('"', cursor); // first " char of current bound
            if (beginQuoteIndex == -1 || beginQuoteIndex >= closingBraceIndex) {
                String nextMapMember = exp.substring(cursor, closingBraceIndex);
                if (!nextMapMember.trim().isEmpty()) {
                    throw new IllegalStateException("map member should be pair of string and value: \"" + nextMapMember + "\"");
                }
                break;
            }
            String expBetweenOpenBraceAndOpenQuote = exp.substring(cursor, beginQuoteIndex);
            if (!expBetweenOpenBraceAndOpenQuote.trim().isEmpty()) {
                throw new IllegalStateException("expression between open brace and open quote should be empty: \"" + expBetweenOpenBraceAndOpenQuote + "\"");
            }
            int closingQuoteIndex = getClosingQuoteIndexOfJSONString(beginQuoteIndex, exp);
            if (closingQuoteIndex == -1 || closingQuoteIndex >= closingBraceIndex) {
                throw new IllegalStateException("Cannot find the second '\"' in \"<..> " + exp.substring(beginQuoteIndex) + "\"");
            }
            String key = exp.substring(beginQuoteIndex + 1, closingQuoteIndex);
            // bounds now is (closingQuoteIndex + 1, closingBraceIndex)
            int colonIndex = exp.indexOf(":", closingQuoteIndex + 1);
            if (colonIndex == -1 || colonIndex >= closingBraceIndex) {
                throw new IllegalStateException("Cannot find \":\" in \"<..> " + exp.substring(closingQuoteIndex) + "\"");
            }
            String expBetweenClosingQuoteAndColon = exp.substring(closingQuoteIndex + 1, colonIndex);
            if (!expBetweenClosingQuoteAndColon.trim().isEmpty()) {
                throw new IllegalStateException("expression between closing quote and colon should be empty: \"" + expBetweenClosingQuoteAndColon + "\"");
            }
            // bounds now is (colonIndex + 1, closingBraceIndex)

            // now we have '"key": may_be_has_commas ,(has or not?)', 
            // where if may_be_has_commas should have even number of all types of braces, 
            // if want to think that has. Else - we think we hasnt
            int commaIndex = getFirstCommaIndexWithEvenNumberOfBracesBefore(exp, colonIndex + 1, closingBraceIndex);
            int valueToIndex;
            if (commaIndex == -1) {
                valueToIndex = closingBraceIndex;
            } else {
                valueToIndex = commaIndex;
            }
            JSONValue element = JSON.getElement(exp, colonIndex + 1, valueToIndex, false);
            root.put(key, element);
            if (commaIndex == -1) {
                break;
            } else {
                cursor = commaIndex + 1;
            }
        }
        return root;
    }

    private static int getFirstCommaIndexWithEvenNumberOfBracesBefore(String exp, int from, int to) {
        int commonBracketsCounter = 0;
        int squareBracketsCounter = 0;
        boolean hasOpenColon = false;
        for (int i = from; i < to; i++) {
            char ch = exp.charAt(i);
            if (hasOpenColon) {
                if (ch == '\\') {
                    i++;
                }
                if (ch != '\"') {
                    continue;
                }
            }
            if (ch == '\"') {
                hasOpenColon = !hasOpenColon;
            } else if (ch == '{') {
                commonBracketsCounter++;
            } else if (ch == '}') {
                commonBracketsCounter--;
            } else if (ch == '[') {
                squareBracketsCounter++;
            } else if (ch == ']') {
                squareBracketsCounter--;
            } else if (ch == ',') {
                if (commonBracketsCounter == 0 && squareBracketsCounter == 0) {
                    return i;
                }
            }
        }
        return -1;
    }

    // private static boolean hasNotClosedSquareOrCommonBrackets(String exp, int from, int to) {
    //     int commonBracketsCounter = 0;
    //     int squareBracketsCounter = 0;
    //     for (char ch : exp.substring(from, to).toCharArray()) {
    //         if (ch == '{') {
    //             commonBracketsCounter++;
    //         } else if (ch == '}') {
    //             commonBracketsCounter--;
    //         } else if (ch == '[') {
    //             squareBracketsCounter++;
    //         } else if (ch == ']') {
    //             squareBracketsCounter--;
    //         }
    //         // if (commonBracketsCounter < 0 || squareBracketsCounter < 0) {

    //         // }
    //     }
    //     return commonBracketsCounter != 0 || squareBracketsCounter != 0;
    // }

    private static JSONValue getElement(String exp, int begin, int end, boolean is_root) throws IllegalStateException {
        String trimmedExp = exp.substring(begin, end).trim();
        if (trimmedExp.length() == 0) {
            throw new IllegalStateException("Expression has blank element in [" + begin + ":" + end + "] range: \"" + exp.substring(begin, end) + "\"");
        }
        char firstChar = trimmedExp.charAt(0);
        char lastChar = trimmedExp.charAt(trimmedExp.length() - 1);
        if (firstChar == '"') {
            if (is_root) {
                throw new IllegalStateException("Root of file should be array or map");
            }
            if (lastChar == '"') {
                return new JSONString(trimmedExp.substring(1, trimmedExp.length() - 1));
            } else {
                throw new IllegalStateException("Cannot find closing colon: \"" + trimmedExp + "\"");
            }
        } else if (firstChar == '{') {
            if (lastChar == '}') {
                return parseJSONMap(exp, begin, end);
            } else {
                throw new IllegalStateException("Cannot find closing brace: \"" + trimmedExp + "\"");
            }
        } else if(firstChar == '[') {
            if (lastChar == ']') {
                return parseJSONArray(exp, begin, end);
            } else {
                throw new IllegalStateException("Cannot find closing square bracket: \"" + trimmedExp + "\"");
            }
        } else {
            if (is_root) {
                throw new IllegalStateException("Root of file should be array or map");
            }
            String value = exp.substring(begin, end).trim();
            try {
                return new JSONIntegerNumber(Long.parseLong(value));
            } catch (NumberFormatException ex) {}
            try {
                return new JSONDoubleNumber(Double.parseDouble(value));
            } catch (NumberFormatException e) {}
            if (value.equals(Boolean.toString(true)) || value.equals(Boolean.toString(false))) {
                return new JSONBoolean(Boolean.parseBoolean(value));
            }
            throw new IllegalStateException("Unknown value: \"" + exp.substring(begin, end) + "\"");
        }
    }

    private static JSONValue parseJSONArray(String exp, int from, int to) {
        int fromIndex;
        for (int i = from;; i++) {
            if (exp.charAt(i) == '[') {
                fromIndex = i + 1;
                break;
            }
        }
        int toIndex;
        for (int i = to - 1;; i--) {
            if (exp.charAt(i) == ']') {
                toIndex = i;
                break;
            }
        }
        return parseJSONArrayEntrails(exp, fromIndex, toIndex);
    }

    private static JSONValue parseJSONArrayEntrails(String exp, int from, int to) throws IllegalStateException {
        JSONArray jsonArray = new JSONArray();
        if (exp.substring(from, to).isBlank()) {
            return jsonArray;
        }
        int cursor = from;
        while (true) {
            int commaIndex = getFirstCommaIndexWithEvenNumberOfBracesBefore(exp, cursor, to);
            int valueToIndex;
            if (commaIndex == -1) {
                valueToIndex = to;
            } else {
                valueToIndex = commaIndex;
            }
            jsonArray.add(JSON.getElement(exp, cursor, valueToIndex, false));
            if (commaIndex == -1) {
                break;
            } else {
                cursor = commaIndex + 1;
            }
        }
        return jsonArray;
    }

    private static int getClosingQuoteIndexOfJSONString(int beginQuoteIndex, String string) {
        for (int i = beginQuoteIndex + 1; i < string.length(); i++) {
            if (string.charAt(i) == '"') {
                return i;
            }
            if (string.charAt(i) == '\\') {
                i++;
                continue;
            }
        }
        return -1;
        
    }
}
