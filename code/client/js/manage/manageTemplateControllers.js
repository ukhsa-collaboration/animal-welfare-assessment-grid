var manageTemplateControllers = angular.module('manageTemplateControllers', ['appConfigModule', 'awCommonDirectives', 'templateServices', 'scaleServices', 'formServices', 'awFilters', 'assessmentServices']);

var templateMngmtFormEvents = {
    onExistingTemplateSelected : 'onExistingTemplateSelected',
    onNewTemplateSelected : 'onNewTemplateSelected',
    onTemplateRemoved : 'onTemplateRemoved',
    onTemplateCleared : 'onTemplateCleared',
    onTemplateUpdateStarted : 'onTemplateUpdateStarted',
    onTemplateUpdateFinished : 'onTemplateUpdateFinished',
    onTemplateUpdated : 'onTemplateUpdated',
    onTemplateScaleSelected : 'onTemplateScaleSelected',
    onTemplateScaleRemoved : 'onTemplateScaleRemoved',
    onTemplateNewParameterSelected : 'onTemplateNewParameterSelected',
    onTemplateNewParameterCleared : 'onTemplateNewParameterCleared',
    onTemplateNewParameterRemoved : 'onTemplateNewParameterRemoved',
    onTemplateNewFactorSelected : 'onTemplateNewFactorSelected',
    onTemplateNewFactorCleared : 'onTemplateNewFactorCleared',
    onTemplateNewFactorRemoved : 'onTemplateNewFactorRemoved',
    onTemplateUpdateForm : 'onTemplateupdateForm',
    onTemplateLocked : 'onTemplateLocked',
    onTemplateUnlocked : 'onTemplateUnlocked'
};

var templateClientSideErrors = {
    parameterAlreadyAdded : 'The parameter {0} has already been added to the template'
};

manageTemplateControllers.controller('MngmtTemplateSelectionCtrl', ['$scope', 'formService', 'templateService', 'appConfig',
function($scope, formService, templateService, appConfig)
{
    this.selectId = 'templateSelect';
    this.placeHolder = 'Create/Search a template...';

    var that = this;

    this.onSearchSelect2DirectiveLinked = function()
    {
        formService.initSearchSelect2(jQuery('#' + this.selectId), templateService.getTemplatesLike, true, this.placeHolder);
    };

    this.onSearchSelect2Selecting = function(choice)
    {
        if (choice.id === appConfig.config.unassignedId) {
            $scope.$emit(templateMngmtFormEvents.onNewTemplateSelected, choice);
        }
        else {
            this.getTemplateById(choice.id);
        }
    };

    this.getTemplateById = function(templateId)
    {
        templateService.getTemplate(getTemplateCallback, templateId);
    };

    var getTemplateCallback = function(template)
    {
        $scope.$emit(templateMngmtFormEvents.onExistingTemplateSelected, template);
    };

    this.onSearchSelect2Removed = function(choice)
    {
        $scope.$emit(templateMngmtFormEvents.onTemplateRemoved, choice);
    };

    $scope.$on(templateMngmtFormEvents.onTemplateCleared, function(event)
    {
        formService.clearSelect2(jQuery('#' + that.selectId));
    });

    $scope.$on(templateMngmtFormEvents.onTemplateUpdateStarted, function(event, template)
    {
        formService.setSelect2Val([{id : template.templateId, text : template.templateName, locked : true}], jQuery('#' + that.selectId));
    });

    $scope.$on(templateMngmtFormEvents.onTemplateUpdated, function(event, template)
    {
        formService.setSelect2Val([{id : template.templateId, text : template.templateName, locked : false}], jQuery('#' + that.selectId));
    });
}]);

manageTemplateControllers.controller('MngmtTemplateSelectionUpdateCtrl', ['$scope', 'formService',
function($scope, formService)
{
    this.selectId = 'templateUpdateSelect';
    this.placeHolder = 'Update the template to...';
    var that = this;

    this.onSelect2DirectiveLinked = function()
    {
        formService.initSelect2([], jQuery('#' + this.selectId), this.placeHolder);
    };

    this.onSelect2Change = function(val)
    {
        $scope.$emit(templateMngmtFormEvents.onTemplateUpdateFinished, val[0]);
        formService.clearSelect2(jQuery('#' + that.selectId));
    };

    $scope.$on(templateMngmtFormEvents.onTemplateCleared, function(event)
    {
        formService.clearSelect2(jQuery('#' + that.selectId));
    });
}]);

