

function refreshUsersList(msg) {
    $('<li>' + msg + '</li>').appendTo($("#listOfMsg"));
}
function mouseOver(){
    document.getElementById("clickMe").style.color = "red";
}
$(function() { // onload...do
    //add a function to the submit event

   document.getElementById("clickMe").onmouseover = function ()
   {mouseOver();};





    $("#role").submit(function() {
        $.ajax({
            data: $(this).serialize(),
            url: this.action,
            //timeout: 2000,
            error: function() {
                console.error("Failed to submit");
            },
            success: function(r) {
                refreshUsersList(r);
            }
        });
        return false;
    });
});