var zoneDetails = JSON.parse(localStorage.getItem('ZoneDetails'));
var zoneName = zoneDetails.zone;
var interval;
var Update_Product_Details_Table_URL = buildUrlWithContextPath("updateProductDetailsTable");
var Update_Store_Details_Table_URL = buildUrlWithContextPath("updateStoreDetailsTable");
var Customer_Order_History_Table_URL = buildUrlWithContextPath("updateCustomerOrderHistoryTable");
var GET_PRODUCTS_FROM_STORE_URL = buildUrlWithContextPath("getStoreProducts");
var Get_Sales_URL = buildUrlWithContextPath("getSales");
var CREATE_BASIC_ORDER_URL = buildUrlWithContextPath("createBasicOrder");
var PLACE_ORDER_IN_MARKET_URL = buildUrlWithContextPath("placeOrderInMarket");
var GET_STORES_BY_OWNER_URL = buildUrlWithContextPath("getStoresByOwner");
var GIVE_FEEDBACK_TO_STORE_URL = buildUrlWithContextPath("giveFeedBackToStore");
var User_Role_URL = buildUrlWithContextPath("userRole");
var ADD_STORE_URL = buildUrlWithContextPath("addStore");
var SEND_ORDER_ALERT_URL = buildUrlWithContextPath("sendOrderAlert");
var SEND_FEEDBACK_ALERT_URL = buildUrlWithContextPath("sendFeedbackAlert");
var SEND_NEW_STORE_ALERT_URL = buildUrlWithContextPath("sendNewStoreAlert");
var GET_ALERTS_URL = buildUrlWithContextPath("getAlerts");


let customerLevelOrderJS;
let myAvailableSales;
let mySales;
var chosenSale;
var productsInArea;

//JAVA SCRIPT OBJECT
function Store(){
    this.storeName;
    this.storeID;
    this.PPK;
    this.storeProducts;
    this.locationX;
    this.locationY;
}
function FeedBack() {
    this.customerName;
    this.orderDate;
    this.rating;
    this.feedbackText;
}
function SaleHandler(){
    var mySales = [];
    var productMapIDAmount;
}
function Sale() {
    this.name;
    this.ifYouBuy;
    this.thenYouGet;
    this.saleString;
    this.storeName;
}
function IfYouBuy() {
    this.quantity;
    this.itemID;
    this.productName;
}
function ThenYouGet() {
    this.operator;
    this.offers = [];
}

function Offer() {
    this.quantity;
    this.itemID;
    this.forAdditional;
    this.productName;
}
function CustomerLevelOrderJS(){
    this.orderType;
    this.storeOrders = [];
    this.customerXLocation;
    this.customerYLocation;
    this.date;
    this.customerName;
    this.customerID;
    }
