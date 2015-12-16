var scaleServices = angular.module('scaleServices', ['dataServices', 'appConfigModule', 'pagingUtilsModule']);

scaleServices.factory('scaleService', ['dataService', 'appConfig', 'pagingUtils',
function(dataService, appConfig, pagingUtils) {
	var scaleServlet = "scale";

	var getScale = function(callback, id){
		var parameters = {};
		parameters[appConfig.services.selectAction] = appConfig.services.selectActions.id;
		parameters[appConfig.services.actionParams.id] = id;
		dataService.dataConnection({
			servlet : scaleServlet,
			parameters : parameters,
			callback : {
				success : callback
			}
		});
	};

	var getScales = function(callback, pagingOptions) {
		var parameters = {};
		parameters[appConfig.services.actionParams.all] = appConfig.services.actionParamsValues.all;
		pagingUtils.setPagingParameters(parameters, pagingOptions);
		dataService.dataConnection({
			servlet : scaleServlet,
			parameters : parameters,
			callback : {
				success : callback
			}
		});
	};

	var getScalesLike = function(callback, likeTerm, pagingOptions){
		var parameters = {};
		parameters[appConfig.services.selectAction] = appConfig.services.selectActions.like;
		parameters[appConfig.services.actionParams.like] = likeTerm;
		pagingUtils.setPagingParameters(parameters, pagingOptions);
		dataService.dataConnection({
			servlet : scaleServlet,
			parameters : parameters,
			callback : {
				success : callback
			}
		});
	};

	var saveScale = function(scale, successCallback, errCallback){
		var parameters = _getStoreDataParameters(scale);
		dataService.postData({
			servlet : scaleServlet,
			parameters : parameters,
			callback : {
				success : successCallback,
				error : errCallback
			}
		});
	};

	var updateScale = function(scale, successCallback, errCallback){
		dataService.putData({
			servlet : scaleServlet,
			resourceId : scale.scaleId,
			data : scale,
			callback : {
				success : successCallback,
				error : errCallback
			}
		});
	};

	var _getStoreDataParameters = function(scale) {
		return {
			scale : scale
		};
	};

	return {
		getScale : getScale,
		getScales : getScales,
		getScalesLike : getScalesLike,
		saveScale : saveScale,
		updateScale : updateScale
	};
}]);