var assessmentFormControllers = angular.module('assessmentFormControllers', ['appConfigModule', 'formServices', 'animalServices', 'studyServices',
    'reasonServices', 'housingServices', 'userServices', 'assessmentServices', 'select2UtilsModule', 'templateServices', 'dateUtilsModule',
    'ui.bootstrap']);

var assessmentFormEvents = {
    animalSelectEntitySelected : "animalSelectEntitySelected",
    onAnimalSelectionCleared : "onAnimalSelectionCleared",
    onClearForm : "onClearForm",
    onClearFormOnSubmit : "onClearFormOnSubmit",
    onAnimalChanged : "onAnimalChanged",
    onParameterPreSelect : "onParameterPreSelect",
    onParameterCommentPreSelect : "onParameterCommentPreSelect",
    onFactorPreSelect : "onFactorPreSelect",
    onLockInputs : "onLockInputs",
    onUnlockInputs : "onUnlockInputs"
};

var assessmentFormModes = {
    newAssessment : "newAssessment", // default mode, no switching to it required
    existingComplete : "existingComplete",
    existingIncomplete : "existingIncomplete"
};

assessmentFormControllers.service("assessFormHelperService", ['formService', function(formService){

    var lockSelectWithValue = function(id, text, selectId)
    {
        var selectElem = jQuery("#" + selectId);
        formService.setSelect2Val([{id : id, text : text, locked : true}], selectElem);
        formService.disableSelect2(selectElem);
    };

    var unlockSelectWithValue = function(id, text, selectId)
    {
        var selectElem = jQuery("#" + selectId);
        if (id && text)
        {
            formService.setSelect2Val([{id : id, text : text, locked : false}], selectElem);
        }
        else
        {
            formService.clearSelect2(selectElem);
        }

        formService.enableSelect2(selectElem);
    };

    var lockSelect = function(selectId)
    {
        formService.disableSelect2(jQuery("#" + selectId));
    };

    var unlockSelect = function(selectId)
    {
        formService.enableSelect2(jQuery("#" + selectId));
    };

    return {
        lockSelectWithValue : lockSelectWithValue,
        unlockSelectWithValue : unlockSelectWithValue,
        lockSelect : lockSelect,
        unlockSelect : unlockSelect
    };
}]);

assessmentFormControllers.controller('AssessFormAnimalChangedModalCtrl', ['$scope', '$modalInstance',
function($scope, $modalInstance) {
  $scope.ok = function () {
    $modalInstance.close();
  };

  $scope.cancel = function () {
    $modalInstance.dismiss();
  };
}]);

