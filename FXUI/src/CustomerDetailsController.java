import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.util.Map;

public class CustomerDetailsController {

    @FXML
    private Button BackButton;

    @FXML
    private ComboBox<User> CustomerComboBox;

    @FXML
    private Label NameLableShow;

    @FXML
    private Label IDLableShow;

    @FXML
    private Label LocationShowLable;

    @FXML
    private Label OrderAmountShowLable;

    @FXML
    private Label ProductCostShowLable;

    @FXML
    private Label DeliveryCostLable;

    Map<Integer,User> userMap;
    Stage customerDetailsStage;

    // public CustomerDetailsController(Map<Integer,User> userMap) {
    //     this.userMap = userMap;
    //     for (Map.Entry<Integer,User> user: userMap.entrySet()) {
    //         CustomerComboBox.getItems().add(user.getValue().getName());
    //     }
    // }
    public CustomerDetailsController() {

    }
    public void setUserMap(Map<Integer,User> userMap){
        this.userMap = userMap;
        updateCustomerComboBox();
    }

    private void updateCustomerComboBox() {
        for (Map.Entry<Integer,User> user: userMap.entrySet()) {
            CustomerComboBox.getItems().add(user.getValue());
        }
    }

    @FXML
    void BackButtonAction(ActionEvent event) {
        Stage s = (Stage)(BackButton.getScene().getWindow());
        onClose();
        s.close();
    }

    @FXML
    void ChooseCustomerAction(ActionEvent event) {
        User chosenCustomer = CustomerComboBox.getValue();
        if (chosenCustomer != null) {
            NameLableShow.setText(chosenCustomer.getName());
            IDLableShow.setText(Integer.toString(chosenCustomer.getID()));
            LocationShowLable.setText("[" + chosenCustomer.getLocation().x + "," + chosenCustomer.getLocation().y + "]");
            OrderAmountShowLable.setText(Integer.toString(chosenCustomer.getAmountOfOrders()));
            ProductCostShowLable.setText(String.format("%.2f", chosenCustomer.getAverageProductCostFromOrders()));
            DeliveryCostLable.setText(String.format("%.2f", chosenCustomer.getAverageDeliveryCostFromOrders()));
        }
    }

    public void onClose() {
        CustomerComboBox.valueProperty().setValue(null);
        NameLableShow.setText("");
        IDLableShow.setText("");
        LocationShowLable.setText("");
        OrderAmountShowLable.setText("");
        ProductCostShowLable.setText("");
        DeliveryCostLable.setText("");
    }
}
