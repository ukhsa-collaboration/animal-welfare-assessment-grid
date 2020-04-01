var webApiConfigModule = angular.module('webApiConfigModule', []);

webApiConfigModule.factory('webApiConfig', [
    function () {
        var baseUrl = window.awconfig.webapiUrl;
        var entityApiBase = baseUrl + '/entity';
        var uploadApiBase = '/upload';
        var animalEntityApiBase = entityApiBase + '/animal';
        var assessmentEntityApiBase = entityApiBase + '/assessment';
        var assessmentEntityCountApiBase = assessmentEntityApiBase + '/count';
        var activitylogEntityApiBase = entityApiBase + '/activitylog';
        var factorEntityApiBase = entityApiBase + '/factor';
        var housingEntityApiBase = entityApiBase + '/housing';
        var reasonEntityApiBase = entityApiBase + '/reason';
        var parameterEntityApiBase = entityApiBase + '/parameter';
        var scaleEntityApiBase = entityApiBase + '/scale';
        var sourceEntityApiBase = entityApiBase + '/source';
        var speciesEntityApiBase = entityApiBase + '/species';
        var studyGroupEntityApiBase = entityApiBase + '/studygroup';
        var studyEntityApiBase = entityApiBase + '/study';
        var templateEntityApiBase = entityApiBase + '/assessment-template';
        var templateEntityCountApiBase = templateEntityApiBase + '/count';
        var sexEntityApiBase = entityApiBase + '/sex';
        var userEntityApiBase = entityApiBase + '/user';
        var userAuthEntityApiBase = entityApiBase + '/user-auth';

        var webApiParameters = {
            likeFilter: 'likeFilter',
            paging: {
                offset: "offset",
                limit: "limit"
            },
            dateFrom: 'dateFrom',
            dateTo: 'dateTo'
        };

        var webApiResponseKeys = {
            pagingInfo: 'pagingInfo'
        };

        var webApiUrls = {
            system: "this api is declared in global-config.js",
            entity: {
                animal: {
                    create: animalEntityApiBase,
                    getAll: animalEntityApiBase + "/all",
                    getDeleted: animalEntityApiBase + "/deleted",
                    getFemaleLike: animalEntityApiBase + "/female/like",
                    getMaleLike: animalEntityApiBase + "/male/like",
                    getLike: animalEntityApiBase + "/like",
                    getById: animalEntityApiBase + "/",
                    update: animalEntityApiBase + "/",
                    delete: animalEntityApiBase + "/",
                    upload: animalEntityApiBase + uploadApiBase
                },
                assessment: {
                    create: assessmentEntityApiBase,
                    update: assessmentEntityApiBase + "/",
                    delete: assessmentEntityApiBase + "/",
                    getById: assessmentEntityApiBase + "/",
                    count: {
                        all: assessmentEntityCountApiBase + "/all",
                        completed: assessmentEntityCountApiBase + "/completed",
                        incomplete: assessmentEntityCountApiBase + "/incomplete",
                        byAnimal: assessmentEntityCountApiBase + "/by-animal/",
                        byTemplate: assessmentEntityCountApiBase + "/by-template/"
                    },
                    search: {
                        searchAndGetUniqueFilters: assessmentEntityApiBase + "/search-and-get-unique-filters",
                        searchCompleteBetweenDates: assessmentEntityApiBase + "/search-complete-between-dates",
                        search: assessmentEntityApiBase + "/search"
                    },
                    getPreviousAssessmentForAnimal: assessmentEntityApiBase + "/previous/animal/",
                    getPreviousAssessmentPreviewByDate: assessmentEntityApiBase + "/previous/preview-by-date",
                    compareWithPrevious: assessmentEntityApiBase + "/compare-with-previous",
                    export: assessmentEntityApiBase + "/export",
                    upload: assessmentEntityApiBase + uploadApiBase
                },
                activitylog: {
                    export: activitylogEntityApiBase + "/export"
                },
                factor: {
                    create: factorEntityApiBase,
                    getLike: factorEntityApiBase + "/like",
                    getById: factorEntityApiBase + "/",
                    update: factorEntityApiBase + "/",
                    upload: factorEntityApiBase + uploadApiBase
                },
                housing: {
                    create: housingEntityApiBase,
                    getLike: housingEntityApiBase + "/like",
                    update: housingEntityApiBase + "/",
                    upload: housingEntityApiBase + uploadApiBase
                },
                reason: {
                    create: reasonEntityApiBase,
                    getLike: reasonEntityApiBase + "/like",
                    update: reasonEntityApiBase + "/",
                    upload: reasonEntityApiBase + uploadApiBase
                },
                parameter: {
                    create: parameterEntityApiBase,
                    getLike: parameterEntityApiBase + "/like",
                    update: parameterEntityApiBase + "/",
                    upload: parameterEntityApiBase + uploadApiBase
                },
                scale: {
                    create: scaleEntityApiBase,
                    getById: scaleEntityApiBase + "/",
                    getLike: scaleEntityApiBase + "/like",
                    update: scaleEntityApiBase + "/",
                    upload: scaleEntityApiBase + uploadApiBase
                },
                source: {
                    create: sourceEntityApiBase,
                    getById: sourceEntityApiBase + "/",
                    getLike: sourceEntityApiBase + "/like",
                    update: sourceEntityApiBase + "/",
                    upload: sourceEntityApiBase + uploadApiBase
                },
                species: {
                    create: speciesEntityApiBase,
                    getById: speciesEntityApiBase + "/",
                    getLike: speciesEntityApiBase + "/like",
                    update: speciesEntityApiBase + "/",
                    upload: speciesEntityApiBase + uploadApiBase
                },
                studyGroup: {
                    create: studyGroupEntityApiBase,
                    getById: studyGroupEntityApiBase + "/",
                    getLike: studyGroupEntityApiBase + "/like",
                    update: studyGroupEntityApiBase + "/",
                    upload: studyGroupEntityApiBase + uploadApiBase
                },
                study: {
                    create: studyEntityApiBase,
                    getById: studyEntityApiBase + "/",
                    getLike: studyEntityApiBase + "/like",
                    getWithAnimal: studyEntityApiBase + "/with-animal/",
                    update: studyEntityApiBase + "/",
                    upload: studyEntityApiBase + uploadApiBase
                },
                template: {
                    create: templateEntityApiBase,
                    getById: templateEntityApiBase + "/",
                    getLike: templateEntityApiBase + "/like",
                    getByAnimal: templateEntityApiBase + "/for-animal/",
                    update: templateEntityApiBase + "/",
                    count: {
                        all: templateEntityCountApiBase + "/all"
                    },
                    getDeleteParameterUrl: function (templateId, parameterId) {
                        return templateEntityApiBase + "/" + templateId + "/parameter/" + parameterId;
                    },
                    upload: templateEntityApiBase + uploadApiBase
                },
                sex: {
                    getAll: sexEntityApiBase
                },
                user: {
                    create: userEntityApiBase,
                    getLike: userEntityApiBase + "/like"
                },
                userAuth: {
                    create: userAuthEntityApiBase,
                    getLike: userAuthEntityApiBase + "/like",
                    getById: userAuthEntityApiBase + "/",
                    update: userAuthEntityApiBase + "/",
                    delete: userAuthEntityApiBase + "/"
                }
            }
        };

        return {
            webApiParameters: webApiParameters,
            webApiResponseKeys: webApiResponseKeys,
            webApiUrls: webApiUrls
        };
    }
]);