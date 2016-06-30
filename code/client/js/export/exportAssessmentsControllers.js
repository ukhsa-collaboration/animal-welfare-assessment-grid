var exportAssessmentsControllers = angular.module('exportAssessmentsControllers', ['formServices', 'assessmentServices', 'dateUtilsModule']);

var exportAssessmentFormEvents = {
    searchStudySelected : "searchStudySelected",
    searchStudyCleared : "searchStudyCleared",
    searchCriteriaCleared : "searchCriteriaCleared"
};

exportAssessmentsControllers.controller('ExportAssessMainCtrl', ['$rootScope', '$scope', 'assessmentDynamicSearchService',
function($rootScope, $scope, assessmentDynamicSearchService)
{
    this.errors = [];
    this.isCriteriaSpecified = false;
    var controllerName = 'ExportAssessMainCtrl';

    var that = this;

    this.clearCriteria = function()
    {
        $rootScope.$broadcast(exportAssessmentFormEvents.searchCriteriaCleared);
    };

    var onAssessmentSearchDataSourceSwitch = function(mode)
    {
        that.isCriteriaSpecified = mode === assessmentDynamicSearchService.filteredDataSourceMode;
        that.errors = [];
    };

    var onAssessmentSearchErrors = function(data)
    {
        that.errors = data;
    };

    $scope.$on("$destroy", function(event, data){
        assessmentDynamicSearchService.unregisterDataSourceSwitchObserver(controllerName);
        assessmentDynamicSearchService.unregisterSearchErrorObserver(controllerName);
    });

    assessmentDynamicSearchService.registerDataSourceSwitchObserver(controllerName, onAssessmentSearchDataSourceSwitch);
    assessmentDynamicSearchService.registerSearchErrorObserver(controllerName, onAssessmentSearchErrors);

}]);

exportAssessmentsControllers.controller('ExportAssessStudySelectionCtrl', ['$rootScope', 'formService', 'assessmentDynamicSearchService', '$scope',
function($rootScope, formService, assessmentDynamicSearchService, $scope)
{
    this.selectId = "searchStudySelect";
    this.placeHolder = "Search assessments by studies...";

    var that = this;

    this.onSearchSelect2DirectiveLinked = function()
    {
        formService.initSearchSelect2(jQuery("#" + this.selectId), assessmentDynamicSearchService.getStudies, false, this.placeHolder);
    };

    this.onSearchSelect2Selecting = function(choice)
    {
        assessmentDynamicSearchService.onValueSelected(assessmentDynamicSearchService.studyEntity, choice);
        $rootScope.$broadcast(exportAssessmentFormEvents.searchStudySelected, null);
    };

    this.onSearchSelect2Removed = function(choice)
    {
        this.onValueCleared();
    };

    this.onValueCleared = function()
    {
        assessmentDynamicSearchService.onValueCleared(assessmentDynamicSearchService.studyEntity);
        $rootScope.$broadcast(exportAssessmentFormEvents.searchStudyCleared, null);
    };

    $scope.$on(exportAssessmentFormEvents.searchCriteriaCleared, function(event){
        formService.clearSelect2(jQuery("#" + that.selectId));
        that.onValueCleared();
    });
}]);

exportAssessmentsControllers.controller('ExportAssessStudyGroupSelectionCtrl', ['formService', 'assessmentDynamicSearchService', '$scope', '$timeout',
function(formService, assessmentDynamicSearchService, $scope, $timeout)
{
    this.selectId = "searchStudyGroupSelect";
    this.placeHolder = "Search assessments by study groups...";
    this.disabled = true;

    var that = this;

    this.setDisabled = function(isDisabled)
    {
        this.disabled = isDisabled;
        $timeout(function(){
            $scope.$apply();
        });
    };

    this.onSearchSelect2DirectiveLinked = function()
    {
        formService.initSearchSelect2(jQuery("#" + this.selectId), assessmentDynamicSearchService.getStudyGroups, false, this.placeHolder);
    };

    this.onSearchSelect2Selecting = function(choice)
    {
        assessmentDynamicSearchService.onValueSelected(assessmentDynamicSearchService.studyGroupEntity, choice);
    };

    this.onSearchSelect2Removed = function(choice)
    {
        this.onValueCleared();
    };

    this.onValueCleared = function()
    {
        assessmentDynamicSearchService.onValueCleared(assessmentDynamicSearchService.studyGroupEntity);
    };
    
    $scope.$on(exportAssessmentFormEvents.searchStudyCleared, function(event){
        formService.clearSelect2(jQuery("#" + that.selectId));
        that.setDisabled(true);
        that.onValueCleared();
    });

    $scope.$on(exportAssessmentFormEvents.searchStudySelected, function(event){
        that.setDisabled(false);
    });

    $scope.$on(exportAssessmentFormEvents.searchCriteriaCleared, function(event){
        formService.clearSelect2(jQuery("#" + that.selectId));
        that.onValueCleared();
    });
}]);

