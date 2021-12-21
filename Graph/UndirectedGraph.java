package Graph;

public class UndirectedGraph extends Graph
{
    public UndirectedGraph(int vertices)
    {
        super(vertices);
    }

    public UndirectedGraph(UndirectedGraph graph)
    {
        super(graph);
    }

    @Override
    public void addEdge(Integer u, Integer v, Integer weight)
    {
        adjacencyList.get(u).add(new Graph.Vertex(v, weight));
        adjacencyList.get(v).add(new Graph.Vertex(u, weight));
    }
}
