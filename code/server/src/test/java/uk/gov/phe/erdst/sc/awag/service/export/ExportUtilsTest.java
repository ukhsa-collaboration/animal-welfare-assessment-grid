package uk.gov.phe.erdst.sc.awag.service.export;

import org.testng.Assert;
import org.testng.annotations.Test;

import uk.gov.phe.erdst.sc.awag.shared.test.TestConstants;

@Test(groups = {TestConstants.TESTNG_UNIT_TESTS_GROUP})
public class ExportUtilsTest
{
    @Test
    public void testIso8601DateFormatting()
    {
        String isoDate = "2015-09-01T00:00:00.000Z";
        String expectedDate = "01/09/2015";
        Assert.assertEquals(ExportUtils.formatIso8601ToSimpleDate(isoDate), expectedDate);
    }
}