manageTemplateControllers.controller('MngmtTemplateScaleCtrl', ['$scope', 'scaleService', 'formService', 
function($scope, scaleService, formService)
{
	this.selectId = 'scaleSelect';
    this.placeHolder = 'Search for a scale...';

    var that = this;

	this.onSearchSelect2DirectiveLinked = function()
    {
        formService.initSearchSelect2(jQuery('#' + this.selectId), scaleService.getScalesLike, false, this.placeHolder);
    };

    this.onSearchSelect2Selecting = function(choice)
    {
       $scope.$emit(templateMngmtFormEvents.onTemplateScaleSelected, choice);
    };

    this.onSearchSelect2Removed = function(choice)
    {
        $scope.$emit(templateMngmtFormEvents.onTemplateScaleRemoved, choice);
    };

    $scope.$on(templateMngmtFormEvents.onTemplateUpdateForm, function(event, template)
    {
        formService.setSelect2Val([{id : template.templateScale.scaleId, text : template.templateScale.scaleName, locked : false}], jQuery('#' + that.selectId));
    });

    $scope.$on(templateMngmtFormEvents.onTemplateCleared, function(event)
    {
        formService.clearSelect2(jQuery('#' + that.selectId));
    });

    $scope.$on(templateMngmtFormEvents.onTemplateLocked, function(event, template){
        formService.setSelect2Val([{id : template.templateScale.scaleId, text : template.templateScale.scaleName, locked : true}], jQuery('#' + that.selectId));
    });

     $scope.$on(templateMngmtFormEvents.onTemplateUnlocked, function(event, template){
        formService.setSelect2Val([{id : template.templateScale.scaleId, text : template.templateScale.scaleName, locked : false}], jQuery('#' + that.selectId));
    });
}]);

manageTemplateControllers.controller('MngmtTemplateParametersCtrl', ['$scope', 'parameterService', 'formService', 
function($scope, parameterService, formService)
{
	this.selectId = 'parameterSelect';
    this.placeHolder = 'Search for a parameter...';

    var that = this;

	this.onSearchSelect2DirectiveLinked = function()
    {
       formService.initSearchSelect2(jQuery('#' + this.selectId), parameterService.getParametersLike, false, this.placeHolder);
    };

    this.onSearchSelect2Selecting = function(choice)
    {
        $scope.$emit(templateMngmtFormEvents.onTemplateNewParameterSelected, choice);
    };

    this.onSearchSelect2Removed = function(choice)
    {
        $scope.$emit(templateMngmtFormEvents.onTemplateNewParameterRemoved, choice);
    };

    $scope.$on(templateMngmtFormEvents.onTemplateNewParameterCleared, function(event)
    {
        formService.clearSelect2(jQuery('#' + that.selectId));
    });

    $scope.$on(templateMngmtFormEvents.onTemplateCleared, function(event)
    {
        formService.clearSelect2(jQuery('#' + that.selectId));
    });
}]);

manageTemplateControllers.controller('MngmtTemplateFactorsCtrl', ['$scope', 'factorService', 'formService', 
function($scope, factorService, formService){
    this.selectId = 'factorSelect';
    this.placeHolder = 'Search for factors...';

    var that = this;

    this.onSearchSelect2DirectiveLinked = function()
    {
        formService.initSearchSelect2(jQuery('#' + this.selectId), factorService.getFactorsLike, false, this.placeHolder, null, true);
    };

    this.onSearchSelect2Selecting = function(choice)
    {
        $scope.$emit(templateMngmtFormEvents.onTemplateNewFactorSelected, choice);
    };

    this.onSearchSelect2Removed = function(choice)
    {
        $scope.$emit(templateMngmtFormEvents.onTemplateNewFactorRemoved, choice);
    };

    $scope.$on(templateMngmtFormEvents.onTemplateNewFactorCleared, function(event)
    {
        formService.clearSelect2(jQuery('#' + that.selectId));
    });

    $scope.$on(templateMngmtFormEvents.onTemplateCleared, function(event)
    {
        formService.clearSelect2(jQuery('#' + that.selectId));
    });
}]);

