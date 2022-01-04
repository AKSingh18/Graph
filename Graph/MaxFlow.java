package Graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

public class MaxFlow
{
    private final int source;
    private final int sink;

    private final Graph graph;
    private Graph residualGraph;

    public MaxFlow(Graph graph, int source, int sink)
    {
        this.graph = graph;
        this.source = source;
        this.sink = sink;
    }

    /**
     *
     * @return Final residual graph after finding max flow
     */
    Graph getFinalResidualGraph()
    {
        dinics();
        return residualGraph;
    }

    private void initResidualGraph()
    {
        residualGraph = new Graph(graph.vertices);

        // Construct residual graph
        for (int u = 0;u < graph.vertices;u++)
        {
            for (Graph.Vertex v : graph.adjacencyList.get(u))
            {
                // If forward edge already exists, update its weight
                if (residualGraph.hasEdge(u, v.i))
                    residualGraph.getEdge(u, v.i).w += v.w;
                // In case it does not exist, create one
                else residualGraph.addEdge(u, v.i, v.w);

                // If backward edge does not already exist, add it
                if (!residualGraph.hasEdge(v.i, u))
                    residualGraph.addEdge(v.i, u, 0);
            }
        }
    }

    /**
     * Test Link: https://practice.geeksforgeeks.org/problems/find-the-maximum-flow2126/1
     *
     * @return maximum flow
     */
    public int edmondsKarp()
    {
        initResidualGraph();

        int[] parent;
        int maxFlow = 0;
        while ((parent = findAugmentedPath()) != null)
        {
            int flow = Integer.MAX_VALUE;
            int currentVertex = sink;

            while (currentVertex != source)
            {
                flow = Math.min(residualGraph.getEdge(parent[currentVertex], currentVertex).w, flow);
                currentVertex = parent[currentVertex];
            }

            currentVertex = sink;
            while (currentVertex != source)
            {
                residualGraph.getEdge(parent[currentVertex], currentVertex).w =
                        Math.max(residualGraph.getEdge(parent[currentVertex], currentVertex).w -=flow, 0);

                residualGraph.getEdge(currentVertex, parent[currentVertex]).w += flow;

                currentVertex = parent[currentVertex];
            }

            maxFlow += flow;
        }

        return maxFlow;
    }

    /**
     * Uses BFS to find path from source to sink
     *
     * @return parent[] if path is found else null
     */
    private int[] findAugmentedPath()
    {
        LinkedList<Integer> queue = new LinkedList<>();
        int[] parent = new int[residualGraph.vertices];
        Arrays.fill(parent, -1);

        parent[source] = -2;
        queue.add(source);

        while (!queue.isEmpty() && parent[sink] == -1)
        {
            int u = queue.remove();

            for (Graph.Vertex v : residualGraph.adjacencyList.get(u))
            {
                if (parent[v.i] == -1 && v.w > 0)
                {
                    parent[v.i] = u;
                    queue.add(v.i);
                }
            }
        }

        // If queue is empty, then sink cannot be located
        if (queue.isEmpty()) return null;
        else return parent;
    }

    /**
     * Test Link: https://practice.geeksforgeeks.org/problems/find-the-maximum-flow2126/1
     *
     * @return max flow
     */
    public int dinics()
    {
        initResidualGraph();

        int maxFlow = 0;

        int[] level;
        while ((level = BFS()) != null)
        {
            int flow;
            while ((flow = DFS(source, level, Integer.MAX_VALUE)) != -1) maxFlow += flow;
        }

        return maxFlow;
    }

    /**
     * Builds up a level array depending upon current state of residual graph
     *
     * @return null if no path exists from source to sink
     *         else an array denoting level of each vertex
     */
    private int[] BFS()
    {
        // Besides, denoting the level for each vertex, level array is also used to check if a vertex
        // has been visited before or not
        int[] level = new int[graph.vertices];
        Arrays.fill(level, -1);

        LinkedList<Integer> queue = new LinkedList<>();

        queue.add(source);
        level[source] = 0;

        while (!queue.isEmpty() && level[sink] == -1)
        {
            int u = queue.removeFirst();

            for (Graph.Vertex v : residualGraph.adjacencyList.get(u))
            {
                if (level[v.i] == -1 && v.w > 0)
                {
                    queue.add(v.i);
                    level[v.i] = level[u]+1;
                }
            }
        }

        if (level[sink] == -1) return null;
        else return level;
    }


