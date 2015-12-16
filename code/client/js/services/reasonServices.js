var reasonServices = angular.module('reasonServices', ['dataServices', 'appConfigModule', 'pagingUtilsModule']);

reasonServices.factory('reasonService', ['dataService', 'appConfig', 'pagingUtils',
function(dataService, appConfig, pagingUtils) {

    var reasonServlet = "reason";

    var create = function(id, name) {
        return {
            reasonId : id,
            reasonName : name
        };
    };

    var getReason = function(callback, id){
        var parameters = {};
        parameters[appConfig.services.selectAction] = appConfig.services.selectActions.id;
        parameters[appConfig.services.actionParams.id] = id;
        dataService.dataConnection({
            servlet : reasonServlet,
            parameters : parameters,
            callback : {
                success : callback
            }
        });
    };

    var getReasons = function(callback, pagingOptions) {
        var parameters = {};
        parameters[appConfig.services.actionParams.all] = appConfig.services.actionParamsValues.all;
        pagingUtils.setPagingParameters(parameters, pagingOptions);
        dataService.dataConnection({
            servlet : reasonServlet,
            parameters : parameters,
            callback : {
                success : callback
            }
        });
    };

    var getReasonsLike = function(callback,likeTerm, pagingOptions){
        var parameters = {};
        parameters[appConfig.services.selectAction] = appConfig.services.selectActions.like;
        parameters[appConfig.services.actionParams.like] = likeTerm;
        pagingUtils.setPagingParameters(parameters, pagingOptions);
        dataService.dataConnection({
            servlet : reasonServlet,
            parameters : parameters,
            callback : {
                success : callback
            }
        });
    };

    var saveReason = function(reason, successCallback, errCallback){
        var parameters = _getStoreDataParameters(reason);
        dataService.postData({
            servlet : reasonServlet,
            parameters : parameters,
            callback : {
                success : successCallback,
                error : errCallback
            }
        });
    };

    var updateReason = function(reason, successCallback, errCallback){
        dataService.putData({
            servlet : reasonServlet,
            resourceId : reason.reasonId,
            data : reason,
            callback : {
                success : successCallback,
                error : errCallback
            }
        });
    };

    var _getStoreDataParameters = function(reason) {
        return {
            reason : reason
        };
    };

//TODO - Find out if this is still necessary after implementing reason management services.
/*  var saveReason = function(callback, reason) {
        dataService.dataConnection({
            servlet : "reason",
            parameters : {
                reason : reason
            },
            callback : {
                success : callback
            }
        });
    };*/

    return {
        getReasons : getReasons,
        getReason : getReason,
        getReasonsLike : getReasonsLike,
        saveReason : saveReason,
        updateReason : updateReason,
        create : create
        /*,
        saveReason : saveReason*/
    };
}]);
