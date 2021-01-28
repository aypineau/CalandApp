package com.example.CalandApp.homeActivity;

import java.util.Comparator;
import java.util.Map;

// Source : https://stackoverflow.com/questions/5369573/how-sort-an-arraylist-of-hashmaps-holding-several-key-value-pairs-each
class MapComparator implements Comparator<Map<String, Object>>
{
    private final String key;

    public MapComparator(String key)
    {
        this.key = key;
    }

    public int compare(Map<String, Object> first,
                       Map<String, Object> second)
    {
        // TODO: Null checking, both for maps and values
        String firstValue = (String) first.get(key);
        String secondValue = (String) second.get(key);
        return firstValue.compareTo(secondValue);
    }
}