package uk.gov.phe.erdst.sc.awag.logging;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import uk.gov.phe.erdst.sc.awag.dao.ActivityLogDao;
import uk.gov.phe.erdst.sc.awag.datamodel.ActivityLog;
import uk.gov.phe.erdst.sc.awag.datamodel.response.ResponsePayload;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNonUniqueException;
import uk.gov.phe.erdst.sc.awag.service.logging.ActivityLogger;
import uk.gov.phe.erdst.sc.awag.service.logging.LoggedUser;
import uk.gov.phe.erdst.sc.awag.shared.test.LoggedMethodsClass;
import uk.gov.phe.erdst.sc.awag.shared.test.TestConstants;
import uk.gov.phe.erdst.sc.awag.utils.Constants;
import uk.gov.phe.erdst.sc.awag.utils.GlassfishTestsHelper;

@Test(groups = {TestConstants.TESTNG_CONTAINER_TESTS_GROUP})
public class LoggedActivityTest
{
    private static final String LOGGED_USERNAME = "TestUser";
    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat(Constants.DATE_FORMAT);

    private ActivityLogDao mActivityLogDao;
    private LoggedMethodsClass mLoggedMethodsInstance;
    private LoggedUser mLoggedUser;

    @BeforeClass
    public static void setUpClass()
    {
        GlassfishTestsHelper.preTestSetup();
    }

    @BeforeMethod
    public void setUp() throws Exception
    {
        mActivityLogDao = (ActivityLogDao) GlassfishTestsHelper.lookupMultiInterface("ActivityLogDaoImpl",
            ActivityLogDao.class);

        mLoggedMethodsInstance = (LoggedMethodsClass) GlassfishTestsHelper.lookup("LoggedMethodsClass");

        mLoggedUser = new LoggedUser(LOGGED_USERNAME);

        LoggedActivityTestUtils.deleteAllLogs(mActivityLogDao);
    }

    @AfterClass
    public static void tearDownClass()
    {
        GlassfishTestsHelper.onTestFinished();
    }

    @Test
    public void testLoggedMethodNoResponsePayload() throws Exception
    {
        final long preMethod = System.currentTimeMillis();
        mLoggedMethodsInstance.loggedMethodNoResponsePayload(mLoggedUser);
        final long pastMethod = System.currentTimeMillis();

        assertTest(preMethod, pastMethod, LoggedMethodsClass.LOGGED_METHOD_NO_RESPONSE_PAYLOAD);
    }

    // CS:OFF: IllegalCatch
    @Test(expectedExceptions = {IllegalStateException.class})
    public void testLoggedMethodMissingLoggedUser() throws Throwable
    {
        try
        {
            mLoggedMethodsInstance.loggedMethodMissingLoggedUser();
        }
        catch (Exception e)
        {
            throw e.getCause();
        }
    }

    @Test(expectedExceptions = {IllegalStateException.class})
    public void testLoggedMethodMissingLoggedUserOtherParamPresent() throws Throwable
    {
        try
        {
            mLoggedMethodsInstance.loggedMethodMissingLoggedUserOtherParamPresent("");
        }
        catch (Exception e)
        {
            throw e.getCause();
        }
    }

    // CS:ON

    @Test
    public void testLoggedMethodWithResponsePayloadSuccess() throws Exception
    {
        ResponsePayload responsePayload = new ResponsePayload();

        final long preMethod = System.currentTimeMillis();
        mLoggedMethodsInstance.loggedMethodWithResponsePayload(responsePayload, mLoggedUser);
        final long pastMethod = System.currentTimeMillis();

        String expectedMsg = LoggedMethodsClass.LOGGED_METHOD_WITH_RESPONSE_PAYLOAD
            + ActivityLogger.ACTION_MSG_SEPARATOR + ActivityLogger.ACTION_SUCCESSFUL;

        assertTest(preMethod, pastMethod, expectedMsg);
    }

    @Test
    public void testLoggedMethodWithResponsePayloadFailure() throws Exception
    {
        ResponsePayload responsePayload = new ResponsePayload();
        responsePayload.addError("");

        final long preMethod = System.currentTimeMillis();
        mLoggedMethodsInstance.loggedMethodWithResponsePayload(responsePayload, mLoggedUser);
        final long pastMethod = System.currentTimeMillis();

        String expectedMsg = LoggedMethodsClass.LOGGED_METHOD_WITH_RESPONSE_PAYLOAD
            + ActivityLogger.ACTION_MSG_SEPARATOR + ActivityLogger.ACTION_FAILED;

        assertTest(preMethod, pastMethod, expectedMsg);
    }

    @Test(expectedExceptions = {AWNonUniqueException.class})
    public void testLoggedMethodThrowsException() throws Exception
    {
        final long preMethod = System.currentTimeMillis();
        mLoggedMethodsInstance.loggedMethodThrowsException(mLoggedUser);
        final long pastMethod = System.currentTimeMillis();

        String expectedMsg = LoggedMethodsClass.LOGGED_METHOD_THROWS_EXCEPTION + ActivityLogger.ACTION_MSG_SEPARATOR
            + LoggedMethodsClass.THROWN_EXCEPTION_MSG;

        assertTest(preMethod, pastMethod, expectedMsg);
    }

    private void assertTest(long preMethod, long pastMethod, String expectedAction) throws ParseException
    {
        Collection<ActivityLog> logs = mActivityLogDao.getEntities(null, null);

        Assert.assertEquals(logs.size(), 1);

        ActivityLog[] logsArr = logs.toArray(new ActivityLog[] {});
        ActivityLog log = logsArr[0];

        Assert.assertEquals(log.getAction(), expectedAction);
        Assert.assertEquals(log.getUsername(), LOGGED_USERNAME);

        final long loggedDateTime = DATE_FORMATTER.parse(log.getDateTime()).getTime();

        Assert.assertTrue(loggedDateTime >= preMethod);
        Assert.assertTrue(loggedDateTime <= pastMethod);
    }
}
