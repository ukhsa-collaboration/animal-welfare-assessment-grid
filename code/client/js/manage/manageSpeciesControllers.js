var manageSpeciesControllers = angular.module('manageSpeciesControllers', ['appConfigModule', 'awCommonDirectives', 'speciesServices', 'formServices']);

var speciesMngmtFormEvents = {
    onExistingSpeciesSelected : "onExistingSpeciesSelected",
    onNewSpeciesSelected : "onNewSpeciesSelected",
    onSpeciesClearSelected : "onSpeciesClearSelected",
    onSpeciesRemoved : "onSpeciesRemoved",
    onSpeciesRemovedClearUpdate : "onSpeciesRemovedClearUpdate",
    onSpeciesUpdateStarted : "onSpeciesUpdateStarted",
    onSpeciesUpdateFinished : "onSpeciesUpdateFinished",
    onSpeciesUpdated : "onSpeciesUpdated"
};

manageSpeciesControllers.controller('MngmtSpeciesSelectionCtrl', ['$scope', 'formService', 'speciesService', 'appConfig',
function($scope, formService, speciesService, appConfig)
{
    this.selectId = "speciesSelect";
    this.placeHolder = "Create/Search a species...";

    var that = this;

    this.onSearchSelect2DirectiveLinked = function()
    {
        formService.initSearchSelect2(jQuery("#" + this.selectId), speciesService.getSpeciesLike, true, this.placeHolder);
    };

    this.onSearchSelect2Selecting = function(choice)
    {
        if (choice.id === appConfig.config.unassignedId) {
            $scope.$emit(speciesMngmtFormEvents.onNewSpeciesSelected, choice);
        }
        else {
            $scope.$emit(speciesMngmtFormEvents.onExistingSpeciesSelected, choice);
        }
    };

    this.onSearchSelect2Removed = function(choice)
    {
        $scope.$emit(speciesMngmtFormEvents.onSpeciesClearSelected, choice);
    };

    $scope.$on(speciesMngmtFormEvents.onSpeciesRemovedClearUpdate, function(event)
    {
        formService.clearSelect2(jQuery("#" + that.selectId));
    });

    $scope.$on(speciesMngmtFormEvents.onSpeciesUpdateStarted, function(event, species)
    {
        formService.setSelect2Val([{id : species.speciesId, text : species.speciesName, locked : true}], jQuery("#" + that.selectId));
    });

    $scope.$on(speciesMngmtFormEvents.onSpeciesUpdated, function(event, species)
    {
        formService.setSelect2Val([{id : species.speciesId, text : species.speciesName, locked : false}], jQuery("#" + that.selectId));
    });
}]);

manageSpeciesControllers.controller('MngmtSpeciesSelectionUpdateCtrl', ['$scope', 'formService',
function($scope, formService)
{
    this.selectId = "speciesUpdateSelect";
    this.placeHolder = "Update the species to...";
    var that = this;

    this.onSelect2DirectiveLinked = function()
    {
        formService.initSelect2([], jQuery("#" + this.selectId), this.placeHolder);
    };

    this.onSelect2Change = function(val)
    {
        $scope.$emit(speciesMngmtFormEvents.onSpeciesUpdateFinished, val[0]);
        formService.clearSelect2(jQuery("#" + that.selectId));
    };
}]);

