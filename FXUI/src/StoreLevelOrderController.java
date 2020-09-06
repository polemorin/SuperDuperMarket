import ProductTypes.SoldProduct;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class StoreLevelOrderController {

    @FXML
    private Label StoreDetailsLable;

    @FXML
    private TableView<StoreLevelOrder> StoreDetailsTable;

    @FXML
    private TableColumn<?, ?> StoreIDColumn;

    @FXML
    private TableColumn<?, ?> StoreNameColumn;

    @FXML
    private TableColumn<?, ?> PPKColumn;

    @FXML
    private TableColumn<?, ?> DistanceColumn;

    @FXML
    private TableColumn<?, ?> DeliveryCostColumn;

    @FXML
    private Label ProductLable;

    @FXML
    private TableView<SoldProduct> ProductTable;

    @FXML
    private TableColumn<?, ?> ProductIDColumn;

    @FXML
    private TableColumn<?, ?> ProductNameColumn;

    @FXML
    private TableColumn<?, ?> ProductCategoryColumn;

    @FXML
    private TableColumn<?, ?> ProductAmountColumn;

    @FXML
    private TableColumn<?, ?> ProductPricePerUnitColumn;

    @FXML
    private TableColumn<?, ?> ProductTotalPriceColumn;

    @FXML
    private TableColumn<?, ?> ProductWasSaleColumn;

}
