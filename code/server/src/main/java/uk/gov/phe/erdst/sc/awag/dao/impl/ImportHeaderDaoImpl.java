package uk.gov.phe.erdst.sc.awag.dao.impl;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uk.gov.phe.erdst.sc.awag.dao.ImportHeaderDao;
import uk.gov.phe.erdst.sc.awag.datamodel.ImportHeader;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNonUniqueException;
import uk.gov.phe.erdst.sc.awag.utils.Constants;

@Stateless
public class ImportHeaderDaoImpl implements ImportHeaderDao
{
    private static final Logger LOGGER = LogManager.getLogger(SourceDaoImpl.class.getName());
    @PersistenceContext(unitName = Constants.PERSISTENCE_CONTEXT_DEFAULT_UNIT_NAME)
    private EntityManager mEntityManager;

    @Override
    public ImportHeader store(ImportHeader importHeader) throws AWNonUniqueException
    {
        ImportHeader newImportHeader;
        try
        {
            newImportHeader = mEntityManager.merge(importHeader);
            mEntityManager.flush();
        }
        catch (PersistenceException ex)
        {
            LOGGER.error(ex.getMessage()); // TODO
            throw ex;
        }

        return newImportHeader;

    }

    @Override
    public void realDelete(Long importHeaderId)
    {
        ImportHeader importHeader = mEntityManager.find(ImportHeader.class, importHeaderId);
        if (importHeader == null)
        {
            // TODO record doesn't exist, doesn't matter, does need to be logged!
            // throw handleNoSuchAnimalResult(importHeaderId);

            return;
        }

        mEntityManager.remove(importHeader);
    }

}
