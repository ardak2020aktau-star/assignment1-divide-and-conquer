package org.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class ClosestPairSolver {

    private static final double INFINITY = Double.POSITIVE_INFINITY;

    private long comparisons;
    private long recursiveCalls;
    private int maxRecursionDepth;

    private static final Comparator<Point> BY_X =
            Comparator.comparingDouble(Point::getX)
                    .thenComparingDouble(Point::getY);

    private static final Comparator<Point> BY_Y =
            Comparator.comparingDouble(Point::getY)
                    .thenComparingDouble(Point::getX);

    public double findClosestDistance(Point[] points) {
        if (points == null || points.length < 2) {
            return INFINITY;
        }

        resetMetrics();

        Point[] pointsByX = points.clone();
        Arrays.sort(pointsByX, BY_X);

        Point[] pointsByY = points.clone();
        Arrays.sort(pointsByY, BY_Y);

        return Math.sqrt(
                closestDistance(
                        pointsByX,
                        pointsByY,
                        1
                )
        );
    }

    private double closestDistance(
            Point[] pointsByX,
            Point[] pointsByY,
            int depth
    ) {
        recursiveCalls++;
        maxRecursionDepth =
                Math.max(maxRecursionDepth, depth);

        int n = pointsByX.length;

        // Base case.
        if (n <= 3) {
            return bruteForceSquared(pointsByX);
        }

        int middle = n / 2;

        Point[] leftByX =
                Arrays.copyOfRange(pointsByX, 0, middle);

        Point[] rightByX =
                Arrays.copyOfRange(pointsByX, middle, n);

        double middleX =
                pointsByX[middle].getX();

        List<Point> leftYList = new ArrayList<>();
        List<Point> rightYList = new ArrayList<>();

        /*
         * Divide the points that are already sorted by Y
         * into left and right sets.
         */
        for (Point point : pointsByY) {
            if (point.getX() < middleX) {
                leftYList.add(point);
            } else if (point.getX() > middleX) {
                rightYList.add(point);
            } else {
                /*
                 * Points with the same X-coordinate must be
                 * distributed according to how many of them
                 * belong to the left half.
                 */
                if (leftYList.size() < leftByX.length) {
                    leftYList.add(point);
                } else {
                    rightYList.add(point);
                }
            }
        }

        Point[] leftByY =
                leftYList.toArray(new Point[0]);

        Point[] rightByY =
                rightYList.toArray(new Point[0]);

        double leftDistance =
                closestDistance(
                        leftByX,
                        leftByY,
                        depth + 1
                );

        double rightDistance =
                closestDistance(
                        rightByX,
                        rightByY,
                        depth + 1
                );

        double delta =
                Math.min(leftDistance, rightDistance);

        /*
         * Build the strip around the dividing line.
         */
        List<Point> strip = new ArrayList<>();

        for (Point point : pointsByY) {
            double dx = point.getX() - middleX;

            comparisons++;

            if (dx * dx < delta) {
                strip.add(point);
            }
        }

        /*
         * Points in strip are already sorted by Y.
         *
         * For each point, only a constant number of following
         * points need to be checked.
         */
        double best = delta;

        for (int i = 0; i < strip.size(); i++) {

            for (int j = i + 1; j < strip.size(); j++) {

                double dy =
                        strip.get(j).getY()
                                - strip.get(i).getY();

                comparisons++;

                if (dy * dy >= best) {
                    break;
                }

                double distance =
                        strip.get(i)
                                .distanceSquared(strip.get(j));

                comparisons++;

                best = Math.min(best, distance);
            }
        }

        return best;
    }

    private double bruteForceSquared(Point[] points) {

        double best = INFINITY;

        for (int i = 0; i < points.length; i++) {

            for (int j = i + 1; j < points.length; j++) {

                comparisons++;

                best = Math.min(
                        best,
                        points[i].distanceSquared(points[j])
                );
            }
        }

        return best;
    }

    private void resetMetrics() {
        comparisons = 0;
        recursiveCalls = 0;
        maxRecursionDepth = 0;
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