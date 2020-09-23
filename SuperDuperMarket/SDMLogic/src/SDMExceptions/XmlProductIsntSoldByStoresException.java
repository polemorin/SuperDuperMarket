package SDMExceptions;

public class XmlProductIsntSoldByStoresException extends Exception{

    int productID;
    public XmlProductIsntSoldByStoresException(int id){
        productID = id;
    }

    public int getProductID() {
        return productID;
    }
}
