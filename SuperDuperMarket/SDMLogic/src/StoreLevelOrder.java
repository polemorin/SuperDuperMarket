import com.sun.org.apache.xpath.internal.operations.Or;

import java.awt.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.time.*;
import ProductTypes.*;

public class StoreLevelOrder {
    private static int OrderIDGenerator = 1000;
    private final Integer OrderID;
    private List<SoldProduct> soldProducts;
    private int amountOfProducts;
    private double totalProductsPrice;
    private final int storeID;
    private final int customerID;
    private final double deliveryPrice;
    private final Date date;
    private final String storeName;
    private int amountOfProductTypes;

    public StoreLevelOrder(Store store, int customerID, Date date,  Point customerLocation ){
        this.storeID = store.getID();
        this.customerID = customerID;
        this.date = date;
        this.storeName = store.getName();
        this.deliveryPrice = getDistanceFromCustomerToStore(store.getLocation(),customerLocation)*store.getDeliveryPPK();
        totalProductsPrice = 0;
        OrderID = OrderIDGenerator++;
        soldProducts = new ArrayList<SoldProduct>();
        amountOfProducts = 0;
        amountOfProductTypes = 0;
    }


    public StoreLevelOrder(StoreLevelOrder other){
        this.OrderID = other.OrderID;
        this.totalProductsPrice = other.totalProductsPrice;
        this.storeName = other.storeName;
        this.date = (Date) other.date.clone();
        this.customerID = other.customerID;
        this.storeID = other.storeID;
        this.deliveryPrice = other.deliveryPrice;
        this.soldProducts = new ArrayList<SoldProduct>(other.soldProducts.size());
        for (SoldProduct itr:other.soldProducts) {
            this.soldProducts.add(new SoldProduct(itr));
        }
    }

    public StoreLevelOrder(Integer orderID, List<SoldProduct> soldProducts, int amountOfProducts, double totalProductsPrice, int storeID, int customerID, double deliveryPrice, Date date, String storeName) {
        OrderID = orderID;
        if(OrderIDGenerator < OrderID){
            OrderIDGenerator = OrderID + 1;
        }
        this.soldProducts = soldProducts;
        this.amountOfProducts = amountOfProducts;
        this.totalProductsPrice = totalProductsPrice;
        this.storeID = storeID;
        this.customerID = customerID;
        this.deliveryPrice = deliveryPrice;
        this.date = date;
        this.storeName = storeName;
        amountOfProductTypes = this.soldProducts.size();
    }

