$.ajax({
    url: window.awconfig.systemApi.authenticationCheck,
    method: 'GET',
    success: function(data) {
        //alert(data.status);
    },
    dataType: 'json',
    cache: false
    })
  .fail(function() {
    //alert("You need to log in!");
    window.location.assign(window.awconfig.serverUrl + "index.html");
  });