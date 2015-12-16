package uk.gov.phe.erdst.sc.awag.service.validation.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import uk.gov.phe.erdst.sc.awag.service.validation.impl.IdValidator;
import uk.gov.phe.erdst.sc.awag.service.validation.utils.ValidationConstants;

@Documented
@Constraint(validatedBy = {IdValidator.class})
@Target({ElementType.FIELD})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface ValidId
{
    String message() default ValidationConstants.VALID_ID_ANNOT_DEFAULT_MESSAGE;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
