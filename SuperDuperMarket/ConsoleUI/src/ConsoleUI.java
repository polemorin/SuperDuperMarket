import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.*;
import java.text.SimpleDateFormat;
import java.util.List;
import SDMExceptions.*;
import ProductTypes.*;

public class ConsoleUI {
    //hello the
    SuperDuperMarket SDM;
    public void run() {
        boolean exitSystem = false;
        int userInput;
        while(!exitSystem)
        {
            try {
                userInput = getUserMenuChoice();
                switch (userInput) {
                    case 1:
                        System.out.println("~~~~ Load Xml file ~~~~");
                        System.out.println();
                        System.out.println("Enter xml file path.");
                        SDM.loadXmlFile();
                        System.out.println("File loaded successfully!");
                        break;
                    case 2:
                        System.out.println("~~~~ Store details ~~~~");
                        System.out.println();
                        printStoresDetailsToConsole();
                        break;
                    case 3:
                        System.out.println("~~~~ Product details ~~~~");
                        System.out.println();
                        printAllProductsInSDMSystemToConsole();
                        break;
                    case 4:
                        System.out.println("~~~~ Place an order ~~~~");
                        System.out.println();
                        makeNewOrder();
                        break;
                    case 5:
                        System.out.println("~~~~ Order history ~~~~");
                        System.out.println();
                        printOrderHistoryToConsole();
                        break;
                    case 6:
                        System.out.println("~~~~ Update a store's items ~~~~");
                        System.out.println();
                        updateStoreItems();
                        break;
                    case 7:
                        System.out.println("~~~~ Save order history to file ~~~~");
                        System.out.println();
                        writeOrderHistoryToFile();
                        break;
                    case 8:
                        System.out.println("~~~~ Load orders from file ~~~~");
                        System.out.println();
                        readOrderHistoryFromFile();
                        break;
                    case 9:
                        exitSystem = true;
                        return;
                }

            }catch(XmlLocationOutOfBoundsException e){
                System.out.println(e.getErrorMsg());
                System.out.println("File was not loaded");
            }
            catch (XmlMultipleStoresShareLocationException e){
                System.out.println("Multiple stores share this location: "+ e.getInvalidLocation());
                System.out.println("File was not loaded");
            }
            catch(XmlStoreSellProductNotInMarketException e){
                System.out.println("A store sells this product even though it is not sold in market, productID: "+ e.getInvalidProductID());
                System.out.println("File was not loaded");
            }
            catch(XmlStoreSellsMultipleProductsWithSameIDException e){
                System.out.println("The store " + e.getStoreName() + " sells this product multiple times, Product ID:" + e.getProductID());
                System.out.println("File was not loaded");
            }
            catch (XmlProductCategoryNotRecognizedException e){
                System.out.println("Product category " + e.getInvalidCategoryString() + "isn't recognized by market.");
                System.out.println("File was not loaded");
            }
            catch(XmlMultipleStoresShareIDException e){
                System.out.println("Multiples stores share this ID: " + e.getInvalidId());
                System.out.println("File was not loaded");
            }
            catch(XmlProductIsntSoldByStoresException e){
                System.out.println("Product with this ID: " + e.getProductID() + " isn't sold by any store.");
                System.out.println("File was not loaded");
            }
            catch(XmlProductsShareIDException e){
                System.out.println("Multiple products share this ID: " + e.getProductID());
                System.out.println("File was not loaded");
            }
            catch(XmlFileNotLoadedException | NoOrdersInSystemException e){
                System.out.println(e.getMessage());
            }
            catch(Exception e){
                System.out.println(e.getMessage());
            }
        }

    }

    private void updateStoreItems() throws  XmlFileNotLoadedException,Exception{
        if (!SDM.isXmlLoaded()) {
            throw new XmlFileNotLoadedException("Option unavailable. No XML file Loaded");
        }
        Scanner scanner = new Scanner(System.in);
        int storeID;
        int menuOption;
        printStoresNameAndID();
        System.out.print("Please enter store ID you wish to update inventory in:");
        try {
            storeID = scanner.nextInt();
        }catch (Exception e){
            throw new Exception("Input is invalid, integer was not entered.");
        }

       if(!SDM.isStoreIDValid(storeID)){
           throw new Exception("Invalid store ID.");
       }
       String storeName = SDM.getStores().get(storeID).getName();
        System.out.println("Enter desired action:");
        System.out.println("1) Remove product from " + storeName);
        System.out.println("2) Add product to " + storeName);
        System.out.println("3) Update " + storeName +"'s product's price ");
        try{
            menuOption = scanner.nextInt();
        }catch (Exception e){
            throw new Exception("Input is invalid, integer was not entered.");
        }

        if(!(menuOption >=1 && menuOption <= 3)){
            throw new Exception("Invalid option.");
        }
        switch(menuOption){
            case 1:
                removeProductFromStore(storeID);
                break;
            case 2:
                addProductToStore(storeID);
                break;
            case 3:
                updateStoreProductPrice(storeID);
                break;
        }
    }

