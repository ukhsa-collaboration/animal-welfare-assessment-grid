var pagingUtilsModule = angular.module('pagingUtilsModule', ['appConfigModule']);
pagingUtilsModule.factory('pagingUtils', ['appConfig',
    function(appConfig) {

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


        this.setOffset = function(parameters, pagingOptions){
            if(pagingOptions){
                parameters[appConfig.services.pagingParams.offset] = pagingOptions[appConfig.services.pagingParams.offset];
            } else {
                parameters[appConfig.services.pagingParams.offset] = appConfig.config.defaultPageOffsetStart;
            }
        };


        this.setLimit = function(parameters, pagingOptions){
            if (pagingOptions) {
                parameters[appConfig.services.pagingParams.limit] = pagingOptions[appConfig.services.pagingParams.limit];
            } else {
                parameters[appConfig.services.pagingParams.limit] = appConfig.config.defaultPageSize;
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
            setPagingParameters: setPagingParameters
        };
    }
]);