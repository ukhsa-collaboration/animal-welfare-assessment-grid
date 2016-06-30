package uk.gov.phe.erdst.sc.awag.service.extractor;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import uk.gov.phe.erdst.sc.awag.datamodel.Assessment;
import uk.gov.phe.erdst.sc.awag.datamodel.Study;
import uk.gov.phe.erdst.sc.awag.shared.test.TestConstants;

@Test(groups = {TestConstants.TESTNG_UNIT_TESTS_GROUP})
public class AssessmentUniqueStudiesExtractorTest extends AssessmentUniqueEntitiesExtractorTestTemplate
{
    @Override
    @BeforeMethod
    public void setUp() throws Exception
    {
        super.setUp();
    }

    @Override
    protected Class<?> getTestEntityClass()
    {
        return Study.class;
    }

    @Test
    public void testWithIdenticalStudy()
    {
        super.testWithIdentical();
    }

    @Test
    public void testWithDifferentStudy()
    {
        super.testWithDifferent();
    }

    // CS:OFF: MagicNumber
    @Test(timeOut = 1000L)
    public void testWithNullStudy()
    {
        super.testWithNull();
    }

    // CS:ON

    @Override
    protected Assessment createTestAssessment(Long studyId, String studyNumber)
    {
        Assessment assessment = new Assessment();
        Study study = new Study();
        study.setId(studyId);
        study.setStudyNumber(studyNumber);
        assessment.setStudy(study);
        return assessment;
    }

    @Override
    protected void nullifyTestEntity(Assessment assessment)
    {
        assessment.setStudy(null);
    }
}
