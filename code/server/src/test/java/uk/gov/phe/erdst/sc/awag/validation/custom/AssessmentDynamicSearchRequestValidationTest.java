package uk.gov.phe.erdst.sc.awag.validation.custom;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import uk.gov.phe.erdst.sc.awag.datamodel.client.AssessmentDynamicSearchRequestParams;
import uk.gov.phe.erdst.sc.awag.service.validation.utils.ValidationConstants;
import uk.gov.phe.erdst.sc.awag.shared.test.TestConstants;

@Test(groups = {TestConstants.TESTNG_UNIT_TESTS_GROUP})
public class AssessmentDynamicSearchRequestValidationTest
{
    private static final String VALID_DATE_FROM = "2015-02-01T00:00:00.000Z";
    private static final String VALID_DATE_TO = "2015-02-02T00:00:00.000Z";
    private static final String INVALID_DATE = "2015-02-02asdf";
    private Validator mValidator;

    @BeforeClass
    private void setUp()
    {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        mValidator = validatorFactory.getValidator();
    }

    @Test
    private void testValidRequest()
    {
        AssessmentDynamicSearchRequestParams params = new AssessmentDynamicSearchRequestParams();
        params.studyId = ValidationConstants.ENTITY_MIN_ID;
        params.studyGroupId = ValidationConstants.ENTITY_MIN_ID;
        params.animalId = ValidationConstants.ENTITY_MIN_ID;
        params.reasonId = ValidationConstants.ENTITY_MIN_ID;
        params.userId = ValidationConstants.ENTITY_MIN_ID;
        params.dateFrom = VALID_DATE_FROM;
        params.dateTo = VALID_DATE_TO;
        params.isComplete = true;

        Set<ConstraintViolation<AssessmentDynamicSearchRequestParams>> constraintViolations = mValidator
            .validate(params);
        Assert.assertEquals(constraintViolations.size(), 0);

        // Make all fields null
        params = new AssessmentDynamicSearchRequestParams();
        params.isComplete = true;

        constraintViolations = mValidator.validate(params);
        Assert.assertEquals(constraintViolations.size(), 0);

        params.dateFrom = VALID_DATE_FROM;
        params.dateTo = VALID_DATE_FROM;
        constraintViolations = mValidator.validate(params);
        Assert.assertEquals(constraintViolations.size(), 0);
    }

    @Test
    private void testInvalidRequest()
    {
        final int expectedErrors = 7;

        AssessmentDynamicSearchRequestParams params = new AssessmentDynamicSearchRequestParams();
        params.studyId = ValidationConstants.ENTITY_NEG_ID;
        params.studyGroupId = ValidationConstants.ENTITY_NEG_ID;
        params.animalId = ValidationConstants.ENTITY_NEG_ID;
        params.reasonId = ValidationConstants.ENTITY_NEG_ID;
        params.userId = ValidationConstants.ENTITY_NEG_ID;
        params.dateFrom = INVALID_DATE;
        params.dateTo = INVALID_DATE;
        // This field can't be invalid at that stage as it would fail at parsing
        params.isComplete = true;

        Set<ConstraintViolation<AssessmentDynamicSearchRequestParams>> constraintViolations = mValidator
            .validate(params);
        Assert.assertEquals(constraintViolations.size(), expectedErrors);
    }

    @Test
    private void testInvalidDateFromAfterDateTo()
    {
        final int expectedErrors = 1;

        AssessmentDynamicSearchRequestParams params = new AssessmentDynamicSearchRequestParams();
        params.studyId = ValidationConstants.ENTITY_MIN_ID;
        params.studyGroupId = ValidationConstants.ENTITY_MIN_ID;
        params.animalId = ValidationConstants.ENTITY_MIN_ID;
        params.reasonId = ValidationConstants.ENTITY_MIN_ID;
        params.userId = ValidationConstants.ENTITY_MIN_ID;

        params.dateFrom = VALID_DATE_TO;
        params.dateTo = VALID_DATE_FROM;

        params.isComplete = true;

        Set<ConstraintViolation<AssessmentDynamicSearchRequestParams>> constraintViolations = mValidator
            .validate(params);
        Assert.assertEquals(constraintViolations.size(), expectedErrors);
    }
}
