var assessmentServices = angular.module('assessmentServices', ['appConfigModule','dataServices', 'pagingUtilsModule', 'studyServices', 'animalServices', 'reasonServices', 'userServices']);

assessmentServices.factory('assessmentService', ['appConfig','dataService', 'pagingUtils',
function(appConfig, dataService, pagingUtils) {
	var assessmentServlet = "assessment";
    var prevAssessmentServlet = "prev-assessment";
    var exportDownloadCookie = "assessmentsExportDownloadStatus";
    var exportDownloadCookieExpectedValue = "finished";
    var exportType = appConfig.services.exportTypes.assessments;

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

    var _getDynamicSearchParameters = function(studyId, studyGroupId, animalId, dateFrom, dateTo, userId, reasonId) {
        var params = {};
        params.studyId = studyId;
        params.studyGroupId = studyGroupId;
        params[appConfig.services.actionParams.animalId] = animalId;
        params[appConfig.services.actionParams.dateFrom] = dateFrom;
        params[appConfig.services.actionParams.dateTo] = dateTo;
        params.userId = userId;
        params.reasonId = reasonId;

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

    var getAssessmentsWithDynamicSearchCriteria = function(studyId, studyGroupId, animalId, dateFrom, dateTo, userId, reasonId, fnSuccessCallback, fnErrorCallback)
    {
        var parameters = _getDynamicSearchParameters(studyId, studyGroupId, animalId, dateFrom, dateTo, userId, reasonId);
        parameters[appConfig.services.selectAction] = appConfig.services.selectActions.assessmentDynamicSearch;
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

    var _getAssessmentsCountByCompleteness = function(isComplete, successCallback, errCallback)
    {
        var parameters = {};
        parameters[appConfig.services.selectAction] = appConfig.services.selectActions.assessmentsCountByCompleteness;
        parameters.isComplete = isComplete;

        dataService.dataConnection({
            servlet : assessmentServlet,
            parameters : parameters,
            callback : {
                success : successCallback,
                error : errCallback
            }
        });
    };

    var getAssessmentsCountComplete = function(successCallback, errCallback)
    {
        _getAssessmentsCountByCompleteness(true, successCallback, errCallback);
    };

    var getAssessmentsCountIncomplete = function(successCallback, errCallback)
    {
        _getAssessmentsCountByCompleteness(false, successCallback, errCallback);
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

    var deleteAssessment = function(assessment, successCallback, errCallback)
    {
        _deleteAssessment(assessment, successCallback, errCallback);
    };

    var _deleteAssessment = function(assessment, successCallback, errCallback)
    {
        dataService.deleteData({
            servlet : assessmentServlet,
            resourceId : assessment.id,
            callback : {
                success : successCallback,
                error : errCallback
            }
        });
    };

    var exportAssessments = function(assessmentIds, fnSuccessCallback)
    {
        var parameters = {};
        parameters[appConfig.services.actionParams.exportData] = {"ids" : assessmentIds};

        var downloadHandler = dataService.getDownloadHandler(fnSuccessCallback, exportDownloadCookie, exportDownloadCookieExpectedValue);
        downloadHandler(fnSuccessCallback, exportDownloadCookie, exportDownloadCookieExpectedValue);

        dataService.exportDataAsFile({parameters : parameters}, exportType);
    };

	return {
		submitAssessment : submitAssessment,
		saveAssessment : saveAssessment,
		getPreviousAssessment : getPreviousAssessment,
		getAssessments : getAssessments,
		getAnimalAssessmentsBetween : getAnimalAssessmentsBetween,
        getExistingAssessmentsByCriteria : getExistingAssessmentsByCriteria,
        getAssessmentsWithDynamicSearchCriteria : getAssessmentsWithDynamicSearchCriteria,
        getPreviousAssessmentByDate : getPreviousAssessmentByDate,
        getAssessmentById : getAssessmentById,
        getAssessmentsCount : getAssessmentsCount,
        getAssessmentCountByAnimalId : getAssessmentCountByAnimalId,
        getAssessmentCountByTemplateId : getAssessmentCountByTemplateId,
        getAssessmentsCountComplete : getAssessmentsCountComplete,
        getAssessmentsCountIncomplete : getAssessmentsCountIncomplete,
        deleteAssessment : deleteAssessment,
        exportAssessments : exportAssessments
	};
}]);

assessmentServices.service('assessmentDynamicSearchService', ['$filter', 'appConfig', 'dataService', 'assessmentService', 'studyService', 'animalService', 'reasonService', 'userService',
function($filter, appConfig, dataService, assessmentService, studyService, animalService, reasonService, userService){
    
    var studyEntity = 'studies';
    var studyGroupEntity = 'studyGroups';
    var animalEntity = 'animals';
    var reasonEntity = 'reasons';
    var userEntity = 'users';
    var startDate = 'dateOfFirstAssessment';
    var endDate = 'dateOfLastAssessment';

    var initialDataSourceMode = 'initialDataSourceMode';
    var filteredDataSourceMode = 'filteredDataSourceMode';

    var model = {
        assessmentsCounter : 0
    };

    var _filteredDataGetter = function(entityType, likeTerm)
    {
        var source = _availableSearchValues[entityType].values;
        var searchTerm = {"entityName" : likeTerm};
        return $filter('filter')(source, searchTerm);
    };

    var _filteredMetadataGetter = function()
    {
        return {};
    };

    var _filteredDataModeFunctions = {
        'studies' : function(callback, likeTerm) {
            callback(_filteredDataGetter('studies', likeTerm), _filteredMetadataGetter());
        },

        'studyGroups' : function(callback, likeTerm) {
            callback(_filteredDataGetter('studyGroups', likeTerm), _filteredMetadataGetter());
        },

        'animals' : function(callback, likeTerm) {
            callback(_filteredDataGetter('animals', likeTerm), _filteredMetadataGetter());
        },

        'reasons' : function(callback, likeTerm) {
            callback(_filteredDataGetter('reasons', likeTerm), _filteredMetadataGetter());
        },

        'users' : function(callback, likeTerm) {
            callback(_filteredDataGetter('users', likeTerm), _filteredMetadataGetter());
        }
    };

    var _initialModeFunctions = {
        'studies' : studyService.getStudiesLike,
        'studyGroups' : null,
        'animals' : animalService.getAnimalsLike,
        'reasons' : reasonService.getReasonsLike,
        'users' : userService.getUsersLike
    };

    var _dataSourceFunctions = _initialModeFunctions;

    var _chosenSearchValues = {};

    var _availableSearchValues = {};

    var _availableAssessments = [];

    var _searchSuccessObservers = {};

    var _searchErrorObservers = {};

    var _dataSourceSwitchObservers = {};

    var _saveSelectedValue = function(entityType, value)
    {
        _chosenSearchValues[entityType] = value;
    };

    var _clearValue = function(entityType)
    {
        delete _chosenSearchValues[entityType];

        if (entityType === 'studies')
        {
            delete _chosenSearchValues['studyGroups'];
        }
    };

    var _onGetAssessmentsCountCompleteSuccessCallback = function(data)
    {
        model.assessmentsCounter = data;
    };

    var _onGetAssessmentsCountCompleteErrorCallback = function(data)
    {
        // TODO report error back to user
        console.error(data.errors);
    };

    var _refreshDataSource = function(entityType)
    {
        var hasAnySearchValuesSet = Object.getOwnPropertyNames(_chosenSearchValues).length;

        if (hasAnySearchValuesSet)
        {
            _dataSourceFunctions = _filteredDataModeFunctions;
            _notifyDataSourceSwitchObservers(filteredDataSourceMode);
        }
        else
        {
            _dataSourceFunctions = _initialModeFunctions;
            if (entityType !== 'studyGroups')
            {
                assessmentService.getAssessmentsCountComplete(_onGetAssessmentsCountCompleteSuccessCallback, _onGetAssessmentsCountCompleteErrorCallback);
            }
            _notifyDataSourceSwitchObservers(initialDataSourceMode);
        }
    };

    var _onSearchDataSuccessCallback = function(data)
    {
        _availableSearchValues = data.filterValues;
        _availableSearchValues[startDate] = data['dateOfFirstAssessment'];
        _availableSearchValues[endDate] = data['dateOfLastAssessment'];

        _availableAssessments = data.assessments;

        model.assessmentsCounter = data.assessments.length;

        _notifySearchSuccessObservers();
    };

    var _onSearchDataErrorCallback = function(data)
    {
        _notifySearchErrorObservers(data);
    };

    var _notifySearchErrorObservers = function(data)
    {
        for (observerName in _searchErrorObservers)
        {
            var callback = _searchErrorObservers[observerName];
            callback(data);
        }
    };

    var _notifySearchSuccessObservers = function()
    {
        for (observerName in _searchSuccessObservers)
        {
            var callbackConfig = _searchSuccessObservers[observerName];
            var data = _availableSearchValues[callbackConfig.observedField];
            callbackConfig.callback(data);
        }
    };

    var _notifyDataSourceSwitchObservers = function(switchType)
    {
        for (observerName in _dataSourceSwitchObservers)
        {
            var callback = _dataSourceSwitchObservers[observerName];
            callback(switchType);
        }
    };    

    var _searchData = function()
    {
        var studyId, studyGroupId, animalId, dateFrom, dateTo, userId, reasonId;

        studyId = _getChosenValueSafe(studyEntity);
        studyGroupId = _getChosenValueSafe(studyGroupEntity);
        animalId = _getChosenValueSafe(animalEntity);
        reasonId = _getChosenValueSafe(reasonEntity);
        userId = _getChosenValueSafe(userEntity);
        dateFrom = _getChosenDateValueSafe(startDate);
        dateTo = _getChosenDateValueSafe(endDate);

        assessmentService.getAssessmentsWithDynamicSearchCriteria(studyId, studyGroupId, animalId, dateFrom, dateTo, userId, reasonId,
            _onSearchDataSuccessCallback, _onSearchDataErrorCallback);
    };

    var _getChosenDateValueSafe = function(dateType)
    {
        return _chosenSearchValues[dateType] ? _chosenSearchValues[dateType] : null;
    };

    var _getChosenValueSafe = function(entityType)
    {
        return _chosenSearchValues[entityType] ? _chosenSearchValues[entityType].id : null;
    };

    var _getEntity = function(dataSourceFunction, callback, likeTerm, pagingOptions)
    {
        var internalCallback = function(callbackData, callbackMetadata)
        {
            callback(callbackData, callbackMetadata);
        };

        dataSourceFunction(internalCallback, likeTerm, pagingOptions);
    };

    var _getSearchSuccessObserverKey = function(observerName, observedFieldName)
    {
        return observerName + "#" + observedFieldName;
    };

    var getStudies = function(callback, likeTerm, pagingOptions)
    {
        _getEntity(_dataSourceFunctions.studies, callback, likeTerm, pagingOptions);
    };

    var getStudyGroups = function(callback, likeTerm, pagingOptions)
    {
        _getEntity(_dataSourceFunctions.studyGroups, callback, likeTerm, pagingOptions);
    };

    var getAnimals = function(callback, likeTerm, pagingOptions)
    {
        _getEntity(_dataSourceFunctions.animals, callback, likeTerm, pagingOptions);
    };

    var getReasons = function(callback, likeTerm, pagingOptions)
    {
        _getEntity(_dataSourceFunctions.reasons, callback, likeTerm, pagingOptions);
    };

    var getUsers = function(callback, likeTerm, pagingOptions)
    {
        _getEntity(_dataSourceFunctions.users, callback, likeTerm, pagingOptions);
    };

    var getCurrentlyAvailableAssessments = function()
    {
        return _availableAssessments.slice();
    };

    var getCurrentSearchCriteria = function()
    {
        return Object.assign({}, _chosenSearchValues);
    };

    var onValueSelected = function(entityType, value)
    {
        _saveSelectedValue(entityType, value);

        // TODO disable inputs until requestData is done

        _refreshDataSource();
        _searchData();
    };

    var onValueCleared = function(entityType)
    {
        _clearValue(entityType);
        _refreshDataSource(entityType);
        _searchData();
    };

    var refreshCompleteAssessmentCount = function()
    {
        assessmentService.getAssessmentsCountComplete(_onGetAssessmentsCountCompleteSuccessCallback, _onGetAssessmentsCountCompleteErrorCallback);
    };

    var registerSearchSuccessObserver = function(observerName, callbackConfig)
    {
        var key = _getSearchSuccessObserverKey(observerName, callbackConfig.observedField);
        _searchSuccessObservers[key] = callbackConfig;
    };

    var unregisterSearchSuccessObserver = function(observerName, observedField)
    {
        var key = _getSearchSuccessObserverKey(observerName, observedField);
        delete _searchSuccessObservers[key];
    };

    var registerDataSourceSwitchObserver = function(observerName, callback)
    {
        _dataSourceSwitchObservers[observerName] = callback;
    };

    var unregisterDataSourceSwitchObserver = function(observerName)
    {
        delete _dataSourceSwitchObservers[observerName];
    };

    var registerSearchErrorObserver = function(observerName, callback)
    {
        _searchErrorObservers[observerName] = callback;
    };

    var unregisterSearchErrorObserver = function(observerName)
    {
        delete _searchErrorObservers[observerName];
    };

    return {
        getStudies : getStudies,
        getStudyGroups : getStudyGroups,
        getAnimals : getAnimals,
        getReasons : getReasons,
        getUsers : getUsers,
        onValueSelected : onValueSelected,
        onValueCleared : onValueCleared,
        studyEntity : studyEntity,
        studyGroupEntity : studyGroupEntity,
        animalEntity : animalEntity,
        reasonEntity : reasonEntity,
        userEntity : userEntity,
        startDate : startDate,
        endDate : endDate,
        initialDataSourceMode : initialDataSourceMode,
        filteredDataSourceMode : filteredDataSourceMode,
        refreshCompleteAssessmentCount : refreshCompleteAssessmentCount,
        registerSearchSuccessObserver : registerSearchSuccessObserver,
        unregisterSearchSuccessObserver : unregisterSearchSuccessObserver,
        registerDataSourceSwitchObserver : registerDataSourceSwitchObserver,
        unregisterDataSourceSwitchObserver : unregisterDataSourceSwitchObserver,
        registerSearchErrorObserver : registerSearchErrorObserver,
        unregisterSearchErrorObserver : unregisterSearchErrorObserver,
        getCurrentlyAvailableAssessments : getCurrentlyAvailableAssessments,
        getCurrentSearchCriteria : getCurrentSearchCriteria,
        model : model
    };

}]);
