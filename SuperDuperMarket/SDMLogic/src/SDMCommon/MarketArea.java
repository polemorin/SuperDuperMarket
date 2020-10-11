package SDMCommon;

import javax.swing.*;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.awt.*;
import java.io.*;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.*;
import java.util.List;

import ProductTypes.*;
import ProductTypes.Product;
import ProductTypes.ProductCategory;
import ProductTypes.StoreProduct;
import SDMCommon.CustomerLevelOrder;
import SDMExceptions.*;
import SDMSale.Sale;
import jaxb.generated.*;

public class MarketArea {
    private Map<Integer, Product> products;
    private Map<Integer, Store> stores;
    private Map<Integer, CustomerLevelOrder> orderHistory;
    private String zone;
    private String ownerName;

    public MarketArea(Map<Integer, Product> tempProductMap, Map<Integer, Store> tempStoreMap, String creatorName, String name) {
        products = tempProductMap;
        stores = tempStoreMap;
        ownerName = creatorName;
        zone = name;
        orderHistory = new HashMap<>();
    }

    public String getOwnerName(){
        return ownerName;
    }

    public int countHowManyStoresSellProduct(Product productToLookFor) {
        int storeCounter = 0;
        for (Map.Entry<Integer, Store> store : stores.entrySet()) {
            if (store.getValue().doesStoreSellProduct(productToLookFor))
                storeCounter++;
        }
        return storeCounter;
    }
    public int countHowManyStoresSellProduct(int productID) {
        return countHowManyStoresSellProduct(products.get(productID));
    }

    public double getAveragePriceForProduct(Product product) {
        double sumPriceForProductInAllStores = 0;
        for (Map.Entry<Integer, Store> storeInSystem : stores.entrySet()) {
            if (storeInSystem.getValue().doesStoreSellProduct(product))
                sumPriceForProductInAllStores += storeInSystem.getValue().getProductPrice(product);
        }
        return sumPriceForProductInAllStores / countHowManyStoresSellProduct(product);
    }

    public double totalAmountSoldInMarket(Product product) {
        double amountSoldCounter = 0;
        for (Map.Entry<Integer, CustomerLevelOrder> order : orderHistory.entrySet()) {
            amountSoldCounter += order.getValue().amountOfTimesSoldInOrder(product);
        }
        return amountSoldCounter;
    }

    public boolean isStoreIDValid(int storeID) {
        return stores.containsKey(storeID);
    }


    public boolean isValidLocationInSDM(Point location) {
        return location.x >= 1 && location.x <= 50 && location.y >= 1 && location.y <= 50;
    }


    public boolean isValidProductID(int productID) {
        return products.containsKey(productID);
    }
    public double getAVGOrderCost(){
        if(orderHistory.size() == 0){
            return 0;
        }
        double sum = 0.0;
        for (Map.Entry<Integer,CustomerLevelOrder> order: orderHistory.entrySet()) {
            sum+=order.getValue().getDeliveryPrice()+order.getValue().getTotalProductPrice();
        }
        return sum/orderHistory.size();
    }

   // public void placeOrderInSDM(CustomerLevelOrder orderToAdd, int userID) {
   //     users.get(userID).addOrderToOrderHistory(orderToAdd);
   //     orderHistory.put(orderToAdd.getOrderID(), orderToAdd);
   //     for (StoreLevelOrder storeOrder:orderToAdd.getOrders()) {
   //         stores.get(storeOrder.getStoreID()).updateStoreAfterOrder(storeOrder);
   //     }
   // }


    public Map<Integer, Product> getProducts() {
        return products;
    }


    public Map<Integer, Store> getStores() {
        return stores;
    }

    public Map<Integer, CustomerLevelOrder> getOrderHistory() {
        return orderHistory;
    }

