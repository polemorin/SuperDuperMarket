import java.awt.*;
import java.util.*;
import java.util.List;

public class Store {


    private final String name;
    private final Point location;
    private final int ID;
    private Map<Integer,StoreProduct> products;
    private List<StoreLevelOrder> storeOrderHistory;
    private final double deliveryPPK;
    private double totalDeliveryPayment;

    public Store(String name, Point location, int ID, Map<Integer,StoreProduct> productSet, double PPK){
        this.name= name;
        this.location = location;
        this.ID = ID;
        deliveryPPK = PPK;
        this.products = productSet;
        storeOrderHistory = new ArrayList<StoreLevelOrder>();
        totalDeliveryPayment = 0;
    }

    public Store(Store other){
        this.name = other.name;
        this.location = (Point) other.location.clone();
        this.ID = other.ID;
        this.deliveryPPK = other.deliveryPPK;
        this.totalDeliveryPayment = other.totalDeliveryPayment;
        this.products = new HashMap<Integer,StoreProduct>(other.products.size());
        for (Map.Entry<Integer,StoreProduct> itr1:other.products.entrySet()) {
            this.products.put(itr1.getKey(),new StoreProduct(itr1.getValue()));
        }
        this.storeOrderHistory = new ArrayList<StoreLevelOrder>(other.storeOrderHistory.size());
        for (StoreLevelOrder itr2:other.storeOrderHistory) {
            this.storeOrderHistory.add(new StoreLevelOrder(itr2));
        }
    }

    public double getProductPrice(Product product){
        //////////////////////////////////////////////////////////EXCEPTION INSTEAD OF RETURN
        double price = -1;
        if(products.containsKey(product.getProductID())){
            return products.get(product.getProductID()).getPrice();
        }
        return price;
    }
    public void addOrderToOrderHistory(StoreLevelOrder orderToAdd){
        storeOrderHistory.add(orderToAdd);
        totalDeliveryPayment += orderToAdd.getDeliveryPrice();
    }
    public String getName() {
        return name;
    }

    public Point getLocation() {
        return location;
    }

    public int getID() {
        return ID;
    }

    public Map<Integer,StoreProduct> getProducts() {
        return products;
    }

    public double getDeliveryPPK() {
        return deliveryPPK;
    }
    public List<StoreLevelOrder> getStoreOrderHistory() {
        return storeOrderHistory;
    }

    public double getTotalDeliveryPayment() {
        return totalDeliveryPayment;
    }

    public boolean doesStoreSellProduct(Product product){
        return products.containsKey(product.getProductID());
    }

    public boolean doesStoreSellProduct(int productID){
        return products.containsKey(productID);
    }

    public void updateStoreAfterOrder(StoreLevelOrder order){
        storeOrderHistory.add(order);
        totalDeliveryPayment += order.getDeliveryPrice();
        order.getSoldProducts().forEach(this::updateProductSoldAmount);

    }
    private void updateProductSoldAmount(SoldProduct product){
        StoreProduct p = products.get(product.getProductID());
        p.setTotalAmountSoldInStore(p.getTotalAmountSoldInStore()+product.getAmountSoldInOrder());
        products.replace(p.getProductID(),p);
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Store store = (Store) o;
        return ID == store.ID;
    }

    @Override
    public int hashCode() {
        return 31 * 17 + ID;
    }

    public void removeProductFromStore(int productID) {
        products.remove(productID);
    }

    public void addNewProductToStore(StoreProduct storeProduct) {
        products.put(storeProduct.getProductID(),storeProduct);
    }

    public void updateStoreProduct(int productID, double price) {
        products.get(productID).setPrice(price);
    }

    public void clearOrderHistory() {
        storeOrderHistory.clear();
    }
}