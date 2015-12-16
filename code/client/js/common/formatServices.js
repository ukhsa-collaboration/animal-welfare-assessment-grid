var formatServices = angular.module('formatServices', ['numberUtilsModule']);

formatServices.factory('formatService', ['numberUtils', function(numberUtils) {
    var getTimelineFormatFromAssessments = function(animalAssessmentData) {
        var formattedData = [];        
        if (animalAssessmentData.assessments) {
             for (var i = 0,j = animalAssessmentData.assessments.length; i < j; i++) {

                formattedData[i] = {};
                var assessment = animalAssessmentData.assessments[i];
                formattedData[i].id = assessment.assessmentId;               
                formattedData[i].date = assessment.assessmentDate;
                formattedData[i].cwas = numberUtils.getCumulativeWelfareAssessmentScore(assessment);
            }
        }
        return formattedData;
    };

    var getRadarFormatFromAssessment = function(assessment) {
        var formattedData = [];
        for (var i = 0,j = assessment.assessmentParameters.length; i < j; i++) {
            formattedData[i] = {};
            formattedData[i].id = assessment.assessmentParameters[i].parameterId;
            formattedData[i].axis = assessment.assessmentParameters[i].parameterName;
            formattedData[i].value = assessment.assessmentParameters[i].parameterAverage;
        }
        return formattedData;
    };

    return {
        getTimelineFormatFromAssessments : getTimelineFormatFromAssessments,
        getRadarFormatFromAssessment: getRadarFormatFromAssessment
    };
}]);