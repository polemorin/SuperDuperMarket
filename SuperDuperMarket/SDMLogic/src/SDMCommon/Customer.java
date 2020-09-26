package SDMCommon;

import java.util.ArrayList;
import java.util.List;

public class Customer extends User {
    private List<CustomerLevelOrder> orderHistory;

    public Customer(String userName) {
        super(userName);
        orderHistory = new ArrayList<>();
    }
}
