package SDMSale;

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
}
