var manageAnimalControllers = angular.module('manageAnimalControllers', ['appConfigModule', 'awCommonDirectives', 'animalServices', 'formServices', 'templateServices', 'select2UtilsModule', 'dateUtilsModule']);

var animalMngmtFormEvents = {
    onExistingAnimalSelected : "onExistingAnimalSelected",
    onNewAnimalSelected : "onNewAnimalSelected",
    onAnimalClearSelected : "onAnimalClearSelected",
    onAnimalClearSelectedUnlockTemplate : "onAnimalClearSelectedUnlockTemplate",
    onAnimalUpdateForm : "onAnimalUpdateForm",
    onAnimalRemoved : "onAnimalRemoved",
    onAnimalCleared : "onAnimalCleared",
    onExcludeChoices : "onExcludeChoices",
    onAnimalUpdateStarted : "onAnimalUpdateStarted",
    onAnimalUpdateFinished : "onAnimalUpdateFinished",
    onAnimalUpdated : "onAnimalUpdated"
};

manageAnimalControllers.controller('MngmtAnimalSelectionCtrl', ['$scope', 'formService', 'animalService', 'appConfig',
function($scope, formService, animalService, appConfig)
{
    this.selectId = "animalSelect";
    this.placeHolder = "Create/Search an animal...";

    var that = this;

    this.onSearchSelect2DirectiveLinked = function()
    {
        formService.initSearchSelect2(jQuery("#" + this.selectId), animalService.getAnimalsLike, true, this.placeHolder, []);
    };

    this.onSearchSelect2Selecting = function(choice)
    {
        if (choice.id === appConfig.config.unassignedId) {
            $scope.$emit(animalMngmtFormEvents.onNewAnimalSelected, choice);
        }
        else {
            this.getAnimalById(choice.id);
        }
    };

    this.onSearchSelect2Removed = function(choice)
    {
        $scope.$emit(animalMngmtFormEvents.onAnimalClearSelected, choice);
    };

    this.getAnimalById = function(animalId)
    {
        animalService.getAnimalById(getAnimalByIdCallback, animalId);
    };

    var getAnimalByIdCallback = function(animal)
    {
        if (animal) {
            $scope.$emit(animalMngmtFormEvents.onExistingAnimalSelected, animal);
        }
    };

    $scope.$on(animalMngmtFormEvents.onAnimalRemoved, function(event)
    {
        formService.clearSelect2(jQuery("#" + that.selectId));
    });

    $scope.$on(animalMngmtFormEvents.onAnimalCleared, function(event)
    {
        formService.clearSelect2(jQuery("#" + that.selectId));
    });

    $scope.$on(animalMngmtFormEvents.onAnimalUpdateStarted, function(event, animal)
    {
        formService.setSelect2Val([{id : animal.id, text : animal.number, locked : true}], jQuery("#" + that.selectId));
    });

    $scope.$on(animalMngmtFormEvents.onAnimalUpdated, function(event, animal)
    {
        formService.setSelect2Val([{id : animal.id, text : animal.number, locked : false}], jQuery("#" + that.selectId));
    });
}]);

manageAnimalControllers.controller('MngmtAnimalSelectionUpdateCtrl', ['$scope', 'formService',
function($scope, formService)
{
    this.selectId = "animalUpdateSelect";
    this.placeHolder = "Update the animal number to...";
    var that = this;

    this.onSelect2DirectiveLinked = function()
    {
        formService.initSelect2([], jQuery("#" + this.selectId), this.placeHolder);
    };

    this.onSelect2Change = function(val)
    {
        $scope.$emit(animalMngmtFormEvents.onAnimalUpdateFinished, val[0]);
        formService.clearSelect2(jQuery("#" + that.selectId));
    };
}]);

