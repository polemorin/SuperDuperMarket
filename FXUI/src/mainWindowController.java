import SDMCommon.*;
import SDMFX.Add.AddProduct.AddProductController;
import SDMFX.Add.AddSale.AddSaleController;
import SDMFX.Add.AddStore.AddStoreController;
import SDMFX.Main.Map.MapTileController;
import SDMFX.PlaceOrder.PlaceOrderHomeController;
import SDMFX.ShowDetails.CustomerDetailsController;
import SDMFX.ShowDetails.OrderHistoryWindowController;
import SDMFX.ShowDetails.ProductDetailsController;
import SDMFX.ShowDetails.StoreDetailsController;
import SDMFX.UpdateStoreProduct.UpdateStoreProductController;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.*;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Random;

public class mainWindowController {


    private SimpleBooleanProperty isXmlFileLoaded;
    private Stage primaryStage;
    private MarketArea SDM;
    private Stage customerDetailsStage;
    private Stage productDetailsStage;
    private Stage updateProductStage;
    private Stage orderHistoryStage;
    private Stage storeDetailsStage;
    private Stage placeOrderStage;
    private Stage addSaleStage;
    private Stage addProductStage;
    private Stage addStoreStage;
    private String currentStyle;

    @FXML
    private ScrollPane MainWindowScrollPane;
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
    private ComboBox<String> styleComboBox;

    @FXML
    private CheckBox animationCheckBox;
    @FXML
    private ProgressBar ProgressBarLoader;
    @FXML
    private Label ProgressLabel;
    @FXML
    private TilePane MapTilePane;
    @FXML
    private ImageView BackIMG;
    @FXML
    private Button ShowMapButton;


    public mainWindowController() {
        isXmlFileLoaded = new SimpleBooleanProperty(false);
    }

    @FXML
    private void initialize() throws IOException {
        orderHistoryButton.disableProperty().bind(isXmlFileLoaded.not());
        storeDetailsButton.disableProperty().bind(isXmlFileLoaded.not());
        addNewProductButton.disableProperty().bind(isXmlFileLoaded.not());
        productDetailsButton.disableProperty().bind(isXmlFileLoaded.not());
        userDetailsButton.disableProperty().bind(isXmlFileLoaded.not());
        addNewSaleButton.disableProperty().bind(isXmlFileLoaded.not());
        addNewStoreButton.disableProperty().bind(isXmlFileLoaded.not());
        updateStoreProductButton.disableProperty().bind(isXmlFileLoaded.not());
        placeAnOrderButton.disableProperty().bind(isXmlFileLoaded.not());
        ShowMapButton.disableProperty().bind(isXmlFileLoaded.not());
        styleComboBox.getItems().add("None");
        styleComboBox.getItems().add("Lime");
        styleComboBox.getItems().add("Summer Breeze");
        styleComboBox.getItems().add("Peach");
    }


