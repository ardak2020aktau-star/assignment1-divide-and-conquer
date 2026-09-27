package org.example;

public class MergeSorter {

    private static final int INSERTION_SORT_CUTOFF = 16;

    private long comparisons;
    private long recursiveCalls;
    private int maxRecursionDepth;

    private int[] buffer;

    public void sort(int[] array) {
        if (array == null || array.length <= 1) {
            return;
        }

        // One reusable auxiliary buffer for the whole sorting process.
        buffer = new int[array.length];

        comparisons = 0;
        recursiveCalls = 0;
        maxRecursionDepth = 0;

        mergeSort(array, 0, array.length - 1, 1);
    }

    private void mergeSort(int[] array, int left, int right, int depth) {
        recursiveCalls++;
        maxRecursionDepth = Math.max(maxRecursionDepth, depth);

        if (left >= right) {
            return;
        }

        // Use Insertion Sort for small subarrays.
        if (right - left + 1 <= INSERTION_SORT_CUTOFF) {
            insertionSort(array, left, right);
            return;
        }

        int middle = left + (right - left) / 2;

        mergeSort(array, left, middle, depth + 1);
        mergeSort(array, middle + 1, right, depth + 1);

        // If already ordered, no merge is necessary.
        comparisons++;
        if (array[middle] <= array[middle + 1]) {
            return;
        }

        merge(array, left, middle, right);
    }

    private void insertionSort(int[] array, int left, int right) {
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

    private void merge(int[] array, int left, int middle, int right) {
        int i = left;
        int j = middle + 1;
        int k = left;

        // Linear merge of two sorted halves.
        while (i <= middle && j <= right) {
            comparisons++;

            if (array[i] <= array[j]) {
                buffer[k++] = array[i++];
            } else {
                buffer[k++] = array[j++];
            }
        }

        while (i <= middle) {
            buffer[k++] = array[i++];
        }

        while (j <= right) {
            buffer[k++] = array[j++];
        }

        // Copy merged result back into the original array.
        for (int index = left; index <= right; index++) {
            array[index] = buffer[index];
        }
    }

    public long getComparisons() {
        return comparisons;
    }

    public long getRecursiveCalls() {
        return recursiveCalls;
    }

    public int getMaxRecursionDepth() {
        return maxRecursionDepth;
    }
}