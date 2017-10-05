/**
 * Class to manage items selected in the GUI
 *
 * @author UCSD MOOC development team
 */

package application;

import geography.GeographicPoint;
import gmapsfx.javascript.object.Marker;

import java.util.logging.Logger;

public class SelectManager {

    private final static Logger LOGGER = Logger.getLogger(SelectManager.class.getName());

    private CLabel<GeographicPoint> pointLabel;
    private CLabel<GeographicPoint> startLabel;
    private CLabel<GeographicPoint> destinationLabel;
    private Marker startMarker;
    private Marker destinationMarker;
    private Marker selectedMarker;
    private MarkerManager markerManager;
    private DataSet dataSet;


    public SelectManager() {
        startMarker = null;
        destinationMarker = null;
        selectedMarker = null;
        pointLabel = null;
        startLabel = null;
        destinationLabel = null;
        dataSet = null;
    }


    public void resetSelect() {
        markerManager.setSelectMode(true);
    }

    public void clearSelected() {
        selectedMarker = null;
        pointLabel.setItem(null);
    }

    public void setAndDisplayData(final DataSet data) {
        setDataSet(data);
        if (markerManager != null) {
            markerManager.displayDataSet();
        } else {
            LOGGER.warning("Error : Marker Manager is null.");
        }
    }

    public void setMarkerManager(final MarkerManager manager) {
        this.markerManager = manager;
    }

    public void setPoint(final GeographicPoint point, final Marker marker) {
        // System.out.println("inSetPoint.. passed : " + point);
        pointLabel.setItem(point);
        selectedMarker = marker;
    }

    public void setDataSet(final DataSet dataSet) {
        this.dataSet = dataSet;
        if (markerManager != null) {
            markerManager.setDataSet(dataSet);
        }
    }

    public void setPointLabel(CLabel<GeographicPoint> label) {
        this.pointLabel = label;
    }

    public void setStartLabel(CLabel<GeographicPoint> label) {
        this.startLabel = label;
    }

    public void setDestinationLabel(CLabel<GeographicPoint> label) {
        this.destinationLabel = label;
    }

    public GeographicPoint getPoint() {
        return pointLabel.getItem();
    }


    public GeographicPoint getStart() {
        return startLabel.getItem();
    }

    public GeographicPoint getDestination() {
        return destinationLabel.getItem();
    }

    public void setStart() {
        if (pointLabel.getItem() != null) {
            GeographicPoint point = pointLabel.getItem();
            startLabel.setItem(point);
            markerManager.setStart(point);
        }
    }

    public void setDestination() {
        if (pointLabel.getItem() != null) {
            GeographicPoint point = pointLabel.getItem();
            destinationLabel.setItem(point);
            markerManager.setDestination(point);
        }
    }


}