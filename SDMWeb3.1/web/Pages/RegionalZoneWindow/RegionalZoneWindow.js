var x = JSON.parse(localStorage.getItem('ZoneDetails'));
var zoneName = x.zone;
var interval;
var Update_Product_Details_Table_URL = buildUrlWithContextPath("updateProductDetailsTable");
var Update_Store_Details_Table_URL = buildUrlWithContextPath("updateStoreDetailsTable");
var Customer_Order_History_Table_URL = buildUrlWithContextPath("updateCustomerOrderHistoryTable");
var GET_PRODUCTS_FROM_STORE_URL = buildUrlWithContextPath("getStoreProducts");
var Get_Sales_URL = buildUrlWithContextPath("getSales");
var CREATE_BASIC_ORDER_URL = buildUrlWithContextPath("createBasicOrder");
let customerLevelOrderJS;
let myAvailableSales;
let mySales;

//JAVA SCRIPT OBJECT
function SaleArray(){
    var mySales = [];
}
function Sale() {
    this.name;
    this.ifYouBuy;
    this.thenYouGet;
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
    }
function StoreLevelOrderJS(){
    this.regProducts = [];
    this.saleProducts = [];
    this.storeID;
    this.deliveryPrice
    this.customerLocation;
    this.storeName;
    this.orderID;

}
function RegProduct(){
    this.productID;
    this.totalProductPrice;
    this.Amount;
}
function saleProduct(){
    this.productID;
    this.totalProductPrice;
    this.Amount;
    this.saleName;
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
            break;
        case "FeedBacksID":
            break;
        case "AddStoreID":
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

$(function(){// area name init
    $("#UserChosenZoneName").empty().append(zoneName);
    customerLevelOrderJS = new CustomerLevelOrderJS();
    myAvailableSales = new SaleArray();
    mySales = new SaleArray();
});
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
    buildSalesPages();
    if(customerLevelOrderJS.orderType === "Static"){

    }else{

    }
}

function getMySales() {
    $.ajax({
        data:{StoreOrders: JSON.stringify(customerLevelOrderJS.storeOrders), ZoneName: zoneName, OrderType: customerLevelOrderJS.orderType, Date: customerLevelOrderJS.date, storeName: customerLevelOrderJS.storeOrders[0].storeName},
        url: Get_Sales_URL,
        success: function (sales) {
             myAvailableSales = sales;
        }
    });
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
            console.log(customerLevelOrderJS);
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
function buildSalesPages() {
    $("#PlaceOrderAndFeedBackID").empty().append("<h3>Place order</h3>\n" +
        "    <form id=\"AddSalesToOrderForm\">\n" +
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
        "        <br><br>\n" +
        "        <h5>Order type</h5>\n" +
        "        <br>\n" +
        "        <div class=\"radio\">\n" +
        "            <input id = \"DynamicRadio\" type=\"radio\" name=\"orderType\" value = \"Dynamic\" checked = \"checked\">  Dynamic&nbsp&nbsp\n" +
        "            <input id = \"StaticRadio\" type=\"radio\" name=\"orderType\" value = \"Static\" >  Static\n" +
        "        </div>\n" +
        "        <br><br>\n" +
        "\n" +
        "        <label for=\"StoresSelect\" class=\"StoresSelect\">Choose a store:</label>\n" +
        "        <select name=\"StoresSelect\" id=\"StoresSelect\" class = \"StoresSelect\">\n" +
        "\n" +
        "        </select>\n" +
        "        <br><br>\n" +
        "\n" +
        "        <button id = \"placeOrderGoToProducts\" type=\"button\">Next</button>\n" +
        "    </form>")

}

function buildPlaceOrderLandingPage() {
    $("#PlaceOrderAndFeedBackID").empty().append("<h3>Place order</h3>\n" +
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
        "        <br><br>\n" +
        "        <h5>Order type</h5>\n" +
        "        <br>\n" +
        "        <div class=\"radio\">\n" +
        "            <input id = \"DynamicRadio\" type=\"radio\" name=\"orderType\" value = \"Dynamic\" checked = \"checked\">  Dynamic&nbsp&nbsp\n" +
        "            <input id = \"StaticRadio\" type=\"radio\" name=\"orderType\" value = \"Static\" >  Static\n" +
        "        </div>\n" +
        "        <br><br>\n" +
        "\n" +
        "        <label for=\"StoresSelect\" class=\"StoresSelect\">Choose a store:</label>\n" +
        "        <select name=\"StoresSelect\" id=\"StoresSelect\" class = \"StoresSelect\">\n" +
        "\n" +
        "        </select>\n" +
        "        <br><br>\n" +
        "\n" +
        "        <button id = \"placeOrderGoToProducts\" type=\"button\">Next</button>\n" +
        "    </form>")

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
            +'$'+ "</td><td>" + order.totalProductPrice.toFixed(2) +'$'+"</td><td>"+order.deliveryPrice.toFixed(2)+'$'+"</td><td>"+order.deliveryPrice+order.totalProductPrice.toFixed(2)+'$'+"</td><td>"+
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
                + "</td><td>"+saleProduct.price+"</td><td>"+saleProduct.price*saleProduct.amountSoldInOrder+"</td><td>saleProduct.saleName</td></tr>" ;
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
            + "<td>" + store.amountOfOrdersFromStore + "</td>"+ "<td>" + store.totalProductsRevenue + "</td>"+
            "<td>" + store.deliveryPPK+"</td><td>" + store.totalDeliveryPayment + "</td> <td>"+ totalRevenue +"</td></tr>" ;

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
            + product.category + "</td>"+"<td>" + product.price + "</td><td>" + product.totalAmountSoldInStore + "</td></tr>" ;

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