exportAssessmentsControllers.controller('ExportAssessAnimalSelectionCtrl', ['formService', 'assessmentDynamicSearchService', '$scope', '$timeout',
function(formService, assessmentDynamicSearchService, $scope, $timeout)
{
    this.selectId = "searchAnimalSelect";
    this.placeHolder = "Search assessments by animal...";

    var that = this;

    this.onSearchSelect2DirectiveLinked = function()
    {
        formService.initSearchSelect2(jQuery("#" + this.selectId), assessmentDynamicSearchService.getAnimals, false, this.placeHolder);
    };

    this.onSearchSelect2Selecting = function(choice)
    {
        assessmentDynamicSearchService.onValueSelected(assessmentDynamicSearchService.animalEntity, choice);
    };

    this.onSearchSelect2Removed = function(choice)
    {
        this.onValueCleared();
    };

    this.onValueCleared = function()
    {
        assessmentDynamicSearchService.onValueCleared(assessmentDynamicSearchService.animalEntity);
    };

    $scope.$on(exportAssessmentFormEvents.searchCriteriaCleared, function(event){
        formService.clearSelect2(jQuery("#" + that.selectId));
        that.onValueCleared();
    });
}]);

exportAssessmentsControllers.controller('ExportAssessDateFromSelectionCtrl', ['$scope', 'formService','assessmentDynamicSearchService', 'dateUtils',
function($scope, formService, assessmentDynamicSearchService, dateUtils)
{
    this.selectId = 'searchDateFromSelect';
    var that = this;
    var controllerName = 'ExportAssessDateFromSelectionCtrl';

    var onAssessmentSearchSuccessStartDateCallback = function(startDate)
    {
        if (startDate)
        {
            var date = dateUtils.getFormDateString(startDate);
            setBorderDateValues(formService.setDatePickerStartDate, date);
        }
    };

    var onAssessmentSearchSuccessEndDateCallback = function(endDate)
    {
        if (endDate)
        {        
            var date = dateUtils.getFormDateString(endDate);
            setBorderDateValues(formService.setDatePickerEndDate, date);
        }
    };

    var setBorderDateValues = function(setter, date)
    {
        setter(date, jQuery("#" + that.selectId));  
    };

    var onAssessmentSearchDataSourceSwitch = function(mode)
    {
        if (mode === assessmentDynamicSearchService.initialDataSourceMode)
        {
            formService.resetDatePickerStartDate(jQuery("#" + that.selectId));
            formService.resetDatePickerEndDate(jQuery("#" + that.selectId));
        }
    };

    this.onDateChange = function(date) {
        // This check is needed since clearing a date triggers a 'date change' event too
        if (dateUtils.isDateValid(date))
        {
            assessmentDynamicSearchService.onValueSelected(assessmentDynamicSearchService.startDate, date);
        }
    };

    this.clearDate = function() {
        this.onValueCleared();
    };

    this.onValueCleared = function() {
        formService.clearDatePickerField(jQuery("#" + that.selectId));
        assessmentDynamicSearchService.onValueCleared(assessmentDynamicSearchService.startDate);
    };

    // TODO: what if the date is cleared manually?

    $scope.$on(exportAssessmentFormEvents.searchCriteriaCleared, function(event)
    {
        that.onValueCleared();
    });

    $scope.$on("$destroy", function(event, data){
        assessmentDynamicSearchService.unregisterSearchSuccessObserver(controllerName,
            assessmentDynamicSearchService.startDate);
        
        assessmentDynamicSearchService.unregisterSearchSuccessObserver(controllerName,
            assessmentDynamicSearchService.endDate);

        assessmentDynamicSearchService.unregisterDataSourceSwitchObserver(controllerName);
    });

    assessmentDynamicSearchService.registerSearchSuccessObserver(controllerName, {
        'observedField' : assessmentDynamicSearchService.startDate,
        'callback' : onAssessmentSearchSuccessStartDateCallback
    });

    assessmentDynamicSearchService.registerSearchSuccessObserver(controllerName, {
        'observedField' : assessmentDynamicSearchService.endDate,
        'callback' : onAssessmentSearchSuccessEndDateCallback
    });

    assessmentDynamicSearchService.registerDataSourceSwitchObserver(controllerName, onAssessmentSearchDataSourceSwitch);
}]);

