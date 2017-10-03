package basicgraph;

import org.junit.Before;
import org.junit.Test;
import util.GraphLoader;
import util.PathsToTheData;

import java.util.List;

/**
 * @author (created on 10/3/2017).
 */
public class GraphAdjListTest {

    @Before
    public void setUp() throws Exception {
        GraphLoader.createIntersectionsFile(PathsToTheData.UCSD_MAP, PathsToTheData.UCSD_INTERSECTIONS);
        GraphLoader.createIntersectionsFile(PathsToTheData.SIMPLETEST_MAP, PathsToTheData.SIMPLETEST_INTERSECTIONS);
    }

    @org.junit.After
    public void tearDown() throws Exception {
    }

    @org.junit.Test
    public void implementAddVertex() throws Exception {
    }

    @org.junit.Test
    public void implementAddEdge() throws Exception {
    }

    @org.junit.Test
    public void getNeighbors() throws Exception {
    }

    @org.junit.Test
    public void getInNeighbors() throws Exception {
    }

    @Test
    public void getDistance2() throws Exception {
        //For testing Part 2 functionality
        // Test your distance2 code here.
        System.out.println("Testing distance-two methods on sample graphs...");
        System.out.println("Goal: implement method using two approaches.");

        GraphAdjList graphFromFileMy = new GraphAdjList();
        GraphLoader.loadRoutes(PathsToTheData.ROUTES_UA, graphFromFileMy);
        System.out.println("/////");
        List<Integer> list = graphFromFileMy.getDistance2(2);
        System.out.println("/---------");
        System.out.println(graphFromFileMy);
        System.out.println("/---------");
        System.out.println(list);
    }

    @org.junit.Test
    public void adjacencyString() throws Exception {
    }

    @Test
    public void test_main1() {


        // For testing of Part 1 functionality
        // Add your tests here to make sure your degreeSequence method is returning
        // the correct list, after examining the graphs.
        System.out.println("Loading graphs based on real data...");
        System.out.println("Goal: use degree sequence to analyse graphs.");

        System.out.println("****");
        System.out.println("Roads / intersections:");
        GraphAdjList graphFromFile = new GraphAdjList();
        GraphLoader.loadRoadMap(PathsToTheData.SIMPLETEST_MAP, graphFromFile);
        System.out.println(graphFromFile);

        System.out.println("Observe all degrees are <= 12.");
        System.out.println("****");

        System.out.println("\n****");



    }

    @Test
    public void test_main2() {
        // You can test with real road data here.  Use the data files in data/maps
        System.out.println("Flight data:");
        final GraphAdjList airportGraph = new GraphAdjList();
        GraphLoader.loadRoutes(PathsToTheData.ROUTES_UA, airportGraph);
        System.out.println(airportGraph);
        System.out.println("Observe most degrees are small (1-30), eight are over 100.");
        System.out.println("****");


    }

}