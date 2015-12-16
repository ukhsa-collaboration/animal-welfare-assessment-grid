var manageScaleControllers = angular.module('manageScaleControllers', ['appConfigModule', 'awCommonDirectives', 'scaleServices', 'formServices', 'select2UtilsModule']);

var scaleMngmtFormEvents = {
    onExistingScaleSelected : "onExistingScaleSelected",
    onNewScaleSelected : "onNewScaleSelected",
    onScaleClearSelected : "onScaleClearSelected",
    onScaleCleared : "onScaleCleared",
    onScaleRemovedClearUpdate : "onScaleRemovedClearUpdate",
    onScaleUpdateStarted : "onScaleUpdateStarted",
    onScaleUpdateFinished : "onScaleUpdateFinished",
    onScaleUpdated : "onScaleUpdated"
};

manageScaleControllers.controller('MngmtScaleSelectionCtrl', ['$scope', 'formService', 'scaleService', 'appConfig',
function($scope, formService, scaleService, appConfig)
{
    this.selectId = "scaleSelect";
    this.placeHolder = "Create/Search a Scale...";

    var that = this;

    this.onSearchSelect2DirectiveLinked = function()
    {
        formService.initSearchSelect2(jQuery("#" + this.selectId), scaleService.getScalesLike, true, this.placeHolder);
    };

    this.onSearchSelect2Selecting = function(choice)
    {
        if (choice.id === appConfig.config.unassignedId) {
            $scope.$emit(scaleMngmtFormEvents.onNewScaleSelected, choice);
        }
        else {
            this.getScaleById(choice.id);
        }
    };

    this.onSearchSelect2Removed = function(choice)
    {
        $scope.$emit(scaleMngmtFormEvents.onScaleClearSelected, choice);
    };

    this.getScaleById = function(scaleId)
    {
        scaleService.getScale(getScaleCallback, scaleId);
    };

    var getScaleCallback = function(scale)
    {
        if(scale)
        {
            $scope.$emit(scaleMngmtFormEvents.onExistingScaleSelected, scale);
        }
    };

    $scope.$on(scaleMngmtFormEvents.onScaleCleared, function(event)
    {
        formService.clearSelect2(jQuery("#" + that.selectId));
    });

    $scope.$on(scaleMngmtFormEvents.onScaleUpdateStarted, function(event, scale)
    {
        formService.setSelect2Val([{id : scale.scaleId, text : scale.scaleName, locked : true}], jQuery("#" + that.selectId));
    });

    $scope.$on(scaleMngmtFormEvents.onScaleUpdated, function(event, scale)
    {
        formService.setSelect2Val([{id : scale.scaleId, text : scale.scaleName, locked : false}], jQuery("#" + that.selectId));
    });
}]);

manageScaleControllers.controller('MngmtScaleSelectionUpdateCtrl', ['$scope', 'formService',
function($scope, formService)
{
    this.selectId = "scaleUpdateSelect";
    this.placeHolder = "Update the scale to...";
    var that = this;

    this.onSelect2DirectiveLinked = function()
    {
        formService.initSelect2([], jQuery("#" + this.selectId), this.placeHolder);
    };

    this.onSelect2Change = function(val)
    {
        $scope.$emit(scaleMngmtFormEvents.onScaleUpdateFinished, val[0]);
        formService.clearSelect2(jQuery("#" + that.selectId));
    };
}]);

