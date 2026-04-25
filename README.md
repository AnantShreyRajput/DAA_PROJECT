# Dijkstra vs A* — Map Routing Comparison (Java)

A clean, structured Java project that compares **Dijkstra's algorithm** and **A\*** for finding shortest paths on map-like graphs.

---

## Project Structure

```
dijkstra-astar-comparison/
├── src/
│   ├── Main.java                        ← Entry point
│   ├── model/
│   │   ├── Node.java                    ← Map node with (x,y) coordinates
│   │   ├── Edge.java                    ← Weighted directed edge
│   │   ├── Graph.java                   ← Adjacency-list graph
│   │   └── PathResult.java              ← Algorithm result (path, cost, stats)
│   ├── algorithm/
│   │   ├── PathFinder.java              ← Common interface
│   │   ├── DijkstraAlgorithm.java       ← Dijkstra implementation
│   │   └── AStarAlgorithm.java         ← A* with Euclidean heuristic
│   ├── map/
│   │   └── MapFactory.java              ← Sample road-network graphs
│   └── comparison/
│       └── RoutingComparator.java       ← Side-by-side analysis & summary
├── out/                                 ← Compiled .class files (auto-created)
├── compile.ps1                          ← Windows compile script
└── README.md
```

---

## How to Compile & Run

### Windows (PowerShell)
```powershell
.\compile.ps1
java -cp out Main
```

### Linux / macOS
```bash
find src -name "*.java" | xargs javac -d out -sourcepath src
java -cp out Main
```

> Requires **Java 17+** (uses text blocks for the conclusion table).

---

## What It Tests

| Map | Nodes | Edges | Test Routes |
|-----|-------|-------|-------------|
| Small City Grid | 10 | 15 | 3 routes |
| Larger City Network | 15 | 50 | 4 routes |

---

## Key Concepts

### Dijkstra's Algorithm
- Priority queue ordered by **g(n)** — cost from source to n.
- Explores in **all directions equally** (no guidance).
- Guarantees the optimal path.
- Best when: destination unknown, or computing all-pairs shortest paths.

### A* Algorithm
- Priority queue ordered by **f(n) = g(n) + h(n)**.
- **h(n)** = Euclidean distance from n to destination (the heuristic).
- Guides search **toward the destination** → explores fewer nodes.
- Optimal when h(n) is **admissible** (never overestimates actual cost).
- Best when: routing between two specific points on a geographic map.

---

## Metrics Compared

| Metric | Description |
|--------|-------------|
| Path | Sequence of nodes on the shortest route |
| Total Cost | Sum of edge weights on the path |
| Nodes Explored | How many nodes were popped from the priority queue |
| Execution Time | Wall-clock time in microseconds |

---

## Conclusion

> **A\* is the better algorithm for map routing.**  
> It consistently explores fewer nodes (up to 64% less in tests) while finding the same optimal path as Dijkstra. Real GPS systems (Google Maps, Apple Maps) use variants of A\* for this reason.

---

## Web GUI (minimal)

A very small, manually-typed web GUI is included for visualizing a simplified graph and running Dijkstra and A* in the browser.

- Files: [web/index.html](web/index.html), [web/styles.css](web/styles.css), [web/app.js](web/app.js)
- To open on Windows (PowerShell):

```powershell
Start-Process .\web\index.html
# or run the helper after compilation:
.\compile.ps1 web
```

The GUI is intentionally minimal to reduce clutter (5 nodes, 6 edges) and is meant for quick interactive comparison.
