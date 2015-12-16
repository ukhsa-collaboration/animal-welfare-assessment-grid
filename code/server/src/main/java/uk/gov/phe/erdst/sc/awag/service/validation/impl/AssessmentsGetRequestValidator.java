package uk.gov.phe.erdst.sc.awag.service.validation.impl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import uk.gov.phe.erdst.sc.awag.datamodel.client.AssessmentsGetRequestParams;
import uk.gov.phe.erdst.sc.awag.service.validation.annotations.ValidAssessmentsGetRequest;
import uk.gov.phe.erdst.sc.awag.service.validation.utils.ValidationConstants;
import uk.gov.phe.erdst.sc.awag.service.validation.utils.ValidatorUtils;

public class AssessmentsGetRequestValidator implements
    ConstraintValidator<ValidAssessmentsGetRequest, AssessmentsGetRequestParams>
{
    @Override
    public void initialize(ValidAssessmentsGetRequest arg0)
    {
    }

    @Override
    public boolean isValid(AssessmentsGetRequestParams assessmentsGetRequestParams, ConstraintValidatorContext context)
    {
        int errCount = 0;
        context.disableDefaultConstraintViolation();

        if (!ValidatorUtils.isFirstDateAfterSecondDate(assessmentsGetRequestParams.dateTo,
            assessmentsGetRequestParams.dateFrom))
        {
            context.buildConstraintViolationWithTemplate(ValidationConstants.ERR_ASSESSMENTS_GET_DATE_PARAMS)
                .addConstraintViolation();
            errCount++;
        }

        if (!ValidatorUtils.isDateValid(assessmentsGetRequestParams.dateFrom))
        {
            context.buildConstraintViolationWithTemplate(
                String.format(ValidationConstants.ERR_DATE_FORMAT, ValidationConstants.DATE_FROM))
                .addConstraintViolation();
            errCount++;
        }

        if (!ValidatorUtils.isDateValid(assessmentsGetRequestParams.dateTo))
        {
            context.buildConstraintViolationWithTemplate(
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
