package uk.gov.phe.erdst.sc.awag.validation.utils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.testng.Assert;
import org.testng.annotations.Test;

import uk.gov.phe.erdst.sc.awag.exceptions.AWInputValidationException;
import uk.gov.phe.erdst.sc.awag.service.validation.utils.ValidatorUtils;
import uk.gov.phe.erdst.sc.awag.shared.test.TestConstants;

@Test(groups = {TestConstants.TESTNG_UNIT_TESTS_GROUP})
public class ValidatorUtilsTest
{
    private static final String VALID_FIRST_DATE = "2014-02-01T00:00:00.000Z";
    private static final String VALID_SECOND_DATE = "2013-02-01T00:00:00.000Z";
    private static final String VALID_FROM_DATE = "2015-02-01T00:00:00.000Z";
    private static final String VALID_TO_DATE = "2015-02-20T00:00:00.000Z";
    private static final String INVALID_PARSE_FIRST_DATE = "2014aaaaaaaaaa-02-01T00:00:00.000Z";
    private static final String INVALID_PARSE_SECOND_DATE = "2013aaaaaaaaaa-02-01T00:00:00.000Z";

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
    public void testValidDateFromToFilterFields() throws AWInputValidationException
    {
        String dateFrom = VALID_FROM_DATE;
        String dateTo = VALID_TO_DATE;
        ValidatorUtils.validateOptionalDateParameters(dateFrom, dateTo);

        dateTo = null;
        ValidatorUtils.validateOptionalDateParameters(dateFrom, dateTo);

        dateFrom = null;
        dateTo = VALID_TO_DATE;
        ValidatorUtils.validateOptionalDateParameters(dateFrom, dateTo);
    }

    @Test()
    public void testInvalidDateFromToFilterFields()
    {
        final int expectedErrors = 2;
        int errorsCount = 0;

        String dateFrom = VALID_TO_DATE;
        String dateTo = VALID_FROM_DATE;
        try
        {
            ValidatorUtils.validateOptionalDateParameters(dateFrom, dateTo);
        }
        catch (AWInputValidationException e)
        {}

        dateFrom = ValidationTestHelper.INVALID_DATE;
        dateTo = null;
        try
        {
            ValidatorUtils.validateOptionalDateParameters(dateFrom, dateTo);
        }
        catch (AWInputValidationException e)
        {
            errorsCount++;
        }

        dateFrom = null;
        dateTo = ValidationTestHelper.INVALID_DATE;
        try
        {
            ValidatorUtils.validateOptionalDateParameters(dateFrom, dateTo);
        }
        catch (AWInputValidationException e)
        {
            errorsCount++;
        }

        Assert.assertEquals(errorsCount, expectedErrors);
    }
}
