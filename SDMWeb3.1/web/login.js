

$("#loginForm").submit(function() {
    $.ajax({
        data: $(this).serialize(),
        url: this.action,
        error: function() {
            console.error("Failed to submit");
        },
        success: function(r) {
           // $(r).appendTo($("#errorMsg"));
            $("#errorMsg").append(r);
        }
    });
    return false;
});