    public void setSDM(MarketArea SDM) {
        this.SDM = SDM;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    private void updateStyle(String style){
        if(style.equals("None")){
            currentStyle = "None";
            styleComboBox.getScene().getStylesheets().clear();
        }
        if(style.equals("Lime")){
            currentStyle = "Lime.css";
            styleComboBox.getScene().getStylesheets().clear();
            styleComboBox.getScene().getStylesheets().add(currentStyle);
        }
        if(style.equals("Summer Breeze")){
            currentStyle = "SummerBreeze.css";
            styleComboBox.getScene().getStylesheets().clear();
            styleComboBox.getScene().getStylesheets().add(currentStyle);
        }

        if(style.equals("Peach")){
            currentStyle = "Peach.css";
            styleComboBox.getScene().getStylesheets().clear();
            styleComboBox.getScene().getStylesheets().add(currentStyle);
        }
    }

    @FXML
    void addNewProductAction(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("SDMFX/Add/AddProduct/AddProduct.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            if(currentStyle.compareTo("None") != 0){
                scene.getStylesheets().add(currentStyle);
            }
            addProductStage = new Stage();
            addProductStage.setTitle("Order history");
            addProductStage.setScene(scene);
            addProductStage.initOwner(primaryStage);
            addProductStage.initModality(Modality.WINDOW_MODAL);
            AddProductController addProductController = fxmlLoader.getController();
            addProductController.setSDM(SDM);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        addProductStage.showAndWait();

    }

    @FXML
    void addNewSaleAction(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("SDMFX/Add/AddSale/AddSale.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            if(currentStyle.compareTo("None") != 0){
                scene.getStylesheets().add(currentStyle);
            }
            addSaleStage = new Stage();
            addSaleStage.setTitle("Add sale");

            addSaleStage.setScene(scene);
            addSaleStage.initOwner(primaryStage);
            addSaleStage.initModality(Modality.WINDOW_MODAL);
            AddSaleController addSaleController = fxmlLoader.getController();
            addSaleController.setSDM(SDM);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        addSaleStage.showAndWait();
    }

    @FXML
    void addNewStoreAction(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("SDMFX/Add/AddStore/AddStore.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            if(currentStyle.compareTo("None") != 0){
                scene.getStylesheets().add(currentStyle);
            }
            addStoreStage = new Stage();
            addStoreStage.setTitle("Add Store");
            addStoreStage.setScene(scene);
            addStoreStage.initOwner(primaryStage);
            addStoreStage.initModality(Modality.WINDOW_MODAL);
            AddStoreController addStoreController = fxmlLoader.getController();
            addStoreController.setSDM(SDM);
        } catch (IOException e) {
            e.printStackTrace();
        }
        addStoreStage.showAndWait();
        try{
            if(!ShowMapButton.visibleProperty().getValue()){
                createMap();
            }
        }catch (Exception ignored){
            System.out.println(ignored.getMessage());
        }


    }

    @FXML
    void animationChecked(ActionEvent event) {

    }

