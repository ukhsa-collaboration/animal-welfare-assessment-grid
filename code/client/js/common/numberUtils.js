var numberUtilsModule = angular.module('numberUtilsModule', []);
numberUtilsModule.factory('numberUtils', [
    function() {

        var pad = function(n) {
            return n < 10 ? '0' + n : n;
        };

        var getCumulativeWelfareAssessmentScore = function(assessment){
            var numbersToSum = [];
            if(!angular.equals(assessment, {})){
                var degrees = 360/assessment.assessmentParameters.length;
                var radians = getRadiansFromDegrees(degrees);
            
                for (var i = 0,j = assessment.assessmentParameters.length; i < j; i++) {
                    var nextParameterPos = i+1;                
                    var areaOfTriangle = 0;
                    if(angular.isDefined(assessment.assessmentParameters[nextParameterPos])){
                        areaOfTriangle = getAreaOfTriangle(radians, assessment.assessmentParameters[i].parameterAverage, assessment.assessmentParameters[nextParameterPos].parameterAverage);
                    }
                    else {
                        areaOfTriangle = getAreaOfTriangle(radians, assessment.assessmentParameters[i].parameterAverage, assessment.assessmentParameters[0].parameterAverage);
                    }
                    numbersToSum.push(areaOfTriangle);
                }
            }
            return getSum(numbersToSum);
        };

        var getAverage = function(arrNumber) {
        	var average = 0;
            if (Object.prototype.toString.call(arrNumber) === '[object Array]') {            	
                if (arrNumber.length > 0) {                	
                    return getSum(arrNumber)/arrNumber.length;
                } else {
                    return average;
                }
            } else {
                return average;
            }
        };

        var getSum  = function(arrNumber) {
        	var sum = 0;
            if (Object.prototype.toString.call(arrNumber) === '[object Array]') {            	
                if (arrNumber.length > 0) {
                	for (var i = 0,j = arrNumber.length; i < j; i++) {
            			sum+=arrNumber[i];
       				 }
                    return sum;
                } else {
                    return sum;
                }
            } else {
                return sum;
            }
        };

        var getRadiansFromDegrees = function(degrees) {
            var radians = Math.PI/(180/degrees);
            return radians;
        };

        var getAreaOfTriangle = function(radians, adjacentSide1, adjacentSide2){
            return 0.5 * Math.sin(radians) * adjacentSide1 * adjacentSide2
        };

        return {
            pad: pad,
            getCumulativeWelfareAssessmentScore : getCumulativeWelfareAssessmentScore,
            getAverage: getAverage,
            getSum : getSum,
            getRadiansFromDegrees : getRadiansFromDegrees,
            getAreaOfTriangle : getAreaOfTriangle
        };
    }
]);