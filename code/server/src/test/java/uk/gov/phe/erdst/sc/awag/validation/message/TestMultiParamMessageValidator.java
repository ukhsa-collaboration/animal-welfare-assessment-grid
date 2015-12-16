package uk.gov.phe.erdst.sc.awag.validation.message;

import java.lang.annotation.Annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class TestMultiParamMessageValidator implements ConstraintValidator<Annotation, Object>
{

    @Override
    public void initialize(Annotation constraintAnnotation)
    {
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context)
    {
        return false;
    }

}
