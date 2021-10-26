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
     * Test Link: https://practice.geeksforgeeks.org/problems/find-the-maximum-flow2126/1
     *
     * @param source
     * @param sink
     * @return maximum flow
     */
    public int edmondsKarp(int source, int sink)
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
            //residualGraph.printGraph();
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
}
