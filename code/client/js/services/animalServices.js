var animalServices = angular.module('animalServices', ['dataServices', 'appConfigModule', 'pagingUtilsModule']);

animalServices.factory('animalService', ['dataService', 'appConfig', 'pagingUtils', 
function(dataService, appConfig, pagingUtils) {
	var animalServlet = "animal";

	var getAnimalsLike = function(callback, likeTerm, pagingOptions) {
		var parameters = {};
		parameters[appConfig.services.selectAction] = appConfig.services.selectActions.like;
		parameters[appConfig.services.actionParams.like] = likeTerm;
		pagingUtils.setPagingParameters(parameters, pagingOptions);
		dataService.dataConnection({
			servlet : animalServlet,
			parameters : parameters,
			callback : {
				success : callback
			}
		});
	};

	var getFullAnimalsLike = function(callback, likeTerm, pagingOptions) {
		var parameters = {};
		parameters[appConfig.services.selectAction] = appConfig.services.selectActions.like;
		parameters[appConfig.services.actionParams.like] = likeTerm;
		parameters[appConfig.services.actionParams.all] = appConfig.services.actionParamsValues.all;
		pagingUtils.setPagingParameters(parameters, pagingOptions);		
		dataService.dataConnection({
			servlet : animalServlet,
			parameters : parameters,
			callback : {
				success : callback
			}
		});
	};

	//TODO : Remove this after reworking assessment form screen to use new awsearchselect2
	var getAssessedAnimalsLike = function(callback, likeTerm) {
		getAnimalsLike(callback, likeTerm);
	};

	var _getSexFilteredAnimalsLike = function(callback, likeTerm, sex, pagingOptions) {
		var parameters = {};
		parameters[appConfig.services.selectAction] = appConfig.services.selectActions.like;
		parameters[appConfig.services.actionParams.like] = likeTerm;
		parameters[appConfig.services.actionParams.sex] = sex;
		pagingUtils.setPagingParameters(parameters, pagingOptions);	
		dataService.dataConnection({
			servlet : animalServlet,
			parameters : parameters,
			callback : {
				success : callback
			}
		});
	};

	var getFemaleAnimalsLike = function(callback, likeTerm) {
		_getSexFilteredAnimalsLike(callback, likeTerm, appConfig.services.actionParamsValues.sex.female);
	};

	var getMaleAnimalsLike = function(callback, likeTerm) {
		_getSexFilteredAnimalsLike(callback, likeTerm, appConfig.services.actionParamsValues.sex.male);
	};

	var getAnimals = function(callback, pagingOptions) {
		var parameters = {};
		parameters[appConfig.services.selectAction] = appConfig.services.selectActions.all;
		pagingUtils.setPagingParameters(parameters, pagingOptions);	
		dataService.dataConnection({
			servlet : animalServlet,
			parameters : parameters,
			callback : {
				success : callback
			}
		});
	};

	var getAnimalById = function(callback, animalId) {
		var parameters = {};
		parameters[appConfig.services.selectAction] = appConfig.services.selectActions.id;
		parameters[appConfig.services.actionParams.id] = animalId;
		dataService.dataConnection({
			servlet : animalServlet,
			parameters : parameters,
			callback : {
				success : callback
			}
		});

	};

	var _getStoreDataParameters = function(animal) {
		return {
			animal : animal
		};
	};

	var saveAnimal = function(animal, successCallback, errCallback) {
		var parameters = _getStoreDataParameters(animal);
		dataService.postData({
			servlet : animalServlet,
			parameters : parameters,
			callback : {
				success : successCallback,
				error : errCallback
			}
		});
	};

	var updateAnimal = function(animal, successCallback, errCallback) {
		dataService.putData({
			servlet : animalServlet,
			resourceId : animal.id,
			data : animal,
			callback : {
				success : successCallback,
				error : errCallback
			}
		});
	};

	var removeAnimal = function(animal, successCallback, errCallback) {
		dataService.deleteData({
			servlet : animalServlet,
			resourceId : animal.id,
			callback : {
				success : successCallback,
				error : errCallback
			}
		});
	};

	var getBasicAnimal = function(id, animalNumber){
		return {
			id : id,
			number : animalNumber
		};
	};

	return {
		getFullAnimalsLike : getFullAnimalsLike,
		getFemaleAnimalsLike : getFemaleAnimalsLike,
		getMaleAnimalsLike : getMaleAnimalsLike,
		getAnimalsLike : getAnimalsLike,
		getAnimals : getAnimals,
		getAnimalById : getAnimalById,
		getAssessedAnimalsLike : getAssessedAnimalsLike,
		saveAnimal : saveAnimal,
		updateAnimal : updateAnimal,
		removeAnimal : removeAnimal, 
		getBasicAnimal : getBasicAnimal
	};
}]);
