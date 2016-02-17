package uk.gov.phe.erdst.sc.awag.logging;

import java.util.Collection;

import uk.gov.phe.erdst.sc.awag.dao.ActivityLogDao;
import uk.gov.phe.erdst.sc.awag.datamodel.ActivityLog;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNoSuchEntityException;

public final class LoggedActivityTestUtils
{
    private LoggedActivityTestUtils()
    {
    }

    public static void deleteAllLogs(ActivityLogDao dao) throws AWNoSuchEntityException
    {
        Collection<ActivityLog> logs = dao.getEntities(null, null);
        for (ActivityLog log : logs)
        {
            dao.deleteEntityById(log.getId());
        }
    }
}
