package Graph;

public class UndirectedGraph extends Graph
{
    public UndirectedGraph(int vertices)
    {
        super(vertices);
    }

    @Override
    public void addEdge(Integer u, Integer v, Integer weight)
    {
        adjacencyList.get(u).add(new Neighbour(v, weight));
        adjacencyList.get(v).add(new Neighbour(u, weight));
    }
}
