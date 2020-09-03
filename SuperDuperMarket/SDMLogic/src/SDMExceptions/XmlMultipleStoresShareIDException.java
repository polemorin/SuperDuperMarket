package SDMExceptions;

public class XmlMultipleStoresShareIDException extends Exception {
    String errorMsg;
    int invalidId;

    public XmlMultipleStoresShareIDException(String msg, int id){
        errorMsg = msg;
        invalidId = id;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public int getInvalidId() {
        return invalidId;
    }

}
