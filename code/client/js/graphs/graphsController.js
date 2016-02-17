var graphsControllers = angular.module('graphsControllers', ['animalServices', 'assessmentServices', 'numberUtilsModule']);

var graphMngmtFormEvents = {
    onAnimalSelected : "onAnimalSelected",
    onAnimalRemoved : "onAnimalRemoved",
    onDateFromSelected : "onDateFromSelected",
    onDateToSelected : "onDateToSelected",
    onSelectDataFormCleared : "onSelectDataFormCleared"
};

graphsControllers.controller('AnimalSelectionController', ['$scope', 'formService', 'animalService', 'appConfig', 
    function($scope, formService, animalService, appConfig) {

    this.selectId = 'animalSelect';
    this.placeHolder = 'Search an animal...';
    var that = this;

    this.onSearchSelect2DirectiveLinked = function() {
        formService.initSearchSelect2(jQuery('#' + this.selectId), animalService.getAnimalsLike, false, this.placeHolder);
    };

    this.onSearchSelect2Selecting = function(choice) {
        if (choice) {
            animalService.getAnimalById(getAnimalCallback, choice.id);
        }
    };

    var getAnimalCallback = function(data) {
        $scope.$emit(graphMngmtFormEvents.onAnimalSelected, data);
    };

    this.onSearchSelect2Removed = function(choice) {
        $scope.$emit(graphMngmtFormEvents.onAnimalRemoved, choice);
    };

    $scope.$on(graphMngmtFormEvents.onSelectDataFormCleared, function(event)
    {
        formService.clearSelect2(jQuery("#" + that.selectId));
    });

}]);

graphsControllers.controller('DateFromSelectionController', ['$scope', 'formService', 'dateUtils', 
    function($scope, formService, dateUtils) {

    this.dateFromId = 'dateFromSelect';
    var that = this;

    this.onDateChange = function(date) {
        if(dateUtils.isDateValid(date)){
            $scope.$emit(graphMngmtFormEvents.onDateFromSelected, date);
        }     
    };

    $scope.$on(graphMngmtFormEvents.onSelectDataFormCleared, function(event)
    {
        formService.clearDatePickerField(jQuery("#" + that.dateFromId));
    });
}]);

graphsControllers.controller('DateToSelectionController', ['$scope', 'formService', 'dateUtils', 
    function($scope, formService, dateUtils) {

    this.dateToId = 'dateToId';
    var that = this;

    this.onDateChange = function(date) {
        if(dateUtils.isDateValid(date))
        {
             $scope.$emit(graphMngmtFormEvents.onDateToSelected, date);
        }
    };

    $scope.$on(graphMngmtFormEvents.onSelectDataFormCleared, function(event)
    {
        formService.clearDatePickerField(jQuery("#" + that.dateToId));
    });
}]);

