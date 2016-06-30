var appConfigModule = angular.module('appConfigModule', []);

appConfigModule.factory('appConfig', [
function()
{
    var config = {
        unassignedId : -1,
        defaultPageOffsetStart : 0,
        defaultPageSize : 10,
        noSuchPage : -1,
        errorAccessDeniedMessage : "Access denied"
    };    

    var services = {
        selectAction : "selaction",
        selectActions : {
            all : "all",
            like: "sellike",
            id: "selid",
            assessedLike : "assessedlike",
            studyWithAnimal : "study-with-animal",
            animalAssessLine : "assessments-between",
            animalTemplate : "animal-template",
            assessmentsSearch : "assessments-search",
            assessmentDynamicSearch : "assessment-dynamic-search",
            assessmentsCountByCompleteness : "assessments-count-by-completeness",
            countByAnimalId : "count-by-animal-id",
            countByTemplateId : "count-by-template-id"
        },
        actionParams : {
        	like : "like",
        	id : "id",
        	sex : "sex",
        	all : "all",
            animalId : "animalId",
            templateId : "templateId",
            dateFrom : "date-from",
            dateTo : "date-to",
            count : "count",
            exportData : "exportData"
        },
        pagingParams : {
            offset : "offset",
            limit : "limit",
            include : "include"
        },
        pagingParamValues : {
            metadata : "metadata"
        },
        actionParamsValues : {
            sex : {
                male : "m",
                female : "f"
            },
            all : "all"
        },
        exportTypes : {
            activityLogs : 'activityLogs',
            assessments : 'assessments'
        }
    };

    var auth = {
        authTypes : {
            database : "database",
            ldap : "ldap"
        },
        groups : {
            admin : {
                userFriendlyName : "Admin",
                roleName : "admin"
            },
            assessmentuser : {
                userFriendlyName : "Assessment user",
                roleName : "assessmentuser"
            }
        }
    };

    return {
        config : config,
        services : services,
        auth : auth
    };
}]);
