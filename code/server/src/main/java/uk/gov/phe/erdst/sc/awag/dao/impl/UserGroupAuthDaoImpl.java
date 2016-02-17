package uk.gov.phe.erdst.sc.awag.dao.impl;

import javax.ejb.Stateless;

import uk.gov.phe.erdst.sc.awag.dao.DaoErrorMessageProvider;
import uk.gov.phe.erdst.sc.awag.dao.UserGroupAuthDao;
import uk.gov.phe.erdst.sc.awag.dao.impl.utils.DaoConstants;
import uk.gov.phe.erdst.sc.awag.datamodel.UserGroupAuth;
import uk.gov.phe.erdst.sc.awag.utils.UnitType;

@Stateless
public class UserGroupAuthDaoImpl extends CommonDaoImpl<UserGroupAuth> implements UserGroupAuthDao
{
    public UserGroupAuthDaoImpl()
    {
        super("mId", "mId", new DaoErrorMessageProvider() {

            @Override
            public String getNonUniqueEntityErrorMessage(Object entity)
            {
                return DaoConstants.UNSUPPORTED_OPERATION;
            }

            @Override
            public String getNoSuchEntityMessage(String nameValue)
            {
                return DaoConstants.UNSUPPORTED_OPERATION;
            }

            @Override
            public String getNoSuchEntityMessage(Object id)
            {
                return DaoConstants.UNSUPPORTED_OPERATION;
            }
        }, UnitType.AUTH);
    }
}
