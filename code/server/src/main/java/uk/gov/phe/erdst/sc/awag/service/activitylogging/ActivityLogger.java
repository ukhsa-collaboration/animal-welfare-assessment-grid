package uk.gov.phe.erdst.sc.awag.service.activitylogging;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uk.gov.phe.erdst.sc.awag.dao.ActivityLogDao;
import uk.gov.phe.erdst.sc.awag.datamodel.ActivityLog;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNonUniqueException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWSeriousException;
import uk.gov.phe.erdst.sc.awag.utils.Constants;

@Stateless
public class ActivityLogger
{
    private static final Logger LOGGER = LogManager.getLogger(ActivityLogger.class.getName());
    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat(Constants.DATE_FORMAT);

    @Inject
    private ActivityLogDao mActivityLogDao;

    public void logActivity(InterceptedData interceptedData) throws AWSeriousException
    {
        ActivityLog logEntry = new ActivityLog();
        logEntry.setAction(interceptedData.actionName);
        logEntry.setUsername(interceptedData.loggedUser.username);
        logEntry.setDateTime(getDate());

        log(logEntry);
    }

    private static String getDate()
    {
        Date date = new Date(System.currentTimeMillis());
        return DATE_FORMATTER.format(date);
    }

    private void log(ActivityLog logEntry) throws AWSeriousException
    {
        try
        {
            mActivityLogDao.store(logEntry);
        }
        catch (AWNonUniqueException e)
        {
            LOGGER.error(e);
            throw new AWSeriousException(e);
        }
    }
}
