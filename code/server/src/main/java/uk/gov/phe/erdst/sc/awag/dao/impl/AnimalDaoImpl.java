package uk.gov.phe.erdst.sc.awag.dao.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uk.gov.phe.erdst.sc.awag.dao.AnimalDao;
import uk.gov.phe.erdst.sc.awag.dao.impl.utils.DaoConstants;
import uk.gov.phe.erdst.sc.awag.dao.impl.utils.DaoUtils;
import uk.gov.phe.erdst.sc.awag.datamodel.Animal;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNoSuchEntityException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNonUniqueException;
import uk.gov.phe.erdst.sc.awag.utils.Constants;

@Stateless
public class AnimalDaoImpl implements AnimalDao
{
    private static final Logger LOGGER = LogManager.getLogger(AnimalDaoImpl.class.getName());

    @PersistenceContext(unitName = Constants.PERSISTENCE_CONTEXT_DEFAULT_UNIT_NAME)
    private EntityManager mEntityManager;

    @SuppressWarnings("unchecked")
    @Override
    public List<Animal> getNonDeletedAnimals(Integer offset, Integer limit)
    {
        Query getAnimalsQuery = mEntityManager.createNamedQuery(Animal.Q_FIND_ALL_DELETED_OR_NOT_ORDERED, Animal.class)
            .setParameter("isDeleted", false);
        DaoUtils.setOffset(getAnimalsQuery, offset);
        DaoUtils.setLimit(getAnimalsQuery, limit);
        return getAnimalsQuery.getResultList();
    }

    @Override
    public Animal getAnimal(Long animalId) throws AWNoSuchEntityException
    {
        Animal animal = mEntityManager.find(Animal.class, animalId);
        if (animal == null)
        {
            throw handleNoSuchAnimalResult(animalId);
        }
        return animal;
    }

    @Override
    public Animal getAnimal(String animalNumber) throws AWNoSuchEntityException // TODO integration test
    {
        TypedQuery<Animal> query = mEntityManager.createNamedQuery(Animal.Q_FIND_ANIMAL_BY_NUMBER, Animal.class)
            .setParameter("animalNumber", animalNumber);

        try
        {
            return query.getSingleResult();
        }
        catch (NoResultException e)
        {
            throw handleNoSuchAnimalResult(0L); // TODO what to do?
        }
    }

    @Override
    public Animal store(Animal animal) throws AWNonUniqueException
    {
        Animal newAnimal;
        try
        {
            newAnimal = mEntityManager.merge(animal);
            mEntityManager.flush();
        }
        catch (PersistenceException ex)
        {
            if (DaoUtils.isUniqueConstraintViolation(ex))
            {
                String errMsg = getNonUniqueAnimalNoMessage(animal);

                if (LOGGER.isDebugEnabled())
                {
                    LOGGER.debug(errMsg);
                }

                throw new AWNonUniqueException(errMsg);
            }
            else
            {
                LOGGER.error(ex.getMessage());
                throw ex;
            }
        }
        return newAnimal;
    }

    @Override
    public Collection<Animal> getAnimals(List<Long> animalIds)
    {
        Collection<Animal> animals = new ArrayList<Animal>();
        if (animalIds.size() > 0)
        {
            animals = mEntityManager.createNamedQuery(Animal.Q_FIND_IN_IDS, Animal.class)
                .setParameter("idList", animalIds).getResultList();
        }
        return animals;
    }

    @Override
    public void realDelete(Long animalId) throws AWNoSuchEntityException
    {
        Animal animal = mEntityManager.find(Animal.class, animalId);
        if (animal == null)
        {
            throw handleNoSuchAnimalResult(animalId);
        }

        mEntityManager.remove(animal);
    }

    @Override
    public Animal updateIsDeleted(Long animalId, boolean isDeleted) throws AWNoSuchEntityException
    {
        Animal animal = mEntityManager.find(Animal.class, animalId);
        if (animal == null)
        {
            throw handleNoSuchAnimalResult(animalId);
        }
        animal.setIsDeleted(isDeleted);
        return animal;
    }

    @Override
    public void updateIsAlive(Long animalId, boolean isAlive) throws AWNoSuchEntityException
    {
        Animal animal = mEntityManager.find(Animal.class, animalId);
        if (animal == null)
        {
            throw handleNoSuchAnimalResult(animalId);
        }
        animal.setIsAlive(isAlive);
    }

    @Override
    public void updateIsAssessed(Long animalId, boolean isAssessed) throws AWNoSuchEntityException
    {
        Animal animal = mEntityManager.find(Animal.class, animalId);
        if (animal == null)
        {
            throw handleNoSuchAnimalResult(animalId);
        }
        animal.setIsAssessed(isAssessed);
    }

