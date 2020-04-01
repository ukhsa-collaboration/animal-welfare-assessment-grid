var pagingUtilsModule = angular.module('pagingUtilsModule', ['appConfigModule', 'webApiConfigModule']);
pagingUtilsModule.factory('pagingUtils', ['appConfig', 'webApiConfig',
    function(appConfig, webApiConfig) {

        var that = this;

        var setPagingParameters = function(parameters, pagingOptions, pageable) {
            if(pageable===false){
                return;
            } else {
                that.setOffset(parameters, pagingOptions);
                that.setLimit(parameters, pagingOptions);
                that.setIncludeMetadata(parameters, pagingOptions);
            }
        };

        var newApiSetPagingParameters = function(parameters, pagingOptions, pageable) {
            if(pageable===false){
                return;
            } else {
                that.setOffset(parameters, pagingOptions);
                that.setLimit(parameters, pagingOptions);
            }
        };

        var buildPagingMetadata = function(pagingInfo, entityNameKey, entityIdKey) {
            var metadata = pagingInfo;
            metadata[appConfig.config.entityNameKey] = entityNameKey;
            metadata[appConfig.config.entityIdKey] = entityIdKey;
            return metadata;
        };

        this.setOffset = function(parameters, pagingOptions){
            if(pagingOptions){
                parameters[webApiConfig.webApiParameters.paging.offset] = pagingOptions[webApiConfig.webApiParameters.paging.offset];
            } else {
                parameters[webApiConfig.webApiParameters.paging.offset] = appConfig.config.defaultPageOffsetStart;
            }
        };

        this.setLimit = function(parameters, pagingOptions){
            if (pagingOptions) {
                parameters[webApiConfig.webApiParameters.paging.limit] = pagingOptions[webApiConfig.webApiParameters.paging.limit];
            } else {
                parameters[webApiConfig.webApiParameters.paging.limit] = appConfig.config.defaultPageSize;
            }
        };

        this.setIncludeMetadata = function(parameters, pagingOptions){
            if (pagingOptions) {
               parameters[appConfig.services.pagingParams.include] = pagingOptions[appConfig.services.pagingParams.include];
            } else {
                parameters[appConfig.services.pagingParams.include] = appConfig.services.pagingParamValues.metadata;
            }
        };

        return {
            setPagingParameters: setPagingParameters,
            newApiSetPagingParameters: newApiSetPagingParameters,
            buildPagingMetadata: buildPagingMetadata
        };
    }
]);