package JSObjects;

public class StoreProductJS {
    String productID;
    String price;

    public StoreProductJS(String productID, String price) {
        this.productID = productID;
        this.price = price;
    }

    public String getProductID() {
        return productID;
    }

    public String getPrice() {
        return price;
    }
}
