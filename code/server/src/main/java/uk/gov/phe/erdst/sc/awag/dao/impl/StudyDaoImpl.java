package uk.gov.phe.erdst.sc.awag.dao.impl;

import java.util.List;

import javax.ejb.Stateless;

import uk.gov.phe.erdst.sc.awag.dao.DaoErrorMessageProvider;
import uk.gov.phe.erdst.sc.awag.dao.StudyDao;
import uk.gov.phe.erdst.sc.awag.dao.impl.utils.DaoConstants;
import uk.gov.phe.erdst.sc.awag.dao.impl.utils.DaoUtils;
import uk.gov.phe.erdst.sc.awag.datamodel.Animal;
import uk.gov.phe.erdst.sc.awag.datamodel.Study;
import uk.gov.phe.erdst.sc.awag.exceptions.AWMultipleResultException;

@Stateless
public class StudyDaoImpl extends CommonDaoImpl<Study> implements StudyDao
{
    public StudyDaoImpl()
    {
        super("mId", "mStudyNumber", new DaoErrorMessageProvider() {
            @Override
            public String getNonUniqueEntityErrorMessage(Object entity)
            {
                return DaoUtils.getUniqueConstraintViolationMessage(DaoConstants.PROP_STUDY_NAME,
                    ((Study) entity).getStudyNumber());
            }

            @Override
            public String getNoSuchEntityMessage(Long id)
            {
                return DaoUtils.getNoSuchEntityMessage(DaoConstants.PROP_STUDY_ID, id);
            }

            @Override
            public String getNoSuchEntityMessage(String nameValue)
            {
                return DaoUtils.getNoSuchEntityMessage(DaoConstants.PROP_STUDY_NAME, nameValue);
            }
        });
    }

    @Override
    public Study getStudyWithAnimal(Animal animal) throws AWMultipleResultException
    {
        List<Study> result = mEntityManager.createNamedQuery(Study.Q_FIND_STUDY_WITH_ANIMAL, Study.class)
            .setParameter("animal", animal).getResultList();

        if (result.isEmpty())
        {
            return null;
        }
        else if (result.size() > 1)
        {
            throw new AWMultipleResultException("More than one study found for animal (id, name): (" + animal.getId()
                + ", " + animal.getAnimalNumber() + ")");
        }

        return result.get(0);
    }
}
