package sudoku.util;

import java.util.Random;

/**
 * Provides utility functions for arrays.
 *
 * @author Taras Mychaskiw
 */
public class ArrayUtil {

    private final static Random rand = new Random();

    private ArrayUtil(){}

    /**
     * Returns an array of all integers [start..end) exclusive.
     *
     * @param start the value at the first position in the array
     * @param end the value at the last position in the array
     * @return the array of integers [start..end)
     */
    public static int[] range(int start, int end){
        final int size = end - start;
        final int[] array = new int[size];
        for (int i = 0; i < array.length; i++){
            array[i] = i + start;
        }
        return array;
    }

    /**
     * Shuffles the array given.
     *
     * @param array the array to shuffle
     */
    public static void shuffle(int[] array){
        for (int i = 0; i < array.length - 1; i++){
            int j = rand.nextInt(i + 1);
            int temp = array[j];
            array[j] = array[i];
            array[i] = temp;
        }
    }

    /**
     * Returns true if the value is in the array given.
     *
     * @param <T> the type held in the array
     * @param array the array to search in
     * @param value the value to search for
     * @return true if the value is in the array
     */
    public static <T> boolean contains(T[] array, T value){
        for (T entry : array){
            if (entry == value || value != null && value.equals(entry)){
                return true;
            }
        }
        return false;
    }
}
