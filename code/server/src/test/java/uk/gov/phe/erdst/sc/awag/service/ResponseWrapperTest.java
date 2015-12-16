package uk.gov.phe.erdst.sc.awag.service;

import static org.testng.AssertJUnit.assertEquals;

import org.testng.annotations.Test;

import uk.gov.phe.erdst.sc.awag.shared.test.TestConstants;
import uk.gov.phe.erdst.sc.awag.utils.ResponseWrapper;

@Test(groups = {TestConstants.TESTNG_UNIT_TESTS_GROUP})
public class ResponseWrapperTest
{
    @Test
    public void testWrapSuccess()
    {
        String expected = "callback(data)";

        String actual = ResponseWrapper.wrap("callback", "data");

        assertEquals(expected, actual);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testWrapFailureNullCallback()
    {
        ResponseWrapper.wrap(null, "data");
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testWrapFailureNullData()
    {
        ResponseWrapper.wrap("callback", null);
    }
}
