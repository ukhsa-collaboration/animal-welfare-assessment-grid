package uk.gov.phe.erdst.sc.awag.validation.factor;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import uk.gov.phe.erdst.sc.awag.datamodel.client.FactorClientData;
import uk.gov.phe.erdst.sc.awag.shared.test.TestConstants;
import uk.gov.phe.erdst.sc.awag.utils.GuiceHelper;
import uk.gov.phe.erdst.sc.awag.utils.StringHelper;

@Test(groups = {TestConstants.TESTNG_UNIT_TESTS_GROUP})
public class FactorValidationTest
{
    private static final String VALID_FACTOR_NAME = "Factor 1";
    private static final String INVALID_PATTERN_FACTOR_NAME = "Fa$*&SHS^&*(";
    private static final String INVALID_MIN_FACTOR_NAME = "F";
    private static final String INVALID_MAX_FACTOR_NAME;
    private static final String INVALID_NULL_FACTOR_NAME = null;
    private Validator mValidator;
    private FactorClientData mFactorClientData;

    static
    {
        final int invalidNameLength = 256;
        INVALID_MAX_FACTOR_NAME = StringHelper.getStringOfLength(invalidNameLength);
    }

    @BeforeClass
    private void setUp()
    {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        mValidator = validatorFactory.getValidator();
    }

    @BeforeMethod
    private void resetValidFactorClientData()
    {
        mFactorClientData = new FactorClientData(-1L, VALID_FACTOR_NAME);
        GuiceHelper.injectTestDependencies(this);
    }

    @Test
    private void testValidFactorClientData()
    {
        int expectedNoViolations = 0;
        Set<ConstraintViolation<FactorClientData>> constraintViolations = mValidator.validate(mFactorClientData);
        Assert.assertEquals(constraintViolations.size(), expectedNoViolations);
    }

    @Test
    private void testPatternInvalidFactorClientData()
    {
        int expectedNoViolations = 1;
        mFactorClientData.factorName = INVALID_PATTERN_FACTOR_NAME;
        Set<ConstraintViolation<FactorClientData>> constraintViolations = mValidator.validate(mFactorClientData);
        Assert.assertEquals(constraintViolations.size(), expectedNoViolations);
    }

    @Test
    private void testMaxInvalidFactorClientData()
    {
        int expectedNoViolations = 1;
        mFactorClientData.factorName = INVALID_MAX_FACTOR_NAME;
        Set<ConstraintViolation<FactorClientData>> constraintViolations = mValidator.validate(mFactorClientData);
        Assert.assertEquals(constraintViolations.size(), expectedNoViolations);
    }

    @Test
    private void testMinInvalidFactorClientData()
    {
        int expectedNoViolations = 1;
        mFactorClientData.factorName = INVALID_MIN_FACTOR_NAME;
        Set<ConstraintViolation<FactorClientData>> constraintViolations = mValidator.validate(mFactorClientData);
        Assert.assertEquals(constraintViolations.size(), expectedNoViolations);
    }

    @Test
    private void testNullInvalidFactorClientData()
    {
        int expectedNoViolations = 1;
        mFactorClientData.factorName = INVALID_NULL_FACTOR_NAME;
        Set<ConstraintViolation<FactorClientData>> constraintViolations = mValidator.validate(mFactorClientData);
        Assert.assertEquals(constraintViolations.size(), expectedNoViolations);
    }
}
