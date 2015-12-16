var sourceServices = angular.module('sourceServices', ['dataServices', 'appConfigModule', 'pagingUtilsModule']);

speciesServices.factory('sourceService', ['dataService', 'appConfig', 'pagingUtils',
function(dataService, appConfig, pagingUtils) {
	var sourceServlet = "source";

	var getSourcesLike = function(callback, likeTerm, pagingOptions) {
		var parameters = {};
		parameters[appConfig.services.selectAction] = appConfig.services.selectActions.like;
		parameters[appConfig.services.actionParams.like] = likeTerm;
		pagingUtils.setPagingParameters(parameters, pagingOptions);
		dataService.dataConnection({
			servlet : sourceServlet,
			parameters : parameters,
			callback : {
				success : callback
			}
		});
	};

	var getSource = function(callback, pagingOptions) {
		var parameters = {};
		parameters[appConfig.services.selectAction] = appConfig.services.selectActions.all;
		pagingUtils.setPagingParameters(parameters, pagingOptions);	
		dataService.dataConnection({
			servlet : sourceServlet,
			parameters : parameters,
			callback : {
				success : callback
			}
		});
	};

	var _getStoreDataParameters = function(source) {
		return {
			source : source
		};
	};

	var saveSource = function(source, successCallback, errCallback) {
		var parameters = _getStoreDataParameters(source);
		dataService.postData({
			servlet : sourceServlet,
			parameters : parameters,
			callback : {
				success : successCallback,
				error : errCallback
			}
		});
	};

	var removeSource = function(species, successCallback, errCallback) {
		dataService.deleteData({
			servlet : sourceServlet,
			resourceId : source.sourceId,
			callback : {
				success : successCallback,
				error : errCallback
			}
		});
	};

	var updateSource = function(source, successCallback, errCallback) {
		dataService.putData({
			servlet : sourceServlet,
			resourceId : source.sourceId,
			data : source,
			callback : {
				success : successCallback,
				error : errCallback
			}
		});
	};

	return {
		getSourcesLike : getSourcesLike,
		getSource : getSource,
		saveSource : saveSource,
		updateSource : updateSource,
		removeSource : removeSource
	};
}]);
