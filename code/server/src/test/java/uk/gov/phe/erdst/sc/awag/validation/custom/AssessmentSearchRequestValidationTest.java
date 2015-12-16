package uk.gov.phe.erdst.sc.awag.validation.custom;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import uk.gov.phe.erdst.sc.awag.datamodel.client.AssessmentSearchRequestParams;
import uk.gov.phe.erdst.sc.awag.service.validation.utils.ValidationConstants;
import uk.gov.phe.erdst.sc.awag.shared.test.TestConstants;

@Test(groups = {TestConstants.TESTNG_UNIT_TESTS_GROUP})
public class AssessmentSearchRequestValidationTest
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
        AssessmentSearchRequestParams params = new AssessmentSearchRequestParams();
        params.animalId = ValidationConstants.ENTITY_MIN_ID;
        params.reasonId = ValidationConstants.ENTITY_MIN_ID;
        params.studyId = ValidationConstants.ENTITY_MIN_ID;
        params.userId = ValidationConstants.ENTITY_MIN_ID;
        params.dateFrom = VALID_DATE_FROM;
        params.dateTo = VALID_DATE_TO;
        params.isComplete = true;

        Set<ConstraintViolation<AssessmentSearchRequestParams>> constraintViolations = mValidator.validate(params);
        Assert.assertEquals(constraintViolations.size(), 0);

        params.isComplete = false;

        constraintViolations = mValidator.validate(params);
        Assert.assertEquals(constraintViolations.size(), 0);

        // Make all fields null
        params = new AssessmentSearchRequestParams();
        params.isComplete = null;

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
        final int expectedErrors = 6;

        AssessmentSearchRequestParams params = new AssessmentSearchRequestParams();
        params.animalId = ValidationConstants.ENTITY_NEG_ID;
        params.reasonId = ValidationConstants.ENTITY_NEG_ID;
        params.studyId = ValidationConstants.ENTITY_NEG_ID;
        params.userId = ValidationConstants.ENTITY_NEG_ID;
        params.dateFrom = INVALID_DATE;
        params.dateTo = INVALID_DATE;
        // This field can't be invalid at that stage as it would fail at parsing
        params.isComplete = true;

        Set<ConstraintViolation<AssessmentSearchRequestParams>> constraintViolations = mValidator.validate(params);
        Assert.assertEquals(constraintViolations.size(), expectedErrors);
    }

    @Test
    private void testInvalidDateFromAfterDateTo()
    {
        final int expectedErrors = 1;

        AssessmentSearchRequestParams params = new AssessmentSearchRequestParams();
        params.animalId = ValidationConstants.ENTITY_MIN_ID;
        params.reasonId = ValidationConstants.ENTITY_MIN_ID;
        params.studyId = ValidationConstants.ENTITY_MIN_ID;
        params.userId = ValidationConstants.ENTITY_MIN_ID;

        params.dateFrom = VALID_DATE_TO;
        params.dateTo = VALID_DATE_FROM;

        params.isComplete = true;

        Set<ConstraintViolation<AssessmentSearchRequestParams>> constraintViolations = mValidator.validate(params);
        Assert.assertEquals(constraintViolations.size(), expectedErrors);
    }
}
