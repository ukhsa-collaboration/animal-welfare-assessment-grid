var entityServices = angular.module('entityServices', ['appConfigModule', 'dataServices', 'pagingUtilsModule']);

entityServices.factory('entityService', ['appConfig', 'dataService', 'pagingUtils',
function(appConfig, dataService, pagingUtils) {

    var getEntityLike = function(servletName, callback, likeTerm, pagingOptions){
        var parameters = {};
        parameters[appConfig.services.selectAction] = appConfig.services.selectActions.like;
        parameters[appConfig.services.actionParams.like] = likeTerm;
        pagingUtils.setPagingParameters(parameters, pagingOptions);
        dataService.dataConnection({
            servlet : servletName,
            parameters : parameters,
            callback : {
                success : callback
            }
        });
    };

    var getEntityById = function(servletName, successCallback, errorCallback, id)
    {
        var parameters = {};
        parameters[appConfig.services.selectAction] = appConfig.services.selectActions.id;
        parameters[appConfig.services.actionParams.id] = id;
        dataService.dataConnection({
            servlet : servletName,
            parameters : parameters,
            callback : {
                success : successCallback,
                error : errorCallback
            }
        });
    };

    var saveEntity = function(servletName, parameters, successCallback, errorCallback){
        dataService.postData({
            servlet : servletName,
            parameters : parameters,
            callback : {
                success : successCallback,
                error : errorCallback
            }
        });
    };

    return {
        getEntityLike : getEntityLike,
        saveEntity : saveEntity,
        getEntityById : getEntityById
    };
}]);
