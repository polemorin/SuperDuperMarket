
$(function () {
    $("#loginForm").submit(function() {
        $.ajax({
            data: $(this).serialize(),
            url: this.action,
            error: function() {
                console.error("Failed to submit");

            },
            success: function(r) {
                console.log("Success" + r);
                $("#errorMsg").empty().append(r);
            }
        });
        return false;
    });

});


