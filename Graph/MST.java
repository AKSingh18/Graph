package Graph;

import java.util.Arrays;

public class MST
{
    private Graph graph;

    public MST(UndirectedGraph graph)
    {
        this.graph = graph;
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
            System.out.printf("%2d-%-2d\t%3d",parent[i], i, graph.getNeighbour(parent[i], i).w);
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
        int[] parent = new int[graph.vertices];
        int[] key = new int[graph.vertices];
        boolean[] inMstSet = new boolean[graph.vertices];

        Arrays.fill(parent, -1);
        Arrays.fill(key, Integer.MAX_VALUE);

        key[0] = 0;
        parent[0] = -2;

        for (int count = 0;count < graph.vertices-1;count++)
        {
            int u = getMinKey(key, inMstSet);
            inMstSet[u] = true;

            for (Graph.Vertex v : graph.adjacencyList.get(u))
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

        for (int v = 0; v < graph.vertices; v++)
        {
            if (!inMstSet[v] && key[v] < min)
            {
                min = key[v];
                minIndex = v;
            }
        }

        return minIndex;
    }
}
