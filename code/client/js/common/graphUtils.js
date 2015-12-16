var graphUtilsModule = angular.module('graphUtilsModule', []);

graphUtilsModule.factory('graphUtils',  
	function($window) {

        var getContainerDimension = function(containingElement) {
            var dimension = {};
            if ($window.innerHeight > $window.innerWidth) {
                dimension.aspect = $window.innerWidth / $window.innerHeight;
            } else if ($window.innerHeight < $window.innerWidth) {
                dimension.aspect = $window.innerHeight / $window.innerWidth;
            } else {
                dimension.aspect = 1;
            }

            dimension.width = containingElement.width();
            dimension.height = dimension.width * dimension.aspect;
            return dimension;
        }

        return {
            getContainerDimension: getContainerDimension
        }
    }
);