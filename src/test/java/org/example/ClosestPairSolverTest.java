package org.example;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ClosestPairSolverTest {

    private static final double EPSILON = 1e-9;

    @Test
    void testSimplePoints() {
        Point[] points = {
                new Point(0, 0),
                new Point(3, 4),
                new Point(1, 1)
        };

        ClosestPairSolver solver = new ClosestPairSolver();

        double result = solver.findClosestDistance(points);

        assertEquals(
                Math.sqrt(2),
                result,
                EPSILON
        );
    }

    @Test
    void testDuplicatePoints() {
        Point[] points = {
                new Point(0, 0),
                new Point(5, 5),
                new Point(0, 0),
                new Point(10, 10)
        };

        ClosestPairSolver solver = new ClosestPairSolver();

        double result = solver.findClosestDistance(points);

        assertEquals(0.0, result, EPSILON);
    }

    @Test
    void testVerticalPoints() {
        Point[] points = {
                new Point(5, 1),
                new Point(5, 2),
                new Point(5, 10),
                new Point(5, 20)
        };

        ClosestPairSolver solver = new ClosestPairSolver();

        double result = solver.findClosestDistance(points);

        assertEquals(1.0, result, EPSILON);
    }

    @Test
    void testHorizontalPoints() {
        Point[] points = {
                new Point(1, 5),
                new Point(2, 5),
                new Point(10, 5)
        };

        ClosestPairSolver solver = new ClosestPairSolver();

        double result = solver.findClosestDistance(points);

        assertEquals(1.0, result, EPSILON);
    }

    @Test
    void testTwoPoints() {
        Point[] points = {
                new Point(0, 0),
                new Point(3, 4)
        };

        ClosestPairSolver solver = new ClosestPairSolver();

        double result = solver.findClosestDistance(points);

        assertEquals(5.0, result, EPSILON);
    }

    @Test
    void testRandomDatasetsAgainstBruteForce() {
        Random random = new Random(42);

        ClosestPairSolver solver =
                new ClosestPairSolver();

        for (int test = 0; test < 100; test++) {

            int size = random.nextInt(50) + 2;

            Point[] points = new Point[size];

            for (int i = 0; i < size; i++) {
                points[i] = new Point(
                        random.nextInt(1000),
                        random.nextInt(1000)
                );
            }

            double expected =
                    bruteForceDistance(points);

            double actual =
                    solver.findClosestDistance(points);

            assertEquals(
                    expected,
                    actual,
                    EPSILON
            );
        }
    }

    @Test
    void testRecursionForLargeDataset() {
        Random random = new Random(42);

        Point[] points = new Point[2000];

        for (int i = 0; i < points.length; i++) {
            points[i] = new Point(
                    random.nextDouble() * 10000,
                    random.nextDouble() * 10000
            );
        }

        ClosestPairSolver solver =
                new ClosestPairSolver();

        double result =
                solver.findClosestDistance(points);

        assertTrue(result >= 0);
        assertTrue(solver.getRecursiveCalls() > 1);
        assertTrue(solver.getMaxRecursionDepth() > 1);
    }

    private double bruteForceDistance(Point[] points) {

        double best = Double.POSITIVE_INFINITY;

        for (int i = 0; i < points.length; i++) {

            for (int j = i + 1; j < points.length; j++) {

                double distance =
                        points[i].distanceSquared(points[j]);

                best = Math.min(best, distance);
            }
        }

        return Math.sqrt(best);
    }
}