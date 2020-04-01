package uk.gov.phe.erdst.sc.awag.service.validation.impl;

import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import uk.gov.phe.erdst.sc.awag.dao.AssessmentTemplateDao;
import uk.gov.phe.erdst.sc.awag.datamodel.Assessment;
import uk.gov.phe.erdst.sc.awag.datamodel.AssessmentTemplate;
import uk.gov.phe.erdst.sc.awag.datamodel.Scale;
import uk.gov.phe.erdst.sc.awag.exceptions.AWInputValidationException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNoSuchEntityException;
import uk.gov.phe.erdst.sc.awag.service.validation.annotations.ValidAssessmentScore;
import uk.gov.phe.erdst.sc.awag.service.validation.utils.ValidationConstants;
import uk.gov.phe.erdst.sc.awag.service.validation.utils.ValidatorUtils;
import uk.gov.phe.erdst.sc.awag.webapi.request.AssessmentClientData;
import uk.gov.phe.erdst.sc.awag.webapi.request.AssessmentClientFactor;

@Stateless
public class AssessmentScoreValidator implements ConstraintValidator<ValidAssessmentScore, AssessmentClientData>
{
    private static Pattern sIllegalCharactersPattern = Pattern.compile(ValidationConstants.COMMENTS_TEXT_INPUT_REGEX);

    @Inject
    private AssessmentScoreCalculator mAssessmentScoreCalculator;

    @Inject
    private AssessmentTemplateDao mAssessmentTemplateDao;

    public AssessmentScoreValidator()
    {
    }

    @Override
    public void initialize(ValidAssessmentScore arg0)
    {
    }

    @Override
    public boolean isValid(AssessmentClientData clientData, ConstraintValidatorContext context)
    {
        context.disableDefaultConstraintViolation();

        if (isDataNotNull(clientData, context))
        {
            int violations = 0;

            violations = checkKeysIdentical(clientData, context, violations);
            violations = checkAverageValues(clientData, context, violations);
            violations = checkScoreValues(clientData, context, violations);
            violations = checkAveragesAndScoresCorrectness(clientData, context, violations);
            violations = checkComments(clientData, context, violations);

            return violations == 0;
        }

        return false;
    }

    public static void validateIsScoresVerifiedFlag(boolean isScoresVerified) throws AWInputValidationException
    {
        if (!isScoresVerified)
        {
            throw new AWInputValidationException(ValidationConstants.ERR_ASSESSMENT_EQUAL_SCORES_NOT_VERIFIED);
        }
    }

    public static void validateIsNotCompleteOnUpdate(Assessment assessment) throws AWInputValidationException
    {
        if (assessment.isComplete())
        {
            throw new AWInputValidationException(ValidationConstants.ERR_ASSESSMENT_UPDATE_COMPLETED_ATTEMPT);
        }
    }

    private boolean isDataNotNull(AssessmentClientData data, ConstraintValidatorContext context)
    {
        if (data.score == null || data.averageScores == null || data.parameterComments == null)
        {
            context.buildConstraintViolationWithTemplate(ValidationConstants.ERR_ASSESSMENT_PARTS_NOT_NULL)
                .addConstraintViolation();

            return false;
        }

        return true;
    }

    // CS:OFF: ParameterAssignment
    private int checkKeysIdentical(AssessmentClientData data, ConstraintValidatorContext context, int violations)
    {
        if (!ValidatorUtils.isKeysIdentical(data.score.keySet(), data.averageScores.keySet()))
        {
            violations++;

            context.buildConstraintViolationWithTemplate(ValidationConstants.ERR_ASSESSMENT_SCORE_AVERAGE_KEYS_DIFF)
                .addConstraintViolation();
        }

        return violations;
    }

    // CS:ON

    // CS:OFF: ParameterAssignment
    private int checkAverageValues(AssessmentClientData data, ConstraintValidatorContext context, int violations)
    {
        for (Entry<String, Double> parameterAverage : data.averageScores.entrySet())
        {
            if (parameterAverage.getValue() < 0)
            {
                violations++;

                context.buildConstraintViolationWithTemplate(String.format(ValidationConstants.ERR_NOT_NEGATIVE_VALUES,
                    ValidationConstants.ASSESSMENT_AVERAGE_SCORES)).addConstraintViolation();
            }
        }

        return violations;
    }