assessmentFormControllers.controller('AssessFormAnimalSelectionCtrl', ['$scope', '$timeout', 'appConfig', 'animalService', 'studyService', 'assessmentService',
    'formService', 'templateService', 'assessFormHelperService', '$modal', 'select2Utils',
function($scope, $timeout, appConfig, animalService, studyService, assessmentService, formService, templateService, assessFormHelperService, $modal, select2Utils)
{
    this.selectId = "animalSelection";
    this.placeHolder = "Search for an animal...";

    this.submitSuccessInExistingModeEvent = "submitSuccessInExistingModeEvent";

    this.previousAnimal = null;

    this.modeHandlers = {};
    this.isInExistingCompleteMode = false;
    this.isInExistingMode = false;

    this.showAnimalChangingMsg = false;
    this.isLockedInputs = false;

    var that = this;
    
    // Data initialisation procedures are run at the end of this controller definition.

    this.resetAssessmentModel = function()
    {
        this.assessment = {
            id : appConfig.config.unassignedId,
            animalId : appConfig.config.unassignedId,
            reason : '',
            date : '',
            animalHousing : '',
            performedBy : '',
            score : {},
            averageScores : {},
            parameterComments : {}
        };
    };

    this.resetAssessmentCtrlScoreParametersModel = function()
    {
        this.assessmentScore = {};
        this.parameterScores = {};
        this.parameterAverages = {};
        this.parameterComments = {};
    };

    this.resetAssessmentScoreParametersModel = function()
    {
        this.assessment.score = {};
        this.assessment.averageScores = {};
        this.assessment.parameterComments = {};
    };

    this.resetParameterComments = function()
    {
        this.assessment.parameterComments = {};
    };

    this.resetStudyModel = function()
    {
        this.study = "";
    };

    this.resetAnimalModel = function()
    {
        this.animal = {};
    };

    this.resetErrors = function()
    {
        this.errors = [];
    };

    this.resetErrorsAndSuccess = function()
    {
        this.resetErrors();
        this.isSuccess = false;
    };

    this.setUpModeHandlers = function()
    {
        this.modeHandlers[assessmentFormModes.existingComplete] = this.onModeExistingComplete;
        this.modeHandlers[assessmentFormModes.existingIncomplete] = this.onModeExistingIncomplete;
    };

    this.onAnimalSelectionCleared = function()
    {
        this.resetAnimalModel();

        this.setStudyNumber('');

        this.setAssessmentTemplate(undefined);

        this.resetAssessmentModel();

        this.resetAssessmentCtrlScoreParametersModel();

        this.resetErrorsAndSuccess();
    };

    this.onAnimalChangedDifferentTemplate = function()
    {
        this.resetAssessmentScoreParametersModel();

        this.resetAssessmentCtrlScoreParametersModel();
    };

    this.clearAnimalSelectInput = function()
    {
        formService.clearSelect2(jQuery("#" + this.selectId));
    };

    this.onClearForm = function()
    {
        this.onAnimalSelectionCleared();
        this.clearAnimalSelectInput();
    };

    this.setAssessmentId = function(id)
    {
        this.assessment.id = id;
    };

    this.setAssessedAnimalId = function(animalId)
    {
        this.assessment.animalId = animalId;
    };

    this.setAssessmentReason = function(reason)
    {
        this.assessment.reason = reason;
    };

    this.setAssessmentDate = function(date)
    {
        this.assessment.date = date;
    };

    this.setAnimalHousing = function(housing)
    {
        this.assessment.animalHousing = housing;
    };

    this.setWhoPerformedAssessment = function(user)
    {
        this.assessment.performedBy = user;
    };

    this.setAssessmentScore = function(score)
    {
        this.assessment.score = score;
    };

    this.setAssessmentAverageScores = function(averageScores)
    {
        this.assessment.averageScores = averageScores;
    };

    this.setParameterComments = function(parameterComments)
    {
        this.assessment.parameterComments = parameterComments;
    };

    this.setStudyNumber = function(studyNumber)
    {
        this.studyNumber = studyNumber;
    };

    this.setAssessmentTemplate = function(template)
    {
        this.assessmentTemplate = template;
    };

    this.onSearchSelect2DirectiveLinked = function()
    {
        formService.initSearchSelect2(jQuery("#" + this.selectId), animalService.getAssessedAnimalsLike, false, this.placeHolder);
    };

    this.onSearchSelect2Selecting = function(animalSelectEntity)
    {
        if (animalSelectEntity.id !== appConfig.config.unassignedId) {
            animalService.getAnimalById(getSelectedAnimalCallback, animalSelectEntity.id);
            
            if (!this.previousAnimal)
            {
                $scope.$broadcast(assessmentFormEvents.animalSelectEntitySelected, animalSelectEntity);
            }
        }
    };

    this.onSearchSelect2Removed = function(choice)
    {
        this.previousAnimal = this.animal;
    };

    this.clearForm = function()
    {
        $timeout(function(){
            that.onClearForm();
            $scope.$apply();
            $scope.$broadcast(assessmentFormEvents.onClearForm);
        });
    };

    this.clearFormOnSubmit = function()
    {
        this.clearAnimalSelectInput();

        this.resetAnimalModel();

        this.setStudyNumber('');

        this.setAssessmentTemplate(undefined);

        this.resetAssessmentModel();

        this.resetAssessmentCtrlScoreParametersModel();

        this.resetErrors();

        $timeout(function(){
            $scope.$apply();
            $scope.$broadcast(assessmentFormEvents.onClearFormOnSubmit);
        });
    };

    var getSelectedAnimalCallback = function(animal)
    {
        that.onAnimalSelected(animal);
    };

    var getStudyWithAnimalCallback = function(study)
    {
        that.setStudy(study);
    };

    var getAnimalFormTemplateCallback = function(data)
    {
        that.setAssessmentTemplate(data);
    };

    this.getStudy = function(animalId)
    {
        studyService.getStudyWithAnimal(getStudyWithAnimalCallback, animalId);
    };

    this.setStudy = function(study)
    {
        var studyNumber = "N/A";

        if (study != undefined && study.studyNumber)
        {
            studyNumber = study.studyNumber;
        }

        this.setStudyNumber(studyNumber);
    };

    this.getFormTemplate = function(animalId)
    {
        templateService.getTemplateByAnimalId(getAnimalFormTemplateCallback, animalId);
    };

    this.onAnimalSelected = function(animal)
    {
        if (this.previousAnimal)
        {
            this.onAnimalChanged(animal);
        }
        else
        {
            this.updateWithNewAnimal(animal);
        }
    };

    this.updateWithNewAnimal = function(animal)
    {
        this.animal = animal;
        this.getStudy(animal.id);
        this.setAssessedAnimalId(animal.id);
        this.getFormTemplate(animal.id);
        this.resetParameterComments();
    };

    this.onAnimalChanged = function(animal)
    {
        var currentTemplate = this.assessmentTemplate;
        var that = this;

        var getNewAnimalTemplateCallback = function(data)
        {
            that.showAnimalChangingMsg = false;

            if (currentTemplate.templateId === data.templateId)
            {
                that.animal = animal;
                that.getStudy(animal.id);
                that.setAssessedAnimalId(animal.id);
                $scope.$broadcast(assessmentFormEvents.onAnimalChanged, animal);
                $scope.$broadcast(assessmentFormEvents.onUnlockInputs);
            }
            else
            {
                var modalInstance = $modal.open({
                  templateUrl: 'partials/modals/assessment-form-animal-change-modal.html',
                  controller: 'AssessFormAnimalChangedModalCtrl'
                });

                modalInstance.result.then(
                    function () {
                        that.confirmAnimalChange(animal);
                    },
                    function () {
                        that.cancelAnimalChange();
                    }
                );
            }
        };

        this.showAnimalChangingMsg = true;
        $scope.$broadcast(assessmentFormEvents.onLockInputs);
        this.isLockedInputs = true;

        $timeout(function(){
            templateService.getTemplateByAnimalId(getNewAnimalTemplateCallback, animal.id);
        }, 1000);
    };

    this.confirmAnimalChange = function(animal)
    {
        $scope.$broadcast(assessmentFormEvents.onAnimalChanged, animal);
        $scope.$broadcast(assessmentFormEvents.onUnlockInputs);
        this.updateWithNewAnimal(animal);
        this.onAnimalChangedDifferentTemplate();
    };

    this.cancelAnimalChange = function()
    {
        $scope.$broadcast(assessmentFormEvents.onUnlockInputs);
        var animal = this.previousAnimal;
        formService.setSelect2Val(select2Utils.convertSimpleDataToSelect2Format(animal.id, animal.animalNumber), jQuery("#" + that.selectId));
    };

    $scope.$on(assessmentFormEvents.onUnlockInputs, function(event)
    {
        that.isLockedInputs = false;
    });

    var updateAssessment = function()
    {
        that.setAssessmentScore(that.assessmentScore);
        that.setAssessmentAverageScores(that.parameterAverages);
    };

    var calculateAverage = function(parameterId, parameterModel)
    {

        that.parameterScores[parameterId] = 0;
        var factorCount = 0;
        for (factorId in parameterModel) {

            if (!parameterModel[factorId].isIgnored) {
                that.parameterScores[parameterId] = that.parameterScores[parameterId] + parameterModel[factorId].score;
                factorCount++;
            }
        }

        that.parameterAverages[parameterId] = factorCount === 0 ? 0 : that.parameterScores[parameterId] / factorCount;
    };

    this.initAssessmentScore = function(parameterId, factorId, score, isIgnored)
    {
        this.setFactorScore(parameterId, factorId, score, isIgnored);
    };

    this.setFactorScore = function(parameterId, factorId, score, isIgnored)
    {
        if (!this.assessmentScore[parameterId]) {
            this.assessmentScore[parameterId] = {};
        }

        var parameterModel = this.assessmentScore[parameterId];

        if (!parameterModel[factorId]) {
            parameterModel[factorId] = {};
        }

        var factorModel = parameterModel[factorId];
        factorModel.score = score;
        factorModel.isIgnored = isIgnored;

        parameterModel[factorId] = factorModel;
        this.assessmentScore[parameterId] = parameterModel;

        calculateAverage(parameterId, parameterModel);
        updateAssessment();
    };

    var onSaveAssessmentCallback = function(data)
    {
        that.isSuccess = true;
        that.resetErrors();
    };

    var onErrorCallBack = function(errors)
    {
        that.isSuccess = false;
        that.errors = errors;
    };

    var onSubmitAssessmentCallback = function(data)
    {
        that.isSuccess = true;
        that.resetErrors();

        if (that.isInExistingMode && that.isInExistingCompleteMode === false)
        {
            $scope.$broadcast(assessmentFormEvents.onLockInputs);
            that.isLockedInputs = true;
            assessFormHelperService.lockSelectWithValue(that.animal.id, that.animal.animalNumber, that.selectId);
            $scope.$emit(that.submitSuccessInExistingModeEvent);
        }
        else
        {
           that.clearFormOnSubmit();
        }
    };

    this.saveAssessment = function()
    {
        assessmentService.saveAssessment(this.assessment, that.isInExistingMode, onSaveAssessmentCallback, onErrorCallBack);
    };

    this.submitAssessment = function()
    {
        assessmentService.submitAssessment(this.assessment, that.isInExistingMode, onSubmitAssessmentCallback, onErrorCallBack);
    };

    this.switchMode = function(mode, assessment, assessmentTemplate)
    {
        this.isLockedInputs = false;
        this.isInExistingMode = mode === assessmentFormModes.existingComplete || mode === assessmentFormModes.existingIncomplete;
        this.isInExistingCompleteMode = mode === assessmentFormModes.existingComplete;

        $timeout(function(){
            that.setAssessmentTemplate(assessmentTemplate);
        });

        that.modeHandlers[mode](assessment);
        this.preSelectParameters(assessment.assessmentParameters);
        $scope.$broadcast(mode, assessment);
    };

    this.registerParentController = function(controller)
    {
        controller.assessmentFormControllerCallback(this);
    };

    this.onModeExistingComplete = function(assessment)
    {
        var animal = assessment.animal;
        that.commonModeHandler(assessment);
        assessFormHelperService.lockSelectWithValue(animal.id, animal.animalNumber, that.selectId);
    };

    this.onModeExistingIncomplete = function(assessment)
    {
        var animal = assessment.animal;
        that.commonModeHandler(assessment);
        assessFormHelperService.unlockSelectWithValue(animal.id, animal.animalNumber, that.selectId);
    };

    this.commonModeHandler = function(assessment)
    {
        this.onAnimalSelectionCleared();
        var animal = assessment.animal;
        this.animal = animal;
        this.setAssessedAnimalId(animal.id);
        this.setAssessmentId(assessment.assessmentId);
        this.setStudy(assessment.study);
    };

    this.preSelectParameters = function(parameters)
    {
        $timeout(function() {
            for (var i = 0, limit = parameters.length; i < limit; i++)
            {
                $scope.$broadcast(assessmentFormEvents.onParameterPreSelect, parameters[i]);
            }
        });
    };

    this.resetAnimalModel();
    this.resetStudyModel();
    this.resetAssessmentModel();
    this.resetAssessmentScoreParametersModel();
    this.resetAssessmentCtrlScoreParametersModel();
    this.resetErrorsAndSuccess();

    this.setUpModeHandlers();
}]);

