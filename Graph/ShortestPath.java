package Graph;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * The following class contains methods for finding shortest paths. It contains algorithms for finding:
 *
 * Single Source Shortest Path:
 * @see ShortestPath#dijkstra(int)
 * @see ShortestPath#bellmanford(int)
 *
 * All Pair Shortest Path:
 * @see ShortestPath#floydWarshall()
 * @see ShortestPath#johnsons()
 */
public class ShortestPath
{
    private final Graph graph;

    public ShortestPath(Graph graph)
    {
        this.graph = graph;
    }

    /**
     * Test Link: https://practice.geeksforgeeks.org/problems/implementing-dijkstra-set-1-adjacency-matrix/1
     *
     * NOTE: The current implementation of dijkstra takes O(V^2) time.
     *
     * @param source source vertex
     * @return 1D distance array containing distance of all the vertices from source vertex
     */
    public int[] dijkstra(int source)
    {
        boolean[] isVisited = new boolean[graph.vertices];
        int[] distance = new int[graph.vertices];

        Arrays.fill(distance, Integer.MAX_VALUE);
        distance[source] = 0;

        for (int i = 0;i < graph.vertices;i++)
        {
            int minDistanceVertex = findMinDistanceVertex(distance, isVisited);
            isVisited[minDistanceVertex] = true;

            for (Graph.Vertex v : graph.adjacencyList.get(minDistanceVertex))
            {
                int destination = v.i;
                int destinationDistance = v.w;

                if (!isVisited[destination] && distance[minDistanceVertex]+destinationDistance < distance[destination])
                    distance[destination] = distance[minDistanceVertex]+destinationDistance;
            }
        }

        return distance;
    }

    /**
     *
     * @param distance distance array
     * @param isVisited isVisited array
     * @return next vertex with minimum distance value from the set of vertices not yet included in the shortest path
     *         tree
     */
    private int findMinDistanceVertex(int[] distance, boolean[] isVisited)
    {
        int minIndex = -1, minDistance = Integer.MAX_VALUE;

        for (int v = 0;v < graph.vertices;v++)
        {
            if (!isVisited[v] && distance[v] <= minDistance)
            {
                minDistance = distance[v];
                minIndex = v;
            }
        }

        return minIndex;
    }


    /**
     * Test Link: https://practice.geeksforgeeks.org/problems/distance-from-the-source-bellman-ford-algorithm/0/?fbclid=IwAR2_lL0T84DnciLyzMTQuVTMBOi82nTWNLuXjUgahnrtBgkphKiYk6xcyJU
     *
     * @param source source vertex
     * @return null if negative cycle is found else 1D distance array
     */
    public int[] bellmanford(int source)
    {
        int[] distance = new int[graph.vertices];

        Arrays.fill(distance, Integer.MAX_VALUE);
        distance[source] = 0;

        for (int i = 0;i < graph.vertices-1;i++)
        {
            for (int currentVertex = 0; currentVertex < graph.vertices; currentVertex++)
            {
                for (Graph.Vertex v : graph.adjacencyList.get(currentVertex))
                {
                    if (distance[currentVertex] != Integer.MAX_VALUE && distance[currentVertex]+ v.w < distance[v.i])
                        distance[v.i] = distance[currentVertex]+ v.w;
                }
            }
        }

        /* Using the inner two loops used again, it can be checked if the graph contains a negative cycle or not.

           After performing the above three loops, we will find SSSP if there is no negative cycle. After another
           iteration of the inner two loops, if there is a change in the distance array, then a new shortest path is
           possible which can be true only if there is a negative cycle.
        */

        for (int currentVertex = 0; currentVertex < graph.vertices; currentVertex++)
        {
            for (Graph.Vertex v : graph.adjacencyList.get(currentVertex))
            {
                if (distance[currentVertex] != Integer.MAX_VALUE && distance[currentVertex]+ v.w < distance[v.i])
                    return null;
            }
        }

        return distance;
    }

    /**
     * Test Link: https://practice.geeksforgeeks.org/problems/implementing-floyd-warshall2042/1#
     *
     * @return 2D distance array
     */
    public int[][] floydWarshall()
    {
        int[][] distances = new int[graph.vertices][graph.vertices];
        for (int[] eachDistance: distances) Arrays.fill(eachDistance, Integer.MAX_VALUE);

        // Fill all diagonal elements with 0
        for (int i = 0;i < graph.vertices;i++) distances[i][i] = 0;

        for (int source = 0; source < graph.adjacencyList.size(); source++)
        {
            ArrayList<Graph.Vertex> vertices = graph.adjacencyList.get(source);

            for (Graph.Vertex v: vertices) distances[source][v.i] = v.w;
        }

        for (int k = 0;k < graph.vertices;k++)
        {
            for (int i = 0;i < graph.vertices;i++)
            {
                for (int j = 0;j < graph.vertices;j++)
                {
                    if (distances[i][k] != Integer.MAX_VALUE && distances[k][j] != Integer.MAX_VALUE)
                        distances[i][j] = Math.min(distances[i][k]+distances[k][j], distances[i][j]);
                }
            }
        }

        return distances;
    }

    /**
     *
     * @return null if negative cycle is found else 2D distance array
     */
    public int[][] johnsons()
    {
        /* Step 1: Add a new vertex q to the original graph, connected by zero-weight edges to all the other vertices
           of the graph */

        graph.vertices++;
        graph.adjacencyList.add(new ArrayList<>());
        for (int i = 0;i < graph.vertices-1;i++) graph.adjacencyList.get(graph.vertices-1).add(new Graph.Vertex(i, 0));

        /* Step 2: Use bellmanford with the new vertex q as source, to find for each vertex v the minimum weight h(v) of
           a path from q to v. If this step detects a negative cycle, the algorithm is terminated. */

        int[] h = bellmanford(graph.vertices-1);
        if (h == null) return null;

        /* Step 3: Re-weight the edges of the original graph using the values computed by the Bellman–Ford algorithm.
           w'(u, v) = w(u,v) + h(u) − h(v). */

        for (int u = 0;u < graph.vertices;u++)
        {
            ArrayList<Graph.Vertex> vertices = graph.adjacencyList.get(u);

            for (Graph.Vertex v: vertices)
            {
                // new weight
                v.w = v.w + h[u] - h[v.i];
            }
        }

        /* Step 4: Remove edge q and apply dijkstra from each node s to every other vertex in the re-weighted graph */

        graph.adjacencyList.remove(graph.vertices-1);
        graph.vertices--;

        int[][] distances = new int[graph.vertices][];

        for (int s = 0;s < graph.vertices;s++) distances[s] = dijkstra(s);

        /* Step 5: Compute the distance in the original graph by adding h[v] - h[u]  to the distance returned by
           dijkstra */

        for (int u = 0;u < graph.vertices;u++)
        {
            for (int v = 0;v < graph.vertices;v++)
            {
                // If no edge exist, continue
                if (distances[u][v] == Integer.MAX_VALUE) continue;

                distances[u][v] += (h[v] - h[u]);
            }
        }

        return distances;
    }
}
