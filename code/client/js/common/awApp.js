'use strict';
/* App Module */
var awApp = angular.module('awApp', ['ngRoute', 'indexControllers', 'manageTemplateControllers', 'manageFactorControllers', 'manageParameterControllers',
  'assessmentFormControllers', 'existingAssessmentFormControllers', 'graphsControllers', 'manageHousingControllers',
   'manageReasonControllers', 'manageScaleControllers', 'manageStudyControllers', 'manageAnimalControllers', 'aboutControllers',
    'manageSpeciesControllers', 'manageSourceControllers', 'manageStudyControllers', 'manageStudyGroupControllers', 'manageAccordionControllers',
     'dataServices', 'entityServices', 'parameterServices', 'factorServices', 'formServices', 'animalServices', 'reasonServices',
      'housingServices', 'userServices', 'assessmentServices', 'sexServices', 'speciesServices', 'sourceServices',
       'templateServices', 'studyServices', 'studyGroupServices', 'ui.bootstrap', 'graphDrawServices', 'formatServices', 'ngTable',
        'tableServices', 'graphUtilsModule']);
awApp.config(['$routeProvider',
    function($routeProvider) {
        $routeProvider.when('/main', {
            templateUrl: 'partials/main.html',
        }).when('/about', {
            templateUrl: 'partials/about.html',
        }).when('/new-assessment', {
            templateUrl: 'partials/new-assessment.html',
        }).when('/existing-assessment', {
            templateUrl: 'partials/existing-assessment.html',
        }).when('/graphs', {
            templateUrl: 'partials/graphs.html',
        }).when('/manage-animals', {
            templateUrl: 'partials/manage-animals.html',
        }).when('/manage-study-study-groups', {
            templateUrl: 'partials/manage-study-study-groups.html',
        }).when('/manage-species', {
            templateUrl: 'partials/manage-species.html',
        }).when('/manage-sources', {
            templateUrl: 'partials/manage-sources.html',
        }).when('/manage-groups', {
            templateUrl: 'partials/manage-groups.html',
        }).when('/manage-reasons', {
            templateUrl: 'partials/manage-reasons.html',
        }).when('/manage-scales', {
            templateUrl: 'partials/manage-scales.html',
        }).when('/manage-housing', {
            templateUrl: 'partials/manage-housing.html',
        }).when('/manage-factors', {
            templateUrl: 'partials/manage-factors.html',
        }).when('/manage-parameters', {
            templateUrl: 'partials/manage-parameters.html',
        }).when('/manage-templates', {
            templateUrl: 'partials/manage-templates.html',
        }).otherwise({
            redirectTo: '/main'
        });
    }
]);