function StoreLevelOrderJS(){
    this.regProducts = [];
    this.saleProducts = [];
    this.storeID;
    this.deliveryPrice;
    this.customerLocation;
    this.storeName;
    this.orderID;
    this.PPK;
    this.distanceFromCustomer;
    this.productTypeAmount;

}
function StoreProduct(){
    this.productID;
    this.price;
}
function RegProduct(){
    this.productID;
    this.totalProductPrice;
    this.Amount;
    this.productName;
    this.category;
}
function saleProduct(){
    this.productID;
    this.totalProductPrice;
    this.Amount;
    this.productName;
    this.saleName;
    this.category;
}
$(function() { // onload...do
    //add a function to the submit event
    $.ajax({
        url: User_Role_URL,
        success: function (role) {
            hideHTMLTabsByRole(role);
            if(role !=="Customer"){
                setInterval(getAlerts,3000);
            }
        }
    });
})
function getAlerts() {
    $.ajax({
        url: GET_ALERTS_URL,
        success: function (alerts) {
            if(alerts!=="no alerts") {
                $.each(JSON.parse(alerts) || [], function (index, alert) {
                    $(".alertDiv").append("<div class=\"alert alert-success alert-dismissible\">\n" +
                        "<a href=\"#\" class=\"close\" data-dismiss=\"alert\" aria-label=\"close\">&times;</a>" +
                        alert.alertText+
                        "</div>")
                })
            }
        }
    });
}
function hideHTMLTabsByRole(role) {
    if(role === "Customer"){
        $("#StoreOwnerOrderHistoryButtonID").hide();
        $("#FeedBacksButtonID").hide();
        $("#AddStoreButtonID").hide();
    }else{
        $("#PlaceOrderAndFeedBackButtonID").hide();
        $("#CustomerOrderHistoryButtonID").hide();

    }
}
function openTab(evt, tabName) {
    clearInterval(interval);
    switch(tabName){
        case "ProductTabID":
            ajaxProductDetailsTable();
            interval = setInterval(ajaxProductDetailsTable,2000);
            break;
        case "StoreTabID":
            ajaxStoreDetailsTable();
            interval = setInterval(ajaxStoreDetailsTable,2000)
            break;
        case "PlaceOrderAndFeedBackID":
            buildPlaceOrderLandingPage();
            hideElementOnPlaceOrderLandingPage();
            PlaceOrderLandingPageSubmitFunc();
            ajaxStoreNameComboBox();
            break;
        case "CustomerOrderHistoryID":
            ajaxCustomerOrderDetailsTable();
            break;
        case "StoreOwnerOrderHistoryID":
            buildStoreOwnerOrderHistoryLandingPage();
            break;
        case "FeedBacksID":
            buildFeedBackTable();
            break;
        case "AddStoreID":
            getStoresAndBuildAddStoreLandingPage();
            break;
    }
    var i, tabcontent, tablinks;
    tabcontent = document.getElementsByClassName("tabcontent");
    for (i = 0; i < tabcontent.length; i++) {
        tabcontent[i].style.display = "none";
    }
    tablinks = document.getElementsByClassName("tablinks");
    for (i = 0; i < tablinks.length; i++) {
        tablinks[i].className = tablinks[i].className.replace(" active", "");
    }
    document.getElementById(tabName).style.display = "block";
    evt.currentTarget.className += " active";
}
function getStoresAndBuildAddStoreLandingPage(){
    var dataString = "ZoneName="+zoneName;
    $.ajax({
        data: dataString,
        url: Update_Store_Details_Table_URL,
        success: function (stores) {
            buildAddStoreLandingPage(stores);

        }
    });
}
function buildAddStoreLandingPage(stores) {
    $("#AddStoreID").empty().append(" <h3>Add new store</h3>\n" +
        "    <label class=\"addStoreCentered\">Store name: &nbsp</label>\n" +
        "    <input type=\"text\" placeholder=\"Store name\" name=\"storeName\" id='StoreNameTextBox' required><br> " +
        "    <label id='StoreNameEmptyLabel'></label><br>\n" +
        "    <label >PPK:</label>\n" +
        "    <input type=\"number\" id=\"PPK\" placeholder=\"PPK\" name=\"PPKText\" min=\"0\" required><br>\n" +
        "    <label id=\"PPKErrorLabel\"></label><br>\n" +
        "    <label >Store ID:</label>\n" +
        "    <input type=\"number\" id=\"StoreID\" placeholder=\"Store ID\" name=\"StoreIDNum\" min=\"1\" required><br>\n" +
        "    <label id=\"StoreIDErrorLabel\"></label><br>\n" +
        "    <label >Store location (between 1-50):</label>X\n" +
        "    <input type=\"number\" id=\"XLocation\" name=\"XLocation\" min=\"1\" max=\"50\" required>&nbsp&nbsp Y\n" +
        "    <input type=\"number\" id=\"YLocation\" name=\"YLocation\" min=\"1\" max=\"50\" required><br>\n" +
        "    <label id=\"StoreLocationTakenLabel\"></label><br>\n" +
        "    <input type = \"button\" value=\"Cancel\" id=\"CancelStoreButton\" class=\"btn btn-danger\">\n" +
        "    <input type = \"button\" value=\"Add product to store\" id=\"GoToAddProductToStoreButton\" class=\"btn btn-primary\">\n");
    $("#CancelStoreButton").click(function () {
        CancelAddStore();
    })
    $("#GoToAddProductToStoreButton").click(function () {
        var storeToAdd;
        var Xlocation = document.getElementById("XLocation").value;
        var Ylocation = document.getElementById("YLocation").value;
        if(document.getElementById("StoreNameTextBox").value ===""){
            $("#StoreNameEmptyLabel").empty().append("Enter your store's name.");
        }else {
            $("#StoreNameEmptyLabel").empty();
            if(document.getElementById("PPK").value ===""){
                $("#PPKErrorLabel").empty().append("Enter PPK.");
            }else {
                $("#PPKErrorLabel").empty();
                if(document.getElementById("StoreID").value ==="") {
                    $("#StoreIDErrorLabel").empty().append("Enter ID.");
                }else {
                    if (!checkIfStoreIDIsTaken(stores, document.getElementById("StoreID").value)) {
                        $("#StoreIDErrorLabel").empty();
                        if (parseInt(Xlocation) <= 50 && parseInt(Xlocation) >= 1 && parseInt(Ylocation) <= 50 && parseInt(Ylocation) >= 1) {
                            if (!checkIfStoreInLocationAddStore(stores, Xlocation, Ylocation)) {
                                storeToAdd = new Store();
                                storeToAdd.storeName = StoreNameTextBox.value;
                                storeToAdd.storeID = StoreID.value;
                                storeToAdd.locationX = parseInt(Xlocation);
                                storeToAdd.locationY = parseInt(Ylocation);
                                storeToAdd.PPK = PPK.value;
                                buildAddProductsToStorePage(storeToAdd);
                            }
                        } else {
                            $("#StoreLocationTakenLabel").empty().append("Location coordinates must be between 1-50.");
                        }
                    }
                }
            }
        }
    })


}
function checkIfStoreIDIsTaken(stores,MyStoreID) {
    var storeIDTaken = false;
    $.each(stores || [], function (index, store) {
        if(store.ID === parseInt(StoreID.value)){
            storeIDTaken = true;
        }
    })
    if(storeIDTaken){
        $("#StoreIDErrorLabel").empty().append("There is a store with your chosen ID, please choose another.");
    }
    return storeIDTaken;
}
function checkIfStoreInLocationAddStore(stores,Xlocation,Ylocation) {
    var storeInLocation = false;
    var storeIDTaken = false;
    $.each(stores || [], function (index, store) {
        if (parseInt(Xlocation) === store.location.x && parseInt(Ylocation) === store.location.y) {
            storeInLocation = true;
        }
    })
    if (!storeInLocation && !storeIDTaken) {
       return false;
    }
    if(storeInLocation){
        $("#StoreLocationTakenLabel").empty().append("There is a store in your chosen location, please choose different location. ");
    }

    return true;
}
function buildAddProductsToStorePage(storeToAdd) {
    storeToAdd.storeProducts = [];
    $("#AddStoreID").empty().append("    <h3>Add products to your new store</h3>\n" +
        "    <label for=\"ProductSelect\" class=\"ProductSelect\">Choose a product:</label>\n" +
        "    <select name=\"ProductSelect\" id=\"ProductSelect\" class = \"ProductSelect\"></select>\n" +
        "    <br>\n" +
        "    <label class=\"ProductSelect\">Set price: </label>\n" +
        "    <input type =\"number\" class =\"ProductSelect\" id=\"productPrice\" min ='1'>\n" +
        "    <input type =\"button\" id=\"AddProductButton\" class=\"btn btn-success ProductSelect\" value=\"Add product\"><br><br>\n" +
        "    <label id=\"addProductPageLabel\"></label><br>\n" +
        "    <input type =\"button\" id=\"CancelAddStoreButton\" class=\"btn btn-danger\" value=\"Cancel\">\n" +
        "    <input type =\"button\" id=\"AddStoreButton\" class=\"btn btn-primary\" value=\"AddStore\">");
        var dataString = "ZoneName="+zoneName;
        $.ajax({
            data: dataString,
            url: Update_Product_Details_Table_URL,
            success: function (products) {
                updateProductComboBox(products);
                $("#AddProductButton").click(function () {
                    addProductButtonFunc(storeToAdd);
                })
                $("#CancelAddStoreButton").click(function () {
                    CancelAddStore();
                })
                $("#AddStoreButton").click(function () {
                    addStoreButtonFunc(storeToAdd);
                })


            }
        });

}
function addStoreButtonFunc(storeToAdd) {
    if(storeToAdd.storeProducts === null){
        $("#addProductPageLabel").empty().append("Add product to store.")
    }else{
        $.ajax({
            data:{StoreToAdd: JSON.stringify(storeToAdd), ZoneName: zoneName},
            url: ADD_STORE_URL,
            success: function (response) {
                $("#AddStoreID").empty().append(response);
            }
        });
        $.ajax({
            data:{Store: JSON.stringify(storeToAdd), ZoneName: zoneName,AreaManagerUserName:zoneDetails.ownerName},
            url: SEND_NEW_STORE_ALERT_URL,
            success: function (response) {
            }
        });

    }

}
function addProductButtonFunc(storeToAdd) {
    var productOptions = document.getElementById('ProductSelect');
    var productToAddToStore;
    if (document.getElementById("productPrice").value === "") {
        $("#addProductPageLabel").empty().append("Enter product price.")
    } else {
        $("#addProductPageLabel").empty();
        var allOptions = document.getElementsByClassName("productOption");
        var optionToRemove;
        $.each(allOptions || [],function (index,option) {
            if(option.id === productOptions.options[productOptions.selectedIndex].id){
                productToAddToStore = new StoreProduct();
                productToAddToStore.productID = option.id;
                productToAddToStore.price = document.getElementById("productPrice").value;
                storeToAdd.storeProducts.push(productToAddToStore)
                optionToRemove = option;
            }
        })
        optionToRemove.parentNode.removeChild(optionToRemove);
        if(productOptions.options[productOptions.selectedIndex] === undefined){
            $(".ProductSelect").hide();
        }
    }
}
function updateProductComboBox(products) {
    $("#ProductSelect").empty();
    $.each(products || [],function (index,product) {
        var productName = "<option value='myValue' class='productOption' id = 'op'>"+product.productName+"</option>";
        $("#ProductSelect").append(productName);
        document.getElementById("op").setAttribute("id",product.productID);
        document.getElementById(product.productID).setAttribute("value",product.productName);
    })
}
function CancelAddStore() {
    $("#AddStoreID").empty().append("<h3>Store was not added</h3>")
}
function buildFeedBackTable(){
    var dataString = "ZoneName="+zoneName;
    $.ajax({
        data: dataString,
        url: GET_STORES_BY_OWNER_URL,
        success: function (stores) {
            putFeedBacksInTable(stores);
        }
    });
}
function putFeedBacksInTable(stores){
    $("#feedBackDetailsTableBody").empty();
    $.each(stores || [] ,function (index,store) {
        $.each(store.storeFeedBacks || [] ,function (index,feedBack) {
            var d = new Date(feedBack.orderDate);
            var n = d.toDateString();

            $("#feedBackDetailsTableBody").append("<tr><td>"+feedBack.customerName+"</td><td>"+
                store.name+"</td><td>"+ n +"</td><td>"+
                feedBack.rating+"</td><td>"+feedBack.feedbackText+"</td></tr>")
        })
    })
}
$(function(){// area name init
    $("#UserChosenZoneName").empty().append(zoneName);
});
function buildStoreOwnerOrderHistoryLandingPage() {
    $("#StoreOwnerOrderHistoryID").empty().append("    <h3>Order History</h3>\n" +
        "    <div id = \"OwnerStores\">\n" +
        "\n" +
        "    </div>");
    buildStoreOwnerOrders();


}
function buildStoreOwnerOrders() {
    //GET STORES BY OWNER
    var dataString = "ZoneName="+zoneName;
    var storesByOwner;
    $.ajax({
        data: dataString,
        url: GET_STORES_BY_OWNER_URL,
        success: function (stores) {
            storesByOwner = stores;
            createStoreSquares(stores);
        }
    });
}
function createStoreSquares(stores) {
    $("#OwnerStores").append("<div id='rowNum' class='row'></div>")
    $.each(stores || [],function (index,store) {
        createStoreSquare(store);
    })
}
function createStoreSquare(store) {
    $("#rowNum").append("<div id = 'colCount' class='storeCol'></div>");
    document.getElementById("colCount").setAttribute("id","Square"+store.ID);

    $("#Square"+store.ID).append("<label class = \"StoreName\">"+store.name+"</label><br><br>\n" +
        "<input type = \"button\" class = \"btn btn-primary\" id=\"StoreButton\" value = \"Orders\">\n"
        );
    document.getElementById("StoreButton").setAttribute("id","button"+store.ID);
    showOrdersFunc(store);
}
function showOrdersFunc(store) {
    $("#button"+store.ID).click(function () {
        $("#StoreOwnerOrderHistoryID").empty().append(" <h3 class = \"centeredHeader\">"+store.name+"</h3>\n" +
            "    <div class = \"tbl-header\" id = \"storeOrderHistoryHeader\">\n" +
            "        <table class=\"StoreOrderHistoryTable\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
            "            <thead>\n" +
            "            <tr><th>Order id</th>\n" +
            "                <th>Date</th>\n" +
            "                <th>Customer name | Location</th>\n" +
            "                <th>Amount of products</th>\n" +
            "                <th>Product cost</th>\n" +
            "                <th>Delivery cost</th>\n" +
            "                <th>Order details</th>\n" +
            "            </tr>\n" +
            "            </thead>\n" +
            "        </table>\n" +
            "    </div>\n" +
            "    <div class=\"tbl-content\" id =\"tbl-store-orderHistory-content\">\n" +
            "        <table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" id = \"storeOrderHistoryDetailsTable\">\n" +
            "            <tbody id=\"storeOrderHistoryDetailsTableBody\">\n" +
            "\n" +
            "            </tbody>\n" +
            "        </table>\n" +
            "        <div id=\"storeOrderHistoryModals\">\n" +
            "\n" +
            "        </div>\n" +
            "    </div>");
            $.each(store.storeOrderHistory || [],function (index,order) {
                addStoreOrderToTable(order);
            })
    })
}
function addStoreOrderToTable(order) {
    $("#storeOrderHistoryDetailsTableBody").append("<tr><td>"+order.OrderID+"</td><td>"+ order.date.day+'/'+order.date.month+'/'+order.date.year +"</td><td>"+order.customerName+"|"+order.customerLocation.x+","+order.customerLocation.y+"</td>" +
        "<td>"+order.amountOfProducts+"</td><td>"+order.totalProductsPrice.toFixed(2)+'$'+"</td>" +
        "<td>"+order.deliveryPrice.toFixed(2)+'$'+"</td><td><input data-toggle='modal' id='orderID' data-target = '#myModal' class='btn btn-primary' type = 'button' value='Details' ></td></tr>");
        buildStoreOrderModal(order);
    document.getElementById("orderID").setAttribute("id",order.OrderID+"Modal");
    document.getElementById(order.OrderID+"Modal").setAttribute("data-target",'#'+"myModal"+order.OrderID+"order");
}
function buildStoreOrderModal(order) {
    var orderHeader = "Order ID: " +order.OrderID+ " - details";
    var modalID = "myModal"+order.OrderID+"order";
    var modalUniqID = order.OrderID + "ModalCustomerOrderHeader";

    $("#storeOrderHistoryModals").append("<div class=\"modal\" id=\"myModal\">\n" +
        "    <div class=\"modal-dialog mw-100 w-80\">\n" +
        "    <div class=\"modal-content\">\n" +
        "\n" +
        "    <!-- Modal Header -->\n" +
        "<div class=\"modal-header\">\n" +
        "    <h4 class=\"modal-title\">" +orderHeader+"</h4>\n" +
        "<button type=\"button\" id = \"closeCustomerOrderModalButton\" class=\"close\" data-dismiss=\"modal\">&times;</button>\n" +
        "</div>\n" +
        "\n" +
        "<!-- Modal body -->\n" +
        "<div class=\"modal-body\" id =\"modalUniqID\">" )

    document.getElementById("modalUniqID").setAttribute("id",modalUniqID);
    document.getElementById("myModal").setAttribute("id",modalID);
    buildStoreOrderDetailsTable(order,modalUniqID);
    $("#" + modalUniqID).append("</div>\n" +
        "\n" +
        "    <!-- Modal footer -->\n" +
        "    <div class=\"modal-footer\">\n" +
        "        <button type=\"button\" id = \"closeCustomerOrderModalButton\" class=\"btn btn-danger\" data-dismiss=\"modal\">Close</button>\n" +
        "        </div>\n" +
        "\n" +
        "        </div>\n" +
        "        </div>");


}
function buildStoreOrderDetailsTable(order, modalUniqID){
    $('#' + modalUniqID).append("<div class = \"tbl-header\">\n" +
        "    <table class=\"StoreTable\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
        "    <thead>\n" +
        "    <tr><th>ID</th>\n" +
        "    <th>Name</th>\n" +
        "<th>Sold by</th>\n" +
        "<th>Total amount</th>\n" +
        "<th>Price per unit/kilo</th>\n" +
        "<th>Total price</th>\n" +
        "<th>Sale</th>\n" +
        "</tr>\n" +
        "</thead>\n" +
        "</table>\n" +
        "</div>");
    $('#' + modalUniqID).append("<div class=\"tbl-content\" id =\"tbl-store-content\">\n" +
        "    <table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" id = \"CustomerOrderDetailsTable\">\n" +
        "    <tbody id=\"CustomerOrderDetailsTableBody\">");
    var value = order.OrderID+"ModalCustomerOrderTable";
    document.getElementById("CustomerOrderDetailsTable").setAttribute("id",value);

    var tableIdString= "#" + order.OrderID+"ModalCustomerOrderTable";
        $.each(order.soldProducts || [],function (jndex,soldProduct) {
            var productInTable = "<tr><td>" + soldProduct.id + "</td><td>" + soldProduct.name + "</td>"+ "<td>"
                + soldProduct.category + "</td><td>" + soldProduct.amountSoldInOrder
                + "</td><td>"+soldProduct.price+"</td><td>"+soldProduct.price*soldProduct.amountSoldInOrder+"</td><td>Not part of a sale</td></tr>" ;
            $(tableIdString).append(productInTable);
        })
        $.each(order.productsSoldOnSale || [],function (jndex,saleProduct) {
            var productInTable = "<tr><td>" + saleProduct.id + "</td><td>" + saleProduct.name + "</td>"+ "<td>"
                + saleProduct.category + "</td><td>" + saleProduct.amountSoldInOrder
                + "</td><td>"+saleProduct.price+'$'+"</td><td>"+saleProduct.price*saleProduct.amountSoldInOrder+'$'+"</td><td>"+saleProduct.saleName+"</td></tr>" ;
            $(tableIdString).append(productInTable);
        })

    $(modalUniqID).append("</tbody></table></div>")
}
function isThereStoreInChosenLocation(Xlocation,Ylocation) {
    var dataString = "ZoneName="+zoneName;
    $.ajax({
        data: dataString,
        url: Update_Store_Details_Table_URL,
        success: function (stores) {
            checkIfStoreInLocation(stores,Xlocation,Ylocation);
        }
    });

}
function checkIfStoreInLocation(storesInArea,Xlocation,Ylocation) {
    var storeInLocation = false;
    $.each(storesInArea || [], function (index, store) {
        if (parseInt(Xlocation) === store.location.x && parseInt(Ylocation) === store.location.y) {
            storeInLocation = true;
        }
    })
    if (!storeInLocation) {
        if ($("#StaticRadio").is(":checked")) {
            customerLevelOrderJS.orderType = "Static";
            customerLevelOrderJS.storeOrders.push((new StoreLevelOrderJS()));
            customerLevelOrderJS.storeOrders[0].storeName = document.getElementById('StoresSelect').value;

        } else {
            customerLevelOrderJS.orderType = "Dynamic";
            customerLevelOrderJS.storeOrders.push((new StoreLevelOrderJS()));
            customerLevelOrderJS.storeOrders[0].storeName = null;

        }
        customerLevelOrderJS.date = document.getElementById('DeliveryDate').value;
        customerLevelOrderJS.CustomerXLocation = document.getElementById('XLocation').value;
        customerLevelOrderJS.CustomerYLocation = document.getElementById('YLocation').value;
        $("#PlaceOrderAndFeedBackID").empty();
        loadChooseProductPage();
    }else{
        $("#LocationLabel").empty().append("There is a store in your chosen location, please choose different location. ");
    }
}

