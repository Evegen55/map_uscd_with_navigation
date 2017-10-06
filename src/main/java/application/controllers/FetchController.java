package application.controllers;

import application.DataSet;
import application.services.GeneralService;
import application.services.RouteService;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import util.PathsToTheData;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Logger;

public class FetchController {

    private final static Logger LOGGER = Logger.getLogger(FetchController.class.getName());

    private static final int ROW_COUNT = 5;
    private GeneralService generalService;
    private RouteService routeService;
    private Node container;
    private Button fetchButton;
    private Button displayButton;
    private ComboBox<DataSet> dataChoices;
    // maybe choice map
    private TextField writeFile;

    //it depend on your hardware
    // TODO: 10/5/2017 calculate it in an appropriate style
    private static final double LIMIT_TOTAL_ERROR = 0.5;
    private static final double LIMIT_WARNING_ERROR = 0.02;

    public FetchController(final GeneralService generalService, final RouteService routeService, final TextField writeFile,
                           final Button fetchButton, ComboBox<DataSet> dataSetComboBox, final Button displayButton, Stage primaryStage) {
        this.generalService = generalService;
        this.routeService = routeService;
        this.fetchButton = fetchButton;
        this.displayButton = displayButton;
        this.writeFile = writeFile;
        this.dataChoices = dataSetComboBox;
        setupComboCells();
        setupFetchButton();
        setupDisplayButton();
        loadDataSets(primaryStage);

    }

    private void loadDataSets(Stage primaryStage) {
        try {
            readFileWithListOfMaps(PathsToTheData.PERSIST_PATH);
        } catch (IOException e) {
            LOGGER.warning("No existing map files found.");
            final FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Find a file with extension .list");
            final File openDialogFile = fileChooser.showOpenDialog(primaryStage);
            if (openDialogFile != null) {
                final String openDialogFilePath = openDialogFile.getPath();
                System.out.println(openDialogFilePath);
                try {
                    readFileWithListOfMaps(openDialogFilePath);
                } catch (IOException e1) {
                    LOGGER.warning("First exception");
                    e.printStackTrace();
                    LOGGER.warning("Second exception");
                    e1.printStackTrace();
                }
            }
        }
    }

    private void readFileWithListOfMaps(final String fileName) throws IOException {
        Files.lines(Paths.get(fileName)).forEach(line -> {
            LOGGER.fine(line);
            final String path = GeneralService.getDataSetDirectory() + line;
            LOGGER.fine(path);
            dataChoices.getItems().add(new DataSet(path));
        });
    }

    private void setupComboCells() {
        //dataChoices.setVisibleRowCount(ROW_COUNT);
        dataChoices.setCellFactory(new Callback<ListView<DataSet>, ListCell<DataSet>>() {
            @Override
            public ListCell<DataSet> call(ListView<DataSet> p) {
                return new ListCell<DataSet>() {
                    {
                        super.setPrefWidth(100);
                        //getItem().getFileName());

                    }

                    @Override
                    protected void updateItem(DataSet item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            super.setText("None.");
                        } else {
                            super.setText(item.getFilePath().substring(GeneralService.getDataSetDirectory().length()));

                        }
                    }
                };

            }
        });

        dataChoices.setButtonCell(new ListCell<DataSet>() {
            @Override
            protected void updateItem(DataSet t, boolean bln) {
                super.updateItem(t, bln);
                if (t != null) {
                    setText(t.getFilePath().substring(GeneralService.getDataSetDirectory().length()));
                } else {
                    setText("Choose...");
                }
            }
        });
    }

    /**
     * Registers event to fetch data
     */
    private void setupFetchButton() {
        fetchButton.setOnAction(e -> {
            String fName = writeFile.getText();

            // check for valid file name ___.map or mapfiles/___.map
            if ((generalService.checkDataFileName(fName)) != null) {
                if (!generalService.checkBoundsSize(LIMIT_TOTAL_ERROR)) {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Size Error");
                    alert.setHeaderText("Map Size Error");
                    alert.setContentText("Map boundaries are too large.");
                    alert.showAndWait();
                } else if (!generalService.checkBoundsSize(LIMIT_WARNING_ERROR)) {
                    Alert warning = new Alert(AlertType.CONFIRMATION);
                    warning.setTitle("Size Warning");
                    warning.setHeaderText("Map Size Warning");
                    warning.setContentText("Your map file may take a long time to download," +
                            "\nand your computer may crash when you try to" +
                            "\nload the intersections. Continue?");
                    warning.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK) {
                            generalService.runFetchTask(generalService.checkDataFileName(fName), dataChoices, fetchButton);
                        }
                    });
                } else {
                    generalService.runFetchTask(generalService.checkDataFileName(fName), dataChoices, fetchButton);
                }


            } else {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Filename Error");
                alert.setHeaderText("Input Error");
                alert.setContentText("Check filename input. \n\n\n"
                        + "Filename must match format : [filename].map."
                        + "\n\nUse only uppercase and lowercase letters,\nnumbers, and underscores in [filename].");

                alert.showAndWait();
            }
        });
    }

    /**
     * Registers event to fetch data
     */
    private void setupDisplayButton() {
        displayButton.setOnAction(e -> {
            LOGGER.info("In setup display button");
            final DataSet dataSet = dataChoices.getValue();

            // was any dataset selected?
            if (dataSet == null) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Display Error");
                alert.setHeaderText("Invalid Action :");
                alert.setContentText("No map file has been selected for display.");
                alert.showAndWait();
            } else if (!dataSet.isDisplayed()) {
                // TODO -- only time I need route service ....redo?
                if (routeService.isRouteDisplayed()) {
                    routeService.hideRoute();
                }
                generalService.displayIntersections(dataSet);

            } else {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Display Info");
                alert.setHeaderText("Intersections Already Displayed");
                alert.setContentText("Data set : " + dataSet.getFilePath() + " has already been loaded.");
                alert.showAndWait();
            }

            // TO TEST : only using test.map for intersections
            //generalService.displayIntersections(new DataSet("my.map"));
        });
    }


}
