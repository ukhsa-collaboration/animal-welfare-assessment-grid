var userServices = angular.module('userServices', ['pagingUtilsModule', 'webApiConfigModule', 'newDataServices']);

userServices.factory('userService', ['pagingUtils', 'webApiConfig', 'newDataService',
function(pagingUtils, webApiConfig, newDataService) {

    var URLS = webApiConfig.webApiUrls.entity.user;
    var USER_NAME_KEY = 'userName';
    var USER_ID_KEY = 'userId';

    var create = function(id, name) {
        return {
            userId : id,
            userName : name
        };
    };

    var getUsersLike = function(callback, likeTerm, pagingOptions) {
      var parameters = {};

      parameters[webApiConfig.webApiParameters.likeFilter] = likeTerm;
      pagingUtils.newApiSetPagingParameters(parameters, pagingOptions);

      var onSuccess = function(data) {
        var metadata = pagingUtils.buildPagingMetadata(
          data[webApiConfig.webApiResponseKeys.pagingInfo], USER_NAME_KEY, USER_ID_KEY);
        callback(data.users, metadata);
      };

      newDataService.httpGet(URLS.getLike, parameters, onSuccess);
    };

    var saveUser = function(user, successCallback, errCallback) {
      var onSuccess = function(data) {
        var response = {};
        response[USER_NAME_KEY] = data.value;
        response[USER_ID_KEY] = data.id;
        successCallback(response);
      }

      newDataService.httpPost(URLS.create, user, onSuccess, errCallback);
    };

    return {
        getUsersLike : getUsersLike,
        saveUser : saveUser,
        create : create
    };
}]);
