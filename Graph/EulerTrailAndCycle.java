package Graph;

import java.util.ArrayList;
import java.util.Collections;

import static Graph.Graph.Vertex;

/**
 *
 * This class contains two static nested classes UG (UndirectedGraph) and DG (DirectedGraph) which contains methods
 * to check if there is an euler trail or cycle present in the input graph and find one if there is.
 */
public class EulerTrailAndCycle
{
    public static class UG
    {
        UndirectedGraph ug;
        private final int[] degree;

        public UG(UndirectedGraph ug)
        {
            this.ug = ug;
            degree = new int[ug.vertices];
        }

        private void initDegree()
        {
            for (int u = 0; u < ug.vertices; u++) degree[u] = ug.adjacencyList.get(u).size();
        }

        /**
         *
         * @return if the graph contains an euler cycle or not
         */
        public boolean hasCycle()
        {
            // Step 1: Calculate degree of all the vertices
            initDegree();

            // Step 2: If the degree of all the vertices is not zero, return false
            for (int i = 0; i < ug.vertices; i++) if (degree[i] % 2 != 0) return false;

            // Step 3: Find the connected components in the graph
            ArrayList<ArrayList<Integer>> connectedComponents =
                    new ConnectedComponents.UG(ug).connectedComponents();

            // Step 4: Label the vertices with the component index they belong to
            int[] componentIndex = new int[ug.vertices];
            for (int i = 0; i < connectedComponents.size(); i++)
            {
                for (Integer v : connectedComponents.get(i)) componentIndex[v] = i;
            }

            // Step 5: Check if all the vertices with non-zero degree belong to a single connected component
            int componentID = -1;
            int i = 0;

            while (i < ug.vertices && componentID == -1)
            {
                if (degree[i] != 0) componentID = componentIndex[i];
                i++;
            }

            while (i < ug.vertices)
            {
                if (degree[i] != 0 && componentIndex[i] != componentID) return false;
                i++;
            }

            return true;
        }

        /**
         *
         * @return if the graph contains an euler trail or not
         */
        public boolean hasTrail()
        {
            // Step 1: Calculate degree of all the vertices
            initDegree();

            int oddDegree = 0;

            // Step 2: Count number of vertices with odd degree
            for (int i = 0; i < ug.vertices; i++) if (degree[i] % 2 != 0) oddDegree++;

            // Step 3: Return false if odd degree vertices count is neither 0 nor 2
            if (!(oddDegree == 0 || oddDegree == 2)) return false;

            // Step 4: Find the connected components in the graph
            ArrayList<ArrayList<Integer>> connectedComponents =
                    new ConnectedComponents.UG(ug).connectedComponents();

            // Step 5: Label the vertices with the component index they belong to
            int[] componentIndex = new int[ug.vertices];
            for (int i = 0; i < connectedComponents.size(); i++)
            {
                for (Integer v : connectedComponents.get(i)) componentIndex[v] = i;
            }

            // Step 6: Check if all the vertices with non-zero degree belong to a single connected component
            int componentID = -1;
            int i = 0;

            while (i < ug.vertices && componentID == -1)
            {
                if (degree[i] != 0) componentID = componentIndex[i];
                i++;
            }

            while (i < ug.vertices)
            {
                if (degree[i] != 0 && componentIndex[i] != componentID) return false;
                i++;
            }

            return true;
        }

        /**
         *
         * @return null if no trail is possible else ArrayList of integers containing a valid euler trail
         */
        public ArrayList<Integer> findTrail()
        {
            if (hasCycle()) return findCycle();
            if (!hasTrail()) return null;

            ArrayList<Integer> trail = new ArrayList<>();
            int a = -1; // Start vertex

            // Set a
            for (int i = 0; i < ug.vertices; i++)
            {
                if (degree[i]%2 != 0)
                {
                    a = i;
                    break;
                }
            }

            // Copy the original graph
            UndirectedGraph tempGraph = new UndirectedGraph(ug);
            DFS(a, tempGraph, trail);

            return trail;
        }

        /**
         *
         * @return null if no cycle is possible else ArrayList of integers containing a valid euler cycle
         */
        public ArrayList<Integer> findCycle()
        {
            if (!hasCycle()) return null;

            ArrayList<Integer> cycle = new ArrayList<>();

            // Copy the original graph
            UndirectedGraph tempGraph = new UndirectedGraph(ug);
            DFS(0, tempGraph, cycle);

            return cycle;
        }

        /**
         *
         * @param u source vertex
         * @param tempGraph copy of original graph
         * @param path ArrayList to store path
         */
        private void DFS(int u, UndirectedGraph tempGraph, ArrayList<Integer> path)
        {
            // Get all the neighbouring vertices of u
            ArrayList<Vertex> vertices = tempGraph.adjacencyList.get(u);

            while (!vertices.isEmpty())
            {
                /* In case of undirected graph, an edge from u-v will be present two times. Once in,
                   neighbouring vertices of u and other in neighbouring vertices of v. Since, it should
                   be traversed only one time. The latter occurrence will be removed.

                    v:  u->v
                   _v:  v->u
                 */
                Vertex v = vertices.remove(vertices.size()-1);
                Vertex _v = tempGraph.getEdge(v.i, u);

                tempGraph.adjacencyList.get(v.i).remove(_v);
                DFS(v.i, tempGraph, path);
            }

            path.add(u);
        }
    }

    public static class DG
    {
        DirectedGraph dg;
        /* 2D array to store in degree and out degree of all the vertices.
               row 1: in degree
               row 2: out degree
         */
        private final int[][] degree;

        public DG(DirectedGraph dg)
        {
            this.dg = dg;
            degree = new int[2][dg.vertices];
        }

