public class XmlStoreSellsMultipleProductsWithSameIDException extends Exception{

    String storeName;
    int productID;

    public XmlStoreSellsMultipleProductsWithSameIDException(String storeName, int productID){
        this.storeName = storeName;
        this.productID = productID;
    }
    public String getStoreName() {
        return storeName;
    }

    public int getProductID() {
        return productID;
    }

}
