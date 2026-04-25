package algorithm;

import model.Graph;
import model.Node;
import model.PathResult;

/**
 * Common interface for shortest-path algorithms.
 * Both Dijkstra and A* implement this so they can be compared uniformly.
 */
public interface PathFinder {
    /**
     * Find the shortest path from source to destination in the given graph.
     *
     * @param graph  the road map graph
     * @param source starting node
     * @param dest   destination node
     * @return PathResult with path details and performance metrics
     */
    PathResult findPath(Graph graph, Node source, Node dest);

    /** Display name of the algorithm. */
    String getName();
}
