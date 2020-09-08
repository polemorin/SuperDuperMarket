package SDMSale;

import ProductTypes.StoreProduct;
import jaxb.generated.SDMOffer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ThenYouGet {
    private String operator;
    private List<Offer> offers;

    public ThenYouGet(jaxb.generated.ThenYouGet thenYouGet, Map<Integer,String> thenYouGetProductNames) {
        operator = thenYouGet.getOperator();
        offers = new ArrayList<Offer>();
        Offer offerToAdd;
        for (SDMOffer SDMOfferToAdd:thenYouGet.getSDMOffer()) {
            offerToAdd = new Offer(SDMOfferToAdd,thenYouGetProductNames.get(SDMOfferToAdd.getItemId()));
            offers.add(offerToAdd);
        }
    }

    @Override
    public String toString() {

        int i = offers.size();
        StringBuilder thenYouGetString = new StringBuilder();
        switch(operator) {
            case "ONE-OF":
                for (Offer offer:offers) {
                    if(isInt(offer.getQuantity())){
                        thenYouGetString.append((int)offer.getQuantity());
                    }
                    else {
                        thenYouGetString.append(offer.getQuantity());
                    }
                    thenYouGetString.append(" ");
                    thenYouGetString.append(offer.getProductName());
                    thenYouGetString.append(" for additional ");
                    thenYouGetString.append(offer.getForAdditional());
                    if(i != 1) {
                        thenYouGetString.append("$ or ");
                    }
                    else
                    {
                        thenYouGetString.append("$.");
                    }
                    i--;
                }
                break;
            case "ALL-OR-NOTHING":
                for (Offer offer:offers) {
                    if(isInt(offer.getQuantity())){
                        thenYouGetString.append((int)offer.getQuantity());
                    }
                    else {
                        thenYouGetString.append(offer.getQuantity());
                    }
                    thenYouGetString.append(" ");
                    thenYouGetString.append(offer.getProductName());
                    thenYouGetString.append(" for additional ");
                    thenYouGetString.append(offer.getForAdditional());
                    if(i != 1) {
                        thenYouGetString.append("$ and ");
                    }
                    else
                    {
                        thenYouGetString.append("$.");
                    }
                    i--;
                }
                break;
            case "IRRELEVANT":
                for (Offer offer:offers) {
                    if(isInt(offer.getQuantity())){
                        thenYouGetString.append((int)offer.getQuantity());
                    }
                    else {
                        thenYouGetString.append(offer.getQuantity());
                    }
                    thenYouGetString.append(" ");
                    thenYouGetString.append(offer.getProductName());
                    thenYouGetString.append(" for additional ");
                    thenYouGetString.append(offer.getForAdditional());
                }
                break;
        }
        return thenYouGetString.toString();
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
    private boolean isInt(double Quantity){
        return Quantity == Math.floor(Quantity);
    }
}
