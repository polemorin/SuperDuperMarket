package JSObjects;

import java.awt.*;

public class customerLevelOrderJS {
    private String orderType;
    private StoreLeverOrderJS[] storeOrders;
    private Point customerLocation;
    private String date;

    public customerLevelOrderJS(String orderType, StoreLeverOrderJS[] storeOrders, Point location, String date) {
        this.orderType = orderType;
        this.storeOrders = storeOrders;
        this.customerLocation = location;
        this.date = date;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setCustomerLocation(Point customerLocation) {
        this.customerLocation = customerLocation;
    }

    public StoreLeverOrderJS[] getStoreOrders() {
        return storeOrders;
    }

    public Point getLocation() {
        return customerLocation;
    }

    public String getDate() {
        return date;
    }
}
