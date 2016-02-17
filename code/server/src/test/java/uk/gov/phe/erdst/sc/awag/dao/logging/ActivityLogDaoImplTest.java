package uk.gov.phe.erdst.sc.awag.dao.logging;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import uk.gov.phe.erdst.sc.awag.dao.ActivityLogDao;
import uk.gov.phe.erdst.sc.awag.datamodel.ActivityLog;
import uk.gov.phe.erdst.sc.awag.logging.LoggedActivityTestUtils;
import uk.gov.phe.erdst.sc.awag.shared.test.TestConstants;
import uk.gov.phe.erdst.sc.awag.utils.GlassfishTestsHelper;

@Test(groups = {TestConstants.TESTNG_CONTAINER_TESTS_GROUP})
public class ActivityLogDaoImplTest
{
    private static final String DATE_A = "2014-08-14T00:00:01.000Z";
    private static final String DATE_B = "2014-08-14T00:00:02.000Z";
    private static final String DATE_C = "2014-08-14T00:00:03.000Z";

    private ActivityLogDao mActivityLogDao;

    @BeforeClass
    public static void setUpClass()
    {
        // GlassfishTestsHelper.eclipsePropertiesTest();
        GlassfishTestsHelper.preTestSetup();
    }

    @BeforeMethod
    public void setUp() throws Exception
    {
        mActivityLogDao = (ActivityLogDao) GlassfishTestsHelper.lookupMultiInterface("ActivityLogDaoImpl",
            ActivityLogDao.class);

        LoggedActivityTestUtils.deleteAllLogs(mActivityLogDao);
    }

    @AfterClass
    public static void tearDownClass()
    {
        GlassfishTestsHelper.onTestFinished();
    }

    @Test
    public void testGetActivityLogsWithCriteriaByUnspecifiedFilters() throws Exception
    {
        List<ActivityLog> result = mActivityLogDao.getActivityLogsBetween(null, null, null, null);
        Assert.assertTrue(result.isEmpty());

        final int expectedResults = 3;
        for (int i = 0; i < expectedResults; i++)
        {
            ActivityLog log = createDefaultLogEntry("");
            mActivityLogDao.store(log);
        }

        result = mActivityLogDao.getActivityLogsBetween(null, null, null, null);

        Assert.assertEquals(result.size(), expectedResults);
    }

    @Test
    public void testGetActivityLogsWithCriteriaByDates() throws Exception
    {
        String dateFrom = DATE_A;
        String logDate = DATE_B;
        String dateTo = DATE_C;

        final int expectedResults = 1;

        List<ActivityLog> result = mActivityLogDao.getActivityLogsBetween(dateFrom, dateTo, null, null);
        Assert.assertTrue(result.isEmpty());

        ActivityLog log = createDefaultLogEntry(logDate);
        mActivityLogDao.store(log);

        result = mActivityLogDao.getActivityLogsBetween(dateFrom, dateTo, null, null);
        Assert.assertEquals(result.size(), expectedResults);

        result = mActivityLogDao.getActivityLogsBetween(dateFrom, dateFrom, null, null);
        Assert.assertTrue(result.isEmpty());

        result = mActivityLogDao.getActivityLogsBetween(dateFrom, logDate, null, null);
        Assert.assertEquals(result.size(), expectedResults);

        log = createDefaultLogEntry(dateFrom);
        mActivityLogDao.store(log);

        int singleDateExpectedResults = 2;
        result = mActivityLogDao.getActivityLogsBetween(dateFrom, null, null, null);
        Assert.assertEquals(result.size(), singleDateExpectedResults);

        log = createDefaultLogEntry(dateTo);
        mActivityLogDao.store(log);

        singleDateExpectedResults = 3;
        result = mActivityLogDao.getActivityLogsBetween(null, dateTo, null, null);
        Assert.assertEquals(result.size(), singleDateExpectedResults);
    }

    private ActivityLog createDefaultLogEntry(String date)
    {
        ActivityLog logEntry = new ActivityLog();
        logEntry.setAction("");
        logEntry.setUsername("");
        logEntry.setDateTime(date);

        return logEntry;
    }
}
