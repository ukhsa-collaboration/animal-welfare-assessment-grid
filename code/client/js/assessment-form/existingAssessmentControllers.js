var existingAssessmentFormControllers = angular.module('existingAssessmentFormControllers', ['formServices', 'animalServices', 'userServices',
    'reasonServices', 'assessmentServices', 'templateServices', 'studyServices', 'dateUtilsModule', 'awFilters', 'ngTable', 'tableServices']);

var existingAssessmentFormEvents = {
    searchAnimalSelected : "searchAnimalSelected",
    searchDateFromSelected : "searchDateFromSelected",
    searchDateToSelected : "searchDateToSelected",
    searchUserSelected : "searchUserSelected",
    searchReasonSelected : "searchReasonSelected",
    searchStudySelected : "searchStudySelected",
    searchIsCompleteSelected : "searchIsCompleteSelected",
    searchResultAvailable: "searchResultAvailable",
    searchNoDataFound: "searchNoDataFound",
    searchForMoreData: "searchForMoreData",
    searchCriteriaCleared : "searchCriteriaCleared",
    beforeSearchRun : "beforeSearchRun",
    assessmentSelected : "assessmentSelected",
    assessmentSelectedError : "assessmentSelectedError",
    assessmentDeleted : "assessmentDeleted"
};

existingAssessmentFormControllers.controller('ExistAssessFormAccordionCtrl', ['$scope',
function($scope)
{
    this.accordionStatus = {
        isSearchOpen : true,
        isResultsOpen : false,
        isAssessmentOpen : false
    };

    var that = this;

    $scope.$on(existingAssessmentFormEvents.beforeSearchRun, function(event, data){
        that.accordionStatus.isAssessmentOpen = false;
    });

    $scope.$on(existingAssessmentFormEvents.searchResultAvailable, function(event, data){
        that.accordionStatus.isSearchOpen = false;
        that.accordionStatus.isResultsOpen = true;
    });

    $scope.$on(existingAssessmentFormEvents.searchNoDataFound, function(event, data){
        that.accordionStatus.isSearchOpen = true;
        that.accordionStatus.isResultsOpen = false;
    });

    $scope.$on(existingAssessmentFormEvents.assessmentSelected, function(event, data){
        that.accordionStatus.isResultsOpen = false;
        that.accordionStatus.isAssessmentOpen = true;
    });

    $scope.$on(existingAssessmentFormEvents.assessmentSelectedError, function(event, data){
        that.accordionStatus.isResultsOpen = false;
        that.accordionStatus.isSearchOpen = false;
        that.accordionStatus.isAssessmentOpen = false;
    });

    $scope.$on(existingAssessmentFormEvents.assessmentDeleted, function(event, data){
        that.accordionStatus.isAssessmentOpen = false;
    });
}]);

existingAssessmentFormControllers.controller('ExistAssessFormAssessmentSectionCtrl', ['$rootScope', '$scope',
function($rootScope, $scope)
{
    this.sectionVisible = false;

    var that = this;

    $scope.$on(existingAssessmentFormEvents.beforeSearchRun, function(event, data){
        that.sectionVisible = false;
    });

    $scope.$on(existingAssessmentFormEvents.assessmentSelected, function(event, data){
        that.sectionVisible = true;
    });

    $scope.$on(existingAssessmentFormEvents.assessmentSelectedError, function(event, data){
        that.sectionVisible = false;
    });

    $scope.$on(existingAssessmentFormEvents.assessmentDeleted, function(event, data){
        that.sectionVisible = false;
    });
}]);

