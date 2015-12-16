package uk.gov.phe.erdst.sc.awag.service.validation.impl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import uk.gov.phe.erdst.sc.awag.datamodel.client.AssessmentSearchRequestParams;
import uk.gov.phe.erdst.sc.awag.service.validation.annotations.ValidAssessmentSearchRequest;
import uk.gov.phe.erdst.sc.awag.service.validation.utils.ValidationConstants;
import uk.gov.phe.erdst.sc.awag.service.validation.utils.ValidatorUtils;

public class AssessmentSearchRequestValidator
    implements ConstraintValidator<ValidAssessmentSearchRequest, AssessmentSearchRequestParams>
{
    @Override
    public void initialize(ValidAssessmentSearchRequest arg0)
    {
    }

    @Override
    public boolean isValid(AssessmentSearchRequestParams params, ConstraintValidatorContext context)
    {
        int errCount = 0;
        context.disableDefaultConstraintViolation();

        if (params.dateTo != null && params.dateFrom != null)
        {
            if (ValidatorUtils.isDateValid(params.dateTo) && ValidatorUtils.isDateValid(params.dateFrom))
            {
                boolean isEqual = params.dateTo.equals(params.dateFrom);
                if (!isEqual && !ValidatorUtils.isFirstDateAfterSecondDate(params.dateTo, params.dateFrom))
                {
                    context.buildConstraintViolationWithTemplate(ValidationConstants.ERR_FROM_TO_DATE_PARAMS)
                        .addConstraintViolation();
                    errCount++;
                }
            }
        }

        if (params.dateFrom != null && !ValidatorUtils.isDateValid(params.dateFrom))
        {
            context
                .buildConstraintViolationWithTemplate(
                    String.format(ValidationConstants.ERR_DATE_FORMAT, ValidationConstants.DATE_FROM))
                .addConstraintViolation();
            errCount++;
        }

        if (params.dateTo != null && !ValidatorUtils.isDateValid(params.dateTo))
        {
            context
                .buildConstraintViolationWithTemplate(
                    String.format(ValidationConstants.ERR_DATE_FORMAT, ValidationConstants.DATE_TO))
                .addConstraintViolation();
            errCount++;
        }

        if (errCount > 0)
        {
            return false;
        }

        return true;
    }
}
