package SDMExceptions;

import java.awt.*;

public class XmlMultipleStoresShareLocationException extends Exception{
    String errorMsg;
    Point location;
    public XmlMultipleStoresShareLocationException(String msg, Point location){
        this.location = location;
        errorMsg = msg;
    }
    public String getErrorMsg() {
        return errorMsg;
    }

    public Point getInvalidLocation() {
        return location;
    }
}
