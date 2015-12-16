angular.module('awFilters',[]).filter('orderAbc', function() {
    return function(unsortedObjects, sortPredicate) {
        if (!sortPredicate) {
            return unsortedObjects;
        } else if (!unsortedObjects){
            return [];
        } else {
            return unsortedObjects.sort(function(a, b) {
                if (a[sortPredicate] < b[sortPredicate]) {
                    return -1;
                }
                if (a[sortPredicate] > b[sortPredicate]) {
                    return 1;
                }
                return 0;
            });
        }
    };
}).filter('yesNo', function() {
    return function(input) {
        return input ? 'yes' : 'no';
    };
}).filter('specifiedUnspecified', function() {
    return function(input) {
        return input ? input : 'UNSPECIFIED';
    };
});