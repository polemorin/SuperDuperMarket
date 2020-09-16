package SDMFX.Add.AddStore;
import ProductTypes.Product;
import SDMCommon.*;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

import java.awt.*;
import java.util.Map;

public class AddStoreController {

    private SuperDuperMarket SDM;
    private boolean isProductAddedToStore;
    private boolean isValidStoreName;
    private boolean isValidStoreID;
    private boolean isValidPPK;
    private boolean isValidLocation;
    private boolean isValidPrice;

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

        for (Map.Entry<Integer, Product> product:SDM.getProducts().entrySet()) {
            ProductsComboBox.getItems().add(product.getValue());
        }
        for (int i = 1 ; i <= 50 ; i++){
            LocationXcomboBox.getItems().add(i);
            LocationYcomboBox.getItems().add(i);
        }

    }

    @FXML
    void AddProductButtonAction(ActionEvent event) {

    }

    @FXML
    void CancelButtonAction(ActionEvent event) {

    }

    @FXML
    void FinishButtonAction(ActionEvent event) {

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

    }

    @FXML
    void LocationXcomboBoxAction(ActionEvent event) {
        isValidLocation = isValidLocation();
    }

    @FXML
    void LocationYcomboBoxAction(ActionEvent event) {
        isValidLocation = isValidLocation();
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
    }

    @FXML
    void PriceTextFieldAction(KeyEvent event) {

    }

    @FXML
    void ProductsComboBoxAction(ActionEvent event) {

    }

    private boolean isValidLocation(){
        Integer x = LocationXcomboBox.getValue();
        Integer y = LocationYcomboBox.getValue();
        if(x != null && y != null){
            return SDM.isAvailableLocationInSDM(new Point(x,y));
        }
        else{
            return false;
        }
    }


}
