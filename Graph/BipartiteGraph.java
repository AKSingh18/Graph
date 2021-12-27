package Graph;

import java.util.ArrayList;

/**
 *
 * The following class can be used to create objects of bipartite graph. It is important to understand how the
 * vertices should be labelled before adding edges to the graph.
 *
 * Labelling Rules:
 *     1: L is the number of vertices in set L (left)
 *     2: R is number of vertices in set R (right)
 *     3: Vertices of L should be labelled from 0 to L-1
 *     4: Vertices of R should be labelled from L to R-1
 *
 * Example:
 *
 *     L = R = 3
 *
 *    [L]    [R]
 *     0 ---> 3
 *     1 ---> 4
 *     2 ---> 5
 *
 * NOTE: No check has been made in the addEdge method of the super class which prevents an addition of edge from
 *       R to L. This is the responsibility of the user.
 */
public class BipartiteGraph extends Graph
{
    final int L;
    final int R;

    /**
     *
     * @param L number of vertices in set L
     * @param R number of vertices in set R
     */
    public BipartiteGraph(int L, int R)
    {
        // create a graph with L+R vertices
        super(L+R);

        this.L = L;
        this.R = R;
    }

    @Override
    public void printGraph()
    {
        // print neighbouring vertices only for the vertices of set L
        for (int i = 0;i < L;i++)
        {
            ArrayList<Vertex> eachVertex = adjacencyList.get(i);

            System.out.printf("%2d: ", i);
            for (Graph.Vertex v : eachVertex) System.out.print(v + " ");
            System.out.println();
        }
    }
}
