package uk.gov.phe.erdst.sc.awag.businesslogic.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import uk.gov.phe.erdst.sc.awag.businesslogic.ActivityLogController;
import uk.gov.phe.erdst.sc.awag.dao.ActivityLogDao;
import uk.gov.phe.erdst.sc.awag.datamodel.ActivityLog;

@Stateless
public class ActivityLogControllerImpl implements ActivityLogController
{
    @Inject
    private ActivityLogDao mActivityLogDao;

    @Override
    public List<ActivityLog> getActivityLogsBetween(String dateFrom, String dateTo, Integer offset, Integer limit)
    {
        return mActivityLogDao.getActivityLogsBetween(dateFrom, dateTo, offset, limit);
    }

}