    public List<String> getStoreStringListToFile(){
        List<String> storeLevelOrderStrings = new ArrayList<String>();


        storeLevelOrderStrings.add(((Integer)(amountOfProductTypes)).toString());
        storeLevelOrderStrings.add(((Integer)(amountOfProducts)).toString());
        storeLevelOrderStrings.add((OrderID).toString());
        storeLevelOrderStrings.add(((Double)(totalProductsPrice)).toString());
        storeLevelOrderStrings.add(((Integer)(storeID)).toString());
        storeLevelOrderStrings.add(((Integer)(customerID)).toString());
        storeLevelOrderStrings.add((String.format("%.2f",deliveryPrice)));
        storeLevelOrderStrings.add(new SimpleDateFormat("dd/MM-HH:mm").format(date));
        storeLevelOrderStrings.add(storeName);
        for(int i = 0; i < soldProducts.size(); i++){
            List<String> productDetails = soldProducts.get(i).getProductStringListToFile();
            storeLevelOrderStrings.addAll(productDetails);
        }
        return storeLevelOrderStrings;
    }
    public static StoreLevelOrder getStoreLevelOrderFromTxtFile(List<String> storeOrderDetails)throws ParseException {

        int productFieldAmount = SoldProduct.class.getDeclaredFields().length+
                PricedProduct.class.getDeclaredFields().length+
                Product.class.getDeclaredFields().length;
        int amountOfTypes = Integer.parseInt(storeOrderDetails.get(0));
        int amountOfProducts = Integer.parseInt(storeOrderDetails.get(1));
        Integer orderID = Integer.parseInt(storeOrderDetails.get(2));
        double productPrice = Double.parseDouble(storeOrderDetails.get(3));
        int storeOrderID = Integer.parseInt(storeOrderDetails.get(4));
        int customerOrderID = Integer.parseInt(storeOrderDetails.get(5));
        double deliveryCost = Double.parseDouble(storeOrderDetails.get(6));
        Date orderDate=new SimpleDateFormat("dd/MM-HH:mm").parse(storeOrderDetails.get(7));
        String storeOrderName = storeOrderDetails.get(8);

        List<SoldProduct> productsSoldInOrder = new ArrayList<SoldProduct>();
        List<String> productDetails = new ArrayList<String>();
        int counterNextLineToRead = 9;

        for(int i = 0; i < amountOfTypes; i++){
            for(int j = 0; j < productFieldAmount; j++){
                productDetails.add(storeOrderDetails.get(counterNextLineToRead++));
            }
            productsSoldInOrder.add(SoldProduct.getSoldProductFromTxtFile(productDetails));
            productDetails.clear();
        }
        return new StoreLevelOrder(orderID,productsSoldInOrder,amountOfProducts,productPrice,storeOrderID,customerOrderID,deliveryCost,orderDate,storeOrderName);

    }

    public double getDistanceFromCustomerToStore(Point store,Point customer){
        double a,b;
        a = store.x - customer.x;
        b= store.y - customer.y;
        a = Math.pow(a,2);
        b = Math.pow(b,2);
        return Math.sqrt(a+b);
    }
    public boolean isProductSoldInStoreLevelOrder(Product product){
        for (Product sp: soldProducts) {
            if(sp.getProductID() == product.getProductID())
                return true;
        }
        return false;
    }
    private void addToTotalProductsPrice(double price)
    {
        totalProductsPrice += price;
    }
    public void addProductToOrder(SoldProduct productToAdd)
    {
        boolean productAlreadyExistsInOrder = false;
        for (SoldProduct product: soldProducts) {
           if(product.getProductID() == productToAdd.getProductID())
           {
               product.addAmountToOrder(productToAdd.getAmountSoldInOrder());
               productAlreadyExistsInOrder = true;
           }
        }
        if(!productAlreadyExistsInOrder){
            soldProducts.add(productToAdd);
            amountOfProductTypes++;
        }

        addToTotalProductsPrice(productToAdd.getTotalPrice());
        if(productToAdd.getProductCategory().toString().compareTo("Quantity") == 0) {
            amountOfProducts +=productToAdd.getAmountSoldInOrder();
        }
        else{
            amountOfProducts ++;
        }

    }


    public List<SoldProduct> getSoldProducts() {
        return soldProducts;
    }

    public double getTotalProductsPrice() {
        return totalProductsPrice;
    }

    public int getStoreID() {
        return storeID;
    }

    public int getCustomerID() {
        return customerID;
    }

    public double getDeliveryPrice() {
        return deliveryPrice;
    }

    public Date getDate() {
        return date;
    }

    public String getStoreName() {
        return storeName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StoreLevelOrder that = (StoreLevelOrder) o;
        return OrderID == that.OrderID;
    }

    @Override
    public int hashCode() {
        int res = 17;
        res = 31*res+OrderID.hashCode();
        return res;
    }

    public int getAmountOfProducts() {
        return amountOfProducts;
    }
    public int getAmountOfProductTypes(){
        return soldProducts.size();
    }

    public double getAmountSoldInOrder(Product product) {
        for (SoldProduct sp:soldProducts) {
            if(sp.getProductID() == product.getProductID()){
                return sp.getAmountSoldInOrder();
            }
        }
        return 0;
    }
}
