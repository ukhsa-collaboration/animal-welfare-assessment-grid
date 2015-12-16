package uk.gov.phe.erdst.sc.awag.validation.message;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.validation.Configuration;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.NotNull;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import uk.gov.phe.erdst.sc.awag.service.validation.annotations.ValidId;
import uk.gov.phe.erdst.sc.awag.service.validation.message.CustomMessageInterpolator;
import uk.gov.phe.erdst.sc.awag.service.validation.utils.ValidationConstants;
import uk.gov.phe.erdst.sc.awag.shared.test.TestConstants;

@Test(groups = {TestConstants.TESTNG_UNIT_TESTS_GROUP})
public class CustomMessageInterpolatorTest
{
    private Validator mValidator;

    @BeforeMethod
    public void setUp() throws Exception
    {
        Configuration<?> configuration = Validation.byDefaultProvider().configure();
        configuration.messageInterpolator(new CustomMessageInterpolator(configuration.getDefaultMessageInterpolator()));
        ValidatorFactory factory = configuration.buildValidatorFactory();
        mValidator = factory.getValidator();
    }

    @Test
    public void testSingleParameterInterpolation()
    {
        Set<ConstraintViolation<AnnotatedTestClass>> violations = mValidator.validate(new AnnotatedTestClass());

        Object templateKey = ValidationConstants.ValidIdRange.class;

        List<String> messages = getViolationsMessages(violations, "id");
        List<String> expected = Arrays
            .asList(String.format(ValidationConstants.getAnnotationTemplate(templateKey), "AnnotatedTestClass"));

        Assert.assertEquals(messages, expected);
    }

    @Test
    public void testNoParameterInterpolation()
    {
        Set<ConstraintViolation<AnnotatedTestClass>> violations = mValidator.validate(new AnnotatedTestClass());

        List<String> messages = getViolationsMessages(violations, "secondaryId");
        List<String> expected = Arrays.asList(ValidationConstants.VALID_ID_ANNOT_DEFAULT_MESSAGE);

        Assert.assertEquals(messages, expected);
    }

    @Test
    public void testMultipleParametersInterpolation()
    {
        Set<ConstraintViolation<AnnotatedTestClass>> violations = mValidator.validate(new AnnotatedTestClass());

        Object templateKey = ValidationConstants.TestMultiParamMessage.class;

        List<String> messages = getViolationsMessages(violations, "name");
        List<String> expected = Arrays.asList(
            String.format(ValidationConstants.getAnnotationTemplate(templateKey), "ParamA", "ParamB", "ParamC"));

        Assert.assertEquals(messages, expected);
    }

    @Test
    public void testUnsupportedAnnotationInterpolation()
    {
        Set<ConstraintViolation<AnnotatedTestClass>> violations = mValidator.validate(new AnnotatedTestClass());

        List<String> messages = getViolationsMessages(violations, "number");
        List<String> expected = Arrays.asList(ValidationConstants.NO_ERR_TEMPLATE);

        Assert.assertEquals(messages, expected);
    }

    @Test
    public void testNoTemplateSpecifiedViaPayload()
    {
        Set<ConstraintViolation<AnnotatedTestClass>> violations = mValidator.validate(new AnnotatedTestClass());

        List<String> messages = getViolationsMessages(violations, "address");
        List<String> expected = Arrays.asList("Non default message");

        Assert.assertEquals(messages, expected);
    }

    private static List<String> getViolationsMessages(Set<ConstraintViolation<AnnotatedTestClass>> violations,
        String fieldName)
    {
        List<String> messages = new ArrayList<String>();

        for (ConstraintViolation<AnnotatedTestClass> violation : violations)
        {
            if (violation.getPropertyPath().toString().equals(fieldName))
            {
                messages.add(violation.getMessage());
            }
        }

        return messages;
    }

    // CS:OFF: VisibilityModifier
    private static class AnnotatedTestClass
    {
        @ValidId(message = "AnnotatedTestClass", payload = ValidationConstants.ValidIdRange.class)
        public Long id;

        @ValidId
        public Long secondaryId;

        @TestMultiParamValidationMessage(message = "ParamA|ParamB|ParamC",
            payload = ValidationConstants.TestMultiParamMessage.class)
        public String name;

        @TestUnconfiguredValidationMessage(message = "Does not matter here",
            payload = ValidationConstants.TestUnconfiguredTemplate.class)
        public String number;

        @NotNull(message = "Non default message")
        public String address;
    }
    // CS:ON
}
