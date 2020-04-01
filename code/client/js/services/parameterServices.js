var parameterServices = angular.module('parameterServices', ['appConfigModule', 'pagingUtilsModule', 'webApiConfigModule', 'newDataServices']);

parameterServices.factory('parameterService', ['appConfig', 'pagingUtils', 'newDataService', 'webApiConfig',
    function (appConfig, pagingUtils, newDataService, webApiConfig) {

        var URLS = webApiConfig.webApiUrls.entity.parameter;
        var PARAMETER_NAME_KEY = 'parameterName';
        var PARAMETER_ID_KEY = 'parameterId';

        var create = function (id, name) {
            return {
                parameterId: id,
                parameterName: name,
                parameterFactors: []
            };
        };

        var getParametersLike = function (callback, likeTerm, pagingOptions) {
            var parameters = {};

            parameters[webApiConfig.webApiParameters.likeFilter] = likeTerm;
            pagingUtils.newApiSetPagingParameters(parameters, pagingOptions);

            var onSuccess = function (data) {
                var metadata = pagingUtils.buildPagingMetadata(
                    data[webApiConfig.webApiResponseKeys.pagingInfo], PARAMETER_NAME_KEY, PARAMETER_ID_KEY);
                callback(data.parameters, metadata);
            };

            newDataService.httpGet(URLS.getLike, parameters, onSuccess);
        };

        var saveParameter = function (parameter, successCallback, errCallback) {
            var onSuccess = function (data) {
                var response = {};
                response[PARAMETER_NAME_KEY] = data.value;
                response[PARAMETER_ID_KEY] = data.id;
                successCallback(response);
            }

            newDataService.httpPost(URLS.create, parameter, onSuccess, errCallback);
        };

        var updateParameter = function (parameter, successCallback, errCallback) {
            var url = URLS.update + parameter.parameterId;
            newDataService.httpPut(url, parameter, successCallback, errCallback);
        };

        var uploadParameter = function (data, successCallback, errCallback) {
            var url = URLS.upload;
            newDataService.httpPostWithHeaderContentType(url, data, successCallback, errCallback);
        };

        return {
            getParametersLike: getParametersLike,
            saveParameter: saveParameter,
            updateParameter: updateParameter,
            create: create,
            uploadParameter: uploadParameter
        };
    }
]);