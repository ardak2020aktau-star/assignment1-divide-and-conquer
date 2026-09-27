package org.example;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class MergeSorterTest {

    @Test
    void testRandomArray() {
        int[] array = {5, 2, 9, 1, 5, 6, 3, 8, 4, 7};

        int[] expected = array.clone();
        Arrays.sort(expected);

        MergeSorter sorter = new MergeSorter();
        sorter.sort(array);

        assertArrayEquals(expected, array);
    }

    @Test
    void testSortedArray() {
        int[] array = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

        int[] expected = array.clone();

        MergeSorter sorter = new MergeSorter();
        sorter.sort(array);

        assertArrayEquals(expected, array);
    }

    @Test
    void testReverseSortedArray() {
        int[] array = {10, 9, 8, 7, 6, 5, 4, 3, 2, 1};

        int[] expected = array.clone();
        Arrays.sort(expected);

        MergeSorter sorter = new MergeSorter();
        sorter.sort(array);

        assertArrayEquals(expected, array);
    }

    @Test
    void testDuplicateHeavyArray() {
        int[] array = {5, 1, 5, 3, 5, 2, 5, 1, 3, 5, 2, 5};

        int[] expected = array.clone();
        Arrays.sort(expected);

        MergeSorter sorter = new MergeSorter();
        sorter.sort(array);

        assertArrayEquals(expected, array);
    }

    @Test
    void testEmptyArray() {
        int[] array = {};

        MergeSorter sorter = new MergeSorter();
        sorter.sort(array);

        assertArrayEquals(new int[]{}, array);
    }

    @Test
    void testSingleElementArray() {
        int[] array = {42};

        MergeSorter sorter = new MergeSorter();
        sorter.sort(array);

        assertArrayEquals(new int[]{42}, array);
    }

    @Test
    void testRandomArrays() {
        Random random = new Random(42);
        MergeSorter sorter = new MergeSorter();

        for (int test = 0; test < 100; test++) {
            int size = random.nextInt(100) + 1;

            int[] array = new int[size];

            for (int i = 0; i < size; i++) {
                array[i] = random.nextInt(1000);
            }

            int[] expected = array.clone();
            Arrays.sort(expected);

            sorter.sort(array);

            assertArrayEquals(expected, array);
        }
    }
}