assessmentFormControllers.controller('AssessFormReasonSelectionCtrl', ['reasonService', 'formService', 'appConfig', '$scope', 'assessFormHelperService',
function(reasonService, formService, appConfig, $scope, assessFormHelperService)
{
    this.errors = [];
    this.selectId = "reasonSelect";
    this.placeHolder = "Search/create reason...";

    var that = this;

    this.onSearchSelect2DirectiveLinked = function()
    {
        formService.initSearchSelect2(jQuery("#" + this.selectId), reasonService.getReasonsLike, true, this.placeHolder);
    };

    var onErrorCallBack = function(errors)
    {
        that.errors = errors;
    }

    var onCreatedOk = function(data)
    {
        that.updateMainController(data.reasonName);
    };

    this.onSearchSelect2Selecting = function(choice)
    {
        if (choice.id === appConfig.config.unassignedId) {
            var reason = reasonService.create(appConfig.config.unassignedId, choice.text);
            reasonService.saveReason(reason, onCreatedOk, onErrorCallBack);
        }
        else {
            that.updateMainController(choice.text);
        }
    };

    this.onSearchSelect2Removed = function(choice)
    {
        this.updateMainController('');
        this.resetErrors();
    };

    this.updateMainController = function(reasonName)
    {
        $scope.animalCtrl.setAssessmentReason(reasonName);
    };

    this.resetErrors = function()
    {
        this.errors = [];
    };

    this.reset = function()
    {
        this.resetErrors();
        formService.clearSelect2(jQuery("#" + this.selectId));
    };

    $scope.$on(assessmentFormEvents.onClearForm, function(event)
    {
        that.reset();
    });

    $scope.$on(assessmentFormEvents.onClearFormOnSubmit, function(event)
    {
        that.reset();
    });

    $scope.$on(assessmentFormEvents.onAnimalSelectionCleared, function(event, animalSelectEntity)
    {
        that.reset();
    });

    this.onModeChanged = function(handlingFun, reason)
    {
        if (reason)
        {
            handlingFun(reason.reasonId, reason.reasonName, that.selectId);
            that.updateMainController(reason.reasonName);
        }
        else
        {
            handlingFun(null, null, that.selectId);
        }
    };

    $scope.$on(assessmentFormModes.existingIncomplete, function(event, assessment)
    {
        that.onModeChanged(assessFormHelperService.unlockSelectWithValue, assessment.reason);
    });

    $scope.$on(assessmentFormModes.existingComplete, function(event, assessment)
    {
        that.onModeChanged(assessFormHelperService.lockSelectWithValue, assessment.reason);
    });

    $scope.$on(assessmentFormEvents.onLockInputs, function(event)
    {
        assessFormHelperService.lockSelect(that.selectId);
    });

    $scope.$on(assessmentFormEvents.onUnlockInputs, function(event)
    {
        assessFormHelperService.unlockSelect(that.selectId);
    });
}]);

