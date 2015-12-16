var speciesServices = angular.module('speciesServices', ['dataServices', 'appConfigModule', 'pagingUtilsModule']);

speciesServices.factory('speciesService', ['dataService', 'appConfig', 'pagingUtils',
function(dataService, appConfig, pagingUtils) {
	var speciesServlet = "species";

	var getSpeciesLike = function(callback, likeTerm, pagingOptions) {
		var parameters = {};
		parameters[appConfig.services.selectAction] = appConfig.services.selectActions.like;
		parameters[appConfig.services.actionParams.like] = likeTerm;
		pagingUtils.setPagingParameters(parameters, pagingOptions);	
		dataService.dataConnection({
			servlet : speciesServlet,
			parameters : parameters,
			callback : {
				success : callback
			}
		});
	};

	var getSpecies = function(callback, pagingOptions) {
		var parameters = {};
		parameters[appConfig.services.selectAction] = appConfig.services.selectActions.all;
		pagingUtils.setPagingParameters(parameters, pagingOptions);	
		dataService.dataConnection({
			servlet : speciesServlet,
			parameters : parameters,
			callback : {
				success : callback
			}
		});
	};

	var _getStoreDataParameters = function(species) {
		return {
			species : species
		};
	};

	var saveSpecies = function(species, successCallback, errCallback) {
		var parameters = _getStoreDataParameters(species);
		dataService.postData({
			servlet : speciesServlet,
			parameters : parameters,
			callback : {
				success : successCallback,
				error : errCallback
			}
		});
	};

	var updateSpecies = function(species, successCallback, errCallback) {
		dataService.putData({
			servlet : speciesServlet,
			resourceId : species.speciesId,
			data : species,
			callback : {
				success : successCallback,
				error : errCallback
			}
		});
	};

	var removeSpecies = function(species, successCallback, errCallback) {
		dataService.deleteData({
			servlet : speciesServlet,
			resourceId : species.speciesId,
			callback : {
				success : successCallback,
				error : errCallback
			}
		});

	};

	return {
		getSpeciesLike : getSpeciesLike,
		getSpecies : getSpecies,
		saveSpecies : saveSpecies,
		updateSpecies : updateSpecies,
		removeSpecies : removeSpecies
	};
}]);
