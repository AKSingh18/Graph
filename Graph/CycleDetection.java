package Graph;

import java.util.ArrayList;
import java.util.Arrays;

import static Graph.Graph.Vertex;

/**
 *
 * This class contains two static nested classes UG (UndirectedGraph) and DG (DirectedGraph) which are used to
 * detect cycle in their respective graph type.
 */
public class CycleDetection
{
    public static class UG
    {
        private final UndirectedGraph ug;

        public UG(UndirectedGraph ug)
        {
            this.ug = ug;
        }

        /**
         *
         * Uses union-find algorithm
         *
         * @return true if the graph has a cycle or else false
         */
        public boolean unionFind()
        {
            UndirectedGraph tempUG = new UndirectedGraph(ug);

            int[] parent = new int[ug.vertices];
            Arrays.fill(parent, -1);

            for (int u = 0;u < ug.vertices;u++)
            {
                ArrayList<Vertex> vertices = tempUG.adjacencyList.get(u);

                while (!vertices.isEmpty())
                {
                    /* In case of undirected graph, an edge from u-v will be present two times. Once in,
                       neighbouring vertices of u and other in neighbouring vertices of v. Since,
                       it should be used only one once. The latter occurrence will be removed.

                        v:  u->v
                       _v:  v->u
                     */

                    Vertex v = vertices.remove(vertices.size()-1);
                    Vertex _v = tempUG.getEdge(v.i, u);

                    if (find(parent, u) == find(parent, v.i)) return true;
                    else
                    {
                        union(parent, u, v.i);

                        // remove latter occurrence
                        tempUG.adjacencyList.get(v.i).remove(_v);
                    }
                }
            }

            return false;
        }

        // Makes parent of u the parent of v
        private void union(int[] parent, int u, int v)
        {
            int x = find(parent, u);
            int y = find(parent, v);

            parent[y] = x;
        }

        // Returns the root parent of i
        private int find(int[] parent, int i)
        {
            if (parent[i] == -1) return i;
            else return find(parent, parent[i]);
        }

        /**
         *
         * Uses DFS to find cycle in the input graph
         *
         * @return true if the graph has a cycle or else false
         */
        public boolean DFS()
        {
            int[] parent = new int[ug.vertices];
            Arrays.fill(parent, -1);

            for (int u = 0; u < ug.vertices; u++)
            {
                if (parent[u] == -1)
                {
                    parent[u] = -2;
                    if (DFS(u, parent)) return true;
                }
            }

            return false;
        }

        private boolean DFS(int u, int[] parent)
        {
            for (Vertex v : ug.adjacencyList.get(u))
            {
                boolean hasCycle = false;

                if (parent[v.i] == -1)
                {
                    parent[v.i] = u;
                    hasCycle = DFS(v.i, parent);
                }
                else if (parent[u] != v.i) hasCycle = true;

                if (hasCycle) return true;
            }

            return false;
        }
    }

    public static class DG
    {
        private final DirectedGraph dg;

        public DG(DirectedGraph dg)
        {
            this.dg = dg;
        }

        /**
         *
         * Uses DFS to find cycle in the input graph
         *
         * @return true if the graph has a cycle or else false
         */
        public boolean DFS()
        {
            int[] parent = new int[dg.vertices];
            boolean[] onStack = new boolean[dg.vertices];
            Arrays.fill(parent, -1);

            for (int u = 0; u < dg.vertices; u++)
            {
                if (parent[u] == -1)
                {
                    parent[u] = -2;
                    if (DFS(u, parent, onStack)) return true;
                }
            }

            return false;
        }

        private boolean DFS(int u, int[] parent, boolean[] onStack)
        {
            onStack[u] = true;

            for (Vertex v : dg.adjacencyList.get(u))
            {
                boolean hasCycle = false;

                if (parent[v.i] == -1)
                {
                    parent[v.i] = u;
                    hasCycle = DFS(v.i, parent, onStack);
                }
                else if (onStack[v.i]) hasCycle = true;

                if (hasCycle) return true;
            }

            onStack[u] = false;

            return false;
        }
    }
}
