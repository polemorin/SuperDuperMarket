
//this function is for the tables design
$(window).on("load resize ", function() {
    var scrollWidth = $('.tbl-content').width() - $('.tbl-content table').width();
    $('.tbl-header').css({'padding-right':scrollWidth});
}).resize();


$(function() { // onload...do
    //add a function to the submit event
    $("#amount").keyup(function myFunc() {
            var x = document.getElementById("amount");

            if(parseInt(x.value) <= 0){
                $("#amountMsgLabel").empty().append("enter a positive number.");
            }
            else{
                $("#amountMsgLabel").empty();
            }
        }
    )
});



