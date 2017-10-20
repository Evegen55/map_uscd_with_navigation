package application_with_features;/**
 * @author (created on 10/20/2017).
 */

import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.MapComponentInitializedListener;
import com.lynden.gmapsfx.javascript.object.GoogleMap;
import com.lynden.gmapsfx.javascript.object.LatLong;
import com.lynden.gmapsfx.javascript.object.MapOptions;
import com.lynden.gmapsfx.javascript.object.MapTypeIdEnum;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class CarControl extends Application implements MapComponentInitializedListener {

    public static void main(String[] args) {
        launch(args);
    }

    protected GoogleMapView mapComponent;
    protected GoogleMap map;

    @FXML
    private AnchorPane myPaneWithMapsAndOtherFeatures;

    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setTitle("Car control panel");

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(CarControl.class.getClassLoader().getResource("fxml/base_form.fxml"));
        // set this instance as its controller
        loader.setController(this);
        Parent parent = loader.load();
        final Scene scene = new Scene(parent);
        primaryStage.setScene(scene);
        primaryStage.show();

        //generates google map with some defaults and put it into top pane
        mapComponent = new GoogleMapView();
        mapComponent.addMapInializedListener(this);
        myPaneWithMapsAndOtherFeatures.getChildren().add(mapComponent);

    }

    @Override
    public void mapInitialized() {
        LatLong center = new LatLong(34.0219, -118.4814);

        MapOptions options = new MapOptions();
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
                .zoom(8)
                .zoomControl(true);

        map = mapComponent.createMap(options);
    }
}
