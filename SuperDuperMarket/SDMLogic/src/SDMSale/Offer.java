package SDMSale;

import jaxb.generated.SDMOffer;

public class Offer {
    private final double quantity;
    private final int itemID;
    private final int forAdditional;
    private final String productName;

    public Offer(SDMOffer sdmOfferToAdd,String productName) {
        itemID = sdmOfferToAdd.getItemId();
        forAdditional  =sdmOfferToAdd.getForAdditional();
        quantity = sdmOfferToAdd.getQuantity();
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

    public int getForAdditional() {
        return forAdditional;
    }

    @Override
    public String toString() {
        return quantity+" "+productName+" for additional "+ forAdditional + "$.";
    }
}