assessmentFormControllers.controller('AssessFormHousingSelectionCtrl', ['housingService', 'formService', 'appConfig', '$scope', 'assessFormHelperService',
function(housingService, formService, appConfig, $scope, assessFormHelperService)
{
    this.errors = [];
    this.selectId = "housingSelect";
    this.placeHolder = "Search/create housing...";

    var that = this;

    this.onSearchSelect2DirectiveLinked = function()
    {
        formService.initSearchSelect2(jQuery("#" + this.selectId), housingService.getHousingsLike, true, this.placeHolder);
    };

    var onErrorCallBack = function(errors)
    {
        that.errors = errors;
    }

    var onCreatedOk = function(data)
    {
        that.updateMainController(data.housingName);
    };

    this.onSearchSelect2Selecting = function(choice)
    {
        if (choice.id === appConfig.config.unassignedId) {
            var housing = housingService.create(appConfig.config.unassignedId, choice.text);
            housingService.saveHousing(housing, onCreatedOk, onErrorCallBack);
        }
        else {
            that.updateMainController(choice.text);
        }
    };

    this.onSearchSelect2Removed = function(choice)
    {
        this.updateMainController('');
        this.resetErrors();
    };

    this.updateMainController = function(housingName)
    {
        $scope.animalCtrl.setAnimalHousing(housingName);
    };

    this.resetErrors = function()
    {
        this.errors = [];
    };

    this.reset = function()
    {
        this.resetErrors();
        formService.clearSelect2(jQuery("#" + this.selectId));
    };

    $scope.$on(assessmentFormEvents.onClearForm, function(event)
    {
        that.reset();
    });

    $scope.$on(assessmentFormEvents.onClearFormOnSubmit, function(event)
    {
        that.reset();
    });

    $scope.$on(assessmentFormEvents.onAnimalSelectionCleared, function(event, animalSelectEntity)
    {
        that.reset();
    });

    this.onModeChanged = function(handlingFun, housing)
    {
        if (housing)
        {
            handlingFun(housing.housingId, housing.housingName, that.selectId);
            that.updateMainController(housing.housingName);
        }
        else
        {
            handlingFun(null, null, that.selectId);
        }
    };

    $scope.$on(assessmentFormModes.existingIncomplete, function(event, assessment)
    {
        that.onModeChanged(assessFormHelperService.unlockSelectWithValue, assessment.housing);
    });

    $scope.$on(assessmentFormModes.existingComplete, function(event, assessment)
    {
        that.onModeChanged(assessFormHelperService.lockSelectWithValue, assessment.housing);
    });

    $scope.$on(assessmentFormEvents.onLockInputs, function(event)
    {
        assessFormHelperService.lockSelect(that.selectId);
    });

    $scope.$on(assessmentFormEvents.onUnlockInputs, function(event)
    {
        assessFormHelperService.unlockSelect(that.selectId);
    });
}]);

