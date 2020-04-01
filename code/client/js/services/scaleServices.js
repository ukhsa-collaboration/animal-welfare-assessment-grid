var scaleServices = angular.module('scaleServices', ['appConfigModule', 'pagingUtilsModule', 'webApiConfigModule', 'newDataServices']);

scaleServices.factory('scaleService', ['appConfig', 'pagingUtils', 'newDataService', 'webApiConfig',
    function (appConfig, pagingUtils, newDataService, webApiConfig) {

        var URLS = webApiConfig.webApiUrls.entity.scale;
        var SCALE_NAME_KEY = 'scaleName';
        var SCALE_ID_KEY = 'scaleId';

        var getScale = function (successCallback, id) {
            var parameters = {};
            var url = URLS.getById + id;
            newDataService.httpGet(url, parameters, successCallback);
        };

        var getScalesLike = function (callback, likeTerm, pagingOptions) {
            var parameters = {};

            parameters[webApiConfig.webApiParameters.likeFilter] = likeTerm;
            pagingUtils.newApiSetPagingParameters(parameters, pagingOptions);

            var onSuccess = function (data) {
                var metadata = pagingUtils.buildPagingMetadata(
                    data[webApiConfig.webApiResponseKeys.pagingInfo], SCALE_NAME_KEY, SCALE_ID_KEY);
                callback(data.scales, metadata);
            };

            newDataService.httpGet(URLS.getLike, parameters, onSuccess);
        };

        var saveScale = function (scale, successCallback, errCallback) {
            var onSuccess = function (data) {
                var response = {};
                response[SCALE_NAME_KEY] = data.value;
                response[SCALE_ID_KEY] = data.id;
                successCallback(response);
            }

            newDataService.httpPost(URLS.create, scale, onSuccess, errCallback);
        };

        var updateScale = function (scale, successCallback, errCallback) {
            var url = URLS.update + scale.scaleId;
            newDataService.httpPut(url, scale, successCallback, errCallback);
        };

        var uploadScale = function (data, successCallback, errCallback) {
            var url = URLS.upload;
            newDataService.httpPostWithHeaderContentType(url, data, successCallback, errCallback);
        };


        return {
            getScale: getScale,
            getScalesLike: getScalesLike,
            saveScale: saveScale,
            updateScale: updateScale,
            uploadScale: uploadScale
        };
    }
]);