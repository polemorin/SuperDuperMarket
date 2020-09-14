package SDMFX;

import SDMFX.StoreLevelOrderController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

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
    private Label DeliveryDateLabel;

    @FXML
    private Label CustomerNameLabel;

    @FXML
    private Label CustomerIDLabel;

    @FXML
    void CancelOrderButtonAction(ActionEvent event) {
        Stage s = (Stage)CancelOrderButton.getScene().getWindow();
        s.close();
    }

    @FXML
    void ConfirmOrderButtonAction(ActionEvent event) {
        SDM.placeOrderInSDM(order,order.getOrders().get(0).getCustomerID());
        Stage s = (Stage)CancelOrderButton.getScene().getWindow();
        s.close();
    }

    public void setData(SuperDuperMarket sdm, CustomerLevelOrder customerLevelOrder) {
        SDM = sdm;
        order = customerLevelOrder;
        initStoreLevelOrders();
        initLabels();
    }

    private void initLabels() {
        DeliveryPriceLabel.setText(String.format("%.2f",order.getDeliveryPrice()));
        ProductsPriceLabel.setText(String.format("%.2f",order.getTotalProductPrice()));
        TotalPriceLabel.setText(String.format("%.2f",order.getDeliveryPrice()+order.getTotalProductPrice()));
        DeliveryDateLabel.setText(order.getDate().toString());
        CustomerIDLabel.setText(Integer.toString(order.getOrders().get(0).getCustomerID()));
        CustomerNameLabel.setText(SDM.getUsers().get(order.getOrders().get(0).getCustomerID()).getName());
    }

    public void initStoreLevelOrders(){
        FXMLLoader fxmlLoader;
        Node storeOrderTile;
        StoreLevelOrderController storeLevelOrderController;
        for (StoreLevelOrder storeLevelOrder:order.getOrders()) {
            try{
                fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("SDMFX/StoreLevelOrder.fxml"));
                storeOrderTile = fxmlLoader.load();
                storeLevelOrderController = fxmlLoader.getController();
                storeLevelOrderController.setData(storeLevelOrder,SDM);
                StoreLevelOrderVbox.getChildren().add(storeOrderTile);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
