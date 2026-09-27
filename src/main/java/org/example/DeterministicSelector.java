package org.example;

public class DeterministicSelector {

    private long comparisons;
    private long swaps;
    private long recursiveCalls;
    private int maxRecursionDepth;

    /**
     * Finds the k-th smallest element in the array.
     *
     * k is zero-based:
     * k = 0 -> smallest element
     * k = 1 -> second smallest element
     * ...
     *
     * The array may be modified during the operation.
     */
    public int select(int[] array, int k) {
        comparisons = 0;
        swaps = 0;
        recursiveCalls = 0;
        maxRecursionDepth = 0;

        if (array == null || array.length == 0) {
            throw new IllegalArgumentException("Array must not be null or empty.");
        }

        if (k < 0 || k >= array.length) {
            throw new IllegalArgumentException(
                    "k must be between 0 and array.length - 1."
            );
        }

        return selectRange(array, 0, array.length - 1, k, 1);
    }

    private int selectRange(
            int[] array,
            int left,
            int right,
            int k,
            int depth
    ) {
        recursiveCalls++;
        maxRecursionDepth = Math.max(maxRecursionDepth, depth);

        if (left == right) {
            return array[left];
        }

        // Find a deterministic pivot using Median-of-Medians.
        int pivot = medianOfMedians(array, left, right, depth + 1);

        // Three-way in-place partition.
        int[] equalRange = partition(array, left, right, pivot);

        int equalStart = equalRange[0];
        int equalEnd = equalRange[1];

        /*
         * Recurse ONLY into the partition containing k.
         */
        if (k < equalStart) {
            return selectRange(
                    array,
                    left,
                    equalStart - 1,
                    k,
                    depth + 1
            );
        }

        if (k > equalEnd) {
            return selectRange(
                    array,
                    equalEnd + 1,
                    right,
                    k,
                    depth + 1
            );
        }

        return pivot;
    }

    /**
     * Median-of-Medians:
     *
     * 1. Divide the range into groups of at most 5.
     * 2. Sort each group.
     * 3. Move each group's median to the beginning of the range.
     * 4. Recursively find the median of those medians.
     */
    private int medianOfMedians(
            int[] array,
            int left,
            int right,
            int depth
    ) {
        int length = right - left + 1;

        if (length <= 5) {
            insertionSort(array, left, right);

            return array[left + (length - 1) / 2];
        }

        int numberOfGroups = 0;

        for (int groupStart = left; groupStart <= right; groupStart += 5) {

            int groupEnd = Math.min(groupStart + 4, right);

            insertionSort(array, groupStart, groupEnd);

            int medianIndex =
                    groupStart + (groupEnd - groupStart) / 2;

            int medianStoreIndex = left + numberOfGroups;

            swap(array, medianStoreIndex, medianIndex);

            numberOfGroups++;
        }

        int mediansLeft = left;
        int mediansRight = left + numberOfGroups - 1;

        int medianOfMediansIndex =
                mediansLeft + (numberOfGroups - 1) / 2;

        return selectRange(
                array,
                mediansLeft,
                mediansRight,
                medianOfMediansIndex,
                depth
        );
    }

    /**
     * Three-way partition around the pivot value:
     *
     * [less than pivot] [equal to pivot] [greater than pivot]
     */
    private int[] partition(
            int[] array,
            int left,
            int right,
            int pivot
    ) {
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

    private void insertionSort(
            int[] array,
            int left,
            int right
    ) {
        for (int i = left + 1; i <= right; i++) {
            int key = array[i];
            int j = i - 1;

            while (j >= left) {
                comparisons++;

                if (array[j] <= key) {
                    break;
                }

                array[j + 1] = array[j];
                j--;
            }

            array[j + 1] = key;
        }
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