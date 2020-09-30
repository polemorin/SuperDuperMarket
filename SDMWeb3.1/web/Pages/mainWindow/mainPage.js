var USER_LIST_URL = buildUrlWithContextPath("updateUserTable");
var Area_List_URL = buildUrlWithContextPath("updateZoneTable");
var Balance_URL = buildUrlWithContextPath("updateBalance");
//this function is for the tables design
$(window).on("load resize ", function() {
    var scrollWidth = $('.tbl-content').width() - $('.tbl-content table').width();
    $('.tbl-header').css({'padding-right':scrollWidth});
}).resize();


$(function() { // onload...do
   //add a function to the submit event
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
   //-------------upload xml file-----------------------------------------------
   $("#uploadXmlFile").submit(function() {

       var file1 = this[0].files[0];

       var formData = new FormData();
       formData.append("file", file1);
       //formData.append("mapName", this[1].value);

       $.ajax({
           method:'POST',
           data: formData,
           url: this.action,
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



$(function() {

    //The users list is refreshed automatically every second
    setInterval(ajaxUsersList, 2000);
    setInterval(ajaxAreaTable,2000);
    setInterval(ajaxBalance,2000);

});
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
            + "</td>"+ "<td>" + area.orderAVGPrice +"</td><td> <input type = 'submit' value = 'goToZone'> </td></tr>" ;
        $("#zoneTableBody").append(zoneInTable);
    });
}
function ajaxBalance() {
    $.ajax({
        url: Balance_URL,
        success: function (balance) {
            $("#creditBalance").empty().append(balance + "$");
        }
    });
}
$(function()
{
    $("#addFundsForm").submit(function () {

        var formData = new FormData();
        formData.append("fundsAmount", fundsAmount);
        formData.append("addFundsDate", addFundsDate);
        $.ajax({
            method: 'POST',
            data: formData,
            url: this.action,
            processData: false,
            contentType: false,
            error: function (e) {
                console.log(e);
            },
            success: function (r) {
                console.log(r);
                return false;
            }
        });

        // return value of the submit operation
        // by default - we'll always return false so it doesn't redirect the user.
        return false;
    });
});




