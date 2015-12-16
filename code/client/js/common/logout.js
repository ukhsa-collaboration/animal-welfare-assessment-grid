function logout()
{
    $.jsonp({
        url : window.awconfig.serverUrl + "logout?callback=?", // ?callback=?

        success : function(data)
        {
            window.location.assign(window.awconfig.serverUrl + "index.html");
        },

        error : function(xOptions, textStatus)
        {
            window.location.assign(window.awconfig.serverUrl + "index.html");
        }
    });
}