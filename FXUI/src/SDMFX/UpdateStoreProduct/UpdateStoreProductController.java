package SDMFX.UpdateStoreProduct;
import SDMCommon.*;
import ProductTypes.Product;
import ProductTypes.StoreProduct;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.util.Map;

public class UpdateStoreProductController {

    private MarketArea SDM;
    private Map<Integer, Store> stores;
    private SimpleBooleanProperty isStoreChosen;
    private SimpleBooleanProperty isProductUpdateChosen;
    private SimpleBooleanProperty isProductRemoveChosen;
    private SimpleBooleanProperty isProductAddChosen;
    private SimpleBooleanProperty isAddPriceValid;
    private SimpleBooleanProperty isUpdatePriceValid;
    @FXML
    private Button BackButton;

    @FXML
    private ComboBox<Store> ChooseStoreComboBox;

    @FXML
    private ComboBox<StoreProduct> RemoveProductComboBox;

    @FXML
    private Button RemoveProductButton;

    @FXML
    private ComboBox<Product> AddProductComboBox;

    @FXML
    private Button AddProductButton;

    @FXML
    private ComboBox<StoreProduct> UpdatePriceComboBox;

    @FXML
    private Button UpdatePriceButton;

    @FXML
    private TextField newPriceTextBox;

    @FXML
    private Label ResultLable;

    @FXML
    private Label SetPriceLable;

    @FXML
    private TextField SetPriceAddTextBox;

    public UpdateStoreProductController(){
        isStoreChosen = new SimpleBooleanProperty();
        isStoreChosen.setValue(false);
        isProductUpdateChosen = new SimpleBooleanProperty(false);
        isProductAddChosen = new SimpleBooleanProperty(false);
        isProductRemoveChosen = new SimpleBooleanProperty(false);
        isUpdatePriceValid = new SimpleBooleanProperty(false);
        isAddPriceValid = new SimpleBooleanProperty(false);
    }
    @FXML
    private void initialize(){
        RemoveProductComboBox.disableProperty().bind(isStoreChosen.not());
        AddProductComboBox.disableProperty().bind((isStoreChosen.not()));
        UpdatePriceComboBox.disableProperty().bind((isStoreChosen.not()));

        AddProductButton.disableProperty().bind((isAddPriceValid.not()));
        RemoveProductButton.disableProperty().bind((isProductRemoveChosen.not()));
        UpdatePriceButton.disableProperty().bind((isUpdatePriceValid.not()));

        newPriceTextBox.disableProperty().bind(isProductUpdateChosen.not());
        SetPriceAddTextBox.disableProperty().bind((isProductAddChosen.not()));

    }
    @FXML
    void AddProductButtonAction(ActionEvent event) {
        int chosenProductID = AddProductComboBox.getValue().getProductID();
        int chosenStoreID = ChooseStoreComboBox.getValue().getID();
        Double price = Double.parseDouble(SetPriceAddTextBox.getText());
        SDM.addProductToStore(chosenProductID,chosenStoreID,price);
        SetPriceAddTextBox.setText("");
        isAddPriceValid.setValue(false);
        isProductAddChosen.setValue(false);
        ResultLable.setText(AddProductComboBox.getValue().getProductName()+" added successfully, price: " + String.format("%.2f",price));
        updateComboBoxes();
        RemoveProductComboBox.setPromptText("");
    }

    @FXML
    void AddProductComboBox(ActionEvent event) {
        if(AddProductComboBox.getValue() != null){
            isProductAddChosen.setValue(true);
        }
    }

    @FXML
    void PriceAddTextBoxAction(KeyEvent event) {
        try {
            if(SetPriceAddTextBox.getText().equals("")){
                ResultLable.setText("");
                isAddPriceValid.setValue(false);
            }
            else {
                if (Double.parseDouble(SetPriceAddTextBox.getText()) > 0) {
                    isAddPriceValid.setValue(true);
                } else {
                    isAddPriceValid.setValue(false);
                    ResultLable.setText("Updated price must be a positive number.");
                }
            }
        }catch(NumberFormatException e){
            isAddPriceValid.setValue(false);
            ResultLable.setText("Price field accepts numbers only.");
        }catch (Exception e){
            isAddPriceValid.setValue(false);
            ResultLable.setText("check at PriceUpdateTextBoxAction()");
        }
    }

    @FXML
    void PriceUpdateTextBoxAction(KeyEvent event) {
        try {
            if(newPriceTextBox.getText().equals("")){
                ResultLable.setText("");
                isUpdatePriceValid.setValue(false);
            }
            else {
                if (Double.parseDouble(newPriceTextBox.getText()) > 0) {
                    isUpdatePriceValid.setValue(true);
                } else {
                    isUpdatePriceValid.setValue(false);
                    ResultLable.setText("Updated price must be a positive number.");
                }
            }
        }catch(NumberFormatException e){
            isUpdatePriceValid.setValue(false);
            ResultLable.setText("Price field accepts numbers only.");
        }catch (Exception e){
            isUpdatePriceValid.setValue(false);
            ResultLable.setText("check at PriceUpdateTextBoxAction()");
        }
    }

