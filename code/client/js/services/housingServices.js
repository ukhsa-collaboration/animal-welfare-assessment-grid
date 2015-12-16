var housingServices = angular.module('housingServices', ['dataServices','appConfigModule','pagingUtilsModule']);

housingServices.factory('housingService', ['dataService','appConfig','pagingUtils',
function(dataService, appConfig, pagingUtils) {
	
	var housingServlet = "housing";

    var create = function(id, name) {
        return {
            housingId : id,
            housingName : name
        };
    };

	var getHousing = function(callback, id){
		var parameters = {};
		parameters[appConfig.services.selectAction] = appConfig.services.selectActions.id;
		parameters[appConfig.services.actionParams.id] = id;
		dataService.dataConnection({
			servlet : housingServlet,
			parameters : parameters,
			callback : {
				success : callback
			}
		});
	};

	var getHousings = function(callback, pagingOptions) {
		var parameters = {};
		parameters[appConfig.services.actionParams.all] = appConfig.services.actionParamsValues.all;
		pagingUtils.setPagingParameters(parameters, pagingOptions);
		dataService.dataConnection({
			servlet : housingServlet,
			parameters : parameters,
			callback : {
				success : callback
			}
		});
	};

	var getHousingsLike = function(callback,likeTerm, pagingOptions){
		var parameters = {};
		parameters[appConfig.services.selectAction] = appConfig.services.selectActions.like;
		parameters[appConfig.services.actionParams.like] = likeTerm;
		pagingUtils.setPagingParameters(parameters, pagingOptions);
		dataService.dataConnection({
			servlet : housingServlet,
			parameters : parameters,
			callback : {
				success : callback
			}
		});
	};

	var saveHousing = function(housing, successCallback, errCallback){
		var parameters = _getStoreDataParameters(housing);
		dataService.postData({
			servlet : housingServlet,
			parameters : parameters,
			callback : {
				success : successCallback,
				error : errCallback
			}
		});
	};

	var updateHousing = function(housing, successCallback, errCallback){
		dataService.putData({
			servlet : housingServlet,
			resourceId : housing.housingId,
			data : housing,
			callback : {
				success : successCallback,
				error : errCallback
			}
		});
	};

	var _getStoreDataParameters = function(housing) {
		return {
			housing : housing
		};
	};

	return {
		getHousing : getHousing,
		getHousings : getHousings,
		getHousingsLike : getHousingsLike,
		saveHousing : saveHousing,
		updateHousing : updateHousing,
		create : create
	};
}]); 