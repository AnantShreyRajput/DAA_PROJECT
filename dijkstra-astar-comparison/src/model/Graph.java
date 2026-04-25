package model;

import java.util.*;

/**
 * Adjacency-list graph representing a road map.
 * Supports both directed and undirected (bidirectional) edges.
 */
public class Graph {
    private final Map<Integer, Node> nodes = new LinkedHashMap<>();
    private final Map<Integer, List<Edge>> adjacencyList = new HashMap<>();

    /** Add a node to the graph. */
    public void addNode(Node node) {
        nodes.put(node.getId(), node);
        adjacencyList.putIfAbsent(node.getId(), new ArrayList<>());
    }

    /**
     * Add a directed edge from -> to with given weight.
     * For undirected (road) edges call addEdge twice or use addBidirectionalEdge.
     */
    public void addEdge(Node from, Node to, double weight) {
        adjacencyList.get(from.getId()).add(new Edge(from, to, weight));
    }

    /** Add an undirected (road) edge between two nodes. */
    public void addBidirectionalEdge(Node from, Node to, double weight) {
        addEdge(from, to, weight);
        addEdge(to, from, weight);
    }

    public Node getNode(int id) {
        return nodes.get(id);
    }

    public Collection<Node> getAllNodes() {
        return nodes.values();
    }

    public List<Edge> getNeighbors(int nodeId) {
        return adjacencyList.getOrDefault(nodeId, Collections.emptyList());
    }

    public int nodeCount() { return nodes.size(); }
    public int edgeCount() {
        return adjacencyList.values().stream().mapToInt(List::size).sum();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Graph (" + nodeCount() + " nodes, " + edgeCount() + " edges):\n");
        for (Node n : nodes.values()) {
            sb.append("  ").append(n.getName()).append(" -> ");
            List<Edge> edges = adjacencyList.get(n.getId());
            if (edges.isEmpty()) sb.append("(no outgoing edges)");
            else edges.forEach(e -> sb.append(e.getTo().getName())
                    .append("(").append(String.format("%.1f", e.getWeight())).append(") "));
            sb.append("\n");
        }
        return sb.toString();
    }
}