assessmentFormControllers.controller('AssessFormUserSelectionCtrl', ['userService', 'formService', 'appConfig', '$scope', 'assessFormHelperService',
function(userService, formService, appConfig, $scope, assessFormHelperService)
{
    this.errors = [];
    this.selectId = "usersSelect";
    this.placeHolder = "Search/create user...";
    var that = this;

    this.onSearchSelect2DirectiveLinked = function()
    {
        formService.initSearchSelect2(jQuery("#" + this.selectId), userService.getUsersLike, true, this.placeHolder);
    };

    var onErrorCallBack = function(errors)
    {
        that.errors = errors;
    }

    var onCreatedOk = function(data)
    {
        that.updateMainController(data.userName);
    };

    this.onSearchSelect2Selecting = function(choice)
    {
        if (choice.id === appConfig.config.unassignedId) {
            var user = userService.create(appConfig.config.unassignedId, choice.text);
            userService.saveUser(user, onCreatedOk, onErrorCallBack);
        }
        else {
            that.updateMainController(choice.text);
        }
    };

    this.onSearchSelect2Removed = function(choice)
    {
        this.updateMainController('');
        this.resetErrors();
    };

    this.updateMainController = function(userName)
    {
        $scope.animalCtrl.setWhoPerformedAssessment(userName);
    };

    this.resetErrors = function()
    {
        this.errors = [];
    };

    this.reset = function()
    {
        this.resetErrors();
        formService.clearSelect2(jQuery("#" + this.selectId));
    };

    $scope.$on(assessmentFormEvents.onClearForm, function(event)
    {
        that.reset();
    });

    $scope.$on(assessmentFormEvents.onClearFormOnSubmit, function(event)
    {
        that.reset();
    });

    $scope.$on(assessmentFormEvents.onAnimalSelectionCleared, function(event, animalSelectEntity)
    {
        that.reset();
    });

    this.onModeChanged = function(handlingFun, user)
    {
        if (user)
        {
            handlingFun(user.userId, user.userName, that.selectId);
            that.updateMainController(user.userName);
        }
        else
        {
            handlingFun(null, null, that.selectId);
        }
    };

    $scope.$on(assessmentFormModes.existingIncomplete, function(event, assessment)
    {
        that.onModeChanged(assessFormHelperService.unlockSelectWithValue, assessment.performedBy);
    });

    $scope.$on(assessmentFormModes.existingComplete, function(event, assessment)
    {
        that.onModeChanged(assessFormHelperService.lockSelectWithValue, assessment.performedBy);
    });

    $scope.$on(assessmentFormEvents.onLockInputs, function(event)
    {
        assessFormHelperService.lockSelect(that.selectId);
    });

    $scope.$on(assessmentFormEvents.onUnlockInputs, function(event)
    {
        assessFormHelperService.unlockSelect(that.selectId);
    });
}]);

