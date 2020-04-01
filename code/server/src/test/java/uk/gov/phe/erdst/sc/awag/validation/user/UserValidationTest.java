package uk.gov.phe.erdst.sc.awag.validation.user;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import uk.gov.phe.erdst.sc.awag.shared.test.TestConstants;
import uk.gov.phe.erdst.sc.awag.utils.GuiceHelper;
import uk.gov.phe.erdst.sc.awag.utils.StringHelper;
import uk.gov.phe.erdst.sc.awag.webapi.request.UserClientData;

@Test(groups = {TestConstants.TESTNG_UNIT_TESTS_GROUP})
public class UserValidationTest
{
    private static final String VALID_USER_NAME = "Technician 1";
    private static final String INVALID_PATTERN_USER_NAME = "User$*&SHS^&*(";
    private static final String INVALID_MIN_USER_NAME = "T";
    private static final String INVALID_MAX_USER_NAME;
    private static final String INVALID_NULL_USER_NAME = null;
    private Validator mValidator;
    private UserClientData mUserClientData;

    static
    {
        final int invalidNameLength = 256;
        INVALID_MAX_USER_NAME = StringHelper.getStringOfLength(invalidNameLength);
    }

    @BeforeClass
    private void setUp()
    {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        mValidator = validatorFactory.getValidator();
    }

    @BeforeMethod
    private void resetValidClientData()
    {
        mUserClientData = new UserClientData(-1L, VALID_USER_NAME);
        GuiceHelper.injectTestDependencies(this);
    }

    @Test
    private void testValidClientData()
    {
        int expectedNoViolations = 0;
        Set<ConstraintViolation<UserClientData>> constraintViolations = mValidator.validate(mUserClientData);
        Assert.assertEquals(constraintViolations.size(), expectedNoViolations);
    }

    @Test
    private void testPatternInvalidClientData()
    {
        int expectedNoViolations = 1;
        mUserClientData.userName = INVALID_PATTERN_USER_NAME;
        Set<ConstraintViolation<UserClientData>> constraintViolations = mValidator.validate(mUserClientData);
        Assert.assertEquals(constraintViolations.size(), expectedNoViolations);
    }

    @Test
    private void testMaxInvalidClientData()
    {
        int expectedNoViolations = 1;
        mUserClientData.userName = INVALID_MAX_USER_NAME;
        Set<ConstraintViolation<UserClientData>> constraintViolations = mValidator.validate(mUserClientData);
        Assert.assertEquals(constraintViolations.size(), expectedNoViolations);
    }

    @Test
    private void testMinInvalidClientData()
    {
        int expectedNoViolations = 1;
        mUserClientData.userName = INVALID_MIN_USER_NAME;
        Set<ConstraintViolation<UserClientData>> constraintViolations = mValidator.validate(mUserClientData);
        Assert.assertEquals(constraintViolations.size(), expectedNoViolations);
    }

    @Test
    private void testNullInvalidClientData()
    {
        int expectedNoViolations = 1;
        mUserClientData.userName = INVALID_NULL_USER_NAME;
        Set<ConstraintViolation<UserClientData>> constraintViolations = mValidator.validate(mUserClientData);
        Assert.assertEquals(constraintViolations.size(), expectedNoViolations);
    }
}
