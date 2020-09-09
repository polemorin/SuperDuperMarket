
import ProductTypes.Product;
import ProductTypes.StoreProduct;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class PlaceOrderSummaryController {

    SuperDuperMarket SDM;
    CustomerLevelOrder order;
    @FXML
    private VBox StoreLevelOrderVbox;

    @FXML
    private Label ProductsPriceLabel;

    @FXML
    private Label DeliveryPriceLabel;

    @FXML
    private Label TotalPriceLabel;

    @FXML
    private Button CancelOrderButton;

    @FXML
    private Button ConfirmOrderButton;

    @FXML
    void CancelOrderButtonAction(ActionEvent event) {

    }

    @FXML
    void ConfirmOrderButtonAction(ActionEvent event) {

    }

    public void setData(SuperDuperMarket sdm, CustomerLevelOrder customerLevelOrder) {
        SDM = sdm;
        order = customerLevelOrder;
        initStoreLevelOrders();
    }
    public void initStoreLevelOrders(){
        FXMLLoader fxmlLoader;
        Node storeOrderTile;
        StoreLevelOrderController storeLevelOrderController;
        for (StoreLevelOrder storeLevelOrder:order.getOrders()) {
            try{
                fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("StoreLevelOrder.fxml"));
                storeOrderTile = fxmlLoader.load();
                storeLevelOrderController = fxmlLoader.getController();
                storeLevelOrderController.setData(storeLevelOrder);
                StoreLevelOrderVbox.getChildren().add(storeOrderTile);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
