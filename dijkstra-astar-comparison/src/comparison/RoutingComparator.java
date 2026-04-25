package comparison;

import algorithm.AStarAlgorithm;
import algorithm.DijkstraAlgorithm;
import algorithm.PathFinder;
import model.Graph;
import model.Node;
import model.PathResult;

import java.util.Arrays;
import java.util.List;

/**
 * Runs both algorithms on the same graph/source/destination and
 * prints a side-by-side comparison report.
 */
public class RoutingComparator {

    private final List<PathFinder> algorithms;

    public RoutingComparator() {
        algorithms = Arrays.asList(new DijkstraAlgorithm(), new AStarAlgorithm());
    }

    /**
     * Run both algorithms on the given route and print results.
     *
     * @param graph    the map graph
     * @param source   starting node
     * @param dest     destination node
     * @param testName label for this test scenario
     */
    public void compare(Graph graph, Node source, Node dest, String testName) {
        System.out.println("\n" + "=".repeat(90));
        System.out.printf("  TEST: %-40s  |  Route: %s  →  %s%n",
                testName, source.getName(), dest.getName());
        System.out.println("=".repeat(90));

        PathResult dijkstraResult = null;
        PathResult astarResult    = null;

        for (PathFinder pf : algorithms) {
            PathResult result = pf.findPath(graph, source, dest);
            System.out.println(result);

            if (pf instanceof DijkstraAlgorithm) dijkstraResult = result;
            else                                  astarResult    = result;
        }

        // Analysis
        if (dijkstraResult != null && astarResult != null &&
            dijkstraResult.pathFound() && astarResult.pathFound()) {
            printAnalysis(dijkstraResult, astarResult);
        }
    }

    private void printAnalysis(PathResult d, PathResult a) {
        System.out.println("-".repeat(90));
        System.out.println("  ANALYSIS:");

        // Path cost check
        double costDiff = Math.abs(d.getTotalCost() - a.getTotalCost());
        if (costDiff < 1e-6) {
            System.out.println("  ✔  Both algorithms found the SAME optimal cost: "
                    + String.format("%.2f", d.getTotalCost()));
        } else {
            System.out.printf("  ⚠  Cost differs — Dijkstra: %.2f | A*: %.2f%n",
                    d.getTotalCost(), a.getTotalCost());
        }

        // Nodes explored
        int nodeDiff = d.getNodesExplored() - a.getNodesExplored();
        if (nodeDiff > 0) {
            System.out.printf("  ✔  A* explored %d fewer nodes than Dijkstra (%d vs %d) — %.1f%% less work%n",
                    nodeDiff, a.getNodesExplored(), d.getNodesExplored(),
                    (nodeDiff * 100.0 / d.getNodesExplored()));
        } else if (nodeDiff < 0) {
            System.out.printf("  !  Dijkstra explored fewer nodes (%d vs %d) — heuristic may not help here%n",
                    d.getNodesExplored(), a.getNodesExplored());
        } else {
            System.out.println("  =  Both algorithms explored the same number of nodes.");
        }

        // Time comparison (use absolute value to avoid noise on micro-benchmarks)
        long timeDiff = d.getExecutionTimeNs() - a.getExecutionTimeNs();
        System.out.printf("  ⏱  Time — Dijkstra: %s | A*: %s%n",
                d.formattedTime(), a.formattedTime());

        // Verdict
        System.out.println();
        System.out.println("  VERDICT:");
        if (a.getNodesExplored() < d.getNodesExplored()) {
            System.out.println("  → A* is more EFFICIENT for this route (fewer nodes explored).");
        } else {
            System.out.println("  → Both algorithms performed similarly — A* heuristic gain is minimal here.");
        }
        System.out.println("  → Both guarantee the OPTIMAL (shortest) path for admissible heuristics.");
    }

    /**
     * Print a summary table for multiple test results.
     */
    public static void printSummaryTable(List<PathResult[]> results, List<String> testNames) {
        System.out.println("\n\n" + "=".repeat(90));
        System.out.println("  SUMMARY TABLE");
        System.out.println("=".repeat(90));
        System.out.printf("  %-25s | %-10s | %-10s | %-14s | %-14s | %-14s%n",
                "Test", "Dijkstra", "A* Nodes", "Dijkstra Cost", "A* Cost", "Node Saving");
        System.out.printf("  %-25s | %-10s | %-10s | %-14s | %-14s | %-14s%n",
                "", "Nodes", "Explored", "", "", "(A* vs Dijk)");
        System.out.println("  " + "-".repeat(88));

        for (int i = 0; i < results.size(); i++) {
            PathResult d = results.get(i)[0];
            PathResult a = results.get(i)[1];
            int saving = d.getNodesExplored() - a.getNodesExplored();
            String savingStr = saving > 0 ? "-" + saving + " (" +
                    String.format("%.0f", saving * 100.0 / d.getNodesExplored()) + "%)" : "0";

            System.out.printf("  %-25s | %-10d | %-10d | %-14.2f | %-14.2f | %-14s%n",
                    testNames.get(i),
                    d.getNodesExplored(), a.getNodesExplored(),
                    d.getTotalCost(), a.getTotalCost(),
                    savingStr);
        }
        System.out.println("=".repeat(90));
    }
}
