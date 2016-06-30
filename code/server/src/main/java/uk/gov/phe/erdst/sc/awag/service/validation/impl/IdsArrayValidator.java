package uk.gov.phe.erdst.sc.awag.service.validation.impl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import uk.gov.phe.erdst.sc.awag.service.validation.annotations.ValidIdsArray;
import uk.gov.phe.erdst.sc.awag.service.validation.utils.ValidationConstants;

public class IdsArrayValidator implements ConstraintValidator<ValidIdsArray, Long[]>
{
    @Override
    public void initialize(ValidIdsArray annotation)
    {
    }

    @Override
    public boolean isValid(Long[] ids, ConstraintValidatorContext context)
    {
        for (Long id : ids)
        {
            if (!isIdValid(id))
            {
                return false;
            }
        }

        return true;
    }

    private boolean isIdValid(Long id)
    {
        if (id == null)
        {
            return false;
        }

        return id >= ValidationConstants.ENTITY_MIN_ID;
    }
}
