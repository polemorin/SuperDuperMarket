package SDMFX.ShowDetails;

import ProductTypes.Product;
import ProductTypes.SoldProduct;
import SDMCommon.Store;
import SDMCommon.StoreLevelOrder;
import SDMCommon.SuperDuperMarket;
import SDMSale.Sale;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.Node;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StoreDetailsController {

    private SuperDuperMarket SDM;
    private SimpleBooleanProperty isStoreChosen;
    @FXML
    private TableView<Product> ProductTableView;

    @FXML
    private TableColumn<Product, String> ProductsNameColumn;

    @FXML
    private TableColumn<Product, Integer> ProductsIDColumn;

    @FXML
    private TableColumn<Product, String> ProductsSoldByColumn;

    @FXML
    private TableColumn<Product, Double> ProductsPriceColumn;

    @FXML
    private TableColumn<Product, Integer> ProductsAmountBoughtColumn;

    @FXML
    private ListView<Sale> SalesListView;

    @FXML
    private TableView<SoldProduct> OrderTableView;

    @FXML
    private TableColumn<SoldProduct, String> OrderNameColumn;

    @FXML
    private TableColumn<SoldProduct, Integer> OrderIDColumn;

    @FXML
    private TableColumn<SoldProduct, Integer> OrderAmountSoldColumn;

    @FXML
    private TableColumn<SoldProduct, Double> OrderPricePerUnitKiloColumn;

    @FXML
    private TableColumn<SoldProduct, Double> OrderTotalColumn;

    @FXML
    private TableColumn<SoldProduct, String> OrderPurchasedOnSaleColumn;

    @FXML
    private Button BackButton;

    @FXML
    private ComboBox<StoreLevelOrder> OrderComboBox;

    @FXML
    private Label OrderTypeAndIDLabel;

    @FXML
    private ComboBox<Store> StoreComboBox;

    @FXML
    private Label DateLabel;

    @FXML
    private Label AmountOfProductsLabel;

    @FXML
    private Label TotalProductPriceLabel;

    @FXML
    private Label DeliveryPriceLabel;

    @FXML
    private Label TotalPriceToPayLabel;
    @FXML
    private Label PPKLabel;

    @FXML
    private Label StoreIDLabel;

    @FXML
    void BackButtonAction(ActionEvent event) {
        Stage s = (Stage)BackButton.getScene().getWindow();
        s.close();
    }

    @FXML
    void OrderComboBoxAction(ActionEvent  event) {
        if(OrderComboBox.getValue() != null){
            setOrderTableView();
            setLabels();
        }
    }
    private void setLabels() {
        StoreLevelOrder order = OrderComboBox.getValue();
        DateLabel.setText(order.getDate().toString());
        AmountOfProductsLabel.setText(Integer.toString(order.getAmountOfProducts()));
        DeliveryPriceLabel.setText(String.format("%.2f",order.getDeliveryPrice()));
        TotalPriceToPayLabel.setText(String.format("%.2f",order.getTotalProductsPrice()+order.getDeliveryPrice()));
        TotalProductPriceLabel.setText(String.format("%.2f",order.getTotalProductsPrice()));
        PPKLabel.setText(Double.toString(SDM.getStores().get(order.getStoreID()).getDeliveryPPK()));
        StoreIDLabel.setText(Integer.toString(order.getStoreID()));
        if(SDM.isStoreLevelOrderPartOfDynamicOrder(order)){
            OrderTypeAndIDLabel.setText("Order was part of dynamic order, dynamic order ID: "+ order.getCustomerLevelOrderID());
        }
        else{
            OrderTypeAndIDLabel.setText("Order was part of static order, static order ID: "+order.getOrderID());
        }
    }

    private void setOrderTableView() {
        List<SoldProduct> productBought = new ArrayList<>(OrderComboBox.getValue().getSoldProducts());
        if(OrderComboBox.getValue().getProductSoldOnSale() != null) {
            productBought.addAll(OrderComboBox.getValue().getProductSoldOnSale());
        }
        final ObservableList<SoldProduct> dataOfItems = FXCollections.observableList(productBought);
        OrderTableView.setItems(dataOfItems);
        OrderTableView.refresh();
    }

    @FXML
    void StoreComboBoxAction(ActionEvent  event) {
       if(StoreComboBox.getValue() != null){
           setSaleListView();
           setProductTable();
           isStoreChosen.setValue(true);
           OrderComboBox.getItems().clear();
           setOrderComboBox();
           OrderTableView.getItems().clear();
           resetLabels();
       }

    }

    private void resetLabels() {
        OrderTypeAndIDLabel.setText("");
        TotalProductPriceLabel.setText("");
        TotalPriceToPayLabel.setText("");
        AmountOfProductsLabel.setText("");
        DateLabel.setText("");
        DeliveryPriceLabel.setText("");
    }

    private void setOrderComboBox() {
       Store store = StoreComboBox.getValue();
       OrderComboBox.getItems().clear();
       for (StoreLevelOrder storeLevelOrder:store.getStoreOrderHistory()) {
           OrderComboBox.getItems().add(storeLevelOrder);
       }
    }

    @FXML
    private void initialize(){
       isStoreChosen = new SimpleBooleanProperty(false);
       OrderComboBox.disableProperty().bind(isStoreChosen.not());
       ProductsNameColumn.setCellValueFactory(new PropertyValueFactory<Product,String>("Name"));
       ProductsPriceColumn.setCellValueFactory(new PropertyValueFactory<>("ProductPrice"));
       ProductsIDColumn.setCellValueFactory(new PropertyValueFactory<>("ProductID"));
       ProductsSoldByColumn.setCellValueFactory(new PropertyValueFactory<>("ProductCategoryString"));
       ProductsAmountBoughtColumn.setCellValueFactory(new PropertyValueFactory<>("totalAmountSoldInStore"));

       OrderNameColumn.setCellValueFactory(new PropertyValueFactory<>("productName"));
       OrderIDColumn.setCellValueFactory(new PropertyValueFactory<>("productID"));
       OrderAmountSoldColumn.setCellValueFactory(new PropertyValueFactory<>("amountSoldInOrder"));
       OrderPricePerUnitKiloColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
       OrderTotalColumn.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
       OrderPurchasedOnSaleColumn.setCellValueFactory(new PropertyValueFactory<>("saleName"));


    }
    private void setProductTable() {
        List<Product> storeProductsList = new ArrayList<>(StoreComboBox.getValue().getProducts().values());
        final ObservableList<Product> dataOfItems = FXCollections.observableList(storeProductsList);
        ProductTableView.setItems(dataOfItems);
        ProductTableView.refresh();
    }

    private void setSaleListView() {
        SalesListView.getItems().clear();
        for (Map.Entry<String,Sale> sale:StoreComboBox.getValue().getSales().entrySet()) {
            SalesListView.getItems().add(sale.getValue());
        }
    }

    public void setSDM(SuperDuperMarket sdm){
        SDM = sdm;
        initStoreComboBox();
    }

    private void initStoreComboBox() {
        for (Map.Entry<Integer,Store> store:SDM.getStores().entrySet()) {
            StoreComboBox.getItems().add(store.getValue());
        }
    }

}
