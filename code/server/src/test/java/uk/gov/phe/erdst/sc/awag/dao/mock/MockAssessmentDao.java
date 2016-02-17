package uk.gov.phe.erdst.sc.awag.dao.mock;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import uk.gov.phe.erdst.sc.awag.dao.AssessmentDao;
import uk.gov.phe.erdst.sc.awag.datamodel.Assessment;
import uk.gov.phe.erdst.sc.awag.datamodel.AssessmentScore;
import uk.gov.phe.erdst.sc.awag.utils.Mock;

@Mock
// FIXME: check if used.
public class MockAssessmentDao implements AssessmentDao
{
    private List<Assessment> mStorage = new ArrayList<Assessment>();

    @Override
    public Collection<Assessment> getAssessments(Integer offset, Integer limit)
    {
        return mStorage;
    }

    @Override
    public Assessment getAssessment(Long id)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void store(Assessment assessment)
    {
        mStorage.add(assessment);
    }

    @Override
    public void deleteAssessment(Assessment assessment)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public Assessment getPreviousAssessment(long animalId)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Long getCountAssessments()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Long getCountAnimalAssessmentsBetween(String dateFrom, String dateTo, Long animalId)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Collection<Assessment> getAssessments(Long animalId, String dateFrom, String dateTo, Long userId,
        Long reasonId, Long studyId, Boolean isComplete, Integer offset, Integer limit)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Assessment getPreviousAssessmentByDate(Long animalId, String date, Long currentAssessmentId)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void update(Assessment assessment)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void deleteAssessmentScore(AssessmentScore score)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public Collection<AssessmentScore> getAssessmentScores()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getCountAnimalAssessments(Long animalId)
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Long getAssessmentsCount(Long animalId, String dateFrom, String dateTo, Long userId, Long reasonId,
        Long studyId, Boolean isComplete)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Long getCountAssessmentsByTemplateId(Long templateId)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Assessment> getAnimalAssessmentsBetween(String dateFrom, String dateTo, Long animalId,
        boolean isComplete, Integer offset, Integer limit)
    {
        // TODO Auto-generated method stub
        return null;
    }

}
