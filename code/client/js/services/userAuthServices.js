var userAuthServices = angular.module('userAuthServices', ['dataServices', 'entityServices']);

userAuthServices.factory('userAuthService', ['dataService', 'entityService',
function(dataService, entityService) {

    var userAuthServlet = "user-auth";

    var getAuthUsersLike = function(callback, likeTerm, pagingOptions) {
        entityService.getEntityLike(userAuthServlet, callback, likeTerm, pagingOptions);
    };

   var saveAuthUser = function(user, successCallback, errorCallback){
        var parameters = _getStoreDataParameters(user);
        entityService.saveEntity(userAuthServlet, parameters, successCallback, errorCallback);
    };

    var updateAuthUser = function(user, successCallback, errorCallback){
        dataService.putData({
            servlet : userAuthServlet,
            resourceId : user.userName,
            data : user,
            callback : {
                success : successCallback,
                error : errorCallback
            }
        });
    };

    var getAuthUserById = function(id, successCallback, errorCallback)
    {
        entityService.getEntityById(userAuthServlet, successCallback, errorCallback, id);
    };

    var deleteAuthUser = function(user, successCallback, errorCallback)
    {
        dataService.deleteData({
            servlet : userAuthServlet,
            resourceId : user.userName,
            data : undefined,
            callback : {
                success : successCallback,
                error : errorCallback
            }
        });
    };

    var _getStoreDataParameters = function(user) {
        return {
            "user-auth" : user
        };
    };

    return {
        getAuthUsersLike : getAuthUsersLike,
        saveAuthUser : saveAuthUser,
        updateAuthUser : updateAuthUser,
        getAuthUserById : getAuthUserById,
        deleteAuthUser : deleteAuthUser
    };
}]);