manageScaleControllers.controller('MngmtScaleManagementCtrl', ['$scope', 'appConfig', 'scaleService', '$timeout',
function($scope, appConfig, scaleService, $timeout)
{
    this.scale = {};
    this.isNewScale = true;
    this.isUpdatingScale = false;
    this.isSuccess = false;
    this.errors = [];
    var that = this;

    this.resetScaleContainer = function()
    {
        this.scale = {
            scaleId : appConfig.config.unassignedId,
            scaleName : undefined,
            scaleMin : undefined,
            scaleMax : undefined
        };
        this.errors = [];
    };

    this.setScaleId = function(id)
    {
        this.scale.scaleId = id;
    };

    this.setScaleName = function(name)
    {
        this.scale.scaleName = name;
    };

    this.setScaleMin = function(min)
    {
        this.scale.scaleMin = min;
    }

    this.setScaleMax = function(max)
    {
        this.scale.scaleMax = max;
    }
    
    this.resetErrors = function()
    {
    	this.errors = [];
    };

    this.onNewScale = function(choice)
    {
    	this.isSuccess = false;
        this.isNewScale = true;
        this.setScaleName(choice.text);
    };

    this.onExistingScale = function(scale)
    {
    	this.isSuccess = false;
        this.isNewScale = false;        
        this.setScaleId(scale.scaleId);
        this.setScaleName(scale.scaleName);
        this.setScaleMin(scale.scaleMin);
        this.setScaleMax(scale.scaleMax);
    };

    this.onClearScale = function(choice)
    {
    	this.isSuccess = false;
    	this.resetErrors();
        this.isNewScale = true;
        this.setScaleId(appConfig.config.unassignedId);
        this.setScaleName(undefined);
        this.setScaleMin(undefined);
        this.setScaleMax(undefined);
        $timeout(function(){
            $scope.$apply();
            $scope.$broadcast(scaleMngmtFormEvents.onScaleRemovedClearUpdate);
        });
    };

    this.submitScale = function()
    {
        scaleService.saveScale(this.scale, submitScaleCallback, onErrorCallBack);
    };

    this.updateScale = function()
    {
        scaleService.updateScale(this.scale, updateScaleCallback, onErrorCallBack);
    };

    this.clearScale = function()
    {
        this.resetScaleContainer();
        this.isNewScale = true;
        this.isSuccess = false;
        this.resetErrors();
        $scope.$broadcast(scaleMngmtFormEvents.onScaleCleared);
    };

    this.startUpdate = function()
    {
        this.isUpdatingScale = true;
        $scope.$broadcast(scaleMngmtFormEvents.onScaleUpdateStarted, this.scale);
    };

    this.finishUpdate = function(scaleName)
    {
        this.setScaleName(scaleName);
        this.isUpdatingScale = false;
        $timeout(function(){
            $scope.$apply();
            $scope.$broadcast(scaleMngmtFormEvents.onScaleUpdated, that.scale);
        });
    };

    this.cancelUpdate = function()
    {
        this.isUpdatingScale = false;
        $timeout(function(){
            $scope.$apply();
            $scope.$broadcast(scaleMngmtFormEvents.onScaleUpdated, that.scale);
        });
    };
    
    var onErrorCallBack = function(errors)
    {
    	that.errors = errors;
    }

    var submitScaleCallback = function(data)
    {
    	that.isSuccess = true;
    	that.resetErrors();
        that.isNewScale = false;
        that.setScaleId(data.scaleId);
    };

    var updateScaleCallback = function(data)
  	{
  		that.isSuccess = true;
  		that.resetErrors();
  	};

    var removeScaleCallback = function(data)
    {
    	that.isSuccess = true;
        that.isNewScale = true;
        that.resetScaleContainer();
        $scope.$broadcast(scaleMngmtFormEvents.onScaleRemoved);
    };

    $scope.$on(scaleMngmtFormEvents.onNewScaleSelected, function(event, choice)
    {
        that.onNewScale(choice);
    });

    $scope.$on(scaleMngmtFormEvents.onExistingScaleSelected, function(event, choice)
    {
        that.onExistingScale(choice);
    });

    $scope.$on(scaleMngmtFormEvents.onScaleClearSelected, function(event, choice)
    {
        that.onClearScale(choice);
    });

    $scope.$on(scaleMngmtFormEvents.onScaleUpdateFinished, function(event, choice)
    {
        that.finishUpdate(choice);
    });

    this.resetScaleContainer();
}]);