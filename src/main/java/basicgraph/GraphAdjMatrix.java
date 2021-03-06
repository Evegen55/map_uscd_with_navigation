package basicgraph;

import java.util.ArrayList;
import java.util.List;

/**
 * A class that implements a directed graph.
 * The graph may have self-loops, parallel edges.
 * Vertices are labeled by integers 0 .. n-1
 * and may also have String labels.
 * The edges of the graph are not labeled.
 * Representation of edges via an adjacency matrix.
 *
 * @author UCSD MOOC development team and YOU
 */
public class GraphAdjMatrix extends Graph {

    private static final int DEFAULT_NUM_VERTICES = 5;

    private int[][] adjMatrix;

    /**
     * Create a new empty Graph
     */
    public GraphAdjMatrix() {
        adjMatrix = new int[DEFAULT_NUM_VERTICES][DEFAULT_NUM_VERTICES];
    }

    /**
     * Implement the abstract method for adding a vertex.
     * If need to increase dimensions of matrix, double them
     * to amortize cost.
     */
    public void implementAddVertex() {
        int v = getNumVertices();
        if (v >= adjMatrix.length) {
            int[][] newAdjMatrix = new int[v * 2][v * 2];
            for (int i = 0; i < adjMatrix.length; i++) {
                for (int j = 0; j < adjMatrix.length; j++) {
                    newAdjMatrix[i][j] = adjMatrix[i][j];
                }
            }
            adjMatrix = newAdjMatrix;
        }
        for (int i = 0; i < adjMatrix[v].length; i++) {
            adjMatrix[v][i] = 0;
        }
    }

    /**
     * Implement the abstract method for adding an edge.
     * Allows for multiple edges between two points:
     * the entry at row vertex_start, column vertex_end stores the number of such edges.
     *
     * @param vertex_start the index of the start point for the edge.
     * @param vertex_end   the index of the end point for the edge.
     */
    public void implementAddEdge(final int vertex_start, final int vertex_end) {
        adjMatrix[vertex_start][vertex_end] += 1;
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
        final List<Integer> neighbors = new ArrayList<>();
        for (int i = 0; i < getNumVertices(); i++) {
            for (int j = 0; j < adjMatrix[v][i]; j++) {
                neighbors.add(i);
            }
        }
        return neighbors;
    }

    /**
     * Implement the abstract method for finding all
     * in-neighbors of a vertex.
     * If there are multiple edges from another vertex
     * to this one, the neighbor
     * appears once in the list for each of these edges.
     *
     * @param v the index of vertex.
     * @return List<Integer> a list of indices of vertices.
     */
    public List<Integer> getInNeighbors(int v) {
        List<Integer> inNeighbors = new ArrayList<Integer>();
        for (int i = 0; i < getNumVertices(); i++) {
            for (int j = 0; j < adjMatrix[i][v]; j++) {
                inNeighbors.add(i);
            }
        }
        return inNeighbors;
    }

    //For learners to implement

    /**
     * Implement the abstract method for finding all
     * vertices reachable by two hops from v.
     * Use matrix multiplication to record length 2 paths.
     *
     * @param v the index of vertex.
     * @return List<Integer> a list of indices of vertices.
     */
    public List<Integer> getDistance2(int v) {
        // XXX: Implement this method in week 1
        //System.out.println(adjacencyString());
        //------------------------------------------------------------------
        int[][] SquareAdjMatrix = sqrMatrix();
        List<Integer> twoHop = new ArrayList<Integer>();
        for (int i = 0; i < SquareAdjMatrix.length; i++) {
            for (int j = 0; j < SquareAdjMatrix[v][i]; j++) {
                if (SquareAdjMatrix[v][i] > 0) {
                    twoHop.add(SquareAdjMatrix[v][i] + i - 1);                //WHYYYYYYYYYYYYYYY?????????????
                }
            }
        }
        return twoHop;

    }

    /**
     * Generate string representation of adjacency matrix
     *
     * @return the String
     */
    public String adjacencyString() {
        int dim = adjMatrix.length;
        String s = "Adjacency matrix";
        s += " (size " + dim + "x" + dim + " = " + dim * dim + " integers):";
        for (int i = 0; i < dim; i++) {
            s += "\n\t" + i + ": ";
            for (int j = 0; j < adjMatrix[i].length; j++) {
                s += adjMatrix[i][j] + ", ";
            }
        }
        return s;
    }


    protected int[][] sqrMatrix() {

        int vert = getNumVertices();
        int countF = 0;
        int countS = 0;

        int[][] squareAdjMatrix = new int[vert][vert];

        for (int k = 0; k < vert; k++) {
            for (int n = 0; n < vert; n++) {
                for (int r = 0; r < vert; r++) {
                    for (int i = 0; i < vert; i++) {
                        countF = adjMatrix[k][r] * adjMatrix[r][n];
                        countS += countF;
                        break;
                    }
                    squareAdjMatrix[k][n] = countS;
                }
                countS = 0;
            }
        }
        return squareAdjMatrix;
    }

    protected int[][] sqrMatrixbyIra() {
        int[][] c = new int[getNumVertices()][getNumVertices()];
        for (int i = 0; i < getNumVertices(); i++) {
            for (int j = 0; j < getNumVertices(); j++) {
                for (int k = 0; k < getNumVertices(); k++) {
                    c[i][j] += adjMatrix[i][k] * adjMatrix[k][j];
                }
            }
        }
        return c;
    }

}
