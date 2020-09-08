
import ProductTypes.Product;
import ProductTypes.ProductCategory;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class ProductTileController {

    private Product product = null;
    private Double price = null;
    private String category = null;
    private Double changeAmountByButtons = null;
    private SimpleDoubleProperty amountLabelProperty = new SimpleDoubleProperty(0.0);

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


    public void setData(Product product, Double price){
        this.product = product;
        this.category = product.getProductCategory().toString().equals("Quantity") ? "Units" : "Kilo";
        this.price = price;
        changeAmountByButtons = category.equals("Units") ? 1 : 0.1;
        NameLabel.setText(product.getProductName());
        SoldByLabel.setText(category);
        if(price == null){
            PriceLabel.visibleProperty().setValue(false);
            PriceStaticLabel.visibleProperty().setValue(false);
        }
        else{
            PriceLabel.setText(String.format("%.2f",price));
        }

    }
    @FXML
    void initialize(){
        AmountLabel.textProperty().bind(amountLabelProperty.asString());
    }

    @FXML
    void MinusButtonAction(ActionEvent event) {
        if(amountLabelProperty.get() > 0.0){
            amountLabelProperty.set(amountLabelProperty.getValue() + changeAmountByButtons);
        }
    }

    @FXML
    void PlusButtonAction(ActionEvent event) {
        amountLabelProperty.set(amountLabelProperty.getValue() + changeAmountByButtons);
    }
    public void MinusAction(){
        if(amountLabelProperty.get() > 0.0){
            amountLabelProperty.set(amountLabelProperty.getValue() + changeAmountByButtons);
        }
    }
    public void PlusAction(){
        amountLabelProperty.set(amountLabelProperty.getValue() + changeAmountByButtons);
    }

}
