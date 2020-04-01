var newDataServices = angular.module('newDataServices', ['appConfigModule', 'cookieUtilsModule']);

newDataServices.factory('newDataService', ['$http', 'appConfig', 'cookieUtils',
  function($http, appConfig, cookieUtils) {

    var httpPost = function(url, data, onSuccess, onError) {
      $http.post(url, data)
        .success(function(response, status) {
          if (response && !response.errors) {
            onSuccess(response);
          } else {
            onError(response.errors);
          }
        }).error(function(response, status) {
          _handleErrors(response.errors, status, onError);
        });
    };

    
    var httpPostWithHeaderContentType = function(url, data, onSuccess, onError) {
      $http.post(url, data, {transformRequest: angular.identity, headers: {'Content-Type': undefined}})
        .success(function(response, status) {
          if (response && !response.errors) {
            onSuccess(response);
          } else {
            onError(response.errors);
          }
        }).error(function(response, status) {
          _handleErrors(response.errors, status, onError);
        });
    };

    var httpGet = function(url, parameters, onSuccess, onError) {
      $http.get(url, {
        params: parameters
      }).success(function(response, status) {
        if (response && !response.errors) {
          onSuccess(response);
        } else {
          onError(response.errors);
        }
      }).error(function(response, status) {
        _handleErrors(response.errors, status, onError);
      });
    };

    var httpPut = function(url, data, onSuccess, onError) {
      $http.put(url, data)
        .success(function(response, status) {
          if (status === 200 && !response) {
            onSuccess();
          } else if (response && !response.errors) {
            onSuccess(response);
          } else {
            onError(response.errors);
          }
        }).error(function(response, status) {
          _handleErrors(response.errors, status, onError);
        });
    };

    var httpDelete = function(url, onSuccess, onError) {
      $http.delete(url)
        .success(function(response, status) {
          if (status === 200 && !response) {
            onSuccess();
          } else if (response && !response.errors) {
            onSuccess(response);
          } else {
            onError(response.errors);
          }
        }).error(function(response, status) {
          _handleErrors(response.errors, status, onError);
        });
    };

    var triggerExportViaHtmlForm = function(url, parameters) {

      var formHtml = '<form style="display:none;" action="' + url + '" method="POST">';
      for (key in parameters) {
        var value = parameters[key];
        if (value != null || value != undefined) {
          formHtml += '<input type="hidden" name=' + key + ' value=' + angular.toJson(value) + ' />';
        }
      }
      formHtml += '</form>';

      var $form = jQuery(formHtml);
      jQuery('body').append($form);

      $form.submit();
      $form.remove();
    };

    var getDownloadHandler = function(){

      var handler = function(successCallback, downloadCookie, downloadCookieExpectedValue) {
          var attempts = 15;
          downloadTimer = window.setInterval( function() {
              var cookie = cookieUtils.getCookie(downloadCookie);
              
              if( cookie === downloadCookieExpectedValue || (attempts == 0) ) {
                  window.clearInterval(downloadTimer);
                  successCallback();
              }

              attempts--;

          }, 1000 );
      };

      return handler;
    };    

    var _handleErrors = function(errors, status, fnErrorCallback) {
      if (angular.isUndefined(errors)) {
        errors = [];
      }

      switch (status) {
        case 403:
          errors.push(appConfig.config.errorAccessDeniedMessage);
          break;
      }

      fnErrorCallback(errors);
    };

    return {
      httpPost: httpPost,
      httpGet: httpGet,
      httpPut: httpPut,
      httpDelete: httpDelete,
      triggerExportViaHtmlForm: triggerExportViaHtmlForm,
      getDownloadHandler: getDownloadHandler,
      httpPostWithHeaderContentType : httpPostWithHeaderContentType
    };
  }
]);