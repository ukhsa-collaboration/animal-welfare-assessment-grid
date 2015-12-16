var filterUtilsModule = angular.module('filterUtilsModule', []);

filterUtilsModule.factory('filterUtils', [
function()
{	
	var clearFilter = function(filter){
		if (filter) {
			filter.$ = "";
		}	
	};
	
	return {
		clearFilter : clearFilter
	};
}]);