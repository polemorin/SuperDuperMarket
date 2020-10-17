package JSObjects;

public class StoreJS {
    private String storeName;
    private String storeID;
    private String PPK;
    private StoreProductJS[] storeProducts;
    private String locationX;
    private String locationY;

    public StoreJS(String storeName, String storeID, String PPK, StoreProductJS[] storeProducts, String locationX, String locationY) {
        this.storeName = storeName;
        this.storeID = storeID;
        this.PPK = PPK;
        this.storeProducts = storeProducts;
        this.locationX = locationX;
        this.locationY = locationY;
    }

    public String getStoreName() {
        return storeName;
    }

    public String getStoreID() {
        return storeID;
    }

    public String getPPK() {
        return PPK;
    }

    public StoreProductJS[] getStoreProducts() {
        return storeProducts;
    }

    public String getLocationX() {
        return locationX;
    }

    public String getLocationY() {
        return locationY;
    }
}
