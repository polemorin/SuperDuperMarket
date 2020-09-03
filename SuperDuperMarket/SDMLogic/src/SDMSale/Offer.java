package SDMSale;

import jaxb.generated.SDMOffer;

public class Offer {
    private final double quantity;
    private final int itemID;
    private final int forAdditional;

    public Offer(SDMOffer sdmOfferToAdd) {
        itemID = sdmOfferToAdd.getItemId();
        forAdditional  =sdmOfferToAdd.getForAdditional();
        quantity = sdmOfferToAdd.getQuantity();
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
}
