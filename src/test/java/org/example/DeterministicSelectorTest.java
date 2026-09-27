package org.example;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DeterministicSelectorTest {

    @Test
    void testMinimum() {
        int[] array = {7, 2, 9, 1, 5};

        DeterministicSelector selector = new DeterministicSelector();

        assertEquals(1, selector.select(array, 0));
    }

    @Test
    void testMaximum() {
        int[] array = {7, 2, 9, 1, 5};

        DeterministicSelector selector = new DeterministicSelector();

        assertEquals(9, selector.select(array, array.length - 1));
    }

    @Test
    void testMiddleElement() {
        int[] array = {7, 2, 9, 1, 5};

        DeterministicSelector selector = new DeterministicSelector();

        assertEquals(5, selector.select(array, 2));
    }

    @Test
    void testDuplicateHeavyArray() {
        int[] array = {
                5, 1, 5, 3, 5,
                2, 5, 1, 3, 5,
                2, 5, 5, 1, 5
        };

        int[] expected = array.clone();
        Arrays.sort(expected);

        DeterministicSelector selector = new DeterministicSelector();

        for (int k = 0; k < array.length; k++) {
            int[] copy = array.clone();

            assertEquals(
                    expected[k],
                    selector.select(copy, k)
            );
        }
    }

    @Test
    void testSingleElement() {
        int[] array = {42};

        DeterministicSelector selector = new DeterministicSelector();

        assertEquals(42, selector.select(array, 0));
    }

    @Test
    void testInvalidK() {
        int[] array = {1, 2, 3};

        DeterministicSelector selector = new DeterministicSelector();

        assertThrows(
                IllegalArgumentException.class,
                () -> selector.select(array, -1)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> selector.select(array, 3)
        );
    }

    @Test
    void testAtLeast100RandomArrays() {
        Random random = new Random(42);

        DeterministicSelector selector =
                new DeterministicSelector();

        for (int test = 0; test < 100; test++) {

            int size = random.nextInt(100) + 1;

            int[] array = new int[size];

            for (int i = 0; i < size; i++) {
                array[i] = random.nextInt(1000);
            }

            int[] expected = array.clone();
            Arrays.sort(expected);

            int k = random.nextInt(size);

            int[] copy = array.clone();

            int actual = selector.select(copy, k);

            assertEquals(expected[k], actual);
        }
    }
}