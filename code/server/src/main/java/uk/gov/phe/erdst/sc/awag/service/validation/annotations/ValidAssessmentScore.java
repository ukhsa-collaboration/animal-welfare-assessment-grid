package uk.gov.phe.erdst.sc.awag.service.validation.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import uk.gov.phe.erdst.sc.awag.service.validation.impl.AssessmentScoreValidator;
import uk.gov.phe.erdst.sc.awag.service.validation.utils.ValidationConstants;

@Constraint(validatedBy = {AssessmentScoreValidator.class})
@Target({ElementType.TYPE})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface ValidAssessmentScore
{
    String message() default ValidationConstants.VALID_ASSESSMENT_SCORE_ANNOT_DEFAULT_MSG;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
