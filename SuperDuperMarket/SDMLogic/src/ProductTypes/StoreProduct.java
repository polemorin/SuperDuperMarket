package ProductTypes;

import java.util.Objects;

public class StoreProduct extends PricedProduct{
    private double totalAmountSoldInStore;

    public StoreProduct(Product product, double price,int storeID){
        super(product,price,storeID);
        totalAmountSoldInStore = 0;
    }

    public StoreProduct(int id, String name, ProductCategory cat, int price, int storeID){
        super(id,name,cat,price,storeID);
        this.totalAmountSoldInStore = 0;
    }
    public double getTotalAmountSoldInStore() {
        return totalAmountSoldInStore;
    }
    public void setTotalAmountSoldInStore(double totalAmountSoldInStore) {
        this.totalAmountSoldInStore = totalAmountSoldInStore;
    }

    public StoreProduct(StoreProduct other){
        super(other);
        this.totalAmountSoldInStore = other.totalAmountSoldInStore;
    }
    public String getName(){
        return getProductName();
    }
    public double getProductPrice(){
        return getPrice();
    }
    public int getProductID(){
        return super.getProductID();
    }
    public String getProductCategoryString(){
        return super.getProductCategoryString();
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        StoreProduct that = (StoreProduct) o;
        return this.getProductID() == that.getProductID()&&
                this.getStoreID() == that.getStoreID()&&
                Double.compare(that.totalAmountSoldInStore, totalAmountSoldInStore) == 0;
    }

    @Override
    public int hashCode() {
        int res = 17;
        res = 31 * res + this.getProductID();
        res = 31 * res + this.getStoreID();
        res = 31 * res + (int)totalAmountSoldInStore;
        return res;
    }
}
