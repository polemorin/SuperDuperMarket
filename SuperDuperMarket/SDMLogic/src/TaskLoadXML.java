import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;

import java.io.File;

public class TaskLoadXML extends Task<Boolean> {
    SuperDuperMarket sdm;
    File selectedFile;
    SimpleBooleanProperty xmlLoaded;
    public TaskLoadXML(SuperDuperMarket sdm, File file, SimpleBooleanProperty isXMLLoaded){
        this.sdm = sdm;
        selectedFile = file;
        updateProgress(0,1);
        xmlLoaded = isXMLLoaded;
    }
    @Override
    protected Boolean call() throws Exception {
        try {
            sdm.loadXmlFileFromFileChooser(selectedFile);
        } catch (Exception e) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Invalid XML file.");
                    alert.setHeaderText("File was not loaded.");
                    alert.setContentText(e.getMessage());
                    alert.showAndWait();
                }
            });

            return false;
        }
            updateMessage("Reading XML file");
            updateProgress(0.3, 1);
            Thread.sleep(200);
            updateMessage("Loading store data");
            updateProgress(0.6, 1);
            Thread.sleep(200);
            updateMessage("Loading products");
            updateProgress(0.9, 1);
            updateMessage("Loading Users");
            Thread.sleep(200);
            updateProgress(1, 1);
            updateMessage("Done!");
            xmlLoaded.setValue(true);
            return true;

    }
}
