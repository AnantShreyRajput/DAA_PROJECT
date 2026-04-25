package map;

import model.Graph;
import model.Node;

/**
 * Factory class that builds sample map graphs for testing.
 *
 * MapA — Small city grid (10 nodes)
 * MapB — Larger road network (15 nodes) with more complex routing
 */
public class MapFactory {

    /**
     * Small city map with 10 intersections.
     * Coordinates are approximate (x = longitude-like, y = latitude-like).
     *
     * Layout (rough):
     *
     *   A(0) --- B(1) --- C(2)
     *   |        |         |
     *   D(3) --- E(4) --- F(5)
     *   |        |         |
     *   G(6) --- H(7) --- I(8) --- J(9)
     */
    public static Graph buildSmallCityMap() {
        Graph g = new Graph();

        Node a = new Node(0, "A-Market",    0, 4);
        Node b = new Node(1, "B-Library",   2, 4);
        Node c = new Node(2, "C-School",    4, 4);
        Node d = new Node(3, "D-Hospital",  0, 2);
        Node e = new Node(4, "E-Center",    2, 2);
        Node f = new Node(5, "F-Park",      4, 2);
        Node h = new Node(6, "G-Station",   0, 0);
        Node i = new Node(7, "H-Mall",      2, 0);
        Node j = new Node(8, "I-Airport",   4, 0);
        Node k = new Node(9, "J-Hotel",     6, 0);

        for (Node n : new Node[]{a, b, c, d, e, f, h, i, j, k}) g.addNode(n);

        // Horizontal roads
        g.addBidirectionalEdge(a, b, 2.5);
        g.addBidirectionalEdge(b, c, 2.5);
        g.addBidirectionalEdge(d, e, 2.5);
        g.addBidirectionalEdge(e, f, 2.5);
        g.addBidirectionalEdge(h, i, 2.5);
        g.addBidirectionalEdge(i, j, 2.5);
        g.addBidirectionalEdge(j, k, 2.5);

        // Vertical roads
        g.addBidirectionalEdge(a, d, 2.5);
        g.addBidirectionalEdge(d, h, 2.5);
        g.addBidirectionalEdge(b, e, 2.5);
        g.addBidirectionalEdge(e, i, 2.5);
        g.addBidirectionalEdge(c, f, 2.5);
        g.addBidirectionalEdge(f, j, 2.5);

        // Diagonal shortcut
        g.addBidirectionalEdge(b, d, 3.5);
        g.addBidirectionalEdge(e, c, 3.5);

        return g;
    }

    /**
     * Larger road network with 15 nodes representing a simplified
     * city with highways, bypasses, and inner roads.
     *
     * Notable: some long-distance edges simulate highways
     *          where A* gains a larger advantage over Dijkstra.
     */
    public static Graph buildLargerCityMap() {
        Graph g = new Graph();

        // Row 0 (top)
        Node n0  = new Node(0,  "North-Gate",   0,  8);
        Node n1  = new Node(1,  "University",   3,  8);
        Node n2  = new Node(2,  "Tech-Park",    6,  8);
        Node n3  = new Node(3,  "Airport",      9,  8);

        // Row 1 (middle-top)
        Node n4  = new Node(4,  "Old-Town",     0,  5);
        Node n5  = new Node(5,  "City-Hall",    3,  5);
        Node n6  = new Node(6,  "Business-Dist",6,  5);
        Node n7  = new Node(7,  "East-Bridge",  9,  5);

        // Row 2 (middle-bottom)
        Node n8  = new Node(8,  "Suburbs",      0,  2);
        Node n9  = new Node(9,  "Mall",         3,  2);
        Node n10 = new Node(10, "Stadium",      6,  2);
        Node n11 = new Node(11, "Harbor",       9,  2);

        // Row 3 (bottom)
        Node n12 = new Node(12, "South-Gate",   0,  0);
        Node n13 = new Node(13, "Train-Station",4,  0);
        Node n14 = new Node(14, "Sea-Port",     9,  0);

        for (Node n : new Node[]{n0,n1,n2,n3,n4,n5,n6,n7,n8,n9,n10,n11,n12,n13,n14})
            g.addNode(n);

        // Top horizontal
        g.addBidirectionalEdge(n0, n1, 3.2);
        g.addBidirectionalEdge(n1, n2, 3.2);
        g.addBidirectionalEdge(n2, n3, 3.2);

        // Middle-top horizontal
        g.addBidirectionalEdge(n4, n5, 3.2);
        g.addBidirectionalEdge(n5, n6, 3.2);
        g.addBidirectionalEdge(n6, n7, 3.2);

        // Middle-bottom horizontal
        g.addBidirectionalEdge(n8,  n9,  3.2);
        g.addBidirectionalEdge(n9,  n10, 3.2);
        g.addBidirectionalEdge(n10, n11, 3.2);

        // Bottom horizontal
        g.addBidirectionalEdge(n12, n13, 4.5);
        g.addBidirectionalEdge(n13, n14, 5.5);

        // Left verticals
        g.addBidirectionalEdge(n0, n4, 3.2);
        g.addBidirectionalEdge(n4, n8, 3.2);
        g.addBidirectionalEdge(n8, n12, 2.5);

        // Center verticals
        g.addBidirectionalEdge(n1, n5, 3.2);
        g.addBidirectionalEdge(n5, n9, 3.2);
        g.addBidirectionalEdge(n9, n13, 2.5);

        // Right verticals
        g.addBidirectionalEdge(n3, n7,  3.2);
        g.addBidirectionalEdge(n7, n11, 3.2);
        g.addBidirectionalEdge(n11, n14, 2.5);

        // Highway (express road — long direct link)
        g.addBidirectionalEdge(n2, n6, 3.2);
        g.addBidirectionalEdge(n6, n10, 3.2);

        // Diagonal shortcuts
        g.addBidirectionalEdge(n1, n6, 4.8);
        g.addBidirectionalEdge(n5, n10, 4.8);
        g.addBidirectionalEdge(n9, n14, 6.5);

        return g;
    }
}
