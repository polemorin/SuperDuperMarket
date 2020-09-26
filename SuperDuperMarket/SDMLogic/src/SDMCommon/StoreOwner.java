package SDMCommon;

import java.util.ArrayList;
import java.util.List;

public class StoreOwner extends User{
    private List<Feedback> feedBacks;
    private List<Store> storeList;

    public StoreOwner(String userName) {
        super(userName);
        this.feedBacks = new ArrayList<>();
        this.storeList = new ArrayList<>();
    }
}