    // CS:ON

    // CS:OFF: ParameterAssignment
    private int checkScoreValues(AssessmentClientData data, ConstraintValidatorContext context, int violations)
    {
        Scale scale = null;

        try
        {
            AssessmentTemplate template = mAssessmentTemplateDao.getAssessmentTemplateByAnimalId(data.animalId);
            scale = template.getScale();
        }
        catch (AWNoSuchEntityException e)
        {
            violations++;
            context.buildConstraintViolationWithTemplate(e.getMessage()).addConstraintViolation();
        }

        if (scale == null)
        {
            return violations;
        }

        for (Entry<String, Map<String, AssessmentClientFactor>> parameter : data.score.entrySet())
        {
            for (Entry<String, AssessmentClientFactor> paramScore : parameter.getValue().entrySet())
            {
                AssessmentClientFactor factor = paramScore.getValue();

                if (factor.score < 0)
                {
                    violations++;

                    context
                        .buildConstraintViolationWithTemplate(String.format(ValidationConstants.ERR_NOT_NEGATIVE_VALUES,
                            ValidationConstants.ASSESSMENT_PARAMETER_SCORES))
                        .addConstraintViolation();
                }
                else if (factor.isIgnored)
                {
                    continue;
                }
                else if (factor.score == 0)
                {
                    violations++;

                    context
                        .buildConstraintViolationWithTemplate(ValidationConstants.ERR_ASSESSMENT_ZERO_NONIGNORED_SCORE)
                        .addConstraintViolation();
                }
                else if (factor.score < scale.getMin() || factor.score > scale.getMax())
                {
                    violations++;

                    context.buildConstraintViolationWithTemplate(String.format(
                        ValidationConstants.ERR_ASSESSMENT_SCORE_OUTSIDE_SCALE_FORMAT, scale.getMin(), scale.getMax()))
                        .addConstraintViolation();
                }
            }
        }

        return violations;
    }

    // CS:ON

    // CS:OFF: ParameterAssignment
    private int checkAveragesAndScoresCorrectness(AssessmentClientData data, ConstraintValidatorContext context,
        int violations)
    {
        Map<String, Double> recalculatedAvg = mAssessmentScoreCalculator.calculateParametersAverages(data);

        for (Entry<String, Double> average : recalculatedAvg.entrySet())
        {
            if (!average.getValue().equals(data.averageScores.get(average.getKey())))
            {
                violations++;

                context
                    .buildConstraintViolationWithTemplate(ValidationConstants.ERR_ASSESSMENT_SCORES_AVERAGES_INCORRECT)
                    .addConstraintViolation();
            }
        }

        return violations;
    }

    // CS:ON

    // CS:OFF: ParameterAssignment
    private int checkComments(AssessmentClientData data, ConstraintValidatorContext context, int violations)
    {
        for (Entry<String, String> commentEntry : data.parameterComments.entrySet())
        {
            String comment = commentEntry.getValue();

            if (comment == null)
            {
                violations++;

                context.buildConstraintViolationWithTemplate(
                    ValidationConstants.ERR_ASSESSMENT_PARAMETER_COMMENTS_NOT_NULL).addConstraintViolation();
            }
            else
            {
                if (comment.length() > ValidationConstants.EXTENDED_SIMPLE_TEXT_INPUT_SIZE_MAX
                    || comment.length() < ValidationConstants.SIMPLE_TEXT_INPUT_SIZE_MIN)
                {
                    violations++;

                    context.buildConstraintViolationWithTemplate(String.format(
                        ValidationConstants.TEXT_SIZE_CHECK_TEMPLATE, ValidationConstants.ASSESSMENT_PARAMETER_COMMENTS,
                        ValidationConstants.SIMPLE_TEXT_INPUT_SIZE_MIN,
                        ValidationConstants.EXTENDED_SIMPLE_TEXT_INPUT_SIZE_MAX)).addConstraintViolation();
                }

                if (!sIllegalCharactersPattern.matcher(comment).matches())
                {
                    violations++;

                    context.buildConstraintViolationWithTemplate(String.format(ValidationConstants.NAME_REGEX_TEMPLATE,
                        ValidationConstants.ASSESSMENT_PARAMETER_COMMENTS)).addConstraintViolation();
                }
            }
        }
        // CS:ON

        return violations;
    }
}
