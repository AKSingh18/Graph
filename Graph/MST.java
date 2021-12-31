package Graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import static Graph.Graph.Vertex;
import static Graph.Graph.Edge;

public class MST
{
    private final UndirectedGraph ug;

    public MST(UndirectedGraph ug)
    {
        this.ug = ug;
    }

    /**
     * Prints the minimum spanning tree using prim's algorithm
     */
    public void printMST()
    {
        int[] parent = primsAlgo();

        System.out.println("Edge\tWeight");
        for (int i = 1;i < parent.length;i++)
        {
            System.out.printf("%2d-%-2d\t%3d",parent[i], i, ug.getNeighbour(parent[i], i).w);
            System.out.println();
        }
    }

    /**
     * Current implementation takes O(V^2) time.
     *
     * Test Link: https://practice.geeksforgeeks.org/problems/minimum-spanning-tree/1#
     *
     * @return parent array of the minimum spanning tree
     */
    public int[] primsAlgo()
    {
        int[] parent = new int[ug.vertices];
        int[] key = new int[ug.vertices];
        boolean[] inMstSet = new boolean[ug.vertices];

        Arrays.fill(parent, -1);
        Arrays.fill(key, Integer.MAX_VALUE);

        key[0] = 0;
        parent[0] = -2;

        for (int count = 0; count < ug.vertices-1; count++)
        {
            int u = getMinKey(key, inMstSet);
            inMstSet[u] = true;

            for (Graph.Vertex v : ug.adjacencyList.get(u))
            {
                if (!inMstSet[v.i] && v.w < key[v.i])
                {
                    parent[v.i] = u;
                    key[v.i] = v.w;
                }
            }
        }

        return parent;
    }

    private int getMinKey(int[] key, boolean[] inMstSet)
    {
        int min = Integer.MAX_VALUE, minIndex = -1;

        for (int v = 0; v < ug.vertices; v++)
        {
            if (!inMstSet[v] && key[v] < min)
            {
                min = key[v];
                minIndex = v;
            }
        }

        return minIndex;
    }

    /**
     *
     * The following kruskals method uses union-find to detect cycle while constructing the MST
     *
     * Test Link: https://practice.geeksforgeeks.org/problems/minimum-spanning-tree/1#
     *
     * @return an integer denoting the sum of weights of the edges of the MST
     */
    public int kruskals()
    {
        // Step 1: Construct edges array consisting of all the edges of the input graph
        ArrayList<Edge> edges = new ArrayList<>();
        UndirectedGraph tempUG = new UndirectedGraph(ug);

        for (int u = 0;u < tempUG.vertices;u++)
        {
            ArrayList<Vertex> vertices = tempUG.adjacencyList.get(u);

            while (!vertices.isEmpty())
            {
                /* In case of undirected graph, an edge from u-v will be present two times. Once in,
                   neighbouring vertices of u and other in neighbouring vertices of v. Since, it should
                   be traversed only one time. The latter occurrence will be removed.

                    v:  u->v
                   _v:  v->u
                */

                Vertex v = vertices.remove(vertices.size()-1);
                Vertex _v = tempUG.getNeighbour(v.i, u);

                tempUG.adjacencyList.get(v.i).remove(_v);

                // Add the edge to the list
                edges.add(new Edge(u, v.i, v.w));
            }
        }

        // Step 2: Sort the edges based on their weights
        edges.sort(new Comparator<>()
        {
            @Override
            public int compare(Edge e1, Edge e2) {
                return Integer.compare(e1.w, e2.w);
            }
        });

        // Step 3: Keep picking the edge which do not form a cycle
        int[] parent = new int[ug.vertices];
        Arrays.fill(parent, -1);

        int weight = 0;

        for (Edge e : edges)
        {
            if (find(parent, e.u) != find(parent, e.v))
            {
                union(parent, e.u, e.v);
                weight += e.w;
            }
        }

        return weight;
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
}
