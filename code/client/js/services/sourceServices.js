var sourceServices = angular.module('sourceServices', ['appConfigModule', 'pagingUtilsModule', 'webApiConfigModule', 'newDataServices']);

speciesServices.factory('sourceService', ['appConfig', 'pagingUtils', 'newDataService', 'webApiConfig',
  function(appConfig, pagingUtils, newDataService, webApiConfig) {

    var URLS = webApiConfig.webApiUrls.entity.source;
    var SOURCE_NAME_KEY = 'sourceName';
    var SOURCE_ID_KEY = 'sourceId';

    var getSourcesLike = function(callback, likeTerm, pagingOptions) {
      var parameters = {};

      parameters[webApiConfig.webApiParameters.likeFilter] = likeTerm;
      pagingUtils.newApiSetPagingParameters(parameters, pagingOptions);

      var onSuccess = function(data) {
        var metadata = pagingUtils.buildPagingMetadata(
          data[webApiConfig.webApiResponseKeys.pagingInfo], SOURCE_NAME_KEY, SOURCE_ID_KEY);
        callback(data.sources, metadata);
      };

      newDataService.httpGet(URLS.getLike, parameters, onSuccess);
    };

    var saveSource = function(source, successCallback, errCallback) {
      var onSuccess = function(data) {
        var response = {};
        response[SOURCE_NAME_KEY] = data.value;
        response[SOURCE_ID_KEY] = data.id;
        successCallback(response);
      }

      newDataService.httpPost(URLS.create, source, onSuccess, errCallback);
    };

    var updateSource = function(source, successCallback, errCallback) {
      var url = URLS.update + source.sourceId;
      newDataService.httpPut(url, source, successCallback, errCallback);
    };

    var uploadSource = function(data, successCallback, errCallback) {
      var url = URLS.upload;
      newDataService.httpPostWithHeaderContentType(url, data, successCallback, errCallback);
    };    

    return {
      getSourcesLike: getSourcesLike,
      saveSource: saveSource,
      updateSource: updateSource,
      uploadSource: uploadSource
    };
  }
]);