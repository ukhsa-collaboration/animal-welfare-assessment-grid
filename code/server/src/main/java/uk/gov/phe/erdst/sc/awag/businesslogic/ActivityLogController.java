package uk.gov.phe.erdst.sc.awag.businesslogic;

import java.util.List;

import uk.gov.phe.erdst.sc.awag.datamodel.ActivityLog;

public interface ActivityLogController
{
    List<ActivityLog> getActivityLogsBetween(String dateFrom, String dateTo, Integer offset, Integer limit);
}
