package JSObjects;

public class regProduct {
    private int productID;

    private double totalProductPrice;
    private double Amount;

    public regProduct(int productID, double amount) {
        this.productID = productID;
        Amount = amount;
    }

    public int getProductID() {
        return productID;
    }

    public double getTotalProductPrice() {
        return totalProductPrice;
    }

    public double getAmount() {
        return Amount;
    }

    public void setTotalProductPrice(double totalProductPrice) {
        this.totalProductPrice = totalProductPrice;
    }
}
