var x = JSON.parse(localStorage.getItem('ZoneDetails'));
var zoneName = x.zone;
var interval;
var Update_Product_Details_Table_URL = buildUrlWithContextPath("updateProductDetailsTable");
var Update_Store_Details_Table_URL = buildUrlWithContextPath("updateStoreDetailsTable");

$(function(){
    $("#UserChosenZoneName").empty().append(zoneName);
});

function openTab(evt, tabName) {
    clearInterval(interval);
    switch(tabName){
        case "ProductTabID":
            ajaxProductDetailsTable();
            interval = setInterval(ajaxProductDetailsTable,2000);
            break;
        case "StoreTabID":
            interval = setInterval(ajaxStoreDetailsTable,2000);
            break;
        case "PlaceOrderAndFeedBackID":
            break;
        case "CustomerOrderHistoryID":
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
        var productInTable = "<tr><td>" + product.ID + "</td><td>" + product.productName + "</td>"+ "<td>"
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

function refreshStoresInArea(stores) {
    $("#storeDetailsTableBody").empty();

    $.each(stores || [], function(index,store) {
        var totalRevenue = store.totalDeliveryPayment + store.totalProductsRevenue;
        var storeInTable = "<tr><td>" + store.ID + "</td>+ <td>" + store.name + "</td><td>" + store.ownerName + "</td><td>"
            + store.location + "</td><td>" +
            "<input class = 'showStoreProductsButton' name = 'showStoreProductsButton'  type = 'submit' id = store.ID value = 'show products'> </td>"
            + "<td>" + store.amountOfOrdersFromStore + "</td>"+ "<td>" + store.totalProductsRevenue + "</td>"+
            "<td>" + store.deliveryPPK+"</td><td>" + store.totalDeliveryPayment + "</td> <td> + totalRevenue + </td></tr>" ;


        $("#storeDetailsTableBody").append(storeInTable);
        document.getElementById("store.ID").setAttribute("id",store.ID);
    });
}