existingAssessmentFormControllers.controller('ExistAssessFormMainCtrl', ['$rootScope', '$scope', 'assessmentService', 'templateService',
function($rootScope, $scope, assessmentService, templateService)
{
    this.getAssessmentErrors = [];
    this.deleteAssessmentSuccess = false;
    this.resultsDirty = false;

    var that = this;

    this.onAssessmentSelected = function(assessment)
    {
        // TODO: Could display a spinner instead of assessment form until data is available
        
        var templateClosure;

        var getAssessmentCallback = function(fullAssessment)
        {
            that.assessmentFormController.switchMode(mode, fullAssessment, templateClosure);
        };

        var getAssessmentErrorCallback = function(errors)
        {
            that.getAssessmentErrors = errors;
            that.onGetAssessmentError();
        };

        var getAssessmentTemplateCallback = function(template)
        {
            templateClosure = template;
            assessmentService.getAssessmentById(assessment.assessmentId, getAssessmentCallback, getAssessmentErrorCallback);
        };

        var mode = this.chooseAssessmentFormMode(assessment);
        this.getAssessmentTemplate(assessment.animal.id, getAssessmentTemplateCallback);
    };

    this.chooseAssessmentFormMode = function(assessment)
    {
        return assessment.isComplete ? assessmentFormModes.existingComplete : assessmentFormModes.existingIncomplete;
    };

    this.getAssessmentTemplate = function(animalId, callback)
    {
        templateService.getTemplateByAnimalId(callback, animalId);
    };

    this.assessmentFormControllerCallback = function(assessmentFormController)
    {
        this.assessmentFormController = assessmentFormController;

        $scope.$on(assessmentFormController.submitSuccessInExistingModeEvent, function(event, data){
            that.resultsDirty = true;
        });

        $scope.$on(assessmentFormController.deleteSuccessEvent, function(event, data){
            $rootScope.$broadcast(existingAssessmentFormEvents.assessmentDeleted);
            that.resultsDirty = true;
            that.deleteAssessmentSuccess = true;
        });
    };

    this.onGetAssessmentError = function()
    {
        $rootScope.$broadcast(existingAssessmentFormEvents.assessmentSelectedError);
        this.assessmentFormController.clearForm();
    };

    $scope.$on(existingAssessmentFormEvents.assessmentSelected, function(event, data){
        that.getAssessmentErrors = [];
        that.onAssessmentSelected(data);
    });

    $scope.$on(existingAssessmentFormEvents.beforeSearchRun, function(event){
        that.resultsDirty = false;
        that.getAssessmentErrors = [];
    });

    // Just to be sure as we link them manually
    $scope.$on("$destroy", function(event, data){
        that.assessmentFormController = undefined;
    });
}]);

existingAssessmentFormControllers.controller('ExistAssessFormSearchCtrl', ['$rootScope', '$scope', 'assessmentService', 'dateUtils', '$timeout',
function($rootScope, $scope, assessmentService, dateUtils, $timeout)
{
    var that = this;
    this.criteria = {};
    this.errors = [];
    this.lastPagingOptions = null;
    this.isCriteriaSpecified = false;

    this.onSearchClick = function()
    {
        if (this.lastPagingOptions)
        {
            this.lastPagingOptions.offset = 0;
        }

        this.search(this.lastPagingOptions);
    };

    this.search = function(pagingOptions) {
        this.resetErrors();
        $rootScope.$broadcast(existingAssessmentFormEvents.beforeSearchRun);

        if (pagingOptions)
        {
            this.lastPagingOptions = pagingOptions;
        }

        var c = this.criteria;

        // Need to do this to force undefined state.
        c.dateFrom = dateUtils.isDateValid(c.dateFrom) ? c.dateFrom : undefined;
        c.dateTo = dateUtils.isDateValid(c.dateTo) ? c.dateTo : undefined;

        assessmentService.getExistingAssessmentsByCriteria(
            c.animalId, c.dateFrom, c.dateTo, c.userId, c.reasonId, c.studyId, c.isComplete, onSearchSuccess, onSearchError, this.lastPagingOptions);
    };

    var onSearchSuccess = function(data) {
        if (data.assessments && data.assessments.length)
        {
            var response = {data : data.assessments, metadata : data.pagingInfo};
            $rootScope.$broadcast(existingAssessmentFormEvents.searchResultAvailable, response);
        }
        else
        {
            $rootScope.$broadcast(existingAssessmentFormEvents.searchNoDataFound);
            that.errors.push("No data found. Try changing search parameters.");
        }
    };

    var onSearchError = function(errors) {
        that.errors = errors;
    };

    $scope.$on(existingAssessmentFormEvents.searchForMoreData, function(event, data){
        that.search(data.pagingOptions);
    });

    this.resetErrors = function()
    {
        this.errors = [];
    };

    this.clearCriteria = function()
    {
        this.criteria = {};
        that.updateCriteriaStatus();
        $rootScope.$broadcast(existingAssessmentFormEvents.searchCriteriaCleared);
    };

    this.updateCriteriaStatus = function()
    {
        if (angular.equals({}, this.criteria))
        {
           this.isCriteriaSpecified = false;
        }
        else
        {
            this.isCriteriaSpecified = this.criteria.animalId != null || dateUtils.isDateValid(this.criteria.dateFrom) ||
                dateUtils.isDateValid(this.criteria.dateTo) || this.criteria.userId  != null || this.criteria.reasonId != null ||
                    this.criteria.studyId != null || this.criteria.isComplete != null;
        }

        $timeout(function(){
            $scope.$apply();
        });
    };

    $scope.$on(existingAssessmentFormEvents.searchAnimalSelected, function(event, data){
        that.criteria.animalId = (data != null ? data.id : null);
        that.updateCriteriaStatus();
    });

    $scope.$on(existingAssessmentFormEvents.searchDateFromSelected, function(event, data){
        that.criteria.dateFrom = data;
        that.updateCriteriaStatus();
    });

    $scope.$on(existingAssessmentFormEvents.searchDateToSelected, function(event, data){
        that.criteria.dateTo = data;
        that.updateCriteriaStatus();
    });

    $scope.$on(existingAssessmentFormEvents.searchUserSelected, function(event, data){
        that.criteria.userId = (data != null ? data.id : null);
        that.updateCriteriaStatus();
    });

    $scope.$on(existingAssessmentFormEvents.searchReasonSelected, function(event, data){
        that.criteria.reasonId = (data != null ? data.id : null);
        that.updateCriteriaStatus();
    });

    $scope.$on(existingAssessmentFormEvents.searchStudySelected, function(event, data){
        that.criteria.studyId = (data != null ? data.id : null);
        that.updateCriteriaStatus();
    });

    $scope.$on(existingAssessmentFormEvents.searchIsCompleteSelected, function(event, data){
        that.criteria.isComplete = (data != null ? data : null);
        that.updateCriteriaStatus();
    });

}]);

