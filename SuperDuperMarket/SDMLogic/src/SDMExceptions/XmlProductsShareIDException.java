package SDMExceptions;

public class XmlProductsShareIDException extends Exception {

    int productID;
    public XmlProductsShareIDException(int productID){
        this.productID = productID;
    }

    public int getProductID() {
        return productID;
    }

}

