package uk.gov.phe.erdst.sc.awag.webapi.response.assessment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

// CS:OFF: VisibilityModifier
public class AssessmentScoreComparisonResultDto
{
    public Map<Long, ParameterComparison> result;
    public Collection<ParameterComparison> resultAsCollection;
    public boolean isAssessmentScoreEqual = true;
    public boolean isAnyFactorScoreEqual = false;

    public AssessmentScoreComparisonResultDto()
    {
        result = new LinkedHashMap<>();
    }

    public static class ParameterComparison
    {
        public final Long parameterId;
        public final String parameterName;
        public Collection<FactorComparison> factorsComparison;
        public boolean hasAnyEqualFactorScores = false;

        public ParameterComparison(Long id, String name)
        {
            parameterId = id;
            parameterName = name;
            factorsComparison = new ArrayList<>();
        }
    }

    public static class FactorComparison
    {
        public final Long factorId;
        public final String factorName;
        public final int scoreA;
        public final int scoreB;
        public final boolean isScoreEqual;

        public FactorComparison(Long factorId, String factorName, int scoreA, int scoreB, boolean isEqual)
        {
            this.factorId = factorId;
            this.factorName = factorName;
            this.scoreA = scoreA;
            this.scoreB = scoreB;
            this.isScoreEqual = isEqual;
        }

    }

    public void addParameter(Long parameterId, String parameterName)
    {
        result.put(parameterId, new ParameterComparison(parameterId, parameterName));
    }

    public void addFactor(Long parameterId, Long factorId, String factorName, int score, int score2,
        boolean isScoreEqual)
    {
        ParameterComparison parameterComparison = result.get(parameterId);
        FactorComparison factorComparison = new FactorComparison(factorId, factorName, score, score2, isScoreEqual);
        parameterComparison.factorsComparison.add(factorComparison);
    }

    public void convertResultToCollection()
    {
        resultAsCollection = new ArrayList<>(result.values());
        result = null;
    }
}
// CS:ON
