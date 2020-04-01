var manageFactorControllers = angular.module('manageFactorControllers', ['appConfigModule', 'awCommonDirectives', 'factorServices', 'formServices']);

var factorMngmtFormEvents = {
    onExistingFactorSelected: "onExistingFactorSelected",
    onNewFactorSelected: "onNewFactorSelected",
    onFactorClearSelected: "onFactorClearSelected",
    onFactorRemoved: "onFactorRemoved",
    onFactorRemovedClearUpdate: "onFactorRemovedCl",
    onFactorUpdateStarted: "onFactorUpdateStarted",
    onFactorUpdateFinished: "onFactorUpdateFinished",
    onFactorUpdated: "onFactorUpdated",
    onFactorUploadSelected: "onFactorUploadSelected",
    onFactorUploadClearSelected: "onFactorUploadClearSelected"
};

manageFactorControllers.controller('MngmtFactorSelectionCtrl', ['$scope', 'formService', 'factorService', 'appConfig',
    function ($scope, formService, factorService, appConfig) {
        this.selectId = "factorSelect";
        this.placeHolder = "Create/Search a factor...";

        var that = this;

        this.onSearchSelect2DirectiveLinked = function () {
            formService.initSearchSelect2(jQuery("#" + this.selectId), factorService.getFactorsLike, true, this.placeHolder);
        };

        this.onSearchSelect2Selecting = function (choice) {
            if (choice.id === appConfig.config.unassignedId) {
                $scope.$emit(factorMngmtFormEvents.onNewFactorSelected, choice);
            } else {
                this.getFactorById(choice.id);
            }
        };

        this.onSearchSelect2Removed = function (choice) {
            $scope.$emit(factorMngmtFormEvents.onFactorClearSelected, choice);
        };

        this.getFactorById = function(factorId)
        {
            factorService.getFactorById(getFactorByIdCallback, factorId);
        };

        var getFactorByIdCallback = function(factor)
        {
            if (factor) {
                $scope.$emit(factorMngmtFormEvents.onExistingFactorSelected, factor);
            }
        };

        $scope.$on(factorMngmtFormEvents.onFactorRemovedClearUpdate, function (event) {
            formService.clearSelect2(jQuery("#" + that.selectId));
        });

        $scope.$on(factorMngmtFormEvents.onFactorUpdateStarted, function (event, factor) {
            formService.setSelect2Val([{
                id: factor.factorId,
                text: factor.factorName,
                locked: true
            }], jQuery("#" + that.selectId));
        });

        $scope.$on(factorMngmtFormEvents.onFactorUpdated, function (event, factor) {
            formService.setSelect2Val([{
                id: factor.factorId,
                text: factor.factorName,
                locked: false
            }], jQuery("#" + that.selectId));
        });
    }
]);

manageFactorControllers.controller('MngmtFactorSelectionUpdateCtrl', ['$scope', 'formService',
    function ($scope, formService) {
        this.selectId = "factorUpdateSelect";
        this.placeHolder = "Update the factor to...";
        var that = this;

        this.onSelect2DirectiveLinked = function () {
            formService.initSelect2([], jQuery("#" + this.selectId), this.placeHolder);
        };

        this.onSelect2Change = function (val) {
            $scope.$emit(factorMngmtFormEvents.onFactorUpdateFinished, val[0]);
            formService.clearSelect2(jQuery("#" + that.selectId));
        };
    }
]);

