package uk.gov.phe.erdst.sc.awag.webapi.response.assessment;

import java.util.Collection;

import uk.gov.phe.erdst.sc.awag.webapi.response.ResponseDto;
import uk.gov.phe.erdst.sc.awag.webapi.response.assessment.AssessmentScoreComparisonResultDto.ParameterComparison;

public class PreviousAssessmentScoreComparisonDto implements ResponseDto
{
    public final Collection<ParameterComparison> result;
    public final boolean isAssessmentScoreEqual;
    public final boolean isAnyFactorScoreEqual;

    public PreviousAssessmentScoreComparisonDto(AssessmentScoreComparisonResultDto comparisonResult)
    {
        result = comparisonResult.resultAsCollection;
        isAssessmentScoreEqual = comparisonResult.isAssessmentScoreEqual;
        isAnyFactorScoreEqual = comparisonResult.isAnyFactorScoreEqual;
    }
}