    @FXML
    void loadXmlFileAction(ActionEvent event) throws InterruptedException, IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("xml files", "*.xml"));
        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        TaskLoadXML taskLoad = new TaskLoadXML(SDM,selectedFile,isXmlFileLoaded);
        ProgressBarLoader.progressProperty().bind(taskLoad.progressProperty());
        SimpleStringProperty progressString = new SimpleStringProperty();
        progressString.bind(taskLoad.messageProperty());
        ProgressLabel.textProperty().bind(progressString);
        if (selectedFile != null) {
          Thread task =  new Thread(taskLoad);
          task.start();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Invalid XML file.");
            alert.setHeaderText("File was not loaded.");
            alert.setContentText("");
            alert.showAndWait();
            return;
        }
        ShowMapButton.visibleProperty().setValue(true);
    }


    @FXML
    void orderHistoryAction(ActionEvent event) throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("SDMFX/ShowDetails/OrderHistoryWindow.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            if(currentStyle.compareTo("None") != 0){
                scene.getStylesheets().add(currentStyle);
            }
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
            fxmlLoader.setLocation(getClass().getResource("SDMFX/PlaceOrder/PlaceOrderHome.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            if(currentStyle.compareTo("None") != 0){
                scene.getStylesheets().add(currentStyle);
            }
            placeOrderStage = new Stage();
            placeOrderStage.setTitle("Place Order");
            placeOrderStage.setScene(scene);
            placeOrderStage.initOwner(primaryStage);
            placeOrderStage.initModality(Modality.WINDOW_MODAL);
            PlaceOrderHomeController placeOrderHomeController = fxmlLoader.getController();
            placeOrderHomeController.setSDM(SDM, primaryStage);
            placeOrderHomeController.setStyle(currentStyle);
            placeOrderHomeController.doAnimation(animationCheckBox.isSelected());
        } catch (IOException e) {
            e.printStackTrace();
        }
        placeOrderStage.showAndWait();
    }

    @FXML
    void productDetailsAction(ActionEvent event) throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("SDMFX/ShowDetails/ProductDetails.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            if(currentStyle.compareTo("None") != 0){
                scene.getStylesheets().add(currentStyle);
            }
            productDetailsStage = new Stage();
            productDetailsStage.setTitle("ProductDetails");
            productDetailsStage.setScene(scene);
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
            fxmlLoader.setLocation(getClass().getResource("SDMFX/ShowDetails/StoreDetails.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            if(currentStyle.compareTo("None") != 0){
                scene.getStylesheets().add(currentStyle);
            }
            storeDetailsStage = new Stage();
            storeDetailsStage.setTitle("Store details");
            storeDetailsStage.setScene(scene);
            storeDetailsStage.initOwner(primaryStage);
            storeDetailsStage.initModality(Modality.WINDOW_MODAL);
            StoreDetailsController storeDetailsController = fxmlLoader.getController();
            storeDetailsController.setSDM(SDM);
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
        updateStyle(styleComboBox.getValue());
    }

    @FXML
    void updateStoreProductAction(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("SDMFX/UpdateStoreProduct/UpdateStoreProduct.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            if(currentStyle.compareTo("None") != 0){
                scene.getStylesheets().add(currentStyle);
            }
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

    private void createMap() throws IOException {

        Point topRight = SDM.getTopRightMapEdge();
        Point bottomLeft = SDM.getBottomLeftMapEdge();

        FXMLLoader fxmlLoader;
        Node MapTile;
        MapTileController mapTileController;
        MapTilePane.getChildren().clear();

        HBox MapRow;

        Map<Integer, Store> storeMap = SDM.getStores();
        Map<Integer, User> userMap = SDM.getUsers();

        BackgroundImage images = new BackgroundImage(new Image("images/MapBackground2new.jpg"), BackgroundRepeat.NO_REPEAT,BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,new BackgroundSize(BackgroundSize.AUTO,BackgroundSize.AUTO,false,false,false,true));
        MapTilePane.setBackground(new Background(images));
        MapTilePane.visibleProperty().setValue(true);
        Boolean isTileOccupied = false;
        Random rand;
        for(int i = topRight.y + 1;i >= bottomLeft.y - 1; i--){
            MapRow = new HBox();
            for(int j = bottomLeft.x - 1; j <= topRight.x + 1; j++){
                fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("SDMFX/Main/Map/MapTileTest.fxml"));
                MapTile = fxmlLoader.load();
                mapTileController = fxmlLoader.getController();
                for (Map.Entry<Integer, User> user:userMap.entrySet()) {
                    if(user.getValue().getLocation().x == j && user.getValue().getLocation().y == i){
                        mapTileController.setUserImage(user.getValue());
                        isTileOccupied = true;
                    }
                }
                for (Map.Entry<Integer, Store> store:storeMap.entrySet()) {
                    if(store.getValue().getLocation().x == j && store.getValue().getLocation().y == i){
                        mapTileController.setStoreImage(store.getValue());
                        isTileOccupied = true;
                    }
                }
                updateMapTileLocationText(i, j, bottomLeft, topRight ,mapTileController,isTileOccupied);

                isTileOccupied = false;

                MapRow.getChildren().add(MapTile);
            }
            MapTilePane.getChildren().add(MapRow);
        }
        ShowMapButton.visibleProperty().setValue(false);
    }

    private void updateMapTileLocationText(int i, int j, Point bottomLeft, Point topRight, MapTileController mapTileController, Boolean isTileOccupied) {
        if(!isTileOccupied) {
            if (i == bottomLeft.x - 1) {
                mapTileController.setCoordinateLabel(Integer.toString(j));
                isTileOccupied = true;
            }
            if (j == bottomLeft.y - 1) {
                mapTileController.setCoordinateLabel(Integer.toString(i));
                isTileOccupied = true;
            }
            if (i == topRight.y + 1) {
                mapTileController.setCoordinateLabel(Integer.toString(j));
                isTileOccupied = true;
            }
            if (j == topRight.x + 1) {
                mapTileController.setCoordinateLabel(Integer.toString(i));
                isTileOccupied = true;
            }
            if (j == bottomLeft.y - 1 && i == bottomLeft.x - 1
                    || j == topRight.x + 1 && i == topRight.y + 1
                    || j == bottomLeft.x - 1 && i == topRight.y + 1
                    || j == topRight.x + 1 && i == bottomLeft.y - 1) {
                mapTileController.setCoordinateLabel("");
                isTileOccupied = true;
            }
        }
        mapTileController.setIsTileOccupied(isTileOccupied);
        if(!isTileOccupied){
            Random rand = new Random();
            if(rand.nextInt(100)<2){
                mapTileController.setPrettyTile();
                isTileOccupied = true;
            }
        }
        if(!isTileOccupied){
            mapTileController.removeTile();
        }
    }

    @FXML
    void userDetailsAction(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("SDMFX/ShowDetails/CustomerDetails.fxml"));
            Scene scene = new Scene(fxmlLoader.load(),600,400);
            if(currentStyle.compareTo("None") != 0){
                scene.getStylesheets().add(currentStyle);
            }
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
    @FXML
    void ShowMapButtonAction(ActionEvent event) throws IOException {
        Stage newMapStage = new Stage();
        TilePane tilePane = new TilePane();
        ScrollPane root = new ScrollPane(tilePane);
        double mapWidth = 1;
        double mapHeight;

        HBox MapRow;


        FXMLLoader fxmlLoader;
        Point topRight = SDM.getTopRightMapEdge();
        Point bottomLeft = SDM.getBottomLeftMapEdge();

        Node MapTile;
        MapTileController mapTileController;
        tilePane.getChildren().clear();

        Map<Integer, Store> storeMap = SDM.getStores();
        Map<Integer, User> userMap = SDM.getUsers();

        BackgroundImage images = new BackgroundImage(new Image("images/MapBackground2new.jpg"), BackgroundRepeat.NO_REPEAT,BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,new BackgroundSize(BackgroundSize.AUTO,BackgroundSize.AUTO,false,false,false,true));
        tilePane.setBackground(new Background(images));
        tilePane.visibleProperty().setValue(true);
        Boolean isTileOccupied = false;
        Random rand;

        mapWidth = 35 *(topRight.x + 2.5 - bottomLeft.x);
        mapHeight = 35 * (topRight.y + 2.5 - bottomLeft.y);
        for(int i = topRight.y + 1;i >= bottomLeft.y - 1; i--){
            MapRow = new HBox();
            for(int j = bottomLeft.x - 1; j <= topRight.x + 1; j++){
                fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("SDMFX/Main/Map/MapTileTest.fxml"));
                MapTile = fxmlLoader.load();
                mapTileController = fxmlLoader.getController();
                for (Map.Entry<Integer, User> user:userMap.entrySet()) {
                    if(user.getValue().getLocation().x == j && user.getValue().getLocation().y == i){
                        mapTileController.setUserImage(user.getValue());
                        isTileOccupied = true;
                    }
                }
                for (Map.Entry<Integer, Store> store:storeMap.entrySet()) {
                    if(store.getValue().getLocation().x == j && store.getValue().getLocation().y == i){
                        mapTileController.setStoreImage(store.getValue());
                        isTileOccupied = true;
                    }
                }
                updateMapTileLocationText(i, j, bottomLeft, topRight ,mapTileController,isTileOccupied);
                isTileOccupied = false;
                MapRow.getChildren().add(MapTile);
            }
            tilePane.setMaxWidth(MapRow.getWidth());
            tilePane.getChildren().add(MapRow);
        }
        Scene scene = new Scene(root,Math.min(mapWidth,700),Math.min(mapHeight,500));
        newMapStage.setScene(scene);
        newMapStage.sizeToScene();
        newMapStage.show();

    }

    public void setStyle(String s) {
        this.currentStyle = s;
    }
}