manageAnimalControllers.controller('MngmtAnimalDoBSelectionCtrl', ['$scope', 'formService', 'dateUtils',
function($scope, formService, dateUtils)
{
    this.selectId = "animalDoB";

    var that = this;

    this.onDateChange = function(date)
    {
        $scope.animalMngmtCtrl.setAnimalDob(date);
    };

    $scope.$on(animalMngmtFormEvents.onAnimalUpdateForm, function(event, animal)
    {
        formService.setDatePickerFieldValue(dateUtils.getFormDateString(animal.dateOfBirth), jQuery("#" + that.selectId));
    });

    $scope.$on(animalMngmtFormEvents.onAnimalRemoved, function(event)
    {
        formService.clearDatePickerField(jQuery("#" + that.selectId));
    });

    $scope.$on(animalMngmtFormEvents.onAnimalCleared, function(event)
    {
        formService.clearDatePickerField(jQuery("#" + that.selectId));
    });

}]);

manageAnimalControllers.controller('MngmtAnimalSexSelectionCtrl', ['sexService', 'formService', '$scope', 'select2Utils',
function(sexService, formService, $scope, select2Utils)
{
    this.selectId = "sexSelect";
    this.placeHolder = "Select a sex...";

    var that = this;

    var updateSex = function(data)
    {
        var sexNames = select2Utils.convertDataToSelect2Format(data, "id", "name");
        formService.initSelect2FixedChoice(sexNames, jQuery("#" + that.selectId), that.placeHolder);
    };

    this.onSelect2Change = function(sexId)
    {
        $scope.animalMngmtCtrl.setAnimalSex(sexId);
    };

    this.onSelect2DirectiveLinked = function()
    {
        sexService.getSex(updateSex);
    };

    $scope.$on(animalMngmtFormEvents.onAnimalUpdateForm, function(event, animal)
    {
        formService.setSelect2Val(select2Utils.convertSimpleDataToSelect2Format(animal.sexId, that.getSexName(animal.isFemale)), jQuery("#" + that.selectId));
    });

    $scope.$on(animalMngmtFormEvents.onAnimalRemoved, function(event)
    {
        formService.clearSelect2(jQuery("#" + that.selectId));
    });

    $scope.$on(animalMngmtFormEvents.onAnimalCleared, function(event)
    {
        formService.clearSelect2(jQuery("#" + that.selectId));
    });

    this.getSexName = function(isFemale)
    {
        if (isFemale) {
            return "Female";
        }
        else {
            return "Male";
        }
    };

}]);

manageAnimalControllers.controller('MngmtAnimalSpeciesSelectionCtrl', ['speciesService', 'formService', '$scope', 'select2Utils',
function(speciesService, formService, $scope, select2Utils)
{
    this.selectId = "speciesSelect";
    this.placeHolder = "Search for a species...";

    var that = this;

    this.onSearchSelect2DirectiveLinked = function()
    {
        formService.initSearchSelect2(jQuery("#" + this.selectId), speciesService.getSpeciesLike, false, this.placeHolder);
    };

    this.onSearchSelect2Selecting = function(choice)
    {
        $scope.animalMngmtCtrl.setAnimalSpecies(choice.id);
    };

    this.onSearchSelect2Removed = function()
    {
        $scope.animalMngmtCtrl.setAnimalSpecies(undefined);
    };

    $scope.$on(animalMngmtFormEvents.onAnimalUpdateForm, function(event, animal)
    {
        formService.setSelect2Val(select2Utils.convertSimpleDataToSelect2Format(animal.speciesId, animal.speciesName), jQuery("#" + that.selectId));
    });

    $scope.$on(animalMngmtFormEvents.onAnimalRemoved, function(event)
    {
        formService.clearSelect2(jQuery("#" + that.selectId));
    });

    $scope.$on(animalMngmtFormEvents.onAnimalCleared, function(event)
    {
        formService.clearSelect2(jQuery("#" + that.selectId));
    });
}]);

