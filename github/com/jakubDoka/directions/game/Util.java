package github.com.jakubDoka.directions.game;
import java.lang.reflect.Array;

/**
 * Util is a stateless class that holds some utility methods. 
 * All static.
 */
public class Util {
    /**
     * Sort sorts array by split-sort algorithm. 
     *
     * This is my own non-recursive implementations, only thing i have taken 
     * is the main principle which preserves O(N log N) complexity.
     *
     * @param array - Array to sort.
     * @param comparator - Lambda that compares the elements.
     */
    @SuppressWarnings("unchecked")
    public static <T> void sort(T[] array, IComparator<? super T> comparator) {
        T[] original = array;
        if (array.length < 2) {
            return;
        }
        // managed to allocate just one array of equal size
        T[] temp = (T[]) Array.newInstance(array[0].getClass(), array.length); 
        int stride = 2;
        while (stride / 2 < array.length) {
            int halfStride = stride / 2;
            for (int i = 0; i < array.length; i += stride) {
                int cursor = i;
                int a = i;
                int aBound = Math.min(i + halfStride, array.length);
                int b = aBound;
                int bBound = Math.min(i + stride, array.length);
                while (cursor < bBound) {
                    if ((b < bBound && comparator.compare(array[a], array[b]) || a >= aBound)) {
                        temp[cursor++] = array[b++];
                    } else {
                        temp[cursor++] = array[a++];
                    }
                }
            }

            T[] swap = array;
            array = temp;
            temp = swap;

            stride *= 2;
        }

        if (array != original) {
            System.arraycopy(array, 0, original, 0, array.length);
        }
    }

    /**
     * Comparator returns true is a is greater then b, otherwise false.
     */
    public interface IComparator<T> {
        boolean compare(T a, T b);
    }
}
