package uk.gov.phe.erdst.sc.awag.dao.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uk.gov.phe.erdst.sc.awag.dao.AssessmentDao;
import uk.gov.phe.erdst.sc.awag.dao.impl.utils.DaoConstants;
import uk.gov.phe.erdst.sc.awag.dao.impl.utils.DaoUtils;
import uk.gov.phe.erdst.sc.awag.datamodel.Animal;
import uk.gov.phe.erdst.sc.awag.datamodel.Assessment;
import uk.gov.phe.erdst.sc.awag.datamodel.AssessmentReason;
import uk.gov.phe.erdst.sc.awag.datamodel.AssessmentScore;
import uk.gov.phe.erdst.sc.awag.datamodel.Study;
import uk.gov.phe.erdst.sc.awag.datamodel.User;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNoSuchEntityException;
import uk.gov.phe.erdst.sc.awag.utils.Constants;
import uk.gov.phe.erdst.sc.awag.utils.DataRetrievalUtils;

@Stateless
public class AssessmentDaoImpl implements AssessmentDao
{
    private static final Logger LOGGER = LogManager.getLogger(AssessmentDaoImpl.class.getName());

    @PersistenceContext(unitName = Constants.PERSISTENCE_CONTEXT_DEFAULT_UNIT_NAME)
    private EntityManager mEntityManager;

    @SuppressWarnings("unchecked")
    @Override
    public Collection<Assessment> getAssessments(Integer offset, Integer limit)
    {
        Query getAssessmentsQuery = mEntityManager.createNamedQuery(Assessment.Q_FIND_ALL, Assessment.class);
        DaoUtils.setOffset(getAssessmentsQuery, offset);
        DaoUtils.setLimit(getAssessmentsQuery, limit);
        return getAssessmentsQuery.getResultList();
    }

    @Override
    public Assessment getAssessment(Long id) throws AWNoSuchEntityException
    {
        Assessment assessment = mEntityManager.find(Assessment.class, id);

        if (assessment == null)
        {
            String errMsg = getNoSuchAssessmentMessage(id);
            LOGGER.error(errMsg);
            throw new AWNoSuchEntityException(errMsg);
        }

        return assessment;
    }

    @Override
    public void store(Assessment assessment)
    {
        try
        {
            mEntityManager.persist(assessment);
            // Flush is needed to force the PersistenceException in case there's a duplicate
            mEntityManager.flush();
        }
        catch (PersistenceException ex)
        {
            LOGGER.error(ex.getMessage());
            throw ex;
        }
    }

    @Override
    public void update(Assessment assessment)
    {
        try
        {
            mEntityManager.merge(assessment);
            mEntityManager.flush();
        }
        catch (PersistenceException ex)
        {
            LOGGER.error(ex.getMessage());
            throw ex;
        }
    }

    @Override
    public void deleteAssessment(Assessment assessment)
    {
        Assessment mergedAssessment = mEntityManager.merge(assessment);
        mEntityManager.remove(mergedAssessment);
    }