    private void updateStoreProductPrice(int storeID) throws Exception{
        printStoreProductNameAndID(storeID);
        Scanner scanner = new Scanner(System.in);
        int productID;
        double price;
        String storeName = SDM.getStores().get(storeID).getName();
        String productName;
        System.out.print("Please enter the product ID you wish to update:");
        try{
            productID = scanner.nextInt();
        }catch (Exception e){
            throw new Exception("Input is invalid, integer was not entered.");
        }
        if(!(SDM.getStores().get(storeID).doesStoreSellProduct(productID))){
            throw  new Exception(storeName + " does not sell a product with this ID.");
        }
        productName = SDM.getProducts().get(productID).getProductName();
        System.out.println("Please enter the new price for " + productName);
        try{
            price = scanner.nextDouble();
        } catch (Exception e){
            throw new Exception("Input is invalid, number was not entered.");
        }
        if(price <= 0){
            throw new Exception("Price must be a positive number. Invalid price.");
        }
        SDM.getStores().get(storeID).updateStoreProduct(productID,price);
        System.out.println(productName + "'s price was updated successfully.");
    }

    private void addProductToStore(int storeID) throws Exception{
        Scanner scanner = new Scanner(System.in);
        int productID;
        double price;
        String storeName = SDM.getStores().get(storeID).getName();
        printMarketProductsNameAndID();
        System.out.print("Please enter the product ID you wish to add to store:");
        try{
            productID =scanner.nextInt();
        }catch(Exception e){
            throw new Exception("Input is invalid, integer was not entered.");
        }
        if((SDM.getStores().get(storeID).doesStoreSellProduct(productID))){
            throw new Exception(storeName + " already sells this product. ");
        }
        System.out.println("Please enter the price:");
        try{
            price = scanner.nextDouble();

        }
        catch (Exception e){
            throw new Exception("Input is invalid, number was not entered.");
        }
        if(price <= 0){
            throw new Exception("Price must be a positive number. Invalid price.");
        }
        SDM.addProductToStore(productID,storeID,price);
        System.out.println( SDM.getProducts().get(productID).getProductName() + " was added successfully to " + storeName);
    }

    private void printMarketProductsNameAndID() {
        for (Map.Entry<Integer,Product> product:SDM.getProducts().entrySet()) {
            System.out.println("Product name: " + product.getValue().getProductName() +
                    ", ID: " + product.getValue().getProductID());
            System.out.println();;
        }
    }

    private void removeProductFromStore(int storeID) throws Exception {
        Scanner scanner = new Scanner(System.in);
        int productID;
        String storeName = SDM.getStores().get(storeID).getName();
        printStoreProductNameAndID(storeID);
        System.out.print("Please enter the product ID you wish to remove from store:");
        try{
            productID =scanner.nextInt();
        }catch(Exception e){
            throw new Exception("Input is invalid, integer was not entered.");
        }
        if(!(SDM.getStores().get(storeID).doesStoreSellProduct(productID))){
            throw  new Exception(storeName + " does not sell a product with this ID.");
        }
        if(SDM.countHowManyStoresSellProduct(productID) == 1){
            throw new Exception( storeName + " is the only store that sells this product so it can not be removed. ");
        }
        if(SDM.getStores().get(storeID).getProducts().size() == 1){
            throw new Exception( storeName + " sells only one item so it cannot be removed.");
        }
        SDM.removeProductFromStore(productID,storeID);
        System.out.println( SDM.getProducts().get(productID).getProductName() + " was removed successfully from " + storeName);
    }

    private void printStoreProductNameAndID(int storeID) {
        for (Map.Entry<Integer,StoreProduct> product :SDM.getStores().get(storeID).getProducts().entrySet()) {
            System.out.println("Product name: " + product.getValue().getProductName() +
            ", ID: " + product.getValue().getProductID());
            System.out.println();
        }
    }