    public CustomerLevelOrder createCheapestOrder(Map<Integer, Double> shoppingList, int customerID, LocalDate date, Point location) {
        List<StoreLevelOrder> storeListForOrder = new ArrayList<StoreLevelOrder>();
        int storeID;
        SoldProduct productToAdd;
        boolean storeAlreadyInList = false;
        StoreLevelOrder storeOrderToAdd;
        for (Map.Entry<Integer, Double> product : shoppingList.entrySet()) {
            storeID = findCheapestStoreID(product.getKey());
            productToAdd = new SoldProduct(
                    (stores.get(storeID).getProducts().get(product.getKey())), product.getValue());

            for (StoreLevelOrder storeOrder : storeListForOrder) {
                if (storeOrder.getStoreID() == storeID) {
                    storeAlreadyInList = true;
                    storeOrder.addProductToOrder(productToAdd);
                }
            }
            if (!storeAlreadyInList) {
                storeOrderToAdd = new StoreLevelOrder(stores.get(storeID), customerID, date, location,CustomerLevelOrder.getNextOrderID());
                storeOrderToAdd.addProductToOrder(productToAdd);
                storeListForOrder.add(storeOrderToAdd);
            }
            storeAlreadyInList = false;
        }
        return new CustomerLevelOrder(storeListForOrder,location);
    }

    public int findCheapestStoreID(int productID) {
        Product p = products.get(productID);
        int cheapestStoreID = 0;
        double lowestPrice = getAveragePriceForProduct(p);
        for (Map.Entry<Integer, Store> store : stores.entrySet()) {
            if (store.getValue().doesStoreSellProduct(p) &&
                    store.getValue().getProductPrice(p) <= lowestPrice) {
                lowestPrice = store.getValue().getProductPrice(p);
                cheapestStoreID = store.getKey();
            }
        }
        return cheapestStoreID;
    }




    private final static String JAXB_XML_GAME_PACKAGE_NAME = "jaxb.generated";

    public static SuperDuperMarketDescriptor deserializeFrom(InputStream in) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_GAME_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        return (SuperDuperMarketDescriptor) u.unmarshal(in);
    }

   // public Map<Integer, Store> createStoresMapFromXml(String xmlName) throws JAXBException,XmlStoreSellProductNotInMarketException, XmlStoreSellsMultipleProductsWithSameIDException, XmlProductCategoryNotRecognizedException, XmlLocationOutOfBoundsException, XmlMultipleStoresShareLocationException, XmlMultipleStoresShareIDException,Exception {
   //     InputStream inputStream = new FileInputStream(xmlName);
   //     Map<Integer, Store> storesMap = new HashMap<Integer, Store>();
   //     SuperDuperMarketDescriptor descriptor = deserializeFrom(inputStream);
   //     SDMStores shops = descriptor.getSDMStores();
   //     List<SDMStore> listOfStores = shops.getSDMStore();
   //     Store storeToAdd;
   //
   //     for (SDMStore store : listOfStores) {
   //         Location location = store.getLocation();
   //         Point p = new Point(location.getX(), location.getY());
   //         if (!isValidLocationInSDM(p)) {
   //             throw new XmlLocationOutOfBoundsException("Store location out of bounds.", p);
   //         } else if (storesMap != null && doesStoreExistInLocation(storesMap, p)) {
   //             throw new XmlMultipleStoresShareLocationException("Multiple stores with same location.", p);
//
   //         } else if (storesMap.containsKey(store.getId())) {
   //             throw new XmlMultipleStoresShareIDException("Multiple Stores with the same ID.", store.getId());
   //         } else {
   //             if(p.x<minMapX){
   //                 minMapX = p.x;
   //             }
   //             if(p.y<minMapY){
   //                 minMapY = p.y;
   //             }
   //             if(p.x>maxMapX){
   //                 maxMapX = p.x;
   //             }
   //             if(p.y>maxMapY){
   //                 maxMapY = p.y;
   //             }
   //             storeToAdd = new Store(store.getName(), p, store.getId(), createStoreProductMap(store, descriptor.getSDMItems()), store.getDeliveryPpk());
   //             storeToAdd.initSalesFromXML(generateSalesFromXml(store,descriptor));
   //             storesMap.put(storeToAdd.getID(), storeToAdd);
   //         }
   //     }
   //     inputStream.close();
   //     return storesMap;
   // }

   // private Map<String, Sale> generateSalesFromXml(SDMStore store,SuperDuperMarketDescriptor descriptor)throws Exception {
   //     Map<String,Sale> sales = new HashMap<String, Sale>();
   //     SDMDiscounts discounts =store.getSDMDiscounts();
   //     String ifYouBuyProductName = "";
   //     Map<Integer,String> thenYouGetProductName = new HashMap<>();
   //     Sale saleToAdd;
   //     if(discounts != null){
   //         for (SDMDiscount discount: discounts.getSDMDiscount()) {
