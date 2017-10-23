package application.controllers;

import application.MapApp;
import application.business.MarkerManager;
import application.business.SelectManager;
import application.entities.CLabel;
import application.services.RouteService;
import geography.GeographicPoint;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.AlgorithmsTypes;

import java.util.List;

public class RouteController {

    private final static Logger LOGGER = LoggerFactory.getLogger(RouteController.class);

    // Strings for slider labels
    public static final int DIJ = 1;
    public static final int A_STAR = 2;
    public static final int BFS = 3;

    //my version
    public static final int D_time = 4;

    private int selectedToggle = DIJ;

    private RouteService routeService;
    private Button displayButton;
    private Button hideButton;
    private Button startButton;
    private Button resetButton;
    private Button destinationButton;
    private Button visualizationButton;

    private ToggleGroup group;
    private CLabel<geography.GeographicPoint> startLabel;
    private CLabel<geography.GeographicPoint> endLabel;
    private CLabel<geography.GeographicPoint> pointLabel;
    private SelectManager selectManager;
    private MarkerManager markerManager;

    private static boolean visualisation_enabled = false;

    public RouteController(final RouteService routeService, final Button displayButton, final Button hideButton,
                           final Button resetButton, final Button startButton, final Button destinationButton,
                           final ToggleGroup group, List<RadioButton> searchOptions, final Button visualizationButton,
                           final CLabel<GeographicPoint> startLabel, final CLabel<GeographicPoint> endLabel,
                           final CLabel<GeographicPoint> pointLabel, final SelectManager manager, MarkerManager markerManager,
                           final boolean vis_enabled) {
        // save parameters
        this.routeService = routeService;
        this.displayButton = displayButton;
        this.hideButton = hideButton;
        this.startButton = startButton;
        this.resetButton = resetButton;
        this.destinationButton = destinationButton;
        this.group = group;
        this.visualizationButton = visualizationButton;

        // maybe don't need references to labels;
        this.startLabel = startLabel;
        this.endLabel = endLabel;
        this.pointLabel = pointLabel;
        this.selectManager = manager;
        this.markerManager = markerManager;
        visualisation_enabled = vis_enabled;

        setupDisplayButtons();
        setupRouteButtons();
        setupVisualizationButton();
        setupToggle();
    }


    private void setupDisplayButtons() {
        displayButton.setOnAction(e -> {
            if (startLabel.getItem() != null && endLabel.getItem() != null) {
                routeService.displayRoute(startLabel.getItem(), endLabel.getItem(), selectedToggle, visualisation_enabled);
            } else {
                MapApp.showErrorAlert("Route Display Error", "Make sure to choose points for both start and destination.");
            }
        });
        hideButton.setOnAction(e -> routeService.hideRoute());
        resetButton.setOnAction(e -> routeService.reset());
    }

    private void setupVisualizationButton() {
        if (visualisation_enabled) {
            visualizationButton.setOnAction(e -> markerManager.startVisualization());
        }
    }

    private void setupRouteButtons() {
        startButton.setOnAction(e -> selectManager.setStart());
        destinationButton.setOnAction(e -> selectManager.setDestination());
    }

    private void setupToggle() {
        group.selectedToggleProperty().addListener(li -> {
            if (group.getSelectedToggle().getUserData().equals(AlgorithmsTypes.DIJKSTRA)) {
                selectedToggle = DIJ;
            } else if (group.getSelectedToggle().getUserData().equals(AlgorithmsTypes.A_STAR)) {
                selectedToggle = A_STAR;
            } else if (group.getSelectedToggle().getUserData().equals(AlgorithmsTypes.BFS)) {
                selectedToggle = BFS;
                //my version
            } else if (group.getSelectedToggle().getUserData().equals(AlgorithmsTypes.D_TIME)) {
                selectedToggle = D_time;
            } else {
                LOGGER.error("Invalid radio button selection");
            }
        });
    }


}
