
import ProductTypes.Product;
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
import javafx.scene.layout.FlowPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;

public class PlaceOrderProductsController {


    private SuperDuperMarket SDM;
    private User customer;
    private Store store;
    private LocalDate deliveryDate;
    Stage placeOrderSalesStage;
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
                placeOrderSalesStage.setAlwaysOnTop(true);
                //placeOrderProductsStage.initOwner(primaryStage);
                placeOrderSalesStage.initModality(Modality.WINDOW_MODAL);
                PlaceOrderSalesController placeOrderSalesController = fxmlLoader.getController();
               // placeOrderSalesController.setData(SDM,customer,store,date);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Stage s = (Stage)(ContinueButton.getScene().getWindow());
        s.close();
        //onClose();
        placeOrderSalesStage.showAndWait();
    }

    public void setData(SuperDuperMarket sdm, User customer, Store store, LocalDate date) throws IOException {
        SDM = sdm;
        this.customer = customer;
        this.store = store;
        deliveryDate = date;
        //THIS CODE MAKES THE FLOW PANE FILL ALL THE WIDTH OF HIS ROOT PANE(SCRO LLPANE)
 //       ScrollProductPane.viewportBoundsProperty().addListener(new ChangeListener<Bounds>() {
 // @Override public void changed(ObservableValue<? extends Bounds> bounds, Bounds oldBounds, Bounds newBounds) {
 //               ProductsFlowPane.setPrefWidth(newBounds.getWidth());
 //           }
 //       });
 //       //
    //   setProducts();
    }


    private void setProducts() throws IOException {
        try {
            FXMLLoader fxmlLoader;
            Node ProductTile;
            ProductTileController productTileController;
            if (store == null) {
                for (Map.Entry<Integer, Product> product : SDM.getProducts().entrySet()) {
                    fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("ProductTile.fxml"));
                    ProductTile = fxmlLoader.load();
                    productTileController = fxmlLoader.getController();
                    productTileController.setData(product.getValue(), null);
                    ProductsFlowPane.getChildren().add(ProductTile);
                }
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