assessmentFormControllers.controller('AssessFormDateSelectionCtrl', ['$scope', 'formService', 'dateUtils',
function($scope, formService, dateUtils)
{
    this.id = "assessmentDate";

    var that = this;

    this.onDateChange = function(date)
    {
        $scope.animalCtrl.setAssessmentDate(date);
    };

    this.clearDate = function()
    {
        formService.clearDatePickerField(jQuery("#" + this.id));
    };

    $scope.$on(assessmentFormEvents.onClearForm, function(event)
    {
        that.clearDate();
    });

    $scope.$on(assessmentFormEvents.onClearFormOnSubmit, function(event)
    {
        that.clearDate();
    });

    $scope.$on(assessmentFormEvents.onAnimalSelectionCleared, function(event, animalSelectEntity)
    {
        that.clearDate();
    });

    $scope.$on(assessmentFormModes.existingIncomplete, function(event, assessment)
    {
        that.clearDate();
        var elem = jQuery("#" + that.id);

        if (assessment.assessmentDate)
        {
            var date = dateUtils.getFormDateString(assessment.assessmentDate);
            that.onDateChange(date);
            formService.setDatePickerFieldValue(date, elem);
        }
        
        formService.enableDatePicker(elem);
    });

    $scope.$on(assessmentFormModes.existingComplete, function(event, assessment)
    {
        var elem = jQuery("#" + that.id);
        var date = dateUtils.getFormDateString(assessment.assessmentDate);
        that.onDateChange(date);
        formService.setDatePickerFieldValue(date, elem);
        formService.disableDatePicker(elem);
    });

    $scope.$on(assessmentFormEvents.onLockInputs, function(event)
    {
        var elem = jQuery("#" + that.id);
        formService.disableDatePicker(elem);
    });

    $scope.$on(assessmentFormEvents.onUnlockInputs, function(event)
    {
        var elem = jQuery("#" + that.id);
        formService.enableDatePicker(elem);
    });
}]);

