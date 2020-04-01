package uk.gov.phe.erdst.sc.awag.businesslogic;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import uk.gov.phe.erdst.sc.awag.dao.ActivityLogDao;
import uk.gov.phe.erdst.sc.awag.datamodel.ActivityLog;
import uk.gov.phe.erdst.sc.awag.exceptions.AWInputValidationException;
import uk.gov.phe.erdst.sc.awag.service.validation.utils.ValidatorUtils;

@Stateless
public class ActivityLogController
{
    @Inject
    private ActivityLogDao mActivityLogDao;

    public List<ActivityLog> getActivityLogsBetween(String dateFrom, String dateTo, Integer offset, Integer limit)
        throws AWInputValidationException
    {
        ValidatorUtils.validateOptionalDateParameters(dateFrom, dateTo);
        return mActivityLogDao.getActivityLogsBetween(dateFrom, dateTo, offset, limit);
    }

}
