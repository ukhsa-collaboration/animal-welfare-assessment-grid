package uk.gov.phe.erdst.sc.awag.validation.custom;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import uk.gov.phe.erdst.sc.awag.datamodel.client.AssessmentsGetRequestParams;
import uk.gov.phe.erdst.sc.awag.shared.test.TestConstants;

@Test(groups = {TestConstants.TESTNG_UNIT_TESTS_GROUP})
public class AssessmentsGetRequestValidationTest
{
    private static final String VALID_DATE_FROM = "2014-02-01T00:00:00.000Z";
    private static final String VALID_DATE_TO = "2015-02-01T00:00:00.000Z";
    private Validator mValidator;

    @BeforeClass
    private void setUp()
    {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        mValidator = validatorFactory.getValidator();
    }

    @Test
    private void testValidGetRequest()
    {
        AssessmentsGetRequestParams assessmentsGetRequestParams = new AssessmentsGetRequestParams();
        assessmentsGetRequestParams.animalId = 1L;
        assessmentsGetRequestParams.dateFrom = VALID_DATE_FROM;
        assessmentsGetRequestParams.dateTo = VALID_DATE_TO;
        Set<ConstraintViolation<AssessmentsGetRequestParams>> constraintViolations = mValidator
            .validate(assessmentsGetRequestParams);
        Assert.assertEquals(constraintViolations.size(), 0);
    }

    @Test
    private void testInvalidAnimalId()
    {
        AssessmentsGetRequestParams assessmentsGetRequestParams = new AssessmentsGetRequestParams();
        assessmentsGetRequestParams.animalId = -1L;
        assessmentsGetRequestParams.dateFrom = VALID_DATE_FROM;
        assessmentsGetRequestParams.dateTo = VALID_DATE_TO;
        Set<ConstraintViolation<AssessmentsGetRequestParams>> constraintViolations = mValidator
            .validate(assessmentsGetRequestParams);
        Assert.assertEquals(constraintViolations.size(), 1);
    }

    @Test
    private void testInvalidDateEmptyString()
    {
        AssessmentsGetRequestParams assessmentsGetRequestParams = new AssessmentsGetRequestParams();
        assessmentsGetRequestParams.animalId = 1L;
        assessmentsGetRequestParams.dateFrom = VALID_DATE_FROM;
        assessmentsGetRequestParams.dateTo = "";
        Set<ConstraintViolation<AssessmentsGetRequestParams>> constraintViolations = mValidator
            .validate(assessmentsGetRequestParams);
        // expecting invalid date format and invalid date from/to.
        Assert.assertEquals(constraintViolations.size(), 2);
    }

    @Test
    private void testInvalidDateFromAfterDateTo()
    {
        AssessmentsGetRequestParams assessmentsGetRequestParams = new AssessmentsGetRequestParams();
        assessmentsGetRequestParams.animalId = 1L;
        assessmentsGetRequestParams.dateFrom = VALID_DATE_TO;
        assessmentsGetRequestParams.dateTo = VALID_DATE_FROM;
        Set<ConstraintViolation<AssessmentsGetRequestParams>> constraintViolations = mValidator
            .validate(assessmentsGetRequestParams);
        Assert.assertEquals(constraintViolations.size(), 1);
    }
}
