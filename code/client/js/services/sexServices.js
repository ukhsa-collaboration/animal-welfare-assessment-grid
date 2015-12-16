var sexServices = angular.module('sexServices', ['dataServices']);

animalServices.factory('sexService', ['dataService',
function(dataService)
{
	var getSex = function (callback)
	{
		dataService.dataConnection({
    		servlet: "sex",
    		callback : {
				success : callback
			}
    	});
	};
	
	return{
		getSex: getSex
	};
}]); 