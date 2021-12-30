package Graph;

import java.util.ArrayList;

public class Graph
{
    public static class Vertex
    {
        Integer i; // number of the vertex
        Integer w; // weight or capacity associated with the vertex

        public Vertex(Integer i, Integer w)
        {
            this.i = i;
            this.w = w;
        }

        @Override
        public String toString()
        {
            if (w != null) return "[" + i + ", " + w + "]";
            else return "[" + i + "]";
        }
    }

    public static class Edge
    {
        Integer u;
        Integer v;
        Integer w;

        public Edge(Integer u, Integer v, Integer w)
        {
            this.u = u;
            this.v = v;
            this.w = w;
        }

        @Override
        public String toString()
        {
            if (w == null) return  "[" + u + "->" + v + "]";
            else return "[" + u + "->" + v + " : " + w + "]";
        }
    }

    final ArrayList<ArrayList<Graph.Vertex>> adjacencyList;
    int vertices;

    public Graph(int vertices)
    {
        this.vertices = vertices;

        adjacencyList = new ArrayList<>(vertices);
        for (int i = 0; i < vertices; i++) adjacencyList.add(new ArrayList<>());
    }

    public Graph(Graph graph)
    {
        this.vertices = graph.vertices;
        this.adjacencyList = new ArrayList<>(vertices);

        for (ArrayList<Graph.Vertex> vertices : graph.adjacencyList)
            this.adjacencyList.add(new ArrayList<>(vertices));
    }

    public void addEdge(Integer u, Integer v, Integer weight)
    {
        adjacencyList.get(u).add(new Graph.Vertex(v, weight));
    }

    /*
    Consider the undirected graph:

         1 ----------- 2
         |             |
         |             |
         |             |
         3 ----------- 4

    On calling printGraph(), it will be printed in the following manner:

     1: [2] [3]
     2: [1] [4]
     3: [1] [4]
     4: [2] [3]

     In case of weighted graphs, it will be printed in the following manner:

               [3]
         1 ----------- 2
         |             |
     [2] |             | [4]
         |             |
         3 ----------- 4
               [7]

     1: [2, 3] [3, 2]
     2: [1, 3] [4, 4]
     3: [1, 2] [4, 7]
     4: [2, 4] [3, 7]
     */
    public void printGraph()
    {
        for (int i = 0;i < vertices;i++)
        {
            ArrayList<Graph.Vertex> eachVertex = adjacencyList.get(i);

            System.out.printf("%2d: ", i);
            for (Graph.Vertex v : eachVertex) System.out.print(v + " ");
            System.out.println();
        }
    }

    /**
     * @param u source vertex
     * @param v destination vertex
     * @return true if edge is found else false
     */

    boolean hasEdge(int u, int v)
    {
        if (u >= vertices) return false;

        for (Graph.Vertex vertex : adjacencyList.get(u)) if (vertex.i == v) return true;

        return false;
    }

    /**
     * The method may produce NullPointerException. Usage of hasEdge(int, int) is suggested to avoid it.
     * @see Graph#hasEdge(int, int)
     *
     * @param u source vertex
     * @param v destination vertex
     * @return Neighbour if found else returns null.
     */
    Graph.Vertex getNeighbour(int u, int v)
    {
        for (Graph.Vertex vertex : adjacencyList.get(u)) if (vertex.i == v) return vertex;

        return null;
    }
}
