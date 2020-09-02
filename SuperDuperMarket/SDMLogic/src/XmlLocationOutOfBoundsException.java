import java.awt.*;

public class XmlLocationOutOfBoundsException extends Exception {


    String errorMsg;
    Point location;
    public XmlLocationOutOfBoundsException(String msg, Point location){
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