    private void printStoresNameAndID() {
        for (Map.Entry<Integer, Store> store : SDM.getStores().entrySet()){
            System.out.println("Store Name: " + store.getValue().getName() +
            ", ID: " + store.getValue().getID());
            System.out.println();
        }
    }

    public ConsoleUI() {
        this.SDM = new SuperDuperMarket();
    }

    private int getUserMenuChoice(){
        MenuOptions.printMenuOptions();
        Scanner scanner = new Scanner(System.in);
        int userInput = 0;
        boolean validChoice = false;
        do{
            try{

                userInput = scanner.nextInt();

                if(MenuOptions.isOptionValid(userInput))
                {
                    validChoice = true;
                }
                else
                {
                    System.out.println("Enter a choice between 1 and "+MenuOptions.EXIT.getOptionNumber());
                }

            }catch(InputMismatchException e){
                System.out.println("Invalid input, input should be an integer number.");
                while(!scanner.hasNextInt())
                {
                    scanner.next();
                }
            }catch(NoSuchElementException e){
                System.out.println("No input was entered.");
            }catch(Exception e){
                System.out.println(e.getMessage());
            }

        }while(!validChoice);
        return userInput;
    }
    //------------------------------------option 2---------------------
    public void printStoresDetailsToConsole() throws XmlFileNotLoadedException {
        if (!SDM.isXmlLoaded()) {
            throw new XmlFileNotLoadedException("Option unavailable. No XML file Loaded");
        }
        for (Map.Entry<Integer, Store> store : SDM.getStores().entrySet()) {
            printStoreDetailToConsole(store.getValue());
            System.out.println("-----------------------------------------------------------------");
            System.out.println("-----------------------------------------------------------------");
        }
    }

    private void printStoreDetailToConsole(Store store) {
        System.out.println("Store ID: " + store.getID());
        System.out.println("Store name: " + store.getName());
        System.out.println("Store Products: ");
        printStoreProductsToConsole(store);
        System.out.println("Store order history: ");
        printStoreOrdersToConsole(store);
        System.out.println("Store's PPK: " + store.getDeliveryPPK()+"$");
        System.out.println("Store's total revenue from deliveries: " + String.format("%.2f",store.getTotalDeliveryPayment())+"$");
    }

    private void printStoreProductsToConsole(Store store) {
        int i = 1;
        for (Map.Entry<Integer, StoreProduct> product : store.getProducts().entrySet()) {
            System.out.println(i++ + ") ");
            printStoreProduct(product.getValue());
            System.out.println("-------------------------------");
        }

    }

    private void printStoreProduct(StoreProduct product) {
        System.out.println("Product ID: " + product.getProductID());
        System.out.println("Product name: " + product.getProductName());
        System.out.println("Product purchase category: " + product.getProductCategory());
        System.out.println("Product price per unit: " +String.format("%.2f",product.getPrice()) + "$");
        System.out.println("Total sold: " + product.getTotalAmountSoldInStore());
    }

    private void printStoreOrdersToConsole(Store store){
        int i = 1;
        for (StoreLevelOrder order : store.getStoreOrderHistory()) {
            System.out.println(i++ + ") ");
            printStoreOrderToConsole(order);
            System.out.println("-----------------------------------------------------------------");
        }
        if(i == 1){
            System.out.println("No orders were made in this store");
            System.out.println();
        }
    }
    private void printStoreOrderToConsole(StoreLevelOrder order){
        DateFormat dateFormat = new SimpleDateFormat("dd/MM-HH:mm");
        System.out.println("Order date: " + dateFormat.format(order.getDate()));
        System.out.println("Order total product amount: " + order.getAmountOfProducts());
        System.out.println("Total products price: " +String.format("%.2f", order.getTotalProductsPrice())+"$");
        System.out.println("Order delivery cost: " + String.format("%.2f",order.getDeliveryPrice())+"$");
        System.out.println("Amount to pay: " + (String.format("%.2f",order.getTotalProductsPrice() + order.getDeliveryPrice())  + "$"));
    }

