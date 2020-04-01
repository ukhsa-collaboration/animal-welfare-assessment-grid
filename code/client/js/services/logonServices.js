var logonServices = angular.module('logonServices', ['newDataServices']);

logonServices.factory('logonService', ['newDataService',
function(newDataService) {
	var getLogonDetails = function(callback) {
      var parameters = {};
      var url = window.awconfig.systemApi.getLogonDetails;
      newDataService.httpGet(url, parameters, callback);
	};

	return {
		getLogonDetails : getLogonDetails
	};
}]); 