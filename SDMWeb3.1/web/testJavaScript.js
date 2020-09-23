function refreshUsersList(users) {
    //clear all current users
    $("#listOfMsg").empty();

    // rebuild the list of users: scan all users and add them to the list of users
    $.each(users || [], function(index, msg) {
        console.log("Adding user #" + index + ": " + msg);

        $('<li>' + msg + '</li>').appendTo($("#listOfMsg"));
    });
}


$(function() { // onload...do
    //add a function to the submit event
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

        $("#userstring").val("");
        // by default - we'll always return false so it doesn't redirect the user.
        return false;
    });
});