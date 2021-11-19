package Graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Stack;

/**
 *
 * This class contains two static nested classes UG (UndirectedGraph) and DG (DirectedGraph) which are used to find
 * connected components in their respective graph type.
 */
public class ConnectedComponents
{
    public static class UG
    {
        UndirectedGraph graph;

        public UG(UndirectedGraph graph)
        {
            this.graph = graph;
        }

        /**
         * Test Link: https://www.hackerearth.com/problem/algorithm/connected-components-in-a-graph/
         *
         * @return ArrayList<ArrayList<Integer>> where each inner list represents a single connected component
         */
        public ArrayList<ArrayList<Integer>> connectedComponents()
        {
            ArrayList<ArrayList<Integer>> connectedComponents = new ArrayList<>();
            boolean[] isVisited = new boolean[graph.vertices];

            for (int i = 0;i < graph.vertices;i++)
            {
                if (!isVisited[i])
                {
                    ArrayList<Integer> component = new ArrayList<>();
                    findConnectedComponent(i, isVisited, component);
                    connectedComponents.add(component);
                }
            }

            return connectedComponents;
        }

        // Finds a connected component using DFS
        private void findConnectedComponent(int source, boolean[] isVisited, ArrayList<Integer> component)
        {
            isVisited[source] = true;
            component.add(source);

            for (Graph.Neighbour neighbour: graph.adjacencyList.get(source))
            {
                if (!isVisited[neighbour.destination]) findConnectedComponent(neighbour.destination, isVisited, component);
            }
        }
    }

    public static class DG
    {
        DirectedGraph graph;

        public DG(DirectedGraph graph) {
            this.graph = graph;
        }

        /**
         *
         * @return ArrayList<ArrayList<Integer>> where each inner list represents a single weakly connected component
         */
        public ArrayList<ArrayList<Integer>> weaklyConnectedComponents()
        {
            // Step 1: Construct underlying undirected graph of the provided directed graph
            UndirectedGraph undirectedGraph = new UndirectedGraph(graph.vertices);
            for (int u = 0;u < graph.vertices;u++)
            {
                for (Graph.Neighbour neighbour : graph.adjacencyList.get(u)) undirectedGraph.addEdge(u, neighbour.destination, null);
            }

            // Step 2: Return the connected components of the underlying undirected graph
            return new UG(undirectedGraph).connectedComponents();
        }

        /**
         * Test Link: https://practice.geeksforgeeks.org/problems/strongly-connected-components-kosarajus-algo/1
         *
         * @return ArrayList<ArrayList<Integer>> where each inner list represents a single strongly connected component
         */
        public ArrayList<ArrayList<Integer>> kosarajus()
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

            // Reset the parent array
            Arrays.fill(parent, -1);
            DirectedGraph transpose =  graph.getTranspose();

            ArrayList<ArrayList<Integer>> connectedComponents = new ArrayList<>();

            while (!ordering.isEmpty())
            {
                if (parent[ordering.peek()] == -1)
                {
                    ArrayList<Integer> connectedComponent = new ArrayList<>();

                    parent[ordering.peek()] = -2;
                    findConnectedComponent(transpose, ordering.peek(), parent, connectedComponent);

                    connectedComponents.add(connectedComponent);
                }

                ordering.pop();
            }

            return connectedComponents;
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
            for (Graph.Neighbour neighbour: graph.adjacencyList.get(source))
            {
                if (parent[neighbour.destination] == -1)
                {
                    parent[neighbour.destination] = source;
                    DFS(neighbour.destination, parent, ordering);
                }
            }

            // On finishing the exploration of the vertex source, add it the stack
            ordering.add(source);
        }

        private void findConnectedComponent(DirectedGraph transpose, int source, int[] parent, ArrayList<Integer> connectedComponent)
        {
            connectedComponent.add(source);

            for (Graph.Neighbour neighbour: transpose.adjacencyList.get(source))
            {
                if (parent[neighbour.destination] == -1)
                {
                    parent[neighbour.destination] = source;
                    findConnectedComponent(transpose, neighbour.destination, parent, connectedComponent);
                }
            }
        }

        // Used to give unique ID to vertices in tarjan's algorithm
        private int time;

        /**
         * Test Link: https://practice.geeksforgeeks.org/problems/strongly-connected-component-tarjanss-algo-1587115621/1#
         *            https://www.codingninjas.com/codestudio/problems/strongly-connected-components-tarjan-s-algorithm_985311?leftPanelTab=1
         *
         * @return List of SCCs present in the directed graph
         */
        public ArrayList<ArrayList<Integer>> tarjans()
        {
            time = 0;

            ArrayList<ArrayList<Integer>> scc = new ArrayList<>();

            int[] discovery = new int[graph.vertices];
            int[] lowLink = new int[graph.vertices];
            Arrays.fill(discovery, -1);

            for (int u = 0;u < graph.vertices;u++)
            {
                if (discovery[u] == -1) tarjansDFS(u, discovery, lowLink, new boolean[graph.vertices], new Stack<>(), scc);
            }

            return scc;
        }

        private void tarjansDFS(int source, int[] discovery, int[] lowLink, boolean[] onStack, Stack<Integer> stack,
                                ArrayList<ArrayList<Integer>> scc)
        {
            discovery[source] = time;
            lowLink[source] = time;
            onStack[source] = true;
            stack.add(source);

            time++;

            for (Graph.Neighbour v : graph.adjacencyList.get(source))
            {
                if (discovery[v.destination] == -1) tarjansDFS(v.destination, discovery, lowLink, onStack, stack, scc);
                if (onStack[v.destination]) lowLink[source] = Math.min(lowLink[source], lowLink[v.destination]);
            }

            if (lowLink[source] == discovery[source])
            {
                ArrayList<Integer> component = new ArrayList<>();

                int vertex = -1;
                while (vertex != source)
                {
                    vertex = stack.pop();

                    onStack[vertex] = false;
                    component.add(vertex);
                }

                component.sort(Comparator.comparingInt(o -> o));
                scc.add(component);
            }
        }
    }
}
