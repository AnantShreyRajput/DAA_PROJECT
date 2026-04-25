package model;

import java.util.List;

/**
 * Encapsulates the result of a pathfinding algorithm run.
 * Stores the path, total cost, nodes explored, and execution time.
 */
public class PathResult {
    private final String algorithmName;
    private final List<Node> path;
    private final double totalCost;
    private final int nodesExplored;      // nodes popped from priority queue
    private final long executionTimeNs;   // nanoseconds

    public PathResult(String algorithmName, List<Node> path,
                      double totalCost, int nodesExplored, long executionTimeNs) {
        this.algorithmName  = algorithmName;
        this.path           = path;
        this.totalCost      = totalCost;
        this.nodesExplored  = nodesExplored;
        this.executionTimeNs = executionTimeNs;
    }

    public String getAlgorithmName()   { return algorithmName; }
    public List<Node> getPath()        { return path; }
    public double getTotalCost()       { return totalCost; }
    public int getNodesExplored()      { return nodesExplored; }
    public long getExecutionTimeNs()   { return executionTimeNs; }

    public boolean pathFound() {
        return path != null && !path.isEmpty();
    }

    /** Formatted execution time in microseconds. */
    public String formattedTime() {
        return String.format("%.3f µs", executionTimeNs / 1000.0);
    }

    /** Human-readable path string. */
    public String pathString() {
        if (!pathFound()) return "No path found";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < path.size(); i++) {
            sb.append(path.get(i).getName());
            if (i < path.size() - 1) sb.append(" → ");
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return String.format(
            "%-12s | Path: %-60s | Cost: %7.2f | Nodes Explored: %3d | Time: %s",
            algorithmName, pathString(), totalCost, nodesExplored, formattedTime()
        );
    }
}