graphsControllers.controller('GraphMainController', ['$scope', 'assessmentService', '$filter', 'numberUtils', 'formatService', 'appConfig', '$timeout',
    function($scope, assessmentService, $filter, numberUtils, formatService, appConfig, $timeout) {
    
    $scope.accordionStatus = {
        dataSelection: true,
        timeLine: false,
        radarChart: false
    };

    $scope.animal = {
        id : appConfig.config.unassignedId
    };
    $scope.dateFrom = "";
    $scope.dateTo = "";

    $scope.animalAssessmentData = {
        assessments: []
    };
    $scope.animalAssessmentTimeLineData = {};
    $scope.animalAssessmentTimeLineSelected = {};
    $scope.animalAssessmentRadarChartData = [];

    $scope.assessmentParametersSelected = [];

    this.errors = [];

    var that = this;

    $scope.$on(graphMngmtFormEvents.onAnimalSelected, function(event, animal) {
        $scope.animal = animal;
    });
    $scope.$on(graphMngmtFormEvents.onDateFromSelected, function(event, dateFrom) {
        $scope.dateFrom = dateFrom;
    });
    $scope.$on(graphMngmtFormEvents.onDateToSelected, function(event, dateTo) {
        $scope.dateTo = dateTo;
    });
    $scope.$on(graphMngmtFormEvents.onAnimalRemoved, function(event, choice) {
        $scope.animal.id = appConfig.config.unassignedId;
        if (choice.id > 0) {
            $scope.animalAssessmentData = {};
            $scope.animalAssessmentTimeLineData = {};
            $scope.animalAssessmentRadarChartData = [];            
        }

        $timeout(function(){
            $scope.$apply();
        });
    });

    $scope.clearForm = function(){
        $scope.animal.id = appConfig.config.unassignedId;
        $scope.dateFrom = "";
        $scope.dateTo = "";
        that.errors = [];
        $scope.$broadcast(graphMngmtFormEvents.onSelectDataFormCleared);
    };

    $scope.getTimeLineData = function() {
        that.errors = [];
        $scope.animalAssessmentTimeLineData = {};
        var pagingOptions = {};
        var isPageable = false;
        assessmentService.getAnimalAssessmentsBetween($scope.animal.id, $scope.dateFrom, $scope.dateTo,
            getAnimalAssessmentTimeLineDataSuccess, getAnimalAssessmentTimeLineDataError, pagingOptions, isPageable);
    };
    var getAnimalAssessmentTimeLineDataError = function(errors) {
        that.errors = errors;
    };
    var getAnimalAssessmentTimeLineDataSuccess = function(data) {
        $scope.animalAssessmentData = data;
        _roundData(data);
        $scope.animalAssessmentTimeLineData = formatService.getTimelineFormatFromAssessments(data);
        $scope.accordionStatus.dataSelection = false;
        $scope.accordionStatus.timeLine = true;
    };

    var _roundData = function(data){
        angular.forEach(data.assessments, function(assessmentData, key){
            angular.forEach(assessmentData.assessmentParameters, function(assessmentParameter, key){
               assessmentParameter.parameterAverage = Math.round(assessmentParameter.parameterAverage*100)/100;
            });
        });
    };

    $scope.getCumulativeWelfareAssessmentScore = function(assessment){
        return numberUtils.getCumulativeWelfareAssessmentScore(assessment);
    };

    $scope.getParametersAverageScore = function(assessment){
        var numbersToAverage = [];
        if(!angular.equals(assessment, {})){
            for (var i = 0,j = assessment.assessmentParameters.length; i < j; i++) {
                numbersToAverage.push(assessment.assessmentParameters[i].parameterAverage);
            }
        }        
        return numberUtils.getAverage(numbersToAverage);
    };

    $scope.timeLineClickCallback = function(data) {
        $scope.assessmentParametersSelected = [];
        var animalAssessmentData = _getAnimalAssessmentDataById(data.id);
        $scope.animalAssessmentTimeLineSelected = animalAssessmentData;
        
        if (animalAssessmentData) {
            _addanimalAssessmentRadarChartData(animalAssessmentData);
            $timeout(function(){
                $scope.$apply();
            });
        }
        
        $scope.accordionStatus.radarChart = true;

        $timeout(function(){
            $scope.$apply();
        });
    };

    $scope.timeLineHoverCallback = function(data) {        
    };

    $scope.radarChartClickCallback = function(data) {
        var assessmentParameter = _getAssessmentParameterById(data.id);
        if(assessmentParameter){
            _addAssessmentParametersSelected(assessmentParameter);
        }

        $timeout(function(){
            $scope.$apply();
        });
    };

    $scope.removeParameter = function(parameter){
        _removeAssessmentParameterById(parameter.parameterId);
    };

    $scope.removeAllParameters = function(){
        $scope.assessmentParametersSelected = [];
    };

    var _getAnimalAssessmentDataById = function(assessmentId) {
        var animalAssessmentFilterBy = {};
        animalAssessmentFilterBy.assessmentId = assessmentId;
        var filteredAnimalAssessment = $filter('filter')($scope.animalAssessmentData.assessments, animalAssessmentFilterBy);
        return filteredAnimalAssessment[0];
    };

    var _addanimalAssessmentRadarChartData = function(assessment) {
        if ($scope.animalAssessmentRadarChartData.length > 0) {
            $scope.animalAssessmentRadarChartData = [];
        }
        $scope.animalAssessmentRadarChartData.push(formatService.getRadarFormatFromAssessment(assessment))
    };

    var _addAnimalAssessmentTimeLineSelected = function(assessment){
        $scope.animalAssessmentTimeLineSelected = assessment;
    };

    var _getAssessmentParameterById = function(parameterId){

        var assessmentParameterFilterBy = {};
        assessmentParameterFilterBy.parameterId = parameterId;
        var filteredAssessmentParameter = $filter('filter')($scope.animalAssessmentTimeLineSelected.assessmentParameters, assessmentParameterFilterBy);
        return filteredAssessmentParameter[0];
    };

    var _addAssessmentParametersSelected = function(parameter){
        var filteredAssessmentParameter = $filter('filter')($scope.assessmentParametersSelected, parameter);
        if(filteredAssessmentParameter.length === 0)
        {
            $scope.assessmentParametersSelected.push(parameter);
        } 
    };

    var _removeAssessmentParameterById = function(parameterId){
        for (var i = $scope.assessmentParametersSelected.length - 1; i >= 0; i--) {
            if($scope.assessmentParametersSelected[i].parameterId===parameterId)
            {
                $scope.assessmentParametersSelected.splice(i, 1);
            }
        };
    };
}]);