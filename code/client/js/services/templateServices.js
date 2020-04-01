var templateServices = angular.module('templateServices', ['appConfigModule', 'pagingUtilsModule', 'webApiConfigModule', 'newDataServices']);

templateServices.factory('templateService', ['appConfig', 'pagingUtils', 'webApiConfig', 'newDataService',
    function (appConfig, pagingUtils, webApiConfig, newDataService) {

        var URLS = webApiConfig.webApiUrls.entity.template;
        var TEMPLATE_NAME_KEY = 'templateName';
        var TEMPLATE_ID_KEY = 'templateId';

        var create = function (id, name) {
            return {
                templateId: id,
                templateName: name,
                templateScale: {},
                templateParameters: []
            };
        };

        var getTemplate = function (callback, id) {
            var parameters = {};
            var url = URLS.getById + id;
            newDataService.httpGet(url, parameters, callback);
        };

        var getTemplatesLike = function (callback, likeTerm, pagingOptions) {
            var parameters = {};

            parameters[webApiConfig.webApiParameters.likeFilter] = likeTerm;
            pagingUtils.newApiSetPagingParameters(parameters, pagingOptions);

            var onSuccess = function (data) {
                var metadata = pagingUtils.buildPagingMetadata(
                    data[webApiConfig.webApiResponseKeys.pagingInfo], TEMPLATE_NAME_KEY, TEMPLATE_ID_KEY);
                callback(data.templates, metadata);
            };

            newDataService.httpGet(URLS.getLike, parameters, onSuccess);
        };

        var getTemplateByAnimalId = function (callback, animalId) {
            var parameters = {};
            var url = URLS.getByAnimal + animalId;
            newDataService.httpGet(url, parameters, callback);
        };

        var saveTemplate = function (template, successCallback, errCallback) {
            var onSuccess = function (data) {
                var response = {};
                response[TEMPLATE_NAME_KEY] = data.value;
                response[TEMPLATE_ID_KEY] = data.id;
                successCallback(response);
            }

            newDataService.httpPost(URLS.create, template, onSuccess, errCallback);
        };

        var updateTemplate = function (template, successCallback, errCallback) {
            var url = URLS.update + template.templateId;
            newDataService.httpPut(url, template, successCallback, errCallback);
        };

        var removeTemplateParameter = function (templateId, parameterId, successCallback, errCallback) {
            var url = URLS.getDeleteParameterUrl(templateId, parameterId);
            newDataService.httpDelete(url, successCallback, errCallback);
        };

        var getTemplateCount = function (successCallback, errCallback) {
            var parameters = {};
            var url = URLS.count.all;

            newDataService.httpGet(url, parameters, successCallback, errCallback);
        };

        var uploadTemplate = function (data, successCallback, errCallback) {
            var url = URLS.upload;
            newDataService.httpPostWithHeaderContentType(url, data, successCallback, errCallback);
        };

        return {
            getTemplatesLike: getTemplatesLike,
            getTemplateByAnimalId: getTemplateByAnimalId,
            getTemplate: getTemplate,
            saveTemplate: saveTemplate,
            updateTemplate: updateTemplate,
            removeTemplateParameter: removeTemplateParameter,
            create: create,
            getTemplateCount: getTemplateCount,
            uploadTemplate: uploadTemplate
        };
    }
]);