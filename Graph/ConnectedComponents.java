package Graph;

import java.util.ArrayList;

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

        public ArrayList<ArrayList<Integer>> strongConnectedComponents()
        {
            return null;
        }
    }
}
