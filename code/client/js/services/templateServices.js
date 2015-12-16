var templateServices = angular.module('templateServices', ['dataServices', 'appConfigModule', 'pagingUtilsModule']);

templateServices.factory('templateService', ['dataService', 'appConfig', 'pagingUtils',
function(dataService, appConfig, pagingUtils) {
	
	var templateServlet = "template";

	var create = function(id, name) {
		return {
			templateId : id,
			templateName : name,
			templateScale : {},
			templateParameters : []
		};
	};

	var getTemplate = function(callback, id){
		var parameters = {};
		parameters[appConfig.services.selectAction] = appConfig.services.selectActions.id;
        parameters[appConfig.services.actionParams.id] = id;
        dataService.dataConnection({
            servlet : templateServlet,
            parameters : parameters,
            callback : {
                success : callback
            }
        });
	};

	var getTemplatesLike = function(callback, likeTerm, pagingOptions) {
		var parameters = {};
		parameters[appConfig.services.selectAction] = appConfig.services.selectActions.like;
		parameters[appConfig.services.actionParams.like] = likeTerm;
		pagingUtils.setPagingParameters(parameters, pagingOptions);
		dataService.dataConnection({
			servlet : templateServlet,
			parameters : parameters,
			callback : {
				success : callback
			}
		});
	};

	var getTemplates = function(callback, pagingOptions){
		var parameters = {};
		parameters[appConfig.services.selectAction] = appConfig.services.actionParamsValues.all;
		pagingUtils.setPagingParameters(parameters, pagingOptions);		
		dataService.dataConnection({
			servlet : templateServlet,
			parameters : parameters,
			callback : {
				success : callback
			}
		});
	};

	var getTemplateByAnimalId = function(callback, animalId){
		var parameters = {};
		parameters[appConfig.services.selectAction] = appConfig.services.selectActions.animalTemplate;
		parameters[appConfig.services.actionParams.animalId] = animalId;
		dataService.dataConnection({
			servlet : templateServlet,
			parameters : parameters,
			callback : {
				success : callback
			}
		});
	};

	var saveTemplate = function(template, successCallback, errCallback){
        var parameters = _getStoreDataParameters(template);
        dataService.postData({
            servlet : templateServlet,
            parameters : parameters,
            callback : {
                success : successCallback,
                error : errCallback
            }
        });
    };

    var updateTemplate = function(template, successCallback, errCallback){
        dataService.putData({
            servlet : templateServlet,
            resourceId : template.templateId,
            data : template,
            callback : {
                success : successCallback,
                error : errCallback
            }
        });
    };

    var removeTemplateParameter = function(templateId, parameterId, successCallback, errCallback){
        dataService.deleteData({
        	servlet : undefined,
        	resourceId : undefined,
			pathParmMap : {
				template : templateId,
				parameter : parameterId
			},
			callback : {
				success : successCallback,
				error : errCallback
			}
		});
    };

    var _getStoreDataParameters = function(template) {
        return {
            template : template
        };
    };

	return {
		getTemplatesLike : getTemplatesLike,
		getTemplates : getTemplates,
		getTemplateByAnimalId : getTemplateByAnimalId,
		getTemplate : getTemplate,
		saveTemplate : saveTemplate,
		updateTemplate : updateTemplate,
		removeTemplateParameter : removeTemplateParameter,
		create : create
	};
}]); 