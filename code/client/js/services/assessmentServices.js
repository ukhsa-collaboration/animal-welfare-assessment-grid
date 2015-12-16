var assessmentServices = angular.module('assessmentServices', ['appConfigModule','dataServices', 'pagingUtilsModule']);

assessmentServices.factory('assessmentService', ['appConfig','dataService', 'pagingUtils',
function(appConfig, dataService, pagingUtils) {
	var assessmentServlet = "assessment";
    var prevAssessmentServlet = "prev-assessment";

	var _getStoreDataParameters = function(assessment, isSubmit) {
		return {
			assessment : assessment,
			isSubmit : isSubmit
		};
	};

    var _getSearchExistingParameters = function(animalId, dateFrom, dateTo, userId, reasonId, studyId, isComplete) {
        var params = {};
        params[appConfig.services.actionParams.animalId] = animalId;
        params[appConfig.services.actionParams.dateFrom] = dateFrom;
        params[appConfig.services.actionParams.dateTo] = dateTo;
        params.userId = userId;
        params.reasonId = reasonId;
        params.studyId = studyId;
        params.isComplete = isComplete;

        return params;
    };

    var submitAssessment = function(assessment, isExisting, successCallback, errCallback)
    {
        var isSubmit = true;
        _handleSaveSubmitAssessment(isSubmit, assessment, isExisting, successCallback, errCallback);
	};

    var saveAssessment = function(assessment, isExisting, successCallback, errCallback)
    {
        var isSubmit = false;
        _handleSaveSubmitAssessment(isSubmit, assessment, isExisting, successCallback, errCallback);
	};

    var _handleSaveSubmitAssessment = function(isSubmit, assessment, isExisting, successCallback, errCallback)
    {
        if (isExisting)
        {
            _updateAssessment(assessment, isSubmit, successCallback, errCallback);
        }
        else
        {
            _createAssessment(assessment, isSubmit, successCallback, errCallback);
        }
    };

    var _createAssessment = function(assessment, isSubmit, successCallback, errCallback)
    {
        var parameters = _getStoreDataParameters(assessment, isSubmit);

        dataService.postData({
            servlet : assessmentServlet,
            parameters : parameters,
            callback : {
                success : successCallback,
                error : errCallback
            }
        });
    };

    var _updateAssessment = function(assessment, isSubmit, successCallback, errCallback)
    {
        dataService.putData({
            servlet : assessmentServlet,
            resourceId : assessment.id,
            data : assessment,
            params : {
                isSubmit : isSubmit,
            },
            callback : {
                success : successCallback,
                error : errCallback
            }
        });
    };

	var getPreviousAssessment = function(animalId, callback) {
		dataService.dataConnection({
			servlet : prevAssessmentServlet,
			parameters : {
				animalId : animalId
			},
			callback : {
				success : callback
			}
		});
	};

    var getPreviousAssessmentByDate = function(animalId, currentAssessmentDate, currentAssessmentId, fnSuccessCallback, fnErrorCallback) {
        dataService.dataConnection({
            servlet : prevAssessmentServlet,
            parameters : {
                animalId : animalId,
                date : currentAssessmentDate,
                id : currentAssessmentId
            },
            callback : {
                success : fnSuccessCallback,
                error : fnErrorCallback
            }
        });
    };

	var getAssessments = function(callback, pagingOptions) {
		var parameters = {};
		parameters[appConfig.services.selectAction] = appConfig.services.actionParams.all;
		pagingUtils.setPagingParameters(parameters, pagingOptions);
        dataService.dataConnection({
            servlet : assessmentServlet,
            parameters : parameters,
           	callback : {
				success : callback
			}
        });
	};

	var getAnimalAssessmentsBetween = function(animalId, dateFrom, dateTo, fnSuccessCallback, fnErrorCallback, pagingOptions, isPageable){
		var parameters = {};
		parameters[appConfig.services.selectAction] = appConfig.services.selectActions.animalAssessLine;
		parameters[appConfig.services.actionParams.animalId] = animalId;
		parameters[appConfig.services.actionParams.dateFrom] = dateFrom;
		parameters[appConfig.services.actionParams.dateTo] = dateTo;
		pagingUtils.setPagingParameters(parameters, pagingOptions, isPageable);
		dataService.dataConnection({
			servlet : assessmentServlet,
			parameters : parameters,
			callback : {
				success : fnSuccessCallback,
				error : fnErrorCallback
			}
		});
	};

    var getExistingAssessmentsByCriteria = function(animalId, dateFrom, dateTo, userId, reasonId, studyId, isComplete, fnSuccessCallback, fnErrorCallback, pagingOptions)
    {
        var parameters = _getSearchExistingParameters(animalId, dateFrom, dateTo, userId, reasonId, studyId, isComplete);
        parameters[appConfig.services.selectAction] = appConfig.services.selectActions.assessmentsSearch;
        pagingUtils.setPagingParameters(parameters, pagingOptions);

        dataService.dataConnection({
            servlet : assessmentServlet,
            parameters : parameters,
            callback : {
                success : fnSuccessCallback,
                error : fnErrorCallback
            }
        });
    };

    var getAssessmentById = function(assessmentId, successCallback, errCallback)
    {
        var parameters = {};
        parameters[appConfig.services.selectAction] = appConfig.services.selectActions.id;
        parameters[appConfig.services.actionParams.id] = assessmentId;
        dataService.dataConnection({
            servlet : assessmentServlet,
            parameters : parameters,
            callback : {
                success : successCallback,
                error : errCallback
            }
        });
    };

    var getAssessmentsCount = function(successCallback, errCallback)
    {
        var parameters = {};
        parameters[appConfig.services.selectAction] = appConfig.services.actionParams.count;
        dataService.dataConnection({
            servlet : assessmentServlet,
            parameters : parameters,
            callback : {
                success : successCallback,
                error : errCallback
            }
        });
    };

    var getAssessmentCountByAnimalId = function(animalId, fnSuccessCallback, fnErrorCallback)
    {
        var parameters = {};
        parameters[appConfig.services.selectAction] = appConfig.services.selectActions.countByAnimalId;   
        parameters[appConfig.services.actionParams.animalId] = animalId;
        dataService.dataConnection({
            servlet : assessmentServlet,
            parameters : parameters,
            callback : {
                success : fnSuccessCallback,
                error : fnErrorCallback
            }
        });
    };

    var getAssessmentCountByTemplateId = function(templateId, fnSuccessCallback, fnErrorCallback)
    {
        var parameters = {};
        parameters[appConfig.services.selectAction] = appConfig.services.selectActions.countByTemplateId;   
        parameters[appConfig.services.actionParams.templateId] = templateId;
        dataService.dataConnection({
            servlet : assessmentServlet,
            parameters : parameters,
            callback : {
                success : fnSuccessCallback,
                error : fnErrorCallback
            }
        });
    };

	return {
		submitAssessment : submitAssessment,
		saveAssessment : saveAssessment,
		getPreviousAssessment : getPreviousAssessment,
		getAssessments : getAssessments,
		getAnimalAssessmentsBetween : getAnimalAssessmentsBetween,
        getExistingAssessmentsByCriteria : getExistingAssessmentsByCriteria,
        getPreviousAssessmentByDate : getPreviousAssessmentByDate,
        getAssessmentById : getAssessmentById,
        getAssessmentsCount : getAssessmentsCount,
        getAssessmentCountByAnimalId : getAssessmentCountByAnimalId,
        getAssessmentCountByTemplateId : getAssessmentCountByTemplateId
	};
}]);
