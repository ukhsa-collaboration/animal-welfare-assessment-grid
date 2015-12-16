package uk.gov.phe.erdst.sc.awag.service.validation.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import uk.gov.phe.erdst.sc.awag.service.validation.impl.DateValidator;
import uk.gov.phe.erdst.sc.awag.service.validation.utils.ValidationConstants;

@Documented
@Constraint(validatedBy = {DateValidator.class})
@Target({ElementType.FIELD})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface ValidDate
{
    String message() default ValidationConstants.VALID_DATE_ANNOT_DEFAULT_MSG;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
