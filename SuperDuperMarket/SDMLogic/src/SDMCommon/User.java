package SDMCommon;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

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


    public int getAmountOfOrders(){
        return orderHistory.size();
    }
    public double getAverageProductCostFromOrders(){
        if(orderHistory.size() == 0)
        {
            return 0;
        }
        double sum= 0;
        for (CustomerLevelOrder order: orderHistory) {
            sum+=order.getTotalProductPrice();
        }
        return sum/orderHistory.size();
    }
    public double getAverageDeliveryCostFromOrders(){
        if(orderHistory.size() == 0)
        {
            return 0;
        }
        double sum= 0;
        for (CustomerLevelOrder order: orderHistory) {
            sum+=order.getDeliveryPrice();
        }
        return sum/orderHistory.size();
    }

    public Double distanceFromStore(Store store){
        return Math.sqrt(Math.pow(store.getLocation().getX() -location.x,2) +
                Math.pow(store.getLocation().getY() -location.y,2));
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

    @Override
    public String toString() {
        return name;
    }
}
