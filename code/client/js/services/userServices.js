var userServices = angular.module('userServices', ['dataServices', 'entityServices']);

userServices.factory('userService', ['dataService', 'entityService',
function(dataService, entityService) {

    var userServlet = "user";

    var create = function(id, name) {
        return {
            userId : id,
            userName : name
        };
    };

    var getUsersLike = function(callback, likeTerm, pagingOptions) {
        entityService.getEntityLike(userServlet, callback, likeTerm, pagingOptions);
    };

    var saveUser = function(user, successCallback, errCallback) {
        var parameters = {user : user};
        entityService.saveEntity(userServlet, parameters, successCallback, errCallback);
    };

    return {
        getUsersLike : getUsersLike,
        saveUser : saveUser,
        create : create
    };
}]);
