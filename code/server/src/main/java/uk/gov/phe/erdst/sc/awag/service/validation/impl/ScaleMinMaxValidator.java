package uk.gov.phe.erdst.sc.awag.service.validation.impl;

import javax.ejb.Stateless;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import uk.gov.phe.erdst.sc.awag.datamodel.client.ScaleClientData;
import uk.gov.phe.erdst.sc.awag.service.validation.annotations.ValidScaleMinMax;
import uk.gov.phe.erdst.sc.awag.service.validation.utils.ValidationConstants;

@Stateless
public class ScaleMinMaxValidator implements ConstraintValidator<ValidScaleMinMax, ScaleClientData>
{
    @Override
    public void initialize(ValidScaleMinMax arg0)
    {
    }

    @Override
    public boolean isValid(ScaleClientData clientData, ConstraintValidatorContext context)
    {
        context.disableDefaultConstraintViolation();
        Integer min = clientData.scaleMin;
        Integer max = clientData.scaleMax;

        if (min != null && max != null)
        {
            if (min > max)
            {
                context.buildConstraintViolationWithTemplate(ValidationConstants.ERR_SCALE_MIN_MAX)
                    .addConstraintViolation();
                return false;
            }
        }
        return true;
    }
}