function PlaceOrderLandingPageSubmitFunc() {// Place order submit function

    $("#placeOrderGoToProducts").click(function () {
        var Xlocation = document.getElementById("XLocation").value;
        var Ylocation = document.getElementById("YLocation").value;
        if(parseInt(Xlocation)<=50 && parseInt(Xlocation)>=1 && parseInt(Ylocation)<=50 && parseInt(Ylocation)>=1) {
            isThereStoreInChosenLocation(Xlocation, Ylocation);
        }else {
            $("#LocationLabel").empty().append("Location coordinates must be between 1-50.");
        }


    })
}
function PriceButtonActionSet(products) {
    $(".RoundButton").click(function () {
        var itemID = this.id;
        var buttonValue = this.value;
        itemID = itemID.slice(0,-1);
        $.each(products||[],function (index,product) {
            if(product.productID === parseInt(itemID)){
                if(buttonValue === '+'){
                    PriceIncrease(product);
                }else{
                    PriceDecrease(product);
                }
            }
        })
    })
}
function PriceDecrease(product) {
    var price;
    var amount;
    amount = document.getElementById(product.productID+"Amount").textContent;
    amount = parseFloat(amount);
    if(customerLevelOrderJS.orderType === "Static") {
        price = document.getElementById(product.productID).textContent;
        price = parseFloat(price);
    }
    if(product.category === "Quantity"){
        if(amount>0){
            amount--;
            if(customerLevelOrderJS.orderType === "Static") {
                price -= product.productPrice;
                document.getElementById(product.productID).textContent = price + '$';
            }
        }
    }else{
        if(amount > 0){
            amount -= 0.5;
            if(customerLevelOrderJS.orderType === "Static") {
                price -= product.productPrice;
                document.getElementById(product.productID).textContent = price + '$';
            }
        }
    }
    document.getElementById(product.productID+"Amount").textContent = amount;
}
function PriceIncrease(product) {
    var price;
    var amount;
    amount = document.getElementById(product.productID+"Amount").textContent;
    amount = parseFloat(amount);
    if(customerLevelOrderJS.orderType ==="Static") {
        price = document.getElementById(product.productID).textContent;
        price = parseFloat(price);
        price += product.productPrice;
        document.getElementById(product.productID).textContent = price + '$';
    }
    if(product.category === "Quantity"){
        amount++;
    }else{
        amount += 0.5;
    }
    document.getElementById(product.productID+"Amount").textContent = amount;
}
function loadChooseProductPage(){
    $("#PlaceOrderAndFeedBackID").append("<h3 id='chooseProductsHeader'>Choose products</h3><br><br>" +
        "<div id ='ProductSquares'> " +
        "</div>"+"<br><br>" +
        "<div><label id='warningProductChoiceLabel'></label><br>" +
        "<input type = 'button' id='PlaceOrderGoToSales' class='btn btn-primary' value ='Next'></div>"
    );
    $("#warningProductChoiceLabel").hide();
    if(customerLevelOrderJS.orderType ==="Dynamic") {
        loadDynamicProductSquares();
    }else{
        loadStaticProductSquares();
    }
}
function loadStaticProductSquares() {

    $.ajax({
        data: {StoreName: customerLevelOrderJS.storeOrders[0].storeName, ZoneName: zoneName},
        url: GET_PRODUCTS_FROM_STORE_URL,
        success: function (products) {
            loadProducts(products);
            PriceButtonActionSet(products);
            goToSalesButtonActionSet();
        }
    });
}
function goToSalesButtonActionSet() {
    $("#PlaceOrderGoToSales").click(function () {
        myAvailableSales = new SaleHandler();
        mySales = new SaleHandler();
        var totalAmount = 0;
        var productsAmounts = document.getElementsByClassName("Amount");
        $.each(productsAmounts || [],function (index,singleProductAmount) {
            totalAmount+=parseFloat(singleProductAmount.textContent);
        })
        if(totalAmount === 0){
            $("#warningProductChoiceLabel").show().empty().append('Shopping cart cant be empty, Choose products.');
        }else{
            updateOrderWithRegProducts();
        }
    })

}
function updateOrderWithRegProducts() {
    var productsAmounts = document.getElementsByClassName("Amount");
    var regProd;
    $.each(productsAmounts || [],function (index,singleProductAmount) {
        if(parseFloat(singleProductAmount.textContent)>0) {
            regProd = new RegProduct();
            regProd.Amount = singleProductAmount.textContent;
            regProd.productID = singleProductAmount.id.slice(0,-6);
            customerLevelOrderJS.storeOrders[0].regProducts.push(regProd);
        }
    })
    createBasicCustomerOrderAndSales();

}

