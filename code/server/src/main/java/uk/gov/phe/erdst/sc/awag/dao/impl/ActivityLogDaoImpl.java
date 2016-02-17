package uk.gov.phe.erdst.sc.awag.dao.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import uk.gov.phe.erdst.sc.awag.dao.ActivityLogDao;
import uk.gov.phe.erdst.sc.awag.dao.DaoErrorMessageProvider;
import uk.gov.phe.erdst.sc.awag.dao.impl.utils.DaoConstants;
import uk.gov.phe.erdst.sc.awag.dao.impl.utils.DaoUtils;
import uk.gov.phe.erdst.sc.awag.datamodel.ActivityLog;

@Stateless
public class ActivityLogDaoImpl extends CommonDaoImpl<ActivityLog> implements ActivityLogDao
{
    private static final String DATE_TIME_FIELD_NAME = "mDateTime";

    public ActivityLogDaoImpl()
    {
        super("mId", null, new DaoErrorMessageProvider() {

            @Override
            public String getNonUniqueEntityErrorMessage(Object entity)
            {
                return DaoConstants.UNSUPPORTED_OPERATION;
            }

            @Override
            public String getNoSuchEntityMessage(Object id)
            {
                return DaoConstants.UNSUPPORTED_OPERATION;
            }

            @Override
            public String getNoSuchEntityMessage(String nameValue)
            {
                return DaoConstants.UNSUPPORTED_OPERATION;
            }
        });
    }

    // TODO: introduce metamodel to avoid using hardcoded fields
    @Override
    public List<ActivityLog> getActivityLogsBetween(String dateFrom, String dateTo, Integer offset, Integer limit)
    {
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<ActivityLog> criteria = builder.createQuery(ActivityLog.class);
        Root<ActivityLog> root = criteria.from(ActivityLog.class);
        Collection<Predicate> restrictions = new ArrayList<>();

        setSearchCriteria(dateFrom, dateTo, builder, criteria, root, restrictions);

        criteria.orderBy(builder.desc(root.get(DATE_TIME_FIELD_NAME)));

        TypedQuery<ActivityLog> query = getEntityManager().createQuery(criteria);
        DaoUtils.setOffset(query, offset);
        DaoUtils.setLimit(query, limit);

        return query.getResultList();
    }

    private void setSearchCriteria(String dateFrom, String dateTo, CriteriaBuilder builder,
        CriteriaQuery<? extends Object> criteria, Root<ActivityLog> root, Collection<Predicate> restrictions)
    {
        if (dateFrom != null && dateTo != null)
        {
            restrictions.add(builder.between(root.<String> get(DATE_TIME_FIELD_NAME), dateFrom, dateTo));
        }

        if (dateFrom != null && dateTo == null)
        {
            restrictions.add(builder.greaterThanOrEqualTo(root.<String> get(DATE_TIME_FIELD_NAME), dateFrom));
        }

        if (dateFrom == null && dateTo != null)
        {
            restrictions.add(builder.lessThanOrEqualTo(root.<String> get(DATE_TIME_FIELD_NAME), dateTo));
        }

        criteria.where(builder.and(restrictions.toArray(new Predicate[] {})));
    }
}
