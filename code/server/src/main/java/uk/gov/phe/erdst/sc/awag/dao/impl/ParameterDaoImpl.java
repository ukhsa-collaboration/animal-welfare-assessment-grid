package uk.gov.phe.erdst.sc.awag.dao.impl;

import javax.ejb.Stateless;

import uk.gov.phe.erdst.sc.awag.dao.DaoErrorMessageProvider;
import uk.gov.phe.erdst.sc.awag.dao.ParameterDao;
import uk.gov.phe.erdst.sc.awag.dao.impl.utils.DaoConstants;
import uk.gov.phe.erdst.sc.awag.dao.impl.utils.DaoUtils;
import uk.gov.phe.erdst.sc.awag.datamodel.Parameter;

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
            public String getNoSuchEntityMessage(Long id)
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
}