    //-----------------------------------option 3-----------------------
   public void printAllProductsInSDMSystemToConsole () throws XmlFileNotLoadedException{
       if (!SDM.isXmlLoaded()) {
           throw new XmlFileNotLoadedException("Option unavailable. No XML file Loaded");
       }
       int i = 1;
       for (Map.Entry<Integer,Product> product: SDM.getProducts().entrySet()) {
           System.out.println(i++ + ") ");
           printSDMProductToConsole(product.getValue());
           System.out.println("-----------------------------------------------------------------");
       }

   }
    private void printSDMProductToConsole(Product product) {
        System.out.println("Product ID number: " + product.getProductID());
        System.out.println("Product name: " + product.getProductName());
        System.out.println("Product purchase category: " + product.getProductCategory());
        System.out.println(SDM.countHowManyStoresSellProduct(product) + " stores sells it.");
        System.out.println("Average price: " + String.format("%.2f",SDM.getAveragePriceForProduct(product)) + "$");
        if (product.getProductCategory().toString().equals("Quantity")) {
            System.out.println("Product was sold in Super Duper Market " + (int)SDM.totalAmountSoldInMarket(product) + " times.");
        }
        else
        {
            System.out.println(SDM.totalAmountSoldInMarket(product) + " kilos sold in Super Duper Market.");
        }
    }
    // --------------------------------option 5-------------------------
    public void printOrderHistoryToConsole() throws XmlFileNotLoadedException,NoOrdersInSystemException{
        if (!SDM.isXmlLoaded()) {
            throw new XmlFileNotLoadedException("Option unavailable. No XML file Loaded");
        }
        if(SDM.getOrderHistory().size() == 0){
            throw new NoOrdersInSystemException("Option unavailable. No orders were made in system.");
        }
        for (Map.Entry<Integer,CustomerLevelOrder> order: SDM.getOrderHistory().entrySet()) {
            printCustomerLevelOrder(order.getValue());
        }
    }
    private void printCustomerLevelOrder(CustomerLevelOrder order){
        int i = 1;
        System.out.println("Order ID: " + order.getOrderID());
        System.out.println("Date: " + new SimpleDateFormat("dd/MM-HH:mm").format(order.getDate()));
        for (StoreLevelOrder innerOrder:order.getOrders()) {
            if(order.getOrders().size() > 1) {
                System.out.println(i++ + ") ");
            }
            printStoreLevelOrder(innerOrder);

            System.out.println("-----------------------------------------------------------------");
        }
        System.out.println("Total price to pay for order: " + String.format("%.2f",order.getTotalProductPrice()+order.getDeliveryPrice()));
        System.out.println();
    }

    private void printStoreLevelOrder(StoreLevelOrder order){
        System.out.println("Store ID: " + order.getStoreID() + " store name: " + order.getStoreName());
        System.out.println("Amount of different products in order: " + order.getSoldProducts().size()
        + ", total amount of products: " + order.getAmountOfProducts());
        String strDeliveryCost = String.format("%.2f", order.getDeliveryPrice());
        System.out.println("Total products price: " + String.format("%.2f",order.getTotalProductsPrice()) + "$");
        System.out.println("Delivery cost: " + strDeliveryCost + "$");
        String amountToPay = String.format("%.2f", order.getDeliveryPrice() + order.getTotalProductsPrice());
        System.out.println("Amount to pay in store: " + amountToPay + "$");
    }
    // ------------------------option 4-----------------------
    public void makeNewOrder() throws XmlFileNotLoadedException{
        if (!SDM.isXmlLoaded()) {
            throw new XmlFileNotLoadedException("Option unavailable. No XML file Loaded");
        }
        CustomerLevelOrder userOrder;
        Scanner scanner = new Scanner(System.in);
        String orderMethod;
        boolean validOrderMethod = false;
        boolean userApproval = false;
        do{
            System.out.println("Please choose order method: " +
                    "press s for static order or d for dynamic order and then ENTER.");
            orderMethod = scanner.nextLine();
            if(orderMethod.equals("S") || orderMethod.equals("s")
                    || orderMethod.equals("d") || orderMethod.equals("D")){
                validOrderMethod = true;
            }
            else{
                System.out.println("Invalid input.");
            }
        }while(!validOrderMethod);

        if(orderMethod.equals("S") || orderMethod.equals("s")){

            userOrder = makeNewStaticOrder();
            if(userOrder != null){
                printStaticCustomerOrderForApproval(userOrder);
            }
        }
        else{
            userOrder = makeNewDynamicOrder();
            if(userOrder != null){
                printDynamicCustomerOrderForApproval(userOrder);
            }
        }
        if(userOrder != null) {
            userApproval = getUserApprovalForOrder();
            if (userApproval) {
                SDM.placeOrderInSDM(userOrder, 0);
                System.out.println("Order was placed!");
            } else {
                System.out.println("Order canceled!");
            }
        }
    }

