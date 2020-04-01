package uk.gov.phe.erdst.sc.awag.dao.impl;

import java.util.Collection;

import javax.ejb.Stateless;
import javax.persistence.PersistenceException;

import uk.gov.phe.erdst.sc.awag.dao.AnimalHousingDao;
import uk.gov.phe.erdst.sc.awag.dao.DaoErrorMessageProvider;
import uk.gov.phe.erdst.sc.awag.dao.impl.utils.DaoConstants;
import uk.gov.phe.erdst.sc.awag.dao.impl.utils.DaoUtils;
import uk.gov.phe.erdst.sc.awag.datamodel.AnimalHousing;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNonUniqueException;

@Stateless
public class AnimalHousingDaoImpl extends CommonDaoImpl<AnimalHousing> implements AnimalHousingDao
{
    public AnimalHousingDaoImpl()
    {
        super("mId", "mName", new DaoErrorMessageProvider() {
            @Override
            public String getNonUniqueEntityErrorMessage(Object entity)
            {
                return DaoUtils.getUniqueConstraintViolationMessage(DaoConstants.PROP_ANIMAL_HOUSING_NAME,
                    ((AnimalHousing) entity).getName());
            }

            @Override
            public String getNoSuchEntityMessage(Object id)
            {
                return DaoUtils.getNoSuchEntityMessage(DaoConstants.PROP_ANIMAL_HOUSING_ID, id);
            }

            @Override
            public String getNoSuchEntityMessage(String nameValue)
            {
                return DaoUtils.getNoSuchEntityMessage(DaoConstants.PROP_ANIMAL_HOUSING_NAME, nameValue);
            }

        });
    }

    @Override
    public void upload(Collection<AnimalHousing> animalHousings) throws AWNonUniqueException
    {
        // TODO persist or merge?
        AnimalHousing lastEntity = null;
        try
        {
            for (AnimalHousing source : animalHousings)
            {
                lastEntity = source;
                getEntityManager().persist(source);
            }
            getEntityManager().flush();
        }
        catch (PersistenceException ex)
        {
            if (DaoUtils.isUniqueConstraintViolation(ex))
            {
                String errMsg = getMessageProvider().getNonUniqueEntityErrorMessage(lastEntity);
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
