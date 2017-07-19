package cn.edu.hust.util;

import java.util.*;
import java.util.stream.Stream;

/**
 * Description: sum, max, min functions...
 * Created by Gaoxinwen on 2017/1/4.
 */
public class MathUtils {

    public static double sum(List<Double> list) {
        double result = 0;
        for (Double d : list) {
            result += d;
        }
        return result;
    }

    public static double sum(double[] args) {
        double result = 0;
        for (double arg : args) {
            result += arg;
        }
        return result;
    }


    public static int[] getColumn(int[][] source, int columnIndex) {
        int[] result = new int[source.length];
        for (int i = 0; i < source.length; i++) {
            result[i] = source[i][columnIndex];
        }
        return result;
    }


    public static double getMax(double[] array) {
        double max = array[0];
        for (double x : array) {
            if (max < x) {
                max = x;
            }
        }
        return max;
    }

    public static int getMaxIndex(double[] array) {
        double max = array[0];
        int maxIndex = 0;
        for (int i = 1; i < array.length; i++) {
            if (max < array[i]) {
                max = array[i];
                maxIndex = i;
            }
        }
        return maxIndex;
    }

    public static double getMin(double[] array) {
        double min = array[0];
        for (double x : array) {
            if (min > x) {
                min = x;
            }
        }
        return min;
    }

    public static int getMinIndex(double[] array) {
        double min = array[0];
        int minIndex = 0;
        for (int i = 1; i < array.length; i++) {
            if (min > array[i]) {
                min = array[i];
                minIndex = i;
            }
        }
        return minIndex;
    }

    /**
     * Map sorted by value
     * @param map map
     * @return sorted map
     */
    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        Map<K, V> result = new LinkedHashMap<>();
        Stream<Map.Entry<K, V>> st = map.entrySet().stream();

        st.sorted(Map.Entry.comparingByValue()).forEachOrdered(e -> result.put(e.getKey(), e.getValue()));

        return result;
    }
}
