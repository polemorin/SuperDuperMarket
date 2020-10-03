package ProductTypes;

public class ProductTableInfo {
    int ID;
    String productName;
    String category;
    int amountOfStoresWhoSellProduct;
    double avgPrice;
    double amountSoldInMarket;

    public ProductTableInfo(int ID, String productName, String category, int amountOfStoresWhoSellProduct, double avgPrice, double amountSoldInMarket) {
        this.ID = ID;
        this.productName = productName;
        this.category = category;
        this.amountOfStoresWhoSellProduct = amountOfStoresWhoSellProduct;
        this.avgPrice = avgPrice;
        this.amountSoldInMarket = amountSoldInMarket;
    }
}
