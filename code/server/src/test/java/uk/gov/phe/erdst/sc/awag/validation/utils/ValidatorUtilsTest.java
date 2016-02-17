package uk.gov.phe.erdst.sc.awag.validation.utils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintValidatorContext;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.NotNull;
import javax.ws.rs.HttpMethod;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import uk.gov.phe.erdst.sc.awag.datamodel.response.ResponsePayload;
import uk.gov.phe.erdst.sc.awag.service.validation.utils.ValidatorUtils;
import uk.gov.phe.erdst.sc.awag.shared.test.TestConstants;
import uk.gov.phe.erdst.sc.awag.utils.Constants;

@Test(groups = {TestConstants.TESTNG_UNIT_TESTS_GROUP})
public class ValidatorUtilsTest
{
    private static final String VALID_FIRST_DATE = "2014-02-01T00:00:00.000Z";
    private static final String VALID_SECOND_DATE = "2013-02-01T00:00:00.000Z";
    private static final String VALID_FROM_DATE = "2015-02-01T00:00:00.000Z";
    private static final String VALID_TO_DATE = "2015-02-20T00:00:00.000Z";
    private static final String INVALID_PARSE_FIRST_DATE = "2014aaaaaaaaaa-02-01T00:00:00.000Z";
    private static final String INVALID_PARSE_SECOND_DATE = "2013aaaaaaaaaa-02-01T00:00:00.000Z";

    private Validator mValidator;

    @BeforeClass
    private void setUp()
    {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        mValidator = validatorFactory.getValidator();
    }

    @Test
    private void testInvalidResources()
    {
        Long invalidGETPUTDELResourceId = Constants.MIN_VALID_ID - 1L;
        Long invalidPOSTResourceId = Constants.UNASSIGNED_ID - 1L;

        boolean expectedValue = false;
        Assert.assertEquals(ValidatorUtils.isResourceValid(invalidGETPUTDELResourceId, HttpMethod.GET), expectedValue);
        Assert.assertEquals(ValidatorUtils.isResourceValid(invalidGETPUTDELResourceId, HttpMethod.PUT), expectedValue);
        Assert.assertEquals(ValidatorUtils.isResourceValid(invalidGETPUTDELResourceId, HttpMethod.DELETE),
            expectedValue);
        Assert.assertEquals(ValidatorUtils.isResourceValid(invalidPOSTResourceId, HttpMethod.POST), expectedValue);
    }

    @Test
    private void testValidResources()
    {
        Long validGETPUTDELResourceId = Constants.MIN_VALID_ID;
        Long validPOSTResourceId = Constants.UNASSIGNED_ID;
        boolean expectedValue = true;
        Assert.assertEquals(ValidatorUtils.isResourceValid(validGETPUTDELResourceId, HttpMethod.GET), expectedValue);
        Assert.assertEquals(ValidatorUtils.isResourceValid(validGETPUTDELResourceId, HttpMethod.PUT), expectedValue);
        Assert.assertEquals(ValidatorUtils.isResourceValid(validGETPUTDELResourceId, HttpMethod.DELETE), expectedValue);
        Assert.assertEquals(ValidatorUtils.isResourceValid(validPOSTResourceId, HttpMethod.POST), expectedValue);
    }

    @Test
    private void testInvalidNullResource()
    {
        Long invalidNullResourceId = null;
        boolean expectedValue = false;
        Assert.assertEquals(ValidatorUtils.isResourceValid(invalidNullResourceId, HttpMethod.GET), expectedValue);
        Assert.assertEquals(ValidatorUtils.isResourceValid(invalidNullResourceId, HttpMethod.PUT), expectedValue);
        Assert.assertEquals(ValidatorUtils.isResourceValid(invalidNullResourceId, HttpMethod.POST), expectedValue);
        Assert.assertEquals(ValidatorUtils.isResourceValid(invalidNullResourceId, HttpMethod.DELETE), expectedValue);
    }

    @Test
    private void testValidIsFirstDateAfterSecondDate()
    {
        boolean expectedValue = true;
        Assert.assertEquals(ValidatorUtils.isFirstDateAfterSecondDate(VALID_FIRST_DATE, VALID_SECOND_DATE),
            expectedValue);

    }

    @Test
    private void testInvalidParseIsFirstDateAfterSecondDate()
    {
        boolean expectedValue = false;
        Assert.assertEquals(ValidatorUtils.isFirstDateAfterSecondDate(INVALID_PARSE_FIRST_DATE, VALID_SECOND_DATE),
            expectedValue);
        Assert.assertEquals(ValidatorUtils.isFirstDateAfterSecondDate(VALID_FIRST_DATE, INVALID_PARSE_SECOND_DATE),
            expectedValue);
    }

