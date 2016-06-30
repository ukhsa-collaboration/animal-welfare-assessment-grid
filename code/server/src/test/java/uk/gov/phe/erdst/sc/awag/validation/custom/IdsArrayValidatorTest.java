package uk.gov.phe.erdst.sc.awag.validation.custom;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import uk.gov.phe.erdst.sc.awag.datamodel.TestIdsArrayClientData;
import uk.gov.phe.erdst.sc.awag.shared.test.TestConstants;

@Test(groups = {TestConstants.TESTNG_UNIT_TESTS_GROUP})
public class IdsArrayValidatorTest
{
    private static final Long INVALID_NEGATIVE_ID = -2L;
    private static final Long SPECIAL_CASE_INVALID_NEGATIVE_ID = -1L;
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
        TestIdsArrayClientData testClientData = new TestIdsArrayClientData(VALID_POSITIVE_ID, null);
        Set<ConstraintViolation<TestIdsArrayClientData>> testConstraintViolations = mValidator.validate(testClientData);
        Assert.assertEquals(testConstraintViolations.size(), expectedNoOfViolations);
    }

    @Test
    private void testInvalidNegativeId()
    {
        int expectedNoOfViolations = 1;
        TestIdsArrayClientData testClientData = new TestIdsArrayClientData(VALID_POSITIVE_ID, INVALID_NEGATIVE_ID);
        Set<ConstraintViolation<TestIdsArrayClientData>> testConstraintViolations = mValidator.validate(testClientData);
        Assert.assertEquals(testConstraintViolations.size(), expectedNoOfViolations);
    }

    @Test
    private void testSpecialCaseInvalidNegativeId()
    {
        int expectedNoOfViolations = 1;
        TestIdsArrayClientData testClientData = new TestIdsArrayClientData(VALID_POSITIVE_ID,
            SPECIAL_CASE_INVALID_NEGATIVE_ID);
        Set<ConstraintViolation<TestIdsArrayClientData>> testConstraintViolations = mValidator.validate(testClientData);
        Assert.assertEquals(testConstraintViolations.size(), expectedNoOfViolations);
    }

    @Test
    private void testInvalidZeroId()
    {
        int expectedNoOfViolations = 1;
        TestIdsArrayClientData testClientData = new TestIdsArrayClientData(VALID_POSITIVE_ID, INVALID_ZERO_ID);
        Set<ConstraintViolation<TestIdsArrayClientData>> testConstraintViolations = mValidator.validate(testClientData);
        Assert.assertEquals(testConstraintViolations.size(), expectedNoOfViolations);
    }

    @Test
    private void testValidPositiveId()
    {
        int expectedNoOfViolations = 0;
        TestIdsArrayClientData testClientData = new TestIdsArrayClientData(VALID_POSITIVE_ID, VALID_POSITIVE_ID);
        Set<ConstraintViolation<TestIdsArrayClientData>> testConstraintViolations = mValidator.validate(testClientData);
        Assert.assertEquals(testConstraintViolations.size(), expectedNoOfViolations);
    }
}
