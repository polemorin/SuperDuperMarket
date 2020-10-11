package JSObjects;

public class SaleProductJS {
    private int productID;
    private double totalProductPrice;
    private double Amount;
    private String saleName;

    public SaleProductJS(int productID, double totalProductPrice, double amount, String saleName) {
        this.productID = productID;
        this.totalProductPrice = totalProductPrice;
        Amount = amount;
        this.saleName = saleName;
    }
}
