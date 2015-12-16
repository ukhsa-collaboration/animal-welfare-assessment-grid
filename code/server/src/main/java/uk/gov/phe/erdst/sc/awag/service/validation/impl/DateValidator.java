package uk.gov.phe.erdst.sc.awag.service.validation.impl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import uk.gov.phe.erdst.sc.awag.service.validation.annotations.ValidDate;
import uk.gov.phe.erdst.sc.awag.service.validation.utils.ValidatorUtils;

public class DateValidator implements ConstraintValidator<ValidDate, String>
{
    @Override
    public void initialize(ValidDate arg0)
    {
    }

    @Override
    public boolean isValid(String date, ConstraintValidatorContext arg1)
    {
        return ValidatorUtils.isDateValid(date);
    }
}
