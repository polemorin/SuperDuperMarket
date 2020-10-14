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
    }

    public List<CustomerLevelOrder> getOrderHistory() {
        return orderHistory;
    }
}
