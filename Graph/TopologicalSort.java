package Graph;

import java.util.Arrays;
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
     * @return Stack<Integer> which stores a valid topological ordering which can be obtained by popping the elements
     * till the stack is empty
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

    private void DFS(int source, int[] parent, Stack<Integer> ordering)
    {
        for (Graph.Neighbour neighbour: graph.adjacencyList.get(source))
        {
            if (parent[neighbour.destination] == -1)
            {
                parent[neighbour.destination] = source;
                DFS(neighbour.destination, parent, ordering);
            }
        }

        ordering.add(source);
    }
}
