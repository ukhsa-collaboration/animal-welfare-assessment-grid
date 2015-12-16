var studyServices = angular.module('studyServices', ['dataServices', 'appConfigModule', 'pagingUtilsModule']);

studyServices.factory('studyService', ['dataService', 'appConfig', 'pagingUtils',
function(dataService, appConfig, pagingUtils)
{
    var studyServlet = "study";

    var getStudyWithAnimal = function(callback, animalId)
    {
        var parameters = {};
        parameters[appConfig.services.selectAction] = appConfig.services.selectActions.studyWithAnimal;
        parameters[appConfig.services.actionParams.id] = animalId;
        dataService.dataConnection({
            servlet : studyServlet,
            parameters : parameters,
            callback : {
				success : callback
			}
        });
    };

	var getStudiesLike = function(callback, likeTerm, pagingOptions) {
		var parameters = {};
		parameters[appConfig.services.selectAction] = appConfig.services.selectActions.like;
		parameters[appConfig.services.actionParams.like] = likeTerm;
		pagingUtils.setPagingParameters(parameters, pagingOptions);
		dataService.dataConnection({
			servlet : studyServlet,
			parameters : parameters,
			callback : {
				success : callback
			}
		});
	};

	var getStudy = function(studyId, successCallback, errCallback) {
		var parameters = {};
		parameters[appConfig.services.selectAction] = appConfig.services.selectActions.id;
		parameters[appConfig.services.actionParams.id] = studyId;
		dataService.dataConnection({
			servlet : studyServlet,
			parameters : parameters,
			callback : {
				success : successCallback,
				error : errCallback
			}
		});
	};

	var saveStudy = function(study, successCallback, errCallback) {
		var parameters = _getStoreDataParameters(study);
		dataService.postData({
			servlet : studyServlet,
			parameters : parameters,
			callback : {
				success : successCallback,
				error : errCallback
			}
		});
	};

	var updateStudy = function(study, successCallback, errCallback) {
		dataService.putData({
			servlet : studyServlet,
			resourceId : study.studyId,
			data : study,
			callback : {
				success : successCallback,
				error : errCallback
			}
		});
	};

	var _getStoreDataParameters = function(study) {
		return {
			study : study
		};
	};

	return {
		updateStudy : updateStudy,
		getStudy : getStudy,
		saveStudy : saveStudy,
		getStudiesLike : getStudiesLike,
		getStudyWithAnimal : getStudyWithAnimal
	};
}]);
