package ProductTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SoldProduct extends PricedProduct{

    private double amountSoldInOrder;

    public SoldProduct(int id, String name, ProductCategory cat, double price, int storeID, double amountSoldInOrder, double totalPrice){
        super(id,name,cat,price,storeID);
        this.amountSoldInOrder = amountSoldInOrder;
    }

    public SoldProduct(PricedProduct pricedProduct, double amount){
        super(pricedProduct);
        this.amountSoldInOrder = amount;

    }

    public SoldProduct(SoldProduct other){
        super(other);
        this.amountSoldInOrder = other.amountSoldInOrder;
    }

    public List<String> getProductStringListToFile(){
        List<String> productStringList = new ArrayList<String>();

        String id = ((Integer)(this.getProductID())).toString();
        String price = String.format("%.2f",(Double)(this.getPrice()));
        String storeID = ((Integer)(this.getStoreID())).toString();
        String soldInOrder = ((Double)(amountSoldInOrder)).toString();

        productStringList.add(id);
        productStringList.add(this.getProductName());
        productStringList.add(this.getProductCategory().toString());
        productStringList.add(price);
        productStringList.add(storeID);
        productStringList.add(soldInOrder);

        return productStringList;
    }

    public static SoldProduct getSoldProductFromTxtFile(List<String> productDetails){
        int productId =Integer.parseInt(productDetails.get(0));
        String productName = productDetails.get(1);
        ProductCategory category;
        if(productDetails.get(2).equals("Quantity")){
            category = ProductCategory.Quantity;
        }
        else
        {
            category = ProductCategory.Weight;
        }

        double price = Double.parseDouble(productDetails.get(3));
        int storeId =Integer.parseInt(productDetails.get(4));
        double amountSold = Double.parseDouble(productDetails.get(5));
        double totalPrice = Double.parseDouble(productDetails.get(6));
        return new SoldProduct(productId,productName,category,price,storeId,amountSold,totalPrice);
    }

    public double getAmountSoldInOrder()
    {
        return amountSoldInOrder;
    }
    public void setAmountSoldInOrder(double amountSoldInOrder) {
        this.amountSoldInOrder = amountSoldInOrder;
    }
    public void addAmountToOrder(double amountToAdd){
        amountSoldInOrder += amountToAdd;

    }
    public double getTotalPrice() {
        return super.getPrice()*amountSoldInOrder;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SoldProduct that = (SoldProduct) o;
        return Double.compare(that.amountSoldInOrder, amountSoldInOrder) == 0 &&
                this.getProductID() == that.getProductID()&&
                this.getStoreID() == this.getStoreID();
    }

    @Override
    public int hashCode() {
        int res = 17;
        res = 31 * res + (int)amountSoldInOrder;
        res = 31 * res + this.getProductID();
        res = 31 * res + this.getStoreID();
        return res;
    }


}
