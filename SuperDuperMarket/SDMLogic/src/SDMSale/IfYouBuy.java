package SDMSale;

import ProductTypes.StoreProduct;

public class IfYouBuy {
    private final double quantity;
    private final int itemID;
    private final String productName;

    public IfYouBuy(jaxb.generated.IfYouBuy ifYouBuy,String productName) {
        quantity = ifYouBuy.getQuantity();
        itemID = ifYouBuy.getItemId();
        this.productName = productName;
    }

    public IfYouBuy(double quantity, int itemID, String productName) {
        this.quantity = quantity;
        this.itemID = itemID;
        this.productName = productName;
    }

    public String getProductName() {
        return productName;
    }
    public double getQuantity() {
        return quantity;
    }

    public int getItemID() {
        return itemID;
    }

    public boolean isProductInIfYouBuy(StoreProduct product) {
        return itemID == product.getProductID();
    }
}