    @Override
    public Collection<Animal> getAnimals()
    {
        return mEntityManager.createNamedQuery(Animal.Q_FIND_ALL, Animal.class).getResultList();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Animal> getNonDeletedAnimalsLike(String like, Integer offset, Integer limit)
    {
        Query getAnimalQuery = mEntityManager.createNamedQuery(Animal.Q_FIND_NON_DELETED_LIKE_ORDERED, Animal.class)
            .setParameter("isDeleted", false).setParameter("like", DaoUtils.getLikeLowerCase(like));

        DaoUtils.setOffset(getAnimalQuery, offset);
        DaoUtils.setLimit(getAnimalQuery, limit);

        return getAnimalQuery.getResultList();
    }

    @Override
    public Long getCountNonDeletedAnimalsLike(String like)
    {
        Query getCount = mEntityManager.createNamedQuery(Animal.Q_FIND_COUNT_NON_DELETED_LIKE_ORDERED, Animal.class)
            .setParameter("isDeleted", false).setParameter("like", DaoUtils.getLikeLowerCase(like));
        return (Long) getCount.getResultList().get(0);
    }

    @Override
    public Animal getNonDeletedAnimalById(Long animalId) throws AWNoSuchEntityException
    {
        TypedQuery<Animal> query = mEntityManager.createNamedQuery(Animal.Q_FIND_NON_DELETED_BY_ID, Animal.class)
            .setParameter("id", animalId);

        try
        {
            return query.getSingleResult();
        }
        catch (NoResultException e)
        {
            throw handleNoSuchAnimalResult(animalId);
        }
    }

    private static String getNoSuchAnimalMessage(Long animalId)
    {
        return DaoUtils.getNoSuchEntityMessage(Animal.class.getName(), animalId);
    }

    private static String getNonUniqueAnimalNoMessage(Animal animal)
    {
        return DaoUtils.getUniqueConstraintViolationMessage(DaoConstants.PROP_ANIMAL_NUMBER, animal.getAnimalNumber());
    }

    private static AWNoSuchEntityException handleNoSuchAnimalResult(Long animalId)
    {
        String errMsg = getNoSuchAnimalMessage(animalId);
        if (LOGGER.isDebugEnabled())
        {
            LOGGER.debug(errMsg);
        }
        return new AWNoSuchEntityException(errMsg);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Animal> getNonDeletedAnimalsLikeSex(String like, String sex, Integer offset, Integer limit)
    {
        Query getAnimalQuery = mEntityManager.createNamedQuery(Animal.Q_FIND_NON_DELETED_LIKE_SEX_ORDERED, Animal.class)
            .setParameter("isDeleted", false).setParameter("sexName", sex)
            .setParameter("like", DaoUtils.getLikeLowerCase(like));

        DaoUtils.setOffset(getAnimalQuery, offset);
        DaoUtils.setLimit(getAnimalQuery, limit);

        return getAnimalQuery.getResultList();
    }

    @Override
    public Long getCountNonDeleteAnimalsLikeSex(String like, String sex)
    {
        Query getCountQuery = mEntityManager
            .createNamedQuery(Animal.Q_FIND_COUNT_NON_DELETED_LIKE_SEX_ORDERED, Long.class)
            .setParameter("isDeleted", false).setParameter("sexName", sex)
            .setParameter("like", DaoUtils.getLikeLowerCase(like));
        return (Long) getCountQuery.getResultList().get(0);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Animal> getDeletedAnimals(Integer offset, Integer limit)
    {
        Query getAnimalsQuery = mEntityManager.createNamedQuery(Animal.Q_FIND_ALL_DELETED_OR_NOT_ORDERED, Animal.class)
            .setParameter("isDeleted", true);
        DaoUtils.setOffset(getAnimalsQuery, offset);
        DaoUtils.setLimit(getAnimalsQuery, limit);
        return getAnimalsQuery.getResultList();
    }

    @Override
    public Long getCountNonDeletedAnimals()
    {
        Query getCountQuery = mEntityManager.createNamedQuery(Animal.Q_FIND_COUNT_ALL_DELETED_OR_NOT, Long.class)
            .setParameter("isDeleted", false);
        return (Long) getCountQuery.getResultList().get(0);
    }

    @Override
    public Long getCountDeletedAnimals()
    {
        Query getCountQuery = mEntityManager.createNamedQuery(Animal.Q_FIND_COUNT_ALL_DELETED_OR_NOT, Long.class)
            .setParameter("isDeleted", true);
        return (Long) getCountQuery.getResultList().get(0);
    }

    @Override
    public void upload(Collection<Animal> animals) throws AWNonUniqueException
    {
        Animal lastAnimal = null;
        try
        {
            for (Animal animal : animals)
            {
                lastAnimal = animal;
                mEntityManager.persist(animal);
            }
            if (!animals.isEmpty())
            {
                mEntityManager.flush();
            }
        }
        catch (PersistenceException ex)
        {
            if (DaoUtils.isUniqueConstraintViolation(ex))
            {
                String errMsg = getNonUniqueAnimalMessage(lastAnimal);
                LOGGER.error(errMsg);
                throw new AWNonUniqueException(errMsg);
            }
            else
            {
                LOGGER.error(ex.getMessage());
                throw ex;
            }
        }
    }

    private static String getNonUniqueAnimalMessage(Animal animal)
    {
        return DaoUtils.getUniqueConstraintViolationMessage(DaoConstants.PROP_SOURCE_NAME, animal.getAnimalNumber());
    }

}
