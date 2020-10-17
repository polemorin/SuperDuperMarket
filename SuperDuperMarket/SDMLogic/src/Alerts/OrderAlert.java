package Alerts;

public class OrderAlert implements IUserAlert{
    int orderID;
    String customerName;
    String storeName;
    int amountOfProductsInOrder;
    double totalProductCost;
    double DeliveryCost;

    public OrderAlert(int orderID, String customerName,String storeName, int amountOfProductsInOrder, double totalProductCost, double deliveryCost) {
        this.orderID = orderID;
        this.customerName = customerName;
        this.amountOfProductsInOrder = amountOfProductsInOrder;
        this.totalProductCost = totalProductCost;
        DeliveryCost = deliveryCost;
        this.storeName = storeName;
    }

    public int getOrderID() {
        return orderID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public int getAmountOfProductsInOrder() {
        return amountOfProductsInOrder;
    }

    public double getTotalProductCost() {
        return totalProductCost;
    }

    public double getDeliveryCost() {
        return DeliveryCost;
    }

    @Override
    public String AlertText() {
        String text;
        text = "Store "+storeName+"just received an order by "+customerName+" order ID: "+orderID+"/n" +
                "amount of products: " + amountOfProductsInOrder+" product cost:" +totalProductCost+ "delivery cost: "+DeliveryCost;
        return text;
    }
}
