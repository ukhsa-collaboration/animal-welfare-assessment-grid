var manageReasonControllers = angular.module('manageReasonControllers', ['appConfigModule', 'awCommonDirectives', 'reasonServices', 'formServices']);

var reasonMngmtFormEvents = {
    onExistingReasonSelected : "onExistingReasonSelected",
    onNewReasonSelected : "onNewReasonSelected",
    onReasonClearSelected : "onReasonClearSelected",
    onReasonRemoved : "onReasonRemoved",
    onReasonRemovedClearUpdate : "onReasonRemovedClearUpdate",
    onReasonUpdateStarted : "onReasonUpdateStarted",
    onReasonUpdateFinished : "onReasonUpdateFinished",
    onReasonUpdated : "onReasonUpdated",
    onReasonUploadSelected : "onReasonUploadSelected",
    onReasonUploadClearSelected : "onReasonUploadClearSelected"
};

manageReasonControllers.controller('MngmtReasonSelectionCtrl', ['$scope', 'formService', 'reasonService', 'appConfig',
function($scope, formService, reasonService, appConfig)
{
    this.selectId = "reasonSelect";
    this.placeHolder = "Create/Search a reason...";

    var that = this;

    this.onSearchSelect2DirectiveLinked = function()
    {
        formService.initSearchSelect2(jQuery("#" + this.selectId), reasonService.getReasonsLike, true, this.placeHolder);
    };

    this.onSearchSelect2Selecting = function(choice)
    {
        if (choice.id === appConfig.config.unassignedId) {
            $scope.$emit(reasonMngmtFormEvents.onNewReasonSelected, choice);
        }
        else {
            $scope.$emit(reasonMngmtFormEvents.onExistingReasonSelected, choice);
        }
    };

    this.onSearchSelect2Removed = function(choice)
    {
        $scope.$emit(reasonMngmtFormEvents.onReasonClearSelected, choice);
    };

    $scope.$on(reasonMngmtFormEvents.onReasonRemovedClearUpdate, function(event)
    {
        formService.clearSelect2(jQuery("#" + that.selectId));
    });

    $scope.$on(reasonMngmtFormEvents.onReasonUpdateStarted, function(event, reason)
    {
        formService.setSelect2Val([{id : reason.reasonId, text : reason.reasonName, locked : true}], jQuery("#" + that.selectId));
    });

    $scope.$on(reasonMngmtFormEvents.onReasonUpdated, function(event, reason)
    {
        formService.setSelect2Val([{id : reason.reasonId, text : reason.reasonName, locked : false}], jQuery("#" + that.selectId));
    });
}]);

manageReasonControllers.controller('MngmtReasonSelectionUpdateCtrl', ['$scope', 'formService',
function($scope, formService)
{
    this.selectId = "reasonUpdateSelect";
    this.placeHolder = "Update the reason to...";
    var that = this;

    this.onSelect2DirectiveLinked = function()
    {
        formService.initSelect2([], jQuery("#" + this.selectId), this.placeHolder);
    };

    this.onSelect2Change = function(val)
    {
        $scope.$emit(reasonMngmtFormEvents.onReasonUpdateFinished, val[0]);
        formService.clearSelect2(jQuery("#" + that.selectId));
    };
}]);

