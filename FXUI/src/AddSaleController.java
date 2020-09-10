import ProductTypes.Product;
import ProductTypes.StoreProduct;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.util.Map;

public class AddSaleController {

    private SuperDuperMarket SDM;
    private SimpleBooleanProperty isSaleTriggerReady;
    private SimpleBooleanProperty isStoreChosenTrigger;
    private SimpleBooleanProperty isStoreProductChosenTrigger;
    private SimpleBooleanProperty isValidSaleName;
    private double PromptAmountDouble;
    @FXML
    private ComboBox<Store> StoreComboBox;

    @FXML
    private Label SaleNameErrorLabel;

    @FXML
    private TextField SaleNameTextField;

    @FXML
    private ComboBox<StoreProduct> TriggerItemComboBox;

    @FXML
    private ComboBox<String> OperatorComboBox;

    @FXML
    private Label PromptAmountLabel;

    @FXML
    private Button MinusPromptAmountButton;

    @FXML
    private Button PlusPromptAmountButton;

    @FXML
    private Button AddOfferButton;

    @FXML
    private ComboBox<?> OfferItemsComboBox;

    @FXML
    private Label PromptAmount1;

    @FXML
    private Button OfferAmountMinusButton;

    @FXML
    private Button OfferAmountPlusButton;

    @FXML
    private Label ForAdditionalPriceLabel;

    @FXML
    private Label UnitOrKiloLabel;

    @FXML
    private TextField ForAdditionalPriceTextBox;

    @FXML
    private TableView<?> SaleTableView;

    @FXML
    private TableColumn<?, ?> ProductNameTableView;

    @FXML
    private TableColumn<?, ?> ProductAmountTableView;

    @FXML
    private TableColumn<?, ?> ProductPriceTableView;

    @FXML
    private Button SetSaleButton;

    @FXML
    private Button BackButton;

    @FXML
    private Button AddSaleButton;


    @FXML
    private void initialize(){
        isSaleTriggerReady = new SimpleBooleanProperty(false);
        isStoreChosenTrigger = new SimpleBooleanProperty(false);
        isStoreProductChosenTrigger = new SimpleBooleanProperty(false);
        isValidSaleName = new SimpleBooleanProperty(false);
        TriggerItemComboBox.disableProperty().bind(isStoreChosenTrigger.not());
        OperatorComboBox.disableProperty().bind(isStoreChosenTrigger.not());
        MinusPromptAmountButton.disableProperty().bind(isStoreProductChosenTrigger.not());
        PlusPromptAmountButton.disableProperty().bind(isStoreProductChosenTrigger.not());
        SetSaleButton.disableProperty().bind(isSaleTriggerReady.not());
        initOperatorComboBox();
        initStoreComboBox();
    }
    @FXML
    void AddOfferButtonAction(ActionEvent event) {

    }

    @FXML
    void AddSaleButtonAction(ActionEvent event) {

    }

    @FXML
    void BackButtonAction(ActionEvent event) {

    }

    @FXML
    void ForAdditionalPriceTextBoxAction(ActionEvent event) {

    }

    @FXML
    void MinusPromptAmountButtonAction(ActionEvent event) {
        StoreProduct product = TriggerItemComboBox.getValue();
        if(product != null){
            if(product.getProductCategory().toString().equalsIgnoreCase("Quantity")&& PromptAmountDouble > 1){
                PromptAmountDouble--;
            }
            else if(product.getProductCategory().toString().equalsIgnoreCase("Weight")&& PromptAmountDouble > 0.5){
                PromptAmountDouble -= 0.5;
            }
            PromptAmountLabel.setText(Double.toString(PromptAmountDouble));
        }
    }

    @FXML
    void OfferAmountMinusButtonAction(ActionEvent event) {

    }

    @FXML
    void OfferAmountPlusButtonAction(ActionEvent event) {

    }

    @FXML
    void OfferItemsComboBoxAction(ActionEvent event) {

    }

    @FXML
    void OperatorComboBoxAction(ActionEvent event) {

    }

    @FXML
    void PlusPromptAmountButtonAction(ActionEvent event) {
        StoreProduct product = TriggerItemComboBox.getValue();
        if(product != null){
            if(product.getProductCategory().toString().equalsIgnoreCase("Quantity")){
                PromptAmountDouble++;
            }
            else{
                PromptAmountDouble += 0.5;
            }
            PromptAmountLabel.setText(Double.toString(PromptAmountDouble));
        }
    }

    @FXML
    void SaleNameTextBoxAction(ActionEvent event) {
        String saleName = SaleNameTextField.getText();
        if(!saleName.isEmpty()){
            if(!SDM.isSaleNameAlreadyInMarket(saleName)){
                isValidSaleName.setValue(true);
            }else{
                isValidSaleName.setValue(false);
            }
        }else{
            isValidSaleName.setValue(false);
        }

    }

    @FXML
    void SetSaleButtonAction(ActionEvent event) {
        isStoreChosenTrigger.setValue(false);
    }

    @FXML
    void StoreComboBoxAction(ActionEvent event) {
        if(StoreComboBox.getValue() != null){
            isStoreChosenTrigger.setValue(true);
        }
        initStoreProductsComboBox();
    }

    @FXML
    void TriggerItemComboBoxAction(ActionEvent event) {
        if(StoreComboBox.getValue() != null){
            isStoreProductChosenTrigger.setValue(true);
        }
        setSoldByLabel();
        setPromptAmountLabel();
    }

    private void initStoreComboBox(){
        for (Map.Entry<Integer,Store> store:SDM.getStores().entrySet()) {
            StoreComboBox.getItems().add(store.getValue());
        }
    }
    private void initStoreProductsComboBox(){
        Store store = StoreComboBox.getValue();
        TriggerItemComboBox.getItems().removeAll();
        for (Map.Entry<Integer, StoreProduct> product:store.getProducts().entrySet()) {
            TriggerItemComboBox.getItems().add(product.getValue());
        }
    }

    private void initOperatorComboBox(){
        OperatorComboBox.getItems().add("Irrelevant");
        OperatorComboBox.getItems().add("All or nothing");
        OperatorComboBox.getItems().add("One of");
    }
    private void setSoldByLabel(){
        if(TriggerItemComboBox.getValue() != null){
            UnitOrKiloLabel.setText(TriggerItemComboBox.getValue().getProductCategory().toString());
        }
    }
    private void setPromptAmountLabel(){
        if(TriggerItemComboBox.getValue() != null){
            if(TriggerItemComboBox.getValue().getProductCategory().toString().equalsIgnoreCase("Quantity")){
                PromptAmountDouble = 1;
            }else{
                PromptAmountDouble = 0.5;
            }
            PromptAmountLabel.setText(Double.toString(PromptAmountDouble));
        }
    }

    public void setSDM(SuperDuperMarket sdm) {
        this.SDM = sdm;
    }
}