exportAssessmentsControllers.controller('ExportAssessDateToSelectionCtrl', ['$scope', 'formService','assessmentDynamicSearchService', 'dateUtils',
function($scope, formService, assessmentDynamicSearchService, dateUtils)
{
    this.selectId = 'searchDateToSelect';
    var that = this;
    var controllerName = 'ExportAssessDateToSelectionCtrl';

    var onAssessmentSearchSuccessStartDateCallback = function(startDate)
    {
        if (startDate)
        {
            var date = dateUtils.getFormDateString(startDate);
            setBorderDateValues(formService.setDatePickerStartDate, date);
        }
    };

    var onAssessmentSearchSuccessEndDateCallback = function(endDate)
    {
        if (endDate)
        {        
            var date = dateUtils.getFormDateString(endDate);
            setBorderDateValues(formService.setDatePickerEndDate, date);
        }
    };

    var setBorderDateValues = function(setter, date)
    {
        setter(date, jQuery("#" + that.selectId));  
    };

    var onAssessmentSearchDataSourceSwitch = function(mode)
    {
        if (mode === assessmentDynamicSearchService.initialDataSourceMode)
        {
            formService.resetDatePickerStartDate(jQuery("#" + that.selectId));
            formService.resetDatePickerEndDate(jQuery("#" + that.selectId));
        }
    };

    this.onDateChange = function(date) {
        // This check is needed since clearing a date triggers a 'date change' event too
        if (dateUtils.isDateValid(date))
        {
            assessmentDynamicSearchService.onValueSelected(assessmentDynamicSearchService.endDate, date);
        }
    };

    this.clearDate = function() {
        this.onValueCleared();
    };

    this.onValueCleared = function() {
        formService.clearDatePickerField(jQuery("#" + that.selectId));
        assessmentDynamicSearchService.onValueCleared(assessmentDynamicSearchService.endDate);
    };

    // TODO: what if the date is cleared manually?

    $scope.$on(exportAssessmentFormEvents.searchCriteriaCleared, function(event)
    {
        that.onValueCleared();
    });

    $scope.$on("$destroy", function(event, data){
        assessmentDynamicSearchService.unregisterSearchSuccessObserver(controllerName,
            assessmentDynamicSearchService.startDate);
        
        assessmentDynamicSearchService.unregisterSearchSuccessObserver(controllerName,
            assessmentDynamicSearchService.endDate);

        assessmentDynamicSearchService.unregisterDataSourceSwitchObserver(controllerName);
    });

    assessmentDynamicSearchService.registerSearchSuccessObserver(controllerName, {
        'observedField' : assessmentDynamicSearchService.startDate,
        'callback' : onAssessmentSearchSuccessStartDateCallback
    });

    assessmentDynamicSearchService.registerSearchSuccessObserver(controllerName, {
        'observedField' : assessmentDynamicSearchService.endDate,
        'callback' : onAssessmentSearchSuccessEndDateCallback
    });

    assessmentDynamicSearchService.registerDataSourceSwitchObserver(controllerName, onAssessmentSearchDataSourceSwitch);
}]);

exportAssessmentsControllers.controller('ExportAssessReasonSelectionCtrl', ['formService', 'assessmentDynamicSearchService', '$scope', '$timeout',
function(formService, assessmentDynamicSearchService, $scope, $timeout)
{
    this.selectId = "searchReasonSelect";
    this.placeHolder = "Search assessments by reason...";

    var that = this;

    this.onSearchSelect2DirectiveLinked = function()
    {
        formService.initSearchSelect2(jQuery("#" + this.selectId), assessmentDynamicSearchService.getReasons, false, this.placeHolder);
    };

    this.onSearchSelect2Selecting = function(choice)
    {
        assessmentDynamicSearchService.onValueSelected(assessmentDynamicSearchService.reasonEntity, choice);
    };

    this.onSearchSelect2Removed = function(choice)
    {
        this.onValueCleared();
    };

    this.onValueCleared = function()
    {
        assessmentDynamicSearchService.onValueCleared(assessmentDynamicSearchService.reasonEntity);
    };

    $scope.$on(exportAssessmentFormEvents.searchCriteriaCleared, function(event){
        formService.clearSelect2(jQuery("#" + that.selectId));
        that.onValueCleared();
    });
}]);

exportAssessmentsControllers.controller('ExportAssessUserSelectionCtrl', ['formService', 'assessmentDynamicSearchService', '$scope', '$timeout',
function(formService, assessmentDynamicSearchService, $scope, $timeout)
{
    this.selectId = "searchUserCtrl";
    this.placeHolder = "Search assessments performed by users...";

    var that = this;

    this.onSearchSelect2DirectiveLinked = function()
    {
        formService.initSearchSelect2(jQuery("#" + this.selectId), assessmentDynamicSearchService.getUsers, false, this.placeHolder);
    };

    this.onSearchSelect2Selecting = function(choice)
    {
        assessmentDynamicSearchService.onValueSelected(assessmentDynamicSearchService.userEntity, choice);
    };

    this.onSearchSelect2Removed = function(choice)
    {
        this.onValueCleared();
    };

    this.onValueCleared = function()
    {
        assessmentDynamicSearchService.onValueCleared(assessmentDynamicSearchService.userEntity);
    };

    $scope.$on(exportAssessmentFormEvents.searchCriteriaCleared, function(event){
        formService.clearSelect2(jQuery("#" + that.selectId));
        that.onValueCleared();
    });
}]);

