package uk.gov.phe.erdst.sc.awag.validation.custom;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import uk.gov.phe.erdst.sc.awag.datamodel.TestDateClientData;
import uk.gov.phe.erdst.sc.awag.shared.test.TestConstants;

@Test(groups = {TestConstants.TESTNG_UNIT_TESTS_GROUP})
public class DateValidatorTest
{
    private static final String VALID_DATE = "2013-02-01T00:00:00.000Z";
    private static final String INVALID_DATE = "2013aaaaaaa-02-01T00:00:00.000Z";
    private Validator mValidator;

    @BeforeClass
    private void setUp()
    {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        mValidator = validatorFactory.getValidator();
    }

    @Test
    private void testNullDate()
    {
        int expectedNoViolations = 1;
        TestDateClientData testDateClientData = new TestDateClientData(null);
        Set<ConstraintViolation<TestDateClientData>> testConstraintViolations = mValidator.validate(testDateClientData);
        Assert.assertEquals(expectedNoViolations, testConstraintViolations.size());
    }

    @Test
    private void testInvalidDate()
    {
        int expectedNoViolations = 1;
        TestDateClientData testDateClientData = new TestDateClientData(INVALID_DATE);
        Set<ConstraintViolation<TestDateClientData>> testConstraintViolations = mValidator.validate(testDateClientData);
        Assert.assertEquals(expectedNoViolations, testConstraintViolations.size());
    }

    @Test
    private void testValidDate()
    {
        int expectedNoViolations = 0;
        TestDateClientData testDateClientData = new TestDateClientData(VALID_DATE);
        Set<ConstraintViolation<TestDateClientData>> testConstraintViolations = mValidator.validate(testDateClientData);
        Assert.assertEquals(expectedNoViolations, testConstraintViolations.size());
    }
}
