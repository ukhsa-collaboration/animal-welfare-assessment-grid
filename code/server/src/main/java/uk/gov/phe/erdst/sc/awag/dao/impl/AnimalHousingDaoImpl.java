package uk.gov.phe.erdst.sc.awag.dao.impl;

import javax.ejb.Stateless;

import uk.gov.phe.erdst.sc.awag.dao.AnimalHousingDao;
import uk.gov.phe.erdst.sc.awag.dao.DaoErrorMessageProvider;
import uk.gov.phe.erdst.sc.awag.dao.impl.utils.DaoConstants;
import uk.gov.phe.erdst.sc.awag.dao.impl.utils.DaoUtils;
import uk.gov.phe.erdst.sc.awag.datamodel.AnimalHousing;

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
            public String getNoSuchEntityMessage(Long id)
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
}
