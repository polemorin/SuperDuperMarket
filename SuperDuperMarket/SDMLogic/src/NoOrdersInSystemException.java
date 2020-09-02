public class NoOrdersInSystemException extends Exception {

    public NoOrdersInSystemException(String message) {
        super(message);
    }


    public int getNumOfOrders() {
        return numOfOrders;
    }

    private final int numOfOrders = 0;
}
