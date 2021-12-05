package Graph;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Stack;

public class TopologicalSort
{
    private final DirectedGraph graph;

    public TopologicalSort(DirectedGraph graph)
    {
        this.graph = graph;
    }

    /**
     * Finds a valid topological ordering using DFS
     *
     * Test Link: https://practice.geeksforgeeks.org/problems/topological-sort/1
     *
     * @return Stack<Integer> which stores a valid topological ordering which can be obtained by popping the elements
     */
    public Stack<Integer> topologicalOrdering()
    {
        int[] parent = new int[graph.vertices];
        Arrays.fill(parent, -1);

        Stack<Integer> ordering = new Stack<>();

        for (int i = 0;i < graph.vertices;i++)
        {
            if (parent[i] == -1)
            {
                parent[i] = -2;
                DFS(i, parent, ordering);
            }
        }

        return ordering;
    }

    /**
     * Test Link: https://practice.geeksforgeeks.org/problems/depth-first-traversal-for-a-graph/1
     *
     * @param source source
     * @param parent parent array
     * @param ordering stack to store vertices according to their finishing time. vertex with the largest
     *                 finishing time will be at the top upon completion
     */
    private void DFS(int source, int[] parent, Stack<Integer> ordering)
    {
        for (Graph.Vertex v : graph.adjacencyList.get(source))
        {
            if (parent[v.i] == -1)
            {
                parent[v.i] = source;
                DFS(v.i, parent, ordering);
            }
        }

        // On finishing the exploration of the vertex source, add it the stack
        ordering.add(source);
    }

    /**
     * Test Link: https://practice.geeksforgeeks.org/problems/topological-sort/1
     *
     * @return LinkedList<Integer> which stores a valid topological ordering which can be obtained by removing all
     * the elements from the start
     */
    public LinkedList<Integer> kahns()
    {
        int[] inDegree = new int[graph.vertices];
        LinkedList<Integer> queue = new LinkedList<>();
        LinkedList<Integer> ordering = new LinkedList<>();

        // Find in-degree of all the vertices
        for (int source = 0;source < graph.vertices;source++)
        {
            for (Graph.Vertex v : graph.adjacencyList.get(source)) inDegree[v.i]++;
        }

        for (int i = 0; i < inDegree.length; i++)
        {
            if (inDegree[i] == 0)
            {
                queue.add(i);
                ordering.add(i);
            }
        }

        while (!queue.isEmpty())
        {
            int u = queue.removeFirst();

            for (Graph.Vertex v : graph.adjacencyList.get(u))
            {
                /*
                Decrease the in-degree of neighbouring vertex by 1. If in-degree now becomes 0, add it to the queue
                 */
                inDegree[v.i]--;

                if (inDegree[v.i] == 0)
                {
                    queue.add(v.i);
                    ordering.add(v.i);
                }
            }
        }

        return ordering;
    }
}
