package uk.gov.phe.erdst.sc.awag.validation.source;

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
import uk.gov.phe.erdst.sc.awag.utils.StringHelper;
import uk.gov.phe.erdst.sc.awag.webapi.request.SourceClientData;

@Test(groups = {TestConstants.TESTNG_UNIT_TESTS_GROUP})
public class SourceValidationTest
{
    private static final String VALID_SOURCE_NAME = "Source 1";
    private static final String INVALID_PATTERN_SOURCE_NAME = "Sou$*&SHS^&*(";
    private static final String INVALID_MIN_SOURCE_NAME = "S";
    private static final String INVALID_MAX_SOURCE_NAME;
    private static final String INVALID_NULL_SOURCE_NAME = null;
    private Validator mValidator;
    private SourceClientData mSourcesClientData;

    static
    {
        final int invalidNameLength = 256;
        INVALID_MAX_SOURCE_NAME = StringHelper.getStringOfLength(invalidNameLength);
    }

    @BeforeClass
    private void setUp()
    {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        mValidator = validatorFactory.getValidator();
    }

    @BeforeMethod
    private void resetValidSourceClientData()
    {
        mSourcesClientData = new SourceClientData(-1L, VALID_SOURCE_NAME);
    }

    @Test
    private void testValidSourceClientData()
    {
        int expectedNoViolations = 0;
        Set<ConstraintViolation<SourceClientData>> constraintViolations = mValidator.validate(mSourcesClientData);
        Assert.assertEquals(constraintViolations.size(), expectedNoViolations);
    }

    @Test
    private void testPatternInvalidSourceClientData()
    {
        int expectedNoViolations = 1;
        mSourcesClientData.sourceName = INVALID_PATTERN_SOURCE_NAME;
        Set<ConstraintViolation<SourceClientData>> constraintViolations = mValidator.validate(mSourcesClientData);
        Assert.assertEquals(constraintViolations.size(), expectedNoViolations);
    }

    @Test
    private void testMinInvalidSourceClientData()
    {
        int expectedNoViolations = 1;
        mSourcesClientData.sourceName = INVALID_MIN_SOURCE_NAME;
        Set<ConstraintViolation<SourceClientData>> constraintViolations = mValidator.validate(mSourcesClientData);
        Assert.assertEquals(constraintViolations.size(), expectedNoViolations);
    }

    @Test
    private void testMaxInvalidSourceClientData()
    {
        int expectedNoViolations = 1;
        mSourcesClientData.sourceName = INVALID_MAX_SOURCE_NAME;
        Set<ConstraintViolation<SourceClientData>> constraintViolations = mValidator.validate(mSourcesClientData);
        Assert.assertEquals(constraintViolations.size(), expectedNoViolations);
    }

    @Test
    private void testNullInvalidSourceClientData()
    {
        int expectedNoViolations = 1;
        mSourcesClientData.sourceName = INVALID_NULL_SOURCE_NAME;
        Set<ConstraintViolation<SourceClientData>> constraintViolations = mValidator.validate(mSourcesClientData);
        Assert.assertEquals(constraintViolations.size(), expectedNoViolations);
    }
}
