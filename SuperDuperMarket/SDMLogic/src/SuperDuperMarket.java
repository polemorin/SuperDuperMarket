

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.awt.*;
import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import jaxb.generated.*;

public class SuperDuperMarket {
    private boolean xmlLoaded;
    private Map<Integer, Product> products;
    private Map<Integer, User> users;
    private Map<Integer, Store> stores;
    private Map<Integer, CustomerLevelOrder> orderHistory;

    public SuperDuperMarket() {
        xmlLoaded = false;
        this.products = new HashMap<Integer, Product>();
        users = new HashMap<Integer, User>();
        stores = new HashMap<Integer, Store>();
        orderHistory = new HashMap<Integer, CustomerLevelOrder>();
        users.put(0, new User("Admin", 0));
    }

    //------------------------Menu Option 3------------------------------
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

    public boolean isXmlLoaded() {
        return xmlLoaded;
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

    //--------------------option 4------------------------
    public boolean isStoreIDValid(int storeID) {
        return stores.containsKey(storeID);
    }

    public boolean isValidDate(Date d) {
        return d.after(new Date());
    }

    public boolean isValidLocationInSDM(Point location) {
        return location.x >= 1 && location.x <= 50 && location.y >= 1 && location.y <= 50;
    }

    public boolean isUserLocationDifferentFromStoreLocation(Point userLocation, Point storeLocation) {
        return !userLocation.equals(storeLocation);
    }

    public boolean isValidProductID(int productID) {
        return products.containsKey(productID);
    }

    public void placeOrderInSDM(CustomerLevelOrder orderToAdd, int userID) {
        users.get(userID).addOrderToOrderHistory(orderToAdd);
        orderHistory.put(orderToAdd.getOrderID(), orderToAdd);
        for (StoreLevelOrder storeOrder:orderToAdd.getOrders()) {
            stores.get(storeOrder.getStoreID()).updateStoreAfterOrder(storeOrder);
        }
    }

    public boolean doesUserExistInSystem(User user) {
        return users.containsKey(user.getID());
    }

    public void writeOrdersToFile(String filePath) throws NoOrdersInSystemException,FileNotFoundException,
            SecurityException,UnsupportedEncodingException,IOException {

        if (orderHistory.size() == 0) {
            throw new NoOrdersInSystemException("No orders in system. Can't write to file.");
        }
        List<String> ordersStringToFile;
        try (Writer out1 = new BufferedWriter(
                new OutputStreamWriter(
                        new FileOutputStream(filePath), "UTF-8"))) {
            out1.write(((Integer) (orderHistory.size())).toString() + "\n");
            for (Map.Entry<Integer, CustomerLevelOrder> customerOrder : orderHistory.entrySet()) {
                ordersStringToFile = customerOrder.getValue().getCustomerLevelOrderStringListToFile();
                for (int j = 0; j < ordersStringToFile.size(); j++) {
                    out1.write(ordersStringToFile.get(j) + "\n");
                }
            }

        }
    }
//--------------option 8-----------------
    public void readOrdersFromFile(String path) throws ParseException,IOException{
        List<String> linesFromFile = new ArrayList<String>();
        String line;
        int numOfCustomerOrders;
        CustomerLevelOrder orderToAdd;
        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(path)))) {
            numOfCustomerOrders = Integer.parseInt(in.readLine());
            clearOrderHistoryFromStoresAndMarket();
            for(int i = 0; i < numOfCustomerOrders ; i++){
                line = in.readLine();
                while(!line.equals("CustomerDone")){
                    linesFromFile.add(line);
                    line = in.readLine();
                }
                orderToAdd = CustomerLevelOrder.getCustomerLevelOrderFromTxtFile(linesFromFile);
                for (StoreLevelOrder storeLevelOrder:orderToAdd.getOrders()) {
                    stores.get(storeLevelOrder.getStoreID()).updateStoreAfterOrder(storeLevelOrder);
                }
                

                orderHistory.put(orderToAdd.getOrderID(),orderToAdd);//add exception
                linesFromFile.clear();
            }
        }
    }

    private void clearOrderHistoryFromStoresAndMarket() {
        orderHistory.clear();

        for (Map.Entry<Integer,Store> store:stores.entrySet()) {
            store.getValue().clearOrderHistory();
        }
    }

    public Map<Integer, Product> getProducts() {
        return products;
    }

    public Map<Integer, User> getUsers() {
        return users;
    }

    public Map<Integer, Store> getStores() {
        return stores;
    }

    public Map<Integer, CustomerLevelOrder> getOrderHistory() {
        return orderHistory;
    }

    public CustomerLevelOrder createCheapestOrder(Map<Integer, Double> shoppingList, int customerID, Date date, Point location) {
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
                storeOrderToAdd = new StoreLevelOrder(stores.get(storeID), customerID, date, location);
                storeOrderToAdd.addProductToOrder(productToAdd);
                storeListForOrder.add(storeOrderToAdd);
            }
            storeAlreadyInList = false;
        }
        return new CustomerLevelOrder(storeListForOrder);
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

    public void loadXmlFile() throws Exception{
        Scanner scanner = new Scanner(System.in);
        String xmlPath;
        Map<Integer,Store> tempStoreMap;
        Map<Integer,Product> tempProductMap;

        xmlPath = scanner.nextLine();
        File f = new File(xmlPath);
        if (f.exists()) {
            if (xmlPath.endsWith("xml")) {

                tempStoreMap = createStoresMapFromXml(f.toPath().getFileName().toString());
                tempProductMap = createProductMapFromXml(f.toPath().getFileName().toString(),tempStoreMap);

                xmlLoaded = true;
            } else {
                throw new Exception("File doesn't end with .xml");
            }
        } else {
            throw new Exception("File doesnt exist.");
        }
        orderHistory.clear();
        stores.clear();
        products.clear();

        stores = tempStoreMap;
        products = tempProductMap;
    }

    private final static String JAXB_XML_GAME_PACKAGE_NAME = "jaxb.generated";

    public static <K, V> Set<K> getSetOfDictionary(Map<K, V> mapToCreateSetFrom) {
        Set<K> setOfKeys = new HashSet<K>();
        for (K key : mapToCreateSetFrom.keySet()) {
            setOfKeys.add(key);
        }
        return setOfKeys;
    }

    public static SuperDuperMarketDescriptor deserializeFrom(InputStream in) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_GAME_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        return (SuperDuperMarketDescriptor) u.unmarshal(in);
    }

    public Map<Integer,Store> createStoresMapFromXml(String xmlName) throws JAXBException,XmlStoreSellProductNotInMarketException, XmlStoreSellsMultipleProductsWithSameIDException, XmlProductCategoryNotRecognizedException, XmlLocationOutOfBoundsException, XmlMultipleStoresShareLocationException, XmlMultipleStoresShareIDException,Exception {
        InputStream inputStream = SuperDuperMarket.class.getResourceAsStream(xmlName);
        Map<Integer, Store> storesMap = new HashMap<Integer, Store>();
        SuperDuperMarketDescriptor descriptor = deserializeFrom(inputStream);
        SDMStores shops = descriptor.getSDMStores();
        List<SDMStore> listOfStores = shops.getSDMStore();
        Store storeToAdd;

        for (SDMStore store : listOfStores) {
            Location location = store.getLocation();
            Point p = new Point(location.getX(), location.getY());
            if (!isValidLocationInSDM(p)) {
                throw new XmlLocationOutOfBoundsException("Store location out of bounds.", p);
            } else if (storesMap != null && doesStoreExistInLocation(storesMap, p)) {
                throw new XmlMultipleStoresShareLocationException("Multiple stores with same location.", p);

            } else if (storesMap.containsKey(store.getId())) {
                throw new XmlMultipleStoresShareIDException("Multiple Stores with the same ID.", store.getId());
            } else {
                storeToAdd = new Store(store.getName(), p, store.getId(), createStoreProductMap(store, descriptor.getSDMItems()), store.getDeliveryPpk());
                storesMap.put(storeToAdd.getID(), storeToAdd);
            }
        }


        inputStream.close();
        return storesMap;
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

    private Map<Integer,Product> createProductMapFromXml(String xmlName, Map<Integer,Store> tempStoreMap ) throws JAXBException, XmlProductsShareIDException, XmlProductCategoryNotRecognizedException, XmlProductIsntSoldByStoresException {
        InputStream inputStream = SuperDuperMarket.class.getResourceAsStream(xmlName);
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
}
