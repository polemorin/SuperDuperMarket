package JSObjects;

import java.awt.*;
import java.time.LocalDate;

public class CustomerLevelOrderJS {
    private String orderType;
    private StoreLevelOrderJS[] storeOrders;
    private Point customerLocation;
    private String date;
    private String customerName;
    private int customerID;

    public CustomerLevelOrderJS(String orderType, StoreLevelOrderJS[] storeOrders, Point location, String date) {
        this.orderType = orderType;
        this.storeOrders = storeOrders;
        this.customerLocation = location;
        this.date = date;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setCustomerLocation(Point customerLocation) {
        this.customerLocation = customerLocation;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public StoreLevelOrderJS[] getStoreOrders() {
        return storeOrders;
    }

    public Point getLocation() {
        return customerLocation;
    }

    public String getDate() {
        return date;
    }

    public Point getCustomerLocation() {
        return customerLocation;
    }

    public String getCustomerName() {
        return customerName;
    }

    public int getCustomerID() {
        return customerID;
    }
}
