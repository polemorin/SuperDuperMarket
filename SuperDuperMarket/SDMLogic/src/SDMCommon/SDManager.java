package SDMCommon;

import java.util.HashMap;
import java.util.Map;

public class SDManager {
    Map<String,MarketArea> marketAreaMap;
    Map<String,User> users;

    public SDManager(){
        marketAreaMap = new HashMap<>();
        users = new HashMap<>();
    }

    public boolean isUsernameAvailable(String userName){
        return !users.containsKey(userName);
    }

    public void addUser(String usernameFromParameter) {

    }
}
