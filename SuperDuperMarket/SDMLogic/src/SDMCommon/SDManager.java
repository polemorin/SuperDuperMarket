package SDMCommon;

import java.util.HashMap;
import java.util.Map;

public class SDManager {
    Map<String,MarketArea> marketAreaMap;
    Map<String,User> users;
    private final String Customer = "Customer";

    public SDManager(){
        marketAreaMap = new HashMap<>();
        users = new HashMap<>();
    }

    public boolean isUsernameAvailable(String userName){
        return !users.containsKey(userName);
    }

    public void addUser(String usernameFromParameter, String role) {
        User user;
        if(role.equals(Customer)) {
            user = new Customer(usernameFromParameter);
        }else{
            user = new StoreOwner(usernameFromParameter);
        }
        users.put(usernameFromParameter, user);
    }
}