//
   //             doesMarketSellXmlDiscountItems(discount,descriptor);
   //             validateSDMDiscountItems(store, discount.getIfYouBuy(), discount.getThenYouGet(),descriptor);
   //             validateSDMDiscountName(store, discount.getName());
   //             for (SDMItem item:descriptor.getSDMItems().getSDMItem()) {
   //                 if(discount.getIfYouBuy().getItemId() == item.getId()){
   //                     ifYouBuyProductName =item.getName();
   //                 }
   //                 thenYouGetProductName.put(item.getId(),item.getName());
   //             }
//
   //             saleToAdd = new Sale(discount.getName(),discount.getIfYouBuy(),discount.getThenYouGet(),ifYouBuyProductName,thenYouGetProductName);
   //             sales.put(saleToAdd.getName(),saleToAdd);
   //         }
   //     }
   //     return sales;
   // }
//
   // private void validateSDMDiscountItems(SDMStore store, IfYouBuy ifYouBuy, ThenYouGet thenYouGet,SuperDuperMarketDescriptor descriptor) throws Exception{
   //     boolean foundIfYouBuyItemIDInStore = false;
   //     boolean foundThenYouGetItemIDInStore = false;
//
   //     for (SDMSell itemToCheck : store.getSDMPrices().getSDMSell()) { // search for If You Buy Item ID in items
   //         if(itemToCheck.getItemId() == ifYouBuy.getItemId()){
   //             foundIfYouBuyItemIDInStore = true;
   //         }
   //     }
   //     if(!foundIfYouBuyItemIDInStore){
   //         throw new Exception("Store doesn't sell ifYouBuy product.Product ID: "+ ifYouBuy.getItemId());
   //     }
   //     if(ifYouBuy.getQuantity() <= 0){
   //         throw new Exception("If you buy item quantity is a non positive number.");
   //     }
//
   //     for (SDMOffer offerToCheck: thenYouGet.getSDMOffer()) {//search all the offer's ID in store and checke quantity and for addiotnal is positive
   //         for (SDMSell itemToCheck:store.getSDMPrices().getSDMSell()) {
   //             if(itemToCheck.getItemId() == offerToCheck.getItemId()){
   //                 foundThenYouGetItemIDInStore = true;
   //             }
   //             if(offerToCheck.getQuantity() <= 0){
   //                 throw new Exception("If you buy item quantity is a non positive number.");
   //             }
   //             if(offerToCheck.getForAdditional() < 0){
   //                 throw new Exception("Invalid sale \"for additional\" is negative.");
   //             }
   //         }
   //         if(!foundThenYouGetItemIDInStore){
   //             throw new Exception("Store doesn't sell \"Then you get\" product");
   //         }
   //     }
//
   //     for (SDMItem item: descriptor.getSDMItems().getSDMItem()){// check category match with quantity in sale.
   //         if(item.getId() == ifYouBuy.getItemId() && item.getPurchaseCategory().compareToIgnoreCase("Quantity") == 0 &&
//
   //                 ifYouBuy.getQuantity() != Math.floor(ifYouBuy.getQuantity())){
   //             throw new Exception("Quantity should be int and is a double.");
   //         }
   //     }
   //     for (SDMOffer offer:thenYouGet.getSDMOffer()) {// check category match with quantity in sale.
   //         for (SDMItem item: descriptor.getSDMItems().getSDMItem()) {
   //             if(offer.getItemId() == item.getId() &&
//
   //                     item.getPurchaseCategory().compareToIgnoreCase("Quantity") == 0 &&
//
   //                     offer.getQuantity() != Math.floor(offer.getQuantity())){
