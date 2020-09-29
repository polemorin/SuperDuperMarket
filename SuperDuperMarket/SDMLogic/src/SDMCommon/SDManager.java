package SDMCommon;

import ProductTypes.Product;
import ProductTypes.ProductCategory;
import ProductTypes.StoreProduct;
import SDMExceptions.XmlStoreSellProductNotInMarketException;
import SDMExceptions.XmlStoreSellsMultipleProductsWithSameIDException;
import SDMSale.Sale;
import jaxb.generated.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.awt.*;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SDManager {
    Map<String, MarketArea> marketAreaMap;
    Map<String, User> users;
    private final String Customer = "Customer";

    public SDManager() {
        marketAreaMap = new HashMap<>();
        users = new HashMap<>();
    }

    public boolean isUsernameAvailable(String userName) {
        return !users.containsKey(userName);
    }

    public void addUser(String usernameFromParameter, String role) {
        User user;
        if (role.equals(Customer)) {
            user = new Customer(usernameFromParameter);
        } else {
            user = new StoreOwner(usernameFromParameter);
        }
        users.put(usernameFromParameter, user);
    }

    public void loadInfoFromXML(InputStream inputStream, String creatorName) throws Exception {

        JAXBContext jaxbContext = JAXBContext.newInstance("jaxb.generated");

        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        SuperDuperMarketDescriptor superDuperMarketDescriptor =
                (SuperDuperMarketDescriptor) jaxbUnmarshaller.unmarshal(inputStream);
        String zoneName = superDuperMarketDescriptor.getSDMZone().getName();
        if (marketAreaMap.containsKey(zoneName)) {
            throw new Exception("A zone name: " + zoneName + " already exists in Super Duper Market. File was not loaded.");
        }

        MarketArea currentMarketArea = createMarketAreaFromDescriptor(superDuperMarketDescriptor, creatorName);
        marketAreaMap.put(zoneName, currentMarketArea);

    }

    private MarketArea createMarketAreaFromDescriptor(SuperDuperMarketDescriptor superDuperMarketDescriptor, String creatorName) throws Exception {
        Map<Integer, Store> tempStoreMap;
        Map<Integer, Product> tempProductMap;

        tempStoreMap = createStoresMapFromXml(superDuperMarketDescriptor);
        tempProductMap = createProductMapFromXml(superDuperMarketDescriptor, tempStoreMap);

        return new MarketArea(tempProductMap, tempStoreMap, creatorName, superDuperMarketDescriptor.getSDMZone().getName());
    }

    private Map<Integer, Product> createProductMapFromXml(SuperDuperMarketDescriptor superDuperMarketDescriptor, Map<Integer, Store> tempStoreMap) throws Exception{

        Map<Integer, Product> productMap = new HashMap<Integer, Product>();
        SDMItems items = superDuperMarketDescriptor.getSDMItems();
        List<SDMItem> listOfItems = items.getSDMItem();
        Product productToAdd;
        ProductCategory category = null;
        boolean itemSold = false;

        for (SDMItem itm : listOfItems) {
            if (productMap.containsKey(itm.getId())) {
                throw new Exception("Product with same id alreay exists. ID: " + itm.getId());
            }
            if (itm.getPurchaseCategory().equals("Quantity")) {
                category = ProductCategory.Quantity;
            } else if (itm.getPurchaseCategory().equals("Weight")) {
                category = ProductCategory.Weight;
            } else {
                throw new Exception("Product category not recognize in SDM. Product category: " + itm.getPurchaseCategory());
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
                throw new Exception("product with id: "  + productToCheck.getValue().getProductID() + "is not sold by any store.");
            }
            itemSold = false;
        }

        return productMap;


    }

    private Map<Integer, Store> createStoresMapFromXml(SuperDuperMarketDescriptor superDuperMarketDescriptor) throws Exception {

        Map<Integer, Store> storesMap = new HashMap<Integer, Store>();


        SDMStores shops = superDuperMarketDescriptor.getSDMStores();
        List<SDMStore> listOfStores = shops.getSDMStore();
        Store storeToAdd;

        for (SDMStore store : listOfStores) {
            Location location = store.getLocation();
            Point p = new Point(location.getX(), location.getY());
            if (!isValidLocationInSDM(p)) {
                throw new Exception(store.getName().toString() + "'s location out of bounds. ");
            } else if (storesMap != null && doesStoreExistInLocationFromXML(storesMap, p)) {
                throw new Exception("Multiple stores with same location.");

            } else if (storesMap.containsKey(store.getId())) {
                throw new Exception("Multiple Stores with the same ID: " + store.getId() + ".");
            } else {

                storeToAdd = new Store(store.getName(), p, store.getId(),
                        createStoreProductMap(store, superDuperMarketDescriptor.getSDMItems()), store.getDeliveryPpk());
                storeToAdd.initSalesFromXML(generateSalesFromXml(store, superDuperMarketDescriptor));
                storesMap.put(storeToAdd.getID(), storeToAdd);
            }
        }
        return storesMap;
    }

    private Map<String, Sale> generateSalesFromXml(SDMStore store, SuperDuperMarketDescriptor superDuperMarketDescriptor) throws Exception {
        Map<String, Sale> sales = new HashMap<String, Sale>();
        SDMDiscounts discounts = store.getSDMDiscounts();
        String ifYouBuyProductName = "";
        Map<Integer, String> thenYouGetProductName = new HashMap<>();
        Sale saleToAdd;
        if (discounts != null) {
            for (SDMDiscount discount : discounts.getSDMDiscount()) {

                doesMarketAreaSellXmlDiscountItems(discount, superDuperMarketDescriptor);
                validateMarketAreaDiscountItems(store, discount.getIfYouBuy(), discount.getThenYouGet(), superDuperMarketDescriptor);
                validateMarketAreaDiscountName(store, discount.getName());
                for (SDMItem item : superDuperMarketDescriptor.getSDMItems().getSDMItem()) {
                    if (discount.getIfYouBuy().getItemId() == item.getId()) {
                        ifYouBuyProductName = item.getName();
                    }
                    thenYouGetProductName.put(item.getId(), item.getName());
                }

                saleToAdd = new Sale(discount.getName(), discount.getIfYouBuy(), discount.getThenYouGet(), ifYouBuyProductName, thenYouGetProductName);
                sales.put(saleToAdd.getName(), saleToAdd);
            }
        }
        return sales;
    }


    private void validateMarketAreaDiscountName(SDMStore store, String name) throws Exception{
        String trimedSaleName = name.trim();
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

    private void validateMarketAreaDiscountItems(SDMStore store, IfYouBuy ifYouBuy, ThenYouGet thenYouGet, SuperDuperMarketDescriptor superDuperMarketDescriptor) throws Exception {
        boolean foundIfYouBuyItemIDInStore = false;
        boolean foundThenYouGetItemIDInStore = false;

        for (SDMSell itemToCheck : store.getSDMPrices().getSDMSell()) { // search for If You Buy Item ID in items
            if (itemToCheck.getItemId() == ifYouBuy.getItemId()) {
                foundIfYouBuyItemIDInStore = true;
            }
        }
        if (!foundIfYouBuyItemIDInStore) {
            throw new Exception("Store doesn't sell ifYouBuy product.Product ID: " + ifYouBuy.getItemId());
        }
        if (ifYouBuy.getQuantity() <= 0) {
            throw new Exception("If you buy item quantity is a non positive number.");
        }

        for (SDMOffer offerToCheck : thenYouGet.getSDMOffer()) {//search all the offer's ID in store and checke quantity and for addiotnal is positive
            for (SDMSell itemToCheck : store.getSDMPrices().getSDMSell()) {
                if (itemToCheck.getItemId() == offerToCheck.getItemId()) {
                    foundThenYouGetItemIDInStore = true;
                }
                if (offerToCheck.getQuantity() <= 0) {
                    throw new Exception("If you buy item quantity is a non positive number.");
                }
                if (offerToCheck.getForAdditional() < 0) {
                    throw new Exception("Invalid sale \"for additional\" is negative.");
                }
            }
            if (!foundThenYouGetItemIDInStore) {
                throw new Exception("Store doesn't sell \"Then you get\" product");
            }
        }

        for (SDMItem item : superDuperMarketDescriptor.getSDMItems().getSDMItem()) {// check category match with quantity in sale.
            if (item.getId() == ifYouBuy.getItemId() && item.getPurchaseCategory().compareToIgnoreCase("Quantity") == 0 &&

                    ifYouBuy.getQuantity() != Math.floor(ifYouBuy.getQuantity())) {
                throw new Exception("Quantity should be int and is a double.");
            }
        }
        for (SDMOffer offer : thenYouGet.getSDMOffer()) {// check category match with quantity in sale.
            for (SDMItem item : superDuperMarketDescriptor.getSDMItems().getSDMItem()) {
                if (offer.getItemId() == item.getId() &&

                        item.getPurchaseCategory().compareToIgnoreCase("Quantity") == 0 &&

                        offer.getQuantity() != Math.floor(offer.getQuantity())) {

                    throw new Exception("Quantity should be int and is a double.");
                }
            }


        }
    }

    private void doesMarketAreaSellXmlDiscountItems(SDMDiscount discount, SuperDuperMarketDescriptor superDuperMarketDescriptor) throws Exception {
        boolean foundIDinMarket = false;
        boolean foundIfYouBuyIDinMarket = false;
        for (SDMOffer offerToCheck : discount.getThenYouGet().getSDMOffer()) {
            for (SDMItem itemToCheck : superDuperMarketDescriptor.getSDMItems().getSDMItem()) {
                if (itemToCheck.getId() == offerToCheck.getItemId()) {
                    foundIDinMarket = true;
                }
            }
            if (!foundIDinMarket) {
                throw new Exception("Market does not sell \"then you get\" item");
            }
            foundIDinMarket = false;
        }
        for (SDMItem itemToCheck : superDuperMarketDescriptor.getSDMItems().getSDMItem()) {
            if (discount.getIfYouBuy().getItemId() == itemToCheck.getId()) {
                foundIfYouBuyIDinMarket = true;
            }
        }
        if (!foundIfYouBuyIDinMarket) {
            throw new Exception("Market does not sell \"if you buy \" item.");
        }

    }

    private Map<Integer, StoreProduct> createStoreProductMap(SDMStore store, SDMItems sdmItems) throws Exception {
        StoreProduct product;
        SDMItem sdmitem = null;
        ProductCategory category = null;
        boolean productFoundInItems = false;
        Map<Integer, StoreProduct> products = new HashMap<Integer, StoreProduct>();
        for (SDMSell item : store.getSDMPrices().getSDMSell()) {
            for (SDMItem sdmitem1 : sdmItems.getSDMItem()) {
                if (sdmitem1.getId() == item.getItemId()) {
                    sdmitem = sdmitem1;
                    if (sdmitem.getPurchaseCategory().equals("Quantity")) {
                        category = ProductCategory.Quantity;
                    } else if (sdmitem.getPurchaseCategory().equals("Weight")) {
                        category = ProductCategory.Weight;
                    } else {
                        throw new Exception("Product category not recognized in SDM. product category is: + " + sdmitem.getPurchaseCategory().toString());
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

    private boolean doesStoreExistInLocationFromXML(Map<Integer, Store> storesMap, Point p) {
        for (Map.Entry<Integer, Store> store : storesMap.entrySet()) {
            if (store.getValue().getLocation() == p) {
                return true;
            }
        }
        return false;

    }

    private boolean isValidLocationInSDM(Point location) {
        return location.x >= 1 && location.x <= 50 && location.y >= 1 && location.y <= 50;

    }
}