    /**
     * Sends flow from source to sink using residual graph and level array
     *
     * @param u parent vertex
     * @param level level array
     * @param flow current flow
     * @return positive integer denoting flow from source to sink if possible else -1
     */
    private int DFS(int u, int[] level, int flow)
    {
        if (u == sink) return flow;

        for (Graph.Vertex v : residualGraph.adjacencyList.get(u))
        {
            if (level[v.i] == level[u]+1 && v.w > 0)
            {
                /* Obtain the bottleneck flow of the path u -> v -> .... -> sink. Initially u is source.
                   If no such path exists, -1 is returned.

                   In the below DFS call, @param flow is passed Math.min(flow, v.w). Since, the flow that
                   can be pushed from u -> v is the minimum of present flow and residual capacity of edge
                   u -> v which is v.w.
                 */
                int bottleNeck = DFS(v.i, level, Math.min(flow, v.w));

                if (bottleNeck > 0)
                {
                    residualGraph.getEdge(v.i, u).w += bottleNeck;
                    v.w -= bottleNeck;

                    return bottleNeck;
                }
            }
        }

        return -1;
    }

    /**
     *
     * The method uses push-relabel algorithm to find max flow.
     *
     * Test Link: https://practice.geeksforgeeks.org/problems/find-the-maximum-flow2126/1
     *
     * @return maximum flow
     */
    public int pushRelabel()
    {
        initResidualGraph();

        LinkedList<Integer> queue = new LinkedList<>();

        // Step 1: Initialize pre-flow
        int[] e = new int[graph.vertices]; // to store excess flow
        int[] h = new int[graph.vertices]; // to store height of vertices
        boolean[] inQueue = new boolean[graph.vertices];

        h[source] = graph.vertices;

        for (Graph.Vertex v : graph.adjacencyList.get(source))
        {
            residualGraph.getEdge(source, v.i).w = 0;
            residualGraph.getEdge(v.i, source).w = v.w;

            e[v.i] = v.w;

            if (v.i != sink)
            {
                queue.add(v.i);
                inQueue[v.i] = true;
            }
        }

        // Step 2: Update the pre-flow while there remains an applicable push or relabel operation
        while (!queue.isEmpty())
        {
            int u = queue.removeFirst();
            inQueue[u] = false;

            relabel(u, h);
            push(u, e, h, queue, inQueue);
        }

        return e[sink];
    }

    /**
     *
     * @param u overflowing vertex
     * @param h height array
     */
    private void relabel(int u, int[] h)
    {
        int minHeight = Integer.MAX_VALUE;

        for (Graph.Vertex v : residualGraph.adjacencyList.get(u))
        {
            if (v.w > 0) minHeight = Math.min(h[v.i], minHeight);
        }

        h[u] = minHeight+1;
    }

    /**
     *
     * @param u overflowing vertex
     * @param e excess flow array
     * @param h height array
     */
    private void push(int u, int[] e, int[] h, LinkedList<Integer> queue,
                      boolean[] inQueue)
    {
        for (Graph.Vertex v : residualGraph.adjacencyList.get(u))
        {
            // after pushing flow if there is no excess flow, then break
            if (e[u] == 0) break;

            // push more flow to the adjacent v if possible
            if (v.w > 0 && h[v.i] < h[u])
            {
                int f = Math.min(e[u], v.w);

                v.w -= f;
                residualGraph.getEdge(v.i,u).w += f;

                e[u] -= f;
                e[v.i] += f;

                if (!inQueue[v.i] && v.i != source && v.i != sink)
                {
                    queue.add(v.i);
                    inQueue[v.i] = true;
                }
            }
        }

        if (e[u] != 0)
        {
            queue.add(u);
            inQueue[u] = true;
        }
    }

    /**
     *
     * @return ArrayList of edges in min cut. Each edge is represented as an ArrayList of size 2
     */
    public ArrayList<ArrayList<Integer>> minCutEdges()
    {
        ArrayList<ArrayList<Integer>> edges = new ArrayList<>();

        // Step 1: Run edmonds karp to obtain the final residual graph
        edmondsKarp();

        // Step 2: Obtain all vertices reachable from source
        int[] parent = DFS(source);

        // Step 3: Print all the edges from visited vertices to unvisited vertices in the parent array
        for (int u = 0; u < parent.length;u++)
        {
            if (parent[u] != -1)
            {
                // graph will be used for printing the edges since residual graph also consists
                // of residual edges
                for (Graph.Vertex v : graph.adjacencyList.get(u))
                {
                    if (parent[v.i] == -1)
                    {
                        ArrayList<Integer> edge = new ArrayList<>(2);

                        edge.add(u);
                        edge.add(v.i);

                        edges.add(edge);
                    }
                }
            }
        }

        return edges;
    }

    /**
     * Traverses the residual graph starting from source.
     *
     * Note: An edge of weight 0 is not considered as a valid edge for traversal.
     *
     * @param u source vertex
     * @return parent array
     */
    private int[] DFS(int u)
    {
        int[] parent = new int[graph.vertices];
        Arrays.fill(parent, -1);

        parent[u] = -2;
        DFS(u, parent);

        return parent;
    }

    private void DFS(int u, int[] parent)
    {
        for (Graph.Vertex v : residualGraph.adjacencyList.get(u))
        {
            if (parent[v.i] == -1 && v.w > 0)
            {
                parent[v.i] = u;
                DFS(v.i, parent);
            }
        }
    }
}
