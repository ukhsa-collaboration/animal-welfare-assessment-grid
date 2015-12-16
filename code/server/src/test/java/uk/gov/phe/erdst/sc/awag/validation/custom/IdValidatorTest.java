package uk.gov.phe.erdst.sc.awag.validation.custom;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import uk.gov.phe.erdst.sc.awag.datamodel.TestIdClientData;
import uk.gov.phe.erdst.sc.awag.shared.test.TestConstants;

@Test(groups = {TestConstants.TESTNG_UNIT_TESTS_GROUP})
public class IdValidatorTest
{
    private static final Long INVALID_NEGATIVE_ID = -2L;
    private static final Long VALID_NEGATIVE_ID = -1L;
    private static final Long INVALID_ZERO_ID = 0L;
    private static final Long VALID_POSITIVE_ID = 1L;
    private Validator mValidator;

    @BeforeClass
    private void setUp()
    {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        mValidator = validatorFactory.getValidator();
    }

    @Test
    private void testNullId()
    {
        int expectedNoOfViolations = 1;
        TestIdClientData testClientData = new TestIdClientData(null);
        Set<ConstraintViolation<TestIdClientData>> testConstraintViolations = mValidator.validate(testClientData);
        Assert.assertEquals(testConstraintViolations.size(), expectedNoOfViolations);
    }

    @Test
    private void testInvalidNegativeId()
    {
        int expectedNoOfViolations = 1;
        TestIdClientData testClientData = new TestIdClientData(INVALID_NEGATIVE_ID);
        Set<ConstraintViolation<TestIdClientData>> testConstraintViolations = mValidator.validate(testClientData);
        Assert.assertEquals(testConstraintViolations.size(), expectedNoOfViolations);
    }

    @Test
    private void testValidNegativeId()
    {
        int expectedNoOfViolations = 0;
        TestIdClientData testClientData = new TestIdClientData(VALID_NEGATIVE_ID);
        Set<ConstraintViolation<TestIdClientData>> testConstraintViolations = mValidator.validate(testClientData);
        Assert.assertEquals(testConstraintViolations.size(), expectedNoOfViolations);
    }

    @Test
    private void testInvalidZeroId()
    {
        int expectedNoOfViolations = 1;
        TestIdClientData testClientData = new TestIdClientData(INVALID_ZERO_ID);
        Set<ConstraintViolation<TestIdClientData>> testConstraintViolations = mValidator.validate(testClientData);
        Assert.assertEquals(testConstraintViolations.size(), expectedNoOfViolations);
    }

    @Test
    private void testValidPositiveId()
    {
        int expectedNoOfViolations = 0;
        TestIdClientData testClientData = new TestIdClientData(VALID_POSITIVE_ID);
        Set<ConstraintViolation<TestIdClientData>> testConstraintViolations = mValidator.validate(testClientData);
        Assert.assertEquals(testConstraintViolations.size(), expectedNoOfViolations);
    }
}