manageReasonControllers.controller('MngmtReasonManagementCtrl', ['$scope', 'appConfig', 'reasonService', '$timeout',
function($scope, appConfig, reasonService, $timeout)
{
    this.reason = {};
    this.isNewReason = true;
    this.isUpdatingReason = false;
    this.isSuccess = false;
    this.errors = [];
    var that = this;

    this.resetReasonContainer = function()
    {
        this.reason = {
            reasonId : appConfig.config.unassignedId,
            reasonName : undefined
        };
        this.errors = [];
    };

    this.setReasonId = function(id)
    {
        this.reason.reasonId = id;
    };

    this.setReasonName = function(name)
    {
        this.reason.reasonName = name;
    };
    
    this.resetErrors = function()
    {
        this.errors = [];
    };

    this.onNewReason = function(choice)
    {
        this.isSuccess = false;
        this.isNewReason = true;
        this.setReasonName(choice.text);
        $timeout(function(){
            $scope.$apply();
        });
    };

    this.onExistingReason = function(choice)
    {
        this.isSuccess = false;
        this.isNewReason = false;
        this.setReasonId(choice.id);
        this.setReasonName(choice.text);
        $timeout(function(){
            $scope.$apply();
        });
    };

    this.onClearReason = function(choice)
    {
        this.isSuccess = false;
        this.resetErrors();     
        this.isNewReason = true;
        this.setReasonId(appConfig.config.unassignedId);
        this.setReasonName(undefined);
        $timeout(function(){
            $scope.$apply();
            $scope.$broadcast(reasonMngmtFormEvents.onReasonRemovedClearUpdate);
        });
    };

    this.startUpdate = function()
    {
        this.isUpdatingReason = true;
        $scope.$broadcast(reasonMngmtFormEvents.onReasonUpdateStarted, this.reason);
    };

    this.finishUpdate = function(reasonName)
    {
        this.setReasonName(reasonName);
        this.isUpdatingReason = false;
        $timeout(function(){
            $scope.$apply();
            $scope.$broadcast(reasonMngmtFormEvents.onReasonUpdated, that.reason);
        });
    };

    this.cancelUpdate = function()
    {
        this.isUpdatingReason = false;
        $timeout(function(){
            $scope.$apply();
            $scope.$broadcast(reasonMngmtFormEvents.onReasonUpdated, that.reason);
        });
    };

    this.clearReason = function()
    {
        this.isSuccess = false;
        this.isUpdatingReason = false;
        this.resetErrors();     
        this.isNewReason = true;
        this.resetReasonContainer();
        $scope.$broadcast(reasonMngmtFormEvents.onReasonRemovedClearUpdate);
    };

    this.submitReason = function()
    {
        reasonService.saveReason(this.reason, submitReasonCallback, onErrorCallBack);
    };

    this.updateReason = function()
    {
        reasonService.updateReason(this.reason, updateReasonCallback, onErrorCallBack);
    };
    
    var onErrorCallBack = function(errors)
    {
        that.isSuccess = false;
        that.errors = errors;
    };

    var submitReasonCallback = function(data)
    {
        that.isSuccess = true;
        that.resetErrors();
        that.isNewReason = false;
        that.setReasonId(data.reasonId);
    };

    var updateReasonCallback = function(data)
    {
        that.isSuccess = true;
        that.resetErrors();
    };

    var removeReasonCallback = function(data)
    {
        that.isSuccess = true;
        that.isNewReason = true;
        that.resetReasonContainer();
        $scope.$broadcast(reasonMngmtFormEvents.onReasonRemoved);
    };

    $scope.$on(reasonMngmtFormEvents.onNewReasonSelected, function(event, choice)
    {
        that.onNewReason(choice);
    });

    $scope.$on(reasonMngmtFormEvents.onExistingReasonSelected, function(event, choice)
    {
        that.onExistingReason(choice);
    });

    $scope.$on(reasonMngmtFormEvents.onReasonClearSelected, function(event, choice)
    {
        that.onClearReason(choice);
    });

    $scope.$on(reasonMngmtFormEvents.onReasonUpdateFinished, function(event, choice)
    {
        that.finishUpdate(choice);
    });

    this.resetReasonContainer();
}]);

manageReasonControllers.controller('MngmtReasonUploadCtrl', ['$scope', 'appConfig', 'reasonService', '$timeout',
function($scope, appConfig, reasonService, $timeout)
{
    this.uploadId = "uploadFile";
    this.uploadElement = null;
    var that = this;

    this.onFileUploadChange = function(event)
    {
        that.uploadElement = event.target;
        $scope.$emit(reasonMngmtFormEvents.onReasonUploadSelected, event.target.files);
    };

    $scope.$on(reasonMngmtFormEvents.onReasonUploadClearSelected, function(event)
    {
        that.uploadElement.value = null;
    });    

}]);

manageReasonControllers.controller('MngmtReasonUploadManagementCtrl', ['$scope', 'appConfig', 'reasonService', '$timeout',
function($scope, appConfig, reasonService, $timeout)
{
    this.fileList = {};
    this.isSuccess = false;
    this.errors = [];
    this.isFileSelected = true;
    var that = this;

    this.resetErrors = function()
    {
        this.errors = [];
    };

    var onErrorCallBack = function(errors)
    {
        that.isSuccess = false;
        that.errors = errors;
    };

    var uploadReasonCallback = function(data)
    {
        that.isSuccess = true;
        that.resetErrors();

    };

    this.setDisabled = function(isDisabled)
    {
        this.isFileSelected = isDisabled;
        $timeout(function(){
            $scope.$apply();
        });
    };


    this.setUploadFiles = function(fileList) 
    {
        Object.assign(this.fileList, fileList);
        that.setDisabled(false);
    };

    this.uploadReason = function()
    {
        var fd = new FormData();
        fd.append('file', this.fileList[0]);

        reasonService.uploadReason(fd, uploadReasonCallback, onErrorCallBack);
    };

    this.clearUploadReason = function()
    {
        this.fileList = {};
        this.setDisabled(true);
        this.resetErrors();
        $scope.$broadcast(reasonMngmtFormEvents.onReasonUploadClearSelected);        
    };

    $scope.$on(reasonMngmtFormEvents.onReasonUploadSelected, function(event, fileList)
    {
        that.setUploadFiles(fileList);
    });
    
}]);