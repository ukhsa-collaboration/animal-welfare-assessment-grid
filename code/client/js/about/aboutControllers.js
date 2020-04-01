var aboutControllers = angular.module('aboutControllers', ['newDataServices']);

aboutControllers.controller('AboutCtrl', ['$scope', 'newDataService',
function($scope, newDataService)
{
    this.version = "UNKNOWN";
    var that = this;
    var url = window.awconfig.resourcesUrl + 'version.json';
    var parameters = {};

    var successCallback = function(data) {
      that.version = data.v;
    };

    var errCallback = function() {
      that.version = "Problem retrieving version number";
    };

    newDataService.httpGet(url, parameters, successCallback, errCallback);
}]);
