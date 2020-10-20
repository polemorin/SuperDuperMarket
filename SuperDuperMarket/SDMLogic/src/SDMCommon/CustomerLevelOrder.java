package SDMCommon;

import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.List;

import ProductTypes.*;

public class CustomerLevelOrder {
    private List<StoreLevelOrder> orders;
    private double totalProductPrice;
    private int totalProductTypeAmount;
    private int totalProductPurchased;
    private final double deliveryPrice;
    private final LocalDate date;
    protected static int OrderIDGenerator = 1000;
    private final int OrderID;
    private final Point orderLocation;

    public CustomerLevelOrder(List<StoreLevelOrder> storeOrders,Point orderLocation){
       orders = storeOrders;
       totalProductPrice = storeOrders.stream().mapToDouble(StoreLevelOrder::getTotalProductsPrice).sum();
       totalProductTypeAmount = storeOrders.stream().mapToInt(StoreLevelOrder::getAmountOfProductTypes).sum();
       totalProductPurchased = storeOrders.stream().mapToInt(StoreLevelOrder::getAmountOfProducts).sum();
       deliveryPrice = storeOrders.stream().mapToDouble(StoreLevelOrder::getDeliveryPrice).sum();
       date = storeOrders.get(0).getDate();
       OrderID = OrderIDGenerator++;
       this.orderLocation = orderLocation;
    }

    public CustomerLevelOrder(List<StoreLevelOrder> storeOrders, int orderID,Point orderLocation){
        orders = storeOrders;
        totalProductPrice = storeOrders.stream().mapToDouble(StoreLevelOrder::getTotalProductsPrice).sum();
        totalProductTypeAmount = storeOrders.stream().mapToInt(StoreLevelOrder::getAmountOfProductTypes).sum();
        totalProductPurchased = storeOrders.stream().mapToInt(StoreLevelOrder::getAmountOfProducts).sum();
        deliveryPrice = storeOrders.stream().mapToDouble(StoreLevelOrder::getDeliveryPrice).sum();
        date = storeOrders.get(0).getDate();
        OrderID = orderID;
        if(OrderIDGenerator <= OrderID){
            OrderIDGenerator = OrderID;
        }
        this.orderLocation = orderLocation;
    }

    public void updatePrices(){
        double totalProductPriceFromStore = 0;
        int totalAmountOfProducts = 0;
        Map<Integer,Product> uniqProductList = new HashMap<>();
        for (StoreLevelOrder storeLevelOrder: orders) {
            totalProductPriceFromStore+= storeLevelOrder.getTotalProductsPrice();
            totalAmountOfProducts+=storeLevelOrder.getAmountOfProducts();
            for (SoldProduct soldProduct: storeLevelOrder.getSoldProducts()) {
                if(!uniqProductList.containsKey(soldProduct.getProductID())){
                    uniqProductList.put(soldProduct.getProductID(),soldProduct);
                }
            }
            if(storeLevelOrder.getProductSoldOnSale() != null) {
                for (SaleProduct saleProduct : storeLevelOrder.getProductSoldOnSale()) {
                    if (!uniqProductList.containsKey(saleProduct.getProductID())) {
                        uniqProductList.put(saleProduct.getProductID(), saleProduct);
                    }
                }
            }
        }
        this.totalProductTypeAmount = uniqProductList.size();
        this.totalProductPrice = totalProductPriceFromStore;
        this.totalProductPurchased = totalAmountOfProducts;
    }
    public static int getNextOrderID(){
        return OrderIDGenerator;
    }
    public CustomerLevelOrder(CustomerLevelOrder other){
        this.OrderID = other.OrderID;
        this.date = other.date;
        this.deliveryPrice = other.deliveryPrice;
        this.totalProductPrice = other.totalProductPrice;
        this.totalProductTypeAmount = other.totalProductTypeAmount;
        this.totalProductPurchased = other.totalProductPurchased;
        this.orders = new ArrayList<StoreLevelOrder>(other.orders.size());
        for (StoreLevelOrder itr:other.orders) {
            this.orders.add(new StoreLevelOrder(itr));
        }
        this.orderLocation = other.orderLocation;
    }

