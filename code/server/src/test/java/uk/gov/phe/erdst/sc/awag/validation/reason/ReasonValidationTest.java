package uk.gov.phe.erdst.sc.awag.validation.reason;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import uk.gov.phe.erdst.sc.awag.datamodel.client.AssessmentReasonClientData;
import uk.gov.phe.erdst.sc.awag.shared.test.TestConstants;
import uk.gov.phe.erdst.sc.awag.utils.GuiceHelper;
import uk.gov.phe.erdst.sc.awag.utils.StringHelper;

@Test(groups = {TestConstants.TESTNG_UNIT_TESTS_GROUP})
public class ReasonValidationTest
{
    private static final String VALID_REASON_NAME = "Reason 1";
    private static final String INVALID_PATTERN_REASON_NAME = "Rea$*&SHS^&*(";
    private static final String INVALID_MIN_REASON_NAME = "Re";
    private static final String INVALID_MAX_REASON_NAME;
    private static final String INVALID_NULL_REASON_NAME = null;
    private Validator mValidator;
    private AssessmentReasonClientData mAssessmentReasonClientData;

    static
    {
        final int invalidNameLength = 256;
        INVALID_MAX_REASON_NAME = StringHelper.getStringOfLength(invalidNameLength);
    }

    @BeforeClass
    private void setUp()
    {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        mValidator = validatorFactory.getValidator();
    }

    @BeforeMethod
    private void resetValidAssessmentReasonClientData()
    {
        mAssessmentReasonClientData = new AssessmentReasonClientData(-1L, VALID_REASON_NAME);
        GuiceHelper.injectTestDependencies(this);
    }

    @Test
    private void testValidAssessmentReasonClientData()
    {
        int expectedNoViolations = 0;
        Set<ConstraintViolation<AssessmentReasonClientData>> constraintViolations = mValidator
            .validate(mAssessmentReasonClientData);
        Assert.assertEquals(constraintViolations.size(), expectedNoViolations);
    }

    @Test
    private void testPatternInvalidAssessmentReasonClientData()
    {
        int expectedNoViolations = 1;
        mAssessmentReasonClientData.reasonName = INVALID_PATTERN_REASON_NAME;
        Set<ConstraintViolation<AssessmentReasonClientData>> constraintViolations = mValidator
            .validate(mAssessmentReasonClientData);
        Assert.assertEquals(constraintViolations.size(), expectedNoViolations);
    }

    @Test
    private void testMaxInvalidAssessmentReasonClientData()
    {
        int expectedNoViolations = 1;
        mAssessmentReasonClientData.reasonName = INVALID_MAX_REASON_NAME;
        Set<ConstraintViolation<AssessmentReasonClientData>> constraintViolations = mValidator
            .validate(mAssessmentReasonClientData);
        Assert.assertEquals(constraintViolations.size(), expectedNoViolations);
    }

    @Test
    private void testMinInvalidAssessmentReasonClientData()
    {
        int expectedNoViolations = 1;
        mAssessmentReasonClientData.reasonName = INVALID_MIN_REASON_NAME;
        Set<ConstraintViolation<AssessmentReasonClientData>> constraintViolations = mValidator
            .validate(mAssessmentReasonClientData);
        Assert.assertEquals(constraintViolations.size(), expectedNoViolations);
    }

    @Test
    private void testNullInvalidAssessmentReasonClientData()
    {
        int expectedNoViolations = 1;
        mAssessmentReasonClientData.reasonName = INVALID_NULL_REASON_NAME;
        Set<ConstraintViolation<AssessmentReasonClientData>> constraintViolations = mValidator
            .validate(mAssessmentReasonClientData);
        Assert.assertEquals(constraintViolations.size(), expectedNoViolations);
    }
}
