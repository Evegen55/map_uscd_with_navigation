/**
 * Class to manage Markers on the Map
 *
 * @author UCSD MOOC development team
 */

package application.business;


import application.entities.DataSet;
import com.lynden.gmapsfx.javascript.event.UIEventType;
import com.lynden.gmapsfx.javascript.object.GoogleMap;
import com.lynden.gmapsfx.javascript.object.LatLong;
import com.lynden.gmapsfx.javascript.object.LatLongBounds;
import com.lynden.gmapsfx.javascript.object.Marker;
import com.lynden.gmapsfx.javascript.object.MarkerOptions;
import javafx.scene.control.Button;
import netscape.javascript.JSObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;


public class MarkerManager {

    private final static Logger LOGGER = LoggerFactory.getLogger(MarkerManager.class);

    private static final double DEFAULT_Z = 2;
    private static final double SELECT_Z = 1;
    private static final double STRTDEST_Z = 3;

    private HashMap<geography.GeographicPoint, Marker> markerMap;
    private ArrayList<geography.GeographicPoint> markerPositions;
    private GoogleMap map;
    public static String startURL = "http://maps.google.com/mapfiles/kml/pal3/icon40.png";
    public static String destinationURL = "http://maps.google.com/mapfiles/kml/pal2/icon5.png";
    private static String SELECTED_URL = "http://maps.google.com/mapfiles/kml/paddle/ltblu-circle.png";
    private static String markerURL = "http://maps.google.com/mapfiles/kml/paddle/blu-diamond-lv.png";
    protected static String visURL = "http://maps.google.com/mapfiles/kml/paddle/red-diamond-lv.png";
    private Marker startMarker;
    private Marker destinationMarker;
    private Marker selectedMarker;
    private DataSet dataSet;
    private LatLongBounds bounds;
    private SelectManager selectManager;
    private RouteVisualization rv;
    private Button vButton;
    private boolean selectMode = true;

    public MarkerManager() {
        markerMap = new HashMap<>();
        this.map = null;
        this.selectManager = null;
        this.rv = null;
        markerPositions = null;
    }

    /**
     * Used to set reference to visualization button. Manager will be responsible
     * for disabling button
     *
     * @param vButton
     */
    public void setVisButton(Button vButton) {
        this.vButton = vButton;
    }

    public void setSelect(boolean value) {
        selectMode = value;
    }

    public RouteVisualization getVisualization() {
        return rv;
    }


    public GoogleMap getMap() {
        return this.map;
    }

    public void setMap(GoogleMap map) {
        this.map = map;
    }

    public void setSelectManager(SelectManager selectManager) {
        this.selectManager = selectManager;
    }

    public void putMarker(geography.GeographicPoint key, Marker value) {
        markerMap.put(key, value);

    }

    /**
     * Used to initialize new RouteVisualization object
     */
    public void initVisualization() {
        rv = new RouteVisualization(this);
    }

    public void clearVisualization() {
        rv.clearMarkers();
        rv = null;
    }

    // TODO -- protect against this being called without visualization built
    public void startVisualization() {
        if (rv != null) {
            rv.startVisualization();
        }
    }

    public void setStart(geography.GeographicPoint point) {
        if (startMarker != null) {
            changeIcon(startMarker, markerURL);
//            startMarker.setZIndex(DEFAULT_Z);
        }
        startMarker = markerMap.get(point);
//        startMarker.setZIndex(STRTDEST_Z);
        changeIcon(startMarker, startURL);
    }

    public void setDestination(geography.GeographicPoint point) {
        if (destinationMarker != null) {
            changeIcon(destinationMarker, markerURL);
//            destinationMarker.setZIndex(DEFAULT_Z);
        }
        destinationMarker = markerMap.get(point);
//        destinationMarker.setZIndex(STRTDEST_Z);
        changeIcon(destinationMarker, destinationURL);
    }

    public void changeIcon(Marker marker, String url) {
        marker.setVisible(false);
        final MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.icon(url);
        marker.setOptions(markerOptions);
        marker.setVisible(true);
    }

    /**
     * TODO -- Might need to create all new markers and add them??
     */
    public void restoreMarkers() {
        LOGGER.info("Start restoring markers");
        markerMap.keySet().forEach(geographicPoint -> {
            final Marker marker = markerMap.get(geographicPoint);
            // destination marker needs to be added because it is added in javascript
            if (marker != startMarker) {
                marker.setVisible(false);
                marker.setVisible(true);
            }
        });
        selectManager.resetSelect();
        LOGGER.info("End restoring markers");
    }

