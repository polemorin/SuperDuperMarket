import ProductTypes.Product;
import ProductTypes.StoreProduct;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.Map;

public class UpdateStoreProductController {

    private SuperDuperMarket SDM;
    private Map<Integer,Store> stores;
    private SimpleBooleanProperty isStoreChosen;


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

    public UpdateStoreProductController(){
        isStoreChosen = new SimpleBooleanProperty();
        isStoreChosen.setValue(false);

    }
    @FXML
    private void initialize(){
        RemoveProductComboBox.disableProperty().bind(isStoreChosen.not());
        AddProductComboBox.disableProperty().bind((isStoreChosen.not()));
        UpdatePriceComboBox.disableProperty().bind((isStoreChosen.not()));

        AddProductButton.disableProperty().bind((isStoreChosen.not()));
        RemoveProductButton.disableProperty().bind((isStoreChosen.not()));
        UpdatePriceButton.disableProperty().bind((isStoreChosen.not()));

        newPriceTextBox.disableProperty().bind(isStoreChosen.not());

    }
    @FXML
    void AddProductButtonAction(ActionEvent event) {

    }

    @FXML
    void AddProductComboBox(ActionEvent event) {

    }

    @FXML
    void BackButtonAction(ActionEvent event) {
        ChooseStoreComboBox.setValue(null);
        isStoreChosen.setValue(false);

        RemoveProductComboBox.setValue(null);
        AddProductComboBox.setValue(null);
        UpdatePriceComboBox.setValue(null);
        newPriceTextBox.setText("");
        Stage s = (Stage)(BackButton.getScene().getWindow());
        s.close();
    }

    @FXML
    void RemoveProductButtonAction(ActionEvent event) {

    }

    @FXML
    void RemoveProductComboBoxAction(ActionEvent event) {

    }

    @FXML
    void StoreComboBoxAction(ActionEvent event) {
       if(ChooseStoreComboBox.getValue()!= null){
           isStoreChosen.setValue(true);
           updateComboBoxes();
       }
       else{
           isStoreChosen.setValue(false);
       }

    }

    @FXML
    void UpdatePriceButtonAction(ActionEvent event) {

    }

    @FXML
    void UpdatePriceComboBoxAction(ActionEvent event) {

    }

    public void setSDM(SuperDuperMarket SDM){
        this.SDM = SDM;
        stores = SDM.getStores();
        updateStoreComboBox();
    }

    private void updateComboBoxes() {
        updateProductToRemoveAndPriceComboBox();
        updateProductToAddComboBox();
    }

    private void updateProductToRemoveAndPriceComboBox() {
        for (Map.Entry<Integer,StoreProduct> product: ChooseStoreComboBox.getValue().getProducts().entrySet()) {
            RemoveProductComboBox.getItems().add(product.getValue());
            UpdatePriceComboBox.getItems().add(product.getValue());
        }
    }

    private void updateStoreComboBox() {
        for (Map.Entry<Integer,Store> store:stores.entrySet()) {
            ChooseStoreComboBox.getItems().add(store.getValue());
        }
    }
    private void updateProductToAddComboBox() {
        for (Map.Entry<Integer, Product> product : SDM.getProducts().entrySet()) {
            if (!ChooseStoreComboBox.getValue().doesStoreSellProduct(product.getKey())) {
                AddProductComboBox.getItems().add(product.getValue());
            }
        }
    }
}