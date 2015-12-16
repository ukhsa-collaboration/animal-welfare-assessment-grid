var dateUtilsModule = angular.module('dateUtilsModule', []);

dateUtilsModule.factory('dateUtils', [
function()
{
    var getFormDateString = function(dateOfBirth)
    {

        var date = dateOfBirth.split("T")[0];
        var dateSections = date.split("-");
        var day = dateSections[2];
        var month = dateSections[1];
        var year = dateSections[0];
        return day + "-" + month + "-" + year;
    };

    var isDateValid = function(date)
    {
        return isNaN(Date.parse(date)) == false;
    };

    return {
        getFormDateString : getFormDateString,
        isDateValid : isDateValid
    };
}]);