manageTemplateControllers.controller('MngmtTemplateManagementCtrl', ['$scope', 'appConfig', 'templateService', 'assessmentService', '$timeout',
function($scope, appConfig, templateService, assessmentService, $timeout)
{
    this.template = {};
    this.parameterAndFactorsToAdd = {};
    this.parameterToDelete = {};
    this.isNewTemplate = true;
    this.isUpdatingTemplate = false;
    this.isTemplateLocked = false;
    this.isBasicInfoSuccess = false;
    this.editParameterSuccess = false;
    this.basicInfoErrors = [];
    this.editParameterErrors = [];
    var that = this;

    $scope.accordionStatus = {
        templateBasicInfo: true,
        selectParameters: false
    };

    this.resetTemplateContainer = function()
    {
        this.template = {
            templateId : appConfig.config.unassignedId,
            templateName : undefined,
            templateScale : {},
            templateParameters : []
        };
        this.parameterToDelete = undefined;
        this.editParameterErrors = [];
        this.basicInfoErrors = [];
        this.isTemplateLocked = false;
    };

    this.resetParameterAndFactorstoAdd = function()
    {
        this.parameterAndFactorsToAdd = {
            parameterId : appConfig.config.unassignedId,
            parameterName : undefined,
            parameterFactors : []
        };
    };

    this.setTemplateId = function(id)
    {
        this.template.templateId = id;
    };

    this.setTemplateName = function(name)
    {
        this.template.templateName = name;
    };

    this.setTemplateScaleId = function(id)
    {
        this.template.templateScale.scaleId = id;
    };

    this.setTemplateScaleName = function(name)
    {
        this.template.templateScale.scaleName = name;
    };

    this.setTemplateParameterToAddId = function(id)
    {
        this.parameterAndFactorsToAdd.parameterId = id;
    };

    this.setTemplateParameterToAddName = function(name)
    {
        this.parameterAndFactorsToAdd.parameterName = name;
    };

    this.addTemplateParameterFactorToAdd = function(factor)
    {
        this.parameterAndFactorsToAdd.parameterFactors.push(factor);
    };

    this.setTemplateParametersFactors = function(parameterFactors)
    {
        this.template.templateParameters = parameterFactors;
    };

    this.addParameterAndFactors = function()
    {
        if(this.parameterAndFactorsToAdd.parameterId!==appConfig.config.unassignedId)
        {
            var isAlreadyAdded = false;
            for (var i = this.template.templateParameters.length - 1; i >= 0; i--) {
                var parameter = this.template.templateParameters[i];
                if(parameter.parameterId==this.parameterAndFactorsToAdd.parameterId)
                {
                    isAlreadyAdded = true;
                    break;
                }
            };

            if(!isAlreadyAdded)
            {
                this.template.templateParameters.push(this.parameterAndFactorsToAdd);
                templateService.updateTemplate(getTemplateClientData(this.template), onUpdateParameterFactorsSuccessCallback, onUpdateParameterFactorsErrorCallback);
            }
            else
            {
                this.editParameterSuccess = false;
                this.resetEditParametersErrors();
                this.editParameterErrors.push(templateClientSideErrors.parameterAlreadyAdded.replace('{0}', this.parameterAndFactorsToAdd.parameterName));
            }
        }
    };
    
    this.resetEditParametersErrors = function()
    {
        this.editParameterErrors = [];
    };

    this.resetBasicInfoErrors = function()
    {
        this.basicInfoErrors = [];
    };

    this.onNewTemplate = function(choice)
    {
        this.isBasicInfoSuccess = false;
        this.editParameterSuccess = false;
        this.isNewTemplate = true;
        this.setTemplateName(choice.text);
        $timeout(function(){
            $scope.$apply();
        });
    };

    this.onExistingTemplate = function(template)
    {
        this.isBasicInfoSuccess = false;
        this.editParameterSuccess = false;
        this.resetEditParametersErrors();
        this.resetBasicInfoErrors();

        this.isNewTemplate = false;
        this.setTemplateId(template.templateId);
        this.setTemplateName(template.templateName);
        this.setTemplateScaleId(template.templateScale.scaleId);
        this.setTemplateScaleName(template.templateScale.scaleName);
        this.setTemplateParametersFactors(template.templateParameters);
        $scope.accordionStatus.selectParameters = true;

        assessmentService.getAssessmentCountByTemplateId(template.templateId, getAssessmentCountByTemplateIdCallback);
    };

    var getAssessmentCountByTemplateIdCallback = function(assessmentCount){
        $scope.$broadcast(templateMngmtFormEvents.onTemplateUpdateForm, that.template);
        if(assessmentCount > 0){
            that.isTemplateLocked = true;
            $scope.$broadcast(templateMngmtFormEvents.onTemplateLocked, that.template);
        }
    };

    this.onTemplateScaleSelected = function(choice)
    {
        this.setTemplateScaleId(choice.id);
    };

    this.onTemplateNewParameterSelected = function(choice)
    {
        this.setTemplateParameterToAddId(choice.id);
        this.setTemplateParameterToAddName(choice.text);
    };

    this.onTemplateNewFactorSelected = function(choice)
    {
        var factor = {
            factorId : choice.id,
            factorName : choice.text
        };

        this.addTemplateParameterFactorToAdd(factor);
    };

    this.onTemplateNewFactorRemoved = function(choice)
    {
        for (var i = this.parameterAndFactorsToAdd.parameterFactors.length - 1; i >= 0; i--) {
            var factor = this.parameterAndFactorsToAdd.parameterFactors[i];
            if(factor.factorId===choice.id)
            {
                this.parameterAndFactorsToAdd.parameterFactors.splice(i, 1);
            }
        };
    };

    this.onTemplateNewParameterRemoved = function(choice)
    {
        this.resetEditParametersErrors();
        $timeout(function(){
            $scope.$apply();
        });
        this.setTemplateParameterToAddId(appConfig.config.unassignedId);
        this.setTemplateParameterToAddName(undefined);
    };

    this.onTemplateScaleRemoved = function()
    {
        this.setTemplateScaleId(undefined);
    };

    this.onTemplateRemoved = function(choice)
    {
        this.isBasicInfoSuccess = false;
        this.editParameterSuccess = false;
        this.resetEditParametersErrors();
        this.resetBasicInfoErrors();

        this.isNewTemplate = true;
        this.setTemplateId(appConfig.config.unassignedId);
        this.setTemplateName(undefined);
        this.setTemplateParametersFactors([]);

        this.isTemplateLocked = false;
        $timeout(function(){
            $scope.$apply();        
            $scope.$broadcast(templateMngmtFormEvents.onTemplateUnlocked, that.template);
        });
    };

    this.startUpdate = function()
    {
        this.isUpdatingTemplate = true;
        $scope.$broadcast(templateMngmtFormEvents.onTemplateUpdateStarted, this.template);
    };

    this.finishUpdate = function(templateName)
    {
        this.setTemplateName(templateName);
        this.isUpdatingTemplate = false;
        $timeout(function(){
            $scope.$apply();
            $scope.$broadcast(templateMngmtFormEvents.onTemplateUpdated, that.template);
        });
    };

    this.cancelUpdate = function()
    {
        this.isUpdatingTemplate = false;
        $scope.$broadcast(templateMngmtFormEvents.onTemplateUpdated, this.template);
    };

    this.clearTemplate = function()
    {
        this.isUpdatingTemplate = false;
        this.isNewTemplate = true;

        this.isBasicInfoSuccess = false;
        this.editParameterSuccess = false;
        this.resetEditParametersErrors();
        this.resetBasicInfoErrors();  

        this.resetTemplateContainer();
        this.resetParameterAndFactorstoAdd();
        $scope.$broadcast(templateMngmtFormEvents.onTemplateCleared);
    };

    this.submitBasicInfo = function()
    {
        templateService.saveTemplate(getTemplateClientData(this.template), onSubmitBasicInfoSuccessCallback, onBasicInfoErrorCallBack);
    };

    this.updateBasicInfo = function()
    {
        templateService.updateTemplate(getTemplateClientData(this.template), onUpdateBasicInfoSuccessCallback, onBasicInfoErrorCallBack);
    };

    var getTemplateClientData = function(template)
    {
        var clientData = {};
        angular.copy(template, clientData);
        clientData.templateScale = template.templateScale.scaleId;
        return clientData;
    };

    this.isDeletingParameter = function(parameter)
    {
        if(angular.isDefined(this.parameterToDelete))
        {
            return (parameter==this.parameterToDelete);
        }
        return false;
    };

    this.startDeleteParameter = function(parameter)
    {
        this.parameterToDelete = parameter;
    };

    this.finishDeleteParameter = function()
    {          
        templateService.removeTemplateParameter(this.template.templateId, this.parameterToDelete.parameterId, onDeleteParameterFactorsSuccess, onDeleteParameterFactorsError);
    };

    var onDeleteParameterFactorsSuccess = function(parameter)
    {
        that.resetEditParametersErrors();
        that.editParameterSuccess = true;
        var parameterIndexToRemove = that.template.templateParameters.indexOf(that.parameterToDelete);
        that.template.templateParameters.splice(parameterIndexToRemove, 1);
        that.parameterToDelete = undefined;
    }

    var onDeleteParameterFactorsError = function(parameter)
    {
        //display error here
    }

    this.cancelDeleteParameter = function()
    {
        this.parameterToDelete = undefined;
    };
    
    var onBasicInfoErrorCallBack = function(basicInfoErrors)
    {
        that.isBasicInfoSuccess = false;
        that.basicInfoErrors = basicInfoErrors;
    };

    var onSubmitBasicInfoSuccessCallback = function(data)
    {
        that.isBasicInfoSuccess = true;
        that.resetBasicInfoErrors();
        that.isNewTemplate = false;
        that.setTemplateId(data.templateId);
        that.setTemplateScaleId(data.templateScale.scaleId);
    };

    var onUpdateBasicInfoSuccessCallback = function(data)
    {
        that.isBasicInfoSuccess = true;
        that.resetBasicInfoErrors();
    };

    var onUpdateParameterFactorsSuccessCallback = function(data)
    {   
        that.editParameterSuccess = true;
        that.resetEditParametersErrors();
        that.resetParameterAndFactorstoAdd();
        $scope.$broadcast(templateMngmtFormEvents.onTemplateNewParameterCleared);
        $scope.$broadcast(templateMngmtFormEvents.onTemplateNewFactorCleared);
    };
    
    var onUpdateParameterFactorsErrorCallback = function(parameterFactorsErrors)
    {
        that.editParameterSuccess = false;
        that.editParameterErrors = parameterFactorsErrors;
    };

    $scope.$on(templateMngmtFormEvents.onTemplateRemoved, function(event, choice)
    {
        that.onTemplateRemoved();
    });

    $scope.$on(templateMngmtFormEvents.onNewTemplateSelected, function(event, choice)
    {
        that.onNewTemplate(choice);
    });

    $scope.$on(templateMngmtFormEvents.onExistingTemplateSelected, function(event, template)
    {
        that.onExistingTemplate(template);
    });

    $scope.$on(templateMngmtFormEvents.onTemplateScaleSelected, function(event, scale)
    {
        that.onTemplateScaleSelected(scale);
    });

    $scope.$on(templateMngmtFormEvents.onTemplateScaleRemoved, function(event, choice)
    {
        that.onTemplateScaleRemoved(choice);
    });

    $scope.$on(templateMngmtFormEvents.onTemplateNewParameterSelected, function(event, choice)
    {
        that.onTemplateNewParameterSelected(choice);
    });

    $scope.$on(templateMngmtFormEvents.onTemplateNewFactorSelected, function(event, choice)
    {
        that.onTemplateNewFactorSelected(choice);
    });

    $scope.$on(templateMngmtFormEvents.onTemplateNewFactorRemoved, function(event, choice)
    {
        that.onTemplateNewFactorRemoved(choice);
    });

    $scope.$on(templateMngmtFormEvents.onTemplateNewParameterRemoved, function(event, choice)
    {
        that.onTemplateNewParameterRemoved(choice);
    });

    $scope.$on(templateMngmtFormEvents.onTemplateUpdateFinished, function(event, choice)
    {
        that.finishUpdate(choice);
    });

    this.resetTemplateContainer();
    this.resetParameterAndFactorstoAdd();
}]);