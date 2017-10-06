package application.services;

import application.DataSet;
import application.MapApp;
import application.MarkerManager;
import application.SelectManager;
import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.javascript.object.GoogleMap;
import com.lynden.gmapsfx.javascript.object.LatLong;
import com.lynden.gmapsfx.javascript.object.LatLongBounds;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import mapmaker.MapMaker;
import util.PathsToTheData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;
import java.util.regex.Pattern;


/**
 * class for map and general application services (file IO, etc.)
 */
public class GeneralService {

    private final static Logger LOGGER = Logger.getLogger(GeneralService.class.getName());
    private static final String DATA_FILE_PATTERN = "[\\w_]+.map";
    private static final ExecutorService EXECUTOR_SERVICE = Executors.newCachedThreadPool();

    //	private static boolean singleton = false;
    private int currentState;
    private SelectManager selectManager;
    private GoogleMap map;
    private MarkerManager markerManager;
    private List<String> filenames;

    public GeneralService(final GoogleMapView mapComponent, final SelectManager selectManager,
                          final MarkerManager markerManager) {
        // get map from GoogleMapView
        this.map = mapComponent.getMap();
        this.selectManager = selectManager;
        this.markerManager = markerManager;
        this.markerManager.setMap(map);
        filenames = new ArrayList<>();

        // uncomment to click map and print coordinates WHAT??
        /*mapComponent.addUIEventHandler(UIEventType.click, (JSObject obj) -> {
            System.out.println(obj.getMember("latLng"));
    	});*/
    }

    /**
     * writes geographic data flat file
     *
     * @param filename
     * @param arr      contains the coordinates of the bounds for the map region
     * @return
     */
    public boolean writeDataToFile(final String filename, final float[] arr) {
        LOGGER.info("Start write geographic data flat file");
        final MapMaker mm = new MapMaker(arr);
        // parse data and write to filename
        return mm.parseData(filename);
    }

    public static String getDataSetDirectory() {
        return PathsToTheData.DATA_FILE_DIR_STR;
    }

    /**
     * gets current bounds of map view
     *
     * @return
     */
    public float[] getBoundsArray() {
        final LatLongBounds bounds = map.getBounds();
        LatLong sw = bounds.getSouthWest();
        LatLong ne = bounds.getNorthEast();

        // [S, W, N, E]
        return new float[]{(float) sw.getLatitude(), (float) sw.getLongitude(),
                (float) ne.getLatitude(), (float) ne.getLongitude()};
    }

    public void addDataFile(String filename) {
        filenames.add(filename);
    }

    public void displayIntersections(DataSet dataset) {
        // remove old data set markers
        if (markerManager == null) {
            LOGGER.warning("failure!");
        }
        if (markerManager.getDataSet() != null) {
            markerManager.clearMarkers();
            markerManager.getDataSet().setDisplayed(false);
        }

        // display new data set
        selectManager.setAndDisplayData(dataset);
        dataset.setDisplayed(true);
    }

    public float boundsSize() {
        float[] bounds = getBoundsArray();
        return (bounds[2] - bounds[0]) * (bounds[3] - bounds[1]);
    }

    public boolean checkBoundsSize(double limit) {
        if (boundsSize() > limit) {
            return false;
        }
        return true;
    }

    /**
     * Check if file name matches pattern [filename].map
     *
     * @param str - path to check
     * @return string to use as path
     */
    public String checkDataFileName(String str) {
        if (Pattern.matches(DATA_FILE_PATTERN, str)) {
            return PathsToTheData.DATA_FILE_DIR_STR + str;
        }
        return null;
    }

    // TODO: 10/5/2017 put the map file into ADT in-memory continuously through scrolling
    // TODO: 10/5/2017 unload data on-premises by asking a user when program has to be terminated
    public void runFetchTask(final String fileName, final ComboBox<DataSet> dataSetComboBox, final Button button) {
        float[] arr = getBoundsArray();
        final Task<String> task = formTaskToFetchData(fileName, dataSetComboBox, button, arr);
        EXECUTOR_SERVICE.submit(task);
    }

    private Task<String> formTaskToFetchData(final String fileName, final ComboBox<DataSet> dataSetComboBox,
                                             final Button button, final float... arr) {
        Task<String> task = new Task<String>() {
            @Override
            public String call() {
                if (writeDataToFile(fileName, arr)) {
                    return fileName;
                }

                return "z" + fileName;

            }
        };

        Alert fetchingAlert = MapApp.getInfoAlert("Loading : ", "Fetching data for current map area...");
        task.setOnSucceeded(e -> {
            if (task.getValue().equals(fileName)) {
                addDataFile(fileName);

                dataSetComboBox.getItems().add(new DataSet(fileName));
                if (fetchingAlert.isShowing()) {
                    fetchingAlert.close();
                }
                MapApp.showInfoAlert("Fetch completed : ", "Data set : \"" + fileName + "\" written to file!");
                LOGGER.info("Fetch Task Succeeded");

            } else {
                LOGGER.warning("Something went wrong, data not written to file : Task succeeded but fileName returned differently");

            }
            button.setDisable(false);

        });


        task.setOnFailed(e -> {
        });

        task.setOnRunning(e -> {
            button.setDisable(true);
            fetchingAlert.showAndWait();
        });
        return task;
    }


    public List<String> getDataFiles() {
        return filenames;
    }

    public static String getFileRegex() {
        return GeneralService.DATA_FILE_PATTERN;
    }


    public void setState(int state) {
        currentState = state;
    }


    public double getState() {
        return currentState;
    }


}

