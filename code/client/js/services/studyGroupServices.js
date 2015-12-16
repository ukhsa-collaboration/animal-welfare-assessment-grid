var studyGroupServices = angular.module('studyGroupServices', ['dataServices', 'appConfigModule', 'pagingUtilsModule']);

studyGroupServices.factory('studyGroupService', ['dataService', 'appConfig', 'pagingUtils',
function(dataService, appConfig, pagingUtils) {
	var studyGroupServlet = "study-group";

	var saveStudyGroup = function(studyGroup, successCallback, errCallback) {
		var parameters = _getStoreDataParameters(studyGroup);
		dataService.postData({
			servlet : studyGroupServlet,
			parameters : parameters,
			callback : {
				success : successCallback,
				error : errCallback
			}
		});
	};

	var updateStudyGroup = function(studyGroup, successCallback, errCallback) {
		dataService.putData({
			servlet : studyGroupServlet,
			resourceId : studyGroup.studyGroupId,
			data : studyGroup,
			callback : {
				success : successCallback,
				error : errCallback
			}
		});
	};

	var getStudyGroup = function(studyGroupId, successCallback, errCallback) {
		var parameters = {};
		parameters[appConfig.services.selectAction] = appConfig.services.selectActions.id;
		parameters[appConfig.services.actionParams.id] = studyGroupId;
		dataService.dataConnection({
			servlet : studyGroupServlet,
			parameters : parameters,
			callback : {
				success : successCallback,
				error : errCallback
			}
		});
	};

	var getStudyGroupsLike = function(callback, likeTerm, pagingOptions) {
		var parameters = {};
		parameters[appConfig.services.selectAction] = appConfig.services.selectActions.like;
		parameters[appConfig.services.actionParams.like] = likeTerm;
		pagingUtils.setPagingParameters(parameters, pagingOptions);	
		dataService.dataConnection({
			servlet : studyGroupServlet,
			parameters : parameters,
			callback : {
				success : callback
			}
		});
	};

	var getFullStudyGroupsLike = function(callback, likeTerm, pagingOptions) {
		var parameters = {};
		parameters[appConfig.services.selectAction] = appConfig.services.selectActions.like;
		parameters[appConfig.services.actionParams.like] = likeTerm;
		parameters[appConfig.services.actionParams.all] = appConfig.services.actionParamsValues.all;
		pagingUtils.setPagingParameters(parameters, pagingOptions);	
		dataService.dataConnection({
			servlet : studyGroupServlet,
			parameters : parameters,
			callback : {
				success : callback
			}
		});
	};

	var _getStoreDataParameters = function(studyGroup) {
		return {
			"study-group" : studyGroup
		};
	};

	var getBasicStudyGroup = function(id, name){
		return {
			studyGroupId : id,
			studyGroupName : name
		};
	};

	return {
		getStudyGroup : getStudyGroup,
		getStudyGroupsLike : getStudyGroupsLike,
		updateStudyGroup : updateStudyGroup,
		saveStudyGroup : saveStudyGroup,
		getFullStudyGroupsLike : getFullStudyGroupsLike,
		getBasicStudyGroup : getBasicStudyGroup
	};
}]); 