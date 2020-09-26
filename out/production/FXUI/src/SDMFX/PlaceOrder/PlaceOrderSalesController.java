package SDMFX.PlaceOrder;
import SDMCommon.*;
import ProductTypes.*;
import SDMSale.Offer;
import SDMSale.Sale;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlaceOrderSalesController
{

    private MarketArea sdm;
    private User customer;
    private Store store;
    private LocalDate deliveryDate;
    private Map<Integer,Double> productsByIdAndAmount;
    private Map<Integer,Double> timesLeftToUseSalePerProduct;
    private Stage mainStage;
    private List<Sale> sales;
    private SimpleBooleanProperty isSaleOfferAvailable;
    private List<SaleProduct> saleProductsList;
    private CustomerLevelOrder customerLevelOrder;


    @FXML
    private ListView<Sale> SalesListView;
    private Map<Integer, SaleProduct> chosenSaleProducts;
    @FXML
    private ComboBox<Offer> OfferComboBox;

    @FXML
    private Button AddOfferButton;

    @FXML
    private TableView SaleTableView;

    @FXML
    private TableColumn<SaleProduct, String> SaleNameColumn;

    @FXML
    private TableColumn<SaleProduct, Integer> SaleIDColumn;

    @FXML
    private TableColumn<SaleProduct, String> SaleAmountColumn;

    @FXML
    private TableColumn<SaleProduct, Double> SalePriceColumn;

    @FXML
    private TableColumn<SaleProduct, Double> SaleTotalPriceColumn;

    @FXML
    private TableColumn<SaleProduct, String> SaleSaleNameColumn;

    @FXML
    private TableView<SoldProduct> CartTableView;

    @FXML
    private TableColumn<SoldProduct, String> CartNameColumn;

    @FXML
    private TableColumn<SoldProduct, Integer> CartIDColumn;

    @FXML
    private TableColumn<SoldProduct, String> CartAmountColumn;

    @FXML
    private TableColumn<SoldProduct, Double> CartPriceColumn;

    @FXML
    private TableColumn<SoldProduct, Double> CartTotalPriceColumn;

    @FXML
    private Button GoToCheckOutButton;

    @FXML
    private Label MessegeLabel;
    private String currentStyle;
    private boolean doesUserWantAnimation;

    @FXML
    void AddOfferButtonAction(ActionEvent event) {
        SaleProduct saleProduct = null;
        Offer offer = OfferComboBox.getValue();
        ProductCategory category;
        int storeID;
        boolean found = false;
        String saleName;
        Sale s = SalesListView.selectionModelProperty().getValue().getSelectedItems().get(0);
        int productID = s.getIfYouBuy().getItemID();
        Double salesLeft = timesLeftToUseSalePerProduct.get(productID) - s.getIfYouBuy().getQuantity();
        timesLeftToUseSalePerProduct.remove(productID);
        timesLeftToUseSalePerProduct.put(productID,salesLeft);

        if(OfferComboBox.getItems().size() == 0){//ALL-OR-NOTHING
           Sale allOrNahSale = SalesListView.selectionModelProperty().getValue().getSelectedItems().get(0);
            for (Offer offerList:allOrNahSale.getThenYouGet().getOffers()) {
                category = sdm.getProducts().get(offerList.getItemID()).getProductCategory();
                storeID = sdm.getStoreIDFromSaleName(SalesListView.selectionModelProperty().getValue().getSelectedItems().get(0).getName());
                saleName = SalesListView.selectionModelProperty().getValue().getSelectedItems().get(0).getName();
                saleProduct = new SaleProduct(offerList.getItemID(),offerList.getProductName(),category,offerList.getForAdditional(),storeID,saleName,offerList.getQuantity());
                for (SaleProduct product:saleProductsList) {
                    if(saleProduct.getProductID()==product.getProductID() && saleName.compareToIgnoreCase(product.getSaleName())==0){
                        found = true;
                        product.setAmountBought(product.getAmountBought() + offerList.getQuantity() );
                    }
                }
                if(!found) {
                    saleProductsList.add(saleProduct);
                }
            }
        }
        else{
            category = sdm.getProducts().get(offer.getItemID()).getProductCategory();
            storeID = sdm.getStoreIDFromSaleName(SalesListView.selectionModelProperty().getValue().getSelectedItems().get(0).getName());
            saleName = SalesListView.selectionModelProperty().getValue().getSelectedItems().get(0).getName();

            saleProduct = new SaleProduct(offer.getItemID(),offer.getProductName(),category,offer.getForAdditional(),storeID,saleName,offer.getQuantity());
            found =false;
            for (SaleProduct product:saleProductsList) {
                if(saleProduct.getProductID()==product.getProductID() && saleName.compareToIgnoreCase(product.getSaleName())==0){
                    found = true;
                    product.setAmountBought(product.getAmountBought() + offer.getQuantity() );
                }
            }
            if(!found){
                saleProductsList.add(saleProduct);
            }

        }
        if(!isUserAbleToGetDiscountAgain(s,productID)){
            isSaleOfferAvailable.setValue(false);
            List<Sale> salesToDelete = new ArrayList<>();
            for (Sale sale:SalesListView.getItems()) {
                if(sale.getIfYouBuy().getItemID() == productID){
                    if(!isUserAbleToGetDiscountAgain(sale,productID)){
                        salesToDelete.add(sale);
                    }
                }
            }
            for (Sale saleToRemove: salesToDelete) {
                SalesListView.getItems().remove(saleToRemove);
            }
            SalesListView.refresh();
            OfferComboBox.getItems().clear();
        }

        setSaleTableView();
    }

    private boolean isUserAbleToGetDiscountAgain(Sale s, int productID) {
        return timesLeftToUseSalePerProduct.get(productID) >= s.getIfYouBuy().getQuantity();
    }


    private void setSaleTableView(){
        SaleTableView.getItems().removeAll();
        final ObservableList<SaleProduct> dataOfItems = FXCollections.observableList(saleProductsList);
        SaleTableView.setItems(dataOfItems);
        SaleTableView.refresh();

    }

    @FXML
    void GoToCheckOutAction(ActionEvent event) {
        updateCustomerLevelOrderWithSales();
        customerLevelOrder.updatePrices();
        Stage PlaceOrderSummeryStage = null;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(PlaceOrderSummaryController.class.getResource("PlaceOrderSummary.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            if(currentStyle.compareTo("None") != 0){
                scene.getStylesheets().add(currentStyle);
            }
            PlaceOrderSummeryStage = new Stage();
            PlaceOrderSummeryStage.setTitle("Place Order Summery");
            PlaceOrderSummeryStage.setScene(scene);
            PlaceOrderSummeryStage.initOwner(mainStage);
            PlaceOrderSummeryStage.initModality(Modality.WINDOW_MODAL);
            PlaceOrderSummaryController placeOrderSummaryController = fxmlLoader.getController();
            placeOrderSummaryController.setData(sdm, customerLevelOrder);
            placeOrderSummaryController.setStyle(currentStyle);
            placeOrderSummaryController.doAnimation(doesUserWantAnimation);
        } catch (IOException e) {
            e.printStackTrace();
        }
        PlaceOrderSummeryStage.show();
        Stage s = (Stage) (GoToCheckOutButton.getScene().getWindow());
        s.close();

    }

    private void updateCustomerLevelOrderWithSales() {
        if(store == null){
            for (SaleProduct saleProduct:saleProductsList) {
                for (StoreLevelOrder storeLevelOrder:customerLevelOrder.getOrders()) {
                    if(saleProduct.getStoreID()==storeLevelOrder.getStoreID()){
                        storeLevelOrder.setProductSoldOnSale(saleProduct);
                    }
                }
            }
        }
        else{
            customerLevelOrder.getOrders().get(0).setProductSoldOnSale(saleProductsList);
        }
    }

    @FXML
    void OfferComboBoxAction(ActionEvent event) {
        if(OfferComboBox.getValue() != null) {
            isSaleOfferAvailable.setValue(true);
        }

    }
    @FXML
    private void initialize(){
        isSaleOfferAvailable = new SimpleBooleanProperty(false);
        AddOfferButton.disableProperty().bind(isSaleOfferAvailable.not());
        chosenSaleProducts = new HashMap<>();
        saleProductsList = new ArrayList<>();

        SaleNameColumn.setCellValueFactory(new PropertyValueFactory<SaleProduct, String>("productName"));
        SaleAmountColumn.setCellValueFactory(new PropertyValueFactory<SaleProduct, String>("amountBought"));
        SaleIDColumn.setCellValueFactory(new PropertyValueFactory<SaleProduct, Integer>("productID"));
        SalePriceColumn.setCellValueFactory(new PropertyValueFactory<SaleProduct, Double>("price"));
        SaleTotalPriceColumn.setCellValueFactory(new PropertyValueFactory<SaleProduct, Double>("totalPrice"));
        SaleSaleNameColumn.setCellValueFactory(new PropertyValueFactory<SaleProduct, String>("saleName"));

        CartNameColumn.setCellValueFactory(new PropertyValueFactory<SoldProduct, String>("productName"));
        CartIDColumn.setCellValueFactory(new PropertyValueFactory<SoldProduct, Integer>("productID"));
        CartAmountColumn.setCellValueFactory(new PropertyValueFactory<SoldProduct, String>("amountSoldInOrder"));
        CartPriceColumn.setCellValueFactory(new PropertyValueFactory<SoldProduct, Double>("price"));
        CartTotalPriceColumn.setCellValueFactory(new PropertyValueFactory<SoldProduct, Double>("totalPrice"));
    }
    @FXML
    void SalesListViewAction(MouseEvent event) {
        if(SalesListView.getItems().size() != 0) {
            if (SalesListView.getItems().get(0) != null) {
                if (SalesListView.selectionModelProperty().getValue().getSelectedItems().get(0) != null) {
                    OfferComboBox.setPromptText("");
                    isSaleOfferAvailable.setValue(false);
                    OfferComboBox.getItems().clear();
                    OfferComboBox.disableProperty().setValue(false);
                    Sale sale = SalesListView.selectionModelProperty().getValue().getSelectedItems().get(0);
                    List<Offer> offers = SalesListView.selectionModelProperty().getValue().getSelectedItems().get(0).getThenYouGet().getOffers();
                    if (sale.getThenYouGet().getOperator().compareToIgnoreCase("ALL-OR-NOTHING") == 0) {
                        OfferComboBox.setPromptText("You get all the items in sale!");
                        OfferComboBox.disableProperty().setValue(true);
                        isSaleOfferAvailable.setValue(true);
                    } else {
                        for (Offer offer : offers) {
                            OfferComboBox.getItems().add(offer);
                        }
                    }
                }
            }
        }
    }

    public void setData(MarketArea sdm, User customer, Store store, LocalDate deliveryDate, Map<Integer, Double> productsByIdAndAmount, Stage mainStage) {
        this.sdm = sdm;
        this.store = store;
        this.deliveryDate = deliveryDate;
        this.customer = customer;
        this.productsByIdAndAmount = productsByIdAndAmount;
        this.mainStage = mainStage;
        timesLeftToUseSalePerProduct = new HashMap<>();
        for (Map.Entry<Integer,Double> product:productsByIdAndAmount.entrySet()) {
            timesLeftToUseSalePerProduct.put(product.getKey(),product.getValue());
        }
        initTableViews();
    }

    private void initTableViews() {
        initSaleView();
        initCartTable();
    }

    private void initCartTable() {
        List<SoldProduct> productsToAdd = new ArrayList<>();
        for (StoreLevelOrder storeLevelOrder:customerLevelOrder.getOrders()) {
            productsToAdd.addAll(storeLevelOrder.getSoldProducts());
        }
        final ObservableList<SoldProduct> dataOfItems = FXCollections.observableList(productsToAdd);
        CartTableView.setItems(dataOfItems);
        CartTableView.refresh();

    }

    private void initSaleView() {
        if(store == null){
            sales = new ArrayList<>();
            Store tempStore;
            customerLevelOrder = sdm.createCheapestOrder(productsByIdAndAmount,customer.getID(),deliveryDate,customer.getLocation());
            for (StoreLevelOrder storeLevelOrder:customerLevelOrder.getOrders()) {
                tempStore = sdm.getStores().get(storeLevelOrder.getStoreID());
                sales.addAll(tempStore.getMySales(productsByIdAndAmount));
            }
        }
        else{
            createCustomerLevelStaticOrder();
            sales = store.getMySales(productsByIdAndAmount);
        }
        for (Sale sale: sales) {
            SalesListView.getItems().add(sale);
        }
        if(sales.isEmpty()){
            MessegeLabel.setText("No sales this time!");
        }
    }

    private void createCustomerLevelStaticOrder() {
        StoreLevelOrder storeOrder = new StoreLevelOrder(store,customer.getID(),deliveryDate,customer.getLocation(), CustomerLevelOrder.getNextOrderID());
        StoreProduct storeProduct;
        for (Map.Entry<Integer,Double> cartProductID:productsByIdAndAmount.entrySet()) {
            storeProduct = store.getProducts().get(cartProductID.getKey());
            storeOrder.addProductToOrder(new SoldProduct(storeProduct,cartProductID.getValue()));
        }
        List<StoreLevelOrder> storeLevelOrderList = new ArrayList<>();
        storeLevelOrderList.add(storeOrder);
        customerLevelOrder = new CustomerLevelOrder(storeLevelOrderList);
    }

    public void setStyle(String currentStyle) {
        this.currentStyle = currentStyle;
    }

    public void doAnimation(boolean doesUserWantAnimation) {
        this.doesUserWantAnimation  =doesUserWantAnimation;
    }
}
