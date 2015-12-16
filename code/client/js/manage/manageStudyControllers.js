var manageStudyControllers = angular.module('manageStudyControllers', ['appConfigModule', 'awCommonDirectives', 'studyServices', 'studyGroupServices', 'formServices']);

var studyMngmtFormEvents = {
    onExistingStudySelected: "onExistingStudySelected",
    onNewStudySelected: "onNewStudySelected",
    onStudyStudyGroupSelected : "onStudyStudyGroupSelected",
    onStudyStudyGroupRemoved : "onStudyStudyGroupRemoved",
    onStudyNameCleared : "onStudyNameCleared",
    onExistingStudyUpdateForm : "onExistingStudyUpdateForm",
    onStudyClearUpdateForm : "onStudyClearUpdateForm",
    onStudyUpdateStarted: "onStudyUpdateStarted",
    onStudyUpdateFinished: "onStudyUpdateFinished",
    onStudyUpdated: "onStudyUpdated",
    onStudyClearSelected: "onStudyClearSelected",
    onStudyRemovedClearUpdate: "onStudyRemovedClearUpdate"
};

manageStudyControllers.controller('MngmtStudySelectionCtrl', ['$scope', 'formService', 'studyService', 'appConfig',
    function($scope, formService, studyService, appConfig) {
        this.selectId = "StudySelect";
        this.placeHolder = "Create/Search a study...";

        var that = this;

        this.onSearchSelect2DirectiveLinked = function() {
            formService.initSearchSelect2(jQuery("#" + this.selectId), studyService.getStudiesLike, true, this.placeHolder);
        };

        this.onSearchSelect2Selecting = function(choice) {
            if (choice.id === appConfig.config.unassignedId) {
                $scope.$emit(studyMngmtFormEvents.onNewStudySelected, choice);
            } else {
                $scope.$emit(studyMngmtFormEvents.onExistingStudySelected, choice);
            }
        };

        this.onSearchSelect2Removed = function(choice) {
            $scope.$emit(studyMngmtFormEvents.onStudyNameCleared);
        };

        $scope.$on(studyMngmtFormEvents.onStudyClearUpdateForm, function(event) {
            formService.clearSelect2(jQuery("#" + that.selectId));
        });

        $scope.$on(studyMngmtFormEvents.onStudyUpdateStarted, function(event, study) {
            formService.setSelect2Val([{
                id: study.studyId,
                text: study.studyName,
                locked: true
            }], jQuery("#" + that.selectId));
        });

        $scope.$on(studyMngmtFormEvents.onStudyUpdated, function(event, study) {
            formService.setSelect2Val([{
                id: study.studyId,
                text: study.studyName,
                locked: false
            }], jQuery("#" + that.selectId));
        });
    }
]);

manageStudyControllers.controller('MngmtStudySelectionUpdateCtrl', ['$scope', 'formService',
    function($scope, formService) {
        this.selectId = "StudyUpdateSelect";
        this.placeHolder = "Update the study to...";
        var that = this;

        this.onSelect2DirectiveLinked = function() {
            formService.initSelect2([], jQuery("#" + this.selectId), this.placeHolder);
        };

        this.onSelect2Change = function(val) {
            $scope.$emit(studyMngmtFormEvents.onStudyUpdateFinished, val[0]);
            formService.clearSelect2(jQuery("#" + that.selectId));
        };
    }
]);

manageStudyControllers.controller('MngmtStudyStudyGroupCtrl', ['$scope', 'studyGroupService', 'formService',
    function($scope, studyGroupService, formService) {

        this.selectId = 'studyStudyGroupSelect';
        this.placeHolder = 'Search for study groups to add...';

        var that = this;

        this.onSearchSelect2DirectiveLinked = function() {
            formService.initSearchSelect2(jQuery('#' + this.selectId), studyGroupService.getStudyGroupsLike, false, this.placeHolder, null, true);
        };

        this.onSearchSelect2Selecting = function(choice) {
            $scope.$emit(studyMngmtFormEvents.onStudyStudyGroupSelected, choice);
        };

        this.onSearchSelect2Removed = function(choice) {
            $scope.$emit(studyMngmtFormEvents.onStudyStudyGroupRemoved, choice);
        };

        $scope.$on(studyMngmtFormEvents.onExistingStudyUpdateForm, function(event, studyGroups)
    	{
    		var results = [];
    		for(var i = studyGroups.length - 1; i >= 0; i--){
    			var studyGroup = studyGroups[i];
    			results.push({id : studyGroup.studyGroupId, text : studyGroup.studyGroupName });
    		};
        	formService.setSelect2Val(results, jQuery('#' + that.selectId));
    	});

    	$scope.$on(studyMngmtFormEvents.onStudyClearUpdateForm, function(event) {
            formService.clearSelect2(jQuery("#" + that.selectId));
        });
    }
]);

