package uk.gov.phe.erdst.sc.awag.validation.scale;

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
import uk.gov.phe.erdst.sc.awag.webapi.request.ScaleClientData;

@Test(groups = {TestConstants.TESTNG_UNIT_TESTS_GROUP})
public class ScaleValidationTest
{
    private static final String VALID_SCALE_NUMBER = "1 to 10";
    private static final String INVALID_PATTERN_SCALE_NUMBER = "$*&SHS^&*(";
    private static final String INVALID_NULL_SCALE_NUMBER = null;
    private static final Integer INVALID_SCALE_MIN = 10;
    private static final Integer INVALID_SCALE_MAX = 0;
    private static final Integer INVALID_SCALE_MIN_LESS_THAN_ZERO = -10;
    private static final Integer INVALID_SCALE_MAX_LESS_THAN_ZERO = -1;

    private Validator mValidator;
    private ScaleClientData mScaleClientData;

    @BeforeClass
    private void setUp()
    {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        mValidator = validatorFactory.getValidator();
    }

    @BeforeMethod
    private void resetValidScaleClientData()
    {
        mScaleClientData = new ScaleClientData(-1L, VALID_SCALE_NUMBER, 1, 10);
    }

    @Test
    private void testValidScale()
    {
        int expectedNoViolations = 0;
        Set<ConstraintViolation<ScaleClientData>> constraintViolations = mValidator.validate(mScaleClientData);
        Assert.assertEquals(constraintViolations.size(), expectedNoViolations);
    }

    @Test
    private void testInvalidScaleNumberNull()
    {
        int expectedNoViolations = 1;
        mScaleClientData.scaleName = INVALID_NULL_SCALE_NUMBER;
        Set<ConstraintViolation<ScaleClientData>> constraintViolations = mValidator.validate(mScaleClientData);
        Assert.assertEquals(constraintViolations.size(), expectedNoViolations);
    }

    @Test
    private void testInvalidScaleNumberRegex()
    {
        int expectedNoViolations = 1;
        mScaleClientData.scaleName = INVALID_PATTERN_SCALE_NUMBER;
        Set<ConstraintViolation<ScaleClientData>> constraintViolations = mValidator.validate(mScaleClientData);
        Assert.assertEquals(constraintViolations.size(), expectedNoViolations);
    }

    @Test
    private void testInvalidMaxMoreThanMin()
    {
        int expectedNoViolations = 1;
        mScaleClientData.scaleMin = INVALID_SCALE_MIN;
        mScaleClientData.scaleMax = INVALID_SCALE_MAX;
        Set<ConstraintViolation<ScaleClientData>> constraintViolations = mValidator.validate(mScaleClientData);
        Assert.assertEquals(constraintViolations.size(), expectedNoViolations);
    }

    @Test
    private void testInvalidMinMaxLessThan0()
    {
        int expectedNoViolations = 2;
        mScaleClientData.scaleMin = INVALID_SCALE_MIN_LESS_THAN_ZERO;
        mScaleClientData.scaleMax = INVALID_SCALE_MAX_LESS_THAN_ZERO;
        Set<ConstraintViolation<ScaleClientData>> constraintViolations = mValidator.validate(mScaleClientData);
        Assert.assertEquals(constraintViolations.size(), expectedNoViolations);
    }

    @Test
    private void testInvalidMinMaxNull()
    {
        int expectedNoViolations = 2;
        mScaleClientData.scaleMin = null;
        mScaleClientData.scaleMax = null;
        Set<ConstraintViolation<ScaleClientData>> constraintViolations = mValidator.validate(mScaleClientData);
        Assert.assertEquals(constraintViolations.size(), expectedNoViolations);
    }
}
