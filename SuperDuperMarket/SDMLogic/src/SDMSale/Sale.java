package SDMSale;

import ProductTypes.Product;
import ProductTypes.StoreProduct;

import java.util.List;
import java.util.Map;

public class Sale {
    private final String name;
    private final IfYouBuy ifYouBuy;
    private final ThenYouGet thenYouGet;


    public Sale(String name, jaxb.generated.IfYouBuy ifYouBuy, jaxb.generated.ThenYouGet thenYouGet, String ifYouBuyProductName, Map<Integer,String> thenYouGetProductNames) {
        this.name = name;
        this.ifYouBuy = new IfYouBuy(ifYouBuy,ifYouBuyProductName);
        this.thenYouGet = new ThenYouGet(thenYouGet,thenYouGetProductNames);
    }
    public Sale(String saleName,Product ifYouBuyProduct,double ifYouBuyAmount, String operator, List<Offer> offerList){
        name = saleName;
        ifYouBuy = new IfYouBuy(ifYouBuyAmount, ifYouBuyProduct.getProductID(),ifYouBuyProduct.getProductName());
        thenYouGet = new ThenYouGet(operator,offerList);
    }

    public boolean isProductPartOfSale(StoreProduct product){
        return ifYouBuy.isProductInIfYouBuy(product)|| thenYouGet.isProductInThenYouGet(product);
    }
    public String getName() {
        return name;
    }

    public IfYouBuy getIfYouBuy() {
        return ifYouBuy;
    }

    public ThenYouGet getThenYouGet() {
        return thenYouGet;
    }

    @Override
    public String toString() {
        StringBuilder saleString = new StringBuilder();
        saleString.append(name + ": if you buy ");
        if(isInt(ifYouBuy.getQuantity())){
            saleString.append((int)ifYouBuy.getQuantity());
        }
        else{
            saleString.append(ifYouBuy.getQuantity());
        }
        saleString.append(" "+ ifYouBuy.getProductName() + " then you get "+ thenYouGet.toString());
       return saleString.toString();
    }

    private boolean isInt(double Quantity){
        return Quantity == Math.floor(Quantity);
    }
}
