var factorServices = angular.module('factorServices', ['appConfigModule', 'pagingUtilsModule', 'webApiConfigModule', 'newDataServices']);

factorServices.factory('factorService', ['appConfig', 'pagingUtils', 'newDataService', 'webApiConfig',
    function (appConfig, pagingUtils, newDataService, webApiConfig) {

        var URLS = webApiConfig.webApiUrls.entity.factor;
        var FACTOR_NAME_KEY = 'factorName';
        var FACTOR_ID_KEY = 'factorId';

        var getFactorsLike = function (callback, likeTerm, pagingOptions) {
            var parameters = {};

            parameters[webApiConfig.webApiParameters.likeFilter] = likeTerm;
            pagingUtils.newApiSetPagingParameters(parameters, pagingOptions);

            var onSuccess = function (data) {
                var metadata = pagingUtils.buildPagingMetadata(
                    data[webApiConfig.webApiResponseKeys.pagingInfo], FACTOR_NAME_KEY, FACTOR_ID_KEY);
                callback(data.factors, metadata);
            };

            newDataService.httpGet(URLS.getLike, parameters, onSuccess);
        };

        var getFactorById = function(successCallback, factorId) {
            var parameters = {};
            var url = URLS.getById + factorId;
            newDataService.httpGet(url, parameters, successCallback);
        };

        var saveFactor = function (factor, successCallback, errCallback) {
            newDataService.httpPost(URLS.create, factor, successCallback, errCallback);
        };

        var updateFactor = function (factor, successCallback, errCallback) {
            var url = URLS.update + factor.factorId;
            newDataService.httpPut(url, factor, successCallback, errCallback);
        };

        var uploadFactor = function (data, successCallback, errCallback) {
            var url = URLS.upload;
            newDataService.httpPostWithHeaderContentType(url, data, successCallback, errCallback);
        };

        return {
            getFactorsLike: getFactorsLike,
            getFactorById: getFactorById,
            saveFactor: saveFactor,
            updateFactor: updateFactor,
            uploadFactor: uploadFactor
        };
    }
]);