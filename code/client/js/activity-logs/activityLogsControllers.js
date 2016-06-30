var activityLogsControllers = angular.module('activityLogsControllers', ['activityLogServices', 'formServices', 'dateUtilsModule']);

var activityLogsEvents = {
    searchDateFromSelected : "searchDateFromSelected",
    searchDateToSelected : "searchDateToSelected",
    searchCriteriaCleared : "searchCriteriaCleared"
};

activityLogsControllers.controller('ActivityLogsMainCtrl', ['$rootScope', '$scope', '$timeout', 'dateUtils', 'formService', 'activityLogService',
function($rootScope, $scope, $timeout, dateUtils, formService, activityLogService)
{
    this.errors = [];
    this.criteria = {};
    this.downloadBtnText = "";
    this.isDownloading = false;

    var that = this;

    this.resetDownloadStatus = function()
    {
        this.isDownloading = false;
        this.downloadBtnText = "Download as CSV";
    };

    this.onDownloadClick = function()
    {
        var c = this.criteria;

        // Need to do this to force undefined state.
        c.dateFrom = dateUtils.isDateValid(c.dateFrom) ? c.dateFrom : undefined;
        c.dateTo = dateUtils.isDateValid(c.dateTo) ? c.dateTo : undefined;

        this.onDownloadStart();

        activityLogService.exportActivityLogBetween(c.dateFrom, c.dateTo, onSearchSuccess);
    };

    var onSearchSuccess = function() {
        $timeout(function (){
            that.resetDownloadStatus();
        });
    };

    this.onDownloadStart = function() {
        this.downloadBtnText = "Download in progress...";
        this.isDownloading = true;
    };

    this.clearCriteria = function(){
        this.criteria = {};
        $rootScope.$broadcast(existingAssessmentFormEvents.searchCriteriaCleared);
    };

    $scope.$on(existingAssessmentFormEvents.searchDateFromSelected, function(event, data){
        that.criteria.dateFrom = data;
    });

    $scope.$on(existingAssessmentFormEvents.searchDateToSelected, function(event, data){
        that.criteria.dateTo = data;
    });

    this.resetDownloadStatus();
}]);

activityLogsControllers.controller('ActivityLogsDateFromSelectionCtrl', ['$scope', 'formService',
function($scope, formService)
{
    this.selectId = 'searchDateFromSelect';
    var that = this;

    this.onDateChange = function(date) {
        $scope.$emit(activityLogsEvents.searchDateFromSelected, date);
    };

    $scope.$on(activityLogsEvents.searchCriteriaCleared, function(event)
    {
        formService.clearDatePickerField(jQuery("#" + that.selectId));
    });
}]);

activityLogsControllers.controller('ActivityLogsDateToSelectionCtrl', ['$scope', 'formService',
function($scope, formService)
{
    this.selectId = 'searchDateToSelect';
    var that = this;

    this.onDateChange = function(date) {
        $scope.$emit(activityLogsEvents.searchDateToSelected, date);
    };

    $scope.$on(activityLogsEvents.searchCriteriaCleared, function(event)
    {
        formService.clearDatePickerField(jQuery("#" + that.selectId));
    });
}]);