manageAnimalControllers.controller('MngmtAnimalSourceSelectionCtrl', ['sourceService', 'formService', '$scope', 'select2Utils',
function(sourceService, formService, $scope, select2Utils)
{
    this.selectId = "sourceSelect";
    this.placeHolder = "Search for a source...";

    var that = this;

    this.onSearchSelect2DirectiveLinked = function()
    {
        formService.initSearchSelect2(jQuery("#" + this.selectId), sourceService.getSourcesLike, false, this.placeHolder);
    };

    this.onSearchSelect2Selecting = function(choice)
    {
        $scope.animalMngmtCtrl.setAnimalSource(choice.id);
    };

    this.onSearchSelect2Removed = function()
    {
        $scope.animalMngmtCtrl.setAnimalSource(undefined);
    };

    $scope.$on(animalMngmtFormEvents.onAnimalUpdateForm, function(event, animal)
    {
        formService.setSelect2Val(select2Utils.convertSimpleDataToSelect2Format(animal.sourceId, animal.sourceName), jQuery("#" + that.selectId));
    });

    $scope.$on(animalMngmtFormEvents.onAnimalRemoved, function(event)
    {
        formService.clearSelect2(jQuery("#" + that.selectId));
    });

    $scope.$on(animalMngmtFormEvents.onAnimalCleared, function(event)
    {
        formService.clearSelect2(jQuery("#" + that.selectId));
    });
}]);

manageAnimalControllers.controller('MngmtAnimalParentsSelectionCtrl', ['animalService', 'formService', '$scope', 'select2Utils', 'appConfig',
function(animalService, formService, $scope, select2Utils, appConfig)
{

    this.damSelectId = "damSelect";
    this.fatherSelectId = "fatherSelect";
    this.damPlaceHolder = "Search for a dam...";
    this.fatherPlaceHolder = "Search for a father...";

    var that = this;

    this.onSearchSelect2DirectiveLinked = function(elemId)
    {
        if (this.damSelectId === elemId) {
            formService.initSearchSelect2(jQuery("#" + this.damSelectId), animalService.getFemaleAnimalsLike, false, this.damPlaceHolder);
        }

        if (this.fatherSelectId === elemId) {
            formService.initSearchSelect2(jQuery("#" + this.fatherSelectId), animalService.getMaleAnimalsLike, false, this.fatherPlaceHolder);
        }
    };

    this.onSearchSelect2Selecting = function(choice, elemId)
    {
        $scope.animalMngmtCtrl.addExcludedChoice(choice.id);

        if (this.damSelectId === elemId) {
            $scope.animalMngmtCtrl.setAnimalDam(choice.id);
        }

        if (this.fatherSelectId === elemId) {
            $scope.animalMngmtCtrl.setAnimalFather(choice.id);
        }
    };

    this.onSearchSelect2Removed = function(choice, elemId)
    {
        $scope.animalMngmtCtrl.removeExcludedChoiceById(choice.id);

        if (this.damSelectId === elemId) {
            $scope.animalMngmtCtrl.setAnimalDam(undefined);
        }

        if (this.fatherSelectId === elemId) {
            $scope.animalMngmtCtrl.setAnimalFather(undefined);
        }
    };

    $scope.$on(animalMngmtFormEvents.onAnimalUpdateForm, function(event, animal)
    {

        if (animal.damId) {
            formService.setSelect2Val(select2Utils.convertSimpleDataToSelect2Format(animal.damId, animal.damName), jQuery("#" + that.damSelectId));
        }
        else {
            formService.clearSelect2(jQuery("#" + that.damSelectId));
        }

        if (animal.fatherId) {
            formService.setSelect2Val(select2Utils.convertSimpleDataToSelect2Format(animal.fatherId, animal.fatherName), jQuery("#" + that.fatherSelectId));
        }
        else {
            formService.clearSelect2(jQuery("#" + that.fatherSelectId));
        }
    });

    $scope.$on(animalMngmtFormEvents.onAnimalRemoved, function(event)
    {
        formService.clearSelect2(jQuery("#" + that.fatherSelectId));
        formService.clearSelect2(jQuery("#" + that.damSelectId));
    });
    
    $scope.$on(animalMngmtFormEvents.onExcludeChoices, function(event, excludeChoices)
    {
        formService.initSearchSelect2(jQuery("#" + that.damSelectId), animalService.getFemaleAnimalsLike, false,
        that.damPlaceHolder, excludeChoices);
        formService.initSearchSelect2(jQuery("#" + that.fatherSelectId), animalService.getMaleAnimalsLike,
        false, that.fatherPlaceHolder, excludeChoices);
    });

}]);

