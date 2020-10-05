package SDMCommon;

import ProductTypes.ProductCategory;
import ProductTypes.SoldProduct;

import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Customer extends User {

    private List<CustomerLevelOrder> orderHistory;

    public Customer(String userName) {
        super(userName);
        orderHistory = new ArrayList<>();
        addtestOrder();
    }

    public List<CustomerLevelOrder> getOrderHistory() {
        return orderHistory;
    }
    //    public StoreLevelOrder(Integer orderID, List<SoldProduct> soldProducts, int amountOfProducts, double totalProductsPrice, int storeID, int customerID, double deliveryPrice, LocalDate date, String storeName,int customerLevelID) {
    private void addtestOrder(){
        List<SoldProduct> mysoldProducts = new ArrayList<>();
        mysoldProducts.add(new SoldProduct(12,"a", ProductCategory.Quantity,1,1,1,1));
        List<StoreLevelOrder> myorders = new ArrayList<>();
        myorders.add(new StoreLevelOrder(1,mysoldProducts,1,1.0,1,1,1,LocalDate.of(12,12,21),"myStore",1));
        CustomerLevelOrder testOrder;
        testOrder = new CustomerLevelOrder(myorders,new Point(1,1));
        orderHistory.add(testOrder);
    }
}
