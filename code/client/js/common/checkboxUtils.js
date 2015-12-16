var checkboxUtilsModule = angular.module('checkboxUtilsModule', []);

checkboxUtilsModule.factory('checkboxUtils', [
function()
{
	var getSelectedCheckBoxIds = function(checkboxItems)
	{
		var ids = [];
		for (var prop in checkboxItems) {
			if (checkboxItems.hasOwnProperty(prop)) {
				if (checkboxItems[prop] == 1) {
					ids.push(prop);
				}
			}
		}
		return ids;
	};
	
	var getResetCheckBoxData = function()
	{
		return {
			'checked' : false,
			items : {}
		};
	};	
	
	return {
		getSelectedCheckBoxIds : getSelectedCheckBoxIds,
		getResetCheckBoxData : getResetCheckBoxData
	};
}]);