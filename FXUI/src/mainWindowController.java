//package C:\Users\LILACH\Desktop\git-SDM\SuperDuperMarket\FXUI\src\mainWindowController;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class mainWindowController {


    private SimpleBooleanProperty isXmlFileLoaded;
    private Stage primaryStage;
    private SuperDuperMarket SDM;
    @FXML
    private Button orderHistoryButton;

    @FXML
    private Button storeDetailsButton;

    @FXML
    private Button addNewProductButton;

    @FXML
    private Button productDetailsButton;

    @FXML
    private Button userDetailsButton;

    @FXML
    private Button addNewSaleButton;

    @FXML
    private Button loadXmlFileButton;

    @FXML
    private Button addNewStoreButton;

    @FXML
    private Button updateStoreProductButton;

    @FXML
    private Button placeAnOrderButton;

    @FXML
    private ComboBox<?> styleComboBox;

    @FXML
    private CheckBox animationCheckBox;

    public mainWindowController() {
        isXmlFileLoaded = new SimpleBooleanProperty(false);
    }
    @FXML
    private void initialize(){
        orderHistoryButton.disableProperty().bind(isXmlFileLoaded.not());
        storeDetailsButton.disableProperty().bind(isXmlFileLoaded.not());
        addNewProductButton.disableProperty().bind(isXmlFileLoaded.not());
        productDetailsButton.disableProperty().bind(isXmlFileLoaded.not());
        userDetailsButton.disableProperty().bind(isXmlFileLoaded.not());
        addNewSaleButton.disableProperty().bind(isXmlFileLoaded.not());
        addNewStoreButton.disableProperty().bind(isXmlFileLoaded.not());
        updateStoreProductButton.disableProperty().bind(isXmlFileLoaded.not());
        placeAnOrderButton.disableProperty().bind(isXmlFileLoaded.not());
    }


    public void setSDM(SuperDuperMarket SDM) {
        this.SDM = SDM;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @FXML
    void addNewProductAction(ActionEvent event) {

    }

    @FXML
    void addNewSaleAction(ActionEvent event) {

    }

    @FXML
    void addNewStoreAction(ActionEvent event) {

    }

    @FXML
    void animationChecked(ActionEvent event) {

    }

    @FXML
    void loadXmlFileAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("xml files","*.xml" ));
        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        if(selectedFile != null){
            try{
                SDM.loadXmlFileFromFileChooser(selectedFile);
                isXmlFileLoaded.set(true);
            }
            catch (Exception e){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Invalid XML file.");
                alert.setHeaderText("File was not loaded.");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
                return;
            }
        }
        else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Invalid XML file.");
            alert.setHeaderText("File was not loaded.");
            alert.setContentText("");
            alert.showAndWait();
            return;
        }
    }

    @FXML
    void placeAnOrderAction(ActionEvent event) {

    }

    @FXML
    void productDetailsAction(ActionEvent event) {

    }

    @FXML
    void storeDetailsAction(ActionEvent event) {

    }

    @FXML
    void styleComboBoxAction(ActionEvent event) {

    }

    @FXML
    void updateStoreProductAction(ActionEvent event) {

    }

    @FXML
    void userDetailsAction(ActionEvent event) {

    }

}
