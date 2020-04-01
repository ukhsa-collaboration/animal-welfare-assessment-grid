var manageHousingControllers = angular.module('manageHousingControllers', ['appConfigModule', 'awCommonDirectives', 'housingServices', 'formServices']);

var housingMngmtFormEvents = {
    onExistingHousingSelected : "onExistingHousingSelected",
    onNewHousingSelected : "onNewHousingSelected",
    onHousingClearSelected : "onHousingClearSelected",
    onHousingRemoved : "onHousingRemoved",
    onHousingRemovedClearUpdate : "onHousingRemovedClearUpdate",
    onHousingUpdateStarted : "onHousingUpdateStarted",
    onHousingUpdateFinished : "onHousingUpdateFinished",
    onHousingUpdated : "onHousingUpdated",
    onHousingUploadSelected : "onHousingUploadSelected",
    onHousingUploadClearSelected : "onHousingUploadClearSelected"
};

manageHousingControllers.controller('MngmtHousingSelectionCtrl', ['$scope', 'formService', 'housingService', 'appConfig',
function($scope, formService, housingService, appConfig)
{
    this.selectId = "housingSelect";
    this.placeHolder = "Create/Search a housing...";

    var that = this;

    this.onSearchSelect2DirectiveLinked = function()
    {
        formService.initSearchSelect2(jQuery("#" + this.selectId), housingService.getHousingsLike, true, this.placeHolder);
    };

    this.onSearchSelect2Selecting = function(choice)
    {
        if (choice.id === appConfig.config.unassignedId) {
            $scope.$emit(housingMngmtFormEvents.onNewHousingSelected, choice);
        }
        else {
            $scope.$emit(housingMngmtFormEvents.onExistingHousingSelected, choice);
        }
    };

    this.onSearchSelect2Removed = function(choice)
    {
        $scope.$emit(housingMngmtFormEvents.onHousingClearSelected, choice);
    };

    $scope.$on(housingMngmtFormEvents.onHousingRemovedClearUpdate, function(event)
    {
        formService.clearSelect2(jQuery("#" + that.selectId));
    });

    $scope.$on(housingMngmtFormEvents.onHousingUpdateStarted, function(event, housing)
    {
        formService.setSelect2Val([{id : housing.housingId, text : housing.housingName, locked : true}], jQuery("#" + that.selectId));
    });

    $scope.$on(housingMngmtFormEvents.onHousingUpdated, function(event, housing)
    {
        formService.setSelect2Val([{id : housing.housingId, text : housing.housingName, locked : false}], jQuery("#" + that.selectId));
    });
}]);

manageHousingControllers.controller('MngmtHousingSelectionUpdateCtrl', ['$scope', 'formService',
function($scope, formService)
{
    this.selectId = "housingUpdateSelect";
    this.placeHolder = "Update the housing to...";
    var that = this;

    this.onSelect2DirectiveLinked = function()
    {
        formService.initSelect2([], jQuery("#" + this.selectId), this.placeHolder);
    };

    this.onSelect2Change = function(val)
    {
        $scope.$emit(housingMngmtFormEvents.onHousingUpdateFinished, val[0]);
        formService.clearSelect2(jQuery("#" + that.selectId));
    };
}]);

