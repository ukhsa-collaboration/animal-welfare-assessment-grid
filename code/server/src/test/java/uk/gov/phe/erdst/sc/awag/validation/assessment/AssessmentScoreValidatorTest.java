package uk.gov.phe.erdst.sc.awag.validation.assessment;

import javax.validation.ConstraintValidator;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import uk.gov.phe.erdst.sc.awag.service.validation.impl.AssessmentScoreTemplateValidator;
import uk.gov.phe.erdst.sc.awag.service.validation.impl.AssessmentScoreValidator;
import uk.gov.phe.erdst.sc.awag.shared.test.TestConstants;
import uk.gov.phe.erdst.sc.awag.utils.GlassfishTestsHelper;
import uk.gov.phe.erdst.sc.awag.validation.utils.ValidationTestHelper;
import uk.gov.phe.erdst.sc.awag.webapi.request.AssessmentClientData;
import uk.gov.phe.erdst.sc.awag.webapi.request.AssessmentClientFactor;

@Test(groups = {TestConstants.TESTNG_CONTAINER_TESTS_GROUP})
public class AssessmentScoreValidatorTest extends AssessmentValidationTestCommon
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
            .lookup("AssessmentScoreValidator");

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
        AssessmentClientData data = getValidAssessmentClientData();
        runTestWithSubmittedAssessmentGroup(expectedViolations, data);
    }

    @Test
    public void testScoreNull()
    {
        final int expectedViolations = 1;
        AssessmentClientData data = getValidAssessmentClientData();
        data.score = null;
        runTestWithSubmittedAssessmentGroup(expectedViolations, data);
    }

    @Test
    public void testAverageScoreNull()
    {
        final int expectedViolations = 1;
        AssessmentClientData data = getValidAssessmentClientData();
        data.averageScores = null;
        runTestWithSubmittedAssessmentGroup(expectedViolations, data);
    }

    @Test
    public void testParameterCommentsNull()
    {
        final int expectedViolations = 1;
        AssessmentClientData data = getValidAssessmentClientData();
        data.parameterComments = null;
        runTestWithSubmittedAssessmentGroup(expectedViolations, data);
    }

    @Test
    public void testScoreParameterKeysNotIdenticalAsAverage()
    {
        final int expectedViolations = 1;
        AssessmentClientData data = getValidAssessmentClientData();
        data.score.remove(PARAMETER_1_ID);
        runTestWithSubmittedAssessmentGroup(expectedViolations, data);
    }

    @Test
    public void testAverageParameterKeysNotIdenticalAsScore()
    {
        final int expectedViolations = 2;
        AssessmentClientData data = getValidAssessmentClientData();
        data.averageScores.remove(PARAMETER_1_ID);
        runTestWithSubmittedAssessmentGroup(expectedViolations, data);
    }

    @Test
    public void testNegativeAverage()
    {
        final int expectedViolations = 2;
        AssessmentClientData data = getValidAssessmentClientData();
        data.averageScores.put(PARAMETER_1_ID, -1d);
        runTestWithSubmittedAssessmentGroup(expectedViolations, data);
    }

    @Test
    public void testIncorrectAverage()
    {
        final int expectedViolations = 1;
        AssessmentClientData data = getValidAssessmentClientData();
        Double validValue = data.averageScores.get(PARAMETER_1_ID);
        data.averageScores.put(PARAMETER_1_ID, 2 * validValue);
        runTestWithSubmittedAssessmentGroup(expectedViolations, data);
    }

    @Test
    public void testNegativeParameterScores()
    {
        final int expectedViolations = 2;
        AssessmentClientData data = getValidAssessmentClientData();
        AssessmentClientFactor factorScore = new AssessmentClientFactor();
        factorScore.score = -1;
        factorScore.isIgnored = false;
        data.score.get(PARAMETER_1_ID).put(FACTOR_1_ID, factorScore);
        runTestWithSubmittedAssessmentGroup(expectedViolations, data);
    }

    @Test
    public void testParametersMissingScores()
    {
        final int expectedViolations = 1;
        AssessmentClientData data = getAssessmentClientData(TestConstants.DUMMY_ASSESSMENT_RAW_INVALID_ZERO_SCORES);
        runTestWithSubmittedAssessmentGroup(expectedViolations, data);
    }

    @Test
    public void testParameterScoresIgnored()
    {
        final int expectedViolations = 0;
        AssessmentClientData data = getAssessmentClientData(TestConstants.DUMMY_ASSESSMENT_RAW_VALID_IGNORED_SCORES);
        runTestWithSubmittedAssessmentGroup(expectedViolations, data);
    }

    @Test
    public void testCommentParameterBeingOptional()
    {
        final int expectedViolations = 0;
        AssessmentClientData data = getValidAssessmentClientData();
        data.parameterComments.remove(PARAMETER_1_ID);
        runTestWithSubmittedAssessmentGroup(expectedViolations, data);
    }

    @Test
    public void testNullComments()
    {
        final int expectedViolations = 1;
        AssessmentClientData data = getValidAssessmentClientData();
        data.parameterComments.put(PARAMETER_1_ID, null);
        runTestWithSubmittedAssessmentGroup(expectedViolations, data);
    }

    @Test
    public void testTooLongComments()
    {
        final int expectedViolations = 1;
        AssessmentClientData data = getValidAssessmentClientData();
        data.parameterComments.put(PARAMETER_1_ID, ValidationTestHelper.TOO_LONG_EXTENDED_TEXT);
        runTestWithSubmittedAssessmentGroup(expectedViolations, data);
    }

    @Test
    public void testTooShortComments()
    {
        final int expectedViolations = 1;
        AssessmentClientData data = getValidAssessmentClientData();
        data.parameterComments.put(PARAMETER_1_ID, ValidationTestHelper.TOO_SHORT_SIMPLE_TEXT);
        runTestWithSubmittedAssessmentGroup(expectedViolations, data);
    }

    @Test
    public void testCommentsWithIllegalCharacters()
    {
        final int expectedViolations = 1;
        AssessmentClientData data = getValidAssessmentClientData();
        data.parameterComments.put(PARAMETER_1_ID, ValidationTestHelper.INVALID_FORMAT_SIMPLE_TEXT);
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
                return (T) new MockAssessmentScoreTemplateValidator();
            }
            else if (AssessmentScoreValidator.class.equals(key))
            {
                return (T) mMainValidator;
            }

            return null;
        }

    }
}
