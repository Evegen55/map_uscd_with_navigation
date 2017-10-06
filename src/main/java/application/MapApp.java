/**
 * JavaFX application which interacts with the Google
 * Maps API to provide a mapping interface with which
 * to test and develop graph algorithms and data structures
 *
 * @author UCSD MOOC development team
 */
package application;

import application.controllers.FetchController;
import application.controllers.RouteController;
import application.services.GeneralService;
import application.services.RouteService;
import gmapsfx.GoogleMapView;
import gmapsfx.MapComponentInitializedListener;
import gmapsfx.javascript.object.GoogleMap;
import gmapsfx.javascript.object.LatLong;
import gmapsfx.javascript.object.MapOptions;
import gmapsfx.javascript.object.MapTypeIdEnum;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import util.AlgorithmsTypes;
import util.PathsToTheData;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;


public class MapApp extends Application
        implements MapComponentInitializedListener {

    private final static Logger LOGGER = Logger.getLogger(MapApp.class.getName());

    protected GoogleMapView mapComponent;
    protected GoogleMap map;
    protected BorderPane bp;
    protected Stage primaryStage;

    // CONSTANTS
    private static final double MARGIN_VAL = 10;
    private static final double FETCH_COMPONENT_WIDTH = 160.0;

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Application entry point
     */
    @Override
    public void start(final Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;

        // MAIN CONTAINER
        bp = new BorderPane();

        // set up map
        mapComponent = new GoogleMapView();
        mapComponent.addMapInitializedListener(this);

        // initialize tabs for data fetching and route controls
        final Tab routeTab = new Tab("Routing");

        // create components for fetch tab
        final Button fetchButton = new Button("Fetch Data");
        final Button displayButton = new Button("Show Intersections");
        final TextField tf = new TextField();
        final ComboBox<DataSet> dataSetComboBox = new ComboBox<>();

        // set on mouse pressed, this fixes Windows 10 / Surface bug
        dataSetComboBox.setOnMousePressed(e -> dataSetComboBox.requestFocus());

        final HBox fetchControls = getBottomBox(tf, fetchButton);
        final VBox fetchBox = getFetchBox(displayButton, dataSetComboBox);


        LOGGER.info("create components for fetch tab");
        final Button routeButton = new Button("Show Route");
        final Button hideRouteButton = new Button("Hide Route");
        final Button resetButton = new Button("Reset");
        final Button visualizationButton = new Button("Start Visualization");
        final Image sImage = new Image(MarkerManager.startURL);
        final Image dImage = new Image(MarkerManager.destinationURL);
        LOGGER.info("create empty start and end points");
        final CLabel<geography.GeographicPoint> startLabel = new CLabel<>("Empty.", new ImageView(sImage), null);
        final CLabel<geography.GeographicPoint> endLabel = new CLabel<>("Empty.", new ImageView(dImage), null);
        //TODO -- hot fix
        startLabel.setMinWidth(180);
        endLabel.setMinWidth(180);
//        startLabel.setWrapText(true);
//        endLabel.setWrapText(true);
        final Button startButton = new Button("Start");
        final Button destinationButton = new Button("Dest");

        LOGGER.info("create radio buttons for selecting search algorithm");
        final ToggleGroup group = new ToggleGroup();
        final List<RadioButton> searchOptions = setupToggle(group);
        LOGGER.info("Radio buttons for selecting search algorithm created");


        LOGGER.info(" Select and marker managers for route choosing and marker display/visuals \n" +
                "should only be one instance (singleton)");
        final SelectManager manager = new SelectManager();
        final MarkerManager markerManager = new MarkerManager();
        markerManager.setSelectManager(manager);
        manager.setMarkerManager(markerManager);
        markerManager.setVisButton(visualizationButton);

        LOGGER.info(" create components for route tab");
        final CLabel<geography.GeographicPoint> pointLabel = new CLabel<>("No point Selected.", null);
        manager.setPointLabel(pointLabel);
        manager.setStartLabel(startLabel);
        manager.setDestinationLabel(endLabel);
        setupRouteTab(routeTab, fetchBox, startLabel, endLabel, pointLabel, routeButton, hideRouteButton,
                resetButton, visualizationButton, startButton, destinationButton, searchOptions);

        LOGGER.info("add tabs to pane, give no option to close");
        final TabPane tp = new TabPane(routeTab);
        tp.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        LOGGER.info("Initialize Services and controllers after map is loaded");
        mapComponent.addMapReadyListener(() -> {
            final GeneralService gs = new GeneralService(mapComponent, manager, markerManager);
            final RouteService rs = new RouteService(mapComponent, markerManager);
            LOGGER.info("in map ready : " + this.getClass());
            LOGGER.info("initialize controllers");
            new RouteController(rs, routeButton, hideRouteButton, resetButton, startButton, destinationButton, group, searchOptions, visualizationButton,
                    startLabel, endLabel, pointLabel, manager, markerManager);
            new FetchController(gs, rs, tf, fetchButton, dataSetComboBox, displayButton, primaryStage);
        });

        LOGGER.info("Add components to border pane");
        bp.setRight(tp);
        bp.setBottom(fetchControls);
        bp.setCenter(mapComponent);

        final Scene scene = new Scene(bp);
        scene.getStylesheets().add(PathsToTheData.HTML_ROUTING_CSS);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    @Override
    public void mapInitialized() {
        LOGGER.info("Map initializing");
        final LatLong center = new LatLong(32.8810, -117.2380);
        // set map options
        final MapOptions options = new MapOptions();
        options.center(center)
                .mapMarker(false)
                .mapType(MapTypeIdEnum.ROADMAP)
                //maybe set false
                .mapTypeControl(true)
                .overviewMapControl(false)
                .panControl(true)
                .rotateControl(false)
                .scaleControl(false)
                .streetViewControl(false)
                .zoom(14)
                .zoomControl(true);
        // create map;
        map = mapComponent.createMap(options);
        setupJSAlerts(mapComponent.getWebView());
    }


    // SETTING UP THE VIEW
    private HBox getBottomBox(final TextField tf, final Button fetchButton) {
        final HBox box = new HBox();
        tf.setPrefWidth(FETCH_COMPONENT_WIDTH);
        box.getChildren().add(tf);
        fetchButton.setPrefWidth(FETCH_COMPONENT_WIDTH);
        box.getChildren().add(fetchButton);
        return box;
    }

    /**
     * Setup layout and controls for Fetch tab
     *
     * @param displayButton
     * @param cb
     * @return
     */
    private VBox getFetchBox(final Button displayButton, final ComboBox<DataSet> cb) {
        // add button to tab, rethink design and add V/HBox for content
        final VBox v = new VBox();
        final HBox h = new HBox();

        final HBox intersectionControls = new HBox();
//        cb.setMinWidth(displayButton.getWidth());
        cb.setPrefWidth(FETCH_COMPONENT_WIDTH);
        intersectionControls.getChildren().add(cb);
        displayButton.setPrefWidth(FETCH_COMPONENT_WIDTH);
        intersectionControls.getChildren().add(displayButton);

        h.getChildren().add(v);
        v.getChildren().add(new Label("Choose map file : "));
        v.getChildren().add(intersectionControls);

        //v.setSpacing(MARGIN_VAL);
        return v;
    }

    /**
     * Setup layout of route tab and controls
     *
     * @param routeTab
     * @param fetchBox
     * @param startLabel
     * @param endLabel
     * @param pointLabel
     * @param showButton
     * @param hideButton
     * @param resetButton
     * @param vButton
     * @param startButton
     * @param destButton
     * @param searchOptions
     */
    private void setupRouteTab(final Tab routeTab, final VBox fetchBox, final Label startLabel, final Label endLabel, final Label pointLabel,
                               final Button showButton, final Button hideButton, final Button resetButton, final Button vButton,
                               final Button startButton,
                               final Button destButton, final List<RadioButton> searchOptions) {

        //set up tab layout
        final HBox h = new HBox();
        // v is inner container
        final VBox v = new VBox();
        h.getChildren().add(v);

        final VBox selectLeft = new VBox();
        selectLeft.getChildren().add(startLabel);

        final HBox startBox = new HBox();
        startBox.getChildren().add(startLabel);
        startBox.getChildren().add(startButton);
        startBox.setSpacing(20);

        final HBox destinationBox = new HBox();
        destinationBox.getChildren().add(endLabel);
        destinationBox.getChildren().add(destButton);
        destinationBox.setSpacing(20);

        final VBox markerBox = new VBox();
        final Label markerLabel = new Label("Selected Marker : ");
        markerBox.getChildren().add(markerLabel);
        markerBox.getChildren().add(pointLabel);

        VBox.setMargin(markerLabel, new Insets(MARGIN_VAL, MARGIN_VAL, MARGIN_VAL, MARGIN_VAL));
        VBox.setMargin(pointLabel, new Insets(0, MARGIN_VAL, MARGIN_VAL, MARGIN_VAL));
        VBox.setMargin(fetchBox, new Insets(0, 0, MARGIN_VAL * 2, 0));

        final HBox showHideBox = new HBox();
        showHideBox.getChildren().add(showButton);
        showHideBox.getChildren().add(hideButton);
        showHideBox.setSpacing(2 * MARGIN_VAL);

        v.getChildren().add(fetchBox);
        v.getChildren().add(new Label("Start Position : "));
        v.getChildren().add(startBox);
        v.getChildren().add(new Label("Goal : "));
        v.getChildren().add(destinationBox);
        v.getChildren().add(showHideBox);
        for (RadioButton rb : searchOptions) {
            v.getChildren().add(rb);
        }
        v.getChildren().add(vButton);
        VBox.setMargin(showHideBox, new Insets(MARGIN_VAL, MARGIN_VAL, MARGIN_VAL, MARGIN_VAL));
        VBox.setMargin(vButton, new Insets(MARGIN_VAL, MARGIN_VAL, MARGIN_VAL, MARGIN_VAL));
        vButton.setDisable(true);
        v.getChildren().add(markerBox);
//        v.getChildren().add(resetButton);
        routeTab.setContent(h);
    }

    private void setupJSAlerts(final WebView webView) {
        webView.getEngine().setOnAlert(e -> {
            final Stage popup = new Stage();
            popup.initOwner(primaryStage);
            popup.initStyle(StageStyle.UTILITY);
            popup.initModality(Modality.WINDOW_MODAL);
            final StackPane content = new StackPane();
            content.getChildren().setAll(
                    new Label(e.getData())
            );
            content.setPrefSize(200, 100);
            popup.setScene(new Scene(content));
            popup.showAndWait();
        });
    }

    private LinkedList<RadioButton> setupToggle(final ToggleGroup group) {

        // Use Dijkstra as default
        final RadioButton rbD = new RadioButton(AlgorithmsTypes.DIJKSTRA.toString());
        rbD.setUserData(AlgorithmsTypes.DIJKSTRA);
        rbD.setSelected(true);

        RadioButton rbA = new RadioButton(AlgorithmsTypes.A_STAR.toString());
        rbA.setUserData(AlgorithmsTypes.A_STAR);

        RadioButton rbB = new RadioButton(AlgorithmsTypes.BFS.toString());
        rbB.setUserData(AlgorithmsTypes.BFS);

        //add new buttons with searching methods by the time
        RadioButton rbBD = new RadioButton(AlgorithmsTypes.D_TIME.toString());
        rbBD.setUserData(AlgorithmsTypes.D_TIME);

        rbB.setToggleGroup(group);
        rbD.setToggleGroup(group);
        rbA.setToggleGroup(group);
        //my code
        rbBD.setToggleGroup(group);

        return new LinkedList<>(Arrays.asList(rbB, rbD, rbA, rbBD));
    }


	/*
     * METHODS FOR SHOWING DIALOGS/ALERTS
	 */

    public void showLoadStage(Stage loadStage, String text) {
        loadStage.initModality(Modality.APPLICATION_MODAL);
        loadStage.initOwner(primaryStage);
        VBox loadVBox = new VBox(20);
        loadVBox.setAlignment(Pos.CENTER);
        Text tNode = new Text(text);
        tNode.setFont(new Font(16));
        loadVBox.getChildren().add(new HBox());
        loadVBox.getChildren().add(tNode);
        loadVBox.getChildren().add(new HBox());
        Scene loadScene = new Scene(loadVBox, 300, 200);
        loadStage.setScene(loadScene);
        loadStage.show();
    }

    public static void showInfoAlert(String header, String content) {
        Alert alert = getInfoAlert(header, content);
        alert.showAndWait();
    }

    public static Alert getInfoAlert(String header, String content) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(header);
        alert.setContentText(content);
        return alert;
    }

    public static void showErrorAlert(String header, String content) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("File Name Error");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }


}
