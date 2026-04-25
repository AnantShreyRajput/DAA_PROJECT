package model;

/**
 * Represents a directed weighted edge between two nodes.
 * Weight typically represents road distance or travel cost.
 */
public class Edge {
    private final Node from;
    private final Node to;
    private final double weight;

    public Edge(Node from, Node to, double weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
    }

    public Node getFrom()   { return from; }
    public Node getTo()     { return to; }
    public double getWeight() { return weight; }

    @Override
    public String toString() {
        return String.format("%s --> %s (%.2f)", from.getName(), to.getName(), weight);
    }
}
