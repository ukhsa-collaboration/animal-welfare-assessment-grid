package uk.gov.phe.erdst.sc.awag.service.factory.template;

import javax.ejb.Stateless;

import uk.gov.phe.erdst.sc.awag.datamodel.AssessmentTemplate;
import uk.gov.phe.erdst.sc.awag.datamodel.AssessmentTemplateParameter;
import uk.gov.phe.erdst.sc.awag.datamodel.AssessmentTemplateParameterFactor;
import uk.gov.phe.erdst.sc.awag.datamodel.AssessmentTemplateParameterFactorPK;
import uk.gov.phe.erdst.sc.awag.datamodel.AssessmentTemplateParameterPK;
import uk.gov.phe.erdst.sc.awag.datamodel.Factor;
import uk.gov.phe.erdst.sc.awag.datamodel.Parameter;
import uk.gov.phe.erdst.sc.awag.utils.Constants;
import uk.gov.phe.erdst.sc.awag.webapi.request.AssessmentTemplateClientData;
import uk.gov.phe.erdst.sc.awag.webapi.request.FactorClientData;
import uk.gov.phe.erdst.sc.awag.webapi.request.ParameterClientData;

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

    // TODO integration test
    public AssessmentTemplateParameter getAssessmentTemplateParameter(AssessmentTemplate assessmentTemplate,
        Parameter parameter, AssessmentTemplateParameterPK id)
    {
        AssessmentTemplateParameter assessmentTemplateParameter = new AssessmentTemplateParameter();
        assessmentTemplateParameter.setmAssessmentTemplate(assessmentTemplate);
        assessmentTemplateParameter.setParameter(parameter);
        assessmentTemplateParameter.setClockwiseDisplayOrderNumber(Constants.DISPLAY_ORDER_NUMBER_NOT_SET);
        assessmentTemplateParameter.setId(id);
        return assessmentTemplateParameter;
    }

    // TODO integration test
    public AssessmentTemplateParameterPK getAssessmentTemplateParameterPK(AssessmentTemplate assessmentTemplate,
        ParameterClientData parameterClientData)
    {
        AssessmentTemplateParameterPK id = new AssessmentTemplateParameterPK();
        id.setAssessmentTemplateId(assessmentTemplate.getId());
        id.setParameterId(parameterClientData.parameterId);
        return id;
    }

    // TODO integration test
    public AssessmentTemplateParameterPK getAssessmentTemplateParameterPK(AssessmentTemplate assessmentTemplate,
        Parameter parameter)
    {
        AssessmentTemplateParameterPK id = new AssessmentTemplateParameterPK();
        id.setAssessmentTemplateId(assessmentTemplate.getId());
        id.setParameterId(parameter.getId());
        return id;
    }

}
