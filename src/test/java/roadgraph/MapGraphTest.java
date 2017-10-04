package roadgraph;

import geography.GeographicPoint;
import org.junit.Before;
import org.junit.Test;

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

}