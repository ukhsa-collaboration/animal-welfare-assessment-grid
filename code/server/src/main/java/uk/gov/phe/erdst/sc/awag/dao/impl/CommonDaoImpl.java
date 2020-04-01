package uk.gov.phe.erdst.sc.awag.dao.impl;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uk.gov.phe.erdst.sc.awag.dao.CommonDao;
import uk.gov.phe.erdst.sc.awag.dao.DaoErrorMessageProvider;
import uk.gov.phe.erdst.sc.awag.dao.impl.utils.DaoUtils;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNoSuchEntityException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNonUniqueException;
import uk.gov.phe.erdst.sc.awag.utils.Constants;
import uk.gov.phe.erdst.sc.awag.utils.DataRetrievalUtils;
import uk.gov.phe.erdst.sc.awag.utils.UnitType;

@Stateless
public abstract class CommonDaoImpl<T> implements CommonDao<T>
{
    private static final Logger LOGGER = LogManager.getLogger(CommonDaoImpl.class);
    private static final String LIKE_SQL = "like";
    private static final String ENTITY_COMMON_NAME_FIELD = "name";
    private static final String ENTITY_COMMON_IDS_FIELD = "ids";

    @PersistenceContext(unitName = Constants.PERSISTENCE_CONTEXT_DEFAULT_UNIT_NAME)
    private EntityManager mDefaultPuEntityManager;
    @PersistenceContext(unitName = Constants.PERSISTENCE_CONTEXT_AUTH_UNIT_NAME)
    private EntityManager mAuthPuEntityManager;

    private final Class<T> type;
    private final String mEntityIdField;
    private final String mEntityNameField;
    private final DaoErrorMessageProvider mErrorMessageProvider;
    private final UnitType mUnitType;

