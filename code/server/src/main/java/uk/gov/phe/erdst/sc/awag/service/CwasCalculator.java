package uk.gov.phe.erdst.sc.awag.service;

import java.util.ArrayList;
import java.util.List;

import uk.gov.phe.erdst.sc.awag.webapi.response.assessment.AssessmentFullDto;
import uk.gov.phe.erdst.sc.awag.webapi.response.parameter.ParameterScoredDto;

public final class CwasCalculator
{
    private static final double FULL_CIRCLE_DEGREES = 360d;

    private CwasCalculator()
    {
    }

    // CWAS - Cumulative Welfare Assessment Score
    public static double calculateCwas(AssessmentFullDto dto)
    {
        final double radians = Math.toRadians(FULL_CIRCLE_DEGREES / dto.assessmentParameters.size());
        List<ParameterScoredDto> scoreList = new ArrayList<>(dto.assessmentParameters);

        double sum = 0d;
        for (int i = 0, limit = scoreList.size(); i < limit; i++)
        {
            int nextIdx = i + 1;
            if (nextIdx < limit)
            {
                sum += calculateTriangleArea(radians, scoreList.get(i).parameterAverage,
                    scoreList.get(nextIdx).parameterAverage);
            }
            else
            {
                sum += calculateTriangleArea(radians, scoreList.get(i).parameterAverage,
                    scoreList.get(0).parameterAverage);
            }
        }

        return sum;
    }

    // CS:OFF: MagicNumber
    public static double roundCwas(double cwas)
    {
        double denominator = 100d;
        return Math.round(cwas * 100) / denominator;
    }

    // CS:ON

    private static double calculateTriangleArea(double radians, double adjacentSide1, double adjacentSide2)
    {
        // CS:OFF: MagicNumber
        return 0.5d * Math.sin(radians) * adjacentSide1 * adjacentSide2;
        // CS:ON
    }

}