assessmentFormControllers.controller('AssessFormParametersCtrl', ['$scope', '$timeout',
function($scope, $timeout)
{
    var that = this;

    this.initParameterScore = function(factorId, score, isIgnored)
    {
        $scope.animalCtrl.initAssessmentScore($scope.parameter.parameterId, factorId, score, isIgnored);
    };

    this.setParameterScore = function(factorId, score, isIgnored)
    {
        $scope.animalCtrl.setFactorScore($scope.parameter.parameterId, factorId, score, isIgnored);
    };

    $scope.$on(assessmentFormEvents.onParameterPreSelect, function(event, parameter)
    {
        if ($scope.parameter.parameterId === parameter.parameterId)
        {
            that.updateData(parameter);
        }
    });

    this.updateData = function(parameter)
    {
        $timeout(function() {
            var factors = parameter.parameterFactors;
            
            $scope.$broadcast(assessmentFormEvents.onParameterCommentPreSelect, parameter.parameterComment);

            for (var i = 0, limit = factors.length; i < limit; i++) {
                $scope.$broadcast(assessmentFormEvents.onFactorPreSelect, factors[i]);
            };
        });
    };
}]);

assessmentFormControllers.controller('AssessFormFactorsCtrl', ['$scope', '$timeout',
function($scope, $timeout)
{
    this.isIgnored = false;
    this.score = 0;

    var that = this;

    this.init = function(factor, score, isIgnored)
    {
        $scope.parametersCtrl.initParameterScore(factor.factorId, score, isIgnored);
    };

    this.onFactorScored = function(factor, score, isIgnored)
    {
        $scope.parametersCtrl.setParameterScore(factor.factorId, score, isIgnored);
    };

    this.onFactorIgnore = function(factor, score, isIgnored)
    {
        $scope.parametersCtrl.setParameterScore(factor.factorId, score, isIgnored);
    };

    $scope.$on(assessmentFormEvents.onFactorPreSelect, function(event, factor)
    {
        if ($scope.factor.factorId === factor.factorId)
        {
            $timeout(function(){
                $scope.factorsCtrl.score = factor.factorScore;
                $scope.factorsCtrl.isIgnored = factor.isIgnored;
                that.onFactorScored(factor, factor.factorScore, factor.isIgnored);
            });
        }
    });
}]);

