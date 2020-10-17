package Alerts;

import java.awt.*;

public class NewStoreAlert  implements IUserAlert{
    String storeOwnerName;
    String storeName;
    Point storeLocation;
    String productAmount;
    String totalProductInArea;
    String alertText;

    public NewStoreAlert(String storeOwnerName, String storeName, Point storeLocation, String productAmount,String totalProductInArea) {
        this.storeOwnerName = storeOwnerName;
        this.storeName = storeName;
        this.storeLocation = storeLocation;
        this.productAmount = productAmount;
        this.totalProductInArea = totalProductInArea;
        alertText = this.toString();
    }

    @Override
    public String toString() {
        String text;
        text = "A new store opened in an area you are the owner of! - " +
                "Owner name:"+storeOwnerName+" store name: "+storeName+" location: "+storeLocation.x+","+storeLocation.y+" with "+productAmount+ " products out of "+totalProductInArea+" choices";
        return text;
    }
}