existingAssessmentFormControllers.controller('ExistAssessFormAnimalSelectionCtrl', ['$scope', 'formService', 'animalService',
function($scope, formService, animalService)
{
    this.selectId = 'searchAnimalSelect';
    this.placeHolder = 'Search assessments by animal...';
    var that = this;

    this.onSearchSelect2DirectiveLinked = function() {
        var acceptNewValues = false;
        formService.initSearchSelect2(jQuery('#' + this.selectId), animalService.getAnimalsLike, acceptNewValues, this.placeHolder);
    };

    this.onSearchSelect2Selecting = function(choice) {
        $scope.$emit(existingAssessmentFormEvents.searchAnimalSelected, choice);
    };

    this.onSearchSelect2Removed = function(choice) {
        $scope.$emit(existingAssessmentFormEvents.searchAnimalSelected, null);
    };

    $scope.$on(existingAssessmentFormEvents.searchCriteriaCleared, function(event){
        formService.clearSelect2(jQuery("#" + that.selectId));
    });

}]);

existingAssessmentFormControllers.controller('ExistAssessFormDateFromSelectionCtrl', ['$scope', 'formService',
function($scope, formService)
{
    this.selectId = 'searchDateFromSelect';
    var that = this;

    this.onDateChange = function(date) {
        $scope.$emit(existingAssessmentFormEvents.searchDateFromSelected, date);
    };

    $scope.$on(existingAssessmentFormEvents.searchCriteriaCleared, function(event)
    {
        formService.clearDatePickerField(jQuery("#" + that.selectId));
    });

    // TODO: what if the date is cleared manually?
}]);

existingAssessmentFormControllers.controller('ExistAssessFormDateToSelectionCtrl', ['$scope', 'formService',
function($scope, formService)
{
    this.selectId = 'searchDateToSelect';
    var that = this;

    this.onDateChange = function(date) {
        $scope.$emit(existingAssessmentFormEvents.searchDateToSelected, date);
    };

    $scope.$on(existingAssessmentFormEvents.searchCriteriaCleared, function(event)
    {
        formService.clearDatePickerField(jQuery("#" + that.selectId));
    });

    // TODO: what if the date is cleared manually?
}]);

existingAssessmentFormControllers.controller('ExistAssessFormUserSelectionCtrl', ['userService', 'formService', '$scope',
function(userService, formService, $scope)
{
    this.selectId = "searchUserSelect";
    this.placeHolder = "Search assessments performed by user...";
    var that = this;

    this.onSearchSelect2DirectiveLinked = function()
    {
        formService.initSearchSelect2(jQuery("#" + this.selectId), userService.getUsersLike, true, this.placeHolder);
    };

    this.onSearchSelect2Selecting = function(choice)
    {
        $scope.$emit(existingAssessmentFormEvents.searchUserSelected, choice);
    };

    this.onSearchSelect2Removed = function(choice)
    {
        $scope.$emit(existingAssessmentFormEvents.searchUserSelected, null);
    };

    $scope.$on(existingAssessmentFormEvents.searchCriteriaCleared, function(event){
        formService.clearSelect2(jQuery("#" + that.selectId));
    });
}]);

existingAssessmentFormControllers.controller('ExistAssessFormReasonSelectionCtrl', ['reasonService', 'formService', '$scope',
function(reasonService, formService, $scope)
{
    this.selectId = "searchReasonSelect";
    this.placeHolder = "Search assessments by reasons...";

    var that = this;

    this.onSearchSelect2DirectiveLinked = function()
    {
        formService.initSearchSelect2(jQuery("#" + this.selectId), reasonService.getReasonsLike, true, this.placeHolder);
    };

    this.onSearchSelect2Selecting = function(choice)
    {
        $scope.$emit(existingAssessmentFormEvents.searchReasonSelected, choice);
    };

    this.onSearchSelect2Removed = function(choice)
    {
        $scope.$emit(existingAssessmentFormEvents.searchReasonSelected, null);
    };

    $scope.$on(existingAssessmentFormEvents.searchCriteriaCleared, function(event){
        formService.clearSelect2(jQuery("#" + that.selectId));
    });

}]);

