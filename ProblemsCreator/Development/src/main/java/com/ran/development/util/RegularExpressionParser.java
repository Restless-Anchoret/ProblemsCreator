package com.ran.development.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RegularExpressionParser {

    private Map<String, List<Character>> mapList = new HashMap<>();
    private Map<String, Set<Character>> mapSet = new HashMap<>();
    
    public char getCharacter(String regularExpression, int index) {
        return getCharactersList(regularExpression).get(index);
    }
    
    public int getCharactersQuantity(String regularExpression) {
        return getCharactersList(regularExpression).size();
    }
    
    public boolean matchesExpression(char symbol, String regularExpression) {
        return getCharactersSet(regularExpression).contains(symbol);
    }
    
    public boolean matchesExpression(String line, String regularExpression) {
        Set<Character> set = getCharactersSet(regularExpression);
        for (int i = 0; i < line.length(); i++) {
            if (!set.contains(line.charAt(i))) {
                return false;
            }
        }
        return true;
    }
    
    private List<Character> getCharactersList(String regularExpression) {
        List<Character> list = mapList.get(regularExpression);
        if (list == null) {
            list = parseExpression(regularExpression);
            mapList.putIfAbsent(regularExpression, list);
        }
        return list;
    }
    
    private Set<Character> getCharactersSet(String regularExpression) {
        Set<Character> set = mapSet.get(regularExpression);
        if (set == null) {
            List<Character> list = getCharactersList(regularExpression);
            set = new HashSet(list);
            mapSet.putIfAbsent(regularExpression, set);
        }
        return set;
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