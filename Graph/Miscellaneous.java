package Graph;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Contains methods to find articulation points and bridges in the graph
 */
public class Miscellaneous
{
    private final Graph graph;

    public Miscellaneous(UndirectedGraph graph)
    {
        this.graph = graph;
    }

    private int time;

    /**
     * Test Link: https://practice.geeksforgeeks.org/problems/articulation-point-1/0/#
     *
     * @return array list containing all the articulation points present in the graph
     */
    public ArrayList<Integer> articulationPoints()
    {
        ArrayList<Integer> points = new ArrayList<>();
        time = 0;

        int[] parent = new int[graph.vertices];
        int[] lowLink = new int[graph.vertices];
        int[] discovery = new int[graph.vertices];
        boolean[] isAP = new boolean[graph.vertices];

        Arrays.fill(parent, -1);

        for (int i = 0;i < graph.vertices;i++)
        {
            if (parent[i] == -1)
            {
                parent[i] = -2;
                DFS(i, parent, lowLink, discovery, isAP);
            }
        }

        for (int i = 0;i < graph.vertices;i++) if (isAP[i]) points.add(i);

        return points;
    }

    private void DFS(int source, int[] parent, int[] lowLink, int[] discovery, boolean[] isAP)
    {
        int children = 0;

        lowLink[source] = discovery[source] = time++;

        for (Graph.Vertex v : graph.adjacencyList.get(source))
        {
            if (parent[v.i] == -1)
            {
                children++;
                parent[v.i] = source;
                DFS(v.i, parent, lowLink, discovery, isAP);

                lowLink[source] = Math.min(lowLink[source], lowLink[v.i]);

                if (parent[source] != -2 && lowLink[v.i] >= discovery[source]) isAP[source] = true;
            }
            else if (v.i != parent[source]) lowLink[source] = Math.min(lowLink[source], discovery[v.i]);
        }

        if (parent[source] == -2 && children > 1) isAP[source] = true;
    }

    /**
     * Test Link: https://practice.geeksforgeeks.org/problems/bridge-edge-in-graph/1#
     *
     * @return Array list of bridges. Each bridge is represented as an array list of size 2.
     */
    public ArrayList<ArrayList<Integer>> findBridges()
    {
        ArrayList<ArrayList<Integer>> bridges = new ArrayList<>();

        time = 0;

        int[] parent = new int[graph.vertices];
        int[] lowLink = new int[graph.vertices];
        int[] discovery = new int[graph.vertices];

        Arrays.fill(parent, -1);

        for (int i = 0;i < graph.vertices;i++)
        {
            if (parent[i] == -1)
            {
                parent[i] = -2;
                DFS(i, parent, lowLink, discovery, bridges);
            }
        }

        return bridges;
    }

    private void DFS(int source, int[] parent, int[] lowLink, int[] discovery,
                     ArrayList<ArrayList<Integer>> bridges)
    {
        lowLink[source] = discovery[source] = time++;

        for (Graph.Vertex v : graph.adjacencyList.get(source))
        {
            if (parent[v.i] == -1)
            {
                parent[v.i] = source;
                DFS(v.i, parent, lowLink, discovery, bridges);

                lowLink[source] = Math.min(lowLink[source], lowLink[v.i]);

                if (lowLink[v.i] > discovery[source])
                {
                    ArrayList<Integer> edge = new ArrayList<>(2);
                    edge.add(source);
                    edge.add(v.i);

                    bridges.add(edge);
                }
            }
            else if (v.i != parent[source]) lowLink[source] = Math.min(lowLink[source], discovery[v.i]);
        }
    }

    /**
     * The method uses edmond-karp algorithm to find maximum matching in the bipartite graph.
     *
     * The user need to use the undirected graph passed in the constructor parameter to represent the
     * bipartite graph in question. The method will convert the undirected graph to flow graph with each edge
     * having unit weight. The flow graph will be used in finding the maximum matching.
     *
     * It is necessary that the user represents their bipartite graph correctly using undirected graph.
     * Here is how the undirected graph should be constructed:
     *
     * Labelling:
     *     1: V       -> total number of vertices
     *     2: L       -> number of vertices in the left set
     *     3: 0-(L-1) -> value of each vertex in the set L
     *     4: L-(V-1) -> value of each vertex in the set R
     *
     * Example graph:
     *
     *    [L]     [R]
     *     0 ----- 3
     *     1 ----- 4
     *     2 ----- 5
     *
     * Corresponding flow graph:
     *
     *         1 -----> 4
     *       /            \
     *      /              \
     *     0 - 2 -----> 5 - 7
     *      \              /
     *       \            /
     *         3 -----> 6
     *
     * Source - 0 ... Sink - 7 ... each edge has unit weight
     *
     * Test Link: https://practice.geeksforgeeks.org/problems/maximum-bipartite-matching/1
     *
     * @param L number of vertices in the left set
     * @return the maximum matching
     */
    public int maxMatchingBiGraph(int L)
    {
        final int source = 0;
        final int sink = graph.vertices+1;

        // Construct a new graph with source 0 and sink graph.vertices.
        DirectedGraph flowGraph = new DirectedGraph(sink+1);

        // Add edges from source to vertices of set L
        for (int i = 1;i <= L;i++) flowGraph.addEdge(source, i, 1);

        // Add edges from vertices of set R to sink
        for (int i = L+1;i <= sink-1;i++) flowGraph.addEdge(i, sink, 1);

        // Add all the edges belonging to the bipartite graph
        for (int i = 1;i <= L;i++)
        {
            // A vertex i in the flow graph will be vertex i-1 in the original graph
            for (Graph.Vertex v : graph.adjacencyList.get(i-1)) flowGraph.addEdge(i, v.i+1, 1);
        }

        // Run edmonds-karp on the flow graph to find the maximum matching
        MaxFlow maxFlow = new MaxFlow(flowGraph);
        return maxFlow.edmondsKarp(source, sink);
    }
}
