package SDMSale;

import ProductTypes.StoreProduct;

public class IfYouBuy {
    private final double quantity;
    private final int itemID;

    public IfYouBuy(jaxb.generated.IfYouBuy ifYouBuy) {
        quantity = ifYouBuy.getQuantity();
        itemID = ifYouBuy.getItemId();
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
