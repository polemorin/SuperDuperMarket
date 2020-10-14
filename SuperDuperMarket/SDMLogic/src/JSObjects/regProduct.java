package JSObjects;

public class regProduct {
    private int productID;
    private String productName;
    private double totalProductPrice;
    private double Amount;
    private String category;

    public regProduct(int productID, double amount) {
        this.productID = productID;
        Amount = amount;
    }

    public int getProductID() {
        return productID;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setProductName(String productName) {
        this.productName = productName;
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

    public String getProductName() {
        return productName;
    }

    public String getCategory() {
        return category;
    }
}
