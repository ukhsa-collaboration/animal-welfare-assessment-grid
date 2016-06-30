var dateUtilsModule = angular.module('dateUtilsModule', []);

dateUtilsModule.factory('dateUtils', ['$filter',
function($filter)
{
    var getFormDateString = function(iso8601date)
    {

        var date = iso8601date.split("T")[0];
        var dateSections = date.split("-");
        var day = dateSections[2];
        var month = dateSections[1];
        var year = dateSections[0];
        return day + "-" + month + "-" + year;
    };

    var formatDateToString = function(date)
    {
        return $filter('date')(date, 'dd-MM-yyyy', 'UTC');
    };

    var isDateValid = function(date)
    {
        return isNaN(Date.parse(date)) == false;
    };

    return {
        getFormDateString : getFormDateString,
        formatDateToString : formatDateToString,
        isDateValid : isDateValid
    };
}]);