manageHousingControllers.controller('MngmtHousingManagementCtrl', ['$scope', 'appConfig', 'housingService', '$timeout',
function($scope, appConfig, housingService, $timeout)
{
    this.housing = {};
    this.isNewHousing = true;
    this.isUpdatingHousing = false;
    this.isSuccess = false;
    this.errors = [];
    var that = this;

    this.resetHousingContainer = function()
    {
        this.housing = {
            housingId : appConfig.config.unassignedId,
            housingName : undefined
        };
        this.errors = [];
    };

    this.setHousingId = function(id)
    {
        this.housing.housingId = id;
    };

    this.setHousingName = function(name)
    {
        this.housing.housingName = name;
    };
    
    this.resetErrors = function()
    {
        this.errors = [];
    };

    this.onNewHousing = function(choice)
    {
        this.isSuccess = false;
        this.isNewHousing = true;
        this.setHousingName(choice.text);
        $timeout(function(){
            $scope.$apply();
        });
    };

    this.onExistingHousing = function(choice)
    {
        this.isSuccess = false;
        this.isNewHousing = false;
        this.setHousingId(choice.id);
        this.setHousingName(choice.text);
        $timeout(function(){
            $scope.$apply();
        });
    };

    this.onClearHousing = function(choice)
    {
        this.isSuccess = false;
        this.resetErrors();     
        this.isNewHousing = true;
        this.setHousingId(appConfig.config.unassignedId);
        this.setHousingName(undefined);
        $timeout(function(){
            $scope.$apply();
            $scope.$broadcast(housingMngmtFormEvents.onHousingRemovedClearUpdate);
        });
    };

    this.startUpdate = function()
    {
        this.isUpdatingHousing = true;
        $scope.$broadcast(housingMngmtFormEvents.onHousingUpdateStarted, this.housing);
    };

    this.finishUpdate = function(housingName)
    {
        this.setHousingName(housingName);
        this.isUpdatingHousing = false;
        $timeout(function(){
            $scope.$apply();
            $scope.$broadcast(housingMngmtFormEvents.onHousingUpdated, that.housing);
        });
    };

    this.cancelUpdate = function()
    {
        this.isUpdatingHousing = false;
        $timeout(function(){
            $scope.$apply();
            $scope.$broadcast(housingMngmtFormEvents.onHousingUpdated, that.housing);
        });
    };

    this.clearHousing = function()
    {
        this.isSuccess = false;
        this.isUpdatingHousing = false;
        this.resetErrors();     
        this.isNewHousing = true;
        this.resetHousingContainer();
        $scope.$broadcast(housingMngmtFormEvents.onHousingRemovedClearUpdate);
    };

    this.submitHousing = function()
    {
        housingService.saveHousing(this.housing, submitHousingCallback, onErrorCallBack);
    };

    this.updateHousing = function()
    {
        housingService.updateHousing(this.housing, updateHousingCallback, onErrorCallBack);
    };
    
    var onErrorCallBack = function(errors)
    {
        that.isSuccess = false;
        that.errors = errors;
    };

    var submitHousingCallback = function(data)
    {
        that.isSuccess = true;
        that.resetErrors();
        that.isNewHousing = false;
        that.setHousingId(data.housingId);
    };

    var updateHousingCallback = function(data)
    {
        that.isSuccess = true;
        that.resetErrors();
    };

    var removeHousingCallback = function(data)
    {
        that.isSuccess = true;
        that.isNewHousing = true;
        that.resetHousingContainer();
        $scope.$broadcast(housingMngmtFormEvents.onHousingRemoved);
    };

    $scope.$on(housingMngmtFormEvents.onNewHousingSelected, function(event, choice)
    {
        that.onNewHousing(choice);
    });

    $scope.$on(housingMngmtFormEvents.onExistingHousingSelected, function(event, choice)
    {
        that.onExistingHousing(choice);
    });

    $scope.$on(housingMngmtFormEvents.onHousingClearSelected, function(event, choice)
    {
        that.onClearHousing(choice);
    });

    $scope.$on(housingMngmtFormEvents.onHousingUpdateFinished, function(event, choice)
    {
        that.finishUpdate(choice);
    });

    this.resetHousingContainer();
}]);

manageHousingControllers.controller('MngmtHousingUploadCtrl', ['$scope', 'appConfig', 'housingService', '$timeout',
function($scope, appConfig, housingService, $timeout)
{
    this.uploadId = "uploadFile";
    this.uploadElement = null;
    var that = this;

    this.onFileUploadChange = function(event)
    {
        that.uploadElement = event.target;
        $scope.$emit(housingMngmtFormEvents.onHousingUploadSelected, event.target.files);
    };

    $scope.$on(housingMngmtFormEvents.onHousingUploadClearSelected, function(event)
    {
        that.uploadElement.value = null;
    });    

}]);

manageHousingControllers.controller('MngmtHousingUploadManagementCtrl', ['$scope', 'appConfig', 'housingService', '$timeout',
function($scope, appConfig, housingService, $timeout)
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

    var uploadHousingCallback = function(data)
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

    this.uploadHousing = function()
    {
        var fd = new FormData();
        fd.append('file', this.fileList[0]);

        housingService.uploadHousing(fd, uploadHousingCallback, onErrorCallBack);
    };

    this.clearUploadHousing = function()
    {
        this.fileList = {};
        this.setDisabled(true);
        this.resetErrors();
        $scope.$broadcast(housingMngmtFormEvents.onHousingUploadClearSelected);        
    };

    $scope.$on(housingMngmtFormEvents.onHousingUploadSelected, function(event, fileList)
    {
        that.setUploadFiles(fileList);
    });
    
}]);