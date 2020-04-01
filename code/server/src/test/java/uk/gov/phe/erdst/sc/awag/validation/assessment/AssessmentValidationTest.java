package uk.gov.phe.erdst.sc.awag.validation.assessment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.validation.ConstraintValidator;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import uk.gov.phe.erdst.sc.awag.service.validation.groups.SavedAssessment;
import uk.gov.phe.erdst.sc.awag.service.validation.groups.SubmittedAssessment;
import uk.gov.phe.erdst.sc.awag.service.validation.impl.AssessmentScoreTemplateValidator;
import uk.gov.phe.erdst.sc.awag.service.validation.impl.AssessmentScoreValidator;
import uk.gov.phe.erdst.sc.awag.service.validation.utils.ValidationConstants;
import uk.gov.phe.erdst.sc.awag.shared.test.TestConstants;
import uk.gov.phe.erdst.sc.awag.validation.utils.ValidationTest;
import uk.gov.phe.erdst.sc.awag.validation.utils.ValidationTestHelper;
import uk.gov.phe.erdst.sc.awag.webapi.request.AssessmentClientData;

@Test(groups = {TestConstants.TESTNG_CONTAINER_TESTS_GROUP})
public class AssessmentValidationTest extends AssessmentValidationTestCommon
{
    @BeforeClass
    public static void setUpClass()
    {
        AssessmentValidationTestCommon.setUpClass();
    }

    @BeforeMethod
    public void setUp() throws Exception
    {
        super.setUp(new ValidatorProviderImpl());
    }

    @AfterClass
    public static void tearDownClass()
    {
        AssessmentValidationTestCommon.tearDownClass();
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Test
    public void testValidSavedAssessmentValidation()
    {
        final int expectedViolations = 0;

        AssessmentClientData data = new AssessmentClientData();
        data.animalId = ValidationConstants.ENTITY_MIN_ID;
        data.id = ValidationConstants.ENTITY_MIN_ID;
        data.date = "2013-02-01T00:00:00.000Z";
        data.score = new HashMap();
        data.averageScores = new HashMap();
        data.parameterComments = new HashMap();

        final int actualViolations = ValidationTestHelper.validate(mValidator, data, SavedAssessment.class);

        Assert.assertEquals(actualViolations, expectedViolations);
    }

    @Test
    public void testValidSavedCompleteAssessmentValidation()
    {
        final int expectedViolations = 0;

        AssessmentClientData data = getValidAssessmentClientData();

        final int actualViolations = ValidationTestHelper.validate(mValidator, data, SavedAssessment.class);

        Assert.assertEquals(actualViolations, expectedViolations);
    }

    @Test
    public void testValidSubmittedAssessmentValidation()
    {
        final int expectedViolations = 0;

        AssessmentClientData data = getValidAssessmentClientData();

        final int actualViolations = ValidationTestHelper.validate(mValidator, data, SubmittedAssessment.class);

        Assert.assertEquals(actualViolations, expectedViolations);
    }

    @Test
    public void testInvalidSubmittedEmptyAssessmentValidation()
    {
        final int expectedViolations = 5;

        AssessmentClientData data = (AssessmentClientData) mRequestConverter.convert(
            TestConstants.DUMMY_ASSESSMENT_RAW_EMPTY_INVALID_DATA, AssessmentClientData.class);

        final int actualViolations = ValidationTestHelper.validate(mValidator, data, SubmittedAssessment.class);

        Assert.assertEquals(actualViolations, expectedViolations);
    }

    // CS:OFF: MultipleStringLiteral
    @Test
    public void testIdAttributeValidation()
    {
        ValidationTestHelper.testMainEntityIdAttribute(getValidAssessmentClientData(), "id",
            AssessmentClientData.class, mValidator, SubmittedAssessment.class);

        ValidationTestHelper.testMainEntityIdAttribute(getValidAssessmentClientData(), "id",
            AssessmentClientData.class, mValidator, SavedAssessment.class);
    }

    // CS:ON

    // CS:OFF: MultipleStringLiteral
    @Test
    public void testAnimalIdAttributeValidation()
    {
        ValidationTestHelper.testCompositeEntityIdAttribute(getValidAssessmentClientData(), "animalId",
            AssessmentClientData.class, mValidator, SubmittedAssessment.class);

        ValidationTestHelper.testCompositeEntityIdAttribute(getValidAssessmentClientData(), "animalId",
            AssessmentClientData.class, mValidator, SavedAssessment.class);
    }

    // CS:ON

    @Test
    public void testReasonAttributeValidation()
    {
        ValidationTestHelper.testSimpleTextAttribute(getValidAssessmentClientData(), "reason",
            AssessmentClientData.class, mValidator, SubmittedAssessment.class);
    }

    @Test
    public void testAnimalHousingAttributeValidation()
    {
        ValidationTestHelper.testSimpleTextAttribute(getValidAssessmentClientData(), "animalHousing",
            AssessmentClientData.class, mValidator, SubmittedAssessment.class);
    }

    @Test
    public void testPerformedByAttributeValidation()
    {
        ValidationTestHelper.testSimpleTextAttribute(getValidAssessmentClientData(), "performedBy",
            AssessmentClientData.class, mValidator, SubmittedAssessment.class);
    }

    // CS:OFF: MultipleStringLiteral
    @Test
    public void testDateAttributeValidation()
    {
        final int expectedViolations = 2;

        List<String> invalidValues = Arrays.asList(null, ValidationTestHelper.INVALID_DATE);

        ValidationTest testSave = new ValidationTest(expectedViolations, getValidAssessmentClientData(), "date",
            invalidValues, mValidator, SavedAssessment.class);

        ValidationTest testSubmit = new ValidationTest(expectedViolations, getValidAssessmentClientData(), "date",
            invalidValues, mValidator, SubmittedAssessment.class);

        ValidationTestHelper.run(Arrays.asList(testSave, testSubmit), AssessmentClientData.class);
    }

    // CS:ON

    @Test
    public void testScoreAttributeValidation()
    {
        final int expectedViolations = 1;

        List<String> invalidValues = new ArrayList<String>();
        invalidValues.add(null);

        ValidationTest testSave = new ValidationTest(expectedViolations, getValidAssessmentClientData(), "score",
            invalidValues, mValidator, SavedAssessment.class);

        ValidationTestHelper.run(Arrays.asList(testSave), AssessmentClientData.class);
    }

    @Test
    public void testAverageScoresAttributeValidation()
    {
        final int expectedViolations = 1;

        List<String> invalidValues = new ArrayList<String>();
        invalidValues.add(null);

        ValidationTest testSave = new ValidationTest(expectedViolations, getValidAssessmentClientData(),
            "averageScores", invalidValues, mValidator, SavedAssessment.class);

        ValidationTestHelper.run(Arrays.asList(testSave), AssessmentClientData.class);
    }

    @Test
    public void testParameterCommentsAttributeValidation()
    {
        final int expectedViolations = 1;

        List<String> invalidValues = new ArrayList<String>();
        invalidValues.add(null);

        ValidationTest testSave = new ValidationTest(expectedViolations, getValidAssessmentClientData(),
            "parameterComments", invalidValues, mValidator, SavedAssessment.class);

        ValidationTestHelper.run(Arrays.asList(testSave), AssessmentClientData.class);
    }

    private static class ValidatorProviderImpl implements ValidationTestHelper.ValidatorProvider
    {
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
                return (T) new MockAssessmentScoreValidator();
            }

            return null;
        }

    }
}
