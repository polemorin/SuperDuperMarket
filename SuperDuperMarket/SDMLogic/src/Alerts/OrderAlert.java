package Alerts;

public class OrderAlert implements IUserAlert{
    int orderID;
    String customerName;
    String storeName;
    int amountOfProductsInOrder;
    double totalProductCost;
    double DeliveryCost;
    String alertText;

    public OrderAlert(int orderID, String customerName,String storeName, int amountOfProductsInOrder, double totalProductCost, double deliveryCost) {
        this.orderID = orderID;
        this.customerName = customerName;
        this.amountOfProductsInOrder = amountOfProductsInOrder;
        this.totalProductCost = totalProductCost;
        DeliveryCost = deliveryCost;
        this.storeName = storeName;
        alertText = this.toString();
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
    public String toString() {
        String text;
        text = "Store "+storeName+" just received an order by "+customerName+" order ID: "+orderID+" " +
                "amount of products: " + amountOfProductsInOrder+" product cost:" +String.format("%.2f",totalProductCost)+ "$ delivery cost: "+String.format("%.2f",DeliveryCost)+"$";
        return text;
    }
}
