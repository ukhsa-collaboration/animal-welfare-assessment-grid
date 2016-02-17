package uk.gov.phe.erdst.sc.awag.service.logging;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uk.gov.phe.erdst.sc.awag.dao.ActivityLogDao;
import uk.gov.phe.erdst.sc.awag.datamodel.ActivityLog;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNonUniqueException;
import uk.gov.phe.erdst.sc.awag.utils.Constants;

@Stateless
public class ActivityLogger
{
    public static final String ACTION_SUCCESSFUL = "SUCCESS";
    public static final String ACTION_FAILED = "FAILED";
    public static final String ACTION_MSG_SEPARATOR = ":";

    private static final Logger LOGGER = LogManager.getLogger(ActivityLogger.class.getName());
    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat(Constants.DATE_FORMAT);

    @Inject
    private ActivityLogDao mActivityLogDao;

    public void logActivityNoResponsePayload(InterceptedData interceptedData)
    {
        ActivityLog logEntry = new ActivityLog();
        logEntry.setAction(interceptedData.actionName);
        logEntry.setUsername(interceptedData.loggedUser.username);
        logEntry.setDateTime(getDate());

        log(logEntry);
    }

    public void logActivityWithResponsePayload(InterceptedData interceptedData)
    {
        ActivityLog logEntry = new ActivityLog();
        String okOrFalse = interceptedData.responsePayload.hasErrors() ? ACTION_FAILED : ACTION_SUCCESSFUL;
        logEntry.setAction(interceptedData.actionName + ACTION_MSG_SEPARATOR + okOrFalse);
        logEntry.setUsername(interceptedData.loggedUser.username);
        logEntry.setDateTime(getDate());

        log(logEntry);
    }

    public void logActivityThrewException(InterceptedData interceptedData)
    {
        ActivityLog logEntry = new ActivityLog();
        logEntry.setAction(interceptedData.actionName + ACTION_MSG_SEPARATOR + interceptedData.exceptionMsg);
        logEntry.setUsername(interceptedData.loggedUser.username);
        logEntry.setDateTime(getDate());

        log(logEntry);
    }

    private static String getDate()
    {
        Date date = new Date(System.currentTimeMillis());
        return DATE_FORMATTER.format(date);
    }

    private void log(ActivityLog logEntry)
    {
        try
        {
            mActivityLogDao.store(logEntry);
        }
        catch (AWNonUniqueException e)
        {
            LOGGER.error(e);
        }
    }
}