    public List<String> getCustomerLevelOrderStringListToFile(){
        List<String> orderDetails = new ArrayList<String>();

        String deliverPrice = String.format("%.2f",(Double)(deliveryPrice));
        orderDetails.add(((Integer)(orders.size())).toString());
        orderDetails.add(((Double)(totalProductPrice)).toString());
        orderDetails.add(((Integer)(totalProductTypeAmount)).toString());
        orderDetails.add(deliverPrice);
        orderDetails.add(new SimpleDateFormat("dd/MM-HH:mm").format(date));
        orderDetails.add(((Integer)(OrderID)).toString());
        List<String> storeStrList;
        for (StoreLevelOrder order : orders) {
            storeStrList = order.getStoreStringListToFile();
            orderDetails.addAll(storeStrList);
        }
        orderDetails.add("CustomerDone");
        return orderDetails;
    }

    public static CustomerLevelOrder getCustomerLevelOrderFromTxtFile(List<String> orderDetails)throws ParseException {

        int productFieldAmount = SoldProduct.class.getDeclaredFields().length+
                PricedProduct.class.getDeclaredFields().length+
                Product.class.getDeclaredFields().length;

        int storeLevelOrderFieldAmount = StoreLevelOrder.class.getDeclaredFields().length - 2;
        int amountOfStoresInOrder = Integer.parseInt(orderDetails.get(0));
        double totalProductPrice = Double.parseDouble(orderDetails.get(1));
        int amountOfProductTypes = Integer.parseInt(orderDetails.get(2));
        double deliveryPrice = Double.parseDouble(orderDetails.get(3));
        Date orderDate=new SimpleDateFormat("dd/MM-HH:mm").parse(orderDetails.get(4));
        int orderIDFromFile = Integer.parseInt(orderDetails.get(5));

        int nextStoreAmountOfProducts = Integer.parseInt(orderDetails.get(6));

        List<StoreLevelOrder> storeOrdersToAdd = new ArrayList<StoreLevelOrder>();
        List<String> storeOrderDetails = new ArrayList<String>();

        int counterNextLineToRead = 6;

        for(int i = 0; i < amountOfStoresInOrder; i++){
            for(int j = 0; j < storeLevelOrderFieldAmount+(productFieldAmount*nextStoreAmountOfProducts) ; j++){
                storeOrderDetails.add(orderDetails.get(counterNextLineToRead++));
            }
            storeOrdersToAdd.add(StoreLevelOrder.getStoreLevelOrderFromTxtFile(storeOrderDetails));
            storeOrderDetails.clear();
            if(i<amountOfStoresInOrder-1){
            nextStoreAmountOfProducts = Integer.parseInt(orderDetails.get(counterNextLineToRead));
            }
        }
        return null;
    }

    public boolean isProductSoldInCustomerLevelOrder(Product product){
        for (StoreLevelOrder storeOrder:orders) {
            if(storeOrder.isProductSoldInStoreLevelOrder(product))
                return true;
        }
        return false;
    }
    public List<StoreLevelOrder> getOrders() {
        return orders;
    }

    public double getTotalProductPrice() {
        return totalProductPrice;
    }

    public int getTotalProductTypeAmount() {
        return totalProductTypeAmount;
    }

    public int getTotalProductPurchased() {
        return totalProductPurchased;
    }

    public double getDeliveryPrice() {
        return deliveryPrice;
    }

    public LocalDate getDate() {
        return date;
    }
    public int getOrderID() {
        return OrderID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomerLevelOrder that = (CustomerLevelOrder) o;
        return OrderID == that.OrderID;
    }

    @Override
    public int hashCode() {
        return 31 * 17 + OrderID;
    }
    @Override
    public String toString(){
        return Integer.toString(this.OrderID);
    }
    public double amountOfTimesSoldInOrder(Product product) {
        double sumAmountSoldInOrder = 0;
        for (StoreLevelOrder order:orders) {
            if(order.isProductSoldInStoreLevelOrder(product))
            {
                sumAmountSoldInOrder += order.getAmountSoldInOrder(product);
            }
        }
        return sumAmountSoldInOrder;
    }


}
