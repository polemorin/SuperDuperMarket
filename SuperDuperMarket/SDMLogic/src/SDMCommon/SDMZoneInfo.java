package SDMCommon;

public class SDMZoneInfo {


    private String creatorName;
    private String zoneName;
    private int productAmount;
    private int storeAmount;
    private int orderAmount;
    private double orderAVGPrice;

    public SDMZoneInfo(MarketArea zone) {
        creatorName = zone.getOwnerName();
        zoneName = zone.getZone();
        productAmount = zone.getProducts().size();
        storeAmount = zone.getStores().size();
        orderAmount = zone.getOrderHistory().size();
        orderAVGPrice = zone.getAVGOrderCost();
    }
    public String getCreatorName() {
        return creatorName;
    }

    public String getZoneName() {
        return zoneName;
    }

    public int getProductAmount() {
        return productAmount;
    }

    public int getStoreAmount() {
        return storeAmount;
    }

    public int getOrderAmount() {
        return orderAmount;
    }

    public double getOrderAVGPrice() {
        return orderAVGPrice;
    }
}
