var USER_LIST_URL = buildUrlWithContextPath("updateUserTable");
var Area_List_URL = buildUrlWithContextPath("updateZoneTable");
var Balance_URL = buildUrlWithContextPath("updateBalance");
var User_Role_URL = buildUrlWithContextPath("userRole");
var Upload_XML_FILE_UTL = buildUrlWithContextPath("loadFile");
var Transactions_TABLE_URL = buildUrlWithContextPath("updateTransactions");
var GO_TO_ZONE_URL = buildUrlWithContextPath("goToZone");
var ADD_FUNDS_URL = buildUrlWithContextPath("addFunds");
var GET_ALERTS_URL = buildUrlWithContextPath("getAlerts");
var CHAT_LIST_URL = buildUrlWithContextPath("chat");

var chatVersion = 0;

//this function is for the tables design
$(window).on("load resize ", function() {
    var scrollWidth = $('.tbl-content').width() - $('.tbl-content table').width();
    $('.tbl-header').css({'padding-right':scrollWidth});
}).resize();


$(function() { // onload...do
   //add a function to the submit event
    $.ajax({
        url: User_Role_URL,
        success: function (role) {
            hideHTMLElementsByRole(role);
            if(role !=="Customer"){
                setInterval(getAlerts,3000);
            }
        }
    });
    ajaxBalance();
    ajaxUsersList();
    ajaxAreaTable();
   $("#fundsAmount").keyup(function myFunc() {
           var x = document.getElementById("fundsAmount");

           if(parseInt(x.value) <= 0){
               $("#amountMsgLabel").empty().append("enter a positive number.");
           }
           else{
               $("#amountMsgLabel").empty();
           }
       }
   )
    $("#contact-submit").click(function () {
        if(document.getElementById("fundsAmount").value !== "" && document.getElementById("fundsAmount").value > 0 ){
            $.ajax({
                data:{fundsAmount:document.getElementById("fundsAmount").value},
                url: ADD_FUNDS_URL,
                success: function (result) {
                    console.log("success");
                }
            });
        }
    })
   //-------------upload xml file-----------------------------------------------
    $("#uploadXmlFile").submit(function() {
        var file1 = this[0].files[0];
        var formData = new FormData();

        formData.append("file", file1);
        //formData.append("mapName", this[1].value);
        $.ajax({
            method:'POST',
            data: formData,
            url: Upload_XML_FILE_UTL,
            processData: false, // Don't process the files
            contentType: false, // Set content type to false as jQuery will tell the server its a query string request
            timeout: 4000,
            error: function(e) {
                console.error("Failed to submit");
                $("#result").text("Failed to get result from server " + e);
            },
            success: function(r) {
                $("#result").text(r);
            }
        });
        // return value of the submit operation
        // by default - we'll always return false so it doesn't redirect the user.
        return false;
    })

});
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
function hideHTMLElementsByRole(role){
    if(role === "StoreOwner"){
        $("#addFundsForm").hide();
        document.getElementById("centeredRight").style.top = "40%";
    }else{
        $("#uploadXmlFile").hide();
    }
}

$(function() {

    //The users list is refreshed automatically every second
    setInterval(ajaxUsersList, 2000);
    setInterval(ajaxAreaTable,2000);
    setInterval(ajaxBalance,2000);
    setInterval(ajaxTransactionTable,2000);

});
function ajaxTransactionTable() {
    $.ajax({
        url: Transactions_TABLE_URL,
        success: function (transactions) {
            refreshTransactions(transactions);
        }
    });
}
function refreshTransactions(transactions) {

    $("#TransactionTableBody").empty();


    $.each(transactions || [], function(index,transaction) {
        var tDate = Date.parse(transaction.date);
        var d = new Date(transaction.date);
        var n = d.toDateString();

        var transactionInTable = "<tr><td>" + transaction.action + "</td><td>" + n + "</td>"+ "<td>"
            + transaction.amount.toFixed(2) +'$'+ "</td>"+ "<td>" + transaction.balanceBeforeAction.toFixed(2) +'$'+ "</td>"+ "<td>" + transaction.balanceAfterAction.toFixed(2)
            +'$'+ "</td></tr>" ;
        $("#TransactionTableBody").append(transactionInTable);
    });
}
//userTable
function ajaxUsersList() {
    $.ajax({
        url: USER_LIST_URL,
        success: function (users) {
            refreshUsersList(users);
        }
    });
}
//{username,user}
function refreshUsersList(users) {
    //clear all current users
    $("#UsersTableBody").empty();

    // rebuild the list of users: scan all users and add them to the list of users
    $.each(users || [], function(userName, role) {
        //create a new <option> tag with a value in it and
        //appeand it to the #userslist (div with id=userslist) element
        var userInTable = "<tr><td>" + userName + "</td><td>" + role + "</td></tr>";
        $("#UsersTableBody").append(userInTable);
    });
}

//AreaTable
function ajaxAreaTable() {
    $.ajax({
        url: Area_List_URL,
        success: function (areas) {
            refreshAreas(areas);
        }
    });
}
//{username,user}
function refreshAreas(areas) {
    //clear all current users
    $("#zoneTableBody").empty();

    // rebuild the list of users: scan all users and add them to the list of users
    $.each(areas || [], function(index,area) {
        //create a new <option> tag with a value in it and
        //appeand it to the #userslist (div with id=userslist) element
        var zoneInTable = "<tr><td>" + area.creatorName + "</td><td>" + area.zoneName + "</td>"+ "<td>"
            + area.productAmount + "</td>"+ "<td>" + area.storeAmount + "</td>"+ "<td>" + area.orderAmount
            + "</td>"+ "<td>" + area.orderAVGPrice.toFixed(2) +"</td><td> <input class = 'goToZoneButton' name = 'goToZoneButton'  type = 'submit' id = area.zoneName value = 'goToZone'> </td></tr>" ;

        $("#zoneTableBody").append(zoneInTable);
        document.getElementById("area.zoneName").setAttribute("id",area.zoneName);
        goToZoneArea();
    });
}
function ajaxBalance() {
        $.ajax({
            url: Balance_URL,
            success: function (balance) {
                $("#creditBalance").empty().append(parseFloat(balance).toFixed(2) + "$");
            }
        });
}
function goToZoneArea(){
    $(".goToZoneButton").click(function () {
        var dataString = "id="+this.id;
    $.ajax({
        data: dataString,
        dataType: "json",
        url: GO_TO_ZONE_URL,
        error: function (e) {
            console.error(e);
            return false;
        },
        success: function (r) {
            console.log(r);
            localStorage.setItem('ZoneDetails',JSON.stringify(r));
            window.location.replace("../RegionalZoneWindow/RegionalZoneWindow.html");
            return true;
        }
    });
})}



