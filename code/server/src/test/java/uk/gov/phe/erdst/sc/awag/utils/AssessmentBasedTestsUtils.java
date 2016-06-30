package uk.gov.phe.erdst.sc.awag.utils;

import java.util.Collection;

import uk.gov.phe.erdst.sc.awag.dao.AssessmentDao;
import uk.gov.phe.erdst.sc.awag.datamodel.Assessment;

public final class AssessmentBasedTestsUtils
{

    private AssessmentBasedTestsUtils()
    {
    }

    public static void deleteAllAssessments(AssessmentDao assessmentDao)
    {
        Collection<Assessment> assessments = assessmentDao.getAssessments(null, null);
        for (Assessment a : assessments)
        {
            assessmentDao.deleteAssessment(a);
        }
    }
}
