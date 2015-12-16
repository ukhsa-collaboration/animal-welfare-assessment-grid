package uk.gov.phe.erdst.sc.awag.validation.assessment;

import java.util.Map;

import javax.validation.ConstraintValidator;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import uk.gov.phe.erdst.sc.awag.datamodel.client.AssessmentClientData;
import uk.gov.phe.erdst.sc.awag.datamodel.client.AssessmentClientFactor;
import uk.gov.phe.erdst.sc.awag.service.validation.impl.AssessmentScoreTemplateValidator;
import uk.gov.phe.erdst.sc.awag.service.validation.impl.AssessmentScoreValidator;
import uk.gov.phe.erdst.sc.awag.shared.test.TestConstants;
import uk.gov.phe.erdst.sc.awag.utils.GlassfishTestsHelper;
import uk.gov.phe.erdst.sc.awag.validation.utils.ValidationTestHelper;

@Test(groups = {TestConstants.TESTNG_CONTAINER_TESTS_GROUP})
public class AssessmentScoreTemplateValidatorTest extends AssessmentValidationTestCommon
{
    @BeforeClass
    public static void setUpClass()
    {
        AssessmentValidationTestCommon.setUpClass();
    }

    @BeforeMethod
    @SuppressWarnings("rawtypes")
    public void setUp() throws Exception
    {
        ConstraintValidator mainValidator = (ConstraintValidator) GlassfishTestsHelper
            .lookup("AssessmentScoreTemplateValidator");

        ValidatorProviderImpl validatorProvider = new ValidatorProviderImpl(mainValidator);

        super.setUp(validatorProvider);
    }

    @AfterClass
    public static void tearDownClass()
    {
        AssessmentValidationTestCommon.tearDownClass();
    }

    @Test
    public void testValidScore()
    {
        final int expectedViolations = 0;
        AssessmentClientData data = getAssessmentClientData(TestConstants.DUMMY_ASSESSMENT_RAW_VALID_SCORE_TEMPLATE);
        runTestWithSubmittedAssessmentGroup(expectedViolations, data);
    }

    @Test
    public void testMissingParameter()
    {
        final int expectedViolations = 1;

        AssessmentClientData data = getAssessmentClientData(TestConstants.DUMMY_ASSESSMENT_RAW_VALID_SCORE_TEMPLATE);
        data.score.remove(PARAMETER_1_ID);

        runTestWithSubmittedAssessmentGroup(expectedViolations, data);
    }

    @Test
    public void testMissingMultipleParameters()
    {
        final int expectedViolations = 1;

        AssessmentClientData data = getAssessmentClientData(TestConstants.DUMMY_ASSESSMENT_RAW_VALID_SCORE_TEMPLATE);
        data.score.remove(PARAMETER_1_ID);
        data.score.remove(PARAMETER_2_ID);

        runTestWithSubmittedAssessmentGroup(expectedViolations, data);
    }

    @Test
    public void testMissingFactor()
    {
        final int expectedViolations = 1;

        AssessmentClientData data = getAssessmentClientData(TestConstants.DUMMY_ASSESSMENT_RAW_VALID_SCORE_TEMPLATE);
        Map<String, AssessmentClientFactor> factors = data.score.get(PARAMETER_1_ID);
        factors.remove(FACTOR_1_ID);

        runTestWithSubmittedAssessmentGroup(expectedViolations, data);
    }

    @Test
    public void testMissingMultipleFactors()
    {
        final int expectedViolations = 1;

        AssessmentClientData data = getAssessmentClientData(TestConstants.DUMMY_ASSESSMENT_RAW_VALID_SCORE_TEMPLATE);
        Map<String, AssessmentClientFactor> factors = data.score.get(PARAMETER_1_ID);
        factors.clear();

        runTestWithSubmittedAssessmentGroup(expectedViolations, data);
    }

    @Test
    public void testIgnoredFactor()
    {
        final int expectedViolations = 0;

        AssessmentClientData data = getAssessmentClientData(TestConstants.DUMMY_ASSESSMENT_RAW_VALID_SCORE_TEMPLATE);
        Map<String, AssessmentClientFactor> factors = data.score.get(PARAMETER_1_ID);
        AssessmentClientFactor factor = factors.get(FACTOR_1_ID);
        factor.isIgnored = true;

        runTestWithSubmittedAssessmentGroup(expectedViolations, data);
    }

    @SuppressWarnings("rawtypes")
    private static class ValidatorProviderImpl implements ValidationTestHelper.ValidatorProvider
    {
        private ConstraintValidator mMainValidator;

        public ValidatorProviderImpl(ConstraintValidator mainValidator)
        {
            mMainValidator = mainValidator;
        }

        @SuppressWarnings("unchecked")
        @Override
        public <T extends ConstraintValidator<?, ?>> T getInstance(Class<T> key)
        {
            if (AssessmentScoreTemplateValidator.class.equals(key))
            {
                return (T) mMainValidator;
            }
            else if (AssessmentScoreValidator.class.equals(key))
            {
                return (T) new MockAssessmentScoreValidator();
            }

            return null;
        }

    }
}
