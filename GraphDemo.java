import Graph.*;

import java.util.ArrayList;
import java.util.Stack;

public class GraphDemo
{
    public static void main(String[] args)
    {
        /*
        final int vertices = 6;

        Graph graph = new UndirectedGraph(vertices);

        graph.addEdge(0, 1, 10);
        graph.addEdge(0, 2, 10);
        graph.addEdge(1, 2, 2);
        graph.addEdge(1, 3, 4);
        graph.addEdge(1, 4, 8);
        graph.addEdge(2, 4, 9);
        graph.addEdge(3, 5, 10);
        graph.addEdge(4, 3, 6);
        graph.addEdge(4, 5, 10);

        graph.printGraph();

        MaxFlow maxFlow = new MaxFlow(graph);
        System.out.println();
        System.out.println(maxFlow.edmondsKarp(0, 5));
        */

        /*DirectedGraph graph = new DirectedGraph(7);

        graph.addEdge(0, 1, null);
        graph.addEdge(0, 2, null);
        graph.addEdge(1, 2, null);
        graph.addEdge(2, 3, null);
        graph.addEdge(1, 5, null);
        graph.addEdge(6, 1, null);
        graph.addEdge(6, 5, null);
        graph.addEdge(5, 3, null);
        graph.addEdge(5, 4, null);

        Stack<Integer> ordering = new TopologicalSort(graph).topologicalOrdering();
        while (!ordering.isEmpty()) System.out.print(ordering.pop() + " ");*/

        DirectedGraph graph = new DirectedGraph(6);
        graph.addEdge(0, 1, null);
        graph.addEdge(0, 2, null);
        graph.addEdge(3, 1, null);
        graph.addEdge(3, 2, null);
        graph.addEdge(4, 5, null);

        ConnectedComponents.DG connectedComponents = new ConnectedComponents.DG(graph);
        ArrayList<ArrayList<Integer>> wcc = connectedComponents.weaklyConnectedComponents();

        for (ArrayList<Integer> component : wcc) System.out.println(component);
    }
}
