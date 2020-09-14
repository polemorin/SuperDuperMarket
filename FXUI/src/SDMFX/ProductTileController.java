package SDMFX;

import ProductTypes.Product;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class ProductTileController {

    private Product product = null;
    private String category = null;
    private double changeAmountByButtons;
    private double currentAmount = 0;
    @FXML
    private Label NameLabel;

    @FXML
    private Label PriceStaticLabel;

    @FXML
    private Button PlusButton;

    @FXML
    private Button MinusButton;

    @FXML
    private Label SoldByLabel;

    @FXML
    private Label AmountLabel;

    @FXML
    private Label PriceLabel;


    public void setData(Product product, Double price) {
        this.product = product;
        this.category = product.getProductCategory().toString().equals("Quantity") ? "Units" : "Kilo";
        changeAmountByButtons = category.equals("Units") ? 1 : 0.5;
        NameLabel.setText(product.getProductName());
        SoldByLabel.setText(category);
        if (price == null) {
            PriceLabel.visibleProperty().setValue(false);
            PriceStaticLabel.visibleProperty().setValue(false);
        } else {
            PriceLabel.setText(String.format("%.1f", price));
        }
        if (category.compareToIgnoreCase("Units") == 0) {
            AmountLabel.setText("0");
        }
        else{
            AmountLabel.setText("0.0");
        }
    }

    @FXML
    void MinusButtonAction(ActionEvent event) {
        if(currentAmount > 0){
            currentAmount -= changeAmountByButtons;
        }
        if(category.equals("Kilo")){
            AmountLabel.setText(String.format("%.1f",currentAmount));
        }
        else {
            AmountLabel.setText(Integer.toString((int)(currentAmount)));
        }
    }

    @FXML
    void PlusButtonAction(ActionEvent event) {
        currentAmount += changeAmountByButtons;
        if(category.equals("Kilo")){
            AmountLabel.setText(String.format("%.1f",currentAmount));
        }
        else {
            AmountLabel.setText(Integer.toString((int)(currentAmount)));
        }
    }

    public Product getProduct() {
        return product;
    }

    public Double getAmount(){
        return currentAmount;
    }
}
