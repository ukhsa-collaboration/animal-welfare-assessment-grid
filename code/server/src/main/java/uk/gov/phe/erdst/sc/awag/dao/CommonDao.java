package uk.gov.phe.erdst.sc.awag.dao;

import java.util.Collection;
import java.util.List;

import uk.gov.phe.erdst.sc.awag.exceptions.AWNoSuchEntityException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNonUniqueException;

public interface CommonDao<T>
{
    T store(T entity) throws AWNonUniqueException;

    T update(T entity) throws AWNonUniqueException;

    Collection<T> getEntities(Integer offset, Integer limit);

    Collection<T> getEntitiesByIds(Long... ids);

    Long getEntityCount();

    T getEntityById(Object id) throws AWNoSuchEntityException;

    List<T> getEntitiesLike(String likeParam, Integer offset, Integer limit);

    Long getEntityCountLike(String likeParam);

    void deleteEntityById(Long id) throws AWNoSuchEntityException;

    void deleteEntityByNameField(String name) throws AWNoSuchEntityException;

    T getEntityByNameField(String name) throws AWNoSuchEntityException;

    T getReference(Long id);

    void removeEntity(T entity);
}