    @FXML
    void BackButtonAction(ActionEvent event) {
        onClose();
        Stage s = (Stage)(BackButton.getScene().getWindow());
        s.close();
    }

    @FXML
    void RemoveProductButtonAction(ActionEvent event) {
        int storeID = ChooseStoreComboBox.getValue().getID();
        int productID = RemoveProductComboBox.getValue().getProductID();
        StoreProduct chosenStoreProduct = RemoveProductComboBox.getValue();
        Store store = ChooseStoreComboBox.getValue();
        SDM.removeProductFromStore(productID,storeID);
        ResultLable.setText(RemoveProductComboBox.getValue().getProductName() + " was removed from "
                + ChooseStoreComboBox.getValue().getName());
        RemoveProductComboBox.setValue(null);
        updateComboBoxes();
        isProductRemoveChosen.setValue(false);
        if(store.isProductPartOfStoreSale(chosenStoreProduct)){
            store.removeSaleByStoreProduct(chosenStoreProduct);
            ResultLable.setText(ResultLable.getText() + ". Sale was removed.");
        }
    }

    @FXML
    void RemoveProductComboBoxAction(ActionEvent event) {
        if(RemoveProductComboBox.getValue() != null){
            isProductRemoveChosen.setValue(true);
        }
    }

    @FXML
    void StoreComboBoxAction(ActionEvent event) {
       if(ChooseStoreComboBox.getValue()!= null){
           RemoveProductComboBox.setPromptText("Choose product");
           AddProductComboBox.setPromptText("Choose product");

           ResultLable.setText("");

           isUpdatePriceValid.setValue(false);
           isAddPriceValid.setValue(false);
           isProductUpdateChosen.setValue(false);
           isProductAddChosen.setValue(false);
           SetPriceAddTextBox.setText("");
           newPriceTextBox.setText("");

           isStoreChosen.setValue(true);
           updateComboBoxes();
       }
       else{
           isStoreChosen.setValue(false);
       }

    }

    @FXML
    void UpdatePriceButtonAction(ActionEvent event) {
        int chosenProductID = UpdatePriceComboBox.getValue().getProductID();
        double price = Double.parseDouble(newPriceTextBox.getText());
        ChooseStoreComboBox.getValue().updateStoreProduct(chosenProductID,price);
        ResultLable.setText(UpdatePriceComboBox.getValue().getProductName() + " price was updated, new price: "+ String.format("%.2f",price));
        newPriceTextBox.setText("");
        isProductUpdateChosen.setValue(false);
        isUpdatePriceValid.setValue(false);
        UpdatePriceComboBox.setValue(null);
    }

    @FXML
    void UpdatePriceComboBoxAction(ActionEvent event) {
        if(UpdatePriceComboBox.getValue() != null){
            isProductUpdateChosen.setValue(true);
        }

    }

    public void setSDM(MarketArea SDM){
        this.SDM = SDM;
        stores = SDM.getStores();
        updateStoreComboBox();
    }

    private void updateComboBoxes() {
        updateProductToRemoveAndPriceComboBox();
        updateProductToAddComboBox();
    }

    private void updateProductToRemoveAndPriceComboBox() {
        RemoveProductComboBox.getItems().clear();
        UpdatePriceComboBox.getItems().clear();
        for (Map.Entry<Integer,StoreProduct> product: ChooseStoreComboBox.getValue().getProducts().entrySet()) {
            UpdatePriceComboBox.getItems().add(product.getValue());
            if(SDM.countHowManyStoresSellProduct(product.getValue().getProductID()) != 1) {
                RemoveProductComboBox.getItems().add(product.getValue());
            }
        }
        if(RemoveProductComboBox.getItems().size() == 1){
            RemoveProductComboBox.getItems().clear();
            RemoveProductComboBox.setPromptText("No items to remove.");
            isProductRemoveChosen.setValue(false);
        }
    }

    private void updateStoreComboBox() {
        for (Map.Entry<Integer, Store> store:stores.entrySet()) {
            ChooseStoreComboBox.getItems().add(store.getValue());
        }
    }
    private void updateProductToAddComboBox() {
        AddProductComboBox.getItems().clear();
        boolean productAdded = false;
        for (Map.Entry<Integer, Product> product : SDM.getProducts().entrySet()) {
            if (!ChooseStoreComboBox.getValue().doesStoreSellProduct(product.getKey())) {
                AddProductComboBox.getItems().add(product.getValue());
                productAdded = true;
            }
        }
        if(!productAdded){
            AddProductComboBox.setPromptText("No more items");
        }
    }

    public void onClose() {
        ChooseStoreComboBox.setValue(null);
        isStoreChosen.setValue(false);
        isProductAddChosen.setValue(false);
        isProductUpdateChosen.setValue(false);
        isAddPriceValid.setValue(false);
        isUpdatePriceValid.setValue(false);
        isProductRemoveChosen.setValue(false);


        RemoveProductComboBox.setValue(null);
        AddProductComboBox.setValue(null);
        UpdatePriceComboBox.setValue(null);

        SetPriceAddTextBox.setText("");
        newPriceTextBox.setText("");
        ResultLable.setText("");
    }
}