var select2UtilsModule = angular.module('select2UtilsModule', []);

select2UtilsModule.factory('select2Utils', [
function()
{
    var getSingleElementListValue = function(list)
    {
        return list.length ? list[0] : '';
    };

    var convertDataToSelect2Format = function(data, idKey, textKey)
    {
        var result = [];
        for(var i=0,limit=data.length; i<limit; i++){
            var d = data[i];
            result.push({id: d[idKey], text: d[textKey]});
        };
        return result;
    };
    
    var convertSimpleDataToSelect2Format = function(id, formDisplayValue)
    {
    	var selectTo = {};
    	
    	selectTo.id = id;
    	selectTo.text = formDisplayValue;
    	
    	return selectTo;
    	
    };

    return {
        getSingleElementListValue : getSingleElementListValue,
        convertDataToSelect2Format : convertDataToSelect2Format,
        convertSimpleDataToSelect2Format : convertSimpleDataToSelect2Format
    };
}]);
