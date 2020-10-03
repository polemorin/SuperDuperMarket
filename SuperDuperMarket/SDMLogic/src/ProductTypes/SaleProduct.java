package ProductTypes;

public class SaleProduct extends SoldProduct{

    private final String saleName;




    private String categoryString;

    public SaleProduct(int id, String name, ProductCategory cat, double price, int storeID,String saleName,double amountBought) {
        super(id,name,cat,price,storeID,amountBought,price);
        this.saleName = saleName;
        categoryString = cat.toString();


    }
    public String getSaleName() {
        return saleName;
    }
    public double getAmountBought() {
        return super.getAmountSoldInOrder();

    }
    public String getCategoryString() {
        return categoryString;
    }
}
