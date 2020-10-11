package ProductTypes;

public class StoreProductInfo {
    int productID;
    String productName;
    double productPrice;
    String category;

    public StoreProductInfo(int productID,String productName, double productPrice, String category) {
        this.productID = productID;
        this.productPrice = productPrice;
        this.category = category;
        this.productName = productName;
    }
}