assessmentFormControllers.controller('AssessFormCommentCtrl', ['$scope',
function($scope)
{
    this.comment = '';
    
    var that = this;

    this.onComment = function(parameterId)
    {
        $scope.animalCtrl.parameterComments[parameterId] = that.comment;
        $scope.animalCtrl.setParameterComments($scope.animalCtrl.parameterComments);
    };

    $scope.$on(assessmentFormEvents.onParameterCommentPreSelect, function(event, comment)
    {
        that.comment = comment;
        that.onComment($scope.parameter.id);
    });
}]);


assessmentFormControllers.controller('AssessFormPreviousAssessmentCtrl', ['$scope', 'assessmentService', '$timeout',
function($scope, assessmentService, $timeout)
{
    var that = this;
    this.date = "";
    this.scores = [];
    this.isComplete = true;

    var onPrevAssessmentData = function(data)
    {
        that.setDate(data.date);
        that.setScores(data.scores);

        if (data.date)
        {
            that.isComplete = data.isComplete;
        }

        $timeout(function() {
            $scope.$apply();
        }, 0);
    };

    var onErrorCallback = function(errors)
    {
        // In this case we ignore errors for now.
    };

    this.setDate = function(date)
    {
        if (date) {
            this.date = date;
        }
        else {
            this.date = "N/A";
        }
    };

    this.setScores = function(scores)
    {
        this.scores = scores;
    };

    this.reset = function() {
        this.date = "";
        this.scores = [];
        this.isComplete = true;
        $timeout(function() {
              $scope.$apply();
        }, 0);
    };

    $scope.$on(assessmentFormEvents.animalSelectEntitySelected, function(event, animalSelectEntity)
    {
        assessmentService.getPreviousAssessment(animalSelectEntity.id, onPrevAssessmentData);
    });

    $scope.$on(assessmentFormEvents.onAnimalChanged, function(event, animal)
    {
        assessmentService.getPreviousAssessment(animal.id, onPrevAssessmentData);
    });

    $scope.$on(assessmentFormEvents.onAnimalSelectionCleared, function(event, animalSelectEntity)
    {
        that.reset();
    });

    $scope.$on(assessmentFormEvents.onClearForm, function(event)
    {
        that.reset();
    });

    $scope.$on(assessmentFormEvents.onClearFormOnSubmit, function(event)
    {
        that.reset();
    });

    this.handleModeSwitch = function(assessment)
    {
        that.reset();
        assessmentService.getPreviousAssessmentByDate(assessment.animal.id, assessment.assessmentDate, assessment.assessmentId, onPrevAssessmentData, onErrorCallback);
    };

    $scope.$on(assessmentFormModes.existingIncomplete, function(event, assessment)
    {
        that.handleModeSwitch(assessment);
    });

    $scope.$on(assessmentFormModes.existingComplete, function(event, assessment)
    {
        that.handleModeSwitch(assessment);
    });

}]);