existingAssessmentFormControllers.controller('ExistAssessFormStudySelectionCtrl', ['studyService', 'formService', '$scope',
function(studyService, formService, $scope)
{
    this.selectId = "searchStudySelect";
    this.placeHolder = "Search assessments by studies...";

    var that = this;

    this.onSearchSelect2DirectiveLinked = function()
    {
        formService.initSearchSelect2(jQuery("#" + this.selectId), studyService.getStudiesLike, true, this.placeHolder);
    };

    this.onSearchSelect2Selecting = function(choice)
    {
        $scope.$emit(existingAssessmentFormEvents.searchStudySelected, choice);
    };

    this.onSearchSelect2Removed = function(choice)
    {
        $scope.$emit(existingAssessmentFormEvents.searchStudySelected, null);
    };

    $scope.$on(existingAssessmentFormEvents.searchCriteriaCleared, function(event){
        formService.clearSelect2(jQuery("#" + that.selectId));
    });
}]);

existingAssessmentFormControllers.controller('ExistAssessFormIsCompleteSelectionCtrl', ['formService', '$scope',
function(formService, $scope)
{
    this.selectId = "searchIsCompleteSelect";
    this.placeHolder = "Search assessments by completeness status...";

    var that = this;

    var data = [{id : "true", text : "YES"}, {id : "false", text : "NO"}];

    this.onSelect2DirectiveLinked = function()
    {
        formService.initSelect2FixedChoice(data, jQuery("#" + this.selectId), that.placeHolder);
    };

    this.onSearchSelect2Removed = function(choice)
    {
        $scope.$emit(existingAssessmentFormEvents.searchIsCompleteSelected, null);
    };

    this.onSelect2Change = function(choice)
    {
        $scope.$emit(existingAssessmentFormEvents.searchIsCompleteSelected, choice);
    };

    $scope.$on(existingAssessmentFormEvents.searchCriteriaCleared, function(event){
        formService.clearSelect2(jQuery("#" + that.selectId));
    });
}]);

existingAssessmentFormControllers.controller('ExistAssessFormResultsCtrl', ['$rootScope', '$scope', 'ngTableParams', 'tableService', '$timeout',
function($rootScope, $scope, ngTableParams, tableService, $timeout)
{
    this.availableAssessments = 0;
    this.isTableInit = true;
    this.clearTable = false;
    this.newData = null;

    var that = this;

    this.tableParams = new ngTableParams({
        page : 1,
        count : 10
    },
    {
        total : 0,
        getData : function($defer, params){
            if (that.newData !== null)
            {
                $defer.resolve(that.newData);
                that.newData = null;
                return;
            }

            if (that.isTableInit) {
                that.isTableInit = false;
                return;
            }

            if (that.clearTable) {
                that.clearTable = false;
                that.tableParams.total(0);
                that.tableParams.page(1);
                $defer.resolve([]);
                return;
            }

            that.getPagedData(params);
        }
    });

    this.getPagedData = function(pagingParams)
    {
        // TODO: put the paging calculations into pagingUtils and update tableServices to use the utils too
        var page = pagingParams.page();
        var count = pagingParams.count();

        var pagingOptions = tableService.pagingOptions;
        pagingOptions.offset = (page - 1) * count;
        pagingOptions.limit = count;

        var eventData = {
            pagingOptions : pagingOptions
        };

        $rootScope.$broadcast(existingAssessmentFormEvents.searchForMoreData, eventData);
    };

    this.assessmentSelected = function(assessment)
    {
        $rootScope.$broadcast(existingAssessmentFormEvents.assessmentSelected, assessment);
    };

    $scope.$on(existingAssessmentFormEvents.searchResultAvailable, function(event, data) {
        var metadata = data.metadata;
        that.newData = data.data;

        that.availableAssessments = metadata.total;
        that.tableParams.total(metadata.total);
        that.tableParams.page(metadata.page);

        that.tableParams.reload();
    });

    $scope.$on(existingAssessmentFormEvents.beforeSearchRun, function(event) {
        that.availableAssessments = 0;
        that.newData = null;
    });

    $scope.$on(existingAssessmentFormEvents.searchNoDataFound, function(event){
        that.availableAssessments = 0;
        that.clearTable = true;
        that.newData = null;
        that.tableParams.reload();
    });
}]);
