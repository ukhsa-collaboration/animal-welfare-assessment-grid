var indexControllers = angular.module('indexControllers', ['logonServices', 'assessmentServices']);

indexControllers.controller('IndexMenuCtrl',['$scope','$location',
function($scope, $location)
{
	$scope.isActive = function (viewLocation) {
        return viewLocation === $location.path();
    };
}]);

indexControllers.controller('IndexLoginDetailsCtrl',['$scope','logonService',
function($scope, logonService)
{
    $scope.message = "Log out";
    jQuery("#logoutMenuEntry").removeClass("hidden");

    var getLogonDetailsCallback = function(data)
    {
        $scope.message = $scope.message + " ( " + data.username + " )";
    };

    logonService.getLogonDetails(getLogonDetailsCallback);
}]);

indexControllers.controller('IndexSummaryDataCtrl',['$scope', 'assessmentService',
function($scope, assessmentService)
{
    this.assessmentsCount = "updating...";
    var that = this;

    var getAssessmentsCountCallback = function(data)
    {
        that.assessmentsCount = data;
    };

    assessmentService.getAssessmentsCount(getAssessmentsCountCallback);
}]);
