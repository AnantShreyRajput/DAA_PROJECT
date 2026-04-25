package model;

/**
 * Represents a node (location) on the map.
 * Each node has an ID, a name, and geographic coordinates (lat/lon or x/y).
 */
public class Node {
    private final int id;
    private final String name;
    private final double x; // longitude or x-coordinate
    private final double y; // latitude or y-coordinate

    public Node(int id, String name, double x, double y) {
        this.id = id;
        this.name = name;
        this.x = x;
        this.y = y;
    }

    public int getId()     { return id; }
    public String getName(){ return name; }
    public double getX()   { return x; }
    public double getY()   { return y; }

    /**
     * Euclidean distance to another node — used as heuristic in A*.
     */
    public double heuristicTo(Node other) {
        double dx = this.x - other.x;
        double dy = this.y - other.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    @Override
    public String toString() {
        return String.format("[%d] %s (%.2f, %.2f)", id, name, x, y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Node)) return false;
        return id == ((Node) o).id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
