package sudoku.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides utility functions for lists.
 *
 * @author Taras Mychaskiw
 */
public class ListUtil {

    private ListUtil(){}

    /**
     * Returns a list of all integers [start..max] inclusive.
     *
     * @param start the value at the first position in the list
     * @param max the value at the last position in the list
     * @return the list of integers [start..max] inclusive
     */
    public static List<Integer> rangeInc(int start, int max){
        final List<Integer> list = new ArrayList(max - start + 1);
        for (int i = start; i <= max; i++){
            list.add(i);
        }
        return list;
    }


    /**
     * Returns a list of all integers [start..end) exclusive.
     *
     * @param start the value at the first position in the list
     * @param end the value at the last position in the list
     * @return the list of integers [start..end) exclusive
     */
    public static List<Integer> range(int start, int end){
        final List<Integer> list = new ArrayList(end - start);
        for (int i = start; i < end; i++){
            list.add(i);
        }
        return list;
    }
}
