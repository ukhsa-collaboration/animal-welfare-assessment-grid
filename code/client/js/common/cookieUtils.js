var cookieUtilsModule = angular.module('cookieUtilsModule', []);

cookieUtilsModule.factory('cookieUtils', [
function()
{   
    var getCookie = function(cname) {
        var name = cname + "=";
        var ca = document.cookie.split(';');
        for(var i=0, limit = ca.length; i<limit; i++) {
            var c = ca[i];
            while (c.charAt(0)==' ') c = c.substring(1);
            if (c.indexOf(name) == 0) return c.substring(name.length,c.length);
        }
        return "";
    };
    
    return {
        getCookie : getCookie
    };
}]);
