package ProductTypes;

import java.util.Objects;

public class PricedProduct extends Product{
    private double price;
    private final int storeID;

    public PricedProduct(Product product, double price, int storeID){
        super(product);
        this.price = price;
        this.storeID = storeID;
    }

    public PricedProduct(int id, String name, ProductCategory cat, double price, int storeID){
        super(id,name,cat);
        this.price = price;
        this.storeID = storeID;
    }
    public PricedProduct(PricedProduct other){
        super(other);
        this.price = other.price;
        this.storeID = other.storeID;
    }

    public double getPrice() {
        return price;
    }

    public int getStoreID() {
        return storeID;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        PricedProduct that = (PricedProduct) o;
        return price == that.price &&
                storeID == that.storeID&&
                this.getProductID() == that.getProductID();
    }

    @Override
    public int hashCode() {
        int res = 17;
        res = 31 * res + (int)price;
        res = 31 * res + storeID;
        res = 31 * res + this.getProductID();
        return res;
    }
}
