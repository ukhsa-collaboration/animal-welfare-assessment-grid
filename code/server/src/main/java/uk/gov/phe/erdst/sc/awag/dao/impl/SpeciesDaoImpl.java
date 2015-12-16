package uk.gov.phe.erdst.sc.awag.dao.impl;

import java.util.Collection;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uk.gov.phe.erdst.sc.awag.dao.SpeciesDao;
import uk.gov.phe.erdst.sc.awag.dao.impl.utils.DaoConstants;
import uk.gov.phe.erdst.sc.awag.dao.impl.utils.DaoUtils;
import uk.gov.phe.erdst.sc.awag.datamodel.Species;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNoSuchEntityException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNonUniqueException;
import uk.gov.phe.erdst.sc.awag.utils.Constants;

@Stateless
public class SpeciesDaoImpl implements SpeciesDao
{
    private static final Logger LOGGER = LogManager.getLogger(SpeciesDaoImpl.class.getName());
    @PersistenceContext(unitName = Constants.PERSISTENCE_CONTEXT_DEFAULT_UNIT_NAME)
    private EntityManager mEntityManager;

    @SuppressWarnings("unchecked")
    @Override
    public Collection<Species> getSpecies(Integer offset, Integer limit)
    {
        Query getSpeciesQuery = mEntityManager.createNamedQuery(Species.Q_FIND_ALL_NON_DELETED, Species.class);
        DaoUtils.setOffset(getSpeciesQuery, offset);
        DaoUtils.setLimit(getSpeciesQuery, limit);
        return getSpeciesQuery.getResultList();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Species> getSpeciesLike(String like, Integer offset, Integer limit)
    {
        Query getSpeciesQuery = mEntityManager
            .createNamedQuery(Species.Q_FIND_ALL_NON_DELETED_LIKE_ORDERED, Species.class)
            .setParameter("like", DaoUtils.getLikeLowerCase(like));
        DaoUtils.setOffset(getSpeciesQuery, offset);
        DaoUtils.setLimit(getSpeciesQuery, limit);
        return getSpeciesQuery.getResultList();
    }

    @Override
    public Species store(Species species) throws AWNonUniqueException
    {
        Species newSpecies;
        try
        {
            newSpecies = mEntityManager.merge(species);
            mEntityManager.flush();
        }
        catch (PersistenceException ex)
        {
            if (DaoUtils.isUniqueConstraintViolation(ex))
            {
                String errMsg = getNonUniqueSpeciesMessage(species);
                LOGGER.error(errMsg);
                throw new AWNonUniqueException(errMsg);
            }
            else
            {
                LOGGER.error(ex.getMessage());
                throw ex;
            }
        }
        return newSpecies;
    }

    @Override
    public void updateIsDeleted(Long speciesId, boolean isDeleted) throws AWNoSuchEntityException
    {
        Species species = mEntityManager.find(Species.class, speciesId);
        if (species == null)
        {
            String errMsg = getNoSuchSpeciesMessage(speciesId);
            LOGGER.error(errMsg);
            throw new AWNoSuchEntityException(errMsg);
        }
        species.setIsDeleted(isDeleted);
    }

    @Override
    public void realDelete(Long id)
    {
        mEntityManager.createNamedQuery(Species.Q_DELETE_SPECIES_BY_ID).setParameter("id", id).executeUpdate();
    }

    @Override
    public Species getSpecies(Long id) throws AWNoSuchEntityException
    {
        Species species = mEntityManager.find(Species.class, id);
        if (species == null)
        {
            String errMsg = getNoSuchSpeciesMessage(id);
            LOGGER.error(errMsg);
            throw new AWNoSuchEntityException(errMsg);
        }
        return species;
    }

    private static String getNoSuchSpeciesMessage(Long speciesId)
    {
        return DaoUtils.getNoSuchEntityMessage(Species.class.getName(), speciesId);
    }

    private static String getNonUniqueSpeciesMessage(Species species)
    {
        return DaoUtils.getUniqueConstraintViolationMessage(DaoConstants.PROP_SPECIES_NAME, species.getName());
    }

    @Override
    public Long getCountSpecies()
    {
        return mEntityManager.createNamedQuery(Species.Q_FIND_COUNT_ALL_NON_DELETED, Long.class).getResultList().get(0);
    }

    @Override
    public Long getCountSpeciesLike(String like)
    {
        return mEntityManager.createNamedQuery(Species.Q_FIND_COUNT_ALL_NON_DELETED_LIKE_ORDERED, Long.class)
            .setParameter("like", DaoUtils.getLikeLowerCase(like)).getResultList().get(0);
    }
}
