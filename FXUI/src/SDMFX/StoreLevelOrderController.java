package SDMFX;

import ProductTypes.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class StoreLevelOrderController  {
    List<SoldProduct> productBought;
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
    private TableView<SoldProduct> ProductsTableView;

    @FXML
    private TableColumn<SoldProduct, String> NameColumn;

    @FXML
    private TableColumn<SoldProduct, Integer> IDColumn;

    @FXML
    private TableColumn<SoldProduct, String> SoldByColumn;

    @FXML
    private TableColumn<SoldProduct, Double> AmountColumn;

    @FXML
    private TableColumn<SoldProduct, Double> PriceColumn;

    @FXML
    private TableColumn<SoldProduct, Double> TotalColumn;

    @FXML
    private TableColumn<SoldProduct, String> PurchasedOnSaleColumn;


    @FXML
    private void initialize(){
        NameColumn.setCellValueFactory(new PropertyValueFactory<>("productName"));
        IDColumn.setCellValueFactory(new PropertyValueFactory<>("productID"));
        SoldByColumn.setCellValueFactory(new PropertyValueFactory<SoldProduct,String>("categoryString"));
        AmountColumn.setCellValueFactory(new PropertyValueFactory<>("amountSoldInOrder"));
        PriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        TotalColumn.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        PurchasedOnSaleColumn.setCellValueFactory(new PropertyValueFactory<>("saleName"));
    }
    public void setData(StoreLevelOrder storeLevelOrder, SuperDuperMarket SDM) {
        productBought = new ArrayList<>();
        productBought.addAll(storeLevelOrder.getSoldProducts());
        if(storeLevelOrder.getProductSoldOnSale() != null) {
            productBought.addAll(storeLevelOrder.getProductSoldOnSale());
        }
        final ObservableList<SoldProduct> dataOfItems = FXCollections.observableList(productBought);
        ProductsTableView.setItems(dataOfItems);
        ProductsTableView.refresh();

        Point customerLocation = SDM.getUsers().get(storeLevelOrder.getCustomerID()).getLocation();
        Point storeLocation = SDM.getStores().get(storeLevelOrder.getStoreID()).getLocation();
        String storeName = SDM.getStores().get(storeLevelOrder.getStoreID()).getName();
        double ppk = SDM.getStores().get(storeLevelOrder.getStoreID()).getDeliveryPPK();

        DeliveryPriceLabel.setText(String.format("%.2f", storeLevelOrder.getDeliveryPrice()));
        DistanceLabel.setText(String.format("%.2f",storeLevelOrder.getDistanceFromCustomerToStore(storeLocation,customerLocation)));
        IDLabel.setText(Integer.toString(storeLevelOrder.getStoreID()));
        NameLabel.setText(storeName);
        PPKLabel.setText(Double.toString(ppk));
    }

}
