package Graph;

public class DirectedGraph extends Graph
{
    private DirectedGraph transpose;

    public DirectedGraph(int vertices) {
        super(vertices);
    }

    public DirectedGraph(DirectedGraph dg)
    {
        super(dg);
    }

    @Override
    public void addEdge(Integer u, Integer v, Integer weight)
    {
        adjacencyList.get(u).add(new Graph.Vertex(v, weight));
    }

    private void setTranspose()
    {
        // Create a new transpose graph and set the current graph as its transpose
        transpose = new DirectedGraph(vertices);
        transpose.transpose = this;

        for (int i = 0;i < vertices;i++)
        {
            for (Graph.Vertex v : adjacencyList.get(i)) transpose.addEdge(v.i, i, v.w);
        }
    }

    public DirectedGraph getTranspose()
    {
        if (transpose == null) setTranspose();
        return transpose;
    }
}