function getMySales() {
    $.ajax({
        data:{StoreOrders: JSON.stringify(customerLevelOrderJS.storeOrders), ZoneName: zoneName, OrderType: customerLevelOrderJS.orderType, Date: customerLevelOrderJS.date, storeName: customerLevelOrderJS.storeOrders[0].storeName},
        url: Get_Sales_URL,
        success: function (sales) {
             myAvailableSales.mySales = sales;
             createProductMap();
             buildSalesPages();
        }
    });
}
function createProductMap() {
    myAvailableSales.productMapIDAmount = new Map();
    $.each(customerLevelOrderJS.storeOrders || [],function (index,storeOrder) {
        $.each(storeOrder.regProducts||[],function (index,regProd) {
            myAvailableSales.productMapIDAmount.set(regProd.productID,regProd.Amount);
        })
    })
}
function createBasicCustomerOrderAndSales() {
    $.ajax({
        data:{CustomerOrder: JSON.stringify(customerLevelOrderJS), ZoneName: zoneName, OrderType: customerLevelOrderJS.orderType, Date: customerLevelOrderJS.date, StoreName: customerLevelOrderJS.storeOrders[0].storeName, CustomerXLocation: customerLevelOrderJS.CustomerXLocation, CustomerYLocation: customerLevelOrderJS.CustomerYLocation},
        url: CREATE_BASIC_ORDER_URL,
        error: function (e) {
            console.error(e);
        },
        success: function (customerOrder) {
            customerLevelOrderJS = null;
            customerLevelOrderJS = customerOrder;
            getMySales();

        }
    });
}

