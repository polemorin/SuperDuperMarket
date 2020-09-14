package SDMFX;

import ProductTypes.SaleProduct;
import ProductTypes.StoreProduct;
import SDMSale.Offer;
import SDMSale.Sale;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AddSaleController {

    private SuperDuperMarket SDM;
    private SimpleBooleanProperty isSaleTriggerReady;
    private SimpleBooleanProperty isStoreChosenTrigger;
    private SimpleBooleanProperty isStoreProductChosenTrigger;
    private SimpleBooleanProperty isValidSaleName;
    private SimpleBooleanProperty isOperatorChosen;
    private double PromptAmountDouble;

    private  SimpleBooleanProperty isOfferProductChosen;
    private  SimpleBooleanProperty isSaleTriggerClicked;
    private SimpleBooleanProperty isOfferReady;
    private SimpleBooleanProperty isSaleReady;
    private double OfferAmount;

    private List<Offer> offerList;


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
    private ComboBox<StoreProduct> OfferItemsComboBox;

    @FXML
    private Label UnitOrKiloLabel;

    @FXML
    private Label OfferAmountLabel;

    @FXML
    private Button OfferAmountMinusButton;

    @FXML
    private Button OfferAmountPlusButton;

    @FXML
    private Label ForAdditionalPriceLabel;

    @FXML
    private Label OfferUnitOrKiloLabel;

    @FXML
    private TextField ForAdditionalPriceTextBox;

    @FXML
    private TableView<Offer> SaleTableView;

    @FXML
    private TableColumn<Offer, String> ProductNameTableView;

    @FXML
    private TableColumn<Offer, Double> ProductAmountTableView;

    @FXML
    private TableColumn<Offer, Double> ProductPriceTableView;

    @FXML
    private Button SetSaleButton;

    @FXML
    private Button BackButton;

    @FXML
    private Button AddSaleButton;
    @FXML
    private Label AddOfferMessageLabel;


    @FXML
    private void initialize(){
        isSaleTriggerReady = new SimpleBooleanProperty(false);
        isStoreChosenTrigger = new SimpleBooleanProperty(false);
        isStoreProductChosenTrigger = new SimpleBooleanProperty(false);
        isValidSaleName = new SimpleBooleanProperty(false);
        isOperatorChosen = new SimpleBooleanProperty(false);


        isOfferProductChosen = new SimpleBooleanProperty(false);
        isSaleTriggerClicked = new SimpleBooleanProperty(false);
        isOfferReady = new SimpleBooleanProperty(false);
        isSaleReady = new SimpleBooleanProperty(false);

        OfferItemsComboBox.disableProperty().bind(isSaleTriggerClicked.not());
        ForAdditionalPriceTextBox.disableProperty().bind(isOfferProductChosen.not());
        OfferAmountMinusButton.disableProperty().bind(isOfferProductChosen.not());
        OfferAmountPlusButton.disableProperty().bind(isOfferProductChosen.not());

        TriggerItemComboBox.disableProperty().bind(isStoreChosenTrigger.not());
        OperatorComboBox.disableProperty().bind(isStoreChosenTrigger.not());
        MinusPromptAmountButton.disableProperty().bind(isStoreProductChosenTrigger.not());
        PlusPromptAmountButton.disableProperty().bind(isStoreProductChosenTrigger.not());
        SetSaleButton.disableProperty().bind(isSaleTriggerReady.not());

        AddOfferButton.disableProperty().bind(isOfferReady.not());
        AddSaleButton.disableProperty().bind(isSaleReady.not());
        initOperatorComboBox();

        offerList = new ArrayList<>();

        ProductNameTableView.setCellValueFactory(new PropertyValueFactory<Offer, String>("productName"));
        ProductAmountTableView.setCellValueFactory(new PropertyValueFactory<Offer, Double>("quantity"));
        ProductPriceTableView.setCellValueFactory(new PropertyValueFactory<Offer, Double>("forAdditional"));
//
    }
    @FXML
    void AddOfferButtonAction(ActionEvent event) {
        Offer offerToAdd = new Offer(Double.parseDouble(OfferAmountLabel.getText()),OfferItemsComboBox.getValue().getProductID(),Double.parseDouble(ForAdditionalPriceTextBox.getText()),OfferItemsComboBox.getValue().getProductName());
        offerList.add(offerToAdd);
        UpdateOfferTableView();
        OfferItemsComboBox.setValue(null);
        OfferAmountLabel.setText("0.0");
        OfferUnitOrKiloLabel.setText("");
        ForAdditionalPriceTextBox.setText("");
        if(OperatorComboBox.getValue().equals("IRRELEVANT")){
            isSaleReady.setValue(true);
        }
        else if(SaleTableView.getItems().size() > 1){
            isSaleReady.setValue(true);
        }
    }

    private void UpdateOfferTableView() {
        SaleTableView.getItems().removeAll();
        final ObservableList<Offer> dataOfItems = FXCollections.observableList(offerList);
        SaleTableView.setItems(dataOfItems);
        SaleTableView.refresh();
        isOfferReady.setValue(false);
    }

    @FXML
    void AddSaleButtonAction(ActionEvent event) {
        Sale saleToAdd = new Sale(SaleNameTextField.getText(), TriggerItemComboBox.getValue(),Double.parseDouble(PromptAmountLabel.getText()),OperatorComboBox.getValue().toUpperCase(),offerList);
        SDM.getStores().get(StoreComboBox.getValue().getID()).addSale(saleToAdd);

        Stage s  = (Stage)AddSaleButton.getScene().getWindow();
        s.close();
    }

    @FXML
    void BackButtonAction(ActionEvent event) {
        Stage s = (Stage)BackButton.getScene().getWindow();
        s.close();
    }

    @FXML
    void ForAdditionalPriceTextBoxAction(KeyEvent event) {
        String price = ForAdditionalPriceTextBox.getText();

        if(!price.isEmpty()) {
            try {
                if (Double.parseDouble(price) < 0) {
                    ForAdditionalPriceLabel.setText("Price can not be negative number!");
                    isOfferReady.setValue(false);
                } else {
                    if(price.endsWith("d") || price.endsWith("f") || price.endsWith("D") || price.endsWith("F")){
                        throw new Exception("Price must be a number");
                    }
                    if(OperatorComboBox.getValue().equals("IRRELEVANT") && SaleTableView.getItems().size() == 1){
                        isOfferReady.setValue(false);
                        AddOfferMessageLabel.setText("cant add more then one item with Irrelevant operator.");
                    }else {
                        isOfferReady.setValue(true);
                        ForAdditionalPriceLabel.setText("");
                    }
                }
            } catch (Exception e) {
                ForAdditionalPriceLabel.setText("Price must be a number");
                isOfferReady.setValue(false);
            }
        }else{
            ForAdditionalPriceLabel.setText("");
            isOfferReady.setValue(false);
        }

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
        StoreProduct product = OfferItemsComboBox.getValue();
        if(product != null){
            if(product.getProductCategory().toString().equalsIgnoreCase("Quantity")&& OfferAmount > 1){
                OfferAmount--;
            }
            else if(product.getProductCategory().toString().equalsIgnoreCase("Weight")&& OfferAmount > 0.5){
                OfferAmount -= 0.5;
            }
            OfferAmountLabel.setText(Double.toString(OfferAmount));
        }
    }

    @FXML
    void OfferAmountPlusButtonAction(ActionEvent event) {
        StoreProduct product = OfferItemsComboBox.getValue();
        if(product != null){
            if(product.getProductCategory().toString().equalsIgnoreCase("Quantity")){
                OfferAmount++;
            }
            else{
                OfferAmount += 0.5;
            }
            OfferAmountLabel.setText(Double.toString(OfferAmount));


        }

    }

    @FXML
    void OfferItemsComboBoxAction(ActionEvent event) {
        if(OfferItemsComboBox.getValue() != null){
            isOfferProductChosen.setValue(true);
        }
        setOfferAmountLabel();
        setOfferSoldByLabel();
    }

    @FXML
    void OperatorComboBoxAction(ActionEvent event) {
        if(OperatorComboBox.getValue() != null){
            isOperatorChosen.setValue(true);
        }
        setIsSaleTriggerReady();
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
    void SaleNameTextBoxAction(KeyEvent event) {
        String saleName = SaleNameTextField.getText();
        if(!saleName.isEmpty()){
            if(!SDM.isSaleNameAlreadyInMarket(saleName)){
                isValidSaleName.setValue(true);
                SaleNameErrorLabel.setText("");
            }else{
                isValidSaleName.setValue(false);
                SaleNameErrorLabel.setText("Sale name taken!");
            }
        }else{
            isValidSaleName.setValue(false);
        }
        setIsSaleTriggerReady();

    }

    @FXML
    void SetSaleButtonAction(ActionEvent event) {
        isStoreChosenTrigger.setValue(false);
        StoreComboBox.disableProperty().setValue(true);
        SaleNameTextField.disableProperty().setValue(true);
        isStoreProductChosenTrigger.setValue(false);
        isSaleTriggerClicked.setValue(true);
        isOfferReady.setValue(false);
        isSaleReady.setValue(false);

        setOfferProductsComboBox();

        OfferUnitOrKiloLabel.setText("");
        OfferAmountLabel.setText("0.0");
        ForAdditionalPriceTextBox.setText("");
        ForAdditionalPriceLabel.setText("");
        SaleTableView.getItems().clear();
        offerList.clear();
    }

    @FXML
    void StoreComboBoxAction(ActionEvent event) {
        if(StoreComboBox.getValue() != null){
            isStoreChosenTrigger.setValue(true);
        }
        PromptAmountLabel.setText("0");
        UnitOrKiloLabel.setText("");
        setStoreProductsComboBox();
        setIsSaleTriggerReady();
    }

    @FXML
    void TriggerItemComboBoxAction(ActionEvent event) {
        if(StoreComboBox.getValue() != null){
            isStoreProductChosenTrigger.setValue(true);
        }
        setSoldByLabel();
        setPromptAmountLabel();
        setIsSaleTriggerReady();
    }

    private void initStoreComboBox(){
        for (Map.Entry<Integer, Store> store:SDM.getStores().entrySet()) {
            StoreComboBox.getItems().add(store.getValue());
        }
    }
    private void setStoreProductsComboBox(){
        Store store = StoreComboBox.getValue();
        TriggerItemComboBox.getItems().clear();
        for (Map.Entry<Integer, StoreProduct> product:store.getProducts().entrySet()) {
            TriggerItemComboBox.getItems().add(product.getValue());
        }
    }

    private void initOperatorComboBox(){
        OperatorComboBox.getItems().add("IRRELEVANT");
        OperatorComboBox.getItems().add("ALL-OR-NOTHING");
        OperatorComboBox.getItems().add("ONE-OF");
    }
    private void setSoldByLabel(){
        if(TriggerItemComboBox.getValue() != null){
            UnitOrKiloLabel.setText(TriggerItemComboBox.getValue().getProductCategory().toString());
        }
    }
    private void setOfferSoldByLabel(){
        if(OfferItemsComboBox.getValue() != null){
            OfferUnitOrKiloLabel.setText(OfferItemsComboBox.getValue().getProductCategory().toString());
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

    private void setOfferAmountLabel(){
        if(OfferItemsComboBox.getValue() != null){
            if(OfferItemsComboBox.getValue().getProductCategory().toString().equalsIgnoreCase("Quantity")){
                OfferAmount = 1;
            }else{
                OfferAmount = 0.5;
            }
            OfferAmountLabel.setText(Double.toString(OfferAmount));
        }
    }


    public void setSDM(SuperDuperMarket sdm) {
        this.SDM = sdm;
        initStoreComboBox();
    }

    private void setIsSaleTriggerReady(){
        isSaleTriggerReady.setValue(isStoreChosenTrigger.getValue() &&
                        isValidSaleName.getValue() &&
                        isStoreProductChosenTrigger.getValue() &&
                        isOperatorChosen.getValue());
    }

    private void setOfferProductsComboBox(){
        Store store = StoreComboBox.getValue();
        OfferItemsComboBox.getItems().clear();
        for (Map.Entry<Integer, StoreProduct> product:store.getProducts().entrySet()) {
            OfferItemsComboBox.getItems().add(product.getValue());
        }
    }
}
