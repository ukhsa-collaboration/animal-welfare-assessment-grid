package uk.gov.phe.erdst.sc.awag.validation.utils;

import java.util.Collection;

import javax.validation.Validator;

public class ValidationTest
{
    // CS:OFF: HiddenFieldCheck|VisibilityModifier
    public final int expectedViolations;
    public final Object validData;
    public final String attributeName;
    public final Collection<? extends Object> valuesToTest;
    public final Validator validator;
    public final Class<?>[] validationGroups;

    public ValidationTest(int expectedViolations, Object validData, String attributeName,
        Collection<? extends Object> valuesToTest, Validator validator, Class<?>... validationGroups)
    {
        this.expectedViolations = expectedViolations;
        this.validData = validData;
        this.attributeName = attributeName;
        this.valuesToTest = valuesToTest;
        this.validator = validator;
        this.validationGroups = validationGroups;
    }

    // CS:ON
}
