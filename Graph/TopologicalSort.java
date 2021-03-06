package Graph;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Stack;

public class TopologicalSort
{
    private final DirectedGraph dg;

    public TopologicalSort(DirectedGraph dg)
    {
        this.dg = dg;
    }

    /**
     *
     * Finds a valid topological ordering using DFS
     *
     * @return Stack<Integer> which stores a valid topological ordering which can be obtained by popping the elements
     */
    public Stack<Integer> topologicalOrdering()
    {
        int[] parent = new int[dg.vertices];
        Arrays.fill(parent, -1);

        Stack<Integer> ordering = new Stack<>();

        for (int i = 0; i < dg.vertices; i++)
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
     *
     * @param u parent vertex
     * @param parent parent array
     * @param ordering stack to store vertices according to their finishing time. vertex with the largest
     *                 finishing time will be at the top upon completion
     */
    private void DFS(int u, int[] parent, Stack<Integer> ordering)
    {
        for (Graph.Vertex v : dg.adjacencyList.get(u))
        {
            if (parent[v.i] == -1)
            {
                parent[v.i] = u;
                DFS(v.i, parent, ordering);
            }
        }

        // On finishing the exploration of the vertex source, add it the stack
        ordering.add(u);
    }

    /**
     *
     * @return LinkedList<Integer> which stores a valid topological ordering which can be obtained by removing all
     * the elements from the start
     */
    public LinkedList<Integer> kahns()
    {
        int[] inDegree = new int[dg.vertices];
        LinkedList<Integer> queue = new LinkedList<>();
        LinkedList<Integer> ordering = new LinkedList<>();

        // Find in-degree of all the vertices
        for (int source = 0; source < dg.vertices; source++)
        {
            for (Graph.Vertex v : dg.adjacencyList.get(source)) inDegree[v.i]++;
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

            for (Graph.Vertex v : dg.adjacencyList.get(u))
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
