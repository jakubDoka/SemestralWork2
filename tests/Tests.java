package tests;
import java.util.Random;

import github.com.jakubDoka.directions.game.Util;

/**
 * Unit test class.
 */
public class Tests {

    /**
     * Runs all tests.
     */
    public static void main(String[] args) {
        Tests.sortTest();
    }


    private static void sortTest() {
        /*
            This is called fuzzy testing or random testing, 
            one of most efficient forms of testing, as possibility of
            not covering all cases is very low.
        */
        
        Random rand = new Random();

        final int length = 1000;

        Integer[] array = new Integer[length];

        for (int i = 0; i < 1000; i++) {
            for (int j = 0; j < length; j++) {
                array[j] = rand.nextInt(10000);
            }
            Util.sort(array, (a, b) -> a > b);
            for (int j = 1; j < array.length; j++) {
                Tests.doAssert(array[j - 1] <= array[j], "%d <= %d %s", array[j - 1], array[j]);
            }                
        }
    }

    private static void doAssert(boolean condition, String message, Object... args) {
        if (!condition) {
            throw new RuntimeException("assertion failed: " + String.format(message, args));
        }
    }
}
