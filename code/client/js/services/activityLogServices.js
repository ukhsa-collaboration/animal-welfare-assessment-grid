var activityLogServices = angular.module('activityLogServices', ['dataServices', 'appConfigModule', 'cookieUtilsModule']);

activityLogServices.factory('activityLogService', ['dataService', 'appConfig', 'cookieUtils', 
function(dataService, appConfig, cookieUtils) {
	var downloadCookie = "activityLogDownloadStatus";
	var downloadCookieExpectedValue = "finished";

	var exportActivityLogBetween = function(dateFrom, dateTo, fnSuccessCallback){
		var parameters = {};
		parameters[appConfig.services.actionParams.dateFrom] = dateFrom;
		parameters[appConfig.services.actionParams.dateTo] = dateTo;
		var exportType = 'activitylogs';

        var downloadHandler = dataService.getDownloadHandler(fnSuccessCallback, downloadCookie, downloadCookieExpectedValue);
        downloadHandler(fnSuccessCallback, downloadCookie, downloadCookieExpectedValue);

		dataService.exportDataAsFile({parameters : parameters}, exportType);
	};

	return {
		exportActivityLogBetween : exportActivityLogBetween
	};
}]);