function loadDynamicProductSquares() {
    var dataString = "ZoneName="+zoneName;
    $.ajax({
        data: dataString,
        url: Update_Product_Details_Table_URL,
        success: function (products) {
            loadProducts(products);
            PriceButtonActionSet(products);
            goToSalesButtonActionSet();
        }
    });

}
function loadProducts(products) {
    var rowNum = 0;
    var colCount = 0;
    $("#ProductSquares").append("<div id='rowNum' class='row'></div>")
    document.getElementById("rowNum").setAttribute("id",rowNum);
        $.each(products || [],function (index,product) {
          createProductSquare(product,rowNum,colCount);
          colCount++;
        })
}
function  createProductSquare(product,rowNum,colCount) {

    $("#"+rowNum).append("<div id = 'colCount' class='productCol'></div>");
    var priceString;
    if(customerLevelOrderJS.orderType === "Dynamic"){
        priceString = "";
    }else{
        priceString = "<label id='Price' class = \"Price\">0.0</label>\n";
    }
    document.getElementById("colCount").setAttribute("id",rowNum+''+colCount+"spot");
    var spotStringID="#"+rowNum+""+colCount+"spot";
    $(spotStringID).append("<div class=\"ProductSquare\">\n" +
        "<div class = \"squareText\">\n" +
        "<label class = \"ProductName\">"+product.productName+"</label><br><br>\n" +
        "<input type = \"button\" class = \"RoundButton Product-\" id=\"Product-\" value = \"-\">\n" +
        "<label class = \"Amount\" id='Amount'>0.0</label>\n" +
        "<input type = \"button\" class = \"RoundButton Product+\" id=\"Product+\" value = \"+\">\n" +
        "<br>\n"
        +priceString +
    "</div>\n" +
        "</div>");
    document.getElementById("Product-").setAttribute("id",product.productID+"-");
    document.getElementById("Product+").setAttribute("id",product.productID+"+");
    document.getElementById("Amount").setAttribute("id",product.productID+"Amount");
    if(priceString!==""){
        document.getElementById("Price").setAttribute("id",product.productID);
    }
}
function hideElementOnPlaceOrderLandingPage(){//PlaceOrder show/hide dynamic static things.
    $(".StoresSelect").hide();
    $("#StaticRadio").click(function () {
        $(".StoresSelect").show();
    })
    $("#DynamicRadio").click(function () {
        $(".StoresSelect").hide();
    })
}
function PlaceOrderBuildSummeryPage() {
    $("#PlaceOrderAndFeedBackID").empty().append("<h3 class = 'centeredOrderLabel'>Summery</h3>\n" +
        "    <form id=\"SummeryForm\">\n" +
        "        <div id = \"SingleStoreOrders\">\n" +
        "\n" +
        "        </div>\n" +
        "        <div id=\"TotalOrderSummery\">\n" +
        "\n" +
        "        </div>\n" +
        "        <button id = \"CancelOrder\" type=\"button\" class = \"btn btn-danger\">Cancel order</button>\n" +
        "        <button id = \"ConfirmOrder\" type=\"button\" class = \"btn btn-primary\">Confirm order</button>\n" +
        "    </form>");
    buildOrderSummeryPerStore();
    buildOrderSummeryLabels();
    cancelOrderButtonFunc();
    confirmOrderButtonFunc();
}
function cancelOrderButtonFunc() {
    $("#CancelOrder").click(function () {
        $("#PlaceOrderAndFeedBackID").empty().append("<label class = 'centeredOrderLabel'>Order was canceled!</label>");
    })
}
function confirmOrderButtonFunc() {
    $("#ConfirmOrder").click(function () {

        $.ajax({
            data: {CustomerOrder: JSON.stringify(customerLevelOrderJS),ZoneName: zoneName},
            url: PLACE_ORDER_IN_MARKET_URL,
            success: function (result) {
                $("#PlaceOrderAndFeedBackID").empty().append("<label class = 'centeredOrderLabel'>"+result+"</label>" +
                    "<br><br><input type = 'button' value  = 'Click here to leave feedback' class = 'centeredButton btn btn-success' id = 'goToLeaveFeedBackPage'>");
                $("#goToLeaveFeedBackPage").click(function () {
                    showStoresToFeedBackTo();
                })
                $.each(customerLevelOrderJS.storeOrders || [] ,function (index, storeOrder) {
                    $.ajax({
                        data: {StoreOrder: JSON.stringify(storeOrder),ZoneName: zoneName},
                        url: SEND_ORDER_ALERT_URL,
                        success: function (result) {
                            console.log(result);
                        }
                    })
                })
            }

        });

    })
}
function showStoresToFeedBackTo() {
    $("#PlaceOrderAndFeedBackID").empty().append("<h3 id='chooseStoreToFeedback'>Choose store</h3><br><br>" +
        "<div id ='StoreFeedBackSquares'> " +
        "</div>"+"<br><br>"
        );
    createStoreFeedbackSquares();

}
function createStoreFeedbackSquares() {
    $("#StoreFeedBackSquares").append("<div id='rowNum' class='row'></div>")
    if(customerLevelOrderJS.storeOrders.length === 0){
        $("#StoreFeedBackSquares").empty().append("<h3 class='centeredLabel'>You left feedback to all of the stores in your order!</h3>")
    }else{
        $.each(customerLevelOrderJS.storeOrders || [],function (index,store) {
        createFeedbackStoreSquare(store);
    })
    }
}
function createFeedbackStoreSquare(store) {
    $("#rowNum").append("<div id = 'colCount' class='feedbackStoreCol'></div>");
    document.getElementById("colCount").setAttribute("id","Square"+store.storeID);

    $("#Square"+store.storeID).append("<label class = \"StoreName\">"+store.storeName+"</label><br><br>\n" +
        "<input type = \"button\" class = \"btn btn-primary\" id=\"StoreButton\" value = \"Leave Feedback\">\n"
    );
    document.getElementById("StoreButton").setAttribute("id","button"+store.storeID);
    $("#button"+store.storeID).click(function () {
        LeaveFeedBackToStoreFunc(store);
    });
}
function LeaveFeedBackToStoreFunc(store) {
    $("#PlaceOrderAndFeedBackID").empty().append("<div id='addFeedbackPageDiv'><label class = \"centeredFeedBackLabel\">Rate your shopping experience</label>\n" +
        "    <input type=\"number\" min=\"1\" max=\"5\" required id='userRating'>\n" +
        "    <br><label id='ratingErrorLabel' class ='ErrorLabel'></label><br>\n" +
        "    <label class =\"centeredFeedBackLabel\">Leave FeedBack:</label><br>\n" +
        "    <textarea id=\"userFeedBack\" name=\"feedBack\" rows=\"4\" cols=\"50\" required></textarea><br>" +
        "    <input type = \"button\" value=\"Submit feedback\" id=\"SubmitFeedBackButton\" class = \"btn btn-primary centeredFeedBackButton\"></div>");
    $("#SubmitFeedBackButton").click(function () {
        if(parseInt(document.getElementById("userRating").value)<=5 && parseInt(document.getElementById("userRating").value)>=1) {
                const index = customerLevelOrderJS.storeOrders.indexOf(store);
                if (index > -1) {
                    customerLevelOrderJS.storeOrders.splice(index, 1);
                }
                var myFeedBack = new FeedBack();
                myFeedBack.customerName = customerLevelOrderJS.customerName;
                myFeedBack.orderDate = customerLevelOrderJS.date;
                myFeedBack.rating = userRating.value;
                myFeedBack.feedbackText = userFeedBack.value;

                $.ajax({
                    data: {FeedBack: JSON.stringify(myFeedBack), StoreID: store.storeID, ZoneName: zoneName},
                    url: GIVE_FEEDBACK_TO_STORE_URL,
                    success: function (response) {
                        console.log(response);
                    }
                });
            $.ajax({
                data: {FeedBack: JSON.stringify(myFeedBack), StoreID: store.storeID, ZoneName: zoneName},
                url: SEND_FEEDBACK_ALERT_URL,
                success: function (response) {
                    console.log(response);
                }
            });
                showStoresToFeedBackTo();

        }else{
            $("#ratingErrorLabel").empty().append("Please leave a rating between 1-5.");
        }
    })
}
function buildOrderSummeryPerStore() {
    $.each(customerLevelOrderJS.storeOrders || [],function (index,storeOrder) {
        var storeOrderString = createStoreOrderString(storeOrder);
        $("#SingleStoreOrders").append(storeOrderString);
    })

}
function createStoreOrderString(storeOrder) {
    var productInTableString = "";
    $.each(storeOrder.regProducts || [] ,function (index,regProd) {
        productInTableString += createProductString(regProd);
    })
    $.each(storeOrder.saleProducts || [] ,function (index,saleProd) {
        productInTableString += createProductString(saleProd);
    })
    var orderString =  "<div class = \"singleOrder\"> <div class = \"tbl-header hiddenTableHead\">\n" +
        "                <table class=\"StoreOrderSummeryHead\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
        "                    <thead>\n" +
        "                    <tr><th>Store name</th>\n" +
        "                        <th>Store ID</th>\n" +
        "                        <th>PPK</th>\n" +
        "                        <th>Distance from customer</th>\n" +
        "                        <th>Delivery price</th>\n" +
        "                    </tr>\n" +
        "                    </thead>\n" +
        "                </table>\n" +
        "            </div>\n" +
        "            <div class=\"tbl-content shortTableHead\" '>\n" +
        "                <table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" id = \"storeOrderSummery1\">\n" +
        "                    <tbody id=\"storeOrderSummery1TableBody\">\n" +
        "                    <tr>\n" +
        "                        <td>Store name: "+storeOrder.storeName+"</td>\n" +
        "                        <td>Store ID: "+storeOrder.storeID+"</td>\n" +
        "                        <td>PPK: "+storeOrder.PPK+"</td>\n" +
        "                        <td>Distance from customer: "+storeOrder.distanceFromCustomer.toFixed(2)+"</td>\n" +
        "                        <td>Delivery price: "+storeOrder.deliveryPrice.toFixed(2)+"</td>\n" +
        "                    </tr>\n" +
        "                    </tbody>\n" +
        "                </table>\n" +
        "            </div>\n" +
        "            <br>\n" +
        "            <label id=\"productsLabel\">Products:</label>\n" +
        "            <div class = \"tbl-header storeOrderProductsHeader\">\n" +
        "                <table class=\"StoreOrderProductsSummeryHead\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
        "                    <thead>\n" +
        "                    <tr><th>Product name</th>\n" +
        "                        <th>Product ID</th>\n" +
        "                        <th>Sold By</th>\n" +
        "                        <th>Amount</th>\n" +
        "                        <th>Price per unit/kilo</th>\n" +
        "                        <th>Total</th>\n" +
        "                        <th>Purchased  on sale</th>\n" +
        "                    </tr>\n" +
        "                    </thead>\n" +
        "                </table>\n" +
        "            </div>\n" +
        "            <div class=\"tbl-content tbl-order-summery-content\">\n" +
        "                <table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" id = \"storeOrderProductSummery\">\n" +
        "                    <tbody id=\"storeOrderProductSummeryTableBody\">\n" +
        "                    \n" +productInTableString+
        "                    \n" +
        "                    </tbody>\n" +
        "                </table>\n" +
        "            </div>\n" +
        "        </div><br><br></div>";
    return orderString;

}
function createProductString(product) {

    return "<tr><td>" + product.productName + "</td><td>" + product.productID + "</td><td>" + product.category +
        "</td><td>" + product.Amount + "</td><td>" + product.totalProductPrice / product.Amount + "</td><td>" + product.totalProductPrice + "</td><td>" + ((product.saleName !== undefined) ? product.saleName : "") +
        "</td></tr>";
}
function buildOrderSummeryLabels() {
    $("#TotalOrderSummery").append(" <div class = \"tbl-header hiddenTableHead\">\n" +
        "                <table class=\"OrderSummeryHead\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
        "                    <thead>\n" +
        "                    <tr><th>Customer name</th>\n" +
        "                        <th>Customer ID</th>\n" +
        "                        <th>Product price</th>\n" +
        "                    </tr>\n" +
        "                    </thead>\n" +
        "                </table>\n" +
        "        </div>\n" +
        "            <div class=\"tbl-content tbl-order-summery-content customerDetails\">\n" +
        "                <table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" id = \"orderSummery1\" >\n" +
        "                    <tbody id=\"orderSummery1TableBody\" >\n" +
        "                    <tr>\n" +
        "                        <td id=\"CustomerNameRow\">Customer name: </td>\n" +
        "                        <td id=\"CustomerIDRow\">Customer ID: </td>\n" +
        "                        <td id=\"ProductPriceRow\">Product price: </td>\n" +
        "                    </tr>\n" +
        "                    </tbody>\n" +
        "                </table>\n" +
        "            </div>\n" +
        "            <div class = \"tbl-header hiddenTableHead \">\n" +
        "                <table class=\"OrderSummeryHead \" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
        "                    <thead>\n" +
        "                    <tr>\n" +
        "                        <th>Delivery price</th>\n" +
        "                        <th>Total price</th>\n" +
        "                        <th>Delivery date</th>\n" +
        "                    </tr>\n" +
        "                    </thead>\n" +
        "                </table>\n" +
        "            </div>\n" +
        "            <div class=\"tbl-content tbl-order-summery-content customerDetails\">\n" +
        "                <table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" id = \"orderSummery2\">\n" +
        "                    <tbody id=\"orderSummery2TableBody\">\n" +
        "                    <tr>\n" +
        "                        <td id=\"DeliveryPriceRow\">Delivery price: </td>\n" +
        "                        <td id=\"TotalPriceRow\">Total price: </td>\n" +
        "                        <td id=\"DeliveryDateRow\">Delivery date: </td>\n" +
        "                    </tr>\n" +
        "                    </tbody>\n" +
        "                </table>\n" +
        "            </div>");
    $("#CustomerNameRow").append(customerLevelOrderJS.customerName);
    $("#CustomerIDRow").append(customerLevelOrderJS.customerID);

    var totalProductPrice = calcTotalProductPrice();
    $("#ProductPriceRow").append(totalProductPrice.toFixed(2));
    var totalDeliveryPrice = calcTotalDeliveryPrice();
    $("#DeliveryPriceRow").append(totalDeliveryPrice.toFixed(2));

    $("#TotalPriceRow").append((totalProductPrice + totalDeliveryPrice).toFixed(2));
    $("#DeliveryDateRow").append(customerLevelOrderJS.date);
}
function calcTotalDeliveryPrice() {
    var deliveryPrice = 0;
    $.each(customerLevelOrderJS.storeOrders || [],function (index,storeOrder) {
        deliveryPrice += storeOrder.deliveryPrice;
    })
    return deliveryPrice;
}
function calcTotalProductPrice() {
    var totalPrice = 0;
    $.each(customerLevelOrderJS.storeOrders || [],function (index,storeOrder) {
        $.each(storeOrder.regProducts || [],function (index,regProd) {
            totalPrice  += regProd.totalProductPrice;
        })
        $.each(storeOrder.saleProducts || [],function (index,saleProd) {
            totalPrice  += saleProd.totalProductPrice;
        })
    })
    return totalPrice;
}
function buildSalesPages() {
    $("#PlaceOrderAndFeedBackID").empty().append("    <h3 id='chooseSalesHeader'>Choose sales:</h3>\n" +
        "    <form id=\"AddSalesToOrderForm\">\n" +
        "        <div class = \"tbl-header salesTableHeader\">\n" +
        "            <table class=\"AvailableSales\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
        "                <thead>\n" +
        "                <tr><th>Sale Details</th>\n" +
        "                </tr>\n" +
        "                </thead>\n" +
        "            </table>\n" +
        "        </div>\n" +
        "        <div class=\"tbl-content\" id =\"tbl-available-sales-content\">\n" +
        "            <table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" id = \"availableSalesContent\">\n" +
        "                <tbody id=\"availableSalesContentBody\">\n" +
        "\n" +
        "                </tbody>\n" +
        "            </table>\n" +
        "        </div>\n" +
        "        \n" +
        "        <label for=\"SaleSelect\" class=\"SaleSelect SaleSelectToHide\">Choose offer:</label>\n" +
        "        <select name=\"SaleSelect\" id=\"SaleSelect\" class = \"SaleSelect SaleSelectToHide\">\n" +
        "\n" +
        "        </select>\n" +
        "        <input type = \"button\" value = \"Add sale\" class = \"SaleSelect SaleSelectToHide\" id=\"AddSaleButton\">\n" +
        "        <br><br>\n" +
        "        <label class=\"SaleSelect salesLabel\">Sales added</label>\n" +
        "        <div class = \"tbl-header SaleSelect salesTableHeader\">\n" +
        "            <table class=\"SaleSelect\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
        "                <thead>\n" +
        "                <tr><th>Product name</th>\n" +
        "                    <th>Product ID</th>\n" +
        "                    <th>Amount</th>\n" +
        "                    <th>Price per unit</th>\n" +
        "                    <th>Total Price</th>\n" +
        "                    <th>Sale name</th>\n" +
        "                </tr>\n" +
        "                </thead>\n" +
        "            </table>\n" +
        "        </div>\n" +
        "        <div class=\"tbl-content SaleSelect\" id =\"tbl-chosen-sales-content\">\n" +
        "            <table class=\"SaleSelect\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" id = \"chosenSalesContent\">\n" +
        "                <tbody class=\"SaleSelect\" id=\"chosenSalesContentBody\">\n" +
        "\n" +
        "                </tbody>\n" +
        "            </table>\n" +
        "        </div>\n" +
        "        <br><br>\n" +
        "        <label class = \"salesLabel\">Cart</label>\n" +
        "        <div class = \"tbl-header salesTableHeader\">\n" +
        "            <table  cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
        "                <thead>\n" +
        "                <tr><th>Product name</th>\n" +
        "                    <th>Product ID</th>\n" +
        "                    <th>Amount</th>\n" +
        "                    <th>Price per unit</th>\n" +
        "                    <th>Total Price</th>\n" +
        "                </tr>\n" +
        "                </thead>\n" +
        "            </table>\n" +
        "        </div>\n" +
        "        <div class=\"tbl-content\" id =\"tbl-cart-content\">\n" +
        "            <table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" id=\"cartContent\">\n" +
        "                <tbody id=\"cartContentBody\">\n" +
        "\n" +
        "                </tbody>\n" +
        "            </table>\n" +
        "        </div>\n" +
        "        <br>\n" +
        "        <button id = \"placeOrderGoToSummery\" type=\"button\" class = \"btn btn-primary\">Next</button>\n" +
        "    </form>")
        $(".SaleSelect").hide();
        addAvailableSalesToTable();
        addSaleButtonClickFunc();
        addItemsToCartTable();
        goToSummeryButtonFunc();
}
function goToSummeryButtonFunc() {
    $("#placeOrderGoToSummery").click(function () {
        var dynamicOfferNumber = 0;
        var saleOffersToAddToOrder = $(".chosenOffer");
        var mySale;
        $.each(saleOffersToAddToOrder || [],function (index,offerInTable) {
            var saleName = offerInTable.id;
            saleName = saleName.slice(9,saleName.length);
            $.each( myAvailableSales.mySales || [],function (index,sale) {
                if(sale.name === saleName){
                    mySale = sale;
                }
            })
            var offerNum = offerInTable.dataset.value;

            var myOrder;
            $.each(customerLevelOrderJS.storeOrders || [],function (index, storeOrder) {
                    if(storeOrder.storeName === mySale.storeName) {
                        myOrder = storeOrder;
                    }
            })

            if(mySale.thenYouGet.offers.length === dynamicOfferNumber){
                dynamicOfferNumber = 0;
            }
            if(offerNum !== undefined) {
                var offerToAdd = mySale.thenYouGet.offers[offerNum];
                var saleProductToAdd = new saleProduct();
                saleProductToAdd.productID = offerToAdd.itemID;
                saleProductToAdd.totalProductPrice = offerToAdd.forAdditional * offerToAdd.quantity;
                saleProductToAdd.Amount = offerToAdd.quantity;
                saleProductToAdd.productName = offerToAdd.productName;
                saleProductToAdd.saleName = mySale.name;
                $.each(productsInArea || [],function (index,product) {
                    if(product.productID === saleProductToAdd.productID)
                        saleProductToAdd.category = product.category;
                })
                if (myOrder.saleProducts === undefined) {
                    myOrder.saleProducts = [];
                }
                myOrder.saleProducts.push(saleProductToAdd);
            }else{
                var offerToAdd = mySale.thenYouGet.offers[dynamicOfferNumber];
                var saleProductToAdd = new saleProduct();
                saleProductToAdd.productID = offerToAdd.itemID;
                saleProductToAdd.totalProductPrice = offerToAdd.forAdditional * offerToAdd.quantity;
                saleProductToAdd.Amount = offerToAdd.quantity;
                saleProductToAdd.productName = offerToAdd.productName;
                saleProductToAdd.saleName = mySale.name;
                $.each(productsInArea || [],function (index,product) {
                    if(product.productID === saleProductToAdd.productID)
                        saleProductToAdd.category = product.category;
                })
                if (myOrder.saleProducts === undefined) {
                    myOrder.saleProducts = [];
                }
                myOrder.saleProducts.push(saleProductToAdd);
                dynamicOfferNumber++;
            }
        })
        console.log(customerLevelOrderJS);
        PlaceOrderBuildSummeryPage();
    })

}
function addItemsToCartTable() {
    $("#cartContentBody").empty();
    $.each(customerLevelOrderJS.storeOrders || [],function (index,storeOrder) {
        this.productID;
        this.totalProductPrice;
        this.Amount;
        $.each(storeOrder.regProducts || [],function (index, regProd) {
            var productInTable = "<tr><td>"+regProd.productName+"</td><td>"+regProd.productID+"</td><td>"+regProd.Amount+"</td><td>"+
                regProd.totalProductPrice/regProd.Amount+"</td><td>"+regProd.totalProductPrice+"</td></tr>";
            $("#cartContentBody").append(productInTable);
        })
        $.each(storeOrder.saleProducts || [],function (index, saleProd) {
            var productInTable = "<tr><td>"+saleProd.productName+"</td><td>"+saleProd.productID+"</td><td>"+saleProd.Amount+"</td><td>"+
                saleProd.totalProductPrice/regProd.Amount+"</td><td>"+saleProd.totalProductPrice+"</td><td>"+saleProd.saleName+"</td></tr>";
                $("#cartContentBody").append(productInTable);
        })
    })
}
function addSaleButtonClickFunc() {
    $("#AddSaleButton").click(function () {
        var offerOptions = document.getElementById("SaleSelect");
        var selectedOption = offerOptions.options[offerOptions.selectedIndex].value;
        selectedOption = parseInt(selectedOption);
        var offerInTable;

        $.each(myAvailableSales.mySales || [], function (index,sale) {
            if(sale.name === chosenSale) {
                var currentNumofProductItemsInCart
                if (canSaleBeMade(sale)) {
                    if (sale.thenYouGet.operator === "ONE-OF") {
                        var selectedOffer = sale.thenYouGet.offers[selectedOption];
                        offerInTable = "<tr class  = 'chosenOffer' id = sale><td>" + selectedOffer.productName + "</td><td>" + selectedOffer.itemID + "</td>" +
                            "<td>" + selectedOffer.quantity + "</td><td>" + selectedOffer.forAdditional + "</td>" +
                            "<td>" + selectedOffer.forAdditional * selectedOffer.quantity + "</td><td>" + sale.name + "</td></tr>";
                        currentNumofProductItemsInCart = myAvailableSales.productMapIDAmount.get(sale.ifYouBuy.itemID) - sale.ifYouBuy.quantity;
                        $("#chosenSalesContentBody").append(offerInTable);
                        document.getElementById("sale").setAttribute("data-value",selectedOption);
                        document.getElementById("sale").setAttribute("id","saleName:"+sale.name);

                    } else {//operator isn't one-of
                        $.each(sale.thenYouGet.offers || [], function (index, offer) {
                            offerInTable = "<tr class  = 'chosenOffer' id = sale><td>" + offer.productName + "</td><td>" + offer.itemID + "</td>" +
                                "<td>" + offer.quantity + "</td><td>" + offer.forAdditional + "</td>" +
                                "<td>" + offer.forAdditional * offer.quantity + "</td><td>" + sale.name + "</td></tr>";
                            $("#chosenSalesContentBody").append(offerInTable);
                            document.getElementById("sale").setAttribute("id","saleName:"+sale.name);
                        })
                        currentNumofProductItemsInCart = myAvailableSales.productMapIDAmount.get(sale.ifYouBuy.itemID) - sale.ifYouBuy.quantity;
                    }
                    myAvailableSales.productMapIDAmount.delete(sale.ifYouBuy.itemID);
                    myAvailableSales.productMapIDAmount.set(sale.ifYouBuy.itemID, currentNumofProductItemsInCart);
                    removeSalesFromTable();
                }
            }
        })
    })
}
function removeSalesFromTable() {
    $.each(myAvailableSales.mySales || [], function (index,sale) {
        var ifYouBuyItemID = sale.ifYouBuy.itemID;
        var amountInCart = myAvailableSales.productMapIDAmount.get(ifYouBuyItemID);
        if(amountInCart< sale.ifYouBuy.quantity){
            var row = document.getElementById(sale.name);
            if(row != null){
            row.parentNode.removeChild(row);
            }
        }
    })

    if(document.getElementById(chosenSale) === null){
        $(".SaleSelectToHide").hide();
    }
}
function canSaleBeMade(sale) {
    var ifYouBuyItemID = sale.ifYouBuy.itemID;
    var amount =  myAvailableSales.productMapIDAmount.get(ifYouBuyItemID);
    return amount >= sale.ifYouBuy.quantity;

}
function addAvailableSalesToTable() {
    $.each(myAvailableSales.mySales || [],function (index,sale) {
        $("#availableSalesContentBody").append("<tr class = 'saleRow'><td id='myID'>"+sale.saleString+"</td></tr>")
        document.getElementById("myID").setAttribute("id",sale.saleString.substr(0, sale.saleString.indexOf(':')));
    })
    chooseRows();
}
function buildPlaceOrderLandingPage() {
    $("#PlaceOrderAndFeedBackID").empty().append("<h3 id ='placeOrderHeader'>Place order</h3>\n" +
        "    <form id=\"PlaceOrderDateAndTypeForm\">\n" +
        "        <label for=\"DeliveryDate\">Delivery date:</label>\n" +
        "        <input type=\"date\" id=\"DeliveryDate\" name=\"DeliveryDate\" required>\n" +
        "        <br><br>\n" +
        "        <div class = \"customerLocationDiv\">\n" +
        "            <label >Delivery location (between 1-50):</label>\n" +
        "            X</label><input type=\"number\" id=\"XLocation\" name=\"XLocation\" min=\"1\" max=\"50\" required>&nbsp&nbsp\n" +
        "            Y</label><input type=\"number\" id=\"YLocation\" name=\"YLocation\" min=\"1\" max=\"50\" required>\n" +
        "            <br>\n" +
        "            <label id=\"LocationLabel\"></label>\n" +
        "        </div>\n" +
        "        <br>\n" +
        "        <h5>Order type</h5>\n" +
        "        <br>\n" +
        "        <div class=\"radio\">\n" +
        "            <input id = \"DynamicRadio\" type=\"radio\" name=\"orderType\" value = \"Dynamic\" checked = \"checked\">  Dynamic&nbsp&nbsp\n" +
        "            <input id = \"StaticRadio\" type=\"radio\" name=\"orderType\" value = \"Static\" >  Static\n" +
        "        </div>\n" +
        "        <br>\n" +
        "\n" +
        "        <label for=\"StoresSelect\" class=\"StoresSelect\">Choose a store:</label>\n" +
        "        <select name=\"StoresSelect\" id=\"StoresSelect\" class = \"StoresSelect\">\n" +
        "\n" +
        "        </select>\n" +
        "        <br><br>\n" +
        "\n" +
        "        <button id = \"placeOrderGoToProducts\" type=\"button\">Next</button>\n" +
        "    </form>");
    customerLevelOrderJS = new CustomerLevelOrderJS();
    var dataString = "ZoneName="+zoneName;
    $.ajax({
        data: dataString,
        url: Update_Product_Details_Table_URL,
        success: function (products) {
            productsInArea = products;
        }
    });

}
function ajaxCustomerOrderDetailsTable() {
    $.ajax({
        url: Customer_Order_History_Table_URL,
        success: function (orders) {
            refreshCustomerOrderHistoryTable(orders);
        }
    });
}
function refreshCustomerOrderHistoryTable(orders) {
    $("#customerOrderHistoryDetailsTableBody").empty();
    $("#customerOrderHistoryModals").empty();
    var storesString;
    $.each(orders || [], function(index,order) {
        if(order.orders.length === 1){
            storesString = " store";
        }else{
            storesString = " stores";
        }
        var orderInTable = "<tr><td>" + order.OrderID + "</td><td>" + order.date.day+'/'+order.date.month+'/'+order.date.year + "</td>"+ "<td>"
            + order.orderLocation.x+","+order.orderLocation.y+ "</td>"+ "<td>" + order.orders.length +storesString+ "</td>"+ "<td>" +order.totalProductPurchased.toFixed(2)
            + "</td><td>" + order.totalProductPrice.toFixed(2) +'$'+"</td><td>"+order.deliveryPrice.toFixed(2)+'$'+"</td><td>"+(order.deliveryPrice+order.totalProductPrice).toFixed(2)+'$'+"</td><td>"+
            "\<input type='button' class='btn btn-primary btn-sm' data-toggle='modal' data-target = '#myModal' id='order.OrderID'  value = 'Show order'> </td>\"</tr>" ;

        $("#customerOrderHistoryDetailsTableBody").append(orderInTable);
        buildCustomerOrderModal(order);
        document.getElementById("order.OrderID").setAttribute("id",order.OrderID+"Modal");
        document.getElementById(order.OrderID+"Modal").setAttribute("data-target",'#'+"myModal"+order.OrderID+"order");
    });
}
function buildCustomerOrderModal(order) {
    var orderHeader = "Order ID: " +order.OrderID+ " - details";
    var modalID = "myModal"+order.OrderID+"order";
    var modalUniqID = order.OrderID + "ModalCustomerOrderHeader";

    $("#customerOrderHistoryModals").append("<div class=\"modal\" id=\"myModal\">\n" +
        "    <div class=\"modal-dialog mw-100 w-80\">\n" +
        "    <div class=\"modal-content\">\n" +
        "\n" +
        "    <!-- Modal Header -->\n" +
        "<div class=\"modal-header\">\n" +
        "    <h4 class=\"modal-title\">" +orderHeader+"</h4>\n" +
        "<button type=\"button\" id = \"closeCustomerOrderModalButton\" class=\"close\" data-dismiss=\"modal\">&times;</button>\n" +
        "</div>\n" +
        "\n" +
        "<!-- Modal body -->\n" +
        "<div class=\"modal-body\" id =\"modalUniqID\">" )

    document.getElementById("modalUniqID").setAttribute("id",modalUniqID);
    document.getElementById("myModal").setAttribute("id",modalID);
    buildCustomerOrderDetailsTable(order,modalUniqID);
    $("#" + modalUniqID).append("</div>\n" +
        "\n" +
        "    <!-- Modal footer -->\n" +
        "    <div class=\"modal-footer\">\n" +
        "        <button type=\"button\" id = \"closeCustomerOrderModalButton\" class=\"btn btn-danger\" data-dismiss=\"modal\">Close</button>\n" +
        "        </div>\n" +
        "\n" +
        "        </div>\n" +
        "        </div>");

}
function buildCustomerOrderDetailsTable(order,modalUniqID) {
    $('#' + modalUniqID).append("<div class = \"tbl-header\">\n" +
        "    <table class=\"StoreTable\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
        "    <thead>\n" +
        "    <tr><th>ID</th>\n" +
        "    <th>Name</th>\n" +
        "<th>Sold by</th>\n" +
        "<th>Store ID | Store Name</th>\n" +
        "<th>Total amount</th>\n" +
        "<th>Price per unit/kilo</th>\n" +
        "<th>Total price</th>\n" +
        "<th>Sale</th>\n" +
        "</tr>\n" +
        "</thead>\n" +
        "</table>\n" +
        "</div>");
    $('#' + modalUniqID).append("<div class=\"tbl-content\" id =\"tbl-store-content\">\n" +
        "    <table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" id = \"CustomerOrderDetailsTable\">\n" +
        "    <tbody id=\"CustomerOrderDetailsTableBody\">");
    var value = order.OrderID+"ModalCustomerOrderTable";
    document.getElementById("CustomerOrderDetailsTable").setAttribute("id",value);

    var tableIdString= "#" + order.OrderID+"ModalCustomerOrderTable";
    $.each(order.orders || [], function(index,storeOrder) {
        $.each(storeOrder.soldProducts || [],function (jndex,soldProduct) {
            var productInTable = "<tr><td>" + soldProduct.id + "</td><td>" + soldProduct.name + "</td>"+ "<td>"
                + soldProduct.category + "</td>"+"<td>" + soldProduct.storeID+'|'+storeOrder.storeName + "</td><td>" + soldProduct.amountSoldInOrder
                + "</td><td>"+soldProduct.price+"</td><td>"+soldProduct.price*soldProduct.amountSoldInOrder+"</td><td>Not part of a sale</td></tr>" ;
            $(tableIdString).append(productInTable);
        })
        $.each(storeOrder.productsSoldOnSale || [],function (jndex,saleProduct) {
            var productInTable = "<tr><td>" + saleProduct.id + "</td><td>" + saleProduct.name + "</td>"+ "<td>"
                + saleProduct.category + "</td>"+"<td>" + saleProduct.storeID+'|'+storeOrder.storeName + "</td><td>" + saleProduct.amountSoldInOrder
                + "</td><td>"+saleProduct.price+'$'+"</td><td>"+saleProduct.price*saleProduct.amountSoldInOrder+'$'+"</td><td>"+saleProduct.saleName+"</td></tr>" ;
            $(tableIdString).append(productInTable);
        })

    });
    $(modalUniqID).append("</tbody></table></div>")
}
function ajaxProductDetailsTable() {
    var dataString = "ZoneName="+zoneName;
    $.ajax({
        data: dataString,
        url: Update_Product_Details_Table_URL,
        success: function (products) {
            refreshProductsInArea(products);
        }
    });
}
function refreshProductsInArea(products) {

    $("#productDetailsTableBody").empty();
    var storesString;
    $.each(products || [], function(index,product) {
        if(product.amountOfStoresWhoSellProduct === 1){
             storesString = " store";
        }else{
            storesString = " stores";
        }
        var productInTable = "<tr><td>" + product.productID + "</td><td>" + product.productName + "</td>"+ "<td>"
            + product.category + "</td>"+ "<td>" + product.amountOfStoresWhoSellProduct +storesString+ "</td>"+ "<td>" + product.avgPrice.toFixed(2)
            +'$'+ "</td>"+ "<td>" + product.amountSoldInMarket.toFixed(2) +"</td></tr>" ;

        $("#productDetailsTableBody").append(productInTable);

    });
}