    private void printDynamicCustomerOrderForApproval(CustomerLevelOrder order) {
        System.out.println("================Stores in order====================");
        for (StoreLevelOrder storeLevelOrder:order.getOrders() ) {
            System.out.println("Store ID: " + storeLevelOrder.getStoreID());
            System.out.println("Store name: " + storeLevelOrder.getStoreName());
            System.out.println("Store Location: [" + SDM.getStores().get(storeLevelOrder.getStoreID()).getLocation().x + "," + SDM.getStores().get(storeLevelOrder.getStoreID()).getLocation().y + "]");
            System.out.print("Store PPK: " + SDM.getStores().get(storeLevelOrder.getStoreID()).getDeliveryPPK());
            System.out.print(",  Distance from user: " + String.format("%.2f",storeLevelOrder.getDistanceFromCustomerToStore(SDM.getStores().get(storeLevelOrder.getStoreID()).getLocation(),SDM.getUsers().get(0).getLocation())));
            System.out.println(",  Delivery cost: " + String.format("%.2f",storeLevelOrder.getDeliveryPrice()) + "$");
            System.out.println("Amount of product types sold: " +  storeLevelOrder.getAmountOfProductTypes());
            System.out.println("Total product price: " + storeLevelOrder.getTotalProductsPrice() + "$");
            System.out.println("----------------");
        }
        System.out.println("================Order summery====================");
        System.out.println();
        for (StoreLevelOrder storeOrder: order.getOrders()) {
            System.out.println("Store ID: " + storeOrder.getStoreID());
            System.out.println("Store name: " + storeOrder.getStoreName());
            System.out.println("Store Location: [" + SDM.getStores().get(storeOrder.getStoreID()).getLocation().x + "," + SDM.getStores().get(storeOrder.getStoreID()).getLocation().y + "]");
            System.out.println("Store PPK: " + SDM.getStores().get(storeOrder.getStoreID()).getDeliveryPPK());
            System.out.println("Distance from user: " + String.format("%.2f",storeOrder.getDistanceFromCustomerToStore(SDM.getStores().get(storeOrder.getStoreID()).getLocation(),SDM.getUsers().get(0).getLocation())));
            System.out.println("Delivery cost: " + String.format("%.2f",storeOrder.getDeliveryPrice()) + "$");
            System.out.println("-------Products from this store---------");
            for (SoldProduct product: storeOrder.getSoldProducts()) {
                printProductDetailsForUserApproval(product);
            }
            System.out.println("=========");
        }
        System.out.println("==============Price summery============");
        System.out.println("Total product price: " + String.format("%.2f",order.getTotalProductPrice()) + "$");
        System.out.println("Total delivery price: " + String.format("%.2f",order.getDeliveryPrice()) + "$");
        System.out.println("Amount to pay: " + String.format("%.2f",order.getTotalProductPrice() +order.getDeliveryPrice()) + "$");
        System.out.println("=======================================");
    }

