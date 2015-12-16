package uk.gov.phe.erdst.sc.awag.datamodel.utils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import uk.gov.phe.erdst.sc.awag.datamodel.AssessmentScore;
import uk.gov.phe.erdst.sc.awag.datamodel.Factor;
import uk.gov.phe.erdst.sc.awag.datamodel.FactorScored;
import uk.gov.phe.erdst.sc.awag.datamodel.ParameterScore;
import uk.gov.phe.erdst.sc.awag.datamodel.client.AssessmentClientData;
import uk.gov.phe.erdst.sc.awag.datamodel.client.AssessmentClientFactor;

public final class AssessmentScoreUtils
{
    private AssessmentScoreUtils()
    {
    }

    public static boolean isAssessmentScoreEqual(AssessmentScore assessmentScore, AssessmentClientData clientData)
    {
        Collection<ParameterScore> paramsScored = assessmentScore.getParametersScored();

        Map<String, ParameterScore> paramsScoredMap = convertParametersScoredToMap(paramsScored);

        return isParametersScoredEqual(paramsScoredMap, clientData);
    }

    private static boolean isParametersScoredEqual(Map<String, ParameterScore> parametersScoredMap,
        AssessmentClientData clientData)
    {
        for (Entry<String, Map<String, AssessmentClientFactor>> clientParameterEntry : clientData.score.entrySet())
        {
            ParameterScore parameterScore = parametersScoredMap.get(clientParameterEntry.getKey());
            if (!isParameterScoreEqual(parameterScore, clientParameterEntry.getValue()))
            {
                return false;
            }

        }

        return true;
    }

    private static boolean isParameterScoreEqual(ParameterScore parameterScore,
        Map<String, AssessmentClientFactor> clientFactorsEntry)
    {
        Map<String, FactorScored> factorsScoredMap = convertFactorsScoredToMap(
            parameterScore.getScoringFactorsScored());

        for (Entry<String, AssessmentClientFactor> clientFactorEntry : clientFactorsEntry.entrySet())
        {
            FactorScored scoringFactorScored = factorsScoredMap.get(clientFactorEntry.getKey());
            if (scoringFactorScored.getScore() != clientFactorEntry.getValue().score)
            {
                return false;
            }
        }

        return true;
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
}
