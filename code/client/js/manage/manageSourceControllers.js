var manageSourceControllers = angular.module('manageSourceControllers', ['appConfigModule', 'awCommonDirectives', 'sourceServices', 'formServices']);

var sourceMngmtFormEvents = {
    onExistingSourceSelected : "onExistingSourceSelected",
    onNewSourceSelected : "onNewSourceSelected",
    onSourceClearSelected : "onSourceClearSelected",
    onSourceRemoved : "onSourceRemoved",
    onSourceRemovedClearUpdate : "onSourceRemovedClearUpdate",
    onSourceUpdateStarted : "onSourceUpdateStarted",
    onSourceUpdateFinished : "onSourceUpdateFinished",
    onSourceUpdated : "onSourceUpdated"
};

manageSourceControllers.controller('MngmtSourceSelectionCtrl', ['$scope', 'formService', 'sourceService', 'appConfig',
function($scope, formService, sourceService, appConfig)
{
    this.selectId = "sourceSelect";
    this.placeHolder = "Create/Search a source...";

    var that = this;

    this.onSearchSelect2DirectiveLinked = function()
    {
        formService.initSearchSelect2(jQuery("#" + this.selectId), sourceService.getSourcesLike, true, this.placeHolder);
    };

    this.onSearchSelect2Selecting = function(choice)
    {
        if (choice.id === appConfig.config.unassignedId) {
            $scope.$emit(sourceMngmtFormEvents.onNewSourceSelected, choice);
        }
        else {
            $scope.$emit(sourceMngmtFormEvents.onExistingSourceSelected, choice);
        }
    };

    this.onSearchSelect2Removed = function(choice)
    {
        $scope.$emit(sourceMngmtFormEvents.onSourceClearSelected, choice);
    };

    $scope.$on(sourceMngmtFormEvents.onSourceRemovedClearUpdate, function(event)
    {
        formService.clearSelect2(jQuery("#" + that.selectId));
    });

    $scope.$on(sourceMngmtFormEvents.onSourceUpdateStarted, function(event, source)
    {
        formService.setSelect2Val([{id : source.sourceId, text : source.sourceName, locked : true}], jQuery("#" + that.selectId));
    });

    $scope.$on(sourceMngmtFormEvents.onSourceUpdated, function(event, source)
    {
        formService.setSelect2Val([{id : source.sourceId, text : source.sourceName, locked : false}], jQuery("#" + that.selectId));
    });
}]);

manageSourceControllers.controller('MngmtSourceSelectionUpdateCtrl', ['$scope', 'formService',
function($scope, formService)
{
    this.selectId = "sourceUpdateSelect";
    this.placeHolder = "Update the source to...";
    var that = this;

    this.onSelect2DirectiveLinked = function()
    {
        formService.initSelect2([], jQuery("#" + this.selectId), this.placeHolder);
    };

    this.onSelect2Change = function(val)
    {
        $scope.$emit(sourceMngmtFormEvents.onSourceUpdateFinished, val[0]);
        formService.clearSelect2(jQuery("#" + that.selectId));
    };
}]);

manageSourceControllers.controller('MngmtSourceManagementCtrl', ['$scope', 'appConfig', 'sourceService', '$timeout',
function($scope, appConfig, sourceService, $timeout)
{
    this.source = {};
    this.isNewSource = true;
    this.isUpdatingSource = false;
    this.isSuccess = false;
    this.errors = [];
    var that = this;

    this.resetSourceContainer = function()
    {
        this.source = {
            sourceId : appConfig.config.unassignedId,
            sourceName : undefined,
            sourceFactors: []
        };
        this.errors = [];
    };

    this.setSourceId = function(id)
    {
        this.source.sourceId = id;
    };

    this.setSourceName = function(name)
    {
        this.source.sourceName = name;
    };
    
    this.resetErrors = function()
    {
        this.errors = [];
    };

    this.onNewSource = function(choice)
    {
        this.isSuccess = false;
        this.isNewSource = true;
        this.setSourceName(choice.text);
        $timeout(function(){
            $scope.$apply();
        });
    };

    this.onExistingSource = function(choice)
    {
        this.isSuccess = false;
        this.isNewSource = false;
        this.setSourceId(choice.id);
        this.setSourceName(choice.text);
        $timeout(function(){
            $scope.$apply();
        });
    };

    this.onClearSource = function(choice)
    {
        this.isSuccess = false;
        this.resetErrors();     
        this.isNewSource = true;
        this.setSourceId(appConfig.config.unassignedId);
        this.setSourceName(undefined);
        $timeout(function(){
            $scope.$apply();
            $scope.$broadcast(sourceMngmtFormEvents.onSourceRemovedClearUpdate);
        });
    };

    this.startUpdate = function()
    {
        this.isUpdatingSource = true;
        $scope.$broadcast(sourceMngmtFormEvents.onSourceUpdateStarted, this.source);
    };

    this.finishUpdate = function(sourceName)
    {
        this.setSourceName(sourceName);
        this.isUpdatingSource = false;
        $timeout(function(){
            $scope.$apply();
            $scope.$broadcast(sourceMngmtFormEvents.onSourceUpdated, that.source);
        });
    };

    this.cancelUpdate = function()
    {
        this.isUpdatingSource = false;
        $timeout(function(){
            $scope.$apply();
            $scope.$broadcast(sourceMngmtFormEvents.onSourceUpdated, that.source);
        });
    };

    this.clearSource = function()
    {
        this.isSuccess = false;
        this.isUpdatingSource = false;
        this.resetErrors();     
        this.isNewSource = true;
        this.resetSourceContainer();
        $scope.$broadcast(sourceMngmtFormEvents.onSourceRemovedClearUpdate);
    };

    this.submitSource = function()
    {
        sourceService.saveSource(this.source, submitSourceCallback, onErrorCallBack);
    };

    this.updateSource = function()
    {
        sourceService.updateSource(this.source, updateSourceCallback, onErrorCallBack);
    };
    
    var onErrorCallBack = function(errors)
    {
        that.isSuccess = false;
        that.errors = errors;
    }

    var submitSourceCallback = function(data)
    {
        that.isSuccess = true;
        that.resetErrors();
        that.isNewSource = false;
        that.setSourceId(data.sourceId);
    };

    var updateSourceCallback = function(data)
    {
        that.isSuccess = true;
        that.resetErrors();
    };

    var removeSourceCallback = function(data)
    {
        that.isSuccess = true;
        that.isNewSource = true;
        that.resetSourceContainer();
        $scope.$broadcast(sourceMngmtFormEvents.onSourceRemoved);
    };

    $scope.$on(sourceMngmtFormEvents.onNewSourceSelected, function(event, choice)
    {
        that.onNewSource(choice);
    });

    $scope.$on(sourceMngmtFormEvents.onExistingSourceSelected, function(event, choice)
    {
        that.onExistingSource(choice);
    });

    $scope.$on(sourceMngmtFormEvents.onSourceClearSelected, function(event, choice)
    {
        that.onClearSource(choice);
    });

    $scope.$on(sourceMngmtFormEvents.onSourceUpdateFinished, function(event, choice)
    {
        that.finishUpdate(choice);
    });

    this.resetSourceContainer();
}]);