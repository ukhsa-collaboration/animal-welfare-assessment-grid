package uk.gov.phe.erdst.sc.awag.dao.impl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;

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
import uk.gov.phe.erdst.sc.awag.datamodel.StudyGroup;
import uk.gov.phe.erdst.sc.awag.datamodel.User;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNoSuchEntityException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNonUniqueException;
import uk.gov.phe.erdst.sc.awag.utils.Constants;
import uk.gov.phe.erdst.sc.awag.utils.DataRetrievalUtils;

@Stateless
public class AssessmentDaoImpl implements AssessmentDao
{
    private static final Logger LOGGER = LogManager.getLogger(AssessmentDaoImpl.class.getName());
    private static final String ENTITY_COMMON_IDS_FIELD = "ids";

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
        query.setParameter(Assessment.Q_ANIMAL_ID_PARAM_NAME, animalId);
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
    public Long getAssessmentsByCompleteness(boolean isComplete)
    {
        return getAssessmentsCount(null, null, null, null, null, null, isComplete);
    }

    @Override
    public Collection<Assessment> getAssessmentsByIds(Long... ids)
    {
        // CS:OFF: LineLength
        MessageFormat messageFormat = new MessageFormat(
            "SELECT o FROM {0} o WHERE o.{1} IN :{2} ORDER BY o.mDate DESC");
        // CS:ON
        String queryString = messageFormat.format(new String[] {"Assessment", "mId", ENTITY_COMMON_IDS_FIELD});
        return mEntityManager.createQuery(queryString, Assessment.class)
            .setParameter(ENTITY_COMMON_IDS_FIELD, DaoUtils.formatIdsForInClause(ids)).getResultList();
    }

