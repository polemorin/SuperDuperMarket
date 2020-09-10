
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class mainWindowController {


    private SimpleBooleanProperty isXmlFileLoaded;
    private Stage primaryStage;
    private SuperDuperMarket SDM;
    private Stage customerDetailsStage;
    private Stage productDetailsStage;
    private Stage updateProductStage;
    private Stage orderHistoryStage;
    private Stage storeDetailsStage;
    private Stage placeOrderStage;
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
    @FXML
    private ProgressBar ProgressBarLoader;
    @FXML
    private Label ProgressLabel;

    public mainWindowController() {
        isXmlFileLoaded = new SimpleBooleanProperty(false);
    }

    @FXML
    private void initialize() {
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
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("xml files", "*.xml"));
        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        TaskLoadXML taskLoad = new TaskLoadXML(SDM,selectedFile,isXmlFileLoaded);
        ProgressBarLoader.progressProperty().bind(taskLoad.progressProperty());
        SimpleStringProperty progressString = new SimpleStringProperty();
        progressString.bind(taskLoad.messageProperty());
        ProgressLabel.textProperty().bind(progressString);

        if (selectedFile != null) {
            new Thread(taskLoad).start();

            isXmlFileLoaded.set(true);
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Invalid XML file.");
            alert.setHeaderText("File was not loaded.");
            alert.setContentText("");
            alert.showAndWait();
            return;
        }
    }

    @FXML
    void orderHistoryAction(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("OrderHistoryWindow.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            orderHistoryStage = new Stage();
            orderHistoryStage.setTitle("Order history");
            orderHistoryStage.setScene(scene);
            orderHistoryStage.initOwner(primaryStage);
            orderHistoryStage.initModality(Modality.WINDOW_MODAL);
            OrderHistoryWindowController orderHistoryWindowController = fxmlLoader.getController();
            orderHistoryWindowController.setSDM(SDM);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        orderHistoryStage.showAndWait();
    }

    @FXML
    void placeAnOrderAction(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("PlaceOrderHome.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            placeOrderStage = new Stage();
            placeOrderStage.setTitle("Place Order");
            placeOrderStage.setScene(scene);
            placeOrderStage.initOwner(primaryStage);
            placeOrderStage.initModality(Modality.WINDOW_MODAL);
            PlaceOrderHomeController placeOrderHomeController = fxmlLoader.getController();
            placeOrderHomeController.setSDM(SDM, primaryStage);
        } catch (IOException e) {
            e.printStackTrace();
        }
        placeOrderStage.showAndWait();
    }

    @FXML
    void productDetailsAction(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("ProductDetails.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            productDetailsStage = new Stage();
            productDetailsStage.setTitle("ProductDetails");
            productDetailsStage.setScene(scene);
            productDetailsStage.setAlwaysOnTop(true);
            productDetailsStage.initOwner(primaryStage);
            productDetailsStage.initModality(Modality.WINDOW_MODAL);
            ProductDetailsController productDetailsController = fxmlLoader.getController();
            productDetailsController.setProductMap(SDM.getProducts());
            productDetailsController.setSDM(SDM);
        } catch (IOException e) {
            e.printStackTrace();
        }
        productDetailsStage.showAndWait();
    }

    @FXML
    void storeDetailsAction(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("StoreDetails.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            storeDetailsStage = new Stage();
            storeDetailsStage.setTitle("Store details");
            storeDetailsStage.setScene(scene);
            storeDetailsStage.initOwner(primaryStage);
            storeDetailsStage.initModality(Modality.WINDOW_MODAL);
            StoreDetailsController storeDetailsController = fxmlLoader.getController();
            storeDetailsController.setSDM(SDM);
            //orderHistoryStage.setOnCloseRequest(we -> orderHistoryWindowController.onClose());
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        storeDetailsStage.showAndWait();
    }

    @FXML
    void styleComboBoxAction(ActionEvent event) {

    }

    @FXML
    void updateStoreProductAction(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("UpdateStoreProduct.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            updateProductStage = new Stage();
            updateProductStage.setTitle("UpdateStoreProduct");
            updateProductStage.setScene(scene);
            updateProductStage.setAlwaysOnTop(true);
            updateProductStage.initOwner(primaryStage);
            updateProductStage.initModality(Modality.WINDOW_MODAL);
            UpdateStoreProductController updateStoreProductController = fxmlLoader.getController();
            updateStoreProductController.setSDM(SDM);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        updateProductStage.showAndWait();
    }

    @FXML
    void userDetailsAction(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("CustomerDetails.fxml"));
            Scene scene = new Scene(fxmlLoader.load(),600,400);
            customerDetailsStage = new Stage();
            customerDetailsStage.setTitle("CustomerDetails");
            customerDetailsStage.setScene(scene);
            customerDetailsStage.setAlwaysOnTop(true);
            customerDetailsStage.initOwner(primaryStage);
            customerDetailsStage.initModality(Modality.WINDOW_MODAL);
            CustomerDetailsController customerDetailsController = fxmlLoader.getController();
            customerDetailsController.setUserMap(SDM.getUsers());
            customerDetailsStage.setOnCloseRequest(we -> customerDetailsController.onClose());
        } catch (IOException e) {
            e.printStackTrace();
        }
        customerDetailsStage.showAndWait();
    }

}
