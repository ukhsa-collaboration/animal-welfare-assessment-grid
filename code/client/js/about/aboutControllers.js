var aboutControllers = angular.module('aboutControllers', []);

aboutControllers.controller('AboutCtrl', ['$scope', '$http',
function($scope, $http)
{
    this.version = "UNKNOWN";
    var that = this;
    var url = window.awconfig.resourcesUrl + 'version.json';

    $http.get(url).
      success(function(data) {
        that.version = data.v;
      }).
      error(function() {
        that.version = "Problem retrieving version number";
      });
}]);
