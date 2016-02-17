package uk.gov.phe.erdst.sc.awag.dao.impl;

import javax.ejb.Stateless;

import uk.gov.phe.erdst.sc.awag.dao.DaoErrorMessageProvider;
import uk.gov.phe.erdst.sc.awag.dao.GroupAuthDao;
import uk.gov.phe.erdst.sc.awag.dao.impl.utils.DaoConstants;
import uk.gov.phe.erdst.sc.awag.dao.impl.utils.DaoUtils;
import uk.gov.phe.erdst.sc.awag.datamodel.GroupAuth;
import uk.gov.phe.erdst.sc.awag.datamodel.UserAuth;
import uk.gov.phe.erdst.sc.awag.utils.UnitType;

@Stateless
public class GroupAuthImpl extends CommonDaoImpl<GroupAuth> implements GroupAuthDao
{
    public GroupAuthImpl()
    {
        super("mGroupName", "mGroupName", new DaoErrorMessageProvider() {

            @Override
            public String getNonUniqueEntityErrorMessage(Object entity)
            {
                return DaoUtils.getUniqueConstraintViolationMessage(DaoConstants.PROP_AUTH_GROUP_NAME,
                    ((UserAuth) entity).getEntitySelectName());
            }

            @Override
            public String getNoSuchEntityMessage(String nameValue)
            {
                return DaoUtils.getNoSuchEntityMessage(DaoConstants.PROP_AUTH_GROUP_NAME, nameValue);
            }

            @Override
            public String getNoSuchEntityMessage(Object id)
            {
                return DaoUtils.getNoSuchEntityMessage(DaoConstants.PROP_AUTH_GROUP_NAME, id);
            }
        }, UnitType.AUTH);
    }
}
