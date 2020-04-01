var housingServices = angular.module('housingServices', ['appConfigModule','pagingUtilsModule', 'webApiConfigModule', 'newDataServices']);

housingServices.factory('housingService', ['appConfig','pagingUtils','newDataService', 'webApiConfig',
function(appConfig, pagingUtils, newDataService, webApiConfig) {
  
  var URLS = webApiConfig.webApiUrls.entity.housing;
  var HOUSING_NAME_KEY = 'housingName';
  var HOUSING_ID_KEY = 'housingId';

  var create = function(id, name) {
      return {
          housingId : id,
          housingName : name
      };
  };

  var getHousingsLike = function(callback, likeTerm, pagingOptions) {
    var parameters = {};
    
    parameters[webApiConfig.webApiParameters.likeFilter] = likeTerm;
    pagingUtils.newApiSetPagingParameters(parameters, pagingOptions);

    var onSuccess = function(data) {
      var metadata = pagingUtils.buildPagingMetadata(
        data[webApiConfig.webApiResponseKeys.pagingInfo], HOUSING_NAME_KEY, HOUSING_ID_KEY);
      callback(data.housings, metadata);
    };

    newDataService.httpGet(URLS.getLike, parameters, onSuccess);
  };

  var saveHousing = function(housing, successCallback, errCallback) {

    var onSuccess = function(data) {
      var response = {};
      response[HOUSING_NAME_KEY] = data.value;
      response[HOUSING_ID_KEY] = data.id;
      successCallback(response);
    }

    newDataService.httpPost(URLS.create, housing, onSuccess, errCallback);
  };

  var updateHousing = function(housing, successCallback, errCallback) {
    var url = URLS.update + housing.housingId;
    newDataService.httpPut(url, housing, successCallback, errCallback);
  };

  var uploadHousing = function(data, successCallback, errCallback) {
    var url = URLS.upload;
    newDataService.httpPostWithHeaderContentType(url, data, successCallback, errCallback);
  };    


  return {
    getHousingsLike : getHousingsLike,
    saveHousing : saveHousing,
    updateHousing : updateHousing,
    create : create,
    uploadHousing : uploadHousing
  };
}]); 