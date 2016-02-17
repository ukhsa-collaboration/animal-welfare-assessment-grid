package uk.gov.phe.erdst.sc.awag.validation.utils;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.validation.Configuration;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintValidatorFactory;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.testng.Assert;

import uk.gov.phe.erdst.sc.awag.service.validation.utils.ValidationConstants;
import uk.gov.phe.erdst.sc.awag.utils.StringHelper;

public final class ValidationTestHelper
{
    public static final String TOO_LONG_SIMPLE_TEXT = StringHelper
        .getStringOfLength(ValidationConstants.SIMPLE_TEXT_INPUT_SIZE_MAX + 1);
    public static final String TOO_SHORT_SIMPLE_TEXT = StringHelper
        .getStringOfLength(ValidationConstants.SIMPLE_TEXT_INPUT_SIZE_MIN - 1);
    public static final String TOO_LONG_EXTENDED_TEXT = StringHelper
        .getStringOfLength(ValidationConstants.EXTENDED_SIMPLE_TEXT_INPUT_SIZE_MAX + 1);

    public static final Long ENTITY_INVALID_NEGATIVE_ID = ValidationConstants.ENTITY_NEG_ID - 1;
    public static final Long ENTITY_INVALID_ZERO_ID = ValidationConstants.ENTITY_MIN_ID - 1;
    public static final String INVALID_FORMAT_SIMPLE_TEXT = "Text$*&SHS^&*(";
    public static final String INVALID_DATE = "2013-02-01";

    private ValidationTestHelper()
    {
    }

    public static int validate(Validator validator, Object toValidate, Class<?>... validationGroups)
    {
        Set<ConstraintViolation<Object>> result = validator.validate(toValidate, validationGroups);
        return result.size();
    }

    public static <T> void run(Collection<ValidationTest> tests, Class<T> validDataClass)
    {
        for (ValidationTest test : tests)
        {
            run(test, validDataClass);
        }
    }

    public static <T> void run(ValidationTest test, Class<T> validDataClass)
    {
        ValidationTestResult result = validateAttribute(test, validDataClass);
        Assert.assertEquals(result.noOfViolations, test.expectedViolations);
    }

    public static <T> void testMainEntityIdAttribute(Object validData, String attributeName, Class<T> validDataClass,
        Validator validator, Class<?>... validationGroups)
    {
        final int expectedViolations = 4;

        List<Long> valuesToTest = Arrays.asList(null, ENTITY_INVALID_NEGATIVE_ID, ENTITY_INVALID_ZERO_ID);

        ValidationTest test = new ValidationTest(expectedViolations, validData, attributeName, valuesToTest, validator,
            validationGroups);

        run(test, validDataClass);
    }

    public static <T> void testCompositeEntityIdAttribute(Object validData, String attributeName,
        Class<T> validDataClass, Validator validator, Class<?>... validationGroups)
    {
        final int expectedViolations = 3;

        List<Long> valuesToTest = Arrays.asList(null, ValidationConstants.ENTITY_NEG_ID, ENTITY_INVALID_ZERO_ID);

        ValidationTest test = new ValidationTest(expectedViolations, validData, attributeName, valuesToTest, validator,
            validationGroups);

        run(test, validDataClass);
    }

    public static <T> void testSimpleTextAttribute(Object validData, String attributeName, Class<T> validDataClass,
        Validator validator, Class<?>... validationGroups)
    {
        final int expectedViolations = 4;

        List<String> valuesToTest = Arrays.asList(null, TOO_SHORT_SIMPLE_TEXT, TOO_LONG_SIMPLE_TEXT,
            INVALID_FORMAT_SIMPLE_TEXT);

        ValidationTest test = new ValidationTest(expectedViolations, validData, attributeName, valuesToTest, validator,
            validationGroups);

        run(test, validDataClass);
    }

    private static <T> ValidationTestResult validateAttribute(ValidationTest test, Class<T> validDataClass)
    {
        int noOfViolations = 0;

        for (Object valueToTest : test.valuesToTest)
        {
            Set<ConstraintViolation<T>> violations = test.validator.validateValue(validDataClass, test.attributeName,
                valueToTest, test.validationGroups);

            noOfViolations += violations.size();
        }

        return new ValidationTestResult(noOfViolations);
    }

    public static ValidatorFactory createValidatorFactory(ValidatorProvider provider)
    {
        Configuration<?> config = Validation.byDefaultProvider().configure();

        config.constraintValidatorFactory(new CustomConstraintValidatorFactory(config
            .getDefaultConstraintValidatorFactory(), provider));

        return config.buildValidatorFactory();
    }

    public interface ValidatorProvider
    {
        <T extends ConstraintValidator<?, ?>> T getInstance(Class<T> key);
    }

    private static class CustomConstraintValidatorFactory implements ConstraintValidatorFactory
    {
        private ConstraintValidatorFactory mDefaultFactory;
        private ValidatorProvider mValidatorProvider;

        public CustomConstraintValidatorFactory(ConstraintValidatorFactory defaultConstraintValidatorFactory,
            ValidatorProvider validatorProvider)
        {
            mDefaultFactory = defaultConstraintValidatorFactory;
            mValidatorProvider = validatorProvider;
        }

        @Override
        public <T extends ConstraintValidator<?, ?>> T getInstance(Class<T> key)
        {
            T instance = mValidatorProvider.getInstance(key);

            if (instance != null)
            {
                return instance;
            }

            return mDefaultFactory.getInstance(key);
        }

        @Override
        public void releaseInstance(ConstraintValidator<?, ?> instance)
        {
            mDefaultFactory.releaseInstance(instance);
        }

    }

    public static class MockConstraintValidatorContext implements ConstraintValidatorContext
    {
        @Override
        public ConstraintViolationBuilder buildConstraintViolationWithTemplate(String messageTemplate)
        {
            return new ConstraintViolationBuilder() {
                @Override
                public LeafNodeBuilderCustomizableContext addBeanNode()
                {
                    return null;
                }

                @Override
                public ConstraintValidatorContext addConstraintViolation()
                {
                    return null;
                }

                @Override
                public NodeBuilderDefinedContext addNode(String name)
                {
                    return null;
                }

                @Override
                public NodeBuilderDefinedContext addParameterNode(int index)
                {
                    return null;
                }

                @Override
                public NodeBuilderCustomizableContext addPropertyNode(String name)
                {
                    return null;
                }
            };
        }

        @Override
        public void disableDefaultConstraintViolation()
        {
        }

        @Override
        public String getDefaultConstraintMessageTemplate()
        {
            return null;
        }

        @Override
        public <T> T unwrap(Class<T> type)
        {
            return null;
        }
    }
}
