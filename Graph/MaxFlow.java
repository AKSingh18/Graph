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
            for (Graph.Neighbour neighbour: graph.adjacencyList.get(u))
            {
                // If forward edge already exists, update its weight
                if (residualGraph.hasEdge(u, neighbour.destination))
                    residualGraph.getNeighbour(u, neighbour.destination).weight += neighbour.weight;
                // In case it does not exist, create one
                else residualGraph.addEdge(u, neighbour.destination, neighbour.weight);

                // If backward edge does not already exist, add it
                if (!residualGraph.hasEdge(neighbour.destination, u))
                    residualGraph.addEdge(neighbour.destination, u, 0);
            }
        }

        return residualGraph;
    }

    /**
     * Test Link: https://practice.geeksforgeeks.org/problems/find-the-maximum-flow2126/1
     *
     * @param source
     * @param sink
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
                flow = Math.min(residualGraph.getNeighbour(parent[currentVertex], currentVertex).weight, flow);
                currentVertex = parent[currentVertex];
            }

            currentVertex = sink;
            while (currentVertex != source)
            {
                residualGraph.getNeighbour(parent[currentVertex], currentVertex).weight =
                        Math.max(residualGraph.getNeighbour(parent[currentVertex], currentVertex).weight-=flow, 0);

                residualGraph.getNeighbour(currentVertex, parent[currentVertex]).weight += flow;

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

            for (Graph.Neighbour neighbour: residualGraph.adjacencyList.get(u))
            {
                if (parent[neighbour.destination] == -1 && neighbour.weight > 0)
                {
                    parent[neighbour.destination] = u;
                    queue.add(neighbour.destination);
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

        for (Graph.Neighbour neighbour: graph.adjacencyList.get(0))
        {
            residualGraph.getNeighbour(0, neighbour.destination).weight = 0;
            residualGraph.getNeighbour(neighbour.destination, 0).weight = neighbour.weight;

            e[neighbour.destination] = neighbour.weight;
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

        for (Graph.Neighbour neighbour: residualGraph.adjacencyList.get(u))
        {
            if (neighbour.weight > 0) minHeight = Math.min(h[neighbour.destination], minHeight);
        }

        h[u] = minHeight+1;
    }

    private void push(Graph residualGraph, int u, int[] e, int[] h)
    {
        for (Graph.Neighbour neighbour: residualGraph.adjacencyList.get(u))
        {
            // after pushing flow if there is no excess flow, then break
            if (e[u] == 0) break;

            // push more flow to the adjacent vertex if possible
            if (neighbour.weight > 0 && h[neighbour.destination] < h[u])
            {
                int f = Math.min(e[u], neighbour.weight);

                neighbour.weight -= f;
                residualGraph.getNeighbour(neighbour.destination,u).weight += f;

                e[u] -= f;
                e[neighbour.destination] += f;
            }
        }
    }
}
