var reasonServices = angular.module('reasonServices', ['appConfigModule', 'pagingUtilsModule', 'webApiConfigModule', 'newDataServices']);

reasonServices.factory('reasonService', ['appConfig', 'pagingUtils', 'newDataService', 'webApiConfig',
    function(appConfig, pagingUtils, newDataService, webApiConfig) {

        var URLS = webApiConfig.webApiUrls.entity.reason;
        var REASON_NAME_KEY = 'reasonName';
        var REASON_ID_KEY = 'reasonId';

        var create = function(id, name) {
            return {
                reasonId: id,
                reasonName: name
            };
        };

        var getReasonsLike = function(callback, likeTerm, pagingOptions) {
            var parameters = {};

            parameters[webApiConfig.webApiParameters.likeFilter] = likeTerm;
            pagingUtils.newApiSetPagingParameters(parameters, pagingOptions);

            var onSuccess = function(data) {
                var metadata = pagingUtils.buildPagingMetadata(
                    data[webApiConfig.webApiResponseKeys.pagingInfo], REASON_NAME_KEY, REASON_ID_KEY);
                callback(data.reasons, metadata);
            };

            newDataService.httpGet(URLS.getLike, parameters, onSuccess);
        };

        var saveReason = function(reason, successCallback, errCallback) {

            var onSuccess = function(data) {
                var response = {};
                response[REASON_NAME_KEY] = data.value;
                response[REASON_ID_KEY] = data.id;
                successCallback(response);
            }

            newDataService.httpPost(URLS.create, reason, onSuccess, errCallback);
        };

        var updateReason = function(reason, successCallback, errCallback) {
            var url = URLS.update + reason.reasonId;
            newDataService.httpPut(url, reason, successCallback, errCallback);
        };

        var uploadReason = function(data, successCallback, errCallback) {
            var url = URLS.upload;
            newDataService.httpPostWithHeaderContentType(url, data, successCallback, errCallback);
        };    

        return {
            getReasonsLike: getReasonsLike,
            saveReason: saveReason,
            updateReason: updateReason,
            create: create,
            uploadReason: uploadReason
        };
    }
]);