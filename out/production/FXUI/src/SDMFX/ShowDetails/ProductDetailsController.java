package SDMFX.ShowDetails;
import SDMCommon.*;
import ProductTypes.Product;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.util.Map;

public class ProductDetailsController {
    private Map<Integer, Product> productMap;
    private MarketArea SDM;
    @FXML
    private Button BackButton;

    @FXML
    private ComboBox<Product> ProductComboBox;

    @FXML
    private Label NameLableShow;

    @FXML
    private Label IDLableShow;

    @FXML
    private Label CategoryLableShow;

    @FXML
    private Label SoldByStoresLable;

    @FXML
    private Label ProductPriceShowLable;

    @FXML
    private Label AmountOfTimesSoldLable;
    public void setSDM(MarketArea SDM){
        this.SDM = SDM;
    }

    public void setProductMap(Map<Integer, Product> productMap){
        this.productMap = productMap;
        updateProductComboBox();
    }

    private void updateProductComboBox() {
        for (Map.Entry<Integer, Product> product: productMap.entrySet()) {
            ProductComboBox.getItems().add(product.getValue());
        }
    }
    @FXML
    void BackButtonAction(ActionEvent event) {
        Stage s = (Stage)(BackButton.getScene().getWindow());
        onClose();
        s.close();
    }

    @FXML
    void ProductComboBoxAction(ActionEvent event) {
        Product chosenProduct = ProductComboBox.getValue();
        if(chosenProduct != null) {
            IDLableShow.setText(Integer.toString(chosenProduct.getProductID()));
            NameLableShow.setText(chosenProduct.getProductName());
            CategoryLableShow.setText(chosenProduct.getProductCategory().toString());
            SoldByStoresLable.setText(Integer.toString(SDM.countHowManyStoresSellProduct(chosenProduct.getProductID())) + " stores");
            ProductPriceShowLable.setText(String.format("%.2f", SDM.getAveragePriceForProduct(chosenProduct)));
            AmountOfTimesSoldLable.setText(String.format("%.2f", SDM.totalAmountSoldInMarket(chosenProduct)));
        }
    }
    public void onClose(){
        ProductComboBox.setValue(null);
        NameLableShow.setText("");
        IDLableShow.setText("");
        CategoryLableShow.setText("");
        SoldByStoresLable.setText("");
        ProductPriceShowLable.setText("");
        AmountOfTimesSoldLable.setText("");
    }

}
