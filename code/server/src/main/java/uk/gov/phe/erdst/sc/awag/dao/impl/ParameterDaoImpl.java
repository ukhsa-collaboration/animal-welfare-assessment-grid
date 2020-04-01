package uk.gov.phe.erdst.sc.awag.dao.impl;

import java.util.Collection;

import javax.ejb.Stateless;
import javax.persistence.PersistenceException;

import uk.gov.phe.erdst.sc.awag.dao.DaoErrorMessageProvider;
import uk.gov.phe.erdst.sc.awag.dao.ParameterDao;
import uk.gov.phe.erdst.sc.awag.dao.impl.utils.DaoConstants;
import uk.gov.phe.erdst.sc.awag.dao.impl.utils.DaoUtils;
import uk.gov.phe.erdst.sc.awag.datamodel.Parameter;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNonUniqueException;

@Stateless
public class ParameterDaoImpl extends CommonDaoImpl<Parameter> implements ParameterDao
{
    public ParameterDaoImpl()
    {
        super("mId", "mName", new DaoErrorMessageProvider() {
            @Override
            public String getNonUniqueEntityErrorMessage(Object entity)
            {
                return DaoUtils.getUniqueConstraintViolationMessage(DaoConstants.PROP_PARAMETER_NAME,
                    ((Parameter) entity).getName());
            }

            @Override
            public String getNoSuchEntityMessage(Object id)
            {
                return DaoUtils.getNoSuchEntityMessage(DaoConstants.PROP_PARAMETER_ID, id);
            }

            @Override
            public String getNoSuchEntityMessage(String nameValue)
            {
                return DaoUtils.getNoSuchEntityMessage(DaoConstants.PROP_PARAMETER_NAME, nameValue);
            }
        });
    }

    @Override
    // TODO check why cannot inherit easily from the Upload..Impl
    public void upload(Collection<Parameter> parameters) throws AWNonUniqueException
    {
        Parameter lastParameter = null;
        try
        {
            for (Parameter parameter : parameters)
            {
                lastParameter = parameter;
                getEntityManager().persist(parameter);
            }
            getEntityManager().flush();
        }
        catch (PersistenceException ex)
        {
            if (DaoUtils.isUniqueConstraintViolation(ex))
            {
                String errMsg = getMessageProvider().getNonUniqueEntityErrorMessage(lastParameter);
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
