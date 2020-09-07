
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;

public class PlaceOrderSalesController
{

    @FXML
    private ListView<?> SalesListView;

    @FXML
    private ComboBox<?> OfferComboBox;

    @FXML
    private Button AddOfferButton;

    @FXML
    private TableView<?> SaleTableView;

    @FXML
    private TableColumn<?, ?> SaleNameColumn;

    @FXML
    private TableColumn<?, ?> SaleIDColumn;

    @FXML
    private TableColumn<?, ?> SaleAmountColumn;

    @FXML
    private TableColumn<?, ?> SalePriceColumn;

    @FXML
    private TableColumn<?, ?> SaleTotalPriceColumn;

    @FXML
    private TableColumn<?, ?> SaleSaleNameColumn;

    @FXML
    private TableView<?> CartTableView;

    @FXML
    private TableColumn<?, ?> CartNameColumn;

    @FXML
    private TableColumn<?, ?> CartIDColumn;

    @FXML
    private TableColumn<?, ?> CartAmountColumn;

    @FXML
    private TableColumn<?, ?> CartPriceColumn;

    @FXML
    private TableColumn<?, ?> CartTotalPriceColumn;

    @FXML
    private Button GoToCheckOutButton;

    @FXML
    private Label MessegeLabel;

    @FXML
    void AddOfferButtonAction(ActionEvent event) {

    }

    @FXML
    void GoToCheckOutAction(ActionEvent event) {

    }

    @FXML
    void OfferComboBoxAction(ActionEvent event) {

    }

    @FXML
    void SalesListViewAction(MouseEvent event) {

    }

}
