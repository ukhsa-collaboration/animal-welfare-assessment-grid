package uk.gov.phe.erdst.sc.awag.dao.impl;

import java.util.Collection;

import javax.ejb.Stateless;
import javax.persistence.PersistenceException;

import uk.gov.phe.erdst.sc.awag.dao.DaoErrorMessageProvider;
import uk.gov.phe.erdst.sc.awag.dao.ScaleDao;
import uk.gov.phe.erdst.sc.awag.dao.impl.utils.DaoConstants;
import uk.gov.phe.erdst.sc.awag.dao.impl.utils.DaoUtils;
import uk.gov.phe.erdst.sc.awag.datamodel.Scale;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNonUniqueException;

@Stateless
public class ScaleDaoImpl extends CommonDaoImpl<Scale> implements ScaleDao
{
    public ScaleDaoImpl()
    {
        super("mId", "mName", new DaoErrorMessageProvider() {
            @Override
            public String getNonUniqueEntityErrorMessage(Object entity)
            {
                return DaoUtils.getUniqueConstraintViolationMessage(DaoConstants.PROP_SCALE_NAME,
                    ((Scale) entity).getName());
            }

            @Override
            public String getNoSuchEntityMessage(Object id)
            {
                return DaoUtils.getNoSuchEntityMessage(DaoConstants.PROP_SCALE_ID, id);
            }

            @Override
            public String getNoSuchEntityMessage(String nameValue)
            {
                return DaoUtils.getNoSuchEntityMessage(DaoConstants.PROP_SCALE_NAME, nameValue);
            }
        });
    }

    @Override
    // TODO check why cannot inherit easily from the Upload..Impl
    public void upload(Collection<Scale> factors) throws AWNonUniqueException
    {
        Scale lastFactor = null;
        try
        {
            for (Scale factor : factors)
            {
                lastFactor = factor;
                getEntityManager().persist(factor);
            }
            getEntityManager().flush();
        }
        catch (PersistenceException ex)
        {
            if (DaoUtils.isUniqueConstraintViolation(ex))
            {
                String errMsg = getMessageProvider().getNonUniqueEntityErrorMessage(lastFactor);
                getLogger().error(errMsg);
                throw new AWNonUniqueException(errMsg);
            }
            else
            {
                getLogger().error(ex.getMessage());
                throw ex;
            }
        }
    }
}
