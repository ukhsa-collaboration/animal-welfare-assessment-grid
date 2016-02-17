var activityLogServices = angular.module('activityLogServices', ['dataServices', 'appConfigModule', 'cookieUtilsModule']);

activityLogServices.factory('activityLogService', ['dataService', 'appConfig', 'cookieUtils', 
function(dataService, appConfig, cookieUtils) {
	var activityLogServlet = "activitylog";
	var downloadCookie = "activityLogDownloadStatus";
	var downloadCookieExpectedValue = "finished";

	var getActivityLogBetween = function(dateFrom, dateTo, fnSuccessCallback){
		var parameters = {};
		parameters[appConfig.services.actionParams.dateFrom] = dateFrom;
		parameters[appConfig.services.actionParams.dateTo] = dateTo;

		_onDownloadStarted(fnSuccessCallback);

		var iFrameElem = jQuery("#activityLogIframe");
		dataService.downloadFileViaIframe(iFrameElem, {
			servlet : activityLogServlet,
			parameters : parameters
		});
	};

	var _onDownloadStarted = function(fnSuccessCallback) {
	  	var attempts = 15;
	    downloadTimer = window.setInterval( function() {
			var cookie = cookieUtils.getCookie(downloadCookie);
			
			if( cookie === downloadCookieExpectedValue || (attempts == 0) ) {
				window.clearInterval(downloadTimer);
				fnSuccessCallback();
			}

			attempts--;

		}, 1000 );
	};

	return {
		getActivityLogBetween : getActivityLogBetween
	};
}]);
