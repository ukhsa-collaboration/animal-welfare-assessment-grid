package uk.gov.phe.erdst.sc.awag.validation.assessment;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.testng.Assert;

import uk.gov.phe.erdst.sc.awag.datamodel.client.AssessmentClientData;
import uk.gov.phe.erdst.sc.awag.service.validation.annotations.ValidAssessmentScore;
import uk.gov.phe.erdst.sc.awag.service.validation.annotations.ValidAssessmentScoreTemplate;
import uk.gov.phe.erdst.sc.awag.service.validation.groups.SubmittedAssessment;
import uk.gov.phe.erdst.sc.awag.servlets.utils.RequestConverter;
import uk.gov.phe.erdst.sc.awag.shared.test.TestConstants;
import uk.gov.phe.erdst.sc.awag.utils.GlassfishTestsHelper;
import uk.gov.phe.erdst.sc.awag.validation.utils.ValidationTestHelper;

public class AssessmentValidationTestCommon
{
    // CS:OFF: MultipleStringLiteral
    protected static final String PARAMETER_1_ID = "10000";
    protected static final String PARAMETER_2_ID = "10001";
    protected static final String FACTOR_1_ID = "10000";
    // CS:ON

    protected RequestConverter mRequestConverter;
    protected Validator mValidator;

    protected static void setUpClass()
    {
        GlassfishTestsHelper.preTestSetup();
    }

    protected void setUp(ValidationTestHelper.ValidatorProvider validatorProvider) throws Exception
    {
        mRequestConverter = (RequestConverter) GlassfishTestsHelper.lookup("RequestConverterImpl");

        ValidatorFactory factory = ValidationTestHelper.createValidatorFactory(validatorProvider);

        mValidator = factory.getValidator();
    }

    protected static void tearDownClass()
    {
        GlassfishTestsHelper.onTestFinished();
    }

    protected AssessmentClientData getValidAssessmentClientData()
    {
        return getAssessmentClientData(TestConstants.DUMMY_ASSESSMENT_RAW_DATA);
    }

    protected AssessmentClientData getAssessmentClientData(String rawData)
    {
        return (AssessmentClientData) mRequestConverter.convert(rawData, AssessmentClientData.class);
    }

    protected void runTestWithSubmittedAssessmentGroup(int expectedViolations, AssessmentClientData data)
    {
        int violations = ValidationTestHelper.validate(mValidator, data, SubmittedAssessment.class);
        Assert.assertEquals(violations, expectedViolations);
    }

    protected static class MockAssessmentScoreValidator implements
        ConstraintValidator<ValidAssessmentScore, AssessmentClientData>
    {
        public MockAssessmentScoreValidator()
        {
        }

        @Override
        public void initialize(ValidAssessmentScore constraintAnnotation)
        {
        }

        @Override
        public boolean isValid(AssessmentClientData clientData, ConstraintValidatorContext context)
        {
            return true;
        }

    }

    protected static class MockAssessmentScoreTemplateValidator implements
        ConstraintValidator<ValidAssessmentScoreTemplate, AssessmentClientData>
    {
        public MockAssessmentScoreTemplateValidator()
        {
        }

        @Override
        public void initialize(ValidAssessmentScoreTemplate constraintAnnotation)
        {
        }

        @Override
        public boolean isValid(AssessmentClientData clientData, ConstraintValidatorContext context)
        {
            return true;
        }

    }

}
