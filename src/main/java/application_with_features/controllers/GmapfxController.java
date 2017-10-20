package application_with_features.controllers;

import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.MapComponentInitializedListener;
import com.lynden.gmapsfx.javascript.object.GoogleMap;
import com.lynden.gmapsfx.javascript.object.LatLong;
import com.lynden.gmapsfx.javascript.object.MapOptions;
import com.lynden.gmapsfx.javascript.object.MapTypeIdEnum;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

/**
 * @author (created on 10/20/2017).
 */
public class GmapfxController  implements MapComponentInitializedListener {


    @FXML
    private AnchorPane myPaneWithMapsAndOtherFeatures;

    protected GoogleMapView mapComponent;
    protected GoogleMap map;

    public void createSimpleMap() {
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
