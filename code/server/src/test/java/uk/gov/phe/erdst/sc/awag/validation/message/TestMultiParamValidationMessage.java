package uk.gov.phe.erdst.sc.awag.validation.message;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = {TestMultiParamMessageValidator.class})
@Target({ElementType.FIELD})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface TestMultiParamValidationMessage
{
    String message() default "Invalid mock message";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
