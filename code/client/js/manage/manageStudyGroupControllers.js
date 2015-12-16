 var manageStudyGroupControllers = angular.module('manageStudyGroupControllers', ['appConfigModule', 'awCommonDirectives', 'studyGroupServices', 'formServices']);

var studyGroupMngmtFormEvents = {
    onExistingStudyGroupSelected: "onExistingStudyGroupSelected",
    onNewStudyGroupSelected: "onNewStudyGroupSelected",
    onStudyGroupAnimalSelected : "onStudyGroupAnimalSelected",
    onStudyGroupAnimalRemoved : "onStudyGroupAnimalRemoved",
    onStudyGroupNameCleared : "onStudyGroupNameCleared",
    onExistingStudyGroupUpdateForm : "onExistingStudyGroupUpdateForm",
    onStudyGroupClearUpdateForm : "onStudyGroupClearUpdateForm",
    onStudyGroupUpdateStarted: "onStudyGroupUpdateStarted",
    onStudyGroupUpdateFinished: "onStudyGroupUpdateFinished",
    onStudyGroupUpdated: "onStudyGroupUpdated",
    onStudyGroupClearSelected: "onStudyGroupClearSelected",
    onStudyGroupRemovedClearUpdate: "onStudyGroupRemovedClearUpdate"
};

manageStudyGroupControllers.controller('MngmtStudyGroupSelectionCtrl', ['$scope', 'formService', 'studyGroupService', 'appConfig',
    function($scope, formService, studyGroupService, appConfig) {
        this.selectId = "studyGroupSelect";
        this.placeHolder = "Create/Search a study group...";

        var that = this;

        this.onSearchSelect2DirectiveLinked = function() {
            formService.initSearchSelect2(jQuery("#" + this.selectId), studyGroupService.getStudyGroupsLike, true, this.placeHolder);
        };

        this.onSearchSelect2Selecting = function(choice) {
            if (choice.id === appConfig.config.unassignedId) {
                $scope.$emit(studyGroupMngmtFormEvents.onNewStudyGroupSelected, choice);
            } else {
                $scope.$emit(studyGroupMngmtFormEvents.onExistingStudyGroupSelected, choice);
            }
        };

        this.onSearchSelect2Removed = function(choice) {
            $scope.$emit(studyGroupMngmtFormEvents.onStudyGroupNameCleared);
        };

        $scope.$on(studyGroupMngmtFormEvents.onStudyGroupClearUpdateForm, function(event) {
            formService.clearSelect2(jQuery("#" + that.selectId));
        });

        $scope.$on(studyGroupMngmtFormEvents.onStudyGroupUpdateStarted, function(event, studyGroup) {
            formService.setSelect2Val([{
                id: studyGroup.studyGroupId,
                text: studyGroup.studyGroupName,
                locked: true
            }], jQuery("#" + that.selectId));
        });

        $scope.$on(studyGroupMngmtFormEvents.onStudyGroupUpdated, function(event, studyGroup) {
            formService.setSelect2Val([{
                id: studyGroup.studyGroupId,
                text: studyGroup.studyGroupName,
                locked: false
            }], jQuery("#" + that.selectId));
        });
    }
]);

manageStudyGroupControllers.controller('MngmtStudyGroupSelectionUpdateCtrl', ['$scope', 'formService',
    function($scope, formService) {
        this.selectId = "studyGroupUpdateSelect";
        this.placeHolder = "Update the studyGroup to...";
        var that = this;

        this.onSelect2DirectiveLinked = function() {
            formService.initSelect2([], jQuery("#" + this.selectId), this.placeHolder);
        };

        this.onSelect2Change = function(val) {
            $scope.$emit(studyGroupMngmtFormEvents.onStudyGroupUpdateFinished, val[0]);
            formService.clearSelect2(jQuery("#" + that.selectId));
        };
    }
]);

manageStudyGroupControllers.controller('MngmtStudyGroupAnimalsCtrl', ['$scope', 'animalService', 'formService',
    function($scope, animalService, formService) {

        this.selectId = 'animalSelect';
        this.placeHolder = 'Search for animals to add...';

        var that = this;

        this.onSearchSelect2DirectiveLinked = function() {
            formService.initSearchSelect2(jQuery('#' + this.selectId), animalService.getAnimalsLike, false, this.placeHolder, null, true);
        };

        this.onSearchSelect2Selecting = function(choice) {
            $scope.$emit(studyGroupMngmtFormEvents.onStudyGroupAnimalSelected, choice);
        };

        this.onSearchSelect2Removed = function(choice) {
            $scope.$emit(studyGroupMngmtFormEvents.onStudyGroupAnimalRemoved, choice);
        };

        $scope.$on(studyGroupMngmtFormEvents.onExistingStudyGroupUpdateForm, function(event, animals)
    	{
    		var results = [];
    		for(var i = animals.length - 1; i >= 0; i--){
    			var animal = animals[i];
    			results.push({id : animal.id, text : animal.number });
    		};
        	formService.setSelect2Val(results, jQuery('#' + that.selectId));
    	});

    	$scope.$on(studyGroupMngmtFormEvents.onStudyGroupClearUpdateForm, function(event) {
            formService.clearSelect2(jQuery("#" + that.selectId));
        });
    }
]);