manageAnimalControllers.controller('MngmtAnimalAssessmentTemplateSelectionCtrl', ['templateService', 'assessmentService', 'formService', '$scope', 'select2Utils',
function(templateService, assessmentService, formService, $scope, select2Utils)
{
    this.selectId = "templateSelect";
    this.placeHolder = "Search for a template...";
    this.animal = undefined;
    this.isTemplateLocked = false;
    this.assessmentCount = 0;
    var that = this;

    this.onSearchSelect2DirectiveLinked = function()
    {
        formService.initSearchSelect2(jQuery("#" + this.selectId), templateService.getTemplatesLike, false, this.placeHolder);
    };

    this.onSearchSelect2Selecting = function(choice)
    {
        $scope.animalMngmtCtrl.setAnimalAssessmentTemplate(choice.id);
    };

    this.onSearchSelect2Removed = function()
    {
        $scope.animalMngmtCtrl.setAnimalAssessmentTemplate(undefined);
    };

    this.resetTemplateLocked = function()
    {
        this.assessmentCount = 0;
        this.isTemplateLocked = false;
        if(that.animal)
        {
            formService.setSelect2Val([{id : that.animal.assessmentTemplateId, text : that.animal.assessmentTemplateName, locked : that.isTemplateLocked}], jQuery("#" + that.selectId));
        } 
    };

    var getAssessmentCountByAnimalIdSuccessCallback = function(count)
    {
        if(count > 0)
        {
            that.assessmentCount = count;
            that.isTemplateLocked = true;            
        }        

        if(that.animal)
        {
            formService.setSelect2Val([{id : that.animal.assessmentTemplateId, text : that.animal.assessmentTemplateName, locked : that.isTemplateLocked}], jQuery("#" + that.selectId));
        }
    };

    $scope.$on(animalMngmtFormEvents.onAnimalClearSelectedUnlockTemplate, function(event)
    {
        that.resetTemplateLocked();
    });

    $scope.$on(animalMngmtFormEvents.onAnimalUpdateForm, function(event, animal)
    {
        that.animal = animal;
        assessmentService.getAssessmentCountByAnimalId(animal.id, getAssessmentCountByAnimalIdSuccessCallback);
    });

    $scope.$on(animalMngmtFormEvents.onAnimalRemoved, function(event)
    {
        formService.clearSelect2(jQuery("#" + that.selectId));
    });

    $scope.$on(animalMngmtFormEvents.onAnimalCleared, function(event)
    {
        formService.clearSelect2(jQuery("#" + that.selectId));
    });
}]);

manageAnimalControllers.controller('MngmtAnimalIsAliveCtrl', ['formService', '$scope',
function(formService, $scope)
{
    this.selectId = "isAliveSelect";
    this.placeHolder = "Select alive...";

    var that = this;

    var data = [];
    data.push({
        id : "true",
        text : "YES"
    });
    data.push({
        id : "false",
        text : "NO"
    });

    this.onSelect2Change = function(isAlive)
    {
        $scope.animalMngmtCtrl.setAnimalIsAlive(isAlive);
    };

    this.onSelect2DirectiveLinked = function()
    {
        formService.initSelect2FixedChoice(data, jQuery("#" + this.selectId), that.placeHolder);
    };

    this.getIsAliveText = function(isAlive)
    {
        if (isAlive) {
            return data[0];
        }
        else {
            return data[1];
        }
    };

    $scope.$on(animalMngmtFormEvents.onAnimalUpdateForm, function(event, animal)
    {
        formService.setSelect2Val(that.getIsAliveText(animal.isAlive), jQuery("#" + that.selectId));
    });

    $scope.$on(animalMngmtFormEvents.onAnimalRemoved, function(event)
    {
        formService.clearSelect2(jQuery("#" + that.selectId));
    });

    $scope.$on(animalMngmtFormEvents.onAnimalCleared, function(event)
    {
        formService.clearSelect2(jQuery("#" + that.selectId));
    });

}]);

