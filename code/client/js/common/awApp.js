'use strict';
/* App Module */
var awApp = angular.module('awApp', ['ngRoute', 'indexControllers', 'manageUsersControllers','manageTemplateControllers', 'manageFactorControllers', 'manageParameterControllers',
  'assessmentFormControllers', 'existingAssessmentFormControllers', 'graphsControllers', 'exportAssessmentsControllers','manageHousingControllers',
   'manageReasonControllers', 'manageScaleControllers', 'manageStudyControllers', 'manageAnimalControllers', 'aboutControllers', 'activityLogsControllers',
    'manageSpeciesControllers', 'manageSourceControllers', 'manageStudyControllers', 'manageStudyGroupControllers', 'manageAccordionControllers',
     'dataServices', 'entityServices', 'userAuthServices', 'parameterServices', 'factorServices', 'formServices', 'animalServices', 'reasonServices',
      'housingServices', 'userServices', 'assessmentServices', 'sexServices', 'speciesServices', 'sourceServices',
       'templateServices', 'studyServices', 'studyGroupServices', 'activityLogServices','ui.bootstrap', 'graphDrawServices', 'formatServices', 'ngTable',
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
        }).when('/export-assessments', {
            templateUrl: 'partials/export-assessments.html',
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
        }).when('/activity-logs', {
            templateUrl: 'partials/activity-logs.html',
        }).when('/manage-users', {
            templateUrl: 'partials/manage-users.html',
        }).otherwise({
            redirectTo: '/main'
        });
    }
]);