
import ProductTypes.Product;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class ProductTileController {

    private Product product = null;
    private Double price = null;
    private String category = null;
    private Double changeAmountByButtons = null;
    private SimpleDoubleProperty amountLabelPropertyDouble = new SimpleDoubleProperty(0.0);
    private SimpleIntegerProperty amountLabelPropertyInt = new SimpleIntegerProperty(0);
    private double d = 0;
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
        changeAmountByButtons = category.equals("Units") ? 1 : 0.5;
        NameLabel.setText(product.getProductName());
        SoldByLabel.setText(category);
        if(category.compareToIgnoreCase("Kilo") == 0){
        AmountLabel.textProperty().bind(amountLabelPropertyDouble.asString());
        }
        else
        {
            AmountLabel.textProperty().bind(amountLabelPropertyInt.asString());
        }
        if(price == null){
            PriceLabel.visibleProperty().setValue(false);
            PriceStaticLabel.visibleProperty().setValue(false);
        }
        else{
            PriceLabel.setText(String.format("%.1f",price));
        }

    }

    @FXML
    void MinusButtonAction(ActionEvent event) {
        if(amountLabelPropertyDouble.get() > 0.0){
            d-=(changeAmountByButtons);
            amountLabelPropertyDouble.set(d);
        }
        System.out.println(amountLabelPropertyDouble);
    }

    @FXML
    void PlusButtonAction(ActionEvent event) {
        d+=(changeAmountByButtons);
        amountLabelPropertyDouble.set(d);
        System.out.println(amountLabelPropertyDouble);
    }

    public Product getProduct() {
        return product;
    }

    public Double getAmount(){
        return amountLabelPropertyDouble.getValue();
    }
}
