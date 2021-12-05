package Graph;

import java.util.Arrays;
import java.util.LinkedList;

public class MaxFlow
{
    private final Graph graph;

    public MaxFlow(Graph graph)
    {
        this.graph = graph;
    }

    /**
     *
     * @return residual graph
     */
    private Graph constructResidualGraph()
    {
        Graph residualGraph = new Graph(graph.vertices);

        // Construct residual graph
        for (int u = 0;u < graph.vertices;u++)
        {
            for (Graph.Vertex v : graph.adjacencyList.get(u))
            {
                // If forward edge already exists, update its weight
                if (residualGraph.hasEdge(u, v.i))
                    residualGraph.getNeighbour(u, v.i).w += v.w;
                // In case it does not exist, create one
                else residualGraph.addEdge(u, v.i, v.w);

                // If backward edge does not already exist, add it
                if (!residualGraph.hasEdge(v.i, u))
                    residualGraph.addEdge(v.i, u, 0);
            }
        }

        return residualGraph;
    }

    /**
     * Test Link: https://practice.geeksforgeeks.org/problems/find-the-maximum-flow2126/1
     *
     * @param source source
     * @param sink sink
     * @return maximum flow
     */
    public int edmondsKarp(int source, int sink)
    {
        Graph residualGraph = constructResidualGraph();

        int[] parent;
        int maxFlow = 0;
        while ((parent = findAugmentedPath(source, sink, residualGraph)) != null)
        {
            int flow = Integer.MAX_VALUE;
            int currentVertex = sink;

            while (currentVertex != source)
            {
                flow = Math.min(residualGraph.getNeighbour(parent[currentVertex], currentVertex).w, flow);
                currentVertex = parent[currentVertex];
            }

            currentVertex = sink;
            while (currentVertex != source)
            {
                residualGraph.getNeighbour(parent[currentVertex], currentVertex).w =
                        Math.max(residualGraph.getNeighbour(parent[currentVertex], currentVertex).w -=flow, 0);

                residualGraph.getNeighbour(currentVertex, parent[currentVertex]).w += flow;

                currentVertex = parent[currentVertex];
            }

            maxFlow += flow;
        }

        return maxFlow;
    }

    /**
     * Uses BFS to find path from source to sink
     *
     * @param source source vertex
     * @param sink destination vertex
     * @param residualGraph residual graph
     * @return parent[] if path is found else null
     */
    private int[] findAugmentedPath(int source, int sink, Graph residualGraph)
    {
        LinkedList<Integer> queue = new LinkedList<>();
        int[] parent = new int[residualGraph.vertices];
        Arrays.fill(parent, -1);

        parent[source] = -2;
        queue.add(source);

        while (!queue.isEmpty() && parent[sink] == -1)
        {
            int u = queue.remove();

            for (Graph.Vertex v : residualGraph.adjacencyList.get(u))
            {
                if (parent[v.i] == -1 && v.w > 0)
                {
                    parent[v.i] = u;
                    queue.add(v.i);
                }
            }
        }

        // If queue is empty, then sink cannot be located
        if (queue.isEmpty()) return null;
        else return parent;
    }

    /**
     *
     * The method uses push-relabel algorithm to find max flow. It assumes 0 as source and vertices-1 as sink.
     *
     * Test Link: https://practice.geeksforgeeks.org/problems/find-the-maximum-flow2126/1
     *
     * @return maximum flow
     */
    public int pushRelabel()
    {
        Graph residualGraph = constructResidualGraph();

        // Step 1: Initialize pre-flow
        int[] e = new int[graph.vertices]; // to store excess flow
        int[] h = new int[graph.vertices]; // to store height of vertices

        h[0] = graph.vertices;

        for (Graph.Vertex v : graph.adjacencyList.get(0))
        {
            residualGraph.getNeighbour(0, v.i).w = 0;
            residualGraph.getNeighbour(v.i, 0).w = v.w;

            e[v.i] = v.w;
        }

        // Step 2: Update the pre-flow while there remains an applicable push or relabel operation
        int u = getOverflowVertex(e);
        while (u != -1)
        {
            relabel(residualGraph, u, h);
            push(residualGraph, u, e, h);

            u = getOverflowVertex(e);
        }

        return e[graph.vertices-1];
    }

    /**
     *
     * @param e excess flow array
     * @return overflow vertex index if found else -1 if there are no overflowing vertices
     */
    private int getOverflowVertex(int[] e)
    {
        for (int i = 1; i < graph.vertices-1; i++) if (e[i] > 0) return i;

        return -1;
    }

    private void relabel(Graph residualGraph, int u, int[] h)
    {
        int minHeight = Integer.MAX_VALUE;

        for (Graph.Vertex v : residualGraph.adjacencyList.get(u))
        {
            if (v.w > 0) minHeight = Math.min(h[v.i], minHeight);
        }

        h[u] = minHeight+1;
    }

    private void push(Graph residualGraph, int u, int[] e, int[] h)
    {
        for (Graph.Vertex v : residualGraph.adjacencyList.get(u))
        {
            // after pushing flow if there is no excess flow, then break
            if (e[u] == 0) break;

            // push more flow to the adjacent v if possible
            if (v.w > 0 && h[v.i] < h[u])
            {
                int f = Math.min(e[u], v.w);

                v.w -= f;
                residualGraph.getNeighbour(v.i,u).w += f;

                e[u] -= f;
                e[v.i] += f;
            }
        }
    }
}
