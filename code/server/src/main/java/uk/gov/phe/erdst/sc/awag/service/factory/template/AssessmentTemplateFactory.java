package uk.gov.phe.erdst.sc.awag.service.factory.template;

import javax.ejb.Stateless;

import uk.gov.phe.erdst.sc.awag.datamodel.AssessmentTemplate;
import uk.gov.phe.erdst.sc.awag.datamodel.AssessmentTemplateParameterFactor;
import uk.gov.phe.erdst.sc.awag.datamodel.AssessmentTemplateParameterFactorPK;
import uk.gov.phe.erdst.sc.awag.datamodel.Factor;
import uk.gov.phe.erdst.sc.awag.datamodel.Parameter;
import uk.gov.phe.erdst.sc.awag.datamodel.client.AssessmentTemplateClientData;
import uk.gov.phe.erdst.sc.awag.datamodel.client.FactorClientData;
import uk.gov.phe.erdst.sc.awag.datamodel.client.ParameterClientData;

@Stateless
public class AssessmentTemplateFactory
{
    public AssessmentTemplateFactory()
    {
    }

    public AssessmentTemplate create(AssessmentTemplateClientData clientData)
    {
        AssessmentTemplate template = new AssessmentTemplate();
        template.setName(clientData.templateName);
        return template;
    }

    public void update(AssessmentTemplate template, AssessmentTemplateClientData clientData)
    {
        template.setId(clientData.templateId);
        template.setName(clientData.templateName);
    }

    public AssessmentTemplateParameterFactor getAssessmentTemplateParameterFactor(AssessmentTemplate assessmentTemplate,
        Parameter parameter, Factor factor, AssessmentTemplateParameterFactorPK id)
    {
        AssessmentTemplateParameterFactor assessmentTemplateParameterFactor = new AssessmentTemplateParameterFactor();
        assessmentTemplateParameterFactor.setAssessmentTemplate(assessmentTemplate);
        assessmentTemplateParameterFactor.setParameter(parameter);
        assessmentTemplateParameterFactor.setFactor(factor);
        assessmentTemplateParameterFactor.setId(id);
        return assessmentTemplateParameterFactor;
    }

    public AssessmentTemplateParameterFactorPK getAssessmentTemplateParameterFactorPK(
        AssessmentTemplate assessmentTemplate, ParameterClientData parameterClientData,
        FactorClientData factorClientData)
    {
        AssessmentTemplateParameterFactorPK id = new AssessmentTemplateParameterFactorPK();
        id.setAssessmentTemplateId(assessmentTemplate.getId());
        id.setParameterId(parameterClientData.parameterId);
        id.setFactorId(factorClientData.factorId);
        return id;
    }

}
