package SDMFX;

import ProductTypes.Product;
import ProductTypes.ProductCategory;
import ProductTypes.StoreProduct;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.util.Map;

public class AddProductController {

    private SuperDuperMarket SDM;
    private SimpleBooleanProperty canProductBeAdded;
    Product sdmNewProduct;
    private boolean isProductIDValid;
    private boolean isStoreSelected;
    private boolean isSoldBySelected;
    private boolean isProductNameSelected;
    private boolean isPriceSelected;
    private double productPrice;
    private int productID;
    @FXML
    private RadioButton WeightRadioButton;

    @FXML
    private ToggleGroup SoldBy;

    @FXML
    private RadioButton QuantityRadioButton;

    @FXML
    private Button BackButton;

    @FXML
    private Button AddProductButton;

    @FXML
    private TextField ProductNameTextField;

    @FXML
    private ComboBox<Store> StoreComboBox;

    @FXML
    private TextField ProductIdTextField;

    @FXML
    private Label ProductIDMsgLabel;

    @FXML
    private TextField PriceTextField;

    @FXML
    private Label PriceMsgLabel;

    @FXML
    private Label GeneralMsgLabel;

    @FXML
    private void initialize(){
        canProductBeAdded = new SimpleBooleanProperty(false);
        AddProductButton.disableProperty().bind(canProductBeAdded.not());
        isProductIDValid = false;
        isStoreSelected = false;
        isSoldBySelected = false;
        isProductNameSelected = false;
        isPriceSelected = false;
    }
    @FXML
    void RadioButtonAction(ActionEvent event) {
        isSoldBySelected = SoldBy.selectedToggleProperty().getValue().isSelected();
        canProductBeAdded();
    }

    @FXML
    void AddProductButtonAction(ActionEvent event) {
        ProductIdTextField.disableProperty().setValue(true);
        WeightRadioButton.disableProperty().setValue(true);
        QuantityRadioButton.disableProperty().setValue(true);
        ProductNameTextField.disableProperty().setValue(true);


        if(sdmNewProduct == null){
            String name = ProductNameTextField.getText();
            ProductCategory category;
            if(QuantityRadioButton.isArmed()){
                category = ProductCategory.Quantity;
            }
            else{
                category = ProductCategory.Weight;
            }
            sdmNewProduct = new Product(productID,name,category);
            SDM.getProducts().put(productID,sdmNewProduct);
        }
        Store store = StoreComboBox.getValue();
        if(store != null){
            StoreProduct product = new StoreProduct(sdmNewProduct,productPrice,store.getID());
            store.addNewProductToStore(product);
            StoreComboBox.getItems().remove(store);

        }
        checkIfStoreComboBoxIsEmpty();

    }

    private void checkIfStoreComboBoxIsEmpty() {
        if(StoreComboBox.getItems().size() == 0){
            GeneralMsgLabel.setText("Product was added to all stores");
            StoreComboBox.disableProperty().setValue(true);
            PriceTextField.disableProperty().setValue(true);
            AddProductButton.disableProperty().unbind();
            AddProductButton.disableProperty().setValue(true);

        }
    }

    @FXML
    void BackButtonAction(ActionEvent event) {
        Stage s = (Stage)BackButton.getScene().getWindow();
        s.close();
    }

    @FXML
    void PriceTextFieldAction(KeyEvent event) {
        String price = PriceTextField.getText();


        if(!price.isEmpty()){
            try{
                productPrice = Double.parseDouble(price);

                if(productPrice > 0){
                    isPriceSelected = true;
                    PriceMsgLabel.setText("");

                }

                if(productPrice <= 0 ){
                    isPriceSelected = false;
                    PriceMsgLabel.setText("Price must be positive number");
                }

            }catch(NumberFormatException e){
                isPriceSelected = false;
                PriceMsgLabel.setText("price must be a number");
            }
        }else{
            isPriceSelected = false;
            PriceMsgLabel.setText("");
        }
        canProductBeAdded();
    }

    @FXML
    void ProductIDTextFiledAction(KeyEvent event) {
        String productIDstr = ProductIdTextField.getText();
        if(!productIDstr.isEmpty()){
            try{
                productID = Integer.parseInt(productIDstr);
                if(SDM.getProducts().containsKey(productID)){
                    ProductIDMsgLabel.setText("Product ID already in market");
                    isProductIDValid = false;
                }
                else if(productID > 0){
                    isProductIDValid = true;
                    ProductIDMsgLabel.setText("");
                }

               if(productID <= 0 ){
                   isProductIDValid = false;
                   ProductIDMsgLabel.setText("Product ID must be positive number");
               }

            }catch(NumberFormatException e){
                isProductIDValid = false;
                ProductIDMsgLabel.setText("Product ID must an integer number");
            }
        }else{
            isProductIDValid = false;
            ProductIDMsgLabel.setText("");
        }
        canProductBeAdded();

    }

    private void canProductBeAdded() {
        canProductBeAdded.setValue(isPriceSelected &&
                isStoreSelected && isProductIDValid
                && isProductNameSelected && isSoldBySelected );
    }

    @FXML
    void ProductNameTextFieldAction(KeyEvent event) {
        if(ProductNameTextField.getText().isEmpty()){
            isProductNameSelected = false;
        }else{
            isProductNameSelected =true;
        }
        canProductBeAdded();
    }

    @FXML
    void StoreComboBoxAction(ActionEvent event) {
        isStoreSelected = StoreComboBox.getValue() != null;
        canProductBeAdded();
    }

    public void setSDM(SuperDuperMarket sdm) {
        SDM = sdm;
        for (Map.Entry<Integer, Store> store:SDM.getStores().entrySet()) {
            StoreComboBox.getItems().add(store.getValue());
        }
    }
}