//
   //                 throw new Exception("Quantity should be int and is a double.");
   //             }
   //         }
   //     }
   // }

    private void doesMarketSellXmlDiscountItems(SDMDiscount discount,SuperDuperMarketDescriptor descriptor) throws Exception{
       boolean foundIDinMarket = false;
        boolean foundIfYouBuyIDinMarket = false;
        for (SDMOffer offerToCheck:discount.getThenYouGet().getSDMOffer()) {
            for (SDMItem itemToCheck:descriptor.getSDMItems().getSDMItem()) {
                if(itemToCheck.getId() == offerToCheck.getItemId()){
                    foundIDinMarket = true;
                }
            }
            if(!foundIDinMarket){
                throw new Exception("Market does not sell \"then you get\" item");
            }
            foundIDinMarket = false;
        }
        for (SDMItem itemToCheck:descriptor.getSDMItems().getSDMItem()) {
            if(discount.getIfYouBuy().getItemId() == itemToCheck.getId())
            {
                foundIfYouBuyIDinMarket = true;
            }
        }
        if(!foundIfYouBuyIDinMarket){
            throw new Exception("Market does not sell \"if you buy \" item.");
        }
    }

    private void validateSDMDiscountName(SDMStore store, String saleName)throws Exception {
        String trimedSaleName = saleName.trim();
        int counter = 0;
        for (SDMDiscount discount : store.getSDMDiscounts().getSDMDiscount()) {
            if(discount.getName().trim().compareToIgnoreCase(trimedSaleName) == 0){
                counter++;
            }
        }
        if(counter != 1)
        {
            throw new Exception("Sale already exists in store.");
        }
    }


    private boolean doesStoreExistInLocation(Map<Integer, Store> stores, Point p) {
        for (Map.Entry<Integer, Store> store : stores.entrySet()) {
            if (store.getValue().getLocation() == p) {
                return true;
            }
        }
        return false;
    }

    private Map<Integer, StoreProduct> createStoreProductMap(SDMStore store, SDMItems sdmitems) throws XmlProductCategoryNotRecognizedException, XmlStoreSellProductNotInMarketException, XmlStoreSellsMultipleProductsWithSameIDException {
        StoreProduct product;
        SDMItem sdmitem = null;
        ProductCategory category = null;
        boolean productFoundInItems = false;
        Map<Integer, StoreProduct> products = new HashMap<Integer, StoreProduct>();
        for (SDMSell item : store.getSDMPrices().getSDMSell()) {
            for (SDMItem sdmitem1 : sdmitems.getSDMItem()) {
                if (sdmitem1.getId() == item.getItemId()) {
                    sdmitem = sdmitem1;
                    if (sdmitem.getPurchaseCategory().equals("Quantity")) {
                        category = ProductCategory.Quantity;
                    } else if (sdmitem.getPurchaseCategory().equals("Weight")) {
                        category = ProductCategory.Weight;
                    } else {
                        throw new XmlProductCategoryNotRecognizedException(sdmitem.getPurchaseCategory());
                    }
                    productFoundInItems = true;
                }

            }
            if (!productFoundInItems) {
                throw new XmlStoreSellProductNotInMarketException(item.getItemId());
            }
            productFoundInItems = false;
            product = new StoreProduct(item.getItemId(), sdmitem.getName(), category, item.getPrice(), store.getId());
            if (products.containsKey(product.getProductID())) {
                throw new XmlStoreSellsMultipleProductsWithSameIDException(store.getName(), product.getProductID());
            } else {
                products.put(product.getProductID(), product);
            }
        }
        return products;
    }

    private Map<Integer,Product> createProductMapFromXml(String xmlName, Map<Integer, Store> tempStoreMap ) throws JAXBException, XmlProductsShareIDException, XmlProductCategoryNotRecognizedException, XmlProductIsntSoldByStoresException, FileNotFoundException {
        InputStream inputStream = new FileInputStream(xmlName);
        Map<Integer, Product> productMap = new HashMap<Integer, Product>();
        SuperDuperMarketDescriptor descriptor = deserializeFrom(inputStream);
        SDMItems items = descriptor.getSDMItems();
        List<SDMItem> listOfItems = items.getSDMItem();
        Product productToAdd;
        ProductCategory category = null;
        boolean itemSold = false;

        for (SDMItem itm : listOfItems) {
            if (productMap.containsKey(itm.getId())) {
                throw new XmlProductsShareIDException(itm.getId());
            }
            if (itm.getPurchaseCategory().equals("Quantity")) {
                category = ProductCategory.Quantity;
            } else if (itm.getPurchaseCategory().equals("Weight")) {
                category = ProductCategory.Weight;
            } else {
                throw new XmlProductCategoryNotRecognizedException(itm.getPurchaseCategory());
            }
            productToAdd = new Product(itm.getId(), itm.getName(), category);
            productMap.put(productToAdd.getProductID(), productToAdd);
        }
        for (Map.Entry<Integer, Product> productToCheck : productMap.entrySet()) {
            for (Map.Entry<Integer, Store> storeToCheck : tempStoreMap.entrySet()) {
                for (Map.Entry<Integer, StoreProduct> storeProductToCheck : storeToCheck.getValue().getProducts().entrySet()) {
                    if (storeProductToCheck.getValue().getProductID() == productToCheck.getValue().getProductID()) {
                        itemSold = true;
                    }
                }
            }
            if (!itemSold) {
                throw new XmlProductIsntSoldByStoresException(productToCheck.getValue().getProductID());
            }
            itemSold = false;
        }

        return productMap;
    }

    public void removeProductFromStore(int productID, int storeID) {
        stores.get(storeID).removeProductFromStore(productID);
    }

    public void addProductToStore(int productID, int storeID, double price) {
        stores.get(storeID).addNewProductToStore(new StoreProduct(products.get(productID),price,storeID));
    }

    public int getStoreIDFromSaleName(String name) {
        for (Map.Entry<Integer, Store> store :stores.entrySet()) {
            for (Map.Entry<String,Sale> sale: store.getValue().getSales().entrySet()) {
                if(sale.getValue().getName().equals(name)){
                    return store.getValue().getID();
                }
            }
        }
        return -1;
    }

    public boolean isStoreLevelOrderPartOfDynamicOrder(StoreLevelOrder order) {
        return orderHistory.get(order.getCustomerLevelOrderID()).getOrders().size() != 1;
    }
    public boolean isSaleNameAlreadyInMarket(String saleName){
        for (Map.Entry<Integer, Store> store:stores.entrySet()) {
            if(store.getValue().getSales().containsKey(saleName)){
                return true;
            }
        }
        return false;
    }

    public boolean isAvailableLocationInSDM(Point location){

        for (Map.Entry<Integer,Store> store:this.stores.entrySet()) {
            if(store.getValue().getLocation().x == location.x &&
                    store.getValue().getLocation().y ==location.y ){
                return false;
            }
        }

        return true;
    }

    public String getZone() {
        return zone;
    }

    public List<ProductTableInfo> getProductsDetails() {
        List<ProductTableInfo> productTableInfos = new ArrayList<>();
        ProductTableInfo productInfo;
        Product product;
        for (Map.Entry<Integer,Product> marketAreaProduct:products.entrySet()) {
            product = marketAreaProduct.getValue();
            productInfo = new ProductTableInfo(product.getProductID(),
                    product.getProductName(),
                    product.getProductCategory().toString(),
                    countHowManyStoresSellProduct(product),
                    getAveragePriceForProduct(product),
                    totalAmountSoldInMarket(product));
            productTableInfos.add(productInfo);

        }
        return productTableInfos;
    }

    public List<StoreProductInfo> getStoreProductInfo(String storeName) {
        List<StoreProductInfo> productInfoList = new ArrayList<>();
        for (Map.Entry<Integer,Store> store:stores.entrySet()) {
            if(store.getValue().getName().equals(storeName)){
                for (Map.Entry<Integer,StoreProduct>product:store.getValue().getProducts().entrySet()) {
                    productInfoList.add(new StoreProductInfo(product.getValue().getProductID(),
                            product.getValue().getProductName(),
                            product.getValue().getPrice(),
                            product.getValue().getProductCategory().toString()));
                }
            }
        }

        return productInfoList;
    }
}
