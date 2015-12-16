$.jsonp({
    url : window.awconfig.serverUrl + "auth-check?callback=?",

    success : function(data)
    {
        //alert(data.status);
    },

    error : function(xOptions, textStatus)
    {
        //alert("You need to log in!");
        window.location.assign(window.awconfig.serverUrl + "index.html");
    }
});
