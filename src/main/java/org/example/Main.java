package org.example;

public class Main {

    public static void main(String[] args) {

        System.out.println("======================================");
        System.out.println("  DAA Assignment 1");
        System.out.println("  Divide-and-Conquer Algorithms");
        System.out.println("======================================");
        System.out.println();

        System.out.println("Algorithms:");
        System.out.println("1. MergeSort");
        System.out.println("2. QuickSort");
        System.out.println("3. Deterministic Select");
        System.out.println("4. Closest Pair of Points");
        System.out.println();

        System.out.println("Starting experiments...");
        System.out.println();

        Experiment experiment = new Experiment();
        experiment.run();

        System.out.println();
        System.out.println("======================================");
        System.out.println("  All experiments finished.");
        System.out.println("======================================");
    }
}