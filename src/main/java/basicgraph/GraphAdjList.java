package basicgraph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A class that implements a directed graph.
 * The graph may have self-loops, parallel edges.
 * Vertices are labeled by integers 0 .. n-1
 * and may also have String labels.
 * The edges of the graph are not labeled.
 * Representation of edges via adjacency lists.
 *
 * @author UCSD MOOC development team and YOU
 */
public class GraphAdjList extends Graph {

    private Map<Integer, ArrayList<Integer>> adjListsMap;

    /**
     * Create a new empty Graph
     */
    public GraphAdjList() {
        adjListsMap = new HashMap<>();

    }

    /**
     * Implement the abstract method for adding a vertex.
     */
    public void implementAddVertex() {
        int v = getNumVertices();
        ArrayList<Integer> neighbors = new ArrayList<>();
        adjListsMap.put(v, neighbors);
    }

    /**
     * Implement the abstract method for adding an edge.
     *
     * @param v the index of the start point for the edge.
     * @param w the index of the end point for the edge.
     */
    public void implementAddEdge(int v, int w) {
        (adjListsMap.get(v)).add(w);
    }

    /**
     * Implement the abstract method for finding all
     * out-neighbors of a vertex.
     * If there are multiple edges between the vertex
     * and one of its out-neighbors, this neighbor
     * appears once in the list for each of these edges.
     *
     * @param v the index of vertex.
     * @return List<Integer> a list of indices of vertices.
     */
    public List<Integer> getNeighbors(int v) {
        return new ArrayList<Integer>(adjListsMap.get(v));
    }

    /**
     * Implement the abstract method for finding all
     * in-neighbors of a vertex.
     * If there are multiple edges from another vertex
     * to this one, the neighbor
     * appears once in the list for each of these edges.
     *
     * @param vertex the index of vertex.
     * @return List<Integer> a list of indices of vertices.
     */
    public List<Integer> getInNeighbors(final int vertex) {
        final List<Integer> inNeighbors = new ArrayList<Integer>();
        for (int u : adjListsMap.keySet()) {
            //iterate through all edges in u's adjacency list and
            //add u to the inNeighbor list of vertex whenever an edge
            //with startpoint u has endpoint vertex.
            for (int w : adjListsMap.get(u)) {
                if (vertex == w) {
                    inNeighbors.add(u);
                }
            }
        }
        return inNeighbors;
    }


    /**
     * Implement the abstract method for finding all
     * vertices reachable by two hops from vertex.
     *
     * @param vertex the index of vertex.
     * @return List<Integer> a list of indices of vertices.
     */
    public List<Integer> getDistance2(final int vertex) {
        // XXX: Implement this method in week 1
        final List<Integer> twoHop = new ArrayList<>();
        //take list of neighbors for vertex vertex
        if (vertex <= adjListsMap.size()) {
            final ArrayList<Integer> firstList = adjListsMap.get(vertex);
            //and iterate it
            for (int i = 0; i < firstList.size(); i++) {
                //take neighbours vertexes
                int neigVert = firstList.get(i);
                //and find their neighbors
                final ArrayList<Integer> secondList = adjListsMap.get(neigVert);
                //loop over it and add to list
                for (int a = 0; a < secondList.size(); a++) {
                    int neigVertSec = secondList.get(a);
                    //delete duplicate vertexes
                    // if(!twoHop.contains(neigVertSec)) {
                    twoHop.add(neigVertSec);
                    //System.out.println("______"+neigVertSec);
                    //		       }
                }
            }
        }
        return twoHop;
    }

    /**
     * Generate string representation of adjacency list
     *
     * @return the String
     */
    public String adjacencyString() {
        String s = "Adjacency list";
        s += " (size " + getNumVertices() + "+" + getNumEdges() + " integers):";

        for (int v : adjListsMap.keySet()) {
            s += "\n\t" + v + ": ";
            for (int w : adjListsMap.get(v)) {
                s += w + ", ";
            }
        }
        return s;
    }
}
