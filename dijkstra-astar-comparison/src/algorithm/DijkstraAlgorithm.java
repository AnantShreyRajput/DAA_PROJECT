package algorithm;

import model.Edge;
import model.Graph;
import model.Node;
import model.PathResult;

import java.util.*;

/**
 * ========================================================
 *  DIJKSTRA'S ALGORITHM
 * ========================================================
 * Classic single-source shortest path algorithm.
 *
 * How it works:
 *  1. Initialise all distances to INFINITY, source to 0.
 *  2. Use a min-heap (priority queue) ordered by g(n) — the known
 *     cost from source to n.
 *  3. Pop the node with the smallest g(n), relax its neighbours.
 *  4. Repeat until destination is popped.
 *
 * Key characteristic:
 *  - Explores in ALL directions equally (no directional guidance).
 *  - Guarantees the optimal path.
 *  - Time: O((V + E) log V)
 */
public class DijkstraAlgorithm implements PathFinder {

    @Override
    public String getName() { return "Dijkstra"; }

    @Override
    public PathResult findPath(Graph graph, Node source, Node dest) {
        long startTime = System.nanoTime();

        // g(n): best known cost from source to each node
        Map<Integer, Double> gCost = new HashMap<>();
        // previous node on best path (for backtracking)
        Map<Integer, Integer> previous = new HashMap<>();
        // PQ entries: [cost, nodeId]
        PriorityQueue<double[]> pq = new PriorityQueue<>(Comparator.comparingDouble(a -> a[0]));

        int nodesExplored = 0;

        // Initialise
        for (Node n : graph.getAllNodes()) gCost.put(n.getId(), Double.MAX_VALUE);
        gCost.put(source.getId(), 0.0);
        pq.offer(new double[]{0.0, source.getId()});

        Set<Integer> visited = new HashSet<>();

        while (!pq.isEmpty()) {
            double[] curr = pq.poll();
            double cost   = curr[0];
            int    nodeId = (int) curr[1];

            if (visited.contains(nodeId)) continue; // stale entry
            visited.add(nodeId);
            nodesExplored++;

            // Reached destination
            if (nodeId == dest.getId()) break;

            // Relax neighbours
            for (Edge edge : graph.getNeighbors(nodeId)) {
                int neighborId = edge.getTo().getId();
                if (visited.contains(neighborId)) continue;

                double newCost = cost + edge.getWeight();
                if (newCost < gCost.get(neighborId)) {
                    gCost.put(neighborId, newCost);
                    previous.put(neighborId, nodeId);
                    pq.offer(new double[]{newCost, neighborId});
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
        // Validate path starts at source
        if (path.isEmpty() || path.getFirst().getId() != sourceId) return new ArrayList<>();
        return new ArrayList<>(path);
    }
}
