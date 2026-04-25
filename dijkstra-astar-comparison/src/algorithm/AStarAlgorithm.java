package algorithm;

import model.Edge;
import model.Graph;
import model.Node;
import model.PathResult;

import java.util.*;

/**
 * ========================================================
 *  A* (A-STAR) ALGORITHM
 * ========================================================
 * Informed search that uses a heuristic to guide exploration
 * toward the destination, resulting in fewer explored nodes
 * compared to Dijkstra on map-routing problems.
 *
 * How it works:
 *  1. Like Dijkstra, maintain g(n) — cost from source to n.
 *  2. Additionally compute f(n) = g(n) + h(n), where
 *     h(n) = heuristic estimate of cost from n to destination.
 *  3. Explore nodes in order of f(n) (not just g(n)).
 *
 * Heuristic used here:
 *  - Euclidean distance between node coordinates.
 *  - This is admissible (never overestimates) if edge weights
 *    reflect straight-line distances, so A* is optimal.
 *
 * Key advantage over Dijkstra:
 *  - Explores far fewer nodes because it "looks ahead" toward
 *    the goal instead of expanding in all directions.
 *  - Time: O((V + E) log V) worst-case, but much better in practice.
 */
public class AStarAlgorithm implements PathFinder {

    @Override
    public String getName() { return "A*"; }

    @Override
    public PathResult findPath(Graph graph, Node source, Node dest) {
        long startTime = System.nanoTime();

        // g(n): cost from source to n
        Map<Integer, Double> gCost = new HashMap<>();
        // previous node for path reconstruction
        Map<Integer, Integer> previous = new HashMap<>();
        // PQ ordered by f(n) = g(n) + h(n)
        PriorityQueue<double[]> openSet = new PriorityQueue<>(Comparator.comparingDouble(a -> a[0]));

        int nodesExplored = 0;

        // Initialise
        for (Node n : graph.getAllNodes()) gCost.put(n.getId(), Double.MAX_VALUE);
        gCost.put(source.getId(), 0.0);
        double h0 = source.heuristicTo(dest);
        openSet.offer(new double[]{h0, source.getId()}); // f = 0 + h

        Set<Integer> closedSet = new HashSet<>();

        while (!openSet.isEmpty()) {
            double[] curr = openSet.poll();
            int nodeId    = (int) curr[1];

            if (closedSet.contains(nodeId)) continue;
            closedSet.add(nodeId);
            nodesExplored++;

            // Reached destination
            if (nodeId == dest.getId()) break;

            for (Edge edge : graph.getNeighbors(nodeId)) {
                int neighborId = edge.getTo().getId();
                if (closedSet.contains(neighborId)) continue;

                double tentativeG = gCost.get(nodeId) + edge.getWeight();
                if (tentativeG < gCost.get(neighborId)) {
                    gCost.put(neighborId, tentativeG);
                    previous.put(neighborId, nodeId);

                    // f(neighbor) = g(neighbor) + h(neighbor)
                    double h = graph.getNode(neighborId).heuristicTo(dest);
                    openSet.offer(new double[]{tentativeG + h, neighborId});
                }
            }
        }

        long execTime = System.nanoTime() - startTime;
        List<Node> path = reconstructPath(graph, previous, source.getId(), dest.getId());
        double totalCost = gCost.getOrDefault(dest.getId(), Double.MAX_VALUE);

        return new PathResult(getName(), path, totalCost == Double.MAX_VALUE ? -1 : totalCost,
                              nodesExplored, execTime);
    }

    /** Backtrack from destination to source using the previous-node map. */
    private List<Node> reconstructPath(Graph graph, Map<Integer, Integer> previous,
                                       int sourceId, int destId) {
        LinkedList<Node> path = new LinkedList<>();
        Integer curr = destId;
        while (curr != null) {
            path.addFirst(graph.getNode(curr));
            curr = previous.get(curr);
            if (curr != null && curr == sourceId) {
                path.addFirst(graph.getNode(sourceId));
                break;
            }
        }
        if (path.isEmpty() || path.getFirst().getId() != sourceId) return new ArrayList<>();
        return new ArrayList<>(path);
    }
}
