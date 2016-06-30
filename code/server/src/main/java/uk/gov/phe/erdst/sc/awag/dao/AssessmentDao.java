package uk.gov.phe.erdst.sc.awag.dao;

import java.util.Collection;
import java.util.List;

import uk.gov.phe.erdst.sc.awag.datamodel.Assessment;
import uk.gov.phe.erdst.sc.awag.datamodel.AssessmentScore;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNoSuchEntityException;

public interface AssessmentDao
{
    Collection<Assessment> getAssessments(Integer offset, Integer limit);

    Long getCountAssessments();

    Assessment getAssessment(Long id) throws AWNoSuchEntityException;

    void store(Assessment assessment);

    void deleteAssessment(Assessment assessment);

    void update(Assessment assessment);

    /**
     * Retrieves the latest assessment for an animal with the given id.
     * @param animalId
     * @return assessment or null if it could not be found.
     */
    Assessment getPreviousAssessment(long animalId);

    List<Assessment> getAnimalAssessmentsBetween(String dateFrom, String dateTo, Long animalId, boolean isComplete,
        Integer offset, Integer limit);

    Long getCountAnimalAssessmentsBetween(String dateFrom, String dateTo, Long animalId);

    Collection<Assessment> getAssessments(Long animalId, String dateFrom, String dateTo, Long userId, Long reasonId,
        Long studyId, Boolean isComplete, Integer offset, Integer limit);

    Collection<Assessment> getAssessments(Long studyId, Long studyGroupId, Long animalId, String dateFrom,
        String dateTo, Long userId, Long reasonId);

    Long getAssessmentsCount(Long animalId, String dateFrom, String dateTo, Long userId, Long reasonId, Long studyId,
        Boolean isComplete);

    /**
     * Retrieves an assessment for an animal with the given id and just before the specified date.
     * @return assessment or null if it could not be found.
     */
    Assessment getPreviousAssessmentByDate(Long animalId, String date, Long currentAssessmentId);

    void deleteAssessmentScore(AssessmentScore score);

    Collection<AssessmentScore> getAssessmentScores();

    long getCountAnimalAssessments(Long animalId);

    Long getCountAssessmentsByTemplateId(Long templateId);

    Long getAssessmentsByCompleteness(boolean isComplete);

    Collection<Assessment> getAssessmentsByIds(Long... ids);
}
