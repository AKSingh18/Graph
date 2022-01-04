# Graph

## Introduction

In computer science, a graph is an abstract data type that is meant to implement the undirected graph and directed 
graph concepts from the field of graph theory within mathematics.

A graph data structure consists of a finite (and possibly mutable) set of vertices (also called nodes or points), 
together with a set of unordered pairs of these vertices for an undirected graph or a set of ordered pairs for a 
directed graph. These pairs are known as edges (also called links or lines), and for a directed graph are also known 
as edges but also sometimes arrows or arcs.

A graph data structure may also associate to each edge some edge value, such as a symbolic label or a
numeric attribute (cost, capacity, length, etc.). [Wikipedia](https://en.wikipedia.org/wiki/Graph_(abstract_data_type))

## Aim

The aim of the project is to act as a reference for **implementation** of various graph algorithms in Java. The project 
does not aim to act as a resource for learning about any algorithm. It only demonstrates one of the ways to implement
them.

## Table of Contents

> * [Title](#Graph)
> * [Introduction](#introduction)
> * [Aim](#aim)
> * [Table of Contents](#table-of-contents)
> * [Algorithm List](#algorithm-list)
> * [Representation Used](#representation-used)
> * [Source Code](#source-code)

## Algorithm List

* [Connected Components](Graph/ConnectedComponents.java)
   * [Undirected Graph](https://github.com/AKSingh18/Graph/blob/master/Graph/ConnectedComponents.java#L15)
      * [DFS](https://github.com/AKSingh18/Graph/blob/master/Graph/ConnectedComponents.java#L28)
   * [Directed Graph](https://github.com/AKSingh18/Graph/blob/master/Graph/ConnectedComponents.java#L59)
      * [Weakly Connected Components](https://github.com/AKSingh18/Graph/blob/master/Graph/ConnectedComponents.java#L71)
      * [Kosaraju](https://github.com/AKSingh18/Graph/blob/master/Graph/ConnectedComponents.java#L88)
      * [Tarjans](https://github.com/AKSingh18/Graph/blob/master/Graph/ConnectedComponents.java#L171)
* [Critical Points and Bridges](Graph/CriticalPointsAndBridges.java)
   * [Articulation Points](https://github.com/AKSingh18/Graph/blob/master/Graph/CriticalPointsAndBridges.java#L26)
   * [Bridges](https://github.com/AKSingh18/Graph/blob/master/Graph/CriticalPointsAndBridges.java#L80)
* [Cycle Detection](Graph/CycleDetection.java)
   * [Undirected Graph](https://github.com/AKSingh18/Graph/blob/master/Graph/CycleDetection.java#L15)
     * [Union find](https://github.com/AKSingh18/Graph/blob/master/Graph/CycleDetection.java#L30)
     * [DFS](https://github.com/AKSingh18/Graph/blob/master/Graph/CycleDetection.java#L90)  
  * [Directed Graph](https://github.com/AKSingh18/Graph/blob/master/Graph/CycleDetection.java#L127)
    * [DFS](https://github.com/AKSingh18/Graph/blob/master/Graph/CycleDetection.java#L142)
* [Euler trail and cycle](Graph/EulerTrailAndCycle.java)
   * [Undirected Graph](https://github.com/AKSingh18/Graph/blob/master/Graph/EulerTrailAndCycle.java#L15)
     * [Checking euler cycle](https://github.com/AKSingh18/Graph/blob/master/Graph/EulerTrailAndCycle.java#L35)
     * [Checking euler trail](https://github.com/AKSingh18/Graph/blob/master/Graph/EulerTrailAndCycle.java#L77)
     * [Finding euler trail](https://github.com/AKSingh18/Graph/blob/master/Graph/EulerTrailAndCycle.java#L124)
     * [Finding euler cycle](https://github.com/AKSingh18/Graph/blob/master/Graph/EulerTrailAndCycle.java#L153)    
   * [Directed Graph](https://github.com/AKSingh18/Graph/blob/master/Graph/EulerTrailAndCycle.java#L197)
     * [Checking euler cycle](https://github.com/AKSingh18/Graph/blob/master/Graph/EulerTrailAndCycle.java#L232)
     * [Checking euler trail](https://github.com/AKSingh18/Graph/blob/master/Graph/EulerTrailAndCycle.java#L277)
     * [Finding euler trail](https://github.com/AKSingh18/Graph/blob/master/Graph/EulerTrailAndCycle.java#L349)
     * [Finding euler cycle](https://github.com/AKSingh18/Graph/blob/master/Graph/EulerTrailAndCycle.java#L381)
 * [Graph Traversal](Graph/GraphTraversal.java)
    * [BFS from a source](https://github.com/AKSingh18/Graph/blob/master/GraphTraversal.java#L25)
    * [Finding a path from a to b using BFS](https://github.com/AKSingh18/Graph/blob/master/GraphTraversal.java#L64)
    * [DFS from a source](https://github.com/AKSingh18/Graph/blob/master/GraphTraversal.java#L97) 
 * [Maximum flow](Graph/MaxFlow.java)
    * [Edmond-Karp](https://github.com/AKSingh18/Graph/blob/master/Graph/MaxFlow.java#L58)
    * [Dinics](https://github.com/AKSingh18/Graph/blob/master/Graph/MaxFlow.java#L130)
    * [FIFO push relabel](https://github.com/AKSingh18/Graph/blob/master/Graph/MaxFlow.java#L228)
    * [Finding edges of min-cut](https://github.com/AKSingh18/Graph/blob/master/Graph/MaxFlow.java#L329) 
 * [Maximum matching in bipartite graph](Graph/MaxMatching.java)
    * [Maximum matching edges](https://github.com/AKSingh18/Graph/blob/master/Graph/MaxMatching.java#L50) 
 * [Minimum vertex cover in bipartite graph](Graph/MinVertexCover.java)
    * [Using maximum matching](https://github.com/AKSingh18/Graph/blob/master/Graph/MinVertexCover.java#L18)
 * [Minimum Spanning Tree](Graph/MST.java)
    * [Prims](https://github.com/AKSingh18/Graph/blob/master/Graph/MST.java#L40)
    * [Kruskals](https://github.com/AKSingh18/Graph/blob/master/Graph/MST.java#L92)
 * [Shortest Path](Graph/ShortestPath.java)
    * [Dijkstra](https://github.com/AKSingh18/Graph/blob/master/Graph/ShortestPath.java#L33)
    * [Bellman-Ford](https://github.com/AKSingh18/Graph/blob/master/Graph/ShortestPath.java#L88)
    * [Floyd Warshall](https://github.com/AKSingh18/Graph/blob/master/Graph/ShortestPath.java#L130)
    * [Johnsons](https://github.com/AKSingh18/Graph/blob/master/Graph/ShortestPath.java#L164)    
 * [Topological Sort](Graph/TopologicalSort.java)
    * [DFS](https://github.com/AKSingh18/Graph/blob/master/Graph/TopologicalSort.java#L22)
    * [Kahns](https://github.com/AKSingh18/Graph/blob/master/Graph/TopologicalSort.java#L68)
  
## Representation Used

Throughout this project, adjacency list is used to represent any graph.

###### Vertex class

Each edge in the adjacency list has been encapsulated using the [Vertex](https://github.com/AKSingh18/Graph/blob/master/Graph/Graph.java#L7)
class. Vertex class objects have two properties:

* _i_
* _w_

For any edge `E(u, v, w)`,

```mermaid
graph LR
    u-- w -->v;
```

`u` will be denoted by _adjacency list index_ and its other two properties `v` and `w` will be denoted by a vertex
object `V(i, w)`.

**Example:**

```mermaid
graph LR
    0-- 20 -->2;
    1-- 10 -->2;
```

Edge `E(0, 2, 20)` is encapsulated by a vertex `V(2, 20)` and edge `E(1, 2, 20)` is encapsulated by a vertex `V(2, 10)`. 
Here is the adjacency list of the above graph.

```
0: [V(2, 20)]
1: [V(2, 10)]
2: []
```

###### Edge class

In the implementation of [kruskals](https://github.com/AKSingh18/Graph/blob/master/Graph/MST.java#L92)
algorithm, to make sorting of edges simpler, each edge `E(u, v, w)` has been represented by an object of [Edge](https://github.com/AKSingh18/Graph/blob/master/Graph/Graph.java#L26) 
class. Edge class objects have three properties:

* _u_
* _v_
* _w_

###### Graph class

All the graphs are constructed using the [Graph](Graph/Graph.java) class. It contains two instance fields:

* _adjacency list_
* _vertices_

It also contains methods for _edge addition, retrieval and checking if an edge exists or not_.

It has three subclasses:

* [UndirectedGraph](Graph/UndirectedGraph.java)
* [DirectedGraph](Graph/DirectedGraph.java)
* [BipartiteGraph](Graph/BipartiteGraph.java)

## Source Code

In order to make the source code more cohesive and readable, all the complementary methods used by an algorithm, will be
defined and implemented just below the algorithm implementation itself. Consider `edmondsKarp` algorithm which uses a
method `findfindAugmentedPath`. Here, `findAugmentedPath` will be defined below `edmondsKarp` in the following manner:

```
public int edmondsKarp()
{
    ...
    while (findAugmentedPath() != null)
    ...
}

private int[] findAugmentedPath()
{
    ...
}
```

Any method which is used by many algorithms is placed before any algorithm declaration after constructors, setters and 
getters. For example, `initResidualGraph` graph method in `MaxFlow`.
