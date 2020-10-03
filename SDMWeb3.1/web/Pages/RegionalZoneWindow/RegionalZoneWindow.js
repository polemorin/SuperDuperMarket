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
            ajaxStoreDetailsTable();
           /* interval = setInterval(ajaxStoreDetailsTable,2000);*/
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
    });

}
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
    var modalUniqID = store.ID + "ModalHeader";

    $("#storeProductsModals").append("<div class=\"modal\" id=\"myModal\">\n" +
        "    <div class=\"modal-dialog\">\n" +
        "    <div class=\"modal-content\">\n" +
        "\n" +
        "    <!-- Modal Header -->\n" +
        "<div class=\"modal-header\">\n" +
        "    <h4 class=\"modal-title\">" +storeHeader+"</h4>\n" +
        "<button type=\"button\" class=\"close\" data-dismiss=\"modal\">&times;</button>\n" +
        "</div>\n" +
        "\n" +
        "<!-- Modal body -->\n" +
        "<div class=\"modal-body\" id =\"modalUniqID\">" )

    document.getElementById("modalUniqID").setAttribute("id",modalUniqID);
    buildStoreProductsTable(store,modalUniqID);
    $("#" + modalUniqID).append("</div>\n" +
        "\n" +
        "    <!-- Modal footer -->\n" +
        "    <div class=\"modal-footer\">\n" +
        "        <button type=\"button\" class=\"btn btn-danger\" data-dismiss=\"modal\">Close</button>\n" +
        "        </div>\n" +
        "\n" +
        "        </div>\n" +
        "        </div>");

}
/*
<!-- The Modal -->
<div class="modal" id="myModal">
    <div class="modal-dialog">
    <div class="modal-content">

    <!-- Modal Header -->
<div class="modal-header">
    <h4 class="modal-title">Modal Heading</h4>
<button type="button" class="close" data-dismiss="modal">&times;</button>
</div>

<!-- Modal body -->
<div class="modal-body">

</div>

<!-- Modal footer -->
<div class="modal-footer">
    <button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
    </div>

    </div>
    </div>
    </div>*/