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
        UndirectedGraph ug;

        public UG(UndirectedGraph ug)
        {
            this.ug = ug;
        }

        /**
         * Test Link: https://www.hackerearth.com/problem/algorithm/connected-components-in-a-graph/
         *
         * @return ArrayList<ArrayList<Integer>> where each inner list represents a single connected component
         */
        public ArrayList<ArrayList<Integer>> connectedComponents()
        {
            ArrayList<ArrayList<Integer>> connectedComponents = new ArrayList<>();
            boolean[] isVisited = new boolean[ug.vertices];

            for (int i = 0; i < ug.vertices; i++)
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
        private void findConnectedComponent(int u, boolean[] isVisited, ArrayList<Integer> component)
        {
            isVisited[u] = true;
            component.add(u);

            for (Graph.Vertex v : ug.adjacencyList.get(u))
            {
                if (!isVisited[v.i]) findConnectedComponent(v.i, isVisited, component);
            }
        }
    }

    public static class DG
    {
        DirectedGraph dg;

        public DG(DirectedGraph dg) {
            this.dg = dg;
        }

        /**
         *
         * @return ArrayList<ArrayList<Integer>> where each inner list represents a single weakly connected component
         */
        public ArrayList<ArrayList<Integer>> weaklyConnectedComponents()
        {
            // Step 1: Construct underlying undirected graph of the provided directed graph
            UndirectedGraph undirectedGraph = new UndirectedGraph(dg.vertices);
            for (int u = 0; u < dg.vertices; u++)
            {
                for (Graph.Vertex v : dg.adjacencyList.get(u)) undirectedGraph.addEdge(u, v.i, null);
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

            // Reset the parent array
            Arrays.fill(parent, -1);
            DirectedGraph transpose =  dg.getTranspose();

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

        private void findConnectedComponent(DirectedGraph transpose, int u, int[] parent, ArrayList<Integer> connectedComponent)
        {
            connectedComponent.add(u);

            for (Graph.Vertex v : transpose.adjacencyList.get(u))
            {
                if (parent[v.i] == -1)
                {
                    parent[v.i] = u;
                    findConnectedComponent(transpose, v.i, parent, connectedComponent);
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

            int[] discovery = new int[dg.vertices];
            int[] lowLink = new int[dg.vertices];
            Arrays.fill(discovery, -1);

            for (int u = 0; u < dg.vertices; u++)
            {
                if (discovery[u] == -1) tarjansDFS(u, discovery, lowLink, new boolean[dg.vertices], new Stack<>(), scc);
            }

            return scc;
        }

        private void tarjansDFS(int u, int[] discovery, int[] lowLink, boolean[] onStack, Stack<Integer> stack,
                                ArrayList<ArrayList<Integer>> scc)
        {
            discovery[u] = time;
            lowLink[u] = time;
            onStack[u] = true;
            stack.add(u);

            time++;

            for (Graph.Vertex v : dg.adjacencyList.get(u))
            {
                if (discovery[v.i] == -1) tarjansDFS(v.i, discovery, lowLink, onStack, stack, scc);
                if (onStack[v.i]) lowLink[u] = Math.min(lowLink[u], lowLink[v.i]);
            }

            if (lowLink[u] == discovery[u])
            {
                ArrayList<Integer> component = new ArrayList<>();

                int v = -1;
                while (v != u)
                {
                    v = stack.pop();

                    onStack[v] = false;
                    component.add(v);
                }

                component.sort(Comparator.comparingInt(o -> o));
                scc.add(component);
            }
        }
    }
}
