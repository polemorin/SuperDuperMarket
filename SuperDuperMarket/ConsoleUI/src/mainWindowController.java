import com.sun.javafx.fxml.builder.JavaFXSceneBuilder;
import com.sun.javafx.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;

public class mainWindowController {

    @FXML
    private ComboBox<?> styleOption;

    @FXML
    private CheckBox allowAnimationCheckBox;

    @FXML
    private Button loadXmlFileButton;

    @FXML
    private Button showStoreDetailsButton;

    @FXML
    private Button addNewSaleButton;

    @FXML
    private Button addNewStoreButton;

    @FXML
    private Button addNewProductButton;

    @FXML
    private Button showUserDataButton;

    @FXML
    private Button showOrderHistoryButton;

    @FXML
    private Button showProductDetailsButton;

    @FXML
    private Button updateStoreProductButton;

    @FXML
    private Button placeAnOrderButton;

    @FXML
    private TextField textField;

    @FXML
    void addNewProductButtonAction(ActionEvent event) {
        textField.setText("Hello");
    }

    @FXML
    void addNewSaleButtonAction(ActionEvent event) {

    }

    @FXML
    void addNewStoreButtonAction(ActionEvent event) {

    }

    @FXML
    void allowAnimationCheckBoxChecked(ActionEvent event) {

    }

    @FXML
    void loadXmlFileButtonAction(ActionEvent event) {

    }

    @FXML
    void placeAnOrderButtonAction(ActionEvent event) {

    }

    @FXML
    void showOrderHistoryButtonAction(ActionEvent event) {

    }

    @FXML
    void showProductDetailsButtonAction(ActionEvent event) {

    }

    @FXML
    void showStoreDetailsButtonAction(ActionEvent event) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("showStoreDetails.fxml"));
                /*
                 * if "fx:controller" is not set in fxml
                 * fxmlLoader.setController(NewWindowController);
                 */
                Scene scene = new Scene(fxmlLoader.load(), 600, 400);
                Stage stage = new Stage();
                stage.setTitle("New Window");
                stage.setScene(scene);
                stage.setAlwaysOnTop(true);
                stage.initOwner(showStoreDetailsButton.getScene().getWindow());
                stage.initModality(Modality.WINDOW_MODAL);
                stage.showAndWait();
            } catch (IOException e) {

            }
    }

    @FXML
    void showUserDataButtonAction(ActionEvent event) {

    }

    @FXML
    void styleOptionAction(ActionEvent event) {

    }

    @FXML
    void updateStoreProductButtonAction(ActionEvent event) {

    }

}
