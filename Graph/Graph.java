package Graph;

import java.util.ArrayList;

public class Graph
{
    public static class Neighbour
    {
        Integer destination;
        Integer weight;

        public Neighbour(Integer destination, Integer weight)
        {
            this.destination = destination;
            this.weight = weight;
        }

        @Override
        public String toString()
        {
            if (weight != null) return "[" + destination + ", " + weight + "]";
            else return "[" + destination + "]";
        }
    }

    final ArrayList<ArrayList<Neighbour>> adjacencyList;
    int vertices;

    public Graph(int vertices)
    {
        this.vertices = vertices;

        adjacencyList = new ArrayList<>(vertices);
        for (int i = 0; i < vertices; i++) adjacencyList.add(new ArrayList<>());
    }

    public void addEdge(Integer u, Integer v, Integer weight)
    {
        adjacencyList.get(u).add(new Neighbour(v, weight));
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
            ArrayList<Neighbour> eachVertex = adjacencyList.get(i);

            System.out.printf("%2d: ", i);
            for (Neighbour neighbour : eachVertex) System.out.print(neighbour + " ");
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

        for (Neighbour neighbour: adjacencyList.get(u)) if (neighbour.destination == v) return true;

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
    Neighbour getNeighbour(int u, int v)
    {
        for (Neighbour neighbour: adjacencyList.get(u)) if (neighbour.destination == v) return neighbour;

        return null;
    }
}
