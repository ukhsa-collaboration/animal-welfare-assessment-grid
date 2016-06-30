var dataServices = angular.module('dataServices', ['appConfigModule', 'cookieUtilsModule']);

dataServices.factory('dataService', ['$http', 'appConfig', 'cookieUtils',
function($http, appConfig, cookieUtils) {

	var pathSeparator = "/";

	var exportDataAsFile = function(options, exportType) {
		var url = __getUrl({servlet : 'exportdata'});

		if (!options.parameters) {
			options.parameters = {};
		}

		var params = options.parameters;
		params['export-type'] = exportType;

		var form = '<form action="' + url + '" method="POST">';
		for (key in params)
		{
			var value = params[key];
			if (value != null || value != undefined)
			{
				form += '<input type="hidden" name=' + key + ' value=' + angular.toJson(value) + ' />';
			}
		}
		form += '</form>';

		jQuery(form).submit();
	};

	var dataConnection = function(options) {
		var url = __getUrl(options);

		if (!options.parameters) {
			options.parameters = {};
		}

		options.parameters.callback = "JSON_CALLBACK";

		var config = {
			params : options.parameters
		};

		$http.jsonp(url, config).success(function(data, status) {
			if(data.errors.length===0)
			{
				options.callback.success(data.data, data.metadata);
			} else {
				options.callback.error(data.errors);
			}
		}).error(function(data, status){
			__handleErrors(data.errors, status, options.callback.error);
		});
	};

	var dataConnectionNextPage = function(options){

		var baseUrl = window.awconfig.serverUrl;
		var nextPageUrl = baseUrl.substr(0, baseUrl.length-1) + options.nextPage;
		var config = {
			headers : {
				"Content-Type" : "text/javascript"
			},
			params : {
				callback : "JSON_CALLBACK"
			}
		};

		$http.jsonp(nextPageUrl, config).success(function(data) {
			if(data.errors.length===0)
			{
				options.callback.success(data.data, data.metadata);
			}
		}).error(function(data, status){
			__handleErrors(data.errors, status, options.callback.error);
		});
	};

	var postData = function(options) {
		var url = __getUrl(options);
		var dataStr = __turnIntoPostDataString(options.parameters);
		var config = {
			headers : {
				"Content-Type" : "application/x-www-form-urlencoded"
			}
		};
		$http.post(url, dataStr, config).success(function(data) {
			options.callback.success(data.data);
		}).error(function(data, status) {
			__handleErrors(data.errors, status, options.callback.error);
		});
	};

	var __handleErrors = function(errors, status, fnErrorCallback){
		if(angular.isUndefined(errors)){
			errors = [];
		}

		switch(status){
			case 403:				
				errors.push(appConfig.config.errorAccessDeniedMessage);
			break;
		}

		fnErrorCallback(errors);
	}

	var putData = function(options) {
		var url = __getUrl(options);
		var dataStr = angular.toJson(options.data);

		var config = {
			headers : {
				"Content-Type" : "application/json"
			},
		};

		if (options.params)
		{
			config.params = options.params;
		}

		$http.put(url, dataStr, config).success(function(data) {
			options.callback.success(data.data);
		}).error(function(data, status) {
			__handleErrors(data.errors, status, options.callback.error);
		});
	};

	/*
		@param options - Options used to build the request url
		@param options.servlet - Path to append to base server url.
		@param options.resourceId - The id for the resource defined by servlet.
				
		if servlet and resource id are provided the following url will be created.
		e.g. If the servlet is "template" and resourceId is 10000, the following would output: https://<base-server-url>/template/10000.
			
		@param options.pathParamMap - If servlet is not provided a custom map of path parameter names and their values should be provided.
		e.g. The following would output https://<base-server-url>/template/10000/parameter/10001 if no servlet and resource id is provided.
		{
			template : 10000,
			parameter : 10001
		}

		if a servlet and resource id is also provided the parameter names and values are appended to the end.
	
		@param options.callback.success The callback method to call if the http request is successful.
		@param options.callback.error The callback method to call if the http request is not successful.
	*/
	var deleteData = function(options) {
		var url = __getUrl(options);
		$http.delete(url).success(function(data) {
			options.callback.success(data);
		}).error(function(data, status) {
			__handleErrors(data.errors, status, options.callback.error);
		});
	};

	var __getUrl = function(options) {
		var url = window.awconfig.serverUrl;

		servletPath = options.servlet;
		resourceId = options.resourceId;
		pathParamMap = options.pathParmMap;

		if(servletPath)
		{
			url += servletPath;
			url += pathSeparator;
			if(resourceId)
			{
				url += options.resourceId
				url += pathSeparator;			
			}
		}		

		if(angular.isDefined(pathParamMap))
		{
			for(var key in pathParamMap)
			{
				url += key;
				url += pathSeparator;
				url += options.pathParmMap[key];
				url += pathSeparator;
			}
		}

		url = url.substring(0, url.length - 1);
		return url;
	};

	var __turnIntoPostDataString = function(data) {
		var dataStr = "";
		for (key in data) {
			dataStr += key + "=" + angular.toJson(data[key]);
			dataStr += "&";
		}
		dataStr = dataStr.substring(0, dataStr.length - 1);
		return dataStr;
	};

    var getDownloadHandler = function(fnSuccessCallback, downloadCookie, downloadCookieExpectedValue){

    	var handler = function(fnSuccessCallback, downloadCookie, downloadCookieExpectedValue) {
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

	    return handler;
    };

	return {
		dataConnection : dataConnection,
		dataConnectionNextPage : dataConnectionNextPage,
		postData : postData,
		putData : putData,
		deleteData : deleteData,
		getDownloadHandler : getDownloadHandler,
		exportDataAsFile : exportDataAsFile
	};
}]);
