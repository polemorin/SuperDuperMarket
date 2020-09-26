package SDMFX.Add.AddStore;
import ProductTypes.Product;
import ProductTypes.StoreProduct;
import SDMCommon.*;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
public class AddStoreController {

    private MarketArea SDM;
    private boolean isValidStoreName;
    private boolean isValidStoreID;
    private boolean isValidPPK;
    private boolean isValidLocation;
    private boolean isValidPrice;
    private List<StoreProduct> storeProducts;

    private SimpleBooleanProperty canStoreBeAddedToSDM;

    @FXML
    private TextField NameTextField;

    @FXML
    private TextField IDTextField;

    @FXML
    private ComboBox<Integer> LocationXcomboBox;

    @FXML
    private ComboBox<Integer> LocationYcomboBox;

    @FXML
    private TextField PPKTextField;

    @FXML
    private ComboBox<Product> ProductsComboBox;

    @FXML
    private Button FinishButton;

    @FXML
    private TextField PriceTextField;

    @FXML
    private Button AddProductButton;

    @FXML
    private Button CancelButton;

    @FXML
    private Label IDMsgLabel;

    @FXML
    private Label PPKMsgLabel;

    @FXML
    private Label PriceMsgLabel;

    @FXML
    private Label GeneralMsgLabel;

    @FXML
    private Label LocationMsgLabel;

    @FXML
    private void initialize(){
      canStoreBeAddedToSDM = new SimpleBooleanProperty(false);
      FinishButton.disableProperty().bind(canStoreBeAddedToSDM.not());
      AddProductButton.disableProperty().setValue(true);
        storeProducts = new ArrayList<>();

      for (int i = 1 ; i <= 50 ; i++){
          LocationXcomboBox.getItems().add(i);
          LocationYcomboBox.getItems().add(i);
      }

    }

    @FXML
    void AddProductButtonAction(ActionEvent event) {

        if(ProductsComboBox.getItems().size() != 0){
            PPKTextField.disableProperty().setValue(true);
            IDTextField.disableProperty().setValue(true);
            NameTextField.disableProperty().setValue(true);
            LocationXcomboBox.disableProperty().setValue(true);
            LocationYcomboBox.disableProperty().setValue(true);
            if(ProductsComboBox.getValue()!=null){
            StoreProduct newStoreProduct = new StoreProduct(ProductsComboBox.getValue(),
                    Double.parseDouble(PriceTextField.getText()),
                    Integer.parseInt(IDTextField.getText()));
            storeProducts.add(newStoreProduct);

                Product currentProductInComboBox = ProductsComboBox.getValue();
                ProductsComboBox.getItems().remove(currentProductInComboBox);
                ProductsComboBox.setValue(null);
                canStoreBeAddedToSDM.setValue(true);
            }

        }

    }

    @FXML
    void CancelButtonAction(ActionEvent event) {
        Stage s = (Stage)CancelButton.getScene().getWindow();
        s.close();
    }

    @FXML
    void FinishButtonAction(ActionEvent event) {
        //String name, Point location, int ID, Map<Integer,StoreProduct> productSet, double PPK
        int storeID = Integer.parseInt(IDTextField.getText());
        Point storeLocation = new Point(LocationXcomboBox.getValue(),LocationYcomboBox.getValue());
        String storeName = NameTextField.getText();
        double ppk = Double.parseDouble(PPKTextField.getText());
        Map<Integer,StoreProduct> products = new HashMap<>();
        for (StoreProduct sp:storeProducts) {
            products.put(sp.getProductID(),sp);
        }

        Store newStore = new Store(storeName,storeLocation,storeID,products,ppk);
        SDM.getStores().put(storeID,newStore);

        Stage s = (Stage)CancelButton.getScene().getWindow();
        s.close();
    }

    @FXML
    void IDTextFieldAction(KeyEvent event) {
        String ID = IDTextField.getText();
        int idAsInt;
        if(!ID.isEmpty()){
            try{
                idAsInt = Integer.parseInt(ID);
                if(SDM.getStores().containsKey(idAsInt)){
                    IDMsgLabel.setText("This ID number is not available");
                    isValidStoreID = false;
                }else{
                    IDMsgLabel.setText("");
                    isValidStoreID = true;
                }

            }catch(Exception e){
                IDMsgLabel.setText("ID can contain numbers only");
                isValidStoreID = false;
            }
        }
        else{
            IDMsgLabel.setText("");
            isValidStoreID = false;
        }
        AddProductButton.disableProperty().setValue(!isValidAllStoreDetails());

    }

    @FXML
    void LocationXcomboBoxAction(ActionEvent event) {
        isValidLocation = isValidLocation();
        AddProductButton.disableProperty().setValue(!isValidAllStoreDetails());

    }

    @FXML
    void LocationYcomboBoxAction(ActionEvent event) {
        isValidLocation = isValidLocation();
        AddProductButton.disableProperty().setValue(!isValidAllStoreDetails());

    }

    @FXML
    void NameTextFieldAction(KeyEvent event) {
        isValidStoreName = !NameTextField.getText().isEmpty();
    }

    @FXML
    void PPKTextFieldAction(KeyEvent event) {
        String PPK = PPKTextField.getText();
        double ppkAsDouble;
        if(!PPK.isEmpty()){
            try{
                ppkAsDouble = Double.parseDouble(PPK);
                if(ppkAsDouble >= 0){
                    PPKMsgLabel.setText("");
                    isValidPPK = true;
                }else{
                    PPKMsgLabel.setText("PPK must be a positive number");
                    isValidPPK = false;
                }

            }catch(Exception e){
                PPKMsgLabel.setText("This is not a number");
                isValidPPK = false;
            }
        }
        else{
            PPKMsgLabel.setText("");
            isValidPPK = false;
        }
        AddProductButton.disableProperty().setValue(!isValidAllStoreDetails());
    }

    @FXML
    void PriceTextFieldAction(KeyEvent event) {
        String price = PriceTextField.getText();
        double priceAsDouble;
        if(!price.isEmpty()){
            try{
                priceAsDouble = Double.parseDouble(price);
                if(priceAsDouble > 0){
                    PriceMsgLabel.setText("");
                    isValidPrice = true;
                }else{
                    PriceMsgLabel.setText("Price must be positive number");
                    isValidPrice = false;
                }

            }catch(Exception e){
                PriceMsgLabel.setText("Price must be a number");
                isValidPrice = false;
            }
        }
        else{
            PriceMsgLabel.setText("");
            isValidPrice = false;
        }

        AddProductButton.disableProperty().setValue(!isValidAllStoreDetails());

    }

    @FXML
    void ProductsComboBoxAction(ActionEvent event) {

    }

    private boolean isValidLocation(){
        Integer x = LocationXcomboBox.getValue();
        Integer y = LocationYcomboBox.getValue();
        boolean availableLocation = false;
        if(x != null && y != null){
            availableLocation =  SDM.isAvailableLocationInSDM(new Point(x,y));
            if(!availableLocation){
                LocationMsgLabel.setText("This location is unavailable");
            }
            else{
                LocationMsgLabel.setText("");
            }
        }
        return availableLocation;
    }


    public void setSDM(MarketArea sdm) {
        SDM = sdm;

        for (Map.Entry<Integer, Product> product:SDM.getProducts().entrySet()) {
            ProductsComboBox.getItems().add(product.getValue());
        }
    }

    private boolean isValidAllStoreDetails(){
        return isValidStoreID && isValidPPK && isValidStoreName && isValidLocation && isValidPrice && ProductsComboBox.getValue()!=null;
    }


}