    private boolean getUserApprovalForOrder()
    {
        Scanner scanner = new Scanner(System.in);
        String userInput;
        System.out.println("Do you wish to place this order? enter \"y\" then press ENTER to confirm order. To cancel enter anything else.");
        userInput = scanner.nextLine();
        return userInput.equals("y") || userInput.equals("Y");
    }
    private void printStaticCustomerOrderForApproval(CustomerLevelOrder order){
        String totalDeliveryPrice = String.format("%.2f",order.getDeliveryPrice());
        String totalAmountToPay = String.format("%.2f",order.getDeliveryPrice() + order.getTotalProductPrice());
        for (StoreLevelOrder storeOrder: order.getOrders()) {
            printStoreOrderForApproval(storeOrder,SDM.getUsers().get(0).getLocation());
            System.out.println("------------------");
        }
        System.out.println("Total product price: " + String.format("%.2f",order.getTotalProductPrice())+ "$");
        if(order.getOrders().size() > 1)
        {
            System.out.println("Total delivery price: " + totalDeliveryPrice + "$");
        }
        System.out.println("Total amount to pay: " + totalAmountToPay + "$");
    }
    private void printStoreOrderForApproval(StoreLevelOrder order, Point customerLocation){
        System.out.println("Order from: " + order.getStoreName());
        System.out.println();
        int i = 1;
        for (SoldProduct product: order.getSoldProducts()) {
            System.out.println(i++ + ")");
            printProductDetailsForUserApproval(product);
        }
        Point storeLocation = SDM.getStores().get(order.getStoreID()).getLocation();
        double storePPK = SDM.getStores().get(order.getStoreID()).getDeliveryPPK();
        System.out.println("Customer distance from store: " +String.format("%.2f",order.getDistanceFromCustomerToStore(storeLocation,customerLocation))+" km");
        System.out.println("Store PPK: " + storePPK + "$");
        String deliveryPriceStr = String.format("%.2f",order.getDeliveryPrice());
        System.out.println("Delivery cost for " + order.getStoreName() +": "+deliveryPriceStr+ "$");
    }
    private void printProductDetailsForUserApproval(SoldProduct product){

        System.out.println("Product ID: " + product.getProductID());
        System.out.println("Product name: "+ product.getProductName());
        System.out.println("Purchase category: "+ product.getProductCategory());
        System.out.println("Price: "+product.getPrice()+"$");
        if(product.getProductCategory().toString().equals("Quantity")){
            System.out.println("Amount bought: "+ (int)product.getAmountSoldInOrder());
        }
        else {
            System.out.println("Amount bought: " + product.getAmountSoldInOrder() + " kilos");
        }
        System.out.println("Total price for item: " +String.format("%.2f",product.getTotalPrice()) + "$");
        System.out.println();
    }
    private Store getChosenStoreFromUser(){
        Scanner scanner = new Scanner(System.in);
        printStoresForStaticOrder();
        int storeID = -1;
        boolean isValidStore = false;
        System.out.println("Please enter the store ID you wish to buy from:");
        do{
           try{
               storeID = scanner.nextInt();
               if(SDM.isStoreIDValid(storeID)){
                   isValidStore = true;
               }
               else {
                   System.out.println("This store ID does not exist.");
               }
           }catch(InputMismatchException e){
               System.out.println("Store ID must be an integer.");
               scanner.nextLine();
           }
           catch (NoSuchElementException e){
               System.out.println("Store ID can't be empty.");
           }
           catch(Exception e){
               System.out.println(e.getMessage());
           }
        }while(!isValidStore);
        return SDM.getStores().get(storeID);
    }

    private void printStoresForStaticOrder(){
        System.out.println("Stores in Super Duper Market:");
        for (Map.Entry<Integer,Store> store:SDM.getStores().entrySet()) {
            System.out.println("- Store ID: " + store.getValue().getID()
            + "   name: " + store.getValue().getName()
            + "   delivery price per kilometer: " + store.getValue().getDeliveryPPK());
        }
    }

    private CustomerLevelOrder makeNewDynamicOrder(){
        Date date = getValidDateFromConsole();
        Point location = getValidDifferentFromStoresLocations();
        SDM.getUsers().get(0).setLocation(location);
        Integer productID = null;
        Double amount = null;
        Map<Integer,Double> shoppingList = new HashMap<Integer, Double>();
        boolean finishedOrder = false;
        do {
            productID  = getValidProductIDOrQFroDynamicOrder();
            if(productID == null) {
                finishedOrder = true;
            }
            else {
                amount = getStoreProductAmountFromUser(SDM.getProducts().get(productID).getProductCategory());
                if(amount  == null)
                    finishedOrder = true;
            }
            if(!finishedOrder) {
                if(shoppingList.containsKey(productID)) {
                    amount+= shoppingList.get(productID);
                    shoppingList.replace(productID,amount);
                }
                else {
                    shoppingList.put(productID,amount);
                    System.out.println("Product was added to order successfully.");
                }
            }
        }while(!finishedOrder);
        if(shoppingList.size() == 0) {
            System.out.println("No products selected, order was not made.");
            return null;
        }
        else {
            return SDM.createCheapestOrder(shoppingList,1,date, location);
        }

    }
    private Integer getValidProductIDOrQFroDynamicOrder() {
        Integer productID = null;
        boolean validUserInput = false;
        Scanner scanner = new Scanner(System.in);
        String userInput;

        showAllProductsForDynamicOrder();
        do {
            System.out.println("Enter the product ID you wish to buy or q to end order.");
            userInput = scanner.nextLine();
            if(userInput.equals("q") || userInput.equals("Q")) {
                return null;
            }
            else {
                try {
                    productID = Integer.parseInt(userInput);
                    validUserInput = SDM.getProducts().containsKey(productID);
                    if(!validUserInput) {
                        System.out.println("Invalid input. Product ID does not exist in market.");
                    }
                }catch(NumberFormatException e) {
                    System.out.println("Invalid input. Product ID must be an Integer number.");
                }

            }
        }while(!validUserInput);
        return productID;
    }
    private void showAllProductsForDynamicOrder(){
        int i = 1;
        for (Map.Entry<Integer,Product> product:SDM.getProducts().entrySet()) {
            System.out.println(i++ +") ");
            System.out.println("Product ID number: " + product.getValue().getProductID());
            System.out.println("Product name: " + product.getValue().getProductName());
            System.out.println("--------------------");
        }
    }

