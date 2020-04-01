package uk.gov.phe.erdst.sc.awag.service.validation.impl;

import java.util.Collection;
import java.util.Map;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import uk.gov.phe.erdst.sc.awag.dao.AssessmentTemplateDao;
import uk.gov.phe.erdst.sc.awag.datamodel.AssessmentTemplate;
import uk.gov.phe.erdst.sc.awag.datamodel.Factor;
import uk.gov.phe.erdst.sc.awag.datamodel.Parameter;
import uk.gov.phe.erdst.sc.awag.datamodel.Scale;
import uk.gov.phe.erdst.sc.awag.datamodel.utils.AssessmentTemplateUtils;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNoSuchEntityException;
import uk.gov.phe.erdst.sc.awag.service.validation.annotations.ValidAssessmentScoreTemplate;
import uk.gov.phe.erdst.sc.awag.service.validation.utils.ValidationConstants;
import uk.gov.phe.erdst.sc.awag.webapi.request.AssessmentClientData;
import uk.gov.phe.erdst.sc.awag.webapi.request.AssessmentClientFactor;

@Stateless
public class AssessmentScoreTemplateValidator
    implements ConstraintValidator<ValidAssessmentScoreTemplate, AssessmentClientData>
{
    @Inject
    private AssessmentTemplateDao mAssessmentTemplateDao;

    public AssessmentScoreTemplateValidator()
    {
    }

    @Override
    public void initialize(ValidAssessmentScoreTemplate arg0)
    {
    }

    // CS:OFF: ParameterAssignment
    @Override
    public boolean isValid(AssessmentClientData clientData, ConstraintValidatorContext context)
    {
        context.disableDefaultConstraintViolation();

        try
        {
            AssessmentTemplate template = mAssessmentTemplateDao.getAssessmentTemplateByAnimalId(clientData.animalId);

            int violations = 0;
            violations = validateScoreAgainstTemplate(template, clientData, context, violations);

            return violations == 0;
        }
        catch (AWNoSuchEntityException e)
        {
            context.buildConstraintViolationWithTemplate(e.getMessage()).addConstraintViolation();
            return false;
        }
    }

    private int validateScoreAgainstTemplate(AssessmentTemplate template, AssessmentClientData clientData,
        ConstraintValidatorContext context, int violations)
    {
        Map<Parameter, Collection<Factor>> templateData = AssessmentTemplateUtils
            .getMappedParameterFactors(template.getAssessmentTemplateParameterFactors());

        if (templateData.size() != clientData.score.size())
        {
            context
                .buildConstraintViolationWithTemplate(ValidationConstants.ERR_ASSESSMENT_TEMPLATE_PARAMETERS_MISMATCH)
                .addConstraintViolation();

            violations++;
            return violations;
        }

        int matchedParams = 0;

        for (Map.Entry<Parameter, Collection<Factor>> entry : templateData.entrySet())
        {
            String templateParamId = String.valueOf(entry.getKey().getId());
            Map<String, AssessmentClientFactor> clientParamData = clientData.score.get(templateParamId);

            if (clientParamData != null)
            {
                matchedParams++;

                violations = validateParameterData(entry.getValue(), clientParamData, template.getScale(), context,
                    violations);
            }

        }

        if (matchedParams != clientData.score.size())
        {
            context
                .buildConstraintViolationWithTemplate(ValidationConstants.ERR_ASSESSMENT_TEMPLATE_PARAMETERS_MISMATCH)
                .addConstraintViolation();

            violations++;
        }

        double sumAverageScores = clientData.averageScores.values().stream().reduce(0D, Double::sum);
        if (!clientData.isAllowZeroScores && sumAverageScores == 0D)
        {
            context.buildConstraintViolationWithTemplate(ValidationConstants.ERR_ASSESSMENT_DISALLOW_AVG_ZERO_SCORING)
                .addConstraintViolation();
            violations++;
        }

        return violations;
    }

    private int validateParameterData(Collection<Factor> templateParamFactors,
        Map<String, AssessmentClientFactor> clientParamData, Scale scale, ConstraintValidatorContext context,
        int violations)
    {
        if (templateParamFactors.size() != clientParamData.size())
        {
            context.buildConstraintViolationWithTemplate(ValidationConstants.ERR_ASSESSMENT_TEMPLATE_FACTORS_MISMATCH)
                .addConstraintViolation();

            violations++;
            return violations;
        }

        int matchedFactors = 0;

        for (Factor factor : templateParamFactors)
        {
            AssessmentClientFactor clientFactor = clientParamData.get(String.valueOf(factor.getId()));
            if (clientFactor != null)
            {
                matchedFactors++;
            }
        }

        if (matchedFactors != clientParamData.size())
        {
            context.buildConstraintViolationWithTemplate(ValidationConstants.ERR_ASSESSMENT_TEMPLATE_FACTORS_MISMATCH)
                .addConstraintViolation();

            violations++;
        }

        return violations;
    }

    // CS:ON
}
