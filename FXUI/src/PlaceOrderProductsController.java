
import ProductTypes.Product;
import ProductTypes.StoreProduct;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlaceOrderProductsController {


    private SuperDuperMarket SDM;
    private User customer;
    private Store store;
    private LocalDate deliveryDate;
    private Stage previousWindow;
    private Stage mainStage;
    private Stage placeOrderSalesStage;
    private List <ProductTileController> productTileControllerList;
    private boolean firstClick = true;

    @FXML
    private ScrollPane WindowScroller;
    @FXML
    private ScrollPane ScrollProductPane;
    @FXML
    private FlowPane ProductsFlowPane;

    @FXML
    private Button ContinueButton;

    @FXML
    void ContinueButtonAction(ActionEvent event) {

        if(placeOrderSalesStage == null){
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("PlaceOrderSales.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                placeOrderSalesStage = new Stage();
                placeOrderSalesStage.setTitle("Place Order Sales");
                placeOrderSalesStage.setScene(scene);
                //placeOrderSalesStage.setAlwaysOnTop(true);
                placeOrderSalesStage.initOwner(mainStage);
                placeOrderSalesStage.initModality(Modality.WINDOW_MODAL);
                PlaceOrderSalesController placeOrderSalesController = fxmlLoader.getController();
                placeOrderSalesController.setData(SDM,customer,store,deliveryDate,getProductsByIdAndAmount(),mainStage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //onClose();
        placeOrderSalesStage.show();
         Stage s = (Stage)(ContinueButton.getScene().getWindow());
         s.close();
    }
    public void setData(SuperDuperMarket sdm,Stage mainStage, User customer, Store store, LocalDate date) throws IOException {
        SDM = sdm;
        this.customer = customer;
        this.store = store;
        deliveryDate = date;
        this.mainStage = mainStage;
        //THIS CODE MAKES THE FLOW PANE FILL ALL THE WIDTH OF HIS ROOT PANE(SCRO LLPANE)
       ScrollProductPane.viewportBoundsProperty().addListener(new ChangeListener<Bounds>() {
    @Override public void changed(ObservableValue<? extends Bounds> bounds, Bounds oldBounds, Bounds newBounds) {
               ProductsFlowPane.setPrefWidth(newBounds.getWidth());
           }
       });

      setProducts();

    }


    private void setProducts() throws IOException {
        try {
            FXMLLoader fxmlLoader;
            Node ProductTile;
            ProductTileController productTileController;
            productTileControllerList = new ArrayList<>();
            if (store == null) {
                for (Map.Entry<Integer, Product> product : SDM.getProducts().entrySet()) {
                    fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("ProductTile.fxml"));
                    ProductTile = fxmlLoader.load();
                    productTileController = fxmlLoader.getController();
                    productTileController.setData(product.getValue(), null);
                    ProductsFlowPane.getChildren().add(ProductTile);
                    productTileControllerList.add(productTileController);
                }
            }
            else{
                for (Map.Entry<Integer, StoreProduct> product : store.getProducts().entrySet()) {
                    fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("ProductTile.fxml"));
                    ProductTile = fxmlLoader.load();
                    productTileController = fxmlLoader.getController();
                    productTileController.setData(product.getValue(), product.getValue().getPrice());
                    ProductsFlowPane.getChildren().add(ProductTile);
                    productTileControllerList.add(productTileController);
                }
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void setPreviousWindow(Stage window) {
        previousWindow = window;
    }

    public void closePreviousWindow(){
        previousWindow.close();
    }

    private Map<Integer,Double> getProductsByIdAndAmount(){
        Map<Integer,Double> products = new HashMap<>();
        Integer productID;
        Double amount;

        for (ProductTileController productController :productTileControllerList) {
            if(productController.getAmount()>0){
                productID = productController.getProduct().getProductID();
                amount = productController.getAmount();
                products.put(productID,amount);
            }
        }
        return products;
    }
}
