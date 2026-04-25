import algorithm.AStarAlgorithm;
import algorithm.DijkstraAlgorithm;
import comparison.RoutingComparator;
import map.MapFactory;
import model.Graph;
import model.Node;
import model.PathResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * ============================================================
 *  Main entry point — Dijkstra vs A* on Map Routing
 * ============================================================
 *
 *  Project structure:
 *    src/
 *      model/          Node, Edge, Graph, PathResult
 *      algorithm/      PathFinder (interface), DijkstraAlgorithm, AStarAlgorithm
 *      map/            MapFactory  (sample road networks)
 *      comparison/     RoutingComparator
 *      Main.java       ← you are here
 *
 *  Compile & Run:
 *    Windows PowerShell (from project root):
 *      javac -d out (Get-ChildItem -Recurse -Filter "*.java" src | ForEach-Object { $_.FullName })
 *      java -cp out Main
 *
 *    Linux / macOS:
 *      find src -name "*.java" | xargs javac -d out
 *      java -cp out Main
 */
public class Main {

    public static void main(String[] args) {
        printHeader();

        RoutingComparator comparator = new RoutingComparator();
        List<PathResult[]> allResults = new ArrayList<>();
        List<String>       testNames  = new ArrayList<>();

        // ────────────────────────────────────────────────────
        //  MAP A — Small city grid (10 nodes)
        // ────────────────────────────────────────────────────
        Graph smallMap = MapFactory.buildSmallCityMap();
        System.out.println("\n📍 MAP A — Small City Grid");
        System.out.println(smallMap);

        runTest(comparator, smallMap, 0, 9, "Small Map: Market → Hotel",
                allResults, testNames);
        runTest(comparator, smallMap, 0, 8, "Small Map: Market → Airport",
                allResults, testNames);
        runTest(comparator, smallMap, 2, 6, "Small Map: School → Station",
                allResults, testNames);

        // ────────────────────────────────────────────────────
        //  MAP B — Larger city network (15 nodes)
        // ────────────────────────────────────────────────────
        Graph largeMap = MapFactory.buildLargerCityMap();
        System.out.println("\n\n📍 MAP B — Larger City Network");
        System.out.println(largeMap);

        runTest(comparator, largeMap, 0,  14, "Large Map: North-Gate → Sea-Port",
                allResults, testNames);
        runTest(comparator, largeMap, 0,  11, "Large Map: North-Gate → Harbor",
                allResults, testNames);
        runTest(comparator, largeMap, 3,  12, "Large Map: Airport → South-Gate",
                allResults, testNames);
        runTest(comparator, largeMap, 1,  13, "Large Map: University → Train-Station",
                allResults, testNames);

        // ────────────────────────────────────────────────────
        //  Summary table
        // ────────────────────────────────────────────────────
        RoutingComparator.printSummaryTable(allResults, testNames);

        printConclusion();
    }

    private static void runTest(RoutingComparator comparator, Graph graph,
                                int sourceId, int destId, String label,
                                List<PathResult[]> allResults, List<String> testNames) {
        Node source = graph.getNode(sourceId);
        Node dest   = graph.getNode(destId);

        DijkstraAlgorithm dijkstra = new DijkstraAlgorithm();
        AStarAlgorithm    astar    = new AStarAlgorithm();

        comparator.compare(graph, source, dest, label);

        PathResult dr = dijkstra.findPath(graph, source, dest);
        PathResult ar = astar.findPath(graph, source, dest);
        allResults.add(new PathResult[]{dr, ar});
        testNames.add(label.length() > 25 ? label.substring(0, 22) + "..." : label);
    }

    private static void printHeader() {
        System.out.println("╔══════════════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║          DIJKSTRA  vs  A*  —  Best Routing Algorithm for Maps  (Java)                   ║");
        System.out.println("╠══════════════════════════════════════════════════════════════════════════════════════════╣");
        System.out.println("║  Dijkstra: Explores all directions uniformly. Optimal but slow on large graphs.         ║");
        System.out.println("║  A*:       Uses a heuristic (Euclidean distance) to guide search toward destination.    ║");
        System.out.println("║           Explores fewer nodes — faster in practice on geographic maps.                  ║");
        System.out.println("╚══════════════════════════════════════════════════════════════════════════════════════════╝");
    }

    private static void printConclusion() {
        System.out.println("\n\n" + "═".repeat(90));
        System.out.println("  CONCLUSION");
        System.out.println("═".repeat(90));
        System.out.println("""
                  Both algorithms guarantee the optimal (shortest) path when A*'s heuristic is admissible.

                  Key differences for MAP ROUTING:
                  ┌─────────────────────┬──────────────────────────────┬───────────────────────────────┐
                  │  Property           │  Dijkstra                    │  A*                           │
                  ├─────────────────────┼──────────────────────────────┼───────────────────────────────┤
                  │  Heuristic          │  None (g(n) only)            │  f(n) = g(n) + h(n)           │
                  │  Search direction   │  All directions equally      │  Biased toward destination    │
                  │  Nodes explored     │  More (entire frontier)      │  Fewer (guided search)        │
                  │  Optimality         │  Always optimal              │  Optimal if h admissible      │
                  │  Best use case      │  Unknown destination / SSSP  │  Single point-to-point routing│
                  │  Map routing verdict│  Works, but slower           │  ✔ Better for map routing     │
                  └─────────────────────┴──────────────────────────────┴───────────────────────────────┘

                  → For map routing (GPS navigation), A* is the preferred algorithm because it
                    leverages geographic coordinates as a heuristic to significantly reduce
                    search space while still guaranteeing the shortest path.
                """);
    }
}