/*------Store details tab---------------------------*/
function ajaxStoreDetailsTable(){
    var dataString = "ZoneName="+zoneName;
    $.ajax({
        data: dataString,
        url: Update_Store_Details_Table_URL,
        success: function (stores) {
            refreshStoresInArea(stores);
        }
    });
}
function ajaxStoreNameComboBox(){
    var dataString = "ZoneName="+zoneName;
    $.ajax({
        data: dataString,
        url: Update_Store_Details_Table_URL,
        success: function (stores) {
            updateStoreComboBox(stores);
        }
    });
}
function updateStoreComboBox(stores) {
    $("#StoresSelect").empty();
    $.each(stores || [],function (index,store) {
        var storeName = "<option value='myValue' id = 'op'>"+store.name+"</option>";
        $("#StoresSelect").append(storeName);
        document.getElementById("op").setAttribute("id",store.name+"option")
        document.getElementById(store.name+"option").setAttribute("value",store.name)
    })
}
function refreshStoresInArea(stores) {
    $("#storeDetailsTableBody").empty();
    $("#storeProductsModals").empty();

    $.each(stores || [], function(index,store) {
        var totalRevenue = store.totalDeliveryPayment + store.totalProductsRevenue;
        var storeInTable = "<tr><td>" + store.ID + "</td>+ <td>" + store.name + "</td><td>" + store.ownerName + "</td><td>"
            + store.location.x+','+ store.location.x+ "</td><td>" +
            "<input type='button' class='btn btn-primary' data-toggle='modal' data-target = '#myModal' id='store.ID'  value = 'show products'> </td>"
            + "<td>" + store.amountOfOrdersFromStore + "</td>"+ "<td>" + store.totalProductsRevenue.toFixed(2) + "</td>"+
            "<td>" + store.deliveryPPK+"</td><td>" + store.totalDeliveryPayment.toFixed(2) + "</td> <td>"+ totalRevenue.toFixed(2) +"</td></tr>" ;

        buildStoreProductsModal(store);
        $("#storeDetailsTableBody").append(storeInTable);
        document.getElementById("store.ID").setAttribute("id",store.ID+"Modal");
        document.getElementById(store.ID+"Modal").setAttribute("data-target",'#'+"myModal"+store.ID);
    });

}
$(document).on('click', '.btn', function(){
    clearInterval(interval);
});
$(document).on('click', '#closeModalButton', function(){
    interval = setInterval(ajaxStoreDetailsTable,2000);
});
function buildStoreProductsTable(store,modalUniqID){
    $('#' + modalUniqID).append("<div class = \"tbl-header\">\n" +
        "    <table class=\"StoreTable\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
        "    <thead>\n" +
        "    <tr><th>ID</th>\n" +
        "    <th>Name</th>\n" +
        "<th>Sold by</th>\n" +
        "<th>Price per unit/kilo</th>\n" +
        "<th>Total amount sold</th>\n" +
        "</tr>\n" +
        "</thead>\n" +
        "</table>\n" +
        "</div>");
    $('#' + modalUniqID).append("<div class=\"tbl-content\" id =\"tbl-store-content\">\n" +
        "    <table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" id = \"storeProductsDetailsTable\">\n" +
        "    <tbody id=\"storeProductsDetailsTableBody\">");
    var value = store.ID+"ModalTable";
    document.getElementById("storeProductsDetailsTable").setAttribute("id",value);

    var tableIdString= "#" + store.ID+"ModalTable";
    $.each(store.products || [], function(index,product) {

        var productInTable = "<tr><td>" + product.id + "</td><td>" + product.name + "</td>"+ "<td>"
            + product.category + "</td>"+"<td>" + product.price.toFixed(2) + "</td><td>" + product.totalAmountSoldInStore + "</td></tr>" ;

        $(tableIdString).append(productInTable);
    });
    $(modalUniqID).append("</tbody></table></div>")

}

