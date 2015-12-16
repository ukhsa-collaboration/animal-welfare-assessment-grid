var manageParameterControllers = angular.module('manageParameterControllers', ['appConfigModule', 'awCommonDirectives', 'parameterServices', 'formServices']);

var parameterMngmtFormEvents = {
    onExistingParameterSelected : "onExistingParameterSelected",
    onNewParameterSelected : "onNewParameterSelected",
    onParameterClearSelected : "onParameterClearSelected",
    onParameterRemoved : "onParameterRemoved",
    onParameterRemovedClearUpdate : "onParameterRemovedClearUpdate",
    onParameterUpdateStarted : "onParameterUpdateStarted",
    onParameterUpdateFinished : "onParameterUpdateFinished",
    onParameterUpdated : "onParameterUpdated"
};

manageParameterControllers.controller('MngmtParameterSelectionCtrl', ['$scope', 'formService', 'parameterService', 'appConfig',
function($scope, formService, parameterService, appConfig)
{
    this.selectId = "parameterSelect";
    this.placeHolder = "Create/Search a parameter...";

    var that = this;

    this.onSearchSelect2DirectiveLinked = function()
    {
        formService.initSearchSelect2(jQuery("#" + this.selectId), parameterService.getParametersLike, true, this.placeHolder);
    };

    this.onSearchSelect2Selecting = function(choice)
    {
        if (choice.id === appConfig.config.unassignedId) {
            $scope.$emit(parameterMngmtFormEvents.onNewParameterSelected, choice);
        }
        else {
            $scope.$emit(parameterMngmtFormEvents.onExistingParameterSelected, choice);
        }
    };

    this.onSearchSelect2Removed = function(choice)
    {
        $scope.$emit(parameterMngmtFormEvents.onParameterClearSelected, choice);
    };

    $scope.$on(parameterMngmtFormEvents.onParameterRemovedClearUpdate, function(event)
    {
        formService.clearSelect2(jQuery("#" + that.selectId));
    });

    $scope.$on(parameterMngmtFormEvents.onParameterUpdateStarted, function(event, parameter)
    {
        formService.setSelect2Val([{id : parameter.parameterId, text : parameter.parameterName, locked : true}], jQuery("#" + that.selectId));
    });

    $scope.$on(parameterMngmtFormEvents.onParameterUpdated, function(event, parameter)
    {
        formService.setSelect2Val([{id : parameter.parameterId, text : parameter.parameterName, locked : false}], jQuery("#" + that.selectId));
    });
}]);

manageParameterControllers.controller('MngmtParameterSelectionUpdateCtrl', ['$scope', 'formService',
function($scope, formService)
{
    this.selectId = "parameterUpdateSelect";
    this.placeHolder = "Update the parameter to...";
    var that = this;

    this.onSelect2DirectiveLinked = function()
    {
        formService.initSelect2([], jQuery("#" + this.selectId), this.placeHolder);
    };

    this.onSelect2Change = function(val)
    {
        $scope.$emit(parameterMngmtFormEvents.onParameterUpdateFinished, val[0]);
        formService.clearSelect2(jQuery("#" + that.selectId));
    };
}]);

manageParameterControllers.controller('MngmtParameterManagementCtrl', ['$scope', 'appConfig', 'parameterService', '$timeout',
function($scope, appConfig, parameterService, $timeout)
{
    this.parameter = {};
    this.isNewParameter = true;
    this.isUpdatingParameter = false;
    this.isSuccess = false;
    this.errors = [];
    var that = this;

    this.resetParameterContainer = function()
    {
        this.parameter = {
            parameterId : appConfig.config.unassignedId,
            parameterName : undefined,
            parameterFactors: []
        };
        this.errors = [];
    };

    this.setParameterId = function(id)
    {
        this.parameter.parameterId = id;
    };

    this.setParameterName = function(name)
    {
        this.parameter.parameterName = name;
    };
    
    this.resetErrors = function()
    {
        this.errors = [];
    };

    this.onNewParameter = function(choice)
    {
        this.isSuccess = false;
        this.isNewParameter = true;
        this.setParameterName(choice.text);
        $timeout(function(){
            $scope.$apply();
        });
    };

    this.onExistingParameter = function(choice)
    {
        this.isSuccess = false;
        this.isNewParameter = false;
        this.setParameterId(choice.id);
        this.setParameterName(choice.text);
        $timeout(function(){
            $scope.$apply();
        });
    };

    this.onClearParameter = function(choice)
    {
        this.isSuccess = false;
        this.resetErrors();     
        this.isNewParameter = true;
        this.setParameterId(appConfig.config.unassignedId);
        this.setParameterName(undefined);
        $timeout(function(){
            $scope.$apply();
            $scope.$broadcast(parameterMngmtFormEvents.onParameterRemovedClearUpdate);
        });
    };

    this.startUpdate = function()
    {
        this.isUpdatingParameter = true;
        $scope.$broadcast(parameterMngmtFormEvents.onParameterUpdateStarted, this.parameter);
    };

    this.finishUpdate = function(parameterName)
    {
        this.setParameterName(parameterName);
        this.isUpdatingParameter = false;
        $timeout(function(){
            $scope.$apply();
            $scope.$broadcast(parameterMngmtFormEvents.onParameterUpdated, that.parameter);
        });
    };

    this.cancelUpdate = function()
    {
        this.isUpdatingParameter = false;
        $timeout(function(){
            $scope.$apply();
            $scope.$broadcast(parameterMngmtFormEvents.onParameterUpdated, that.parameter);
        });
    };

    this.clearParameter = function()
    {
        this.isSuccess = false;
        this.isUpdatingParameter = false;
        this.resetErrors();     
        this.isNewParameter = true;
        this.resetParameterContainer();
        $scope.$broadcast(parameterMngmtFormEvents.onParameterRemovedClearUpdate);
    };

    this.submitParameter = function()
    {
        parameterService.saveParameter(this.parameter, submitParameterCallback, onErrorCallBack);
    };

    this.updateParameter = function()
    {
        parameterService.updateParameter(this.parameter, updateParameterCallback, onErrorCallBack);
    };
    
    var onErrorCallBack = function(errors)
    {
        that.isSuccess = false;
        that.errors = errors;
    }

    var submitParameterCallback = function(data)
    {
        that.isSuccess = true;
        that.resetErrors();
        that.isNewParameter = false;
        that.setParameterId(data.parameterId);
    };

    var updateParameterCallback = function(data)
    {
        that.isSuccess = true;
        that.resetErrors();
    };

    var removeParameterCallback = function(data)
    {
        that.isSuccess = true;
        that.isNewParameter = true;
        that.resetParameterContainer();
        $scope.$broadcast(parameterMngmtFormEvents.onParameterRemoved);
    };

    $scope.$on(parameterMngmtFormEvents.onNewParameterSelected, function(event, choice)
    {
        that.onNewParameter(choice);
    });

    $scope.$on(parameterMngmtFormEvents.onExistingParameterSelected, function(event, choice)
    {
        that.onExistingParameter(choice);
    });

    $scope.$on(parameterMngmtFormEvents.onParameterClearSelected, function(event, choice)
    {
        that.onClearParameter(choice);
    });

    $scope.$on(parameterMngmtFormEvents.onParameterUpdateFinished, function(event, choice)
    {
        that.finishUpdate(choice);
    });

    this.resetParameterContainer();
}]);