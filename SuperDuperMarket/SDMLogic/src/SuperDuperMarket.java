

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.awt.*;
import java.io.*;
import java.text.ParseException;
import java.util.*;
import java.util.List;

import ProductTypes.*;
import ProductTypes.Product;
import ProductTypes.ProductCategory;
import ProductTypes.StoreProduct;
import SDMExceptions.*;
import SDMSale.Sale;
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
                storeOrderToAdd = new StoreLevelOrder(stores.get(storeID), customerID, date, location,CustomerLevelOrder.getNextOrderID());
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
    public void loadXmlFileFromFileChooser(File selectedFile) throws Exception {
        Map<Integer,Store> tempStoreMap;
        Map<Integer,Product> tempProductMap;
        Map<Integer,User> tempUserMap;
        if (selectedFile.exists()) {
            if (selectedFile.getPath().endsWith("xml")) {

                tempStoreMap = createStoresMapFromXml(selectedFile.getAbsolutePath());
                tempProductMap = createProductMapFromXml(selectedFile.getAbsolutePath(),tempStoreMap);
                tempUserMap = createUserMapFromXML(selectedFile.getAbsolutePath(),tempStoreMap);
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
        users = tempUserMap;
    }



    public void loadXmlFile() throws Exception{
        Scanner scanner = new Scanner(System.in);
        String xmlPath;
        Map<Integer,Store> tempStoreMap;
        Map<Integer,Product> tempProductMap;
        Map<Integer,User> tempUserMap;
        xmlPath = scanner.nextLine();
        File f = new File(xmlPath);
        if (f.exists()) {
            if (xmlPath.endsWith("xml")) {

                tempStoreMap = createStoresMapFromXml(f.toPath().getFileName().toString());
                tempProductMap = createProductMapFromXml(f.toPath().getFileName().toString(),tempStoreMap);
                tempUserMap = createUserMapFromXML(f.toPath().getFileName().toString(),tempStoreMap);
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
        users = tempUserMap;
    }

    private Map<Integer,User> createUserMapFromXML(String xmlName,Map<Integer,Store> tempStoreMap) throws Exception {
        Map<Integer,User> userMapToAdd = new HashMap<>();
        InputStream inputStream = new FileInputStream(xmlName);
        SuperDuperMarketDescriptor descriptor = deserializeFrom(inputStream);
        SDMCustomers customersFromXML = descriptor.getSDMCustomers();
        Point location;
        for (SDMCustomer customer:customersFromXML.getSDMCustomer()) {
            validateSDMCustomers(customer,userMapToAdd,tempStoreMap);
            location = new Point(customer.getLocation().getX(),customer.getLocation().getY());
            userMapToAdd.put(customer.getId(),new User(customer.getName(),customer.getId(),location));
        }

        return userMapToAdd;
    }

    private void validateSDMCustomers(SDMCustomer customer, Map<Integer,User> userMapToAdd, Map<Integer,Store> tempStoreMap) throws Exception{
        if(userMapToAdd.containsKey(customer.getId())){
            throw new Exception("User with this ID: "+customer.getId() + ", already exists");
        }
        Point location = new Point(customer.getLocation().getX(),customer.getLocation().getY());
        if(!isValidLocationInSDM(location)){
            throw new Exception("Location out of range, location: "+customer.getLocation().getX()+","+customer.getLocation().getY());
        }
        isLocationAvailableFromXMLInput(location,userMapToAdd,tempStoreMap);
    }

    private void isLocationAvailableFromXMLInput(Point location, Map<Integer,User> userMap, Map<Integer,Store> tempStoreMap)throws Exception {
        for (Map.Entry<Integer,User> user: userMap.entrySet()) {
            if(user.getValue().getLocation().equals(location)){
                throw new Exception("More then one user in location, location:" +location.x+","+location.y );
            }
        }
        for (Map.Entry<Integer,Store> storesToCheck:tempStoreMap.entrySet()) {
            if(storesToCheck.getValue().getLocation().equals(location)){
                throw new Exception("both user and store share this location: "+location.x+","+location.y);
            }
        }
    }

    private final static String JAXB_XML_GAME_PACKAGE_NAME = "jaxb.generated";

    public static <K, V> Set<K> getSetOfDictionary(Map<K, V> mapToCreateSetFrom) {
        Set<K> setOfKeys = new HashSet<K>(mapToCreateSetFrom.keySet());
        return setOfKeys;
    }

    public static SuperDuperMarketDescriptor deserializeFrom(InputStream in) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_GAME_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        return (SuperDuperMarketDescriptor) u.unmarshal(in);
    }

    public Map<Integer,Store> createStoresMapFromXml(String xmlName) throws JAXBException,XmlStoreSellProductNotInMarketException, XmlStoreSellsMultipleProductsWithSameIDException, XmlProductCategoryNotRecognizedException, XmlLocationOutOfBoundsException, XmlMultipleStoresShareLocationException, XmlMultipleStoresShareIDException,Exception {
        InputStream inputStream = new FileInputStream(xmlName);
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
                storeToAdd.initSalesFromXML(generateSalesFromXml(store,descriptor));
                storesMap.put(storeToAdd.getID(), storeToAdd);
            }
        }
        inputStream.close();
        return storesMap;
    }

    private Map<String, Sale> generateSalesFromXml(SDMStore store,SuperDuperMarketDescriptor descriptor)throws Exception {
        Map<String,Sale> sales = new HashMap<String, Sale>();
        SDMDiscounts discounts =store.getSDMDiscounts();
        Sale saleToAdd;
        if(discounts != null){
            for (SDMDiscount discount: discounts.getSDMDiscount()) {

                doesMarketSellXmlDiscountItems(discount,descriptor);
                validateSDMDiscountItems(store, discount.getIfYouBuy(), discount.getThenYouGet(),descriptor);
                validateSDMDiscountName(store, discount.getName());

                saleToAdd = new Sale(discount.getName(),discount.getIfYouBuy(),discount.getThenYouGet());
                sales.put(saleToAdd.getName(),saleToAdd);
            }
        }
        return sales;
    }

    private void validateSDMDiscountItems(SDMStore store, IfYouBuy ifYouBuy, ThenYouGet thenYouGet,SuperDuperMarketDescriptor descriptor) throws Exception{
        boolean foundIfYouBuyItemIDInStore = false;
        boolean foundThenYouGetItemIDInStore = false;

        for (SDMSell itemToCheck : store.getSDMPrices().getSDMSell()) { // search for If You Buy Item ID in items
            if(itemToCheck.getItemId() == ifYouBuy.getItemId()){
                foundIfYouBuyItemIDInStore = true;
            }
        }
        if(!foundIfYouBuyItemIDInStore){
            throw new Exception("Store doesn't sell ifYouBuy product.Product ID: "+ ifYouBuy.getItemId());
        }
        if(ifYouBuy.getQuantity() <= 0){
            throw new Exception("If you buy item quantity is a non positive number.");
        }

        for (SDMOffer offerToCheck: thenYouGet.getSDMOffer()) {//search all the offer's ID in store and checke quantity and for addiotnal is positive
            for (SDMSell itemToCheck:store.getSDMPrices().getSDMSell()) {
                if(itemToCheck.getItemId() == offerToCheck.getItemId()){
                    foundThenYouGetItemIDInStore = true;
                }
                if(offerToCheck.getQuantity() <= 0){
                    throw new Exception("If you buy item quantity is a non positive number.");
                }
                if(offerToCheck.getForAdditional() < 0){
                    throw new Exception("Invalid sale \"for additional\" is negative.");
                }
            }
            if(!foundThenYouGetItemIDInStore){
                throw new Exception("Store doesn't sell \"Then you get\" product");
            }
        }

        for (SDMItem item: descriptor.getSDMItems().getSDMItem()){// check category match with quantity in sale.
            if(item.getId() == ifYouBuy.getItemId() && item.getPurchaseCategory().compareToIgnoreCase("Quantity") == 0 &&

                    ifYouBuy.getQuantity() != Math.floor(ifYouBuy.getQuantity())){
                throw new Exception("Quantity should be int and is a double.");
            }
        }
        for (SDMOffer offer:thenYouGet.getSDMOffer()) {// check category match with quantity in sale.
            for (SDMItem item: descriptor.getSDMItems().getSDMItem()) {
                if(offer.getItemId() == item.getId() &&

                        item.getPurchaseCategory().compareToIgnoreCase("Quantity") == 0 &&

                        offer.getQuantity() != Math.floor(offer.getQuantity())){

                    throw new Exception("Quantity should be int and is a double.");
                }
            }
        }
    }

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

    private Map<Integer,Product> createProductMapFromXml(String xmlName, Map<Integer,Store> tempStoreMap ) throws JAXBException, XmlProductsShareIDException, XmlProductCategoryNotRecognizedException, XmlProductIsntSoldByStoresException, FileNotFoundException {
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
}
