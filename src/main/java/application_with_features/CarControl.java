package application_with_features;/**
 * @author (created on 10/20/2017).
 */

import application_with_features.controllers.GmapfxController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Here is the main application
 */
public class CarControl extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setTitle("Car control panel");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/base_form.fxml"));
        // set google maps controller
        final GmapfxController gmapfxController = new GmapfxController();
        loader.setController(gmapfxController);
        final Parent parent = loader.load();
        final Scene scene = new Scene(parent);
        primaryStage.setScene(scene);
        primaryStage.show();
        gmapfxController.createSimpleMap();
    }

}
