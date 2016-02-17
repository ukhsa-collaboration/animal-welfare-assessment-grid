package uk.gov.phe.erdst.sc.awag.service.validation.impl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import uk.gov.phe.erdst.sc.awag.datamodel.client.ActivityLogSearchRequestParams;
import uk.gov.phe.erdst.sc.awag.service.validation.annotations.ValidActivityLogSearchRequest;
import uk.gov.phe.erdst.sc.awag.service.validation.utils.ValidatorUtils;

public class ActivityLogSearchRequestValidator implements
    ConstraintValidator<ValidActivityLogSearchRequest, ActivityLogSearchRequestParams>
{
    @Override
    public void initialize(ValidActivityLogSearchRequest arg0)
    {
    }

    @Override
    public boolean isValid(ActivityLogSearchRequestParams params, ConstraintValidatorContext context)
    {
        int errCount = ValidatorUtils.validateDateFromToFilter(params.dateFrom, params.dateTo, context);

        if (errCount > 0)
        {
            return false;
        }

        return true;
    }

}
