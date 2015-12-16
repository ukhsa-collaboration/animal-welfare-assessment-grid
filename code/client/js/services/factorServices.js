var factorServices = angular.module('factorServices', ['dataServices','appConfigModule','pagingUtilsModule']);

factorServices.factory('factorService', ['dataService','appConfig','pagingUtils',
function(dataService, appConfig, pagingUtils) {
	
	var factorServlet = "factor";

    var create = function(id, name) {
        return {
            factorId : id,
            factorName : name
        };
    };

	var getFactor = function(callback, id){
		var parameters = {};
		parameters[appConfig.services.selectAction] = appConfig.services.selectActions.id;
		parameters[appConfig.services.actionParams.id] = id;
		dataService.dataConnection({
			servlet : factorServlet,
			parameters : parameters,
			callback : {
				success : callback
			}
		});
	};

	var getFactors = function(callback, pagingOptions) {
		var parameters = {};
		parameters[appConfig.services.actionParams.all] = appConfig.services.actionParamsValues.all;
		pagingUtils.setPagingParameters(parameters, pagingOptions);
		dataService.dataConnection({
			servlet : factorServlet,
			parameters : parameters,
			callback : {
				success : callback
			}
		});
	};

	var getFactorsLike = function(callback, likeTerm, pagingOptions){
		var parameters = {};
		parameters[appConfig.services.selectAction] = appConfig.services.selectActions.like;
		parameters[appConfig.services.actionParams.like] = likeTerm;
		pagingUtils.setPagingParameters(parameters, pagingOptions);
		dataService.dataConnection({
			servlet : factorServlet,
			parameters : parameters,
			callback : {
				success : callback
			}
		});
	};

	var saveFactor = function(factor, successCallback, errCallback){
		var parameters = _getStoreDataParameters(factor);
		dataService.postData({
			servlet : factorServlet,
			parameters : parameters,
			callback : {
				success : successCallback,
				error : errCallback
			}
		});
	};

	var updateFactor = function(factor, successCallback, errCallback){
		dataService.putData({
			servlet : factorServlet,
			resourceId : factor.factorId,
			data : factor,
			callback : {
				success : successCallback,
				error : errCallback
			}
		});
	};

	var _getStoreDataParameters = function(factor) {
		return {
			factor : factor
		};
	};

	return {
		getFactor : getFactor,
		getFactors : getFactors,
		getFactorsLike : getFactorsLike,
		saveFactor : saveFactor,
		updateFactor : updateFactor,
		create : create
	};
}]); 