package roadgraph;

import geography.GeographicPoint;
import org.junit.Before;
import org.junit.Test;
import util.GraphLoader;
import util.PathsToTheData;

import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * @author (created on 10/4/2017).
 */
public class MapGraphTest {

    MapGraph mapGraph;

    @Before
    public void tests() {
        mapGraph = new MapGraph();
    }

    @Test
    public void getNumEdges0() throws Exception {
        assertTrue(mapGraph.getNumEdges() == 0);
    }

    @Test
    public void getNumEdges2() throws Exception {
        int numEdges;
        GeographicPoint geographicPoint1_1 = new GeographicPoint(0.0, 0.0);
        GeographicPoint geographicPoint1_2 = new GeographicPoint(1.0, 1.0);
        GeographicPoint geographicPoint2_1 = new GeographicPoint(2.0, 2.0);
        GeographicPoint geographicPoint2_2 = new GeographicPoint(3.0, 3.0);

        mapGraph.addVertex(geographicPoint1_1);
        mapGraph.addVertex(geographicPoint1_2);
        mapGraph.addEdge(geographicPoint1_1, geographicPoint1_2, "roadName", "unclassified", 100);

        //assertion one
        numEdges = mapGraph.getNumEdges();
        assertTrue(numEdges == 1);

        //assertion two
        mapGraph.addVertex(geographicPoint2_1);
        mapGraph.addVertex(geographicPoint2_2);

        numEdges = mapGraph.getNumEdges();
        assertTrue(numEdges == 1);

        //assertion three
        mapGraph.addEdge(geographicPoint2_1, geographicPoint2_2, "roadName1", "unclassified", 100);
        numEdges = mapGraph.getNumEdges();
        assertTrue(numEdges == 2);

    }

    @Test
    public void testMain() {
        /*
        System.out.print("Making a new map...");
		MapGraph theMap = new MapGraph();
		System.out.print("DONE. \nLoading the map...");
		GraphLoader.loadRoadMap("data/testdata/simpletest.map", theMap);
		System.out.println("DONE.");
        */

        //System.out.println("Num nodes: " + theMap.getNumVertices());
        //System.out.println("Num edges: " + theMap.getNumEdges());
        //List<GeographicPoint> route = theMap.dijkstra(new GeographicPoint(1.0,1.0), new GeographicPoint(8.0,-1.0));

        //for (GeographicPoint gp: route) {
        //	System.out.println(gp.toString());
        //}


        //-----------------------------------------------------
        //basic map
        //Use this code in Week 3 End of Week Quiz
        /*

		MapGraph theMap = new MapGraph();
		System.out.print("DONE. \nLoading the map...");
		GraphLoader.loadRoadMap("data/maps/utc.map", theMap);
		System.out.println("DONE.");

		GeographicPoint start = new GeographicPoint(32.8648772, -117.2254046);
		GeographicPoint end = new GeographicPoint(32.8660691, -117.217393);

		List<GeographicPoint> route_1 = theMap.bfs(start,end);
		List<GeographicPoint> route_2 = theMap.dijkstra(start,end);
		List<GeographicPoint> route_3 = theMap.aStarSearch(start,end);
		List<GeographicPoint> route_4 = theMap.dijkstraBySpeed(start,end);
		List<GeographicPoint> route_5 = theMap.aStarSearchByTime(start,end);

		System.out.println(
				"Using BFS algorithm to find SHORTEST path" + "\t" + route_1.size() + "\n" +
				"Using Dijkstra algorithm to find SHORTEST path" + "\t" + route_2.size() + "\n" +
				"Using A-star algorithm to find SHORTEST path" + "\t" + route_3.size() + "\n" +
				"Using Dijkstra algorithm to find FASTER path" + "\t" + route_4.size() + "\n"
				+
				"Using A-star algorithm to find FASTER path" + "\t" + route_5.size()
				);

		*/

        //-----------------------------------------------------
        //another map

        System.out.print("Making a new map...");
        MapGraph mapOfMyDistrict = new MapGraph();
        System.out.print("DONE. \nLoading the map...");
        GraphLoader.loadRoadMap(PathsToTheData.MY_BIG_MAP, mapOfMyDistrict);
        System.out.println("DONE.");

        GeographicPoint startMy = new GeographicPoint(59.9305655, 30.4824903);
        GeographicPoint endMy = new GeographicPoint(59.8980511, 30.4444886);

        List<GeographicPoint> My_route_1 = mapOfMyDistrict.bfs(startMy, endMy);
        List<GeographicPoint> My_route_2 = mapOfMyDistrict.dijkstra(startMy, endMy);
        List<GeographicPoint> My_route_3 = mapOfMyDistrict.aStarSearch(startMy, endMy);
        List<GeographicPoint> My_route_4 = mapOfMyDistrict.dijkstraByTime(startMy, endMy);
        List<GeographicPoint> My_route_5 = mapOfMyDistrict.aStarSearchByTime(startMy, endMy);

        System.out.println(
                "Using BFS algorithm to find SHORTEST path" + "\t" + My_route_1.size() + "\n"
                        +
                        "Using Dijkstra algorithm to find SHORTEST path" + "\t" + My_route_2.size() + "\n"
                        +
                        "Using A-star algorithm to find SHORTEST path" + "\t" + My_route_3.size() + "\n"
                        +
                        "Using Dijkstra algorithm to find FASTER path" + "\t" + My_route_4.size() + "\n"
                        +
                        "Using A-star algorithm to find FASTER path" + "\t" + My_route_5.size()
        );
    }

}