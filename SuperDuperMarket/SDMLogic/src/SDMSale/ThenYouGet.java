package SDMSale;

import ProductTypes.StoreProduct;
import jaxb.generated.SDMOffer;

import java.util.ArrayList;
import java.util.List;

public class ThenYouGet {
    private String operator;
    private List<Offer> offers;

    public ThenYouGet(jaxb.generated.ThenYouGet thenYouGet) {
        operator = thenYouGet.getOperator();
        offers = new ArrayList<Offer>();
        Offer offerToAdd;
        for (SDMOffer SDMOfferToAdd:thenYouGet.getSDMOffer()) {
            offerToAdd = new Offer(SDMOfferToAdd);
            offers.add(offerToAdd);
        }
    }


    public String getOperator() {
        return operator;
    }

    public List<Offer> getOffers() {
        return offers;
    }

    public boolean isProductInThenYouGet(StoreProduct product) {
        for (Offer offer:offers) {
            if(offer.getItemID() == product.getProductID()) {
                return true;
            }
        }
        return false;
    }
}