    @Test
    private void testInvalidNullIsFirstDateAfterSecondDate()
    {
        boolean expectedValue = false;
        Assert.assertEquals(ValidatorUtils.isFirstDateAfterSecondDate(null, VALID_SECOND_DATE), expectedValue);
        Assert.assertEquals(ValidatorUtils.isFirstDateAfterSecondDate(VALID_FIRST_DATE, null), expectedValue);
    }

    @Test
    private void testInvalidNullIsDateValid()
    {
        boolean expectedValue = false;
        Assert.assertEquals(ValidatorUtils.isDateValid(null), expectedValue);
    }

    @Test
    private void testInvalidParseIsDateValid()
    {
        boolean expectedValue = false;
        Assert.assertEquals(ValidatorUtils.isDateValid(INVALID_PARSE_FIRST_DATE), expectedValue);
    }

    @Test
    private void testValidIsDateValid()
    {
        boolean expectedValue = true;
        Assert.assertEquals(ValidatorUtils.isDateValid(VALID_FIRST_DATE), expectedValue);
    }

    // CS:OFF: MultipleStringLiteral
    @Test
    private void testValidIdKeysIdentical()
    {
        List<String> keysList = Arrays.asList("1", "2", "3");
        Set<String> keySetA = new HashSet<>(keysList);
        Set<String> keySetB = new HashSet<>(keysList);

        Assert.assertTrue(ValidatorUtils.isKeysIdentical(keySetA, keySetB));
    }

    @Test
    private void testValidIdKeysIdenticalDifferentOrder()
    {
        Set<String> keySetA = new HashSet<>(Arrays.asList("1", "2", "3"));
        Set<String> keySetB = new HashSet<>(Arrays.asList("3", "2", "1"));

        Assert.assertTrue(ValidatorUtils.isKeysIdentical(keySetA, keySetB));
    }

    @Test
    private void testInvalidIdKeysIdentical()
    {
        Set<String> keySetA = new HashSet<>(Arrays.asList("1", "2", "3"));
        Set<String> keySetB = new HashSet<>(Arrays.asList("1", "2", "6"));

        Assert.assertFalse(ValidatorUtils.isKeysIdentical(keySetA, keySetB));
    }

    // CS:ON

    @Test
    public void testGenericValidateRequestValidData()
    {
        DataToValidate data = new DataToValidate();
        data.nonNullData = "";
        ResponsePayload responsePayload = new ResponsePayload();

        ValidatorUtils.validateRequest(data, responsePayload, mValidator);

        Assert.assertFalse(responsePayload.hasErrors());
    }

    @Test
    public void testGenericValidateRequestInvalidData()
    {
        ResponsePayload responsePayload = new ResponsePayload();

        ValidatorUtils.validateRequest(new DataToValidate(), responsePayload, mValidator);

        Assert.assertTrue(responsePayload.hasErrors());
    }

    @Test
    public void testValidDateFromToFilterFields()
    {
        final int expectedErrors = 0;
        ConstraintValidatorContext context = new ValidationTestHelper.MockConstraintValidatorContext();

        String dateFrom = VALID_FROM_DATE;
        String dateTo = VALID_TO_DATE;
        Assert.assertEquals(ValidatorUtils.validateDateFromToFilter(dateFrom, dateTo, context), expectedErrors);

        dateTo = null;
        Assert.assertEquals(ValidatorUtils.validateDateFromToFilter(dateFrom, dateTo, context), expectedErrors);

        dateFrom = null;
        dateTo = VALID_TO_DATE;
        Assert.assertEquals(ValidatorUtils.validateDateFromToFilter(dateFrom, dateTo, context), expectedErrors);
    }

    @Test
    public void testInvalidDateFromToFilterFields()
    {
        final int expectedErrors = 1;
        ConstraintValidatorContext context = new ValidationTestHelper.MockConstraintValidatorContext();

        String dateFrom = VALID_TO_DATE;
        String dateTo = VALID_FROM_DATE;
        Assert.assertEquals(ValidatorUtils.validateDateFromToFilter(dateFrom, dateTo, context), expectedErrors);

        dateFrom = ValidationTestHelper.INVALID_DATE;
        dateTo = null;
        Assert.assertEquals(ValidatorUtils.validateDateFromToFilter(dateFrom, dateTo, context), expectedErrors);

        dateFrom = null;
        dateTo = ValidationTestHelper.INVALID_DATE;
        Assert.assertEquals(ValidatorUtils.validateDateFromToFilter(dateFrom, dateTo, context), expectedErrors);
    }

    private static class DataToValidate
    {
        // CS:OFF: VisibilityModifier
        @NotNull
        public String nonNullData;
        // CS:ON
    }
}
