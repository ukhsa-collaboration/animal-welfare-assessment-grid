package uk.gov.phe.erdst.sc.awag.servlets.utils;

import junit.framework.Assert;

import org.testng.annotations.Test;

import uk.gov.phe.erdst.sc.awag.servlets.utils.impl.ResponseFormatterImpl;
import uk.gov.phe.erdst.sc.awag.shared.test.TestConstants;

@Test(groups = {TestConstants.TESTNG_UNIT_TESTS_GROUP})
public class ResponseFormatterImplTest
{
    @Test
    public void testJsonNullHandling()
    {
        ResponseFormatterImpl formatter = new ResponseFormatterImpl();
        Assert.assertEquals("null", formatter.toJson(null));
    }
}
