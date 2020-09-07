
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class StoreLevelOrderController  {

    @FXML
    private Label NameLabel;

    @FXML
    private Label IDLabel;

    @FXML
    private Label PPKLabel;

    @FXML
    private Label DistanceLabel;

    @FXML
    private Label DeliveryPriceLabel;

    @FXML
    private TableView<?> ProductsTabelView;

    @FXML
    private TableColumn<?, ?> NameColumn;

    @FXML
    private TableColumn<?, ?> IDColumn;

    @FXML
    private TableColumn<?, ?> SoldByColumn;

    @FXML
    private TableColumn<?, ?> AmountCoulmn;

    @FXML
    private TableColumn<?, ?> PriceColumn;

    @FXML
    private TableColumn<?, ?> TotalColumn;

    @FXML
    private TableColumn<?, ?> PurchasedOnSaleColumn;

}
