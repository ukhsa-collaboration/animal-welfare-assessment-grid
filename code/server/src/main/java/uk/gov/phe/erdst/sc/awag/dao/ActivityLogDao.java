package uk.gov.phe.erdst.sc.awag.dao;

import java.util.List;

import uk.gov.phe.erdst.sc.awag.datamodel.ActivityLog;

public interface ActivityLogDao extends CommonDao<ActivityLog>
{
    List<ActivityLog> getActivityLogsBetween(String dateFrom, String dateTo, Integer offset, Integer limit);
}
