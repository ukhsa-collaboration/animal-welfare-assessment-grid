package uk.gov.phe.erdst.sc.awag.dao.impl;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import uk.gov.phe.erdst.sc.awag.dao.ImportAnimalDao;
import uk.gov.phe.erdst.sc.awag.datamodel.ImportAnimal;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNoSuchEntityException;
import uk.gov.phe.erdst.sc.awag.utils.Constants;

@Stateless
public class ImportAnimalDaoImpl implements ImportAnimalDao
{
    @PersistenceContext(unitName = Constants.PERSISTENCE_CONTEXT_DEFAULT_UNIT_NAME)
    private EntityManager mEntityManager;

    @Override
    public ImportAnimal getImportAnimal(String animalNumber, Long importHeaderId) throws AWNoSuchEntityException
    {
        TypedQuery<ImportAnimal> query = mEntityManager
            .createNamedQuery(ImportAnimal.Q_FIND_IMPORT_ANIMAL_BY_NUMBER, ImportAnimal.class)
            .setParameter("animalNumber", animalNumber).setParameter("importHeaderId", importHeaderId);

        try
        {
            return query.getSingleResult();
        }
        catch (NoResultException e)
        {
            return null; // TODO: what to do?
        }

    }
}
