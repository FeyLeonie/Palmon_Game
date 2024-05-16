package data_structures;

import java.util.*;

// Works like a HashMap but allows you to save multiple Values for one Key
public class MultiHashMap<K, V>
{
    Map<K, List<V>> map;

    public MultiHashMap()
    {
        map = new HashMap<>();
    }

    // Adds a value to the list of values for the specified key
    public void put(K key, V value)
    {
        if (!map.containsKey(key))
        {
            map.put(key, new ArrayList<>());
        }
        map.get(key).add(value);
    }

    // Returns the list of values associated with the specified key
    public List<V> get(K key)
    {
        return map.getOrDefault(key, new ArrayList<>());
    }

    // Returns a set view of the keys contained in this MultiHashMap
    public Set<K> keySet()
    {
        return map.keySet();
    }
}