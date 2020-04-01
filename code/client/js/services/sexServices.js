var sexServices = angular.module('sexServices', ['webApiConfigModule', 'newDataServices']);

animalServices.factory('sexService', ['webApiConfig', 'newDataService',
function(webApiConfig, newDataService)
{
  var URLS = webApiConfig.webApiUrls.entity.sex;

	var getSex = function (callback)
	{
    var url = URLS.getAll;
    var parameters = {};
    var successCallback = function(data) {
      callback(data.sexes);
    };
    newDataService.httpGet(url, parameters, successCallback);
	};
	
	return{
		getSex: getSex
	};
}]); 