function buildStoreProductsModal(store){
    var storeHeader = store.name + "'s products";
    var modalID = "myModal"+store.ID;
    var modalUniqID = store.ID + "ModalHeader";

    $("#storeProductsModals").append("<div class=\"modal\" id=\"myModal\">\n" +
        "    <div class=\"modal-dialog mw-100 w-80\">\n" +
        "    <div class=\"modal-content\">\n" +
        "\n" +
        "    <!-- Modal Header -->\n" +
        "<div class=\"modal-header\">\n" +
        "    <h4 class=\"modal-title\">" +storeHeader+"</h4>\n" +
        "<button type=\"button\" id = \"closeModalButton\" class=\"close\" data-dismiss=\"modal\">&times;</button>\n" +
        "</div>\n" +
        "\n" +
        "<!-- Modal body -->\n" +
        "<div class=\"modal-body\" id =\"modalUniqID\">" )

    document.getElementById("modalUniqID").setAttribute("id",modalUniqID);
    document.getElementById("myModal").setAttribute("id",modalID);
    buildStoreProductsTable(store,modalUniqID);
    $("#" + modalUniqID).append("</div>\n" +
        "\n" +
        "    <!-- Modal footer -->\n" +
        "    <div class=\"modal-footer\">\n" +
        "        <button type=\"button\" id = \"closeModalButton\" class=\"btn btn-danger\" data-dismiss=\"modal\">Close</button>\n" +
        "        </div>\n" +
        "\n" +
        "        </div>\n" +
        "        </div>");

}
//-----------Highlighted Table---------
function chooseRows() {

    /* Get all rows from your 'table' but not the first one
     * that includes headers. */
    var rows = $(".saleRow");

    /* Create 'click' event handler for rows */
    rows.on('click', function(e) {

        /* Get current row */
        var row = $(this);

        rows.removeClass('highlight');
        row.addClass('highlight');
        var saleName = this.firstChild.id;
        chosenSale = this.firstChild.id;
        addSaleDetailsToComboBox(saleName);
    });

    /* This 'event' is used just to avoid that the table text
     * gets selected (just for styling).
     * For example, when pressing 'Shift' keyboard key and clicking
     * (without this 'event') the text of the 'table' will be selected.
     * You can remove it if you want, I just tested this in
     * Chrome v30.0.1599.69 */
    $(document).bind('selectstart dragstart', function(e) {
        e.preventDefault(); return false;
    });

}
function addSaleDetailsToComboBox(saleName) {
    var chosenSale;
    $(".SaleSelect").show();
    $.each(myAvailableSales.mySales || [],function (index, sale) {
        if(sale.name === saleName){
            chosenSale = sale;
        }
    })
    var offerNum = 0;
    $("#SaleSelect").empty();
    if(chosenSale.thenYouGet.operator ==="ONE-OF") {
        $.each(chosenSale.thenYouGet.offers || [], function (index, offer) {
            var saleOption = "<option class='offerOption' value='myValue' id = 'op'>" + offer.quantity + " " + offer.productName + " for additional " + offer.forAdditional + '$' + "</option>";
            $("#SaleSelect").append(saleOption);
            document.getElementById("op").setAttribute("id", "option" + offerNum);
            document.getElementById("option" + offerNum).setAttribute("value", offerNum.toString());
            document.getElementById("option" + offerNum).setAttribute("class", chosenSale.name);
            offerNum++;
        })
    }else{
        var saleOption = "";
        $.each(chosenSale.thenYouGet.offers || [], function (index, offer) {
            saleOption += offer.quantity + " " + offer.productName + " for additional " + offer.forAdditional + '$';
            saleOption +=" and ";
        })
        saleOption = saleOption .substring(0,saleOption.length-5);
        var option = "<option class='offerOption' value='myValue' id = 'op'>"+saleOption+"</option>";
        $("#SaleSelect").append(option);
        document.getElementById("op").setAttribute("id", "optionAllOrNothing");
        document.getElementById("optionAllOrNothing").setAttribute("class", chosenSale.name);
    }

}
$(function() {
    $("#BackToMainPage").click(function () {
        window.location.replace("../mainWindow/mainPage.html");
    })
})


