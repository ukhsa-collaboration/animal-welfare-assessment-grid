function logout()
{
    var redirectUrl = window.awconfig.serverUrl + "index.html";
    $.ajax({
        url: window.awconfig.systemApi.logout,
        method: 'GET',
        success: function(data) {
            window.location.assign(redirectUrl);
        },
        dataType: 'json',
        cache: false
        })
      .fail(function() {
        window.location.assign(redirectUrl);
      });
}