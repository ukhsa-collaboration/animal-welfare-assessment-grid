package uk.gov.phe.erdst.sc.awag.dao.impl;

import javax.ejb.Stateless;

import uk.gov.phe.erdst.sc.awag.dao.DaoErrorMessageProvider;
import uk.gov.phe.erdst.sc.awag.dao.UserDao;
import uk.gov.phe.erdst.sc.awag.dao.impl.utils.DaoConstants;
import uk.gov.phe.erdst.sc.awag.dao.impl.utils.DaoUtils;
import uk.gov.phe.erdst.sc.awag.datamodel.User;

@Stateless
public class UserDaoImpl extends CommonDaoImpl<User> implements UserDao
{
    public UserDaoImpl()
    {
        super("mId", "mName", new DaoErrorMessageProvider() {

            @Override
            public String getNonUniqueEntityErrorMessage(Object entity)
            {
                return DaoUtils.getUniqueConstraintViolationMessage(DaoConstants.PROP_USER_NAME,
                    ((User) entity).getName());
            }

            @Override
            public String getNoSuchEntityMessage(Long id)
            {
                return DaoUtils.getNoSuchEntityMessage(DaoConstants.PROP_USER_ID, id);
            }

            @Override
            public String getNoSuchEntityMessage(String nameValue)
            {
                return DaoUtils.getNoSuchEntityMessage(DaoConstants.PROP_USER_NAME, nameValue);
            }
        });
    }
}
