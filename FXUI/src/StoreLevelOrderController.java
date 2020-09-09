
import ProductTypes.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class StoreLevelOrderController  {
    List<Product> productBought;
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
    private TableView<Product> ProductsTableView;

    @FXML
    private TableColumn<Product, String> NameColumn;

    @FXML
    private TableColumn<Product, Integer> IDColumn;

    @FXML
    private TableColumn<Product, ProductCategory> SoldByColumn;

    @FXML
    private TableColumn<Product, Double> AmountColumn;

    @FXML
    private TableColumn<Product, Double> PriceColumn;

    @FXML
    private TableColumn<Product, Double> TotalColumn;

    @FXML
    private TableColumn<Product, String> PurchasedOnSaleColumn;

    @FXML
    private void initialize(){
        NameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        IDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        SoldByColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        AmountColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
    }
    public void setData(StoreLevelOrder storeLevelOrder) {
        productBought.addAll(storeLevelOrder.getSoldProducts());
        productBought.addAll(storeLevelOrder.getProductSoldOnSale());

        final ObservableList<Product> dataOfItems = FXCollections.observableList(productBought);
        ProductsTableView.setItems(dataOfItems);
        ProductsTableView.refresh();
    }

}
