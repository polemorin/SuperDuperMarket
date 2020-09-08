package ProductTypes;

public class SaleProduct extends PricedProduct{

    private final String saleName;
    private double totalPrice;
    private double amountBought;

    public SaleProduct(int id, String name, ProductCategory cat, double price, int storeID,String saleName,double amountBought) {
        super(id, name, cat, price, storeID);
        this.saleName = saleName;
        this.amountBought = amountBought;
        totalPrice = this.amountBought * price;
    }
    public String getSaleName() {
        return saleName;
    }
    public double getAmountBought() {
        return amountBought;
    }
    public void setAmountBought(double amountBought) {
        this.amountBought = amountBought;
        totalPrice = this.amountBought * this.getPrice();
    }
}
