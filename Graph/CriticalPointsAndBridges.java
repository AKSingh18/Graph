package Graph;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Contains methods to find
 *     1: articulation points
 *     2: bridges
 */
public class CriticalPointsAndBridges
{
    private final UndirectedGraph ug;

    public CriticalPointsAndBridges(UndirectedGraph ug)
    {
        this.ug = ug;
    }

    private int time;

    /**
     * Test Link: https://practice.geeksforgeeks.org/problems/articulation-point-1/0/#
     *
     * @return array list containing all the articulation points present in the graph
     */
    public ArrayList<Integer> articulationPoints()
    {
        ArrayList<Integer> points = new ArrayList<>();
        time = 0;

        int[] parent = new int[ug.vertices];
        int[] lowLink = new int[ug.vertices];
        int[] discovery = new int[ug.vertices];
        boolean[] isAP = new boolean[ug.vertices];

        Arrays.fill(parent, -1);

        for (int i = 0; i < ug.vertices; i++)
        {
            if (parent[i] == -1)
            {
                parent[i] = -2;
                DFS(i, parent, lowLink, discovery, isAP);
            }
        }

        for (int i = 0; i < ug.vertices; i++) if (isAP[i]) points.add(i);

        return points;
    }

    private void DFS(int u, int[] parent, int[] lowLink, int[] discovery, boolean[] isAP)
    {
        int children = 0;

        lowLink[u] = discovery[u] = time++;

        for (Graph.Vertex v : ug.adjacencyList.get(u))
        {
            if (parent[v.i] == -1)
            {
                children++;
                parent[v.i] = u;
                DFS(v.i, parent, lowLink, discovery, isAP);

                lowLink[u] = Math.min(lowLink[u], lowLink[v.i]);

                if (parent[u] != -2 && lowLink[v.i] >= discovery[u]) isAP[u] = true;
            }
            else if (v.i != parent[u]) lowLink[u] = Math.min(lowLink[u], discovery[v.i]);
        }

        if (parent[u] == -2 && children > 1) isAP[u] = true;
    }

    /**
     * Test Link: https://practice.geeksforgeeks.org/problems/bridge-edge-in-graph/1#
     *
     * @return Array list of bridges. Each bridge is represented as an array list of size 2.
     */
    public ArrayList<ArrayList<Integer>> findBridges()
    {
        ArrayList<ArrayList<Integer>> bridges = new ArrayList<>();

        time = 0;

        int[] parent = new int[ug.vertices];
        int[] lowLink = new int[ug.vertices];
        int[] discovery = new int[ug.vertices];

        Arrays.fill(parent, -1);

        for (int i = 0; i < ug.vertices; i++)
        {
            if (parent[i] == -1)
            {
                parent[i] = -2;
                DFS(i, parent, lowLink, discovery, bridges);
            }
        }

        return bridges;
    }

    private void DFS(int u, int[] parent, int[] lowLink, int[] discovery,
                     ArrayList<ArrayList<Integer>> bridges)
    {
        lowLink[u] = discovery[u] = time++;

        for (Graph.Vertex v : ug.adjacencyList.get(u))
        {
            if (parent[v.i] == -1)
            {
                parent[v.i] = u;
                DFS(v.i, parent, lowLink, discovery, bridges);

                lowLink[u] = Math.min(lowLink[u], lowLink[v.i]);

                if (lowLink[v.i] > discovery[u])
                {
                    ArrayList<Integer> edge = new ArrayList<>(2);
                    edge.add(u);
                    edge.add(v.i);

                    bridges.add(edge);
                }
            }
            else if (v.i != parent[u]) lowLink[u] = Math.min(lowLink[u], discovery[v.i]);
        }
    }
}
