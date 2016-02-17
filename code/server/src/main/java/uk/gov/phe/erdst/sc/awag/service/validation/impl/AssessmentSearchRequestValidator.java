package uk.gov.phe.erdst.sc.awag.service.validation.impl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import uk.gov.phe.erdst.sc.awag.datamodel.client.AssessmentSearchRequestParams;
import uk.gov.phe.erdst.sc.awag.service.validation.annotations.ValidAssessmentSearchRequest;
import uk.gov.phe.erdst.sc.awag.service.validation.utils.ValidatorUtils;

public class AssessmentSearchRequestValidator implements
    ConstraintValidator<ValidAssessmentSearchRequest, AssessmentSearchRequestParams>
{
    @Override
    public void initialize(ValidAssessmentSearchRequest arg0)
    {
    }

    @Override
    public boolean isValid(AssessmentSearchRequestParams params, ConstraintValidatorContext context)
    {
        int errCount = ValidatorUtils.validateDateFromToFilter(params.dateFrom, params.dateTo, context);

        if (errCount > 0)
        {
            return false;
        }

        return true;
    }

}
