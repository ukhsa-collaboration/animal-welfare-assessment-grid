var formServices = angular.module('formServices', []);
formServices.factory('formService', ['appConfig', '$timeout',
    function (appConfig, $timeout) {
        var _isExcludedChoice = function (choice, excludedChoices) {
            if (!excludedChoices) {
                return false;
            }

            for (var k = 0, l = excludedChoices.length; k < l; k++) {
                if (excludedChoices[k].id === choice.id) {
                    return true;
                }
            }
            return false;
        }

        var _getResetPagingOptions = function () {
            return {
                offset: appConfig.config.defaultPageOffsetStart,
                limit: appConfig.config.defaultPageSize,
                include: appConfig.services.pagingParamValues.metadata
            };
        }

        var initSearchSelect2 = function (element, fnGetChoices, acceptNewValues, placeHolder, excludedChoices, acceptMultiple) {
            //holds a promise that will be executed after the timout time
            var timeoutQuery;
            var timeoutTime = 500;

            var data = {
                results: []
            };

            var pagingOptions = _getResetPagingOptions();

            /*
                 This function inspects the metadata sent back from a request fired by the select2 box and 
                 calculates whether it is the last page.
                 @param callbackMetadata - Holds all paging metadata.
                 @param callbackMetadata.page - Holds current page number for a query. If it is -1 there are no more valid pages.
                 @param callbackMetadata.totalPages - Holds the total possible pages for the current query.
             */
            var _isLastPage = function (callbackMetadata) {
                return (!(callbackMetadata.page < callbackMetadata.totalPages) || (callbackMetadata.page === appConfig.config.noSuchPage));
            };

            element.select2({
                query: function (query) {
                    var onSelectLikeData = function (callbackData, callbackMetadata) {
                        //load the data from the query response into the select2 box emitting excluded choices
                        data.results = [];

                        if (callbackData) {

                            for (var i = 0,
                                    j = callbackData.length; i < j; i++) {

                                var found = false;
                                var result = callbackData[i];

                                if (!_isExcludedChoice(result, excludedChoices)) {
                                    data.results.push({
                                        id: callbackData[i][callbackMetadata[appConfig.config.entityIdKey]],
                                        text: callbackData[i][callbackMetadata[appConfig.config.entityNameKey]]
                                    });
                                }
                            }
                        }

                        //if there are more pages for a query store the paging metadata in the select2 context and enable scrolling
                        if (callbackMetadata && !_isLastPage(callbackMetadata)) {
                            data.more = true;
                            data.context = {
                                pagingMetadata: callbackMetadata
                            };
                        } else {
                            data.more = false;
                        }

                        //return the data object back to select2
                        query.callback(data);
                    };

                    //initially disable scrolling
                    data.more = false;
                    /*
                        if a user has supplied a query and there is nothing in the context for the search term reset paging options and 
                        carry on with the data request otherwise set the paging options for the request and get the next page.  
                    */
                    if (query.term !== "") {
                        if (query.context == null) {
                            pagingOptions = _getResetPagingOptions();
                        } else if (data.context != null) {
                            if (!_isLastPage(data.context.pagingMetadata)) {
                                pagingOptions.offset += appConfig.config.defaultPageSize;
                            }
                        }

                        //cancel old query/promise
                        $timeout.cancel(timeoutQuery);

                        //set the new query to search
                        timeoutQuery = $timeout(function () {
                            fnGetChoices(onSelectLikeData, query.term, pagingOptions);
                        }, timeoutTime);
                    }
                },
                createSearchChoice: function (term, data) {
                    if (!acceptNewValues) {
                        return null;
                    }
                    if ($(data).filter(function () {
                            return this.text.localeCompare(term) === 0;
                        }).length === 0) {
                        return {
                            id: appConfig.config.unassignedId,
                            text: term
                        };
                    }
                },
                formatNoMatches: function (term) {
                    return "No matches for " + term;
                },
                allowClear: true,
                width: "100%",
                placeholder: placeHolder,
                maximumSelectionSize: function () {
                    if (angular.isDefined(acceptMultiple)) {
                        return -1;
                    } else {

                        return 1;
                    }
                },
                minimumResultsForSearch: -1,
                tags: data,
                formatSearching: "<div align='left'>Start typing to search...</div><div align='right'>X</div>",
                formatLoadMore: "Loading more results..."
            });
        };

        var initSelect2 = function (data, element, placeHolder) {
            element.select2({
                multiple: true,
                width: "100%",
                maximumSelectionSize: 1,
                placeholder: placeHolder,
                tags: data
            });
        };

        var initSelect2FixedChoice = function (data, element, placeHolder) {
            element.select2({
                placeholder: placeHolder,
                multiple: false,
                width: "100%",
                maximumSelectionSize: 1,
                minimumResultsForSearch: -1,
                data: data
            });
        };

        var setSelect2Val = function (data, element) {
            element.select2('data', data);
        };

        var clearSelect2 = function (element) {
            element.select2('val', "");
        };

        var setDatePickerFieldValue = function (date, element) {
            element.datepicker("setDate", date);
        };

        var clearDatePickerField = function (element) {
            element.datepicker("setDate", null);
        };

        var setDatePickerStartDate = function (date, element) {
            element.datepicker("setStartDate", date);
        };

        var resetDatePickerStartDate = function (element) {
            element.datepicker("setStartDate", false);
        };

        var setDatePickerEndDate = function (date, element) {
            element.datepicker("setEndDate", date);
        };

        var resetDatePickerEndDate = function (element) {
            element.datepicker("setEndDate", false);
        };

        var enableDatePicker = function (element) {
            element.removeProp("disabled");
        };

        var disableDatePicker = function (element) {
            element.prop("disabled", "disabled");
        };

        var destroySelect2 = function (element) {
            element.select2("destroy");
        };

        var disableSelect2 = function (element) {
            element.select2("enable", false);
        };

        var enableSelect2 = function (element) {
            element.select2("enable", true);
        };

        return {
            initSelect2: initSelect2,
            initSelect2FixedChoice: initSelect2FixedChoice,
            setSelect2Val: setSelect2Val,
            clearSelect2: clearSelect2,
            setDatePickerFieldValue: setDatePickerFieldValue,
            initSearchSelect2: initSearchSelect2,
            destroySelect2: destroySelect2,
            clearDatePickerField: clearDatePickerField,
            setDatePickerStartDate: setDatePickerStartDate,
            setDatePickerEndDate: setDatePickerEndDate,
            resetDatePickerStartDate: resetDatePickerStartDate,
            resetDatePickerEndDate: resetDatePickerEndDate,
            enableDatePicker: enableDatePicker,
            disableDatePicker: disableDatePicker,
            enableSelect2: enableSelect2,
            disableSelect2: disableSelect2
        };
    }
]);