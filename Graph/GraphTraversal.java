package Graph;

import java.util.Arrays;
import java.util.LinkedList;

public class GraphTraversal
{
    private final Graph graph;

    public GraphTraversal(Graph graph)
    {
        this.graph = graph;
    }

    /**
     * Current implementation of BFS returns distance[] array which contains the shortest distance of each vertex in the
     * graph from the source vertex. The return value can be changed to return parent[].
     *
     * NOTE: BFS considers the graph as non-weighted, ie, it does take the weight of the edges into account while
     * calculating shortest path.
     *
     * @param source source vertex
     * @return distance
     */
    public int[] BFS(int source)
    {
        LinkedList<Integer> queue = new LinkedList<>();
        int[] parent = new int[graph.vertices];
        int[] distance = new int[graph.vertices];
        Arrays.fill(parent, -1);
        Arrays.fill(distance, -1);

        parent[source] = -2;
        distance[source] = 0;
        queue.add(source);

        int currentDistance = 0;

        while (!queue.isEmpty())
        {
            currentDistance++;
            int u = queue.remove();

            for (Graph.Vertex v : graph.adjacencyList.get(u))
            {
                if (parent[v.i] == -1)
                {
                    parent[v.i] = u;
                    distance[v.i] = currentDistance;
                    queue.add(v.i);
                }
            }
        }

        return distance;
    }

    /**
     *
     * @param source source vertex
     * @param destination destination vertex
     * @return parent[] if path is found else null
     */
    public int[] BFS(int source, int destination)
    {
        LinkedList<Integer> queue = new LinkedList<>();
        int[] parent = new int[graph.vertices];
        Arrays.fill(parent, -1);

        parent[source] = -2;
        queue.add(source);

        while (!queue.isEmpty() && parent[destination] == -1)
        {
            int u = queue.remove();

            for (Graph.Vertex v : graph.adjacencyList.get(u))
            {
                if (parent[v.i] == -1)
                {
                    parent[v.i] = u;
                    queue.add(v.i);
                }
            }
        }

        if (parent[destination] == -1) return null;
        else return parent;
    }

    /**
     * Finds all vertices reachable from the source vertex or traverses the graph from a given source vertex.
     *
     * @param u parent vertex
     * @return parent array
     */
    public int[] DFS(int u)
    {
        /* In parent, a value of
               1: -1 denotes an unvisited vertex.
               2: -2 denotes a root vertex which is the source
               3: Other than that it denotes parent
         */
        int[] parent = new int[graph.vertices];
        Arrays.fill(parent, -1);

        parent[u] = -2;
        DFS(u, parent);

        return parent;
    }

    private void DFS(int u, int[] parent)
    {
        for (Graph.Vertex v : graph.adjacencyList.get(u))
        {
            if (parent[v.i] == -1)
            {
                parent[v.i] = u;
                DFS(v.i, parent);
            }
        }
    }
}