manageAnimalControllers.controller('MngmtAnimalManagementCtrl', ['$scope', 'appConfig', 'animalService', '$timeout',
function($scope, appConfig, animalService, $timeout)
{
    this.excludeChoices = [];
    this.animal = {};
    this.isNewAnimal = true;
    this.isUpdatingAnimal = false;
    this.isSuccess = false;
    this.errors = [];

    var that = this;

    this.resetAnimalContainer = function()
    {
        this.animal = {
            id : appConfig.config.unassignedId,
            number : undefined,
            dob : "",
            sex : undefined,
            species : undefined,
            source : undefined,
            dam : undefined,
            father : undefined,
            isAlive : undefined,
            assessmentTemplate : undefined
        };
        this.errors = [];
    };

    this.setAnimalId = function(id)
    {
        this.animal.id = id;
    };

    this.setAnimalNumber = function(animalNumber)
    {
        this.animal.number = animalNumber;
    };

    this.setAnimalDob = function(dob)
    {
        this.animal.dob = dob;
    };

    this.setAnimalSex = function(sex)
    {
        this.animal.sex = parseInt(sex);
    };

    this.setAnimalSpecies = function(species)
    {
        this.animal.species = species;
    };

    this.setAnimalSource = function(source)
    {
        this.animal.source = source;
    };

    this.setAnimalDam = function(dam)
    {
        this.animal.dam = dam;
    };

    this.setAnimalFather = function(father)
    {
        this.animal.father = father;
    };

    this.setAnimalIsAlive = function(isAlive)
    {
        this.animal.isAlive = (isAlive === "true");
    };

    this.setAnimalAssessmentTemplate = function(template)
    {
        this.animal.assessmentTemplate = template;
    };
    
    this.resetErrors = function()
    {
    	this.errors = [];
    };

    this.onClearAnimal = function(choice)
    {
    	this.resetErrors();    	
        this.isNewAnimal = true;
        this.setAnimalId(appConfig.config.unassignedId);
        this.setAnimalNumber(undefined);
        this.removeAllExcludedChoices();

        $timeout(function(){
            $scope.$broadcast(animalMngmtFormEvents.onExcludeChoices, that.excludeChoices);
            $scope.$broadcast(animalMngmtFormEvents.onAnimalClearSelectedUnlockTemplate);
            $scope.$apply();
        });
    };

    this.onNewAnimal = function(choice)
    {    	
    	this.resetErrors(); 
        this.isNewAnimal = true;
        this.setAnimalId(choice.id);
        this.setAnimalNumber(choice.text);
        this.removeAllExcludedChoices();
        this.addExcludedChoice(choice.id);

        $timeout(function(){
            $scope.$broadcast(animalMngmtFormEvents.onExcludeChoices, that.excludeChoices);
            $scope.$apply();
        });
    };

    this.onExistingAnimal = function(animal)
    {    	
    	this.resetErrors(); 
        this.isNewAnimal = false;
        this.setAnimalId(animal.id);
        this.setAnimalNumber(animal.animalNumber);
        this.setAnimalDob(animal.dateOfBirth);
        this.setAnimalSex(animal.sexId);
        this.setAnimalSpecies(animal.speciesId);
        this.setAnimalSource(animal.sourceId);
        this.setAnimalIsAlive(animal.isAlive);
        this.setAnimalDam(animal.damId);
        this.setAnimalFather(animal.fatherId);
        this.setAnimalAssessmentTemplate(animal.assessmentTemplateId);
        this.removeAllExcludedChoices();
        this.addExcludedChoice(animal.id);
        $scope.$broadcast(animalMngmtFormEvents.onExcludeChoices, this.excludeChoices);        
        $scope.$broadcast(animalMngmtFormEvents.onAnimalUpdateForm, animal);
    };

    this.removeExcludedChoiceById = function(id)
    {
        for (var i = 0,
            j = this.excludeChoices.length; i < j; i++) {
            if (this.excludeChoices[i].id === id) {
                this.excludeChoices.splice(i, 1);
                break;
            }
        };
    };

    this.removeAllExcludedChoices = function()
    {
        this.excludeChoices = [];
    };

    this.addExcludedChoice = function(id)
    {
        if (id !== appConfig.config.unassignedId) {
            this.excludeChoices.push({
                id : id
            });
        }
    };
    
    var onErrorCallBack = function(errors)
    {   
        that.isSuccess = false;
    	that.errors = errors;
    }

    var onSubmitAnimalCallBack = function(data)
    {
        that.isNewAnimal = false;
        that.isSuccess = true;
        that.resetErrors();
        that.setAnimalId(data.id);
    };

    this.submitAnimal = function()
    {
        animalService.saveAnimal(this.animal, onSubmitAnimalCallBack, onErrorCallBack);
    };

    var onUpdateAnimalCallBack = function(data)
    {
    	that.isSuccess = true;
    	that.resetErrors();
    };

    this.updateAnimal = function()
    {
        animalService.updateAnimal(this.animal, onUpdateAnimalCallBack, onErrorCallBack);
    };

    var onRemoveAnimalCallBack = function(data)
    {
        that.isNewAnimal = true;
        that.isSuccess = true;
        that.resetAnimalContainer();
        $scope.$broadcast(animalMngmtFormEvents.onAnimalClearSelectedUnlockTemplate);
        $scope.$broadcast(animalMngmtFormEvents.onAnimalRemoved);
    };

    this.removeAnimal = function()
    {
        animalService.removeAnimal(this.animal, onRemoveAnimalCallBack, onErrorCallBack);
    };

    this.clearAnimal = function()
    {
        this.isNewAnimal = true;
        this.isSuccess = false;
        this.isUpdatingAnimal = false;
        this.resetAnimalContainer(); 
        this.removeAllExcludedChoices();    
        $scope.$broadcast(animalMngmtFormEvents.onExcludeChoices, this.excludeChoices);
        $scope.$broadcast(animalMngmtFormEvents.onAnimalClearSelectedUnlockTemplate);
        $scope.$broadcast(animalMngmtFormEvents.onAnimalCleared);
    };

    this.startUpdate = function()
    {
        this.isUpdatingAnimal = true;
        $scope.$broadcast(animalMngmtFormEvents.onAnimalUpdateStarted, this.animal);
    };

    this.finishUpdate = function(animalNumber)
    {
        this.setAnimalNumber(animalNumber);
        this.isUpdatingAnimal = false;
        $timeout(function(){
            $scope.$apply();
            $scope.$broadcast(animalMngmtFormEvents.onAnimalUpdated, that.animal);
        });
        
    };

    this.cancelUpdate = function()
    {
        this.isUpdatingAnimal = false;
        $scope.$broadcast(animalMngmtFormEvents.onAnimalUpdated, this.animal);
    };

    $scope.$on(animalMngmtFormEvents.onNewAnimalSelected, function(event, choice)
    {
        that.onNewAnimal(choice);
    });

    $scope.$on(animalMngmtFormEvents.onExistingAnimalSelected, function(event, animal)
    {
        that.onExistingAnimal(animal);
    });

    $scope.$on(animalMngmtFormEvents.onAnimalClearSelected, function(event, choice)
    {
        that.onClearAnimal(choice);
    });

    $scope.$on(animalMngmtFormEvents.onAnimalUpdateFinished, function(event, choice)
    {
        that.finishUpdate(choice);
    });

    this.resetAnimalContainer();
}]);
