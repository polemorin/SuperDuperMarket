import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Map;

public class OrderHistoryWindowController {

    SuperDuperMarket SDM;

    @FXML
    private ComboBox<CustomerLevelOrder> OrderIDComboBox;

    @FXML
    private VBox OrderVbox;

    @FXML
    private Button BackButton;

    @FXML
    private Label ProductsPriceLabel;

    @FXML
    private Label DeliveryPriceLabel;

    @FXML
    private Label TotalPriceLabel;

    @FXML
    private Label DateLabel;

    @FXML
    private Label CustomerIDLabel;

    @FXML
    private Label CustomerNameLabel;

    @FXML
    void BackButtonAction(ActionEvent event) {
        Stage s = (Stage)BackButton.getScene().getWindow();
        s.close();
    }

    @FXML
    void OrderIDComboBoxAction(ActionEvent event) {
        if(OrderIDComboBox.getValue() != null){
            initLabels();
            addStoreLevelOrdersToWindow();
        }
    }

    private void addStoreLevelOrdersToWindow() {
        FXMLLoader fxmlLoader;
        Node storeOrderTile;
        CustomerLevelOrder order = OrderIDComboBox.getValue();
        StoreLevelOrderController storeLevelOrderController;
        for (StoreLevelOrder storeLevelOrder:order.getOrders()) {
            try{
                fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("StoreLevelOrder.fxml"));
                storeOrderTile = fxmlLoader.load();
                storeLevelOrderController = fxmlLoader.getController();
                storeLevelOrderController.setData(storeLevelOrder,SDM);
                OrderVbox.getChildren().add(storeOrderTile);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void initLabels() {
        CustomerLevelOrder order = OrderIDComboBox.getValue();
        CustomerIDLabel.setText(Integer.toString(order.getOrders().get(0).getCustomerID()));
        CustomerNameLabel.setText(SDM.getUsers().get(order.getOrders().get(0).getCustomerID()).getName());
        DateLabel.setText(order.getDate().toString());
        DeliveryPriceLabel.setText(String.format("%.2f",order.getDeliveryPrice()));
        ProductsPriceLabel.setText(String.format("%.2f",order.getTotalProductPrice()));
        TotalPriceLabel.setText(String.format("%.2f",order.getTotalProductPrice() + order.getDeliveryPrice()));
    }

    @FXML
    private void initialize(){

    }

    public void setSDM(SuperDuperMarket sdm) {
        SDM = sdm;
        initOrderComboBox();
    }

    private void initOrderComboBox() {
        for (Map.Entry<Integer,CustomerLevelOrder> order:SDM.getOrderHistory().entrySet()) {
            OrderIDComboBox.getItems().add(order.getValue());
        }
    }
}
