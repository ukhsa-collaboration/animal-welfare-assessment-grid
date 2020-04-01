var importAssessmentControllers = angular.module('importAssessmentControllers', ['appConfigModule', 'awCommonDirectives', 'assessmentServices', 'formServices', 'templateServices']);

var importAssessmentMngmtFormEvents = {
    onAssessmentUploadSelected: "onAssessmentUploadSelected",
    onAssessmentUploadClearSelected: "onAssessmentUploadClearSelected",
    onAssessmentTemplateSelected: "onAssessmentTemplateSelected",
    onAssessmentTemplateCleared: "onAssessmentTemplateCleared",
};

importAssessmentControllers.controller('MngmtAssessmentUploadCtrl', ['$scope', 'appConfig', 'assessmentService', '$timeout',
    function ($scope, appConfig, assessmentService, $timeout) {
        this.uploadId = "uploadFile";
        this.uploadElement = null;
        this.disabled = false;
        var that = this;

        this.onFileUploadChange = function (event) {
            that.uploadElement = event.target;
            $scope.$emit(importAssessmentMngmtFormEvents.onAssessmentUploadSelected, event.target.files);
        };

        $scope.$on(importAssessmentMngmtFormEvents.onAssessmentUploadClearSelected, function (event) {
            that.uploadElement.value = null;
        });

    }
]);

importAssessmentControllers.controller('MngmtAssessmentUploadManagementCtrl', ['$scope', 'appConfig', 'assessmentService', 'templateService', '$timeout',
    function ($scope, appConfig, assessmentService, templateService, $timeout) {
        this.fileList = {};
        this.isSuccess = false;
        this.errors = [];
        this.isFileSelected = true;
        this.isAllowAssessmentUpload = true;
        this.assessmentTemplateId = undefined;

        var that = this;

        this.resetErrors = function () {
            this.errors = [];
        };

        var onErrorCallBack = function (errors) {
            that.isSuccess = false;
            that.errors = errors;
        };

        var uploadAssessmentCallback = function (data) {
            that.isSuccess = true;
            that.resetErrors();

        };

        this.setDisabled = function (isDisabled) {
            this.isFileSelected = (isDisabled && this.assessmentTemplateId != undefined);
            $timeout(function () {
                $scope.$apply();
            });
        };

        this.setUploadFiles = function (fileList) {
            Object.assign(this.fileList, fileList);
            that.setDisabled(false);
        };

        this.uploadAssessment = function () {
            var fd = new FormData();
            fd.append('id', this.assessmentTemplateId);
            fd.append('file', this.fileList[0]);

            assessmentService.uploadAssessment(fd, uploadAssessmentCallback, onErrorCallBack);
        };

        this.clearUploadAssessment = function () {
            this.fileList = {};
            this.assessmentTemplateId = undefined;
            this.setDisabled(true);
            this.resetErrors();
            $scope.$broadcast(importAssessmentMngmtFormEvents.onAssessmentUploadClearSelected);
            $scope.$broadcast(importAssessmentMngmtFormEvents.onAssessmentTemplateCleared);
        };

        this.setAssessmentTemplateId = function (assessmentTemplateId) {
            this.assessmentTemplateId = assessmentTemplateId;
        }

        var getTemplateCountCallback = function (data) {
            if (data && data.count > 0) { //   TODO test
                that.templateCount = data.count;
                that.isAllowAssessmentUpload = false;
            } else {
                that.isAllowAssessmentUpload = true;
            }
        };

        templateService.getTemplateCount(getTemplateCountCallback);

        $scope.$on(importAssessmentMngmtFormEvents.onAssessmentUploadSelected, function (event, fileList) {
            that.setUploadFiles(fileList);
        });

        $scope.$on(importAssessmentMngmtFormEvents.onAssessmentTemplateSelected, function (event, assessmentTemplateId) {
            that.setAssessmentTemplateId(assessmentTemplateId);
        });


    }
]);

importAssessmentControllers.controller('MngmtUploadAnimalAssessmentTemplateSelectionCtrl', ['templateService', 'assessmentService', 'formService', '$scope', 'select2Utils',
    function (templateService, assessmentService, formService, $scope, select2Utils) {
        this.selectId = "templateSelect";
        this.placeHolder = "Search for a template...";
        var that = this;

        this.onSearchSelect2DirectiveLinked = function () {
            formService.initSearchSelect2(jQuery("#" + this.selectId), templateService.getTemplatesLike, false, this.placeHolder);
        };

        this.onSearchSelect2Selecting = function (choice) {
            //console.log(JSON.stringify(choice));    //    TODO remove
            $scope.$emit(importAssessmentMngmtFormEvents.onAssessmentTemplateSelected, choice.id);
        };

        this.onSearchSelect2Removed = function () {};

        var getAssessmentCountByAnimalIdSuccessCallback = function (count) {
            if (count > 0) {
                that.assessmentCount = count;
            }

            if (that.animal) {
                formService.setSelect2Val([{
                    id: that.animal.assessmentTemplateId,
                    text: that.animal.assessmentTemplateName,
                    locked: false
                }], jQuery("#" + that.selectId));
            }
        };

        $scope.$on(importAssessmentMngmtFormEvents.onAssessmentTemplateCleared, function (event) {
            formService.clearSelect2(jQuery("#" + that.selectId));
        });

    }
]);