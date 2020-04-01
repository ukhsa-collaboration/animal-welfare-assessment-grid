var userAuthServices = angular.module('userAuthServices', ['pagingUtilsModule', 'webApiConfigModule', 'newDataServices']);

userAuthServices.factory('userAuthService', ['pagingUtils', 'webApiConfig', 'newDataService',
function(pagingUtils, webApiConfig, newDataService) {

    var URLS = webApiConfig.webApiUrls.entity.userAuth;
    var USER_NAME_KEY = 'userName';
    var USER_ID_KEY = 'userName';

    var getAuthUsersLike = function(callback, likeTerm, pagingOptions) {
      var parameters = {};

      parameters[webApiConfig.webApiParameters.likeFilter] = likeTerm;
      pagingUtils.newApiSetPagingParameters(parameters, pagingOptions);

      var onSuccess = function(data) {
        var metadata = pagingUtils.buildPagingMetadata(
          data[webApiConfig.webApiResponseKeys.pagingInfo], USER_NAME_KEY, USER_ID_KEY);
        callback(data.userAuths, metadata);
      };

      newDataService.httpGet(URLS.getLike, parameters, onSuccess);        
    };

   var saveAuthUser = function(user, successCallback, errCallback){
      var onSuccess = function(data) {
        var response = {};
        response[USER_NAME_KEY] = data.value;
        response[USER_ID_KEY] = data.id;
        successCallback(response);
      }

      newDataService.httpPost(URLS.create, user, onSuccess, errCallback);
    };

    var updateAuthUser = function(user, successCallback, errCallback) {
      var url = URLS.update + user.userName;
      newDataService.httpPut(url, user, successCallback, errCallback);        
    };

    var getAuthUserById = function(id, successCallback, errCallback)
    {
      var parameters = {};
      var url = URLS.getById + id;
      newDataService.httpGet(url, parameters, successCallback, errCallback);        
    };

    var deleteAuthUser = function(user, successCallback, errCallback)
    {
      var url = URLS.delete + user.userName;
      newDataService.httpDelete(url, successCallback, errCallback);        
    };

    return {
        getAuthUsersLike : getAuthUsersLike,
        saveAuthUser : saveAuthUser,
        updateAuthUser : updateAuthUser,
        getAuthUserById : getAuthUserById,
        deleteAuthUser : deleteAuthUser
    };
}]);