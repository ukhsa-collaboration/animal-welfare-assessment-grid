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

import uk.gov.phe.erdst.sc.awag.dao.SourceDao;
import uk.gov.phe.erdst.sc.awag.dao.impl.utils.DaoConstants;
import uk.gov.phe.erdst.sc.awag.dao.impl.utils.DaoUtils;
import uk.gov.phe.erdst.sc.awag.datamodel.Source;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNoSuchEntityException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNonUniqueException;
import uk.gov.phe.erdst.sc.awag.utils.Constants;

@Stateless
public class SourceDaoImpl implements SourceDao
{
    private static final Logger LOGGER = LogManager.getLogger(SourceDaoImpl.class.getName());
    @PersistenceContext(unitName = Constants.PERSISTENCE_CONTEXT_DEFAULT_UNIT_NAME)
    private EntityManager mEntityManager;

    @SuppressWarnings("unchecked")
    @Override
    public Collection<Source> getSources(Integer offset, Integer limit)
    {
        Query getSources = mEntityManager.createNamedQuery(Source.Q_FIND_ALL, Source.class);
        DaoUtils.setOffset(getSources, offset);
        DaoUtils.setLimit(getSources, limit);
        return getSources.getResultList();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Source> getSourcesLike(String like, Integer offset, Integer limit)
    {
        Query getSources = mEntityManager.createNamedQuery(Source.Q_FIND_ALL_SOURCES_LIKE, Source.class)
            .setMaxResults(DaoConstants.MAX_RESULTS_FOR_LIKE_QUERY)
            .setParameter("like", DaoUtils.getLikeLowerCase(like));
        DaoUtils.setOffset(getSources, offset);
        DaoUtils.setLimit(getSources, limit);
        return getSources.getResultList();
    }

    @Override
    public Source store(Source source) throws AWNonUniqueException
    {
        Source newSource;
        try
        {
            newSource = mEntityManager.merge(source);
            mEntityManager.flush();
        }
        catch (PersistenceException ex)
        {
            if (DaoUtils.isUniqueConstraintViolation(ex))
            {
                String errMsg = getNonUniqueSourceMessage(source);
                LOGGER.error(errMsg);
                throw new AWNonUniqueException(errMsg);
            }
            else
            {
                LOGGER.error(ex.getMessage());
                throw ex;
            }
        }
        return newSource;
    }

    @Override
    public void realDelete(Long id)
    {
        mEntityManager.createNamedQuery(Source.Q_DELETE_SOURCE_BY_ID).setParameter("id", id).executeUpdate();
    }

    private static String getNonUniqueSourceMessage(Source source)
    {
        return DaoUtils.getUniqueConstraintViolationMessage(DaoConstants.PROP_SOURCE_NAME, source.getName());
    }

    @Override
    public Source getSource(Long sourceId) throws AWNoSuchEntityException
    {
        Source source = mEntityManager.find(Source.class, sourceId);
        if (source == null)
        {
            String errMsg = getNoSuchSourceMessage(sourceId);
            LOGGER.error(errMsg);
            throw new AWNoSuchEntityException(errMsg);
        }
        return source;
    }

    private static String getNoSuchSourceMessage(Long sourceId)
    {
        return DaoUtils.getNoSuchEntityMessage(Source.class.getName(), sourceId);
    }

    @Override
    public Long getCountSourcesLike(String like)
    {
        return mEntityManager.createNamedQuery(Source.Q_FIND_COUNT_ALL_SOURCES_LIKE, Long.class)
            .setMaxResults(DaoConstants.MAX_RESULTS_FOR_LIKE_QUERY)
            .setParameter("like", DaoUtils.getLikeLowerCase(like)).getResultList().get(0);
    }

    @Override
    public Long getCountSources()
    {
        return mEntityManager.createNamedQuery(Source.Q_FIND_COUNT_ALL, Long.class).getResultList().get(0);
    }
}