manageStudyGroupControllers.controller('MngmtStudyGroupManagementCtrl', ['$scope', 'studyGroupService', 'animalService', 'appConfig', '$timeout',
    function($scope, studyGroupService, animalService, appConfig, $timeout) {

        this.studyGroup = {};
        this.isExistingStudyGroup = false;
        this.isSuccess = false;
        this.errors = [];
        this.isUpdatingStudyGroup = false;        

        var that = this;

        this.resetStudyGroupContainer = function() {
            this.studyGroup = {
                studyGroupId: appConfig.config.unassignedId,
                studyGroupName: undefined,
                studyGroupAnimals: []
            };
            this.isExistingStudyGroup = false;
            this.isSuccess = false;
            this.errors = [];
            this.isUpdatingStudyGroup = false;
        };

        this.setStudyGroupName = function(name) {
            this.studyGroup.studyGroupName = name;
        };

        this.setStudyGroupId = function(id) {
            this.studyGroup.studyGroupId = id;
        };

        this.onNewStudyGroup = function(choice) {
            this.setStudyGroupName(choice.text);
        };

        var getStudyGroupCallback = function(data) {
            that.studyGroup = data;
            that.isExistingStudyGroup = true;
            $scope.$broadcast(studyGroupMngmtFormEvents.onExistingStudyGroupUpdateForm, that.studyGroup.studyGroupAnimals);
        };

        this.getStudyGroupById = function(id) {
            studyGroupService.getStudyGroup(id, getStudyGroupCallback);
        };

        this.onStudyGroupCleared = function() {

        };

        this.onExistingStudyGroupSelected = function(choice) {
            this.getStudyGroupById(choice.id);
        };

        this.addAnimalToStudyGroup = function(animal){
			if (this.studyGroup.studyGroupAnimals.indexOf() === -1) {
                this.studyGroup.studyGroupAnimals.push(animal);
            }
        };

        this.removeAnimalFromStudyGroup = function(animal){
        	this.studyGroup.studyGroupAnimals.splice(this.studyGroup.studyGroupAnimals.indexOf(), 1);
        };

        this.onStudyGroupAnimalSelected = function(choice) {
            var animal = animalService.getBasicAnimal(choice.id, choice.text);
            this.addAnimalToStudyGroup(animal);
        };

        this.onStudyGroupAnimalRemoved = function(choice) {
            var animal = animalService.getBasicAnimal(choice.id, choice.text);
            this.removeAnimalFromStudyGroup(animal);
        };

        var onStudyGroupSubmitSuccess = function(data){
        	that.errors = [];
        	that.isSuccess = true;
        	that.studyGroup = data;
        };

        this.submitStudyGroup = function(){
        	studyGroupService.saveStudyGroup(this.studyGroup, onStudyGroupSubmitSuccess, onErrorCallback);
        };

        var onStudyGroupUpdateSuccess = function(data){
        	that.errors = [];
        	that.isSuccess = true;
        	that.studyGroup = data;
        };

        var onErrorCallback = function(errors){
        	that.isSuccess = false;
        	that.errors = errors;
        };

        this.updateStudyGroup = function(){
        	studyGroupService.updateStudyGroup(this.studyGroup, onStudyGroupUpdateSuccess, onErrorCallback);
        };

        this.clearStudyGroup = function(){
        	this.isExistingStudyGroup = false;
        	this.resetStudyGroupContainer();
        	$scope.$broadcast(studyGroupMngmtFormEvents.onStudyGroupClearUpdateForm);
        };

        this.startUpdate = function(){
        	this.isUpdatingStudyGroup = true;
        	$scope.$broadcast(studyGroupMngmtFormEvents.onStudyGroupUpdateStarted, this.studyGroup);
    	};

    	this.finishUpdate = function(studyGroupName){
        	this.setStudyGroupName(studyGroupName);
        	this.isUpdatingStudyGroup = false;
            $timeout(function(){
                $scope.$apply();
                $scope.$broadcast(studyGroupMngmtFormEvents.onStudyGroupUpdated, that.studyGroup);
            })
    	};

        this.cancelUpdate = function(){
            this.isUpdatingStudyGroup = false;
            $scope.$broadcast(studyGroupMngmtFormEvents.onStudyGroupUpdated, this.studyGroup);
        };

    	this.onStudyGroupNameCleared = function(){
    		this.setStudyGroupName(undefined);
            this.isExistingStudyGroup = false;
            $timeout(function(){
                $scope.$apply();
            });
    	};

        $scope.$on(studyGroupMngmtFormEvents.onNewStudyGroupSelected, function(event, choice) {
            that.onNewStudyGroup(choice);
        });

        $scope.$on(studyGroupMngmtFormEvents.onExistingStudyGroupSelected, function(event, choice) {
            that.onExistingStudyGroupSelected(choice);
        });

        $scope.$on(studyGroupMngmtFormEvents.onStudyGroupAnimalSelected, function(event, choice) {
            that.onStudyGroupAnimalSelected(choice);
        });

        $scope.$on(studyGroupMngmtFormEvents.onStudyGroupAnimalRemoved, function(event, choice) {
            that.onStudyGroupAnimalRemoved(choice);
        });

        $scope.$on(studyGroupMngmtFormEvents.onStudyGroupUpdateFinished, function(event, choice) {
        	that.finishUpdate(choice);
    	});

    	$scope.$on(studyGroupMngmtFormEvents.onStudyGroupNameCleared, function(event) {
            that.onStudyGroupNameCleared();
        });

        this.resetStudyGroupContainer();
    }
]);