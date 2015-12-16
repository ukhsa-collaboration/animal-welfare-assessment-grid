var logonServices = angular.module('logonServices', ['dataServices']);

logonServices.factory('logonService', ['dataService',
function(dataService) {
	var getLogonDetails = function(callback) {
		dataService.dataConnection({
			servlet : "logondetails",
			callback : {
				success : callback
			}
		});
	};

	return {
		getLogonDetails : getLogonDetails
	};
}]); 