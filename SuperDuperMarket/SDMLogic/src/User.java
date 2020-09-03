import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User {

    private List<CustomerLevelOrder> orderHistory;
    private final String name;
    private final int ID;
    private Point location;

    public User(String name, int ID,Point location) {
        this.orderHistory = new ArrayList<CustomerLevelOrder>();
        this.name = name;
        this.ID = ID;
        this.location = location;
    }

    public User(User other){
        this.name = other.name;
        this.ID = other.ID;
        this.orderHistory = new ArrayList<CustomerLevelOrder>(other.orderHistory.size());
        for (CustomerLevelOrder itr:other.orderHistory) {
            this.orderHistory.add(new CustomerLevelOrder(itr));
        }
    }

    public void addOrderToOrderHistory(CustomerLevelOrder orderToAdd) {
        orderHistory.add(orderToAdd);
    }
    public List<CustomerLevelOrder> getOrderHistory() {
        return orderHistory;
    }
    public String getName() {
        return name;
    }

    public int getID() {
        return ID;
    }

    public Point getLocation() {
        return location;
    }
    public void setLocation(Point location) {
        this.location = location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return ID == user.ID &&
                name.equals(user.name);
    }

    @Override
    public int hashCode() {
        int res = 17;
        res = 31 * res + ID;
        res = 31 * res + name.hashCode();
        return res;
    }
}