manageFactorControllers.controller('MngmtFactorManagementCtrl', ['$scope', 'appConfig', 'factorService', '$timeout',
    function ($scope, appConfig, factorService, $timeout) {
        this.factor = {};
        this.isNewFactor = true;
        this.isUpdatingFactor = false;
        this.isSuccess = false;
        this.errors = [];
        var that = this;

        this.resetFactorContainer = function () {
            this.factor = {
                factorId: appConfig.config.unassignedId,
                factorName: undefined,
                factorDescription: undefined
            };
            this.errors = [];
        };

        this.setFactorId = function (id) {
            this.factor.factorId = id;
        };

        this.setFactorName = function (name) {
            this.factor.factorName = name;
        };

        this.setFactorDescription = function(description) {
            this.factor.factorDescription = description;
        }

        this.resetErrors = function () {
            this.errors = [];
        };

        this.onNewFactor = function (choice) {
            this.isSuccess = false;
            this.isNewFactor = true;
            this.setFactorName(choice.text);
            $timeout(function () {
                $scope.$apply();
            });
        };

        this.onExistingFactor = function (factor) {
            this.isSuccess = false;
            this.isNewFactor = false;
            this.setFactorId(factor.factorId);
            this.setFactorName(factor.factorName);
            this.setFactorDescription(factor.factorDescription);
            $timeout(function () {
                $scope.$apply();
            });
        };

        this.onClearFactor = function (choice) {
            this.isSuccess = false;
            this.resetErrors();
            this.isNewFactor = true;
            this.setFactorId(appConfig.config.unassignedId);
            this.setFactorName(undefined);
            this.setFactorDescription(undefined);
            $timeout(function () {
                $scope.$apply();
                $scope.$broadcast(factorMngmtFormEvents.onFactorRemovedClearUpdate);
            });
        };

        this.startUpdate = function () {
            this.isUpdatingFactor = true;
            $scope.$broadcast(factorMngmtFormEvents.onFactorUpdateStarted, this.factor);
        };

        this.finishUpdate = function (factorName) {
            this.setFactorName(factorName);
            this.isUpdatingFactor = false;
            $timeout(function () {
                $scope.$apply();
                $scope.$broadcast(factorMngmtFormEvents.onFactorUpdated, that.factor);
            });
        };

        this.cancelUpdate = function () {
            this.isUpdatingFactor = false;
            $timeout(function () {
                $scope.$apply();
                $scope.$broadcast(factorMngmtFormEvents.onFactorUpdated, that.factor);
            });
        };

        this.clearFactor = function () {
            this.isSuccess = false;
            this.isUpdatingFactor = false;
            this.resetErrors();
            this.isNewFactor = true;
            this.resetFactorContainer();
            $scope.$broadcast(factorMngmtFormEvents.onFactorRemovedClearUpdate);
        };

        this.submitFactor = function () {
            factorService.saveFactor(this.factor, submitFactorCallback, onErrorCallBack);
        };

        this.updateFactor = function () {
            factorService.updateFactor(this.factor, updateFactorCallback, onErrorCallBack);
        };

        var onErrorCallBack = function (errors) {
            that.isSuccess = false;
            that.errors = errors;
        };

        var submitFactorCallback = function (data) {
            that.isSuccess = true;
            that.resetErrors();
            that.isNewFactor = false;
            that.setFactorId(data.factorId);
        };

        var updateFactorCallback = function (data) {
            that.isSuccess = true;
            that.resetErrors();
        };

        var removeFactorCallback = function (data) {
            that.isSuccess = true;
            that.isNewFactor = true;
            that.resetFactorContainer();
            $scope.$broadcast(factorMngmtFormEvents.onFactorRemoved);
        };

        $scope.$on(factorMngmtFormEvents.onNewFactorSelected, function (event, choice) {
            that.onNewFactor(choice);
        });

        $scope.$on(factorMngmtFormEvents.onExistingFactorSelected, function (event, factor) {
            that.onExistingFactor(factor);
        });

        $scope.$on(factorMngmtFormEvents.onFactorClearSelected, function (event, choice) {
            that.onClearFactor(choice);
        });

        $scope.$on(factorMngmtFormEvents.onFactorUpdateFinished, function (event, choice) {
            that.finishUpdate(choice);
        });

        this.resetFactorContainer();
    }
]);

manageFactorControllers.controller('MngmtFactorUploadCtrl', ['$scope', 'appConfig', 'factorService', '$timeout',
    function ($scope, appConfig, factorService, $timeout) {
        this.uploadId = "uploadFile";
        this.uploadElement = null;
        var that = this;

        this.onFileUploadChange = function (event) {
            that.uploadElement = event.target;
            $scope.$emit(factorMngmtFormEvents.onFactorUploadSelected, event.target.files);
        };

        $scope.$on(factorMngmtFormEvents.onFactorUploadClearSelected, function (event) {
            that.uploadElement.value = null;
        });

    }
]);

manageFactorControllers.controller('MngmtFactorUploadManagementCtrl', ['$scope', 'appConfig', 'factorService', '$timeout',
    function ($scope, appConfig, factorService, $timeout) {
        this.fileList = {};
        this.isSuccess = false;
        this.errors = [];
        this.isFileSelected = true;
        var that = this;

        this.resetErrors = function () {
            this.errors = [];
        };

        var onErrorCallBack = function (errors) {
            that.isSuccess = false;
            that.errors = errors;
        };

        var uploadFactorCallback = function (data) {
            that.isSuccess = true;
            that.resetErrors();
        };

        this.setDisabled = function (isDisabled) {
            this.isFileSelected = isDisabled;
            $timeout(function () {
                $scope.$apply();
            });
        };

        this.setUploadFiles = function (fileList) {
            Object.assign(this.fileList, fileList);
            that.setDisabled(false);
        };

        this.uploadFactor = function () {
            var fd = new FormData();
            fd.append('file', this.fileList[0]);

            factorService.uploadFactor(fd, uploadFactorCallback, onErrorCallBack);
        };

        this.clearUploadFactor = function () {
            this.fileList = {};
            this.setDisabled(true);
            this.resetErrors();
            $scope.$broadcast(factorMngmtFormEvents.onFactorUploadClearSelected);
        };

        $scope.$on(factorMngmtFormEvents.onFactorUploadSelected, function (event, fileList) {
            that.setUploadFiles(fileList);
        });

    }
]);