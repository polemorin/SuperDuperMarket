package SDMExceptions;

public class XmlStoreSellProductNotInMarketException extends Exception {
    int invalidProductID;

    public XmlStoreSellProductNotInMarketException(int id){
        invalidProductID = id;
    }

    public int getInvalidProductID() {
        return invalidProductID;
    }

}
