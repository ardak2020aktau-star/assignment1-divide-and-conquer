package org.example;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Random;

public class Experiment {

    private static final int[] SIZES = {
            100,
            500,
            1000,
            5000,
            10000,
            20000
    };

    private static final int REPETITIONS = 3;

    private final Random random = new Random(42);

    public void run() {
        String filePath = "results/results.csv";

        createResultsDirectory();

        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {

            writer.println(
                    "algorithm,input_type,n,time_ns,recursion_depth,comparisons,swaps,recursive_calls"
            );

            runSortingExperiments(writer);
            runSelectionExperiments(writer);
            runClosestPairExperiments(writer);

            System.out.println();
            System.out.println("Experiments completed.");
            System.out.println("Results saved to: " + filePath);

        } catch (IOException e) {
            System.err.println(
                    "Could not save results: " + e.getMessage()
            );
        }
    }

    private void runSortingExperiments(PrintWriter writer) {

        String[] inputTypes = {
                "random",
                "sorted",
                "reverse_sorted",
                "duplicate_heavy"
        };

        for (int n : SIZES) {

            for (String inputType : inputTypes) {

                int[] original =
                        generateArray(n, inputType);

                runMergeSort(
                        writer,
                        original,
                        inputType
                );

                runQuickSort(
                        writer,
                        original,
                        inputType
                );
            }
        }
    }

    private void runMergeSort(
            PrintWriter writer,
            int[] original,
            String inputType
    ) {

        long bestTime = Long.MAX_VALUE;

        long bestComparisons = 0;
        long bestSwaps = 0;
        long bestRecursiveCalls = 0;
        int bestDepth = 0;

        for (int repetition = 0;
             repetition < REPETITIONS;
             repetition++) {

            int[] array = original.clone();

            MergeSorter sorter =
                    new MergeSorter();

            long start = System.nanoTime();

            sorter.sort(array);

            long end = System.nanoTime();

            long time = end - start;

            if (time < bestTime) {
                bestTime = time;
                bestComparisons =
                        sorter.getComparisons();
                bestSwaps =
                        0;
                bestRecursiveCalls =
                        sorter.getRecursiveCalls();
                bestDepth =
                        sorter.getMaxRecursionDepth();
            }
        }

        writeResult(
                writer,
                "MergeSort",
                inputType,
                original.length,
                bestTime,
                bestDepth,
                bestComparisons,
                bestSwaps,
                bestRecursiveCalls
        );
    }

    private void runQuickSort(
            PrintWriter writer,
            int[] original,
            String inputType
    ) {

        long bestTime = Long.MAX_VALUE;

        long bestComparisons = 0;
        long bestSwaps = 0;
        long bestRecursiveCalls = 0;
        int bestDepth = 0;

        for (int repetition = 0;
             repetition < REPETITIONS;
             repetition++) {

            int[] array = original.clone();

            QuickSorter sorter =
                    new QuickSorter();

            long start = System.nanoTime();

            sorter.sort(array);

            long end = System.nanoTime();

            long time = end - start;

            if (time < bestTime) {
                bestTime = time;
                bestComparisons =
                        sorter.getComparisons();
                bestSwaps =
                        sorter.getSwaps();
                bestRecursiveCalls =
                        sorter.getRecursiveCalls();
                bestDepth =
                        sorter.getMaxRecursionDepth();
            }
        }

        writeResult(
                writer,
                "QuickSort",
                inputType,
                original.length,
                bestTime,
                bestDepth,
                bestComparisons,
                bestSwaps,
                bestRecursiveCalls
        );
    }

