import com.sun.xml.internal.bind.v2.model.core.ID;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import ProductTypes.*;

public class CustomerLevelOrder {



    private List<StoreLevelOrder> orders;
    private final double totalProductPrice;
    private final int totalProductTypeAmount;
    private final int totalProductPurchased;
    private final double deliveryPrice;
    private final Date date;
    private static int OrderIDGenerator = 1000;
    private final int OrderID;

    public CustomerLevelOrder(List<StoreLevelOrder> storeOrders){
       orders = storeOrders;
       totalProductPrice = storeOrders.stream().mapToDouble(StoreLevelOrder::getTotalProductsPrice).sum();
       totalProductTypeAmount = storeOrders.stream().mapToInt(StoreLevelOrder::getAmountOfProductTypes).sum();
       totalProductPurchased = storeOrders.stream().mapToInt(StoreLevelOrder::getAmountOfProducts).sum();
       deliveryPrice = storeOrders.stream().mapToDouble(StoreLevelOrder::getDeliveryPrice).sum();
       date = storeOrders.get(0).getDate();
       OrderID = OrderIDGenerator++;
    }

    public CustomerLevelOrder(List<StoreLevelOrder> storeOrders,int orderID){
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
    }

    public CustomerLevelOrder(double totalProductPrice, int totalProductTypeAmount, int totalProductPurchased, double deliveryPrice, Date date) {
        this.totalProductPrice = totalProductPrice;
        this.totalProductTypeAmount = totalProductTypeAmount;
        this.totalProductPurchased = totalProductPurchased;
        this.deliveryPrice = deliveryPrice;
        this.date = date;
        OrderID = OrderIDGenerator++;
        orders = new ArrayList<StoreLevelOrder>();
    }
    public static int getNextOrderID(){
        return OrderIDGenerator + 1;
    }
    public CustomerLevelOrder(CustomerLevelOrder other){
        this.OrderID = other.OrderID;
        this.date = (Date) other.date.clone();
        this.deliveryPrice = other.deliveryPrice;
        this.totalProductPrice = other.totalProductPrice;
        this.totalProductTypeAmount = other.totalProductTypeAmount;
        this.totalProductPurchased = other.totalProductPurchased;
        this.orders = new ArrayList<StoreLevelOrder>(other.orders.size());
        for (StoreLevelOrder itr:other.orders) {
            this.orders.add(new StoreLevelOrder(itr));
        }
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
        return new CustomerLevelOrder(storeOrdersToAdd,orderIDFromFile);
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

    public Date getDate() {
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
