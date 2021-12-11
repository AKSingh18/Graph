package Graph;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Contains methods to find articulation points in the graph
 */
public class Miscellaneous
{
    private final Graph graph;

    public Miscellaneous(UndirectedGraph graph)
    {
        this.graph = graph;
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

        int[] parent = new int[graph.vertices];
        int[] lowLink = new int[graph.vertices];
        int[] discovery = new int[graph.vertices];
        boolean[] isAP = new boolean[graph.vertices];

        Arrays.fill(parent, -1);

        for (int i = 0;i < graph.vertices;i++)
        {
            if (parent[i] == -1)
            {
                parent[i] = -2;
                DFS(i, parent, lowLink, discovery, isAP);
            }
        }

        for (int i = 0;i < graph.vertices;i++) if (isAP[i]) points.add(i);

        return points;
    }

    private void DFS(int source, int[] parent, int[] lowLink, int[] discovery, boolean[] isAP)
    {
        int children = 0;

        lowLink[source] = discovery[source] = time++;

        for (Graph.Vertex v : graph.adjacencyList.get(source))
        {
            if (parent[v.i] == -1)
            {
                children++;
                parent[v.i] = source;
                DFS(v.i, parent, lowLink, discovery, isAP);

                lowLink[source] = Math.min(lowLink[source], lowLink[v.i]);

                if (parent[source] != -2 && lowLink[v.i] >= discovery[source]) isAP[source] = true;
            }
            else if (v.i != parent[source]) lowLink[source] = Math.min(lowLink[source], discovery[v.i]);
        }

        if (parent[source] == -2 && children > 1) isAP[source] = true;
    }
}
