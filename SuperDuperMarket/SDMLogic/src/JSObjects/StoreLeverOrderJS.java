package JSObjects;

import java.awt.*;

public class StoreLeverOrderJS {
    private regProduct[] regProducts;
    private SaleProductJS[] saleProducts;
    private int storeID;
    private double deliveryPrice;
    private double PPK;
    private int productTypeAmount;
    private double totalPrice;
    private Point customerLocation;
    private String storeName;
    private int orderID;
    private Point location;
    private double distanceFromCustomer;

    public StoreLeverOrderJS(regProduct[] regProducts, String storeName) {
        this.regProducts = regProducts;
        this.storeName = storeName;
    }

    public SaleProductJS[] getSaleProducts() {
        return saleProducts;
    }

    public void setSaleProducts(SaleProductJS[] saleProducts) {
        this.saleProducts = saleProducts;
    }

    public int getStoreID() {
        return storeID;
    }

    public void setStoreID(int storeID) {
        this.storeID = storeID;
    }

    public double getDeliveryPrice() {
        return deliveryPrice;
    }

    public void setDeliveryPrice(double deliveryPrice) {
        this.deliveryPrice = deliveryPrice;
    }

    public double getPPK() {
        return PPK;
    }

    public void setPPK(double PPK) {
        this.PPK = PPK;
    }

    public int getProductTypeAmount() {
        return productTypeAmount;
    }

    public void setProductTypeAmount(int productTypeAmount) {
        this.productTypeAmount = productTypeAmount;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Point getCustomerLocation() {
        return customerLocation;
    }

    public void setCustomerLocation(Point customerLocation) {
        this.customerLocation = customerLocation;
    }

    public String getStoreName() {
        return storeName;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
    }

    public double getDistanceFromCustomer() {
        return distanceFromCustomer;
    }

    public void setDistanceFromCustomer(double distanceFromCustomer) {
        this.distanceFromCustomer = distanceFromCustomer;
    }

    public regProduct[] getRegProducts() {
        return regProducts;
    }
}