manageSpeciesControllers.controller('MngmtSpeciesManagementCtrl', ['$scope', 'appConfig', 'speciesService', '$timeout',
function($scope, appConfig, speciesService, $timeout)
{
    this.species = {};
    this.isNewSpecies = true;
    this.isUpdatingSpecies = false;
    this.isSuccess = false;
    this.errors = [];
    var that = this;

    this.resetSpeciesContainer = function()
    {
        this.species = {
            speciesId : appConfig.config.unassignedId,
            speciesName : undefined,
            speciesFactors: []
        };
        this.errors = [];
    };

    this.setSpeciesId = function(id)
    {
        this.species.speciesId = id;
    };

    this.setSpeciesName = function(name)
    {
        this.species.speciesName = name;
    };
    
    this.resetErrors = function()
    {
        this.errors = [];
    };

    this.onNewSpecies = function(choice)
    {
        this.isSuccess = false;
        this.isNewSpecies = true;
        this.setSpeciesName(choice.text);
        $timeout(function(){
            $scope.$apply();
        });
    };

    this.onExistingSpecies = function(choice)
    {
        this.isSuccess = false;
        this.isNewSpecies = false;
        this.setSpeciesId(choice.id);
        this.setSpeciesName(choice.text);
        $timeout(function(){
            $scope.$apply();
        });
    };

    this.onClearSpecies = function(choice)
    {
        this.isSuccess = false;
        this.resetErrors();     
        this.isNewSpecies = true;
        this.setSpeciesId(appConfig.config.unassignedId);
        this.setSpeciesName(undefined);
        $timeout(function(){
            $scope.$apply();
            $scope.$broadcast(speciesMngmtFormEvents.onSpeciesRemovedClearUpdate);
        });        
    };

    this.startUpdate = function()
    {
        this.isUpdatingSpecies = true;
        $scope.$broadcast(speciesMngmtFormEvents.onSpeciesUpdateStarted, this.species);
    };

    this.finishUpdate = function(speciesName)
    {
        this.setSpeciesName(speciesName);
        this.isUpdatingSpecies = false;
        $timeout(function(){
            $scope.$apply();
            $scope.$broadcast(speciesMngmtFormEvents.onSpeciesUpdated, that.species);
        });
    };

    this.clearSpecies = function()
    {
        this.isSuccess = false;
        this.isUpdatingSpecies = false;
        this.resetErrors();     
        this.isNewSpecies = true;
        this.resetSpeciesContainer();
        $scope.$broadcast(speciesMngmtFormEvents.onSpeciesRemovedClearUpdate);
    };

    this.submitSpecies = function()
    {
        speciesService.saveSpecies(this.species, submitSpeciesCallback, onErrorCallBack);
    };

    this.updateSpecies = function()
    {
        speciesService.updateSpecies(this.species, updateSpeciesCallback, onErrorCallBack);
    };

    this.cancelUpdate = function()
    {
        this.isUpdatingSpecies = false;
        $timeout(function(){
            $scope.$apply();
            $scope.$broadcast(speciesMngmtFormEvents.onSpeciesUpdated, that.species);
        });
    };
    
    var onErrorCallBack = function(errors)
    {
        that.isSuccess = false;
        that.errors = errors;
    }

    var submitSpeciesCallback = function(data)
    {
        that.isSuccess = true;
        that.resetErrors();
        that.isNewSpecies = false;
        that.setSpeciesId(data.speciesId);
    };

    var updateSpeciesCallback = function(data)
    {
        that.isSuccess = true;
        that.resetErrors();
    };

    var removeSpeciesCallback = function(data)
    {
        that.isSuccess = true;
        that.isNewSpecies = true;
        that.resetSpeciesContainer();
        $scope.$broadcast(speciesMngmtFormEvents.onSpeciesRemoved);
    };

    $scope.$on(speciesMngmtFormEvents.onNewSpeciesSelected, function(event, choice)
    {
        that.onNewSpecies(choice);
    });

    $scope.$on(speciesMngmtFormEvents.onExistingSpeciesSelected, function(event, choice)
    {
        that.onExistingSpecies(choice);
    });

    $scope.$on(speciesMngmtFormEvents.onSpeciesClearSelected, function(event, choice)
    {
        that.onClearSpecies(choice);
    });

    $scope.$on(speciesMngmtFormEvents.onSpeciesUpdateFinished, function(event, choice)
    {
        that.finishUpdate(choice);
    });

    this.resetSpeciesContainer();
}]);