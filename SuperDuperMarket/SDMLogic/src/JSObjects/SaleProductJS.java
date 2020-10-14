package JSObjects;

public class SaleProductJS {
    private int productID;
    private double totalProductPrice;
    private double Amount;
    private String productName;
    private String saleName;
    private String category;

    public SaleProductJS(int productID, double totalProductPrice, double amount, String saleName) {
        this.productID = productID;
        this.totalProductPrice = totalProductPrice;
        Amount = amount;
        this.saleName = saleName;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getTotalProductPrice() {
        return totalProductPrice;
    }

    public void setTotalProductPrice(double totalProductPrice) {
        this.totalProductPrice = totalProductPrice;
    }

    public double getAmount() {
        return Amount;
    }

    public void setAmount(double amount) {
        Amount = amount;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getSaleName() {
        return saleName;
    }

    public void setSaleName(String saleName) {
        this.saleName = saleName;
    }

    public int getProductID() {
        return productID;
    }

    public String getCategory() {
        return category;
    }
}
