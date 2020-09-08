package ProductTypes;

public class SaleProduct extends PricedProduct{

    private final String saleName;
    private double totalPrice;
    private double amountBought;
    private int productID;
    private String productName;
    private int storeID;
    private double price;




    public SaleProduct(int id, String name, ProductCategory cat, double price, int storeID,String saleName,double amountBought) {
        super(id, name, cat, price, storeID);
        this.saleName = saleName;
        this.price = price;
        this.amountBought = amountBought;
        totalPrice = this.amountBought * price;
        System.out.println(totalPrice);
        productID = id;
        productName = name;
        this.storeID = storeID;


    }
    public String getSaleName() {
        return saleName;
    }
    public double getAmountBought() {
        return amountBought;

    }
    public void setAmountBought(double amountBought) {
        this.amountBought = amountBought;
        this.totalPrice = this.amountBought * this.price;
        System.out.println(totalPrice);
    }
}
