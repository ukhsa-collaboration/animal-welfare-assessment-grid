package uk.gov.phe.erdst.sc.awag.shared.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import uk.gov.phe.erdst.sc.awag.datamodel.AssessmentTemplate;
import uk.gov.phe.erdst.sc.awag.datamodel.AssessmentTemplateParameterFactor;
import uk.gov.phe.erdst.sc.awag.datamodel.AssessmentTemplateParameterFactorPK;
import uk.gov.phe.erdst.sc.awag.datamodel.Factor;
import uk.gov.phe.erdst.sc.awag.datamodel.Parameter;
import uk.gov.phe.erdst.sc.awag.datamodel.client.AssessmentClientData;
import uk.gov.phe.erdst.sc.awag.datamodel.client.AssessmentClientFactor;

public final class AssessmentFactoryTestUtils
{
    private AssessmentFactoryTestUtils()
    {
    }

    public static void changeClientData(AssessmentClientData clientData)
    {
        clientData.animalHousing = "Housing 2";
        clientData.animalId = 2L;
        clientData.date = "2014-12-31T00:00:00Z";
        clientData.performedBy = "User 2";
        clientData.reason = "Reason 2";

        String parameterToReplace = "10000";
        String newParameter = "20000";

        clientData.score.put(newParameter, clientData.score.get(parameterToReplace));
        clientData.score.remove(parameterToReplace);

        clientData.averageScores.put(newParameter, clientData.averageScores.get(parameterToReplace));
        clientData.averageScores.remove(parameterToReplace);

        clientData.parameterComments.put(newParameter, clientData.parameterComments.get(parameterToReplace));
        clientData.parameterComments.remove(parameterToReplace);
    }

    public static AssessmentTemplate createTemplateFromAssessmentClientData(Long templateId,
        AssessmentClientData clientData)
    {
        Map<String, Map<String, AssessmentClientFactor>> score = clientData.score;
        AssessmentTemplate template = new AssessmentTemplate();
        List<AssessmentTemplateParameterFactor> paramFactors = new ArrayList<>();
        template.setAssessmentTemplateParameterFactors(paramFactors);
        template.setId(templateId);

        for (Map.Entry<String, Map<String, AssessmentClientFactor>> parameterClient : score.entrySet())
        {
            String paramId = parameterClient.getKey();
            Parameter parameter = new Parameter();
            parameter.setId(Long.parseLong(paramId));

            for (Map.Entry<String, AssessmentClientFactor> factorClient : parameterClient.getValue().entrySet())
            {
                Factor factor = new Factor();
                factor.setId(Long.parseLong(factorClient.getKey()));

                AssessmentTemplateParameterFactor entry = new AssessmentTemplateParameterFactor();
                entry.setAssessmentTemplate(template);
                entry.setParameter(parameter);
                entry.setFactor(factor);

                AssessmentTemplateParameterFactorPK id = new AssessmentTemplateParameterFactorPK();
                id.setAssessmentTemplateId(templateId);
                id.setParameterId(parameter.getId());
                id.setFactorId(factor.getId());

                entry.setId(id);

                paramFactors.add(entry);
            }
        }

        return template;
    }
}