    public void refreshMarkers() {
        // TODO: 10/5/2017 Java8 style
        Iterator<geography.GeographicPoint> it = markerMap.keySet().iterator();
        while (it.hasNext()) {
            Marker marker = markerMap.get(it.next());
            marker.setVisible(true);
        }
    }

    public void clearMarkers() {
        LOGGER.info("Start clear markers");
        if (rv != null) {
            rv.clearMarkers();
            rv = null;
        }
        markerMap.keySet()
                .forEach(geographicPoint -> markerMap.get(geographicPoint).setVisible(false));

        LOGGER.info("End clear markers");
    }

    public void setSelectMode(boolean value) {
        if (!value) {
            selectManager.clearSelected();
        }
        selectMode = value;
    }

    public boolean getSelectMode() {
        return selectMode;
    }

    static MarkerOptions createDefaultOptions(final LatLong coord) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.animation(null)
                .icon(markerURL)
                .position(coord)
                .title(null)
                .visible(true);
        return markerOptions;
    }

    /**
     *
     */
    public void hideIntermediateMarkers() {
        LOGGER.info("Start hide intermediate markers");
        markerMap.keySet().forEach(geographicPoint -> {
            final Marker marker = markerMap.get(geographicPoint);
            // destination marker needs to be added because it is added in javascript
            if (marker != startMarker && marker != destinationMarker) {
                marker.setVisible(false);
            }
        });
        LOGGER.info("End hide intermediate markers");
    }

    public void hideDestinationMarker() {
        destinationMarker.setVisible(false);
    }

    public void displayMarker(geography.GeographicPoint point) {
        if (markerMap.containsKey(point)) {
            Marker marker = markerMap.get(point);
            marker.setVisible(true);
            // System.out.println("Marker : " + marker + "set to visible");
        } else {
            // System.out.println("no key found for MarkerManager::displayMarker");
        }
    }

    void displayDataSet() {
        LOGGER.info("Start of display Intersections");
        markerPositions = new ArrayList<>();
        dataSet.initializeGraph();
        bounds = new LatLongBounds();

        //it is slow just because it invokes javaScript function.
        dataSet.getIntersections().stream()
                .forEach(geographicPoint -> {
                    LatLong latLong = new LatLong(geographicPoint.getX(), geographicPoint.getY());
                    MarkerOptions markerOptions = createDefaultOptions(latLong);
                    bounds.extend(latLong);
                    Marker marker = new Marker(markerOptions);
                    registerEvents(marker, geographicPoint);
                    map.addMarker(marker);
                    putMarker(geographicPoint, marker);
                    markerPositions.add(geographicPoint);
//            marker.setZIndex(DEFAULT_Z);
                });

        map.fitBounds(bounds);
        LOGGER.info("End of display Intersections");

    }


    private void registerEvents(final Marker marker, final geography.GeographicPoint point) {
//        map.addUIEventHandler(marker, UIEventType.mouseover, (JSObject o) -> {
//           marker.setVisible(true);
//           //marker.setAnimation(Animation.BOUNCE);
//        });
//
//        map.addUIEventHandler(marker, UIEventType.mouseout, (JSObject o) -> {
//        	marker.setAnimation(null);
//        });

        map.addUIEventHandler(marker, UIEventType.click, (JSObject o) -> {
            LOGGER.info("Clicked Marker : " + point.toString());
            if (selectMode) {
                if (selectedMarker != null && selectedMarker != startMarker
                        && selectedMarker != destinationMarker) {
                    changeIcon(selectedMarker, markerURL);
//                		selectedMarker.setZIndex(DEFAULT_Z);
                }
                selectManager.setPoint(point, marker);
                selectedMarker = marker;
                changeIcon(selectedMarker, SELECTED_URL);
//                selectedMarker.setZIndex(SELECT_Z);

                // re add markers to map
                // slightly glitchy
//                refreshMarkers();
            }
        });
    }

    public void disableVisButton(boolean value) {
        if (vButton != null) {
            vButton.setDisable(value);
        }
    }

    public void setDataSet(DataSet dataSet) {
        this.dataSet = dataSet;
    }


    public DataSet getDataSet() {
        return this.dataSet;
    }
}