    @Override
    public Long getCountAnimalAssessmentsBetween(String dateFrom, String dateTo, Long animalId, boolean isComplete)
    {
        Query getCountQuery = mEntityManager.createNamedQuery(Assessment.Q_COUNT_ANIMAL_ASSESSMENT_BETWEEN, Long.class)
            .setParameter(DaoConstants.QUERY_PARAM_ANIMAL_ID, animalId)
            .setParameter(DaoConstants.QUERY_PARAM_IS_COMPLETE, isComplete)
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

    @Override
    public Collection<Assessment> getAssessments(Long studyId, Long studyGroupId, Long animalId, String dateFrom,
        String dateTo, Long userId, Long reasonId)
    {
        if (studyGroupId == null || (studyId != null && animalId != null))
        {
            return getAssessments(animalId, dateFrom, dateTo, userId, reasonId, studyId, Boolean.TRUE, null, null);
        }

        Metamodel metamodel = mEntityManager.getMetamodel();
        EntityType<StudyGroup> studyGroupClass = metamodel.entity(StudyGroup.class);
        EntityType<Assessment> assessmentClass = metamodel.entity(Assessment.class);

        CriteriaBuilder builder = mEntityManager.getCriteriaBuilder();

        CriteriaQuery<Assessment> criteriaQuery = builder.createQuery(Assessment.class);
        Root<Assessment> assessment = criteriaQuery.from(Assessment.class);

        Subquery<StudyGroup> sq = criteriaQuery.subquery(StudyGroup.class);
        Root<StudyGroup> group = sq.from(StudyGroup.class);
        sq.select(group);

        Expression<Set<Animal>> groupAnimals = group
            .get(studyGroupClass.getDeclaredSet(StudyGroup.F_ANIMALS_FIELD_NAME, Animal.class));

        Predicate groupRestriction = builder.equal(group.get(studyGroupClass.getId(Long.class)), studyGroupId);

        Predicate animalsIsMemberRestriction = builder.isMember(
            assessment.get(assessmentClass.getSingularAttribute(Assessment.F_ANIMAL_FIELD_NAME, Animal.class)),
            groupAnimals);

        // Study + study group + animal + [restriction]* should be handled by if clause at the top
        final Long animalPredicate = null;

        Collection<Predicate> restrictions = getSelectedPredicatesForAssessment(animalPredicate, dateFrom, dateTo,
            userId, reasonId, studyId, Boolean.TRUE, builder, assessment, assessmentClass);

        restrictions.add(animalsIsMemberRestriction);
        restrictions.add(groupRestriction);

        criteriaQuery.where(builder.and(restrictions.toArray(new Predicate[] {})));

        TypedQuery<Assessment> query = mEntityManager.createQuery(criteriaQuery);
        Collection<Assessment> assessments = query.getResultList();

        return assessments;
    }

    @Override
    public Assessment getPreviousAssessmentByDate(Long animalId, String date, Long currentAssessmentId)
    {
        Metamodel metamodel = mEntityManager.getMetamodel();
        EntityType<Assessment> assessmentClass = metamodel.entity(Assessment.class);

        CriteriaBuilder builder = mEntityManager.getCriteriaBuilder();
        CriteriaQuery<Assessment> criteria = builder.createQuery(Assessment.class);
        Root<Assessment> root = criteria.from(Assessment.class);

        Predicate animalRestriction = builder.equal(root.get(Assessment.F_ANIMAL_FIELD_NAME), getAnimal(animalId));

        Predicate dateRestriction = builder.lessThanOrEqualTo(
            root.get(assessmentClass.getSingularAttribute(Assessment.F_DATE_FIELD_NAME, String.class)), date);

        Predicate idRestriction = builder
            .not(builder.equal(root.get(assessmentClass.getId(Long.class)), currentAssessmentId));

        criteria.where(builder.and(animalRestriction, dateRestriction, idRestriction)).orderBy(
            builder.desc(root.get(assessmentClass.getSingularAttribute(Assessment.F_DATE_FIELD_NAME, String.class))));

        TypedQuery<Assessment> query = mEntityManager.createQuery(criteria);
        query.setMaxResults(1);

        List<Assessment> result = query.getResultList();
        return DataRetrievalUtils.getEntityFromListResult(result);
    }

    @Override
    public Collection<Assessment> getAssessments(Long animalId, String dateFrom, String dateTo, Long userId,
        Long reasonId, Long studyId, Boolean isComplete, Integer offset, Integer limit)
    {
        Metamodel metamodel = mEntityManager.getMetamodel();
        EntityType<Assessment> assessmentClass = metamodel.entity(Assessment.class);

        CriteriaBuilder builder = mEntityManager.getCriteriaBuilder();
        CriteriaQuery<Assessment> criteria = builder.createQuery(Assessment.class);
        Root<Assessment> root = criteria.from(Assessment.class);
        Collection<Predicate> restrictions = new ArrayList<>();

        setSearchCriteria(animalId, dateFrom, dateTo, userId, reasonId, studyId, isComplete, builder, criteria, root,
            restrictions, assessmentClass);

        criteria.orderBy(
            builder.desc(root.get(assessmentClass.getSingularAttribute(Assessment.F_DATE_FIELD_NAME, String.class))));

        TypedQuery<Assessment> query = mEntityManager.createQuery(criteria);
        DaoUtils.setOffset(query, offset);
        DaoUtils.setLimit(query, limit);

        return query.getResultList();
    }

    @Override
    public Long getAssessmentsCount(Long animalId, String dateFrom, String dateTo, Long userId, Long reasonId,
        Long studyId, Boolean isComplete)
    {
        Metamodel metamodel = mEntityManager.getMetamodel();
        EntityType<Assessment> assessmentClass = metamodel.entity(Assessment.class);

        CriteriaBuilder builder = mEntityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        Root<Assessment> root = criteria.from(Assessment.class);
        Collection<Predicate> restrictions = new ArrayList<>();

        criteria.select(builder.count(root));

        setSearchCriteria(animalId, dateFrom, dateTo, userId, reasonId, studyId, isComplete, builder, criteria, root,
            restrictions, assessmentClass);

        TypedQuery<Long> query = mEntityManager.createQuery(criteria);

        Long result = query.getResultList().get(0);

        return result;
    }

    @Override
    public void upload(Collection<Assessment> assessments) throws AWNonUniqueException
    {
        Assessment lastAnimal = null;
        try
        {
            for (Assessment assessment : assessments)
            {
                lastAnimal = assessment;
                mEntityManager.persist(assessment);
            }
            mEntityManager.flush();
        }
        catch (PersistenceException ex)
        {
            if (DaoUtils.isUniqueConstraintViolation(ex))
            {
                String errMsg = getNonUniqueAssessmentMessage(lastAnimal);
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

    private void setSearchCriteria(Long animalId, String dateFrom, String dateTo, Long userId, Long reasonId,
        Long studyId, Boolean isComplete, CriteriaBuilder builder, CriteriaQuery<? extends Object> criteria,
        Root<Assessment> root, Collection<Predicate> restrictions, EntityType<Assessment> assessmentClass)
    {
        Collection<Predicate> selectedPredicates = getSelectedPredicatesForAssessment(animalId, dateFrom, dateTo,
            userId, reasonId, studyId, isComplete, builder, root, assessmentClass);

        restrictions.addAll(selectedPredicates);

        criteria.where(builder.and(restrictions.toArray(new Predicate[] {})));
    }

    private Collection<Predicate> getSelectedPredicatesForAssessment(Long animalId, String dateFrom, String dateTo,
        Long userId, Long reasonId, Long studyId, Boolean isComplete, CriteriaBuilder builder, Root<Assessment> root,
        EntityType<Assessment> assessmentClass)
    {
        Collection<Predicate> predicates = new ArrayList<>();

        if (animalId != null)
        {
            predicates.add(builder.equal(root.get(Assessment.F_ANIMAL_FIELD_NAME), getAnimal(animalId)));
        }

        if (userId != null)
        {
            predicates.add(builder.equal(root.get(Assessment.F_PERFORMED_BY_FIELD_NAME), getUser(userId)));
        }

        if (reasonId != null)
        {
            predicates.add(builder.equal(root.get(Assessment.F_REASON_FIELD_NAME), getReason(reasonId)));
        }

        if (studyId != null)
        {
            predicates.add(builder.equal(root.get(Assessment.F_STUDY_FIELD_NAME), getStudy(studyId)));
        }

        if (isComplete != null)
        {
            predicates.add(builder.equal(root.get(Assessment.F_IS_COMPLETE_FIELD_NAME), isComplete));
        }

        if (dateFrom != null && dateTo != null)
        {
            predicates.add(builder.between(
                root.get(assessmentClass.getSingularAttribute(Assessment.F_DATE_FIELD_NAME, String.class)), dateFrom,
                dateTo));
        }

        if (dateFrom != null && dateTo == null)
        {
            predicates.add(builder.greaterThanOrEqualTo(
                root.get(assessmentClass.getSingularAttribute(Assessment.F_DATE_FIELD_NAME, String.class)), dateFrom));
        }
        if (dateFrom == null && dateTo != null)
        {
            predicates.add(builder.lessThanOrEqualTo(
                root.get(assessmentClass.getSingularAttribute(Assessment.F_DATE_FIELD_NAME, String.class)), dateTo));
        }

        return predicates;
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

    private static String getNonUniqueAssessmentMessage(Assessment assessment)
    {
        return DaoUtils.getUniqueConstraintViolationMessage(DaoConstants.PROP_SOURCE_NAME,
            assessment.getEntitySelectName());
    }

    @Override
    public long getCountAnimalAssessments(Long animalId)
    {
        return mEntityManager.createNamedQuery(Assessment.Q_COUNT_ANIMAL_ASSESSMENTS, Long.class)
            .setParameter(Assessment.Q_ANIMAL_ID_PARAM_NAME, animalId).getResultList().get(0);
    }

    @Override
    public Long getCountAssessmentsByTemplateId(Long templateId)
    {
        return mEntityManager.createNamedQuery(Assessment.Q_COUNT_TEMPLATE_ASSESSMENTS, Long.class)
            .setParameter(Assessment.Q_TEMPLATE_ID_PARAM_NAME, templateId).getResultList().get(0);
    }
}
