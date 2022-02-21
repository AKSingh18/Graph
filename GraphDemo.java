import Graph.*;

public class GraphDemo
{
    /**
     * Visit here to see bipartite graph implemented below:
     * @see Files/minimum-vertex-covered-labelled.jpg
     *
     */
    public static void main(String[] args)
    {
        BipartiteGraph bg = new BipartiteGraph(7, 7);

        bg.addEdge(0, 9, null);
        bg.addEdge(0, 10, null);
        bg.addEdge(1, 7, null);
        bg.addEdge(1, 8, null);
        bg.addEdge(1, 11, null);
        bg.addEdge(2, 9, null);
        bg.addEdge(2, 10, null);
        bg.addEdge(2, 12, null);
        bg.addEdge(3, 8, null);
        bg.addEdge(3, 11, null);
        bg.addEdge(3, 12, null);
        bg.addEdge(4, 9, null);
        bg.addEdge(4, 10, null);
        bg.addEdge(4, 12, null);
        bg.addEdge(5, 7, null);
        bg.addEdge(5, 11, null);
        bg.addEdge(5, 12, null);
        bg.addEdge(5, 13, null);
        bg.addEdge(6, 12, null);

        MinVertexCover minVertexCover = new MinVertexCover(bg);
        System.out.println(minVertexCover.minVertexCover());
    }
}
