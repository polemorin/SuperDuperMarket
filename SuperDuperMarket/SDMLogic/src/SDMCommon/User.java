package SDMCommon;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class User {
    private  String name;
    private static int IDGenerator = 0;
    private  int ID;
    private Point location; //??
    private double fundsAccount;
    private final Object UserCtorLockContext = new Object();

    public User(){}

    public User(String userName){
        synchronized (UserCtorLockContext) {
            name = userName;
            ID = ++IDGenerator;
        }
    }

    public User(String name, int ID,Point location) {
        this.name = name;
        this.ID = ID;
        this.location = location;
    }

    public User(User other){
        this.name = other.name;
        this.ID = other.ID;
        //this.orderHistory = new ArrayList<CustomerLevelOrder>(other.orderHistory.size());
        //for (CustomerLevelOrder itr:other.orderHistory) {
        //    this.orderHistory.add(new CustomerLevelOrder(itr));
        //}
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

    public double getAverageProductCostFromOrders(){
      // if(orderHistory.size() == 0)
      // {
      //     return 0;
      // }
      // double sum= 0;
      // for (CustomerLevelOrder order: orderHistory) {
      //     sum+=order.getTotalProductPrice();
      // }
      // return sum/orderHistory.size();
        return  0;
    }
    public double getAverageDeliveryCostFromOrders(){
      // if(orderHistory.size() == 0)
      // {
      //     return 0;
      // }
      // double sum= 0;
      // for (CustomerLevelOrder order: orderHistory) {
      //     sum+=order.getDeliveryPrice();
      // }
      // return sum/orderHistory.size();
        return 0;
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

    public double getFundsAccount() {
        return fundsAccount;
    }

    public void setFundsAccount(double fundsAccount) {
        this.fundsAccount = fundsAccount;
    }
}