manageStudyControllers.controller('MngmtStudyManagementCtrl', ['$scope', 'studyService', 'studyGroupService', 'appConfig', '$timeout',
    function($scope, studyService, studyGroupService, appConfig, $timeout) {

        this.study = {};
        this.isExistingStudy = false;
        this.isSuccess = false;
        this.errors = [];
        this.isUpdatingStudy = false;

        this.accordionStatus = {
            isStudyAnimalsOpen: true,
            isStudyStudysOpen: false,
        };

        var that = this;

        this.resetStudyContainer = function() {
            this.study = {
                studyId: appConfig.config.unassignedId,
                studyName: undefined,
                isStudyOpen : false,
                studyGroups: []
            };
            this.isExistingStudy = false;
            this.isSuccess = false;
            this.errors = [];
            this.isUpdatingStudy = false;
        };

        this.setStudyName = function(name) {
            this.study.studyName = name;
        };

        this.setStudyId = function(id) {
            this.study.studyId = id;
        };

        this.onNewStudy = function(choice) {
            this.setStudyName(choice.text);
        };

        var getStudyCallback = function(data) {
            that.study = data;
            that.isExistingStudy = true;
            $scope.$broadcast(studyMngmtFormEvents.onExistingStudyUpdateForm, that.study.studyGroups);
        };

        this.getStudyById = function(id) {
            studyService.getStudy(id, getStudyCallback);
        };

        this.onStudyCleared = function() {

        };

        this.onExistingStudySelected = function(choice) {
            this.getStudyById(choice.id);
        };

        this.addStudyGroupToStudy = function(studyGroup){
			if (this.study.studyGroups.indexOf(studyGroup) === -1) {
                this.study.studyGroups.push(studyGroup);
            }
        };

        this.removeStudyGroupFromStudy = function(studyGroup){
        	this.study.studyGroups.splice(this.study.studyGroups.indexOf(studyGroup), 1);
        };

        this.onStudyStudyGroupSelected = function(choice) {
            var studyGroup = studyGroupService.getBasicStudyGroup(choice.id, choice.text);
            this.addStudyGroupToStudy(studyGroup);
        };

        this.onStudyStudyGroupRemoved = function(choice) {
            var studyGroup = studyGroupService.getBasicStudyGroup(choice.id, choice.text);
            this.removeStudyGroupFromStudy(studyGroup);
        };

        var onStudySubmitSuccess = function(data){
        	that.errors = [];
            that.isExistingStudy = true;
        	that.isSuccess = true;
        	that.study = data;
        };

        this.submitStudy = function(){
        	studyService.saveStudy(this.study, onStudySubmitSuccess, onErrorCallback);
        };

        var onStudyUpdateSuccess = function(data){
        	that.errors = [];
        	that.isSuccess = true;
            that.isExistingStudy = true;
        	that.study = data;
        };

        var onErrorCallback = function(errors){
        	that.isSuccess = false;
        	that.errors = errors;
        };

        this.updateStudy = function(){
        	studyService.updateStudy(this.study, onStudyUpdateSuccess, onErrorCallback);
        };

        this.clearStudy = function(){
        	this.isExistingStudy = false;
        	this.resetStudyContainer();
        	$scope.$broadcast(studyMngmtFormEvents.onStudyClearUpdateForm);
        };

        this.startUpdate = function(){
        	this.isUpdatingStudy = true;
        	$scope.$broadcast(studyMngmtFormEvents.onStudyUpdateStarted, this.study);
    	};

    	this.finishUpdate = function(StudyName){
        	this.setStudyName(StudyName);
        	this.isUpdatingStudy = false;
            $timeout(function(){
                $scope.$apply();
                $scope.$broadcast(studyMngmtFormEvents.onStudyUpdated, that.study);
            })
    	};

        this.cancelUpdate = function(){
            this.isUpdatingStudy = false;
            $scope.$broadcast(studyMngmtFormEvents.onStudyUpdated, this.study);
        };

    	this.onStudyNameCleared = function(){
    		this.setStudyName(undefined);
            this.isExistingStudy = false;
            $timeout(function(){
                $scope.$apply();
            });
    	};

        $scope.$on(studyMngmtFormEvents.onNewStudySelected, function(event, choice) {
            that.onNewStudy(choice);
        });

        $scope.$on(studyMngmtFormEvents.onExistingStudySelected, function(event, choice) {
            that.onExistingStudySelected(choice);
        });

        $scope.$on(studyMngmtFormEvents.onStudyStudyGroupSelected, function(event, choice) {
            that.onStudyStudyGroupSelected(choice);
        });

        $scope.$on(studyMngmtFormEvents.onStudyStudyGroupRemoved, function(event, choice) {
            that.onStudyStudyGroupRemoved(choice);
        });

        $scope.$on(studyMngmtFormEvents.onStudyUpdateFinished, function(event, choice) {
        	that.finishUpdate(choice);
    	});

    	$scope.$on(studyMngmtFormEvents.onStudyNameCleared, function(event) {
            that.onStudyNameCleared();
        });

        this.resetStudyContainer();
    }
]);