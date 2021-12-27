package Graph;

import java.util.ArrayList;

public class MaxMatching
{
    private final BipartiteGraph biGraph;

    public MaxMatching(BipartiteGraph biGraph)
    {
        this.biGraph = biGraph;
    }

    /**
     * This method converts the given bi-graph to flow graph which is used in finding maximum matching edges.
     * In the flow graph, labelling of L and R vertices will change.
     *
     * Labelling change:
     *    1: Flow graph will consist of L+R+2 vertices.
     *    2: All L vertices from 0 to (L-1) will be changed to 1 to L in the flow graph.
     *    3: All R vertices from L to (R-1) will be changed to L+1 to R in the flow graph.
     *    4: 0 is the source and R+1 is the sink in the flow graph
     *
     * Example graph:
     *
     *    [L]     [R]
     *     0 ----> 3
     *     1 ----> 4
     *     2 ----> 5
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
     *
     *  All edges are outgoing from source 0 are directed. No arrows have been added due to diagram limitation.
     *  All edges are ingoing to sink 7 are directed. No arrows have been added due to diagram limitation.
     *
     *  Each edge has a unit weight.
     *
     * @return Edges which are part of maximum matching. Each edge is represented as an ArrayList of size 2
     */
    public ArrayList<ArrayList<Integer>> maxMatchingEdges()
    {
        final int source = 0;
        final int sink = biGraph.vertices+1;

        final int L = biGraph.L;

        // Construct a new graph with source 0 and sink graph.vertices.
        DirectedGraph flowGraph = new DirectedGraph(sink+1);

        // Add edges from source to vertices of set L
        for (int i = 1;i <= L;i++) flowGraph.addEdge(source, i, 1);

        // Add edges from vertices of set R to sink
        for (int i = L+1;i <= biGraph.vertices;i++) flowGraph.addEdge(i, sink, 1);

        // Add all the edges belonging to the bipartite graph
        for (int i = 1;i <= L;i++)
        {
            // A vertex i in the flow graph will be vertex i-1 in the original graph
            for (Graph.Vertex v : biGraph.adjacencyList.get(i-1)) flowGraph.addEdge(i, v.i+1, 1);
        }

        // Find the final residual graph
        MaxFlow maxFlow = new MaxFlow(flowGraph, source, sink);
        Graph finalResidualGraph = maxFlow.getFinalResidualGraph();

        ArrayList<ArrayList<Integer>> edges = new ArrayList<>();

        // Using residual graph find all the edges in maximum matching
        for (int u = 1;u <= L;u++)
        {
            for (Graph.Vertex v : finalResidualGraph.adjacencyList.get(u))
            {
                if (v.w == 0 && v.i >= L+1 && v.i <= biGraph.vertices)
                {
                    ArrayList<Integer> e = new ArrayList<>(2);

                    e.add(u-1);
                    e.add(v.i-1);

                    edges.add(e);

                    break;
                }
            }
        }

        return edges;
    }
}