exportAssessmentsControllers.controller('ExportAssessExportCtrl', ['formService', 'assessmentService', 'assessmentDynamicSearchService', '$scope', 'dateUtils', '$timeout',
function(formService, assessmentService, assessmentDynamicSearchService, $scope, dateUtils, $timeout)
{
    this.exports = [];
    this.isExporting = false;
    this.errors = [];

    $scope.model = assessmentDynamicSearchService.model;
    assessmentDynamicSearchService.refreshCompleteAssessmentCount();

    var that = this;

    var onGetAllAssessmentsSuccessCallback = function(data)
    {
        that.addExport({}, data.assessments);
    };

    var onGetAllAssessmentsErrorCallback = function(data)
    {
        that.errors = data;
    };

    this.onAddExportBtnClick = function()
    {
        this.errors = [];

        var searchCriteria = assessmentDynamicSearchService.getCurrentSearchCriteria();
        var isSearchCriteriaSet = Object.getOwnPropertyNames(searchCriteria).length;

        if (isSearchCriteriaSet)
        {
            var assessments = assessmentDynamicSearchService.getCurrentlyAvailableAssessments();
            this.addExport(searchCriteria, assessments);
        }
        else
        {
            assessmentService.getAssessmentsWithDynamicSearchCriteria(null, null, null, null, null, null, null,
                onGetAllAssessmentsSuccessCallback, onGetAllAssessmentsErrorCallback);
        }
    };

    this.addExport = function(searchCriteria, assessments)
    {
        var summary = this.getSearchCriteriaSummary(searchCriteria);

        var exportData = {
            'searchCriteriaSummary' : summary,
            'noOfAssessments' : assessments.length,
            'assessments' : assessments
        };

        this.exports.push(exportData);
    };

    this.getSearchCriteriaSummary = function(searchCriteria)
    {
        var criteria = [];
        criteria.push({'key' : 'Study', 'val' : assessmentDynamicSearchService.studyEntity});
        criteria.push({'key' : 'study group', 'val' : assessmentDynamicSearchService.studyGroupEntity});
        criteria.push({'key' : 'animal', 'val' : assessmentDynamicSearchService.animalEntity});
        criteria.push({'key' : 'reason', 'val' : assessmentDynamicSearchService.reasonEntity});
        criteria.push({'key' : 'performed by', 'val' : assessmentDynamicSearchService.userEntity});
        criteria.push({'key' : 'date from', 'val' : assessmentDynamicSearchService.startDate});
        criteria.push({'key' : 'date to', 'val' : assessmentDynamicSearchService.endDate});

        var summary = "";
        for (var i = 0, limit = criteria.length; i < limit; i++)
        {
            var entry = criteria[i];
            var searchCriteriaEntry = searchCriteria[entry.val];
            var criteriaValue;

            if (searchCriteriaEntry)
            {
                if (entry.val === assessmentDynamicSearchService.startDate
                    || entry.val === assessmentDynamicSearchService.endDate)
                {
                    criteriaValue = dateUtils.formatDateToString(searchCriteriaEntry);
                }
                else
                {
                    criteriaValue = searchCriteriaEntry.text;
                }
            }
            else
            {
                criteriaValue = 'any';
            }

            summary += entry.key + ': ' + criteriaValue + ', ';
        }

        summary = summary.substring(0, summary.length-2);

        return summary;
    };

    this.removeFromExport = function(searchCriteriaSummary)
    {
        var idx = null;

        for (var i = 0, limit = this.exports.length; i < limit; i++) {
            var exportData = this.exports[i];
            if (exportData.searchCriteriaSummary === searchCriteriaSummary)
            {
                idx = i;
                break;
            }
        };

        if (idx != null)
        {
            this.exports.splice(idx, 1);
        }
    };

    this.removeAllFromExport = function()
    {
        this.exports = [];
    };

    var onExportSuccessCallback = function()
    {
        that.isExporting = false;
        $timeout(function(){
            $scope.$apply();
        });
    };

    this.onExportBtnClick = function()
    {
        this.isExporting = true;

        var assessmentIds = {};

        for (var i = 0, ilimit = this.exports.length; i < ilimit; i++)
        {
            var exportData = this.exports[i];
            for (var j = 0, jlimit = exportData.assessments.length; j < jlimit; j++)
            {
                var assessment = exportData.assessments[j];
                assessmentIds[assessment.id] = null;
            }
        }

        assessmentService.exportAssessments(Object.getOwnPropertyNames(assessmentIds),
            onExportSuccessCallback);
    };

}]);    