    private void runSelectionExperiments(
            PrintWriter writer
    ) {

        String[] inputTypes = {
                "random",
                "sorted",
                "reverse_sorted",
                "duplicate_heavy"
        };

        for (int n : SIZES) {

            for (String inputType : inputTypes) {

                int[] original =
                        generateArray(n, inputType);

                int k = n / 2;

                long bestTime = Long.MAX_VALUE;

                long bestComparisons = 0;
                long bestSwaps = 0;
                long bestRecursiveCalls = 0;
                int bestDepth = 0;

                for (int repetition = 0;
                     repetition < REPETITIONS;
                     repetition++) {

                    int[] array =
                            original.clone();

                    DeterministicSelector selector =
                            new DeterministicSelector();

                    long start =
                            System.nanoTime();

                    selector.select(array, k);

                    long end =
                            System.nanoTime();

                    long time = end - start;

                    if (time < bestTime) {
                        bestTime = time;

                        bestComparisons =
                                selector.getComparisons();

                        bestSwaps =
                                selector.getSwaps();

                        bestRecursiveCalls =
                                selector.getRecursiveCalls();

                        bestDepth =
                                selector.getMaxRecursionDepth();
                    }
                }

                writeResult(
                        writer,
                        "DeterministicSelect",
                        inputType,
                        n,
                        bestTime,
                        bestDepth,
                        bestComparisons,
                        bestSwaps,
                        bestRecursiveCalls
                );
            }
        }
    }

    private void runClosestPairExperiments(
            PrintWriter writer
    ) {

        for (int n : SIZES) {

            Point[] original =
                    generatePoints(n);

            long bestTime = Long.MAX_VALUE;

            long bestComparisons = 0;
            long bestRecursiveCalls = 0;
            int bestDepth = 0;

            for (int repetition = 0;
                 repetition < REPETITIONS;
                 repetition++) {

                Point[] points =
                        original.clone();

                ClosestPairSolver solver =
                        new ClosestPairSolver();

                long start =
                        System.nanoTime();

                solver.findClosestDistance(points);

                long end =
                        System.nanoTime();

                long time = end - start;

                if (time < bestTime) {
                    bestTime = time;

                    bestComparisons =
                            solver.getComparisons();

                    bestRecursiveCalls =
                            solver.getRecursiveCalls();

                    bestDepth =
                            solver.getMaxRecursionDepth();
                }
            }

            writeResult(
                    writer,
                    "ClosestPair",
                    "random",
                    n,
                    bestTime,
                    bestDepth,
                    bestComparisons,
                    -1,
                    bestRecursiveCalls
            );
        }
    }

    private int[] generateArray(
            int n,
            String type
    ) {

        int[] array = new int[n];

        switch (type) {

            case "random":

                for (int i = 0; i < n; i++) {
                    array[i] =
                            random.nextInt(n * 10 + 1);
                }

                break;

            case "sorted":

                for (int i = 0; i < n; i++) {
                    array[i] = i;
                }

                break;

            case "reverse_sorted":

                for (int i = 0; i < n; i++) {
                    array[i] = n - i;
                }

                break;

            case "duplicate_heavy":

                for (int i = 0; i < n; i++) {
                    array[i] =
                            random.nextInt(10);
                }

                break;

            default:
                throw new IllegalArgumentException(
                        "Unknown input type: " + type
                );
        }

        return array;
    }

    private Point[] generatePoints(int n) {

        Point[] points = new Point[n];

        for (int i = 0; i < n; i++) {

            double x =
                    random.nextDouble() * 10000;

            double y =
                    random.nextDouble() * 10000;

            points[i] =
                    new Point(x, y);
        }

        return points;
    }

    private void writeResult(
            PrintWriter writer,
            String algorithm,
            String inputType,
            int n,
            long timeNs,
            int recursionDepth,
            long comparisons,
            long swaps,
            long recursiveCalls
    ) {

        writer.println(
                algorithm + "," +
                        inputType + "," +
                        n + "," +
                        timeNs + "," +
                        recursionDepth + "," +
                        comparisons + "," +
                        swaps + "," +
                        recursiveCalls
        );
    }

    private void createResultsDirectory() {

        File directory =
                new File("results");

        if (!directory.exists()) {
            directory.mkdirs();
        }
    }
}