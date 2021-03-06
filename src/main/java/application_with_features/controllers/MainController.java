package application_with_features.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;

/**
 * @author (created on 10/23/2017).
 */
public class MainController {

    @FXML
    private AnchorPane myPaneWithMapsAndOtherFeatures;

    @FXML
    private AnchorPane myPaneWithControls;

    @FXML
    private Slider gen_temperature_slider;

    public void showMap(){
        final GmapfxController gmapfxController = new GmapfxController();
        gmapfxController.createSimpleMap(myPaneWithMapsAndOtherFeatures);
    }

    public void showControlsInit() {
        final ClimateControlController climateControlController = new ClimateControlController(gen_temperature_slider);
        climateControlController.createInitForControls(myPaneWithControls);
    }
}