    @Override
    public Assessment getPreviousAssessment(long animalId)
    {
        TypedQuery<Assessment> query = mEntityManager.createNamedQuery(Assessment.Q_FIND_PREV_ANIMAL_ASSESSMENT,
            Assessment.class);
        query.setParameter("animalId", animalId);
        query.setMaxResults(1);

        List<Assessment> result = query.getResultList();
        return DataRetrievalUtils.getEntityFromListResult(result);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Assessment> getAnimalAssessmentsBetween(String dateFrom, String dateTo, Long animalId,
        boolean isComplete, Integer offset, Integer limit)
    {
        Query getAssessmentsQuery = mEntityManager
            .createNamedQuery(Assessment.Q_ANIMAL_ASSESSMENT_BETWEEN, Assessment.class)
            .setParameter(DaoConstants.QUERY_PARAM_ANIMAL_ID, animalId)
            .setParameter(DaoConstants.QUERY_PARAM_DATE_FROM, dateFrom)
            .setParameter(DaoConstants.QUERY_PARAM_DATE_TO, dateTo)
            .setParameter(DaoConstants.QUERY_PARAM_IS_COMPLETE, isComplete);
        DaoUtils.setOffset(getAssessmentsQuery, offset);
        DaoUtils.setLimit(getAssessmentsQuery, limit);
        return getAssessmentsQuery.getResultList();
    }

    @Override
    public Long getCountAssessments()
    {
        return mEntityManager.createNamedQuery(Assessment.Q_COUNT_FIND_ALL, Long.class).getResultList().get(0);
    }

    @Override
    public Long getCountAnimalAssessmentsBetween(String dateFrom, String dateTo, Long animalId)
    {
        Query getCountQuery = mEntityManager.createNamedQuery(Assessment.Q_COUNT_ANIMAL_ASSESSMENT_BETWEEN, Long.class)
            .setParameter(DaoConstants.QUERY_PARAM_ANIMAL_ID, animalId)
            .setParameter(DaoConstants.QUERY_PARAM_DATE_FROM, dateFrom)
            .setParameter(DaoConstants.QUERY_PARAM_DATE_TO, dateTo);
        return (Long) getCountQuery.getResultList().get(0);
    }

    @Override
    public void deleteAssessmentScore(AssessmentScore score)
    {
        AssessmentScore merged = mEntityManager.merge(score);
        mEntityManager.remove(merged);
    }

    @Override
    public Collection<AssessmentScore> getAssessmentScores()
    {
        TypedQuery<AssessmentScore> query = mEntityManager.createNamedQuery(AssessmentScore.Q_FIND_ALL,
            AssessmentScore.class);
        return query.getResultList();
    }

    // TODO: introduce metamodel to avoid using hardcoded fields
    @Override
    public Assessment getPreviousAssessmentByDate(Long animalId, String date, Long currentAssessmentId)
    {
        CriteriaBuilder builder = mEntityManager.getCriteriaBuilder();
        CriteriaQuery<Assessment> criteria = builder.createQuery(Assessment.class);
        Root<Assessment> root = criteria.from(Assessment.class);

        Predicate animalRestriction = builder.equal(root.get("mAnimal"), getAnimal(animalId));
        Predicate dateRestriction = builder.lessThanOrEqualTo(root.<String> get("mDate"), date);
        Predicate idRestriction = builder.not(builder.equal(root.<Long> get("mId"), currentAssessmentId));

        criteria.where(builder.and(animalRestriction, dateRestriction, idRestriction)).orderBy(
            builder.desc(root.get("mId")));

        TypedQuery<Assessment> query = mEntityManager.createQuery(criteria);
        query.setMaxResults(1);

        List<Assessment> result = query.getResultList();
        return DataRetrievalUtils.getEntityFromListResult(result);
    }

    // TODO: introduce metamodel to avoid using hardcoded fields
    @Override
    public Collection<Assessment> getAssessments(Long animalId, String dateFrom, String dateTo, Long userId,
        Long reasonId, Long studyId, Boolean isComplete, Integer offset, Integer limit)
    {
        CriteriaBuilder builder = mEntityManager.getCriteriaBuilder();
        CriteriaQuery<Assessment> criteria = builder.createQuery(Assessment.class);
        Root<Assessment> root = criteria.from(Assessment.class);
        Collection<Predicate> restrictions = new ArrayList<>();

        setSearchCriteria(animalId, dateFrom, dateTo, userId, reasonId, studyId, isComplete, builder, criteria, root,
            restrictions);

        criteria.orderBy(builder.asc(root.get("mId")));

        TypedQuery<Assessment> query = mEntityManager.createQuery(criteria);
        DaoUtils.setOffset(query, offset);
        DaoUtils.setLimit(query, limit);

        return query.getResultList();
    }

    // TODO: introduce metamodel to avoid using hardcoded fields
    @Override
    public Long getAssessmentsCount(Long animalId, String dateFrom, String dateTo, Long userId, Long reasonId,
        Long studyId, Boolean isComplete)
    {
        CriteriaBuilder builder = mEntityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        Root<Assessment> root = criteria.from(Assessment.class);
        Collection<Predicate> restrictions = new ArrayList<>();

        criteria.select(builder.count(root));

        setSearchCriteria(animalId, dateFrom, dateTo, userId, reasonId, studyId, isComplete, builder, criteria, root,
            restrictions);

        TypedQuery<Long> query = mEntityManager.createQuery(criteria);

        Long result = query.getResultList().get(0);

        return result;
    }

    private void setSearchCriteria(Long animalId, String dateFrom, String dateTo, Long userId, Long reasonId,
        Long studyId, Boolean isComplete, CriteriaBuilder builder, CriteriaQuery<? extends Object> criteria,
        Root<Assessment> root, Collection<Predicate> restrictions)
    {
        if (animalId != null)
        {
            restrictions.add(builder.equal(root.get("mAnimal"), getAnimal(animalId)));
        }

        if (userId != null)
        {
            restrictions.add(builder.equal(root.get("mPerformedBy"), getUser(userId)));
        }

        if (reasonId != null)
        {
            restrictions.add(builder.equal(root.get("mReason"), getReason(reasonId)));
        }

        if (studyId != null)
        {
            restrictions.add(builder.equal(root.get("mStudy"), getStudy(studyId)));
        }

        if (isComplete != null)
        {
            restrictions.add(builder.equal(root.get("mIsComplete"), isComplete));
        }

        if (dateFrom != null && dateTo != null)
        {
            restrictions.add(builder.between(root.<String> get("mDate"), dateFrom, dateTo));
        }

        if (dateFrom != null && dateTo == null)
        {
            restrictions.add(builder.greaterThanOrEqualTo(root.<String> get("mDate"), dateFrom));
        }
        if (dateFrom == null && dateTo != null)
        {
            restrictions.add(builder.lessThanOrEqualTo(root.<String> get("mDate"), dateTo));
        }

        criteria.where(builder.and(restrictions.toArray(new Predicate[] {})));
    }

    private static Animal getAnimal(Long id)
    {
        Animal animal = new Animal();
        animal.setId(id);
        return animal;
    }

    private static User getUser(Long id)
    {
        User user = new User();
        user.setId(id);
        return user;
    }

    private static AssessmentReason getReason(Long id)
    {
        AssessmentReason reason = new AssessmentReason();
        reason.setId(id);
        return reason;
    }

    private static Study getStudy(Long id)
    {
        Study study = new Study();
        study.setId(id);
        return study;
    }

    private static String getNoSuchAssessmentMessage(Long id)
    {
        return DaoUtils.getNoSuchEntityMessage(Assessment.class.getName(), id);
    }

    @Override
    public long getCountAnimalAssessments(Long animalId)
    {
        return mEntityManager.createNamedQuery(Assessment.Q_COUNT_ANIMAL_ASSESSMENTS, Long.class)
            .setParameter("animalId", animalId).getResultList().get(0);
    }

    @Override
    public Long getCountAssessmentsByTemplateId(Long templateId)
    {
        return mEntityManager.createNamedQuery(Assessment.Q_COUNT_TEMPLATE_ASSESSMENTS, Long.class)
            .setParameter("templateId", templateId).getResultList().get(0);
    }
}
