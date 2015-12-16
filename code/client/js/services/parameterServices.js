var parameterServices = angular.module('parameterServices', ['dataServices','appConfigModule','pagingUtilsModule']);

parameterServices.factory('parameterService', ['dataService','appConfig','pagingUtils',
function(dataService, appConfig, pagingUtils) {
    
    var parameterServlet = "parameter";

    var create = function(id, name) {
        return {
            parameterId : id,
            parameterName : name,
            parameterFactors : []
        };
    };

    var getParameter = function(callback, id){
        var parameters = {};
        parameters[appConfig.services.selectAction] = appConfig.services.selectActions.id;
        parameters[appConfig.services.actionParams.id] = id;
        dataService.dataConnection({
            servlet : parameterServlet,
            parameters : parameters,
            callback : {
                success : callback
            }
        });
    };

    var getParameters = function(callback, pagingOptions) {
        var parameters = {};
        parameters[appConfig.services.actionParams.all] = appConfig.services.actionParamsValues.all;
        pagingUtils.setPagingParameters(parameters, pagingOptions);
        dataService.dataConnection({
            servlet : parameterServlet,
            parameters : parameters,
            callback : {
                success : callback
            }
        });
    };

    var getParametersLike = function(callback, likeTerm, pagingOptions){
        var parameters = {};
        parameters[appConfig.services.selectAction] = appConfig.services.selectActions.like;
        parameters[appConfig.services.actionParams.like] = likeTerm;
        pagingUtils.setPagingParameters(parameters, pagingOptions);
        dataService.dataConnection({
            servlet : parameterServlet,
            parameters : parameters,
            callback : {
                success : callback
            }
        });
    };

    var saveParameter = function(parameter, successCallback, errCallback){
        var parameters = _getStoreDataParameters(parameter);
        dataService.postData({
            servlet : parameterServlet,
            parameters : parameters,
            callback : {
                success : successCallback,
                error : errCallback
            }
        });
    };

    var updateParameter = function(parameter, successCallback, errCallback){
        dataService.putData({
            servlet : parameterServlet,
            resourceId : parameter.parameterId,
            data : parameter,
            callback : {
                success : successCallback,
                error : errCallback
            }
        });
    };

    var _getStoreDataParameters = function(parameter) {
        return {
            parameter : parameter
        };
    };

    return {
        getParameter : getParameter,
        getParameters : getParameters,
        getParametersLike : getParametersLike,
        saveParameter : saveParameter,
        updateParameter : updateParameter,
        create : create
    };
}]); 