    private CustomerLevelOrder makeNewStaticOrder(){
        Store chosenStore = getChosenStoreFromUser();
        Date date = getValidDateFromConsole();
        Point location = getValidDifferentFromStoresLocations();
        SDM.getUsers().get(0).setLocation(location);
        Scanner scanner = new Scanner(System.in);
        Integer productID;
        boolean finishedOrder = false;
        StoreLevelOrder order = new StoreLevelOrder(chosenStore,1,date,location);
        Double productAmount = null;
        do{
            printProductsForStaticOrder(chosenStore);
            productID = getStoreProductIDOrQ(chosenStore);
            if(productID == null){
                finishedOrder = true;
            }
            else{
                productAmount = getStoreProductAmountFromUser(
                        chosenStore.getProducts().get(productID).getProductCategory());
                if(productAmount == null) {
                    finishedOrder = true;
                }
            }
            if(!finishedOrder)
            {
                order.addProductToOrder(new SoldProduct(chosenStore.getProducts().get(productID),productAmount));
                System.out.println("Product was added to order successfully.");
            }
        }while(!finishedOrder);
        if(order.getAmountOfProducts() == 0)
        {
            System.out.println("No products selected, order was not made.");
            return null;
        }
        else{
            List<StoreLevelOrder> storeOrder = new ArrayList<StoreLevelOrder>();
            storeOrder.add(order);
            return new CustomerLevelOrder(storeOrder);
        }
    }

