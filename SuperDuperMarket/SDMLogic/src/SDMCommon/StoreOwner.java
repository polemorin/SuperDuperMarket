package SDMCommon;

import Alerts.IUserAlert;

import java.util.ArrayList;
import java.util.List;

public class StoreOwner extends User{
    private List<Feedback> feedBacks;
    private List<Store> storeList;
    private List<IUserAlert> alertList;
    public StoreOwner(String userName) {
        super(userName);
        this.feedBacks = new ArrayList<>();
        this.storeList = new ArrayList<>();
        alertList = new ArrayList<>();
    }
    public void addAlert(IUserAlert alert){
        alertList.add(alert);
    }

    public List<IUserAlert> getAlertList() {
        return alertList;
    }
    public void emptyAlertList(){
        alertList.clear();
    }
}
