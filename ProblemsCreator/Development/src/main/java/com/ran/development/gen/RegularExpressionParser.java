package com.ran.development.gen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegularExpressionParser {

    private Map<String, List<Character>> map = new HashMap<>();
    
    public char getCharacter(String regularExpression, int index) {
        return getCharacters(regularExpression).get(index);
    }
    
    public int getCharactersQuantity(String regularExpression) {
        return getCharacters(regularExpression).size();
    }
    
    private List<Character> getCharacters(String regularExpression) {
        List<Character> list = map.get(regularExpression);
        if (list == null) {
            list = parseExpression(regularExpression);
            map.putIfAbsent(regularExpression, list);
        }
        return list;
    }
    
    private List<Character> parseExpression(String regularExpression) {
        List<Character> list = new ArrayList<>();
        int index = 0;
        while (index < regularExpression.length()) {
            switch (regularExpression.charAt(index)) {
                case '%':
                    if (index + 1 >= regularExpression.length()) {
                        throw new IllegalArgumentException("Percent character at the end of the regular expression");
                    }
                    list.add(regularExpression.charAt(index + 1));
                    index += 2;
                    break;
                case '[':
                    if (index + 4 >= regularExpression.length() ||
                            regularExpression.charAt(index + 2) != '-' ||
                            regularExpression.charAt(index + 4) != ']') {
                        throw new IllegalArgumentException("Incorrect usage of square brackets in the regular expression");
                    }
                    for (char symbol = regularExpression.charAt(index + 1);
                            symbol <= regularExpression.charAt(index + 3); symbol++) {
                        list.add(symbol);
                    }
                    index += 5;
                    break;
                case ']':
                    throw new IllegalArgumentException("Unexpected closing square bracket in the regular expression");
                default:
                    list.add(regularExpression.charAt(index));
                    index++;
            }
        }
        return list;
    }
    
}