    private Double getStoreProductAmountFromUser(ProductCategory category){
        Double amount = null;
        String userInput;
        boolean validAmountOfProduct = false;
        Scanner scanner = new Scanner(System.in);
        do{
            System.out.println("Please enter the " +category.toString()+ " you wish to buy or q to finish the order:");
            userInput = scanner.nextLine();
            if( userInput.equals("q") || userInput.equals("Q")) {
                amount = null;
                validAmountOfProduct = true;
            }
            else{
                try{
                    amount = Double.parseDouble(userInput);
                    if(amount <= 0)
                    {
                        throw new Exception("product amount must be positive.");
                    }
                    else{
                        if(category.toString().equals("Quantity"))
                        {
                            if(amount == Math.floor(amount)){
                                validAmountOfProduct = true;
                            }
                            else{
                                throw new Exception("This product sells in quantity. Please enter an integer.");
                            }
                        }
                        else{
                            validAmountOfProduct = true;
                        }
                    }
                }catch(NullPointerException e){
                    System.out.println(category.toString() + " was not entered.");
                }
                catch(NumberFormatException e){
                    System.out.println("The input is not a number. Please enter a number.");
                }
                catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
        }while(!validAmountOfProduct);
        return amount;
    }

    private Integer getStoreProductIDOrQ(Store store){
        Integer productID = null;
        Scanner scanner = new Scanner(System.in);
        String strProductID;
        boolean validProductChoice = false;
        do{
            System.out.println("Please enter the product's ID you wish to purchase or q to finish the order:");
            try{
                strProductID = scanner.nextLine();
                if( strProductID.equals("q") || strProductID.equals("Q")) {
                    validProductChoice = true;
                    productID = null;
                }
                else {
                    productID = Integer.parseInt(strProductID);
                    if (SDM.getProducts().containsKey(productID)) {
                        if (store.getProducts().containsKey(productID)) {
                            validProductChoice = true;
                        } else {
                            throw new Exception(store.getName() + "does not sell " +
                                    SDM.getProducts().get(productID).getProductName() +
                                    ". Please choose a different product.");
                        }
                    }
                }
            }catch(NumberFormatException e){
                System.out.println("Enter product ID as an integer.");
            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }
        }while(!validProductChoice);
        return productID;
    }

    private void printProductsForStaticOrder(Store store){
        System.out.println("Products in Super Duper Market with \"" + store.getName() + "'s\"prices:");
        for (Map.Entry<Integer,Product> SDMProduct: SDM.getProducts().entrySet()) {
            System.out.println("ID: " + SDMProduct.getValue().getProductID());
            System.out.println("Name: " + SDMProduct.getValue().getProductName());
            System.out.println("Purchase category: " + SDMProduct.getValue().getProductCategory());
            if(store.doesStoreSellProduct(SDMProduct.getValue())){
                System.out.println("Price: " + store.getProducts().get(SDMProduct.getKey()).getPrice() + "$.");
            }
            else{
                System.out.println(store.getName() + " does not sell " + SDMProduct.getValue().getProductName());
            }
            System.out.println("-----------------------------------------------------------------");
        }
    }

    private Point getValidDifferentFromStoresLocations(){
        Point userLocation;
        Scanner scanner = new Scanner(System.in);
        int x = 0,y = 0;
        boolean isValidLocation = false;
        do{
            try{
                System.out.println("Please enter your X position. a value between 1-50");
                x = scanner.nextInt();
                System.out.println("Please enter your Y position. a value between 1-50");
                y = scanner.nextInt();
                if((x >= 1 && x <= 50) && (y >= 1 && y <= 50)){
                    for (Map.Entry<Integer,Store> store: SDM.getStores().entrySet()) {
                        if (!SDM.isUserLocationDifferentFromStoreLocation(new Point(x, y), store.getValue().getLocation())) {
                            System.out.println("Location must be different from any of our stores." +
                                    "Your location matches " + store.getValue().getName() + "'s location.");
                        } else {
                            isValidLocation = true;
                        }
                    }
                }
                else{
                    System.out.println("X or Y are not in range.");
                }

            }
            catch(InputMismatchException e){
                System.out.println("Coordinates can only be an integer.");
                scanner.nextLine();
            }
            catch (NoSuchElementException e){
                System.out.println("Location wasn't entered in the format.");
            }
            catch(Exception e){
                System.out.println(e.getMessage());
            }
        }while(!isValidLocation);

        userLocation = new Point(x,y);
        return  userLocation;
    }

    private Date getValidDateFromConsole(){
        Scanner scanner = new Scanner(System.in);
        SimpleDateFormat simpleDate = null;
        Date date = null;
        String dateFromConsole;
        System.out.println("Please Enter date in this format: dd/MM-HH:mm.");
        boolean validDateFromConsole = false;
        do{
            dateFromConsole = scanner.nextLine();
            try{
                simpleDate = new SimpleDateFormat("dd/MM-HH:mm");
                simpleDate.setLenient(false);
                date = simpleDate.parse(dateFromConsole);
                validDateFromConsole = true;
            }catch (Exception e) {
                System.out.println("Invalid date. Please Enter date in this format: dd/MM-HH:mm.");
            }
        }while (!(validDateFromConsole));

        return  date;
    }
    //--------------option 7-----------------------
    private void    writeOrderHistoryToFile() throws XmlFileNotLoadedException,Exception{
        if (!SDM.isXmlLoaded()) {
            throw new XmlFileNotLoadedException("Option unavailable. No XML file Loaded");
        }
        String path;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the file path you wish to save orders at.");
        try {
            path = scanner.nextLine();
            File file = new File(path);
            if(file.exists()){
                System.out.println("File already exists are you sure you want to override it?");
                System.out.println("Enter y to override file or anything else to cancel save.");
                if(!scanner.nextLine().toUpperCase().equals("Y")){
                    System.out.println("Orders were not saved.");
                    return;
                }
            }
            SDM.writeOrdersToFile(path);
        }catch(SecurityException ex){
            throw new Exception("Cannot Write to file for security reasons. orders were not saved.");
        }catch(FileNotFoundException e){
            throw new Exception("Attempted to open a read-only file for writing or file is inaccessible. orders were not saved.");
        }
        catch (Exception e){
            throw e;
        }
        System.out.println("Wrote to file successfully!");

    }
    //--------------option 8-----------------------
    private void readOrderHistoryFromFile() throws XmlFileNotLoadedException,Exception{
        if (!SDM.isXmlLoaded()) {
            throw new XmlFileNotLoadedException("Option unavailable. No XML file Loaded");
        }
        try{
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter the full path of the file");
            String path = scanner.nextLine();
            SDM.readOrdersFromFile(path);
        }catch(ParseException e){
            throw new Exception("File format is invalid. Can't read from file. ");

        }catch(FileNotFoundException e){
            throw new Exception("File was not found or is a directory. Read from file failed.");
        }
        catch(IOException e){
            throw new Exception(e.getMessage());
        }
        System.out.println("File loaded successfully!");
    }
}
