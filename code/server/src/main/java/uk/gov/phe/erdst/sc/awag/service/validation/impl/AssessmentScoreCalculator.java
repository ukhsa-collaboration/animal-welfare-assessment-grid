package uk.gov.phe.erdst.sc.awag.service.validation.impl;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.Stateless;

import uk.gov.phe.erdst.sc.awag.webapi.request.AssessmentClientData;
import uk.gov.phe.erdst.sc.awag.webapi.request.AssessmentClientFactor;

@Stateless
public class AssessmentScoreCalculator
{
    public Map<String, Double> calculateParametersAverages(AssessmentClientData data)
    {
        Map<String, Double> averages = new HashMap<String, Double>(data.score.size());

        for (Map.Entry<String, Map<String, AssessmentClientFactor>> entry : data.score.entrySet())
        {
            Double average = calculateParameterAverage(entry.getValue());
            averages.put(entry.getKey(), average);
        }

        return averages;
    }

    private Double calculateParameterAverage(Map<String, AssessmentClientFactor> parameterScores)
    {
        int count = 0;
        double factorCount = 0;

        for (Map.Entry<String, AssessmentClientFactor> entry : parameterScores.entrySet())
        {
            AssessmentClientFactor value = entry.getValue();
            if (value.isIgnored)
            {
                continue;
            }
            else
            {
                count += value.score;
                factorCount++;
            }
        }

        return factorCount == 0 ? 0 : count / factorCount;
    }
}