    public CommonDaoImpl()
    {
        this.type = null;
        this.mEntityIdField = null;
        this.mEntityNameField = null;
        this.mErrorMessageProvider = null;
        this.mUnitType = null;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public CommonDaoImpl(String entityIdField, String entityNameField, DaoErrorMessageProvider daoErrorMessageProvider)
    {
        Type t = getClass().getGenericSuperclass();
        ParameterizedType pt = (ParameterizedType) t;
        this.type = (Class) pt.getActualTypeArguments()[0];

        this.mEntityIdField = entityIdField;
        this.mEntityNameField = entityNameField;
        this.mErrorMessageProvider = daoErrorMessageProvider;
        this.mUnitType = UnitType.DEFAULT;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public CommonDaoImpl(String entityIdField, String entityNameField, DaoErrorMessageProvider daoErrorMessageProvider,
        UnitType unitType)
    {
        Type t = getClass().getGenericSuperclass();
        ParameterizedType pt = (ParameterizedType) t;
        type = (Class) pt.getActualTypeArguments()[0];

        this.mEntityIdField = entityIdField;
        this.mEntityNameField = entityNameField;
        this.mErrorMessageProvider = daoErrorMessageProvider;
        this.mUnitType = unitType;
    }

    protected EntityManager getEntityManager()
    {
        switch (mUnitType)
        {
            case DEFAULT:
                return mDefaultPuEntityManager;
            case AUTH:
                return mAuthPuEntityManager;
            default:
                return null;
        }
    }

    protected Logger getLogger()
    {
        return LOGGER;
    }

    protected DaoErrorMessageProvider getMessageProvider()
    {
        return mErrorMessageProvider;
    }

    @Override
    public T store(T entity) throws AWNonUniqueException
    {
        try
        {
            getEntityManager().persist(entity);
            // Flush is needed to force the PersistenceException in case there's a duplicate
            getEntityManager().flush();
        }
        catch (PersistenceException ex)
        {
            if (DaoUtils.isUniqueConstraintViolation(ex))
            {
                String errMsg = mErrorMessageProvider.getNonUniqueEntityErrorMessage(entity);
                LOGGER.error(errMsg);
                throw new AWNonUniqueException(errMsg);
            }
            else
            {
                LOGGER.error(ex.getMessage());
                throw ex;
            }
        }

        return entity;
    }

    @Override
    public T update(T entity) throws AWNonUniqueException
    {
        try
        {
            T merged = getEntityManager().merge(entity);
            // Flush is needed to force the PersistenceException in case there's a duplicate
            getEntityManager().flush();

            return merged;
        }
        catch (PersistenceException ex)
        {
            if (DaoUtils.isUniqueConstraintViolation(ex))
            {
                String errMsg = mErrorMessageProvider.getNonUniqueEntityErrorMessage(entity);
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

    @Override
    public Collection<T> getEntities(Integer offset, Integer limit)
    {
        final StringBuffer queryString = new StringBuffer("SELECT o FROM ");
        queryString.append(type.getSimpleName()).append(" o ");
        final TypedQuery<T> query = getEntityManager().createQuery(queryString.toString(), type);
        DaoUtils.setOffset(query, offset);
        DaoUtils.setLimit(query, limit);
        return query.getResultList();
    }

    @Override
    public Collection<T> getEntitiesByIds(Long... ids)
    {
        MessageFormat messageFormat = new MessageFormat("SELECT o FROM {0} o WHERE o.{1} IN :{2}");
        String queryString = messageFormat
            .format(new String[] {type.getSimpleName(), mEntityIdField, ENTITY_COMMON_IDS_FIELD});
        return getEntityManager().createQuery(queryString, type)
            .setParameter(ENTITY_COMMON_IDS_FIELD, DaoUtils.formatIdsForInClause(ids)).getResultList();
    }

    @Override
    public Long getEntityCount()
    {
        final StringBuffer queryString = new StringBuffer("SELECT count(o) FROM ");
        queryString.append(type.getSimpleName()).append(" o ");
        final TypedQuery<T> query = getEntityManager().createQuery(queryString.toString(), type);
        return (Long) query.getResultList().get(0);
    }

    @Override
    public T getReference(Long id)
    {
        return getEntityManager().getReference(type, id);
    }

    @Override
    public T getEntityById(Object id) throws AWNoSuchEntityException
    {
        T entity = getEntityManager().find(type, id);

        if (entity == null)
        {
            if (id instanceof Long)
            {
                String errorMsg = mErrorMessageProvider.getNoSuchEntityMessage(id);
                handleNoSuchEntityException(errorMsg);
            }
        }

        getEntityManager().refresh(entity);

        return entity;
    }

    @Override
    public T getEntityByNameField(String name) throws AWNoSuchEntityException
    {
        MessageFormat messageFormat = new MessageFormat("SELECT o FROM {0} o WHERE o.{1} = :{2}");
        String queryString = messageFormat
            .format(new String[] {type.getSimpleName(), mEntityNameField, ENTITY_COMMON_NAME_FIELD});

        List<T> entityResultList = getEntityManager().createQuery(queryString, type)
            .setParameter(ENTITY_COMMON_NAME_FIELD, name).getResultList();

        T entity = DataRetrievalUtils.getEntityFromListResult(entityResultList);

        if (entity == null)
        {
            String errorMsg = mErrorMessageProvider.getNoSuchEntityMessage(name);
            handleNoSuchEntityException(errorMsg);
        }

        return entity;
    }

    @Override
    public void deleteEntityByNameField(String name) throws AWNoSuchEntityException
    {
        Object entity = getEntityByNameField(name);
        getEntityManager().remove(entity);
    }

    @Override
    public void deleteEntityById(Object id) throws AWNoSuchEntityException
    {
        T object = getEntityManager().getReference(type, id);
        if (object == null)
        {
            throw new AWNoSuchEntityException(mErrorMessageProvider.getNoSuchEntityMessage(id));
        }
        getEntityManager().remove(object);
    }

    @Override
    public List<T> getEntitiesLike(String likeParam, Integer offset, Integer limit)
    {
        MessageFormat messageFormat = new MessageFormat(
            "SELECT o FROM {0} o WHERE LOWER(o.{1}) LIKE :like ORDER BY LENGTH(o.{1}) ASC," + " o.{1} ASC");
        String queryString = messageFormat.format(new String[] {type.getSimpleName(), mEntityNameField});

        TypedQuery<T> query = getEntityManager().createQuery(queryString, type).setParameter(LIKE_SQL,
            DaoUtils.getLikeLowerCase(likeParam));
        DaoUtils.setOffset(query, offset);
        DaoUtils.setLimit(query, limit);
        return query.getResultList();
    }

    @Override
    public Long getEntityCountLike(String likeParam)
    {
        MessageFormat messageFormat = new MessageFormat("SELECT COUNT(o) FROM {0} o WHERE LOWER(o.{1}) LIKE :like");
        String queryString = messageFormat.format(new String[] {type.getSimpleName(), mEntityNameField});

        return getEntityManager().createQuery(queryString, Long.class)
            .setParameter(LIKE_SQL, DaoUtils.getLikeLowerCase(likeParam)).getResultList().get(0);
    }

    @Override
    public void removeEntity(T entity)
    {
        getEntityManager().remove(entity);
        getEntityManager().flush();
    }

    private static void handleNoSuchEntityException(String errorMsg) throws AWNoSuchEntityException
    {
        LOGGER.error(errorMsg);
        throw new AWNoSuchEntityException(errorMsg);
    }
}
