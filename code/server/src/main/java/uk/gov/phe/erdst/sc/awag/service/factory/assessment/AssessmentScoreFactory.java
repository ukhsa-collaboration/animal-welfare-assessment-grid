package uk.gov.phe.erdst.sc.awag.service.factory.assessment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;

import uk.gov.phe.erdst.sc.awag.datamodel.AssessmentScore;
import uk.gov.phe.erdst.sc.awag.datamodel.AssessmentTemplate;
import uk.gov.phe.erdst.sc.awag.datamodel.Factor;
import uk.gov.phe.erdst.sc.awag.datamodel.FactorScored;
import uk.gov.phe.erdst.sc.awag.datamodel.Parameter;
import uk.gov.phe.erdst.sc.awag.datamodel.ParameterScore;
import uk.gov.phe.erdst.sc.awag.datamodel.client.AssessmentClientData;
import uk.gov.phe.erdst.sc.awag.datamodel.client.AssessmentClientFactor;
import uk.gov.phe.erdst.sc.awag.datamodel.utils.AssessmentTemplateUtils;

@Stateless
public class AssessmentScoreFactory
{
    public AssessmentScore create(AssessmentTemplate template, AssessmentClientData clientData)
    {
        AssessmentScore score = new AssessmentScore();

        List<ParameterScore> parametersScores = createParameterScores(template, clientData);
        score.setParametersScored(parametersScores);

        return score;
    }

    private List<ParameterScore> createParameterScores(AssessmentTemplate template, AssessmentClientData clientData)
    {
        List<ParameterScore> parametersScores = new ArrayList<ParameterScore>();

        Map<Parameter, Collection<Factor>> paramsFactors = AssessmentTemplateUtils
            .getMappedParameterFactors(template.getAssessmentTemplateParameterFactors());

        for (Map.Entry<Parameter, Collection<Factor>> paramFactors : paramsFactors.entrySet())
        {
            ParameterScore parameterScore = new ParameterScore();
            Parameter parameter = paramFactors.getKey();
            String parameterId = String.valueOf(parameter.getId());

            parameterScore.setAverageScore(clientData.averageScores.get(parameterId));
            parameterScore.setComment(clientData.parameterComments.get(parameterId));
            parameterScore.setParameterScored(parameter);

            parameterScore.setScoringFactorsScored(
                createScoringFactorsScored(paramFactors.getValue(), clientData.score.get(parameterId)));

            parametersScores.add(parameterScore);
        }

        return parametersScores;
    }

    private Collection<FactorScored> createScoringFactorsScored(Collection<Factor> factors,
        Map<String, AssessmentClientFactor> clientScoringFactorsScored)
    {
        Collection<FactorScored> factorsScored = new ArrayList<FactorScored>();

        for (Factor factor : factors)
        {
            AssessmentClientFactor clientFactor = clientScoringFactorsScored.get(String.valueOf(factor.getId()));

            FactorScored factorScored = new FactorScored();
            factorScored.setScore(clientFactor.score);
            factorScored.setIsIgnored(clientFactor.isIgnored);
            factorScored.setScoringFactor(factor);

            factorsScored.add(factorScored);
        }

        return factorsScored;
    }

    public AssessmentScore update(AssessmentScore assessmentScoreToUpdate, AssessmentTemplate template,
        AssessmentClientData clientData)
    {
        List<ParameterScore> parametersScores = createParameterScores(template, clientData);

        assessmentScoreToUpdate.getParametersScored().clear();
        assessmentScoreToUpdate.setParametersScored(null);
        assessmentScoreToUpdate.setParametersScored(parametersScores);

        return assessmentScoreToUpdate;
    }

}
