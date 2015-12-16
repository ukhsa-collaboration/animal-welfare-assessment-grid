package uk.gov.phe.erdst.sc.awag.service.validation.impl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import uk.gov.phe.erdst.sc.awag.service.validation.annotations.ValidId;
import uk.gov.phe.erdst.sc.awag.service.validation.utils.ValidationConstants;

public class IdValidator implements ConstraintValidator<ValidId, Long>
{
    @Override
    public void initialize(ValidId annotation)
    {
    }

    @Override
    public boolean isValid(Long id, ConstraintValidatorContext context)
    {
        if (id == null)
        {
            return false;
        }

        return id.equals(ValidationConstants.ENTITY_NEG_ID) || id >= ValidationConstants.ENTITY_MIN_ID;
    }
}
