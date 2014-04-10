package sudoku.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * Provides utility functions for strings.
 *
 * @author Taras Mychaskiw
 */
public final class StringUtil {

    private StringUtil(){}

    /**
     * Returns a new string which is the input string repeated n times.
     *
     * @param in the string to repeat
     * @param n the number of times to repeat the string
     * @return the repeated string
     */
    public static String repeat(String in, int n){
        StringBuilder sb = new StringBuilder(n * in.length());
        for (int i = 0; i < n; i++){
            sb.append(in);
        }
        return sb.toString();
    }

    /**
     * Folds the collection into a string, putting the delimeter in between
     * each element of the collection. eg join([1,2,3], " ") = "1 2 3"
     *
     * @param collection the collection to fold
     * @param delimeter string to put in between each element of the collection
     * @return the joined collection string
     * @see StringUtil.join(ArrayList<?>, String)
     */
    public static String join(Collection<?> collection, String delimeter){
        return join(new ArrayList(collection), delimeter);
    }

    /**
     * Folds the list into a string, putting the delimeter in between
     * each element of the collection. eg join([1,2,3], " ") = "1 2 3"
     *
     * @param list the list to fold
     * @param delimeter string to put in between each element of the list
     * @return the joined list string
     */
    public static String join(ArrayList<?> list, String delimeter){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size() - 1; i++){
            sb.append(list.get(i).toString()).append(delimeter);
        }
        return sb.append(list.get(list.size() - 1).toString()).toString();
    }

    /**
     * Folds the array into a string, putting the delimeter in between
     * each element of the collection. eg join([1,2,3], " ") = "1 2 3"
     *
     * @param array to array to fold
     * @param delimeter string to put in between each element of the list
     * @return the joined array string
     */
    public static String join(Object[] array, String delimeter){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < array.length - 1; i++){
            sb.append(array[i].toString()).append(delimeter);
        }
        return sb.append(array[array.length - 1].toString()).toString();
    }

    /**
     * Returns the string, padded with the character to the length specified.
     * The new characters are added to the end (the right) of the input string.
     *
     * @param in the string to extend
     * @param pad the character to add onto the end of the string
     * @param length how long to make the new string
     * @return the input string padded from the right
     */
    public static String padRight(String in, char pad, int length){
        char[] space = new char[length - in.length()];
        Arrays.fill(space, pad);
        return in.concat(new String(space));
    }

    /**
     * Returns the string, padded with the character to the length specified.
     * The new characters are added to the start (the left) of the input string.
     *
     * @param in the string to extend
     * @param pad the character to add onto the beginning of the string
     * @param length how long to make the new string
     * @return the input string padded from the left
     */
    public static String padLeft(String in, char pad, int length){
        char[] space = new char[length - in.length()];
        Arrays.fill(space, pad);
        return new String(space).concat(in);
    }


    /**
     * Replaces all Strings in replace by the corresponding String in by, that
     * is the ith String in replace is replaced by the ith String in by, in
     * order.
     *
     * @param src the string to replace values in
     * @param replace the substrings to find
     * @param by what to replace the substrings with
     * @return the replaced string
     */
    public static String replaceAll(String src, String[] replace, String[] by){
        for (int i = 0; i < replace.length; i++){
            src = src.replace(replace[i], by[i]);
        }
        return src;
    }


    /**
     * Replaces all Strings in replace by by.
     *
     * @param src the string to replace values in
     * @param replace the substrings to find
     * @param by what to replace the substrings with
     * @return the replaced string
     */
    public static String replaceAll(String src, String[] replace, String by){
        String[] rb = new String[replace.length];
        Arrays.fill(rb, by);
        return replaceAll(src, replace, rb);
    }
}
