var studyServices = angular.module('studyServices', ['appConfigModule', 'pagingUtilsModule', 'webApiConfigModule', 'newDataServices']);

studyServices.factory('studyService', ['appConfig', 'pagingUtils', 'webApiConfig', 'newDataService',
function(appConfig, pagingUtils, webApiConfig, newDataService)
{
    var URLS = webApiConfig.webApiUrls.entity.study;
    var STUDY_NAME_KEY = 'studyName';
    var STUDY_ID_KEY = 'studyId';    

    var getStudyWithAnimal = function(callback, animalId)
    {
      var parameters = {};
      var url = URLS.getWithAnimal + animalId;
      newDataService.httpGet(url, parameters, callback);
    };

	  var getStudiesLike = function(callback, likeTerm, pagingOptions) {
      var parameters = {};

      parameters[webApiConfig.webApiParameters.likeFilter] = likeTerm;
      pagingUtils.newApiSetPagingParameters(parameters, pagingOptions);

      var onSuccess = function(data) {
        var metadata = pagingUtils.buildPagingMetadata(
          data[webApiConfig.webApiResponseKeys.pagingInfo], STUDY_NAME_KEY, STUDY_ID_KEY);
        callback(data.studies, metadata);
      };

      newDataService.httpGet(URLS.getLike, parameters, onSuccess);
  	};

  	var getStudy = function(studyId, successCallback, errCallback) {
        var parameters = {};
        var url = URLS.getById + studyId;
        newDataService.httpGet(url, parameters, successCallback, errCallback);
  	};

  	var saveStudy = function(study, successCallback, errCallback) {
        var onSuccess = function(data) {
          var response = {};
          response[STUDY_NAME_KEY] = data.value;
          response[STUDY_ID_KEY] = data.id;
          successCallback(response);
        }

        newDataService.httpPost(URLS.create, study, onSuccess, errCallback);
  	};

  	var updateStudy = function(study, successCallback, errCallback) {
        var url = URLS.update + study.studyId;
        newDataService.httpPut(url, study, successCallback, errCallback);
  	};

    var uploadStudy = function(data, successCallback, errCallback) {
      var url = URLS.upload;
      newDataService.httpPostWithHeaderContentType(url, data, successCallback, errCallback);
    };    


  	return {
  		updateStudy : updateStudy,
  		getStudy : getStudy,
  		saveStudy : saveStudy,
  		getStudiesLike : getStudiesLike,
  		getStudyWithAnimal : getStudyWithAnimal,
      uploadStudy : uploadStudy
  	};
}]);
