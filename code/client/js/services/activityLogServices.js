var activityLogServices = angular.module('activityLogServices', ['newDataServices', 'appConfigModule', 'cookieUtilsModule', 'webApiConfigModule']);

activityLogServices.factory('activityLogService', ['newDataService', 'appConfig', 'cookieUtils', 'webApiConfig',
function(newDataService, appConfig, cookieUtils, webApiConfig) {

	var EXPORT_DOWNLOAD_COOKIE = "activityLogDownloadStatus";
	var EXPORT_DOWNLOAD_COOKIE_EXPECTED_VALUE = "finished";

	var URLS = webApiConfig.webApiUrls.entity.activitylog;

	var exportActivityLogBetween = function(dateFrom, dateTo, successCallback) {

    var parameters = {};

    parameters[webApiConfig.webApiParameters.dateFrom] = dateFrom;
    parameters[webApiConfig.webApiParameters.dateTo] = dateTo;

    var downloadHandler = newDataService.getDownloadHandler();
    downloadHandler(successCallback, EXPORT_DOWNLOAD_COOKIE, EXPORT_DOWNLOAD_COOKIE_EXPECTED_VALUE);

    newDataService.triggerExportViaHtmlForm(URLS.export, parameters);
	};

	return {
		exportActivityLogBetween : exportActivityLogBetween
	};
}]);
