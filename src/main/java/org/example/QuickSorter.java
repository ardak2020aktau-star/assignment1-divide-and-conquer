package org.example;

import java.util.concurrent.ThreadLocalRandom;

public class QuickSorter {

    private long comparisons;
    private long swaps;
    private long recursiveCalls;
    private int maxRecursionDepth;

    public void sort(int[] array) {
        comparisons = 0;
        swaps = 0;
        recursiveCalls = 0;
        maxRecursionDepth = 0;

        if (array == null || array.length <= 1) {
            return;
        }

        quickSort(array, 0, array.length - 1, 1);
    }

    private void quickSort(int[] array, int left, int right, int depth) {
        while (left < right) {
            recursiveCalls++;
            maxRecursionDepth = Math.max(maxRecursionDepth, depth);

            int[] partition = partition(array, left, right);

            int leftPartSize = partition[0] - left;
            int rightPartSize = right - partition[1];

            /*
             * Recurse into the smaller partition.
             * Iterate over the larger partition.
             *
             * This keeps the recursion depth O(log n).
             */
            if (leftPartSize < rightPartSize) {
                if (left < partition[0] - 1) {
                    quickSort(array, left, partition[0] - 1, depth + 1);
                }

                left = partition[1] + 1;
            } else {
                if (partition[1] + 1 < right) {
                    quickSort(array, partition[1] + 1, right, depth + 1);
                }

                right = partition[0] - 1;
            }
        }
    }

    /*
     * Three-way in-place partition:
     *
     * [left ... less-1]       < pivot
     * [less ... greater]      = pivot
     * [greater+1 ... right]   > pivot
     */
    private int[] partition(int[] array, int left, int right) {
        int pivotIndex = ThreadLocalRandom.current().nextInt(left, right + 1);
        int pivot = array[pivotIndex];

        int less = left;
        int current = left;
        int greater = right;

        while (current <= greater) {
            comparisons++;

            if (array[current] < pivot) {
                swap(array, less, current);
                less++;
                current++;
            } else if (array[current] > pivot) {
                swap(array, current, greater);
                greater--;
            } else {
                current++;
            }
        }

        return new int[]{less, greater};
    }

    private void swap(int[] array, int i, int j) {
        if (i == j) {
            return;
        }

        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;

        swaps++;
    }

    public long getComparisons() {
        return comparisons;
    }

    public long getSwaps() {
        return swaps;
    }

    public long getRecursiveCalls() {
        return recursiveCalls;
    }

    public int getMaxRecursionDepth() {
        return maxRecursionDepth;
    }
}