/**
 * Class to aid with route visualization for search
 *
 * @author UCSD MOOC development team
 */

package application;

import gmapsfx.javascript.IJavascriptRuntime;
import gmapsfx.javascript.JavascriptArray;
import gmapsfx.javascript.JavascriptRuntime;
import gmapsfx.javascript.object.LatLong;
import gmapsfx.javascript.object.LatLongBounds;
import gmapsfx.javascript.object.Marker;
import gmapsfx.javascript.object.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * It uses the JavaScript function to show the animation
 */
public class RouteVisualization {

    private final static Logger LOGGER = Logger.getLogger(RouteVisualization.class.getName());

    private List<geography.GeographicPoint> points;
    private ArrayList<Marker> markerList;
    private MarkerManager manager;
    private JavascriptArray markers;
    private IJavascriptRuntime runtime;

    public RouteVisualization(final MarkerManager manager) {
        points = new ArrayList<>();
        markerList = new ArrayList<>();
        this.manager = manager;
    }

    public void acceptPoint(geography.GeographicPoint point) {
        points.add(point);
        LOGGER.fine("accepted point : " + point);
    }

    public void startVisualization() {
        LOGGER.info("Start visualisation");
        LatLongBounds bounds = new LatLongBounds();
        List<LatLong> latLongs = new ArrayList<>();
        JavascriptArray jsArray = new JavascriptArray();
        manager.hideIntermediateMarkers();
        manager.hideDestinationMarker();
//    	manager.disableRouteButtons(true);

        // create JavascriptArray of points
//        for (geography.GeographicPoint point : points) {
//            LatLong ll = new LatLong(point.getX(), point.getY());
//            MarkerOptions options = MarkerManager.createDefaultOptions(ll);
//            Marker newMarker = new Marker(options);
//            jsArray.push(newMarker);
//            markerList.add(newMarker);
//            bounds.extend(ll);
//        }
        points.forEach(geographicPoint -> {
            LatLong ll = new LatLong(geographicPoint.getX(), geographicPoint.getY());
            MarkerOptions options = MarkerManager.createDefaultOptions(ll);
            Marker newMarker = new Marker(options);
            jsArray.push(newMarker);
            markerList.add(newMarker);
            bounds.extend(ll);
        });

        // fit map bounds to visualization
        manager.getMap().fitBounds(bounds);

        // get javascript runtime and execute animation
        runtime = JavascriptRuntime.getInstance();
        String command = runtime.getFunction("visualizeSearch", manager.getMap(), jsArray);
        LOGGER.fine(command);
        runtime.execute(command);
//    	MapApp.showInfoAlert("Nodes visited :"  , latLongs.size() +" nodes were visited in the search");
        manager.disableVisButton(true);
//        manager.disableRouteButtons(false);
    }

    void clearMarkers() {
        markerList.forEach(marker -> marker.setVisible(false));
    }


}
