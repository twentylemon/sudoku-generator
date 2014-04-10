package sudoku.util;

import java.util.Map;

/**
 * Provides utility functions for maps.
 *
 * @author Taras Mychaskiw
 */
public class MapUtil {

    private MapUtil(){}

    /**
     * Returns the value in the map at the specified key, or the default
     * value if the key does not exist in the map.
     *
     * @param <K> key type of the map
     * @param <V> value type of the map
     * @param map the map to get from
     * @param key the key to retrieve from the map
     * @param defaultValue value to return if they key does not exist
     * @return the value at the key, or the defaultValue if it doesn't exist
     */
    public static <K,V> V maybeGet(Map<K,V> map, K key, V defaultValue){
        V value = map.get(key);
        if (value == null){
            return defaultValue;
        }
        return value;
    }
}
