package Graph;

import java.util.ArrayList;

public class MinVertexCover
{
    private final BipartiteGraph biGraph;

    public MinVertexCover(BipartiteGraph biGraph)
    {
        this.biGraph = biGraph;
    }

    /**
     *
     * @return ArrayList of integers which are part of min vertex cover
     */
    public ArrayList<Integer> minVertexCover()
    {
        // Step 1: Obtain all the edges part of the maximum matching
        MaxMatching matching = new MaxMatching(biGraph);
        ArrayList<ArrayList<Integer>> edges = matching.maxMatchingEdges();

        // Step 2: Find all the unmatched vertices of set L and R
        boolean[] hasMatching = new boolean[biGraph.vertices];
        for (ArrayList<Integer> e : edges)
        {
            hasMatching[e.get(0)] = true;
            hasMatching[e.get(1)] = true;
        }

        boolean[] isCovered = new boolean[biGraph.vertices];

        // Step 3: Mark all the vertices of set R covered, which are connected to
        // all the unmatched vertices of set L
        for (int i = 0;i < biGraph.L;i++)
        {
            if (!hasMatching[i])
            {
                for (Graph.Vertex v : biGraph.adjacencyList.get(i)) isCovered[v.i] = true;
            }
        }

        // Step 4: Mark all the vertices of set L covered, which are
        // connected to an unmatched vertex of set R
        for (int i = 0;i < biGraph.R;i++)
        {
            if (!hasMatching[i+biGraph.L])
            {
                for (Graph.Vertex v : biGraph.adjacencyList.get(i+biGraph.L)) isCovered[v.i] = true;
            }
        }

        /* Step 5:
           Mark all the vertices of set R covered which are
               a: part of maximum matching
               b: are not matched with a covered vertex of set L
         */
        for (ArrayList<Integer> e : edges)
        {
            if (!isCovered[e.get(0)]) isCovered[e.get(1)] = true;
        }

        // Step 6: Add all the covered vertices to the vertex cover
        ArrayList<Integer> minVertexCover = new ArrayList<>();
        for (int i = 0;i < biGraph.vertices;i++) if (isCovered[i]) minVertexCover.add(i);

        return minVertexCover;
    }
}
