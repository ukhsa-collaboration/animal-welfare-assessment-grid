var studyGroupServices = angular.module('studyGroupServices', ['appConfigModule', 'pagingUtilsModule', 'webApiConfigModule', 'newDataServices']);

studyGroupServices.factory('studyGroupService', ['appConfig', 'pagingUtils', 'newDataService', 'webApiConfig',
  function(appConfig, pagingUtils, newDataService, webApiConfig) {

    var URLS = webApiConfig.webApiUrls.entity.studyGroup;
    var STUDY_GROUP_NAME_KEY = 'studyGroupName';
    var STUDY_GROUP_ID_KEY = 'studyGroupId';

    var saveStudyGroup = function(studyGroup, successCallback, errCallback) {
      var onSuccess = function(data) {
        var response = {};
        response[STUDY_GROUP_NAME_KEY] = data.value;
        response[STUDY_GROUP_ID_KEY] = data.id;
        successCallback(response);
      }

      newDataService.httpPost(URLS.create, studyGroup, onSuccess, errCallback);
    };

    var updateStudyGroup = function(studyGroup, successCallback, errCallback) {
      var url = URLS.update + studyGroup.studyGroupId;
      newDataService.httpPut(url, studyGroup, successCallback, errCallback);
    };

    var uploadStudyGroup = function(data, successCallback, errCallback) {
      var url = URLS.upload;
      newDataService.httpPostWithHeaderContentType(url, data, successCallback, errCallback);
    };    

    var getStudyGroup = function(studyGroupId, successCallback, errCallback) {
      var parameters = {};
      var url = URLS.getById + studyGroupId;
      newDataService.httpGet(url, parameters, successCallback);
    };

    var getStudyGroupsLike = function(callback, likeTerm, pagingOptions) {
      var parameters = {};

      parameters[webApiConfig.webApiParameters.likeFilter] = likeTerm;
      pagingUtils.newApiSetPagingParameters(parameters, pagingOptions);

      var onSuccess = function(data) {
        var metadata = pagingUtils.buildPagingMetadata(
          data[webApiConfig.webApiResponseKeys.pagingInfo], STUDY_GROUP_NAME_KEY, STUDY_GROUP_ID_KEY);
        callback(data.studyGroups, metadata);
      };

      newDataService.httpGet(URLS.getLike, parameters, onSuccess);
    };

    var getBasicStudyGroup = function(id, name) {
      return {
        studyGroupId: id,
        studyGroupName: name
      };
    };

    return {
      getStudyGroup: getStudyGroup,
      getStudyGroupsLike: getStudyGroupsLike,
      updateStudyGroup: updateStudyGroup,
      saveStudyGroup: saveStudyGroup,
      getBasicStudyGroup: getBasicStudyGroup,
      uploadStudyGroup: uploadStudyGroup
    };
  }
]);