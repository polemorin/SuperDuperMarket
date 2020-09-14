package SDMFX.PlaceOrder;
import SDMCommon.*;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.print.DocFlavor;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;

public class PlaceOrderHomeController {


    private Stage mainStage;
    private Stage placeOrderProductsStage;
    private SuperDuperMarket SDM;
    private SimpleBooleanProperty isStaticOrderType;
    private SimpleBooleanProperty isAllDetailsFilled;
    private Boolean isDateChosen;
    private Boolean isCustomerChosen;
    private Boolean isStoreChosen;
    private Boolean isDynamicOrderTypeChosen;


    @FXML
    private ComboBox<User> CustomerComboBox;

    @FXML
    private DatePicker DeliveryDatePicker;

    @FXML
    private RadioButton StaticRadio;

    @FXML
    private RadioButton DynamicRadio;

    @FXML
    private Label deliveryPriceStaticLabel;

    @FXML
    private ComboBox<Store> StoreComboBox;

    @FXML
    private Label DeliveryPriceLabel;

    @FXML
    private Button NextButton;

    public PlaceOrderHomeController() {
        isAllDetailsFilled = new SimpleBooleanProperty(false);
        isStaticOrderType = new SimpleBooleanProperty(false);
        isDateChosen = false;
        isCustomerChosen = false;
        isStoreChosen = false;
        isDynamicOrderTypeChosen = false;
    }

    @FXML
   private  void initialize(){
      StoreComboBox.visibleProperty().bind(isStaticOrderType);
      deliveryPriceStaticLabel.visibleProperty().bind(isStaticOrderType);
      DeliveryPriceLabel.visibleProperty().bind(isStaticOrderType);
      NextButton.disableProperty().bind(isAllDetailsFilled.not());


   }

    private void CheckAllDataFilled(){
        if(isCustomerChosen && isDateChosen ){
            if(isDynamicOrderTypeChosen)
                isAllDetailsFilled.setValue(true);
            else if(isStaticOrderType.getValue() && isStoreChosen){
                isAllDetailsFilled.setValue(true);
            }
            else{
                isAllDetailsFilled.setValue(false);
            }
        }
        else{
            isAllDetailsFilled.setValue(false);
        }
   }

    public void setSDM(SuperDuperMarket sdm, Stage mainStage) {
        SDM = sdm;
        Map<Integer, User> userMap = SDM.getUsers();
        for (Map.Entry<Integer, User> user:userMap.entrySet()) {
            CustomerComboBox.getItems().add(user.getValue());
        }
        Map<Integer, Store> storeMap = SDM.getStores();
        for (Map.Entry<Integer, Store> store:storeMap.entrySet()) {
            StoreComboBox.getItems().add(store.getValue());
        }
        this.mainStage = mainStage;
    }

    @FXML
    void CustomerComboBoxAction(ActionEvent event) {
        if(StoreComboBox.getValue()!= null){
            setDeliveryPriceLabel();
            isCustomerChosen = true;
        }
        isCustomerChosen = true;
        CheckAllDataFilled();


    }

    @FXML
    void DeliveryDatePickerAction(ActionEvent event) {
        if(DeliveryDatePicker.getValue() != null){
            isDateChosen = true;
        }
        isDateChosen = true;
        CheckAllDataFilled();

    }

    @FXML
    void DynamicRadioAction(ActionEvent event) {
        isStaticOrderType.setValue(false);
        isDynamicOrderTypeChosen = true;
        CheckAllDataFilled();
    }

    @FXML
    void NextButtonAction(ActionEvent event) {

        User customer = CustomerComboBox.getValue();
        Store store = null;
        if(isStaticOrderType.getValue()){
            store = StoreComboBox.getValue();
        }
        LocalDate date = DeliveryDatePicker.getValue();
        //----------------------------------------------------
        if(placeOrderProductsStage == null){
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(PlaceOrderProductsController.class.getResource("PlaceOrderProducts.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                placeOrderProductsStage = new Stage();
                placeOrderProductsStage.setTitle("Place Order");
                placeOrderProductsStage.setScene(scene);
                placeOrderProductsStage.initOwner(mainStage);
                placeOrderProductsStage.initModality(Modality.WINDOW_MODAL);
                PlaceOrderProductsController placeOrderProductsController = fxmlLoader.getController();
                placeOrderProductsController.setData(SDM,mainStage,customer,store,date);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        placeOrderProductsStage.show();
        Stage s = (Stage)NextButton.getScene().getWindow();
        s.close();
    }

    @FXML
    void StaticRadioAction(ActionEvent event) {
        isStaticOrderType.setValue(true);
        isDynamicOrderTypeChosen = false;
        CheckAllDataFilled();
    }

    @FXML
    void StoreComboBoxAction(ActionEvent event) {
        if(StoreComboBox.getValue() != null){
            setDeliveryPriceLabel();
            isStoreChosen = true;
        }
        setDeliveryPriceLabel();
        isStoreChosen = true;
        CheckAllDataFilled();
    }

    void setDeliveryPriceLabel() {
            Double userDistanceFromStore;
            Store chosenStore = StoreComboBox.getValue();
            User customer = CustomerComboBox.getValue();
            userDistanceFromStore = customer.distanceFromStore(chosenStore);

            DeliveryPriceLabel.setText(String.format("%.2f", chosenStore.getDeliveryPPK() * userDistanceFromStore));
        }

}
