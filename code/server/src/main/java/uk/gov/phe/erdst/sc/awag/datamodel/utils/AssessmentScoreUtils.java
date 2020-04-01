package uk.gov.phe.erdst.sc.awag.datamodel.utils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import uk.gov.phe.erdst.sc.awag.datamodel.AssessmentScore;
import uk.gov.phe.erdst.sc.awag.datamodel.Factor;
import uk.gov.phe.erdst.sc.awag.datamodel.FactorScored;
import uk.gov.phe.erdst.sc.awag.datamodel.ParameterScore;
import uk.gov.phe.erdst.sc.awag.utils.Constants;
import uk.gov.phe.erdst.sc.awag.webapi.request.AssessmentClientData;
import uk.gov.phe.erdst.sc.awag.webapi.request.AssessmentClientFactor;
import uk.gov.phe.erdst.sc.awag.webapi.response.assessment.AssessmentScoreComparisonResultDto;
import uk.gov.phe.erdst.sc.awag.webapi.response.assessment.AssessmentScoreComparisonResultDto.FactorComparison;
import uk.gov.phe.erdst.sc.awag.webapi.response.assessment.AssessmentScoreComparisonResultDto.ParameterComparison;

public final class AssessmentScoreUtils
{
    private AssessmentScoreUtils()
    {
    }

    public static AssessmentScoreComparisonResultDto isAssessmentScoreEqual(AssessmentScore assessmentScore,
        AssessmentClientData clientData, ParametersOrdering parametersOrdering)
    {
        Collection<ParameterScore> paramsScored = assessmentScore.getParametersScored();

        Map<String, ParameterScore> paramsScoredMap = convertParametersScoredToMap(paramsScored);

        AssessmentScoreComparisonResultDto result = new AssessmentScoreComparisonResultDto();

        compareParametersScored(paramsScoredMap, clientData, result, parametersOrdering);

        setScoresEqualityFlags(result);

        return result;
    }

    private static void compareParametersScored(Map<String, ParameterScore> parametersScoredMap,
        AssessmentClientData clientData, AssessmentScoreComparisonResultDto result,
        ParametersOrdering parametersOrdering)
    {
        for (Long parameterId : parametersOrdering.getParameterIdList())
        {
            result.addParameter(parameterId, Constants.EMPTY_STRING);
        }

        for (Entry<String, Map<String, AssessmentClientFactor>> clientParameterEntry : clientData.score.entrySet())
        {
            ParameterScore parameterScore = parametersScoredMap.get(clientParameterEntry.getKey());
            Long parameterId = parameterScore.getParameterScored().getId();
            String parameterName = parameterScore.getParameterScored().getName();

            result.addParameter(parameterId, parameterName);

            compareParameterScore(parameterScore, clientParameterEntry.getValue(), result);
        }
    }

    private static void compareParameterScore(ParameterScore parameterScore,
        Map<String, AssessmentClientFactor> clientFactorsEntry, AssessmentScoreComparisonResultDto result)
    {
        Map<String, FactorScored> factorsScoredMap = convertFactorsScoredToMap(
            parameterScore.getScoringFactorsScored());

        for (Entry<String, AssessmentClientFactor> clientFactorEntry : clientFactorsEntry.entrySet())
        {
            FactorScored scoringFactorScored = factorsScoredMap.get(clientFactorEntry.getKey());

            Long factorId = scoringFactorScored.getScoringFactor().getId();
            String factorName = scoringFactorScored.getScoringFactor().getName();

            boolean isScoreIdentical = scoringFactorScored.getScore() == clientFactorEntry.getValue().score;

            result.addFactor(parameterScore.getParameterScored().getId(), factorId, factorName,
                scoringFactorScored.getScore(), clientFactorEntry.getValue().score, isScoreIdentical);
        }
    }

    private static Map<String, ParameterScore> convertParametersScoredToMap(Collection<ParameterScore> parametersScored)
    {
        Map<String, ParameterScore> parametersScoredMap = new HashMap<String, ParameterScore>();

        for (ParameterScore parameterScore : parametersScored)
        {
            parametersScoredMap.put(String.valueOf(parameterScore.getParameterScored().getId()), parameterScore);
        }
        return parametersScoredMap;
    }

    private static Map<String, FactorScored> convertFactorsScoredToMap(Collection<FactorScored> factorsScored)
    {
        Map<String, FactorScored> factorsScoredMap = new HashMap<String, FactorScored>();

        for (FactorScored scoringFactorScored : factorsScored)
        {
            Factor scoringFactor = scoringFactorScored.getScoringFactor();
            factorsScoredMap.put(String.valueOf(scoringFactor.getId()), scoringFactorScored);
        }
        return factorsScoredMap;
    }

    private static void setScoresEqualityFlags(AssessmentScoreComparisonResultDto comparison)
    {
        for (Entry<Long, ParameterComparison> entry : comparison.result.entrySet())
        {
            ParameterComparison parameterComparison = entry.getValue();
            Collection<FactorComparison> factorsComparison = parameterComparison.factorsComparison;

            for (FactorComparison factorComparison : factorsComparison)
            {
                if (!factorComparison.isScoreEqual)
                {
                    comparison.isAssessmentScoreEqual = false;
                }
                else
                {
                    parameterComparison.hasAnyEqualFactorScores = true;
                    comparison.isAnyFactorScoreEqual = true;
                }
            }
        }
    }
}
