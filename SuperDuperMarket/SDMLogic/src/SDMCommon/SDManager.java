package SDMCommon;

import Alerts.FeedBackAlert;
import Alerts.IUserAlert;
import Alerts.NewStoreAlert;
import Alerts.OrderAlert;
import JSObjects.*;
import ProductTypes.*;
import SDMExceptions.XmlStoreSellsMultipleProductsWithSameIDException;
import SDMSale.Sale;
import jaxb.generated.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.awt.*;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.*;
import java.util.List;

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

        tempStoreMap = createStoresMapFromXml(superDuperMarketDescriptor, creatorName);
        tempProductMap = createProductMapFromXml(superDuperMarketDescriptor, tempStoreMap);

        return new MarketArea(tempProductMap, tempStoreMap, creatorName, superDuperMarketDescriptor.getSDMZone().getName());
    }

    private Map<Integer, Product> createProductMapFromXml(SuperDuperMarketDescriptor superDuperMarketDescriptor, Map<Integer, Store> tempStoreMap) throws Exception {

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
                throw new Exception("product with id: " + productToCheck.getValue().getProductID() + "is not sold by any store.");
            }
            itemSold = false;
        }


        return productMap;


    }

    private Map<Integer, Store> createStoresMapFromXml(SuperDuperMarketDescriptor superDuperMarketDescriptor, String creatorName) throws Exception {

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
                        createStoreProductMap(store, superDuperMarketDescriptor.getSDMItems()), store.getDeliveryPpk(), creatorName);
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
                saleToAdd.setStoreName(store.getName());
                sales.put(saleToAdd.getName(), saleToAdd);
            }
        }
        return sales;
    }


    private void validateMarketAreaDiscountName(SDMStore store, String name) throws Exception {
        String trimedSaleName = name.trim();
        int counter = 0;
        for (SDMDiscount discount : store.getSDMDiscounts().getSDMDiscount()) {
            if (discount.getName().trim().compareToIgnoreCase(trimedSaleName) == 0) {
                counter++;
            }
        }
        if (counter != 1) {
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
                throw new Exception("Store sells an item that was not found in market.");
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

    public Map<String, User> getUsers() {
        return users;
    }

    public Map<String, String> getUserNameAndRoleMap() {
        Map<String, String> userNameAndRoleMap = new HashMap<>();
        for (Map.Entry<String, User> user : users.entrySet()) {
            if (user.getValue() instanceof StoreOwner) {
                userNameAndRoleMap.put(user.getValue().getName(), "Store Owner");
            } else {
                userNameAndRoleMap.put(user.getValue().getName(), "Customer");
            }
        }
        return userNameAndRoleMap;
    }

    public List<SDMZoneInfo> getAllZonesInfo() {
        List<SDMZoneInfo> zoneInfoList = new ArrayList<>();
        for (Map.Entry<String, MarketArea> zone : marketAreaMap.entrySet()) {
            zoneInfoList.add(new SDMZoneInfo(zone.getValue()));
        }
        return zoneInfoList;
    }

    public List<Transaction> getallTransactions(String username) {
        return users.get(username).getUserTransactions();
    }

    public MarketArea getMarketArea(String zoneName) {
        MarketArea market = null;
        if (marketAreaMap.containsKey(zoneName)) {
            return marketAreaMap.get(zoneName);
        }
        return null;
    }

    public List<ProductTableInfo> getAllProductFromZoneInfo(String zoneName) {
        List<ProductTableInfo> productTableInfos = null;
        if (marketAreaMap.containsKey(zoneName)) {
            productTableInfos = marketAreaMap.get(zoneName).getProductsDetails();
        }
        return productTableInfos;
    }

    public List<StoreProductInfo> getStoreProducts(String storeName, String zoneName) throws Exception {
        List<StoreProductInfo> productList;
        try {
            productList = getMarketArea(zoneName).getStoreProductInfo(storeName);
        } catch (Exception e) {
            throw new Exception("Store doesnt exist in area.");
        }
        return productList;
    }

    public Sale[] getMyStaticSales(StoreLevelOrderJS[] storeLevelOrderJSArray, String zoneName) {
        Map<Integer, Double> productsByIdAndAmount = new HashMap<>();
        List<Sale> mySalesList = new ArrayList<>();
        Store store = null;
        MarketArea area = marketAreaMap.get(zoneName);
        for (StoreLevelOrderJS storeLevelOrderJS : storeLevelOrderJSArray) {
            for (Map.Entry<Integer, Store> marketStore : area.getStores().entrySet()) {
                if (marketStore.getValue().getName().equals(storeLevelOrderJS.getStoreName())) {
                    store = marketStore.getValue();
                }
            }
            for (regProduct regProd : storeLevelOrderJS.getRegProducts()) {
                productsByIdAndAmount.put(regProd.getProductID(), regProd.getAmount());
            }
            if (store != null) {
                mySalesList = store.getMySales(productsByIdAndAmount);
            }
        }
        Sale[] mySalesListArray = new Sale[mySalesList.size()];
        return mySalesList.toArray(mySalesListArray);
    }

    public Sale[] getMyDynamicSales(StoreLevelOrderJS[] storeOrderJS, String zoneName) {
        Store store = null;
        Map<Integer, Double> productsByIdAndAmount = new HashMap<>();
        List<Sale> mySalesList = new ArrayList<>();
        for (int i = 0; i < storeOrderJS.length; i++) {
            store = marketAreaMap.get(zoneName).getStores().get(storeOrderJS[i].getStoreID());
            for (regProduct regProd : storeOrderJS[i].getRegProducts()) {
                productsByIdAndAmount.put(regProd.getProductID(), regProd.getAmount());
            }
            if (store != null) {
                mySalesList.addAll(store.getMySales(productsByIdAndAmount));
            }
        }
        Sale[] mySalesListArray = new Sale[mySalesList.size()];
        return mySalesList.toArray(mySalesListArray);
    }

    public CustomerLevelOrderJS createBasicStaticCustomerOrder(CustomerLevelOrderJS customerOrderJS, String storeName, String zoneName) {
        Store store = null;
        StoreLevelOrderJS myOrder = customerOrderJS.getStoreOrders()[0];
        double productAmount;
        double totalPrice = 0;
        int productTypeAmount = 0;
        regProduct product;
        for (Map.Entry<Integer, Store> storeToCheck : marketAreaMap.get(zoneName).getStores().entrySet()) {
            if (storeToCheck.getValue().getName().equals(storeName)) {
                store = storeToCheck.getValue();
            }
        }
        if (store != null) {
            for (int i = 0; i < myOrder.getRegProducts().length; i++) {
                product = myOrder.getRegProducts()[i];
                productAmount = product.getAmount();
                product.setTotalProductPrice(productAmount * store.getProducts().get(product.getProductID()).getProductPrice());
                product.setProductName(marketAreaMap.get(zoneName).getProducts().get(product.getProductID()).getProductName());
                product.setCategory(marketAreaMap.get(zoneName).getProducts().get(product.getProductID()).getProductCategory().toString());
                totalPrice += product.getTotalProductPrice();
                productTypeAmount++;
            }
            initStoreJSOrder(myOrder, customerOrderJS.getLocation(), store.getLocation(), store.getDeliveryPPK(), store.getID(), totalPrice, productTypeAmount);
        }


        return customerOrderJS;
    }

    private void initStoreJSOrder(StoreLevelOrderJS myOrder, Point customerLocation, Point storeLocation, double storePPK, int storeID, double totalPrice, int productTypeAmount) {
        myOrder.setCustomerLocation(customerLocation);
        myOrder.setPPK(storePPK);
        myOrder.setLocation(storeLocation);
        myOrder.setOrderID(CustomerLevelOrder.getNextOrderID());
        myOrder.setDistanceFromCustomer(StoreLevelOrder.getDistanceFromCustomerToStore(customerLocation, storeLocation));
        myOrder.setDeliveryPrice(myOrder.getDistanceFromCustomer() * myOrder.getPPK());
        myOrder.setStoreID(storeID);
        myOrder.setTotalPrice(totalPrice);
        myOrder.setProductTypeAmount(productTypeAmount);
    }

    public CustomerLevelOrderJS createBasicDynamicCustomerOrder(CustomerLevelOrderJS customerOrderJS, String zoneName, int customerID, LocalDate date, Point location) {
        MarketArea area = marketAreaMap.get(zoneName);
        Map<Integer, Double> productsByIdAndAmount = new HashMap<>();
        for (StoreLevelOrderJS storeLevelOrderJS : customerOrderJS.getStoreOrders()) {
            for (regProduct regProd : storeLevelOrderJS.getRegProducts()) {
                productsByIdAndAmount.put(regProd.getProductID(), regProd.getAmount());
            }
        }

        CustomerLevelOrder myJavaOrder = area.createCheapestOrder(productsByIdAndAmount, customerID, date, location, customerOrderJS.getCustomerName());
        CustomerLevelOrder.OrderIDGenerator--;
        return convertJavaCustomerOrderToJSCustomerOrder(myJavaOrder, customerOrderJS, customerOrderJS.getOrderType(), zoneName);
    }

    private CustomerLevelOrderJS convertJavaCustomerOrderToJSCustomerOrder(CustomerLevelOrder myJavaOrder, CustomerLevelOrderJS myJSOrder, String orderType, String zoneName) {
        StoreLevelOrderJS[] storeOrdersJS = new StoreLevelOrderJS[myJavaOrder.getOrders().size()];
        StoreLevelOrder storeOrder;
        SoldProduct soldProduct;
        Store store;
        double totalPrice = 0;
        int totalTypeAmount = 0;
        for (int i = 0; i < myJavaOrder.getOrders().size(); i++) {
            storeOrder = myJavaOrder.getOrders().get(i);
            store = marketAreaMap.get(zoneName).getStores().get(storeOrder.getStoreID());
            regProduct[] regProd = new regProduct[storeOrder.getSoldProducts().size()];
            for (int j = 0; j < storeOrder.getSoldProducts().size(); j++) {
                soldProduct = storeOrder.getSoldProducts().get(j);
                regProd[j] = new regProduct(soldProduct.getProductID(), soldProduct.getAmountSoldInOrder());
                regProd[j].setTotalProductPrice(soldProduct.getTotalPrice());
                regProd[j].setProductName(marketAreaMap.get(zoneName).getProducts().get(regProd[j].getProductID()).getProductName());
                regProd[j].setCategory(marketAreaMap.get(zoneName).getProducts().get(regProd[j].getProductID()).getProductCategory().toString());
                totalPrice += soldProduct.getTotalPrice();
                totalTypeAmount++;
            }
            storeOrdersJS[i] = new StoreLevelOrderJS(regProd, storeOrder.getStoreName());
            initStoreJSOrder(storeOrdersJS[i], myJSOrder.getLocation(), store.getLocation(), store.getDeliveryPPK(), store.getID(), totalPrice, totalTypeAmount);
        }
        return new CustomerLevelOrderJS(orderType, storeOrdersJS, myJSOrder.getLocation(), myJSOrder.getDate());
    }

    public void placeOrderInMarket(CustomerLevelOrderJS customerLevelOrderJS, String zoneName, String userName) {
        MarketArea area = marketAreaMap.get(zoneName);
        CustomerLevelOrder myCustomerOrder = convertFullJavaCustomerOrderToJSCustomerOrder(customerLevelOrderJS);
        area.placeOrderInSDM(myCustomerOrder);
        Customer customer = (SDMCommon.Customer) (users.get(userName));
        customer.getOrderHistory().add(myCustomerOrder);
        PaymentHandleFunc(myCustomerOrder, area, customer);
    }

    private void PaymentHandleFunc(CustomerLevelOrder myCustomerOrder, MarketArea area, Customer customer) {
        customer.takeMoneyOutOfUserBankAccount(myCustomerOrder.getDeliveryPrice() + myCustomerOrder.getTotalProductPrice());
        Store store;
        StoreOwner storeOwner;
        for (StoreLevelOrder storeLevelOrder : myCustomerOrder.getOrders()) {
            store = area.getStores().get(storeLevelOrder.getStoreID());
            storeOwner = (StoreOwner) users.get(store.getOwnerName());
            storeOwner.addFuds(storeLevelOrder.getTotalProductsPrice() + storeLevelOrder.getDeliveryPrice(), new Date());
        }
    }

    private CustomerLevelOrder convertFullJavaCustomerOrderToJSCustomerOrder(CustomerLevelOrderJS customerLevelOrderJS) {
        List<StoreLevelOrder> storeLevelOrderList = new ArrayList<>();
        List<SoldProduct> soldProductsList = new ArrayList<>();
        List<SaleProduct> saleProductList = new ArrayList<>();
        ProductCategory category;
        StoreLevelOrder storeLevelOrderToAdd;
        for (StoreLevelOrderJS storeOrder : customerLevelOrderJS.getStoreOrders()) {
            for (regProduct regProd : storeOrder.getRegProducts()) {
                if (regProd.getCategory().equals("Quantity")) {
                    category = ProductCategory.Quantity;
                } else {
                    category = ProductCategory.Weight;
                }
                soldProductsList.add(new SoldProduct(regProd.getProductID(), regProd.getProductName(),
                        category, (regProd.getTotalProductPrice() / regProd.getAmount()), storeOrder.getStoreID(),
                        regProd.getAmount(), regProd.getTotalProductPrice()));
            }
            if (storeOrder.getSaleProducts() != null) {
                for (SaleProductJS saleProductJS : storeOrder.getSaleProducts()) {
                    if (saleProductJS.getCategory().equals("Quantity")) {
                        category = ProductCategory.Quantity;
                    } else {
                        category = ProductCategory.Weight;
                    }
                    saleProductList.add(new SaleProduct(saleProductJS.getProductID(), saleProductJS.getProductName(), category, saleProductJS.getTotalProductPrice() / saleProductJS.getAmount(),
                            storeOrder.getStoreID(), saleProductJS.getSaleName(), saleProductJS.getAmount()));
                }
            }

            storeLevelOrderToAdd = new StoreLevelOrder(storeOrder.getOrderID(), soldProductsList,
                    storeOrder.getRegProducts().length,
                    storeOrder.getTotalPrice(), storeOrder.getStoreID(), customerLevelOrderJS.getCustomerID(),
                    storeOrder.getDeliveryPrice(), LocalDate.parse(customerLevelOrderJS.getDate()), storeOrder.getStoreName(),
                    CustomerLevelOrder.getNextOrderID(), customerLevelOrderJS.getCustomerName(), customerLevelOrderJS.getLocation());
            if (saleProductList.size() > 0) {
                storeLevelOrderToAdd.setProductSoldOnSale(saleProductList);
            }
            storeLevelOrderList.add(storeLevelOrderToAdd);
            soldProductsList = new ArrayList<>();
            saleProductList = new ArrayList<>();
        }
        return new CustomerLevelOrder(storeLevelOrderList, customerLevelOrderJS.getLocation());
    }

    public List<Store> getStoreByOwner(String ownerName, String zoneName) {
        List<Store> storesByOwner = new ArrayList<>();
        MarketArea area = marketAreaMap.get(zoneName);
        for (Map.Entry<Integer, Store> store : area.getStores().entrySet()) {
            if (store.getValue().getOwnerName().equals(ownerName)) {
                storesByOwner.add(store.getValue());
            }
        }
        return storesByOwner;
    }

    public void placeFeedBackInMarket(Feedback customerFeedBack, String zoneName, String StoreID) {
        MarketArea area = marketAreaMap.get(zoneName);
        Store store = area.getStores().get(Integer.parseInt(StoreID));
        store.addFeedBackToStore(customerFeedBack);
    }

    public void addStoreToMarket(StoreJS storeJS, String zoneName, String userName) {
        Map<Integer, StoreProduct> storeProductMap = new HashMap<>();
        Product product;
        StoreProduct storeProduct;
        MarketArea area = marketAreaMap.get(zoneName);
        for (StoreProductJS productToAdd : storeJS.getStoreProducts()) {
            product = area.getProducts().get(Integer.parseInt(productToAdd.getProductID()));
            storeProduct = new StoreProduct(product, Double.parseDouble(productToAdd.getPrice()), Integer.parseInt(storeJS.getStoreID()));
            storeProductMap.put(storeProduct.getProductID(), storeProduct);
        }
        Point storeLocation = new Point(Integer.parseInt(storeJS.getLocationX()), Integer.parseInt(storeJS.getLocationY()));
        Store storeToAdd = new Store(storeJS.getStoreName(), storeLocation, Integer.parseInt(storeJS.getStoreID()), storeProductMap, Double.parseDouble(storeJS.getPPK()), userName);
        area.getStores().put(storeToAdd.getID(),storeToAdd);
    }

    public void addOrderAlertToUser(StoreLevelOrderJS storeLevelOrderJS, String zoneName, String userName) {
        OrderAlert orderAlert;
        StoreLevelOrder myOrder = null;
        List<StoreLevelOrder> storeOrders = marketAreaMap.get(zoneName).getStores().get(storeLevelOrderJS.getStoreID()).getStoreOrderHistory();
        for (StoreLevelOrder storeOrder: storeOrders) {
            if(storeOrder.getOrderID() == storeLevelOrderJS.getOrderID()){
                myOrder = storeOrder;
            }
        }
        User userStoreOwner = users.get(marketAreaMap.get(zoneName).getStores().get(storeLevelOrderJS.getStoreID()).getOwnerName());
        StoreOwner storeOwner = (StoreOwner)(userStoreOwner);
        orderAlert = new OrderAlert(storeLevelOrderJS.getOrderID(),myOrder.getCustomerName(),myOrder.getStoreName(),myOrder.getAmountOfProducts(),myOrder.getTotalProductsPrice(),myOrder.getDeliveryPrice());
        storeOwner.addAlert(orderAlert);
    }

    public List<IUserAlert> AlertsForUser(StoreOwner storeOwner) {
        return storeOwner.getAlertList();
    }

    public void addFeedbackAlertToUser(Feedback customerFeedBack,String zoneName,String storeID) {
        FeedBackAlert feedbackToAdd;
        Store storeToAddFeedBackTo = marketAreaMap.get(zoneName).getStores().get(Integer.parseInt(storeID));
        feedbackToAdd = new FeedBackAlert(customerFeedBack.getRating(),storeToAddFeedBackTo.getName(),customerFeedBack.getFeedbackText());
        User userStoreOwner = users.get(storeToAddFeedBackTo.getOwnerName());
        StoreOwner storeOwner = (StoreOwner)(userStoreOwner);
        storeOwner.addAlert(feedbackToAdd);
    }

    public void addNewStoreAlertToUser(StoreJS storeJS, String zoneName, String areaManagerUserName,String storeOwnerName) {
        NewStoreAlert storeAlertToAdd;
        Point newStoreLocation = new Point(Integer.parseInt(storeJS.getLocationX()),Integer.parseInt(storeJS.getLocationY()));
        int amountOfProductsInArea = marketAreaMap.get(zoneName).getProducts().size();
        storeAlertToAdd = new NewStoreAlert(storeOwnerName,storeJS.getStoreName(),newStoreLocation,Integer.toString(storeJS.getStoreProducts().length),Integer.toString(amountOfProductsInArea));
        User userStoreOwner = users.get(areaManagerUserName);
        StoreOwner storeOwner = (StoreOwner)(userStoreOwner);
        storeOwner.addAlert(storeAlertToAdd);
    }

    public void addProductToMarket(Map<Integer,Double> storeMap,String productName, String productCategory, String zoneName) {
        Product productToAdd;
        MarketArea area = marketAreaMap.get(zoneName);
        int productID = 1;
        boolean productIDExists = true;
        while(productIDExists){
            if(!area.getProducts().containsKey(productID)){
                productIDExists = false;
            }else{
                productID++;
            }
        }
        ProductCategory cat;
        if(productCategory.equals("quantity")){
            cat = ProductCategory.Quantity;
        }
        else{
            cat = ProductCategory.Weight;
        }
        productToAdd = new Product(productID,productName,cat);
        area.getProducts().put(productID,productToAdd);
        Store store;
        for (Map.Entry<Integer,Double> storeIDProductPrice:storeMap.entrySet()) {
            store = area.getStores().get(storeIDProductPrice.getKey());
            store.addNewProductToStore(new StoreProduct(productToAdd,storeIDProductPrice.getValue(),store.getID()));
        }
    }
}