        private void initDegree()
        {
            int[] inDegree = new int[dg.vertices];
            int[] outDegree = new int[dg.vertices];

            for (int u = 0; u < dg.vertices; u++)
            {
                outDegree[u] = dg.adjacencyList.get(u).size();

                for (Graph.Vertex v : dg.adjacencyList.get(u)) inDegree[v.i]++;
            }

            degree[0] = inDegree;
            degree[1] = outDegree;
        }

        /**
         *
         * @return if the graph contains an euler cycle or not
         */
        public boolean hasCycle()
        {
            // Step 1: Calculate in degree and out degree of all the vertices
            initDegree();

            int[] inDegree = degree[0];
            int[] outDegree = degree[1];

            // Step 2: Check if in degree = out degree for all the vertices
            for (int i = 0; i < dg.vertices; i++) if (inDegree[i] != outDegree[i]) return false;

            // Step 3: Calculate scc of the graph
            ArrayList<ArrayList<Integer>> scc = new ConnectedComponents.DG(dg).tarjans();

            // Step 4: Label the vertices with the component index they belong to
            int[] componentIndex = new int[dg.vertices];
            for (int i = 0; i < scc.size(); i++)
            {
                for (Integer v : scc.get(i)) componentIndex[v] = i;
            }

            // Step 5: Check if all the vertices with non-zero degree belong to a single strongly connected
            // component
            int componentID = -1;
            int i = 0;

            while (i < dg.vertices && componentID == -1)
            {
                if (inDegree[i] != 0 && inDegree[i]%2 == 0) componentID = componentIndex[i];
                i++;
            }

            while (i < dg.vertices)
            {
                if (inDegree[i] != 0 && inDegree[i]%2 == 0 && componentIndex[i] != componentID) return false;
                i++;
            }

            return true;
        }

        /**
         *
         * @return if the graph contains an euler trail or not
         */
        public boolean hasTrail()
        {
            // Step 1: Calculate in degree and out degree of all the vertices
            initDegree();

            int[] inDegree = degree[0];
            int[] outDegree = degree[1];

            // 'a' will store a vertex with outDegree-inDegree = 1
            // 'b' will store a vertex with inDegree-outDegree = 1
            int a, b;
            a = b = -1;

            // Step 2: Find a and b
            for (int i = 0; i < dg.vertices; i++)
            {
                if (outDegree[i]-inDegree[i] == 1)
                {
                    // If more than two vertices have outDegree-inDegree = 1, return false
                    if (a != -1) return false;
                    a = i;
                }

                if (inDegree[i]-outDegree[i] == 1)
                {
                    // If more than two vertices have inDegree-outDegree = 1, return false
                    if (b != -1) return false;
                    b = i;
                }
            }

            // Step 3: Except a and b, all other vertices should have in degree = out degree
            for (int i = 0; i < dg.vertices; i++)
            {
                if (i == a || i == b) continue;
                if (inDegree[i] != outDegree[i]) return false;
            }

            // Step 4: Calculate weakly connected component of the graph
            ArrayList<ArrayList<Integer>> wcc = new ConnectedComponents.DG(dg).weaklyConnectedComponents();

            // Step 5: Label the vertices with the component index they belong to
            int[] componentIndex = new int[dg.vertices];
            for (int i = 0; i < wcc.size(); i++)
            {
                for (Integer v : wcc.get(i)) componentIndex[v] = i;
            }

            // Step 6: Check if all the vertices with non-zero degree belong to a single weakly connected
            // component
            int componentID = -1;
            int i = 0;

            while (i < dg.vertices && componentID == -1)
            {
                if (inDegree[i] != 0) componentID = componentIndex[i];
                i++;
            }

            while (i < dg.vertices)
            {
                if (inDegree[i] != 0 && componentIndex[i] != componentID) return false;
                i++;
            }

            return true;
        }

        /**
         *
         * @return null if no trail is possible else ArrayList of integers containing a valid euler trail
         */
        public ArrayList<Integer> findTrail()
        {
            if (hasCycle()) return findCycle();
            if (!hasTrail()) return null;

            ArrayList<Integer> trail = new ArrayList<>();
            int a = -1; // Start vertex

            // Set a
            for (int i = 0; i < dg.vertices; i++)
            {
                if (degree[1][i]-degree[0][i] == 1)
                {
                    a = i;
                    break;
                }
            }

            // Copy the original graph
            DirectedGraph tempGraph = new DirectedGraph(dg);
            DFS(a, tempGraph, trail);

            // reverse the elements for correct order
            Collections.reverse(trail);

            return trail;
        }

        /**
         *
         * @return null if no cycle is possible else ArrayList of integers containing a valid euler cycle
         */
        public ArrayList<Integer> findCycle()
        {
            if (!hasCycle()) return null;

            ArrayList<Integer> cycle = new ArrayList<>();

            // Copy the original graph
            DirectedGraph tempGraph = new DirectedGraph(dg);
            DFS(0, tempGraph, cycle);

            // reverse the elements for correct order
            Collections.reverse(cycle);

            return cycle;
        }

        /**
         *
         * @param u parent vertex
         * @param tempGraph copy of original graph
         * @param path ArrayList to store path
         */
        private void DFS(int u, DirectedGraph tempGraph, ArrayList<Integer> path)
        {
            // Get all the neighbouring vertices of u
            ArrayList<Vertex> vertices = tempGraph.adjacencyList.get(u);

            while (!vertices.isEmpty())
            {
                Vertex v = vertices.remove(vertices.size()-1);
                DFS(v.i, tempGraph, path);
            }

            path.add(u);
        }
    }
}
