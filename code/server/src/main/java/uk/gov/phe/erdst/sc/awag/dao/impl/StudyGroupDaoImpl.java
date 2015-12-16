package uk.gov.phe.erdst.sc.awag.dao.impl;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uk.gov.phe.erdst.sc.awag.dao.StudyGroupDao;
import uk.gov.phe.erdst.sc.awag.dao.impl.utils.DaoConstants;
import uk.gov.phe.erdst.sc.awag.dao.impl.utils.DaoUtils;
import uk.gov.phe.erdst.sc.awag.datamodel.StudyGroup;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNoSuchEntityException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNonUniqueException;
import uk.gov.phe.erdst.sc.awag.utils.Constants;

@Stateless
public class StudyGroupDaoImpl implements StudyGroupDao
{
    private static final Logger LOGGER = LogManager.getLogger(StudyGroupDaoImpl.class.getName());
    @PersistenceContext(unitName = Constants.PERSISTENCE_CONTEXT_DEFAULT_UNIT_NAME)
    private EntityManager mEntityManager;

    @Override
    public StudyGroup store(StudyGroup studyGroup) throws AWNonUniqueException
    {
        StudyGroup newStudyGroup = null;

        try
        {
            newStudyGroup = mEntityManager.merge(studyGroup);
            mEntityManager.flush();
        }
        catch (PersistenceException ex)
        {
            if (DaoUtils.isUniqueConstraintViolation(ex))
            {
                String errMsg = getNonUniqueStudyGroupNameMessage(studyGroup);
                LOGGER.error(errMsg);
                throw new AWNonUniqueException(errMsg);
            }
            else
            {
                LOGGER.error(ex.getMessage());
                throw ex;
            }
        }
        return newStudyGroup;
    }

    private String getNonUniqueStudyGroupNameMessage(StudyGroup studyGroup)
    {
        return DaoUtils.getUniqueConstraintViolationMessage(DaoConstants.PROP_STUDY_GROUP_NAME,
            studyGroup.getStudyGroupNumber());
    }

    @Override
    public void deleteStudyGroupById(Long id) throws AWNoSuchEntityException
    {
        StudyGroup studyGroup = null;

        studyGroup = mEntityManager.find(StudyGroup.class, id);

        if (studyGroup == null)
        {
            String errMsg = getNoSuchStudyGroupMessage(id);
            LOGGER.error(errMsg);
            throw new AWNoSuchEntityException(errMsg);
        }

        mEntityManager.remove(studyGroup);
    }

    @Override
    public StudyGroup getStudyGroup(Long id) throws AWNoSuchEntityException
    {
        StudyGroup studyGroup = null;

        studyGroup = mEntityManager.find(StudyGroup.class, id);

        if (studyGroup == null)
        {
            String errMsg = getNoSuchStudyGroupMessage(id);
            LOGGER.error(errMsg);
            throw new AWNoSuchEntityException(errMsg);
        }

        return studyGroup;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Set<StudyGroup> getStudyGroupsLike(String like, Integer offset, Integer limit)
    {
        Query getStudyGroupsQuery = mEntityManager
            .createNamedQuery(StudyGroup.Q_FIND_ALL_STUDY_GROUPS_LIKE, StudyGroup.class)
            .setParameter("like", DaoUtils.getLikeLowerCase(like));
        DaoUtils.setOffset(getStudyGroupsQuery, offset);
        DaoUtils.setLimit(getStudyGroupsQuery, limit);
        return new LinkedHashSet<StudyGroup>(getStudyGroupsQuery.getResultList());
    }

    private static String getNoSuchStudyGroupMessage(Long studyGroupId)
    {
        return DaoUtils.getNoSuchEntityMessage(StudyGroup.class.getName(), studyGroupId);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Set<StudyGroup> getStudyGroups(Integer offset, Integer limit)
    {
        Query getStudyGroupsQuery = mEntityManager.createNamedQuery(StudyGroup.Q_FIND_ALL, StudyGroup.class);
        DaoUtils.setOffset(getStudyGroupsQuery, offset);
        DaoUtils.setLimit(getStudyGroupsQuery, limit);
        return new HashSet<StudyGroup>(getStudyGroupsQuery.getResultList());
    }

    @Override
    public Long getCountStudyGroupsLike(String like)
    {
        Query getCountStudyGroupsLikeQuery = mEntityManager.createNamedQuery(StudyGroup.Q_COUNT_ALL_STUDY_GROUPS_LIKE)
            .setParameter("like", DaoUtils.getLikeLowerCase(like));
        return (Long) getCountStudyGroupsLikeQuery.getResultList().get(0);
    }

    @Override
    public Long getCountStudyGroups()
    {
        return mEntityManager.createNamedQuery(StudyGroup.Q_FIND_COUNT_ALL, Long.class).getResultList().get(0);
    }
}
